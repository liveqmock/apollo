<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Group"%>
<%@ page import="cn.com.youtong.apollo.data.*"%>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<%
  String unitID=(String)request.getAttribute("unitID");
  String taskID=(String)request.getAttribute("taskID");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script language="JavaScript">
function addGroup()
{
      var varGroup=new Array()
      <%
         Collection colAllGroup=(Collection)request.getAttribute("group");
         Iterator itrAllGroup=colAllGroup.iterator();
         for(int i=0;i<colAllGroup.size();i++)
      {
         Group reAllGroup=(Group)itrAllGroup.next();
      %>
        varGroup[<%=i%>]="<%=reAllGroup.getName()%>";
      <%}%>
      var varGroupName=window.showModalDialog("../jsp/userManager/unit/showAssignGroupUnit.jsp",varGroup,"dialogWidth=80,dialogLeft=100,dialogTop=100,edge=sunken,center=yes,status=no,unadorned=yes");

      if (varGroupName == null || varGroupName.length == 0 || varGroupName == "undefined")
              return;
      var url = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN%>&groupName="+encodeURIComponent(varGroupName)+"&taskID="+thisForm.taskID.value+"&unitID="+thisForm.unitID.value;
      window.location = encodeURI(url);
}
function deleteGroup()
{
       if(thisForm.groupID.value=="")
      {
        alert("请先选择组");
        return;
      }
      var put=parent.document.all.tags("input");
      var button=parent.document.all("btnChange");
      button.disabled=true;
      for (var i=0;i<put.length;i++)
      {
       if (put[i].type=="checkbox")
       {
         put[i].disabled=true;
         put[i].checked=false;
       }
      }
        window.open("unitPermission?operation=<%=UnitPermissionServlet.DELETE_UNIT_PERMISSION%>&groupID="+thisForm.groupID.value+"&taskID="+thisForm.taskID.value+"&unitID="+thisForm.unitID.value,"_self");
}

function getPermission()
{
	if(thisForm.groupID.value=="") {
		return;
	}
  var unitRightRead=parent.document.all("unitRightRead");
  var unitRightWrite=parent.document.all("unitRightWrite");
  var button=parent.document.all("btnChange");
  button.disabled=false;
  unitRightRead.disabled=false;
  unitRightWrite.disabled=false;
<%
     Collection colAssignedGroup1=(Collection)request.getAttribute("assignedGroup");
     Iterator itrGroup1=(Iterator)colAssignedGroup1.iterator();
     while(itrGroup1.hasNext())
    {
       UnitAssignment rdf1=(UnitAssignment)itrGroup1.next();
%>
    if(thisForm.groupID.value==<%=rdf1.getGroup().getGroupID().intValue()%>){
       <%
         if(rdf1.getUnitPermission().getPermission(UnitPermission.UNIT_PERMISSION_READ))
       {%>
         unitRightRead.checked=true;
       <%}else{%>
         unitRightRead.checked=false;
      <%}%>
    }
if(thisForm.groupID.value==<%=rdf1.getGroup().getGroupID().intValue()%>){
       <%
         if(rdf1.getUnitPermission().getPermission(UnitPermission.UNIT_PERMISSION_WRITE))
       {%>
         unitRightWrite.checked=true;
       <%}else{%>
         unitRightWrite.checked=false;
      <%}%>
    }

<%
    }
%>
}
//清空页面
function clearPage()
{
  window.open("unitPermission?operation=<%=UnitPermissionServlet.INFO_PAGE%>","_self");
}
</script>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
</head>
<body>
<form name="thisForm">
<input type="hidden" name="unitID" value="<%=unitID%>">
<input type="hidden" name="taskID" value="<%=taskID%>">
<table style="width:100%;height:100%">
        <tr height=5%>
             <td width="100%" align="center">选择组为单位:<%=(String)request.getSession().getAttribute("unitName")%> 分配权限</td>
       </tr>
       <tr height=90%>
          <td height=89% align="center">
                <select size=13 style="width:250px;height=100%" name="groupID" onclick="getPermission();">
                <%

                   Collection colAssignedGroup=(Collection)request.getAttribute("assignedGroup");
                   Iterator itrGroup= colAssignedGroup.iterator();
                   while(itrGroup.hasNext())
                  {
                    UnitAssignment rdf=(UnitAssignment)itrGroup.next();
                %>
                <option value="<%=rdf.getGroup().getGroupID()%>"><%=rdf.getGroup().getName()%></option>
               <%}%>
                </select>
          </td>
      </tr height=5%>
      <tr align=center>
          <td align=center><button onclick="addGroup();" id=buttonAdd name=buttonAdd class="btnStaticNarrow" >添加组</button>
          &nbsp;&nbsp;<button onclick="deleteGroup();" id=buttonDelete name=buttonDelete class="btnStaticNarrow">删除组</button></td>
      </tr>
  </table>
</form>

</body>
</html>