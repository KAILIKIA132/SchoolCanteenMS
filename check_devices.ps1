$ErrorActionPreference = "Stop"

$TomcatHome = "C:\apache-tomcat-9.0.84"
$MySQLRootPassword = "Canteen@2026"  # Default from your previous inputs

# Auto-detect MySQL
$MySqlExe = "mysql"
try { Get-Command mysql -ErrorAction Stop | Out-Null } catch {
    $CommonPaths = @(
        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
        "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe"
    )
    foreach ($p in $CommonPaths) {
        if (Test-Path $p) { $MySqlExe = "`"$p`""; break }
    }
}

Write-Host "--- CHECKING DEVICE_INFO TABLE ---" -ForegroundColor Yellow

$Query = "SELECT device_sn, device_name, alias_name, state, dept_id, ipaddress FROM device_info;"
$Cmd = "$MySqlExe -u root -p$MySQLRootPassword -P 3306 -t -e `"$Query`" pushdemo"

Invoke-Expression $Cmd

Write-Host "`n--- CHECKING TOMCAT LOGS FOR ERRORS ---" -ForegroundColor Yellow
$LogFile = "$TomcatHome\logs\localhost.$((Get-Date).ToString('yyyy-MM-dd')).log"
if (Test-Path $LogFile) {
    Get-Content $LogFile -Tail 20
} else {
    Write-Warning "Log file not found: $LogFile"
}
