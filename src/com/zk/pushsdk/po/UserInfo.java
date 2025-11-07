package com.zk.pushsdk.po;

import java.io.Serializable;
/**
 * User info data entity
 * @author seiya
 *
 */
public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695975455644400220L;
	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int userId;
	/**User ID number*/
	private String userPin;
	/**User privilege*/
	private int privilege;
	/**User name*/
	private String name;
	/**User password*/
	private String password;
	/**ID of face group, reserved*/
	private int faceGroupId;
	/**ID of plam group, reserved*/
	//private int plamGroupId;
	/**ID of access group*/
	private int accGroupId;
	/**ID of department, reserved*/
	private int deptId;
	/**Use group timezone or not*/
	private int isGroupTz;
	/**Biometric identification type, like fingerprint, face, password, vein, etc.*/
	private int verifyType;
	/**Main card number*/
	private String mainCard;
	/**Vice card number, reserved*/
	private String viceCard;
	/**User expires, reserved*/
	private int expires;
	/**Device serial number*/
	private String deviceSn;
	/**User timezone*/
	private String tz;
	/**Name of user photo*/
	private String photoIdName;
	/**The size of user photo data in Base64 format*/
	private int photoIdSize;
	/**User photo data in Base64 format*/
	private String photoIdContent;
	/**Fingerprint count of user*/
	private int userFpCount;
	/**Face template count of user*/
	private int userFaceCount;
	/** Plam template count of user*/
	private int userPalmCount;
	/**Meet code*/
	private String meetCode;
	/**Category*/
	private int Category;

	/*public int getPlamGroupId() {
		return plamGroupId;
	}

	public void setPlamGroupId(int plamGroupId) {
		this.plamGroupId = plamGroupId;
	}*/

	public int getUserPalmCount() {
		return userPalmCount;
	}

	public void setUserPalmCount(int userPalmCount) {
		this.userPalmCount = userPalmCount;
	}

	public int getCategory() {
		return Category;
	}

	public void setCategory(int category) {
		Category = category;
	}

	public String getTz() {
		return tz;
	}

	public void setTz(String tz) {
		this.tz = tz;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFaceGroupId() {
		return faceGroupId;
	}

	public void setFaceGroupId(int faceGroupId) {
		this.faceGroupId = faceGroupId;
	}

	public int getAccGroupId() {
		return accGroupId;
	}

	public void setAccGroupId(int accGroupId) {
		this.accGroupId = accGroupId;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getIsGroupTz() {
		return isGroupTz;
	}

	public void setIsGroupTz(int isGroupTz) {
		this.isGroupTz = isGroupTz;
	}

	public int getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(int verifyType) {
		this.verifyType = verifyType;
	}

	public String getMainCard() {
		return mainCard;
	}

	public void setMainCard(String mainCard) {
		this.mainCard = mainCard;
	}

	public String getViceCard() {
		return viceCard;
	}

	public void setViceCard(String viceCard) {
		this.viceCard = viceCard;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}

	public String getPhotoIdName() {
		return photoIdName;
	}

	public void setPhotoIdName(String photoIdName) {
		this.photoIdName = photoIdName;
	}

	public int getPhotoIdSize() {
		return photoIdSize;
	}

	public void setPhotoIdSize(int photoIdSize) {
		this.photoIdSize = photoIdSize;
	}

	public String getPhotoIdContent() {
		return photoIdContent;
	}

	public void setPhotoIdContent(String photoIdContent) {
		this.photoIdContent = photoIdContent;
	}

	public int getUserFpCount() {
		return userFpCount;
	}

	public void setUserFpCount(int userFpCount) {
		this.userFpCount = userFpCount;
	}

	public int getUserFaceCount() {
		return userFaceCount;
	}

	public void setUserFaceCount(int userFaceCount) {
		this.userFaceCount = userFaceCount;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	
	public String getMeetCode() {
		return meetCode;
	}

	public void setMeetCode(String meetCode) {
		this.meetCode = meetCode;
	}
		
	
}
