<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Group"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>
<%
//��ֹ��������汾ҳ��
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

  //����ǰ���
  function checkInit()
  {
    if(form1.file.value == "")
    {
        alert("��ѡ������ļ���");
        return false;
    }
	if(confirm("�ò��������������û���Ϣ���Ƿ�ִ�У�"))
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
       <td align="center"><img src="../img/infomation.bmp">&nbsp;�˹���������ʼ���û���Ϣ,һ���Խ��û����ݵ������ݿ���.<br>
       ע��:�˲��������õ�ǰ���ݿ������û���Ϣ!
    </td>
    </tr>
    <tr>
      <form action="systemOperation?operation=<%=SystemOperationServlet.DO_INIT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>��ʼ���ļ���&nbsp;&nbsp;&nbsp;

          <input type="file" name="file"/>
          <input type="submit" value="�ᡡ��" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="center"><%=(request.getAttribute("succeed")==null)?"":"�û���ʼ���ɹ�"%></td>
    </tr>
  </table>
  <jsp:include page= "/jsp/footer.jsp" />
</body>
</html>