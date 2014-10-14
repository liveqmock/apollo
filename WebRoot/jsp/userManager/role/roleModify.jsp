<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Role"%>
<%@ page import="cn.com.youtong.apollo.usermanager.SetOfPrivileges"%>
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
<title>�޸Ľ�ɫ</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script language="JavaScript">
   function checkForm()
    {
        if (thisForm.roleName.value == "")
        {
        alert("���Ʋ���Ϊ��");
        thisForm.roleName.focus();
        return false;
        }
        return true;
    }
function submit_onclick()
{
      if (checkForm())
      {
         var aInputs=document.getElementsByName("privileges");
         var flag=false;
         for(var i=0;i<aInputs.length;i++){
              if(aInputs[i].checked == true)
              {
                 flag = true;
              }
         }
         if(!flag){
            alert("��ѡ��Ȩ�ޣ�");
             return false;
         }
        thisForm.submit();
        close();
       }
 }
        function return_onclick()
        {
          close();
        }
  </script>
<body class="clsPopu">

  <%
    Role role=(Role)request.getAttribute("roleInfo");
  %>
<form name="thisForm" action="systemOperation?operation=<%=SystemOperationServlet.ROLE_MODIFY%>" method="post" target="main">
<input type="hidden" name="roleID" value="<%=role.getRoleID().intValue()%>">
<br>
<table width=95% height=90% align=center border=0 class="popTable">
            <tr class="clsPopu">
                    <td align="center" >���ƣ�</td>
                    <td><INPUT type="text" name="roleName" value="<%=Convertor.getHTMLString(role.getName())%>" style="HEIGHT: 22px; WIDTH: 130px "></td>
            </tr>
            <tr class="clsPopu">
                    <td align="center" >��ע��</td>
                    <td><INPUT  type="text" name="roleMemo" value="<%=Convertor.getHTMLString(role.getMemo())%>" style="HEIGHT: 22px; WIDTH: 350px" ></td>
             </tr>
             <tr class="clsPopu">
                   <td align="center">
                      ѡ��Ȩ��:
                   </td>
                  <td>
                            <%
                              SetOfPrivileges partPrivileges=(SetOfPrivileges)request.getAttribute("privilegesInfo");
                              for(int i = 0;i < partPrivileges.AVAILABLE_PRIVILEGES_SIZE;i++)
                              {
                                 if(partPrivileges.getPrivilege(i))
                                 {
                            %>
                              <input type="checkbox" name="privileges" value="<%=i%>" checked>&nbsp;&nbsp;<%=SetOfPrivileges.PRIVILEGE_NAMES[i]%>��<%= SetOfPrivileges.PRIVILEGE_MEMOS[i] %>��<br><br>
                            <%}else{%>
                              <input type="checkbox" name="privileges" value="<%=i%>">&nbsp;&nbsp;<%=SetOfPrivileges.PRIVILEGE_NAMES[i]%>��<%= SetOfPrivileges.PRIVILEGE_MEMOS[i] %>��<br><br>
                            <%}}%>
                </td>
          </tr>
<tr class="clsPopu"><td colspan="4"><hr NOSHADE size=1></td></tr>
        <tr class="clsPopu" align="center">
                <td colspan=2>
                   <button class="OutButton" onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="submit_onclick()" id=button1 name=button1>��&nbsp;&nbsp;��</button>
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <button class="OutButton" onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="return_onclick()" id=button2 name=button2>��&nbsp;&nbsp;��</button>
                </td>
        </tr>
</table>
</form>

<script language="javascript">
  thisForm.roleName.focus();
</script>
</body>
</html>


