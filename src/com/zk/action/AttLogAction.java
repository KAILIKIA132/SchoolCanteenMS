package com.zk.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.AttLog;
import com.zk.pushsdk.po.AttPhoto;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.FileOperateUtil;
import com.zk.util.PagenitionUtil;

public class AttLogAction implements ServletRequestAware,ServletResponseAware {
	private static Logger logger = Logger.getLogger(AttLogAction.class);
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	private String jsonResponse;
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * Gets the list of recent verification responses (real-time biometric scans) as JSON
	 * Query parameters:
	 *   - limit: number of records to return (default: 100, max: 1000)
	 *   - deviceSn: filter by device serial number (optional)
	 *   - userPin: filter by user PIN (optional)
	 *   - since: return only records after this timestamp (optional, format: yyyy-MM-dd HH:mm:ss)
	 * @return JSON response with verification data
	 */
	public String getVerificationsJson() throws IOException, JSONException {
		try {
			/**Set response headers*/
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			
			/**Get query parameters*/
			String limitStr = request.getParameter("limit");
			String deviceSn = request.getParameter("deviceSn");
			String userPin = request.getParameter("userPin");
			String since = request.getParameter("since");
			
			int limit = 100;
			if (limitStr != null && !limitStr.isEmpty()) {
				try {
					limit = Integer.parseInt(limitStr);
					if (limit > 1000) limit = 1000; // Max limit
					if (limit < 1) limit = 1;
				} catch (NumberFormatException e) {
					limit = 100;
				}
			}
			
			/**Get verification responses*/
			List<AttLog> list = ManagerFactory.getAttLogManager().getAttLogList(deviceSn, userPin, 0, limit);
			if (list == null) {
				list = new java.util.ArrayList<AttLog>();
			}
			
			/**Filter by since timestamp if provided*/
			if (since != null && !since.isEmpty() && list != null) {
				java.util.Iterator<AttLog> it = list.iterator();
				while (it.hasNext()) {
					AttLog log = it.next();
					if (log.getVerifyTime() != null && log.getVerifyTime().compareTo(since) <= 0) {
						it.remove();
					}
				}
			}
			
			/**Reverse to show newest first*/
			java.util.Collections.reverse(list);
			
			/**Build JSON response*/
			JSONObject jsonObj = new JSONObject();
			JSONArray verifications = new JSONArray();
			
			for (AttLog attLog : list) {
				JSONObject verification = new JSONObject();
				verification.put("id", attLog.getAttLogId());
				verification.put("timestamp", attLog.getVerifyTime());
				verification.put("deviceSn", attLog.getDeviceSn() != null ? attLog.getDeviceSn() : "");
				verification.put("userPin", attLog.getUserPin() != null ? attLog.getUserPin() : "");
				verification.put("userName", attLog.getUserName() != null ? attLog.getUserName() : "");
				verification.put("verifyType", attLog.getVerifyType());
				verification.put("verifyTypeStr", attLog.getVerifyTypeStr() != null ? attLog.getVerifyTypeStr() : "");
				verification.put("status", attLog.getStatus());
				verification.put("statusStr", attLog.getStatusStr() != null ? attLog.getStatusStr() : "");
				verification.put("mask", attLog.getMaskFlag());
				verification.put("temperature", attLog.getTemperatureReading() != null ? attLog.getTemperatureReading() : "");
				verification.put("workCode", attLog.getWorkCode());
				verification.put("sensorNo", attLog.getSensorNo());
				verification.put("palm", attLog.getPalmFlag());
				verifications.put(verification);
			}
			
			jsonObj.put("success", true);
			jsonObj.put("count", verifications.length());
			jsonObj.put("verifications", verifications);
			jsonObj.put("timestamp", new java.util.Date().toString());
			
			jsonResponse = jsonObj.toString();
			
			/**Store JSON response for JSP to output*/
			request.setAttribute("jsonResponse", jsonResponse);
			return "jsonResult";
			
		} catch (Exception e) {
			logger.error("Error in getVerificationsJson: " + e.getMessage(), e);
			JSONObject errorResponse = new JSONObject();
			try {
				errorResponse.put("success", false);
				errorResponse.put("error", e.getMessage());
				errorResponse.put("timestamp", new java.util.Date().toString());
				PrintWriter out = response.getWriter();
				out.print(errorResponse.toString());
				out.flush();
			} catch (Exception ex) {
				logger.error("Error writing error response: " + ex.getMessage(), ex);
			}
			return null;
		}
	}
	
	public String getJsonResponse() {
		return jsonResponse;
	}
	
	/**
	 * Gets the list of recent verification responses (real-time biometric scans) - JSP view
	 * @return
	 */
	public String verificationList() {
		/**Get recent verification responses - last 100 records, ordered by time descending*/
		List<AttLog> list = ManagerFactory.getAttLogManager().getAttLogList(null, null, 0, 100);
		if (list != null) {
			/**Reverse to show newest first*/
			java.util.Collections.reverse(list);
		} else {
			list = new java.util.ArrayList<AttLog>();
		}
		request.setAttribute("verificationList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		return "verificationList";
	}
	
	/**
	 * Gets the list of Attendance logs
	 * @return
	 */
	public String attLogList() {
		int recCount = 0;
		int pageCount = 0;
		/**Gets the parameters of interface*/
		String deviceSn = request.getParameter("deviceSn");
		String userPin = request.getParameter("userPin");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Gets the count of attendance logs*/
		recCount = ManagerFactory.getAttLogManager().getAttLogCount(deviceSn, userPin);
		/**Gets the count of pages and the current page*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Gets the start logs*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Gets the list of logs by the page condition and page information*/
		List<AttLog> list = ManagerFactory.getAttLogManager().getAttLogList(deviceSn, userPin, startRec, PagenitionUtil.getPageSize());
		
		/** Sets the parameters of interface*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("attList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("user.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		if (null == userPin || userPin.isEmpty()) {
			userPin = resource.getString("user.serach.input.name.or.userpin");
		}
		request.setAttribute("byUserPin21", userPin);
		return "attLogList";
	}
	
	/**
	 * According to the attendance record ID from Interface,removes the specified attendance record in server.
	 * @return
	 */
	public String delById() {
		String logIds = request.getParameter("logId");
		if (null == logIds || logIds.isEmpty()) {
			return "";
		}
		String[] ids = logIds.split(",");
		ManagerFactory.getAttLogManager().deleteAttLogByIds(ids);
		return "";
	}
	/**
	 * Clear all attendance records from the server.
	 * @return
	 */
	public String clearAll() {
		ManagerFactory.getAttLogManager().clearAllAttLog();
		return "";
	}
	
	/**
	 * Clear all attendance photos from the server.
	 * @return
	 */
	public String clearAllPhoto(){
		new Thread(new Runnable() {
			public void run() {
				/**Delete database records*/
				ManagerFactory.getAttPhotoManager().clearAllPhoto();
			}
		}).start();
		return "";
	}
	

}
