$ErrorActionPreference = "Stop"

$TomcatHome = "C:\apache-tomcat-9.0.84"
$MySQLRootPassword = "Canteen@2026"
$FixSql = "$PSScriptRoot\sql\fix_stored_procedure.sql"

# Auto-detect MySQL
$MySqlExe = "mysql"
try { Get-Command mysql -ErrorAction Stop | Out-Null } catch {
    $CommonPaths = @(
        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
        "C:\Program Files\MySQL\MySQL Server 8.1\bin\mysql.exe"
    )
    foreach ($p in $CommonPaths) {
        if (Test-Path $p) { $MySqlExe = $p; break }
    }
}

Write-Host "--- APPLYING STORED PROCEDURE FIX ---" -ForegroundColor Yellow

if ($MySqlExe -match " ") {
    & "$MySqlExe" -u root "-p$MySQLRootPassword" -P 3306 < "$FixSql"
} else {
    & $MySqlExe -u root "-p$MySQLRootPassword" -P 3306 < "$FixSql"
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "Success! Stored Procedure Updated." -ForegroundColor Green
} else {
    Write-Error "Failed to update stored procedure."
}
