package com.zk.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.zk.manager.ManagerFactory;
import com.zk.po.AdminUser;
import com.zk.util.SecurityUtil;
import com.zk.dao.BaseDao;

public class LoginAction extends ActionSupport implements ServletRequestAware, SessionAware {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginAction.class);

	private HttpServletRequest request;
	private Map<String, Object> session;
	
	private String username;
	private String password;
	
	public String login() {
		return "login";
	}
	
	public String authenticate() {
		// First test database connection
		BaseDao baseDao = new BaseDao();
		if (!baseDao.testConnection()) {
			addActionError("Database connection failed. Please check your database settings.");
			return "login";
		}
		
		if (username == null || password == null) {
			addActionError("Username and password are required");
			return "login";
		}
		
		String hashedPassword = SecurityUtil.sha256(password);
		AdminUser user = ManagerFactory.getAdminUserManager().login(username, hashedPassword);
		
		if (user != null) {
			session.put("validUser", user);
			return "success";
		} else {
			addActionError("Invalid username or password");
			return "login";
		}
	}
	
	public String logout() {
		if (session instanceof org.apache.struts2.dispatcher.SessionMap) {
			((org.apache.struts2.dispatcher.SessionMap) session).invalidate();
		}
		return "login";
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
