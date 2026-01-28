# Database Connection Test Script for Push Demo
# Tests MySQL connectivity and configuration

param(
    [string]$ConfigPath = "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml"
)

function Write-Status {
    param([string]$Message, [string]$Type = "INFO")
    
    switch ($Type) {
        "SUCCESS" { Write-Host "✓ $Message" -ForegroundColor Green }
        "ERROR" { Write-Host "✗ $Message" -ForegroundColor Red }
        "WARNING" { Write-Host "⚠ $Message" -ForegroundColor Yellow }
        "INFO" { Write-Host "  $Message" -ForegroundColor Cyan }
        default { Write-Host "  $Message" }
    }
}

Write-Host "==========================================" -ForegroundColor Green
Write-Host "  Database Connection Test" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host ""

# Check config file
if (-not (Test-Path $ConfigPath)) {
    Write-Status "Config file not found: $ConfigPath" "ERROR"
    Write-Host "Please check the path and try again."
    exit 1
}

Write-Status "Config file found" "SUCCESS"
Write-Host ""

try {
    # Parse config.xml
    [xml]$config = Get-Content $ConfigPath
    $url = $config.root.databaseconnect.url
    $username = $config.root.databaseconnect.user
    $password = $config.root.databaseconnect.password
    
    Write-Status "Configuration loaded successfully" "SUCCESS"
    Write-Host ""
    
    # Extract database name
    if ($url -match "jdbc:mysql://[^/]+/([^?]+)") {
        $database = $matches[1]
        Write-Status "Testing connection to database: $database" "INFO"
        Write-Host ""
        
        # Test MySQL connection
        $mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
        
        if (Test-Path $mysqlPath) {
            Write-Status "Testing with MySQL command line..." "INFO"
            
            # Test basic connection
            $testCmd = "& `"$mysqlPath`" -u $username -p$password -e `"SELECT 'Connection successful' as status, DATABASE() as current_db;`" $database 2>`$null"
            $result = Invoke-Expression $testCmd
            
            if ($LASTEXITCODE -eq 0) {
                Write-Status "Database connection successful" "SUCCESS"
                Write-Status "Connected to database: $database" "SUCCESS"
                
                # Test table existence
                $tableCmd = "& `"$mysqlPath`" -u $username -p$password -e `"SHOW TABLES LIKE 'user_info';`" $database 2>`$null"
                $tableResult = Invoke-Expression $tableCmd
                
                if ($tableResult -and ($tableResult -join "`n") -match "user_info") {
                    Write-Status "Required tables exist" "SUCCESS"
                } else {
                    Write-Status "Required tables may be missing" "WARNING"
                }
                
                Write-Host ""
                Write-Host "==========================================" -ForegroundColor Green
                Write-Host "  Database Connection Test PASSED" -ForegroundColor Green
                Write-Host "==========================================" -ForegroundColor Green
                Write-Host ""
                Write-Host "Your database configuration is working correctly."
                Write-Host "The Push Demo application should be able to connect."
            } else {
                throw "Database connection failed"
            }
        } else {
            Write-Status "MySQL command line tool not found at: $mysqlPath" "ERROR"
            Write-Host "Please verify MySQL installation path."
            exit 1
        }
    } else {
        Write-Status "Could not parse database name from URL: $url" "ERROR"
        exit 1
    }
}
catch {
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Red
    Write-Host "  Database Connection Test FAILED" -ForegroundColor Red
    Write-Host "==========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host ""
    Write-Host "Please check your database configuration:"
    Write-Host "1. Verify MySQL service is running (net start MySQL80)"
    Write-Host "2. Check database credentials in config.xml"
    Write-Host "3. Ensure database exists"
    Write-Host "4. Verify MySQL is accessible on localhost:3306"
}

Write-Host ""
Write-Host "Press any key to exit..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")