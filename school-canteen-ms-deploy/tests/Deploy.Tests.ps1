# filepath: /school-canteen-ms-deploy/school-canteen-ms-deploy/tests/Deploy.Tests.ps1

# This file contains tests for the deployment scripts, ensuring that the deployment process works as expected.

function Test-Deployment {
    param (
        [string]$DeployScriptPath
    )

    # Check if the deployment script exists
    if (-Not (Test-Path $DeployScriptPath)) {
        Write-Host "Deployment script not found: $DeployScriptPath" -ForegroundColor Red
        return $false
    }

    # Execute the deployment script
    & $DeployScriptPath

    # Check the exit code
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Deployment script executed successfully." -ForegroundColor Green
        return $true
    } else {
        Write-Host "Deployment script failed with exit code: $LASTEXITCODE" -ForegroundColor Red
        return $false
    }
}

# Test the deployment
$deployScript = "..\scripts\deploy.ps1"
$deploymentResult = Test-Deployment -DeployScriptPath $deployScript

if ($deploymentResult) {
    Write-Host "All deployment tests passed." -ForegroundColor Green
} else {
    Write-Host "Some deployment tests failed." -ForegroundColor Red
}