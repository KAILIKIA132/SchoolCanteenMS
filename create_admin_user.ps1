<#
.SYNOPSIS
    Creates a new Admin User in the Push Demo database.

.DESCRIPTION
    Prompts for a username and password, hashes the password with SHA-256 (matching the Java app),
    and inserts the user into the database.

.PARAMETER MySQLRootPassword
    Password for the MySQL 'root' user.

.EXAMPLE
    .\create_admin_user.ps1
#>

param(
    [string]$MySQLRootPassword
)

# 1. Prompt for Inputs if not provided
if ([string]::IsNullOrWhiteSpace($MySQLRootPassword)) {
    $MySQLRootPassword = Read-Host "Enter MySQL Root Password" -AsSecureString
    $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($MySQLRootPassword)
    $MySQLRootPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)
}

$NewUsername = ReadHost "Enter New Admin Username"
if ([string]::IsNullOrWhiteSpace($NewUsername)) { Write-Error "Username cannot be empty"; exit 1 }

$NewPassword = Read-Host "Enter New Admin Password" -AsSecureString
$BSTR_Pass = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($NewPassword)
$PlainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR_Pass)

if ([string]::IsNullOrWhiteSpace($PlainPassword)) { Write-Error "Password cannot be empty"; exit 1 }

# 2. Compute SHA-256 Hash to match Java's SecurityUtil
# Java Implementation:
# MessageDigest.getInstance("SHA-256")
# Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1) -> effectively hex string
function Get-Sha256Hash {
    param($String)
    $StringBuilder = New-Object System.Text.StringBuilder
    [System.Security.Cryptography.HashAlgorithm]::Create("SHA256").ComputeHash([System.Text.Encoding]::UTF8.GetBytes($String)) | ForEach-Object {
        [Void]$StringBuilder.Append($_.ToString("x2"))
    }
    return $StringBuilder.ToString()
}

$HashedPassword = Get-Sha256Hash -String $PlainPassword
Write-Host "Generated Hash: $HashedPassword" -ForegroundColor Gray

# 3. Locate MySQL
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

# 4. Insert into Database
$Query = "INSERT INTO admin_users (username, password, role) VALUES ('$NewUsername', '$HashedPassword', 'ADMIN');"

Write-Host "Creating user '$NewUsername'..." -ForegroundColor Cyan

try {
    # Use cmd /c for reliable redirection/piping
    cmd /c "$MySqlExe -u root -p$MySQLRootPassword -P 3306 pushdemo -e `"$Query`""
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "User '$NewUsername' created successfully!" -ForegroundColor Green
    } else {
        Write-Error "Failed to create user. Username '$NewUsername' might already exist."
    }
} catch {
    Write-Error "Database connection failed."
}
