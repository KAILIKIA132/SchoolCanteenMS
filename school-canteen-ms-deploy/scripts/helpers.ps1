function Print-Msg {
    param([string]$msg, [string]$color="Cyan")
    Write-Host -ForegroundColor $color "[$((Get-Date).ToString('HH:mm:ss'))] $msg"
}

function Check-Path {
    param([string]$path)
    if (-not (Test-Path $path)) {
        Print-Msg "Path does not exist: $path" "Red"
        return $false
    }
    return $true
}

function Install-Dependency {
    param([string]$dependency)
    Print-Msg "Installing dependency: $dependency" "Yellow"
    # Placeholder for actual installation logic
}

function Restart-Service {
    param([string]$serviceName)
    Print-Msg "Restarting service: $serviceName" "Yellow"
    # Placeholder for actual service restart logic
}