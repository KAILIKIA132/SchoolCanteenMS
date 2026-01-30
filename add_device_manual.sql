-- Manual Device Insertion Script
-- Replace the values below with your actual device information

-- Example: Insert a device with Serial Number TDBD250100333
INSERT INTO device_info (
    device_sn,
    device_name,
    alias_name,
    dept_id,
    state,
    last_activity,
    trans_times,
    trans_interval,
    log_stamp,
    op_log_stamp,
    photo_stamp,
    ipaddress,
    dev_language,
    push_version,
    time_zone,
    bioData_Stamp,
    idCard_Stamp,
    errorLog_Stamp,
    palm,
    mask,
    temperature
) VALUES (
    'TDBD254600293',                    -- Device Serial Number (REQUIRED - must be unique)
    'TDBD254600293(192.168.1.100)',     -- Device Name (format: SN(IP))
    '192.168.1.100',                    -- Alias Name (usually IP address)
    1,                                  -- Department ID
    'Online',                           -- State
    NOW(),                              -- Last Activity
    '00:00;14:05',                      -- Transfer Times
    1,                                  -- Transfer Interval (minutes)
    '0',                                -- Log Stamp
    '0',                                -- Operation Log Stamp
    '0',                                -- Photo Stamp
    '192.168.1.100',                    -- Device IP Address (REQUIRED)
    '69',                               -- Device Language (69 = English)
    '2.4.1',                            -- Push Version
    '+0800',                            -- Time Zone
    '0',                                -- BioData Stamp
    '0',                                -- ID Card Stamp
    '0',                                -- Error Log Stamp
    0,                                  -- Palm (0 = disabled)
    1,                                  -- Mask (1 = enabled)
    NULL                                -- Temperature
);

-- To use this script:
-- 1. Replace 'TDBD250100333' with your actual device serial number
-- 2. Replace '192.168.1.100' with your actual device IP address
-- 3. Run: docker-compose exec mysql mysql -uroot -proot -D pushdemo < add_device_manual.sql

