-- Consolidated Update Script for Windows Deployment
-- 1. Create admin_users table and add admin user
CREATE TABLE IF NOT EXISTS `admin_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) DEFAULT 'admin',
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Password is 'admin123' hashed with SHA-256
INSERT INTO `admin_users` (`username`, `password`, `role`) 
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin') 
ON DUPLICATE KEY UPDATE `password`='240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9';

-- 2. Add missing columns to device_info table
-- Check if columns exist before adding (using separate statements to avoid errors if some exist)
-- Note: MySQL doesn't support IF NOT EXISTS for columns in ALTER TABLE directly in all versions easily without procedure.
-- Running these blindly might error if they exist, but is safe to retry.
ALTER TABLE `device_info` ADD COLUMN `time_zone` VARCHAR(30) DEFAULT NULL;
ALTER TABLE `device_info` ADD COLUMN `bioData_Stamp` VARCHAR(30) DEFAULT NULL;
ALTER TABLE `device_info` ADD COLUMN `idCard_Stamp` VARCHAR(30) DEFAULT NULL;
ALTER TABLE `device_info` ADD COLUMN `errorLog_Stamp` VARCHAR(30) DEFAULT NULL;

-- 3. Add device_sn column to api_verification_report table
ALTER TABLE `api_verification_report` ADD COLUMN `device_sn` VARCHAR(50) DEFAULT NULL;
