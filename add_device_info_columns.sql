-- SQL script to add missing columns to device_info table
-- These columns are referenced in the DeviceInfo class but may be missing from the database

ALTER TABLE device_info ADD COLUMN bioData_Stamp VARCHAR(30);

ALTER TABLE device_info ADD COLUMN idCard_Stamp VARCHAR(30) DEFAULT NULL;

ALTER TABLE device_info ADD COLUMN errorLog_Stamp VARCHAR(30) DEFAULT NULL;

-- Verify the columns were added successfully
DESCRIBE device_info;