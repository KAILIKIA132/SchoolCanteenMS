package com.zk.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.util.PagenitionUtil;

public class CommandAction implements ServletRequestAware,ServletResponseAware{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static Logger logger = Logger.getLogger(CommandAction.class);
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String command() {
		String deviceSn = request.getParameter("deviceSn");
		String command = request.getParameter("command");
		
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		int recCount = 0;
		
		recCount = ManagerFactory.getCommandManager().getDeviceCommandCount(deviceSn, command);
		/**Calculate the total number of pages and the current page numbe*/
		int pageCount = PagenitionUtil.getPageCount(recCount);
		int curPage =1;
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		
		List<DeviceCommand> list = ManagerFactory.getCommandManager().getDeviceCommandList(deviceSn, command, startRec, PagenitionUtil.getPageSize());
		
		
		request.setAttribute("cmdList", list);
		return "command";
	}
}
