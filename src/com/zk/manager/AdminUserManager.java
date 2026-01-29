package com.zk.manager;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AdminUserDao;
import com.zk.exception.DaoException;
import com.zk.po.AdminUser;

public class AdminUserManager {
	private static Logger logger = Logger.getLogger(AdminUserManager.class);

	/**
	 * Authenticate admin user
	 * @param username
	 * @param password (hashed)
	 * @return AdminUser if successful, null otherwise
	 */
	public AdminUser login(String username, String password) {
		AdminUserDao dao = new AdminUserDao();
		try {
			AdminUser user = dao.fatch(username);
			if (user != null && user.getPassword().equals(password)) {
				return user;
			}
		} catch (DaoException e) {
			logger.error(e);
		} finally {
			dao.close();
		}
		return null;
	}
}
