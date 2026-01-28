@echo off
title Database Connection Test
echo ==========================================
echo   Database Connection Test
echo ==========================================
echo.

set CONFIG_PATH=C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml
set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Checking if config file exists...
if not exist "%CONFIG_PATH%" (
    echo ✗ Config file not found: %CONFIG_PATH%
    echo Please check the path and try again.
    goto :end
)

echo ✓ Config file found
echo.

echo Parsing database configuration...
for /f "tokens=3 delims=<>" %%a in ('findstr "url" "%CONFIG_PATH%"') do set DB_URL=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "user" "%CONFIG_PATH%"') do set DB_USER=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "password" "%CONFIG_PATH%"') do set DB_PASS=%%a

echo Database URL: %DB_URL%
echo Username: %DB_USER%
echo Password: ***%DB_PASS:~3%

echo.
echo Testing MySQL command line availability...
if not exist %MYSQL_PATH% (
    echo ✗ MySQL command line tool not found at: %MYSQL_PATH%
    echo Please verify MySQL installation path.
    goto :end
)

echo ✓ MySQL command line tool found

echo.
echo Testing database connection...
echo.

REM Extract database name from URL
for /f "tokens=4 delims=/" %%a in ("%DB_URL%") do set DB_NAME=%%a
for /f "tokens=1 delims=?" %%a in ("%DB_NAME%") do set DB_NAME=%%a

echo Testing connection to database: %DB_NAME%
echo.

%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SELECT 'Connection successful' as Status, DATABASE() as Current_Database, VERSION() as MySQL_Version;" %DB_NAME% 2>nul

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Database Connection Test PASSED
    echo ==========================================
    echo.
    echo ✓ Database connection successful
    echo ✓ Configuration is working correctly
    echo ✓ Push Demo application should connect properly
) else (
    echo.
    echo ==========================================
    echo   Database Connection Test FAILED
    echo ==========================================
    echo.
    echo ✗ Database connection failed
    echo.
    echo Please check:
    echo 1. MySQL service is running (net start MySQL80)
    echo 2. Database credentials in config.xml are correct
    echo 3. Database '%DB_NAME%' exists
    echo 4. MySQL is accessible on localhost:3306
    echo.
    echo You can test manually with:
    echo %MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME%
)

:end
echo.
echo Press any key to exit...
pause >nul