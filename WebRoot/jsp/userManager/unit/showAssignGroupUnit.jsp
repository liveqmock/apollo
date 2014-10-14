<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css"/>
<title>
   选择可以操作此单位的组
</title>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"> <META HTTP-EQUIV="Expires" CONTENT="0">
<script language="JavaScript">
function addOption()
{
var a = window.dialogArguments;
for(var i=0;i<a.length;i++)
{
   var oOption = document.createElement("OPTION");
      oOption.text=a[i];
      oOption.value=a[i];
      document.all.groupID.add(oOption);
}
}
function submit_onclick()
{
 if(thisForm.groupID.value=="")
{
 alert("请先选择要分配的组！");
 return;
}
   var reGroup=thisForm.groupID.value;
   window.returnValue=reGroup;
   window.close();
}
</script>
</head>
<body onload="addOption()">

<form name="thisForm" method="post" action="unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN%>">

<table style="BACKGROUND-COLOR: whitesmoke;width:100%;height:100%">
      <tr height=5% class="TrHeader" align=center>
         <td align="center">选择需要给单位分配权限的组</td>
       </tr>
     <tr height=90%>
        <td nowrap width="45%" align="center">
           <select size=20 style="width:250px" name="groupID">
           </select>
        </td>
      </tr>
      <tr height=5% align=center>
            <td height="26" align="center" class="TTitle">
                 <button class="OutButton" onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="submit_onclick()" id=button1 name=button1>提&nbsp;&nbsp;交</button>
            </td>
      </tr>
</table>

</form>

</body>
</html>