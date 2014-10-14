<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.Group"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
</head>
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<script language="JavaScript">
  window.name = "main";
function groupAdd()
{
  window.open("systemOperation?operation=<%=SystemOperationServlet.SHOW_GROUP_ADD%>","childAdd","status=no,height=500,width=520,top=100,left=250");
}
function modifyGroup(groupID,curpage)
{
  window.open("systemOperation?operation=<%=SystemOperationServlet.SHOW_GROUP_MODIFY%>&groupID="+groupID+"&curPage=" +curpage,"childMdf","status=no,height=500,width=520,top=100,left=250");
}
function deleteGroup(groupID,groupName,curpage)
{
  if (window.confirm("确定删除"+groupName+"吗?")) {
  		window.open("systemOperation?operation=<%=SystemOperationServlet.GROUP_DELETE%>&groupID="+groupID+"&curPage=" +curpage,"_self");
  } else {
  		return;
  }
}
function displayAllGroup(){
    window.location = "systemOperation?operation=<%=SystemOperationServlet.SHOW_GROUP_INFO%>";
}
function queryGroupByID(){


  queryForm.action="systemOperation?operation=<%=SystemOperationServlet.QUERY_GROUP_INFO%>";
  queryForm.submit();
}

function exportGroup()
{
	 window.open("../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.EXPORT_GROUP_INFO%>");
}
</script>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

          <table class="clsContentListTable">
            <form name="queryForm" method="post">
                  <tr class="clsTrContext">
                     <td align="center" colspan=1>根据组名称查询组详细信息</td>
                     <td align="left" colspan=5>&nbsp;&nbsp;&nbsp;&nbsp;输入要查询的组名称:
					 <%
					      String groupName=request.getParameter("groupName");
						  if(groupName==null)
						    groupName="";
					   %>
                        <input type="text" name="groupName" value="<%=groupName%>">
                        <input type="button" onclick="queryGroupByID()" name="queryTxt" value="查询">
                        <input type="button" onclick="groupAdd()" name="addGroup" value="新增组">
                        <input type="button" onclick="displayAllGroup()" name="display" value="显示所有组">
                        <input type="button" onclick="exportGroup()" name="btnExport" value="导出Excel">
                     </td>
                  </tr>
             </form>
                  <tr align="center" class="clsTrContext">
                          <td colspan=6>系统组列表</td>
                  </tr>
         </table>
          <table class="clsContentListTable">
                  <tr class="clsTrHeader">
                      <td>ID号</td>
                      <td>组名称</td>
                      <td>创建日期</td>
                      <td>修改日期</td>
                      <td>备注</td>
                      <td>操作</td>
                    </tr>
                            <%
                             if(((Collection)request.getAttribute("allGroup")).size()!=0){
                               Collection colGroupInfo=(Collection)request.getAttribute("allGroup");
                               Object[] userObject= colGroupInfo.toArray();
                               String curPage = (String)request.getAttribute("curPage");
                               String maxRowCount = (String)request.getAttribute("maxRowCount");
                               String maxPage = (String)request.getAttribute("maxPage");
                               String rowsPerPage = (String)request.getAttribute("rowsPerPage");
                               int i=0;
                               int curPg = Integer.parseInt(curPage);
                               int maxPg = Integer.parseInt(maxPage);
                               int stopFlag = Integer.parseInt(rowsPerPage)*curPg;
                               if(curPg==maxPg){
                                  stopFlag = Integer.parseInt(maxRowCount);
                               }
                               for(int j=Integer.parseInt(rowsPerPage)*(curPg-1);j<stopFlag;j++)
                               {
                                 Group rdf=(Group)userObject[j];
                            %>
                      <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                            <td width=8%><%= Convertor.getHTMLString(rdf.getGroupID().toString())%>&nbsp;</td>
                            <td width=25% nowrap><%= Convertor.getHTMLString(rdf.getName()) %>&nbsp;</td>
                            <td width=13%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateCreated())) %>&nbsp;</td>
                            <td width=13%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateModified())) %>&nbsp;</td>
                            <td width=17%><%= Convertor.getHTMLString(rdf.getMemo()) %>&nbsp;</td>
                            <td width=10%>
                                <a href="javascript:modifyGroup('<%= rdf.getGroupID() %>',<%=curPage%>)">修改</a>&nbsp;
                                <a href="javascript:deleteGroup('<%= rdf.getGroupID() %>','<%= rdf.getName()%>',<%=curPage%>)">删除</a>
                            </td>
                     </tr>
                    <%i++;}%>
             <tr>
                 <td align="right" colspan=6>
                          共有组:<%=maxRowCount%>&nbsp;
                          每页<%=rowsPerPage%>行&nbsp;
                          当前第<%=curPage%>页/
                          共<%=maxPage%>页
                          &nbsp;&nbsp;&nbsp;&nbsp;

                    <%if(curPage.equals("1")){
                    %>
                        <A disabled>首页</A>
                        <A disabled>上一页</A>
                    <%}else{%>
                        <A HREF="javascript:gotoFirstPage()">首页</A>
                        <A HREF="javascript:gotoPrePage()">上一页</A>
                    <%}%>
                    <%if(curPage.equals(maxPage)){
                    %>
                        <A disabled>下一页</A>
                        <A disabled>尾页</A>
                    <%}else{%>
                        <A HREF="javascript:gotoNextPage()">下一页</A>
                        <A HREF="javascript:gotoLastPage()">尾页</A>
                        &nbsp;&nbsp;
                    <%}%>
                 </td>
             </tr>
<script language="javascript">
    queryForm.groupName.focus();
function gotoFirstPage(){
queryForm.action="systemOperation?operation=<%=SystemOperationServlet.QUERY_GROUP_INFO%>";
  queryForm.submit();
}
function gotoPrePage(){
queryForm.action="systemOperation?operation=<%=SystemOperationServlet.QUERY_GROUP_INFO%>"+"&curPage="+"<%=String.valueOf(curPg-1)%>";
  queryForm.submit();
}
function gotoNextPage(){
queryForm.action="systemOperation?operation=<%=SystemOperationServlet.QUERY_GROUP_INFO%>"+"&curPage="+"<%=String.valueOf(curPg+1)%>";
  queryForm.submit();
}
function gotoLastPage(){
queryForm.action="systemOperation?operation=<%=SystemOperationServlet.QUERY_GROUP_INFO%>"+"&curPage="+"<%=maxPage%>";
  queryForm.submit();
}
</script>
            <%}else{%>
              <tr><td colspan=10 align="center"><img src="../img/infomation.bmp">&nbsp;没有符合条件的组</td></tr>
            <%}%>
         </table>

  <jsp:include page= "/jsp/footer.jsp" />

</body>
</html>