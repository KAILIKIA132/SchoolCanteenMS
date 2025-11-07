package com.zk.pushsdk.po;

import java.io.Serializable;

public class DeviceAttrs implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1754691984901806692L;
	
	private int id;
	
	private String deviceSn;
	
	private String attrName;
	
	private String attrValue;

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

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	
	
}
