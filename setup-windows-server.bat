@echo off
cls
powershell.exe -ExecutionPolicy Bypass -File "%~dp0setup-windows-server.ps1" %*
pause