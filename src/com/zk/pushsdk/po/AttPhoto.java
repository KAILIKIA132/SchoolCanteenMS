package com.zk.pushsdk.po;

import java.io.Serializable;
/**
 * Attendance photo data entity
 * @author seiya
 *
 */
public class AttPhoto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8173424668895487162L;

	/**AUTO_INCREMENT NOT NULL PRIMARY KEY*/
	private int id;
	/**Device serial number*/
	private String deviceSn;
	/**File name of the photo*/
	private String fileName;
	/**File size of the photo in byte*/
	private int size;
	/**Command used to upload attendance photo, like 'uploadphoto'*/
	private String cmd;
	/**The absolute path  of the photo stored on the Server*/
	private String filePath;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
