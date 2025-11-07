package com.zk.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.Adv;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.PagenitionUtil;

public class AdvAction implements ServletRequestAware,ServletResponseAware{

	private HttpServletRequest request;
	private HttpServletResponse response;
	private static int curPage = 1;
	private static Logger logger = Logger.getLogger(AdvAction.class);
	
	private File advFile; //Receive file
	private String advFileContentType;//File type
	private String advFileFileName;//File name
	
	public String getAdvFileContentType() {
		return advFileContentType;
	}

	public void setAdvFileContentType(String advFileContentType) {
		this.advFileContentType = advFileContentType;
	}

	public String getAdvFileFileName() {
		return advFileFileName;
	}

	public void setAdvFileFileName(String advFileFileName) {
		this.advFileFileName = advFileFileName;
	}

	public File getAdvFile() {
		return advFile;
	}

	public void setAdvFile(File advFile) {
		this.advFile = advFile;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	} 
	
	public String advList() {
		int recCount = 0;
		int pageCount = 0;
		/**Get interface parameters*/
		String deviceSn = request.getParameter("deviceSn");
		//String code = request.getParameter("code");
		String act = request.getParameter("act");
		String jumpPage = request.getParameter("jump");
		/**Get the total number of records based on conditions*/
		recCount = ManagerFactory.getAdvManager().getAdvCount(deviceSn);
		/**Calculate the total number of pages and the current page number*/
		pageCount = PagenitionUtil.getPageCount(recCount);
		curPage = PagenitionUtil.getCurPage(jumpPage, act, pageCount, curPage);
		/**Calculation start recording*/
		int startRec = (curPage - 1) * PagenitionUtil.getPageSize();
		/**Search record lists based on page conditions and page information*/
		List<Adv> list = ManagerFactory.getAdvManager().fatchAllAdv(deviceSn, startRec, PagenitionUtil.getPageSize());
		/**Set interface parameters*/
		request.setAttribute("curPage", curPage);
		request.setAttribute("pageCount", pageCount);
		request.setAttribute("advList", list);
		request.setAttribute("devList", PushUtil.getDeviceList());
		Locale local = request.getLocale();
		ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource", local);
		if (null == deviceSn || deviceSn.isEmpty()) {
			deviceSn = resource.getString("meet.search.by.device");
		}
		request.setAttribute("byDeviceSn", deviceSn);
		/*if (null == code || code.isEmpty()) {
			code = resource.getString("meet.serach.input.name.or.code");
		}
		request.setAttribute("byCode21", code);*/
		return "advList";
	}
	
	/**
	 * Removes the adv data by adv id from the server.
	 * @return
	 */
	public String deleteAdvServ() {
		String advIds = request.getParameter("advId");
		if (null == advIds || advIds.isEmpty()) {
			return "advList";
		}
		String[] ids = advIds.split(",");
		ManagerFactory.getAdvManager().deleteAdv(ids);
		return "advList";
	}
	
	/**
	 * Removes the adv data by adv id from the device,Corresponding to the "DATA DELETE ADV" command.
	 * @return
	 */
	public String deleteAdvDev() {
		final String advIdStr = request.getParameter("advId");
		if (null == advIdStr || "".equals(advIdStr)) {
			return "advList";
		}
		new Thread(new Runnable() {
			public void run() {
				String[] ids = advIdStr.split(",");
				ManagerFactory.getCommandManager().createDeleteAdvCommandByIds(ids, null);
			}
		}).start();
		return "advList";
	}
	
	/**
	 * Transmitting adv data to the adv's device,Corresponding to the "DATA UPDATE" command.
	 * @return
	 */
	public String sendAdvDev() {
		final String advIdStr = request.getParameter("advId");
		if (null == advIdStr || "".equals(advIdStr)) {
			return "";
		}
		
		new Thread(new Runnable() {
			public void run() {
				String[] ids = advIdStr.split(",");
				ManagerFactory.getCommandManager().createUpdateAdvsCommandByIds(ids, null);
			}
		}).start();
		
		return "advList";
	}
	
	/**
	 * new or edit adv
	 * <li>act=new new adv
	 * <li>act=edit edit adv
	 * @return
	 */
	public String newAdv() {
		String act = request.getParameter("act");
		String advId = request.getParameter("advId");
		
		if (null == act || act.isEmpty()) {
			return "advList";
		}
		if ("new".equals(act)){
			request.setAttribute("act", "new");
			request.setAttribute("devList", PushUtil.getDeviceList());
			return "newAdv";	
		} else if ("edit".equals(act)) {
			if (null == advId || advId.isEmpty()) {
				return "advList";
			} else {
				try{
				int id = Integer.valueOf(advId);
				Adv adv = ManagerFactory.getAdvManager().getAdvById(id);
				request.setAttribute("act", "edit");
				request.setAttribute("adv", adv);
				return "newAdv"; 
				} catch (NumberFormatException e) {
					logger.error(e);
					return "advList";
				}
			}
		}
		return "advList";
	}
	
	/**
	 * Save new adv or save edit adv
	 * <li>when act parameter value is "new" will process new adv
	 * <li>when act parameter value is "edit" will process edit adv
	 * @return
	 */
	public String editAdv() {
		String act = request.getParameter("act");
		String deviceSn = request.getParameter("devSn");
		String type = request.getParameter("type");
		String fileName = request.getParameter("fileName");
		String advId = request.getParameter("advId");
		if (null == act || act.isEmpty()) {
			return "redirectAdvList";
		}
		if ("new".equals(act)) {
			if (null == deviceSn || deviceSn.isEmpty()
					|| null == type || type.isEmpty()
					|| null == fileName || fileName.isEmpty()
					|| null == advFileFileName || advFileFileName.isEmpty()
					) {
				return "redirectAdvList";
			}
			
			Adv adv = new Adv();
			//Handle the file
			String realPhotoPath = ServletActionContext.getServletContext().getRealPath(File.separator + "putfile");
			if(advFileFileName != null) {
				File putFile = new File(new File(realPhotoPath), advFileFileName);
				if (!putFile.getParentFile().exists())
				{
					putFile.getParentFile().mkdirs();
				}
				else if (putFile.exists())
				{
					putFile.delete();
				}
				try {
					advFile.createNewFile();
					FileUtils.copyFile(advFile,putFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				advFile.delete();
				String personPhotoPath = "/putfile/" + advFileFileName;
				String suffix = advFileFileName.substring(advFileFileName.lastIndexOf("."));// advFileFileName.split("\\.")[1];
				adv.setFileName(fileName + suffix);
				adv.setUrl(personPhotoPath);
			}
			
			adv.setType(Integer.parseInt(type));
			//adv.setFileName(fileName);
			adv.setDeviceSn(deviceSn);
			List<Adv> list = new ArrayList<Adv>();
			list.add(adv);
			ManagerFactory.getAdvManager().createAdv(list);
			return "redirectAdvList";
		} else if ("edit".equals(act)){
			if (//null == metName || metName.isEmpty()||
					 null == advId || advId.isEmpty()) {
				return "redirectAdvList";
			}
			try{
				int id = Integer.valueOf(advId);
				Adv adv = ManagerFactory.getAdvManager().getAdvById(id);
				adv.setType(Integer.parseInt(type));
				adv.setFileName(fileName);
				adv.setDeviceSn(deviceSn);
				List<Adv> list = new ArrayList<Adv>();
				list.add(adv);
				ManagerFactory.getAdvManager().createAdv(list);
				return "redirectAdvList"; 
			} catch (NumberFormatException e) {
				logger.error(e);
				return "advList";
			}
		}
		return "redirectAdvList";
	}
	
}
