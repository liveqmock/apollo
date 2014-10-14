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

<title>模板查询</title>
</head>
<%
    Iterator templateItr = (Iterator)request.getAttribute("templateItr");
    Task task = (Task) request.getAttribute( "task" );
%>
<script language= "javascript">
var additionalPart = ""; // 打开任务时间选择页面，其他查询字符


function deleteScalarQueryTemplate(templateID, templateName)
{
    var bConfirmed = window.confirm( "确认删除模板 \"" + templateName + "\" 吗？" );
    
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
        alert("请选择要导入的指标查询模板文件");
        return false;
    }
    
    return true;
}

</script>
<body>

<table width=100% border="0">
	<tr height=26 class="clsTrHeader" >
		<td style="BACKGROUND-COLOR: #c7c9b1;color:black" width="100%" colspan="4" valign="middle">
		任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">模板管理</font>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="left">
			<div>
		<img src="../img/infomation.bmp">&nbsp; 查询模板的发布、导出和删除
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
                 <td valign="middle" colspan="4">&nbsp;&nbsp;模板文件：&nbsp;&nbsp;
	                 <input type="file" name="file"/>
	                 <input type="submit" value="发布模板"/>
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
		<td align=center colspan="4">模板列表</td>
	</tr>
	<tr class="clsTrHeader" height="20">
		<td>序号</td>
		<td>名称</td>		
		<td>导出</td>
		<td>删除</td>
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
		<a href="javascript:exportScalarQueryTemplate('<%= template.getTemplateID() %>')">导出模板</a>
		</td>
		<td>
		<a href="javascript:deleteScalarQueryTemplate('<%= template.getTemplateID() %>', '<%=template.getName()%>')">删除模板</a>
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
