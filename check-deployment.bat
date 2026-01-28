@echo off
title Application Deployment Check
echo ==========================================
echo   Application Deployment Check
echo ==========================================
echo.

echo 1. Checking if Tomcat service is running:
sc query TomcatPushDemo | findstr "STATE"
echo.

echo 2. Checking Tomcat webapps directory:
if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps" (
    echo ✓ Tomcat webapps directory exists
    echo Contents:
    dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps" | findstr "pushdemo"
    if %errorlevel% equ 0 (
        echo ✓ pushdemo application found in webapps
        echo.
        echo Checking pushdemo directory structure:
        if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF" (
            echo ✓ WEB-INF directory exists
            if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes" (
                echo ✓ classes directory exists
                if exist "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes\config.xml" (
                    echo ✓ config.xml exists in deployed app
                    echo.
                    echo Deployed database configuration:
                    findstr "url" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes\config.xml"
                    findstr "user" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes\config.xml"
                    findstr "password" "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps\pushdemo\WEB-INF\classes\config.xml"
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
        dir "C:\Program Files\Apache Software Foundation\Tomcat 9.0\webapps" | findstr "\.war"
    )
) else (
    echo ✗ Tomcat webapps directory not found
)
echo.

echo 3. Checking source project configuration:
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

echo 4. Checking if WAR file exists for deployment:
if exist "C:\Meal_Management\SchoolCanteenMS\*.war" (
    echo Found WAR files:
    dir "C:\Meal_Management\SchoolCanteenMS\*.war"
) else (
    echo No WAR files found in source directory
)
echo.

echo 5. Testing if Tomcat is responding:
curl -s http://localhost:8080 >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat is responding on port 8080
    curl -s http://localhost:8080/pushdemo >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✓ pushdemo context is accessible
    ) else (
        echo ✗ pushdemo context is NOT accessible
        echo This suggests the application is not properly deployed
    )
) else (
    echo ✗ Tomcat is NOT responding on port 8080
)
echo.

echo.
echo Press any key to exit...
pause >nul