<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>

<%
	//防止浏览器缓存本页面
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
<title>用户管理</title>
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
		alert("您不能删除自己");
		return;
	}

	if (window.confirm("确定删除用户选定的用户吗?")) 
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
		alert("请选中需要审核的用户的审核标志!");
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
		<td align="left" colspan=8>输入要查询的用户名:
			<input type="text" name="userName" size=12 value="<%= Convertor.getHTMLString(userName) %>">
                &nbsp;输入要查询的企业名称:<input type="text" size=15 name="enterpriseInfo" value="<%= Convertor.getHTMLString(enterpriseInfo) %>">
			<input type="submit" name="queryBtn" value="查询">
			<input type="button" onclick="addUser()" name="adduser" value="新增用户">
			<input type="button" onclick="displayAllNotPass()" name="notPass" value="显示未开通用户">
			<input type="button" name="passUser" onclick="passUsers()" value="审核通过">
			<input type="button" name="passUser" onclick="exportUser()" value="导出Excel">
		</td>
	</tr>
	</form>
	<tr>
		<td align="center" colspan=8>用户列表</td>
	</tr>
    </table>
    
	<table class="clsContentListTable" cellPadding=0 cellspacing=0 border=0>
		<tr class="clsTrHeader">
			<td width=8%>审核标志</td>
			<td width=11%>用户名</td>
			<td width=25%>企业名称</td>
			<td width=12%>法人代表</td>
			<td width=12%>联系人</td>
			<td width=12%>所属角色</td>
			<!--td width=8%>所属组</td-->
			<td width=15%>操作</td>
		</tr>

		<%
		if(colAllUser == null || colAllUser.size()==0)
		{
		%>
			<tr><td colspan=10 align="center"><img src="../img/infomation.bmp">&nbsp;没有符合条件的用户数据</td></tr>
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
				<a href="javascript:modifyUser('<%= rdf.getUserID().toString() %>',<%=curPage%>)">修改</a>&nbsp;
				<a href="javascript:deleteUser('<%= rdf.getUserID().toString() %>',<%=curPage%>)">删除</a>&nbsp;
				<%
					if(rdf.isValidated() && !(((User)request.getSession().getAttribute("loginUser")).getName().equals(rdf.getName())))
					{
				%>
				<a href="javascript:stopUser('<%= rdf.getUserID().toString() %>')">停用帐号</a>
			   <%}%>
			</td>
		</tr>
             <%i++;}%>
		<tr>
			<td align="right" colspan=8>
				共有用户:<%=maxRowCount%>&nbsp;	每页<%=rowsPerPage%>行&nbsp;当前第<%=curPage%>页/共<%=maxPage%>页
				&nbsp;&nbsp;&nbsp;&nbsp;
			<%if(curPage.equals("1")){%>
				<A disabled>首页</A><A disabled>上一页</A>
			<%}else{%>
				<A HREF="javascript:gotoFirstPage()">首页</A><A HREF="javascript:gotoPrePage()">上一页</A>
			<%}%>

			<%if(curPage.equals(maxPage)){%>
				<A disabled>下一页</A><A disabled>尾页</A>
			<%}else{%>
				<A HREF="javascript:gotoNextPage()">下一页</A><A HREF="javascript:gotoLastPage()">尾页</A>&nbsp;&nbsp;
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