<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.log.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>��־��ѯ</title>
</head>

<%
//condition
int [] types = new int [0];
Date startTime = null;
Date endTime = null;
String source = null;
String userName = null;
String memo = null;

EventQueryCondition condition = (EventQueryCondition)request.getAttribute("condition");

if(condition != null)
{
    types = condition.getTypes();
    startTime = condition.getStartTime();
    endTime = condition.getEndTime();
    source = condition.getSource();
    userName = condition.getUserName();
    memo = condition.getMemo();
}
%>

<script language="javascript">
function queryEvent()
{
    //���ʱ�䷶Χ
    var startTime = isDate(form1.start.value);
    var endTime = isDate(form1.end.value);

    if(startTime && endTime)
    {
        if(compareDate(startTime, endTime) > 0)
        {
            alert("��ʼʱ������ڽ���ʱ��֮ǰ");
            return false;
        }
    }

    //��ʱ�����ʱ����
    form1.startTime.value = form1.start.value;
    form1.endTime.value = form1.end.value;

    if(endTime)
    {
        form1.endTime.value = form1.endTime.value + " 23:59:59";
    }

    form1.submit();
}

</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
<form name="form1" method="post" action="../servlet/log?operation=<%= LogServlet.QUERY_EVENT %>">
<table>
<tr>
  <td>�¼����ͣ�</td>
  <td>
<input type="checkbox" name="types" value="<%= Event.INFO %>" <%= isMemberOf(Event.INFO, types) ? "checked" : "" %>/>
<%= getTypeName(Event.INFO) %>&nbsp;
<input type="checkbox" name="types" value="<%= Event.WARNING %>" <%= isMemberOf(Event.WARNING, types) ? "checked" : "" %>/>
<%= getTypeName(Event.WARNING) %>&nbsp;
<input type="checkbox" name="types" value="<%= Event.ERROR %>" <%= isMemberOf(Event.ERROR, types) ? "checked" : "" %>/>
<%= getTypeName(Event.ERROR) %>&nbsp;
<input type="checkbox" name="types" value="<%= Event.AUDIT_SUCCESS %>" <%= isMemberOf(Event.AUDIT_SUCCESS, types) ? "checked" : "" %>/>
<%= getTypeName(Event.AUDIT_SUCCESS) %>&nbsp;
<input type="checkbox" name="types" value="<%= Event.AUDIT_FAIL %>" <%= isMemberOf(Event.AUDIT_FAIL, types) ? "checked" : "" %>/>
<%= getTypeName(Event.AUDIT_FAIL) %>
  </td>
</tr>
<tr>
  <td>�ӣ�</td>
  <td>
    <input type="hidden" name="startTime"/>
    <input name="start" readonly value="<%= Convertor.getHTMLString(Convertor.date2ShortString(startTime)) %>"/>
    <input type="button" onClick="popUpCalendar(this, form1.start, 'yyyy-mm-dd');" value="..."/>
  </td>
  <td>����</td>
  <td>
    <input type="hidden" name="endTime"/>
    <input name="end" readonly value="<%= Convertor.getHTMLString(Convertor.date2ShortString(endTime)) %>"/>
    <input type="button" onClick="popUpCalendar(this, form1.end, 'yyyy-mm-dd');" value="..."/>
  </td>
  </tr>
<tr>
  <td>�û���</td><td><input name="userName" value="<%= Convertor.getHTMLString(userName) %>"/></td>
  <td>��Դ��</td><td><input name="source" value="<%= Convertor.getHTMLString(source) %>"/></td>
  <td>˵����</td><td><input name="memo" value="<%= Convertor.getHTMLString(memo) %>"/></td>
</tr>
<tr><td><button onclick="queryEvent()">��ѯ</button></td></tr>
</table>
</form>


  <table class="clsContentListTable">
    <tr class="clsTrHeader">
      <td nowrap>����</td>
      <td nowrap>ʱ��</td>
      <td nowrap>�û�</td>
      <td nowrap>��Դ</td>
      <td nowrap>˵��</td>
    </tr>
<%
                if(request.getAttribute("eventCol")!=null){
                       if(((Collection)request.getAttribute("eventCol")).size()!=0){
                       Collection eventCol=(Collection)request.getAttribute("eventCol");
                       Object[] userObject= eventCol.toArray();
                       String curPage = (String)request.getAttribute("curPage");
                       String maxRowCount = (String)request.getAttribute("maxRowCount");
                       String rowsPerPage = (String)request.getAttribute("rowsPerPage");
                       String maxPage = (String)request.getAttribute("maxPage");
                       int i=0;
                       int curPg = Integer.parseInt(curPage);
                       int maxPg = Integer.parseInt(maxPage);
                       int stopFlag = Integer.parseInt(rowsPerPage)*curPg;
                       if(curPg==maxPg){
                          stopFlag = Integer.parseInt(maxRowCount);
                       }
                       for(int j=Integer.parseInt(rowsPerPage)*(curPg-1);j<stopFlag;j++)
                       {
                         Event event = (Event)userObject[j];
%>
    <tr <%= (((i % 2) == 1) ? "class=\"TrLight\"" : "class=\"TrDark\"") %>>
      <td nowrap><%= Convertor.getHTMLString(getTypeName(event.getType()))%>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(Convertor.date2String(event.getTimeOccured())) %>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(event.getUserName()) %>&nbsp;</td>
      <td nowrap><%= Convertor.getHTMLString(event.getSource()) %>&nbsp;</td>
      <td ><%= Convertor.getHTMLString(event.getMemo()) %>&nbsp;</td>
    </tr>
<%
i++;}
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
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.QUERY_EVENT %>"+"&flag="+"true";
      }
      function gotoPrePage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.QUERY_EVENT %>"+"&curPage="+"<%=String.valueOf(curPg-1)%>"+"&flag="+"true";
      }
      function gotoNextPage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.QUERY_EVENT %>"+"&curPage="+"<%=String.valueOf(curPg+1)%>"+"&flag="+"true";
      }
      function gotoLastPage(){
      window.location="../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.QUERY_EVENT %>"+"&curPage="+"<%=maxPage%>"+"&flag="+"true";
      }
      </script>
<%}else{%>
    <tr>
      <td colspan="5" align="center"><img src="../img/infomation.bmp">&nbsp;  û�з��ϴ˲�ѯ����������</td>
    </tr>
<%}}%>
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

/**
 * �ж�number�Ƿ���numbers��
 * @param number number
 * @param numbers numbers
 * @return number��numbers�У�����true�����򷵻�false
 */
public boolean isMemberOf(int number, int [] numbers)
{
    for(int i = 0; i < numbers.length; i++)
    {
        if(numbers[i] == number)
        {
            return true;
        }
    }
    return false;
}
%>