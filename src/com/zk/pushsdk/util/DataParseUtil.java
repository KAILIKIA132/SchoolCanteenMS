package com.zk.pushsdk.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.zk.exception.ParseDataException;
import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.AttLog;
import com.zk.pushsdk.po.DeviceLog;
import com.zk.pushsdk.po.PersonBioTemplate;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.process.UploadProcess;
import java.util.*;

public class DataParseUtil {

	private static Logger logger = Logger.getLogger(UploadProcess.class);
	
	/**
	 * Gets the face template from data. save it into the database
	 * @param data
	 * @param deviceSn
	 * @return
	 */
	public static int parseFace(String data, String deviceSn) {
		if (null == data || data.isEmpty()
				|| null == deviceSn || deviceSn.isEmpty()) {
			return -1;
		}
		logger.info("face data:\n"+data);
		List<PersonBioTemplate> list = new ArrayList<PersonBioTemplate>();
		String[] faces = data.split("\n");
		for (String string : faces) {
			String fieldsStr = string.substring(string.indexOf("FACE ") + "FACE ".length());
			String[] fields = fieldsStr.split("\t");
			PersonBioTemplate face = parseFace(fields, deviceSn);
			
			if (null == face) {
				return -1;
			}
			list.add(face);

		}
		/**Save the templates into the database*/
		ManagerFactory.getBioTemplateManager().createBioTemplate(list);
		logger.info("face size:" + list.size());
		return 0;
	}
	
	/**
	 * Gets face PersonBioTemplate from data.
	 * 
	 * @param fields
	 * @param deviceSn
	 * @return
	 */
	private static PersonBioTemplate parseFace(String[] fields, String deviceSn) {
		PersonBioTemplate template = new PersonBioTemplate();
		/**Gets all the field value*/
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				template.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("FID")) {
				try {
					int templateNo = Integer.valueOf(string.substring(string.indexOf("FID=") + "FID=".length()));
					template.setTemplateNo(templateNo);
				} catch (Exception e) {
					template.setTemplateNo(0);
				}
			} else if (string.startsWith("SIZE")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("SIZE=") + "SIZE=".length()));
					template.setSize(size);
				} catch (Exception e) {
					template.setSize(0);
				}
			} else if (string.startsWith("VALID")) {
				try {
					int valid = Integer.valueOf(string.substring(string.indexOf("VALID=") + "VALID=".length()));
					template.setValid(valid);
				} catch (Exception e) {
					template.setValid(1);
				}
			} else if (string.startsWith("TMP")) {
				template.setTemplateData(string.substring(string.indexOf("TMP=") + "TMP=".length()));
			}
		}
		
		template.setIsDuress(0);
		template.setBioType(Constants.BIO_TYPE_FACE);//Biometrics type: face
		template.setDataFormat(Constants.BIO_DATA_FMT_ZK);//Data format: zk type
		template.setVersion(Constants.BIO_VERSION_FACE_7);//Face algorithm version
		template.setTemplateNoIndex(0);
		template.setDeviceSn(deviceSn);
		
		/**Gets the user info by user ID and device SN*/
		UserInfo userInfo = ManagerFactory.getUserInfoManager().getUserInfoByPinAndSn(template.getUserPin(), template.getDeviceSn());
		
		if (null == userInfo) {
			return null;
		}
		
		template.setUserId(userInfo.getUserId());
		return template;
	}
	

	/**
	 * Gets the user photo from data. save it.
	 * 
	 * @param data
	 * @param deviceSn
	 * @return
	 */
	public static int parseUserPic(String data, String deviceSn) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != data && !data.isEmpty()) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("USERPIC ") + "USERPIC ".length());
				String[] fields = fieldsStr.split("\t");
				UserInfo info = parseUserPic(fields, deviceSn);
				
				if (null == info) {
					return -1;
				}
				list.add(info);
			}
		}
		/**save it into database*/
		int ret = ManagerFactory.getUserInfoManager().updateUserPic(list);
		logger.info("userpic size:" + list.size());
		return ret;
		
	}
	
	public static int parseBioPhotoPic(String data, String deviceSn) {
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != data && !data.isEmpty()) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("BIOPHOTO ") + "BIOPHOTO ".length());
				String[] fields = fieldsStr.split("\t");
				UserInfo info = parseUserPic(fields, deviceSn);
				
				if (null == info) {
					return -1;
				}
				list.add(info);
			}
		}
		/**save it into database*/
		int ret = ManagerFactory.getUserInfoManager().updateUserPic(list);
		logger.info("userpic size:" + list.size());
		return ret;
		
	}

	/**
	 * Gets the user info by user photo data.
	 * 
	 * @param fields
	 * @param deviceSn
	 * @return
	 */
	private static UserInfo parseUserPic(String[] fields, String deviceSn) {
		UserInfo info = new UserInfo();
		/**Gets all the field value*/
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				info.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("FileName")) {
				info.setPhotoIdName(string.substring(string.indexOf("FileName=") + "FileName=".length()));
			} else if (string.startsWith("Size")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("Size=") + "Size=".length()));
					info.setPhotoIdSize(size);
				} catch (Exception e) {
					return null;
				}
			} else if (string.startsWith("Content")) {
				info.setPhotoIdContent(string.substring(string.indexOf("Content=") + "Content=".length()));
			} 
		}
		/**gets the user info*/
		UserInfo userInfo = ManagerFactory.getUserInfoManager().getUserInfoByPinAndSn(info.getUserPin(), deviceSn);
		if (null == userInfo) {
			return null;
		}
		/**update the user info*/
		userInfo.setPhotoIdName(info.getPhotoIdName());
		userInfo.setPhotoIdSize(info.getPhotoIdSize());
		userInfo.setPhotoIdContent(info.getPhotoIdContent());

		return userInfo;
	}
	
	/**
	 * Gets operation logs from data, save it.
	 * 
	 * @param data
	 * @param deviceSn
	 * @return
	 * @throws ParseDataException
	 */
	public static int parseOPLog(String data, String deviceSn) throws ParseDataException{
		List<DeviceLog> list = new ArrayList<DeviceLog>();
		if (null != data && !"".equals(data)) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("OPLOG ") + "OPLOG ".length());
				String[] fields = fieldsStr.split("\t");
				try {
					DeviceLog log = parseOpLog(fields);
					if (null != log) {
						log.setDeviceSn(deviceSn);
						list.add(log);
						/**Add operate log to monitor list*/
						PushUtil.monitorList.add(log);
						if (PushUtil.monitorList.size() > PushUtil.getMonitorSize()) {
							PushUtil.monitorList.remove(0);
						}
					}
				} catch (ParseDataException e) {
					throw e;
				}
			}
		}
		ManagerFactory.getDeviceLogManager().addDeviceLog(list);
		logger.info("oplog size:" + list.size());
		return 0;
	}
	
	/**
	 * Gets DeviceLog from field data
	 * @param fields
	 * @return
	 * @throws ParseDataException
	 */
	private static DeviceLog parseOpLog(String[] fields) throws ParseDataException{
		if (null == fields || fields.length <= 0) {
			return null;
		}
		DeviceLog log = new DeviceLog(); 
		try {
			int opType = Integer.valueOf(fields[0]);
			log.setOperateType(opType);
			
			log.setOperator(fields[1]);
			log.setOperateTime(fields[2]);
			log.setValue1(fields[3]);
			log.setValue2(fields[4]);
			log.setValue3(fields[5]);
			log.setReserved(fields[6]);
		} catch (Exception e) {
			throw new ParseDataException("data parse error");
		}
		return log;
	}
	
	
	/**
	 * Split the user data by \n, and get the keys/values to parse to UserInfo,
	 * add to list, then save to database.
	 * @param data
	 * @param deviceSn
	 * @return
	 */
	public static int parseUserData(String data, String deviceSn) {
		logger.info("user data:\n" + data);
		List<UserInfo> list = new ArrayList<UserInfo>();
		if (null != data && !"".equals(data)) {
			String[] userInfos = data.split("\n");
			for (String string : userInfos) {
				String fieldsStr = string.substring(string.indexOf("USER ") + "USER ".length());
				String[] fields = fieldsStr.split("\t");
				UserInfo info = parseUser(fields);
				info.setDeviceSn(deviceSn);
				
				list.add(info);
			}
		}
		ManagerFactory.getUserInfoManager().createUserInfo(list);
		logger.info("user size:" + data);
		return 0;
	}

	/**
	 * Parse every user info by key=value then return <code>UserInfo</code> entity
	 * @param fields
	 * @return
	 */
	private static UserInfo parseUser(String[] fields) {
		UserInfo info = new UserInfo();
		for (String string : fields) {
			if (string.startsWith("PIN")) {
				info.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
			} else if (string.startsWith("Name")) {
				info.setName(string.substring(string.indexOf("Name=") + "Name=".length()));
			} else if (string.startsWith("Pri")) {
				try {
					int pri = Integer.valueOf(string.substring(string.indexOf("Pri=") + "Pri=".length()));
					info.setPrivilege(pri);
				} catch (Exception e) {
					info.setPrivilege(0);
				}
			} else if (string.startsWith("Passwd")) {
				info.setPassword(string.substring(string.indexOf("Passwd=") + "Passwd=".length()));
			} else if (string.startsWith("Card")) {
				info.setMainCard(string.substring(string.indexOf("Card=") + "Card=".length()));
			} else if (string.startsWith("Grp")) {
				try {
					int accGroupId = Integer.valueOf(string.substring(string.indexOf("Grp=") + "Grp".length()));
					info.setAccGroupId(accGroupId);
				} catch (Exception e) {
					info.setAccGroupId(1);
				}
			} else if (string.startsWith("TZ")) {
				info.setTz(string.substring(string.indexOf("TZ=") + "TZ=".length()));
			}
		}
		return info;
	}
	
	/**
	 * Gets attendance record from data and save it
	 * @param data
	 * @param deviceSn
	 * @return
	 */
	public static int parseAttlog(String data, String deviceSn) {
		List<AttLog> list = new ArrayList<AttLog>();
		if (null != data && !"".equals(data)) {
			String[] attLogs = data.split("\n");
			for (String string : attLogs) {
				String[] attValues = string.split("\t");
				AttLog log = new AttLog();
				
				DeviceLog devLog = new DeviceLog();
				devLog.setDeviceSn(deviceSn);
				devLog.setOperator(attValues[0]);
				devLog.setOperateTime(attValues[1]);
				
				log.setDeviceSn(deviceSn);
				log.setUserPin(attValues[0]);
				log.setVerifyTime(attValues[1]);
				StringBuilder sb = new StringBuilder();
				try {
					sb.append(PushUtil.ATT_STATUS.get(attValues[2]));
					sb.append(":");
					int status = Integer.valueOf(attValues[2]);
					log.setStatus(status);
				} catch (Exception e) {
					log.setStatus(0);
				}
				try {
					sb.append(PushUtil.ATT_VERIFY.get(attValues[3]));
					int verifyType = Integer.valueOf(attValues[3]);
					log.setVerifyType(verifyType);
				} catch (Exception e) {
					log.setVerifyType(1);
				}
				try {
					int workcode = Integer.valueOf(attValues[4]);
					log.setWorkCode(workcode);
				} catch (Exception e) {
					log.setWorkCode(0);
				}
				try {
					int reserved1 = Integer.valueOf(attValues[5]);
					log.setReserved1(reserved1);
				} catch (Exception e) {
					log.setReserved1(0);
				}
				try {
					int reserved2 = Integer.valueOf(attValues[6]);
					log.setReserved2(reserved2);
				} catch (Exception e) {
					log.setReserved2(0);
				}
				
				try {
					int maskFlag = Integer.valueOf(attValues[7]);
					log.setMaskFlag(maskFlag);
					devLog.setMaskFlag(maskFlag);
				} catch (Exception e) {
					log.setMaskFlag(255);
				}
				
				try {
					String tempReading = attValues[8];
					log.setTemperatureReading(tempReading);
					devLog.setTemperatureReading(tempReading);
				} catch (Exception e) {
					log.setTemperatureReading("255");
				}
				devLog.setOperateTypeStr(sb.toString());
				/**Add operate log to monitor list*/
				PushUtil.monitorList.add(devLog);
				if (PushUtil.monitorList.size() > PushUtil.getMonitorSize()) {
					PushUtil.monitorList.remove(0);
				}
				list.add(log);
			}
		}
		int ret = ManagerFactory.getAttLogManager().createAttLog(list);
		logger.info("attlog size:" + list.size());
		return ret;
	}
	
	/**
	 * Gets finger print template from data and save it.
	 * @param data
	 * @param deviceSn
	 * @return
	 */
	public static int parseFingerPrint(String data, String deviceSn) {
		logger.info("finger data:\n" + data);
		List<PersonBioTemplate> list = new ArrayList<PersonBioTemplate>();
		String fieldsStr=null;
		if (null != data && !"".equals(data)) {
			String[] fps = data.split("\n");
			for (String string : fps) {
				if(data.startsWith("FP ")){
				fieldsStr = string.substring(string.indexOf("FP ") + "FP ".length());
				}else if(data.startsWith("BIODATA ")){
				fieldsStr = string.substring(string.indexOf("BIODATA ") + "BIODATA ".length());
				}
				String[] fields = fieldsStr.split("\t");
				PersonBioTemplate fp = parseFingerPrint(fields, deviceSn);
				
				if (null == fp) {
					return -1;
				}
				list.add(fp);
			}
		}
		ManagerFactory.getBioTemplateManager().createBioTemplate(list);
		logger.info("fp size:" + list.size());
		return 0;
	}

	/**
	 * Gets FP PersonBioTemplate by field data.
	 * @param fields
	 * @param deviceSn
	 * @return
	 */
	private static PersonBioTemplate parseFingerPrint(String[] fields, String deviceSn) {
		PersonBioTemplate template = new PersonBioTemplate();
		
		for (String string : fields) {
			if (string.startsWith("PIN") || string.startsWith("Pin")) {
				if(string.startsWith("PIN")){
				template.setUserPin(string.substring(string.indexOf("PIN=") + "PIN=".length()));
				}else if(string.startsWith("Pin")){
					template.setUserPin(string.substring(string.indexOf("Pin=") + "Pin=".length()));
				}
			} else if (string.startsWith("FID")) {
				try {
					int templateNo = Integer.valueOf(string.substring(string.indexOf("FID=") + "FID=".length()));
					template.setTemplateNo(templateNo);
				} catch (Exception e) {
					template.setTemplateNo(0);
				}
			} else if (string.startsWith("No")) {
				try {
					int templateNo = Integer.valueOf(string.substring(string.indexOf("No=") + "No=".length()));
					template.setTemplateNo(templateNo);
				} catch (Exception e) {
					template.setTemplateNo(0);
				}
			}else if (string.startsWith("Size")) {
				try {
					int size = Integer.valueOf(string.substring(string.indexOf("Size=") + "Size=".length()));
					template.setSize(size);
				} catch (Exception e) {
					template.setSize(0);
				}
			} else if (string.startsWith("Valid")) {
				try {
					int valid = Integer.valueOf(string.substring(string.indexOf("Valid=") + "Valid".length()));
					template.setValid(valid);
				} catch (Exception e) {
					template.setValid(1);
				}
			} else if (string.startsWith("Index")) {
				try {
					int index = Integer.valueOf(string.substring(string.indexOf("Index=") + "Index".length()+1));
					template.setTemplateNoIndex(index);
				} catch (Exception e) {
					template.setValid(1);
				}
			}else if (string.startsWith("TMP")) {
				template.setTemplateData(string.substring(string.indexOf("TMP=") + "TMP=".length()));
			}else if (string.startsWith("Tmp")) {
				template.setTemplateData(string.substring(string.indexOf("Tmp=") + "Tmp=".length()));
				String s=string.substring(string.indexOf("Tmp=") + "Tmp=".length());
				int y=s.length();
				template.setSize(y);
			}
		}
		template.setIsDuress(0);
		
		template.setBioType(Constants.BIO_TYPE_FP);
		template.setDataFormat(Constants.BIO_DATA_FMT_ZK);
		for (String string : fields) {
			if (string.startsWith("MajorVer")){
				//MajorVer
				int a = Integer.valueOf(string.substring(string.indexOf("MajorVer=") + "MajorVer=".length()));
				String MajorVer=Integer.toString(a);
				template.setVersion(MajorVer);
			}
		}
		for (String string : fields) {
			if (string.startsWith("Type")){
				try {
					int type = Integer.valueOf(string.substring(string.indexOf("Type=") + "Type=".length()));
					template.setBioType(type);
					if(type==9){
						template.setVersion(Constants.BIO_VERSION_FACE_7);
					}
				} catch (Exception e) {
					template.setBioType(1);
				}
			}
		}
		
		template.setDeviceSn(deviceSn);
		
		UserInfo userInfo = ManagerFactory.getUserInfoManager().getUserInfoByPinAndSn(template.getUserPin(), template.getDeviceSn());
		
		if (null == userInfo) {
			return null;
		}
		
		template.setUserId(userInfo.getUserId());
		if(template.getVersion()==null){
			template.setVersion(Constants.BIO_VERSION_FP_12);
		}
		return template;
	}
	
	public static String getDateTimeInGMTFormat() {
		Calendar cd = Calendar.getInstance();
		SimpleDateFormat gmtFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String str = gmtFormat.format(cd.getTime());
        return str;
	}
}
