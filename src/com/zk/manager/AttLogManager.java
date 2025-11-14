package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AttLogDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.AttLog;
import com.zk.pushsdk.po.UserInfo;
import com.zk.util.ExternalApiUtil;

/**
 * AttLog management class, can be used for database operation
 * @author seiya
 *
 */
public class AttLogManager {
	private static Logger logger = Logger.getLogger(AttLogManager.class);
	
	/**
	 * Get AttLog data according to condition
	 * @param deviceSn
	 * device serialnumber
	 * @param userPin
	 * user ID
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 * AttLog data list
	 */
	public List<AttLog> getAttLogList(String deviceSn, String userPin, int startRec, int pageSize) {
		AttLogDao attLogDao = new AttLogDao();
		StringBuilder sb = new StringBuilder();
		/**combine the device SN for condition**/
		if (null != deviceSn && !deviceSn.isEmpty()) {
			sb.append(" and device_sn='").append(deviceSn).append("' ");
		}
		/**combine the user ID for condition**/
		if (null != userPin && !userPin.isEmpty()) {
			sb.append(" and user_pin='").append(userPin).append("' ");
		}
		try {
			List<AttLog> list = attLogDao.fatchList(sb.toString(), startRec, pageSize);
			return list;
		} catch (DaoException e) {
			logger.error(e.toString());
		} finally {
			attLogDao.close();
		}
		
		return null;
	}
	
	/**
	 * Get the AttLog record number according to condition
	 * @param deviceSn
	 * device serialnumber
	 * @param userPin
	 * user ID
	 * @return
	 */
	public int getAttLogCount(String deviceSn, String userPin) {
		AttLogDao logDao = new AttLogDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the device SN for condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			/**combine the user ID for condition**/
			if (null != userPin && !userPin.isEmpty()) {
				sb.append(" and user_pin='").append(userPin).append("' ");
			}
			return logDao.fatchCount(sb.toString());
		} catch (DaoException e) {
			logger.error(e.toString());
		} finally {
			logDao.close();
		}
		return 0;
	}
	
	/**
	 * Add AttLog to database
	 * @param list
	 * AttLog list
	 * @return
	 */
	public int createAttLog(List<AttLog> list) {
		if (null == list) {
			return -1;
		}
		AttLogDao attLogDao = new AttLogDao();
		try {
			// Look up user names for all AttLog entries before saving
			for (AttLog attLog : list) {
				// If userName is not set, look it up from UserInfo table
				if ((attLog.getUserName() == null || attLog.getUserName().isEmpty()) 
						&& attLog.getUserPin() != null && !attLog.getUserPin().isEmpty()
						&& attLog.getDeviceSn() != null && !attLog.getDeviceSn().isEmpty()) {
					try {
						UserInfo userInfo = ManagerFactory.getUserInfoManager().getUserInfoByPinAndSn(
							attLog.getUserPin(), attLog.getDeviceSn());
						if (userInfo != null && userInfo.getName() != null) {
							attLog.setUserName(userInfo.getName());
							logger.debug("Found user name for PIN " + attLog.getUserPin() + ": " + userInfo.getName());
						} else {
							logger.warn("User not found for PIN: " + attLog.getUserPin() + ", Device: " + attLog.getDeviceSn());
						}
					} catch (Exception e) {
						logger.warn("Error looking up user name for PIN " + attLog.getUserPin() + ": " + e.getMessage());
					}
				}
				attLogDao.addOrUpdate(attLog);
			}
			attLogDao.commit();
			logger.info("=== EXTERNAL API: Attendance logs saved, starting external API calls ===");
			logger.info("EXTERNAL API: Processing " + list.size() + " verification(s)");
			
			// Notify external API for each verification
			for (AttLog attLog : list) {
				logger.info("EXTERNAL API: Processing verification - User PIN: " + attLog.getUserPin() + 
					", User Name: " + (attLog.getUserName() != null ? attLog.getUserName() : "N/A") + 
					", Timestamp: " + attLog.getVerifyTime());
				
				if (attLog.getUserPin() != null && !attLog.getUserPin().isEmpty()) {
					logger.info("EXTERNAL API: Calling notifyVerification for User ID: " + attLog.getUserPin());
					ExternalApiUtil.notifyVerification(attLog.getUserPin(), attLog.getVerifyTime(), 
						attLog.getUserName() != null ? attLog.getUserName() : "");
					logger.info("EXTERNAL API: notifyVerification called for User ID: " + attLog.getUserPin() + 
						", Timestamp: " + attLog.getVerifyTime());
				} else {
					logger.warn("EXTERNAL API: Skipping - User PIN is null or empty for verification at: " + attLog.getVerifyTime());
				}
			}
			
			logger.info("=== EXTERNAL API: Finished processing all verifications ===");
		} catch (DaoException e) {
			attLogDao.rollback();
			logger.error(e.toString());
		} finally {
			attLogDao.close();
		}
		return list.size();
	}
	
	/**
	 * Delete the AttLog according to the record ID
	 * @param logIds
	 * @return
	 */
	public int deleteAttLogByIds(String[] logIds) {
		if (null == logIds || logIds.length <= 0) {
			return -1;
		}
		AttLogDao attLogDao = new AttLogDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the ID for condition**/
			sb.append(" and att_log_id in(");
			for (String logId : logIds) {
				sb.append(logId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			attLogDao.delete(sb.toString());
			
			attLogDao.commit();
		} catch (DaoException e) {
			attLogDao.rollback();
		} finally {
			attLogDao.close();
		}
		return 0;
	}
	
	/**
	 * Delete all the AttLog in database
	 * @return
	 */
	public int clearAllAttLog() {
		AttLogDao attLogDao = new AttLogDao();
		try {
			attLogDao.delete("");
			attLogDao.commit();
		} catch (DaoException e) {
			attLogDao.rollback();
		} finally {
			attLogDao.close();
		}
		return 0;
	}
}
