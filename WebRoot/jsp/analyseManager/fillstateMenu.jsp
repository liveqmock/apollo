<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<script language="javascript">
function queryAllFillState(){
       form1.action="<%=request.getContextPath()%>/servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALL_FILLSTATE_RESULT%>";
		form1.submit();
}
function queryNotFillState()
{
 	form1.action="<%=request.getContextPath()%>/servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_NOT_FILLSTATE_RESULT%>";
		form1.submit();

}
function queryAlreadyFillState(){
 	form1.action="<%=request.getContextPath()%>/servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_ALREADY_FILLSTATE_RESULT%>";
		form1.submit();
}

function queryFillState()
{
         
         form1.action="<%=request.getContextPath()%>/servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.QUERY_FILL_STATE%>";

         form1.submit();
      
}

function showData(taskID, taskTimeID, unitID)
{
    var url = "<%=request.getContextPath()%>/servlet/model?operation=" + "<%= ModelServlet.SHOW_DATA %>" + "&taskID=" + taskID + "&taskTimeID=" + taskTimeID + "&unitID=" + unitID;
    window.open(url, "showData");
}
function linkToQuery(obj){
  if(event.keyCode=="13"){
        url="<%=request.getContextPath()%>/servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.QUERY_FILL_STATE%>";
        url+="&taskTimeID="+form1.taskTimeID.value;
	//判断是否输入了条件
        form1.codeOrName.value = trim(obj.value);
        if(obj.value == "")
        {
            alert("请输入查询条件");
            form1.codeOrName.focus();
            return false;
        }
        url += "&codeOrName=" + encodeURIComponent(obj.value);
    	window.location = encodeURI(url);
  }
}
</script>
<body scroll="no">

  <table width=100% border=0 height=5%>
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            数据传送-><font class="clsLightColorOfTask">上报情况</font>
         </td>
       </tr>
<form name="form1" method="post" style="margin:0">
	<tr>
           <td colspan=3>

                  任务时间：
                    <select name="taskTimeID">
                <%
         String codeOrName =request.getParameter("codeOrName");
		 if(codeOrName==null)
		    codeOrName="";

       
//                  Task task = (Task) request.getAttribute("task");
                  Task task = (Task)session.getAttribute("task");
                  for(Iterator itrTaskTime = task.getTaskTimes(); itrTaskTime.hasNext();)
                  {
                    TaskTime taskTime = (TaskTime)itrTaskTime.next();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat monthly = new SimpleDateFormat("MM");
                        int nowTime = Integer.parseInt(monthly.format(calendar.getTime()));
                        if(nowTime==1){nowTime=13;}
                        int selectTime = Integer.parseInt(monthly.format(taskTime.getFromTime()));
                        if((nowTime-1==selectTime) && (calendar.getTime().getTime()>taskTime.getFromTime().getTime())){
                  %>
                      <option value="<%= taskTime.getTaskTimeID() %>" selected><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></option>
                  <%}else{%>
                      <option value="<%= taskTime.getTaskTimeID() %>"><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></option>
                  <%}}%>
                    </select>

`           <input type="button" value="上报情况" onclick="queryAllFillState()"/>
            <input type="button" value="未上报" onclick="queryNotFillState()"/>
            <input type="button" value="已上报" onclick="queryAlreadyFillState()"/>
            &nbsp;&nbsp;单位代码或名称：<input type="text" name="codeOrName" value="<%= codeOrName %>"/>
            <input type="button" value="查询" onclick="queryFillState()"/>
           </td>
      </tr>
</form>
</table>

<script language="javascript">
//设置默认taskTime
setDefaultTaskTime();

function setDefaultTaskTime()
{
<%
    Integer taskTimeID = (Integer)session.getAttribute("taskTimeID");
    if(taskTimeID != null)
    {
%>
form1.taskTimeID.value = "<%= taskTimeID %>";
<%
    }
%>
}
form1.codeOrName.focus();

</script>

