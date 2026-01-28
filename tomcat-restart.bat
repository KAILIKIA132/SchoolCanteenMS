@echo off
title Tomcat Restart Script
echo ==========================================
echo   Tomcat Service Restart
echo ==========================================
echo.

echo Stopping Tomcat service...
echo.

net stop TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat service stopped successfully
) else (
    echo ⚠ Tomcat service may already be stopped or not installed
)

echo.
echo Waiting 5 seconds for service to fully stop...
timeout /t 5 /nobreak >nul

echo.
echo Starting Tomcat service...
echo.

net start TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat service started successfully
) else (
    echo ✗ Failed to start Tomcat service
    echo.
    echo Troubleshooting:
    echo 1. Check if Tomcat service is installed
    echo 2. Verify service name is TomcatPushDemo
    echo 3. Run as Administrator
    echo.
    goto :end
)

echo.
echo Waiting 10 seconds for Tomcat to initialize...
timeout /t 10 /nobreak >nul

echo.
echo ==========================================
echo   Tomcat Restart Complete!
echo ==========================================
echo.
echo Application should be available at:
echo http://localhost:8080/pushdemo
echo.
echo Checking Tomcat status...
sc query TomcatPushDemo | findstr "STATE"
echo.

:end
echo.
echo Press any key to exit...
pause >nul