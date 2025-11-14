SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE  IF NOT EXISTS pushdemo DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE `pushdemo`;

-- ----------------------------
-- Table structure for adv_info
-- ----------------------------
DROP TABLE IF EXISTS `adv_info`;
CREATE TABLE `adv_info`  (
  `adv_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `file_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `device_sn` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`adv_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for att_log
-- ----------------------------
DROP TABLE IF EXISTS `att_log`;
CREATE TABLE `att_log`  (
  `ATT_LOG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_PIN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `VERIFY_TYPE` int(11) NOT NULL,
  `VERIFY_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `STATUS` int(11) NOT NULL,
  `WORK_CODE` int(11) NULL DEFAULT NULL,
  `SENSOR_NO` int(11) NULL DEFAULT 0,
  `ATT_FLAG` int(11) NULL DEFAULT 0,
  `DEVICE_SN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `RESERVED1` int(11) NULL DEFAULT NULL,
  `RESERVED2` int(11) NULL DEFAULT NULL,
  `MASK` int(11) NULL DEFAULT 0,
  `TEMPERATURE` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ATT_LOG_ID`) USING BTREE,
  UNIQUE INDEX `USER_PIN`(`USER_PIN`, `VERIFY_TIME`, `DEVICE_SN`, `VERIFY_TYPE`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 243 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for att_photo
-- ----------------------------
DROP TABLE IF EXISTS `att_photo`;
CREATE TABLE `att_photo`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `FILE_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `SIZE` int(11) NULL DEFAULT 0,
  `CMD` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `FILE_PATH` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 298 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for device_attrs
-- ----------------------------
DROP TABLE IF EXISTS `device_attrs`;
CREATE TABLE `device_attrs`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ATTR_NAME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `ATTR_VALUE` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `DEVICE_SN`(`DEVICE_SN`, `ATTR_NAME`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for device_command
-- ----------------------------
DROP TABLE IF EXISTS `device_command`;
CREATE TABLE `device_command`  (
  `DEV_CMD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `CMD_CONTENT` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `CMD_COMMIT_TIMES` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `CMD_TRANS_TIMES` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `CMD_OVER_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `CMD_RETURN` varchar(11) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `CMD_RETURN_INFO` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  PRIMARY KEY (`DEV_CMD_ID`) USING BTREE,
  INDEX `DEVICE_SN`(`DEVICE_SN`, `CMD_RETURN`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for device_info
-- ----------------------------
DROP TABLE IF EXISTS `device_info`;
CREATE TABLE `device_info` (
  `DEVICE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(24) COLLATE utf8_unicode_ci NOT NULL,
  `DEVICE_NAME` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `ALIAS_NAME` varchar(24) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEPT_ID` int(11) DEFAULT '1',
  `STATE` varchar(30) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'Online',
  `LAST_ACTIVITY` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_TIMES` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_INTERVAL` int(11) NOT NULL DEFAULT '1',
  `LOG_STAMP` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `OP_LOG_STAMP` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PHOTO_STAMP` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FW_VERSION` varchar(40) COLLATE utf8_unicode_ci DEFAULT NULL,
  `USER_COUNT` int(11) DEFAULT '0',
  `FP_COUNT` int(11) DEFAULT '0',
  `TRANS_COUNT` int(11) DEFAULT '0',
  `FP_ALG_VER` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PUSH_VERSION` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `DEVICE_TYPE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IPADDRESS` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `DEV_LANGUAGE` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `PUSH_COMM_KEY` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `FACE_COUNT` int(11) DEFAULT '0',
  `FACE_ALG_VER` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REG_FACE_COUNT` int(11) DEFAULT '12',
  `DEV_FUNS` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TRANS_FLAG` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ERROR_DELAY` int(11) DEFAULT '60',
  `DELAY` int(11) DEFAULT '30',
  `REAL_TIME` int(11) DEFAULT '1',
  `PALM` int(11) DEFAULT '0',
  `MASK` int(11) DEFAULT '1',
  `TEMPERATURE` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ENCRYPT` int(11) DEFAULT '0',
  PRIMARY KEY (`DEVICE_ID`) USING BTREE,
  UNIQUE KEY `DEVICE_SN` (`DEVICE_SN`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for device_log
-- ----------------------------
DROP TABLE IF EXISTS `device_log`;
CREATE TABLE `device_log`  (
  `DEV_LOG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `OPERATOR` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `OPERATOR_TYPE` int(11) NULL DEFAULT NULL,
  `OP_TYPE` int(11) NULL DEFAULT NULL,
  `OP_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `VALUE1` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `VALUE2` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `VALUE3` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `RESERVED` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`DEV_LOG_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for meet_info
-- ----------------------------
DROP TABLE IF EXISTS `meet_info`;
CREATE TABLE `meet_info`  (
  `meet_info_id` int(11) NOT NULL AUTO_INCREMENT,
  `met_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `met_star_sign_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `met_lat_sign_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `ear_ret_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `lat_ret_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `met_str_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `met_end_tm` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `device_sn` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`meet_info_id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEVICE_SN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `SMS_TYPE` int(11) NOT NULL,
  `START_TIME` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `VALID_MINUTES` int(11) NOT NULL,
  `SMS_CONTENT` varchar(640) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for pers_bio_template
-- ----------------------------
DROP TABLE IF EXISTS `pers_bio_template`;
CREATE TABLE `pers_bio_template`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `USER_PIN` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `DEVICE_SN` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `VALID` int(1) NOT NULL DEFAULT 1,
  `IS_DURESS` int(1) NOT NULL DEFAULT 0,
  `BIO_TYPE` int(11) NOT NULL DEFAULT 0,
  `VERSION` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `DATA_FORMAT` int(11) NOT NULL DEFAULT 0,
  `TEMPLATE_NO` int(11) NOT NULL DEFAULT 0,
  `TEMPLATE_NO_INDEX` int(11) NOT NULL DEFAULT 0,
  `SIZE` int(11) NOT NULL,
  `TEMPLATE_DATA` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `USER_ID`(`USER_ID`, `USER_PIN`, `TEMPLATE_NO`, `BIO_TYPE`, `DEVICE_SN`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_PIN` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `PRIVILEGE` int(11) NULL DEFAULT 0,
  `NAME` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `PASSWORD` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `FACE_GROUP_ID` int(11) NULL DEFAULT 1,
  `ACC_GROUP_ID` int(11) NULL DEFAULT 1,
  `DEPT_ID` int(11) NULL DEFAULT 1,
  `IS_GROUP_TZ` int(11) NULL DEFAULT 1,
  `VERIFY_TYPE` int(11) NULL DEFAULT 1,
  `MAIN_CARD` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `VICE_CARD` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `EXPIRES` int(11) NULL DEFAULT 0,
  `TZ` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `PHOTO_ID_NAME` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `PHOTO_ID_SIZE` int(11) NULL DEFAULT NULL,
  `PHOTO_ID_CONTENT` longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  `DEVICE_SN` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `MEET_CODE` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `category` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`USER_ID`) USING BTREE,
  UNIQUE INDEX `USER_PIN`(`USER_PIN`, `DEVICE_SN`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for api_verification_report
-- ----------------------------
DROP TABLE IF EXISTS `api_verification_report`;
CREATE TABLE `api_verification_report`  (
  `report_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_pin` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `student_id` varchar(24) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `verification_time` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `api_call_time` datetime NOT NULL,
  `meal_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT 'SUCCESS or FAILED',
  `response_code` int(11) NULL DEFAULT NULL,
  `response_message` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  `error_message` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL,
  `api_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`report_id`) USING BTREE,
  INDEX `idx_user_pin`(`user_pin`) USING BTREE,
  INDEX `idx_verification_time`(`verification_time`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_api_call_time`(`api_call_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------



-- Procedure structure for get_device_for_page
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_device_for_page`;
delimiter ;;
CREATE PROCEDURE `get_device_for_page`(IN cond varchar(200),IN startRec int, IN pageSize int)
BEGIN
	
DECLARE sqlstr varchar(1000);

set sqlstr ='select device_id , device_sn , device_name, alias_name, dept_id, 

							state, last_activity, trans_times, trans_interval, log_stamp, 

							op_log_stamp, photo_stamp, fw_version, user_count, fp_count, 

							trans_count, fp_alg_ver, push_version, device_type, ipaddress, 

							dev_language, push_comm_key, face_count, face_alg_ver, reg_face_count, dev_funs,

							(select count(*) from user_info where device_sn=a.DEVICE_SN) act_user_count,

							(select count(*) from pers_bio_template where DEVICE_SN=a.device_sn and bio_type=1) act_fp_count, 

							(select count(*) from pers_bio_template where DEVICE_SN=a.device_sn and bio_type=2 and template_no=0) act_face_count,
							
							(select count(*) from att_log where device_sn=a.device_sn) act_att_count, mask, temperature, palm,
							time_zone, bioData_Stamp, idCard_Stamp, errorLog_Stamp

							from device_info a where 1=1 ';

set sqlstr = CONCAT(sqlstr,cond);

set sqlstr = CONCAT(sqlstr," limit ",startRec, ",", pageSize);

set @s = sqlstr;

PREPARE stmt FROM @s;


EXECUTE stmt ;

DEALLOCATE PREPARE stmt;

END
;;
delimiter ;





