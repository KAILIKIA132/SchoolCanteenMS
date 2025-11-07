<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name="push.web.demo.name"/></title>
<%@ include file="includejs.jsp" %>

<script type="text/javascript">
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
</script>
</head>
<body>

<!--------------------------å¤´é¨å¼å§------------------------------------------>
<%@ include file="top.jsp"%>
<!--------------------------å¤´é¨ç»æ------------------------------------------>

<!--------------------------å¤´é¨æä½åè½å¼å§------------------------------------------>
<div class="push_Search_box"> 
<div class="l zkoperation_box">
	<div id="zkoperation_nav">
		<ul id="zkoperation_navMenu">
			<li><a href="#" rel='dropmenu1'>éä¸­æä½</a></li>
		</ul>
	</div>
	<script type='text/javascript' src='js/dropdown.js'></script>
	<ul id="dropmenu1" class="zkoperation_dropMenu">
		<li><a href="#" class="zkoperation_ico1">å é¤éä¸­æ°æ®</a></li>


	</ul>
	<script type="text/javascript">cssdropdown.startchrome("zkoperation_navMenu")</script> 
	<input type="button" align="right" class="input_Export l" value="å¯¼åº" onclick="location.href='javascript:void()'" id="open1"/>
</div>
<div class="l zk_Search">

  
      <div class="nice-select2 l" name="nice-select">
    <input type="text" value="ææ¶é´" readonly>
    <ul>
      <li >ä»å¤©</li>
      <li >å7å¤©</li>
      <li >æ¬æ</li>
	  <li >ä»å¹´</li>
      <li onclick="javascript:document.getElementById('01').click();"><a href="javascript:void()" id="open2">æå®èå´æ¥è¯¢..</a>
	  </li>
    </ul>
  </div>
      <div class="nice-select1 l" name="nice-select">
    <input type="text" value="æè®¾å¤" readonly>
    <ul>
      <li>0625133100090(10.20.4.101)</li>
      <li >0625133100090(210.10.2.201)</li>
      <li >0625133100090(10.20.4.134)</li>
      <li >06343533100090(10.20.4.101)</li>
	  <li >0625133100090(210.10.2.201)</li>
      <li >0625133100090(10.20.4.134)</li>
      <li >06343533100090(10.20.4.101)</li>
	  <li >0625133100090(210.10.2.201)</li>
      <li >0625133100090(10.20.4.134)</li>
      <li >06343533100090(10.20.4.101)</li>
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
</div>

<!--------------------------å¤´é¨æä½åè½ç»æ------------------------------------------>

<!--------------------------åè¡¨å¼å§------------------------------------------->
<table border="0" cellpadding="0" cellspacing="0" class="push_tab1">
  <tr>
    <th><input type="checkbox" name="checkbox" value="checkbox" /></th>
    <th>è®¾å¤</th>
    <th>ä¸ä¼ æ¶é´</th>
    <th>æ°æ® </th>
    <th>å¯¹è±¡</th>
    <th>æ°æ®è®°å½æ°</th>
    <th>éè¯¯</th>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox2" value="checkbox" /></td>
    <td>3084144480031 </td>
    <td>5634    </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>0</td>
  </tr>
</table>
<!--------------------------åè¡¨ç»æ------------------------------------------->
<%@ include file="bottom.jsp"%>
</body>
</html>
