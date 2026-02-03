# filepath: /school-canteen-ms-deploy/school-canteen-ms-deploy/scripts/install-dependencies.ps1

# This script installs necessary dependencies for the School Canteen Management System project.

$ErrorActionPreference = "Stop"

function Print-Msg {
    param([string]$msg, [string]$color="Cyan")
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

Print-Msg "Starting Dependency Installation..." "Green"

# Check for Chocolatey (Windows package manager)
if (-not (Get-Command choco -ErrorAction SilentlyContinue)) {
    Print-Msg "Chocolatey not found. Installing Chocolatey..." "Yellow"
    Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))
}

# Install required packages
$packages = @(
    "git",
    "mysql",
    "jdk8"
)

foreach ($package in $packages) {
    Print-Msg "Installing $package..." "Gray"
    choco install $package -y
}

Print-Msg "All dependencies installed successfully." "Green"