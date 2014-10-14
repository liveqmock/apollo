<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.report.form.ReportTemplateForm" %>
<%@ page import="java.util.*" %>
<%
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
  List templates = (List)request.getAttribute("templatelist");
%>
<html>
<head>
<script language="javascript">
	function templateDo(tid)
	{
		parent.reportform.tid.value=tid;
		parent.reportform.action = '../do/report?action=view';
		parent.reportform.submit();
	}

</script>
</head>
<body style="margin:0px;background-color:#aaaae2">
									<table cellspacing=2 cellpadding=0 border=0 style="width:100%;height:100%">
<%
	Iterator iter = templates.iterator();
	while(iter.hasNext())
	{
		ReportTemplateForm form = (ReportTemplateForm)iter.next();
		String name = (String)form.getTemplatename();
		if(name==null) name="";
		String displayname = (name.length()>15)?(name.substring(1,15)+"..."):name;
%>
										<tr style="height:20px">
											<td width="10px">&nbsp;</td>
											<td width="20px"><img src="../img/audit.gif">
											</td>
											<td style="color:blue;cursor:hand" onclick="templateDo('<%=form.getTemplateid()%>')" ALT=<%=name%>><%=displayname%>
											</td>
										</tr>
<%
	}
%>
										<tr>
											<td colspan=3>&nbsp;
											</td>
										</tr>
									</table>
</body>
</html>
