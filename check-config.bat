@echo off
title Check Application Configuration
echo ==========================================
echo   Check Application Configuration
echo ==========================================
echo.

set CONFIG_PATH=C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml
set TOMCAT_CONFIG=C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes\config.xml

echo 1. Checking source config.xml:
if exist "%CONFIG_PATH%" (
    echo ✓ Source config.xml found
    echo.
    echo Database configuration:
    findstr "url" "%CONFIG_PATH%"
    findstr "user" "%CONFIG_PATH%"
    findstr "password" "%CONFIG_PATH%"
    echo.
) else (
    echo ✗ Source config.xml not found at %CONFIG_PATH%
)
echo.

echo 2. Checking deployed config.xml:
if exist "%TOMCAT_CONFIG%" (
    echo ✓ Deployed config.xml found
    echo.
    echo Database configuration:
    findstr "url" "%TOMCAT_CONFIG%"
    findstr "user" "%TOMCAT_CONFIG%"
    findstr "password" "%TOMCAT_CONFIG%"
    echo.
) else (
    echo ✗ Deployed config.xml not found at %TOMCAT_CONFIG%
    echo.
    echo You may need to redeploy the application.
)
echo.

echo 3. Comparing configurations:
if exist "%CONFIG_PATH%" if exist "%TOMCAT_CONFIG%" (
    echo Comparing database URLs:
    for /f "tokens=3 delims=<>" %%a in ('findstr "url" "%CONFIG_PATH%"') do set SRC_URL=%%a
    for /f "tokens=3 delims=<>" %%b in ('findstr "url" "%TOMCAT_CONFIG%"') do set DEPLOY_URL=%%b
    echo Source URL: %SRC_URL%
    echo Deployed URL: %DEPLOY_URL%
    if "%SRC_URL%"=="%DEPLOY_URL%" (
        echo ✓ URLs match
    ) else (
        echo ✗ URLs do not match - redeployment needed
    )
    echo.
)
echo.

echo 4. Checking if application needs redeployment:
if not exist "%TOMCAT_CONFIG%" (
    echo Application needs to be redeployed.
    echo.
    echo Steps to redeploy:
    echo 1. Stop Tomcat service
    echo 2. Copy updated WAR file or directory to webapps
    echo 3. Start Tomcat service
    echo.
) else (
    echo Application appears to be deployed correctly.
)
echo.

echo Press any key to exit...
pause >nul