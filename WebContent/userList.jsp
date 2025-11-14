<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="includejs.jsp"%>
<title><s:text name="push.web.demo.name" /></title>
<script type="text/javascript">
var actionName="userAction!userList.action";
function dealData(url)
{
	$.ajax({
       type: "POST",
       url: url,
       dataType:"json",
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

	
var toNewDeviceDialogHtml='<div class="win_box">'
	+'<h2><s:text name="user.operate.move.userinfo2.newdevice"/></h2><hr />'
	+'<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">'
	+'<th align="center"><s:text name="table.header.device.sn"/></th>'
	+'<c:forEach var="dev" items="${devList}">'
	+'<tr><td>'
	+'<input name="devSn" type="checkbox" value="${dev.deviceSn}">${dev.deviceSn}</input>'
	+'</td></tr>'
	+'</c:forEach></table></div>';	

function procToNewDevice(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=userId]:checked").each(function(i){
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var obj=document.getElementsByName("devSn");
		//alert(obj.length);
		var destSn='';  
		for(var i=0; i<obj.length; i++){  
		  if(obj[i].checked) destSn+=obj[i].value+',';
		}  
		if (destSn.length > 0) {
			destSn = destSn.substring(0, destSn.length - 1);
			url = "userAction!toNewDevice.action?userId="+temp + "&destSn=" + destSn;
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
	$("input:checkbox[name=userId]:checked").each(function(i){
	
		temp += ($(this).val()+",");
	});
	if (temp.length <= 0) {
		alert("<s:text name='device.operate.warring1'/>");
		return -1;
	}
	return 0;
}

$(function(){

/*Using an id to close the Modal*/
    $("#close").click( function(){
        $(".test1").fbmodal({close:true});
    });	
 
    $("#toNewDeviceOpen").bind("click",function(){
    	if (0 != checkSelectItem()) {
    		return;
    	}
    	    	
	   	$('.test1').append(toNewDeviceDialogHtml)
	   				.fbmodal(pushDialog,procToNewDevice);
    });
});
	
function operateUser(operate)
{
	var temp = "";
	$("input:checkbox[name=userId]:checked").each(function(i){
		
			temp += ($(this).val()+",");
	});
	if (temp.length > 0) {
		temp=temp.substring(0,temp.length-1);
	    var url= "";
	    if ("deleteUserServ" == operate) {
	    	url = "userAction!deleteUserServ.action?userId="+temp;
	    } else if ("deleteUserDev" == operate) {
	    	url = "userAction!deleteUserDev.action?userId=" + temp;
	    }else if ("deleteUserFpDev" == operate) {
	    	url = "userAction!deleteUserFpDev.action?userId=" + temp;
	    } else if ("deleteUserPicDev" == operate) {
	    	url = "userAction!deleteUserPicDev.action?userId=" + temp;
	    } else if ("deleteUserFaceDev" == operate) {
	    	url = "userAction!deleteUserFaceDev.action?userId=" + temp;
	    } else if ("deleteUserPlamDev" == operate) {
    	   url = "userAction!deleteUserPlamDev.action?userId=" + temp;
        }else if ("sendUserDev" == operate) {
	    	url = "userAction!sendUserDev.action?userId="+temp;
	    } else if ("deleteUserFaceServ" == operate) {
			url = "userAction!deleteUserFaceServ.action?userId="+temp;
	    } else if ("deleteUserPlamServ" == operate) {
		   url = "userAction!deleteUserPlamServ.action?userId="+temp;
        }else if ("deleteUserFpServ" == operate) {
	    	url = "userAction!deleteUserFpServ.action?userId="+temp;
	    } else if ("deleteUserPicServ" == operate) {
	    	url = "userAction!deleteUserPicServ.action?userId="+temp;
	    }
	    dealData(url);
	} else {
		alert("<s:text name='device.operate.warring1'/>");
	}	
}

function getCond()
{
	var devSn = $("#seledDev").val();
	var userPin = $("#searchUserPin").val();
	if ('<s:text name="user.search.by.device"/>' == devSn) {
		devSn='';
	} 
	if ('<s:text name="user.serach.input.name.or.userpin"/>' == userPin) {
		userPin='';
	}
	var url = "deviceSn="+devSn + "&userPin=" + userPin;
	return url;
}

function search() 
{
	var url= "?" + getCond();
	location.href = actionName + url;
}

function selAll()
{
	var devSelAllChk = document.getElementsByName("userSelAll");
	var snChk = document.getElementsByName("userId");
	if (devSelAllChk[0].checked == false) {
		for (var i = 0; i < snChk.length; i ++) {
			snChk[i].checked = false;
		}
	} else {
		for (var i = 0; i < snChk.length; i ++) {
			snChk[i].checked = true;
		}
	}
}

function editUser(userId) {
	if (null == userId || "" == userId) {
		return;
	}
	var url = "userAction!newUser.action?act=edit&userId=" + userId;
	location.href = url;
}
</script>
</head>
<body>

	<!--------------------------å¤´é¨å¼å§------------------------------------------>
	<%@ include file="top.jsp"%>
	<!--------------------------å¤´é¨ç»æ------------------------------------------>

	<!--------------------------å¤´é¨æä½åè½å¼å§------------------------------------------>
	<div class="push_Search_box">
		<div class="l zkoperation_box">
			<div class="zk_nav">
				<ul>
					<li id="zkoperation_navMenu1"><a href="#"><s:text
								name="user.operate.items" /></a>
						<ul>
							<li><a href="#"><s:text
										name="user.operate.category.server" /></a>
								<ul>
									<li><a onclick="operateUser('deleteUserServ')" href="#"><s:text
												name="user.operate.deleteuser.server" /></a></li>
									<li><a onclick="operateUser('deleteUserFaceServ')"
										href="#"><s:text name="user.operate.delete.user.face.serv" /></a></li>
									<li><a onclick="operateUser('deleteUserPlamServ')"
										href="#"><s:text name="user.operate.delete.user.plam.server" /></a></li>
									<li><a onclick="operateUser('deleteUserFpServ')" href="#"><s:text
												name="user.operate.delete.user.fp.serv" /></a></li>
									<li><a onclick="operateUser('deleteUserPicServ')" href="#"><s:text
												name="user.operate.delete.user.pic.serv" /></a></li>
								</ul></li>
							<!--
					<li>
						<a href="#"><s:text name="user.operate.category.remote.enroll.cmd"/></a>
						<ul>
							<li><a href="#"><s:text name="user.operate.enrollfp"/></a></li>
							<li><a href="#"><s:text name="user.operate.enrollface"/></a></li>
							<li><a href="#"><s:text name="user.operate.enrollplam"/></a></li>			
						</ul>
					</li>
					-->
							<li><a href="#"><s:text
										name="user.operate.category.data.cmd" /></a>
								<ul>
									<li><a onclick="operateUser('sendUserDev')" href="#"><s:text
												name="user.operate.senduser2device" /></a></li>
									<li><a id="toNewDeviceOpen"
										onclick="location.href='javascript:void()'" href="#"><s:text
												name="user.operate.move.userinfo2.newdevice" /></a></li>
									<li><a onclick="operateUser('deleteUserDev')" href="#"><s:text
												name="user.operate.deleteuser.device" /></a></li>
									<li><a onclick="operateUser('deleteUserFpDev')" href="#"><s:text
												name="user.operate.delete.user.fp.device" /></a></li>
									<li><a onclick="operateUser('deleteUserPicDev')" href="#"><s:text
												name="user.operate.delete.user.pic.device" /></a></li>
									<li><a onclick="operateUser('deleteUserFaceDev')" href="#"><s:text
												name="user.operate.delete.user.face.device" /></a></li>
									<li><a onclick="operateUser('deleteUserPlamDev')" href="#"><s:text
												name="user.operate.delete.user.plam.device" /></a></li>
								</ul></li>
						</ul></li>
				</ul>
			</div>
			<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script>

			<input type="button" align="right" class="input_add l"
				value='<s:text name="user.operate.add.user"/>'
				onclick="location.href='<%=basePath + "userAction!newUser.action?act=new"%>'" />
			<input type="button" align="right" class="input_add l"
				value="Bulk Import Users"
				onclick="location.href='<%=basePath + "userAction!showBulkImport.action"%>'" 
				style="margin-left: 10px;" />
			<input type="button" align="right" class="input_add l"
				value="Verification Reports"
				onclick="location.href='<%=basePath + "verificationReportAction!reportList.action"%>'" 
				style="margin-left: 10px;" />
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
				<input class="input_sou" id="searchUserPin" type="text"
					value='${byUserPin21}' onfocus="this.value=''"
					onblur="if(!value){value=defaultValue;}" /> <input
					class="input_ico r" type="submit" name="Submit" value=""
					onclick="search()" />
				<!--
		<input class="input_ico_Alone" type="submit" name="Submit" value="" onclick="search()"/>
		-->
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
		</div>
	</div>

	<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

	<!--------------------------List------------------------------------------->
	<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
		<tr>
			<th><input type="checkbox" name="userSelAll" value="checkbox"
				onclick="selAll()" /></th>
			<th><s:text name="table.header.user.pin" /></th>
			<th><s:text name="table.header.user.devicesn" /></th>
			<th><s:text name="table.header.user.name" /></th>
			<th><s:text name="table.header.user.privilege" /></th>
			<th><s:text name="table.header.user.category" /></th>
			<th><s:text name="table.header.user.accgroup" /></th>
			<th><s:text name="table.header.user.card" /></th>
			<th><s:text name="table.header.user.fp.count" /></th>
			<th><s:text name="table.header.user.face.count" /></th>
			<th><s:text name="table.header.user.palm.count" /></th>
			<th><s:text name="table.header.user.photo" /></th>
		</tr>
		<c:forEach var="user" items="${userInfoList}">
			<tr>
				<td><input type="checkbox" name="userId" value="${user.userId}" /></td>
				<td><a href="#" onclick="editUser('${user.userId}')">${user.userPin}</a></td>
				<td>${user.deviceSn}</td>
				<td>${user.name}</td>
				<td>${user.privilege}</td>
				<td>${user.category}</td>
				<td>${user.accGroupId}</td>
				<td>${user.mainCard}</td>
				<td>${user.userFpCount}</td>
				<td>${user.userFaceCount}</td>
				<td>${user.userPalmCount}</td>
				<td><c:if test="${not empty user.photoIdContent}">
						<img src="data:image/jpeg;base64,${user.photoIdContent}"
							width="30" height="30" />
					</c:if></td>
			</tr>
		</c:forEach>
	</table>

	<%@ include file="pagenition.jsp"%>
	<!--------------------------List end------------------------------------------->
	<%@ include file="bottom.jsp"%>
	<div class="test1"></div>
</body>
</html>
