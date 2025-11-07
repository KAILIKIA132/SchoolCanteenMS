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
	$("#form1").submit();
}

function URLencode(sStr) { 

  return escape(sStr)
  .replace(/\+/g, '%2B')
  .replace(/\"/g,'%22')
  .replace(/\'/g, '%27')
  .replace(/\//g,'%2F'); 

} 
	
	
function operationNew() {
	var type = $("#type").val();
	if (null == type || "" == type) {
		alert('<s:text name="adv.hint.input.adv.type"/>');
		return;
	}
	var fileName = $("#fileName").val();
	if (null == fileName || "" == fileName) {
		alert('<s:text name="adv.hint.input.adv.filename"/>');
		return;
	}

	var file = $("#advFile").val();
	if (null == file || "" == file) {
		alert('<s:text name="adv.hint.select.adv.file"/>');
		return;
	}
	var deviceSn = $("input:radio[name=devSn]:checked").val();
	if (null == deviceSn) {
		alert('<s:text name="user.hint.select.device.sn"/>');
		return;
	}
	dealData();
}

function operationEdit() {
	var type = $("#type").val();
	if (null == type || "" == type) {
		alert('<s:text name="adv.hint.input.adv.name"/>');
		return;
	}
	var file = $("#advFile").val();
	if (null == file || "" == file) {
		alert('<s:text name="adv.hint.input.adv.fileName"/>');
		return;
	}
	var advId = $("#advId").val();
	var url="advAction!editAdv.action?act=edit&type=" + type
			+ "&fileName=" + fileName + "&url=" + url + "&advId=" + advId;
	dealData();
}
</script>

</head>	
<body>
<!--------------------------top of page------------------------------------------>
<%@ include file="top.jsp" %>
<!--------------------------top of page end------------------------------------------>



<!--------------------------edit area------------------------------------------->

<div class="push_Webcontent_bigbox">
<form id="form1" method="post" action="advAction!editAdv.action" enctype="multipart/form-data">
	<input type="hidden" id="act" name="act" value="${act }" />
  	<c:choose>	
  		<c:when test="${act == 'new'}">
      		<h1><s:text name="adv.operate.add.adv"/></h1>
      		<ul class="Webcontent_dt">
      			<li>
					<h2><s:text name="table.header.adv.type"/>:</h2>
					<select name="type" id="type">
						<option value ="1" selected="selected"><s:text name="adv.file.type.picture"/></option>
						<option value ="2"><s:text name="adv.file.type.video"/></option>
					</select>
				</li>
				<li>
					<h2><s:text name="table.header.adv.name"/>:</h2>
					<input type="text" id="fileName" name="fileName" class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.adv.file"/>:</h2>
					<input type="file" id="advFile" name="advFile" class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.att.devicesn"/>:</h2>
					<div>
					<table>
						<c:forEach var="selDev" items="${devList}">
							<tr>
								<td>
									<input name="devSn" type="radio" value="${selDev.deviceSn}">${selDev.deviceSn}(${selDev.ipAddress})</input>
								</td>
							</tr>
						</c:forEach>
					</table>
					</div>
				</li>
		  	</ul>
		 	<div class="content_button_box">
		 		<input type="button" class="content_input2" value='<s:text name="sms.operate.add"/>' onclick="operationNew();"/>
				<input type="button" class="content_input3" value='<s:text name="sms.operate.return"/>' onclick="location.href='<%=basePath+"advAction!advList.action"%>'" />
			</div>
      	</c:when> 
      	<%-- <c:when test="${act == 'edit' }">
      		<h1><s:text name="user.operate.edit.user"/></h1>
      		<ul class="Webcontent_dt">
				<li>
					<h2><s:text name="table.header.meet.code"/>:</h2>
					<input type="text" id="code" value="${meetInfo.code }" class="text_time content_input" 
					readonly="readonly" disabled="disabled" style="background:#CCCCCC" onfocus=this.blur()/>
					<input type="hidden" id="meetId" value="${meetInfo.meetInfoId }" />
				</li>
				<li>
					<h2><s:text name="table.header.meet.name"/>:</h2>
					<input type="text" id="metName" value="${meetInfo.metName }" class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.metStarSignTm"/>:</h2>
					<input type="text" id="metStarSignTm" value="${meetInfo.metStarSignTm }" class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.metLatSignTm"/>:</h2>
					<input type="text" id="metLatSignTm" value="${meetInfo.metLatSignTm }"  class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.earRetTm"/>:</h2>
					<input type="text" id="earRetTm" value="${meetInfo.earRetTm }"  class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.latRetTm"/>:</h2>
					<input type="text" id="latRetTm" value="${meetInfo.latRetTm }"  class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.metStrTm"/>:</h2>
					<input type="text" id="metStrTm" value="${meetInfo.metStrTm }"  class="text_time content_input"/>
				</li>
				<li>
					<h2><s:text name="table.header.meet.metEndTm"/>:</h2>
					<input type="text" id="metEndTm" value="${meetInfo.metEndTm }"  class="text_time content_input"/>
				</li>
		  	</ul>
		 	<div class="content_button_box">
		 		<input type="submit" class="content_input2" value='<s:text name="dev.edit.commit"/>' onclick="operationEdit();"/>
				<input type="button" class="content_input3" value='<s:text name="sms.operate.return"/>' onclick="location.href='<%=basePath+"meetAction!meetinfoList.action"%>'" />
			</div>
      	</c:when> --%>
      </c:choose>
</form>
</div>

<!--------------------------edit area end------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
