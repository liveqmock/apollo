<%@ page contentType="text/html; charset=GBK" %>
<%
/**
 *  ��ʾ�������������ʱ�䡣
 *  
 *  ���Խ��ܵĲ���:
 *  @param taskID   ����ID �������ȿ���taskID��task��Ϣ������request.getAttribute( "task" ) ����
 *  @parma timesPerLine  ��������ʾһ����ʾ���ٸ�����ʱ�䣨Ĭ��Ϊ6����
 *  @param checkedTimeID  �����������ʾ��ЩtaskTime��ѡ�У�Ĭ��û���κα�ѡ�У�
 *  @param taskTimeFieldID ѡ���taskTimeID�����Ǹ��ط���
 *  			��Ĭ��ֵΪwindow.opener.document.getElementById( "taskTimeIDs") 
 *				��ʾ�����ڵ�taskTimeIDs��
 *			ѡ���taskTimeIDs��ֵΪ����ʱ��idֵ���������м��Զ��Ÿ񿪣���󻹶���һ������
 * 
 *  @param closeAfterDone  boolean�����ѡ����Ϻ��Ƿ�رյ���ҳ�档��Ĭ�Ϲرյ�����ҳ�棩
 *  @param doAfterDone     �ַ�����һ���ʾ�����ڵ�һ��javascript������window.opener.afterSelectTimes()��
 *                          ��Ĭ��Ϊ�գ�
 *  
 *  ���task����ͨ��request.getAttribute( "task" )�õ������ߴӴ���Ĳ���
 *  taskID�õ���
 *  ������߶�û�У���ʾ���������û���ҵ��κ�������Ϣ��
 *  Ȼ��ر���������ڡ�
 */
%>

<html>
<head>
<title>ѡ������ʱ��</title>
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
// ���ܴ���Ĳ���
String taskID = request.getParameter( "taskID" );
String strTimesPerLine = request.getParameter( "timesPerLine" );
String[] strCheckedTaskTimes = request.getParameterValues( "checkedTimeID" );
String taskTimeFieldID = request.getParameter( "taskTimeFieldID" );
String strCloseAfterDone = request.getParameter( "closeAfterDone" );
String doAfterDone = request.getParameter( "doAfterDone" );


int timesPerLine = 6;
int[] checkedTaskTimeIDs = null;
boolean closeAfterDone = true;

// ��������Ĳ��������ȱʡ��Ĭ��ֵ
if( strTimesPerLine != null )
{
	try
	{
		timesPerLine = Integer.parseInt( strTimesPerLine );
	} 
	catch( Exception ex )
	{ // ����ʧ�ܣ���Ȼʹ��Ĭ��ֵ
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

// �鿴�Ƿ��Ǵ�����taskID
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
	alert( "�������û���ҵ��κ�������Ϣ" );
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
	suitableTaskTimeID = new Integer( 0 ); // ������������û�������0ֵ	
}
else
{
	suitableTaskTimeID = suitableTaskTime.getTaskTimeID();
}
%>
var suitableTimeID = <%=suitableTaskTimeID.intValue()%>;
var timeID = new Array(); // ����ʱ��id����
var timeDesc = new Array(); // ����ʱ����������

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
		// ѡ�����ʱ���
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


<!-- �µ�����ʱ����ʾ��ʽ -->
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
			<input type="radio" name="selectType" value="single" onclick="disableAnother()" checked/> ��ʱ��ѡ��
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
			<input type="radio" name="selectType" value="multi" onclick="disableAnother()"/> ʱ���ѡ��
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
			&nbsp;&nbsp;��&nbsp;&nbsp;
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
		<input type="button" name="subButt" value="ȷ ��" onclick="doneSelect()"/>
		&nbsp;
		<input type="reset" name="resetButt" value="ȡ ��" onclick="javascript:window.close()"/>
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