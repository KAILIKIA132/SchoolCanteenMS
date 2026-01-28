@echo off
title Import Database Schema
echo ==========================================
echo   Import Database Schema
echo ==========================================
echo.

set CONFIG_PATH=C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml
set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
set SCHEMA_PATH=C:\Meal_Management\SchoolCanteenMS\doc\pushdemo.sql

echo Looking for database schema file...
echo.

REM Parse config
for /f "tokens=3 delims=<>" %%a in ('findstr "url" "%CONFIG_PATH%"') do set DB_URL=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "user" "%CONFIG_PATH%"') do set DB_USER=%%a
for /f "tokens=3 delims=<>" %%a in ('findstr "password" "%CONFIG_PATH%"') do set DB_PASS=%%a

REM Extract database name
for /f "tokens=4 delims=/" %%a in ("%DB_URL%") do set DB_NAME=%%a
for /f "tokens=1 delims=?" %%a in ("%DB_NAME%") do set DB_NAME=%%a

echo Database: %DB_NAME%
echo Schema file: %SCHEMA_PATH%
echo.

if exist "%SCHEMA_PATH%" (
    echo Importing schema...
    %MYSQL_PATH% -u %DB_USER% -p%DB_PASS% %DB_NAME% < "%SCHEMA_PATH%" 2>nul
    
    if %errorlevel% equ 0 (
        echo.
        echo ==========================================
        echo   Schema Imported Successfully!
        echo ==========================================
        echo.
        echo Database schema has been imported.
        echo You should now have the required tables.
        echo.
    ) else (
        echo.
        echo ==========================================
        echo   Schema Import Failed
        echo ==========================================
        echo.
        echo There was an error importing the schema.
        echo Please check the schema file and try again.
        echo.
    )
) else (
    echo.
    echo ==========================================
    echo   Schema File Not Found
    echo ==========================================
    echo.
    echo Could not find schema file at:
    echo %SCHEMA_PATH%
    echo.
    echo Please verify the file exists or provide the correct path.
    echo.
)

echo.
echo Press any key to exit...
pause >nul