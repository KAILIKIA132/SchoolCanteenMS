@echo off
title Database Structure Diagnostic
echo ==========================================
echo   Database Structure Diagnostic
echo ==========================================
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Checking database structure...
echo.

echo 1. Checking if pushdemo database exists:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SHOW DATABASES LIKE 'pushdemo';" 2>nul
echo.

echo 2. Checking tables in pushdemo database:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SHOW TABLES;" pushdemo 2>nul
echo.

echo 3. Checking device_info table structure:
%MYSQL_PATH% -u root -pCanteen@2026 -e "DESCRIBE device_info;" pushdemo 2>nul
if %errorlevel% neq 0 (
    echo Table device_info does not exist or is inaccessible
)
echo.

echo 4. Checking user_info table structure:
%MYSQL_PATH% -u root -pCanteen@2026 -e "DESCRIBE user_info;" pushdemo 2>nul
if %errorlevel% neq 0 (
    echo Table user_info does not exist or is inaccessible
)
echo.

echo 5. Checking if any tables exist at all:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = 'pushdemo';" 2>nul
echo.

echo 6. Checking database permissions:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SHOW GRANTS FOR 'root'@'localhost';" 2>nul
echo.

echo Press any key to exit...
pause >nul