<%@ page contentType="text/html; charset=GB2312" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>
<%
  String tableName="userinfo";
  response.reset();
  response.setContentType("application/vnd.ms-excel;charset=GB2312");
  response.setHeader("Content-disposition",
                       "attachment; filename=" + tableName +
                       ".xls");
  
   Collection colAllUser=(Collection)request.getAttribute("alluser");
%>




<table  cellPadding=0 cellspacing=0 border=0>
		<tr >
			<td width=8%>审核标志</td>
			<td width=11%>用户名</td>
			<td width=25%>企业名称</td>
			<td width=12%>法人代表</td>
			<td width=12%>联系人</td>
			<td width=12%>所属角色</td>
		</tr>

		<%
		if(colAllUser == null || colAllUser.size()==0)
		{
		%>
			<tr><td colspan=10 align="center">没有符合条件的用户数据</td></tr>
		<%
		}
		else
		{
			Object[] userObject= colAllUser.toArray();
			
		
			for(int j=0;j<userObject.length;j++)
			{
				User rdf=(User)userObject[j];
				Role role= rdf.getRole();
		%>
		<tr >
			
			<td width=11%><%= Convertor.getHTMLString(rdf.getName())%>&nbsp;</td>
			<td width=25%><%= Convertor.getHTMLString(rdf.getEnterpriseName()) %>&nbsp;</td>
			<td width=12%><%= Convertor.getHTMLString(rdf.getLawPersionName()) %>&nbsp;</td>
			<td width=12%><%= Convertor.getHTMLString(rdf.getContactPersionName()) %>&nbsp;</td>
			<td width=12%><%= role == null ? "" : Convertor.getHTMLString(role.getName()) %>&nbsp;</td>
			
			</td>
		</tr>
         <%}%>
	   <%}%>
	</table>
	      