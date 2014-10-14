<%@ page contentType="text/html; charset=GBK" %>
<%
/**
 *  显示单个任务的任务时间。
 *  
 *  可以接受的参数:
 *  @param taskID   任务ID 。（优先考虑taskID的task信息，后考虑request.getAttribute( "task" ) ）；
 *  @parma timesPerLine  整数。表示一行显示多少个任务时间（默认为6个）
 *  @param checkedTimeID  多个整数。表示哪些taskTime被选中（默认没有任何被选中）
 *  @param taskTimeFieldID 选择的taskTimeID放在那个地方。
 *  			（默认值为window.opener.document.getElementById( "taskTimeIDs") 
 *				表示父窗口的taskTimeIDs）
 *			选择后taskTimeIDs的值为任务时间id值的连串，中间以逗号格开，最后还多余一个逗号
 * 
 *  @param closeAfterDone  boolean。点击选择完毕后，是否关闭弹出页面。（默认关闭弹出的页面）
 *  @param doAfterDone     字符串。一般表示父窗口的一个javascript。比如window.opener.afterSelectTimes()。
 *                          （默认为空）
 *  
 *  如果task可以通过request.getAttribute( "task" )得到，或者从传入的参数
 *  taskID得到。
 *  如果两者都没有，提示：程序错误：没有找到任何任务信息。
 *  然后关闭浏览器窗口。
 */
%>

<html>
<head>
<title>选择任务时间</title>
<style type="text/css">
<!--
body {
	background-color: #edebea;
}
-->
</style>
<link rel="stylesheet" type="text/css" href="/csslib/main.css"/>
<script type="text/javascript" src="/jslib/calendar/popcalendar.js"/>

<%
// 接受传入的参数
String taskID = request.getParameter( "taskID" );
String strTimesPerLine = request.getParameter( "timesPerLine" );
String[] strCheckedTaskTimes = request.getParameterValues( "checkedTimeID" );
String taskTimeFieldID = request.getParameter( "taskTimeFieldID" );
String strCloseAfterDone = request.getParameter( "closeAfterDone" );
String doAfterDone = request.getParameter( "doAfterDone" );


int timesPerLine = 6;
int[] checkedTaskTimeIDs = null;
boolean closeAfterDone = true;

// 解析传入的参数，如果缺省赋默认值
if( strTimesPerLine != null )
{
	try
	{
		timesPerLine = Integer.parseInt( strTimesPerLine );
	} 
	catch( Exception ex )
	{ // 解析失败，依然使用默认值
	}
}

if( strCheckedTaskTimes != null )
{
	try
	{
		checkedTaskTimeIDs = new int[ strCheckedTaskTimes.length ];
		
		for( int index=0; index<checkedTaskTimeIDs.length; index++ )
		{
			checkedTaskTimeIDs[index] = Integer.parseInt( strCheckedTaskTimes[index] );
		}
	}
	catch( Exception ex )
	{
	}
}

if( taskTimeFieldID == null )
	taskTimeFieldID = "window.opener.document.getElementById( \"taskTimeIDs\")";

if( strCloseAfterDone != null 
	&& strCloseAfterDone.equalsIgnoreCase( "false" ) )
	closeAfterDone = false;

cn.com.youtong.apollo.task.Task task = null;

// 查看是否是传入了taskID
if( taskID != null )
{
	cn.com.youtong.apollo.task.TaskManager mng = ( (cn.com.youtong.apollo.task.TaskManagerFactory) 
			cn.com.youtong.apollo.services.Factory.getInstance(
				cn.com.youtong.apollo.task.TaskManagerFactory.class.getName())).createTaskManager();
	        
	task = mng.getTaskByID( taskID );
} else {
	task = (cn.com.youtong.apollo.task.Task) request.getAttribute( "task" );
}

if( task == null )
{ %>
<script>
	alert( "程序错误：没有找到任何任务信息" );
	window.close();
</script>
<% 
return;
}
%>


<script>

<%
cn.com.youtong.apollo.task.TaskTime suitableTaskTime 
	= cn.com.youtong.apollo.javabean.TaskTimeBean.suitableTaskTime( task );

Integer suitableTaskTimeID = null;

if( suitableTaskTime == null )
{
	suitableTaskTimeID = new Integer( 0 ); // 赋个“几乎”没有意义的0值	
}
else
{
	suitableTaskTimeID = suitableTaskTime.getTaskTimeID();
}
%>
var suitableTimeID = <%=suitableTaskTimeID.intValue()%>;
var timeID = new Array(); // 任务时间id数组
var timeDesc = new Array(); // 任务时间描述数组

<%
java2javascript( out, task.getTaskTimes(), "timeID", "timeDesc" );
%>

</script>


<script>
function doneSelect()
{
	var oTimeBuff = "";
	var oSrcForm = window.document.getElementById( "subTaskTimeForm" );
		
	var oSingleTime = window.document.getElementById( "singleTime" );
	
	if( oSingleTime.disabled )
	{
		// 选择的是时间段
		var beginIndex = window.document.getElementById( "beginTime" ).selectedIndex;
		var endIndex = window.document.getElementById( "endTime" ).selectedIndex;
		
		var beginTime = window.document.getElementById( "beginTime" );
		var oTimes = beginTime.options;
		
		if( beginIndex <=endIndex )
		{
			for( var i=beginIndex; i<=endIndex; i++ )
			{
				oTimeBuff += oTimes(i).value;
				oTimeBuff += ",";
			}
		}
		else
		{
			for( var i=beginIndex; i>=endIndex; i-- )
			{
				oTimeBuff += oTimes(i).value;
				oTimeBuff += ",";
			}
		}
	}
	else
	{
		var index = window.document.getElementById( "singleTime" ).selectedIndex;
		
		oTimeBuff = window.document.getElementById( "singleTime" ).options(index).value + ",";
	}
	
	
	<%=taskTimeFieldID%>.value = oTimeBuff;
	
	<% if( doAfterDone != null ) { %>
		<%=doAfterDone%>;
	<% } %>
	<% if( closeAfterDone ) { %>
		window.close();
	<% } %>
}
</script>

<script language="javascript">
function disableAnother()
{
	if( window.event.srcElement.value=="single" )
	{
		subTaskTimeForm.singleTime.disabled = false;
		subTaskTimeForm.beginTime.disabled = true;
		subTaskTimeForm.endTime.disabled = true;
	}
	else
	{
		subTaskTimeForm.singleTime.disabled = true;
		subTaskTimeForm.beginTime.disabled = false;
		subTaskTimeForm.endTime.disabled = false;
	}
}
</script>

</head>
<body class="clsPopu">


<!-- 新的任务时间显示方式 -->
<form id="subTaskTimeForm">
<table align="center">

	<tr>
		<td colspan="4">&nbsp</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp</td>
	</tr>
	
	<tr>
		<td>
			<input type="radio" name="selectType" value="single" onclick="disableAnother()" checked/> 单时间选择
		</td>
		
		<td>
			<select name="singleTime" id="singleTime">
<% for(java.util.Iterator itr = task.getTaskTimes(); itr.hasNext();)
{
	cn.com.youtong.apollo.task.TaskTime taskTime = 
		(cn.com.youtong.apollo.task.TaskTime)itr.next();
		
%>
				<option value="<%=taskTime.getTaskTimeID()%>" 
				<%=taskTime.getTaskTimeID().equals( suitableTaskTimeID )? "selected": ""%>
				>
				<%= cn.com.youtong.apollo.common.Convertor.date2MonthlyString(taskTime.getFromTime()) %>
				</option>
<% } %>
			</select>
		</td>
	</tr>
	
	<tr>
		<td>
			<input type="radio" name="selectType" value="multi" onclick="disableAnother()"/> 时间段选择
		</td>
		
		<td>
			<select name="beginTime" id="beginTime" disabled>
<% for(java.util.Iterator itr = task.getTaskTimes(); itr.hasNext();)
{
	cn.com.youtong.apollo.task.TaskTime taskTime = 
		(cn.com.youtong.apollo.task.TaskTime)itr.next();
		
%>
				<option value="<%=taskTime.getTaskTimeID()%>" 
				<%=taskTime.getTaskTimeID().equals( suitableTaskTimeID )? "selected": ""%>
				>
				<%= cn.com.youtong.apollo.common.Convertor.date2MonthlyString(taskTime.getFromTime()) %>
				</option>
<% } %>
			</select>
		<td>
		
		<td>
			&nbsp;&nbsp;到&nbsp;&nbsp;
		</td>
		
		<td>
			<select name="endTime" id="endTime" disabled>
<% for(java.util.Iterator itr = task.getTaskTimes(); itr.hasNext();)
{
	cn.com.youtong.apollo.task.TaskTime taskTime = 
		(cn.com.youtong.apollo.task.TaskTime)itr.next();
		
%>
				<option value="<%=taskTime.getTaskTimeID()%>" 
				<%=taskTime.getTaskTimeID().equals( suitableTaskTimeID )? "selected": ""%>
				>
				<%= cn.com.youtong.apollo.common.Convertor.date2MonthlyString(taskTime.getFromTime()) %>
				</option>
<% } %>
			</select>
		</td>
		
	</tr>

	<tr>
		<td colspan="4">&nbsp</td>
	</tr>
	
	<tr>
		<td colspan="4">
		<input type="button" name="subButt" value="确 定" onclick="doneSelect()"/>
		&nbsp;
		<input type="reset" name="resetButt" value="取 消" onclick="javascript:window.close()"/>
		</td>
		
	</tr>
<table>

</form>

</body>
</html>
<%!
void java2javascript( javax.servlet.jsp.JspWriter  out,
		      java.util.Iterator iter,
		      String timeID,
		      String timeDesc )
	throws java.io.IOException
{
	int index = 0;
	
	while( iter.hasNext() ) {
		cn.com.youtong.apollo.task.TaskTime time = 
			(cn.com.youtong.apollo.task.TaskTime) iter.next();
		
		out.print( timeID );
		out.print( "[" );
		out.print( index );
		out.print( "]=\"" );
		out.print( time.getTaskTimeID().intValue() );
		out.println( "\";" );
		
		out.print( timeDesc );
		out.print( "[" );
		out.print( index );
		out.print( "]=\"" );
		out.print( cn.com.youtong.apollo.common.Convertor.date2MonthlyString( time.getFromTime() ) );
		out.println( "\";" );
		
		index++;
	}
}
%>