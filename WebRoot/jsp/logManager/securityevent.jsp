<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.log.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>事件列表</title>
</head>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
  <table class="clsContentListTable">
    <tr class="clsTrHeader">
      <td nowrap>类型</td>
      <td nowrap>时间</td>
      <td nowrap>用户</td>
      <td nowrap>来源</td>
      <td nowrap>说明</td>
    </tr>
<%
              
                       Collection eventCol=(Collection)request.getAttribute("eventCol");
                       Object[] userObject= eventCol.toArray();
                       String curPage = (String)request.getAttribute("curPage");
                       String maxRowCount = (String)request.getAttribute("maxRowCount");
                       String rowsPerPage = (String)request.getAttribute("rowsPerPage");
                       String maxPage = (String)request.getAttribute("maxPage");
                       int curPg = Integer.parseInt(curPage);


                       for(int i=0;i<userObject.length;i++)
                       {
                         Event event = (Event)userObject[i];
%>
    <tr <%= (((i % 2) == 1) ? "class=\"TrLight\"" : "class=\"TrDark\"") %>>
      <td nowrap><%= Convertor.getHTMLString(getTypeName(event.getType()))%>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(Convertor.date2String(event.getTimeOccured())) %>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(event.getUserName()) %>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(event.getSource()) %>&nbsp;</td>
      <td ><%= Convertor.getHTMLString(event.getMemo()) %>&nbsp;</td>
    </tr>
<%
}
%>
                   <tr>
                       <td align="right" colspan=8>
                                共有记录:<%=maxRowCount%>&nbsp;
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
      function gotoFirstPage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_SECURITY_LOG_PAGE %>";
      }
      function gotoPrePage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_SECURITY_LOG_PAGE %>"+"&curPage="+"<%=String.valueOf(curPg-1)%>";
      }
      function gotoNextPage(){

      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_SECURITY_LOG_PAGE %>"+"&curPage="+"<%=String.valueOf(curPg+1)%>";
      }
      function gotoLastPage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_SECURITY_LOG_PAGE %>"+"&curPage="+"<%=maxPage%>";
      }
      </script>

  </table>

  <jsp:include page= "/jsp/footer.jsp" />

</body>
</html>
<%!
/**
 * 得到事件类型的名称
 * @param type 事件类型
 * @return 事件类型的名称
 */
public String getTypeName(int type)
{
    switch(type)
    {
        case Event.AUDIT_FAIL     : return "失败审核";
        case Event.AUDIT_SUCCESS  : return "成功审核";
        case Event.ERROR          : return "错误";
        case Event.INFO           : return "信息";
        case Event.WARNING        : return "警告";
        default                   : return "未知";
    }
}
%>