@echo off
title FORCE Deploy to ROOT
echo ==========================================
echo   FORCE Deploy Application as ROOT
echo ==========================================
echo.
echo WARN: This script will AGGRESSIVELY delete:
echo  - webapps\ROOT
echo  - webapps\ROOT.war
echo  - webapps\pushdemo
echo  - webapps\pushdemo.war (after backup)
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

set /p CONFIRM=Are you ready to FORCE deploy? (Y/N): 
if /i "%CONFIRM%" neq "Y" goto :EOF

echo.
echo 1. Stopping Tomcat (Force Kill)...
net stop Tomcat8 2>nul
taskkill /F /IM tomcat8.exe 2>nul
timeout /t 5 >nul

echo.
echo 2. Deleting OLD deployments...
if exist "%WEBAPPS%\ROOT" (
    echo    - Deleting ROOT folder...
    rmdir /s /q "%WEBAPPS%\ROOT"
)
if exist "%WEBAPPS%\ROOT.war" (
    echo    - Deleting ROOT.war...
    del /f /q "%WEBAPPS%\ROOT.war"
)
if exist "%WEBAPPS%\pushdemo" (
    echo    - Deleting pushdemo folder...
    rmdir /s /q "%WEBAPPS%\pushdemo"
)

echo.
echo 3. Renaming pushdemo.war to ROOT.war...
if exist "%WEBAPPS%\pushdemo.war" (
    ren "%WEBAPPS%\pushdemo.war" ROOT.war
    echo    - SUCCESS: Renamed pushdemo.war to ROOT.war
) else (
    echo    ERROR: Could not find 'pushdemo.war' in webapps!
    echo    Please make sure you copied your WAR file there.
    if exist "pushdemo.war" (
        echo    - Found pushdemo.war in CURRENT folder. Moving it...
        copy "pushdemo.war" "%WEBAPPS%\ROOT.war"
        echo    - Copied local pushdemo.war to webapps\ROOT.war
    )
)

echo.
echo 4. Starting Tomcat...
net start Tomcat8
echo.
echo ==========================================
echo   Check your URL now:
echo   http://localhost:8080/iclock/cdata?options=all
echo ==========================================
pause
