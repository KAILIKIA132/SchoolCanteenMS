#requires -version 5.1
<#
.SYNOPSIS
    Complete setup script for Push Demo Project on Windows Server using Python virtual environment
.DESCRIPTION
    This script automates the entire setup process for the Push Demo biometric device management system
    on Windows Server. It includes:
    - Prerequisites installation (Java, MySQL, Tomcat)
    - Project cloning and configuration
    - Python virtual environment setup for proxy API
    - Database setup and configuration
    - Service configuration and deployment
.PARAMETER InstallPath
    Path where the project will be installed (default: C:\pushdemoNew)
.PARAMETER GitUrl
    Git repository URL (default: https://github.com/KAILIKIA132/SchoolCanteenMS.git)
.PARAMETER MySQLRootPassword
    MySQL root password (will be prompted if not provided)
.PARAMETER TomcatVersion
    Tomcat version to download (default: 9.0.84)
.PARAMETER JavaVersion
    Java version to download (default: 8.0.392)
.EXAMPLE
    .\setup.ps1
    .\setup.ps1 -InstallPath "D:\SchoolCanteen" -MySQLRootPassword "MySecurePassword123"
.NOTES
    Author: Generated for Push Demo Project
    Requires: PowerShell 5.1+, Administrator privileges
#>

param(
    [string]$InstallPath = "C:\pushdemoNew",
    [string]$GitUrl = "https://github.com/KAILIKIA132/SchoolCanteenMS.git",
    [string]$MySQLRootPassword,
    [string]$TomcatVersion = "9.0.84",
    [string]$JavaVersion = "8.0.392"
)

# Ensure running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Error "This script must be run as Administrator. Please run PowerShell as Administrator and try again."
    exit 1
}

# Colors for output
$host.UI.RawUI.ForegroundColor = "White"
function Write-ColorOutput($ForegroundColor) {
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
}

# Global variables
$ScriptPath = $PSScriptRoot

# Auto-detect if running inside the repository
if (Test-Path "$PSScriptRoot\.git") {
    $InstallPath = $PSScriptRoot
    Write-Host "Detected running inside repository. Setting InstallPath to $InstallPath"
}

$LogPath = "$InstallPath\setup.log"
$TempPath = "$env:TEMP\pushdemo_setup"
$JavaInstallPath = "C:\Program Files\Eclipse Adoptium"
$TomcatInstallPath = "C:\apache-tomcat-$TomcatVersion"
$MySQLInstallPath = "$env:ProgramFiles\MySQL\MySQL-8.0"
$MySQLBinPath = "$env:ProgramFiles\MySQL\MySQL-8.0\bin"
$PythonPath = "$InstallPath\venv"

# Create log directory
if (-not (Test-Path (Split-Path $LogPath -Parent))) {
    New-Item -ItemType Directory -Path (Split-Path $LogPath -Parent) -Force | Out-Null
}

# Start logging
Start-Transcript -Path $LogPath -Append

Write-ColorOutput Green
Write-Host "=========================================="
Write-Host "  Push Demo Project Setup for Windows Server"
Write-Host "=========================================="
Write-Host ""
Write-Host "Installation Path: $InstallPath"
Write-Host "Git Repository: $GitUrl"
Write-Host "Tomcat Version: $TomcatVersion"
Write-Host "Java Version: $JavaVersion"
Write-Host "=========================================="
Write-Host ""

# Create temp directory
if (-not (Test-Path $TempPath)) {
    New-Item -ItemType Directory -Path $TempPath -Force | Out-Null
}

# Function to log messages
function Write-Log {
    param([string]$Message, [string]$Level = "INFO")
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $logMessage = "[$timestamp] [$Level] $Message"
    Write-Host $logMessage
    # Add-Content -Path $LogPath -Value $logMessage  # Disabled to avoid file lock conflict with Start-Transcript
}

# Function to check if a command exists
function Test-Command {
    param([string]$Command)
    try {
        Get-Command $Command -ErrorAction Stop | Out-Null
        return $true
    }
    catch {
        return $false
    }
}

# Function to download and extract file
function Invoke-DownloadAndExtract {
    param(
        [string]$Url,
        [string]$DestinationPath,
        [string]$FileName
    )
    
    $filePath = Join-Path $TempPath $FileName
    
    Write-Log "Downloading $FileName from $Url..."
    try {
        Invoke-WebRequest -Uri $Url -OutFile $filePath -UseBasicParsing
        Write-Log "Download completed: $filePath"
    }
    catch {
        Write-Log "Failed to download ${FileName}: $($_.Exception.Message)" "ERROR"
        throw
    }
    
    # Extract if it's a zip file
    if ($FileName -like "*.zip") {
        Write-Log "Extracting $FileName..."
        try {
            Expand-Archive -Path $filePath -DestinationPath $DestinationPath -Force
            Write-Log "Extraction completed"
        }
        catch {
            Write-Log "Failed to extract ${FileName}: $($_.Exception.Message)" "ERROR"
            throw
        }
    }
}

# Function to install Java
function Install-Java {
    Write-Log "Checking Java installation..."
    
    if (Test-Command "java") {
        $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString() }
        Write-Log "Java already installed: $javaVersion"
        return
    }
    
    Write-Log "Installing Java $JavaVersion..."
    
    # Download Java
    $javaUrl = "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u$($JavaVersion.Split('.')[2])-b08/OpenJDK8U-jdk_x64_windows_hotspot_$($JavaVersion.Replace('.', 'u'))b08.msi"
    $javaInstaller = "OpenJDK8U-jdk_x64_windows_hotspot_$($JavaVersion.Replace('.', 'u'))b08.msi"
    
    try {
        Invoke-DownloadAndExtract -Url $javaUrl -DestinationPath $TempPath -FileName $javaInstaller
        
        Write-Log "Installing Java MSI..."
        $installArgs = @(
            "/i", "$TempPath\$javaInstaller"
            "/quiet", "INSTALLDIR=`"$JavaInstallPath`""
            "ADDLOCAL=FeatureMain,FeatureEnvironment,FeatureJarFileRunWith,FeatureJavaHome"
        )
        
        Start-Process -FilePath "msiexec.exe" -ArgumentList $installArgs -Wait -NoNewWindow
        
        # Set environment variables
        [Environment]::SetEnvironmentVariable("JAVA_HOME", $JavaInstallPath, "Machine")
        $env:JAVA_HOME = $JavaInstallPath
        
        $currentPath = [Environment]::GetEnvironmentVariable("PATH", "Machine")
        if ($currentPath -notlike "*$JavaInstallPath\bin*") {
            [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$JavaInstallPath\bin", "Machine")
            $env:PATH = "$env:PATH;$JavaInstallPath\bin"
        }
        
        Write-Log "Java installation completed"
    }
    catch {
        Write-Log "Java installation failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Function to install MySQL
function Install-MySQL {
    Write-Log "Checking MySQL installation..."
    
    if (Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue) {
        Write-Log "MySQL service already exists"
        return
    }
    
    Write-Log "Installing MySQL 8.0..."
    
    # Prompt for MySQL root password if not provided
    if (-not $MySQLRootPassword) {
        do {
            $MySQLRootPassword = Read-Host "Enter MySQL root password (minimum 8 characters)"
        } while ($MySQLRootPassword.Length -lt 8)
        
        $confirmPassword = Read-Host "Confirm MySQL root password"
        if ($MySQLRootPassword -ne $confirmPassword) {
            Write-Log "Passwords do not match. Exiting." "ERROR"
            exit 1
        }
    }
    
    # Download MySQL ZIP archive instead of installer (more reliable)
    $mysqlUrls = @(
        "https://cdn.mysql.com/archives/mysql-8.0/mysql-8.0.36-winx64.zip",
        "https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.36-winx64.zip"
    )
    $mysqlZip = "mysql-8.0.36-winx64.zip"
    $MySQLInstallPath = "$env:ProgramFiles\MySQL\MySQL-8.0"
    
    $downloadSuccess = $false
    foreach ($url in $mysqlUrls) {
        try {
            Write-Log "Attempting to download MySQL from: $url"
            $mysqlUrl = $url
            Invoke-DownloadAndExtract -Url $mysqlUrl -DestinationPath $TempPath -FileName $mysqlZip
            $downloadSuccess = $true
            break
        }
        catch {
            Write-Log "Failed to download from: $url. Trying next URL..." -Level WARN
            continue
        }
    }
    
    if (-not $downloadSuccess) {
        throw "All MySQL download URLs failed. Please download MySQL manually and place it in the TempPath."
    }
    
    try {
        
        # Extract MySQL ZIP to destination
        Write-Log "Extracting MySQL..."
        Expand-Archive -Path "$TempPath\$mysqlZip" -DestinationPath "$env:ProgramFiles\MySQL" -Force
                
        # Rename extracted folder to match our expected path
        $extractedFolder = Get-ChildItem "$env:ProgramFiles\MySQL" -Directory | Where-Object { $_.Name -like "mysql-*" }
        if ($extractedFolder) {
            Rename-Item -Path $extractedFolder.FullName -NewName "MySQL-8.0"
        }
        
        # Configure MySQL
        $mysqlBinPath = "$MySQLInstallPath\bin"
        $dataDir = "$MySQLInstallPath\data"
        
        # Create data directory
        if (!(Test-Path $dataDir)) {
            New-Item -ItemType Directory -Path $dataDir -Force | Out-Null
        }
        
        $configScript = @"
[mysqld]
port=3306
datadir=$dataDir
basedir=$MySQLInstallPath
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION"

[mysql]
default-character-set=utf8mb4

[client]
port=3306
default-character-set=utf8mb4
"@
        
        $configScript | Out-File -FilePath "$MySQLInstallPath\my.ini" -Encoding ASCII
        
        # Initialize MySQL data directory
        Write-Log "Initializing MySQL data directory..."
        Start-Process -FilePath "$mysqlBinPath\mysqld.exe" -ArgumentList "--initialize", "--console", "--datadir=$dataDir", "--basedir=$MySQLInstallPath" -Wait -NoNewWindow
        
        # Install MySQL as a Windows service
        Write-Log "Installing MySQL as Windows service..."
        Start-Process -FilePath "$mysqlBinPath\mysqld.exe" -ArgumentList "--install", "MySQL80" -Wait -NoNewWindow
        
        # Start MySQL service
        Start-Service -Name "MySQL80" -ErrorAction SilentlyContinue
        Set-Service -Name "MySQL80" -StartupType Automatic
        
        # Wait a moment for MySQL service to be ready
        Start-Sleep -Seconds 5
        
        # Set root password - with ZIP distribution, we need to connect with the auto-generated temp password first
        Write-Log "Setting MySQL root password..."
        
        # First, find the temporary password from the error log
        $tempPassword = Get-Content "$dataDir\$env:COMPUTERNAME.err" | Select-String "temporary password" | ForEach-Object { ($_ -split "root@localhost: ")[1] }
        
        if ($tempPassword) {
            Write-Log "Found temporary password, changing to user-specified password..."
            $mysqlCmd = "$MySQLBinPath\mysql.exe"
            $changePasswordCmd = "ALTER USER 'root'@'localhost' IDENTIFIED BY '$MySQLRootPassword'; FLUSH PRIVILEGES;"
            
            # Execute the password change command using the temporary password
            echo $changePasswordCmd | & $mysqlCmd -u root -p$tempPassword --connect-expired-password
        } else {
            Write-Log "Could not find temporary password, attempting with --skip-password..." -Level WARN
            $secureInstallCmd = @"
ALTER USER 'root'@'localhost' IDENTIFIED BY '$MySQLRootPassword';
FLUSH PRIVILEGES;
"@
            echo $secureInstallCmd | & $mysqlCmd -u root --skip-password
        }
        
        Write-Log "MySQL installation completed"
    }
    catch {
        Write-Log "MySQL installation failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Function to install Tomcat
function Install-Tomcat {
    Write-Log "Checking Tomcat installation..."
    
    if (Test-Path $TomcatInstallPath) {
        Write-Log "Tomcat already installed at $TomcatInstallPath"
        return
    }
    
    Write-Log "Installing Apache Tomcat $TomcatVersion..."
    
    # Download Tomcat
    $tomcatUrl = "https://archive.apache.org/dist/tomcat/tomcat-9/v$TomcatVersion/bin/apache-tomcat-$TomcatVersion-windows-x64.zip"
    $tomcatZip = "apache-tomcat-$TomcatVersion-windows-x64.zip"
    
    try {
        Invoke-DownloadAndExtract -Url $tomcatUrl -DestinationPath "C:\" -FileName $tomcatZip
        
        # Set environment variables
        [Environment]::SetEnvironmentVariable("CATALINA_HOME", $TomcatInstallPath, "Machine")
        $env:CATALINA_HOME = $TomcatInstallPath
        
        $currentPath = [Environment]::GetEnvironmentVariable("PATH", "Machine")
        if ($currentPath -notlike "*$TomcatInstallPath\bin*") {
            [Environment]::SetEnvironmentVariable("PATH", "$currentPath;$TomcatInstallPath\bin", "Machine")
            $env:PATH = "$env:PATH;$TomcatInstallPath\bin"
        }
        
        # Install as Windows service
        Write-Log "Installing Tomcat as Windows service..."
        Set-Location "$TomcatInstallPath\bin"
        .\service.bat install TomcatPushDemo
        
        # Configure service
        Set-Service -Name "TomcatPushDemo" -StartupType Automatic
        
        Write-Log "Tomcat installation completed"
    }
    catch {
        Write-Log "Tomcat installation failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Function to clone and setup project
function Setup-Project {
    # Check if we are running INSIDE the InstallPath or if we detected we are already in the repo
    if ($InstallPath -eq $PSScriptRoot) {
        Write-Log "Running from installation directory. Skipping clone."
    }
    else {
        Write-Log "Cloning project from $GitUrl..."
        
        # Remove existing directory if it exists
        if (Test-Path $InstallPath) {
            Write-Log "Removing existing installation directory..."
            Remove-Item -Path $InstallPath -Recurse -Force
        }
        
        # Clone repository
        try {
            git clone $GitUrl $InstallPath
            Set-Location $InstallPath
            Write-Log "Project cloned successfully"
        }
        catch {
            Write-Log "Failed to clone repository: $($_.Exception.Message)" "ERROR"
            throw
        }
    }
    
    # Setup Python virtual environment
    Write-Log "Setting up Python virtual environment..."
    
    if (-not (Test-Command "python")) {
        Write-Log "Python not found. Please install Python 3.8+ from https://www.python.org/downloads/" "ERROR"
        throw
    }
    
    try {
        python -m venv $PythonPath
        Write-Log "Python virtual environment created"
        
        # Activate virtual environment and install dependencies
        $activateScript = "$PythonPath\Scripts\Activate.ps1"
        if (Test-Path $activateScript) {
            & $activateScript
        }
        
        # Install Python dependencies if requirements.txt exists
        if (Test-Path "$InstallPath\requirements.txt") {
            pip install -r "$InstallPath\requirements.txt"
            Write-Log "Python dependencies installed"
        }
        elseif (Test-Path "$InstallPath\proxy-api.py") {
            # Install basic dependencies for the proxy API
            pip install flask requests
            Write-Log "Installed Flask and requests for proxy API"
        }
        
    }
    catch {
        Write-Log "Failed to setup Python virtual environment: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Function to setup database
function Setup-Database {
    Write-Log "Setting up database..."
    
    $mysqlCmd = "$MySQLBinPath\mysql.exe"
    
    try {
        # Create database
        $createDbCmd = "CREATE DATABASE IF NOT EXISTS pushdemo DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;"
        echo $createDbCmd | & $mysqlCmd -u root -p$MySQLRootPassword
        
        # Import schema
        if (Test-Path "$InstallPath\doc\pushdemo.sql") {
            $result = & $mysqlCmd -u root -p$MySQLRootPassword pushdemo -e "source $InstallPath\doc\pushdemo.sql" 2>&1
            if ($LASTEXITCODE -ne 0) {
                Write-Log "Failed to import database schema: $($result -join ' ')" "ERROR"
                throw $result
            }
            Write-Log "Database schema imported"
        }
        else {
            Write-Log "Database schema file not found, skipping import" "WARN"
        }
        
        Write-Log "Database setup completed"
    }
    catch {
        Write-Log "Database setup failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Function to configure application
function Configure-Application {
    Write-Log "Configuring application..."
    
    # Update config.xml with MySQL credentials
    $configPath = "$InstallPath\WebContent\WEB-INF\classes\config.xml"
    if (Test-Path $configPath) {
        [xml]$config = Get-Content $configPath
        $config.root.databaseconnect.user = "root"
        $config.root.databaseconnect.password = $MySQLRootPassword
        $config.Save($configPath)
        Write-Log "Updated config.xml with MySQL credentials"
    }
    
    # Copy MySQL connector to Tomcat lib
    $mysqlConnector = "$InstallPath\WebContent\WEB-INF\lib\mysql-connector-java-8.0.33.jar"
    if (Test-Path $mysqlConnector) {
        Copy-Item $mysqlConnector "$TomcatInstallPath\lib\" -Force
        Write-Log "MySQL connector copied to Tomcat lib directory"
    }
    else {
        Write-Log "MySQL connector not found. Downloading..." "WARN"
        try {
            $connectorUrl = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar"
            Invoke-WebRequest -Uri $connectorUrl -OutFile "$TomcatInstallPath\lib\mysql-connector-java-8.0.33.jar" -UseBasicParsing
            Write-Log "MySQL connector downloaded and installed"
        }
        catch {
            Write-Log "Failed to download MySQL connector: $($_.Exception.Message)" "ERROR"
            throw
        }
    }
    
    # Deploy application to Tomcat
    $webappsPath = "$TomcatInstallPath\webapps"
    $appPath = "$webappsPath\pushdemo"
    
    if (Test-Path $appPath) {
        Remove-Item $appPath -Recurse -Force
    }
    
    Copy-Item "$InstallPath\WebContent" $appPath -Recurse -Force
    Write-Log "Application deployed to Tomcat"
    
    Write-Log "Application configuration completed"
}

# Function to create startup scripts
function Create-StartupScripts {
    Write-Log "Creating startup scripts..."
    
    # Create start script
    $startScript = @"
@echo off
echo Starting Push Demo Services...
echo.

echo Starting MySQL...
net start MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo MySQL service started successfully
) else (
    echo MySQL service may already be running or failed to start
)

timeout /t 10 /nobreak >nul

echo Starting Tomcat...
net start TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo Tomcat service started successfully
) else (
    echo Tomcat service may already be running or failed to start
)

echo.
echo Services startup complete!
echo Access the application at: http://localhost:8080/pushdemo
echo.
pause
"@
    
    $startScript | Out-File -FilePath "$InstallPath\start-services.bat" -Encoding ASCII
    
    # Create stop script
    $stopScript = @"
@echo off
echo Stopping Push Demo Services...
echo.

echo Stopping Tomcat...
net stop TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo Tomcat service stopped successfully
) else (
    echo Tomcat service may already be stopped or failed to stop
)

echo Stopping MySQL...
net stop MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo MySQL service stopped successfully
) else (
    echo MySQL service may already be stopped or failed to stop
)

echo.
echo Services shutdown complete!
echo.
pause
"@
    
    $stopScript | Out-File -FilePath "$InstallPath\stop-services.bat" -Encoding ASCII
    
    # Create Python start script
    $pythonStartScript = @"
@echo off
set VIRTUAL_ENV=$PythonPath
set PATH=%VIRTUAL_ENV%\Scripts;%PATH%

if exist "%VIRTUAL_ENV%\Scripts\python.exe" (
    echo Starting Python proxy API...
    cd /d "$InstallPath"
    "%VIRTUAL_ENV%\Scripts\python.exe" proxy-api.py
) else (
    echo Python virtual environment not found. Please run setup again.
    pause
)
"@
    
    $pythonStartScript | Out-File -FilePath "$InstallPath\start-proxy-api.bat" -Encoding ASCII
    
    Write-Log "Startup scripts created"
}

# Function to configure firewall
function Configure-Firewall {
    Write-Log "Configuring Windows Firewall..."
    
    try {
        # Allow Tomcat port
        New-NetFirewallRule -DisplayName "Tomcat Web Server" -Direction Inbound -Protocol TCP -LocalPort 8080 -Action Allow
        Write-Log "Firewall rule for Tomcat (port 8080) created"
        
        # Allow MySQL port (optional - restrict to internal network in production)
        New-NetFirewallRule -DisplayName "MySQL Database" -Direction Inbound -Protocol TCP -LocalPort 3306 -Action Allow
        Write-Log "Firewall rule for MySQL (port 3306) created"
        
        Write-Log "Firewall configuration completed"
    }
    catch {
        Write-Log "Firewall configuration failed: $($_.Exception.Message)" "ERROR"
        Write-Log "Please configure firewall rules manually" "WARN"
    }
}

# Main execution
try {
    Write-Log "Starting setup process..."
    
    # Install prerequisites
    Install-Java
    Install-MySQL
    Install-Tomcat
    
    # Setup project
    Setup-Project
    Setup-Database
    Configure-Application
    
    # Create startup scripts
    Create-StartupScripts
    Configure-Firewall
    
    # Start services
    Write-Log "Starting services..."
    Start-Service -Name "MySQL80" -ErrorAction SilentlyContinue
    Start-Service -Name "TomcatPushDemo" -ErrorAction SilentlyContinue
    
    Write-ColorOutput Green
    Write-Host ""
    Write-Host "=========================================="
    Write-Host "  Setup Completed Successfully!"
    Write-Host "=========================================="
    Write-Host ""
    Write-Host "Installation Path: $InstallPath"
    Write-Host "MySQL Root Password: $MySQLRootPassword"
    Write-Host "Tomcat Path: $TomcatInstallPath"
    Write-Host "Python Virtual Environment: $PythonPath"
    Write-Host ""
    Write-Host "Access the application at: http://localhost:8080/pushdemo"
    Write-Host ""
    Write-Host "Startup Scripts:"
    Write-Host "  - $InstallPath\start-services.bat"
    Write-Host "  - $InstallPath\stop-services.bat"
    Write-Host "  - $InstallPath\start-proxy-api.bat"
    Write-Host ""
    Write-Host "Log file: $LogPath"
    Write-Host "=========================================="
    Write-Host ""
}
catch {
    Write-ColorOutput Red
    Write-Host ""
    Write-Host "=========================================="
    Write-Host "  Setup Failed!"
    Write-Host "=========================================="
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Please check the log file: $LogPath"
    Write-Host "=========================================="
    Write-Host ""
    exit 1
}
finally {
    # Cleanup temp files
    if (Test-Path $TempPath) {
        Remove-Item -Path $TempPath -Recurse -Force -ErrorAction SilentlyContinue
    }
    
    Stop-Transcript
}
