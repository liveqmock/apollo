<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.data.UnitTreeNode" %>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../../csslib/main.css">
<title>增加选择汇总单位完成</title>
</head>

<%
	UnitTreeNode unit = (UnitTreeNode)request.getAttribute("unit");
	Exception e= (Exception)request.getAttribute("exception");
%>

<body>
</body>

<SCRIPT LANGUAGE="JavaScript">
<%
if(unit == null)
{
%>
	alert("添加用户失败: <%=e.toString()%>");
<%
}
else
{
%>
var wndOpenner= window.parent.opener;
wndOpenner.appendSelectSumUnit("<%= unit.id()%>", "<%= unit.getUnitName()%>");
window.parent.close();
<%
}
%>

</SCRIPT>
</html>