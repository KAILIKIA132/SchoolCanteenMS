<!DOCTYPE html
	PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
	<%@ include file="include.jsp" %>

		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
			<title>PUSH SDK</title>
			<%@ include file="includejs.jsp" %>
				<script type="text/javascript">
					var actionName = "smsAction!smsList.action";
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
						//timeoutId=setTimeout("window.location.reload();", 5000);
						window.location.reload();
					}


					var sendSmsDialogHtml = '<div class="win_box">'
						+ '<h2><s:text name="sms.operate.send.device"/></h2><hr />'
						+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+ '<th align="center"><s:text name="table.header.device.sn"/></th>'
						+ '<c:forEach var="dev" items="${devList}">'
						+ '<tr><td>'
						+ '<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
						+ '</td></tr>'
						+ '</c:forEach></table></div>';

					function procSendSms(v) {
						if (1 == v) {
							var tempValue = "";
							$("input:checkbox[name=smsId]:checked").each(function (i) {

								tempValue += ($(this).val() + ",");
							});
							tempValue = tempValue.substring(0, tempValue.length - 1);
							var temp1 = tempValue.split(",");
							var temp = "";
							var temp3;
							var temp4 = "";
							for (temp3 in temp1) {
								var a = temp1[temp3].split(":");
								temp += a[0] + ",";
								temp4 += a[1] + ",";
							}
							temp = temp.substring(0, temp.length - 1);

							var obj = document.getElementsByName("devSn");
							var destSn = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) destSn += obj[i].value + ',';
							}
							if (destSn.length > 0) {
								destSn = destSn.substring(0, destSn.length - 1);
								url = "smsAction!sendToDev.action?smsId=" + temp + "&destSn=" + destSn;
								dealData(url);
							}
							else {
								alert('<s:text name="dialog.hint.please.select.device"/>');
							}

						}
						window.location.reload();
					}

					var sendUserSmsDialogHtml = '<div class="win_box">'
						+ '<h2><s:text name="sms.operate.send.user"/></h2><hr />'
						+ '<s:text name="dialog.send.user.sms.hint.user.pin"/>：<br/>' +
						'<input id="userPin" type="text"/>'
						+ '<br/><br/>'
						+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+ '<th align="center"><s:text name="table.header.device.sn"/></th>'
						+ '<c:forEach var="dev" items="${devList}">'
						+ '<tr><td>'
						+ '<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
						+ '</td></tr>'
						+ '</c:forEach></table></div>';

					function procSendUserSms(v) {
						if (1 == v) {
							var tempValue = "";
							$("input:checkbox[name=smsId]:checked").each(function (i) {

								tempValue += ($(this).val() + ",");
							});
							tempValue = tempValue.substring(0, tempValue.length - 1);
							var temp1 = tempValue.split(",");
							var temp = "";
							var temp3;
							var temp4 = "";
							for (temp3 in temp1) {
								var a = temp1[temp3].split(":");
								temp += a[0] + ",";
								temp4 += a[1] + ",";
							}
							temp = temp.substring(0, temp.length - 1);

							var obj = document.getElementsByName("devSn");
							var destSn = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) destSn += obj[i].value + ',';
							}
							var userPin = $(".content>.win_box>#userPin").val();
							if (destSn.length <= 0) {
								alert('<s:text name="dialog.hint.please.select.device"/>');
							} else if ('' == userPin) {
								alert('<s:text name="dialog.send.user.sms.hint.user.pin"/>');
							} else {
								destSn = destSn.substring(0, destSn.length - 1);
								url = "smsAction!sendUserSms.action?smsId=" + temp + "&userPin=" + userPin + "&destSn=" + destSn;
								dealData(url);
							}
						}
						window.location.reload();
					}

					var deleteSmsDialogHtml = '<div class="win_box">'
						+ '<h2><s:text name="sms.operate.delete.device"/></h2><hr />'
						+ '<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+ '<th align="center"><s:text name="table.header.device.sn"/></th>'
						+ '<c:forEach var="dev" items="${devList}">'
						+ '<tr><td>'
						+ '<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
						+ '</td></tr>'
						+ '</c:forEach></table></div>';

					function procDeleteSms(v) {
						if (1 == v) {
							var tempValue = "";
							$("input:checkbox[name=smsId]:checked").each(function (i) {

								tempValue += ($(this).val() + ",");
							});
							tempValue = tempValue.substring(0, tempValue.length - 1);
							var temp1 = tempValue.split(",");
							var temp = "";
							var temp3;
							var temp4 = "";
							for (temp3 in temp1) {
								var a = temp1[temp3].split(":");
								temp += a[0] + ",";
								temp4 += a[1] + ",";
							}
							temp = temp.substring(0, temp.length - 1);

							var obj = document.getElementsByName("devSn");
							var destSn = '';
							for (var i = 0; i < obj.length; i++) {
								if (obj[i].checked) destSn += obj[i].value + ',';
							}
							if (destSn.length > 0) {
								destSn = destSn.substring(0, destSn.length - 1);
								url = "smsAction!deleteSms.action?smsId=" + temp + "&destSn=" + destSn;
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
						$("input:checkbox[name=smsId]:checked").each(function (i) {

							temp += ($(this).val() + ",");
						});
						if (temp.length <= 0) {
							alert("<s:text name='device.operate.warring1'/>");
							return -1;
						}
						return 0;
					}

					function checkPrivateSms() {
						var tempValue = "";
						$("input:checkbox[name=smsId]:checked").each(function (i) {

							tempValue += ($(this).val() + ",");
						});
						if (tempValue.length > 0) {
							tempValue = tempValue.substring(0, tempValue.length - 1);
							var temp1 = tempValue.split(",");
							var temp = "";
							var temp3;
							var temp4 = "";
							for (temp3 in temp1) {
								var a = temp1[temp3].split(":");
								temp += a[0] + ",";
								temp4 += a[1] + ",";
							}
							temp = temp.substring(0, temp.length - 1);
							if (temp4.indexOf("253") >= 0) {
								alert('<s:text name="sms.hint.is.only.private"/>');
								return -2;
							}
						} else {
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

						$("#sendSmsOpen").bind("click", function () {
							if (0 != checkSelectItem()) {
								return;
							}

							$('.test1').append(sendSmsDialogHtml)
								.fbmodal(pushDialog, procSendSms);
						});

						$("#sendUserSmsOpen").bind("click", function () {
							if (0 != checkPrivateSms()) {
								return;
							}

							$('.test1').append(sendUserSmsDialogHtml)
								.fbmodal(pushDialog, procSendUserSms);
						});
						$("#deleteSmsOpen").bind("click", function () {
							if (0 != checkSelectItem()) {
								return;
							}

							$('.test1').append(deleteSmsDialogHtml)
								.fbmodal(pushDialog, procDeleteSms);
						});
					});



					function operation(operate) {
						var tempValue = "";
						$("input:checkbox[name=smsId]:checked").each(function (i) {

							tempValue += ($(this).val() + ",");
						});
						if (tempValue.length > 0) {
							tempValue = tempValue.substring(0, tempValue.length - 1);
							var temp1 = tempValue.split(",");
							var temp = "";
							var temp3;
							var temp4 = "";
							for (temp3 in temp1) {
								var a = temp1[temp3].split(":");
								temp += a[0] + ",";
								temp4 += a[1] + ",";
							}
							temp = temp.substring(0, temp.length - 1);
							var url = "";
							if ("deleteSmsServ" == operate) {
								url = "smsAction!deleteSmsServ.action?smsId=" + temp;
							}
							dealData(url);
						} else {
							alert("<s:text name='device.operate.warring1'/>");
						}
					}

					function addNewSms() {
						dealData("smsAction!newSms.action");
					}

					function getCond() {
						return "";
					}


					function selAll() {
						var devSelAllChk = document.getElementsByName("smsSelAll");
						var snChk = document.getElementsByName("smsId");
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
										<s:text name="sms.operate.items" />
									</a>
									<ul>
										<li>
											<a href="#">
												<s:text name="sms.operate.category.server" />
											</a>
											<ul>
												<li><a href="#" onclick="operation('deleteSmsServ')">
														<s:text name="sms.operate.delete.selected" />
													</a></li>
											</ul>
										</li>
										<li>
											<a href="#">
												<s:text name="sms.operate.category.data.cmd" />
											</a>
											<ul>
												<li><a href="#" id="sendSmsOpen"
														onclick="location.href='javascript:void()'">
														<s:text name="sms.operate.send.device" />
													</a></li>
												<li><a href="#" id="sendUserSmsOpen"
														onclick="location.href='javascript:void()'">
														<s:text name="sms.operate.send.user" />
													</a></li>
												<li><a href="#" id="deleteSmsOpen"
														onclick="location.href='javascript:void()'">
														<s:text name="sms.operate.delete.device" />
													</a></li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</div>
						<!--<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script> 
	--><input type="button" align="right" class="input_add l" value='<s:text name="sms.operate.add.sms"/>'
							onclick="location.href='<%=basePath+" smsAction!newSms.action"%>'"/>

					</div><!--
<div class="l zk_Search">
  
    <div class="nice-select l" name="nice-select">
    <input type="text" value='<s:text name="sms.search.by.type"/>' readonly>
    <ul>
      <li><s:text name="sms.type.private"/></li>
      <li><s:text name="sms.type.public"/></li>

    </ul>
  </div>

<script>	
$('[name="nice-select"]').click(function(e){
	$('[name="nice-select"]').find('ul').hide();
	$(this).find('ul').show();
	e.stopPropagation();
});
$('[name="nice-select"] li').hover(function(e){
	$(this).toggleClass('on');
	e.stopPropagation();
});
$('[name="nice-select"] li').click(function(e){
	var val = $(this).text();
	var dataVal = $(this).attr("data-value");
	$(this).parents('[name="nice-select"]').find('input').val(val);
	$('[name="nice-select"] ul').hide();

	//alert($(this).parents('[name="nice-select"]').find('input').val());
});
$(document).click(function(){
	$('[name="nice-select"] ul').hide();
});
</script>

	<div class="zk_Search_bd l"><input class="input_sou" name="" type="text" value="è¾å¥äººåå,å·¥å·" onfocus="this.value=''" onblur="if(!value){value=defaultValue;}"/>
 <input class="input_ico r" type="submit" name="Submit" value="" />
    </div>

  </div>
-->
				</div>

				<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

				<!--------------------------åè¡¨å¼å§------------------------------------------->
				<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
					<tr>
						<th><input type="checkbox" name="smsSelAll" value="checkbox" onclick="selAll()" /></th>
						<th>
							<s:text name="table.header.sms.start.time" />
						</th>
						<th>
							<s:text name="table.header.sms.valid.minutes" />
						</th>
						<th>
							<s:text name="table.header.sms.type" />
						</th>
						<th>
							<s:text name="table.header.sms.content" />
						</th>
					</tr>
					<c:forEach var="sms" items="${list}">
						<tr>
							<td><input type="checkbox" name="smsId" value="${sms.id}:${sms.smsType}" /></td>
							<td>${sms.startTime}</td>
							<td>${sms.validMinutes}</td>
							<td>${sms.smsTypeStr}</td>
							<td>${sms.smsContent}</td>
						</tr>
					</c:forEach>
				</table>

				<%@ include file="pagenition.jsp" %>
					<!--------------------------åè¡¨ç»æ------------------------------------------->
					<%@ include file="bottom.jsp" %>
						<div class="test1">

						</div>
		</body>

</html>