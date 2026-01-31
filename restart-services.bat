@echo off
title Push Demo Service Restart
echo ==========================================
echo   Push Demo Services Restart
echo ==========================================
echo.

REM 1. Prompt for Tomcat Path (Optional)
echo Please enter the full path to your Tomcat folder (e.g. C:\Program Files\Tomcat)
echo If you installed Tomcat as a Service (TomcatPushDemo), just press ENTER.
echo.
set /p TOMCAT_HOME="Tomcat Path: "

echo.
echo ==========================================
echo 1. STOPPING SERVICES
echo ==========================================

echo Stopping Tomcat...
if "%TOMCAT_HOME%"=="" (
    net stop TomcatPushDemo >nul 2>&1
    if %errorlevel% equ 0 echo    [OK] Service Stopped
) else (
    if exist "%TOMCAT_HOME%\bin\shutdown.bat" (
        call "%TOMCAT_HOME%\bin\shutdown.bat"
        echo    [OK] Shutdown Command Sent
    ) else (
        echo    [!] Warning: shutdown.bat not found at provided path.
    )
)

echo Stopping MySQL...
net stop MySQL80 >nul 2>&1
if %errorlevel% equ 0 echo    [OK] MySQL Stopped

echo.
echo Waiting 5 seconds (Cleaning up)...
timeout /t 5 /nobreak

echo.
echo ==========================================
echo 2. STARTING SERVICES
echo ==========================================

echo Starting MySQL...
net start MySQL80 >nul 2>&1
if %errorlevel% equ 0 (
    echo    [OK] MySQL Started
) else (
    echo    [!] MySQL Service failed (or already running)
)

echo.
echo Waiting 10 seconds (Database Init)...
timeout /t 10 /nobreak

echo Starting Tomcat...
if "%TOMCAT_HOME%"=="" (
    net start TomcatPushDemo >nul 2>&1
    if %errorlevel% equ 0 ( 
        echo    [OK] Tomcat Service Started 
    ) else (
        echo    [X] Tomcat Service Failed! (Check service name)
    )
) else (
    if exist "%TOMCAT_HOME%\bin\startup.bat" (
        echo    [OK] Launching Startup Script...
        call "%TOMCAT_HOME%\bin\startup.bat"
    ) else (
        echo    [X] Error: startup.bat not found at: %TOMCAT_HOME%\bin\
    )
)

echo.
echo ==========================================
echo   RESTART COMPLETE
echo ==========================================
echo Application URL: http://localhost:8080/
echo.
pause