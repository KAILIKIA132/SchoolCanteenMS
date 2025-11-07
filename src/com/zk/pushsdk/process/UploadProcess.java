package com.zk.pushsdk.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zk.exception.ParseDataException;
import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.AttPhoto;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.po.PersonBioTemplate;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.Constants;
import com.zk.pushsdk.util.DataParseUtil;
import com.zk.pushsdk.util.DevCmdUtil;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.ConfigUtil;

/**
 * <code>UploadProcess</code>process the data upload to the server.
 * @author seiya
 *
 */
public class UploadProcess {

	private static Logger logger = Logger.getLogger(UploadProcess.class);
	
	/**
	 * Process "cdata" GET request.
	 * <li>initialization information
	 * <li>remote attendance
	 * @param request
	 * @param response
	 * @return
	 */
	public String getCdata(HttpServletRequest request,
			HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String dateTime = DataParseUtil.getDateTimeInGMTFormat();
		response.addHeader("Date",dateTime);
		String deviceSn = request.getParameter("SN");
		/**option parameters. it is for initialization*/
		String options = request.getParameter("options");
		
		/** table and PIN parameters. it is for remote attendance*/
		String table = request.getParameter("table");
		String userPin = request.getParameter("PIN");
		
		logger.info("get request and make the device options :" 
				+ request.getRemoteAddr()+";"
				+ request.getRequestURL() + "?" 
				+ request.getQueryString());
		try {
			/**there is no Device SN*/
			if (null == deviceSn || deviceSn.isEmpty()) {
				response.getWriter().write("error");
				logger.info("device is null or empty");
				return null;
			}
			/**Define the character encoding type by the current language*/
			String lang = PushUtil.getDeviceLangBySn(deviceSn);
			if (Constants.DEV_LANG_ZH_CN.equals(lang)) {
				response.setCharacterEncoding("GB2312");
			} else {
				response.setCharacterEncoding("UTF-8");
			}
			/**
			 * <li>if the parameter options equal "all", it is initialization connection,
			 * <li>if the parameter table equal "RemoteAtt", it is remote attendance
			 * */
			if (null != options && "all".equals(options)) {
				DeviceInfo devInfo = getDeviceInfo(deviceSn, request);
				if (null != devInfo) {
					String deviceOptions = getDeviceOptions(devInfo);
					response.getWriter().write(deviceOptions);
					logger.info(deviceOptions);					
				} else {
					response.getWriter().write("error");
					logger.info("option=all device is null");
				}
			} else if (null != table && "RemoteAtt".equals(table)) {
				/**userPin is null*/
				if (null == userPin || userPin.isEmpty()) {
					response.getWriter().write("error");
					logger.info("remoteatt userpin is null or empty");
					return null;
				}
				if (0 == processRemoteAtt(userPin, deviceSn, response, lang)) {
				} else {
					response.getWriter().write("error");
					logger.info("process remote att failed");
				}
			} else {
				response.getWriter().write("error");
				logger.info("device request not existing");
				return null;
			}
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	/**
	 * Gets the device info by Device SN<br>
	 * <li>Query the device info from Server. if it is existed, return it.
	 * <li>if not, create a new device info.
	 * 
	 * @param deviceSn
	 * @param request
	 * @return
	 * device info<code>DeviceInfo</code>
	 */
	private DeviceInfo getDeviceInfo(String deviceSn, HttpServletRequest request) {
		 DeviceInfo devInfo = ManagerFactory.getDeviceManager()
				.getDeviceInfoBySn(deviceSn);
		/**push version*/
		String pushver = request.getParameter("pushver");
		/**Current language*/
		String language = request.getParameter("language");
		/**the communication key between Server and device*/
		String pushcommkey = request.getParameter("pushcommkey");
		
		/**Device IP Address*/
		String ipAddress = request.getRemoteAddr();
		if (null == devInfo) {			
			/**if the device is not existed. Set the device info as default value.*/
			devInfo = new DeviceInfo();
			devInfo.setIpAddress(ipAddress);
		
			devInfo.setDeviceName(deviceSn + "(" + ipAddress + ")");
			devInfo.setAliasName(ipAddress);
			devInfo.setTransInterval(1);
			devInfo.setDeviceSn(deviceSn);
			devInfo.setState("Online");
			devInfo.setLogStamp("0");
			devInfo.setOpLogStamp("0");
			devInfo.setPhotoStamp("0");
			devInfo.setDevLanguage(language);
			if (null == pushver) {
				devInfo.setPushVersion("1.0.0");
			} else {
				devInfo.setPushVersion(pushver);
			}
			//devInfo.setDevFuns("0");
			devInfo.setTimeZone("+200");
			devInfo.setTransTimes("00:00;14:05");
			devInfo.setPushCommKey(pushcommkey);
			devInfo.setLastActivity(PushUtil.dateFormat.format(new Date()));
			devInfo.setBioDataStamp("0");
			devInfo.setIdCardStamp("0");
			devInfo.setErrorLogStamp("0");
			System.out.println("getting "+ devInfo.getDeviceSn());
			devInfo = ManagerFactory.getDeviceManager().createDeviceInfo(devInfo);
			System.out.println("getting DeviceSn"+ devInfo.getDeviceSn());
			/**update the device info cache*/
			PushUtil.updateDevMap(devInfo);
			
			//new device add a INFO command
			ManagerFactory.getCommandManager().createINFOCommand(
			devInfo.getDeviceSn());
		} else {
			devInfo.setIpAddress(ipAddress);
			devInfo.setState("Online");//Device Status
			devInfo.setLastActivity(PushUtil.dateFormat.format(new Date()));
			ManagerFactory.getDeviceManager().updateDeviceInfo(devInfo);
		}
		
		return devInfo;
	}

	/**
	 * Update device table data Stamp
	 * 
	 * @param deviceSn
	 * @param request
	 * @return
	 * <li> -1: device non-existed
	 * <li> 0 : OK
	 */
	private int updateDeviceStamp(String deviceSn, HttpServletRequest request) {
		String stamp = request.getParameter("Stamp");
		String opStamp = request.getParameter("OpStamp");
		String photoStamp = request.getParameter("PhotoStamp");
		String bioDataStamp = request.getParameter("BioDataStamp");
		String idCardStamp = request.getParameter("IdCardStamp");
		String errorLogStamp = request.getParameter("ErrorLogStamp");
		DeviceInfo deviceInfo = ManagerFactory.getDeviceManager().getDeviceInfoBySn(deviceSn);
		
		if (null == deviceInfo) {
			return -1;
		}
		if (null != stamp) {
			deviceInfo.setLogStamp(stamp);
		}
		if (null != opStamp) {
			deviceInfo.setOpLogStamp(opStamp);
		}
		if (null != photoStamp) {
			deviceInfo.setPhotoStamp(photoStamp);
		}
		
		if (null != bioDataStamp) {
			deviceInfo.setBioDataStamp(bioDataStamp);
		}
		
		if (null != idCardStamp) {
			deviceInfo.setIdCardStamp(idCardStamp);
		}
		
		if (null != errorLogStamp) {
			deviceInfo.setErrorLogStamp(errorLogStamp);
		}
		
		
		
		deviceInfo.setState("connection");
		deviceInfo.setLastActivity(PushUtil.dateFormat.format(new Date()));
		ManagerFactory.getDeviceManager().updateDeviceInfo(deviceInfo);
		return 0;
	}
	
	/**
	 * Process the POST cdata HTTP requestï¼ŒGets the data from device, process the attendance photo.
	 * @param request
	 * @param response
	 * @return
	 */
	public String postCdata(HttpServletRequest request,
			HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String dateTime = DataParseUtil.getDateTimeInGMTFormat();
		response.addHeader("Date",dateTime);
		String deviceSn = request.getParameter("SN");
		/**Data table from device*/
		String table = request.getParameter("table");
		
		/**Current Language of device*/ 
		String lang = PushUtil.getDeviceLangBySn(deviceSn);
		
		logger.info("device language:"+ lang 
				+ ",get request and begin update device stamp:" 
				+ request.getRemoteAddr()+";"
				+ request.getRequestURL() + "?" + request.getQueryString());
		try {
			/**update the device stamp for current table.*/
			int re = updateDeviceStamp(deviceSn, request);
			if (0 != re) {
				response.getWriter().write("error:device not exist");
				return null;
			}
			logger.info("update device stamp end and get stream.");
			int iReadLength = 0;
			byte[] buffer = new byte[1024];
			StringBuffer bufferData = new StringBuffer();
			InputStream postData = request.getInputStream();
			/**Gets the path of attendance photo.*/
			String pathStr = request.getSession().getServletContext().getRealPath("\\AttPhoto");
			FileOutputStream fileOS = null;
			while ((iReadLength = postData.read(buffer)) != -1) {
				String inString = new String(buffer, 0, iReadLength);
				/**check if it is attendance.*/
				if ((inString.contains("CMD=realupload")
						|| inString.contains("CMD=uploadphoto")) && "ATTPHOTO".equals(table)) {
					/**Create attendance photo*/
					File path = createAttPhotoFile(inString, pathStr);
					if (path == null) {
						response.getWriter().write("OK");
						return null;
					}
					String filePath = path.toString();
					fileOS = new FileOutputStream(filePath);
					String cmdUpload = "";
					 if (inString.contains("CMD=realupload")) {
						cmdUpload = "CMD=realupload";
					} else {
						cmdUpload = "CMD=uploadphoto";
					}
					/**Gets the data stream, put it into the file*/
					inString = inString.substring(0, inString
							.indexOf(cmdUpload)
							+ cmdUpload.length());
					fileOS.write(buffer, inString.length() + 1, iReadLength
							- (inString.length() + 1));
				} else {
					/**if it is photo. continue.*/
					if (fileOS != null) {
						fileOS.write(buffer, 0, iReadLength);
					} else {
						/**Define the character encoding type by the current language*/
						if (Constants.DEV_LANG_ZH_CN.equals(lang)) {
							bufferData.append(new String(buffer, 0, iReadLength,
							"GB2312"));
						} else {
							bufferData.append(new String(buffer, 0, iReadLength,
							"UTF-8"));
						}
					}
				}
			}
			if (fileOS != null) {
				fileOS.close();
			}
			String data = bufferData.toString();
			if(table.equals("options")) {
				updateDevInfo(data, deviceSn);
			}
			
			logger.info("data:" + data);
			logger.info("end get stream and process data");
			/**Data processing*/
			int result = processDatas(table, data, deviceSn);
			logger.info("end process data and return msg to device");
			/**return the result.*/
			if (0 == result) {
				response.getWriter().write("OK");
				logger.info("return msg to device and request over OK");
			} else {
				response.getWriter().write("error");
				logger.info("return msg to device and request over error");
			}
			
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	int updateDevInfo(String data, String devSn) {
		DeviceInfo deviceInfo = ManagerFactory.getDeviceManager().getDeviceInfoBySn(devSn);
		if (null == deviceInfo) {
			return -1;
		}
		
		data = data.substring(0, data.length()-1);           //remove curly brackets
		
		String[] keyValuePairs = data.split(",");              //split the string to create key-value pairs
		Map<String,String> map = new HashMap<>();               
		
		for(String pair : keyValuePairs)                        //iterate over the pairs
		{
		    String[] entry = pair.split("=");
		  //split the pairs to get key and value 
		    if(entry.length > 1)
		    	map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		}
		
		if(map.containsKey("IRTempDetectionFunOn")) {
			try {
				String tempReading = map.get("IRTempDetectionFunOn");
				
				deviceInfo.setTemperature(tempReading);
			} catch (Exception e) {
				deviceInfo.setTemperature("255");
			}
		}
	
		if(map.containsKey("MaskDetectionFunOn")) {
			try {
				int maskFlag = Integer.parseInt(map.get("MaskDetectionFunOn"));
			
				deviceInfo.setMask(maskFlag);
			} catch (Exception e) {
				deviceInfo.setMask(255);
			}
		}
		
		if(map.containsKey("PvFunOn")) {
			try {
				int palm = Integer.parseInt(map.get("PvFunOn"));
				deviceInfo.setPalm(palm);
			} catch (Exception e) {
				deviceInfo.setPalm(255);
			}
		}
		
		PushUtil.updateDevMap(deviceInfo);
		return 0;
	}
	/**
	 * Process remote attendance request. Send the user info(info and FP) into the device.
	 * @param userPin	User ID
	 * @param deviceSn	Device SN
	 * @param response
	 * @return
	 * <li> -1: user non-existed
	 * <li> 0 : OK
	 */
	private int processRemoteAtt(String userPin, String deviceSn, 
			HttpServletResponse response, String lang) {
		/**Gets the user info by User ID and Device SN*/
		UserInfo userInfo = ManagerFactory.getUserInfoManager()
				.getUserInfoByPinAndSn(userPin, deviceSn);
		if (null == userInfo) {
			return -1;
		}
		/**Convert the user info to protocol content*/
		String name = PushUtil.encodingByDeviceLang(userInfo.getName(), lang);
		userInfo.setName(name);
		StringBuilder sb = new StringBuilder();
		sb.append(DevCmdUtil.getUpdateUserContent(userInfo));
		sb.append("\n"); //new line
		/**Gets user photo*/
		if (null != userInfo.getPhotoIdName() 
				&& !userInfo.getPhotoIdName().isEmpty()) {
			sb.append(DevCmdUtil.getUpdateUserPicContent(userInfo));
			sb.append("\n");
		}
		
		/**Gets user template(including face, FP and Palm)*/
		List<PersonBioTemplate> list = ManagerFactory.getBioTemplateManager()
										.getPersonTemplatesByUserIdAndSn(
												userInfo.getUserId(), 
												deviceSn);
		if (null != list) {
			for (PersonBioTemplate template : list) {
				if (Constants.BIO_TYPE_FP == template.getBioType()) {
					sb.append(DevCmdUtil.getUpdateFpContent(template));
					sb.append("\n");
				} else if (Constants.BIO_TYPE_FACE == template.getBioType()) {
					 sb.append(DevCmdUtil.getUpdateFaceContent(template));
					sb.append("\n");
				}
				/*else if (Constants.BIO_TYPE_PALM == template.getBioType()) {
					sb.append(DevCmdUtil.getUpdatePlamContent(template));
					sb.append("\n");
				}*/
			}
		}
		try {
			response.getWriter().write(sb.toString());// Send to device
			logger.info(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Processes the data by table structure.
	 * 
	 * @param table	data table<br>
	 * <li>OPERLOG including operation logs:OPLOGï¼Œuser info: USERï¼Œfinger print data: FPï¼Œuser photo: USERPICï¼Œface template: FACE
	 * <li>ATTLOG attendance record
	 * @param data	post data
	 * @param deviceSn	Device SN
	 * @return
	 */
	private int processDatas(String table, String data1, String deviceSn) {
		//System.out.println("Table :"+table);
		String record[] = data1.split("\n");
		for(String data : record){
			if ("OPERLOG".equals(table)) {
				if (data.startsWith("OPLOG ")) {
					try {
						logger.info("begin parse op log");
						int result = DataParseUtil.parseOPLog(data, deviceSn);
						logger.info("end parse op log");
//						return result;
					} catch (ParseDataException e) {
//						return -1;
					}
				} else if (data.startsWith("USER ")) {
					logger.info("begin parse op user");
					DataParseUtil.parseUserData(data, deviceSn);
					logger.info("end parse op user");
//					return 0;
				} else if (data.startsWith("FP ")) {
					logger.info("begin parse op fp");
					DataParseUtil.parseFingerPrint(data, deviceSn);
					logger.info("end parse op fp");
//					return 0;
					
				} else if(data.startsWith("USERPIC ")) {
					logger.info("begin parse op user pic");
					DataParseUtil.parseUserPic(data, deviceSn);
					logger.info("end parse op user pic");
//					return 0;
					
				} else if (data.startsWith("FACE ")) {
					logger.info("begin parse op face");
					DataParseUtil.parseFace(data, deviceSn);
					logger.info("end parse op face");
//					return 0;
				} else if (data.startsWith("PLAM ")) {
					logger.info("begin parse op plam");
					DataParseUtil.parseFace(data, deviceSn);
					logger.info("end parse op plam");
//					return 0;


				}else if(data.startsWith("BIOPHOTO ")){
					logger.info("begin parse biophoto");
					DataParseUtil.parseBioPhotoPic(data,deviceSn);
					logger.info("end parse biophoto");
				}
				
			} else if ("BIODATA".equals(table)) {
				logger.info("begin parse op fp");
				DataParseUtil.parseFingerPrint(data, deviceSn);
				logger.info("end parse op fp");
//				return 0;
				
			} else if ("ATTLOG".equals(table)) {
				logger.info("begin parse op attlog");
				DataParseUtil.parseAttlog(data, deviceSn);
				logger.info("end parse op attlog");
//				return 0;
			}
		}
		
		return 0;
	}
	
	/**
	 * Creates attendance photo file. saves the photo.
	 * 
	 * @param data	data
	 * @param filePath	file path of attendance photo
	 * @return
	 */
	private File createAttPhotoFile(String data, String filePath) {
		File file = null;
		try {
			String photoArr[] = data.split("\n");
			/**data fields*/
			String fileName = photoArr[0].split("=")[1];
			String sn = photoArr[1].split("=")[1];
			String size = photoArr[2].split("=")[1];
			/**creates sub-directory by device SN*/
			File fileSn = new File(filePath + "\\" + sn);
			if (!fileSn.exists() && !fileSn.isDirectory()) {
				fileSn.mkdir();
			}
			/**Gets the attendance photo path*/
			file = new File(fileSn + "\\" + fileName);
			
			/**file non-existed*/
			if (!file.exists()) {
				file.createNewFile();
				
				/**creates attendance photo, save the logs*/
				AttPhoto attPhoto = new AttPhoto();
				attPhoto.setDeviceSn(sn);
				attPhoto.setFileName(fileName);
				attPhoto.setSize(Integer.valueOf(size));
				attPhoto.setFilePath(file.toString().replace("\\", "/"));
				List<AttPhoto> list = new ArrayList<AttPhoto>();
				list.add(attPhoto);
				ManagerFactory.getAttPhotoManager().createAttPhoto(list);
			} else {
				file = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * Protocol have deprecated, use "cdata" instead
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Deprecated
	public String fdata(HttpServletRequest request, HttpServletResponse response) {
		return postCdata(request, response);
	}

	 public static String changeTimeZone(String timeZone) {
	        StringBuffer timeStr = new StringBuffer();
	        String str1 = timeZone.substring(0,1);
	        String str2 = timeZone.substring(1,3);
	        String str3 = timeZone.substring(3);
	        if("-".equals(str1))
	        {
	            timeStr.append(str1);
	        }
	        if("00".equals(str3))//æ•´æ—¶åŒº
	        {
	            timeStr.append(Integer.valueOf(str2));
	        }
	        else//å�Šæ—¶åŒº
	        {
	        	timeStr.append(Integer.valueOf(str2)*60+Integer.valueOf(str3));
	        }
	        return  timeStr.toString();
	   }
	
	/**
	 * Gets device info by the information in the initialization
	 * @param devInfo
	 * @return
	 */
	private String getDeviceOptions(DeviceInfo devInfo) {
		StringBuilder sb = new StringBuilder();

		sb.append("GET OPTION FROM: ").append(devInfo.getDeviceSn()).append(
				"\n");
		String ver = ConfigUtil.getInstance().getValue(Constants.OPTION_VER);
		/**processes Stamp and TransFlag by the old style*/
		if (null != ver && "1".equals(ver)) {
			sb.append("Stamp=").append(devInfo.getLogStamp()).append("\n");
			sb.append("OpStamp=").append(devInfo.getOpLogStamp()).append("\n");
			sb.append("PhotoStamp=").append(devInfo.getPhotoStamp()).append("\n");	
			sb.append("BioDataStamp=").append(devInfo.getBioDataStamp()).append("\n");	
			sb.append("IdCardStamp=").append(devInfo.getIdCardStamp()).append("\n");	
			sb.append("ErrorLogStamp=").append(devInfo.getErrorLogStamp()).append("\n");	
			sb.append("TransFlag=TransData AttLog\tOpLog\tAttPhoto\tEnrollUser\tChgUser\tEnrollFP\tChgFP\tFPImag\tFACE\tUserPic\n");
		} else {
			/**Processes Stamp and TransFlag */
			int verComp = -1;
			try {
				verComp = PushUtil.compareVersion(devInfo.getPushVersion(), "2.0.0");
			} catch (Exception e) {
				e.printStackTrace();
			}
			/**if the push is high then 2.0.0, it will do like {table}Stamp. otherwise, old style*/
			if (verComp >= 0) {
				sb.append("ATTLOGStamp=").append(devInfo.getLogStamp()).append("\n");
				sb.append("OPERLOGStamp=").append(devInfo.getOpLogStamp()).append("\n");
				sb.append("ATTPHOTOStamp=").append(devInfo.getPhotoStamp()).append("\n");
				sb.append("BIODATAStamp=").append(devInfo.getBioDataStamp()).append("\n");
				sb.append("IDCARDStamp=").append(devInfo.getIdCardStamp()).append("\n");
				sb.append("ERRORLOGStamp=").append(devInfo.getErrorLogStamp()).append("\n");
				sb.append("TransFlag=TransData AttLog\tOpLog\tAttPhoto\tEnrollUser\tChgUser\tEnrollFP\tChgFP\tFPImag\tFACE\tUserPic\tBioPhoto\n");
			} else {
				sb.append("Stamp=").append(devInfo.getLogStamp()).append("\n");
				sb.append("OpStamp=").append(devInfo.getOpLogStamp()).append("\n");
				sb.append("PhotoStamp=").append(devInfo.getPhotoStamp()).append("\n");
				sb.append("BioDataStamp=").append(devInfo.getBioDataStamp()).append("\n");
				sb.append("IdCardStamp=").append(devInfo.getIdCardStamp()).append("\n");
				sb.append("ErrorLogStamp=").append(devInfo.getErrorLogStamp()).append("\n");
				sb.append("TransFlag=111111111111\n");
			}
		}
		/**other information*/
		sb.append("ErrorDelay=60\n");
		sb.append("Delay=30\n");
		sb.append("transTimes=").append(devInfo.getTransTimes()).append("\n");
		sb.append("TransInterval=").append(devInfo.getTransInterval()).append("\n");
		sb.append("Realtime=1\n");
		sb.append("Encrypt=0\n");
		sb.append("ServerVer=2.2.14\n");
		String timeZone = devInfo.getTimeZone();
		if(!timeZone.equals("")) {
			timeZone = changeTimeZone(devInfo.getTimeZone());
		}
		sb.append("TimeZone=").append(timeZone).append("\n");
		//sb.append("TimeZone=330\n");

		return sb.toString();
	}
}
