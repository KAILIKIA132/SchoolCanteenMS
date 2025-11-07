package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.PersonBioTemplateDao;
import com.zk.dao.impl.UserInfoDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.UserInfo;
import com.zk.pushsdk.util.Constants;
/**
 * Attlog management class, can be used for database operation
 * @author seiya
 *
 */
public class UserInfoManager {

	private static Logger logger = Logger.getLogger(UserInfoManager.class);
		
	/**
	 * Save UserInfos 
	 * @param list
	 * user base informations
	 * @return
	 */
	public int createUserInfo(List<UserInfo> list) {
		UserInfoDao dao = new UserInfoDao();
		try {
			for (UserInfo userInfo : list) {
				dao.addOrUpdate(userInfo);
			}
			
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		
		return list.size();
	}
	
	/**
	 * Delete user data according to condition, including basic information, biometrics
	 * @param userIds
	 * user ID
	 * @return
	 */
	public int deleteUserInfo(String[] userIds) {
		UserInfoDao dao = new UserInfoDao();
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") ");
			
			dao.delete(sb.toString());
			bioTemplateDao.delete(sb.toString());
			
			dao.commit();
			bioTemplateDao.commit();
		} catch (DaoException e) {
			dao.rollback();
			bioTemplateDao.rollback();
		} finally {
			dao.close();
			bioTemplateDao.close();
		}
		
		return userIds.length;
	}
	
	/**
	 * Get the user basic information list according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param code
	 * meet code
	 * @return
	 * user basic information list
	 */
	public List<UserInfo> fatchAllUser(String deviceSn) {
		UserInfoDao userInfoDao = new UserInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			
			List<UserInfo> list = userInfoDao.fatchList(sb.toString());
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			userInfoDao.close();
		}
		
		return null;
	}
	
	/**
	 * Get the user basic information list according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param userPin
	 * user ID
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 * user basic information list
	 */
	public List<UserInfo> fatchAllUser(String deviceSn, String userPin, int startRec, int pageSize) {
		UserInfoDao userInfoDao = new UserInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			if (null != userPin && !userPin.isEmpty()) {
				sb.append(" and user_pin='").append(userPin).append("' ");
			}
			List<UserInfo> list = userInfoDao.fatchListForPage(sb.toString(), startRec, pageSize);
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			userInfoDao.close();
		}
		
		return null;
	}
	
	/**
	 * Get the total number of user basic information according to condition
	 * @param deviceSn
	 * device serialnumber
	 * @param userPin
	 * user ID
	 * @return
	 */
	public int getUserInfoCount(String deviceSn, String userPin) {
		UserInfoDao dao = new UserInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			if (null != userPin && !userPin.isEmpty()) {
				sb.append(" and user_pin='").append(userPin).append("' ");
			}
			return dao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Get the user basic information  according to user ID and device serialnumber
	 * @param userPin
	 * user ID
	 * @param deviceSn
	 * device serialnumber
	 * @return
	 */
	public UserInfo getUserInfoByPinAndSn(String userPin, String deviceSn) {
		UserInfoDao dao = new UserInfoDao();
		try {
			UserInfo info = dao.fatch(userPin, deviceSn);
			return info;
		} catch (DaoException e) {
		} finally {
			dao.close();
		}
		return null;
	}
	
	/**
	 * Update user photo
	 * 
	 * @param list
	 * user basic information
	 * @return
	 */
	public int updateUserPic(List<UserInfo> list) {
		UserInfoDao dao = new UserInfoDao();
		try {
			for (UserInfo userInfo : list) {
				dao.updateUserPic(userInfo);
			}
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		return list.size();
	}
	
	/**
	 * Delete a specific user fingerprint from server
	 * 
	 * @param userIds
	 * user ID
	 * @return
	 */
	public int deleteFpFromServer(String[] userIds) {
		if (null == userIds || userIds.length <= 0) {
			return -1;
		}
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the biometrics data type for condition**/
			sb.append(" and bio_type=").append(Constants.BIO_TYPE_FP).append(" ");
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			templateDao.delete(sb.toString());
			
			templateDao.commit();
		} catch (DaoException e) {
			templateDao.rollback();
		} finally {
			templateDao.close();
		}
		return userIds.length;
	}
	
	/**
	 * Delete a specific user face template from server
	 * 
	 * @param userIds
	 * user ID
	 * @return
	 */
	public int deleteFaceFromServer(String[] userIds) {
		if (null == userIds || userIds.length <= 0) {
			return -1;
		}
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the biometrics data type for condition**/
			sb.append(" and bio_type=").append(Constants.BIO_TYPE_FACE).append(" ");
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			templateDao.delete(sb.toString());
			
			templateDao.commit();
		} catch (DaoException e) {
			templateDao.rollback();
		} finally {
			templateDao.close();
		}
		return userIds.length;
	}
	
	/**
	 * Delete a specific user photo from server
	 * 
	 * @param userIds
	 * user ID
	 * @return
	 */
	public int deleteUserPicFromServer(String[] userIds) {
		if (null == userIds || userIds.length <= 0) {
			return -1;
		}
		UserInfoDao userInfoDao = new UserInfoDao();
		try {
			for (String userId : userIds) {
				UserInfo info = new UserInfo();
				info.setUserId(Integer.valueOf(userId));
				/**delete all the user photos**/
				info.setPhotoIdContent("");
				info.setPhotoIdName("");
				info.setPhotoIdSize(0);
				
				userInfoDao.updateUserPic(info);
			}
			
			userInfoDao.commit();
		} catch (DaoException e) {
			userInfoDao.rollback();
		} finally {
			userInfoDao.close();
		}
		return userIds.length;
	}
	
	/**
	 * Get user info throw ID condition
	 * @param id
	 * @return
	 * user info<code>UserInfo</code>
	 */
	public UserInfo getUserInfoById(int id) {
		UserInfoDao dao = new UserInfoDao();
		try {
			UserInfo info = dao.fatch(id);
			return info;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			dao.close();
		}
		return null;
	}

	/**
	 * Delete a specific user palm template from server
	 * 
	 * @param userIds
	 * user ID
	 * @return
	 */
	public int deletePlamFromServer(String[] userIds) {
		if (null == userIds || userIds.length <= 0) {
			return -1;
		}
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the biometrics data type for condition**/
			sb.append(" and bio_type=").append(Constants.BIO_TYPE_PALM).append(" ");
			sb.append(" and user_id in(");
			for (String userId : userIds) {
				sb.append(userId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			templateDao.delete(sb.toString());
			
			templateDao.commit();
		} catch (DaoException e) {
			templateDao.rollback();
		} finally {
			templateDao.close();
		}
		return userIds.length;
	}
}
