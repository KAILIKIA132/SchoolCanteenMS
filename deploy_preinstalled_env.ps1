#requires -version 5.1
<#
.SYNOPSIS
    Deploys Push Demo Project to an existing Windows Server environment (No Docker).

.DESCRIPTION
    This script deploys the application to a server that ALREADY has Java, MySQL, and Tomcat installed.
    It performs the following:
    1.  Compiles the Java source code (requires javac).
    2.  Creates the MySQL database and schema.
    3.  Configures the application (config.xml) with DB credentials.
    4.  Deploys the compiled application to the existing Tomcat instance.
    5.  Sets up a Python virtual environment for the Proxy API.

.PARAMETER TomcatHome
    Path to the existing Tomcat installation (e.g. C:\Program Files\Apache Software Foundation\Tomcat 9.0)
.PARAMETER MySQLRootPassword
    Password for the MySQL 'root' user.
.PARAMETER MySQLPort
    Port MySQL is running on (default 3306).
.PARAMETER ProjectPath
    Path to the source code root (default: current directory).

.EXAMPLE
    .\deploy_preinstalled_env.ps1 -TomcatHome "C:\apache-tomcat-9.0.84" -MySQLRootPassword "root"
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$TomcatHome,

    [Parameter(Mandatory=$true)]
    [string]$MySQLRootPassword,

    [string]$MySQLPort = "3306",
    
    [string]$ProjectPath = $PSScriptRoot
)

# --- configuration ---
$ErrorActionPreference = "Stop"

function Print-Msg {
    param([string]$msg, [string]$color="Cyan")
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

Print-Msg "Starting Deployment to Pre-Installed Environment..." "Green"
Print-Msg "Project Path: $ProjectPath"
Print-Msg "Tomcat Home:  $TomcatHome"

# 1. Verify Prerequisites
Print-Msg "Step 1: Verifying Environment..."
if (-not (Test-Path "$TomcatHome\bin\catalina.bat")) {
    Write-Error "Tomcat not found at $TomcatHome. Please check the path."
}

try {
    $javacVer = javac -version 2>&1
    Print-Msg "Found javac: $javacVer" "Gray"
} catch {
    Write-Error "Java Compiler (javac) not found in PATH. Please install JDK 8 and add 'bin' to PATH."
}

try {
    mysql --version | Out-Null
    Print-Msg "Found mysql CLI." "Gray"
} catch {
    Write-Error "MySQL client (mysql) not found in PATH. Please add MySQL bin to PATH."
}

# 2. Compile Java Source
Print-Msg "Step 2: Compiling Java Source..."

$SrcDir = "$ProjectPath\src"
$WebInfDir = "$ProjectPath\WebContent\WEB-INF"
$ClassesDir = "$WebInfDir\classes"
$LibDir = "$WebInfDir\lib"

# Ensure output directory exists
if (-not (Test-Path $ClassesDir)) {
    New-Item -ItemType Directory -Path $ClassesDir | Out-Null
}

# List all java files
$fs = Get-ChildItem -Path $SrcDir -Recurse -Filter "*.java"
if ($fs.Count -eq 0) {
    Write-Error "No Java source files found in $SrcDir"
}
$fs.FullName > "$ProjectPath\sources.txt"

# Build Classpath
# Includes Tomcat libs (servlet-api.jar etc) and Project libs
$TomcatLib = "$TomcatHome\lib\*"
$ProjectLib = "$LibDir\*"
$Classpath = "$TomcatLib;$ProjectLib"

Print-Msg "Compiling $($fs.Count) source files..." "Gray"
try {
    javac -cp $Classpath -d $ClassesDir "@$ProjectPath\sources.txt"
    Print-Msg "Compilation Successful." "Green"
} catch {
    Write-Error "Compilation Failed. See errors above."
}
Remove-Item "$ProjectPath\sources.txt" -ErrorAction SilentlyContinue

# Copy Resources (xml, properties) to classes
Print-Msg "Copying resource files (XML, properties)..." "Gray"
Get-ChildItem -Path $SrcDir -Recurse -Include "*.xml", "*.properties" | ForEach-Object {
    $relPath = $_.FullName.Substring($SrcDir.Length)
    $dest = Join-Path $ClassesDir $relPath
    $parent = Split-Path $dest -Parent
    if (-not (Test-Path $parent)) { New-Item -ItemType Directory -Path $parent | Out-Null }
    Copy-Item $_.FullName -Destination $dest -Force
}

# 3. Setup Database
Print-Msg "Step 3: Setting component Database..."
$MySqlCmd = "mysql -u root -p$MySQLRootPassword -P $MySQLPort"

# Create Database if not exists
Print-Msg "Creating database 'pushdemo'..." "Gray"
cmd /c "echo CREATE DATABASE IF NOT EXISTS pushdemo DEFAULT CHARACTER SET utf8; | $MySqlCmd"

# Run Init Scripts
$InitScript = "$ProjectPath\docker\mysql\init\02-pushdemo-schema.sql"
if (Test-Path $InitScript) {
    Print-Msg "Importing Schema ($InitScript)..." "Gray"
    cmd /c "$MySqlCmd pushdemo < `"$InitScript`""
}

$AdminScript = "$ProjectPath\sql\create_admin_table.sql"
if (Test-Path $AdminScript) {
    Print-Msg "Creating Admin Table ($AdminScript)..." "Gray"
    cmd /c "$MySqlCmd pushdemo < `"$AdminScript`""
}

# 4. Configure Application
Print-Msg "Step 4: Configuring Application..."
$ConfigXml = "$ClassesDir\config.xml"
if (Test-Path $ConfigXml) {
    [xml]$xml = Get-Content $ConfigXml
    $xml.root.databaseconnect.user = "root"
    $xml.root.databaseconnect.password = $MySQLRootPassword
    # Ensure URL points to localhost/correct port
    $currentUrl = $xml.root.databaseconnect.url
    if ($currentUrl -match "mysql:3306") {
        # Fix docker hostname to localhost
        $xml.root.databaseconnect.url = $currentUrl.Replace("mysql:3306", "localhost:$MySQLPort")
    }
    $xml.Save($ConfigXml)
    Print-Msg "Updated config.xml with provided credentials." "Gray"
} else {
    Write-Warning "config.xml not found at $ConfigXml"
}

# 5. Deploy to Tomcat
Print-Msg "Step 5: Deploying to Tomcat..."
$WebAppDir = "$TomcatHome\webapps\pushdemo"

# Stop Tomcat (Recommended but optional, trying to do hot deploy if running)
# We will just overwrite files.
Print-Msg "Deploying artifacts to $WebAppDir..." "Gray"

# Remove existing if exists to ensure clean deploy
if (Test-Path $WebAppDir) {
    Remove-Item -Path $WebAppDir -Recurse -Force
}
New-Item -ItemType Directory -Path $WebAppDir | Out-Null

Copy-Item -Path "$ProjectPath\WebContent\*" -Destination $WebAppDir -Recurse -Force

Print-Msg "Ensuring MySQL Connector is in Tomcat Lib..."
$Connector = "$ProjectLib\mysql-connector-java*.jar"
$ConnectorFile = Get-ChildItem $ProjectLib -Filter "mysql-connector-java*.jar" | Select-Object -First 1
if ($ConnectorFile) {
    Copy-Item $ConnectorFile.FullName -Destination "$TomcatHome\lib\" -Force
} else {
    Write-Warning "MySQL Connector JAR not found in $LibDir. Database connection may fail."
}

# 6. Python Virtual Env
Print-Msg "Step 6: Setting up Python Environment for Proxy..."
$VenvDir = "$ProjectPath\venv"
if (-not (Test-Path $VenvDir)) {
    try {
        python -m venv $VenvDir
        Print-Msg "Virtual Environment created at $VenvDir" "Green"
    } catch {
        Write-Warning "Failed to create Python venv. Is 'python' installed?"
    }
} else {
    Print-Msg "Virtual Environment already exists." "Gray"
}

# Create a simple runner for the proxy
$ProxyBat = "$ProjectPath\run_proxy.bat"
@"
@echo off
call "$VenvDir\Scripts\activate.bat"
python "$ProjectPath\proxy-api.py"
"@ | Out-File $ProxyBat -Encoding ASCII

Print-Msg "Deploy Complete!" "Green"
Print-Msg "-------------------------------------------"
Print-Msg "1. Restart Tomcat to load the new application."
Print-Msg "2. Run '$ProxyBat' to start the proxy listener."
Print-Msg "3. Access http://localhost:8080/pushdemo"
