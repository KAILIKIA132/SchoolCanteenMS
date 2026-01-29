#requires -version 5.1
<#
.SYNOPSIS
    Updates an EXISTING Push Demo installation with recent changes.

.DESCRIPTION
    Use this script if you have already deployed the application and just want to apply
    the latest changes (Admin Table, Login Page, Security Interceptors).

    Actions:
    1.  Compiles the latest Java code.
    2.  Updates the Database (Force resets admin user).
    3.  Updates the application files in Tomcat.
    4.  Restarts Tomcat.

.PARAMETER TomcatHome
    Path to existing Tomcat (e.g. C:\apache-tomcat-9.0.84)
.PARAMETER MySQLRootPassword
    MySQL Root Password.

.EXAMPLE
    .\update_existing_install.ps1 -TomcatHome "C:\apache-tomcat-9.0.84" -MySQLRootPassword "root"
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$TomcatHome,

    [Parameter(Mandatory=$true)]
    [string]$MySQLRootPassword,

    [string]$ProjectPath = $PSScriptRoot
)

$ErrorActionPreference = "Stop"

function Print-Msg {
    param([string]$msg, [string]$color="Cyan")
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

Print-Msg "Starting Update Process..." "Green"

# 1. Compile Latest Code
Print-Msg "Step 1: Compiling Latest Code..."
$SrcDir = "$ProjectPath\src"
$WebInfDir = "$ProjectPath\WebContent\WEB-INF"
$ClassesDir = "$WebInfDir\classes"
$LibDir = "$WebInfDir\lib"

# Ensure output directory exists
if (-not (Test-Path $ClassesDir)) { New-Item -ItemType Directory -Path $ClassesDir | Out-Null }

# Get Sources
$fs = Get-ChildItem -Path $SrcDir -Recurse -Filter "*.java"
$fs.FullName | Out-File "$ProjectPath\sources.txt" -Encoding ascii

# Classpath
$LibJars = Get-ChildItem "$LibDir" -Filter "*.jar" -Recurse | Select-Object -ExpandProperty FullName
if (-not $LibJars) { Write-Warning "No JARs found in $LibDir" }
$TomcatJars = "$TomcatHome\lib\*"
$Classpath = ($LibJars -join ";") + ";" + $TomcatJars

Write-Host "Classpath length: $($Classpath.Length)" -ForegroundColor Gray

try {
    & javac -encoding UTF-8 -cp $Classpath -d $ClassesDir "@$ProjectPath\sources.txt"
    if ($LASTEXITCODE -ne 0) { throw "Javac exited with code $LASTEXITCODE" }
    Print-Msg "Compilation Successful." "Green"
} catch {
    Write-Error "Compilation Failed. Ensure JDK 8 is installed and javac is in PATH."
}
Remove-Item "$ProjectPath\sources.txt" -ErrorAction SilentlyContinue

# Copy Resources (struts.xml, properties)
Print-Msg "Updating Resource Files..." "Gray"
Get-ChildItem -Path $SrcDir -Recurse -Include "*.xml", "*.properties" | ForEach-Object {
    $relPath = $_.FullName.Substring($SrcDir.Length)
    $dest = Join-Path $ClassesDir $relPath
    $parent = Split-Path $dest -Parent
    if (-not (Test-Path $parent)) { New-Item -ItemType Directory -Path $parent | Out-Null }
    Copy-Item $_.FullName -Destination $dest -Force
}

# 1b. Update config.xml with provided credentials
Print-Msg "Configuring Database Connection..." "Gray"
$ConfigXml = "$ClassesDir\config.xml"
if (Test-Path $ConfigXml) {
    [xml]$xml = Get-Content $ConfigXml
    $xml.root.databaseconnect.user = "root"
    $xml.root.databaseconnect.password = $MySQLRootPassword
    # Ensure URL points to localhost (fix if coming from docker config)
    $currentUrl = $xml.root.databaseconnect.url
    if ($currentUrl -match "mysql:3306") {
        $xml.root.databaseconnect.url = $currentUrl.Replace("mysql:3306", "localhost:3306")
    }
    $xml.Save($ConfigXml)
    Print-Msg "Updated config.xml with provided password." "Gray"
}

# 2. Update Database
Print-Msg "Step 2: Updating Database Schema..."
$AdminSql = "$ProjectPath\sql\create_admin_table.sql"

if (Test-Path $AdminSql) {
    try {
        # Auto-detect MySQL if not in PATH
        $MySqlExe = "mysql"
        try { Get-Command mysql -ErrorAction Stop | Out-Null } catch {
            $CommonPaths = @(
                "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
                "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe"
            )
            foreach ($p in $CommonPaths) {
                if (Test-Path $p) { $MySqlExe = "`"$p`""; break }
            }
        }
        
        $MySqlCmd = "& $MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo"
        
        # Run force reset
        $ResetSql = "$ProjectPath\sql\force_reset_user.sql"
        if (Test-Path $ResetSql) {
             # Use cmd /c for redirection
             cmd /c "$MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo < `"$ResetSql`""
             if ($LASTEXITCODE -eq 0) {
                Print-Msg "Admin User FORCED RESET successfully." "Green"
             } else {
                Write-Warning "SQL Execution failed. Check password/path."
             }
        } else {
             # Fallback
             cmd /c "$MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo < `"$AdminSql`""
             Print-Msg "Admin Table SQL applied." "Green"
        }
    } catch {
        Write-Warning "Failed to apply SQL. Please check your MySQL password and ensure MySQL is running."
    }
} else {
    Write-Warning "SQL file not found: $AdminSql"
}

# 3. Update Tomcat Deployment
Print-Msg "Step 3: Updating Tomcat Deployment..."
$WebAppDir = "$TomcatHome\webapps\pushdemo"

if (-not (Test-Path $WebAppDir)) {
    Write-Error "Application not found at $WebAppDir. Please run the full deployment first."
}

# Overwrite WebContent files (JSPs, CSS, WEB-INF)
Print-Msg "Copying new files to $WebAppDir..." "Gray"
Copy-Item -Path "$ProjectPath\WebContent\*" -Destination $WebAppDir -Recurse -Force

Print-Msg "Step 4: Restarting Tomcat..."
$Service = Get-Service -Name "TomcatPushDemo" -ErrorAction SilentlyContinue

if ($Service) {
    Restart-Service -Name "TomcatPushDemo"
    Print-Msg "Tomcat Service Restarted." "Green"
} else {
    Write-Warning "Tomcat Service 'TomcatPushDemo' not found. You may need to restart Tomcat manually."
}

Print-Msg "Update Complete! Please refresh your browser." "Green"
