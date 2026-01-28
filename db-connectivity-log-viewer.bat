@echo off
title Database Connectivity Log Viewer
echo ==========================================
echo   Database Connectivity Log Viewer
echo ==========================================
echo.

echo This script will continuously monitor logs for database connectivity issues.
echo.
echo Actions to take:
echo 1. Run this script in a separate window
echo 2. Reload your application in browser
echo 3. Watch for database connection messages in real-time
echo.

:loop
echo.
echo ==========================================
echo   %DATE% %TIME% - Checking logs...
echo ==========================================

echo.
echo Catalina log (database connections):
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out" (
    powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out' -Tail 30" | findstr /i /c:"connect" /c:"database" /c:"error" /c:"exception" /c:"connection" /c:"sql"
    if %errorlevel% neq 0 (
        echo No database/connection related messages found recently
    )
) else (
    echo Catalina log not found
)
echo.

echo Localhost log (application errors):
for /f "delims=" %%i in ('dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\localhost.*.log" /b /o-d') do (
    powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\%%i' -Tail 30" | findstr /i /c:"connect" /c:"database" /c:"error" /c:"exception" /c:"connection" /c:"sql" /c:"NullPointerException"
    if %errorlevel% neq 0 (
        echo No database/application error messages found recently
    )
    goto :found_log
)
:found_log
echo.

echo Current device count in database:
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026 -e "SELECT COUNT(*) as device_count FROM device_info;" pushdemo 2>nul
echo.

echo Press Ctrl+C to exit, or wait 5 seconds to refresh...
timeout /t 5 /nobreak >nul
goto loop