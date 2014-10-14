<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.report.form.*" %>
<%@ page import="java.util.*" %>
<%
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
  List templates = (List)request.getAttribute("templatelist");
  String tid = request.getParameter("tid");
  String url = "../do/report?action=reportchoosetemplate";
  if(tid==null) tid="";
  else url += "&tid="+tid;
%>
<html>
<head>
<script language="javascript">
	function onChooseTemplate(tid)
	{
		e.tid.value=tid;
		e.submit();
	}
</script>
</head>
<body>
<form name="e" action="../do/report?action=reportpublish" target="_self" method="post">
	<input type="hidden" name="tid" value="<%=tid%>" />
	<table cellpadding=0 cellspacing=1 border=0>
	</table>
</form>
<script language="javascript">
	parent.document.all['optionid'].innerHTML = "&nbsp;Ä£°æÁÐ±í";
	parent.document.all['contentid'].innerHTML = "<iframe width='100%' height='100%' frameborder='0' name='reportframe' src='<%=url%>' scrolling='auto'></iframe>";
</script>
</body>
</html>
