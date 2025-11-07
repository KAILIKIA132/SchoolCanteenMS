package com.zk.pushsdk.po;

import java.io.Serializable;
import java.util.List;

/**
 * Device info entity
 * @author seiya
 *
 */
public class DeviceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -713219808104772168L;
	/**Device ID, AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int deviceId;
	/**Device serial number*/
	private String deviceSn;
	/**Device name*/
	private String deviceName;
	/**Device alias name*/
	private String aliasName;
	/**Palm detection*/
	private int palm;
	/**Device name*/
	private int mask;
	/**Device alias name*/
	private String temperature;
	
	/**Department ID*/
	private int deptId;
	/**Device status*/
	private String state;
	/**Device last online activity time*/
	private String lastActivity;
	/**
	 * This parameter will be sent to device when connecting.
	 * The transTimes is several time combination separated by semicolon that
	 * the device will go to  check and upload any data updates on time.
	 * It must be 24 hour format HH:MM, like transTimes=00:00;14:00.
	 * It can support up to 10 time combination.
	 */
	private String transTimes;
	/**
	 * This parameter will be sent to device when connecting.
	 * transInterval in minutes is the interval that 
	 * the device goes to check and upload any data updates.
	 * transInterval = 0 means no check. 
	 */
	private int transInterval;
	/**
	 * This parameter will be sent to device when connecting.
	 * Attendance transaction timestamp
	 * The device will upload attendance transactions created after the timestamp.
	 */
	private String logStamp;
	/**
	 * This parameter will be sent to device when connecting.
	 * Oplog timestamp
	 * The device will upload oplogs created after the timestamp.
	 */
	private String opLogStamp;
	/**
	 * This parameter will be sent to device when connecting.
	 * Attendance photo timestamp.
	 * The device will upload attendance photos created after the timestamp.
	 */
	private String photoStamp;
	
	/**
	 * This parameter will be sent to device when connecting.
	 * Attendance BioDataStamp.
	 */	
	private String bioDataStamp;
	
	/**
	 * This parameter will be sent to device when connecting.
	 * Attendance IdCardStamp.
	 */
	private String idCardStamp;
	
	/**
	 * This parameter will be sent to device when connecting.
	 * Attendance ErrorLogStamp.
	 */
	private String errorLogStamp;
	
	/**Push firmware version of the device*/
	private String firmwareVersion;
	/**User count of the device*/
	private int userCount;
	/**User count in the server*/
	private int actUserCount;
	/**Finggerprint count of the device*/
	private int fpCount;
	/**Finggerprint count in the server*/
	private int actFpCount;
	/**Attendance transaction count of the device*/
	private int transCount;
	/**Attendance transaction count in the server*/
	private int actTransCount;
	/**Fingerprint algorithm version of the device*/
	private String fpAlgVersion;
	/**Push protocal version of the device*/
	private String pushVersion;
	/**Device type*/
	private String deviceType;
	/**Device IP address*/
	private String ipAddress;
	/**Device displayed language*/
	private String devLanguage;
	/**Communication password*/
	private String pushCommKey;
	/**Face count in the device*/
	private int faceCount;
	/**Face count in the server*/
	private int actFaceCount;
	/**Face count in the server*/
	private String faceAlgVer;
	/**Face algorithm version of the device*/
	private int regFaceCount;
	/**Functions supported in device*/
	private String devFuns;
	
	private String timeZone;
	
	private List<DeviceAttrs> attrList = null;


	public String getPushCommKey() {
		return pushCommKey;
	}

	public void setPushCommKey(String pushCommKey) {
		this.pushCommKey = pushCommKey;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getDevLanguage() {
		return devLanguage;
	}

	public void setDevLanguage(String devLanguage) {
		this.devLanguage = devLanguage;
	}

	public String getPushVersion() {
		return pushVersion;
	}

	public void setPushVersion(String pushVersion) {
		this.pushVersion = pushVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(String lastActivity) {
		this.lastActivity = lastActivity;
	}

	public String getTransTimes() {
		return transTimes;
	}

	public void setTransTimes(String transTimes) {
		this.transTimes = transTimes;
	}

	public int getTransInterval() {
		return transInterval;
	}

	public void setTransInterval(int transInterval) {
		this.transInterval = transInterval;
	}

	public String getLogStamp() {
		return logStamp;
	}

	public void setLogStamp(String logStamp) {
		this.logStamp = logStamp;
	}

	public String getOpLogStamp() {
		return opLogStamp;
	}

	public void setOpLogStamp(String opLogStamp) {
		this.opLogStamp = opLogStamp;
	}

	public String getPhotoStamp() {
		return photoStamp;
	}

	public void setPhotoStamp(String photoStamp) {
		this.photoStamp = photoStamp;
	}
	
	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getFpCount() {
		return fpCount;
	}

	public void setFpCount(int fpCount) {
		this.fpCount = fpCount;
	}

	public int getTransCount() {
		return transCount;
	}

	public void setTransCount(int transCount) {
		this.transCount = transCount;
	}

	public String getFpAlgVersion() {
		return fpAlgVersion;
	}

	public void setFpAlgVersion(String fpAlgVersion) {
		this.fpAlgVersion = fpAlgVersion;
	}

	public int getActUserCount() {
		return actUserCount;
	}

	public void setActUserCount(int actUserCount) {
		this.actUserCount = actUserCount;
	}

	public int getActFpCount() {
		return actFpCount;
	}

	public void setActFpCount(int actFpCount) {
		this.actFpCount = actFpCount;
	}

	public int getActTransCount() {
		return actTransCount;
	}

	public void setActTransCount(int actTransCount) {
		this.actTransCount = actTransCount;
	}

	public int getFaceCount() {
		return faceCount;
	}

	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
	}

	public int getActFaceCount() {
		return actFaceCount;
	}

	public void setActFaceCount(int actFaceCount) {
		this.actFaceCount = actFaceCount;
	}

	public String getFaceAlgVer() {
		return faceAlgVer;
	}

	public void setFaceAlgVer(String faceAlgVer) {
		this.faceAlgVer = faceAlgVer;
	}

	public int getRegFaceCount() {
		return regFaceCount;
	}

	public void setRegFaceCount(int regFaceCount) {
		this.regFaceCount = regFaceCount;
	}

	public String getDevFuns() {
		return devFuns;
	}

	public void setDevFuns(String devFuns) {
		this.devFuns = devFuns;
	}

	public List<DeviceAttrs> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<DeviceAttrs> attrList) {
		this.attrList = attrList;
	}

	public int getPalm() {
		return palm;
	}

	public void setPalm(int palm) {
		this.palm = palm;
	}

	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	
	public String getBioDataStamp() {
		return bioDataStamp;
	}

	public void setBioDataStamp(String bioDataStamp) {
		this.bioDataStamp = bioDataStamp;
	}

	public String getIdCardStamp() {
		return idCardStamp;
	}

	public void setIdCardStamp(String idCardStamp) {
		this.idCardStamp = idCardStamp;
	}

	public String getErrorLogStamp() {
		return errorLogStamp;
	}

	public void setErrorLogStamp(String errorLogStamp) {
		this.errorLogStamp = errorLogStamp;
	}
	
}
