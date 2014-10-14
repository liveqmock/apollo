<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.report.form.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>
<%
	ReportTemplateForm form = (ReportTemplateForm)request.getAttribute("form");

%>
<html>
<head>
<title>
templateview
</title>
</head>
<body>
<%
	if(form!=null)
	{
		out.print(form.getTemplatename());
%>
<img src="../do/report?action=demoimage&tid=<%=form.getTemplateid()%>">
<%
	}
%>
</body>
</html>
