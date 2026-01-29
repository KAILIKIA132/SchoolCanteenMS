$ErrorActionPreference = "Stop"
$TomcatHome = "C:\apache-tomcat-9.0.84"
$ServiceName = "TomcatPushDemo"

Write-Host "--- RESTARTING TOMCAT & CLEARING CACHE ---" -ForegroundColor Yellow

# 1. Stop Tomcat
Write-Host "Stopping $ServiceName..."
Stop-Service -Name $ServiceName -Force
Start-Sleep -Seconds 5

# 2. Clear Work Directory (JSP Cache)
$WorkDir = "$TomcatHome\work\Catalina\localhost\pushdemo"
if (Test-Path $WorkDir) {
    Write-Host "Deleting JSP cache at: $WorkDir"
    Remove-Item -Path $WorkDir -Recurse -Force
}

# 3. Start Tomcat
Write-Host "Starting $ServiceName..."
Start-Service -Name $ServiceName
Start-Sleep -Seconds 10

Write-Host "Tomcat restarted and cache cleared." -ForegroundColor Green
