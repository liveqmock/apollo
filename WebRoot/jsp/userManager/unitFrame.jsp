<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="cn.com.youtong.apollo.task.*"%>
<%@ page import="cn.com.youtong.apollo.data.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Group"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>
<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>

<html>
<head>
<Style Type="text/css">
.clsUnitTree{
    OVERFLOW: auto;
    HEIGHT: 100%
       }
</Style>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>Ȩ�޷���</title>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<script type="text/javascript" src="../jslib/calendar/popcalendar.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_layers.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_menu.js"></script>
<script type="text/javascript" src="../jslib/public.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
</head>
<script language="JavaScript">
var sourceArray = new Array;
//��ʾ�������Ȩ��
function displayRightList(obj){
      selectIndexOpt = obj.options[obj.selectedIndex].value;
      var url = "unitPermission?operation=<%=UnitPermissionServlet.DISPLLAY_GROUP_RIGHT%>";
      url += "&groupID="+selectIndexOpt;
      window.location=url;
}
function deleteRight(unitID){
   changeForm.groupID.value = <%=(String)request.getAttribute("groupID")%>;
   changeForm.unitID.value = unitID;
   changeForm.action = "unitPermission?operation=<%=UnitPermissionServlet.DELETE_GROUP_RIGHT%>";
   changeForm.submit();
}
function changeRight(unitID){
   var flag = document.getElementById("write"+unitID).checked;
   changeForm.groupID.value = <%=(String)request.getAttribute("groupID")%>;
   changeForm.unitID.value = unitID;
   changeForm.writeFlag.value = flag;
   changeForm.action = "unitPermission?operation=<%=UnitPermissionServlet.CHANGE_RIGHT%>";
   changeForm.submit();
}
function addUnitRight(){
  unitTree.style.visibility= "visible";
}

</script>
<body scroll=no>

<form name="thisForm" method="post" style="margin:0">
<table width=100% height=99% border=0>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=3 valign="middle">
            �������-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">Ȩ�޹���</font>
         </td>
       </tr>
     <tr>
        <td width=30% height="100%" valign="top" >
           <SELECT id="groupSelect" name="groupSelect" onChange="displayRightList(this);" style="width: 100%;height:100%" size="2">
                    <%
                      Collection groups = (Collection)request.getAttribute("groups");
                      Iterator groupItr = groups.iterator();
                      for(int i=0; groupItr.hasNext(); i++)
                     {
                        Group group = (Group)groupItr.next();
                        String groupID = (String)request.getAttribute("groupID");
                        if(groupID!=null && groupID.equals(group.getGroupID().toString())){
                    %>
                      <OPTION VALUE='<%=group.getGroupID()%>' selected><%=group.getName()%></OPTION>
                    <%
                     }else{
                    %>
                      <OPTION VALUE='<%=group.getGroupID()%>' ><%=group.getName()%></OPTION>
                    <%}}%>

           </SELECT>
		 
        </td>
        <td width="30%" valign="top">
             <table width=100% height=100%>
                  <tr height=5%>
                    <td>
                      ѡ��Ȩ��λ:
                       <input type=button value="��Ȩ��" onclick="endueRight('1');">
                       <input type=button value="��дȨ��" onclick="endueRight('2');">
                    </td>
                  </tr>
                  <tr  height=85%><td>
                    <div class="clsTreeDiv" style="height:98%">
                      <%=(String)request.getAttribute("checkboxUnitTree")%>
                    </div></td>
                  </tr>
             </table>
        </td>
      <td valign="top" width="40%">
        
		<div class="clsTreeDiv" style="BORDER-TOP: thin groove;OVERFLOW:auto;width:100%;height:97%">
        <table width=100% cellPadding=0 cellspacing=0 border=0>
             <tr>
                <td colspan=5 align="center">
                    Ȩ���б�
                </td>
              </tr>
             <tr style="BACKGROUND-COLOR: #e7e7e4">
                <td width=13%>��λID</td>
                <td width=40%>��λ����</td>
                <td width=5% nowrap>��</td>
                <td width=5% nowrap>д</td>
                <td width=10% nowrap>����</td>
              </tr>
                        <%
                         if(request.getAttribute("groupPermissions")!=null){
                           if(((Collection)request.getAttribute("groupPermissions")).size()!=0){
                           Iterator groupPermissionsItr = ((Collection)request.getAttribute("groupPermissions")).iterator();
                           for(int i=0;groupPermissionsItr.hasNext();i++)
                           {
                             UnitAssignment unitAssignment=(UnitAssignment)groupPermissionsItr.next();
                        %>
                  <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                        <td><%= Convertor.getHTMLString(unitAssignment.getUnitID())%>&nbsp;</td>
                        <td ><%= Convertor.getHTMLString(unitAssignment.getUnitName())%>&nbsp;</td>
                     <%if (unitAssignment.getUnitPermission().getPermission((int)1)){%>
                        <td><input type=checkbox id='read<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>' checked disabled></td>
                     <%}else{%>
                        <td><input type=checkbox id='read<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>' disabled></td>
                     <%}%>
                      <%if (unitAssignment.getUnitPermission().getPermission((int)2)){%>
                        <td><input type=checkbox id='write<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>' checked></td>
                     <%}else{%>
                        <td><input type=checkbox id='write<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>'></td>
                     <%}%>
                        <td nowrap>
                            <a href="javascript:changeRight('<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>')">����</a>
                            <a href="javascript:deleteRight('<%=Convertor.getHTMLString(unitAssignment.getUnitID())%>')">ɾ��</a>
                        </td>
                 </tr>
                        <%}}}%>

            </table>
</div>
        </td>

      </tr>
  </table>
<input type="hidden" name="groupID">
<input type="hidden" name="rightFlag">
</form>

<script language="javascript">
function endueRight(flag){

 var unitArray = new Array;
 unitArray = getCheckedValuesOfCheckboxTree();
 if(thisForm.groupSelect.value==""){
    alert("��ѡ����Ҫ��Ȩ�޵��飡");
    return false;
 }
  if(unitArray==null || unitArray==""){
     alert("��ѡ��λ!");
     return false;
  }
  thisForm.groupID.value = thisForm.groupSelect.value
  for(var i=0;i<unitArray.length;i++){
     var obj = document.createElement("input");
     obj.type = "hidden";
     obj.name = "unitArray";
     obj.value = unitArray[i];
     thisForm.appendChild(obj);
  }
  thisForm.action = "unitPermission?operation=<%=UnitPermissionServlet.ENDUE_RIGHT%>";
  if(flag=="1"){
   thisForm.rightFlag.value = "1";
  }else{
   thisForm.rightFlag.value = "2";
  }
  thisForm.submit();
}
</script>

<form name="changeForm" method="post" style="margin:0">
<input type="hidden" name="groupID">
<input type="hidden" name="unitID">
<input type="hidden" name="writeFlag">
</form>

</body>
</html>

