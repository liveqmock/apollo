<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="csslib/main.css" rel="stylesheet" type="text/css">
<title>
系统--用户登录
</title>
<script  language= "javascript" src="jslib/public.js"></script>

<script language="JavaScript">
  getRidOfParentFrame();

//Name: checkvalue(检查用户输入)
  function checkvalue()
  {
	if(isNull(document.frmLogin.account.value.trim())==true)
    {
      alert("用户名不能为空");
      document.frmLogin.account.focus();
      return false;
    }
    if((hasIllegalChar(document.frmLogin.account.value))==true)
    {
      alert("用户名有非法字符");
      document.frmLogin.account.focus();
      return false;
    }
    if((isNull(document.frmLogin.password.value.trim()))==true)
    {
      alert("密码不能为空!");
      document.frmLogin.password.focus();
      return false;
    }
    if(judgeVersionOfBrowser()==false){
      alert("本系统需要IE5.5或以上才能正常使用,请升级你的IE浏览器!");
	  return false;
    }
	return true;
  }
//新用户注册
function register(){
   window.location = "servlet/login?operation=<%= LoginServlet.SHOW_REGISTER_PAGE %>";
}
</script>
<!--************************************************-->
</head>
<body bgcolor="#ffffff">
 <br><br> <br>

<!--登录表单开始-->
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
              <div align="right">用户名：</div>
            </td>
            <td width="69%" height="37" align="left" valign="middle">
              <input type="text" name="account" width="100%">
            </td>
          </tr>

          <tr>
            <td width="31%" height="37">
              <div align="right">密&nbsp;&nbsp;码：</div>
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
				 <input type="submit" class="button" value="登录"  onclick="javascript:return checkvalue();">
				 &nbsp;&nbsp;
				 <a href="../apollo/yuebao_setup.exe">客户端软件下载</a>
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
<!--登录表单结束-->
<script language="javascript">
//让用户名输入框获得焦点
frmLogin.account.focus();
</script>
</body>
</html>