package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.MeetInfoDao;
import com.zk.dao.impl.UserInfoDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.MeetInfo;
import com.zk.pushsdk.po.UserInfo;

public class MeetInfoManager {
	private static Logger logger = Logger.getLogger(MeetInfoManager.class);
	
	/**
	 * Save MeetInfos 
	 * @param list
	 * meet base informations
	 * @return
	 */
	public int createMeetInfo(List<MeetInfo> list) {
		MeetInfoDao dao = new MeetInfoDao();
		try {
			for (MeetInfo meetInfo : list) {
				dao.addOrUpdate(meetInfo);
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
	 * Delete meet data according to condition, including basic information
	 * @param meetIds
	 * meet ID
	 * @return
	 */
	public int deleteMeetInfo(String[] meetInfoIds) {
		UserInfoDao userInfoDao = new UserInfoDao();
		MeetInfoDao dao = new MeetInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			sb.append(" and meet_info_id in(");
			for (String meetInfoId : meetInfoIds) {
				sb.append(meetInfoId);
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") ");
			
			/**Set User's meet code as null*/
			List<MeetInfo> meetInfos = dao.fatchList(sb.toString());
			if(meetInfos != null && meetInfos.size() > 0) {
				String codeStr = " and meet_code in (";
				for(MeetInfo meetInfo : meetInfos) {
					String code = meetInfo.getCode();
					codeStr += code + ",";
				}
				codeStr = codeStr.substring(0, codeStr.length()-1);
				codeStr += ")";
				userInfoDao.updateMeet(null,codeStr);
			}
			
			dao.delete(sb.toString());
			
			
			userInfoDao.commit();
			dao.commit();
		} catch (DaoException e) {
			userInfoDao.rollback();
			dao.rollback();
		} finally {
			userInfoDao.close();
			dao.close();
		}
		
		return meetInfoIds.length;
	}
	
	/**
	 * Get the meet basic information list according to condition
	 * 
	 * @param deviceSn
	 * device serialnumber
	 * @param code
	 * meet ID
	 * @param startRec
	 * start record position
	 * @param pageSize
	 * maximum number of query data
	 * @return
	 * meet basic information list
	 */
	public List<MeetInfo> fatchAllMeet(String deviceSn, String code, int startRec, int pageSize) {
		MeetInfoDao meetInfoDao = new MeetInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			if (null != code && !code.isEmpty()) {
				sb.append(" and code='").append(code).append("' ");
			}
			List<MeetInfo> list = meetInfoDao.fatchListForPage(sb.toString(), startRec, pageSize);
			return list;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			meetInfoDao.close();
		}
		
		return null;
	}
	
	public int getMeetInfoCount(String deviceSn, String code) {
		MeetInfoDao dao = new MeetInfoDao();
		try {
			StringBuilder sb = new StringBuilder();
			/**combine the condition**/
			if (null != deviceSn && !deviceSn.isEmpty()) {
				sb.append(" and device_sn='").append(deviceSn).append("' ");
			}
			if (null != code && !code.isEmpty()) {
				sb.append(" and code='").append(code).append("' ");
			}
			return dao.fatchCount(sb.toString());
		} catch (DaoException e) {
			
		} finally {
			dao.close();
		}
		return 0;
	}
	
	/**
	 * Get meet info throw ID condition
	 * @param id
	 * @return
	 * meet info<code>MeetInfo</code>
	 */
	public MeetInfo getMeetInfoById(int id) {
		MeetInfoDao dao = new MeetInfoDao();
		try {
			MeetInfo info = dao.fatch(id);
			return info;
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			dao.close();
		}
		return null;
	}
}
