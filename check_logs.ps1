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

# Get latest localhost log specifically (usually contains the stack trace for Filter failures)
$LocalhostLog = Get-ChildItem -Path $LogDir -Filter "localhost.*.log" -File | Sort-Object LastWriteTime -Descending | Select-Object -First 1

if ($LocalhostLog) {
    Write-Host "`n--- LOG FILE: $($LocalhostLog.Name) ---" -ForegroundColor Yellow
    Get-Content $LocalhostLog.FullName -Tail 100
    Write-Host "-----------------------------" -ForegroundColor Yellow
}

# Get other recent logs
$OtherLogs = Get-ChildItem -Path $LogDir -Filter "*2026*.log" -File | Where-Object { $_.Name -notlike "localhost*" } | Sort-Object LastWriteTime -Descending | Select-Object -First 2

foreach ($file in $OtherLogs) {
    Write-Host "`n--- LOG FILE: $($file.Name) ---" -ForegroundColor Cyan
    Get-Content $file.FullName -Tail 50
    Write-Host "-----------------------------" -ForegroundColor Cyan
}

# Also check logs inside subfolders if any (sometimes in newer Tomcats)
