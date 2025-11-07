package com.zk.pushsdk.po;

import java.io.Serializable;

import com.zk.pushsdk.util.PushUtil;

/**
 * Attendance Log Data entity
 * @author seiya
 *
 */

public class AttLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2276220903759258580L;

	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int attLogId;
	
	/**User ID number*/
	private String userPin;
	/**Biometric identification type, like fingerprint, face, vein, etc.*/
	private int verifyType;
	/**The string name of verifyType*/
	private String verifyTypeStr;
	/**The time of the attendance log created*/
	private String verifyTime;
	/**Attendance status, like check-in, check-out, break-in, break-out, etc.*/
	private int status;
	/**The string name of status*/
	private String statusStr;
	/**WorkCode number, defined in the Device*/
	private int workCode;
	/**Reserved, sensor number*/
	private int sensorNo;
	/**Reserved, use this flag to indicate the exception attendance log*/
	private int attFlag;
	/**Palm Detection, use this flag to indicate the Palm Detection*/
	private int palmFlag;
	/**Mask Detection, use this flag to indicate the Mask Detection*/
	private int maskFlag;
	/**Temperature Detection, use this flag to indicate the Temperature Detection*/
	private String temperatureReading;
	/**Device serial number*/
	private String deviceSn;
	/**reserved field*/
	private int reserved1;
	/**reserved field*/
	private int reserved2;
	/**user name , use to view in web page*/
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getReserved1() {
		return reserved1;
	}

	public void setReserved1(int reserved1) {
		this.reserved1 = reserved1;
	}

	public int getReserved2() {
		return reserved2;
	}

	public void setReserved2(int reserved2) {
		this.reserved2 = reserved2;
	}

	public int getAttLogId() {
		return attLogId;
	}

	public void setAttLogId(int attLogId) {
		this.attLogId = attLogId;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public int getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(int verifyType) {
		this.verifyType = verifyType;
	}

	public String getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(String verifyTime) {
		this.verifyTime = verifyTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getWorkCode() {
		return workCode;
	}

	public void setWorkCode(int workCode) {
		this.workCode = workCode;
	}

	public int getSensorNo() {
		return sensorNo;
	}

	public void setSensorNo(int sensorNo) {
		this.sensorNo = sensorNo;
	}

	public int getAttFlag() {
		return attFlag;
	}

	public void setAttFlag(int attFlag) {
		this.attFlag = attFlag;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getVerifyTypeStr() {
		if (null == verifyTypeStr || verifyTypeStr.isEmpty()) {
			verifyTypeStr = PushUtil.ATT_VERIFY.get(String.valueOf(verifyType));
		}
		return verifyTypeStr;
	}

	public void setVerifyTypeStr(String verifyTypeStr) {
		this.verifyTypeStr = verifyTypeStr;
	}

	public String getStatusStr() {
		if (null == statusStr || statusStr.isEmpty()) {
			statusStr = PushUtil.ATT_STATUS.get(String.valueOf(status));
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public int getPalmFlag() {
		return palmFlag;
	}

	public void setPalmFlag(int palmFlag) {
		this.palmFlag = palmFlag;
	}

	public int getMaskFlag() {
		return maskFlag;
	}

	public void setMaskFlag(int maskFlag) {
		this.maskFlag = maskFlag;
	}

	public String getTemperatureReading() {
		return temperatureReading;
	}

	public void setTemperatureReading(String temperatureReading) {
		this.temperatureReading = temperatureReading;
	}

	
}
