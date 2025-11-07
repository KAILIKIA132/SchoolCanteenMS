package com.zk.pushsdk.util;

import java.text.MessageFormat;
import java.util.Date;

import com.zk.pushsdk.po.Adv;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.pushsdk.po.MeetInfo;
import com.zk.pushsdk.po.Message;
import com.zk.pushsdk.po.PersonBioTemplate;
import com.zk.pushsdk.po.UserInfo;
/**
 * Device command utility. it contain all the device commands
 * @author seiya
 *
 */
public class DevCmdUtil {
	
	/**
	 * Gets object of command "UPDATE" FP template
	 * @param template
	 * FP template
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateFPCommand(PersonBioTemplate template, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the template
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = template.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Get device command entity*/
		return getNewCommand(sn, getUpdateFpContent(template));
	}
	
	/**
	 * Get UPDATE finger print command content
	 * @param template
	 * @return
	 */
	public static String getUpdateFpContent(PersonBioTemplate template) {
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_FINGER, 
				template.getUserPin(), 
				template.getTemplateNo(), 
				String.valueOf(template.getSize()), 
				template.getValid(), 
				template.getTemplateData());
	}
	
	/**
	 * Gets object of command UPDATE Face template
	 * @param template
	 * face template
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateFaceCommand(PersonBioTemplate template, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the template
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = template.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Get device command entity*/
		return getNewCommand(sn, getUpdateFaceContent(template));
	}
	/**
	 * Gets object of command UPDATE biodata abhishek
	 * @param template
	 * face template
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateBiodataCommand(PersonBioTemplate template, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the template
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = template.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Get device command entity*/
		return getNewCommand(sn, getUpdateBioDataContent(template));
	}
	
	/**
	 * Get UPDATE face template command content
	 * @param template
	 * @return
	 */
	public static String getUpdateBioDataContent(PersonBioTemplate template) {
		
		String major = "", minor = "";
		
		if(template.getVersion()!=null && !template.getVersion().isEmpty()){
			major = template.getVersion().contains(".") ? template.getVersion().toString().split("\\.")[0] : template.getVersion();
			minor = template.getVersion().contains(".") ? template.getVersion().toString().split("\\.")[1] : "0";
		} else {
			major = "58";
			minor = "12";
		}
		
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_BIODATA, 
				template.getUserPin(), //Pin=70059
				template.getTemplateNo(), //No=0
				template.getTemplateNoIndex(), //Index=1
				template.getValid(), //Valid=0
				template.getIsDuress(),//Duress=9
				template.getBioType(),//Type=9
				major,
				minor,
				template.getDataFormat(),
				template.getTemplateData());
	}
	
	/**
	 * Get UPDATE face template command content
	 * @param template
	 * @return
	 */
	public static String getUpdateFaceContent(PersonBioTemplate template) {
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_FACE, 
				template.getUserPin(), 
				template.getTemplateNo(), 
				String.valueOf(template.getSize()), 
				template.getValid(), 
				template.getTemplateData());
	}
	
	/**
	 * Gets the command of Online enroll finger print.
	 * @param userPin
	 * user ID
	 * @param fingerId
	 * finger id, Range: 0 ~ 9
	 * @param retryTimes
	 * retry times
	 * @param isOverWrite
	 * overwrite or not
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getEnrollFpCommand(String userPin, int fingerId, 
			int retryTimes, int isOverWrite, String deviceSn) {
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_ENROLL_FP, 
				userPin,
				fingerId,
				retryTimes,
				isOverWrite);
		/**Gets Command object*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of Delete user info
	 * @param userInfo
	 * user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteUserCommand(UserInfo userInfo, String deviceSn) {
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_USERINFO, 
				userInfo.getUserPin());
		/**
		 * if there is no device SN. Gets the SN from the device which has the user info
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}
	
	/**
	 * Gets the command of Delete user photo
	 * @param userInfo
	 * user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteUserPicCommand(UserInfo userInfo, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_USERPIC, 
				userInfo.getUserPin());
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}
	
	/**
	 * Gets the command of UPDATE user photo
	 * @param userInfo
	 * user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateUserPicCommand(UserInfo userInfo, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, getUpdateUserPicContent(userInfo));
	}
	
	/**
	 * Gets the command of UPDATE user bio photo
	 * @param userInfo
	 * user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateBioPhotoCommand(UserInfo userInfo, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, getUpdateBioPhotoContent(userInfo));
	}
	/**abhishek
	 * Gets the command of UPDATE user bio Data
	 * @param userInfo
	 * user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateBioDataCommand(UserInfo userInfo, String deviceSn) {
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, getUpdateBioDataContent(userInfo));
	}
	

	/**
	 * Get UPDATE user picture command content
	 * @param userInfo
	 * @return
	 */
	public static String getUpdateUserPicContent(UserInfo userInfo) {
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_USERPIC, 
				userInfo.getUserPin(),
				String.valueOf(userInfo.getPhotoIdSize()),
				userInfo.getPhotoIdContent());
	}
	
	/**
	 * Get UPDATE user bio photo command content
	 * @param userInfo
	 * @return
	 */
	private static String getUpdateBioPhotoContent(UserInfo userInfo) {
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_BIOPHOTO, 
				userInfo.getUserPin(),
				String.valueOf(2),
				String.valueOf(userInfo.getPhotoIdSize()),
				userInfo.getPhotoIdContent());
	}
	/**abhishek
	 * Get UPDATE user bio Data command content
	 * @param userInfo
	 * @return
	 */
	private static String getUpdateBioDataContent(UserInfo userInfo) {
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_BIOPHOTO, 
				userInfo.getUserPin(),
				String.valueOf(2),
				String.valueOf(userInfo.getPhotoIdSize()),
				userInfo.getPhotoIdContent());
	}
	
	/**
	 * Gets the command of DELETE finger print template
	 * @param bioTemplate
	 * FP template
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteFpCommand(PersonBioTemplate bioTemplate, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_FINGER, 
				bioTemplate.getUserPin(), 
				bioTemplate.getTemplateNo());
		/**
		 * if there is no device SN. Gets the SN from the device which has the template
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = bioTemplate.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}
	
	/**
	 * Gets the command of DELETE Face template
	 * @param bioTemplate
	 * Face Template
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteFaceCommand(PersonBioTemplate bioTemplate, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_FACE, 
				bioTemplate.getUserPin(), 
				bioTemplate.getTemplateNo());
		/**
		 * if there is no device SN. Gets the SN from the device which has the template
		 */
		String sn = null;
		if (null == deviceSn || "".equals(deviceSn)) {
			sn = bioTemplate.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}
	
	/**
	 * Gets the command of UPDATE SMS
	 * @param message
	 * SMS
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateSmsCommand(Message message, String deviceSn) {
		/**Gets the current language*/
		String lang = "";
		lang = PushUtil.getDeviceLangBySn(deviceSn);
		/**Get sms content and set the encoding*/
		String smsContent = PushUtil.encodingByDeviceLang(message.getSmsContent(), lang);
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_SMS, 
				smsContent, 
				message.getSmsType(),
				message.getId(),
				message.getValidMinutes(),
				message.getStartTime());
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of UPDATE User SMS
	 * @param userPin
	 * user ID
	 * @param smsId
	 * SMS ID
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateUserSmsCommand(String userPin, String smsId, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_USER_SMS,
				userPin,
				smsId);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of DELETE SMS
	 * @param smsId
	 * SMS ID
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteSmsCommand(String smsId, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_SMS,
				smsId);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of PutFile
	 * @param srcFile
	 * the source file in the server. including the path of the file
	 * @param destFile
	 * Destination file. including the path
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getPutFileCommand(String srcFile, String destFile, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_PUT_FILE,
				srcFile,
				destFile);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of automatic Proofreading attendance record
	 * @param startTime
	 * Start time
	 * @param endTime
	 * End time
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getVerifySumAttLogCommand(String startTime, String endTime, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_VERIFY_SUM_ATTLOG,
				startTime,
				endTime);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of GetFile
	 * @param file
	 * the file which you want. including the path.
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getGetFileCommand(String file, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_GET_FILE,
				file);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Gets the command of SHELL
	 * @param cmd
	 * the SHELL command
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getShellCommand(String cmd, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_SHELL, 
				cmd);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
	
	/**
	 * Get the command of SET Option to device
	 * @param option
	 * options: key=value
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getSetOptionCommand(String option, String deviceSn) {
		/**Format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_SET_OPTION, 
				option);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}
		
	/**
	 * Gets the command of UPDATE User Info.
	 * @param userInfo
	 * User Info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateUserCommand(UserInfo userInfo, String deviceSn) {
		String sn = null;
		String lang = "";
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		if (null == deviceSn || "".equals(deviceSn)) {
			sn = userInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets current language*/
		lang = PushUtil.getDeviceLangBySn(sn);
		/**Get User name, convert it to UTF-8*/
		//String name = PushUtil.encodingByDeviceLang(userInfo.getName(), lang);
		//userInfo.setName(name);
		/**Gets Command objects*/
		return getNewCommand(sn, getUpdateUserContent(userInfo));
	}
	
	/**
	 * Format the command content UPDATE USERINFO 
	 * @param userInfo
	 * @return
	 */
	public static String getUpdateUserContent(UserInfo userInfo) {
		/**Format the command content*/
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_USERINFO, 
				userInfo.getUserPin(), 
				userInfo.getName(),
				userInfo.getPrivilege(),
				userInfo.getPassword(),
				userInfo.getMainCard(), 
				userInfo.getAccGroupId(),
				userInfo.getTz(),
				userInfo.getCategory());
	}
	
	/**
	 * Gets the command of INFO
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getINFOCommand(String deviceSn) {
		/**Gets INFO Command objects*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_INFO);
	}
	
	/**
	 * Gets the command of CLEAR ALL DATA
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearAllDataCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_CLEAR_DATA);
	}
	
	/**
	 * Gets the command of CLEAR ATTENDANCE RECORD
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearAttLogCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_CLEAR_LOG);
	}
	
	/**
	 * Gets the command of CLEAR ATTENDANCE PHOTO
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearPhotoCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_CLEAR_PHOTO);
	}
	
	/**
	 * Gets the command of REBOOT DEVICE
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getRebootCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_REBOOT);
	}
	
	/**
	 * Gets the command of CHECK(new data)
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getCheckCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_CHECK);
	}
	
	/**
	 * Gets the command of RELOAD DEVICE CONFIGURATION
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getReloadOptionCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_RELOAD_OPTIONS);
	}
	
	/**
	 * Gets the command of CHECK AND SEND LOG
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getLOGCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_LOG);
	}
	
	/**
	 * Gets the command of UNLOCK THE DOOR
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUnLockCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_AC_UNLOCK);
	}
	
	/**
	 * Gets the command of CLOSE ALARM
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUnAlarmCommand(String deviceSn) {
		/**Gets the command*/
		return getNewCommand(deviceSn, Constants.DEV_CMD_AC_UNALARM);
	}
	
	/**
	 * Gets the command of QUERY ATTENDANCE RECORD
	 * @param startTime
	 * Start time
	 * @param endTime
	 * End time
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getQueryAttLogCommand(String startTime, String endTime, String deviceSn) {
		/**Format the command content*/
		String devCmdContent = MessageFormat.format(
				Constants.DEV_CMD_DATA_QUERY_ATTLOG, startTime, endTime);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, devCmdContent);
	}
	
	/**
	 * Gets the command of QUERY ATTENDANCE PHOTO
	 * @param startTime
	 * Start time
	 * @param endTime
	 * End Time
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getQueryAttPhotoCommand(String startTime, String endTime, String deviceSn) {
		/**Format the command content*/
		String devCmdContent = MessageFormat.format(
				Constants.DEV_CMD_DATA_QUERY_ATTPHOTO, startTime, endTime);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, devCmdContent);
	}
	
	/**
	 * Gets the command of QUREY USER INFO
	 * @param userPin
	 * User ID
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getQueryUserInfoCommand(String userPin, String deviceSn) {
		/**Format the command content*/
		String devCmdContent = MessageFormat.format(
				Constants.DEV_CMD_DATA_QUERY_USERINFO, userPin);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, devCmdContent);
	}
	
	/**
	 * Gets the command of QUERY FINGER TEMPLATES
	 * @param userPin
	 * User ID
	 * @param fingerId
	 * Finger index, Range:0~9
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getQueryFingerTempCommand(String userPin, String fingerId, String deviceSn) {
		String devCmdContent = "";
		if (null == fingerId || fingerId.isEmpty() ) {
			/**Gets the FP template by user ID*/
			devCmdContent = MessageFormat.format(
					Constants.DEV_CMD_DATA_QUERY_FINGERTMP_ALL, userPin);
		} else {
			/**Gets the FP template by User ID and finger index*/
			devCmdContent = MessageFormat.format(
					Constants.DEV_CMD_DATA_QUERY_FINGERTMP, userPin, fingerId);
		}
		/**Gets Command objects*/
		return getNewCommand(deviceSn, devCmdContent);
	}
	//abhi
	public static DeviceCommand getQueryUpdateCommand(String sn,String cmd) {
//		String devCmdContent = "";
//			/*devCmdContent = MessageFormat.format(
//				Constants.DEV_CMD_DA*/TA_UPDATE_BIODATA, Pin, No, Index, Valid, Duress, Type, MajorVer, MinorVer, Format, Tmp);
		return getNewCommand(sn, cmd);
	}
	//end
	/**
	 * Gets the command of user-defined.
	 * @param deviceSn
	 * Device SN
	 * @param content
	 * command content
	 * @return
	 * Command objects
	 */
	private static DeviceCommand getNewCommand(String deviceSn, String content) {
		DeviceCommand command = new DeviceCommand();
		command.setDeviceSn(deviceSn);
		command.setCmdContent(content);
		command.setCmdCommitTime(PushUtil.dateFormat.format(new Date()));
		command.setCmdTransTime("");
		command.setCmdOverTime("");
		command.setCmdReturn("");
		return command;
	}
	
	/**
	 * Gets the command of UPDATE Meet Info.
	 * @param meetInfo
	 * Meet Info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateMeetCommand(MeetInfo meetInfo, String deviceSn) {
		String sn = null;
		String lang = "";
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		if (null == deviceSn || "".equals(deviceSn)) {
			sn = meetInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets current language*/
		lang = PushUtil.getDeviceLangBySn(sn);
		/**Get User name, convert it to UTF-8*/
		String name = PushUtil.encodingByDeviceLang(meetInfo.getMetName(), lang);
		meetInfo.setMetName(name);
		/**Gets Command objects*/
		return getNewCommand(sn, getUpdateMeetContent(meetInfo));
	}
	
	/**
	 * Format the command content UPDATE MEETINFO 
	 * @param userInfo
	 * @return
	 */
	public static String getUpdateMeetContent(MeetInfo meetInfo) {
		/**Format the command content*/
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_MEETINFO, 
				meetInfo.getMetName(), 
				meetInfo.getMetStarSignTm(),
				meetInfo.getMetLatSignTm(),
				meetInfo.getEarRetTm(),
				meetInfo.getLatRetTm(),
				meetInfo.getCode(),
				meetInfo.getMetStrTm(),
				meetInfo.getMetEndTm());
	}

	/**
	 * Gets the command of Delete meet info
	 * @param meetInfo
	 * meet info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteMeetCommand(MeetInfo meetInfo, String deviceSn) {
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_MEETINFO, 
				meetInfo.getCode());
		/**
		 * if there is no device SN. Gets the SN from the device which has the user info
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = meetInfo.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}

	/**
	 * Gets the command of Update PersMeet
	 * @param devSn
	 * @param code
	 * @param pin
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdatePersMeetCommand(String devSn, String code, String pin) {
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_PERSMEET, 
				code,pin);
		/**Gets Command objects*/
		return getNewCommand(devSn, content);
	}

	/**
	 * Gets the command of Update Adv
	 * @param adv
	 * @param deviceSn
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getUpdateAdvCommand(Adv adv, String devSn) {
		
		String sn = null;
		/**
		 * if there is no device SN. Gets the SN from the device which has the user
		 */
		if (null == devSn || "".equals(devSn)) {
			sn = adv.getDeviceSn();
		} else {
			sn = devSn;
		}
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_ADV, 
				adv.getType(),adv.getFileName(),adv.getDeviceSn(),adv.getUrl());
		/**Gets Command objects*/
		return getNewCommand(sn, content);
		
	}

	/**
	 * Gets the command of Delete adv
	 * @param adv
	 * adv
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getDeleteAdvCommand(Adv adv, String deviceSn) {
		/**format the command content*/
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_ADV, 
				adv.getType(),adv.getFileName());
		/**
		 * if there is no device SN. Gets the SN from the device which has the user info
		 */
		String sn = null;
		if (null == deviceSn || deviceSn.isEmpty()) {
			sn = adv.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		/**Gets Command objects*/
		return getNewCommand(sn, content);
	}

	/**
	 * Gets the command of Clear meet
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearMeetCommand(String deviceSn) {
		/**format the command content*/
		String content = Constants.DEV_CMD_DATA_CLEAR_MEET;
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}

	/**
	 * Gets the command of Clear pers meet
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearPersMeetCommand(String deviceSn) {
		/**format the command content*/
		String content = Constants.DEV_CMD_DATA_CLEAR_PERSMEET;
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}

	/**
	 * Gets the command of Clear user info
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearUserInfoCommand(String deviceSn) {
		/**format the command content*/
		String content = Constants.DEV_CMD_DATA_CLEAR_USERINFO;
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}

	/**
	 * Gets the command of Clear advertisement
	 * @param deviceSn
	 * Device SN
	 * @return
	 * Command objects
	 */
	public static DeviceCommand getClearAdvCommand(int type, String deviceSn) {
		/**format the command content*/
		String content = MessageFormat.format(Constants.DEV_CMD_DATA_CLEAR_ADV,type);
		/**Gets Command objects*/
		return getNewCommand(deviceSn, content);
	}

	/*public static DeviceCommand getDeletePlamCommand(PersonBioTemplate personTemplate, String deviceSn) {
	
		*//**Format the command content*//*
		String content = MessageFormat.format(
				Constants.DEV_CMD_DATA_DELETE_PALM, 
				personTemplate.getUserPin(), 
				personTemplate.getTemplateNo());
		*//**
		 * if there is no device SN. Gets the SN from the device which has the template
		 *//*
		String sn = null;
		if (null == deviceSn || "".equals(deviceSn)) {
			sn = personTemplate.getDeviceSn();
		} else {
			sn = deviceSn;
		}
		*//**Gets Command objects*//*
		return getNewCommand(sn, content);
	}*/

	public static Object getUpdatePlamContent(PersonBioTemplate template) {
		
		return MessageFormat.format(
				Constants.DEV_CMD_DATA_UPDATE_PALM, 
				template.getUserPin(), 
				template.getTemplateNo(), 
				String.valueOf(template.getSize()), 
				template.getValid(), 
				template.getTemplateData());

	}

}
