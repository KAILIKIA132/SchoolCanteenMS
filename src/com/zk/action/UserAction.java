package com.zk.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import java.util.Base64;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.BaseImgEncodeUtil;
import com.zk.util.PagenitionUtil;

public class UserAction implements ServletRequestAware,ServletResponseAware{
	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	private static Logger logger = Logger.getLogger(UserAction.class);
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	private File userPic; // myFileå±žæ€§ç”¨æ�¥å°�è£…ä¸Šä¼ çš„æ–‡ä»¶
	
	/**
	 * Get User Information List
	 * @return
	 */
	public String userList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String deviceSn = request.getParameter("deviceSn");
		String userPin = request.getParameter("userPin");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getUserInfoManager().getUserInfoCount(deviceSn, userPin);
		/**Calculate the total number of pages and the current page number*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Search record lists based on page conditions and page information*/
		List<UserInfo> list = ManagerFactory.getUserInfoManager().fatchAllUser(deviceSn, userPin, startRec, PagenitionUtil.getPageSize());
		/**Set the user's face total number of data*/
		for (UserInfo userInfo : list) {
			userInfo.setUserFaceCount(userInfo.getUserFaceCount());
			//userInfo.setUserPalmCount(userInfo.getUserPalmCount());
		}
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("userInfoList", list);
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
		return "userList";
	}
	
	/**
	 * Removes the user data by user id from the server.
	 * @return
	 */
	public String deleteUserServ() {
		String userIds = request.getParameter("userId");
		if (null == userIds || userIds.isEmpty()) {
			return "userList";
		}
		String[] ids = userIds.split(",");
		ManagerFactory.getUserInfoManager().deleteUserInfo(ids);
		return "userList";
	}
	
	/**
	 * Removes the user data by user id from the device,Corresponding to the "DATA DELETE USERINFO" command.
	 * @return
	 */
	public String deleteUserDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteUserCommandByIds(ids, null);
			}
		}).start();
		return "";
	}
	
	/**
	 * Removes the fingerprint data by user id from the device,Corresponding to the "DATA DELETE FINGERTMP" command.
	 * @return
	 */
	public String deleteUserFpDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteUserFpByIds(ids, null);
			}
		}).start();
		
		return "";
	}

	/**
	 * Removes the user photos by user id from the device,Corresponding to the "DATA DELETE USERPIC" command.
	 * @return
	 */
	public String deleteUserPicDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteUserPicByIds(ids, null);
			}
		}).start();
		
		return "";
	}
	
	/**
	 * Removes the Facetemplate data by user id from the device.Corresponding to the "DATA DELETE FACE" command.
	 * @return
	 */
	public String deleteUserFaceDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteFaceByIds(ids, null);
			}
		}).start();
		
		return "";
	}
	
	/**
	 * Removes the Plamtemplate data by user id from the device.Corresponding to the "DATA DELETE PLAM" command.
	 * @return
	 *//*
	public String deleteUserPlamDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createDeletePlamByIds(ids, null);
			}
		}).start();
		
		return "";
	}*/
	/**
	 * Transmitting user data(user basic information/fingerprint/face/User photo) to the user's device,Corresponding to the "DATA UPDATE" command.
	 * @return
	 */
	public String sendUserDev() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		
		new Thread(new Runnable() {
			public void run() {
				String[] ids = userIdStr.split(",");
				ManagerFactory.getCommandManager().createUpdateUserInfosCommandByIds(ids, null);
			}
		}).start();
		
		return "userList";
	}
	
	/**
	 * Transmitting user data(user basic information/fingerprint/face/User photo) to the specified device,Corresponding to the "DATA UPDATE" command.
	 * @return
	 */
	public String toNewDevice() {
		final String destSn = request.getParameter("destSn");
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr) || null == destSn || "".equals(destSn)) {
			return "";
		}
		
		String[] destSns = destSn.split(",");
		System.out.println("SN "+destSn);
		for (final String deviceSn : destSns) {
			new Thread(new Runnable() {
				public void run() {
					String[] ids = userIdStr.split(",");
					ManagerFactory.getCommandManager().createUpdateUserInfosCommandByIds(ids, deviceSn);
				}
			}).start();	
		}
		
		return "";
	}
	
	/**
	 * Removes the Facetemplate data by user id from the server.
	 * @return
	 */
	public String deleteUserFaceServ() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		String[] ids = userIdStr.split(",");
		ManagerFactory.getUserInfoManager().deleteFaceFromServer(ids);
		return "";
	}
	/**
	 * Removes the Plamtemplate data by user id from the server.
	 * @return
	 */
	public String deleteUserPlamServ() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		String[] ids = userIdStr.split(",");
		ManagerFactory.getUserInfoManager().deletePlamFromServer(ids);
		return "";
	}
	
	/**
	 * Removes the Fingerprint data by  user id from the server.
	 * @return
	 */
	public String deleteUserFpServ() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		String[] ids = userIdStr.split(",");
		ManagerFactory.getUserInfoManager().deleteFpFromServer(ids);
		return "";
	}
	
	/**
	 * Removes the user photos by user id from the server.
	 * @return
	 */
	public String deleteUserPicServ() {
		final String userIdStr = request.getParameter("userId");
		if (null == userIdStr || "".equals(userIdStr)) {
			return "";
		}
		String[] ids = userIdStr.split(",");
		ManagerFactory.getUserInfoManager().deleteUserPicFromServer(ids);
		return "";
	}
	
	/**
	 * new or edit user info 
	 * <li>act=new new user info
	 * <li>act=edit edit user info
	 * @return
	 */
	public String newUser() {
		String act = request.getParameter("act");
		String userId = request.getParameter("userId");
		
		if (null == act || act.isEmpty()) {
			return "userList";
		}
		if ("new".equals(act)){
			request.setAttribute("act", "new");
			request.setAttribute("devList", PushUtil.getDeviceList());
			return "newUser";	
		} else if ("edit".equals(act)) {
			if (null == userId || userId.isEmpty()) {
				return "userList";
			} else {
				try{
				int id = Integer.valueOf(userId);
				UserInfo info = ManagerFactory.getUserInfoManager().getUserInfoById(id);
				request.setAttribute("act", "edit");
				request.setAttribute("userInfo", info);
				return "newUser"; 
				} catch (NumberFormatException e) {
					logger.error(e);
					return "userList";
				}
			}
		}
		return "userList";
	}
	
	/**
	 * Save new user info or save edit user info
	 * <li>when act parameter value is "new" will process new user info
	 * <li>when act parameter value is "edit" will process edit user info
	 * @return
	 * @throws FileUploadException 
	 */
	public String editUser() throws FileUploadException {
		String img = null;
		String realPhotoPath = ServletActionContext.getServletContext().getRealPath(File.separator + "pers" + File.separator + "photo");
		long nowTime = System.currentTimeMillis();
		String fileName = nowTime + ".jpg";
		File userPicFile =  new File(new File(realPhotoPath), fileName);
		if(userPic != null) {
			InputStream in = null;
			byte[] data = null;
			 try {
				 if (!userPicFile.getParentFile().exists())
					{
					 userPicFile.getParentFile().mkdirs();
					}
					else if (userPicFile.exists())
					{
						userPicFile.delete();
					}
				 BaseImgEncodeUtil.createZoomImage(userPic, userPicFile, 640, 1);
				 in = new FileInputStream(userPicFile);
				 data = new byte[in.available()];
				 in.read(data);
				 in.close();
				 img = Base64.getEncoder().encodeToString(data);
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				if(in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		String act = request.getParameter("act");
		String deviceSn = request.getParameter("deviceSn");
		String userPin = request.getParameter("userPin");
		String userName = request.getParameter("userName");
		String userCard = request.getParameter("userCard");
		String userPassword = request.getParameter("userPassword");
		String privilege = request.getParameter("privilege");
		String category = request.getParameter("category");
		String userId = request.getParameter("userId");
		if (null == act || act.isEmpty()) {
			return "redirectToUserList";
		}
		if ("new".equals(act)) {
			if (null == deviceSn || deviceSn.isEmpty()
					|| null == userPin || userPin.isEmpty()
					|| null == userName || userName.isEmpty()
					|| null == userCard || userCard.isEmpty()
					|| null == userPassword || userPassword.isEmpty()) {
				return "redirectToUserList";
			}
			UserInfo info = new UserInfo();
			info.setUserPin(userPin);
			info.setName(userName);
			info.setMainCard(userCard);
			info.setPassword(userPassword);
			info.setDeviceSn(deviceSn);
			if(img != null) {
				img = img.replace("\r\n", "");
				img = img.replace("\t", "");
				img = img.replace(" ", "");
				info.setPhotoIdContent(img);
				info.setPhotoIdName(userPin + ".jpg");
				info.setPhotoIdSize(img.length());
			}
			info.setPrivilege(Integer.parseInt(privilege));
			info.setCategory(Integer.parseInt(category));
			List<UserInfo> list = new ArrayList<UserInfo>();
			list.add(info);
			ManagerFactory.getUserInfoManager().createUserInfo(list);
			return "redirectToUserList";
		} else if ("edit".equals(act)){
			if (null == userName || userName.isEmpty()
					|| null == userId || userId.isEmpty()) {
				return "redirectToUserList";
			}
			try{
				int id = Integer.valueOf(userId);
				UserInfo info = ManagerFactory.getUserInfoManager().getUserInfoById(id);
				info.setName(userName);
				info.setPrivilege(Integer.parseInt(privilege));
				info.setCategory(Integer.parseInt(category));
				List<UserInfo> list = new ArrayList<UserInfo>();
				list.add(info);
				ManagerFactory.getUserInfoManager().createUserInfo(list);
				return "redirectToUserList"; 
			} catch (NumberFormatException e) {
				logger.error(e);
				return "redirectToUserList";
			}
		}
		return "redirectToUserList";
	}

	public File getUserPic() {
		return userPic;
	}

	public void setUserPic(File userPic) {
		this.userPic = userPic;
	}
}
