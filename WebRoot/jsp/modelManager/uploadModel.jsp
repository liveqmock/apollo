<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>�ϱ�����</title>
</head>
<script language="JavaScript">
function publishModel()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ�ϱ��ı��������ļ�");
        return false;
    }
    return true;
}

</script>
<body>



  <table height=70% width=100% >
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            ���ݴ���-><font class="clsLightColorOfTask">�ϱ�����</font>
         </td>
       </tr>
    <tr>
    <tr>
       <td align="center"><img src="../img/infomation.bmp">&nbsp;�˹����������������ݵ��뵽��������.</td>
    </tr>
    <tr>
  <tr height=20 colspan=8>
        <form action="model?operation=<%= ModelServlet.UPLOAD_MODEL %>" method="post" enctype="multipart/form-data" name="form1">
                 <td align=center>�ϴ����������ļ���
                    <input type="file" name="file"/>
                 <input type="submit"value="�ϱ�" onclick="return publishModel()"/></td>
        </form>
  </tr>
    <tr>
       <td align="center">&nbsp;</td>
    </tr>
</table>



</body>
</html>
