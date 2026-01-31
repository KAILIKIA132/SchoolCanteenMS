@echo off
title Fix Device List Issue
echo ==========================================
echo   Fix Device List Issue
echo ==========================================
echo.
echo This script will fix the missing database objects causing the device list to be empty.
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=Canteen@2026
set SQL_FILE=fix_device_list.sql

if not exist "%SQL_FILE%" (
    echo Error: %SQL_FILE% not found!
    echo Please make sure the SQL file is in the same directory.
    pause
    exit /b
)

echo Applying fix...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME% < "%SQL_FILE%" 2>nul

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Fix Applied Successfully!
    echo ==========================================
) else (
    echo.
    echo ==========================================
    echo   Failed to Apply Fix
    echo ==========================================
    echo.
    echo Please check your database connection.
)

echo.
echo Press any key to exit...
pause >nul
