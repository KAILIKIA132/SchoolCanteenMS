@echo off
title Push Demo Service Restart
echo ==========================================
echo   Push Demo Services Restart
echo ==========================================
echo.

echo Stopping Push Demo Services...
echo.

echo Stopping Tomcat service...
net stop TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat service stopped successfully
) else (
    echo ⚠ Tomcat service may already be stopped or not installed
)

echo Stopping MySQL service...
net stop MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MySQL service stopped successfully
) else (
    echo ⚠ MySQL service may already be stopped or not installed
)

echo.
echo Waiting 5 seconds for services to fully stop...
timeout /t 5 /nobreak >nul

echo.
echo Starting Push Demo Services...
echo.

echo Starting MySQL service...
net start MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ MySQL service started successfully
) else (
    echo ✗ MySQL service failed to start
    echo.
    echo Troubleshooting:
    echo 1. Check if MySQL service is installed
    echo 2. Verify service name is MySQL80
    echo 3. Run as Administrator
    echo.
    goto :end
)

timeout /t 10 /nobreak >nul

echo Starting Tomcat service...
net start TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat service started successfully
) else (
    echo ✗ Tomcat service failed to start
    echo.
    echo Troubleshooting:
    echo 1. Check if Tomcat service is installed
    echo 2. Verify service name is TomcatPushDemo
    echo 3. Run as Administrator
    echo.
    goto :end
)

echo.
echo Waiting 15 seconds for services to fully initialize...
timeout /t 15 /nobreak >nul

echo.
echo ==========================================
echo   Services Restart Complete!
echo ==========================================
echo.
echo Application should be available at:
echo http://localhost:8080/pushdemo
echo.
echo Current service status:
sc query TomcatPushDemo | findstr "STATE" 
sc query MySQL80 | findstr "STATE"
echo.

:end
echo.
echo Press any key to exit...
pause >nul