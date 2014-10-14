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

<%
 String filename = (String)request.getAttribute("filename");
 Integer filelen = (Integer)request.getAttribute("filelen");
%>


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
<script language="JavaScript">
<!--
  function importScalarQueryTemplate()
{
    if(form1.file.value == "")
    {
        alert("请选择要导入的参数文件");
        return false;
    }
    
    return true;
}


//-->
</script>
</head>
<%
    Task task = (Task) request.getAttribute( "task" );
%>
<body>

<table width=100% border="0">
	<tr height=26 class="clsTrHeader" >
		<td style="BACKGROUND-COLOR: #c7c9b1;color:black" width="100%" colspan="4" valign="middle">
		任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">参数管理</font>
		</td>
	</tr>

	<tr>
		<td colspan="4" align="left">
			<div>
		<img src="../img/infomation.bmp">&nbsp; 参数文件的发布、更新
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
	<form action="../servlet/analyse?operation=<%= AnalyseServlet.IMPORT_PARAM %>&taskID=<%=taskID%>"
			method="post" enctype="multipart/form-data" name="form1"
		 	onsubmit="return importScalarQueryTemplate()">
                 <td valign="middle" colspan="4">&nbsp;&nbsp;参数文件：&nbsp;&nbsp;
	                 <input type="file" name="file"/>
	                 <input type="submit" value="发布参数"/>
                 </td>
        </form>
  	</tr>

  	<form id="execTplForm" name="execTplForm" method="post" target="_self">
		<input type="hidden" name="operation" value=""/>
		<input type="hidden" name="taskID" value="<%=taskID%>"/>

 	</form>
</table>

<%
	if(filename!=null)
	{
%>
	<table width=100% border="0">
		<tr>
			<td colspan=2><b>发布成功</b>
			</td>
		</tr>
		<tr>
			<td width="60px">文件名称:
			</td>
			<td><%=filename%>
			</td>
		</tr>
		<tr>
			<td>文件大小:
			</td>
			<td><%=filelen.toString()%>
			</td>
		</tr>
	</table>
<%
	}
%>
</body>
</html>
