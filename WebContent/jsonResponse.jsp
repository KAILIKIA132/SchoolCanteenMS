<%@ page language="java" contentType="application/json; charset=utf-8" pageEncoding="utf-8"%>
<%
	response.setContentType("application/json; charset=utf-8");
	response.setCharacterEncoding("utf-8");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
	response.setHeader("Access-Control-Allow-Headers", "Content-Type");
	
	String jsonResponse = (String) request.getAttribute("jsonResponse");
	if (jsonResponse != null && !jsonResponse.isEmpty()) {
		out.print(jsonResponse);
	} else {
		out.print("{\"success\":false,\"error\":\"No data available\"}");
	}
%>

