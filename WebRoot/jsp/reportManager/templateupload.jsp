<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<%
	String reportEditHTML = (String)request.getAttribute("reportEditHTML");
	String action = request.getParameter("action");
%>

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
<script language="javascript">
<%
	if(action!=null&&action.equals("upload"))
	{
%>
	parent.reportlist.location.replace("../do/report?action=templatelist");
<%
	}
%>
</script>
</head>
<%
    Task task = (Task) request.getAttribute( "task" );
%>
<body>

<table width=100% border="0">
	<tr height=26 class="clsTrHeader" >
		<td style="BACKGROUND-COLOR: #c7c9b1;color:black" width="100%" colspan="4" valign="middle">
		报表管理->模板管理-><font class="clsLightColorOfTask">上传文件</font>
		</td>
	</tr>

	<tr>
		<td colspan="4" align="left">
			<div>
		<img src="../img/infomation.bmp">&nbsp; 报表文件的发布、更新，管理
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
	<form action="../do/report?action=upload&taskID=<%=taskID%>"
			method="post" enctype="multipart/form-data" name="form">
                 <td valign="middle" colspan="4">&nbsp;&nbsp;报表文件：&nbsp;&nbsp;
	                 <input type="file" name="file"/>
	                 <input type="submit" value="发布报表"/>
                 </td>
        </form>
  	</tr>

  	<form id="execTplForm" name="execTplForm" method="post" target="_self">
		<input type="hidden" name="operation" value=""/>
		<input type="hidden" name="taskID" value="<%=taskID%>"/>

 	</form>
</table>
<%if(reportEditHTML!=null) out.print(reportEditHTML);%>

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
