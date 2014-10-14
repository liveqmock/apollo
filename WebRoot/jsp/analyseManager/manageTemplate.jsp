<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/calendar/popcalendar.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<style type="text/css">


form {
	margin:		4;
	padding:	1;
}

/* over ride styles from webfxlayout */

body {
	margin:		0px;
	width:		auto;
	height:		auto;
}

</style>

<title>ģ���ѯ</title>
</head>
<%
    Iterator templateItr = (Iterator)request.getAttribute("templateItr");
    Task task = (Task) request.getAttribute( "task" );
%>
<script language= "javascript">
var additionalPart = ""; // ������ʱ��ѡ��ҳ�棬������ѯ�ַ�


function deleteScalarQueryTemplate(templateID, templateName)
{
    var bConfirmed = window.confirm( "ȷ��ɾ��ģ�� \"" + templateName + "\" ��" );
    
    if( !bConfirmed )
    	return;
    	
    execTplForm.action = "analyse?operation=<%= AnalyseServlet.DELETE_SCALAR_QUERY_TEMPLATE %>" + "&templateID=" + templateID;
    execTplForm.submit();
}

function exportScalarQueryTemplate(templateID)
{ 
    execTplForm.action = "analyse?operation=<%= AnalyseServlet.EXPORT_SCALAR_QUERY_TEMPLATE %>" + "&templateID=" + templateID;
    execTplForm.submit();
}

function importScalarQueryTemplate()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ�����ָ���ѯģ���ļ�");
        return false;
    }
    
    return true;
}

</script>
<body>

<table width=100% border="0">
	<tr height=26 class="clsTrHeader" >
		<td style="BACKGROUND-COLOR: #c7c9b1;color:black" width="100%" colspan="4" valign="middle">
		�������-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">ģ�����</font>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="left">
			<div>
		<img src="../img/infomation.bmp">&nbsp; ��ѯģ��ķ�����������ɾ��
		<br/>
			</div>
		</td>
	</tr>
	
	<% 
	String taskID = request.getParameter( "taskID" );
	if( cn.com.youtong.apollo.common.Util.isEmptyString( taskID ) )
	{
		Task attrTask = (Task) request.getAttribute( "task" );
		if( attrTask != null )
			taskID = attrTask.id();
	}
	
	if( cn.com.youtong.apollo.common.Util.isEmptyString( taskID ) )
	{
		taskID = (String) request.getSession().getAttribute( "taskID" );
	}
	%>
		
	<tr>
	<form action="../servlet/analyse?operation=<%= AnalyseServlet.IMPORT_SCALAR_QUERY_TEMPLATE %>&taskID=<%=taskID%>" 
			method="post" enctype="multipart/form-data" name="form1"
		 	onsubmit="return importScalarQueryTemplate()">
                 <td valign="middle" colspan="4">&nbsp;&nbsp;ģ���ļ���&nbsp;&nbsp;
	                 <input type="file" name="file"/>
	                 <input type="submit" value="����ģ��"/>
                 </td>
        </form>
  	</tr>
  	
  	<form id="execTplForm" name="execTplForm" method="post" target="_self">
		<input type="hidden" name="operation" value=""/>
		<input type="hidden" name="templateID" value=""/>
		
		<input type="hidden" name="taskID" value="<%=taskID%>"/>
		
  <% if( templateItr.hasNext() ) { %>
  	<tr>
  		<td colspan="4">&nbsp;</td>
  	</td>
  	
	<tr height="20" class="clsTrContext">
		<td align=center colspan="4">ģ���б�</td>
	</tr>
	<tr class="clsTrHeader" height="20">
		<td>���</td>
		<td>����</td>		
		<td>����</td>
		<td>ɾ��</td>
	</tr>
	<% String styleClassName = "TrDark";
	int i=1;
	while( templateItr.hasNext() ) { 
		ScalarQueryTemplate template = (ScalarQueryTemplate)templateItr.next();
	%>
	<tr height="20" class="<%=styleClassName%>">
		<td><%=i++%></td>
		<td><%=template.getName()%></td>		
		<td>
		<a href="javascript:exportScalarQueryTemplate('<%= template.getTemplateID() %>')">����ģ��</a>
		</td>
		<td>
		<a href="javascript:deleteScalarQueryTemplate('<%= template.getTemplateID() %>', '<%=template.getName()%>')">ɾ��ģ��</a>
		</td>
	</tr>
	<% 
	if( i % 2 == 0 )
		styleClassName = "TrLight"; 
	else 
		styleClassName = "TrDark";
	%>
	<% } %>
  <% } %>
  
 	</form>
</table>
		
</body>
</html>
