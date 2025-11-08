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

function validateFile() {
	var fileInput = document.getElementById("bulkImportFile");
	if (!fileInput || !fileInput.files || !fileInput.files[0]) {
		alert("Please select a CSV file to upload.");
		return false;
	}
	
	var fileName = fileInput.files[0].name;
	if (!fileName.toLowerCase().endsWith('.csv')) {
		alert("Please select a CSV file (.csv extension).");
		return false;
	}
	
	return true;
}

function submitForm() {
	if (validateFile()) {
		$("#form1").submit();
	}
}

</script>

</head>	
<body>
<!--------------------------top of page------------------------------------------>
<%@ include file="top.jsp" %>
<!--------------------------top of page end------------------------------------------>

<!--------------------------edit area------------------------------------------->

<div class="push_Webcontent_bigbox">
<h1>Bulk Import Users</h1>

<c:if test="${not empty error}">
	<div style="color: red; padding: 10px; background-color: #ffcccc; margin: 10px 0;">
		<strong>Error:</strong> ${error}
	</div>
</c:if>

<c:if test="${not empty successCount || not empty failureCount}">
	<div style="padding: 10px; margin: 10px 0; background-color: #e8f5e9;">
		<h3>Import Results:</h3>
		<p><strong>Successfully imported:</strong> ${successCount} users</p>
		<p><strong>Failed:</strong> ${failureCount} users</p>
		<c:if test="${not empty errors}">
			<div style="color: red; margin-top: 10px;">
				<strong>Errors:</strong>
				<div style="max-height: 200px; overflow-y: auto; background-color: #fff; padding: 10px; border: 1px solid #ccc;">
					${errors}
				</div>
			</div>
		</c:if>
	</div>
</c:if>

<form id="form1" method="post" action="userAction!bulkImportUser.action" enctype="multipart/form-data">
	<ul class="Webcontent_dt">
		<li>
			<h2>CSV File:</h2>
			<input type="file" name="bulkImportFile" id="bulkImportFile" accept=".csv" class="text_time content_input"/>
			<p style="font-size: 12px; color: #666; margin-top: 5px;">
				Please upload a CSV file with the following format:
			</p>
		</li>
		<li>
			<h2>CSV Format:</h2>
			<div style="background-color: #f5f5f5; padding: 15px; border: 1px solid #ddd; margin: 10px 0;">
				<p><strong>Required columns (in order):</strong></p>
				<ol style="margin-left: 20px;">
					<li><strong>userPin</strong> - User PIN/ID (required)</li>
					<li><strong>userName</strong> - User Name (required)</li>
					<li><strong>userCard</strong> - Card Number (optional - can be left empty)</li>
					<li><strong>userPassword</strong> - Password (required)</li>
					<li><strong>deviceSn</strong> - Device Serial Number (required)</li>
					<li><strong>privilege</strong> - Privilege level (0=Ordinary, 2=Registrar, 6=Administrator, 10=Custom, 14=SuperAdmin, default: 0)</li>
					<li><strong>category</strong> - Category (0=Ordinary, 1=VIP, 2=Blacklist, default: 0)</li>
				</ol>
				<p style="margin-top: 10px;"><strong>Example CSV:</strong></p>
				<pre style="background-color: #fff; padding: 10px; border: 1px solid #ccc; overflow-x: auto;">userPin,userName,userCard,userPassword,deviceSn,privilege,category
1001,John Doe,1234567890,1234,TDBD250100333,0,0
1002,Jane Smith,,5678,TDBD250100333,0,1
1003,Bob Johnson,1122334455,9012,TDBD250100333,0,0</pre>
				<p style="margin-top: 10px; color: #666; font-size: 12px;">
					<strong>Note:</strong> The <code>userCard</code> field is optional. You can leave it empty (as shown in the second example above) or provide a card number.
				</p>
				<p style="margin-top: 10px; color: #666;">
					<strong>Note:</strong> The first row is treated as a header and will be skipped. 
					User images are not imported - they can be added later from the device.
				</p>
			</div>
		</li>
		<li>
			<h2>Available Devices:</h2>
			<div style="background-color: #f5f5f5; padding: 15px; border: 1px solid #ddd; margin: 10px 0;">
				<table>
					<c:forEach var="selDev" items="${devList}">
						<tr>
							<td style="padding: 5px;">${selDev.deviceSn} (${selDev.ipAddress})</td>
						</tr>
					</c:forEach>
				</table>
				<c:if test="${empty devList}">
					<p style="color: #999;">No devices available. Please add devices first.</p>
				</c:if>
			</div>
		</li>
	</ul>
	
	<div class="content_button_box">
		<input type="button" class="content_input2" value="Import Users" onclick="submitForm();"/>
		<input type="button" class="content_input3" value="Return" onclick="location.href='<%=basePath+"userAction!userList.action"%>'" />
	</div>
</form>
</div>

<!--------------------------edit area end------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>

