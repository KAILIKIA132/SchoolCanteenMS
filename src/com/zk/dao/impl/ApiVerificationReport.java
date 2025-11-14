package com.zk.dao.impl;

import java.io.Serializable;
import java.util.Date;

/**
 * API Verification Report entity
 * @author system
 */
public class ApiVerificationReport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int reportId;
	private String userPin;
	private String userName;
	private String studentId;
	private String verificationTime;
	private Date apiCallTime;
	private String mealType;
	private String status; // SUCCESS or FAILED
	private Integer responseCode;
	private String responseMessage;
	private String errorMessage;
	private String apiUrl;

	public int getReportId() {
		return reportId;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getVerificationTime() {
		return verificationTime;
	}

	public void setVerificationTime(String verificationTime) {
		this.verificationTime = verificationTime;
	}

	public Date getApiCallTime() {
		return apiCallTime;
	}

	public void setApiCallTime(Date apiCallTime) {
		this.apiCallTime = apiCallTime;
	}

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
}



