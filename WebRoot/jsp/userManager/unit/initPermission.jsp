<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="java.util.*"%>
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
    return true;
  }
</script>
<body>

  <table height=70% width=100% border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=2 valign="middle">
            �������-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">��ʼ��Ȩ��</font>
         </td>
       </tr>
    <tr>
       <td align="center"><img src="../img/infomation.bmp">&nbsp;�˹���������ʼ��Ȩ��,��ʼ��֮ǰ��Ҫȷ���Ƿ��Ѿ��ȵ������û���Ϣ.</td>
    </tr>
    <tr>
      <form action="unitPermission?operation=<%=UnitPermissionServlet.DO_INIT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>��ʼ���ļ���
          <input type="file" name="file"/>
          <input type="hidden" name="taskID" value='<%=(String)request.getSession().getAttribute("taskID")%>'>
          <input type="submit" value="�ᡡ��" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" align="center"><%=(request.getAttribute("succeed")==null)?"":"Ȩ�޳�ʼ���ɹ�"%></td>
    </tr>
  </table>

</body>
</html>