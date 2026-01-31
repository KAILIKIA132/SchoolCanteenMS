@echo off
title Clear Device Info
echo ==========================================
echo   Clear Device Info Table
echo ==========================================
echo.
echo WARNING: This will delete ALL data from the 'device_info' table.
echo This action cannot be undone.
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=Canteen@2026

set /p CONFIRM=Are you sure you want to continue? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

echo.
echo Clearing data...

rem Disable FK checks, Truncate table, Enable FK checks
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SET FOREIGN_KEY_CHECKS = 0; TRUNCATE TABLE device_info; SET FOREIGN_KEY_CHECKS = 1;" %DB_NAME%

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Device Info Cleared Successfully!
    echo ==========================================
) else (
    echo.
    echo ==========================================
    echo   Failed to Clear Device Info
    echo ==========================================
    echo.
    echo Please check if the database and table exist.
)

echo.
echo Press any key to exit...
pause >nul
