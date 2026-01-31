@echo off
title Fresh Setup - School Canteen System
echo ==========================================
echo        FRESH SERVER SETUP
echo ==========================================
echo.
echo This script will set up the entire system from scratch:
echo 1. Reset Database (Import local_backup.sql + Fixes)
echo 2. Deploy Application as ROOT (No /pushdemo URL)
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set APACHE_HOME=C:\Program Files\Apache Software Foundation\Tomcat 8.5
set WEBAPPS=%APACHE_HOME%\webapps

set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=Canteen@2026

set BACKUP_FILE=local_backup.sql
set FIX_FILE=fix_device_list.sql
set WAR_FILE=pushdemo.war

REM --- CHECK FILES ---
if not exist "%BACKUP_FILE%" (
    echo [ERROR] %BACKUP_FILE% not found!
    echo Please ensure you have copied the database backup file.
    pause
    exit /b
)
if not exist "%FIX_FILE%" (
    echo [ERROR] %FIX_FILE% not found!
    echo Please ensure 'fix_device_list.sql' is in this folder.
    pause
    exit /b
)
if not exist "%WAR_FILE%" (
    echo [ERROR] %WAR_FILE% not found!
    echo Please ensure you exported your project as 'pushdemo.war'.
    pause
    exit /b
)

set /p CONFIRM=WARNING: This will WIPE the existing database and redeploy the app. Continue? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

REM --- STEP 1: DATABASE SETUP ---
echo.
echo [1/3] Setting up Database...
echo Dropping old database...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "DROP DATABASE IF EXISTS %DB_NAME%; CREATE DATABASE %DB_NAME% DEFAULT CHARACTER SET utf8;" 2>nul
if %errorlevel% neq 0 goto DB_ERROR

echo Importing backup data...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME% < "%BACKUP_FILE%" 2>nul
if %errorlevel% neq 0 goto DB_ERROR

echo Applying fixes (Stored Procedures)...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME% < "%FIX_FILE%" 2>nul
if %errorlevel% neq 0 goto DB_ERROR
echo Database setup complete.

REM --- STEP 2: TOMCAT DEPLOYMENT ---
echo.
echo [2/3] Deploying Application...

echo Removing old ROOT application...
if exist "%WEBAPPS%\ROOT" rd /s /q "%WEBAPPS%\ROOT"
if exist "%WEBAPPS%\ROOT.war" del /f /q "%WEBAPPS%\ROOT.war"

echo Deploying new ROOT.war...
copy "%WAR_FILE%" "%WEBAPPS%\ROOT.war" >nul
if %errorlevel% neq 0 (
    echo [ERROR] Failed to copy WAR file. Is Tomcat running and locking the file?
    echo Please STOP Tomcat and try again.
    pause
    exit /b
)
echo Deployment successful.

REM --- FINISH ---
echo.
echo ==========================================
echo        SETUP COMPLETED SUCCESSFULLY
echo ==========================================
echo.
echo 1. Start/Restart your Tomcat Server.
echo 2. Access your site at: http://localhost:8080/ (or your server IP)
echo 3. Devices communicate at: http://IP:8080/iclock/cdata
echo.
pause
exit /b

:DB_ERROR
echo.
echo [ERROR] Database operation failed. Check password or connection.
pause
exit /b
