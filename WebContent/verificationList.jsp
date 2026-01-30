<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
	<%@ include file="include.jsp" %>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<!-- <meta http-equiv="refresh" content="3" /> Removed to prevent session loss/redirects -->
			<title>Verification Responses -
				<s:text name="push.web.demo.name" />
			</title>
			<%@ include file="includejs.jsp" %>
				<style type="text/css">
					.verification-success {
						background-color: #d4edda;
					}

					.verification-failed {
						background-color: #f8d7da;
					}

					.verification-pending {
						background-color: #fff3cd;
					}
				</style>
		</head>

		<body>
			<!--------------------------Header Start------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------Header End------------------------------------------>

				<!--------------------------Content Start------------------------------------------>
				<div class="push_Search_box">
					<div class="Monitor_Record l"><strong>Real-time Verification Responses</strong> - Auto-refreshing
						every 3 seconds</div>
				</div>

				<!--------------------------List Start------------------------------------------->
				<table border="0" cellpadding="0" cellspacing="0" class="push_tab2" id="verificationTable">
					<thead>
						<tr>
							<th>Time</th>
							<th>Device</th>
							<th>User PIN</th>
							<th>User Name</th>
							<th>Verification Type</th>
							<th>Status</th>
							<th>Mask</th>
							<th>Temperature</th>
							<th>Work Code</th>
						</tr>
					</thead>
					<tbody id="verificationBody">
						<c:choose>
							<c:when test="${empty verificationList}">
								<tr id="no-data-row">
									<td colspan="9" style="text-align: center; padding: 20px;">
										No verification responses yet. When devices scan biometrics, they will appear
										here.
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach var="verification" items="${verificationList}">
									<tr class="<c:choose>
					<c:when test=" ${verification.status==0 || verification.status==1}">verification-success</c:when>
										<c:when test="${verification.status >= 2}">verification-failed</c:when>
										<c:otherwise>verification-pending</c:otherwise>
						</c:choose>">
						<td>${verification.verifyTime}</td>
						<td>${verification.deviceSn}</td>
						<td>${verification.userPin}</td>
						<td>${verification.userName != null ? verification.userName : 'N/A'}</td>
						<td>${verification.verifyTypeStr != null ? verification.verifyTypeStr : verification.verifyType}
						</td>
						<td>${verification.statusStr != null ? verification.statusStr : verification.status}</td>
						<td>
							<c:choose>
								<c:when test="${verification.maskFlag == 1}">Yes</c:when>
								<c:when test="${verification.maskFlag == 0}">No</c:when>
								<c:otherwise>N/A</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when
									test="${verification.temperatureReading != null && verification.temperatureReading != '' && verification.temperatureReading != '0'}">
									${verification.temperatureReading}°C
								</c:when>
								<c:otherwise>N/A</c:otherwise>
							</c:choose>
						</td>
						<td>${verification.workCode}</td>
						</tr>
						</c:forEach>
						</c:otherwise>
						</c:choose>
					</tbody>
				</table>

				<script type="text/javascript">
					$(document).ready(function () {
						// Poll for new data every 3 seconds
						setInterval(function () {
							fetchVerifications();
						}, 3000);
					});

					function fetchVerifications() {
						$.ajax({
							url: 'attAction!getVerificationsJson.action',
							type: 'GET',
							dataType: 'json',
							success: function (data) {
								if (data && data.success) {
									updateVerificationTable(data.verifications);
								}
							},
							error: function (xhr, status, error) {
								console.error("Failed to fetch verifications:", error);
							}
						});
					}

					function updateVerificationTable(verifications) {
						var tbody = $("#verificationBody");
						tbody.empty();

						if (!verifications || verifications.length === 0) {
							tbody.append('<tr id="no-data-row"><td colspan="9" style="text-align: center; padding: 20px;">No verification responses yet. When devices scan biometrics, they will appear here.</td></tr>');
							return;
						}

						$.each(verifications, function (i, v) {
							var rowClass = "verification-pending";
							var status = parseInt(v.status);
							if (status === 0 || status === 1) {
								rowClass = "verification-success";
							} else if (status >= 2) {
								rowClass = "verification-failed";
							}

							var mask = "N/A";
							if (v.mask === 1) mask = "Yes";
							else if (v.mask === 0) mask = "No";

							var temp = "N/A";
							if (v.temperature && v.temperature !== '' && v.temperature !== '0') {
								temp = v.temperature + "°C";
							}

							var row = '<tr class="' + rowClass + '">' +
								'<td>' + (v.timestamp || '') + '</td>' +
								'<td>' + (v.deviceSn || '') + '</td>' +
								'<td>' + (v.userPin || '') + '</td>' +
								'<td>' + (v.userName || 'N/A') + '</td>' +
								'<td>' + (v.verifyTypeStr || v.verifyType) + '</td>' +
								'<td>' + (v.statusStr || v.status) + '</td>' +
								'<td>' + mask + '</td>' +
								'<td>' + temp + '</td>' +
								'<td>' + (v.workCode || '') + '</td>' +
								'</tr>';

							tbody.append(row);
						});
					}
				</script>
				<!--------------------------List End------------------------------------------->

				<%@ include file="bottom.jsp" %>
		</body>

</html>