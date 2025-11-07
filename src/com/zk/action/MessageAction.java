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
import com.zk.pushsdk.po.Message;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.PagenitionUtil;

public class MessageAction implements ServletRequestAware,ServletResponseAware{
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
	 * Get short message list.
	 * @return
	 */
	public String smsList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getMessageManager().getMessageCount(-1);
		/**Calculate the total number of pages and the current page numbe*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Modify Short Message Type*/
		List<Message> list = ManagerFactory.getMessageManager().getSmsList(-1, startRec, PagenitionUtil.getPageSize());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		/**Modify Short Message Type*/
		if (null != list) {
			for (Message message : list) {
				if (253 == message.getSmsType()) {
					/**Public Short Message(SMS)*/
					message.setSmsTypeStr(resource.getString("sms.type.public"));
				} else {
					/**Personal Short Message(SMS)*/
					message.setSmsTypeStr(resource.getString("sms.type.private"));
				}
			}
		} else {
			list = new ArrayList<Message>();
		}
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("list", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		return "smsList";
	}
	
	public String newSms() {
		return "newSms";
	}

	/**
	 * Add Short Message(SMS).
	 * @return
	 */
	public String addSms() {
		/**Get interface parameters*/
		String smsType = request.getParameter("smsType");
		String startTime = request.getParameter("startTime");
		String validMinute = request.getParameter("validMinute");
		String smsContent = request.getParameter("smsContent");
		
		/**Create a short message object*/
		Message message = new Message();
		message.setStartTime(startTime);
		message.setValidMinutes(Integer.valueOf(validMinute));
		message.setSmsContent(smsContent);
		message.setSmsType(Integer.valueOf(smsType));
		/**Add Short Message*/
		ManagerFactory.getMessageManager().addSms(message);
		
		return "smsList";
	}
	
	/**
	 * Send a message to the specified device,Corresponding to the "DATA UPDATE SMS" command.
	 * @return
	 */
	public String sendToDev() {
		/**Get interface parameters*/
		String smsId = request.getParameter("smsId");
		String destSn = request.getParameter("destSn");
		
		if (null == smsId || null == destSn || smsId.isEmpty() || destSn.isEmpty()) {
			return "smsList";
		}
		
		String[] ids = smsId.split(",");
		ManagerFactory.getCommandManager().createUpdateSmsCommand(ids, destSn);
		
		return "smsList";
	}
	
	/**
	 * Send user Short message to the specified device,Corresponding to the "DATA UPDATE USER_SMS" command.
	 * @return
	 */
	public String sendUserSms() {
		/**Get interface parameters*/
		String smsId = request.getParameter("smsId");
		String userPin = request.getParameter("userPin");
		String destSn = request.getParameter("destSn");
		if (null == smsId || smsId.isEmpty()
				|| null == userPin || userPin.isEmpty()
				|| null == destSn || destSn.isEmpty()) {
			return "smsList";
		}
		String[] ids = smsId.split(",");
		String[] sns = destSn.split(",");
		for (String deviceSn : sns) {
			ManagerFactory.getCommandManager().createUpdateUserSmsCommand(ids, userPin, deviceSn);	
		}
		return "smsList";
	}
	
	/**
	 * Send delete Short Message command issued to the specified device,Corresponding to the "DATA DELETE SMS" command.
	 * @return
	 */
	public String deleteSms() {
		String smsId = request.getParameter("smsId");
		String destSn = request.getParameter("destSn");
		
		if (null == smsId || null == destSn || smsId.isEmpty() || destSn.isEmpty()) {
			return "";
		}
		
		String[] ids = smsId.split(",");
		String[] sns = destSn.split(",");
		for (String devcieSn : sns) {
			ManagerFactory.getCommandManager().createDeleteSmsCommand(ids, devcieSn);			
		}
		
		return "smsList";
	}
	
	/**
	 * Delete short message on the server
	 * @return
	 */
	public String deleteSmsServ() {
		String smsId = request.getParameter("smsId");
		
		if (null == smsId || smsId.isEmpty() ) {
			return "";
		}
		
		String[] ids = smsId.split(",");
		ManagerFactory.getMessageManager().deleteSms(ids);
		
		return "smsList";
	}
	
	
	
}
