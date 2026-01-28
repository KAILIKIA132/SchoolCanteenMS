@echo off
title Check Device Info Table Structure
echo ==========================================
echo   Check Device Info Table Structure
echo ==========================================
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Checking device_info table structure:
echo.

%MYSQL_PATH% -u root -pCanteen@2026 -e "DESCRIBE device_info;" pushdemo 2>nul
echo.

echo Checking if any devices already exist:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT * FROM device_info;" pushdemo 2>nul
echo.

echo Testing a simple insert:
%MYSQL_PATH% -u root -pCanteen@2026 -e "INSERT INTO device_info (device_sn, device_name) VALUES ('TEST123', 'Test Device') ON DUPLICATE KEY UPDATE device_name='Test Device Updated';" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo ✓ Test insert successful
    echo Checking if test record exists:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT device_sn, device_name FROM device_info WHERE device_sn = 'TEST123';" pushdemo 2>nul
    echo.
    echo Cleaning up test record:
    %MYSQL_PATH% -u root -pCanteen@2026 -e "DELETE FROM device_info WHERE device_sn = 'TEST123';" pushdemo 2>nul
) else (
    echo ✗ Test insert failed
)
echo.

echo Press any key to exit...
pause >nul