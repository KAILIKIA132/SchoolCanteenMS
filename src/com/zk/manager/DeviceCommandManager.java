package com.zk.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AdvDao;
import com.zk.dao.impl.DeviceCommandDao;
import com.zk.dao.impl.DeviceInfoDao;
import com.zk.dao.impl.MeetInfoDao;
import com.zk.dao.impl.MessageDao;
import com.zk.dao.impl.PersonBioTemplateDao;
import com.zk.dao.impl.UserInfoDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.Adv;
import com.zk.pushsdk.po.DeviceCommand;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.po.MeetInfo;
import com.zk.pushsdk.po.Message;
import com.zk.pushsdk.po.PersonBioTemplate;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.Constants;
import com.zk.pushsdk.util.Constants.DEV_FUNS;
import com.zk.pushsdk.util.DevCmdUtil;
import com.zk.pushsdk.util.PushUtil;

/**
 * Device command management class, can be used for database operation
 * @author seiya
 *
 */
public class DeviceCommandManager {
	private static Logger logger = Logger.getLogger(DeviceCommandManager.class);
	
	/**
	 * Get the total number of device commands according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param command
	 * command name
	 * @return
	 * total number of device commands
	 */
	public int getDeviceCommandCount(String deviceSn, String command) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the device SN for condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("'");
			}
			/**combine the device command for condition, with Indistinct Enquiry**/
			if (null != command && !command.isEmpty()) {
				sb.append(" and cmd_content like '%").append(command).append("%' ");
			}
			return commandDao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the INFO command for specific device
	 * @param deviceSn
	 * device serialnumber
	 */
	public void createINFOCommand(String deviceSn) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		try {
			cmdDao.add(DevCmdUtil.getINFOCommand(deviceSn));
			
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
		} finally {
			cmdDao.close();
		}
	}
	
	/**
	 * Create the delete all datas in device command for specific device
	 * @param deviceSn
	 * device serialnumber
	 * @return
	 */
	public int createClearAllDataCommand(String deviceSn) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		try {
			cmdDao.add(DevCmdUtil.getClearAllDataCommand(deviceSn));
			
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
		} finally {
			cmdDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the delete Attlog data command for specific device
	 * @param sns
	 * device serialnumber array
	 * @return
	 */
	public int createClearAttLogCommand(String[] sns) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				cmdDao.add(DevCmdUtil.getClearAttLogCommand(deviceSn));
			}
			
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
		} finally {
			cmdDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the delete AttPhoto command for specific device
	 * @param sns
	 * device serialnumber array
	 * @return
	 */
	public int createClearPhotoCommand(String[] sns) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				cmdDao.add(DevCmdUtil.getClearPhotoCommand(deviceSn));
			}
			
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
		} finally {
			cmdDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the reboot command for specific device
	 * @param sns
	 * device serialnumber array
	 * @return
	 */
	public int createRebootCommand(String[] sns) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				cmdDao.add(DevCmdUtil.getRebootCommand(deviceSn));
			}
			
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
		} finally {
			cmdDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the check device update command for specific device
	 * @param deviceSn
	 * device serialnumber
	 * @param isClearStamp
	 * whether reset the device time stamp in server
	 * @return
	 */
	public int createCheckCommand(String deviceSn, boolean isClearStamp) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		DeviceInfoDao infoDao = new DeviceInfoDao();
		try {
			/**clean the device time stamp**/
			if (isClearStamp) {
				DeviceInfo info = infoDao.fatchDeviceInfoBySn(deviceSn);
				if (null == info) {
					return -1;
				}
				
				info.setLogStamp("0");
				info.setOpLogStamp("0");
				info.setPhotoStamp("0");
				infoDao.update(info);
			}
			
			cmdDao.add(DevCmdUtil.getCheckCommand(deviceSn));
			
			infoDao.commit();	
			cmdDao.commit();
		} catch (Exception e) {
			cmdDao.rollback();
			infoDao.rollback();
		} finally {
			cmdDao.close();
			infoDao.close();
		}
		return 0;
	}
	
	/**
	 * Get the device command list according to condition
	 * @param deviceSn
	 * device serialnumber
	 * @param command
	 * command name
	 * @param startRec
	 * start record
	 * @param pageSize
	 * size of getting list
	 * @return
	 */
	public List<DeviceCommand> getDeviceCommandList(String deviceSn, 
			String command, int startRec, int pageSize) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		List<DeviceCommand> cmdList = new ArrayList<DeviceCommand>();
		
		StringBuilder sb = new StringBuilder();
		/**combine the device SN for condition**/
		if (null != deviceSn && !deviceSn.isEmpty()) {
			sb.append(" and device_sn='").append(deviceSn).append("' ");
		}
		/**combine the command name for condition, with Indistinct Enquiry**/
		if (null != command && !command.isEmpty()) {
			sb.append(" and cmd_content like '%").append(command).append("%' ");
		}
		try {
			cmdList = cmdDao.fatchListForPage(sb.toString(), startRec, pageSize);
		} catch (Exception e) {
			
		} finally {
			cmdDao.close();
		}
		
		return cmdList;
	}
	
	/**
	 * Get the device command list which is used for sending to device
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @return
	 */
	public List<DeviceCommand> getDeviceCommandListToDevice(String deviceSn) {
		DeviceCommandDao cmdDao = new DeviceCommandDao();
		List<DeviceCommand> cmdList = new ArrayList<DeviceCommand>();
		
		StringBuilder sb = new StringBuilder();
		/**combine the device SN for condition**/
		if (null != deviceSn && !deviceSn.isEmpty()) {
			sb.append(" and device_sn='").append(deviceSn).append("' ");
		}
		/**this field is used for determination by checking if it is NULL, after the command has been executed */
		sb.append(" and cmd_return='' ");
		try {
			cmdList = cmdDao.fatchList(sb.toString());
		} catch (Exception e) {
			
		} finally {
			cmdDao.close();
		}
		
		return cmdList;
	}
	
	/**
	 * Get the device command object according to ID
	 * 
	 * @param devCmdId
	 * device command ID
	 * @return
	 * device command object<code>DeviceCommand</code>
	 */
	public DeviceCommand getDeviceCommandById(int devCmdId) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			DeviceCommand command = commandDao.fatch(devCmdId);
			return command;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			commandDao.close();
		}
		return null;
	}
	
	/**
	 * Update device command information
	 * @param command
	 * device command object
	 * @return
	 */
	public int updateDeviceCommand(DeviceCommand command) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			commandDao.update(command);
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		
		return 0;
	}
	
	/**
	 * update device command information
	 * @param list
	 * device command list
	 * @return
	 */
	public int updateDeviceCommand(List<DeviceCommand> list) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (DeviceCommand command : list) {
				commandDao.update(command);
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Delete the device command which has been executed 
	 *  
	 * @return
	 */
	public int deleteCommandReturn() {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			String cond = " and (CMD_RETURN<>null or CMD_RETURN<>'') ";
			commandDao.delete(cond);
			commandDao.commit();
		} catch (Exception e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create the update user data command for specific device, including user basic information, fingerprint, face, user photo
	 * 
	 * @param srcSn
	 * source device serialnumber
	 * @param destSn
	 * destination device serialnumber
	 * @return
	 * 
	 */
	public int createUpdateUserInfosCommandBySn(String srcSn, String destSn) {
		if (null == srcSn || srcSn.isEmpty() 
				|| null == destSn || destSn.isEmpty()) {
			return -1;
		}
		UserInfoDao userInfoDao = new UserInfoDao();
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		List<UserInfo> userList = null;
		List<PersonBioTemplate> bioTemplates = null;
		DeviceInfo deviceInfo = null;
		boolean isSupportFP = false;
		boolean isSupportFace = false;
		boolean isSupportUserPic = false;
		boolean isSupportBioPhoto = false;
		//boolean isSupportPalm=false;
		logger.info("begin make cond");
		/**get the device information from buffer*/
		if (PushUtil.devMaps.containsKey(destSn)) {
			deviceInfo = PushUtil.devMaps.get(destSn);
			if (null == deviceInfo) {
				return -1;
			}
			/**see what functions the device can support*/
			isSupportFP = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FP);
			isSupportFace = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FACE);
			//isSupportPalm = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.PLAM);
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.USERPIC);
			isSupportBioPhoto = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.BIOPHOTO);
		}
		logger.info("end make cond and get user list");
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition, get the user basic information list from source device database*/
			sb.append(" and device_sn='").append(srcSn).append("' ");
			userList = userInfoDao.fatchList(sb.toString());
			logger.info("end get user list and get tmp list");
			if (null == userList) {
				return -1;
			}
			
			/**get the biometrics template from source device database*/
			if (isSupportFP || isSupportFace) {
				bioTemplates = templateDao.fatchList(sb.toString());
			}
			
			logger.info("end get tmp list and make user cmd");
			for (UserInfo userInfo : userList) {
				/**create update user information command*/
				 commandDao.add(DevCmdUtil.getUpdateUserCommand(userInfo, destSn));
				/**create update user photo command*/
				if (isSupportUserPic 
						&& null != userInfo.getPhotoIdName() 
						&& !userInfo.getPhotoIdName().isEmpty()) {
					commandDao.add(DevCmdUtil.getUpdateUserPicCommand(userInfo, destSn));
				}
				/**create update user biophoto command*/
				if(isSupportBioPhoto 
						&& null != userInfo.getPhotoIdName() 
						&& !userInfo.getPhotoIdName().isEmpty()) {
					commandDao.add(DevCmdUtil.getUpdateBioPhotoCommand(userInfo, destSn));
				}
			}
			logger.info("end make user cmd and make bio tmp cmd");
			
			if(bioTemplates != null) { // to resolve the NullPointer exception
				for (PersonBioTemplate personTemplate : bioTemplates) {
					if (isSupportFP && Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
						/**create update user fingerprint template command*/
						commandDao.add(DevCmdUtil.getUpdateFPCommand(personTemplate, destSn));
					} else if (isSupportFace && Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
						/**create update user face template command*/
						commandDao.add(DevCmdUtil.getUpdateFaceCommand(personTemplate, destSn));
					}
				}
			}
			
			logger.info("end make bio tmp cmd and commit");
			commandDao.commit();
			logger.info("end commit");
		} catch (Exception e) {
			e.printStackTrace();
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	
	/**
	 * Create update device user data command according to user ID
	 * <li>update fingerprint template command will be send to device if fingerprint function is support in device
	 * <li>update user photo command will be send to device if user photo function is support in device
	 * <li>update face template command will be send to device if face function is support in device
	 * @param userIds
	 * @param deviceSn
	 * @return
	 */
	public int createUpdateUserInfosCommandByIds(String[] userIds, String deviceSn) {
		UserInfoDao userInfoDao = new UserInfoDao();
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		List<UserInfo> userInfos = null;
		List<PersonBioTemplate> bioTemplates = null;
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFP = false;
		boolean isSupportFace = false;
		boolean isSupportUserPic = false;
		boolean isSupportBioPhoto = false;
		boolean isSupportBioData = false;
		/**get the target device information from buffer*/
		//System.out.println(PushUtil.devMaps.containsKey(deviceSn));
		if (null != deviceSn && !deviceSn.isEmpty()) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/**see what function the device is support*/
			isSupportFP = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FP);
			isSupportFace = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FACE);
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.USERPIC);
			isSupportBioPhoto = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.BIOPHOTO);
			isSupportBioData = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.BIODATA);
			//System.out.println("isSupportFace"+deviceInfo.getDevFuns());
		}
		//System.out.println("isSupportFace "+isSupportFace);
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**get the user basic information by condition*/
			userInfos = userInfoDao.fatchList(sb.toString());

			if (null == userInfos) {
				return -1;
			}

			/**get the biometrics template list from database*/
			if (!haveDestSn || (haveDestSn && (isSupportFP || isSupportFace))) {
				bioTemplates = bioTemplateDao.fatchList(sb.toString());
				//System.out.println(isSupportFace);
			}

			for (UserInfo userInfo : userInfos) {
				/**create update user information command*/
				commandDao.add(DevCmdUtil.getUpdateUserCommand(userInfo, deviceSn));
				if (haveDestSn) {  //specify the target device
					/**create update user photo command*/
					if (isSupportUserPic && null != userInfo.getPhotoIdName()
							&& !userInfo.getPhotoIdName().isEmpty()) {
						commandDao.add(DevCmdUtil.getUpdateUserPicCommand(userInfo, deviceSn));
					}
					/**create update user bio photo command*/
					if ( null != userInfo.getPhotoIdName()
							&& !userInfo.getPhotoIdName().isEmpty()) {
						commandDao.add(DevCmdUtil.getUpdateBioPhotoCommand(userInfo, deviceSn));
					}
				} else {
					/**if target device is not specify, need to check if user photo function is support in this device where the user belonging*/
					String tempSn = userInfo.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null != tempDev
							&& PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.USERPIC)
							&& null != userInfo.getPhotoIdName()
							&& !"".equals(userInfo.getPhotoIdName())) {
						/**create update user photo command*/
						commandDao.add(DevCmdUtil.getUpdateUserPicCommand(userInfo, deviceSn));
					}
					if (null != tempDev
							&& PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.BIOPHOTO)
							&& null != userInfo.getPhotoIdName()
							&& !"".equals(userInfo.getPhotoIdName())) {
						/**create update user biophoto command*/
						commandDao.add(DevCmdUtil.getUpdateBioPhotoCommand(userInfo, deviceSn));
					}
					//abhishek
					if (null != tempDev
							&& PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.BIODATA)
							&& null != userInfo.getPhotoIdName()
							&& !"".equals(userInfo.getPhotoIdName())) {
						/**create update user biodata command*/
						commandDao.add(DevCmdUtil.getUpdateBioDataCommand(userInfo, deviceSn));
					}
				}
			}

			for (PersonBioTemplate personTemplate : bioTemplates) {
				if (haveDestSn) {
					if (isSupportFP && Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
						/**create update fingerprint template command*/
						commandDao.add(DevCmdUtil.getUpdateFPCommand(personTemplate, deviceSn));
					} 
					/*else if (isSupportFace && Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
						*//**create update face template command*//*
						commandDao.add(DevCmdUtil.getUpdateFaceCommand(personTemplate, deviceSn));
					}
					else if (isSupportFace && Constants.BIO_TYPE_VF == personTemplate.getBioType()) {
						*//**create update face template command*//*
						commandDao.add(DevCmdUtil.getUpdateFaceCommand(personTemplate, deviceSn));
					}*/
					else if (isSupportBioData ) {
						/**create update face template command*/
						commandDao.add(DevCmdUtil.getUpdateBiodataCommand(personTemplate, deviceSn));
					}
					else {
						/**create update face template command*/
						commandDao.add(DevCmdUtil.getUpdateBiodataCommand(personTemplate, deviceSn));
					}
				} else {
					/**if target device is not specify, need to check the device where user belonging*/
					String tempSn = personTemplate.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						continue;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.FP)
							&& Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
						/**create update fingerprint template command*/
						commandDao.add(DevCmdUtil.getUpdateFPCommand(personTemplate, deviceSn));
					} else if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.FACE)
							&& Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
						/**create update face template command*/
						commandDao.add(DevCmdUtil.getUpdateFaceCommand(personTemplate, deviceSn));
					}
					  else if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.BIODATA)) {
						/**create update face template command*/
						commandDao.add(DevCmdUtil.getUpdateBiodataCommand(personTemplate, deviceSn));
					}
				} 
				}
			

			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create update SMS command for specific device
	 * 
	 * @param ids
	 * SMS ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createUpdateSmsCommand(String[] ids, String deviceSn) {
		if (null == ids || ids.length <= 0 
				|| null == deviceSn || deviceSn.isEmpty()) {
			return -1;
		}
		MessageDao dao = new MessageDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**combine condition*/
			StringBuilder sb = new StringBuilder();
			sb.append(" and id in(");
			for (String id : ids) {
				sb.append(id);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**get SMS list by condition*/
			List<Message> list = dao.fatchList(sb.toString());
			if (null == list) {
				return -1;
			}
			
			for (Message message : list) {
				/**create SMS command*/
				commandDao.add(DevCmdUtil.getUpdateSmsCommand(message, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Create user SMS command for specific device
	 * 
	 * @param ids
	 * SMS ID
	 * @param userPin
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createUpdateUserSmsCommand(String[] ids, String userPin, String deviceSn) {
		if (null == ids || ids.length <= 0
				|| null == userPin || userPin.isEmpty()
				|| null == deviceSn || deviceSn.isEmpty()) {
			return -1;
		}
		MessageDao dao = new MessageDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**combine condition*/
			StringBuilder sb = new StringBuilder();
			sb.append(" and id in(");
			for (String id : ids) {
				sb.append(id);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**get SMS list*/
			List<Message> list = dao.fatchList(sb.toString());
			if (null == list) {
				return -1;
			}
			
			for (Message message : list) {
				/**create SMS command*/
				commandDao.add(DevCmdUtil.getUpdateSmsCommand(message, deviceSn));
			}
			
			for (String id : ids) {
				/**create user SMS command*/
				commandDao.add(DevCmdUtil.getUpdateUserSmsCommand(userPin, id, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Create delete SMS command for specific device
	 * 
	 * @param ids
	 * SMS ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteSmsCommand(String[] ids, String deviceSn) {
		if (null == ids || ids.length <= 0
				|| null == deviceSn || deviceSn.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			
			for (String id : ids) {
				/**create delete SMS command*/
				commandDao.add(DevCmdUtil.getDeleteSmsCommand(id, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Create shell command for specific device 
	 * 
	 * @param sns
	 * device SN
	 * @param cmd
	 * shell command
	 * @return
	 */
	public int createShellCommand(String[] sns, String cmd) {
		if (null == sns || sns.length <= 0 || null == cmd || cmd.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create shell command*/
				commandDao.add(DevCmdUtil.getShellCommand(cmd, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create device param setting command for specific device
	 * 
	 * @param sns
	 * device SN
	 * @param option
	 * device param, pattern with key=value
	 * @return
	 */
	public int createSetOptionCommand(String[] sns, String option) {
		if (null == sns || sns.length <= 0 || null == option || option.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create device param setting command*/
				commandDao.add(DevCmdUtil.getSetOptionCommand(option, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create refresh device param command for specific device
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createReloadOptionCommand(String[] sns) {
		if (null == sns || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create refresh device param command*/
				commandDao.add(DevCmdUtil.getReloadOptionCommand(deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create LOG command for specific device
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createLogDataCommand(String[] sns) {
		if (null == sns || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create LOG command*/
				commandDao.add(DevCmdUtil.getLOGCommand(deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create unlock door command for specific device
	 * 
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createUnlockCommand(String[] sns) {
		if (null == sns || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create unlock door command*/
				commandDao.add(DevCmdUtil.getUnLockCommand(deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create cancel alarm command for specific device
	 * 
	 * @param sns
	 * @return
	 */
	public int createUnalarmCommand(String[] sns) {
		if (null == sns || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create cancel alarm command*/
				commandDao.add(DevCmdUtil.getUnAlarmCommand(deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create get file command for specific device
	 * 
	 * @param sns
	 * device SN
	 * @param file
	 * the file name in device
	 * @return
	 */
	public int createGetFileCommand(String[] sns, String file) {
		if (null == sns || sns.length <= 0 || null == file || file.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String sn : sns) {
				/**create get file command*/
				commandDao.add(DevCmdUtil.getGetFileCommand(file, sn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create auto check Attlog command for specific device
	 * 
	 * @param sns
	 * device SN
	 * @param startTime
	 * start time
	 * @param endTime
	 * end time
	 * @return
	 */
	public int createVerifySumAttLogCommand(String[] sns, String startTime, String endTime) {
		if (null == sns || sns.length <= 0 
				|| null == startTime || startTime.isEmpty()
				|| null == endTime || endTime.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create auto check Attlog command*/
				commandDao.add(DevCmdUtil.getVerifySumAttLogCommand(startTime, endTime, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/**
	 * Create send file command for specific device
	 * 
	 * @param sns
	 * device SN
	 * @param srcFile
	 * server file(including folder path)
	 * @param destFile
	 * device file(including device path)
	 * @return
	 */
	public int createPutFileCommand(String[] sns, String srcFile, String destFile) {
		if (null == sns || sns.length <= 0
				|| null == srcFile || srcFile.isEmpty()
				|| null == destFile || destFile.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create send file command*/
				commandDao.add(DevCmdUtil.getPutFileCommand(srcFile, destFile, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create query AttLog command for specific device
	 * 		
	 * @param startTime
	 * start time
	 * @param endTime
	 * end time
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createQueryAttLogCommand(String startTime, String endTime, String[] sns) {
		if (null == sns || sns.length <= 0
				|| null == startTime || startTime.isEmpty()
				|| null == endTime || endTime.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create query AttLog command*/
				commandDao.add(DevCmdUtil.getQueryAttLogCommand(startTime, endTime, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create query AttPhoto command for specific device
	 * 		
	 * @param startTime
	 * start time
	 * @param endTime
	 * end time
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createQueryAttPhotoCommand(String startTime, String endTime, String[] sns) {
		if (null == sns || sns.length <= 0
				|| null == startTime || startTime.isEmpty()
				|| null == endTime || endTime.isEmpty()) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create query AttPhoto command*/
				commandDao.add(DevCmdUtil.getQueryAttPhotoCommand(startTime, endTime, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create query user information command for specific device
	 * 
	 * @param userPin
	 * user ID
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createQueryUserInfoCommand(String userPin, String[] sns) {
		if (null == userPin || null == sns 
				|| userPin.isEmpty() || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create query user information command*/
				commandDao.add(DevCmdUtil.getQueryUserInfoCommand(userPin, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create query fingerprint template command for specific device
	 * 
	 * @param userPin
	 * user ID
	 * @param fingerId
	 * finger index from 0-9
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createQueryFingerTmpCommand(String userPin, String fingerId, String[] sns) {
		if (null == userPin || null == sns 
				|| userPin.isEmpty() || sns.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String string : sns) {
				/**create query fingerprint template command*/
				commandDao.add(DevCmdUtil.getQueryFingerTempCommand(userPin, fingerId, string));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	//abhi
	public int updateUpdatequeryCommand(String sn,String cmd) {
		if (null == sn || null == cmd) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
				commandDao.add(DevCmdUtil.getQueryUpdateCommand(sn,cmd));
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	//end
	/**
	 * Create enroll fingerprint command for specific device
	 * 
	 * @param userPin
	 * user ID
	 * @param fingerId
	 * finger index from 0-9
	 * @param retryTimes
	 * retry times if faile
	 * @param isOverWrite
	 * whether overwrite flag, 0 for not overwrite if data exist, 1 for overwrite if data exist
	 * @param sns
	 * device SN
	 * @return
	 */
	public int createEnrollFpCommand(String userPin, int fingerId, 
			int retryTimes, int isOverWrite, String[] sns) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			for (String deviceSn : sns) {
				/**create enroll finger command*/
				commandDao.add(DevCmdUtil.getEnrollFpCommand(userPin, fingerId, retryTimes, 
						isOverWrite, deviceSn));
			}
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create delete user finger command for specific device
	 * 
	 * @param userIds
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteUserFpByIds(String[] userIds, String deviceSn) {
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		//UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFP = false;
		/**get target device information from buffer*/
		if (null != deviceSn && !deviceSn.isEmpty() 
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/**see what function support in device*/
			isSupportFP = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FP);
		}
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get user basic information by condition*/
			//List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
			
			/**if target device is not support finger function or user list is empty, then not send the command to device*/
			if ((haveDestSn && !isSupportFP)/* || null == userInfos || userInfos.size() <= 0*/) {
				return -1;
			}
			/**get biometrics template list from database*/
			List<PersonBioTemplate> bioTemplates = bioTemplateDao.fatchList(sb.toString());
			
			for (PersonBioTemplate personTemplate : bioTemplates) {
				if (!haveDestSn) {
					/**if target device not exist, then check the device SN from the fingerprint template*/
					String tempSn = personTemplate.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						continue;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.FP)
							&& Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
						/**create delete fingerprint command*/
						commandDao.add(DevCmdUtil.getDeleteFpCommand(personTemplate, deviceSn));
					}
				} else {
					if (Constants.BIO_TYPE_FP == personTemplate.getBioType()) {
						/**create delete fingerprint command*/
						commandDao.add(DevCmdUtil.getDeleteFpCommand(personTemplate, deviceSn));
					}
				}
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create delete user face template command for specific device
	 * 
	 * @param userIds
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteFaceByIds(String[] userIds, String deviceSn) {
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
//		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportFace = false;
		/**get target device information from buffer*/
		if (null != deviceSn && !deviceSn.isEmpty() 
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/**see what function support*/
			isSupportFace = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.FACE);
		}
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get user basic information by condition*/
//			List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
//			
//			if(null == userInfos || userInfos.size() <= 0) {
//				return -1;
//			}
			
			/**if face function is not support by target device, then not send the command*/
			if (haveDestSn && !isSupportFace) {
				return -1;
			}
			
			/**get the biometrics template list from database*/
			List<PersonBioTemplate> bioTemplates = bioTemplateDao.fatchList(sb.toString());
			
			for (PersonBioTemplate personTemplate : bioTemplates) {
				if (!haveDestSn) {
					/**if target device is not exist, then check the device SN from face template*/
					String tempSn = personTemplate.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						continue;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.FACE)
							&& Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
						/**create delete face template command*/
						commandDao.add(DevCmdUtil.getDeleteFaceCommand(personTemplate, deviceSn));
					}
				} else {
					if (Constants.BIO_TYPE_FACE == personTemplate.getBioType()) {
						/**create delete face template command*/
						commandDao.add(DevCmdUtil.getDeleteFaceCommand(personTemplate, deviceSn));
					}
				}
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create delete user command according to the user ID and device SN
	 * <li>query user information from database by user id
	 * <li>check every single user list data for Creating delete user command, device will execute these command accordingly
	 * all user information will be deleted together, so here no need to send delete finger/face/user photo and other command
	 * @param userIds
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteUserCommandByIds(String[] userIds, String deviceSn) {
		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get user basic information*/
			List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
			
			if(null == userInfos) {
				return -1;
			}
			
			for (UserInfo userInfo : userInfos) {
				/**create delete user command*/
				commandDao.add(DevCmdUtil.getDeleteUserCommand(userInfo, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create delete user photo command for specific device
	 * 
	 * @param userIds
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteUserPicByIds(String[] userIds, String deviceSn) {
		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportUserPic = false;
		/**get target device information from buffer*/
		if (null != deviceSn && !deviceSn.isEmpty() 
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/**see what function support*/
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.USERPIC);
		}
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**if device not support user photo*/
			if (haveDestSn && !isSupportUserPic) {
				return -1;
			}
			
			/**get user basic information by condition*/
			List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
			
			if (null == userInfos) {
				return -1;
			}
			for (UserInfo userInfo : userInfos) {
				if (!haveDestSn) {
					/**if target device is not exist, then check the device SN in user information*/
					String tempSn = userInfo.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						return -1;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.USERPIC)) {
						/**create delete user photo command*/
						commandDao.add(DevCmdUtil.getDeleteUserPicCommand(userInfo, deviceSn));
					}
				} else {
					/**create delete user photo command*/
					commandDao.add(DevCmdUtil.getDeleteUserPicCommand(userInfo, deviceSn));
				}
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create update user photo command
	 * 
	 * @param userIds
	 * user ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createUpdateUserPicByIds(String[] userIds, String deviceSn) {
		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportUserPic = false;
		/**get target device information from buffer*/
		if (null != deviceSn && !deviceSn.isEmpty() 
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			/**see what function support*/
			isSupportUserPic = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.USERPIC);
		}
		
		if (haveDestSn && !isSupportUserPic) {
			return -1;
		}
		
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get user basic information by condition*/
			List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
			
			if (null == userInfos) {
				return -1;
			}
			for (UserInfo userInfo : userInfos) {
				if (!haveDestSn) {
					/**if target device is not exist, then check the device SN in user information*/
					String tempSn = userInfo.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						return -1;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.USERPIC)) {
						/**create update user photo command*/
						commandDao.add(DevCmdUtil.getUpdateUserPicCommand(userInfo, tempSn));
					}
				} else {
					/**create update user photo command*/
					commandDao.add(DevCmdUtil.getUpdateUserPicCommand(userInfo, deviceSn));
				}
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Delete specific device command
	 * 
	 * @param ids
	 * device command ID
	 * @return
	 */
	public int deleteCmdById(String[] ids) {
		if (null == ids || ids.length <= 0) {
			return -1;
		}
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and dev_cmd_id in(");
			for (String id : ids) {
				sb.append(id);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			commandDao.delete(sb.toString());
			commandDao.commit();
		} catch (Exception e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return ids.length;
	}

	/**
	 * Synchronize software data to devices
	 * @param sns
	 */
	public void syncDevice(String[] sns) {
		UserInfoDao userInfoDao = new UserInfoDao();
		AdvDao advDao = new AdvDao();
		for(String sn : sns) {
			createClearUserInfoCommand(sn);
			//List<UserInfo> userInfoList = userInfoDao.fatchList(" and device_sn = " + sn);
			List<UserInfo> userInfoList = userInfoDao.fatchList(" and device_sn = '" + sn+"'");

			String[] userIds = null;
			String userIdsStr = "";
			for(UserInfo userInfo : userInfoList) {
				userIdsStr += userInfo.getUserId() + ",";
			}
			if(userIdsStr.length() > 0) {
				userIdsStr = userIdsStr.substring(0, userIdsStr.length() -1 );
				userIds = userIdsStr.split(",");
				createUpdateUserInfosCommandByIds(userIds, null);
			}
			//synchronize advertisement
			createClearAdvCommand(1,sn);
			createClearAdvCommand(2,sn);
			//List<Adv> advList = advDao.fatchList(" and device_sn = " + sn);
			List<Adv> advList = advDao.fatchList(" and device_sn = '" + sn+"'");
			String[] advIds = null;
			String advIdsStr = "";
			for(Adv adv : advList) {
				advIdsStr += adv.getAdvId() + ",";
			}
			if(advIdsStr.length() > 0) {
				advIdsStr = advIdsStr.substring(0, advIdsStr.length() -1 );
				advIds = advIdsStr.split(",");
				createUpdateAdvsCommandByIds(advIds,sn);
			}
		}
		
	}



	/**
	 * Create update device user data command according to user ID
	 * <li>update fingerprint template command will be send to device if fingerprint function is support in device
	 * <li>update user photo command will be send to device if user photo function is support in device
	 * <li>update face template command will be send to device if face function is support in device
	 * @param userIds
	 * @param deviceSn
	 * @return
	 */
	public int createUpdateMeetInfosCommandByIds(String[] meetIds, String deviceSn) {
		MeetInfoDao userInfoDao = new MeetInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		List<MeetInfo> meetInfos = null;
		DeviceInfo deviceInfo = null;
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition*/
			sb.append(" and meet_info_id in(");
			for (String meetId : meetIds) {
				sb.append(meetId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**get the meet basic information by condition*/
			meetInfos = userInfoDao.fatchList(sb.toString());

			if (null == meetInfos) {
				return -1;
			}

			for (MeetInfo meetInfo : meetInfos) {
				/**create update user information command*/
				commandDao.add(DevCmdUtil.getUpdateMeetCommand(meetInfo, deviceSn));
			}

			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
		
	}

	/**
	 * Create delete meet command according to the meet ID and device SN
	 * <li>query meet information from database by meet id
	 * <li>check every single meet list data for Creating delete user command, device will execute these command accordingly
	 * @param meetIds
	 * meet ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteMeetCommandByIds(String[] meetIds, String deviceSn) {
		MeetInfoDao meetInfoDao = new MeetInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and meet_info_id in(");
			for (String meetId : meetIds) {
				sb.append(meetId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get meet information*/
			List<MeetInfo> meetInfos = meetInfoDao.fatchList(sb.toString());
			
			if(null == meetInfos) {
				return -1;
			}
			
			for (MeetInfo meetInfo : meetInfos) {
				/**create delete user command*/
				commandDao.add(DevCmdUtil.getDeleteMeetCommand(meetInfo, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/**
	 * Create update persMeet data command according to deviceSN
	 * @param devSn
	 * @param code
	 * @param pin
	 * @return
	 */
	public int createUpdatePersMeetCommand(String devSn,String code, String pin) {
		DeviceCommandDao commandDao = new DeviceCommandDao();

		try {
			
			commandDao.add(DevCmdUtil.getUpdatePersMeetCommand(devSn,code, pin));

			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/**
	 * Create update device adv data command according to adv ID
	 * @param advIds
	 * @param deviceSn
	 * @return
	 */
	public int createUpdateAdvsCommandByIds(String[] advIds, String deviceSn) {
		AdvDao advDao = new AdvDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		List<Adv> advs = null;
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		/**get the target device information from buffer*/
		if (null != deviceSn && !deviceSn.isEmpty()
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
		}

		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition*/
			sb.append(" and adv_id in(");
			for (String advId : advIds) {
				sb.append(advId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			/**get the adv information by condition*/
			advs = advDao.fatchList(sb.toString());

			if (null == advs) {
				return -1;
			}

			for (Adv adv : advs) {
				/**create update adv information command*/
				commandDao.add(DevCmdUtil.getUpdateAdvCommand(adv, deviceSn));
				if (haveDestSn) {  //specify the target device
				} else {
				}
			}

			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
		
	}

	/**
	 * Create delete meet command according to the meet ID and device SN
	 * <li>query meet information from database by meet id
	 * <li>check every single meet list data for Creating delete user command, device will execute these command accordingly
	 * @param meetIds
	 * meet ID
	 * @param deviceSn
	 * device SN
	 * @return
	 */
	public int createDeleteAdvCommandByIds(String[] advIds, String deviceSn) {
		AdvDao advDao = new AdvDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine condition*/
			sb.append(" and adv_id in(");
			for (String advId : advIds) {
				sb.append(advId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			/**get meet information*/
			List<Adv> advs = advDao.fatchList(sb.toString());
			
			if(null == advs) {
				return -1;
			}
			
			for (Adv adv : advs) {
				/**create delete user command*/
				commandDao.add(DevCmdUtil.getDeleteAdvCommand(adv, deviceSn));
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/**
	 * Create clear meet command
	 * @return
	 */
	public int createClearMeetCommand(String deviceSn) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**create clear meet command*/
			commandDao.add(DevCmdUtil.getClearMeetCommand(deviceSn));
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/**
	 * Create clear persmeet command
	 * @return
	 */
	public int createClearPersMeetCommand(String deviceSn) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**create clear meet command*/
			commandDao.add(DevCmdUtil.getClearPersMeetCommand(deviceSn));
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	

	/**
	 * Create clear user info command
	 * @return
	 */
	public  int createClearUserInfoCommand(String deviceSn) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**create clear user info command*/
			commandDao.add(DevCmdUtil.getClearUserInfoCommand(deviceSn));
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}
	
	/**
	 * Create clear user info command
	 * @return
	 */
	private int createClearAdvCommand(int type,String deviceSn) {
		DeviceCommandDao commandDao = new DeviceCommandDao();
		try {
			/**create clear user info command*/
			commandDao.add(DevCmdUtil.getClearAdvCommand(type,deviceSn));
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
	}

	/*public int createDeletePlamByIds(String[] userIds, String deviceSn) {
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
//		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfo deviceInfo = null;
		boolean haveDestSn = false;
		boolean isSupportPlam = false;
		*//**get target devcie information from buffer*//*
		if (null != deviceSn && !deviceSn.isEmpty() 
				&& PushUtil.devMaps.containsKey(deviceSn)) {
			deviceInfo = PushUtil.devMaps.get(deviceSn);
			if (null == deviceInfo) {
				return -1;
			}
			haveDestSn = true;
			*//**see what function support*//*
			isSupportPlam = PushUtil.isDevFun(deviceInfo.getDevFuns(), DEV_FUNS.PLAM);
		}
		try {
			StringBuilder sb = new StringBuilder();
			*//**combine condition*//*
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			*//**get user basic information by condition*//*
//			List<UserInfo> userInfos = userInfoDao.fatchList(sb.toString());
//			
//			if(null == userInfos || userInfos.size() <= 0) {
//				return -1;
//			}
			
			*//**if face function is not support bytarget device, then not send the command*//*
			if (haveDestSn && !isSupportPlam) {
				return -1;
			}
			
			*//**get the biometrics template list from database*//*
			List<PersonBioTemplate> bioTemplates = bioTemplateDao.fatchList(sb.toString());
			
			for (PersonBioTemplate personTemplate : bioTemplates) {
				if (!haveDestSn) {
					*//**if target device is not exist, then check the device SN from face template*//*
					String tempSn = personTemplate.getDeviceSn();
					DeviceInfo tempDev = PushUtil.devMaps.get(tempSn);
					if (null == tempDev) {
						continue;
					}
					if (PushUtil.isDevFun(tempDev.getDevFuns(), DEV_FUNS.PLAM)
							&& Constants.BIO_TYPE_PALM == personTemplate.getBioType()) {
						*//**create delete plam template command*//*
						commandDao.add(DevCmdUtil.getDeletePlamCommand(personTemplate, deviceSn));
					}
				} else {
					if (Constants.BIO_TYPE_PALM == personTemplate.getBioType()) {
						*//**create delete plam template command*//*
						commandDao.add(DevCmdUtil.getDeletePlamCommand(personTemplate, deviceSn));
					}
				}
			}
			
			commandDao.commit();
		} catch (DaoException e) {
			commandDao.rollback();
		} finally {
			commandDao.close();
		}
		return 0;
		
	}*/
	
}
