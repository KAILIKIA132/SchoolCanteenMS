package com.zk.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AdvDao;
import com.zk.dao.impl.AttLogDao;
import com.zk.dao.impl.AttPhotoDao;
import com.zk.dao.impl.DeviceAttrsDao;
import com.zk.dao.impl.DeviceCommandDao;
import com.zk.dao.impl.DeviceInfoDao;
import com.zk.dao.impl.DeviceLogDao;
import com.zk.dao.impl.MeetInfoDao;
import com.zk.dao.impl.MessageDao;
import com.zk.dao.impl.PersonBioTemplateDao;
import com.zk.dao.impl.UserInfoDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.AttPhoto;
import com.zk.pushsdk.po.DeviceInfo;
import com.zk.pushsdk.util.PushUtil;
import com.zk.util.FileOperateUtil;

/**
 * Device management class, can be used for database operation
 * 
 * @author seiya
 *
 */
public class DeviceManager {

	private static Logger logger = Logger.getLogger(DeviceManager.class);
	/**
	 * Get the device information according to serialnumber
	 * 
	 * @param deviceSn
	 * @return
	 */
	public DeviceInfo getDeviceInfoBySn(String deviceSn) {
		DeviceInfo info = null;
		DeviceInfoDao devDao = new DeviceInfoDao();
		try {
			info = devDao.fatchDeviceInfoBySn(deviceSn);
		} catch (Exception e) {
			
		} finally {
			devDao.close();
		}
		
		return info;
	}
	
	/**
	 * Get all the devices information
	 * 
	 * @return
	 */
	public List<DeviceInfo> getAllDeviceInfoList() {
		DeviceInfoDao dao = new DeviceInfoDao();
		try {
			List<DeviceInfo> list = dao.fatchList("");
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			dao.close();
		}
		return null;
	}
	
	/**
	 * Get device total number according to condition
	 * 
	 * @param deviceSn
	 * @return
	 */
	public int getDeviceInfoCount(String deviceSn) {
		DeviceInfoDao devDao = new DeviceInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("'");
			}
			return devDao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			devDao.close();
		}
		return 0;
	}	
	
	/**
	 * Get device list according to condition, and then use for window display
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param startRec
	 * start record number
	 * @param pageSize
	 * maximum number of single getting operation
	 * @return
	 */
	public List<DeviceInfo> getDeviceInfoListForPage(String deviceSn, int startRec, int pageSize) {
		DeviceInfoDao dao = new DeviceInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			List<DeviceInfo> list = dao.fatchListWithCount(sb.toString(), startRec, pageSize);
			return list;
		} catch (Exception e) {
		} finally {
			dao.close();
		}
		return null;
	}
	
	/**
	 * Add new device to database
	 * 
	 * @param deviceInfo
	 * device information
	 * @return
	 * device information
	 */
	public DeviceInfo createDeviceInfo(DeviceInfo deviceInfo) {
		DeviceInfo info = null;
		DeviceInfoDao devDao = new DeviceInfoDao();
		try {
			devDao.add(deviceInfo);
			
			devDao.commit();
			
			info = devDao.fatchDeviceInfoBySn(deviceInfo.getDeviceSn());
		} catch (Exception e) {
			devDao.rollback();
			logger.error(e);
		} finally {
			devDao.close();
		}
		return info;
	}

	/**
	 * Update device information
	 * 
	 * @param deviceInfo
	 * device information object
	 * @return
	 */
	public int updateDeviceInfo(DeviceInfo deviceInfo) {
		DeviceInfoDao infoDao = new DeviceInfoDao();
		try {
			infoDao.update(deviceInfo);
			infoDao.commit();
		} catch (DaoException e) {
			infoDao.rollback();
		} finally {
			infoDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Update device status and refresh time
	 * 
	 * @param deviceSn
	 * @param state
	 * @param lastActive
	 * @return
	 */
	public int updateDeviceState(String deviceSn, String state, String lastActive) {
		DeviceInfoDao dao = new DeviceInfoDao();
		try {
			dao.updateState(deviceSn, state, lastActive);
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		
		return 0;
	}
	
	/**
	 * Delete data from specific device, including user information, biometrics, Attlog
	 * 
	 * @param sns
	 * @return
	 */
	public int deleteUserInfoByDeviceSn(String[] sns) {
		if (null == sns || sns.length <= 0) {
			return -1;
		}
		UserInfoDao userInfoDao = new UserInfoDao();
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		AttLogDao attLogDao = new AttLogDao();
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceLogDao deviceLogDao = new DeviceLogDao();
		try {
			List<List<AttPhoto>> photoList = new ArrayList<List<AttPhoto>>();
			for (String deviceSn : sns) {
				StringBuilder sb = new StringBuilder();
				sb.append(" and device_sn='").append(deviceSn).append("'");
				
				userInfoDao.delete(sb.toString());
				bioTemplateDao.delete(sb.toString());
				attLogDao.delete(sb.toString());
				
				List<AttPhoto> list = attPhotoDao.fatchList(sb.toString());
				photoList.add(list);
				
				attPhotoDao.delete(sb.toString());
				commandDao.delete(sb.toString());
				deviceLogDao.delete(sb.toString());
			}
			
			bioTemplateDao.commit();
			userInfoDao.commit();
			attLogDao.commit();
			attPhotoDao.commit();
			commandDao.commit();
			deviceLogDao.commit();
			
			for (List<AttPhoto> list : photoList) {
				for (AttPhoto attPhoto : list) {
					FileOperateUtil.delete(attPhoto.getFilePath());
				}
			}
		} catch (DaoException e) {
			bioTemplateDao.rollback();
			userInfoDao.rollback();
			attLogDao.rollback();
			attPhotoDao.rollback();
			commandDao.rollback();
			deviceLogDao.rollback();
		} finally {
			userInfoDao.close();
			bioTemplateDao.close();
			attLogDao.close();
			attPhotoDao.close();
			commandDao.close();
			deviceLogDao.close();
		}
		return 0;
	}
	
	/**
	 * Delete specific device and data, including user information, biometrics, AttLog, AttPhoto
	 * 
	 * @param sns
	 * @return
	 */
	public int deleteDeviceBySn(String[] sns) {
		DeviceInfoDao dao = new DeviceInfoDao();
		AttLogDao attLogDao = new AttLogDao();
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceLogDao deviceLogDao = new DeviceLogDao();
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		UserInfoDao userInfoDao = new UserInfoDao();
		DeviceAttrsDao attrsDao = new DeviceAttrsDao();
		MeetInfoDao meetInfoDao = new MeetInfoDao();
		AdvDao advDao = new AdvDao();
		MessageDao messageDao = new MessageDao();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" and device_sn in(");
			for (String string : sns) {
				sb.append("'").append(string).append("'");
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			List<AttPhoto> photoList = attPhotoDao.fatchList(sb.toString());
			
			attLogDao.delete(sb.toString());
			attPhotoDao.delete(sb.toString());
			commandDao.delete(sb.toString());
			deviceLogDao.delete(sb.toString());
			templateDao.delete(sb.toString());
			userInfoDao.delete(sb.toString());
			dao.delete(sb.toString());
			attrsDao.delete(sb.toString());
			meetInfoDao.delete(sb.toString());
			advDao.delete(sb.toString());
			messageDao.delete(sb.toString());
			
			attLogDao.commit();
			attPhotoDao.commit();
			commandDao.commit();
			deviceLogDao.commit();
			templateDao.commit();
			userInfoDao.commit();
			dao.commit();
			attrsDao.commit();
			meetInfoDao.commit();
			advDao.commit();
			messageDao.commit();
			
			/**delete device info from memory cache*/
			for (String deviceSn : sns) {
				PushUtil.deleteDeviceBySn(deviceSn);
			}
			
			for (AttPhoto attPhoto : photoList) {
				FileOperateUtil.delete(attPhoto.getFilePath());
			}
		} catch (DaoException e) {
			attLogDao.rollback();
			attPhotoDao.rollback();
			commandDao.rollback();
			deviceLogDao.rollback();
			templateDao.rollback();
			userInfoDao.rollback();
			dao.rollback();
			attrsDao.rollback();
			meetInfoDao.rollback();
			advDao.rollback();
			messageDao.rollback();
		} finally {
			attLogDao.close();
			attPhotoDao.close();
			commandDao.close();
			deviceLogDao.close();
			templateDao.close();
			userInfoDao.close();
			dao.close();
			attrsDao.close();
			meetInfoDao.close();
			advDao.close();
			messageDao.close();
		}
		return 0;
	}
	
	/**
	 * Initialize the server database
	 * 
	 * @return
	 */
	public int initDemo() {
		AttLogDao attLogDao = new AttLogDao();
		DeviceCommandDao commandDao = new DeviceCommandDao();
		DeviceInfoDao deviceInfoDao = new DeviceInfoDao();
		DeviceLogDao deviceLogDao = new DeviceLogDao();
		MessageDao messageDao = new MessageDao();
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		UserInfoDao userInfoDao = new UserInfoDao();
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		DeviceAttrsDao attrsDao = new DeviceAttrsDao();
		MeetInfoDao meetDao = new MeetInfoDao();
		AdvDao advDao = new AdvDao();
		try {
			List<AttPhoto> photoList = attPhotoDao.fatchList("");
			attLogDao.delete("");
			commandDao.delete("");
			deviceInfoDao.delete("");
			deviceLogDao.delete("");
			messageDao.delete("");
			bioTemplateDao.delete("");
			userInfoDao.delete("");
			attPhotoDao.delete("");
			attrsDao.delete("");		
			meetDao.delete("");
			advDao.delete("");
			
			attLogDao.commit();
			commandDao.commit();
			deviceInfoDao.commit();
			deviceLogDao.commit();
			messageDao.commit();
			bioTemplateDao.commit();
			userInfoDao.commit();
			attPhotoDao.commit();
			attrsDao.commit();
			meetDao.commit();
			advDao.commit();
			
			PushUtil.clearDevMaps();
			for (AttPhoto attPhoto : photoList) {
				FileOperateUtil.delete(attPhoto.getFilePath());
			}
		} catch (DaoException e) {
			attLogDao.rollback();
			commandDao.rollback();
			deviceInfoDao.rollback();
			deviceLogDao.rollback();
			messageDao.rollback();
			bioTemplateDao.rollback();
			userInfoDao.rollback();
			attPhotoDao.rollback();
			attrsDao.rollback();
			meetDao.rollback();
			advDao.rollback();
		} finally {
			attLogDao.close();
			commandDao.close();
			deviceInfoDao.close();
			deviceLogDao.close();
			messageDao.close();
			bioTemplateDao.close();
			userInfoDao.close();
			attPhotoDao.close();
			attrsDao.close();
			meetDao.close();
			advDao.close();
		}
		
		return 0;
	}
	
	/**
	 * Delete all the information of all users, including user information, biometrics, AttLog, AttPhoto
	 * @return
	 */
	public int deleteAllUserData() {
		AttLogDao attLogDao = new AttLogDao();
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		UserInfoDao userInfoDao = new UserInfoDao();
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		try {
			List<AttPhoto> photoList = attPhotoDao.fatchList("");
			attLogDao.delete("");
			bioTemplateDao.delete("");
			userInfoDao.delete("");
			attPhotoDao.delete("");
						
			attLogDao.commit();
			bioTemplateDao.commit();
			userInfoDao.commit();
			attPhotoDao.commit();
			
			for (AttPhoto attPhoto : photoList) {
				FileOperateUtil.delete(attPhoto.getFilePath());
			}
		} catch (DaoException e) {
			attLogDao.rollback();
			bioTemplateDao.rollback();
			userInfoDao.rollback();
			attPhotoDao.rollback();
		} finally {
			attLogDao.close();
			bioTemplateDao.close();
			userInfoDao.close();
			attPhotoDao.close();
		}
		return 0;
	}

	
}
