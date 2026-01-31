<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
	<%@ include file="include.jsp" %>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<title>
				<s:text name="push.web.demo.name" />
			</title>
			<link href="css/css.css?v=2" rel="stylesheet" type="text/css" />
			<link href="css/menu.css?v=2" rel="stylesheet" media="screen" type="text/css" />
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

				<div class="main_content">
					<form id="form1" method="post" action="userAction!bulkImportUser.action"
						enctype="multipart/form-data">
						<div class="form-container">
							<div class="form-header">
								Bulk Import Users
							</div>

							<c:if test="${not empty error}">
								<div
									style="color: #721c24; background-color: #f8d7da; border-color: #f5c6cb; padding: .75rem 1.25rem; margin-bottom: 1rem; border: 1px solid transparent; border-radius: .25rem;">
									<strong>Error:</strong> ${error}
								</div>
							</c:if>

							<c:if test="${not empty successCount || not empty failureCount}">
								<div
									style="color: #155724; background-color: #d4edda; border-color: #c3e6cb; padding: .75rem 1.25rem; margin-bottom: 1rem; border: 1px solid transparent; border-radius: .25rem;">
									<h3 style="margin-top:0;">Import Results:</h3>
									<p><strong>Successfully imported:</strong> ${successCount} users</p>
									<p><strong>Failed:</strong> ${failureCount} users</p>
									<c:if test="${not empty errors}">
										<div style="color: #721c24; margin-top: 10px;">
											<strong>Errors details:</strong>
											<div
												style="max-height: 200px; overflow-y: auto; background-color: #fff; padding: 10px; border: 1px solid #dee2e6; font-size: 0.9em; margin-top:5px;">
												${errors}
											</div>
										</div>
									</c:if>
								</div>
							</c:if>

							<div class="form-group">
								<label class="form-label">
									CSV File:
								</label>
								<input type="file" name="bulkImportFile" id="bulkImportFile" accept=".csv"
									class="form-control" style="padding: 3px;" />
								<p style="font-size: 12px; color: #6c757d; margin-top: 5px;">
									Please upload a CSV file with the following format:
								</p>
							</div>

							<div class="form-group">
								<label class="form-label">
									CSV Format:
								</label>
								<div
									style="background-color: #f8f9fa; padding: 15px; border: 1px solid #dee2e6; border-radius: 4px;">
									<p><strong>Required columns (in order):</strong></p>
									<ol style="margin-left: 20px; color: #495057;">
										<li><strong>userPin</strong> - User PIN/ID (required)</li>
										<li><strong>userName</strong> - User Name (required)</li>
										<li><strong>userCard</strong> - Card Number (optional)</li>
										<li><strong>userPassword</strong> - Password (required)</li>
										<li><strong>deviceSn</strong> - Device Serial Number (required)</li>
										<li><strong>privilege</strong> - Level (0=Ord, 2=Reg, 6=Admin, 14=Super, etc)
										</li>
										<li><strong>category</strong> - Cat (0=Ord, 1=VIP, 2=Blacklist)</li>
									</ol>
									<p style="margin-top: 10px; margin-bottom: 5px;"><strong>Example CSV:</strong></p>
									<div
										style="background-color: #fff; padding: 10px; border: 1px solid #ced4da; overflow-x: auto; font-family: monospace; font-size: 12px; color: #212529;">
										userPin,userName,userCard,userPassword,deviceSn,privilege,category
										1001,John Doe,1234567890,1234,TDBD250100333,0,0
										1002,Jane Smith,,5678,TDBD250100333,0,1
									</div>
									<p style="margin-top: 10px; color: #6c757d; font-size: 12px; margin-bottom: 0;">
										<strong>Note:</strong> First row is header. Images not imported.
									</p>
								</div>
							</div>

							<div class="form-group">
								<label class="form-label">
									Available Devices:
								</label>
								<div
									style="background-color: #f8f9fa; padding: 10px; border: 1px solid #dee2e6; border-radius: 4px; max-height: 150px; overflow-y: auto;">
									<c:if test="${empty devList}">
										<p style="color: #6c757d; margin:0;">No devices available.</p>
									</c:if>
									<c:if test="${not empty devList}">
										<table style="width: 100%; border-collapse: collapse;">
											<c:forEach var="selDev" items="${devList}">
												<tr>
													<td style="padding: 5px; border-bottom: 1px solid #e9ecef;">
														<span style="font-weight: bold;">${selDev.deviceSn}</span>
														<span
															style="color: #6c757d; font-size: 0.9em;">(${selDev.ipAddress})</span>
													</td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
								</div>
							</div>

							<div class="form-actions">
								<input type="button" class="btn" value="Import Users" onclick="submitForm();" />
								<input type="button" class="btn content_input3" value="Return"
									onclick="location.href='<%=basePath+" userAction!userList.action"%>'" />
							</div>

						</div>
					</form>
				</div>

				<!--------------------------edit area end------------------------------------------->
				<%@ include file="bottom.jsp" %>
		</body>

</html>