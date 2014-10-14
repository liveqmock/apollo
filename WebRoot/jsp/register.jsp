<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="java.util.*"%>

<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
</head>
<script language="JavaScript">
//去掉字符串两边的空格
    function trim(inputVal)
{
      inputStr = inputVal.toString();
      while ((inputStr.charAt(inputStr.length - 1) == " ") || (inputStr.charAt(0)== " "))
{
        if (inputStr.charAt(inputStr.length - 1) == " ") //字符串后面有空格
        {
          inputStr = inputStr.substring(0,inputStr.length - 1);
        }
        if (inputStr.charAt(0) == " ")//字符串前面有空格
        {
          inputStr = inputStr.substring(1,inputStr.length);
        }
      }
      return inputStr;
    }

//输入的字符串是否为空
function isNull(inputStr){
      if (inputStr == null || inputStr == "")
      {
        return true;
      }
      return false;
}

 //检查用户输入
    function submit_onclick()
    {

      if((isNull(trim(document.thisForm.userName.value)))==true)
      {
        alert("账号名称不能为空！");
        document.thisForm.userName.focus();
        return false;
      }
        if((isNull(trim(document.thisForm.password.value)))==true)
        {
          alert("密码不能为空！");
          document.thisForm.password.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.validatePassword.value)))==true)
        {
          alert("确认密码不能为空！");
          document.thisForm.validatePassword.focus();
          return false;
        }
        if(document.thisForm.password.value!=document.thisForm.validatePassword.value)
        {
          alert("输入的两次密码不相同！");
          document.thisForm.password.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.enterpriseName.value)))==true)
        {
          alert("企业名称不能为空！");
          document.thisForm.enterpriseName.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonCode.value)))==true)
        {
          alert("法人代码不能为空！");
          document.thisForm.lawPersonCode.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonName.value)))==true)
        {
          alert("法人代表名称不能为空！");
          document.thisForm.lawPersonName.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonPhone.value)))==true)
        {
          alert("法人代表电话不能为空！");
          document.thisForm.lawPersonPhone.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPerson.value)))==true)
        {
          alert("联系人不能为空！");
          document.thisForm.contactPerson.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPersonPhone.value)))==true)
        {
          alert("联系人电话不能为空！");
          document.thisForm.contactPersonPhone.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPersonAddress.value)))==true)
        {
          alert("联系人地址不能为空！");
          document.thisForm.contactPersonAddress.focus();
          return false;
        }
//联系人手机可以为空
        if((isNull(trim(document.thisForm.postcode.value)))==true)
        {
          alert("邮编不能为空！");
          document.thisForm.postcode.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.fax.value)))==true)
        {
          alert("传真号码不能为空！");
          document.thisForm.fax.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.email.value)))==true)
        {
          alert("电子邮件不能为空！");
          document.thisForm.email.focus();
          return false;
        }
  //thisForm.submit();
       return true;
}
</script>
<body>

<jsp:include page= "/jsp/logo.jsp" />

<form name="thisForm" action="login?operation=<%= LoginServlet.REGISTER_USER %>" method="post">

<table align="center" border="0" cellpadding="0" cellspacing="0" bordercolordark="white" bordercolorlight="black" class="clsContentTable">
 <tr align="center" height="300" >
                <td nowrap class="Tbody">
     <fieldset style="width:50%"><legend style='color:0D5502'>新用户注册(带*号为必填项目)</legend>
     <table border="0" class="Tbody">
     <tr>
          <td class="TdDark" width=100>用户名:</td>
          <td class="TdLight"><input type="text" size=30 name="userName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">密码:</td>
          <td class="TdLight"><input type="password" size=30 name="password" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">确认密码:</td>
          <td class="TdLight"><input type="password" size=30 name="validatePassword" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">企业名称:</td>
          <td class="TdLight"><input type="text" size=30 name="enterpriseName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">法人代码:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonCode" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">法人代表:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">法人代表电话:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonPhone" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">联系人:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPerson" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">联系人电话:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonPhone" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">联系人手机:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonMobile" value=""></td>
     </tr>
     <tr>
          <td class="TdDark">联系地址:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonAddress" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">邮编:</td>
          <td class="TdLight"><input type="text" size=30 name="postcode" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">传真:</td>
          <td class="TdLight"><input type="text" size=30 name="fax" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">电子邮件:</td>
          <td class="TdLight"><input type="text" size=30 name="email" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">备注:</td>
          <td class="TdLight"><input type="text" size=30 name="memo" value=""></td>
     </tr>
     <tr>
          <td class="TdLight" align=center colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="submit" name="submit" value="提交" onclick="return submit_onclick()">&nbsp;&nbsp;
          <input type="reset" name="reset" value="清空">
          &nbsp;&nbsp;<input type="button" name="reset" onclick="gotoLogin()"value="返回"></td>
     </tr>
  </table>
 </fieldset>
</form>


  <jsp:include page= "/jsp/footer.jsp" />
<script language="javascript">
  thisForm.userName.focus();
function gotoLogin(){
  history.back(-1);
}
</script>
</body>
</html>


