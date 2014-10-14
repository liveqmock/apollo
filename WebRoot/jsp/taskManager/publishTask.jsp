<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>发布任务</title>
</head>
<script language="JavaScript">
function publishTask()
{
    if(form1.file.value == "")
    {
        alert("请选择要发布的任务参数文件");
        return false;
    }
    return true;
}
function deleteTask(taskID,taskName)
{
	if(confirm("该操作将删除任务("+taskName+")的一切数据，而且不能恢复，是否执行？"))
    {
        parent.location = "task?operation=<%= TaskServlet.DELETE_TASK %>"
            + "&taskID=" + taskID;
    }
}
/*
function openTask(taskID,taskName)
{
        ul = "task?operation=<%= TaskServlet.OPEN_TASK %>"
            + "&taskID=" + taskID + "&taskName=" + taskName;
        window.location=ul;
} */
</script>
<body>

<table class="clsTaskFrameTable" border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            任务管理-><font class="clsLightColorOfTask">设置任务</font>
         </td>
       </tr>
       <tr>
         <td colspan=8><br>
            <img src="../img/infomation.bmp">&nbsp;此页面用来管理任务的发布和删除.
         </td>
       </tr>
       <tr>
         <td colspan=8>
            &nbsp;
         </td>
       </tr>
  <tr>
        <form action="task?operation=<%= TaskServlet.PUBLISH_TASK %>" method="post" enctype="multipart/form-data" name="form1" target="_parent">
                 <td align=left colspan=8>
                 任务参数文件：&nbsp;&nbsp;&nbsp;
                 <input type="file" name="file"/>&nbsp;&nbsp;
                 <input type="submit"value="发布" onclick="return publishTask()"/> </td>
        </form>
  </tr>
  <tr height=20 class="clsTrContext">
    <td align=center colspan=8>任务列表</td>
  </tr>
 <tr class="clsTrHeader" height=20px>
    <td width=20% >名称</td>
    <td width=20% >标识</td>
    <td width=20% >发布时间</td>
    <td width=20% >描述</td>
    <td width=20% >操作</td>
  </tr>
<%
    int count=0;//表格的行数。
    Iterator taskItr = (Iterator)request.getAttribute("taskItr");
    while(taskItr.hasNext())
    {
      count++;
      Task task = (Task)taskItr.next();
%>
  <tr height=20 <%=(((count%2)==1)?"class=\"TrDark\"":"class=\"TrLight\"")%>>
    <td width=20% height=20><%= Convertor.getHTMLString(task.getName()) %>&nbsp;</td>
    <td width=20% height=20><%= Convertor.getHTMLString(task.id()) %>&nbsp;</td>
    <td width=20% height=20><%= Convertor.date2String(task.getCreatedDate()) %>&nbsp;</td>
    <td width=20% height=20><%= Convertor.getHTMLString(task.getMemo()) %>&nbsp;</td>
    <td width=20% height=20>
    &nbsp;<a href="javascript:deleteTask('<%= task.id() %>','<%= task.getName() %>')">删除任务</a></td>
</tr>
<%
    }
%>
</table>


</body>
</html>
