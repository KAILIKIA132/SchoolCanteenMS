package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.PersonBioTemplateDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.PersonBioTemplate;

/**
 * biometrics data management class, can be used for database operation, including fingerprint, face, and other biometrics data in future like vein
 * 
 * @author seiya
 * 
 */
public class BioTemplateManager {
	private static Logger logger = Logger.getLogger(BioTemplateManager.class);

	/**
	 * Add biometrics template to database
	 * 
	 * @param list
	 * biometrics data list
	 * @return 
	 * success, total number of AttLog number will be return; fail, -1 will be return
	 */
	public int createBioTemplate(List<PersonBioTemplate> list) {
		PersonBioTemplateDao bioTemplateDao = new PersonBioTemplateDao();
		try {
			for (PersonBioTemplate personBioTemplate : list) {
				bioTemplateDao.addOrUpdate(personBioTemplate);
			}

			bioTemplateDao.commit();
		} catch (DaoException e) {
			logger.error(e.toString());
			bioTemplateDao.rollback();
			return -1;
		} finally {
			bioTemplateDao.close();
		}

		return list.size();
	}

	/**
	 * Get the biometrics templates by specific device and user ID
	 * 
	 * @param userId
	 * server user ID
	 * @param deviceSn
	 * device serialnumber
	 * @return
	 * biometrics template data list, like fingerprint, face, etc.
	 */
	public List<PersonBioTemplate> getPersonTemplatesByUserIdAndSn(int userId,
			String deviceSn) {
		if (null == deviceSn || deviceSn.isEmpty()) {
			return null;
		}
		PersonBioTemplateDao templateDao = new PersonBioTemplateDao();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" and user_id=").append(userId).append(" ");
			sb.append(" and device_sn='").append(deviceSn).append("' ");
			List<PersonBioTemplate> list = templateDao.fatchList(sb.toString());
			return list;
		} catch (DaoException e) {
			logger.error(e.toString());
		} finally {
			templateDao.close();
		}
		return null;
	}
}
