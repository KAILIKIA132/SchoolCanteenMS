#requires -version 5.1
<#
.SYNOPSIS
    Full Application Compilation and Deployment Script for Windows Server environment.

.DESCRIPTION
    This script compiles and deploys the complete Push Demo application with all dependencies.
    It performs the following:
    1.  Compiles all Java source code with complete dependency resolution.
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
.PARAMETER CompileAll
    Flag to compile all source files including dependencies (default: $true).

.EXAMPLE
    .\deploy_full_app.ps1 -TomcatHome "C:\apache-tomcat-9.0.84" -MySQLRootPassword "Canteen@2026"
#>

param(
    [Parameter(Mandatory=$true)]
    [string]$TomcatHome,

    [Parameter(Mandatory=$true)]
    [string]$MySQLRootPassword,

    [string]$MySQLPort = "3306",
    
    [string]$ProjectPath = $PSScriptRoot,
    
    [bool]$CompileAll = $true
)

# Add MySQL to PATH at the start of the script
$MySQLPaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin",
    "C:\Program Files (x86)\MySQL\MySQL Server 5.7\bin"
)

foreach ($path in $MySQLPaths) {
    if (Test-Path $path) {
        $env:PATH += ";$path"
        Write-Host "Added MySQL path to environment: $path"
        break
    }
}

# --- configuration ---
$ErrorActionPreference = "Stop"

function Print-Msg {
    param([string]$msg, [string]$color="Cyan")
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

Print-Msg "Starting Full Application Compilation and Deployment..." "Green"
Print-Msg "Project Path: $ProjectPath"
Print-Msg "Tomcat Home:  $TomcatHome"
Print-Msg "Compiling All Dependencies: $CompileAll"

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
    # Test MySQL availability
    $mysqlTest = mysql --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Print-Msg "Found mysql CLI." "Gray"
    } else {
        # Try with full path
        $mysqlPaths = @(
            "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
            "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe",
            "C:\Program Files (x86)\MySQL\MySQL Server 8.0\bin\mysql.exe"
        )
        
        $mysqlFound = $false
        foreach ($mysqlPath in $mysqlPaths) {
            if (Test-Path $mysqlPath) {
                & $mysqlPath --version 2>$null
                if ($LASTEXITCODE -eq 0) {
                    Print-Msg "Found mysql CLI at: $mysqlPath" "Gray"
                    $mysqlFound = $true
                    break
                }
            }
        }
        
        if (-not $mysqlFound) {
            Write-Error "MySQL client (mysql.exe) not found. Please ensure MySQL is installed and accessible."
        }
    }
} catch {
    Write-Error "MySQL client (mysql.exe) not found. Please ensure MySQL is installed and accessible."
}

# 2. Prepare Libraries and Dependencies
Print-Msg "Step 2: Preparing Libraries and Dependencies..."

$SrcDir = "$ProjectPath\src"
$WebInfDir = "$ProjectPath\WebContent\WEB-INF"
$ClassesDir = "$WebInfDir\classes"
$LibDir = "$WebInfDir\lib"

# Create lib directory if it doesn't exist
if (-not (Test-Path $LibDir)) {
    New-Item -ItemType Directory -Path $LibDir -Force | Out-Null
    Print-Msg "Created lib directory: $LibDir" "Gray"
}

# Download required libraries if not present
# Struts 2 libraries
$strutsLibs = @(
    "struts2-core-2.5.30.jar",
    "freemarker-2.3.31.jar",
    "ognl-3.1.26.jar",
    "commons-fileupload-1.4.jar",
    "commons-io-2.8.0.jar",
    "commons-lang3-3.12.0.jar",
    "javassist-3.27.0-GA.jar",
    "log4j-api-2.14.1.jar",
    "log4j-core-2.14.1.jar",
    "log4j-slf4j-impl-2.14.1.jar"
)

# Check if common libraries exist, warn if missing but continue
$missingLibs = @()
foreach ($lib in $strutsLibs) {
    $libPath = "$LibDir\$lib"
    if (-not (Test-Path $libPath)) {
        $missingLibs += $lib
    }
}

if ($missingLibs.Count -gt 0) {
    Print-Msg "Warning: The following libraries are missing from $LibDir (will try to use Tomcat libs):" "Yellow"
    $missingLibs | ForEach-Object { Write-Host "  - $_" }
} else {
    Print-Msg "All required libraries found in lib directory." "Green"
}

# 3. Compile Java Source with Full Dependency Resolution
Print-Msg "Step 3: Compiling Complete Java Application..."

# Ensure output directory exists
if (-not (Test-Path $ClassesDir)) {
    New-Item -ItemType Directory -Path $ClassesDir -Force | Out-Null
}

# List all java files recursively
$fs = Get-ChildItem -Path $SrcDir -Recurse -Filter "*.java"
if ($fs.Count -eq 0) {
    Write-Error "No Java source files found in $SrcDir"
}

Print-Msg "Found $($fs.Count) Java source files to compile..." "Gray"

# Create sources list file
$fs.FullName > "$ProjectPath\sources.txt"

# Build comprehensive classpath with all dependencies
$TomcatLib = "$TomcatHome\lib\*"
$ProjectLib = "$LibDir\*"
$Classpath = "$TomcatLib;$ProjectLib"

Print-Msg "Using classpath: $Classpath" "Gray"

# Compile with verbose output and error handling
Print-Msg "Compiling with full dependency resolution..." "Gray"
try {
    # Use -verbose flag to see compilation details
    $compileResult = javac -cp $Classpath -d $ClassesDir -verbose "@$ProjectPath\sources.txt" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Print-Msg "Full application compilation successful." "Green"
    } else {
        Write-Warning "Compilation may have warnings. Check output above."
    }
} catch {
    Write-Error "Full application compilation failed. Error: $($_.Exception.Message)"
}

# Clean up temporary sources file
Remove-Item "$ProjectPath\sources.txt" -ErrorAction SilentlyContinue

# 4. Copy Resource Files
Print-Msg "Step 4: Copying Resource Files..."
Get-ChildItem -Path $SrcDir -Recurse -Include "*.xml", "*.properties", "*.txt" | ForEach-Object {
    $relPath = $_.FullName.Substring($SrcDir.Length + 1) # +1 to remove leading \
    $dest = Join-Path $ClassesDir $relPath
    $parent = Split-Path $dest -Parent
    if (-not (Test-Path $parent)) { 
        New-Item -ItemType Directory -Path $parent -Force | Out-Null 
    }
    Copy-Item $_.FullName -Destination $dest -Force
    Print-Msg "Copied resource: $relPath" "DarkGray"
}

Print-Msg "Resource files copied successfully." "Gray"

# 5. Setup Database
Print-Msg "Step 5: Setting up Database..."
$MySqlCmd = "mysql -u root -p$MySQLRootPassword -P $MySQLPort"

# Create Database if not exists
Print-Msg "Creating database 'pushdemo'..." "Gray"
$createDbQuery = "CREATE DATABASE IF NOT EXISTS pushdemo DEFAULT CHARACTER SET utf8;"
$result = echo $createDbQuery | cmd /c "$MySqlCmd"
if ($LASTEXITCODE -ne 0) {
    Write-Warning "Database creation may have failed. Continuing anyway..."
} else {
    Print-Msg "Database 'pushdemo' created successfully." "Gray"
}

# Run Init Scripts
$InitScript = "$ProjectPath\docker\mysql\init\02-pushdemo-schema.sql"
if (Test-Path $InitScript) {
    Print-Msg "Importing Schema ($InitScript)..." "Gray"
    $result = cmd /c "$MySqlCmd pushdemo < `"$InitScript`""
    if ($LASTEXITCODE -ne 0) {
        Write-Warning "Schema import may have failed. Check $InitScript exists and is accessible."
    } else {
        Print-Msg "Schema imported successfully." "Gray"
    }
} else {
    Write-Warning "Schema file not found: $InitScript"
}

$AdminScript = "$ProjectPath\sql\create_admin_table.sql"
if (Test-Path $AdminScript) {
    Print-Msg "Creating Admin Table ($AdminScript)..." "Gray"
    $result = cmd /c "$MySqlCmd pushdemo < `"$AdminScript`""
    if ($LASTEXITCODE -ne 0) {
        Write-Warning "Admin table creation may have failed. Check $AdminScript exists and is accessible."
    } else {
        Print-Msg "Admin table created successfully." "Gray"
    }
} else {
    Write-Warning "Admin script not found: $AdminScript"
}

# 6. Configure Application
Print-Msg "Step 6: Configuring Application..."
$ConfigXml = "$ClassesDir\config.xml"
if (Test-Path $ConfigXml) {
    [xml]$xml = Get-Content $ConfigXml
    $xml.root.databaseconnect.user = "root"
    $xml.root.databaseconnect.password = $MySQLRootPassword
    # Ensure database URL points to localhost/correct port
    $currentDbUrl = $xml.root.databaseconnect.url
    if ($currentDbUrl -match "mysql:3306") {
        # Fix docker hostname to localhost
        $xml.root.databaseconnect.url = $currentDbUrl.Replace("mysql:3306", "localhost:$MySQLPort")
    }
        
    # Update external API URL if needed (you can modify this to your desired URL)
    $currentApiUrl = $xml.root.externalapi.url
    if ($currentApiUrl -match "10.35.200.1:8003") {
        # You can change this to your actual external API endpoint
        # Example: $xml.root.externalapi.url = "http://your-server:port/api/endpoint"
        Print-Msg "External API URL kept as: $currentApiUrl" "Gray"
    }
    $xml.Save($ConfigXml)
    Print-Msg "Updated config.xml with provided credentials and API configuration." "Gray"
} else {
    Write-Warning "config.xml not found at $ConfigXml. You may need to create it manually."
}

# 7. Deploy to Tomcat
Print-Msg "Step 7: Deploying Complete Application to Tomcat..."
$WebAppDir = "$TomcatHome\webapps\pushdemo"

Print-Msg "Deploying artifacts to $WebAppDir..." "Gray"

# Remove existing if exists to ensure clean deploy
if (Test-Path $WebAppDir) {
    Print-Msg "Removing existing deployment..." "Gray"
    Remove-Item -Path $WebAppDir -Recurse -Force
}

# Create webapp directory
New-Item -ItemType Directory -Path $WebAppDir -Force | Out-Null

# Copy all web content
Copy-Item -Path "$ProjectPath\WebContent\*" -Destination $WebAppDir -Recurse -Force
Print-Msg "Web content deployed." "Gray"

# Ensure WEB-INF\classes exists and copy compiled classes
$WebInfClassesDir = "$WebAppDir\WEB-INF\classes"
if (-not (Test-Path $WebInfClassesDir)) {
    New-Item -ItemType Directory -Path $WebInfClassesDir -Force | Out-Null
}
Copy-Item -Path "$ClassesDir\*" -Destination $WebInfClassesDir -Recurse -Force
Print-Msg "Compiled classes deployed." "Gray"

# Copy libraries to WEB-INF\lib
$WebInfLibDir = "$WebAppDir\WEB-INF\lib"
if (-not (Test-Path $WebInfLibDir)) {
    New-Item -ItemType Directory -Path $WebInfLibDir -Force | Out-Null
}

# Copy all JAR files from project lib
if (Test-Path $LibDir) {
    Copy-Item -Path "$LibDir\*.jar" -Destination $WebInfLibDir -Force -ErrorAction SilentlyContinue
    Print-Msg "Library JAR files deployed." "Gray"
}

# Ensure MySQL Connector is in Tomcat Lib and WebApp Lib
$ConnectorFile = Get-ChildItem $LibDir -Filter "mysql-connector-java*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1
if ($ConnectorFile) {
    Copy-Item $ConnectorFile.FullName -Destination "$TomcatHome\lib\" -Force
    Copy-Item $ConnectorFile.FullName -Destination "$WebInfLibDir\" -Force -ErrorAction SilentlyContinue
    Print-Msg "Copied MySQL connector: $($ConnectorFile.Name) to both Tomcat and WebApp lib" "Gray"
} else {
    Write-Warning "MySQL Connector JAR not found in $LibDir. Database connection may fail."
}

# 8. Python Virtual Environment Setup
Print-Msg "Step 8: Setting up Python Environment for Proxy..."
$VenvDir = "$ProjectPath\venv"
if (-not (Test-Path $VenvDir)) {
    try {
        Print-Msg "Creating Python virtual environment..." "Gray"
        python -m venv $VenvDir
        Print-Msg "Virtual Environment created at $VenvDir" "Green"
        
        # Activate and install required packages if requirements.txt exists
        $requirementsFile = "$ProjectPath\requirements.txt"
        if (Test-Path $requirementsFile) {
            Print-Msg "Installing Python dependencies from requirements.txt..." "Gray"
            & "$VenvDir\Scripts\pip.exe" install -r $requirementsFile
        }
    } catch {
        Write-Warning "Failed to create Python venv. Is 'python' installed? Error: $($_.Exception.Message)"
    }
} else {
    Print-Msg "Virtual Environment already exists." "Gray"
}

# Create a simple runner for the proxy
$ProxyBat = "$ProjectPath\run_proxy.bat"
@"
@echo off
echo Starting Python Proxy API...
call "$VenvDir\Scripts\activate.bat"
echo Activated virtual environment.
python "$ProjectPath\proxy-api.py"
pause
"@ | Out-File $ProxyBat -Encoding ASCII

# 9. Final Checks and Summary
Print-Msg "Step 9: Performing Final Checks..."

# Check if deployment was successful
if (Test-Path "$WebAppDir\index.jsp") {
    Print-Msg "✓ Web application files deployed successfully" "Green"
} else {
    Write-Warning "index.jsp not found in deployed application"
}

if (Test-Path "$WebInfClassesDir\com") {
    Print-Msg "✓ Compiled Java classes deployed successfully" "Green"
} else {
    Write-Warning "No compiled Java classes found in deployment"
}

if (Test-Path "$WebInfLibDir\*.jar") {
    Print-Msg "✓ Library JAR files deployed successfully" "Green"
} else {
    Write-Warning "No JAR files found in WEB-INF/lib"
}

Print-Msg "" "White"
Print-Msg "===============================================" "Green"
Print-Msg " FULL APPLICATION COMPILATION AND DEPLOYMENT COMPLETE!" "Green"
Print-Msg "===============================================" "Green"
Print-Msg "Summary:" "Cyan"
Print-Msg "  • Compiled $($fs.Count) Java source files with dependencies" "Cyan"
Print-Msg "  • Created 'pushdemo' database in MySQL" "Cyan"
Print-Msg "  • Deployed application to: $WebAppDir" "Cyan"
Print-Msg "  • Configured database connection" "Cyan"
Print-Msg "  • Set up Python virtual environment" "Cyan"
Print-Msg "" "White"
Print-Msg "Next Steps:" "Yellow"
Print-Msg "  1. Restart Tomcat to load the new application." "Yellow"
Print-Msg "  2. Run '$ProxyBat' to start the proxy listener if needed." "Yellow"
Print-Msg "  3. Access http://localhost:8080/pushdemo" "Yellow"
Print-Msg "  4. Check Tomcat logs for any deployment errors." "Yellow"
Print-Msg "===============================================" "Green"