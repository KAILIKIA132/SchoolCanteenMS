@echo off
title Find Tomcat Installation
echo ==========================================
echo   Find Tomcat Installation
echo ==========================================
echo.

echo 1. Checking common Tomcat installation paths:
set TOMCAT_PATHS="C:\Program Files\Apache Software Foundation\Tomcat 9.0" "C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0" "C:\apache-tomcat-9.0" "C:\tomcat9"

for %%p in (%TOMCAT_PATHS%) do (
    echo Checking: %%p
    if exist "%%p" (
        echo ✓ Found Tomcat installation at: %%p
        echo Checking webapps directory:
        if exist "%%p\webapps" (
            echo ✓ webapps directory exists
            dir "%%p\webapps" | findstr "pushdemo"
        ) else (
            echo ✗ webapps directory not found
        )
        echo.
    ) else (
        echo ✗ Not found at %%p
    )
    echo.
)
echo.

echo 2. Checking Windows Services for Tomcat installation path:
echo TomcatPushDemo service details:
sc qc TomcatPushDemo | findstr "BINARY_PATH_NAME"
echo.

echo 3. Checking environment variables:
echo CATALINA_HOME: %CATALINA_HOME%
echo CATALINA_BASE: %CATALINA_BASE%
echo.

echo 4. Looking for any pushdemo deployments:
for /f "delims=" %%i in ('dir /s /b "C:\Program Files\*\pushdemo*" 2^>nul') do echo Found: %%i
for /f "delims=" %%i in ('dir /s /b "C:\apache*\pushdemo*" 2^>nul') do echo Found: %%i
echo.

echo 5. Checking if there's a different Tomcat installation:
echo Looking for Tomcat directories...
for /f "delims=" %%i in ('dir /a:d /s /b "C:\Program Files\*\*tomcat*" 2^>nul') do (
    if exist "%%i\webapps" echo Found Tomcat with webapps: %%i
)
echo.

echo 6. Testing which Tomcat installation is actually running:
echo Process information for Tomcat:
tasklist | findstr -i "tomcat"
if %errorlevel% equ 0 (
    echo Getting Tomcat process details:
    for /f "tokens=2" %%p in ('tasklist | findstr -i "tomcat"') do (
        echo Tomcat PID: %%p
        wmic process where "processid=%%p" get ExecutablePath,CommandLine
        goto :found_process
    )
    :found_process
)
echo.

echo Press any key to exit...
pause >nul