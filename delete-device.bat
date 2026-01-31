@echo off
title Delete Specific Device
echo ==========================================
echo   Delete Specific Device
echo ==========================================
echo.
echo WARNING: This will delete device 'TDBD254600293' from 'device_info'.
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=Canteen@2026
set DEVICE_SN=TDBD254600293

set /p CONFIRM=Are you sure you want to delete device %DEVICE_SN%? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

echo.
echo Deleting device %DEVICE_SN%...

%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "DELETE FROM device_info WHERE device_sn = '%DEVICE_SN%';" %DB_NAME%

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Device Deleted Successfully!
    echo ==========================================
) else (
    echo.
    echo ==========================================
    echo   Failed to Delete Device
    echo ==========================================
    echo.
    echo Please check if the database exists.
)

echo.
echo Press any key to exit...
pause >nul
