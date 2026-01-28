#requires -version 5.1
<#
.SYNOPSIS
    Database connection test script for Push Demo application
.DESCRIPTION
    Tests MySQL database connectivity and verifies configuration settings.
    Checks connection parameters, database existence, and basic queries.
.PARAMETER ConfigPath
    Path to config.xml file (defaults to standard location)
.PARAMETER Verbose
    Show detailed connection information
.EXAMPLE
    .\test-database-connection.ps1
    .\test-database-connection.ps1 -Verbose
    .\test-database-connection.ps1 -ConfigPath "C:\custom\path\config.xml"
.NOTES
    Author: Generated for Push Demo Project
    Requires: PowerShell 5.1+
#>

param(
    [string]$ConfigPath = "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml",
    [switch]$Verbose
)

# Colors for output
function Write-ColorOutput($ForegroundColor, $Message) {
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    Write-Host $Message
    $host.UI.RawUI.ForegroundColor = "White"
}

function Test-DatabaseConnection {
    param(
        [string]$ConnectionString,
        [string]$Username,
        [string]$Password,
        [string]$Database
    )
    
    try {
        # Load MySQL .NET connector if available
        Add-Type -AssemblyName System.Data
        
        # Create connection string
        $builder = New-Object System.Data.Common.DbConnectionStringBuilder
        $builder.ConnectionString = $ConnectionString
        
        # Test basic connection
        $connection = New-Object System.Data.SqlClient.SqlConnection($ConnectionString)
        $connection.Open()
        
        Write-ColorOutput Green "✓ Database connection successful"
        
        # Test specific database access
        $command = $connection.CreateCommand()
        $command.CommandText = "SELECT DATABASE(), VERSION(), CONNECTION_ID()"
        $reader = $command.ExecuteReader()
        
        if ($reader.Read()) {
            $dbName = $reader.GetString(0)
            $version = $reader.GetString(1)
            $connId = $reader.GetInt32(2)
            
            Write-ColorOutput Green "✓ Connected to database: $dbName"
            Write-ColorOutput Green "✓ MySQL Version: $version"
            Write-ColorOutput Green "✓ Connection ID: $connId"
        }
        $reader.Close()
        
        # Test basic table query
        $command.CommandText = "SHOW TABLES LIKE 'user_info'"
        $reader = $command.ExecuteReader()
        if ($reader.HasRows) {
            Write-ColorOutput Green "✓ Required tables exist"
        } else {
            Write-ColorOutput Yellow "⚠ Required tables may be missing"
        }
        $reader.Close()
        
        $connection.Close()
        return $true
    }
    catch {
        Write-ColorOutput Red "✗ Database connection failed: $($_.Exception.Message)"
        return $false
    }
}

# Main execution
Write-ColorOutput Green "=========================================="
Write-ColorOutput Green "  Database Connection Test"
Write-ColorOutput Green "=========================================="
Write-Host ""

# Check if config file exists
if (-not (Test-Path $ConfigPath)) {
    Write-ColorOutput Red "✗ Config file not found: $ConfigPath"
    Write-Host "Please specify the correct path to config.xml"
    exit 1
}

Write-ColorOutput Cyan "Reading configuration from: $ConfigPath"
Write-Host ""

# Parse config.xml
try {
    [xml]$config = Get-Content $ConfigPath
    
    $driver = $config.root.databaseconnect.driverclass
    $url = $config.root.databaseconnect.url
    $username = $config.root.databaseconnect.user
    $password = $config.root.databaseconnect.password
    
    Write-ColorOutput Green "Configuration loaded successfully"
    Write-Host ""
    
    if ($Verbose) {
        Write-Host "Driver: $driver"
        Write-Host "URL: $url" 
        Write-Host "Username: $username"
        Write-Host "Password: $($password.Substring(0, [Math]::Min(3, $password.Length)))***"
        Write-Host ""
    }
    
    # Extract database name from URL
    if ($url -match "jdbc:mysql://[^/]+/([^?]+)") {
        $database = $matches[1]
        Write-ColorOutput Cyan "Testing connection to database: $database"
        Write-Host ""
        
        # Convert JDBC URL to .NET connection string format
        $netUrl = $url -replace "jdbc:mysql://", "" -replace "\\?.*$", ""
        $serverPart = $netUrl -replace "/.*$", ""
        $dbName = $netUrl -replace "^[^/]+/", ""
        
        $connectionString = "Server=$serverPart;Database=$dbName;Uid=$username;Pwd=$password;Allow User Variables=True;SslMode=None"
        
        # Test connection
        $result = Test-DatabaseConnection -ConnectionString $connectionString -Username $username -Password $password -Database $database
        
        Write-Host ""
        if ($result) {
            Write-ColorOutput Green "=========================================="
            Write-ColorOutput Green "  Database Connection Test PASSED"
            Write-ColorOutput Green "=========================================="
            Write-Host ""
            Write-Host "Your database configuration is working correctly."
            Write-Host "The Push Demo application should be able to connect."
        } else {
            Write-ColorOutput Red "=========================================="
            Write-ColorOutput Red "  Database Connection Test FAILED"
            Write-ColorOutput Red "=========================================="
            Write-Host ""
            Write-Host "Please check your database configuration:"
            Write-Host "1. Verify MySQL service is running"
            Write-Host "2. Check database credentials in config.xml"
            Write-Host "3. Ensure database 'pushdemo' exists"
            Write-Host "4. Verify MySQL is accessible on localhost:3306"
        }
    } else {
        Write-ColorOutput Red "✗ Could not parse database name from URL: $url"
    }
}
catch {
    Write-ColorOutput Red "✗ Failed to read or parse config.xml: $($_.Exception.Message)"
    exit 1
}

Write-Host ""
Write-ColorOutput Gray "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")