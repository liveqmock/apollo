<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="csslib/main.css" rel="stylesheet" type="text/css">
<title>
�ӱ�����ί��ҵ�챨-�û���¼
</title>
<style type="text/css">
body,a,div,p,h1,h2,h3,h4,h5,h6,form,input,img{ margin:0px; padding:0px;}
.clear{ font-size:0px; width:0px; height:0px; margin:0px; padding:0px; line-height:0px; clear:both;}
body{ background:#56b6f3;margin-left:auto;margin-right:auto;}
#page{ width:1000px; background:url(images/login_02.jpg) no-repeat; margin:0px auto;}
#page .text{font:30px ����; color:#084e8f; width:580px; padding-top:115px; padding-left:30px; font-weight:bold;}
#page .link a{ font:14px ����; display:block; width:130px; margin-left:570px; text-decoration:none; color:#012b40;}
#page .link a:hover{ text-decoration:underline;}
.form{ margin:80px 0 0 10px; width:630px;}
.form .left{width:450px; float:left; margin-top:23px;}
.form .left p {margin:10px;}
.form .left p span{ color:#FFFFFF; font:14px ����; display:block; height:32px; width:45px; float:left; line-height:32px; padding-right:2px;}
.form .left p input{ font:14px ����; border-top:none; border-left:none; border-bottom:none; border-right:none; background:url(images/login_03.gif) no-repeat; width:381px; height:32px; padding:10px;}

.form .right{ width:169px; float:right;}

.bottom{ margin-left:60px; width:690px; margin-top:80px;}
.bottom .bot_left{ float:left;}
.bottom .bot_right{ width:260px; float:left; margin-left:45px; float:right;}
.bottom .bot_right div.text01{ float:left;}
.bottom .bot_right div.text02{ float:left; margin-left:30px;}
.bottom .bot_right div p{ font:12px ����; color:#cce4fb; line-height:1.6em;}
</style>
<SCRIPT language=javascript src="js/public.js"></SCRIPT>
<SCRIPT language=JavaScript>
  getRidOfParentFrame();
  //Name: trim(����ַ���)
  function trim(inputVal){
    inputStr = inputVal.toString();
    while ((inputStr.charAt(inputStr.length - 1) == " ") || (inputStr.charAt(0)== " ")){
      if (inputStr.charAt(inputStr.length - 1) == " ") //�ַ��������пո�
      {
        inputStr = inputStr.substring(0,inputStr.length - 1);
      }
      if (inputStr.charAt(0) == " ")//�ַ���ǰ���пո�
      {
        inputStr = inputStr.substring(1,inputStr.length);
      }
    }
    return inputStr;
  }

  //Name: isNull��������ַ����Ƿ�Ϊ�գ�
  function isNull(inputStr) {
    if (inputStr == null || inputStr == "") {
      return true;
    }
    return false;
  }

//Name: checkvalue(����û�����)
  function checkvalue()
  {
    if((isNull(trim(document.frmLogin.account.value)))==true)
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
    if((isNull(trim(document.frmLogin.password.value)))==true)
    {
      alert("���벻��Ϊ��!");
      document.frmLogin.password.focus();
      return false;
    }
    if(judgeVersionOfBrowser()==false){
      alert("��ϵͳ��ҪIE5.5�����ϲ�������ʹ��,���������IE�����!");
    }
  }
//���û�ע��
function register(){
   window.location = "servlet/login?operation=showRegisterPage";
}
</SCRIPT>
</head>

<body>
<center>
	<div id="page">
		<div class="text">�ӱ�ʡ����ί��ҵ����챨</div>
		<div class="link"><a href="http://www.hbsa.gov.cn:8080/setup2010.zip">�ͻ����������</a></div>
		<form id="frmLogin" name="frmLogin"  method="post" action="servlet/login?operation=<%= LoginServlet.LOGIN %>">
		<div  class="form">
			<div class="left">
				<p><span>�û���</span><input type="text" name="account"/></p>				
				<p><span>��&nbsp;��</span><input type="password" name="password"/></p>
			</div>
			<div class="right"><input type="image" src="images/login02.jpg" onClick="javascript:return checkvalue();"/></div>
			<div class="clear"></div>
		</div>
		</form>
		<div class="bottom">
			<div class="bot_left"><a href="http://www.hbsa.gov.cn" target="_blank"><img src="images/logo01.jpg" border="0"/></a>&nbsp;&nbsp;<a href="http://www.yotop.com" target="_blank"><img src="images/logo02.jpg"  alt="������ͨ"  border="0"/></a></div>
			<div class="bot_right">
				<div class="text01"><p>�ӱ�ʡ����ί����ල�뿼�����۴�  ����</p><p>����������ͨ����������޹�˾&nbsp;&nbsp;&nbsp;&nbsp;����</p><p>�� ַ��www.yotop.com</p><p>�� ����   15010636217</p><p>QQȺ�ţ�  93774691   </p></div>
			</div>
			<div class="clear"></div>
		</div>
		
	</div>
<center>
</body>
</html>
