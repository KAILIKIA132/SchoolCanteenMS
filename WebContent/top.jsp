<style>
	.btn-logout {
		color: #fff !important;
		text-decoration: none !important;
		font-weight: bold;
		background: rgba(0, 0, 0, 0.2);
		padding: 5px 15px;
		border-radius: 20px;
		border: 1px solid rgba(255, 255, 255, 0.3);
		transition: all 0.3s ease;
		display: inline-block;
	}

	.btn-logout:hover {
		background: rgba(255, 255, 255, 0.2);
		border-color: #fff;
		transform: translateY(-1px);
		box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
	}
</style>
<script type="text/javascript">


	function dealData1(url) {
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

	function operation1(operate) {
		if (confirm("<s:text name='delete.operate.confirm'/>")) {
			var url = "";
			if ("initDemo" == operate) {
				url = "deviceAction!initDemo.action"
			} else if ("deleteAllUserData" == operate) {
				url = "deviceAction!deleteAllUserData.action"
			}
			dealData1(url);
		}
		window.location.reload();
		return;
	}


</script>

<div class="push_top_Line"></div>
<div class="push_top_box">
	<div class="top_logo"><a href="#"><img src="images/crawford_crest.jpg" border="0" style="max-height: 60px;" /></a>
	</div>
	<div class="top_menu l">

		<ul id="jsddm">
			<li><a href="<%=basePath+"deviceAction!deviceList.action"%>" ><div class="menu_ico_Device"></div>
					<s:text name="menu.device" />
				</a>
				<ul class="li_link">
					<li><a href="<%=basePath+"deviceAction!deviceList.action"%>">
							<s:text name="menu.device.device.manager" />
						</a></li>
					<li><a href="<%=basePath+"monitorAction!monitorList.action"%>">
							<s:text name="menu.device.transaction.monitor" />
						</a></li>
				</ul>
			</li>
			<li><a href="<%=basePath+"userAction!userList.action"%>"><div class="menu_ico_Data"></div>
					<s:text name="menu.data" />
				</a>
				<ul class="li_link">
					<li><a href="<%=basePath+"userAction!userList.action"%>">
							<s:text name="menu.data.employee" />
						</a></li>
					<li><a href="<%=basePath+"attAction!verificationList.action"%>">Verification Responses</a></li>
					<li><a href="<%=basePath+"attAction!attLogList.action"%>">
							<s:text name="menu.data.transaction" />
						</a></li>
					<li><a href="<%=basePath+"smsAction!smsList.action"%>">
							<s:text name="menu.data.sms" />
						</a></li>
					<li><a href="<%=basePath+"meetAction!meetinfoList.action"%>">
							<s:text name="menu.data.meetinfo" />
						</a></li>
					<li><a href="<%=basePath+"advAction!advList.action"%>">
							<s:text name="menu.data.adv" />
						</a></li>
				</ul>
			</li>
			<li><a href="<%=basePath+"cmdAction!devCmdList.action"%>"><div class="menu_ico_Log"></div>
					<s:text name="menu.log" />
				</a>
				<ul class="li_link">
					<li><a href="<%=basePath+"devLogAction!deviceLogList.action"%>">
							<s:text name="menu.log.oplog" />
						</a></li>
					<!--<li><a href="devDataLog.jsp"><s:text name="menu.log.data.from.device"/></a></li>
			-->
					<li><a href="<%=basePath+"cmdAction!devCmdList.action"%>">
							<s:text name="menu.log.command" />
						</a></li>
				</ul>
			</li>
			<li><a href="#">
					<div class="menu_ico_System"></div>
					<s:text name="menu.system" />
				</a>
				<ul class="li_link">
					<li><a onclick="operation1('initDemo')" href="#">
							<s:text name="menu.system.init.demo" />
						</a></li>
					<li><a onclick="operation1('deleteAllUserData')" href="#">
							<s:text name="menu.system.delete.all.user.data" />
						</a></li>
				</ul>
			</li>
			<li><a href="<%=basePath+"commandAction!command.action"%>" ><div class="menu_ico_commands"></div>
					<s:text name="menu.commands" />
				</a>
			</li>
		</ul>

	</div>


	<div class="top_r_box r">
		<div class="zkadmin_nav" id="zkadmin_nav" style="float:left; margin-right:20px; color:white; line-height:58px;">
			<span>Welcome,
				<s:property value="#session.validUser.username" />
			</span>
			<span style="margin: 0 10px;">|</span>
			<a href="<%=basePath+"logout.action"%>" class="btn-logout">LOGOUT</a>
		</div>
		<li><a>
				<s:text name="push.web.demo.version" />
			</a></li>
	</div>

</div>