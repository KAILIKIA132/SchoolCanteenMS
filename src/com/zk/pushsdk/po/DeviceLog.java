package com.zk.pushsdk.po;

import java.io.Serializable;

import com.zk.pushsdk.util.PushUtil;

/**
 * Device operate log
 * @author seiya
 *
 */
public class DeviceLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3106565937078222296L;

	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int devLogId;
	/**Device serial number*/
	private String deviceSn;
	/**operator*/
	private String operator;
	/**operator type: administrator, enroller etc.*/
	private int operatorType;
	/**operate type*/
	private int operateType;
	/**operate name for view in web page*/
	private String operateTypeStr;
	/**operate time*/
	private String operateTime;
	
	private int palmFlag;
	private int maskFlag;
	
	private String temperatureReading;
	/**operate value 1*/
	private String value1;
	/**operate value 2*/
	private String value2;
	/**operate value 3*/
	private String value3;
	/**reserved field*/
	private String reserved;
	/**alarm log flag*/
	private boolean alarm = false;
	
	

	public int getDevLogId() {
		return devLogId;
	}

	public void setDevLogId(int devLogId) {
		this.devLogId = devLogId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getOperateTypeStr() {
		if (null == operateTypeStr || operateTypeStr.isEmpty()) {
			operateTypeStr = PushUtil.ATT_OP_TYPE.get(operateType);
		}
		return operateTypeStr;
	}

	public void setOperateTypeStr(String operateTypeStr) {
		this.operateTypeStr = operateTypeStr;
	}

	public boolean isAlarm() {
		/**check alarm event*/
		alarm = operateType == 3 || operateType == 32;
		return alarm;
	}

	public void setAlarm(boolean alarm) {
		this.alarm = alarm;
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
