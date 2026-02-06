-- Add missing time_zone column to device_info table
ALTER TABLE device_info 
ADD COLUMN IF NOT EXISTS time_zone VARCHAR(50) DEFAULT NULL;

-- Also add the other missing columns that appear to be expected by the Java code
ALTER TABLE device_info 
ADD COLUMN IF NOT EXISTS bioData_Stamp VARCHAR(30) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS idCard_Stamp VARCHAR(30) DEFAULT NULL,
ADD COLUMN IF NOT EXISTS errorLog_Stamp VARCHAR(30) DEFAULT NULL;