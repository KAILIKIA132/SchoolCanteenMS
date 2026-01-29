package com.zk.interceptor;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.zk.po.AdminUser;

public class AuthInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		
		Object user = session.get("validUser");
		if (user != null && user instanceof AdminUser) {
			return invocation.invoke();
		}
		
		return "login_required";
	}

}
