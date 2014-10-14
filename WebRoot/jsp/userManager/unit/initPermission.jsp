<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script language="JavaScript">

  //发布前检查
  function checkInit()
  {
    if(form1.file.value == "")
    {
        alert("请选择参数文件！");
        return false;
    }
    return true;
  }
</script>
<body>

  <table height=70% width=100% border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=2 valign="middle">
            任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">初始化权限</font>
         </td>
       </tr>
    <tr>
       <td align="center"><img src="../img/infomation.bmp">&nbsp;此功能用来初始化权限,初始化之前先要确认是否已经先导入了用户信息.</td>
    </tr>
    <tr>
      <form action="unitPermission?operation=<%=UnitPermissionServlet.DO_INIT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>初始化文件：
          <input type="file" name="file"/>
          <input type="hidden" name="taskID" value='<%=(String)request.getSession().getAttribute("taskID")%>'>
          <input type="submit" value="提　交" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="center"><%=(request.getAttribute("succeed")==null)?"":"权限初始化成功"%></td>
    </tr>
  </table>

</body>
</html>