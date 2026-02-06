

/*-------------------------头部用户管理----------------------------------*/
$(function(){

	$(".zkadmin_nav p").click(function(){
		var ul=$(".zkadmin_new");
		if(ul.css("display")=="none"){
			ul.slideDown();
		}else{
			ul.slideUp();
		}
	});
	
	$(".zkadmin_set").click(function(){
		var _name = $(this).attr("name");
		if( $("[name="+_name+"]").length > 1 ){
			$("[name="+_name+"]").removeClass("zkadmin_select");
			$(this).addClass("zkadmin_select");
		} else {
			if( $(this).hasClass("zkadmin_select") ){
				$(this).removeClass("zkadmin_select");
			} else {
				$(this).addClass("zkadmin_select");
			}
		}
	});
	
	$(".zkadmin_nav li").click(function(){
		var li=$(this).text();
		$(".zkadmin_nav p").html(li);
		$(".zkadmin_new").hide();
		/*$(".set").css({background:'none'});*/
		$("p").removeClass("zkadmin_select") ;   
	});
})


/*-------------------------菜单下拉----------------------------------*/

var timeout         = 500;
var closetimer		= 0;
var ddmenuitem      = 0;

function jsddm_open()
{	jsddm_canceltimer();
	jsddm_close();
	ddmenuitem = $(this).find('ul').eq(0).css('visibility', 'visible');}

function jsddm_close()
{	if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');}

function jsddm_timer()
{	closetimer = window.setTimeout(jsddm_close, timeout);}

function jsddm_canceltimer()
{	if(closetimer)
	{	window.clearTimeout(closetimer);
		closetimer = null;}}

$(document).ready(function()
{	$('#jsddm > li').bind('mouseover', jsddm_open);
	$('#jsddm > li').bind('mouseout',  jsddm_timer);});

document.onclick = jsddm_close;