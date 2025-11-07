<script type="text/javascript">
<!--
function pageAct(act) {
	var url="";
	if ("next" == act) {
		url = "?act="+act;
		
	} else if ("previous" == act) {
		url = "?act="+act;
	} else if ("jump" == act) {
		url = "?jump="+$("#jumpPage").val();
	}
	var cond = "";
	if ("" != getCond()) {
		cond = "&" + getCond();
	}
	location.href = actionName + url + cond;
}
//-->
</script>
<div class="pagination">
<ul>
<li><s:text name="page.count"/></li>
<li><c:out value="${curPage}"/>/<c:out value="${pageCount}"/></li>
<li><a href="#" onclick="pageAct('previous')" ><s:text name="page.previous"/></a></li>
<li><a href="#" onclick="pageAct('next')"><s:text name="page.next"/></a></li>
<li><s:text name="page.hint.jump"/></li>
<li><input type="text" id="jumpPage"  class="content_input" size="3"/>
<s:text name="page.hint.page"/>
<input type="button" onclick="pageAct('jump')" value='<s:text name="page.jump"/>' /></li>

</ul>
</div>