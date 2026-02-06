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
	var cmdSize = $("#cmdSize").val();
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
	+ "&transInterval=" + transInterval
	+ "&cmdSize=" + cmdSize;
	dealData(encodeURI(url));
}
</script>

</head>
<body>
<!--------------------------å¤´é¨å¼å§------------------------------------------>
<%@ include file="top.jsp" %>
<!--------------------------å¤´é¨ç»æ------------------------------------------>



<!--------------------------é¡µé¢åå®¹å¼å§------------------------------------------->

<div class="push_Webcontent_bigbox">
 
      <h1><s:text name="device.edit.title"/></h1> 
	  <ul class="Webcontent_dt">
    <li>
    	<h2><s:text name="table.header.device.sn"/>:</h2>      
    	<input type="text" id="deviceSn" class="text_time content_input" value="${deviceInfo.deviceSn}"
    	 readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/> 
    </li>
	<li>
		<h2><s:text name="table.header.device.ip"/>:</h2>
		<input type="text" id="ipAddress" class="text_time content_input" value="${deviceInfo.ipAddress}" 
		readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
	</li>
	<li>
		<h2><s:text name="table.header.device.name"/>:</h2>
		<input type="text" id="devName" class="text_time content_input" value="${deviceInfo.deviceName}"/>
	</li>
	<li>
		<h2><s:text name="table.header.device.transtimes"/>:</h2>
		<input type="text" id="transTimes" class="text_time content_input" value="${deviceInfo.transTimes}"/>
	</li>
	<li>
		<h2><s:text name="table.header.device.transinterval"/>:</h2>
		<input type="text" id="transInterval" class="text_time content_input" value="${deviceInfo.transInterval}"/>
	</li>
	<li>
		<h2><s:text name="table.header.device.firmwareversion"/>:</h2>
		<input type="text" id="fwVersion" class="text_time content_input" value="${deviceInfo.firmwareVersion}" 
		readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
	</li>
	<li>
		<h2><s:text name="device.edit.fpver"/>:</h2>
		<input type="text" id="fpVer" class="text_time content_input" value="${deviceInfo.fpAlgVersion}" 
		readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
	</li>
	<li>
		<h2><s:text name="device.edit.facever"/>:</h2>
		<input type="text" id="faceVer" class="text_time content_input" value="${deviceInfo.faceAlgVer}" 
		readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
	</li>	
	<li>
		<h2><s:text name="table.header.device.dev.fun"/>:</h2>
		<input type="text" id="devFuns" class="text_time content_input" value="${deviceInfo.devFuns}" 
		readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
	</li>
	<li>
		<h2><s:text name="device.edit.hint.cmd.size"/></h2>
		<input type="text" id="cmdSize" class="text_time content_input" value="${cmdSize}"/>
	</li>
		
  </ul>
 <div class="content_button_box">
 	<input type="button" class="content_input2" value='<s:text name="dev.edit.commit"/>' onclick="operation()"/>
	<input type="button" class="content_input3" value='<s:text name="sms.operate.return"/>' onclick="location.href='<%=basePath+"deviceAction!deviceList.action"%>'" /></div>
	</div>					
<!--------------------------é¡µé¢åå®¹ç»æ------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
