#requires -version 5.1
<#
.SYNOPSIS
    Updates an EXISTING Push Demo installation with recent changes.
    FORCES JAVA 8 COMPATIBILITY AND CLEAN BUILD.
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

# 0. Clean Inputs
$TomcatHome = $TomcatHome -replace '"', ''
if (-not (Test-Path $TomcatHome)) {
    Write-Error "TomcatHome does not exist: $TomcatHome"
}

Print-Msg "Starting Update Process (CLEAN BUILD)..." "Green"

# 1. Compile Latest Code
Print-Msg "Step 1: Compiling Latest Code..."
$SrcDir = "$ProjectPath\src"
$WebInfDir = "$ProjectPath\WebContent\WEB-INF"
$ClassesDir = "$WebInfDir\classes"
$LibDir = "$WebInfDir\lib"

# CLEAN: Delete existing classes to prevent version mixing
if (Test-Path $ClassesDir) {
    Print-Msg "Cleaning old class files..." "Gray"
    Remove-Item -Path $ClassesDir -Recurse -Force
}
New-Item -ItemType Directory -Path $ClassesDir | Out-Null

# Get Sources
$fs = Get-ChildItem -Path $SrcDir -Recurse -Filter "*.java"
$fs.FullName | Out-File "$ProjectPath\sources.txt" -Encoding ascii

# Classpath
$LibJars = Get-ChildItem "$LibDir" -Filter "*.jar" -Recurse | Select-Object -ExpandProperty FullName
if (-not $LibJars) { Write-Warning "No JARs found in $LibDir" }
$TomcatJars = "$TomcatHome\lib\*"
$Classpath = ($LibJars -join ";") + ";" + $TomcatJars

Write-Host "Classpath length: $($Classpath.Length)" -ForegroundColor Gray

# Compile
try {
    Write-Host "Checking Compiler Version:" -ForegroundColor Gray
    & javac -version

    Print-Msg "Compiling with -source 1.8 -target 1.8 (Java 8 Compatibility)..." "Yellow"
    
    # Run Javac
    & javac -source 1.8 -target 1.8 -encoding UTF-8 -cp $Classpath -d $ClassesDir "@$ProjectPath\sources.txt"
    
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

# 1b. Update config.xml
Print-Msg "Configuring Database Connection..." "Gray"
$ConfigXml = "$ClassesDir\config.xml"
if (Test-Path $ConfigXml) {
    [xml]$xml = Get-Content $ConfigXml
    $xml.root.databaseconnect.user = "root"
    $xml.root.databaseconnect.password = $MySQLRootPassword
    # Fix URL
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
$ResetSql = "$ProjectPath\sql\force_reset_user.sql"

# Auto-detect MySQL
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

# Try Force Reset (assumes table exists)
$ResetSuccess = $false
if (Test-Path $ResetSql) {
    try {
        $proc = Start-Process -FilePath "cmd.exe" -ArgumentList "/c `"$MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo < `"$ResetSql`"`"" -Wait -PassThru -NoNewWindow
        if ($proc.ExitCode -eq 0) {
            Print-Msg "Admin User FORCED RESET successfully." "Green"
            $ResetSuccess = $true
        } else {
            Write-Warning "Force reset failed. Attempting to create table..."
        }
    } catch { Write-Warning "SQL Error" }
}

# Fallback: Create Table
if (-not $ResetSuccess -and (Test-Path $AdminSql)) {
    try {
        $proc = Start-Process -FilePath "cmd.exe" -ArgumentList "/c `"$MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo < `"$AdminSql`"`"" -Wait -PassThru -NoNewWindow
        if ($proc.ExitCode -eq 0) {
             Print-Msg "Admin Table Created successfully." "Green"
        } else {
             Write-Error "Failed to create Admin Table. Check your database connection."
        }
    } catch { Write-Error "Failed to apply SQL schema." }
}

# 3. Update Tomcat Deployment
Print-Msg "Step 3: Updating Tomcat Deployment..."
$WebAppDir = "$TomcatHome\webapps\pushdemo"

if (-not (Test-Path $WebAppDir)) {
    Print-Msg "Application folder not found. Creating it..." "Gray"
    New-Item -ItemType Directory -Path $WebAppDir -Force | Out-Null
}

# Overwrite WebContent files
Print-Msg "Copying new files to $WebAppDir..." "Gray"
Copy-Item -Path "$ProjectPath\WebContent\*" -Destination $WebAppDir -Recurse -Force

Print-Msg "Step 4: Restarting Tomcat..."
$Service = Get-Service -Name "TomcatPushDemo" -ErrorAction SilentlyContinue
if ($Service) {
    Restart-Service -Name "TomcatPushDemo"
    Print-Msg "Tomcat Service Restarted." "Green"
} else {
    Write-Warning "Tomcat Service 'TomcatPushDemo' not found. Please restart manually."
}

Print-Msg "Update Complete! Please refresh your browser." "Green"
