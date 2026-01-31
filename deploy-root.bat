@echo off
title Deploy to ROOT Context
echo ==========================================
echo   Deploy Application as ROOT
echo ==========================================
echo.
echo This script will:
echo 1. Stop Tomcat.
echo 2. Rename your 'pushdemo' to 'ROOT'.
echo 3. Start Tomcat.
echo.
echo This ensures the device connects to http://IP:8080/iclock/...
echo instead of http://IP:8080/pushdemo/iclock/...
echo.

set TOMCAT_HOME=C:\Program Files\Apache Software Foundation\Tomcat 8.0
set WEBAPPS=%TOMCAT_HOME%\webapps

if not exist "%WEBAPPS%" (
    echo Error: Tomcat webapps folder not found at:
    echo %WEBAPPS%
    echo Please edit this script if your Tomcat path is different.
    pause
    exit /b
)

set /p CONFIRM=Are you ready to stop Tomcat and rename the app? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

echo.
echo 1. Stopping Tomcat...
net stop Tomcat8 2>nul
taskkill /F /IM tomcat8.exe 2>nul
timeout /t 3 >nul

echo.
echo 2. Cleaning old ROOT...
if exist "%WEBAPPS%\ROOT" (
    rmdir /s /q "%WEBAPPS%\ROOT"
    echo    - Removed existing ROOT folder
)
if exist "%WEBAPPS%\ROOT.war" (
    del /f /q "%WEBAPPS%\ROOT.war"
    echo    - Removed existing ROOT.war
)

echo.
echo 3. Renaming pushdemo to ROOT...
if exist "%WEBAPPS%\pushdemo.war" (
    ren "%WEBAPPS%\pushdemo.war" ROOT.war
    echo    - Renamed pushdemo.war -> ROOT.war
) else if exist "%WEBAPPS%\pushdemo" (
    ren "%WEBAPPS%\pushdemo" ROOT
    echo    - Renamed pushdemo folder -> ROOT folder
) else (
    echo    WARNING: Could not find 'pushdemo' or 'pushdemo.war' in webapps!
    echo    Please ensure you have copied your project there.
)

echo.
echo 4. Starting Tomcat...
net start Tomcat8
echo.
echo ==========================================
echo   Deployment Complete!
echo ==========================================
echo.
echo Your app is now running at http://localhost:8080/
echo Devices can now connect directly.
echo.
pause
