param(
    [string]$MySQLRootPassword
)

if (-not $MySQLRootPassword) {
    $MySQLRootPassword = Read-Host "Enter MySQL Root Password"
}

$Query = "SELECT id, username, password FROM admin_users;"
$Cmd = "mysql -u root -p$MySQLRootPassword pushdemo -e `"$Query`""

Write-Host "Checking 'admin_users' table..." -ForegroundColor Cyan
try {
    cmd /c $Cmd
} catch {
    Write-Error "Failed to query database. Check password."
}
