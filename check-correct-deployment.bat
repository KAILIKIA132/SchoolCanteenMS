@echo off
title Check Correct Tomcat Deployment
echo ==========================================
echo   Check Correct Tomcat Deployment
echo ==========================================
echo.

set TOMCAT_HOME=C:\apache-tomcat-9.0.84

echo Tomcat installation path: %TOMCAT_HOME%
echo.

echo 1. Checking if Tomcat directory exists:
if exist "%TOMCAT_HOME%" (
    echo ✓ Tomcat installation found
) else (
    echo ✗ Tomcat installation not found at %TOMCAT_HOME%
    goto :end
)
echo.

echo 2. Checking webapps directory:
if exist "%TOMCAT_HOME%\webapps" (
    echo ✓ webapps directory exists
    echo Contents:
    dir "%TOMCAT_HOME%\webapps" | findstr "pushdemo"
    if %errorlevel% equ 0 (
        echo ✓ pushdemo application found in webapps
        echo.
        echo Checking pushdemo directory structure:
        if exist "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF" (
            echo ✓ WEB-INF directory exists
            if exist "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes" (
                echo ✓ classes directory exists
                if exist "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml" (
                    echo ✓ config.xml exists in deployed app
                    echo.
                    echo Deployed database configuration:
                    findstr "url" "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml"
                    findstr "user" "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml"
                    findstr "password" "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml"
                ) else (
                    echo ✗ config.xml NOT found in deployed app
                )
            ) else (
                echo ✗ classes directory NOT found in deployed app
            )
        ) else (
            echo ✗ WEB-INF directory NOT found in deployed app
        )
    ) else (
        echo ✗ pushdemo application NOT found in webapps
        echo.
        echo Looking for any WAR files:
        dir "%TOMCAT_HOME%\webapps" | findstr "\.war"
    )
) else (
    echo ✗ webapps directory not found at %TOMCAT_HOME%\webapps
)
echo.

echo 3. Checking logs directory:
if exist "%TOMCAT_HOME%\logs" (
    echo ✓ logs directory exists
    echo Recent log files:
    dir "%TOMCAT_HOME%\logs" /o-d | findstr "\.log"
    echo.
    echo Checking for database connection messages in logs:
    for /f "delims=" %%i in ('dir /b "%TOMCAT_HOME%\logs\localhost.*.log" 2^>nul') do (
        echo Checking %%i for database messages:
        powershell -Command "Get-Content '%TOMCAT_HOME%\logs\%%i' -Tail 20" | findstr /i "database\|connect\|sql\|error\|exception"
        goto :found_log
    )
    :found_log
) else (
    echo ✗ logs directory not found at %TOMCAT_HOME%\logs
)
echo.

echo 4. Checking source project configuration:
if exist "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml" (
    echo ✓ Source config.xml found
    echo Source database configuration:
    findstr "url" "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml"
    findstr "user" "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml"
    findstr "password" "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml"
) else (
    echo ✗ Source config.xml not found
)
echo.

echo 5. Comparing configurations if both exist:
if exist "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml" if exist "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml" (
    echo Comparing database URLs:
    for /f "tokens=3 delims=<>" %%a in ('findstr "url" "C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml"') do set SRC_URL=%%a
    for /f "tokens=3 delims=<>" %%b in ('findstr "url" "%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml"') do set DEPLOY_URL=%%b
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

:end
echo Press any key to exit...
pause >nul