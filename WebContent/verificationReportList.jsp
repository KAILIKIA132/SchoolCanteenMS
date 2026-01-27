<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="includejs.jsp"%>
<title>Verification Reports</title>
<style>
.status-success { color: #28a745; font-weight: bold; }
.status-failed { color: #dc3545; font-weight: bold; }
.stats-box { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 5px; }
.stats-item { display: inline-block; margin-right: 30px; }
.filter-box { background: #e9ecef; padding: 15px; margin: 10px 0; border-radius: 5px; }
</style>
</head>
<body>
<div class="container">
	<h2>API Verification Reports</h2>
	
	<!-- Statistics -->
	<div class="stats-box">
		<div class="stats-item">
			<strong>Total:</strong> <span style="color: #007bff; font-size: 18px;">${totalCount}</span>
		</div>
		<div class="stats-item">
			<strong>Successful:</strong> <span class="status-success" style="font-size: 18px;">${successCount}</span>
		</div>
		<div class="stats-item">
			<strong>Failed:</strong> <span class="status-failed" style="font-size: 18px;">${failedCount}</span>
		</div>
	</div>
	
	<!-- Filters -->
	<div class="filter-box">
		<form method="get" action="<%=basePath%>verificationReportAction!reportList.action" id="filterForm">
			<div style="margin-bottom: 10px;">
				<label>Status:</label>
				<select name="status" id="statusFilter" onchange="document.getElementById('filterForm').submit();">
					<option value="ALL" ${statusFilter == 'ALL' ? 'selected' : ''}>All</option>
					<option value="SUCCESS" ${statusFilter == 'SUCCESS' ? 'selected' : ''}>Success</option>
					<option value="FAILED" ${statusFilter == 'FAILED' ? 'selected' : ''}>Failed</option>
				</select>
				
				<label style="margin-left: 20px;">User PIN:</label>
				<input type="text" name="userPin" value="${userPinFilter}" placeholder="Filter by User PIN" />
			</div>
			
			<div style="margin-top: 10px;">
				<label>Start Date:</label>
				<input type="date" name="startDate" value="${startDateFilter}" style="margin-left: 5px;" />
				
				<label style="margin-left: 20px;">End Date:</label>
				<input type="date" name="endDate" value="${endDateFilter}" style="margin-left: 5px;" />
			</div>
			
			<div style="margin-top: 10px;">
				<input type="submit" value="Filter" class="input_add" />
				<a href="<%=basePath%>verificationReportAction!reportList.action" class="input_add" style="margin-left: 10px;">Clear</a>
			</div>
		</form>
	</div>
	
	<!-- Export Section -->
	<div class="filter-box" style="margin-top: 10px;">
		<h3 style="margin-top: 0;">Export to Excel</h3>
		<form method="get" action="<%=basePath%>verificationReportAction!exportExcel.action" id="exportForm" target="_blank">
			<!-- Preserve current filters -->
			<input type="hidden" name="status" value="${statusFilter}" />
			<input type="hidden" name="userPin" value="${userPinFilter}" />
			<input type="hidden" name="startDate" value="${startDateFilter}" />
			<input type="hidden" name="endDate" value="${endDateFilter}" />
			
			<div style="margin-bottom: 10px;">
				<strong>Select Columns to Export:</strong>
			</div>
			<div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; margin-bottom: 10px;">
				<label><input type="checkbox" name="columns" value="id" checked /> ID</label>
				<label><input type="checkbox" name="columns" value="userPin" checked /> User PIN</label>
				<label><input type="checkbox" name="columns" value="userName" checked /> User Name</label>
				<label><input type="checkbox" name="columns" value="studentId" checked /> Student ID</label>
				<label><input type="checkbox" name="columns" value="verificationTime" checked /> Verification Time</label>
				<label><input type="checkbox" name="columns" value="apiCallTime" checked /> API Call Time</label>
				<label><input type="checkbox" name="columns" value="mealType" checked /> Meal Type</label>
				<label><input type="checkbox" name="columns" value="status" checked /> Status</label>
				<label><input type="checkbox" name="columns" value="responseCode" checked /> Response Code</label>
				<label><input type="checkbox" name="columns" value="responseMessage" /> Response Message</label>
				<label><input type="checkbox" name="columns" value="errorMessage" /> Error Message</label>
				<label><input type="checkbox" name="columns" value="apiUrl" /> API URL</label>
			</div>
			<div>
				<button type="button" onclick="selectAllColumns()" class="input_add" style="margin-right: 10px;">Select All</button>
				<button type="button" onclick="deselectAllColumns()" class="input_add" style="margin-right: 10px;">Deselect All</button>
				<input type="submit" value="Export to Excel" class="input_add" style="background-color: #28a745; color: white; font-weight: bold;" />
			</div>
		</form>
	</div>
	
	<script>
	function selectAllColumns() {
		var checkboxes = document.querySelectorAll('input[name="columns"]');
		for (var i = 0; i < checkboxes.length; i++) {
			checkboxes[i].checked = true;
		}
	}
	
	function deselectAllColumns() {
		var checkboxes = document.querySelectorAll('input[name="columns"]');
		for (var i = 0; i < checkboxes.length; i++) {
			checkboxes[i].checked = false;
		}
	}
	</script>
	
	<!-- Report Table -->
	<table border="1" cellpadding="5" cellspacing="0" class="push_tab1" style="width: 100%;">
		<thead>
			<tr style="background-color: #343a40; color: white;">
				<th>ID</th>
				<th>User PIN</th>
				<th>User Name</th>
				<th>Student ID</th>
				<th>Verification Time</th>
				<th>API Call Time</th>
				<th>Meal Type</th>
				<th>Status</th>
				<th>Response Code</th>
				<th>Response/Error</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${reportList != null && !reportList.isEmpty()}">
					<c:forEach var="report" items="${reportList}">
						<tr>
							<td>${report.reportId}</td>
							<td>${report.userPin}</td>
							<td>${fn:escapeXml(report.userName != null ? report.userName : '')}</td>
							<td>${report.studentId}</td>
							<td>${report.verificationTime}</td>
							<td>
								<fmt:setTimeZone value="Africa/Nairobi" />
								<fmt:formatDate value="${report.apiCallTime}" pattern="yyyy-MM-dd HH:mm:ss" timeZone="Africa/Nairobi" />
							</td>
							<td>${report.mealType}</td>
							<td>
								<c:choose>
									<c:when test="${report.status == 'SUCCESS'}">
										<span class="status-success">✓ SUCCESS</span>
									</c:when>
									<c:otherwise>
										<span class="status-failed">✗ FAILED</span>
									</c:otherwise>
								</c:choose>
							</td>
							<td>${report.responseCode != null ? report.responseCode : '-'}</td>
							<td style="max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${report.status == 'SUCCESS' ? fn:escapeXml(report.responseMessage) : fn:escapeXml(report.errorMessage)}">
								<c:choose>
									<c:when test="${report.status == 'SUCCESS'}">
										<c:choose>
											<c:when test="${report.responseMessage != null && fn:length(report.responseMessage) > 50}">
												${fn:escapeXml(fn:substring(report.responseMessage, 0, 50))}...
											</c:when>
											<c:otherwise>
												${fn:escapeXml(report.responseMessage)}
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<span style="color: #dc3545;">
											<c:choose>
												<c:when test="${report.errorMessage != null && fn:length(report.errorMessage) > 50}">
													${fn:escapeXml(fn:substring(report.errorMessage, 0, 50))}...
												</c:when>
												<c:otherwise>
													${fn:escapeXml(report.errorMessage)}
												</c:otherwise>
											</c:choose>
										</span>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="9" style="text-align: center; padding: 20px;">
							No verification reports found.
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	
	<!-- Pagination -->
	<c:if test="${totalPages > 1}">
		<div style="margin-top: 20px; text-align: center;">
			<c:if test="${currentPage > 1}">
				<a href="<%=basePath%>verificationReportAction!reportList.action?page=${currentPage - 1}&status=${statusFilter}&userPin=${userPinFilter}&startDate=${startDateFilter}&endDate=${endDateFilter}" class="input_add">Previous</a>
			</c:if>
			<span style="margin: 0 20px;">Page ${currentPage} of ${totalPages}</span>
			<c:if test="${currentPage < totalPages}">
				<a href="<%=basePath%>verificationReportAction!reportList.action?page=${currentPage + 1}&status=${statusFilter}&userPin=${userPinFilter}&startDate=${startDateFilter}&endDate=${endDateFilter}" class="input_add">Next</a>
			</c:if>
		</div>
	</c:if>
	
	<div style="margin-top: 20px;">
		<a href="<%=basePath%>userAction!userList.action" class="input_add">Back to User List</a>
	</div>
</div>
</body>
</html>



