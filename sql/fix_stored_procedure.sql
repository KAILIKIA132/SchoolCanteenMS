USE pushdemo;

DROP PROCEDURE IF EXISTS get_device_for_page;

DELIMITER $$
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
(select count(*) from att_log where device_sn=a.device_sn) act_att_count, 
mask, temperature, palm, time_zone, bioData_Stamp, idCard_Stamp, errorLog_Stamp
from device_info a where 1=1 ';
set sqlstr = CONCAT(sqlstr,cond);
set sqlstr = CONCAT(sqlstr," limit ",startRec, ",", pageSize);
set @s = sqlstr;
PREPARE stmt FROM @s;
EXECUTE stmt ;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;
