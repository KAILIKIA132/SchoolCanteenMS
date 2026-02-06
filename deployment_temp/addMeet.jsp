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
			<link href="css/css.css" rel="stylesheet" type="text/css" />
			<link href="css/menu.css" rel="stylesheet" media="screen" type="text/css" />
			<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
			<script src="js/zkadmin.js" type="text/javascript"></script>
			<script src="js/jquery.min.js" type="text/javascript"></script>
			<link rel="stylesheet" type="text/css" href="css/fbmodal.css" />
			<script type="text/javascript" src="js/showdate.js"></script>
			<script type="text/javascript">

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
					window.location.href = '<%=basePath+"meetAction!meetinfoList.action"%>';
				}

				function URLencode(sStr) {

					return escape(sStr)
						.replace(/\+/g, '%2B')
						.replace(/\"/g, '%22')
						.replace(/\'/g, '%27')
						.replace(/\//g, '%2F');

				}


				function operationNew() {
					var code = $("#code").val();
					if (null == code || "" == code) {
						alert('<s:text name="meet.hint.input.meet.code"/>');
						return;
					}
					var metName = $("#metName").val();
					if (null == metName || "" == metName) {
						alert('<s:text name="meet.hint.input.meet.name"/>');
						return;
					}
					var metStarSignTm = $("#metStarSignTm").val();
					if (null == metStarSignTm || "" == metStarSignTm) {
						alert('<s:text name="meet.hint.input.meet.metStarSignTm"/>');
						return;
					}
					var metLatSignTm = $("#metLatSignTm").val();
					if (null == metLatSignTm || "" == metLatSignTm) {
						alert('<s:text name="meet.hint.input.metLatSignTm"/>');
						return;
					}
					var earRetTm = $("#earRetTm").val();
					if (null == earRetTm || "" == earRetTm) {
						alert('<s:text name="meet.hint.input.earRetTm"/>');
						return;
					}
					var latRetTm = $("#latRetTm").val();
					if (null == latRetTm || "" == latRetTm) {
						alert('<s:text name="meet.hint.input.latRetTm"/>');
						return;
					}
					var metStrTm = $("#metStrTm").val();
					if (null == metStrTm || "" == metStrTm) {
						alert('<s:text name="meet.hint.input.metStrTm"/>');
						return;
					}
					var metEndTm = $("#metEndTm").val();
					if (null == metEndTm || "" == metEndTm) {
						alert('<s:text name="meet.hint.input.metEndTm"/>');
						return;
					}
					var deviceSn = $("input:radio[name=devSn]:checked").val();
					if (null == deviceSn) {
						alert('<s:text name="user.hint.select.device.sn"/>');
						return;
					}
					var url = "meetAction!editMeet.action?act=new&deviceSn=" + deviceSn + "&code="
						+ code + "&metName=" + metName + "&metStarSignTm=" + metStarSignTm + "&metLatSignTm=" + metLatSignTm + "&earRetTm=" + earRetTm
						+ "&latRetTm=" + latRetTm + "&metStrTm=" + metStrTm + "&metEndTm=" + metEndTm;
					//alert(url);
					dealData(encodeURI(url));
				}

				function operationEdit() {
					var metName = $("#metName").val();
					if (null == metName || "" == metName) {
						alert('<s:text name="meet.hint.input.meet.name"/>');
						return;
					}
					var metStarSignTm = $("#metStarSignTm").val();
					if (null == metStarSignTm || "" == metStarSignTm) {
						alert('<s:text name="meet.hint.input.meet.metStarSignTm"/>');
						return;
					}
					var metLatSignTm = $("#metLatSignTm").val();
					if (null == metLatSignTm || "" == metLatSignTm) {
						alert('<s:text name="meet.hint.input.metLatSignTm"/>');
						return;
					}
					var earRetTm = $("#earRetTm").val();
					if (null == earRetTm || "" == earRetTm) {
						alert('<s:text name="meet.hint.input.earRetTm"/>');
						return;
					}
					var latRetTm = $("#latRetTm").val();
					if (null == latRetTm || "" == latRetTm) {
						alert('<s:text name="meet.hint.input.latRetTm"/>');
						return;
					}
					var metStrTm = $("#metStrTm").val();
					if (null == metStrTm || "" == metStrTm) {
						alert('<s:text name="meet.hint.input.metStrTm"/>');
						return;
					}
					var metEndTm = $("#metEndTm").val();
					if (null == metEndTm || "" == metEndTm) {
						alert('<s:text name="meet.hint.input.metEndTm"/>');
						return;
					}
					var meetId = $("#meetId").val();
					var url = "meetAction!editMeet.action?act=edit&metName=" + metName
						+ "&metStarSignTm=" + metStarSignTm + "&metLatSignTm=" + metLatSignTm + "&earRetTm=" + earRetTm
						+ "&latRetTm=" + latRetTm + "&metStrTm=" + metStrTm + "&metEndTm=" + metEndTm
						+ "&meetId=" + meetId;
					dealData(encodeURI(url));
				}
			</script>

		</head>

		<body>
			<!--------------------------top of page------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------top of page end------------------------------------------>



				<!--------------------------edit area------------------------------------------->

				<div class="main_content">
					<c:choose>
						<c:when test="${act == 'new'}">
							<div class="form-container">
								<div class="form-header">
									<s:text name="meet.operate.add.meet" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.code" />:
									</label>
									<input type="text" id="code" class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.name" />:
									</label>
									<input type="text" id="metName" class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metStarSignTm" />:
									</label>
									<input type="text" id="metStarSignTm" class="form-control" placeholder="HH:mm" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metLatSignTm" />:
									</label>
									<input type="text" id="metLatSignTm" class="form-control" placeholder="HH:mm" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.earRetTm" />:
									</label>
									<input type="text" id="earRetTm" class="form-control" placeholder="HH:mm" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.latRetTm" />:
									</label>
									<input type="text" id="latRetTm" class="form-control" placeholder="HH:mm" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metStrTm" />:
									</label>
									<input type="text" id="metStrTm" class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metEndTm" />:
									</label>
									<input type="text" id="metEndTm" class="form-control" />
								</div>

								<div class="form-group" style="align-items: flex-start;">
									<label class="form-label" style="padding-top: 5px;">
										<s:text name="table.header.att.devicesn" />:
									</label>
									<div class="radio-group" style="padding-top: 5px;">
										<c:if test="${empty devList}">
											<span style="color: #666;">No devices available</span>
										</c:if>
										<c:forEach var="selDev" items="${devList}">
											<label class="radio-item">
												<input name="devSn" type="radio" value="${selDev.deviceSn}" />
												<span>${selDev.deviceSn} <span
														style="color:#888; font-size:0.9em;">(${selDev.ipAddress})</span></span>
											</label>
										</c:forEach>
									</div>
								</div>

								<div class="form-actions">
									<input type="submit" class="btn" value='<s:text name="sms.operate.add"/>'
										onclick="operationNew();" />
									<input type="button" class="btn content_input3"
										value='<s:text name="sms.operate.return"/>'
										onclick="location.href='<%=basePath+" meetAction!meetinfoList.action"%>'" />
								</div>
							</div>
						</c:when>
						<c:when test="${act == 'edit' }">
							<div class="form-container">
								<div class="form-header">
									<s:text name="user.operate.edit.user" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.code" />:
									</label>
									<input type="text" id="code" value="${meetInfo.code }" class="form-control"
										readonly="readonly" disabled="disabled" style="background:#f0f0f0; color:#666;"
										onfocus=this.blur() />
									<input type="hidden" id="meetId" value="${meetInfo.meetInfoId }" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.name" />:
									</label>
									<input type="text" id="metName" value="${meetInfo.metName }" class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metStarSignTm" />:
									</label>
									<input type="text" id="metStarSignTm" value="${meetInfo.metStarSignTm }"
										class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metLatSignTm" />:
									</label>
									<input type="text" id="metLatSignTm" value="${meetInfo.metLatSignTm }"
										class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.earRetTm" />:
									</label>
									<input type="text" id="earRetTm" value="${meetInfo.earRetTm }"
										class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.latRetTm" />:
									</label>
									<input type="text" id="latRetTm" value="${meetInfo.latRetTm }"
										class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metStrTm" />:
									</label>
									<input type="text" id="metStrTm" value="${meetInfo.metStrTm }"
										class="form-control" />
								</div>

								<div class="form-group">
									<label class="form-label">
										<s:text name="table.header.meet.metEndTm" />:
									</label>
									<input type="text" id="metEndTm" value="${meetInfo.metEndTm }"
										class="form-control" />
								</div>

								<div class="form-actions">
									<input type="submit" class="btn" value='<s:text name="dev.edit.commit"/>'
										onclick="operationEdit();" />
									<input type="button" class="btn content_input3"
										value='<s:text name="sms.operate.return"/>'
										onclick="location.href='<%=basePath+" meetAction!meetinfoList.action"%>'" />
								</div>
							</div>
						</c:when>
					</c:choose>
				</div>

				<!--------------------------edit area end------------------------------------------->
				<%@ include file="bottom.jsp" %>
		</body>

</html>