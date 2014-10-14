<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Group"%>
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
	if(confirm("该操作将重置所有用户信息，是否执行？"))
    {
        return true;
    }
    return false;
  }
</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
  <table height=70% width=100% border=0>
    <tr>
       <td align="center"><img src="../img/infomation.bmp">&nbsp;此功能用来初始化用户信息,一次性将用户数据导入数据库中.<br>
       注意:此操作将重置当前数据库所有用户信息!
    </td>
    </tr>
    <tr>
      <form action="systemOperation?operation=<%=SystemOperationServlet.DO_INIT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>初始化文件：&nbsp;&nbsp;&nbsp;

          <input type="file" name="file"/>
          <input type="submit" value="提　交" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="center"><%=(request.getAttribute("succeed")==null)?"":"用户初始化成功"%></td>
    </tr>
  </table>
  <jsp:include page= "/jsp/footer.jsp" />
</body>
</html>