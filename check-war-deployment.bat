@echo off
title Check WAR Deployment
echo ==========================================
echo   Check WAR Deployment
echo ==========================================
echo.

echo 1. Looking for pushdemo WAR file:
for /f "delims=" %%i in ('dir /s /b "C:\*.war" 2^>nul ^| findstr -i "pushdemo"') do echo Found WAR: %%i
echo.

echo 2. Checking temp directories where WARs might be extracted:
set TEMP_PATHS="%TEMP%" "%TMP%" "C:\temp" "C:\tmp"

for %%p in (%TEMP_PATHS%) do (
    echo Checking: %%p
    if exist "%%p" (
        for /f "delims=" %%d in ('dir /a:d /b "%%p\*pushdemo*" 2^>nul') do (
            echo Found pushdemo directory: %%p\%%d
            dir "%%p\%%d" 2>nul | findstr "WEB-INF"
        )
    )
    echo.
)
echo.

echo 3. Checking system temp directories:
for /f "delims=" %%i in ('dir /a:d /s /b "%SystemRoot%\Temp\*pushdemo*" 2^>nul') do (
    echo Found in system temp: %%i
    dir "%%i" 2>nul | findstr "WEB-INF"
)
echo.

echo 4. Checking user profile temp directories:
for /f "delims=" %%i in ('dir /a:d /s /b "%USERPROFILE%\AppData\Local\Temp\*pushdemo*" 2^>nul') do (
    echo Found in user temp: %%i
    dir "%%i" 2>nul | findstr "WEB-INF"
)
echo.

echo 5. Checking if application is running from a different location:
echo Process command line for Tomcat:
for /f "tokens=2" %%p in ('tasklist | findstr -i "tomcat"') do (
    wmic process where "processid=%%p" get CommandLine | findstr -i "pushdemo"
    goto :found
)
:found
echo.

echo 6. Looking for any config.xml files related to pushdemo:
for /f "delims=" %%i in ('dir /s /b "C:\*config.xml" 2^>nul ^| findstr -i "pushdemo"') do (
    echo Found config: %%i
    echo Database URL:
    findstr "url" "%%i"
    echo.
)
echo.

echo Press any key to exit...
pause >nul