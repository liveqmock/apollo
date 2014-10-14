<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>��������</title>
</head>
<script language="JavaScript">
function publishTask()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ��������������ļ�");
        return false;
    }
    return true;
}
function deleteTask(taskID,taskName)
{
	if(confirm("�ò�����ɾ������("+taskName+")��һ�����ݣ����Ҳ��ָܻ����Ƿ�ִ�У�"))
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
            �������-><font class="clsLightColorOfTask">��������</font>
         </td>
       </tr>
       <tr>
         <td colspan=8><br>
            <img src="../img/infomation.bmp">&nbsp;��ҳ��������������ķ�����ɾ��.
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
                 ��������ļ���&nbsp;&nbsp;&nbsp;
                 <input type="file" name="file"/>&nbsp;&nbsp;
                 <input type="submit"value="����" onclick="return publishTask()"/> </td>
        </form>
  </tr>
  <tr height=20 class="clsTrContext">
    <td align=center colspan=8>�����б�</td>
  </tr>
 <tr class="clsTrHeader" height=20px>
    <td width=20% >����</td>
    <td width=20% >��ʶ</td>
    <td width=20% >����ʱ��</td>
    <td width=20% >����</td>
    <td width=20% >����</td>
  </tr>
<%
    int count=0;//����������
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
    &nbsp;<a href="javascript:deleteTask('<%= task.id() %>','<%= task.getName() %>')">ɾ������</a></td>
</tr>
<%
    }
%>
</table>


</body>
</html>
