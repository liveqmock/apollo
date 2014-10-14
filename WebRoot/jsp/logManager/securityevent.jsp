<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.log.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>�¼��б�</title>
</head>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
  <table class="clsContentListTable">
    <tr class="clsTrHeader">
      <td nowrap>����</td>
      <td nowrap>ʱ��</td>
      <td nowrap>�û�</td>
      <td nowrap>��Դ</td>
      <td nowrap>˵��</td>
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
                                ���м�¼:<%=maxRowCount%>&nbsp;
                                ÿҳ<%=rowsPerPage%>��&nbsp;
                                ��ǰ��<%=curPage%>ҳ/
                                ��<%=maxPage%>ҳ
                                &nbsp;&nbsp;&nbsp;&nbsp;

                          <%if(curPage.equals("1")){
                          %>
                              <A disabled>��ҳ</A>
                              <A disabled>��һҳ</A>
                          <%}else{%>
                              <A HREF="javascript:gotoFirstPage()">��ҳ</A>
                              <A HREF="javascript:gotoPrePage()">��һҳ</A>
                          <%}%>
                          <%if(curPage.equals(maxPage)){
                          %>
                              <A disabled>��һҳ</A>
                              <A disabled>βҳ</A>
                          <%}else{%>
                              <A HREF="javascript:gotoNextPage()">��һҳ</A>
                              <A HREF="javascript:gotoLastPage()">βҳ</A>
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
 * �õ��¼����͵�����
 * @param type �¼�����
 * @return �¼����͵�����
 */
public String getTypeName(int type)
{
    switch(type)
    {
        case Event.AUDIT_FAIL     : return "ʧ�����";
        case Event.AUDIT_SUCCESS  : return "�ɹ����";
        case Event.ERROR          : return "����";
        case Event.INFO           : return "��Ϣ";
        case Event.WARNING        : return "����";
        default                   : return "δ֪";
    }
}
%>