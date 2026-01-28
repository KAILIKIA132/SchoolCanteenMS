@echo off
title Add ZK Device to Database
echo ==========================================
echo   Add ZK Device to Database
echo ==========================================
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Please enter your ZK device information:
echo.

set /p DEVICE_SN="Device Serial Number: "
set /p DEVICE_NAME="Device Name (press Enter for default): "
if "%DEVICE_NAME%"=="" set DEVICE_NAME=ZK_Device_%RANDOM%

echo.
echo Adding device to database...
echo.

echo Database: pushdemo
echo Device SN: %DEVICE_SN%
echo Device Name: %DEVICE_NAME%
echo.

REM Insert device into database using direct connection
%MYSQL_PATH% -u root -pCanteen@2026 -e "INSERT INTO device_info (device_sn, device_name, alias_name, state, last_activity, trans_times, trans_interval) VALUES ('%DEVICE_SN%', '%DEVICE_NAME%', '%DEVICE_NAME%', 'Offline', NOW(), '00:00;12:00', 60) ON DUPLICATE KEY UPDATE device_name='%DEVICE_NAME%', alias_name='%DEVICE_NAME%', last_activity=NOW();" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Device Added Successfully!
    echo ==========================================
    echo.
    echo Device %DEVICE_SN% has been added to the database.
    echo.
    echo You can now:
    echo 1. Connect your ZK device to the server
    echo 2. Restart Tomcat services
    echo 3. Access the application at http://localhost:8080/pushdemo
    echo.
) else (
    echo.
    echo ==========================================
    echo   Failed to Add Device
    echo ==========================================
    echo.
    echo There was an error adding the device to the database.
    echo Please check:
    echo 1. Database connection
    echo 2. Table structure
    echo 3. Device serial number format
    echo.
)

echo.
echo Press any key to exit...
pause >nul