package com.zk.pushsdk.po;

import java.io.Serializable;

/**
 * Biometric template data entity
 * @author seiya
 *
 */
public class PersonBioTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1224885050967443958L;
	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int id;
	/**User AUTO_INCREMENT ID*/
	private int userId;
	/**User business logic ID*/
	private String userPin;
	/**Is valid template or not. 0-invalid, 1-valid. Default 1.*/
	private int valid;
	/** Is duress template or not. 0-not duress, 1-duress. Default 0.*/
	private int isDuress;
	/**Type of biometric, like fingerprint, face, etc.*/
	private int bioType;
	/**Biometric algorithm version*/
	private String version;
	/**Format of biometric template*/
	private int dataFormat;
	/**Number of individual organisms*/
	private int templateNo;
	/**Index of template*/
	private int templateNoIndex;
	/**template size */
	private int size;
	/**device serial number*/
	private String deviceSn;
	/**template data*/
	private String templateData;
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

	public int getIsDuress() {
		return isDuress;
	}

	public void setIsDuress(int isDuress) {
		this.isDuress = isDuress;
	}

	public int getBioType() {
		return bioType;
	}

	public void setBioType(int bioType) {
		this.bioType = bioType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(int dataFormat) {
		this.dataFormat = dataFormat;
	}

	public int getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(int templateNo) {
		this.templateNo = templateNo;
	}

	public int getTemplateNoIndex() {
		return templateNoIndex;
	}

	public void setTemplateNoIndex(int templateNoIndex) {
		this.templateNoIndex = templateNoIndex;
	}
	
	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

}
