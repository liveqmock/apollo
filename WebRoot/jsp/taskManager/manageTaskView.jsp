<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<%
Task task = (Task)request.getAttribute("task");
%>
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>表样管理</title>
</head>
<body>

  <table height=70% width=100% border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=2 valign="middle">
            任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">表样管理</font>
         </td>
       </tr>
    <tr>
       <td align="center"><img src="../img/infomation.bmp">&nbsp;发布表样文件可以对系统表样进行更新.</td>
    </tr>
    <tr>
      <form action="task?operation=<%= TaskServlet.PUBLISH_XSLT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>
           选择表样文件：
           <input type="file" name="file"/>
           <input type="hidden" name="tableID" value="">
           <input type="submit" name="doSubmit" value="发  布" onclick="return check()"/>
        </td>
      </form>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>
<script language="javascript">
function check(){
  if(form1.file.value == ""){
    alert("请选择表样文件!");
    return false;
  }
}
</script>
</body>
</html>