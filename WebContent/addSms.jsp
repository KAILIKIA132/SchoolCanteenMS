<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"  %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name="push.web.demo.name"/></title>
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
	window.location.href='<%=basePath+"smsAction!smsList.action"%>'; 
}

function URLencode(sStr) 

{ 

  return escape(sStr)
  .replace(/\+/g, '%2B')
  .replace(/\"/g,'%22')
  .replace(/\'/g, '%27')
  .replace(/\//g,'%2F'); 

} 
	
	
function operation() {
	var typeValue = $("input:radio[name=smsType]:checked").val();
	var startTimeValue = $("#startTime").val();
	if (null == startTimeValue || "" == startTimeValue) {
		alert('<s:text name="sms.hint.time.cannot.empty"/>');
		return;
	}
	var validMinuteValue = $("#validMinute").val();
	if (null == validMinuteValue || "" == validMinuteValue) {
		alert('<s:text name="sms.hint.valid.minute.empty"/>');
	}
	var smsContentValue = $("#smsContent").val();
	if (null == smsContentValue || "" == smsContentValue) {
		alert('<s:text name="sms.hint.content.empty"/>');
	}
	var url="smsAction!addSms.action?smsType=" + typeValue + "&startTime=" 
	+ startTimeValue + "&validMinute=" + validMinuteValue 
	+ "&smsContent=" + smsContentValue;
	//alert(url);
	dealData(encodeURI(url));
}
</script>

</head>	
<body>
<!--------------------------top of page------------------------------------------>
<%@ include file="top.jsp" %>
<!--------------------------top of page end------------------------------------------>



<!--------------------------edit area------------------------------------------->

<div class="push_Webcontent_bigbox">
  <!-- <form action="smsAction!addSms.action" method="post"> -->
      <h1><s:text name="sms.operate.add.sms"/></h1> 
      
	  <ul class="Webcontent_dt">
	  	<li>
	  		<h2><s:text name="table.header.sms.type"/>:</h2>
			<input  type="radio" id="publicSms"  checked="checked" name="smsType" value="253"/><label for="radiobutton"><s:text name="sms.type.public"/></label>
			<input type="radio"  value="254"  id="privateSms" name="smsType" /> 
			<label for="radio"><s:text name="sms.type.private"/></label>
    	</li>
		<li>
			<h2><s:text name="table.header.sms.start.time"/>:</h2>
			<input type="text" id="startTime" name="startTime" class="text_time content_input"/>
			<s:text name="sms.hint.time.format"/>
		</li>
		<li>
			<h2><s:text name="table.header.sms.valid.minutes"/>:</h2>
			<input type="text" id="validMinute" name ="validMinute" class="text_time content_input"/>
			<s:text name="sms.hint.minute"/>
		</li>
		<li>
			<h2><s:text name="table.header.sms.content"/>:</h2>
	  		<textarea id="smsContent" name ="smsContent" rows="6" class="content_input" style="height:120px"></textarea>
	  		<s:text name="sms.hint.content.length"/>
		</li>
  	</ul>
 	<div class="content_button_box">
 		<input type="submit" class="content_input2" value='<s:text name="sms.operate.add"/>' onclick="operation();"/>
		<input type="button" class="content_input3" value='<s:text name="sms.operate.return"/>' onclick="location.href='<%=basePath+"smsAction!smsList.action"%>'" />
	</div>
	<!-- </form> -->
</div>

<!--------------------------edit area end------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
