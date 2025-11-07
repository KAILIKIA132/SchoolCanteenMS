<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name="push.web.demo.name"/></title>
<%@ include file="includejs.jsp" %>

<script type="text/javascript">
var actionName="cmdAction!devCmdList.action";
$(function(){

/*Using an id to close the Modal*/
    $("#close").click( function(){
        $(".test1").fbmodal({close:true});
    });	
 

    $("#open1").click( function(){
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
	
	
    $("#close").click( function(){
        $(".test2").fbmodal({close:true});
    });	
    $("#open2").click( function(){
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

	
function operation(operate)
{
	var temp = "";
	$("input:checkbox[name=cmdId]:checked").each(function(i){
		
			temp += ($(this).val()+",");
	});
	if (temp.length > 0) {
		temp=temp.substring(0,temp.length-1);
	    var url= "";
	    if ("deleteSel" == operate) {
	    	url = "cmdAction!deleteSel.action?cmdId="+temp;
	    } 
	    dealData(url);
	} else if ("deleteReturn" == operate){
		url = "cmdAction!deleteReturn.action";
		dealData(url);
	} else {
		alert("<s:text name='device.operate.warring1'/>");
	}
}

function viewCmd(cmdId) {
	if (null == cmdId || "" == cmdId) {
		return;
	}
	var url = "cmdAction!viewCmd.action?cmdId=" + cmdId;
	location.href = url;
}

function getCond() {
	var devSn = $("#seledDev").val();
	var command = $("#searchCommand").val();
	if ('<s:text name="user.search.by.device"/>' == devSn) {
		devSn='';
	} 
	if ('<s:text name="search.by.cmd"/>' == command) {
		command='';
	}
	var url = "deviceSn="+devSn + "&command=" + command;
	return url;
}

function search() 
{
	var url = "?" + getCond();
	location.href = actionName + url;
}


function selAll()
{
	var devSelAllChk = document.getElementsByName("cmdSelAll");
	var snChk = document.getElementsByName("cmdId");
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
		<ul>
			<li id="zkoperation_navMenu1">
				<a href="#"><s:text name="cmd.operate.items"/></a>
				<ul>
					<li><a href="#" onclick="operation('deleteSel')"><s:text name="cmd.operate.delete.selected"/></a></li>
					<li><a href="#" onclick="operation('deleteReturn')"><s:text name="cmd.operate.delete.all.return"/></a></li>					
				</ul>
			</li>
		</ul>
	</div>
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
		<input class="input_sou" id="searchCommand" type="text" value='${byCommand}' onfocus="this.value=''" onblur="if(!value){value=defaultValue;}"/>
		<input class="input_ico r" type="submit" name="Submit" value="" onclick="search()"/>
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

<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

<!--------------------------åè¡¨å¼å§------------------------------------------->
<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
  <tr>
    <th><input type="checkbox" name="cmdSelAll" value="checkbox" onclick="selAll()"/></th>
    <th><s:text name="table.header.cmd.id"/></th>
    <th><s:text name="table.header.cmd.device"/></th>
    <th><s:text name="table.header.cmd.content"/></th>
    <th><s:text name="cmd.header.cmd.commit.time"/></th>
    <th><s:text name="cmd.header.cmd.trans.time"/></th>
    <th><s:text name="cmd.header.cmd.return.time"/></th>
    <th><s:text name="cmd.header.cmd.return.value"/></th>
    <th><s:text name="table.header.cmd.return.info"/></th>
  </tr>
  <c:forEach var="cmd" items="${cmdList}">
   <tr>
    <td><input type="checkbox" name="cmdId" value="${cmd.devCmdId}" /></td>
    <td><a href="#" onclick="viewCmd('${cmd.devCmdId}')">${cmd.devCmdId}</a></td>
    <td>${cmd.deviceSn}</td>
    <td>${cmd.cmdContent}</td>
    <td>${cmd.cmdCommitTime}</td>
    <td>${cmd.cmdTransTime}</td>
    <td>${cmd.cmdOverTime}</td>
    <td>${cmd.cmdReturn}</td>
    <td>${cmd.cmdReturnInfo}</td>
  </tr>
  </c:forEach>
</table>


<%@ include file="pagenition.jsp"%>
<!--------------------------åè¡¨ç»æ------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
