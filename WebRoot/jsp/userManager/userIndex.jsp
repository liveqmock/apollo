<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>

<%
	//��ֹ��������汾ҳ��
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", 0);

	String userName = request.getParameter("userName");
	String enterpriseInfo = request.getParameter("enterpriseInfo");
	Collection colAllUser=(Collection)request.getAttribute("allUser");
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>�û�����</title>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script Language="JavaScript" src="../jslib/user/user.js"></script>


<script language="JavaScript">
function addUser()
{
	window.open("systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_ADD%>","childAddWin","status=no,height=570,width=650,top=75,left=250");
}

function modifyUser(userID,curpage)
{
   	window.open("systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_MODIFY%>&userID="+userID+"&curPage=" +curpage,"childModifyWin","status=no,height=570,width=650,top=75,left=250");
}

function deleteUser(userID,curPage)
{
	if(userID==<%= RootServlet.getLoginUser(request).getUserID()%>)
	{
		alert("������ɾ���Լ�");
		return;
	}

	if (window.confirm("ȷ��ɾ���û�ѡ�����û���?")) 
		window.open("systemOperation?operation=<%=SystemOperationServlet.USER_DELETE%>&userID="+userID+"&curPage="+curPage,"_self");
}

function query()
{
	checkForm.operation.value = "<%=SystemOperationServlet.QUERY_USER_INFO%>";
	return true;
}

function displayAllNotPass()
{
   window.location = "../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.DISPLAY_ALL_NOT_PASS_USER%>";
}

function stopUser(userID)
{
	window.location = "../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.UNVALIDATE_USER%>"+"&userID="+userID;
}

function passUsers()
{
	var collectionInput=document.getElementsByName("validate");
	var flag=false;
	var url="";
	for(var i=0;i<collectionInput.length;i++)
	{
		if(collectionInput[i].checked == true && collectionInput[i].disabled == false)
		{
			flag=true;
			url = url+"&validate="+collectionInput[i].value;
		}
	}

	if(flag==false)
	{
		alert("��ѡ����Ҫ��˵��û�����˱�־!");
		return false;
	}
	else
	{
		window.location="../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.PASS_ALL_CHECKED_USER%>"+url;
	}
}

function exportUser()
{
    window.open("../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.EXPORT_USER_INFO%>");
}

</script>

</head>

<body>
	<jsp:include page= "/jsp/logo.jsp" />
	<jsp:include page= "/jsp/navigation.jsp" />

	<table class="clsContentListTable">
	<form name="checkForm" action="systemOperation" method="post" onsubmit="return query();">
	<input type="hidden" name="operation">
	<tr>
		<td align="left" colspan=8>����Ҫ��ѯ���û���:
			<input type="text" name="userName" size=12 value="<%= Convertor.getHTMLString(userName) %>">
                &nbsp;����Ҫ��ѯ����ҵ����:<input type="text" size=15 name="enterpriseInfo" value="<%= Convertor.getHTMLString(enterpriseInfo) %>">
			<input type="submit" name="queryBtn" value="��ѯ">
			<input type="button" onclick="addUser()" name="adduser" value="�����û�">
			<input type="button" onclick="displayAllNotPass()" name="notPass" value="��ʾδ��ͨ�û�">
			<input type="button" name="passUser" onclick="passUsers()" value="���ͨ��">
			<input type="button" name="passUser" onclick="exportUser()" value="����Excel">
		</td>
	</tr>
	</form>
	<tr>
		<td align="center" colspan=8>�û��б�</td>
	</tr>
    </table>
    
	<table class="clsContentListTable" cellPadding=0 cellspacing=0 border=0>
		<tr class="clsTrHeader">
			<td width=8%>��˱�־</td>
			<td width=11%>�û���</td>
			<td width=25%>��ҵ����</td>
			<td width=12%>���˴���</td>
			<td width=12%>��ϵ��</td>
			<td width=12%>������ɫ</td>
			<!--td width=8%>������</td-->
			<td width=15%>����</td>
		</tr>

		<%
		if(colAllUser == null || colAllUser.size()==0)
		{
		%>
			<tr><td colspan=10 align="center"><img src="../img/infomation.bmp">&nbsp;û�з����������û�����</td></tr>
		<%
		}
		else
		{
			Object[] userObject= colAllUser.toArray();
			String curPage = (String)request.getAttribute("curPage");
			String maxRowCount = (String)request.getAttribute("maxRowCount");
			String rowsPerPage = (String)request.getAttribute("rowsPerPage");
			String maxPage = (String)request.getAttribute("maxPage");
			int i=0;
			int curPg = Integer.parseInt(curPage);
			int maxPg = Integer.parseInt(maxPage);
			int stopFlag = Integer.parseInt(rowsPerPage)*curPg;
			if(curPg==maxPg)
			{
				stopFlag = Integer.parseInt(maxRowCount);
			}
		
			for(int j=Integer.parseInt(rowsPerPage)*(curPg-1);j<stopFlag;j++)
			{
				User rdf=(User)userObject[j];
				Role role= rdf.getRole();
		%>
		<tr class="<%=((i%2)==1)?"TrLight":"TrDark"%>">
			<td width=8%>
				<input type="checkbox" name="validate" value="<%= rdf.getUserID().toString() %>" <%=(rdf.isValidated())? "checked":""%> <%=(rdf.isValidated())? "disabled":""%>>
			</td>
			<td width=11%><%= Convertor.getHTMLString(rdf.getName())%>&nbsp;</td>
			<td width=25%><%= Convertor.getHTMLString(rdf.getEnterpriseName()) %>&nbsp;</td>
			<td width=12%><%= Convertor.getHTMLString(rdf.getLawPersionName()) %>&nbsp;</td>
			<td width=12%><%= Convertor.getHTMLString(rdf.getContactPersionName()) %>&nbsp;</td>
			<td width=12%><%= role == null ? "" : Convertor.getHTMLString(role.getName()) %>&nbsp;</td>
			<td width=15%>
				<a href="javascript:modifyUser('<%= rdf.getUserID().toString() %>',<%=curPage%>)">�޸�</a>&nbsp;
				<a href="javascript:deleteUser('<%= rdf.getUserID().toString() %>',<%=curPage%>)">ɾ��</a>&nbsp;
				<%
					if(rdf.isValidated() && !(((User)request.getSession().getAttribute("loginUser")).getName().equals(rdf.getName())))
					{
				%>
				<a href="javascript:stopUser('<%= rdf.getUserID().toString() %>')">ͣ���ʺ�</a>
			   <%}%>
			</td>
		</tr>
             <%i++;}%>
		<tr>
			<td align="right" colspan=8>
				�����û�:<%=maxRowCount%>&nbsp;	ÿҳ<%=rowsPerPage%>��&nbsp;��ǰ��<%=curPage%>ҳ/��<%=maxPage%>ҳ
				&nbsp;&nbsp;&nbsp;&nbsp;
			<%if(curPage.equals("1")){%>
				<A disabled>��ҳ</A><A disabled>��һҳ</A>
			<%}else{%>
				<A HREF="javascript:gotoFirstPage()">��ҳ</A><A HREF="javascript:gotoPrePage()">��һҳ</A>
			<%}%>

			<%if(curPage.equals(maxPage)){%>
				<A disabled>��һҳ</A><A disabled>βҳ</A>
			<%}else{%>
				<A HREF="javascript:gotoNextPage()">��һҳ</A><A HREF="javascript:gotoLastPage()">βҳ</A>&nbsp;&nbsp;
			<%}%>
			</td>
		</tr>

		<script language="javascript">
		checkForm.userName.focus();
		function gotoFirstPage()
		{
			window.location="systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_INFO%>";
		}
		function gotoPrePage()
		{
			window.location="systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_INFO%>"+"&curPage="+"<%=String.valueOf(curPg-1)%>";
		}

		function gotoNextPage()
		{
			window.location="systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_INFO%>"+"&curPage="+"<%=String.valueOf(curPg+1)%>";
		}
		function gotoLastPage()
		{
			window.location="systemOperation?operation=<%=SystemOperationServlet.SHOW_USER_INFO%>"+"&curPage="+"<%=maxPage%>";
		}
		</script>

		<%}%>
	</table>
	<jsp:include page= "/jsp/footer.jsp" />
</body>

</html>