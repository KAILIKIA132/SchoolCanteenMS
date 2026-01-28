@echo off
title Fix Application Configuration
echo ==========================================
echo   Fix Application Configuration
echo ==========================================
echo.

set TOMCAT_HOME=C:\apache-tomcat-9.0.84
set SOURCE_CONFIG=C:\Meal_Management\SchoolCanteenMS\WebContent\WEB-INF\classes\config.xml
set DEPLOYED_CONFIG=%TOMCAT_HOME%\webapps\pushdemo\WEB-INF\classes\config.xml

echo Fixing deployed application configuration...
echo.

echo 1. Checking source configuration:
if exist "%SOURCE_CONFIG%" (
    echo ✓ Source config.xml found
    echo Source database configuration:
    findstr "url" "%SOURCE_CONFIG%"
    findstr "user" "%SOURCE_CONFIG%" 
    findstr "password" "%SOURCE_CONFIG%"
    echo.
) else (
    echo ✗ Source config.xml not found
    goto :end
)
echo.

echo 2. Checking deployed configuration:
if exist "%DEPLOYED_CONFIG%" (
    echo ✓ Deployed config.xml found
    echo Current deployed configuration:
    findstr "url" "%DEPLOYED_CONFIG%"
    findstr "user" "%DEPLOYED_CONFIG%"
    findstr "password" "%DEPLOYED_CONFIG%"
    echo.
) else (
    echo ✗ Deployed config.xml not found
    goto :end
)
echo.

echo 3. Copying correct configuration:
echo Backing up current deployed config...
copy "%DEPLOYED_CONFIG%" "%DEPLOYED_CONFIG%.backup" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Backup created
) else (
    echo ⚠ Could not create backup
)
echo.

echo Copying source config to deployed location...
copy "%SOURCE_CONFIG%" "%DEPLOYED_CONFIG%" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Configuration copied successfully
) else (
    echo ✗ Failed to copy configuration
    goto :end
)
echo.

echo 4. Verifying the fix:
echo New deployed configuration:
findstr "url" "%DEPLOYED_CONFIG%"
findstr "user" "%DEPLOYED_CONFIG%"
findstr "password" "%DEPLOYED_CONFIG%"
echo.

echo 5. Restarting Tomcat to apply changes:
echo Stopping Tomcat service...
net stop TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat stopped successfully
) else (
    echo ⚠ Tomcat may already be stopped
)
echo.

echo Waiting 5 seconds...
timeout /t 5 /nobreak >nul

echo Starting Tomcat service...
net start TomcatPushDemo >nul 2>&1
if %errorlevel% equ 0 (
    echo ✓ Tomcat started successfully
) else (
    echo ✗ Failed to start Tomcat
    goto :end
)
echo.

echo Waiting 15 seconds for Tomcat to initialize...
timeout /t 15 /nobreak >nul

echo.
echo ==========================================
echo   Configuration Fix Complete!
echo ==========================================
echo.
echo The application should now be able to connect to your database.
echo Your device should appear when you access:
echo http://localhost:8080/pushdemo
echo.

:end
echo.
echo Press any key to exit...
pause >nul