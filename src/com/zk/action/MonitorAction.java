package com.zk.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.pushsdk.po.DeviceLog;
import com.zk.pushsdk.util.PushUtil;

public class MonitorAction implements ServletRequestAware,ServletResponseAware{
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}

	/**
	 * Get the real-time monitoring data list
	 * @return
	 */
	public String monitorList() {
		List<DeviceLog> monList = new ArrayList<DeviceLog>();
		/**Get the real-time monitoring data from buffer*/
		monList.addAll(PushUtil.monitorList);
		/**Execute reverse operations*/
		Collections.reverse(monList);
		String time = PushUtil.dateFormat.format(new Date());
		request.setAttribute("time", time);
		request.setAttribute("monList", monList);
		return "monitorList";
	}
}
