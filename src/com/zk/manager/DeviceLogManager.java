package com.zk.manager;

import java.util.ArrayList;
import java.util.List;

import com.zk.dao.impl.DeviceLogDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.DeviceLog;

/**
 * Device operation Log management class, can be used for database operation
 * 
 * @author seiya
 *
 */
public class DeviceLogManager {

	/**
	 * Add device operation log to the database
	 * 
	 * @param list
	 * device operation log data list
	 * @return
	 */
	public int addDeviceLog(List<DeviceLog> list) {
		DeviceLogDao dao = new DeviceLogDao();
		try {
			for (DeviceLog deviceLog : list) {
				dao.add(deviceLog);
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
	 * Get the device operation log list according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 */
	public List<DeviceLog> getDeviceLogList(String deviceSn, int startRec, int pageSize) {
		DeviceLogDao dao = new DeviceLogDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !"".equals(deviceSn)) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			/**get the device operation log**/
			List<DeviceLog> list = dao.fatchList(sb.toString(), startRec, pageSize);
			return list;
		} catch (DaoException e) {
		} finally {
			dao.close();
		}
		return new ArrayList<DeviceLog>();
	}
	
	/**
	 * Get the total number of device operation log according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @return
	 */
	public int getDeviceLogCount(String deviceSn) {
		DeviceLogDao dao = new DeviceLogDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			return dao.fatchCount(sb.toString());
		} catch (DaoException e) {
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Delete a specific device operation log
	 * 
	 * @param ids
	 * device operation log ID
	 * @return
	 */
	public int deleteDevLogById(String[] ids) {
		if (null == ids || ids.length <= 0) {
			return -1;
		}
		DeviceLogDao logDao = new DeviceLogDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			sb.append(" and dev_log_id in(");
			for (String id : ids) {
				sb.append(id);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			
			logDao.delete(sb.toString());
			logDao.commit();
		} catch (Exception e) {
			logDao.rollback();
		} finally {
			logDao.close();
		}
		return ids.length;
	}
	
	
}
