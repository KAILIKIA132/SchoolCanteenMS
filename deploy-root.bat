@echo off
title Deploy as ROOT Context
echo ==========================================
echo   Deploy Project as ROOT Application
echo ==========================================
echo.
echo This script will rename your project to ROOT so devices can connect.
echo.

set APACHE_HOME=C:\Program Files\Apache Software Foundation\Tomcat 8.5
set WEBAPPS=%APACHE_HOME%\webapps

echo Checking for existing ROOT...
if exist "%WEBAPPS%\ROOT" (
    echo Backing up existing ROOT folder...
    rename "%WEBAPPS%\ROOT" "ROOT_BACKUP_%RANDOM%"
)
if exist "%WEBAPPS%\ROOT.war" (
    echo Backing up existing ROOT.war...
    rename "%WEBAPPS%\ROOT.war" "ROOT_BACKUP_%RANDOM%.war"
)

echo.
echo Deploying pushdemo as ROOT...
if exist "pushdemo.war" (
    copy "pushdemo.war" "%WEBAPPS%\ROOT.war"
    echo Success: Copied pushdemo.war to ROOT.war
) else (
    echo Error: pushdemo.war not found in current directory!
    echo Please make sure you have exported the WAR file to this folder.
    pause
    exit /b
)

echo.
echo ==========================================
echo   Deployment Complete!
echo ==========================================
echo Please Restart Tomcat for changes to take effect.
echo.
pause
