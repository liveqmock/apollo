<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />

<style type="text/css">
A { 
	color: darkblue
} 

.dynamic-tab-pane-control .tab-page {
	height:		450px;
	overflow:	visible;
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
	height:		420px;
       overflow:	visible;
}


form {
	margin:		4;
	padding:	1;
}

/* over ride styles from webfxlayout */

body {
	margin:		0px;
	width:		auto;
	height:		auto;
}

.dynamic-tab-pane-control h2 {
	text-align:	center;
	width:		auto;
}

.dynamic-tab-pane-control h2 a {
	display:	inline;
	width:		auto;
}

.dynamic-tab-pane-control a:hover {
	background: transparent;
}
</style>

<title>ģ���ѯ</title>
</head>
<%
    Iterator templateItr = (Iterator)request.getAttribute("templateItr");
    Task task = (Task) request.getAttribute( "task" );
    
    TaskTime suitTaskTime 
    		= cn.com.youtong.apollo.javabean.TaskTimeBean.suitableTaskTime( task );
%>
<script language= "javascript">
var additionalPart = ""; // ������ʱ��ѡ��ҳ�棬������ѯ�ַ�
var unitIDselected = new Array();
var timeID = new Array(); // ����ʱ��id����
var timeDesc = new Array(); // ����ʱ����������
var oPopWin;

<%
java2javascript( out, task.getTaskTimes(), "timeID", "timeDesc" );
%>

</script>

<script>

function afterSelectUnitIDs()
{
	unitIDselected = new Array();
	
	var opopUnitIDs = oPopWin.unitIDselected;
	for( var i=0; i<opopUnitIDs.length; i++ )
	{
		unitIDselected[i] = opopUnitIDs[i];
	}
	
	var unitNames = new Array();
	var opopUnitNames = oPopWin.unitNameselected;
	for( var i=0; i<opopUnitNames.length; i++ )
	{
		unitNames[i] = opopUnitNames[i];
	}
	
	fillUnitSelect( unitNames );
}

function fillUnitSelect( unitNames ) 
{
	// ���ԭ����¼��������ʾselect����
	var unitSelect = document.getElementById( "unitSelect" );
	clearSelect( unitSelect ); 
	unitSelect.style.display = "inline";
	
	var size = 0;
	for( var i=0; i<unitNames.length; i++ )
	{
		var s = unitNames[i];
		
		if( s != null
			&& s.length > 0 )
		{
			var oOption = document.createElement( "option" );
			oOption.innerText = " " + s + " ";
			unitSelect.appendChild( oOption );
			
			size++;
		}
	}
	
	if( size == 1 )
	{
		unitSelect.size = size;
		unitSelect.selectedIndex = 0;
	}
	else if( size < 10 )
	{
		unitSelect.size = size;
	}
	else
	{
		unitSelect.size = 10;
	}
}

function selectTaskTimes()
{
	additionalPart = "&doAfterDone=window.opener.afterSelectTaskTimes()";
	
	var url = "../jsp/taskManager/showTaskTimes.jsp?taskID=<%=task.id()%>" + additionalPart;
	window.open( url, null, "location=no,menubar=no,toolbar=no,height=300,width=500" );
	
	additionalPart = ""; // ֵ��λ
} 

function afterSelectTaskTimes()
{
	fillTimeSelect();
}

function fillTimeSelect()
{
	// ���ԭ����¼��������ʾselect����
	var timeSelect = document.getElementById( "timeSelect" );
	clearSelect( timeSelect ); 
	
	var oTimes = execTplForm.taskTimeIDs.value;
	
	var beginIndex = 0;
	var endIndex= oTimes.indexOf( ",", beginIndex );
	
	var size = 0;
	while( endIndex > 0 )
	{
		var s = oTimes.substring( beginIndex, endIndex );
		var oOption = document.createElement( "option" );
		oOption.innerText = " " + getTimeDesc( s ) + " ";
		timeSelect.appendChild( oOption );
		beginIndex = endIndex+1;
		
		endIndex = oTimes.indexOf( ",", beginIndex );
		
		size++;
	}
	
	if( size == 1 )
	{
		timeSelect.size = size;
		timeSelect.selectedIndex  = 0;
	}
	else if( size < 5 )
	{
		timeSelect.size = size;
	}
	else
	{
		timeSelect.size = 5;
	}
}


function getTimeDesc( tid )
{
	for( var i=0; i<timeID.length; i++ )
	{
		if( timeID[i] == tid ) 
		{
			return timeDesc[i];
		}
	}
}


function selectAllTpl()
{
	var oInputs = execTplForm.getElementsByTagName( "input" );
	for( var i=0; i<oInputs.length; i++ )
	{
		if( oInputs[i].name.indexOf( "tpl_" ) >= 0 )
			oInputs[i].checked = true;
	}
}

function deSelectTpl()
{
	var oInputs= execTplForm.getElementsByTagName( "input" );
	for( var i=0; i<oInputs.length; i++ )
	{	
		if( oInputs[i].name.indexOf( "tpl_" ) == 0 )
			oInputs[i].checked = ! (oInputs[i].checked);
	}
}

function execTpl()
{
	var tplIDs = ""; // ѡ���ģ��
	
	var oInputs = execTplForm.getElementsByTagName( "input" );
	for( var i=0; i<oInputs.length; i++ )
	{	
		if( oInputs[i].name.indexOf( "tpl_" ) == 0 )
		{
			if( oInputs[i].checked )
			{
				tplIDs += oInputs[i].name.substring( 4 );
				tplIDs += ",";
			}
		}
	}
	
	execTplForm.tplIDs.value = tplIDs;
	
	var oTaskTimeIDs = document.getElementById( "taskTimeIDs" );
	
	if( tplIDs.length == 0 )
	{
		alert( "��ѡ����Ҫִ�е�ģ��" );	
		return false;
	}
	
	if( oTaskTimeIDs.value.length == 0 )
	{
		alert( "��ѡ������ʱ��" );
		return false;
	}
	
	removeHiddenField( execTplForm, "unitIDs" );
	
	for( var i=0; i<unitIDselected.length; i++ )
	{
		if( unitIDselected[i] != null 
			&& unitIDselected[i].length > 0 )
		{
			var oInput =document.createElement("input");
			oInput.type = "hidden";
			oInput.name = "unitIDs";
			oInput.value = unitIDselected[i];
			execTplForm.appendChild(oInput);
		}
	}
}

function showUnitTree()
{
	oPopWin = window.open( "utilServlet?operation=<%=UtilServlet.SELECT_UNIT%>&doAfterDone=window.opener.afterSelectUnitIDs()", 
					null, 
					"location=no,menubar=no,toolbar=no,height=500,width=800,resizable=yes" );
}

function enableDefaultUnits()
{
	unitIDselected = new Array();
		
	var unitSelect = document.getElementById( "unitSelect" );
	unitSelect.style.display = "none";
	
	var unitNames = new Array();
	unitNames[0] = "<<���е�λ>>";
	fillUnitSelect( unitNames );
}

function enableCustomeUnits()
{
	//unitIDselected = new Array();
	showUnitTree();
}

function enableSuitTime()
{
	
<% if( suitTaskTime == null ) { %>
	execTplForm.taskTimeIDs.value="";
<% } else { %>
	execTplForm.taskTimeIDs.value="<%=suitTaskTime.getTaskTimeID()%>" + ",";
<% } %>
	
	fillTimeSelect();
}

function enableCustomTime()
{	
	selectTaskTimes();
}

function up( index )
{
	var diff = 3;
	var oTable = document.getElementById( "tplTable" );
	
	var oUpwardRow = oTable.rows( index+diff );
	var oDownwardRow = oTable.rows( index+diff-1 );
	
	changeRow( oUpwardRow, oDownwardRow );
}

function down( index )
{
	var diff = 3;
	var oTable = document.getElementById( "tplTable" );
	
	var oUpwardRow = oTable.rows( index+diff+1 );
	var oDownwardRow = oTable.rows( index+diff );
	
	changeRow( oUpwardRow, oDownwardRow );
}

function changeRow( oRow1, oRow2 )
{
	// ֻ�ı��һ��checkbox�͵ڶ������ֱ�ǩ
	var oTemp = oRow1.cells(0).children(0).name;
	oRow1.cells(0).children(0).name = oRow2.cells(0).children(0).name;
	oRow2.cells(0).children(0).name = oTemp;
	
	oTemp = oRow1.cells(0).children(0).checked;
	oRow1.cells(0).children(0).checked = oRow2.cells(0).children(0).checked;
	oRow2.cells(0).children(0).checked = oTemp;
	
	oTemp = oRow1.cells(1).innerText;
	oRow1.cells(1).innerText = oRow2.cells(1).innerText;
	oRow2.cells(1).innerText = oTemp;
}

function afterLoad()
{
	enableSuitTime();
	enableDefaultUnits();
}

</script>
<body onload="afterLoad()">

<jsp:include page= "/jsp/logo.jsp" />
<jsp:include page= "/jsp/navigation.jsp" />


<form id="execTplForm" action="analyse"
		method="post" onsubmit="return execTpl()" target="tpl_result">
	
	<input type="hidden" name="operation" value="<%=AnalyseServlet.EXECUTE_SCALAR_QUERY_TEMPLATE%>"/>
	<input type="hidden" name="tplIDs" />
	<input type="hidden" name="taskTimeIDs" id="taskTimeIDs" 
	<% if( suitTaskTime != null ) {
		out.print( "value=\"" + suitTaskTime.getTaskTimeID() + ",\"" ); 
	}%> />
	
<table>
<tr>
<td align="left" valign="top">		
	
	<%
	if( templateItr.hasNext() )
	{%>
	<table id="tplTable">
	<tr>
		<td colspan="3"><input type="button" value="ȫѡ" onclick="selectAllTpl()"/>&nbsp;
			<input type="button" value="��ѡ" onclick="deSelectTpl()"/>&nbsp;
			<input type="submit" value="ִ�в�ѯ"/>
		</td>
	</tr>
	
	<tr>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<tr height="20" class="clsTrContext">
		<td align=center colspan="3">ģ���б�</td>
	</tr>
	
	<tr class="clsTrHeader" height="20">
		<td>��ѡ��</td>
		<td>����</td>
		<td>���˳��</td>		
	</tr>
	<%	
		int index = 1;
		String styleClassName = "TrDark";
		while( templateItr.hasNext() ) 
		{
			
			ScalarQueryTemplate tmpl = (ScalarQueryTemplate) templateItr.next();
		%>
			<tr class="<%=styleClassName%>">
				<td><input type="checkbox" name="tpl_<%=tmpl.getTemplateID().intValue()%>"/></td>
				<td><%=tmpl.getName()%></td>
				<td>
					<% if( index != 1 ) { %>
					<a href="javascript:up(<%=index%>)" style="border:none">
						<!--img src="../img/up.gif" alt="����" style="border:none"/-->
						��
					</a>
					<% } else { %>
					&nbsp;&nbsp;
					<% } %>
					&nbsp;
					<% if( templateItr.hasNext() ) { %>
					<a href="javascript:down(<%=index%>)" style="border:none">
						<!--img src="../img/down.gif" alt="����" style="border:none"/-->
						��
					</a>
					<% } else { %>
					&nbsp;&nbsp;
					<% } %>
				</td>
			</tr>
		<%
			index++;
			if( index % 2 == 0 )
			{
				styleClassName = "TrLight"; 
			}
			else 
			{
				styleClassName = "TrDark";
			}
		} %>
	</table>
	<%
	}
	else
	{	
	%>
	û��ģ��ɹ���ѯ
	<%
	}
	%>
</td>
	
<td width="10%">&nbsp;</td>

<td align="left" valign="top">	
<p>&nbsp;
	<table width="520" valign="top">
		<tr>
			<td colspan="4">
				<a id="ttime-switch" href="javascript:toggle('ttime', 'ʱ��ѡ��', 'ʱ��ѡ��')">ʱ��ѡ��</a>
			</td>
		</tr>
	
		<tr>
			<td colspan="4">
				<hr size="1" color="green"/>
			</td>
		</tr>
	
		<tr id="ttime">
			<td width="20">&nbsp;</td>
			<td valign="top" width="100">
				<a href="javascript:enableSuitTime()"><%=Convertor.date2MonthlyString( suitTaskTime.getFromTime() )%></a>
			</td>
			<td valign="top" width="100">
				<a href="javascript:enableCustomTime()">�Զ���ʱ��</a>
			</td>
			
			<td valign="top" width="300">					
				<select id="timeSelect" size="5">
				</select>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<a id="uunit-switch" href="javascript:toggle('uunit', '��λѡ��', '��λѡ��')">��λѡ��</a>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<hr size="1" color="green"/>
			</td>
		</tr>
		
		<tr id="uunit">
			<td width="20">&nbsp;</td>
			<td valign="top" width="100">
				<a href="javascript:enableDefaultUnits()">���е�λ</a>
			</td>
			<td valign="top" width="100">
				<a href="javascript:enableCustomeUnits()">�Զ��嵥λ</a>
			</td>

		 	<td valign="top" width="300">
				<select id="unitSelect" size="10" style="display: none">
				</select>
			</td>
		</tr>
	
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<a id="print-switch" href="javascript:toggle('print', '���ѡ��', '���ѡ��')">���ѡ��</a>
			</td>
		</tr>
		
		<tr>
			<td colspan="4">
				<hr size="1" color="green"/>
			</td>
		</tr>
		
		<tr id="print">
			<td width="20">&nbsp;</td>
			<td width="100">
				<input type="checkbox" name="showIndex" checked />
				��ʾ���
			</td>
			<td width="100">
				<input type="checkbox" name="showUnitName" checked />
				��ʾ��λ
			</td>
			<td width="300">
				<input type="checkbox" name="sameWorkbook" checked />
				�����һ���ļ�
			</td>
		</tr>
	</table>
</td>
</tr>
</table>
	
</form>

<jsp:include page= "/jsp/footer.jsp" />
	
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