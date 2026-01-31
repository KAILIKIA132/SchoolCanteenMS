@echo off
title Setup Migration from Local DB
echo ==========================================
echo   Migrate Database from Local Dump
echo ==========================================
echo.
echo This script will:
echo 1. DROP the existing 'pushdemo' database on this server.
echo 2. CREATE a new empty 'pushdemo' database.
echo 3. IMPORT data from 'local_backup.sql'.
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=Canteen@2026
set BACKUP_FILE=local_backup.sql

if not exist "%BACKUP_FILE%" (
    echo Error: %BACKUP_FILE% not found!
    echo.
    echo Please make sure you have copied 'local_backup.sql' from your Mac
    echo to this folder: %CD%
    echo.
    pause
    exit /b
)

set /p CONFIRM=WARNING: ALL existing data on this server will be LOST. Continue? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

echo.
echo 1. Dropping existing database...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "DROP DATABASE IF EXISTS %DB_NAME%;" 2>nul

echo 2. Creating new database...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "CREATE DATABASE %DB_NAME% DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;" 2>nul

echo 3. Importing backup...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME% < "%BACKUP_FILE%" 2>nul

if %errorlevel% equ 0 (
    echo.
    echo ==========================================
    echo   Migration Completed Successfully!
    echo ==========================================
    echo.
    echo The Windows server database now matches your local Mac database.
) else (
    echo.
    echo ==========================================
    echo   Migration Failed
    echo ==========================================
    echo.
    echo Please check the error messages above.
)

echo.
echo Press any key to exit...
pause >nul
