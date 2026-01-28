@echo off
title Application Troubleshooting
echo ==========================================
echo   Application Troubleshooting
echo ==========================================
echo.

echo 1. Checking if Tomcat is running:
sc query TomcatPushDemo | findstr "STATE"
echo.

echo 2. Checking if MySQL is running:
sc query MySQL80 | findstr "STATE"
echo.

echo 3. Testing database connection from application perspective:
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026 -e "SELECT device_sn, device_name, state FROM device_info;" pushdemo 2>nul
echo.

echo 4. Checking Tomcat logs for recent errors:
echo Looking for recent log entries in catalina.out...
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out" (
    echo Last 10 lines of catalina.out:
    powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\catalina.out' -Tail 10"
) else (
    echo catalina.out log file not found
)
echo.

echo 5. Checking localhost log for recent errors:
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\localhost.*.log" (
    echo Last 10 lines of localhost log:
    for /f "delims=" %%i in ('dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\localhost.*.log" /b /o-d') do (
        powershell -Command "Get-Content 'C:\Program Files\Apache Software Foundation\Tomcat 9.0\logs\%%i' -Tail 10"
        goto :found_log
    )
    :found_log
) else (
    echo localhost log file not found
)
echo.

echo 6. Testing web access:
curl -s http://localhost:8080/pushdemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Web application is accessible
) else (
    echo ✗ Cannot access web application
    echo.
    echo Troubleshooting:
    echo 1. Check if Tomcat is properly configured
    echo 2. Verify pushdemo application is deployed
    echo 3. Check firewall settings
)
echo.

echo 7. Checking if pushdemo application is deployed:
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo" (
    echo ✓ pushdemo application directory exists
    dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo" | findstr "WEB-INF"
) else (
    echo ✗ pushdemo application not found in webapps
    echo.
    echo Troubleshooting:
    echo 1. Check if WAR file was deployed
    echo 2. Verify Tomcat deployment configuration
)
echo.

echo Press any key to exit...
pause >nul