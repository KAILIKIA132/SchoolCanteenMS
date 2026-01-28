@echo off
title Database Connectivity Log Viewer (Correct Path)
echo ==========================================
echo   Database Connectivity Log Viewer
echo ==========================================
echo.

set TOMCAT_HOME=C:\apache-tomcat-9.0.84

echo Monitoring logs at: %TOMCAT_HOME%\logs
echo.
echo Every time you reload the application, you'll see:
echo - Database connection messages
echo - SQL queries executed  
echo - Any errors or exceptions
echo.

:loop
echo.
echo ==========================================
echo   %DATE% %TIME% - Checking logs...
echo ==========================================

echo.
echo Catalina log (database connections):
if exist "%TOMCAT_HOME%\logs\catalina.out" (
    echo Last 20 lines of catalina.out:
    powershell -Command "Get-Content '%TOMCAT_HOME%\logs\catalina.out' -Tail 20" | findstr /i "database\|connect\|sql\|error\|exception\|connection"
    if %errorlevel% neq 0 (
        echo No recent database-related messages found in catalina.out
        powershell -Command "Get-Content '%TOMCAT_HOME%\logs\catalina.out' -Tail 10"
    )
) else (
    echo catalina.out not found at %TOMCAT_HOME%\logs\
)
echo.

echo Localhost log (application errors):
if exist "%TOMCAT_HOME%\logs" (
    for /f "delims=" %%i in ('dir /b "%TOMCAT_HOME%\logs\localhost.*.log" 2^>nul ^| sort') do (
        set "latest_log=%%i"
    )
    if defined latest_log (
        echo Last 20 lines of %latest_log%:
        powershell -Command "Get-Content '%TOMCAT_HOME%\logs\%latest_log%' -Tail 20" | findstr /i "database\|connect\|sql\|error\|exception\|connection\|NullPointerException"
        if %errorlevel% neq 0 (
            echo No recent database/application error messages found
            powershell -Command "Get-Content '%TOMCAT_HOME%\logs\%latest_log%' -Tail 10"
        )
    ) else (
        echo No localhost log files found
    )
) else (
    echo Logs directory not found at %TOMCAT_HOME%\logs\
)
echo.

echo Current device count in database:
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026 -e "SELECT device_sn, device_name, state FROM device_info;" pushdemo 2>nul
echo.

echo.
echo Press Ctrl+C to exit, or wait 5 seconds to refresh...
timeout /t 5 /nobreak >nul
goto loop