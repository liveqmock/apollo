<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.FillStateForm" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>��ѯ��λ����</title>
</head>
<body>


<%
    int curPg=1;int maxPage=1;
     Collection col = (Collection) request.getAttribute("fillstates");
     int allUnitCount = col.size();
     Iterator iter = col.iterator();
     while(iter.hasNext()){
        FillStateForm fillStateForm = (FillStateForm) iter.next();
        if(!fillStateForm.isFilled()){
          iter.remove();
        }
     }
     Object[] userObject = col.toArray();
     String curPage = (String)request.getAttribute("curPage");
     String taskTimeID = ((Integer)request.getAttribute("taskTimeID")).toString();
     int maxRowCount = col.size();

     //���ϱ���
     double percent = 0.0;
     if(allUnitCount > 0)
     {
         percent = (double)maxRowCount / (double)allUnitCount;
     }

     String rowsPerPage = (String)request.getAttribute("rowsPerPage");
    if (maxRowCount % Integer.parseInt(rowsPerPage)==0){
       maxPage = maxRowCount/Integer.parseInt(rowsPerPage);
      }else{
       maxPage = maxRowCount/Integer.parseInt(rowsPerPage) + 1;
        }
     curPg = Integer.parseInt(curPage);
%>
<jsp:include page= "/jsp/analyseManager/fillstateMenu.jsp" />
<div style="height:88%;OVERFLOW:auto">
  <table width=100% border=0>
    <tr>
      <td>

                    <table width=100%>
                    <tr align=center>
                     <td colspan=3><img src="../img/infomation.bmp">&nbsp;���ϱ���λ��<%= maxRowCount + "/" + allUnitCount %>���ϱ���<%= Convertor.double2Percent(percent) %></td>
                    </tr>
                    <tr class="clsTrHeader"><td>��λ����</td><td>��λ����</td><td>�Ƿ��</td></tr>
                    <%
                       int i=0;
                       int stopFlag = Integer.parseInt(rowsPerPage)*curPg;
                       if(curPg >= maxPage){
                          stopFlag = maxRowCount;
                       }
                       for(int j=Integer.parseInt(rowsPerPage)*(curPg-1);j<stopFlag;j++)
                       {
                        FillStateForm form = (FillStateForm)userObject[j];
                        if(form.isFilled()){
                    %>
                            <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                            <td><a href="javascript:showData('<%= form.getTask().id() %>', '<%= form.getTaskTime().getTaskTimeID() %>', '<%= form.getUnit().id() %>')"><%=form.getUnit().getUnitCode()%></a></td>
                            <td><a href="javascript:showData('<%= form.getTask().id() %>', '<%= form.getTaskTime().getTaskTimeID() %>', '<%= form.getUnit().id() %>')"><%=form.getUnit().getUnitName()%></a></td>
                            <td>��</td>
                            </tr>
                    <%
                        }i++;}
                    %>
                    <%
                        if(maxRowCount > 0)
                        {
                    %>
                   <tr class="clsContentListTable">
                       <td align="right" colspan=3>
                          ÿҳ<%=rowsPerPage%>��&nbsp;
                          ��ǰ��<%=curPage%>ҳ/
                          ��<%=String.valueOf(maxPage)%>ҳ
                          &nbsp;&nbsp;&nbsp;&nbsp;
                          <%if(curPage.equals("1")){
                          %>
                              <A disabled>��ҳ</A>
                              <A disabled>��һҳ</A>
                          <%}else{%>
                              <A HREF="javascript:gotoFirstPage()">��ҳ</A>
                              <A HREF="javascript:gotoPrePage()">��һҳ</A>
                          <%}%>
                          <%if(curPage.equals(String.valueOf(maxPage))){
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
                    <%
                        }
                    %>
            </table>

       </td>
     </tr>
</table>

</div>
<script language="javascript">
function gotoFirstPage(){
window.location="../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALREADY_FILLSTATE_RESULT%>"+"&taskTimeID="+"<%=taskTimeID%>";
}
function gotoPrePage(){
window.location="../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALREADY_FILLSTATE_RESULT%>"+"&curPage="+"<%=String.valueOf(curPg-1)%>"+"&taskTimeID="+"<%=taskTimeID%>";
}
function gotoNextPage(){
window.location="../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALREADY_FILLSTATE_RESULT%>"+"&curPage="+"<%=String.valueOf(curPg+1)%>"+"&taskTimeID="+"<%=taskTimeID%>";
}
function gotoLastPage(){
window.location="../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALREADY_FILLSTATE_RESULT%>"+"&curPage="+"<%=maxPage%>"+"&taskTimeID="+"<%=taskTimeID%>";
}
</script>


</body>
</html>
