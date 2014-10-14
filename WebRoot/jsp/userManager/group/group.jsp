<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.User"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
String operation = request.getParameter("operation");
Collection colGroup=(Collection)request.getAttribute("allGroup");
Group group = null;
if(request.getAttribute("userInfo") != null)
{
    group=(Group)request.getAttribute("groupInfo");
}
%>
<html>
<head>
<title>组</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
</head>
<script language="JavaScript">
function createGroup()
{
<%
   Iterator itrGroup=colGroup.iterator();
   while(itrGroup.hasNext())
   {
    Group rdf=(Group)itrGroup.next();
%>
//判断是否重名
form1.groupName.value = trim(form1.groupName.value);
if(form1.groupName.value=="<%=rdf.getName()%>")
    {
     alert("该组已经存在，请重新输入！");
     return false;
   }
<%

   }
%>
  if(isNull(form1.groupName.value))
{
  alert("名称不能为空");
  form1.groupName.focus();
  return false;
}

  form1.groupMemo.value = trim(form1.groupMemo.value);
  form1.userIDs.value = formAllOptionIds("selSelectedUsers");
  form1.operation.value = "<%= SystemOperationServlet.GROUP_ADD %>";
  window.opener.name = "groupList";
  form1.submit();
  window.close();
}

<%
if(operation.equals(SystemOperationServlet.SHOW_GROUP_MODIFY))
{
%>
function modifyGroup()
{
<%
   itrGroup=colGroup.iterator();
   while(itrGroup.hasNext())
   {
    Group rdf=(Group)itrGroup.next();
%>
//判断是否重名
form1.groupName.value = trim(form1.groupName.value);
if(form1.groupName.value=="<%=rdf.getName()%>" && form1.groupName.value != "<%= group.getName() %>")
    {
     alert("该组已经存在，请重新输入！");
     return false;
   }
<%

   }
%>
  if(isNull(form1.groupName.value))
{
  alert("名称不能为空");
  form1.groupName.focus();
  return false;
}

  form1.groupMemo.value = trim(form1.groupMemo.value);
  form1.userIDs.value = formAllOptionIds("selSelectedUsers");
  form1.operation.value = "<%= SystemOperationServlet.GROUP_MODIFY %>";
  window.opener.name = "groupList";
  form1.submit();
  window.close();
}
<%
}
%>

function return_onclick()
{
  close();
}
</script>
<body class="clsPopu">

<br>
<form name="form1" action="systemOperation" method="post" target="groupList">
<input type="hidden" name="userIDs"/>
<input type="hidden" name="operation"/>
<input type="hidden" name="groupID"/>
<input type="hidden" name="curPage" value="<%=request.getParameter("curPage")%>"/>


    <table border="0" width="95%" height="95%" align="center" class="popTable">

              <tr  class="clsPopu">
                      <td align="center">名称：</td>
                      <td><INPUT type="text" name="groupName" style="HEIGHT: 22px; WIDTH: 230px "></td>
              </tr>
              <tr  class="clsPopu">
                      <td align="center">备注：</td>
                      <td><INPUT  type="text" name="groupMemo" style="HEIGHT: 22px; WIDTH: 350px" ></td>
              </tr>
                <tr class="clsPopu">
                  <td colspan=4 align="center">
                         <table border="0" height="350px">
                                <tr height="7%" >
                                    <td nowrap width="45%" align="center">备选用户</td>
                                    </td>
                                    <td nowrap width="10%" align="center">&nbsp;</td>
                                    </td>
                                    <td nowrap width="45%" align="center">已选择的用户</td>
                                    </td>
                                </tr>
                                <tr height="93%">
                                        <td nowrap width="45%" align="center">
                                        <select ondblclick="moveOption('selAllUsers','selSelectedUsers')" size="18" style="width:100%" name="selAllUsers" multiple>
                                          <%
                                           Collection colPartUserInfo=(Collection)request.getAttribute("partUserInfo");
                                           Iterator itrPartUserData=colPartUserInfo.iterator();
                                           while(itrPartUserData.hasNext())
                                              {
                                                User rdfPart=(User)itrPartUserData.next();
                                         %>

                                          <option value="<%=rdfPart.getUserID().intValue()%>"><%=rdfPart.getName()%></option>
                                           <%}%>
                                        </select>
                                        </td>
                                        <td nowrap width="10%" align="center">
                                                <button onclick="moveOption('selAllUsers','selSelectedUsers')" id=button3 name=button3 class="btnStaticNarrow">添加&gt;</button><br><br>
                                                <button onclick="moveOption('selSelectedUsers','selAllUsers')" id=button4 name=button4 class="btnStaticNarrow">&lt;删除</button>
                                        </td>
                                        <td nowrap width="45%" align="center">

                                        <select size="18" style="width:100%" name="selSelectedUsers" ondblclick="moveOption('selSelectedUsers','selAllUsers')" multiple>

                                           <%
                                            Collection colUserInfo=(Collection)request.getAttribute("userInfo");
                                            Iterator itrUserData=colUserInfo.iterator();
                                            while(itrUserData.hasNext())
                                               {
                                                User rdf=(User)itrUserData.next();
                                         %>
                                          <option value="<%=rdf.getUserID().intValue()%>"><%=rdf.getName()%></option>
                                         <%
                                               }
                                         %>

                                        </select>
                                        </td>
                                        </tr>
                         </table>
                </td>
        </tr>
<tr class="clsPopu"><td colspan="4"><hr NOSHADE size=1></td></tr>
         <tr class="clsPopu">
                <td align="center" class="TTitle" colspan="4">
<%
String onclick = null;
if(operation.equals(SystemOperationServlet.SHOW_GROUP_ADD))
{
    onclick = "createGroup()";
}
else if(operation.equals(SystemOperationServlet.SHOW_GROUP_MODIFY))
{
    onclick = "modifyGroup()";
}
%>
                        <button onclick="<%= onclick %>">提&nbsp;&nbsp;交</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <button onclick="return_onclick()">返&nbsp;&nbsp;回</button>
                </td>
        </tr>
  </table>
</form>

<script language="javascript">
<%
if(operation.equals(SystemOperationServlet.SHOW_GROUP_MODIFY))
{
%>
form1.groupID.value = "<%= group.getGroupID() %>";
form1.groupName.value = "<%= group.getName() %>";
form1.groupMemo.value = "<%= group.getMemo() %>";
<%
}
%>
  form1.groupName.focus();
</script>
</body>
</html>


