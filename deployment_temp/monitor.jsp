<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="5" />
<title><s:text name="push.web.demo.name"/></title>
<%@ include file="includejs.jsp" %>

</head>
<body>
<!--------------------------å¤´é¨å¼å§------------------------------------------>
<%@ include file="top.jsp"%>
<!--------------------------å¤´é¨ç»æ------------------------------------------>

<!--------------------------å¤´é¨æä½åè½å¼å§------------------------------------------>
<div class="push_Search_box">

<div class="Monitor_Record l"><strong>${time}</strong></div>

</div>

<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

<!--------------------------åè¡¨å¼å§------------------------------------------->
<table border="0" cellpadding="0" cellspacing="0" class="push_tab2">
  <tr>
    <th><s:text name="table.header.monitor.time"/></th>
    <th><s:text name="table.header.monitor.device"/></th>
    <th><s:text name="table.header.monitor.event"/></th>
    <th><s:text name="table.header.monitor.pin"/></th>
     <th><s:text name="table.header.monitor.mask"/></th>
      <th><s:text name="table.header.monitor.temperature"/></th>
    <th><s:text name="table.header.monitor.param1"/></th>
    <th><s:text name="table.header.monitor.param2"/></th>
    <!--<th><s:text name="table.headr.monitor.param3"/></th>
  --></tr>
  <c:forEach var="mon" items="${monList}">
  	<c:choose>
	<c:when test="${mon.alarm == true}">
	  	<tr class="Alarm">
	  		<td>${mon.operateTime}</td>
	  		<td>${mon.deviceSn}</td>
	  		<td>${mon.operateTypeStr}</td>
	  		<td>${mon.operator}</td>
	  		<td>${mon.maskFlag}</td>
	  		<td>${mon.temperatureReading}</td>
	  		<td>${mon.value2 }</td>
	  		<td>${mon.value3 }</td>
  		<!--<td>${mon.reserved }</td>
  	--></tr>
	</c:when>
	<c:otherwise>
		<tr>
	  		<td>${mon.operateTime}</td>
	  		<td>${mon.deviceSn}</td>
	  		<td>${mon.operateTypeStr}</td>
	  		<td>${mon.operator}</td>
	  		<td>${mon.maskFlag}</td>
	  		<td>${mon.temperatureReading}</td>
	  		<td>${mon.value2 }</td>
	  		<td>${mon.value3 }</td>
  		<!--<td>${mon.reserved }</td>
  	--></tr>
	</c:otherwise>
	</c:choose>
  </c:forEach>
</table>
<!--------------------------åè¡¨ç»æ------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
