#requires -version 5.1
<#
.SYNOPSIS
    Restart script for Push Demo Services on Windows Server
.DESCRIPTION
    This script safely stops and restarts MySQL and Tomcat services for the Push Demo application.
    Provides detailed status feedback and error handling.
.PARAMETER Force
    Force restart even if services appear to be running
.EXAMPLE
    .\restart-services.ps1
    .\restart-services.ps1 -Force
.NOTES
    Author: Generated for Push Demo Project
    Requires: PowerShell 5.1+, Administrator privileges
#>

param(
    [switch]$Force
)

# Ensure running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Error "This script must be run as Administrator. Please run PowerShell as Administrator and try again."
    exit 1
}

# Colors for output
function Write-ColorOutput($ForegroundColor, $Message) {
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    Write-Host $Message
    $host.UI.RawUI.ForegroundColor = "White"
}

# Global variables
$InstallPath = "C:\Meal_Management\SchoolCanteenMS"
$LogPath = "$InstallPath\setup.log"

# Start logging
if (Test-Path $LogPath) {
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Add-Content -Path $LogPath -Value "[$timestamp] [INFO] Restart script initiated"
}

Write-ColorOutput Green "=========================================="
Write-ColorOutput Green "  Push Demo Services Restart Script"
Write-ColorOutput Green "=========================================="
Write-Host ""

Write-ColorOutput Yellow "Stopping Push Demo Services..."
Write-Host ""

# Stop MySQL service
Write-ColorOutput Cyan "Stopping MySQL service..."
try {
    $mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
    if ($mysqlService -and $mysqlService.Status -eq "Running") {
        Stop-Service -Name "MySQL80" -Force
        Write-ColorOutput Green "✓ MySQL service stopped successfully"
        if (Test-Path $LogPath) {
            Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [INFO] MySQL service stopped"
        }
    } else {
        Write-ColorOutput Yellow "⚠ MySQL service is not running or not installed"
    }
} catch {
    Write-ColorOutput Red "✗ Failed to stop MySQL service: $($_.Exception.Message)"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [ERROR] Failed to stop MySQL service: $($_.Exception.Message)"
    }
}

# Stop Tomcat service
Write-ColorOutput Cyan "Stopping Tomcat service..."
try {
    $tomcatService = Get-Service -Name "TomcatPushDemo" -ErrorAction SilentlyContinue
    if ($tomcatService -and $tomcatService.Status -eq "Running") {
        Stop-Service -Name "TomcatPushDemo" -Force
        Write-ColorOutput Green "✓ Tomcat service stopped successfully"
        if (Test-Path $LogPath) {
            Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [INFO] Tomcat service stopped"
        }
    } else {
        Write-ColorOutput Yellow "⚠ Tomcat service is not running or not installed"
    }
} catch {
    Write-ColorOutput Red "✗ Failed to stop Tomcat service: $($_.Exception.Message)"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [ERROR] Failed to stop Tomcat service: $($_.Exception.Message)"
    }
}

Write-Host ""
Write-ColorOutput Yellow "Waiting 5 seconds for services to fully stop..."
Start-Sleep -Seconds 5

Write-Host ""
Write-ColorOutput Yellow "Starting Push Demo Services..."
Write-Host ""

# Start MySQL service
Write-ColorOutput Cyan "Starting MySQL service..."
try {
    Start-Service -Name "MySQL80" -ErrorAction Stop
    Write-ColorOutput Green "✓ MySQL service started successfully"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [INFO] MySQL service started"
    }
} catch {
    Write-ColorOutput Red "✗ Failed to start MySQL service: $($_.Exception.Message)"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [ERROR] Failed to start MySQL service: $($_.Exception.Message)"
    }
}

# Wait for MySQL to be ready
Write-ColorOutput Yellow "Waiting 10 seconds for MySQL to initialize..."
Start-Sleep -Seconds 10

# Start Tomcat service
Write-ColorOutput Cyan "Starting Tomcat service..."
try {
    Start-Service -Name "TomcatPushDemo" -ErrorAction Stop
    Write-ColorOutput Green "✓ Tomcat service started successfully"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [INFO] Tomcat service started"
    }
} catch {
    Write-ColorOutput Red "✗ Failed to start Tomcat service: $($_.Exception.Message)"
    if (Test-Path $LogPath) {
        Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [ERROR] Failed to start Tomcat service: $($_.Exception.Message)"
    }
}

Write-Host ""
Write-ColorOutput Green "=========================================="
Write-ColorOutput Green "  Services Restart Complete!"
Write-ColorOutput Green "=========================================="
Write-Host ""
Write-Host "Application should be available at:" -ForegroundColor White
Write-ColorOutput Green "http://localhost:8080/pushdemo"
Write-Host ""
Write-Host "Service Status:" -ForegroundColor White
Get-Service -Name "MySQL80", "TomcatPushDemo" | Format-Table -Property Name, Status, StartType
Write-Host ""
Write-Host "Log file location:" -ForegroundColor White
Write-ColorOutput Gray "$LogPath"
Write-Host ""
Write-ColorOutput Gray "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Final log entry
if (Test-Path $LogPath) {
    Add-Content -Path $LogPath -Value "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] [INFO] Restart script completed"
}