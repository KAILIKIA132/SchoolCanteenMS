package com.zk.pushsdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zk.dao.impl.DeviceInfoDao;
import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.DeviceAttrs;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.po.DeviceLog;
import com.zk.pushsdk.util.Constants.DEV_FUNS;
import com.zk.util.ConfigUtil;
/**
 * Push utility
 * @author seiya
 *
 */
public class PushUtil {
	
	/**Device cache*/
	public static Map<String, DeviceInfo> devMaps = new HashMap<String, DeviceInfo>();
	/**language resource*/
//	public static ResourceBundle resource = ResourceBundle.getBundle("PushDemoResource");
	/**date format*/
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a ZZZ");
	/**real time event data. please be noted. these data will not be saved into any database*/
	public static List<DeviceLog> monitorList = new ArrayList<DeviceLog>();
	private static int monitorSize = 50;
	/**
	 * md5
	 * 
	 * @param 
	 * @return String md5s
	 */
	public final static String MD5(String s) {
		try {
			byte[] Input = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(Input);
			byte[] md = mdInst.digest();
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = (md[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return new String(hexValue.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Data is read in the data stream
	 * 
	 * @param request
	 * @return String data
	 * @throws IOException
	 */
	public String getStreamData(HttpServletRequest request) throws IOException {
		StringBuilder bufferData = new StringBuilder();
		BufferedReader br = null;
		try {
			request.setCharacterEncoding("UTF-8");
			br = new BufferedReader(new InputStreamReader(request
					.getInputStream(), "UTF-8"));
			String inline = "";
			while ((inline = br.readLine()) != null) {
				bufferData.append(inline + "\r\n");
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return bufferData.toString().trim();
	}
	
	/** 
	 * Compares the version. 
	 * @param version1 
	 * @param version2 
	 * @return version1>version2 positive. version1<version2 negative, equal 0.
	 */ 
	public static int compareVersion(String version1, String version2) throws Exception {  
	    if (version1 == null || version2 == null) {  
	        throw new Exception("compareVersion error:illegal params.");  
	    }  
	    String[] versionArray1 = version1.split("\\.");  
	    String[] versionArray2 = version2.split("\\.");  
	    int idx = 0;  
	    int minLength = Math.min(versionArray1.length, versionArray2.length);  
	    int diff = 0;  
	    while (idx < minLength  
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0  
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {  
	        ++idx;  
	    }  
	  //if it is not 0, return the value. otherwise, compare the length.  
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;  
	    return diff;  
	}
	
	/**
	 * the flag which can indicate device features. format: 101，each character indicate one feature. 0 non-support. 1 support.
	 * @param devFuns
	 * @param devFun
	 * @return
	 */
	public static boolean isDevFun(String devFuns, DEV_FUNS devFun) {
		boolean isSupport = false;
		if (null == devFuns || devFuns.isEmpty()) {
			return isSupport;
		}
		switch (devFun) {
		case FP://the first mark indicate finger print feature.
			isSupport = devFuns.substring(0,1).equals("1") ? true : false;
			break;
		case FACE://the second indicate face feature.
			isSupport = devFuns.substring(1, 2).equals("1") ? true : false;;
			break;
		case USERPIC://the third indicate user photo.
			isSupport = devFuns.substring(2, 3).equals("1") ? true : false;;
			break;
		case BIOPHOTO://the fourth indicate user photo.
			if(devFuns.length() > 3) {
				isSupport = devFuns.substring(3, 4).equals("1") ? true : false;;
			}
			break;
		case BIODATA://the fourth indicate user photo.
			if(devFuns.length() > 4) {
				isSupport = devFuns.substring(4, 5).equals("1") ? true : false;;
			}
			break;
		case PLAM://the fifth indicate user palm.
			if(devFuns.length() > 5) {
				isSupport = devFuns.substring(5, 6).equals("1") ? true : false;;
			}
			break;
		default:
			break;
		}
		return isSupport;
	}
	
	/**Gets all the device list from server. store it into the cache.*/
	static{
		// Initialize device list lazily to avoid NPE during static initialization
		// Device list will be loaded when first accessed
		try {
			iniDevice();
		} catch (Exception e) {
			// Silently fail during static initialization - will be loaded on first use
			System.err.println("Warning: Could not initialize device list during static initialization: " + e.getMessage());
		}
	}
	
	private static void iniDevice(){
		try {
			List<DeviceInfo> list = ManagerFactory.getDeviceManager().getAllDeviceInfoList();
			if (list != null) {
				for (DeviceInfo deviceInfo : list) {
					List<DeviceAttrs> attrs = ManagerFactory.getAttrsManager().getDeviceAttrsBySn(deviceInfo.getDeviceSn());
					if (null != attrs) {
						deviceInfo.setAttrList(attrs);
					}
					devMaps.put(deviceInfo.getDeviceSn(), deviceInfo);
				}
			}
		} catch (Exception e) {
			// If database connection is not ready, this is OK - will be loaded later
			System.err.println("Warning: Could not load device list: " + e.getMessage());
		}
	}
	/**
	 * Update the device list cache.
	 * @param deviceInfo
	 * device info
	 * @return
	 */
	public static int updateDevMap(DeviceInfo deviceInfo) {
		if (null == deviceInfo) {
			return -1;
		}
		DeviceInfoDao obj = new DeviceInfoDao();
		obj.updateOptions(deviceInfo);
		return 0;
	}
	
	public static void clearDevMaps() {
		devMaps.clear();
	}
	
	public static int getCmdSizeBySn(String deviceSn) {
		int cmdSize = 64 * 1024;
		DeviceInfo info = devMaps.get(deviceSn);
		if (null != info) {
			List<DeviceAttrs> attrs = info.getAttrList();
			if (null != attrs) {
				for (DeviceAttrs deviceAttrs : attrs) {
					if (Constants.DEV_ATTR_CMD_SIZE.equals(deviceAttrs.getAttrName())) {
						if (null == deviceAttrs.getAttrValue() 
								|| deviceAttrs.getAttrValue().isEmpty()) {
							break;
						}
						try {
							cmdSize = Integer.valueOf(deviceAttrs.getAttrValue());
						} catch (Exception e) {
							break;
						}
						break;
					}
				}
			}
		}
		return cmdSize;
	}
	
	public static void deleteDeviceBySn(String deviceSn) {
		devMaps.remove(deviceSn);
	}
	
	/**
	 * Gets device list from cache.
	 * @return
	 */
	public static List<DeviceInfo> getDeviceList() {
		try {
			List<DeviceInfo> list = new ArrayList<DeviceInfo>();
			if (devMaps != null && !devMaps.isEmpty()) {
				for (Map.Entry<String, DeviceInfo> entry : devMaps.entrySet()) {
					if (entry != null && entry.getValue() != null) {
						list.add(entry.getValue());
					}
				}
			}
			return list;
		} catch (Exception e) {
			// Return empty list on error
			return new ArrayList<DeviceInfo>();
		}
	}
	
	public static String getDeviceOption() {
		String list = "";
		iniDevice();
		for (Map.Entry<String, DeviceInfo> entry : devMaps.entrySet()) {
			list+="<option value='"+entry.getValue().getDeviceSn()+"'>"+entry.getValue().getDeviceSn()+"</option>";
		}
		
		
		return list;
	}
	
	/**
	 * Gets the device language by the SN(serial number)
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public static String getDeviceLangBySn(String deviceSn) {
		if (null == deviceSn || deviceSn.isEmpty()) {
			return "";
		}
		DeviceInfo info = devMaps.get(deviceSn);
		if (null != info) {
			return info.getDevLanguage();
		}
		return "";
	}
	
	/**Verification type*/
	public static Map<String, String> ATT_VERIFY = new HashMap<String, String>();
	static {
		ATT_VERIFY.put("0", "FP/PW/RF");
		ATT_VERIFY.put("1", "FP");
		ATT_VERIFY.put("2", "PIN");
		ATT_VERIFY.put("3", "PW");
		ATT_VERIFY.put("4", "RF");
		ATT_VERIFY.put("5", "FP/PW");
		ATT_VERIFY.put("6", "FP/RF");
		ATT_VERIFY.put("7", "PW/RF");
		ATT_VERIFY.put("8", "PIN&FP");
		ATT_VERIFY.put("9", "FP&PW");
		ATT_VERIFY.put("10", "FP&RF");
		ATT_VERIFY.put("11", "PW&RF");
		ATT_VERIFY.put("12", "FP&PW&RF");
		ATT_VERIFY.put("13", "PIN&FP&PW");
		ATT_VERIFY.put("14", "FP&RF/PIN");
		ATT_VERIFY.put("15", "FACE");
		ATT_VERIFY.put("16", "FACE&FP");
		ATT_VERIFY.put("17", "FACE&PW");
		ATT_VERIFY.put("18", "FACE&RF");
		ATT_VERIFY.put("19", "FACE&FP&RF");
		ATT_VERIFY.put("20", "FACE&FP&PW");
		ATT_VERIFY.put("101", "SLAVE DEVICE");
	}

	/**Attendance status*/
	public static Map<String, String> ATT_STATUS = new HashMap<String, String>();
	static {
		ATT_STATUS.put ("0", "Check-In"); // work attendance
		ATT_STATUS.put ("1", "CheckOut"); // sign-off from work
		ATT_STATUS.put ("2", "BreakOut"); // out
		ATT_STATUS.put ("3", "Break-In"); // out return
		ATT_STATUS.put ("4", "OT-IN"); // overtime attendance
		ATT_STATUS.put ("5", "OT-OUT"); // overtime sign-off
		ATT_STATUS.put ("255", "255"); 
	}

	/**Real time event*/
	public static Map<Integer, String> ATT_OP_TYPE = new HashMap<Integer, String>();
	static {
		ATT_OP_TYPE.put(0, "Power ON Device");
		ATT_OP_TYPE.put(1, "Power OFF Device");
		ATT_OP_TYPE.put(2, "Fail Verification");
		ATT_OP_TYPE.put(3, "Generate an Alarm");
		ATT_OP_TYPE.put(4, "Enter Menu");
		ATT_OP_TYPE.put(5, "Modify Configuration");
		ATT_OP_TYPE.put(6, "Enroll FingerPrint");
		ATT_OP_TYPE.put(7, "Enroll Password");
		ATT_OP_TYPE.put(8, "Enroll HID Card");
		ATT_OP_TYPE.put(9, "Delete one User");
		ATT_OP_TYPE.put(10, "Delete FingerPrint");
		ATT_OP_TYPE.put(11, "Delete Password");
		ATT_OP_TYPE.put(12, "Delete RF Card");
		ATT_OP_TYPE.put(13, "Purge Data");
		ATT_OP_TYPE.put(14, "Create MF Card");
		ATT_OP_TYPE.put(15, "Enroll MF Card");
		ATT_OP_TYPE.put(16, "Register MF Card");
		ATT_OP_TYPE.put(17, "Delete MF Card");
		ATT_OP_TYPE.put(18, "Clean MF Card");
		ATT_OP_TYPE.put(19, "Copy Data to Card");
		ATT_OP_TYPE.put(20, "Copy C. D. to Device");
		ATT_OP_TYPE.put(21, "Set New Time");
		ATT_OP_TYPE.put(22, "Factory Setting");
		ATT_OP_TYPE.put(23, "Delete Att. Records");
		ATT_OP_TYPE.put(24, "Clean Admin. Privilege");
		ATT_OP_TYPE.put(25, "Modify Group Settings");
		ATT_OP_TYPE.put(26, "Modify User AC Settings");
		ATT_OP_TYPE.put(27, "Modify Time Zone");
		ATT_OP_TYPE.put(28, "Modify Unlock Settings");
		ATT_OP_TYPE.put(29, "Open Door Lock");
		ATT_OP_TYPE.put(30, "Enroll one User");
		ATT_OP_TYPE.put(31, "Modify FP Template");
		ATT_OP_TYPE.put(32, "Duress Alarm");
		ATT_OP_TYPE.put(34, "Antipassback Failed");
		ATT_OP_TYPE.put(35, "Delete User pic");
	}
	
	static {
		/**Initialize the capacity of real time monitor*/
		try {
			ConfigUtil configUtil = ConfigUtil.getInstance();
			if (configUtil != null) {
				String monitorSizeStr = configUtil.getValue(Constants.OPTION_MONIGOR_SIZE);
				if (monitorSizeStr != null && !monitorSizeStr.isEmpty()) {
					monitorSize = Integer.valueOf(monitorSizeStr);
					if (monitorSize <= 0) {
						monitorSize = 50;
					}
				} else {
					monitorSize = 50;
				}
			} else {
				monitorSize = 50;
			}
		} catch (Exception e) {
			monitorSize = 50;
		}
	}
	
	public static int getMonitorSize() {
		return monitorSize;
	}
	
	public static String encodingByDeviceLang(String src, String lang) {
		String dest = src;
		try {
			if (lang.equals(Constants.DEV_LANG_ZH_CN)) {
				dest = new String(src.getBytes(), "GB2312");
			} else {
				dest = new String(src.getBytes(), "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return dest;
	}
}