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
					var actionName = "advAction!advList.action";
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


					/* var addPersMeetDialogHtml='<div class="win_box">'
						+'<h2><s:text name="meet.operate.add.pers"/></h2><hr />'
						+'<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
						+'<th align="center"><s:text name="table.header.user.name"/></th>'
						+'<c:forEach var="user" items="${userList}">'
						+'<tr><td>'
						+'<input name="name" type="checkbox" value="${user.name}">${user.name}</input>'
						+'</td></tr>'
						+'</c:forEach></table></div>';	 */


					function checkSelectItem() {
						var temp = "";
						$("input:checkbox[name=advId]:checked").each(function (i) {

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

					});

					function operateAdv(operate) {
						var temp = "";
						$("input:checkbox[name=advId]:checked").each(function (i) {

							temp += ($(this).val() + ",");
						});
						if (temp.length > 0) {
							temp = temp.substring(0, temp.length - 1);
							var url = "";
							if ("deleteAdvServ" == operate) {
								url = "advAction!deleteAdvServ.action?advId=" + temp;
							} else if ("sendAdvDev" == operate) {
								url = "advAction!sendAdvDev.action?advId=" + temp;
							} else if ("deleteAdvDev" == operate) {
								url = "advAction!deleteAdvDev.action?advId=" + temp;
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
						var devSelAllChk = document.getElementsByName("advSelAll");
						var snChk = document.getElementsByName("advId");
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

					function editAdv(advId) {
						if (null == advId || "" == advId) {
							return;
						}
						var url = "advAction!newAdv.action?act=edit&advtId=" + advId;
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
												<li><a onclick="operateAdv('deleteAdvServ')" href="#">
														<s:text name="adv.operate.deleteadv.server" />
													</a></li>
											</ul>
										</li>
										<li>
											<a href="#">
												<s:text name="user.operate.category.data.cmd" />
											</a>
											<ul>
												<li><a onclick="operateAdv('sendAdvDev')" href="#">
														<s:text name="adv.operate.sendadv2device" />
													</a></li>
												<%-- <li><a id="addPersMeetOpen"
														onclick="location.href='javascript:void()'" href="#">
														<s:text name="meet.operate.add.pers" />
													</a>
										</li> --%>
										<li><a onclick="operateAdv('deleteAdvDev')" href="#">
												<s:text name="adv.operate.deleteadv.device" />
											</a></li>
									</ul>
								</li>
							</ul>
							</li>
							</ul>
						</div>
						<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script>

						<input type="button" align="right" class="input_add l"
							value='<s:text name="adv.operate.add.adv"/>' onclick="location.href='<%=basePath+"advAction!newAdv.action?act=new"%>'"/>
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
						<div class="zk_Search_bd l" style="width:28px">
							<%-- <input class="input_sou" id="searchCode" type="text" value='${byCode21}'
								onfocus="this.value=''" onblur="if(!value){value=defaultValue;}" /> --%>
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
						<th><input type="checkbox" name="advSelAll" value="checkbox" onclick="selAll()" /></th>
						<th>
							<s:text name="table.header.adv.type" />
						</th>
						<th>
							<s:text name="table.header.adv.fileName" />
						</th>
						<th>
							<s:text name="table.header.adv.url" />
						</th>
						<th>
							<s:text name="table.header.adv.device" />
						</th>
					</tr>
					<c:forEach var="adv" items="${advList}">
						<tr>
							<td><input type="checkbox" name="advId" value="${adv.advId}" /></td>
							<c:if test="${adv.type == '1' }">　<td>
									<s:text name="adv.file.type.picture"></s:text>
								</td>
							</c:if>
							<c:if test="${adv.type == '2' }">　<td>
									<s:text name="adv.file.type.video"></s:text>
								</td>
							</c:if>
							<td>${adv.fileName}</td>
							<td>${adv.url}</td>
							<td>${adv.deviceSn}</td>
						</tr>
					</c:forEach>
				</table>

				<%@ include file="pagenition.jsp" %>
					<!--------------------------List end------------------------------------------->
					<%@ include file="bottom.jsp" %>
						<div class="test1">

						</div>
		</body>

</html>