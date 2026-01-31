<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
	<%@ include file="include.jsp" %>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<meta http-equiv="refresh" content="60" />
			<title>
				<s:text name="push.web.demo.name" />
			</title>
			<%@ include file="includejs.jsp" %>
				<%@ include file="deviceDialogs.jsp" %>
					<script type="text/javascript">
						var actionName = "deviceAction!deviceList.action";
						function dealData(url) {
							$.ajax({
								type: "POST",
								url: url,
								dataType: "json",
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
							window.location.reload();
						}


						function checkSelectItem() {
							var temp = "";
							$("input:checkbox[name=sn]:checked").each(function (i) {

								temp += ($(this).val() + ",");
							});
							if (temp.length <= 0) {
								alert("<s:text name='device.operate.warring1'/>");
								return -1;
							}
							return 0;
						}

						function checkOnlyOneSelectItem() {
							var temp = "";
							$("input:checkbox[name=sn]:checked").each(function (i) {

								temp += ($(this).val() + ",");
							});
							if (temp.length <= 0) {
								alert("<s:text name='device.operate.warring1'/>");
								return -1;
							}
							temp = temp.substring(0, temp.length - 1);
							if (temp.indexOf(",") > 0) {
								alert('<s:text name="device.operate.hint.only.one.item"/>');
								return -2;
							}
							return 0;
						}

						$(function () {
							console.log("--------------------------------------------------");
							console.log("Device List Page Loaded");

							var deviceCount = 0;
							// Iterate over server-side list to count items for client-side logging
							<c:forEach items="${deviceInfoList}" var="dev">
								deviceCount++;
								console.log("Device: ${dev.deviceSn} - ${dev.deviceName} - IP: ${dev.ipAddress}");
							</c:forEach>

							console.log("Total devices fetched from server: " + deviceCount);
							console.log("--------------------------------------------------");

							/*Using an id to close the Modal*/
							$("#close").click(function () {
								$(".test1").fbmodal({ close: true });
							});

							$("#syncTimeZoneOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(querySyncTimeZoneDialogHtml)
									.fbmodal(pushDialog, procSyncingTimeZone);
							});

							$("#queryAttLogOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(queryAttLogDialogHtml)
									.fbmodal(pushDialog, procQueryAttLog);
							});

							$("#toNewDeviceOpen").bind("click", function () {
								if (0 != checkOnlyOneSelectItem()) {
									return;
								}
								$('.test1').append(toNewDeviceDialogHtml)
									.fbmodal(pushDialog, procToNewDevice);
							});

							$("#queryAttPhotoOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(queryAttPhotoDialogHtml)
									.fbmodal(pushDialog, procQueryAttPhoto);
							});

							$("#queryUserInfoOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(queryUserDialogHtml)
									.fbmodal(pushDialog, procQueryUserInfo);
							});

							$("#queryFingerTmpOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(queryFingerTmpDialogHtml)
									.fbmodal(pushDialog, procQueryFingerTmp);
							});

							$("#verifyLogOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(verifyLogDialogHtml)
									.fbmodal(pushDialog, procVerifyLog);
							});

							$("#getFileOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(getFileDialogHtml)
									.fbmodal(pushDialog, procGetFile);
							});

							$("#putFileOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(putFileDialogHtml)
									.fbmodal(pushDialog, procPutFile);
							});
							$("#setOptionOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(setOptionDialogHtml)
									.fbmodal(pushDialog, procSetOption);
							});
							$("#enrollFpOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(enrollFPDialogHtml)
									.fbmodal(pushDialog, procEnrollFPc);
							});
							$("#shellOpen").bind("click", function () {
								if (0 != checkSelectItem()) {
									return;
								}

								$('.test1').append(shellDialogHtml)
									.fbmodal(pushDialog, procShell);
							});
						});


						function operateDevice(operate) {
							var temp = "";
							$("input:checkbox[name=sn]:checked").each(function (i) {

								temp += ($(this).val() + ",");
							});
							if (temp.length > 0) {
								temp = temp.substring(0, temp.length - 1);
								var url = "";
								if ("clearAllData" == operate) {
									url = "deviceAction!clearAllData.action?sn=" + temp;
								} else if ("check" == operate) {
									url = "deviceAction!checkDeviceData.action?sn=" + temp;
								} else if ("restore" == operate) {
									url = "deviceAction!restoreUserInfo.action?sn=" + temp;
								} else if ("checkNew" == operate) {
									url = "deviceAction!checkDeviceData.action?checkNew=yes&sn=" + temp;
								} else if ("info" == operate) {
									url = "deviceAction!checkDeviceInfo.action?sn=" + temp;
								} else if ("deleteServer" == operate) {
									if (confirm("<s:text name='delete.operate.confirm'/>")) {
										url = "deviceAction!deleteServer.action?sn=" + temp;
									} else {
										return;
									}
								} else if ("deleteDevice" == operate) {
									url = "deviceAction!deleteDevice.action?sn=" + temp;
								} else if ("clearAttLog" == operate) {
									url = "deviceAction!clearAttLog.action?sn=" + temp;
								} else if ("clearPhoto" == operate) {
									url = "deviceAction!clearPhoto.action?sn=" + temp;
								} else if ("rebootDevice" == operate) {
									url = "deviceAction!rebootDevice.action?sn=" + temp;
								} else if ("reloadOption" == operate) {
									url = "deviceAction!reloadOption.action?sn=" + temp;
								} else if ("logData" == operate) {
									url = "deviceAction!logData.action?sn=" + temp;
								} else if ("unlock" == operate) {
									url = "deviceAction!unlock.action?sn=" + temp;
								} else if ("unalarm" == operate) {
									url = "deviceAction!unalarm.action?sn=" + temp;
								} else if ("syncDevice" == operate) {
									url = "deviceAction!syncDevice.action?sn=" + temp;
								} else if ("getAllUsers" == operate) {
									if (confirm("This will retrieve all users from the selected device(s) and sync them to the webapp. Continue?")) {
										url = "deviceAction!getAllUsersFromDevice.action?sn=" + temp;
									} else {
										return;
									}
								}
								dealData(url);
							} else {
								alert("<s:text name='device.operate.warring1'/>");
							}
						}

						function editDevice(sn) {
							if (null == sn || "" == sn) {
								return;
							}
							var url = "deviceAction!editDevice.action?sn=" + sn;
							location.href = url;
						}

						function getCond() {
							return "";
						}

						function selAll() {
							var devSelAllChk = document.getElementsByName("devSelAll");
							var snChk = document.getElementsByName("sn");
							if (devSelAllChk[0].checked == false) {
								for (var i = 0; i < snChk.length; i++) {
									snChk[i].checked = false;
								}
							} else {
								for (var i = 0; i < snChk.length; i++) {
									snChk[i].checked = true;
								}
							}
						}
					</script>
		</head>

		<body>
			<!--------------------------page top start------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------page top end------------------------------------------>

				<!--------------------------header operate start------------------------------------------>
				<div class="push_Search_box">
					<div class="l zkoperation_box">
						<div class="zk_nav">
							<ul id="jsddm">
								<li id="zkoperation_navMenu1"><a href="#">
										<s:text name="device.operate.items" />
									</a>
									<ul>
										<li>
											<a href="#">
												<s:text name="device.operate.category.server" />
											</a>
											<ul>
												<li><a href="#" onclick="operateDevice('deleteDevice')">
														<s:text name="device.operate.deletedevice" />
													</a></li>
												<li><a href="#" onclick="operateDevice('deleteServer')">
														<s:text name="device.operate.delete.server.data" />
													</a></li>
												<li><a href="#" onclick="operateDevice('syncDevice')">
														<s:text name="device.operate.syncDevice" />
													</a></li>
												<%-- <li><a href="#" onclick="operateDevice('syncTimeZone')">
														<s:text name="device.operate.syncTimeZone" />
													</a>
										</li> --%>
										<li><a href="#" id="syncTimeZoneOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.syncTimeZone" />
											</a></li>
									</ul>
								</li>
								<li>
									<a href="#">
										<s:text name="device.operate.category.data.cmd" />
									</a>
									<ul>
										<li><a href="#" id="toNewDeviceOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.backupdata.tootherdevice" />
											</a></li>
										<li><a href="#" onclick="operateDevice('restore')">
												<s:text name="device.operate.restoreuserinfo" />
											</a></li>
										<li><a href="#" onclick="operateDevice('getAllUsers')">Get All Users from
												Device</a></li>
										<li><a href="#" id="queryAttLogOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.query.att.log" />
											</a></li>
										<li><a href="#" id="queryAttPhotoOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.query.att.photo" />
											</a></li>
										<li><a href="#" id="queryUserInfoOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.query.user.info" />
											</a></li>
										<li><a href="#" id="queryFingerTmpOpen"
												onclick="location.href='javascript:void()'">
												<s:text name="device.operate.query.finger.tmp" />
											</a></li>
									</ul>
								</li>
								<li>
									<a href="#">
										<s:text name="device.operate.category.clear.cmd" />
									</a>
									<ul>
										<li><a href="#" onclick="operateDevice('clearAllData')">
												<s:text name="device.operate.clearalldata" />
											</a></li>
										<li><a href="#" onclick="operateDevice('clearAttLog')">
												<s:text name="device.operate.clearattlog" />
											</a></li>
										<li><a href="#" onclick="operateDevice('clearPhoto')">
												<s:text name="device.operate.clarattpic" />
											</a></li>
									</ul>
								</li>
								<li>
									<a href="#">
										<s:text name="device.operate.category.check.data.cmd" />
									</a>
									<ul>
										<li><a href="#" onclick="operateDevice('check')">
												<s:text name="device.operate.reuploadalldata" />
											</a></li>
										<%-- <li><a href="#" onclick="operateDevice('checkNew')">
												<s:text name="device.operate.checkanduploaddata" />
											</a>
								</li> --%>
								<li><a href="#" onclick="operateDevice('logData')">
										<s:text name="device.operate.check.and.send.new.data" />
									</a></li>
								<li><a href="#" id="verifyLogOpen" onclick="location.href='javascript:void()'">
										<s:text name="device.operate.verify.att.log" />
									</a></li>
							</ul>
							</li>
							<li>
								<a href="#">
									<s:text name="device.operate.category.file.cmd" />
								</a>
								<ul>
									<li><a href="#" id="getFileOpen" onclick="location.href='javascript:void()'">
											<s:text name="devcie.operate.get.device.file" />
										</a></li>
									<li><a href="#" id="putFileOpen" onclick="location.href='javascript:void()'">
											<s:text name="device.operate.put.file.dev" />
										</a></li>
								</ul>
							</li>
							<li>
								<a href="#">
									<s:text name="device.operate.category.config.cmd" />
								</a>
								<ul>
									<li><a href="#" onclick="operateDevice('info')">
											<s:text name="device.operate.refreshdata" />
										</a></li>
									<li><a href="#" id="setOptionOpen" onclick="location.href='javascript:void()'">
											<s:text name="device.operate.set.option" />
										</a></li>
									<li><a href="#" onclick="operateDevice('reloadOption')">
											<s:text name="device.operate.refresh.dev.option" />
										</a></li>
								</ul>
							</li>
							<li>
								<a>
									<s:text name="device.operate.category.control.cmd" />
								</a>
								<ul>
									<li><a href="#" onclick="operateDevice('rebootDevice')">
											<s:text name="device.operate.rebootdevice" />
										</a></li>
									<li><a href="#" onclick="operateDevice('unlock')">
											<s:text name="device.operate.ac.unlock" />
										</a></li>
									<li><a href="#" onclick="operateDevice('unalarm')">
											<s:text name="device.operate.ac.unalarm" />
										</a></li>
								</ul>
							</li>
							<li>
								<a href="#">
									<s:text name="device.operate.category.remote.enroll.cmd" />
								</a>
								<ul>
									<li><a href="#" id="enrollFpOpen" onclick="location.href='javascript:void()'">
											<s:text name="device.operate.enroll.fp" />
										</a></li>
								</ul>
							</li>
							<li>
								<a href="#">
									<s:text name="device.operate.category.other.cmd" />
								</a>
								<ul>
									<li><a href="#" id="shellOpen" onclick="location.href='javascript:void()'">
											<s:text name="device.operate.shell" />
										</a></li>
								</ul>
							</li>
							</ul>
							</li>
							</ul>
						</div>
						<%--<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script>
							--%>
					</div>
				</div>

				<!--------------------------header operate end------------------------------------------>

				<!--------------------------list start------------------------------------------->
				<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
					<tr>

						<th><input type="checkbox" name="devSelAll" value="checkbox" onclick="selAll()" /></th>
						<%-- <th>
							<s:text name="HelloWorld" />
							</th>--%>
							<th>
								<s:text name="table.header.device.sn" />
							</th>
							<th>
								<s:text name="table.header.device.ip" />
							</th>
							<th>
								<s:text name="table.header.device.name" />
							</th>
							<%--<th>
								<s:text name="table.header.device.alias" />
								</th>
								--%><th>
									<s:text name="table.header.device.state" />
								</th><%-- <th>
									<s:text name="table.header.device.transtimes" />
									</th>
									<th>
										<s:text name="table.header.device.transinterval" />
									</th>
									--%><th>
										<s:text name="table.header.device.lastactivity" />
									</th>
									<%--<th>
										<s:text name="table.header.device.firmwareversion" />
										</th>
										--%><th>
											<s:text name="table.header.device.usercount" />
										</th>
										<th>
											<s:text name="table.header.device.act.user.count" />
										</th>
										<th>
											<s:text name="table.header.device.fpcount" />
										</th>
										<th>
											<s:text name="table.header.device.act.fp.count" />
										</th>
										<th>
											<s:text name="table.header.device.face.count" />
										</th>
										<th>
											<s:text name="table.header.device.act.face.count" />
										</th>

										<th>
											<s:text name="table.header.device.palmFlag" />
										</th>
										<th>
											<s:text name="table.header.device.maskFlag" />
										</th>
										<th>
											<s:text name="table.header.device.tempReading" />
										</th>
										<th>
											<s:text name="table.header.device.transcount" />
										</th>
										<th>
											<s:text name="table.header.device.act.trans.count" />
										</th>
										<th>
											<s:text name="table.header.device.dev.fun" />
										</th>



										<%--<th>
											<s:text name="table.header.data.maintain" />
											</th>
											--%>
					</tr>
					<c:forEach var="device" items="${deviceInfoList}">
						<tr>
							<td><input type="checkbox" name="sn" value="${device.deviceSn}" /></td>
							<td><a href="#" onclick="editDevice('${device.deviceSn}')">${device.deviceSn}</a></td>
							<td>${device.ipAddress}</td>
							<td>${device.deviceName}</td>
							<%--<td>${device.aliasName}</td>
								--%><td>${device.state}</td>
								<%--<td>${device.transTimes}</td>
									<td>${device.transInterval}</td>
									--%><td>${device.lastActivity}</td>
									<%--<td>${device.firmwareVersion}</td>
										--%><td>${device.userCount}</td>
										<td>${device.actUserCount}</td>
										<td>${device.fpCount}</td>
										<td>${device.actFpCount}</td>
										<td>${device.faceCount}</td>
										<td>${device.actFaceCount}</td>
										<td>${device.palm}</td>
										<td>${device.mask}</td>
										<td>${device.temperature}</td>
										<td>${device.transCount}</td>
										<td>${device.actTransCount}</td>
										<td>${device.devFuns}</td>
										<%-- <td>
											<a href="#">L</a> <!-- att log -->
											<a href="#">E</a> <!-- employee -->
											<a href="#">U</a> <!-- user -->
											<a href="#">C</a>
											<a href="#">R</a>
											<a href="#">P</a> <!-- photo -->
											</td>
											--%>
						</tr>
					</c:forEach>
				</table>

				<%@ include file="pagenition.jsp" %>
					<!--------------------------list end------------------------------------------->

					<%@ include file="bottom.jsp" %>

						<div class="test1">

						</div>
		</body>

</html>