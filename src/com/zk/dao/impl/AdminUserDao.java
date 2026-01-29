package com.zk.dao.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.BaseDao;
import com.zk.dao.IBaseDao;
import com.zk.exception.DaoException;
import com.zk.po.AdminUser;

public class AdminUserDao extends BaseDao implements IBaseDao<AdminUser> {
	
	private static Logger logger = Logger.getLogger(AdminUserDao.class);

	@Override
	public void add(AdminUser entity) throws DaoException {
		// Implementation omitted for now as we only need read for login
	}

	@Override
	public void update(AdminUser entity) throws DaoException {
		// Implementation omitted
	}

	@Override
	public void delete(int id) throws DaoException {
		// Implementation omitted
	}

	@Override
	public void delete(String cond) throws DaoException {
		// Implementation omitted
	}

	@Override
	public AdminUser fatch(int id) throws DaoException {
		// Implementation omitted
		return null;
	}
	
	/**
	 * Fetch admin user by username
	 * @param username
	 * @return AdminUser or null
	 * @throws DaoException
	 */
	public AdminUser fatch(String username) throws DaoException {
		AdminUser user = null;
		String sql = "select id, username, password, role, last_login from admin_users where username='" + username + "'";
		try {
			Statement st = getConnection().createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				user = new AdminUser();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setRole(rs.getString("role"));
				user.setLastLogin(rs.getTimestamp("last_login"));
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			logger.error(e);
			throw new DaoException(e);
		}
		return user;
	}

	@Override
	public List<AdminUser> fatchList(String cond) throws DaoException {
		return new ArrayList<AdminUser>();
	}

	@Override
	public int truncate() throws DaoException {
		return 0;
	}

}
