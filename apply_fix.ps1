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
        if (Test-Path $p) { $MySqlExe = "$p"; break } # No extra quotes here
    }
}

Write-Host "--- APPLYING STORED PROCEDURE FIX ---" -ForegroundColor Yellow

# Use cmd.exe /S /C "..." to handle inner quotes correctly
# Format: " "Param 1" "Param 2" < "File" "
$CmdArg = "/S /C `"`"$MySqlExe`" -u root -p$MySQLRootPassword -P 3306 pushdemo < `"$FixSql`"`""

Start-Process "cmd.exe" -ArgumentList $CmdArg -Wait -NoNewWindow

if ($LASTEXITCODE -eq 0) {
    Write-Host "Success! Stored Procedure Updated." -ForegroundColor Green
} else {
    Write-Error "Failed to update stored procedure."
}
