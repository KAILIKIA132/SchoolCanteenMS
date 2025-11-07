package com.zk.action;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.DeviceLog;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.PagenitionUtil;

public class DeviceLogAction implements ServletRequestAware,ServletResponseAware{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	/**
	 * Get device operation log list.
	 * @return
	 */
	public String deviceLogList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String deviceSn = request.getParameter("deviceSn");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getDeviceLogManager().getDeviceLogCount(deviceSn);
		/**Calculate the total number of pages and the current page numbe*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Search record lists based on page conditions and page information*/
		List<DeviceLog> list = ManagerFactory.getDeviceLogManager().getDeviceLogList(deviceSn, startRec, PagenitionUtil.getPageSize());
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("list", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("user.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		return "deviceLogList";
	}
	
	/**
	 * Delete the selected  operation log in the server.
	 * 
	 * @return
	 */
	public String deleteSel() {
		String logId = request.getParameter("logId");
		if (null == logId || logId.isEmpty()) {
			return "deviceLogList";
		}
		
		String[] ids = logId.split(",");
		
		ManagerFactory.getDeviceLogManager().deleteDevLogById(ids);		
		return "deviceLogList";
	}
	
}
