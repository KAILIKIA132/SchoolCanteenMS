package com.zk.pushsdk.po;

import java.io.Serializable;

/**
 * Device Command data entity
 * @author seiya
 *
 */
public class DeviceCommand implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4897891380884898686L;
	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int devCmdId;
	/**Device serial number*/
	private String deviceSn;
	/**Content of command*/
	private String cmdContent;
	/**Time of command committed on Server*/
	private String cmdCommitTime;
	/**Time of command transfered to Device*/
	private String cmdTransTime;
	/**Time of Device reply to Sever after command executed*/
	private String cmdOverTime;
	/**The return value from Device after command executed*/
	private String cmdReturn;
	/**The return info from Device after command executed*/
	private String cmdReturnInfo;

	public int getDevCmdId() {
		return devCmdId;
	}

	public void setDevCmdId(int devCmdId) {
		this.devCmdId = devCmdId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getCmdContent() {
		return cmdContent;
	}

	public void setCmdContent(String cmdContent) {
		this.cmdContent = cmdContent;
	}

	public String getCmdCommitTime() {
		return cmdCommitTime;
	}

	public void setCmdCommitTime(String cmdCommitTime) {
		this.cmdCommitTime = cmdCommitTime;
	}

	public String getCmdTransTime() {
		return cmdTransTime;
	}

	public void setCmdTransTime(String cmdTransTime) {
		this.cmdTransTime = cmdTransTime;
	}

	public String getCmdOverTime() {
		return cmdOverTime;
	}

	public void setCmdOverTime(String cmdOverTime) {
		this.cmdOverTime = cmdOverTime;
	}

	public String getCmdReturn() {
		return cmdReturn;
	}

	public void setCmdReturn(String cmdReturn) {
		this.cmdReturn = cmdReturn;
	}

	public String getCmdReturnInfo() {
		return cmdReturnInfo;
	}

	public void setCmdReturnInfo(String cmdReturnInfo) {
		this.cmdReturnInfo = cmdReturnInfo;
	}
	
	
}
