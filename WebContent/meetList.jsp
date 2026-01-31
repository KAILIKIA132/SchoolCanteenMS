<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
	<%@ include file="include.jsp" %>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<%@ include file="includejs.jsp" %>
				<title>
					<s:text name="push.web.demo.name" />
				</title>
				<script type="text/javascript">
					var actionName = "meetAction!meetinfoList.action";
					function dealData(url) {
						$.ajax({
							type: "POST",
							url: url,
							dataType: "json",
							async: false,
						});
						window.location.reload();
					}


					var clearMeetDialogHtml = '<div class="win_box">'
						+ '<h2><s:text name="meet.operate.clearmeet.device"/></h2><hr />'
						+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+ '<th align="center"><s:text name="table.header.device.sn"/></th>'
						+ '<c:forEach var="dev" items="${devList}">'
						+ '<tr><td>'
						+ '<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
						+ '</td></tr>'
						+ '</c:forEach></table></div>';

					var clearPersMeetDialogHtml = '<div class="win_box">'
						+ '<h2><s:text name="meet.operate.clearpersmeet.device"/></h2><hr />'
						+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+ '<th align="center"><s:text name="table.header.device.sn"/></th>'
						+ '<c:forEach var="dev" items="${devList}">'
						+ '<tr><td>'
						+ '<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
						+ '</td></tr>'
						+ '</c:forEach></table></div>';

					function procClearMeet(v) {
						if (1 == v) {
							var url = "";

							var obj = document.getElementsByName("devSn");
							//alert(obj.length);
							var destSn = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) destSn += obj[i].value + ',';
							}
							if (destSn.length > 0) {
								destSn = destSn.substring(0, destSn.length - 1);
								url = "meetAction!clearMeetDev.action?destSn=" + destSn;
								dealData(url);
							}
							else {
								alert('<s:text name="dialog.hint.please.select.device"/>');
							}

						}
						window.location.reload();
					}

					function procClearPersMeet(v) {
						if (1 == v) {
							var url = "";

							var obj = document.getElementsByName("devSn");
							//alert(obj.length);
							var destSn = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) destSn += obj[i].value + ',';
							}
							if (destSn.length > 0) {
								destSn = destSn.substring(0, destSn.length - 1);
								url = "meetAction!clearPersMeetDev.action?destSn=" + destSn;
								dealData(url);
							}
							else {
								alert('<s:text name="dialog.hint.please.select.device"/>');
							}

						}
						window.location.reload();
					}

					function addPersMeet(v) {
						if (1 == v) {
							var temp = "";
							var url = "";
							$("input:checkbox[name=meetId]:checked").each(function (i) {
								temp += ($(this).val() + ",");
							});
							temp = temp.substring(0, temp.length - 1);

							var obj = document.getElementsByName("userPin");
							//alert(obj.length);
							var userPins = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) userPins += obj[i].value + ',';
							}
							if (userPins.length > 0) {
								userPins = userPins.substring(0, userPins.length - 1);
								url = "meetAction!addPersMeet.action?meetId=" + temp + "&userPins=" + userPins;
								dealData(url);
							}
							else {
								alert('<s:text name="dialog.hint.please.select.device"/>');
							}

						}
						window.location.reload();
					}

					function checkSelectItem() {
						var temp = "";
						$("input:checkbox[name=meetId]:checked").each(function (i) {

							temp += ($(this).val() + ",");
						});
						if (temp.length <= 0) {
							alert("<s:text name='device.operate.warring1'/>");
							return -1;
						}
						return 0;
					}

					$(function () {

						/*Using an id to close the Modal*/
						$("#close").click(function () {
							$(".test1").fbmodal({ close: true });
						});

						$("#clearMeetOpen").bind("click", function () {

							$('.test1').append(clearMeetDialogHtml)
								.fbmodal(pushDialog, procClearMeet);

						});

						$("#clearPersMeetOpen").bind("click", function () {

							$('.test1').append(clearPersMeetDialogHtml)
								.fbmodal(pushDialog, procClearPersMeet);

						});


						$("#addPersMeetOpen").bind("click", function () {
							if (0 != checkSelectItem()) {
								return;
							}

							var temp = "";
							var url = "";
							$("input:checkbox[name=meetId]:checked").each(function (i) {
								temp += ($(this).val() + ",");
							});
							temp = temp.substring(0, temp.length - 1);

							//点击添加按钮，弹出页面。
							$.ajax({
								type: "POST",
								url: "meetAction!getUserByDev.action?meetId=" + temp,
								dataType: "json",
								async: false,

								success: function (data) {
									var tempStr = "";
									for (var i = 0; i < data.length; i++) {
										if (data[i].meetCode == null || data[i].meetCode == "" || data[i].meetCode == "undefined") {
											tempStr += '<tr><td>'
												+ '<input name="userPin" type="checkbox" value="' + data[i].userPin + '">' + data[i].userName + '</input>'
												+ '</td></tr>';
										} else {
											tempStr += '<tr><td>'
												+ '<input name="userPin" type="checkbox" checked="checked" value="' + data[i].userPin + '">' + data[i].userName + '</input>'
												+ '</td></tr>';
										}
										//alert(data[i].userName);
									}
									//alert(data);
									//window.location.reload();
									var addPersMeetDialogHtml = "";
									addPersMeetDialogHtml = '<div class="win_box">'
										+ '<h2><s:text name="meet.operate.add.pers"/></h2><hr />'
										+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
										+ '<th align="center"><s:text name="table.header.user.name"/></th>'
										+ tempStr
										+ '</table></div>';
									$('.test1').append(addPersMeetDialogHtml)
										.fbmodal(pushDialog, addPersMeet);
								},
								error: function (XMLHttpRequest, textStatus, errorThrown) {
									alert("The operation failed, please try again...");
									window.location.reload();
								}

							});

							/* $('.test1').append(addPersMeetDialogHtml)
										  .fbmodal(pushDialog,addPersMeet); */
						});
					});


					function operateMeet(operate) {
						var temp = "";
						$("input:checkbox[name=meetId]:checked").each(function (i) {

							temp += ($(this).val() + ",");
						});
						if (temp.length > 0) {
							temp = temp.substring(0, temp.length - 1);
							var url = "";
							if ("deleteMeetServ" == operate) {
								url = "meetAction!deleteMeetServ.action?meetId=" + temp;
							} else if ("sendMeetDev" == operate) {
								url = "meetAction!sendMeetDev.action?meetId=" + temp;
							} else if ("deleteMeetDev" == operate) {
								url = "meetAction!deleteMeetDev.action?meetId=" + temp;
							}
							dealData(url);
						} else {
							alert("<s:text name='device.operate.warring1'/>");
						}
					}

					function getCond() {
						var devSn = $("#seledDev").val();
						var code = $("#searchCode").val();
						if ('<s:text name="meet.search.by.device"/>' == devSn) {
							devSn = '';
						}
						if ('<s:text name="meet.serach.input.name.or.code"/>' == code) {
							code = '';
						}
						var url = "deviceSn=" + devSn + "&code=" + code;
						return url;
					}

					function search() {
						var url = "?" + getCond();
						location.href = actionName + url;
					}

					function selAll() {
						var devSelAllChk = document.getElementsByName("meetSelAll");
						var snChk = document.getElementsByName("meetId");
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

					function editMeet(meetId) {
						if (null == meetId || "" == meetId) {
							return;
						}
						var url = "meetAction!newMeet.action?act=edit&meetId=" + meetId;
						location.href = url;
					}
				</script>
		</head>

		<body>

			<!--------------------------å¤´é¨å¼å§------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------å¤´é¨ç»æ------------------------------------------>

				<!--------------------------å¤´é¨æä½åè½å¼å§------------------------------------------>
				<div class="push_Search_box">
					<div class="l zkoperation_box">
						<div class="zk_nav">
							<ul id="jsddm">
								<li id="zkoperation_navMenu1">
									<a href="#">
										<s:text name="meet.operate.items" />
									</a>
									<ul>
										<li>
											<a href="#">
												<s:text name="user.operate.category.server" />
											</a>
											<ul>
												<li><a onclick="operateMeet('deleteMeetServ')" href="#">
														<s:text name="meet.operate.deletemeet.server" />
													</a></li>
											</ul>
										</li>
										<li>
											<a href="#">
												<s:text name="user.operate.category.data.cmd" />
											</a>
											<ul>
												<li><a onclick="operateMeet('sendMeetDev')" href="#">
														<s:text name="meet.operate.sendmeet2device" />
													</a></li>
												<li><a id="addPersMeetOpen" onclick="location.href='javascript:void()'"
														href="#">
														<s:text name="meet.operate.add.pers" />
													</a></li>
												<li><a onclick="operateMeet('deleteMeetDev')" href="#">
														<s:text name="meet.operate.deletemeet.device" />
													</a></li>
												<li><a id="clearMeetOpen" onclick="location.href='javascript:void()'"
														href="#">
														<s:text name="meet.operate.clearmeet.device" />
													</a></li>
												<li><a id="clearPersMeetOpen"
														onclick="location.href='javascript:void()'" href="#">
														<s:text name="meet.operate.clearpersmeet.device" />
													</a></li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</div>
						<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script>

						<input type="button" align="right" class="input_add l"
							value='<s:text name="meet.operate.add.meet"/>' onclick="location.href='<%=basePath+"meetAction!newMeet.action?act=new"%>'"/>
					</div>

					<div class="l zk_Search">
						<div class="nice-select1 l" name="nice-select">
							<input type="text" id="seledDev" value='${byDeviceSn}' readonly />
							<ul>
								<c:forEach var="selDev" items="${devList}">
									<li>${selDev.deviceSn}</li>
								</c:forEach>
							</ul>
						</div>
						<div class="zk_Search_bd l">
							<input class="input_sou" id="searchCode" type="text" value='${byCode21}'
								onfocus="this.value=''" onblur="if(!value){value=defaultValue;}" />
							<input class="input_ico r" type="submit" name="Submit" value="" onclick="search()" />
							<!--
		<input class="input_ico_Alone" type="submit" name="Submit" value="" onclick="search()"/>
		-->
						</div>

						<script>
							$('[name="nice-select"]').click(function (e) {
								$('[name="nice-select"]').find('ul').hide();
								$(this).find('ul').show();
								e.stopPropagation();
							});
							$('[name="nice-select"] li').hover(function (e) {
								$(this).toggleClass('on');
								e.stopPropagation();
							});
							$('[name="nice-select"] li').click(function (e) {
								var val = $(this).text();
								var dataVal = $(this).attr("data-value");
								$(this).parents('[name="nice-select"]').find('input').val(val);
								$('[name="nice-select"] ul').hide();

								//alert($(this).parents('[name="nice-select"]').find('input').val());
							});
							$(document).click(function () {
								$('[name="nice-select"] ul').hide();
							});
						</script>
					</div>
				</div>

				<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

				<!--------------------------List------------------------------------------->
				<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
					<tr>
						<th><input type="checkbox" name="meetSelAll" value="checkbox" onclick="selAll()" /></th>
						<th>
							<s:text name="table.header.meet.code" />
						</th>
						<th>
							<s:text name="table.header.user.devicesn" />
						</th>
						<th>
							<s:text name="table.header.meet.name" />
						</th>
						<th>
							<s:text name="table.header.meet.metStarSignTm" />
						</th>
						<th>
							<s:text name="table.header.meet.metLatSignTm" />
						</th>
						<th>
							<s:text name="table.header.meet.earRetTm" />
						</th>
						<th>
							<s:text name="table.header.meet.latRetTm" />
						</th>
						<th>
							<s:text name="table.header.meet.metStrTm" />
						</th>
						<th>
							<s:text name="table.header.meet.metEndTm" />
						</th>
					</tr>
					<c:forEach var="meet" items="${meetInfoList}">
						<tr>
							<td><input type="checkbox" name="meetId" value="${meet.meetInfoId}" /></td>
							<td><a href="#" onclick="editMeet('${meet.meetInfoId}')">${meet.code}</a></td>
							<td>${meet.deviceSn}</td>
							<td>${meet.metName}</td>
							<td>${meet.metStarSignTm}</td>
							<td>${meet.metLatSignTm}</td>
							<td>${meet.earRetTm}</td>
							<td>${meet.latRetTm}</td>
							<td>${meet.metStrTm}</td>
							<td>${meet.metEndTm}</td>
						</tr>
					</c:forEach>
				</table>

				<%@ include file="pagenition.jsp" %>
					<!--------------------------List end------------------------------------------->
					<%@ include file="bottom.jsp" %>
						<div class="test1">

						</div>
						<div class="">

						</div>
		</body>

</html>