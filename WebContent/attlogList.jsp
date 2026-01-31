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
			<%@ include file="includejs.jsp" %>
				<script type="text/javascript">
					var actionName = "attAction!attLogList.action";
					$(function () {
						/*Using an id to close the Modal*/
						$("#close").click(function () {
							$(".test1").fbmodal({ close: true });
						});


						$("#open1").click(function () {
							$(".test1").fbmodal({
								okaybutton: true,
								cancelbutton: true,
								buttons: true,
								opacity: 0.35,
								fadeout: true,
								overlayclose: true,
								modaltop: "25%",
								modalwidth: "220"
							});
						});


						$("#close").click(function () {
							$(".test2").fbmodal({ close: true });
						});
						$("#open2").click(function () {
							$(".test2").fbmodal({
								okaybutton: true,
								cancelbutton: true,
								buttons: true,
								opacity: 0.35,
								fadeout: true,
								overlayclose: true,
								modaltop: "25%",
								modalwidth: "220"
							});
						});


					});

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

					function operateAtt(operate) {
						var temp = "";
						$("input:checkbox[name=logId]:checked").each(function (i) {

							temp += ($(this).val() + ",");
						});
						var url = "";
						if (temp.length > 0 && "delById" == operate) {
							temp = temp.substring(0, temp.length - 1);
							url = "attAction!delById.action?logId=" + temp;
							dealData(url);
						} else if ("clearAll" == operate) {
							url = "attAction!clearAll.action";
							dealData(url);
						} else if ("clearAllPhoto" == operate) {
							url = "attAction!clearAllPhoto.action";
							dealData(url);
						} else {
							alert("<s:text name='device.operate.warring1'/>");
						}
					}

					function getCond() {
						var devSn = $("#seledDev").val();
						var userPin = $("#searchUserPin").val();
						if ('<s:text name="user.search.by.device"/>' == devSn) {
							devSn = '';
						}
						if ('<s:text name="user.serach.input.name.or.userpin"/>' == userPin) {
							userPin = '';
						}
						var url = "deviceSn=" + devSn + "&userPin=" + userPin;
						return url;
					}

					function search() {
						var url = "?" + getCond();
						location.href = actionName + url;
					}


					function selAll() {
						var devSelAllChk = document.getElementsByName("attSelAll");
						var snChk = document.getElementsByName("logId");
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

			<!--------------------------top of page------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------top of page end------------------------------------------>

				<!--------------------------search area------------------------------------------>
				<div class="push_Search_box">
					<div class="l zkoperation_box">
						<div class="zk_nav">
							<ul id="jsddm">
								<li id="zkoperation_navMenu1">
									<a href="#">
										<s:text name="att.operate.items" />
									</a>
									<ul>
										<li>
											<a href="#">
												<s:text name="att.operate.category.server" />
											</a>
											<ul>
												<li><a href="#" onclick="operateAtt('delById')">
														<s:text name="att.operate.delete.selected" />
													</a></li>
												<li><a href="#" onclick="operateAtt('clearAll')">
														<s:text name="att.operate.clearall" />
													</a></li>
												<li><a href="#" onclick="operateAtt('clearAllPhoto')">
														<s:text name="att.operate.clearall.att.photo" />
													</a></li>
											</ul>
										</li>
									</ul>
								</li>
							</ul>
						</div>
						<!--<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script> 
	<input type="button" align="right" class="input_Export l" value='<s:text name="att.export"/>' onclick="location.href='javascript:void()'" id="open1"/>
-->
					</div>

					<div class="l zk_Search">
						<div class="nice-select1 l" name="nice-select">
							<input type="text" id="seledDev" value='${byDeviceSn}' readonly>
							<ul>
								<c:forEach var="selDev" items="${devList}">
									<li>${selDev.deviceSn}</li>
								</c:forEach>
							</ul>
						</div>
						<div class="zk_Search_bd l">
							<input class="input_sou" id="searchUserPin" type="text" value='${byUserPin21}'
								onfocus="this.value=''" onblur="if(!value){value=defaultValue;}" />
							<input class="input_ico r" type="submit" name="Submit" value="" onclick="search()" />
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

				<!--------------------------search area end------------------------------------------>

				<!--------------------------åè¡¨å¼å§------------------------------------------->
				<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
					<tr>
						<th><input type="checkbox" name="attSelAll" value="checkbox" onclick="selAll()" /></th>
						<th>
							<s:text name="table.header.att.user.pin" />
						</th>
						<th>
							<s:text name="table.header.att.user.name" />
						</th>
						<th>
							<s:text name="table.header.att.verify.type" />
						</th>
						<th>
							<s:text name="table.header.att.verify.time" />
						</th>
						<th>
							<s:text name="table.header.att.state" />
						</th>
						<th>
							<s:text name="table.header.att.workcode" />
						</th>
						<th>
							<s:text name="table.header.att.devicesn" />
						</th>
					</tr>
					<c:forEach var="att" items="${attList}">
						<tr>
							<td><input type="checkbox" name="logId" value="${att.attLogId}" /></td>
							<td>${att.userPin}</td>
							<td>${att.userName}</td>
							<td>${att.verifyTypeStr}</td>
							<td>${att.verifyTime}</td>
							<td>${att.statusStr}</td>
							<td>${att.workCode}</td>
							<td>${att.deviceSn}</td>
						</tr>
					</c:forEach>
				</table>

				<%@ include file="pagenition.jsp" %>
					<!--------------------------åè¡¨ç»æ------------------------------------------->
					<%@ include file="bottom.jsp" %>

		</body>

</html>