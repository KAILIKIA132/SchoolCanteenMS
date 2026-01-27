@echo off
set VIRTUAL_ENV=%~dp0venv
set PATH=%VIRTUAL_ENV%\Scripts;%PATH%

if exist "%VIRTUAL_ENV%\Scripts\python.exe" (
    echo Running setup verification...
    "%VIRTUAL_ENV%\Scripts\python.exe" test-setup.py
) else (
    echo Python virtual environment not found.
    echo Please run the setup script first.
    pause
    exit /b 1
)

pause