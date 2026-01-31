@echo off
title Stop Server Services
echo ==========================================
echo        STOPPING SERVICES
echo ==========================================
echo.

echo [1/2] Stopping Tomcat Service...
net stop Tomcat8 2>nul
if %errorlevel% equ 0 echo Service Stopped.

echo.
echo [2/2] Killing Database/Java Processes...
REM This ensures manual Tomcat instances are closed
taskkill /F /IM java.exe 2>nul
if %errorlevel% equ 0 echo Java processes killed.

REM Optional: Stop MySQL if needed (Remove REM to enable)
REM net stop MySQL80

echo.
echo ==========================================
echo          STOP COMPLETE
echo ==========================================
pause
