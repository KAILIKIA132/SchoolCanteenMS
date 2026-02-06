@echo off
echo Adding missing columns to device_info table...

REM Execute MySQL commands to add missing columns to device_info table
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -pCanteen@2026 -e "USE pushdemo; ALTER TABLE device_info ADD COLUMN bioData_Stamp VARCHAR(30); ALTER TABLE device_info ADD COLUMN idCard_Stamp VARCHAR(30) DEFAULT NULL; ALTER TABLE device_info ADD COLUMN errorLog_Stamp VARCHAR(30) DEFAULT NULL; DESCRIBE device_info;"

echo.
echo Columns have been added to the device_info table.
echo Press any key to exit...
pause > nul