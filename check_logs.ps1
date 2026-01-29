<#
.SYNOPSIS
    Reads the latest Tomcat logs to debug startup errors.
.EXAMPLE
    .\check_logs.ps1 -TomcatHome "C:\apache-tomcat-9.0.84"
#>
param(
    [string]$TomcatHome = "C:\apache-tomcat-9.0.84"
)

$LogDir = "$TomcatHome\logs"
if (-not (Test-Path $LogDir)) {
    Write-Error "Log directory not found: $LogDir"
    exit
}

# Get latest stdout/catalina/localhost logs
$LogFiles = Get-ChildItem -Path $LogDir -Filter "*2026*.log" -File | Sort-Object LastWriteTime -Descending | Select-Object -First 3

foreach ($file in $LogFiles) {
    Write-Host "`n--- LOG FILE: $($file.Name) ---" -ForegroundColor Cyan
    Get-Content $file.FullName -Tail 50
    Write-Host "-----------------------------" -ForegroundColor Cyan
}

# Also check logs inside subfolders if any (sometimes in newer Tomcats)
