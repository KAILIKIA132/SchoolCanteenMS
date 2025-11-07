package com.zk.pushsdk.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.zk.manager.ManagerFactory;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.util.Constants;
import com.zk.pushsdk.util.PushUtil;

public class DownloadProcess {

	Logger logger = Logger.getLogger(DownloadProcess.class);

	/**
	 * Processing UPDATE INFO and checks command request.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String getrequest(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String info = request.getParameter("INFO");
		String devSn = request.getParameter("SN");
		try {
			/** return when device serial number exception */
			if (null == devSn || devSn.isEmpty()) {
				response.getWriter().write("error");
				return null;
			}
			/** Define the character encoding type by the current language */
			if (Constants.DEV_LANG_ZH_CN.equals(PushUtil.getDeviceLangBySn(devSn))) {
				response.setCharacterEncoding("GB2312");
			} else {
				response.setCharacterEncoding("UTF-8");
			}
			logger.info("get request and begin get cmd list." + request.getRemoteAddr() + ";" + request.getRequestURL()
					+ "?" + request.getQueryString());
			/** Gets the command list by device SN */
			List<DeviceCommand> list = ManagerFactory.getCommandManager().getDeviceCommandListToDevice(devSn);
			logger.info("get cmd list over and begin contact:" + list.size());
			/** save the logs of commands */
			List<DeviceCommand> tempList = new ArrayList<DeviceCommand>();
			if (list.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (DeviceCommand command : list) {
					/** Gets the command content */
					StringBuilder content = new StringBuilder();
					content.append(Constants.DEV_CMD_TITLE);
					content.append(command.getDevCmdId()).append(":");
					content.append(command.getCmdContent()).append("\n");
					/** the command should be less than setting, default 64K */
					logger.info("SN:" + devSn + "cmdSize:" + PushUtil.getCmdSizeBySn(devSn));
					// if (sb.toString().length() + content.length() <
					// PushUtil.getCmdSizeBySn(devSn) * 1024) {
					sb.append(content);
					/** Sets the transmit time */
					command.setCmdTransTime(PushUtil.dateFormat.format(new Date()));
					tempList.add(command);
					// } else {
					// break;
					// }
				}
				/** Sets the command */
				response.setCharacterEncoding("GB2312");
				response.getWriter().write(sb.toString());
				logger.info("contact cmd and send list:" + tempList.size());
				logger.info("cmd info:" + sb.toString());
				/** Update the command list */
				ManagerFactory.getCommandManager().updateDeviceCommand(tempList);
			} else {
				response.getWriter().write("OK");
			}
			/** Update device INFO */
			if (null != info) {
				logger.info("INFO:" + info);
				updateDeviceInfo(info, devSn);
			} else {
				ManagerFactory.getDeviceManager().updateDeviceState(devSn, "connecting",
						PushUtil.dateFormat.format(new Date()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Updates the device info by the information
	 * 
	 * @param info
	 *            the info which is send by device
	 * @param devSn
	 *            Device SN
	 * @return
	 */
	private int updateDeviceInfo(String info, String devSn) {
		String[] values = info.split(",");
		/** Gets the device info by SN */
		DeviceInfo deviceInfo = ManagerFactory.getDeviceManager().getDeviceInfoBySn(devSn);
		if (null == deviceInfo) {
			return -1;
		}
		// INFO:Ver 2.0.1.test-20150901, 1, 2, 4, 192.168.18.212, 10, 7, 12, 1,
		// 111, 1, 1, 1
		// 0 1 2 3 4 5 6 7 8 9 10 11 12
		deviceInfo.setFirmwareVersion(values[0]); // firmware version
		try {
			int userCount = Integer.valueOf(values[1]);// user count
			deviceInfo.setUserCount(userCount);
		} catch (Exception e) {
			return -1;
		}
		try {
			int fpCount = Integer.valueOf(values[2]);// FP count
			deviceInfo.setFpCount(fpCount);
		} catch (Exception e) {
		}
		try {
			int attCount = Integer.valueOf(values[3]);// the count of time
														// attendance logs
			deviceInfo.setTransCount(attCount);
		} catch (Exception e) {
		}
		deviceInfo.setIpAddress(values[4]);// IP Address
		deviceInfo.setFpAlgVersion(values[5]); // FP algorithm
		deviceInfo.setFaceAlgVer(values[6]);
		try {
			int regFaceCount = Integer.valueOf(values[7]);
			deviceInfo.setRegFaceCount(regFaceCount);
		} catch (Exception e) {
		}
		try {
			int faceCount = Integer.valueOf(values[8]);
			deviceInfo.setFaceCount(faceCount);
		} catch (Exception e) {
			// handle exception
		}
		deviceInfo.setDevFuns(values[9]);// the feature which device can support

		deviceInfo.setState("Online");
		deviceInfo.setLastActivity(PushUtil.dateFormat.format(new Date()));
		ManagerFactory.getDeviceManager().updateDeviceInfo(deviceInfo);

		PushUtil.updateDevMap(deviceInfo);
		return 0;
	}

	/**
	 * Un-support
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Deprecated
	public String querydata(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	/**
	 * Processing device command
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String devicecmd(HttpServletRequest request, HttpServletResponse response) {
		response.addHeader("Content-Type", "text/plain");
		String deviceSn = request.getParameter("SN");
		logger.info("get request and begin process cmd return." + request.getRemoteAddr() + ";"
				+ request.getRequestURL() + "?" + request.getQueryString());
		logger.info("content-length:" + request.getContentLength());
		try {
			int iReadLength = 0;
			byte[] buffer = new byte[1024];
			StringBuffer bufferData = new StringBuffer();
			InputStream postData;
			postData = request.getInputStream();
			/** Gets the path of command GetFile */
			String getFilePathStr = request.getSession().getServletContext().getRealPath("\\getfile");
			FileOutputStream fileOS = null;
			while ((iReadLength = postData.read(buffer)) != -1) {
				String inString = new String(buffer, 0, iReadLength);
				/** process the return of command GetFile */
				if (inString.contains("CMD=GetFile")) {
					File path = processGetFileReturn(inString, getFilePathStr);
					if (null == path) {
						response.getWriter().write("OK");
						return null;
					}
					String filePath = path.toString();
					fileOS = new FileOutputStream(filePath);
					inString = inString.substring(0, inString.indexOf("Content=") + "Content=".length());
					fileOS.write(buffer, inString.length(), iReadLength - inString.length());
				} else {
					if (null != fileOS) {
						fileOS.write(buffer, 0, iReadLength);
					} else {
						/** Process the return of normal command */
						if (Constants.DEV_LANG_ZH_CN.equals(PushUtil.getDeviceLangBySn(deviceSn))) {
							bufferData.append(new String(buffer, 0, iReadLength, "GB2312"));
						} else {
							bufferData.append(new String(buffer, 0, iReadLength, "UTF-8"));
						}
					}
				}

			}
			String data = bufferData.toString();
			logger.info("DEV CMD RETURN:\n" + data);
			logger.info("update cmd return begin");
			int ret = -1;
			if (data.contains("CMD=Shell")) {
				ret = processShellReturn(data, response);
			} else {
			}
			ret = updateDeviceCommand(data, response);
			logger.info("update cmd return end");
			if (0 == ret) {
				response.getWriter().write("OK");
			} else {
				response.getWriter().write("Error");
			}
			ManagerFactory.getDeviceManager().updateDeviceState(deviceSn, "connecting",
					PushUtil.dateFormat.format(new Date()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Processing the return of command GetFile, create the file.
	 * 
	 * @param data
	 * @param filePath
	 * @return
	 */
	private File processGetFileReturn(String data, String filePath) {
		if (null == data || data.isEmpty() || null == filePath || filePath.isEmpty()) {
			return null;
		}
		File file = null;
		String[] lines = data.split("\n");
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		String fileName = "";
		/***/
		for (String string : lines) {
			/** split by character & */
			if (string.contains("ID=") && string.contains("Return=") && string.contains("CMD=")) {
				String[] cmdReturns = string.split("&");
				for (String field : cmdReturns) {
					if (field.startsWith("ID")) {
						try {
							devCmdId = Integer.valueOf(field.substring(field.indexOf("ID=") + "ID=".length()));
						} catch (Exception e) {
							return null;
						}
					} else if (field.startsWith("Return")) {
						cmdReturn = field.substring(field.indexOf("Return=") + "Return=".length());
					} else if (field.startsWith("CMD")) {
						cmd = field.substring(field.indexOf("CMD=") + "CMD=".length());
					}
				}
			} else {// try with character \n
				if (string.startsWith("ID")) {
					try {
						devCmdId = Integer.valueOf(string.substring(string.indexOf("ID=") + "ID=".length()));
					} catch (Exception e) {
						return null;
					}
				} else if (string.startsWith("Return")) {
					cmdReturn = string.substring(string.indexOf("Return=") + "Return=".length());
				} else if (string.startsWith("CMD")) {
					cmd = string.substring(string.indexOf("CMD=") + "CMD=".length());
				} else if (string.startsWith("SN")) {
					deviceSn = string.substring(string.indexOf("SN=") + "SN=".length());
				} else if (string.startsWith("FILENAME")) {
					fileName = string.substring(string.indexOf("FILENAME=") + "FILENAME=".length());
				}
			}
		}

		/** update the device command info */
		DeviceCommand command = ManagerFactory.getCommandManager().getDeviceCommandById(devCmdId);
		if (null != command) {
			command.setCmdOverTime(PushUtil.dateFormat.format(new Date()));
			command.setCmdReturn(cmdReturn);
			command.setCmdReturnInfo(data);
			ManagerFactory.getCommandManager().updateDeviceCommand(command);
		}

		/** create the file */
		try {
			File fileSn = new File(filePath + "\\" + deviceSn);
			if (!fileSn.exists() && !fileSn.isDirectory()) {
				fileSn.mkdir();
			}
			if (fileName.contains("/")) {
				String[] dirs = fileName.split("/");
				StringBuilder temp = new StringBuilder();
				for (int i = 0; i < dirs.length - 1; i++) {
					if (dirs[i].isEmpty()) {
						continue;
					}
					temp.append(dirs[i]).append("\\");
				}

				String tempFile = fileSn + "\\" + temp.toString();
				fileSn = new File(tempFile);
				FileUtils.forceMkdir(fileSn);
				fileName = fileName.substring(fileName.lastIndexOf("/"));
			}
			file = new File(fileSn + "\\" + fileName);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	/**
	 * Processing the command of shell
	 * 
	 * @param data
	 * @param response
	 * @return
	 */
	private int processShellReturn(String data, HttpServletResponse response) {
		if (null == data || data.isEmpty()) {
			return -1;
		}
		String[] lines = data.split("\n");
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		/** Gets the field values of command shell */
		for (String string : lines) {
			if (string.startsWith("ID")) {
				try {
					devCmdId = Integer.valueOf(string.substring(string.indexOf("ID=") + "ID=".length()));
				} catch (Exception e) {
					return -1;
				}
			} else if (string.startsWith("Return")) {
				cmdReturn = string.substring(string.indexOf("Return=") + "Return=".length());
			} else if (string.startsWith("CMD")) {
				cmd = string.substring(string.indexOf("CMD=") + "CMD=".length());
			} else if (string.startsWith("SN")) {
				deviceSn = string.substring(string.indexOf("SN=") + "SN=".length());
			}
		}

		DeviceCommand command = ManagerFactory.getCommandManager().getDeviceCommandById(devCmdId);
		if (null != command) {
			command.setCmdOverTime(PushUtil.dateFormat.format(new Date()));
			command.setCmdReturn(cmdReturn);
			command.setCmdReturnInfo(data);
			ManagerFactory.getCommandManager().updateDeviceCommand(command);
		}
		return 0;
	}

	/**
	 * process the return value
	 * 
	 * @param data
	 * @param response
	 * @return
	 */
	private int updateDeviceCommand(String data, HttpServletResponse response) {
		if (null == data || "".equals(data)) {
			return -1;
		}
		int devCmdId = 0;
		String cmdReturn = "";
		String cmd = "";
		String deviceSn = "";
		String[] lines = data.split("\n");
		List<DeviceCommand> returnList = new ArrayList<DeviceCommand>();
		/** processing the return string */
		for (String string : lines) {
			/** split by "&" */
			if (string.contains("ID=") && string.contains("Return=") && string.contains("CMD=")) {
				String[] cmdReturns = string.split("&");
				for (String field : cmdReturns) {
					if (field.startsWith("ID")) {
						try {
							devCmdId = Integer.valueOf(field.substring(field.indexOf("ID=") + "ID=".length()));
						} catch (Exception e) {
							return -1;
						}
					} else if (field.startsWith("Return")) {
						cmdReturn = field.substring(field.indexOf("Return=") + "Return=".length());
					} else if (field.startsWith("CMD")) {
						cmd = field.substring(field.indexOf("CMD=") + "CMD=".length());
					}
				}
				DeviceCommand command = ManagerFactory.getCommandManager().getDeviceCommandById(devCmdId);

				if (null != command) {
					deviceSn = command.getDeviceSn();
					command.setCmdOverTime(PushUtil.dateFormat.format(new Date()));
					command.setCmdReturn(cmdReturn);
					command.setCmdReturnInfo(data);

					returnList.add(command);
				}
			} else if (Constants.DEV_CMD_INFO.equals(cmd)) {
				/** command INFO */
				updateDeviceInfo(lines, deviceSn);
				break;
			}
		}
		ManagerFactory.getCommandManager().updateDeviceCommand(returnList);
		return 0;
	}

	/**
	 * Update the device info from the return of command INFO
	 * 
	 * @param infoDatas
	 * @param deviceSn
	 * @return
	 */
	public int updateDeviceInfo(String[] infoDatas, String deviceSn) {
		DeviceInfo deviceInfo = ManagerFactory.getDeviceManager().getDeviceInfoBySn(deviceSn);
		for (String string : infoDatas) {

			if (string.startsWith("UserCount")) {
				try {
					int userCount = Integer.valueOf(string.substring(string.indexOf("UserCount=")
							+ "UserCount=".length()));
					deviceInfo.setUserCount(userCount);
				} catch (Exception e) {

				}
			} else if (string.startsWith("FPCount")) {
				try {
					int fpCount = Integer.valueOf(string.substring(string.indexOf("FPCount=") + "FPCount=".length()));
					deviceInfo.setFpCount(fpCount);
				} catch (Exception e) {

				}
			} else if (string.startsWith("FWVersion")) {
				try {
					String fwVersion = string.substring(string.indexOf("FWVersion=") + "FWVersion=".length());
					deviceInfo.setFirmwareVersion(fwVersion);
				} catch (Exception e) {

				}
				/*
				 * } else if (string.startsWith("PvCount")) { try { int palm =
				 * Integer.valueOf(string.substring(string.indexOf("PvCount=") +
				 * "PvCount=".length())); deviceInfo.setPalm(palm); } catch
				 * (Exception e) {
				 * 
				 * }
				 */
			} else if (string.startsWith("FaceCount")) {
				try {
					int faceCount = Integer.valueOf(string.substring(string.indexOf("FaceCount=")
							+ "FaceCount=".length()));
					deviceInfo.setFaceCount(faceCount);
				} catch (Exception e) {
				}
			}
		}

		deviceInfo.setLastActivity(PushUtil.dateFormat.format(new Date()));

		ManagerFactory.getDeviceManager().updateDeviceInfo(deviceInfo);
		return 0;
	}

	/**
	 * Processing the command PutFile
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String devDownloadFile(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info("get request for download file:" + request.getRequestURL());
			// **Gets the Root path*//*
			String prefix = request.getSession().getServletContext().getRealPath("/");
			String reqUrl = request.getRequestURL().toString();
			// **Gets the path*//*
			String file1 = reqUrl.substring(reqUrl.indexOf("/iclock") + "/iclock/".length());
			String fileName = prefix + file1;
			if (request.getParameter("path") != null) {
				fileName = prefix + request.getParameter("path");
			}
			fileName = fileName.replace("\\", "/");
			logger.info(fileName);
			// **Gets the file name*//*
			String temp = fileName.substring(fileName.lastIndexOf("/") + 1);
			// **Set the content and file name of response*//*
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + temp);

			ServletOutputStream out;
			File file = new File(fileName);
			// **Get the input stream of file*//*
			FileInputStream inputStream = new FileInputStream(file);
			// **Gets the output stream of Servlet*//*
			out = response.getOutputStream();
			int b = 0;
			byte[] buffer = new byte[512];
			int size = 0;
			// **puts it into the file*//*
			while (-1 != (b = inputStream.read(buffer))) {
				out.write(buffer, 0, b);
				size += b;
			}
			inputStream.close();
			out.close();
			out.flush();
			logger.info("download file over, size:" + size);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

/*
 * public String devDownloadFile(HttpServletRequest request, HttpServletResponse
 * response) { FileInputStream adFis = null; OutputStream out = null; try {
 * String method = request.getMethod(); String requestUrl =
 * request.getRequestURL().toString(); if ("GET".equals(method) &&
 * requestUrl.contains("iclock/downloadFile")) { String path =
 * request.getParameter("path"); String path2 =
 * request.getSession().getServletContext().getRealPath("") + path; File adFile
 * = new File(path2); if (adFile.exists()) { adFis = new
 * FileInputStream(adFile); String filename =
 * URLEncoder.encode(adFile.getName(), "utf-8"); byte[] adByteArray = new
 * byte[adFis.available()]; adFis.read(adByteArray);
 * response.setContentType("application/vnd.android.package-archive; charset=utf-8"
 * ); // response.setContentType("application/octet-stream");
 * response.setCharacterEncoding("utf-8");
 * response.setHeader("Content-Disposition", "attachment; filename=" + filename
 * + ""); out = response.getOutputStream(); out.write(adByteArray); } } } catch
 * (Exception e) { e.printStackTrace(); } finally { if (out != null) { try {
 * out.flush(); out.close(); out = null; } catch (IOException e) {
 * e.printStackTrace(); }
 * 
 * } if (adFis != null) { try { adFis.close(); adFis = null; } catch
 * (IOException e) { e.printStackTrace(); } } } return null; } }
 */
