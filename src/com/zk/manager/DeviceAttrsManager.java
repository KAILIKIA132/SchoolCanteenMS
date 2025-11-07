package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.DeviceAttrsDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.DeviceAttrs;

public class DeviceAttrsManager {
	private static Logger logger = Logger.getLogger(DeviceAttrsManager.class);
	
	public int createDeviceAttrs(List<DeviceAttrs> list) {
		DeviceAttrsDao attrsDao = new DeviceAttrsDao();
		try {
			for (DeviceAttrs deviceAttrs : list) {
				attrsDao.addOrUpdate(deviceAttrs);
			}
			attrsDao.commit();
		} catch (DaoException e) {
			attrsDao.rollback();
			logger.error(e);
		} finally {
			attrsDao.close();
		}
		
		return list.size();
	}
	
	public List<DeviceAttrs> getDeviceAttrsBySn(String deviceSn) {
		if (null == deviceSn || deviceSn.isEmpty()) {
			return null;
		}
		DeviceAttrsDao attrsDao = new DeviceAttrsDao();
		try {
			
			List<DeviceAttrs> list = attrsDao.fatchList(" and device_sn='" + deviceSn + "'");
			
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			attrsDao.close();
		}
		
		return null;
	}
}
