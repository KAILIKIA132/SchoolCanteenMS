@echo off
title Create Database Tables
echo ==========================================
echo   Create Database Tables
echo ==========================================
echo.

set MYSQL_PATH="C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"

echo Creating required database tables...
echo.

echo 1. Creating device_info table:
%MYSQL_PATH% -u root -pCanteen@2026 -e "CREATE TABLE IF NOT EXISTS device_info (device_sn VARCHAR(50) PRIMARY KEY, device_name VARCHAR(100), alias_name VARCHAR(100), state VARCHAR(20), last_activity DATETIME, trans_times VARCHAR(50), trans_interval INT, ip_address VARCHAR(15), port INT, comm_password VARCHAR(50));" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo ✓ device_info table created successfully
) else (
    echo ✗ Failed to create device_info table
)
echo.

echo 2. Creating user_info table:
%MYSQL_PATH% -u root -pCanteen@2026 -e "CREATE TABLE IF NOT EXISTS user_info (user_pin VARCHAR(50) PRIMARY KEY, name VARCHAR(100), password VARCHAR(50), card_no VARCHAR(50), privilege INT, enabled INT, device_sn VARCHAR(50), FOREIGN KEY (device_sn) REFERENCES device_info(device_sn));" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo ✓ user_info table created successfully
) else (
    echo ✗ Failed to create user_info table
)
echo.

echo 3. Creating att_log table:
%MYSQL_PATH% -u root -pCanteen@2026 -e "CREATE TABLE IF NOT EXISTS att_log (id INT AUTO_INCREMENT PRIMARY KEY, user_pin VARCHAR(50), device_sn VARCHAR(50), verify_time DATETIME, verify_type INT, work_code INT, FOREIGN KEY (user_pin) REFERENCES user_info(user_pin), FOREIGN KEY (device_sn) REFERENCES device_info(device_sn));" pushdemo 2>nul

if %errorlevel% equ 0 (
    echo ✓ att_log table created successfully
) else (
    echo ✗ Failed to create att_log table
)
echo.

echo 4. Verifying table creation:
%MYSQL_PATH% -u root -pCanteen@2026 -e "SHOW TABLES;" pushdemo 2>nul
echo.

echo Press any key to exit...
pause >nul