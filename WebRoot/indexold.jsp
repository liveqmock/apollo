<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="csslib/main.css" rel="stylesheet" type="text/css">
<title>
ϵͳ--�û���¼
</title>
<script  language= "javascript" src="jslib/public.js"></script>

<script language="JavaScript">
  getRidOfParentFrame();

//Name: checkvalue(����û�����)
  function checkvalue()
  {
	if(isNull(document.frmLogin.account.value.trim())==true)
    {
      alert("�û�������Ϊ��");
      document.frmLogin.account.focus();
      return false;
    }
    if((hasIllegalChar(document.frmLogin.account.value))==true)
    {
      alert("�û����зǷ��ַ�");
      document.frmLogin.account.focus();
      return false;
    }
    if((isNull(document.frmLogin.password.value.trim()))==true)
    {
      alert("���벻��Ϊ��!");
      document.frmLogin.password.focus();
      return false;
    }
    if(judgeVersionOfBrowser()==false){
      alert("��ϵͳ��ҪIE5.5�����ϲ�������ʹ��,���������IE�����!");
	  return false;
    }
	return true;
  }
//���û�ע��
function register(){
   window.location = "servlet/login?operation=<%= LoginServlet.SHOW_REGISTER_PAGE %>";
}
</script>
<!--************************************************-->
</head>
<body bgcolor="#ffffff">
 <br><br> <br>

<!--��¼����ʼ-->
<form id="frmLogin" name="frmLogin"  method="post" action="servlet/login?operation=<%= LoginServlet.LOGIN %>">
<table border="0" cellpadding="0" cellspacing="0" width="800" align="center">
  <!-- fwtable fwsrc="Untitled" fwbase="dl.gif" fwstyle="Dreamweaver" fwdocid = "742308039" fwnested="0" -->
  <tr>
   <td><img src="img/spacer.gif" width="279" height="1" border="0"></td>
   <td><img src="img/spacer.gif" width="10" height="1" border="0"></td>
   <td><img src="img/spacer.gif" width="240" height="1" border="0"></td>
   <td><img src="img/spacer.gif" width="271" height="1" border="0"></td>
   <td><img src="img/spacer.gif" width="1" height="1" border="0"></td>
  </tr>


  <tr>
   <td><img name="dl_r2_c1" src="img/dl_r2_c1.gif" width="279" height="38" border="0"></td>
   <td colspan="2"><img name="dl_r2_c2" src="img/dl_r2_c2.gif" width="250" height="38" border="0"></td>
   <td><img name="dl_r2_c4" src="img/dl_r2_c4.gif" width="271" height="38" border="0"></td>
   <td><img src="spacer.gif" width="1" height="38" border="0"></td>
  </tr>
  <tr>
   <td rowspan="2" colspan="2"><img name="dl_r3_c1" src="img/dl_r3_c1.gif" width="289" height="244" border="0"></td>
   <td rowspan="2"><img name="dl_r3_c3" src="img/dl_r3_c3.gif" width="240" height="244" border="0"></td>
   <td><img name="dl_r3_c4" src="img/dl_r3_c4.gif" width="271" height="64" border="0"></td>
   <td><img src="img/spacer.gif" width="1" height="64" border="0"></td>
  </tr>
  <tr>
    <td rowspan="2" background="img/dl_r4_c4.gif">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="118" align="center" background="img/dl_r4_c4.gif">
          <tr >
            <td width="31%" height="37">
              <div align="right">�û�����</div>
            </td>
            <td width="69%" height="37" align="left" valign="middle">
              <input type="text" name="account" width="100%">
            </td>
          </tr>

          <tr>
            <td width="31%" height="37">
              <div align="right">��&nbsp;&nbsp;�룺</div>
            </td>
            <td width="69%" height="32" align="left" valign="middle">
              <input type="password" name="password" width="100%">
            </td>
          </tr>

          <tr>
            <td width="31%" height="37" align="left">
             &nbsp;
            </td>
			  <td width="69%" height="37" align="left">
				 <input type="submit" class="button" value="��¼"  onclick="javascript:return checkvalue();">
				 &nbsp;&nbsp;
				 <a href="../apollo/yuebao_setup.exe">�ͻ����������</a>
			  </td>
          </tr>
        </table>

    </td>
    <td><img src="img/spacer.gif" width="1" height="180" border="0"></td>
  </tr>
  <tr>
   <td rowspan="2" colspan="2"><img name="dl_r5_c1" src="img/dl_r5_c1.gif" width="289" height="205" border="0"></td>
   <td rowspan="2"><img name="dl_r5_c3" src="img/dl_r5_c3.gif" width="240" height="205" border="0"></td>
   <td><img src="img/spacer.gif" width="1" height="14" border="0"></td>
  </tr>
  <tr>
   <td><img name="dl_r6_c4" src="img/dl_r6_c4.gif" width="271" height="191" border="0"></td>
   <td><img src="img/spacer.gif" width="1" height="191" border="0"></td>
  </tr>

</table>
</form>
<!--��¼������-->
<script language="javascript">
//���û���������ý���
frmLogin.account.focus();
</script>
</body>
</html>