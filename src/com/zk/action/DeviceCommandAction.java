package com.zk.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.PagenitionUtil;

public class DeviceCommandAction implements ServletRequestAware,ServletResponseAware {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	/**
	 * Get Device command  data list
	 * @return
	 */
	public String devCmdList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String deviceSn = request.getParameter("deviceSn");
		String command = request.getParameter("command");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getCommandManager().getDeviceCommandCount(deviceSn, command);
		/**Calculate the total number of pages and the current page numbe*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Search record lists based on page conditions and page information*/
		List<DeviceCommand> list = ManagerFactory.getCommandManager().getDeviceCommandList(deviceSn, command, startRec, PagenitionUtil.getPageSize());
		/**Process page show and display long data separately*/
		if (null != list) {
			for (DeviceCommand deviceCommand : list) {
				if (null != deviceCommand.getCmdContent()
						&& deviceCommand.getCmdContent().length() > 50) {
					String content = deviceCommand.getCmdContent();
					StringBuilder sb = new StringBuilder();
					sb.append(content.substring(0, 30));
					sb.append("......");
					sb.append(content.substring(content.length() - 10));
					deviceCommand.setCmdContent(sb.toString());
				}
				if (null != deviceCommand.getCmdReturnInfo() 
						&& deviceCommand.getCmdReturnInfo().length() > 50) {
					String info = deviceCommand.getCmdReturnInfo();
					StringBuilder sb = new StringBuilder();
					sb.append(info.substring(0, 10));
					sb.append("......");
					sb.append(info.substring(info.length() - 10));
					deviceCommand.setCmdReturnInfo(sb.toString());
				}
			}
		} else {
			list = new ArrayList<DeviceCommand>();
		}
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("cmdList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("user.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		if (null == command || command.isEmpty()) {
			command = resource.getString("search.by.cmd");
		}
		request.setAttribute("byCommand", command);
		return "cmdList";
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	/**
	 * Delete the device command which has been executed completed.
	 * @return
	 */
	public String deleteReturn() {
		ManagerFactory.getCommandManager().deleteCommandReturn();
		return "cmdList";
	}
	
	/**
	 * Show detailed information about a specified device command
	 * @return
	 */
	public String viewCmd() {
		String cmdId = request.getParameter("cmdId");
		if (null == cmdId || cmdId.isEmpty()) {
			return "cmdList";
		}
		DeviceCommand command = ManagerFactory.getCommandManager().getDeviceCommandById(Integer.valueOf(cmdId));
		
		if (null == command) {
			return "cmdList";
		}
		
		request.setAttribute("command", command);
		
		return "viewCmd";
	}
	
	/**
	 * Remove selected device command on the server.
	 * 
	 * @return
	 */
	public String deleteSel() {
		String cmdId = request.getParameter("cmdId");
		if (null == cmdId || cmdId.isEmpty()) {
			return "cmdList";
		}
		
		String[] ids = cmdId.split(",");
		
		ManagerFactory.getCommandManager().deleteCmdById(ids);		
		return "cmdList";
	}
}
