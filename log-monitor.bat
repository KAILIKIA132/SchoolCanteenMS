@echo off
title Real-time Log Monitor
echo ==========================================
echo   Real-time Log Monitor for Push Demo
echo ==========================================
echo.

echo Monitoring Tomcat logs for database connectivity...
echo.
echo Every time you reload the application, you'll see:
echo - Database connection messages
echo - SQL queries executed
echo - Any errors or exceptions
echo.

:monitor
echo.
echo ------------------------------------------
echo   Current Time: %DATE% %TIME%
echo ------------------------------------------
echo.

echo Checking catalina.out for database messages:
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out" (
    echo Last 20 lines of catalina.out:
    powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out' -Tail 20" | findstr /i "database\|connect\|sql\|error\|exception\|connection"
    if %errorlevel% neq 0 (
        echo No recent database-related messages found in catalina.out
        powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out' -Tail 10"
    )
) else (
    echo catalina.out not found
)
echo.

echo Checking localhost log for database messages:
for /f "delims=" %%i in ('dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\localhost.*.log" /b /o-d') do (
    echo Last 20 lines of localhost log:
    powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\%%i' -Tail 20" | findstr /i "database\|connect\|sql\|error\|exception\|connection"
    if %errorlevel% neq 0 (
        echo No recent database-related messages found in localhost log
        powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\%%i' -Tail 10"
    )
    goto :found_log
)
:found_log
echo.

echo Checking device_info table directly:
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026 -e "SELECT device_sn, device_name, state, last_activity FROM device_info;" pushdemo 2>nul
echo.

echo.
echo Press Ctrl+C to stop monitoring, or refresh to see latest logs.
echo Reload your application at http://localhost:8080/pushdemo to see logs.
echo.
goto monitor