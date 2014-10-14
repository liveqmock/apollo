<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="java.util.*"%>

<%
//��ֹ��������汾ҳ��
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
//ȥ���ַ������ߵĿո�
    function trim(inputVal)
{
      inputStr = inputVal.toString();
      while ((inputStr.charAt(inputStr.length - 1) == " ") || (inputStr.charAt(0)== " "))
{
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

//������ַ����Ƿ�Ϊ��
function isNull(inputStr){
      if (inputStr == null || inputStr == "")
      {
        return true;
      }
      return false;
}

 //����û�����
    function submit_onclick()
    {

      if((isNull(trim(document.thisForm.userName.value)))==true)
      {
        alert("�˺����Ʋ���Ϊ�գ�");
        document.thisForm.userName.focus();
        return false;
      }
        if((isNull(trim(document.thisForm.password.value)))==true)
        {
          alert("���벻��Ϊ�գ�");
          document.thisForm.password.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.validatePassword.value)))==true)
        {
          alert("ȷ�����벻��Ϊ�գ�");
          document.thisForm.validatePassword.focus();
          return false;
        }
        if(document.thisForm.password.value!=document.thisForm.validatePassword.value)
        {
          alert("������������벻��ͬ��");
          document.thisForm.password.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.enterpriseName.value)))==true)
        {
          alert("��ҵ���Ʋ���Ϊ�գ�");
          document.thisForm.enterpriseName.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonCode.value)))==true)
        {
          alert("���˴��벻��Ϊ�գ�");
          document.thisForm.lawPersonCode.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonName.value)))==true)
        {
          alert("���˴������Ʋ���Ϊ�գ�");
          document.thisForm.lawPersonName.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.lawPersonPhone.value)))==true)
        {
          alert("���˴���绰����Ϊ�գ�");
          document.thisForm.lawPersonPhone.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPerson.value)))==true)
        {
          alert("��ϵ�˲���Ϊ�գ�");
          document.thisForm.contactPerson.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPersonPhone.value)))==true)
        {
          alert("��ϵ�˵绰����Ϊ�գ�");
          document.thisForm.contactPersonPhone.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.contactPersonAddress.value)))==true)
        {
          alert("��ϵ�˵�ַ����Ϊ�գ�");
          document.thisForm.contactPersonAddress.focus();
          return false;
        }
//��ϵ���ֻ�����Ϊ��
        if((isNull(trim(document.thisForm.postcode.value)))==true)
        {
          alert("�ʱ಻��Ϊ�գ�");
          document.thisForm.postcode.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.fax.value)))==true)
        {
          alert("������벻��Ϊ�գ�");
          document.thisForm.fax.focus();
          return false;
        }
        if((isNull(trim(document.thisForm.email.value)))==true)
        {
          alert("�����ʼ�����Ϊ�գ�");
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
     <fieldset style="width:50%"><legend style='color:0D5502'>���û�ע��(��*��Ϊ������Ŀ)</legend>
     <table border="0" class="Tbody">
     <tr>
          <td class="TdDark" width=100>�û���:</td>
          <td class="TdLight"><input type="text" size=30 name="userName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">����:</td>
          <td class="TdLight"><input type="password" size=30 name="password" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">ȷ������:</td>
          <td class="TdLight"><input type="password" size=30 name="validatePassword" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">��ҵ����:</td>
          <td class="TdLight"><input type="text" size=30 name="enterpriseName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">���˴���:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonCode" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">���˴���:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonName" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">���˴���绰:</td>
          <td class="TdLight"><input type="text" size=30 name="lawPersonPhone" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">��ϵ��:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPerson" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">��ϵ�˵绰:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonPhone" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">��ϵ���ֻ�:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonMobile" value=""></td>
     </tr>
     <tr>
          <td class="TdDark">��ϵ��ַ:</td>
          <td class="TdLight"><input type="text" size=30 name="contactPersonAddress" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">�ʱ�:</td>
          <td class="TdLight"><input type="text" size=30 name="postcode" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">����:</td>
          <td class="TdLight"><input type="text" size=30 name="fax" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">�����ʼ�:</td>
          <td class="TdLight"><input type="text" size=30 name="email" value="">*</td>
     </tr>
     <tr>
          <td class="TdDark">��ע:</td>
          <td class="TdLight"><input type="text" size=30 name="memo" value=""></td>
     </tr>
     <tr>
          <td class="TdLight" align=center colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="submit" name="submit" value="�ύ" onclick="return submit_onclick()">&nbsp;&nbsp;
          <input type="reset" name="reset" value="���">
          &nbsp;&nbsp;<input type="button" name="reset" onclick="gotoLogin()"value="����"></td>
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


