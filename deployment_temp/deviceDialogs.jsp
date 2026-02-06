<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<script type="text/javascript">

var queryAttLogDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.query.att.log"/></h2><hr />'
	+'<s:text name="dialog.query.start.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryStart" type="text"/>'
	+'<br/><br/>'
	+'<s:text name="dialog.query.end.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryEnd" type="text">'
	+'<br /><br/></div>';
	
var querySyncTimeZoneDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.timezone.att"/></h2><hr />'
	+'<s:text name="device.operate.category.other.timeZone"/>' +
	'<select class="Ent" name="timeZone" id="timeZone" style="width:100%;">' +
	 '<option value ="0" selected="selected">--</option>' +
	' <c:forEach items="${mapTimeZoneDetails}" var="timeZoneEntry">'+
		 '<option value ="${timeZoneEntry.key}">${timeZoneEntry.value}</option>' +
	 '</c:forEach>' +
	'</select>' +

	'<br/><br/>'+
	'<br/><br/></div>';
	
function procQueryAttLog(v)
{
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var startTime = $(".content>.win_box>#queryStart").val();
		var endTime = $(".content>.win_box>#queryEnd").val();
		if (null == startTime || "" == startTime) {
			alert('<s:text name="dialog.query.hint.input.start.time"/>');
			window.location.reload();
			return;
		}
		if (null == endTime || "" == endTime) {
			alert('<s:text name="dialog.query.hint.input.end.time"/>');
			window.location.reload();
			return;
		}
		url = "deviceAction!queryAttLog.action?sn="+temp + "&startTime=" + startTime + "&endTime=" + endTime;
		dealData(url);
	} 
	window.location.reload();
}

function procSyncingTimeZone(v)
{
	if (1 == v) {
		var timeZone = $('.Ent option:selected').text();
		 //= $('#timeZone').val();
		
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);

		url1 = "deviceAction!updateTimeZone.action?sn="+temp+"&timeZone="+timeZone;
		dealData(url1);
	} 
	window.location.reload();
}

var queryAttPhotoDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.query.att.photo"/></h2><hr />'
	+'<s:text name="dialog.query.start.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryStart" type="text"/>'
	+'<br/><br/>'
	+'<s:text name="dialog.query.end.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryEnd" type="text">'
	+'<br /><br/></div>';


function procQueryAttPhoto(v)
{
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var startTime = $(".content>.win_box>#queryStart").val();
		var endTime = $(".content>.win_box>#queryEnd").val();
		if (null == startTime || "" == startTime) {
			alert('<s:text name="dialog.query.hint.input.start.time"/>');
			window.location.reload();
			return;
		}
		if (null == endTime || "" == endTime) {
			alert('<s:text name="dialog.query.hint.input.end.time"/>');
			window.location.reload();
			return;
		}
		url = "deviceAction!queryAttPhoto.action?sn="+temp + "&startTime=" + startTime + "&endTime=" + endTime;
		dealData(url);
	} 
	window.location.reload();
}
	
var toNewDeviceDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.backupdata.tootherdevice"/></h2><hr />'
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
		$("input:checkbox[name=sn]:checked").each(function(i){
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
			url = "deviceAction!toNewDevice.action?sn="+temp + "&destSn=" + destSn;
			//alert(url);
			dealData(url);
		}
		else {
			alert('<s:text name="dialog.hint.please.select.device"/>');
		}
		
	} 
	window.location.reload();	
}

var queryUserDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.query.user.info"/></h2><hr />'
	+'<s:text name="device.operate.query.hint.input.user.pin"/><br/><br/>' 
	+'<input id="userPin" type="text"/>'
	+'<br/><br/>'
	+'</div>';


function procQueryUserInfo(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var userPin = $(".content>.win_box>#userPin").val();
		if ("" == userPin) {
	    	alert('<s:text name="device.operate.query.hint.input.user.pin"/>');
	    } else {
	    	url = "deviceAction!queryUserInfo.action?sn="+temp + "&userPin=" + userPin;
			dealData(url);
	    }
	}
	window.location.reload();
}


	
var queryFingerTmpDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.query.finger.tmp"/></h2><hr />'
	+'<s:text name="dialog.query.hint.user.pin"/>：<br/>' +
	'<input id="userPin" type="text"/>'
	+'<br/><br/>'
	+'<s:text name="dialog.query.hint.finger.id"/>：<br/> ' +
	'<input id="fingerId" type="text">'
	+'<br /><br/></div>';

function procQueryFingerTmp(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var userPin = $(".content>.win_box>#userPin").val();
		var fingerId = $(".content>.win_box>#fingerId").val();
		if ("" == userPin) {
	    	alert('<s:text name="device.operate.query.hint.input.user.pin"/>');
	    } else {
	    	url = "deviceAction!queryFingerTmp.action?sn="+temp + "&userPin=" + userPin + "&fingerId=" + fingerId;
			dealData(url);
	    }
	}
	window.location.reload();
}

var verifyLogDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.verify.att.log"/></h2><hr />'
	+'<s:text name="dialog.query.start.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryStart" type="text"/>'
	+'<br/><br/>'
	+'<s:text name="dialog.query.end.time"/>：<br/> <s:text name="dialog.query.time.format"/>' +
	'<input id="queryEnd" type="text">'
	+'<br /><br/></div>';

	
function procVerifyLog(v)
{
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var startTime = $(".content>.win_box>#queryStart").val();
		var endTime = $(".content>.win_box>#queryEnd").val();
		if (null == startTime || "" == startTime) {
			alert('<s:text name="dialog.query.hint.input.start.time"/>');
			window.location.reload();
			return;
		}
		if (null == endTime || "" == endTime) {
			alert('<s:text name="dialog.query.hint.input.end.time"/>');
			window.location.reload();
			return;
		}
		url = "deviceAction!verifyLog.action?sn="+temp + "&startTime=" + startTime + "&endTime=" + endTime;
		dealData(url);
	} 
	window.location.reload();
}


var getFileDialogHtml='<div class="win_box">'
	+'<h2><s:text name="devcie.operate.get.device.file"/></h2><hr />'
	+'<s:text name="device.hint.get.file.name"/><br/><br/>' 
	+'<input id="fileName" type="text"/>'
	+'<br/><br/>'
	+'</div>';


function procGetFile(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var fileName = $(".content>.win_box>#fileName").val();
		if ("" == fileName) {
	    	alert('<s:text name="device.hint.get.file.name"/>');
	    } else {
	    	url = "deviceAction!getFile.action?sn="+temp + "&file=" + fileName;
			dealData(url);
	    }
	}
	window.location.reload();
}


function procQueryFingerTmp(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var userPin = $(".content>.win_box>#userPin").val();
		var fingerId = $(".content>.win_box>#fingerId").val();
		if ("" == userPin) {
	    	alert('<s:text name="device.operate.query.hint.input.user.pin"/>');
	    } else {
	    	url = "deviceAction!queryFingerTmp.action?sn="+temp + "&userPin=" + userPin + "&fingerId=" + fingerId;
			dealData(url);
	    }
	}
	window.location.reload();
}

var putFileDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.put.file.dev"/></h2><hr />'
	+'<s:text name="dialog.putfile.src"/>：<br/> <s:text name="dialog.putfile.src.format"/><br/>' +
	'<input id="srcFile" type="text"/>'
	+'<br/><br/>'
	+'<s:text name="dialog.putfile.dest"/>：<br/> ' +
	'<input id="destFile" type="text">'
	+'<br /><br/></div>';

	
function procPutFile(v)
{
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var srcFile = $(".content>.win_box>#srcFile").val();
		var destFile = $(".content>.win_box>#destFile").val();
		if (null == srcFile || "" == srcFile) {
			alert('<s:text name="dialog.put.file.input.src.file"/>');
			window.location.reload();
			return;
		}
		if (null == destFile || "" == destFile) {
			alert('<s:text name="dialog.put.file.input.dest.file"/>');
			window.location.reload();
			return;
		}
		url = "deviceAction!putFile.action?sn="+temp + "&srcFile=" + srcFile + "&destFile=" + destFile;
		dealData(url);
	} 
	window.location.reload();
}


var setOptionDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.set.option"/></h2><hr />'
	+'<s:text name="device.hint.set.option"/><br/><br/>' 
	+'<input id="option" type="text"/>'
	+'<br/><br/>'
	+'</div>';


function procSetOption(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var option = $(".content>.win_box>#option").val();
		if ("" == option) {
	    	alert('<s:text name="device.hint.set.option"/>');
	    } else {
	    	url = "deviceAction!setOption.action?sn="+temp + "&option=" + option;
			dealData(url);
	    }
	}
	window.location.reload();
}

var enrollFPDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.enroll.fp"/></h2><hr />'
	+'<s:text name="dialog.enroll.fp.hint.user.pin"/><br/>' 
	+'<input id="userPin" type="text"/> <br/><br/>'
	+'<s:text name="dialog.enroll.fp.hint.finger.id"/><br/>'
	+'<input id="fingerId" type="text"/> <br/><br/>'
	+'<s:text name="dialog.enroll.fp.hint.retry.times"/><br/>'
	+'<input id="retryTimes" type="text"/> <br/><br/>'
	+'<input id="isCover" type="checkbox"><s:text name="dialog.enroll.fp.hint.cover"/></input>'
	+'<br/><br/>'
	+'</div>';


function procEnrollFPc(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var userPin = $(".content>.win_box>#userPin").val();
		var fingerId = $(".content>.win_box>#fingerId").val();
		var retryTimes = $(".content>.win_box>#retryTimes").val();
		var isCover = 0;
		if ($(".content>.win_box>#isCover").attr("checked") != undefined) {
			isCover = 1;
		}
		
		if ("" == userPin) {
	    	alert('<s:text name="device.operate.query.hint.input.user.pin"/>');
	    } else if ("" == fingerId) {
	    	alert('<s:text name="dialog.enroll.fp.input.finger.id"/>');
	    } else if ("" == retryTimes) {
	    	alert('<s:text name="dialog.enroll.fp.hint.input.retry.times"/>');
	    } else {
	    	url = "deviceAction!enrollFp.action?sn="+temp + "&userPin=" + userPin 
	    	+ "&fingerId=" + fingerId + "&retryTimes=" + retryTimes + "&isCover=" +isCover;
			dealData(url);
	    }
	}
	window.location.reload();
}


var shellDialogHtml='<div class="win_box">'
	+'<h2><s:text name="device.operate.shell"/></h2><hr />'
	+'<s:text name="device.hint.shell.command"/><br/><br/>' 
	+'<input id="shellCmd" type="text"/>'
	+'<br/><br/>'
	+'</div>';


function procShell(v) {
	if (1 == v) {
		var temp = "";
		var url= "";
		$("input:checkbox[name=sn]:checked").each(function(i){
		
			temp += ($(this).val()+",");
		});
		temp=temp.substring(0,temp.length-1);
		
		var shellCmd = $(".content>.win_box>#shellCmd").val();
		if ("" == shellCmd) {
	    	alert('<s:text name="device.hint.shell.command"/>');
	    } else {
	    	//alert(shellCmd);
	    	shellCmd = shellCmd.replace(/&/g,':');
	    	//alert(shellCmd);
	    	url = "deviceAction!shell.action?sn="+temp + "&cmd=" + shellCmd;
			dealData(url);
	    }
	}
	window.location.reload();
}

</script>