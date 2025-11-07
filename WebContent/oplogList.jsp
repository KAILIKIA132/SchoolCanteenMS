<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="includejs.jsp" %>

<script type="text/javascript">
var actionName="devLogAction!deviceLogList.action";
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
	$("input:checkbox[name=logId]:checked").each(function(i){
		
			temp += ($(this).val()+",");
	});
	if (temp.length > 0) {
		temp=temp.substring(0,temp.length-1);
	    var url= "";
	    if ("deleteSel" == operate) {
	    	url = "devLogAction!deleteSel.action?logId="+temp;
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
	var url = "deviceSn="+devSn;
	return url;
}

function search() 
{
	var url = "?" + getCond();
	location.href = actionName + url;
}


function selAll()
{
	var devSelAllChk = document.getElementsByName("opSelAll");
	var snChk = document.getElementsByName("logId");
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

<!--------------------------header------------------------------------------>
<%@ include file="top.jsp" %>
<!--------------------------header end------------------------------------------>

<!------------------------------operate-------------------------------------->
<div class="push_Search_box"> 
<div class="l zkoperation_box">
	<div class="zk_nav">
		<ul>
			<li id="zkoperation_navMenu1">
				<a href="#"><s:text name="dev.log.operate.items"/></a>
				<ul>
					<li><a href="#" onclick="operation('deleteSel')"><s:text name="dev.log.operate.delete.selected"/></a></li>		
				</ul>
			</li>
		</ul>
	</div>
	
	<!--<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script> 
	<input type="button" align="right" class="input_Export l" value='<s:text name="dev.log.operate.export"/>' onclick="location.href='javascript:void()'" id="open1"/>
--></div>

<div class="l zk_Search">
<div class="nice-select1 l" name="nice-select">
    <input type="text" id="seledDev" value='${byDeviceSn}' readonly>
    <ul>
		<c:forEach var="selDev" items="${devList}">
			<li>${selDev.deviceSn}</li>
		</c:forEach>
    </ul>
  </div>
		<input class="input_ico_Alone" type="submit" name="Submit" value="" onclick="search()"/>
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

<!--------------------------operate end------------------------------------------>

<!--------------------------åè¡¨å¼å§------------------------------------------->
<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
  <tr>
    <th><input type="checkbox" name="opSelAll" value="checkbox" onclick="selAll()"/></th>
    <th><s:text name="table.header.dev.log.operator"/></th>
    <th><s:text name="table.header.dev.log.op.time"/></th>
    <th><s:text name="table.header.dev.log.op.type"/></th>
    <th><s:text name="table.header.dev.log.op.data"/></th>
    <th><s:text name="table.header.dev.log.device.sn"/></th>
<%--     <th><s:text name="table.header.dev.log.temperatureReading"/></th>
 --%>  </tr>
	<c:forEach var="log" items="${list}"> 
	  <tr>
    <td><input type="checkbox" name="logId" value="${log.devLogId}" /></td>
    <td>${log.operator}</td>
    <td>${log.operateTime}</td>
    <td>${log.operateType}</td>
    <td>${log.value1}&emsp;${log.value2}&emsp;${log.value3}&emsp;${log.reserved}</td>
    <td>${log.deviceSn}</td>
  	</tr>
	</c:forEach>
</table>
<%@ include file="pagenition.jsp"%>
<!--------------------------åè¡¨ç»æ------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
