<%@ page contentType="text/html; charset=GBK"%>

<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Role"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>

<% //��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script language="JavaScript">
function roleAdd()
{
 window.name="main";
  window.open("systemOperation?operation=<%=SystemOperationServlet.SHOW_ROLE_ADD%>","childAdd","status=no,height=400,width=570,top=120,left=210");
}

function modifyRole(roleID)
{
  window.open("systemOperation?operation=<%= SystemOperationServlet.SHOW_ROLE_MODIFY%>&roleID="+roleID,"childMdf","status=no,height=400,width=570,top=120,left=210");
}

function deleteRole(roleID)
{
    <%
    Collection colAllRole=(Collection)request.getAttribute("allRole");
	Iterator itrAllRole=colAllRole.iterator();
	while(itrAllRole.hasNext()) {
		Role role=(Role)itrAllRole.next();
    %>
		if(roleID==<%=role.getRoleID().intValue()%>) {
                    if (window.confirm("ȷ��ɾ��<<%= role.getName()%>>��?")) {
                            window.open("systemOperation?operation=<%= SystemOperationServlet.ROLE_DELETE%>&roleID="+roleID,"_self");
                    } else {
                            return;
                    }
		}
	<% } %>
}


function exportExcel()
{
    window.open("../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.EXPORT_ROLE_INFO%>");
}

</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

    <table class="clsContentListTable">
	          <tr class="clsTrContext">
                        <td width=15%></td>
                        <td width=15%></td>
                        <td width=15%></td>
                        <td width=15%></td>
                        <td width=20%>  <input type="button" onclick="roleAdd()" name="queryBtn" value="������ɫ"></td>
                        <td width=20%>
                           <input type="button" onclick="exportExcel()" name="btnExcel" value="����Excel">
                        </td>
                 </tr>
                  <tr align="center" class="clsTrContext">
                          <td colspan=6>ϵͳ��ɫ��Ϣ�б�</td>
                  </tr>
                   <tr class="clsTrHeader">
                      <td width=8%>ID��</td>
                      <td width=19%>��ϸ��Ϣ</td>
                      <td width=15%>��������</td>
                      <td width=15%>�޸�����</td>
                      <td width=20%>��ע</td>
                      <td width=20%>����</td>
                    </tr>
                           <% Collection colRole=(Collection)request.getAttribute("allRole");
                                 Iterator iteRole=colRole.iterator();
                                 int i=0;
                                 while(iteRole.hasNext())
                                 {
                                   Role rdf=(Role)iteRole.next();

                           %>
                  <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                        <td width=15%><%= Convertor.getHTMLString(rdf.getRoleID().toString())%>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(rdf.getName()) %>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateCreated())) %>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateModified())) %>&nbsp;</td>
                        <td width=20%><%= Convertor.getHTMLString(rdf.getMemo()) %>&nbsp;</td>
                        <td width=20%>
                            <a href="javascript:modifyRole('<%= rdf.getRoleID() %>')">�޸�</a>&nbsp;
                            <a href="javascript:deleteRole('<%= rdf.getRoleID() %>')">ɾ��</a>
                        </td>
                 </tr>
                    <%i++;}%>
                 <tr class="clsTrContext">
                        <td></td>
                 </tr>
               
        </table>

  <jsp:include page= "/jsp/footer.jsp" />

  </body>
</html>
