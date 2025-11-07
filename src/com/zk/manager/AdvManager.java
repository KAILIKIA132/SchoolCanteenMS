package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AdvDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.Adv;

public class AdvManager {
	private static Logger logger = Logger.getLogger(AdvManager.class);
	
	/**
	 * Save Advs 
	 * @param list
	 * adv informations
	 * @return
	 */
	public int createAdv(List<Adv> list) {
		AdvDao dao = new AdvDao();
		try {
			for (Adv adv : list) {
				dao.addOrUpdate(adv);
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
	 * Delete adv data according to condition
	 * @param advIds
	 * adv ID
	 * @return
	 */
	public int deleteAdv(String[] advIds) {
		AdvDao dao = new AdvDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			sb.append(" and adv_id in(");
			for (String advId : advIds) {
				sb.append(advId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") ");
			
			dao.delete(sb.toString());
			
			dao.commit();
		} catch (DaoException e) {
			dao.rollback();
		} finally {
			dao.close();
		}
		
		return advIds.length;
	}
	
	/**
	 * Get the adv information list according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 * adv information list
	 */
	public List<Adv> fatchAllAdv(String deviceSn, int startRec, int pageSize) {
		AdvDao advDao = new AdvDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			/*if (null != code && !code.isEmpty()) {
				sb.append(" and code='").append(code).append("' ");
			}*/
			List<Adv> list = advDao.fatchListForPage(sb.toString(), startRec, pageSize);
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			advDao.close();
		}
		
		return null;
	}
	
	public int getAdvCount(String deviceSn) {
		AdvDao dao = new AdvDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			/*if (null != code && !code.isEmpty()) {
				sb.append(" and code='").append(code).append("' ");
			}*/
			return dao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Get adv throw ID condition
	 * @param id
	 * @return
	 * adv<code>Adv</code>
	 */
	public Adv getAdvById(int id) {
		AdvDao dao = new AdvDao();
		try {
			Adv adv = dao.fatch(id);
			return adv;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			dao.close();
		}
		return null;
	}
}
