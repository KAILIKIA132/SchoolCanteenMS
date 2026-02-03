# filepath: /school-canteen-ms-deploy/school-canteen-ms-deploy/scripts/deploy.ps1

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

Print-Msg "Starting Deployment Process..." "Green"

# Step 1: Install Dependencies
Print-Msg "Installing Dependencies..." "Gray"
& "$ProjectPath\scripts\install-dependencies.ps1"

# Step 2: Update Existing Installation
Print-Msg "Updating Existing Installation..." "Gray"
& "$ProjectPath\scripts\update_existing_install.ps1" -TomcatHome $TomcatHome -MySQLRootPassword $MySQLRootPassword -ProjectPath $ProjectPath

# Step 3: Additional Deployment Logic (if needed)
# Add any additional deployment steps here

Print-Msg "Deployment Complete! Please verify the installation." "Green"