<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>
<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<title>������ɫ</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
</head>
<script language="JavaScript">
function checkForm()
{
var roleName = document.getElementById("roleName").value;
      if(trim(roleName)=="")
      {
         alert("��ɫ���Ʋ���Ϊ��!");
         document.getElementById("roleName").focus();
         return false;
      }
      <%
         Collection colRole=(Collection)request.getAttribute("allRole");
         Iterator itrRole=colRole.iterator();
         while(itrRole.hasNext())
         {
          Role rdf=(Role)itrRole.next();
      %>
      //���û������Ʊ��浽������
      if(roleName=="<%=rdf.getName()%>")
          {
           alert("�ý�ɫ�Ѿ����ڣ�����������!");
           return false;
         }
      <%
         }
      %>
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


<br>
<form name="thisForm" action="systemOperation?operation=<%=SystemOperationServlet.ROLE_ADD%>" method="post" target="main">
<input type="hidden" name="userID" value="<%=request.getParameter("userID")%>">
<table width=95% height=90% align=center border=0 class="popTable">
            <tr class="clsPopu" width="20%">
                    <td align="center">���ƣ�</td>
                    <td><INPUT type="text" id="roleName" name="roleName" style="HEIGHT: 22px; WIDTH: 130px "></td>
            </tr>
            <tr class="clsPopu" width="20%">
                    <td align="center">��ע��</td>
                    <td><INPUT  type="text" name="roleMemo" style="HEIGHT: 22px; WIDTH: 350px" ></td>
             </tr>
             <tr class="clsPopu" width="20%">
                   <td align="center">
                      ѡ��Ȩ�ޣ�
                   </td>
                  <td>
                            <%
                              SetOfPrivileges pSet = new  SetOfPrivileges();
                              for(int i = 0;i < pSet.AVAILABLE_PRIVILEGES_SIZE;i++)
                              {
                                 if(!pSet.getPrivilege(i))
                                 {
                           %>
                              <input type="checkbox" name="privileges" value="<%=i%>"><%=SetOfPrivileges.PRIVILEGE_NAMES[i]%>��<%= SetOfPrivileges.PRIVILEGE_MEMOS[i] %>��<br><br>
                            <%
                                 }
                              }
                            %>
                </td>
        </tr>
<tr class="clsPopu"><td colspan="4"><hr NOSHADE size=1></td></tr>
        <tr class="clsPopu" align="center">
                <td colspan=2>
                   <button class="OutButton" onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="submit_onclick()" id=button1 name=button1>��&nbsp;&nbsp;��</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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


