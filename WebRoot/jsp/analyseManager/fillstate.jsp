<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<head>
<STYLE type=text/css>
.clsDataTable
{
    MARGIN-TOP: 0.1cm;
    WIDTH: 100%;
    HEIGHT: 63%;
    BACKGROUND-COLOR: whitesmoke
}
</STYLE>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>��ѯ��λ����</title>
</head>
<body>
<jsp:include page= "/jsp/analyseManager/fillstateMenu.jsp" />
<div style="height:88%;OVERFLOW:auto">
  <table width=100% border=0>
    <%
         Collection col = (Collection) request.getAttribute("fillstates");
    %>
    <tr>
      <td>
                    <table width=100%>
                    <tr class="clsTrHeader"><td>��λ����</td><td>��λ����</td><td>�Ƿ��</td></tr>

                     <%
                       int i=0;
                       for(Iterator itr = col.iterator(); itr.hasNext(); i++)
                       {
                           FillStateForm form = (FillStateForm)itr.next();
                           if(form.isFilled()){
                    %>
                            <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                            <td><a href="javascript:showData('<%= form.getTask().id() %>', '<%= form.getTaskTime().getTaskTimeID() %>', '<%= form.getUnit().id() %>')"><%=form.getUnit().getUnitCode()%></a></td>
                            <td><a href="javascript:showData('<%= form.getTask().id() %>', '<%= form.getTaskTime().getTaskTimeID() %>', '<%= form.getUnit().id() %>')"><%=form.getUnit().getUnitName()%></a></td>
                            <td><%= form.isFilled() ? "��" : "��" %></td>
                            </tr>
                    <%}else{%>
                            <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                            <td><%=form.getUnit().getUnitCode()%><%= form.isFilled() ? "��" : "" %></td>
                            <td><%=form.getUnit().getUnitName()%></td>
                            <td><%= form.isFilled() ? "��" : "��" %></td>
                            </tr>
                    <%}}%>
                    </tr>
<%
if(col.size() == 0)
{
%>
                    <tr align=center><td colspan=3>û�����������ĵ�λ������ָ���ĵ�λ�����ϱ�����</td></tr>
<%
}
%>
                    </table>
      </td>
    </tr>
</table>


</body>

</html>