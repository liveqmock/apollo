<%@ page contentType="text/html; charset=GBK" %>

<%
String unitID= request.getParameter("unitID");
String taskTimeID= request.getParameter("taskTimeID");

%>
<html>
<head>
<title>ѡ�����</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>

<body>
<center>
ѡ�������ɡ�<br>
<a href="javascript:openResult()">����鿴 <%=unitID%> ����</a><br>
<a href="javascript:window.close()">�ر�</a><br>
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