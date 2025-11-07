<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>PUSH SDK</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/menu.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
 <script src="js/zkadmin.js" type="text/javascript"></script>
 <script src="js/jquery.min.js" type="text/javascript"></script>
 <link rel="stylesheet" type="text/css" href="css/fbmodal.css" />
<script type="text/javascript" src="js/showdate.js"></script>
<script type="text/javascript">

function dealData(url)
{
	$.ajax({
       type: "POST",
       url: url,
       dataType:"json",
       async: false,
      /**
       success: function(data)
       {
   		alert("operations are successfully");
   		window.location.reload(); 
       },
       error:function (XMLHttpRequest, textStatus, errorThrown)
		{
		    alert("The operation failed, please try again...");
		    window.location.reload(); 
		}
       **/
	});
	window.location.href='<%=basePath+"deviceAction!deviceList.action"%>'; 
}

	
function operation() {
	var devName = $("#devName").val();
	var transTimes = $("#transTimes").val();
	var transInterval = $("#transInterval").val();
	if (null == devName || "" == devName) {
		alert('<s:text name="device.hit.device.name.empty"/>');
		return;
	}
	if (null == transTimes || "" == transTimes) {
		alert('<s:text name="device.hint.device.trans.times"/>');
		return;
	}
	if (null == transInterval || "" == transInterval) {
		alert('<s:text name="device.hint.device.trans.interval"/>');
		return;
	}
	var url="deviceAction!modifyDevice.action?deviceSn=" + ${deviceInfo.deviceSn} 
	+ "&devName="+ devName 
	+ "&transTimes=" + transTimes 
	+ "&transInterval=" + transInterval;
	dealData(url);
}
</script>

</head>
<body>

<%@ include file="top.jsp" %>


<div class="push_Webcontent_bigbox">
 
      <h1><s:text name="cmd.view.detail"/></h1> 
	  <ul class="Webcontent_dt">
    <li>
    	<h2><s:text name="table.header.cmd.id"/>:</h2>      
    	<input type="text" class="text_time content_input" value="${command.devCmdId}" readonly="readonly"/> 
    </li>
	<li>
		<h2><s:text name="table.header.cmd.device"/>:</h2>
		<input type="text" id="ipAddress" class="text_time content_input" value="${command.deviceSn}" readonly="readonly"/>
	</li>
	<li>
		<h2><s:text name="cmd.header.cmd.commit.time"/>:</h2>
		<input type="text" id="transTimes" class="text_time content_input" value="${command.cmdCommitTime}" readonly="readonly"/>
	</li>
	<li>
		<h2><s:text name="cmd.header.cmd.trans.time"/>:</h2>
		<input type="text" id="transInterval" class="text_time content_input" value="${command.cmdTransTime}" readonly="readonly"/>
	</li>
	<li>
		<h2><s:text name="cmd.header.cmd.return.time"/>:</h2>
		<input type="text" id="fwVersion" class="text_time content_input" value="${command.cmdOverTime}" readonly="readonly"/>
	</li>
	<li>
		<h2><s:text name="cmd.header.cmd.return.value"/>:</h2>
		<input type="text" id="fpVer" class="text_time content_input" value="${command.cmdReturn}" readonly="readonly"/>
	</li>
	<li>
		<h2><s:text name="table.header.cmd.content"/>:</h2>
		<textarea name="" rows="6" class="content_input" style="height:120px;width: 800px;" readonly="readonly">${command.cmdContent}</textarea>
		
	</li>	
	<li>
		<h2><s:text name="table.header.cmd.return.info"/>:</h2>
		<textarea name="" rows="6" class="content_input" style="height:120px;width: 800px" readonly="readonly">${command.cmdReturnInfo}</textarea>
	</li>	
  </ul>
 <div class="content_button_box">
 	<!--<input type="button" class="content_input2" value='<s:text name="dev.edit.commit"/>' onclick="operation()"/>
	--><input type="button" class="content_input3" value='<s:text name="sms.operate.return"/>' onclick="location.href='javascript:history.go(-1);'" /></div>
	
	</div>				

<%@ include file="bottom.jsp"%>
</body>
</html>
