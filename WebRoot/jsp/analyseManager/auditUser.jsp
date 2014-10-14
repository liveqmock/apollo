<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*,
				cn.com.youtong.apollo.task.Task,
				cn.com.youtong.apollo.task.TaskTime,
				java.text.SimpleDateFormat,
				cn.com.youtong.apollo.common.Convertor
				" %>

<%
ArrayList userIDs= (ArrayList)request.getAttribute("userIDs");
ArrayList userNames= (ArrayList)request.getAttribute("userNames");
ArrayList showNames= (ArrayList)request.getAttribute("showNames");
Map unpassedUsers= (Map)request.getAttribute("unpassedUsers");
Task task = (Task)request.getAttribute("task");
Integer taskTimeID= (Integer)request.getAttribute("taskTimeID"); 
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>审核部局领导</title>
</head>

<script language="javascript">
function auditPass(passed)
{
	var hasCheckedUser=false;
	var users= document.all("formAudit");

  for (i=0;i<document.formAudit.elements.length;i++){
         var ele=document.formAudit.elements[i];
         if (ele.type=="checkbox"&&ele.name=='chkUserID'&&ele.checked)
	    {   

            hasCheckedUser= true;

			//用户 hidden input
			var oInput =document.createElement("input");
			oInput.type = "hidden";
			oInput.name = "userID";
			oInput.value = ele.value;
			formAudit.appendChild(oInput);
        }
  }


	if(!hasCheckedUser)
	{
		alert("请选择要审核的用户。");
		return;
	}
	if(passed)
		formAudit.passState.value= "1";
	else
		formAudit.passState.value= "0";

	formAudit.submit();
}

function changeTime()
{
	formAudit.action= "analyse?operation=auditUser";
	formAudit.submit();
}
</script>

<body>
<jsp:include page= "/jsp/logo.jsp" />
<jsp:include page= "/jsp/navigation.jsp" />
<br>

<form name="formAudit" action="analyse?operation=doAuditUser" method=post>
<input type=hidden name="passState">

任务时间：
<select name="taskTimeID" onchange="changeTime()">
	<%	
	for(Iterator itrTaskTime = task.getTaskTimes(); itrTaskTime.hasNext();)
	{
		TaskTime taskTime = (TaskTime)itrTaskTime.next();
		String selected="";
		if(taskTimeID.equals(taskTime.getTaskTimeID()))
		{
			selected= "selected";
		}
	%>
		<option value="<%=taskTime.getTaskTimeID() %>" <%=selected%>>
			<%= Convertor.date2MonthlyString(taskTime.getFromTime()) %>
		</option>
	<%
	}
	%>
</select>

<input type=button value= "审核不通过" onclick="auditPass(false)">
<input type=button value= "审核通过" onclick="auditPass(true)">

<table class="clsContentListTable" cellPadding=0 cellspacing=0 border=0>
	<tr class="clsTrHeader">
		<td width=100>编号</td>
		<td width=150>标识</td>
		<td width=250>名称</td>
		<td width=100>审核标志</td>
		<td align="left">审核日期</td>
	</tr>
	<%
	for(int i=0; i<userIDs.size(); i++)
	{
		Integer userID= (Integer)userIDs.get(i);
		boolean unpassed= unpassedUsers.containsKey(userID);
		String img="";
		String dateHtml="";
		if(unpassed)
		{
			img="<img src='../img/audit_error.gif'/>";
			Date dateAudit= (Date)unpassedUsers.get(userID);
			dateHtml= Convertor.date2String(dateAudit);
		}
	%>
		<tr class="<%=(i%2==0)? "TrDark" : "TrLight"%>">
			<td><input type="checkbox" name="chkUserID" value="<%= userID%>"><%= userID%></td>
			<td><font color="blue"><%= userNames.get(i)%></font></td>
			<td><font color="blue"><%= showNames.get(i)%></font></td>
			<td align="left"><%= img%></td>
			<td align="left"><font color="blue"><%= dateHtml%></font></td>
		</tr>
	<%
	}
	%>
</table>

</form>

<jsp:include page= "/jsp/footer.jsp" />
</body>
</html>