@echo off
title Check Database Content
echo ==========================================
echo   Database Content Check
echo ==========================================
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Checking database content with direct connection...
echo.

echo Testing connection to pushdemo database:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT 'Connection OK' as status, VERSION() as mysql_version;" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Database Connection Successful
    echo ==========================================
    echo.
    
    echo Checking tables in pushdemo database:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "SHOW TABLES;" pushdemo 2>nul
    echo.
    
    echo Checking device table:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT COUNT(*) as device_count FROM device_info;" pushdemo 2>nul
    echo.
    
    echo Checking user table:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT COUNT(*) as user_count FROM user_info;" pushdemo 2>nul
    echo.
    
    echo Checking if devices exist:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT device_sn, device_name, state, last_activity FROM device_info LIMIT 5;" pushdemo 2>nul
    echo.
    
) else (
    echo.
    echo ==========================================
    echo   Database Connection Failed
    echo ==========================================
    echo.
    echo Could not connect to pushdemo database.
    echo Please verify:
    echo 1. MySQL service is running
    echo 2. Credentials are correct
    echo 3. Database 'pushdemo' exists
    echo.
)

echo Press any key to exit...
pause >nul