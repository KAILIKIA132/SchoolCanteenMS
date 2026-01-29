$ErrorActionPreference = "Stop"

$TomcatHome = "C:\apache-tomcat-9.0.84"
$MySQLRootPassword = "Canteen@2026"

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

Write-Host "--- CHECKING STORED PROCEDURE ---" -ForegroundColor Yellow
# Use Call Operator & for safer execution with spaces in paths
if ($MySqlExe -match " ") {
    & "$MySqlExe" -u root "-p$MySQLRootPassword" -P 3306 -t -e "SHOW CREATE PROCEDURE get_device_for_page;" pushdemo
} else {
    & $MySqlExe -u root "-p$MySQLRootPassword" -P 3306 -t -e "SHOW CREATE PROCEDURE get_device_for_page;" pushdemo
}

Write-Host "`n--- CHECKING TABLES ---" -ForegroundColor Yellow
$QueryTables = "SHOW TABLES;"
if ($MySqlExe -match " ") {
    & "$MySqlExe" -u root "-p$MySQLRootPassword" -P 3306 -t -e "$QueryTables" pushdemo
} else {
    & $MySqlExe -u root "-p$MySQLRootPassword" -P 3306 -t -e "$QueryTables" pushdemo
}

Write-Host "`n--- CHECKING DEPARTMENT (If Exists) ---" -ForegroundColor Yellow
$QueryDept = "SELECT * FROM department_info;" # Guessing table name based on common ZK schemas, might fail
if ($MySqlExe -match " ") {
    & "$MySqlExe" -u root "-p$MySQLRootPassword" -P 3306 -t -v -e "$QueryDept" pushdemo 2>&1
} else {
    & $MySqlExe -u root "-p$MySQLRootPassword" -P 3306 -t -v -e "$QueryDept" pushdemo 2>&1
}
