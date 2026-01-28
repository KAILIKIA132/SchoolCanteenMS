#requires -version 5.1
<#
.SYNOPSIS
    Database connection test script for Push Demo application
.DESCRIPTION
    Tests MySQL database connectivity and verifies configuration settings.
.PARAMETER ConfigPath
    Path to config.xml file (defaults to standard location)
.PARAMETER Verbose
    Show detailed connection information
.EXAMPLE
    .\test-database-connection.ps1
    .\test-database-connection.ps1 -Verbose
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
        
        # Test MySQL connection using command line
        $mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
        
        if (Test-Path $mysqlPath) {
            Write-ColorOutput Cyan "Testing with MySQL command line..."
            
            # Test basic connection
            $testResult = & $mysqlPath -u $username -p$password -e "SELECT 'Connection successful' as status, DATABASE() as current_db;" $database 2>$null
            
            if ($LASTEXITCODE -eq 0) {
                Write-ColorOutput Green "✓ Database connection successful"
                Write-ColorOutput Green "✓ Connected to database: $database"
                
                # Test table existence
                $tableResult = & $mysqlPath -u $username -p$password -e "SHOW TABLES LIKE 'user_info';" $database 2>$null
                if ($tableResult -and $tableResult -match "user_info") {
                    Write-ColorOutput Green "✓ Required tables exist"
                } else {
                    Write-ColorOutput Yellow "⚠ Required tables may be missing"
                }
                
                Write-Host ""
                Write-ColorOutput Green "=========================================="
                Write-ColorOutput Green "  Database Connection Test PASSED"
                Write-ColorOutput Green "=========================================="
                Write-Host ""
                Write-Host "Your database configuration is working correctly."
                Write-Host "The Push Demo application should be able to connect."
            } else {
                Write-ColorOutput Red "✗ Database connection failed"
                throw "Connection test failed"
            }
        } else {
            Write-ColorOutput Red "✗ MySQL command line tool not found at: $mysqlPath"
            Write-Host "Please verify MySQL installation path."
            exit 1
        }
    } else {
        Write-ColorOutput Red "✗ Could not parse database name from URL: $url"
    }
}
catch {
    Write-Host ""
    Write-ColorOutput Red "=========================================="
    Write-ColorOutput Red "  Database Connection Test FAILED"
    Write-ColorOutput Red "=========================================="
    Write-Host ""
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host ""
    Write-Host "Please check your database configuration:"
    Write-Host "1. Verify MySQL service is running"
    Write-Host "2. Check database credentials in config.xml"
    Write-Host "3. Ensure database '$database' exists"
    Write-Host "4. Verify MySQL is accessible on localhost:3306"
}

Write-Host ""
Write-ColorOutput Gray "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")