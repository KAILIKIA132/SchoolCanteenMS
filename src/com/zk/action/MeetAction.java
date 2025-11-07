package com.zk.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.zk.pushsdk.po.MeetInfo;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.PagenitionUtil;

public class MeetAction implements ServletRequestAware,ServletResponseAware{

	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	private static Logger logger = Logger.getLogger(MeetAction.class);
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	} 
	
	public String meetinfoList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String deviceSn = request.getParameter("deviceSn");
		String code = request.getParameter("code");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getMeetInfoManager().getMeetInfoCount(deviceSn, code);
		/**Calculate the total number of pages and the current page number*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Search record lists based on page conditions and page information*/
		List<MeetInfo> list = ManagerFactory.getMeetInfoManager().fatchAllMeet(deviceSn, code, startRec, PagenitionUtil.getPageSize());
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("meetInfoList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("meet.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		if (null == code || code.isEmpty()) {
			code = resource.getString("meet.serach.input.name.or.code");
		}
		request.setAttribute("byCode21", code);
		return "meetList";
	}
	
	/**
	 * Removes the meet data by meet id from the server.
	 * @return
	 */
	public String deleteMeetServ() {
		String meetIds = request.getParameter("meetId");
		if (null == meetIds || meetIds.isEmpty()) {
			return "meetList";
		}
		String[] ids = meetIds.split(",");
		ManagerFactory.getMeetInfoManager().deleteMeetInfo(ids);
		return "meetList";
	}
	
	/**
	 * Removes the meet data by meet id from the device,Corresponding to the "DATA DELETE MEETINFO" command.
	 * @return
	 */
	public String deleteMeetDev() {
		final String meetIdStr = request.getParameter("meetId");
		if (null == meetIdStr || "".equals(meetIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = meetIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteMeetCommandByIds(ids, null);
			}
		}).start();
		return "";
	}
	
	/**
	 * Transmitting meet data to the meet's device,Corresponding to the "DATA UPDATE" command.
	 * @return
	 */
	public String sendMeetDev() {
		final String meetIdStr = request.getParameter("meetId");
		if (null == meetIdStr || "".equals(meetIdStr)) {
			return "";
		}
		
		new Thread(new Runnable() {
			public void run() {
				String[] ids = meetIdStr.split(",");
				ManagerFactory.getCommandManager().createUpdateMeetInfosCommandByIds(ids, null);
			}
		}).start();
		
		return "meetList";
	}
	
	/**
	 * new or edit meet info 
	 * <li>act=new new meet info
	 * <li>act=edit edit meet info
	 * @return
	 */
	public String newMeet() {
		String act = request.getParameter("act");
		String meetId = request.getParameter("meetId");
		
		if (null == act || act.isEmpty()) {
			return "meetList";
		}
		if ("new".equals(act)){
			request.setAttribute("act", "new");
			request.setAttribute("devList", PushUtil.getDeviceList());
			return "newMeet";	
		} else if ("edit".equals(act)) {
			if (null == meetId || meetId.isEmpty()) {
				return "meetList";
			} else {
				try{
				int id = Integer.valueOf(meetId);
				MeetInfo info = ManagerFactory.getMeetInfoManager().getMeetInfoById(id);
				request.setAttribute("act", "edit");
				request.setAttribute("meetInfo", info);
				return "newMeet"; 
				} catch (NumberFormatException e) {
					logger.error(e);
					return "meetList";
				}
			}
		}
		return "meetList";
	}
	
	/**
	 * Save new meet info or save edit meet info
	 * <li>when act parameter value is "new" will process new meet info
	 * <li>when act parameter value is "edit" will process edit meet info
	 * @return
	 */
	public String editMeet() {
		String act = request.getParameter("act");
		String deviceSn = request.getParameter("deviceSn");
		String code = request.getParameter("code");
		String metName = request.getParameter("metName");
		String metStarSignTm = request.getParameter("metStarSignTm");
		String metLatSignTm = request.getParameter("metLatSignTm");
		String earRetTm = request.getParameter("earRetTm");
		String latRetTm = request.getParameter("latRetTm");
		String metStrTm = request.getParameter("metStrTm");
		String metEndTm = request.getParameter("metLatSignTm");
		String meetId = request.getParameter("meetId");
		if (null == act || act.isEmpty()) {
			return "meetList";
		}
		if ("new".equals(act)) {
			if (null == deviceSn || deviceSn.isEmpty()
					|| null == code || code.isEmpty()
					|| null == metName || metName.isEmpty()
					|| null == metStarSignTm || metStarSignTm.isEmpty()
					|| null == metLatSignTm || metLatSignTm.isEmpty()
					|| null == earRetTm || earRetTm.isEmpty()
					|| null == latRetTm || latRetTm.isEmpty()
					|| null == metStrTm || metStrTm.isEmpty()
					|| null == metEndTm || metEndTm.isEmpty()) {
				return "meetList";
			}
			MeetInfo info = new MeetInfo();
			info.setCode(code);
			info.setMetName(metName);
			info.setMetStarSignTm(metStarSignTm);
			info.setMetLatSignTm(metLatSignTm);
			info.setEarRetTm(earRetTm);
			info.setLatRetTm(latRetTm);
			info.setMetStrTm(metStrTm);
			info.setMetEndTm(metEndTm);
			info.setDeviceSn(deviceSn);
			List<MeetInfo> list = new ArrayList<MeetInfo>();
			list.add(info);
			ManagerFactory.getMeetInfoManager().createMeetInfo(list);
			return "meetList";
		} else if ("edit".equals(act)){
			if (null == metName || metName.isEmpty()
					|| null == meetId || meetId.isEmpty()) {
				return "meetList";
			}
			try{
				int id = Integer.valueOf(meetId);
				MeetInfo info = ManagerFactory.getMeetInfoManager().getMeetInfoById(id);
				info.setMetName(metName);
				info.setMetStarSignTm(metStarSignTm);
				info.setMetLatSignTm(metLatSignTm);
				info.setEarRetTm(earRetTm);
				info.setLatRetTm(latRetTm);
				info.setMetStrTm(metStrTm);
				info.setMetEndTm(metEndTm);
				List<MeetInfo> list = new ArrayList<MeetInfo>();
				list.add(info);
				ManagerFactory.getMeetInfoManager().createMeetInfo(list);
				return "meetList"; 
			} catch (NumberFormatException e) {
				logger.error(e);
				return "meetList";
			}
		}
		return "meetList";
	}
	
	/**
	 * Get users by which device
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void getUserByDev() throws JSONException, IOException {
		String meetId = request.getParameter("meetId");
		MeetInfo meetInfo = ManagerFactory.getMeetInfoManager().getMeetInfoById(Integer.parseInt(meetId));
		String deviceSn = meetInfo.getDeviceSn();
		List<UserInfo> users = ManagerFactory.getUserInfoManager().fatchAllUser(deviceSn);
		JSONArray jsonArr = new JSONArray();
		for(UserInfo user : users) {
			JSONObject obj = new JSONObject();
			obj.put("userId", user.getUserId());
			obj.put("userPin", user.getUserPin());
			obj.put("userName", user.getName());
			obj.put("meetCode", user.getMeetCode());
			jsonArr.put(obj);
		}
		
		PrintWriter out = response.getWriter();
		out.write(jsonArr.toString());
	}
	
	/**
	 * Add meet person
	 */
	public String addPersMeet() {
		String meetId = request.getParameter("meetId");
		String userPins = request.getParameter("userPins");
		String[] pins = userPins.split(",");
		MeetInfo meetInfo = ManagerFactory.getMeetInfoManager().getMeetInfoById(Integer.parseInt(meetId));
		String devSn = meetInfo.getDeviceSn();
		String code = meetInfo.getCode();
		/**CLEAR PERSMEET*/
		ManagerFactory.getCommandManager().createClearPersMeetCommand(code);
		
		List<UserInfo> list = new ArrayList<UserInfo>();
		ManagerFactory.getUserInfoManager().createUserInfo(list);
		for(String pin : pins) {
			UserInfo userInfo = ManagerFactory.getUserInfoManager().getUserInfoByPinAndSn(pin, devSn);
			userInfo.setMeetCode(code);
			list.add(userInfo);
			ManagerFactory.getCommandManager().createUpdatePersMeetCommand(devSn,code,pin);
		}
		ManagerFactory.getUserInfoManager().createUserInfo(list);
		return "meetList";
	}
	
	/**
	 * Clear meet data in device
	 * @return
	 */
	public String clearMeetDev() {
		
		final String destSn = request.getParameter("destSn");
		
		String[] destSns = destSn.split(",");
		for (final String deviceSn : destSns) {
			new Thread(new Runnable() {
				public void run() {
					ManagerFactory.getCommandManager().createClearMeetCommand(deviceSn);
				}
			}).start();	
		}
		
		return "meetList";
	}
	
	public String clearPersMeetDev() {
		
		final String destSn = request.getParameter("destSn");
		
		String[] destSns = destSn.split(",");
		for (final String deviceSn : destSns) {
			new Thread(new Runnable() {
				public void run() {
					ManagerFactory.getCommandManager().createClearPersMeetCommand(deviceSn);
				}
			}).start();	
		}
		
		return "meetList";
	}

}
