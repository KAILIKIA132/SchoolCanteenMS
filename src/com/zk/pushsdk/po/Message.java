package com.zk.pushsdk.po;

import java.io.Serializable;

/**
 * Message data entity
 * @author seiya
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5062264875398901377L;
	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int id;
	/**Device serial number*/
	private String deviceSn;
	/**Message type. 253-public, 254-individual, 255-reserved*/
	private int smsType;
	/**Message type name.*/
	private String smsTypeStr;
	/**Message activation time*/
	private String startTime;
	/**The valid length of the message in minutes*/
	private int validMinutes;
	/**Message content*/
	private String smsContent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public int getSmsType() {
		return smsType;
	}

	public void setSmsType(int smsType) {
		this.smsType = smsType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getValidMinutes() {
		return validMinutes;
	}

	public void setValidMinutes(int validMinutes) {
		this.validMinutes = validMinutes;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String getSmsTypeStr() {
		return smsTypeStr;
	}

	public void setSmsTypeStr(String smsTypeStr) {
		this.smsTypeStr = smsTypeStr;
	}
	
	
}
