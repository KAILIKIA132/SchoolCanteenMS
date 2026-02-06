@echo off
setlocal

echo Adding missing columns to device_info table...

REM Path to mysql executable - adjust this according to your MySQL installation
set MYSQL_PATH=mysql
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=pushdemo
set DB_USER=root
set DB_PASS=123456

REM Alternative credentials based on your docker-compose.yml
if exist "docker-compose.yml" (
    set DB_PASS=root
)

echo Connecting to database %DB_NAME% on %DB_HOST%:%DB_PORT%

%MYSQL_PATH% -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASS% %DB_NAME% -e "
ALTER TABLE device_info 
ADD COLUMN IF NOT EXISTS time_zone VARCHAR(50) DEFAULT NULL;

ALTER TABLE device_info 
ADD COLUMN IF NOT EXISTS bioData_Stamp VARCHAR(30) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS idCard_Stamp VARCHAR(30) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS errorLog_Stamp VARCHAR(30) DEFAULT NULL;
"

if %ERRORLEVEL% EQU 0 (
    echo Successfully added missing columns to device_info table
) else (
    echo Failed to add columns. Please check your database connection settings.
)

pause
