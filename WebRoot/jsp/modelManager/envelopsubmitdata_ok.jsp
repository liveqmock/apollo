<%@ page contentType="text/html; charset=GBK" %>

<%
String unitID= request.getParameter("unitID");
String taskTimeID= request.getParameter("taskTimeID");

%>
<html>
<head>
<title>选择汇总</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>

<body>
<center>
选择汇总完成。<br>
<a href="javascript:openResult()">点击查看 <%=unitID%> 数据</a><br>
<a href="javascript:window.close()">关闭</a><br>
</center>
</body>
<SCRIPT LANGUAGE="JavaScript">

function openResult()
{
	var url= "model?operation=showData&unitID=<%=unitID%>&taskTimeID=<%=taskTimeID%>";
	window.open(url, "_blank");
	window.close();
}
</SCRIPT>
</html>