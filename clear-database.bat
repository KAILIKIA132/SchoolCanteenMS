@echo off
title Clear Database Content
echo ==========================================
echo   Clear Database Content
echo ==========================================
echo.
echo WARNING: This will delete ALL data from the 'pushdemo' database.
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

rem Disable FK checks, Truncate tables, Enable FK checks
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SET FOREIGN_KEY_CHECKS = 0; TRUNCATE TABLE att_log; TRUNCATE TABLE user_info; TRUNCATE TABLE device_info; SET FOREIGN_KEY_CHECKS = 1;" %DB_NAME%

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Database Cleared Successfully!
    echo ==========================================
) else (
    echo.
    echo ==========================================
    echo   Failed to Clear Database
    echo ==========================================
    echo.
    echo Please check if the database and tables exist.
)

echo.
echo Press any key to exit...
pause >nul
