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
<title>查询单位填报情况</title>
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

     //已上报率
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
                     <td colspan=3><img src="../img/infomation.bmp">&nbsp;已上报单位数<%= maxRowCount + "/" + allUnitCount %>，上报率<%= Convertor.double2Percent(percent) %></td>
                    </tr>
                    <tr class="clsTrHeader"><td>单位代码</td><td>单位名称</td><td>是否填报</td></tr>
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
                            <td>是</td>
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
                          每页<%=rowsPerPage%>行&nbsp;
                          当前第<%=curPage%>页/
                          共<%=String.valueOf(maxPage)%>页
                          &nbsp;&nbsp;&nbsp;&nbsp;
                          <%if(curPage.equals("1")){
                          %>
                              <A disabled>首页</A>
                              <A disabled>上一页</A>
                          <%}else{%>
                              <A HREF="javascript:gotoFirstPage()">首页</A>
                              <A HREF="javascript:gotoPrePage()">上一页</A>
                          <%}%>
                          <%if(curPage.equals(String.valueOf(maxPage))){
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
