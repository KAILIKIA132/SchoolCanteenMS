@echo off
title Check Database Content
echo ==========================================
echo   Database Content Check
echo ==========================================
echo.

set CONFIG_PATH=C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml
set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Checking database content...
echo.

REM Parse config
for /f "tokens=3 delims=<>" %%a in ('findstr "url" "%CONFIG_PATH%"') do set DB_URL=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "user" "%CONFIG_PATH%"') do set DB_USER=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "password" "%CONFIG_PATH%"') do set DB_PASS=%%a

REM Extract database name
for /f "tokens=4 delims=/" %%a in ("%DB_URL%") do set DB_NAME=%%a
for /f "tokens=1 delims=?" %%a in ("%DB_NAME%") do set DB_NAME=%%a

echo Database: %DB_NAME%
echo.

echo Checking device table...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SELECT COUNT(*) as device_count FROM device_info;" %DB_NAME% 2>nul
echo.

echo Checking user table...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SELECT COUNT(*) as user_count FROM user_info;" %DB_NAME% 2>nul
echo.

echo Checking if any devices exist...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SELECT device_sn, device_name, state FROM device_info LIMIT 5;" %DB_NAME% 2>nul
echo.

echo Checking if any users exist...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SELECT user_pin, name, device_sn FROM user_info LIMIT 5;" %DB_NAME% 2>nul
echo.

echo ==========================================
echo   Database Schema Check
echo ==========================================
echo.

echo Checking all tables...
%MYSQL_PATH% -u %DB_USER% -p%DB_PASS% -e "SHOW TABLES;" %DB_NAME% 2>nul

echo.
echo Press any key to exit...
pause >nul