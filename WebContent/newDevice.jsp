<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="include.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name="push.web.demo.name"/></title>
<%@ include file="includejs.jsp" %>
<script type="text/javascript">
$(function(){

/*Using an id to close the Modal*/
    $("#close").click( function(){
        $(".test").fbmodal({close:true});
    });	
 

    $("#open3").click( function(){
        $(".test").fbmodal({
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
<%@ include file="top.jsp" %>
<!--------------------------å¤´é¨ç»æ------------------------------------------>



<!--------------------------é¡µé¢åå®¹å¼å§------------------------------------------->

<div class="push_Webcontent_bigbox">
 
      <h1><em>â¼</em>æ°å¢è®¾å¤</h1> 
	  <ul class="Webcontent_dt">
    <li><h2>åºåå·:</h2><input class="content_input" type="text"  />
    <span>åºè¯¥è®¾ç½®ä¸èå¤æºä¸è´, å¨èå¤æºä¸, è¿å¥ç®¡çèåç "ç³»ç»ä¿¡æ¯"/"è®¾å¤ä¿¡æ¯"/"åºåå·" å¯æ¥</span></li>
	<li><h2>æè¿èæºæ¶é´:</h2><input type="text" class="content_input"  /></li>
	<li><h2>ä¼ éæ¶é´:</h2><input class="content_input" name="" type="text" /><span>ç¨äºè®¾å®èå¤æºä»æä¸ªæ¶å»èµ·å¼å§æ£æ¥å¹¶åæå¡å¨ä¼ éæ°æ°æ®. hh:mm (æ¶:å)æ ¼å¼, å¤ä¸ªæ¶é´ä¹é´ç¨åå·(;)åå¼</span></li>
	<li><h2>å·æ°é´éæ¶é´:</h2><input class="content_input" name="" type="text"  /><span>ç¨äºè®¾å®è®¾å¤æ¯é´éå¤å°åéå°±æ£æ¥å¹¶åæå¡å¨ä¼ éæ°æ°æ®</span></li>
	<li><h2>ä¼ éç­¾å°è®°å½æ è®°:</h2><input class="content_input" name="" type="text" /><span>ç¨äºæ è¯è®¾å¤åæå¡å¨ä¼ éææ°çç­¾å°è®°å½çæ¶é´æ³</span></li>
	<li><h2>ä¼ éç¨æ·æ°æ®æ è®°:</h2><input class="content_input" name="" type="text" /><span>ç¨äºæ è¯è®¾å¤åæå¡å¨ä¼ éææ°çç¨æ·æ°æ®çæ¶é´æ³</span></li>
	<li><h2>ä¼ éå¾çæ è®°:</h2><input class="content_input" name="" type="text"  /><span>ç¨äºæ è¯è®¾å¤åæå¡å¨ä¼ éææ°çç¨æ·æ°æ®çæ¶é´æ³</span></li>
	<li><h2>è®¾å¤å«å:</h2><input class="content_input" name="" type="text"  /><span>è®¾å¤çå«å</span></li>
	<li><h2>èªå¨å¤ä»½ç»è®°æ°æ®å°:</h2><input class="content_input" name="" type="text" />
	<span id="content"><input class="content_input1" type="button" value=" " onclick="location.href='javascript:void()'" id="open3"/>
	<!--------------------------å¼¹çªåå®¹å¼å§------------------------------------------->
	<div class="test">
	<div class="win_box"><h2>è¯·éæ© è®¾å¤: </h2><select name="" size="10">
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.12.123)</option>
	  <option>0530143200138(192.168.3.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	  <option>0530143200138(192.168.0.123)</option>
	    <option>0530143200138(192.168.0.123)</option>
		  <option>0530143200138</option>
		    <option>0530143200138</option>  <option>0530143200138</option>
			  <option>0530143200138</option>
			    <option>0530143200138</option>
	</select>
	</div>
	</div>
	<!--------------------------å¼¹çªåå®¹ç»æ------------------------------------------->
	</span></li>
	<li><h2>æå¨åå¸:</h2><select class="content_input" name="" value="shenzhen" ></select></li>
	<li><h2>æ¶åº:</h2><input class="content_input" name="" type="text" /></li>
  </ul>
 <div class="content_button_box"> <input type="button" class="content_input2" value="æ°å¢è®¾å¤" onClick="alert('æ°å¢è®¾å¤æåï¼');location.href= 'index.html';return false;"/>
	<input type="button" class="content_input3" value="è¿ å" onclick="location.href='javascript:history.go(-1);'" /></div>
</div>

<!--------------------------é¡µé¢åå®¹ç»æ------------------------------------------->
<div class="push_bottom">CopyRight: ZKTeco IncÂ©. 2010-2018</div>
</body>
</html>
