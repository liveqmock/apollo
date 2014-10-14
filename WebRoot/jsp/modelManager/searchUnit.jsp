<%@ page contentType="text/html; charset=GBK" %>


<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />

<style type="text/css">

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
	margin-right: 	0px;
	margin-left:	0px; 
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

span.numspan {
	color: red
}

</style>

<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>

<script>
var unitIDs = new Array();
var unitNames = new Array();

<%
java.util.Collection nodesCol = (java.util.Collection) request.getAttribute( "nodes" );
java2javascript( out, nodesCol, "unitIDs", "unitNames" );
%>

var searchResultUnitIDs = new Array(); // 搜索结果数组
for( var i=0; i<unitIDs.length; i++ )
{
	searchResultUnitIDs[i] = i; // 表示查找到了unitIDs第i个值，被查找到了
}

var selectedResultUnitIDs = new Array(); // 选中结果数组
for( var i=0; i<unitIDs.length; i++ )
{
	selectedResultUnitIDs[i] = false; // 表示数组unitIDs第i个值，没有被选择
}
</script>

<script>
function doneSelect()
{
	<%
	String strCloseAfterDone = request.getParameter( "closeAfterDone" );
	String doAfterDone = request.getParameter( "doAfterDone" );
	
	boolean closeAfterDone = true;
	if( strCloseAfterDone != null 
			&& strCloseAfterDone.equalsIgnoreCase( "false" ) )
		closeAfterDone = false;
	
	
	%>
	
	<%
	if( doAfterDone != null ) {
	%>
	<%=doAfterDone%>;
	<% } %>
	
	<%
	if( closeAfterDone ) {
	%>
	window.close();
	<% } %>
}

function fillUnitSelect( resultUnitIDs )
{
	var oTable = document.getElementById( "unitTable" );
	var oSearchNum = document.getElementById( "searchNum" );
	
	clearTable( oTable );
	
	if( resultUnitIDs.length == 0 )
	{
		var oRow = oTable.insertRow();
		
		var oCell = oRow.insertCell();
		oCell.innerText = "没有找到相应值";
		oCell.style.color = "red";
		oCell.align = "center";
		
		oSearchNum.innerText = 0;
	}
	else
	{
		oSearchNum.innerText = resultUnitIDs.length;
		fillTable( resultUnitIDs, oTable );
	}
}

function insertIntoResult()
{
	var oSelected = new Array();
	
	var oTable = document.getElementById( "unitTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; i++ )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		if( oInput.checked )
		{
			var index = oInput.value;
			
			// 过滤一下，看是否被选中过
			if( selectedResultUnitIDs[index] == false )
			{
				oSelected[oSelected.length] = oInput.value;
				selectedResultUnitIDs[index] = true;
			}
		}
	}
	
	var oResultTable = document.getElementById( "selectedTable" );
	
	if( oSelected.length == 0 )
	{	
		displayNoUnitSelected();
	}
	else
	{
		var oRows = oResultTable.rows;
		var oCells = oRows(0).cells;
		
		if( oCells.length == 1 )
		{
			oResultTable.deleteRow( 0 );
		}
		
		fillTable( oSelected, oResultTable );
	}
	
	displaySelectNum();
}

function fillTable( resultUnitIDs, oTable )
{
	for( var i=0; i<resultUnitIDs.length; i++ )
	{
		var index = resultUnitIDs[i];
		var unitID = unitIDs[index];
		var unitName = unitNames[index];
		var iconName = "../img/icon_" 
				+ unitID.charAt( unitID.length - 1 ) 
				+".gif"
				
		var oRow = oTable.insertRow();
		
		var oCell = oRow.insertCell();
		var oInput = document.createElement( "input" );
		oInput.type = "checkbox";
		oInput.value = index;
		oInput.checked = true;
		oCell.className = "tdcssone";
		oCell.appendChild( oInput );
		
		
		oCell = oRow.insertCell();
		var oImg = document.createElement( "img" );
		oImg.src = iconName;
		oCell.className = "tdcssone";
		oCell.appendChild( oImg );
		
		oCell = oRow.insertCell();
		oCell.className = "tdcssone";
		oCell.innerText = unitID;
		
		oCell = oRow.insertCell();
		oCell.className = "tdcssone";
		oCell.innerText = unitName;
	}
}

function clearTable( oTable )
{
	var oRows = oTable.rows;
	for( var i=oRows.length-1; i>=0; i-- )
	{
		oTable.deleteRow( i );
	}
}

function searchUnit()
{
	var oForm = document.getElementById( "searchForm" );

	var suType = oForm.su_Type.value;
	var suCode = oForm.su_Code.value;
	
	searchResultUnitIDs = new Array();
	
	if( suType == "name" )
	{
		for( var i=0; i<unitNames.length; i++ )
		{
			if( unitNames[i].indexOf( suCode ) >= 0 )
			{
				searchResultUnitIDs[ searchResultUnitIDs.length ] = i;
			}
		}
	}
	else if( suType == "id" )
	{
		for( var i=0; i<unitIDs.length; i++ )
		{
			if( unitIDs[i].indexOf( suCode ) >= 0 )
			{
				searchResultUnitIDs[ searchResultUnitIDs.length ] = i;
			}
		}
	}
	
	fillUnitSelect( searchResultUnitIDs );
}

function removeFromResult()
{
	var oSelected = new Array();
	
	var oTable = document.getElementById( "selectedTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		if( oInput.checked )
		{
			var index = oInput.value;
			selectedResultUnitIDs[index] = false; // 设置成没有被选择
			
			oTable.deleteRow( i );
		}
		else
		{
			i++;
		}
	}
	
	oRows = oTable.rows;
	if( oRows.length == 0 )
	{
		displayNoUnitSelected();
	}
	
	displaySelectNum();
}

function displaySelectNum()
{
	var oSelectNum = document.getElementById( "selectNum" );
	
	var oTable = document.getElementById( "selectedTable" );
	var oRows = oTable.rows;
	if( oRows != null
		&& oRows.length > 0 )
	{
		var oCells = oRows(0).cells;
		if( oCells.length == 1 )
		{
			oSelectNum.innerText = 0;
		}
		else
		{
			oSelectNum.innerText = oRows.length;
		}
	}
	else
	{
		oSelectNum.innerText = oRows.length;
	}
}

function selectAllUnitTable()
{
	var oTable = document.getElementById( "unitTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; i++ )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		oInput.checked = true;
	}
}

function deselectUnitTable()
{
	var oTable = document.getElementById( "unitTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; i++ )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		oInput.checked = !oInput.checked;
	}
}


function selectAllSelectedTable()
{
	var oTable = document.getElementById( "selectedTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; i++ )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		oInput.checked = true;
	}
}

function deselectSelectedTable()
{
	var oTable = document.getElementById( "selectedTable" );
	var oRows = oTable.rows;
	
	for( var i=0; i<oRows.length; i++ )
	{
		var oRow = oRows(i);
		var oCell = oRow.cells(0);
		var oInput = oCell.children(0);
		
		oInput.checked = !oInput.checked;
	}
}

function getSelectedUnitID()
{
	var selectedUnitIDs = new Array();
	for( var i=0; i<selectedResultUnitIDs.length; i++ )
	{
		if( selectedResultUnitIDs[i] )
		{
			selectedUnitIDs[selectedUnitIDs.length] = unitIDs[i];
		}
	}
	
	return selectedUnitIDs;
}

function getSelectedUnitName()
{
	var selectedUnitNames = new Array();
	for( var i=0; i<selectedResultUnitIDs.length; i++ )
	{
		if( selectedResultUnitIDs[i] )
		{
			selectedUnitNames[selectedUnitNames.length] = unitNames[i];
		}
	}
	
	return selectedUnitNames;
}

function displayNoUnitSelected()
{
	var oTable = document.getElementById( "selectedTable" );
	
	var oRows = oTable.rows;
	if( oRows.length > 0 )
	{
		return;
	}
	
	var oRow = oTable.insertRow();
		
	var oCell = oRow.insertCell();
	oCell.innerText = "没有选中任何单位";
	oCell.style.color = "red";
	oCell.align = "center";	
}
	
function doOnload()
{
	fillUnitSelect(searchResultUnitIDs);
	
	preSelectUnitIDs();
	
	displayNoUnitSelected();
	displaySelectNum();
}

function preSelectUnitIDs()
{
	var oTable = document.getElementById( "unitTable" );
	var oRows = oTable.rows;
	
	var preSelectUnitIDs = new Array();
	<%java2javascript( out, 
			   request.getParameterValues( "unitID" ),
			   "preSelectUnitIDs" );%>
	
	var oSelected = new Array();
	for( var i=0; i<preSelectUnitIDs.length; i++ )
	{
		for( var j=0; j<unitIDs.length; j++ )
		{
			if( preSelectUnitIDs[i] == unitIDs[j] )
			{
				oSelected[oSelected.length] = j;
				selectedResultUnitIDs[j] = true;
				break;
			}
		}
	}
	
	var oResultTable = document.getElementById( "selectedTable" );
	
	fillTable( oSelected, oResultTable );
}

</script>

<title>查询单位</title>
</head>
<body onload="doOnload()">


<table style="height: 85%; width: 100%">
	<tr height="10%">
		<td colspan="2">
			<form name="searchForm" id="searchForm" method="post" 
					action="javascript:searchUnit()">
				<select name="su_Type">
					<option value="name">按单位名称搜索</option>
					<option value="id">按单位代码搜索</option>
				<select>
				
				<input type="text" name="su_Code"/>
				<input type="submit" value="搜索"/>
			</form>
		</td>
	</tr>
	
	<tr height="80%">
		<td width="50%">
			<div style="height: 100%; width: 100%; border: 1pt outset #c4c9e1; overflow: auto">
				<table style="height: 100%; width: 100%">
					<tr>
						<td>搜索结果
							（<span id="searchNum" class="numspan"></span>个，
							总<span class="numspan">
								<script language="javascript">
									document.write( unitIDs.length );
								</script>个</span>）
						</td>					
						<td>&nbsp;</td>
					</tr>
					
					<tr>
						<td width="95%">
							<div style="height: 100%; width: 100%; overflow: auto">
								<table id="unitTable" width="100%" class="xsltTable">
								</table>
							</div>
						</td>
						
						<td align="left" valign="middle" width="5%">
							<table> 
								<tr>
									<td>
										<input type="button" value="全选" 
											onclick="selectAllUnitTable()"/>
									</td>
								</tr>
								
								<tr>
									<td>
										<input type="button" value="反选" 
											onclick="deselectUnitTable()"/>
									</td>
								</tr>
								
								<tr>
									<td>
										<input type="button" value=">>" onclick="insertIntoResult()"/>
									</td>
								</tr>	
							</table>					
						</td>
					</tr>
				</table>
			</div>
		</td>
		
		
	
		<td width="50%">
			<div style="height: 100%; width: 100%; border: 1pt outset #c4c9e1; overflow: auto">
				<table style="height: 100%; width: 100%">
					<tr>
						<td>选中单位
						（共 <span id="selectNum" class="numspan"></span>个）
						</td>
						<td>&nbsp;</td>
					</tr>
					
					<tr>
						<td width="95%">
							<div style="height: 100%; width: 100%; overflow: auto">
								<table id="selectedTable" border="1" width="100%" class="xsltTable">
								</table>
							</div>
						</td>
						
						<td width="5%">							
							<table>
								<tr>
									<td>
										<input type="button" value="全选"
											onclick="selectAllSelectedTable()"/>
									</td>
								</tr>
								
								<tr>
									<td>
										<input type="button" value="反选"
											onclick="deselectSelectedTable()"/>
									</td>
								</tr>
								
								<tr>
									<td>
										<input type="button" value=">>" onclick="removeFromResult()"/>
									</td>
								</tr>
							</table>
						</td>		
					</tr>
				</table>
			</div>
		</td>
	</tr>
	
	<tr>
		<td colspan="2" align="center">
			&nbsp;&nbsp;
			<input type="button" value="确定" onclick="doneSelect()"/>
			&nbsp;
			<input type="button" value="取消" onclick="javascript:window.close()"/>
			&nbsp;&nbsp;
		</td>
	</tr>
</table>


</body>
</html>

<%!
void java2javascript( javax.servlet.jsp.JspWriter out, 
		 java.util.Collection nodesCol, 
		 String unitIDs, 
		 String unitNames )
	throws java.io.IOException
{
	int index = 0;
	
	java.util.Iterator iter = nodesCol.iterator();
	
	while( iter.hasNext() ) {
		cn.com.youtong.apollo.data.UnitTreeNode node = 
			(cn.com.youtong.apollo.data.UnitTreeNode) iter.next();
		
		out.print( unitIDs );
		out.print( "[" );
		out.print( index );
		out.print( "]=\"" );
		out.print( node.id() );
		out.println( "\";" );
		
		out.print( unitNames );
		out.print( "[" );
		out.print( index );
		out.print( "]=\"" );
		out.print( node.getUnitName() );
		out.println( "\";" );
		
		index++;
	}
}

void java2javascript( javax.servlet.jsp.JspWriter  out,
		      String[] preSelect,
		      String unitIDs )
	throws java.io.IOException
{
	if( preSelect == null )
		return;
		
	for( int i=0; i<preSelect.length; i++ )
	{
		out.print( unitIDs );
		out.print( "[" );
		out.print( i );
		out.print( "]=\"" );
		out.print( preSelect[i] );
		out.println( "\";" );
	}
}
%>