<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
  Task task = (Task)request.getAttribute("task");
%>
<%
    if(task.getTaskType() == task.REPORT_OF_MONTH){
%>
        <select id="taskTimeIDSelect" name="taskTimeIDSelect" onchange="javascript:submitForm()">
    <%

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
<%
    }else if(task.getTaskType() == task.REPORT_OF_DAY){
%>
<!--        ÈÕ±¨         -->
 
 <%
    String selData=request.getParameter("timeOfTask");
    if(selData==null||selData.equals(""))
       selData=Convertor.date2ShortString(new Date());
  
 %>

 <input id="timeOfTask" name="timeOfTask"  type="text" value="<%=selData%>" size="12"/>
 <input type="button" onclick="popUpCalendar(this,document.getElementById('timeOfTask'),'yyyy-mm-dd');" value="¡ý"/>
<script language="javascript">
  var dateInfos = new Array();
     <%
      int i=0;
      for(Iterator itrTaskTime = task.getTaskTimes(); itrTaskTime.hasNext();)
      {
        TaskTime taskTime = (TaskTime)itrTaskTime.next();
      %>
        dateInfos[<%=i%>] = new Array;
        dateInfos[<%=i%>][0] = "<%=taskTime.getTaskTimeID()%>";
        dateInfos[<%=i%>][1] = "<%=Convertor.date2ShortString(taskTime.getFromTime())%>";
      <%i++;}%>
</script>

<%
    }else if(task.getTaskType() == task.REPORT_OF_QUARTER){
%>
		<select id="taskTimeIDSelect" name="taskTimeIDSelect" onchange="javascript:submitForm()">
    <%

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
          <option value="<%= taskTime.getTaskTimeID() %>" selected><%= Convertor.date2QuateString(taskTime.getFromTime()) %></option>
      <%}else{%>
          <option value="<%= taskTime.getTaskTimeID() %>"><%= Convertor.date2QuateString(taskTime.getFromTime()) %></option>
      <%}}%>
        </select>
<%
    }else if(task.getTaskType() == task.REPORT_OF_YEAR){
%>
<select id="taskTimeIDSelect" name="taskTimeIDSelect" onchange="javascript:submitForm()">
    <%

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
          <option value="<%= taskTime.getTaskTimeID() %>" selected><%= Convertor.date2YearString(taskTime.getFromTime()) %></option>
      <%}else{%>
          <option value="<%= taskTime.getTaskTimeID() %>"><%= Convertor.date2YearString(taskTime.getFromTime()) %></option>
      <%}}%>
        </select>
<%
    }else if(task.getTaskType() == task.REPORT_OF_XUN){
%>
Ñ®±¨
<%}%>


