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

				function dealData(url) {
					$("#form1").submit();
				}

				function URLencode(sStr) {

					return escape(sStr)
						.replace(/\+/g, '%2B')
						.replace(/\"/g, '%22')
						.replace(/\'/g, '%27')
						.replace(/\//g, '%2F');

				}


				function operationNew() {
					var userPin = $("#userPin").val();
					if (null == userPin || "" == userPin) {
						alert('<s:text name="user.hint.input.user.pin"/>');
						return;
					}
					var userName = $("#userName").val();
					if (null == userName || "" == userName) {
						alert('<s:text name="user.hint.input.user.name"/>');
						return;
					}
					var userCard = $("#userCard").val();
					if (null == userCard || "" == userCard) {
						alert('<s:text name="user.hint.input.user.card"/>');
						return;
					}
					var userPassword = $("#userPassword").val();
					if (null == userPassword || "" == userPassword) {
						alert('<s:text name="user.hint.input.password"/>');
						return;
					}
					var userPasswordCom = $("#userPasswordCom").val();
					if (userPassword != userPasswordCom) {
						alert('<s:text name="user.hint.confirm.password"/>');
						return;
					}
					var deviceSn = $("input:radio[name=deviceSn]:checked").val();
					if (null == deviceSn) {
						alert('<s:text name="user.hint.select.device.sn"/>');
						return;
					}
					dealData();
				}

				function operationEdit() {
					var userName = $("#userName").val();
					if (null == userName || "" == userName) {
						alert('<s:text name="user.hint.input.user.name"/>');
						return;
					}
					var userId = $("#userId").val();
					var url = "userAction!editUser.action?act=edit&userName=" + userName + "&userId="
						+ userId;
					dealData();
				}
			</script>

		</head>

		<body>
			<!--------------------------top of page‹------------------------------------------>
			<%@ include file="top.jsp" %>
				<!--------------------------top of page end------------------------------------------>



				<!--------------------------edit area‹------------------------------------------->

				<div class="main_content">
					<form id="form1" method="post" action="userAction!editUser.action" enctype="multipart/form-data">
						<c:choose>
							<c:when test="${act == 'new'}">
								<input type="hidden" name="act" value="new" />
								<div class="form-container">
									<div class="form-header">
										<s:text name="user.operate.add.user" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.pin" />:
										</label>
										<input type="text" name="userPin" id="userPin" class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.name" />:
										</label>
										<input type="text" name="userName" id="userName" class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.card" />:
										</label>
										<input type="text" name="userCard" id="userCard" class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.password" />:
										</label>
										<input type="password" name="userPassword" id="userPassword"
											class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.confirm.password" />:
										</label>
										<input type="password" id="userPasswordCom" class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.pic" />:
										</label>
										<input type="file" name="userPic" id="userPic" class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.privilege" />:
										</label>
										<select name="privilege" id="privilege" class="form-control">
											<option value="0" selected="selected">
												<s:text name="user.add.user.privilege.ordinary" />
											</option>
											<option value="2">
												<s:text name="user.add.user.privilege.registrar" />
											</option>
											<option value="6">
												<s:text name="user.add.user.privilege.administrator" />
											</option>
											<option value="10">
												<s:text name="user.add.user.privilege.custom" />
											</option>
											<option value="14">
												<s:text name="user.add.user.privilege.superadmin" />
											</option>
										</select>
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.category" />:
										</label>
										<select name="category" id="category" class="form-control">
											<option value="0" selected="selected">
												<s:text name="user.add.user.category.ordinary" />
											</option>
											<option value="1">
												<s:text name="user.add.user.category.vip" />
											</option>
											<option value="2">
												<s:text name="user.add.user.category.blacklist" />
											</option>
										</select>
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.att.devicesn" />:
										</label>
										<div class="radio-group">
											<c:forEach var="selDev" items="${devList}">
												<label class="radio-item">
													<input name="deviceSn" type="radio" value="${selDev.deviceSn}" />
													${selDev.deviceSn}(${selDev.ipAddress})
												</label>
											</c:forEach>
										</div>
									</div>

									<div class="form-actions">
										<input type="button" class="btn" value='<s:text name="sms.operate.add"/>'
											onclick="operationNew();" />
										<input type="button" class="btn content_input3"
											value='<s:text name="sms.operate.return"/>'
											onclick="location.href='<%=basePath+" userAction!userList.action"%>'" />
									</div>
								</div>
							</c:when>
							<c:when test="${act == 'edit' }">
								<input type="hidden" name="act" value="edit" />
								<div class="form-container">
									<div class="form-header">
										<s:text name="user.operate.edit.user" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.pin" />:
										</label>
										<input type="text" id="userPin" value="${userInfo.userPin }"
											class="form-control" readonly="readonly" disabled="disabled"
											style="background:#e9ecef" onfocus=this.blur() />
										<input type="hidden" id="userId" name="userId" value="${userInfo.userId }" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.name" />:
										</label>
										<input type="text" name="userName" id="userName" value="${userInfo.name }"
											class="form-control" />
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.privilege" />:
										</label>
										<select name="privilege" id="privilege" class="form-control">
											<option value="0" <c:if test="${userInfo.privilege=='0' }">
												selected="selected"</c:if> >
												<s:text name="user.add.user.privilege.ordinary" />
											</option>
											<option value="2" <c:if test="${userInfo.privilege=='2' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.privilege.registrar" />
											</option>
											<option value="6" <c:if test="${userInfo.privilege=='6' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.privilege.administrator" />
											</option>
											<option value="10" <c:if test="${userInfo.privilege=='10' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.privilege.custom" />
											</option>
											<option value="14" <c:if test="${userInfo.privilege=='14' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.privilege.superadmin" />
											</option>
										</select>
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="user.add.user.category" />:
										</label>
										<select name="category" id="category" class="form-control">
											<option value="0" <c:if test="${userInfo.category=='0' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.category.ordinary" />
											</option>
											<option value="1" <c:if test="${userInfo.category=='1' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.category.vip" />
											</option>
											<option value="2" <c:if test="${userInfo.category=='2' }">
												selected="selected"</c:if>>
												<s:text name="user.add.user.category.blacklist" />
											</option>
										</select>
									</div>

									<div class="form-group">
										<label class="form-label">
											<s:text name="table.header.user.card" />:
										</label>
										<input type="text" id="userCard" value="${userInfo.mainCard }"
											class="form-control" readonly="readonly" disabled="disabled"
											style="background:#e9ecef" onfocus=this.blur() />
									</div>

									<div class="form-actions">
										<input type="submit" class="btn" value='<s:text name="dev.edit.commit"/>'
											onclick="operationEdit();" />
										<input type="button" class="btn content_input3"
											value='<s:text name="sms.operate.return"/>'
											onclick="location.href='<%=basePath+" userAction!userList.action"%>'" />
									</div>
								</div>
							</c:when>
						</c:choose>
					</form>
				</div>

				<!--------------------------edit area end Ÿ------------------------------------------->
				<%@ include file="bottom.jsp" %>
		</body>

</html>