<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>

<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

List templateList = (List) request.getAttribute( "templateList" );
List resultList = (List) request.getAttribute( "resultList" );
List unitList = (List) request.getAttribute( "unitList" );


request.removeAttribute( "templateList" );
request.removeAttribute( "resultList" );
request.removeAttribute( "unitList" ); 

Boolean oshowIndex = (Boolean) request.getAttribute( "showIndex" );
Boolean oshowUnitName = (Boolean) request.getAttribute( "showUnitName" );

boolean showIndex = oshowIndex == null? true: oshowIndex.booleanValue();
boolean showUnitName = oshowUnitName == null? true: oshowUnitName.booleanValue();
  
%>

<HTML>
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<link type="text/css" rel="stylesheet" href="../csslib/webfxlayout.css" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">

<TITLE>查询结果</TITLE>

<style type="text/css">

.dynamic-tab-pane-control .tab-page {

   overflow : hidden
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {

}

body {
	margin:		1px;
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

<style type="text/css">
<!--
<%
for( int i=0, size=templateList.size(); i<size; i++ )
{
	ScalarQueryTemplate template = (ScalarQueryTemplate) templateList.get( i );
	java2css( out, template );
}
%>
-->
</style>
</HEAD>


<body>

  <jsp:include page= "/jsp/logo.jsp" />

<p>
<input type="button" value="另存为Excel文件" onclick="javascript:downloadAttatchment()"/>

<div class="tab-pane" id="taskPane">

<script type="text/javascript">
var taskPane = new WebFXTabPane( document.getElementById( "taskPane" ),false);

function getSelectedID()
{
  if(taskPane!=null && taskPane.selectedIndex!=null)
  {
    return taskPane.pages[taskPane.selectedIndex].element.id;
  }else
  {
    return "";
  }
}

function downloadAttatchment()
{
  window.open("utilServlet?operation=<%=UtilServlet.DOWN_EXCEL%>", "_self");
}

</script>
	
	<% for( int i=0, size=templateList.size(); i<size; i++ ) 
	{
		ScalarQueryTemplate template = (ScalarQueryTemplate) templateList.get( i );
		ScalarResultForm result = (ScalarResultForm) resultList.get( i );
		UnitTreeNode[] units = (UnitTreeNode[]) unitList.get( i );
		
		TaskTime[] taskTimes = result.getTaskTimes();
		HeadForm head = template.getHead();
		BodyForm body = template.getBody();
	 %>
	<div class="tab-page" id="tpl_<%=template.getTemplateID()%>">
		<h2 class="tab"><%=template.getName()%>&nbsp;</h2>
		
		<script type="text/javascript">
			taskPane.addTabPage( document.getElementById( "tpl_<%=template.getTemplateID()%>" ) );
		</script>
		
		<table  width="100%" height="100%" border="0">
          	<tr><td>
		<table xmlns:fo="http://www.w3.org/1999/XSL/Format" class="xsltTable">
			<%/**print head */%>
			
			<%
			RowForm firstRow = head.getRows()[0];
			%>
			<tr bgcolor="<%=head.getDefaultColor()%>">
			<%if( head.getDefaultFontID()!=0 ) { %> 
				class="<%=cssName( template, head.getDefaultFontID() )%>">
			<% } %>
			<%
			if( showIndex ) 
			{ 
			%>
				<td class="tdcssone" rowspan="<%=taskTimes.length>1? (head.getRownum() + 1): (head.getRownum())%>" nowrap>
					<div align="center" valign="bottom">序号</div>
				</td>
			<%
			}
			%>
			<%
			if( showUnitName ) 
			{ 
			%>
				<td class="tdcssone" rowspan="<%=taskTimes.length>1? (head.getRownum() + 1): (head.getRownum())%>" nowrap>
					<div align="center" valign="bottom">单位名称</div>
				</td>
			<%
			}
			%>
			
			<%
			RowForm[] rows = head.getRows();
			for( int cellIndex=0, cellNums=rows[0].getCells().length; 
					cellIndex < cellNums; cellIndex++ )
			{
				CellForm cell = rows[0].getCells()[ cellIndex ];
			%>
				<td class="tdcssone" colspan="<%=cell.getColspan()*taskTimes.length%>" rowspan="<%=cell.getRowspan()%>"  bgcolor="<%=cell.getBgColor()%>" nowrap>
					<div align="<%=cell.getHalign()%>" valign="<%=cell.getValign()%>"
					<%if( cell.getFontID()!=0 ) {%> 
						class="<%=cssName( template, cell.getFontID() )%>">
					<% } %>
					<%=cell.getLabel()%></div>
				</td>
			<%
			}
			%>
			</tr>
			
			<%
			for( int rowIndex=1; rowIndex<rows.length; rowIndex++ )
			{
			%>
			<tr  bgcolor="<%=head.getDefaultColor()%>">
			<%
				for( int cellIndex=0, cellNum=rows[rowIndex].getCells().length;
						cellIndex<cellNum; cellIndex++ )
				{
					CellForm cell = rows[rowIndex].getCells()[ cellIndex ];
				%>
				<td class="tdcssone" colspan="<%=cell.getColspan()*taskTimes.length%>" rowspan="<%=cell.getRowspan()%>" bgcolor="<%=cell.getBgColor()%>" nowrap>
					<div align="<%=cell.getHalign()%>" valign="<%=cell.getValign()%>"
					<%if( cell.getFontID()!=0 ) { %> 
						class="<%=cssName( template, cell.getFontID() )%>">
					<% } %>
					<%=cell.getLabel()%></div>
				</td>
				<%
				}
				%>
			</tr>
			<%
			}
			%>

		<% if( taskTimes.length > 1 )
		{
		%>
			<tr  bgcolor="<%=head.getDefaultColor()%>">
			<%/**print last row tasktime*/%>
			<%
			for ( int cellIndex=0, cellNum=head.getColnum();
					cellIndex<cellNum; cellIndex++ )
			{
				for ( int timeIndex=0; timeIndex<taskTimes.length; timeIndex++ )
				{
				%>
				<td class="tdcssone" nowrap>
					<div align="center">
					<%=Convertor.date2MonthlyString( taskTimes[timeIndex].getFromTime() )%></div>
				</td>
				<%
				}
			}
			%>
			</tr>
		<%
		}
		%>
		
			
			<%/**print body */ %>
		<%
RowForm[] rowForms = template.getBody().getRows();
// 找个一个非汇总航的cellForms
CellForm[] cellForms = null;
for( int iRowIndex=0; iRowIndex<rowForms.length; iRowIndex++ )
{
	if( !rowForms[iRowIndex].isTotalRow() )
	{
		cellForms = rowForms[iRowIndex].getCells();
		break;
	}
}

// 构造Format
java.text.DecimalFormat[] decFmts = new java.text.DecimalFormat[cellForms.length];
for( int iCellIndex=0; iCellIndex<cellForms.length; iCellIndex++ )
{
	if( cellForms[iCellIndex].getContentStyle().equals( "number" ) )
	{
		if( Util.isEmptyString( cellForms[iCellIndex].getFormatStyle() ) )
		{
			decFmts[iCellIndex] = new java.text.DecimalFormat( "0.##" ); // 缺省给它保留两位有效数字
		}
		else
		{			
			decFmts[iCellIndex] = new java.text.DecimalFormat( cellForms[iCellIndex].getFormatStyle() );
		}
	}
}

for( int rowIndex=0; rowIndex<rowForms.length; rowIndex++ )
{
	RowForm bodyRow = rowForms[rowIndex];
	if( bodyRow.isTotalRow() )
	{%>
		<tr>
	<%
		int totalCellColspan = 0;
		if( showIndex ) {
			totalCellColspan++;
		}
		if( showUnitName ) {
			totalCellColspan++;
		}
		
		if( totalCellColspan > 0 ) {
		%>
			<td colspan="<%=totalCellColspan%>" class="tdcssone" nowrap>合计</td>
		<%
		}
		
		Object[][][] scalarData = result.getResult();
		for( int iCellIndex=0; iCellIndex<cellForms.length; iCellIndex++ )
		{
			if( cellForms[iCellIndex].getContentStyle().equals( "number" ) )
			{				
				for( int iTimeIndex=0; iTimeIndex<scalarData[0].length; iTimeIndex++ ) {
					double sum = 0;
					for( int iUnitIndex=0; iUnitIndex<scalarData.length; iUnitIndex++ ) {
						sum += Double.parseDouble( scalarData[iUnitIndex][iTimeIndex][iCellIndex].toString() );
					}
					%>
			<td class="tdcssone" nowrap><%=decFmts[iCellIndex].format( sum )%></td>
					<%
				}
			}
			else
			{ 
				for( int iTimeIndex=0; iTimeIndex<scalarData[0].length; iTimeIndex++ ) {
			%>
			<td class="tdcssone" nowrap>&nbsp;</td>
			<%      }
			}
		}
		%>
		</tr>
	<%
	}
	else
	{
		int index=0;
		Object[][][] scalarData = null;
		
		if( result != null ) {
			scalarData = result.getResult();
		}
		
		if( scalarData == null ) {
			scalarData = new String[0][0][0];
		}
		
		for( int unitIndex=0; unitIndex<scalarData.length; unitIndex++ )
		{
		%>
			<tr  bgcolor="<%=body.getDefaultColor()%>">
			<%
			if( showIndex )
			{
			%>
				<td class="tdcssone" nowrap>
					<div align="center"><%=unitIndex+1%></div>
				</td>
			<% } %>
			<%
			if( showUnitName )
			{
			%>
				<td class="tdcssone" nowrap>
					<div align="center"><%=units[unitIndex].getUnitName()%></div>
				</td>
			<% } %>
			
			<%
			for( int scalarIndex=0; scalarIndex<scalarData[unitIndex][0].length; scalarIndex++ )
			{
				CellForm cell = bodyRow.getCells()[scalarIndex];
				for( int timeIndex=0; timeIndex<scalarData[unitIndex].length; timeIndex++ )
				{
				%>
				<td  class="tdcssone"  bgcolor="<%=cell.getBgColor()%>" nowrap>
					<div align="<%=cell.getHalign()%>" valign="<%=cell.getValign()%>"
					<%if( cell.getFontID()!=0 ) { %> 
						class="<%=cssName( template, cell.getFontID() )%>">
					<% } %>
					<% if( cell.getContentStyle() != null 
						&& cell.getContentStyle().equals( "number" ) ) {
					%>
					<%=decFmts[scalarIndex].format( scalarData[unitIndex][timeIndex][scalarIndex] )%>
					<% } else { %>
					<%=scalarData[unitIndex][timeIndex][scalarIndex]%>
					<% } %>
					</div>
				</td>
				<%
				}
			}
			%>			
			</tr>
		<%
		}
	}
}
		%>
		</table>
		</td></tr>
		</table>
	</div>
	<% 
	} 
	%>

</div>

<script type="text/javascript">
	setupAllTabs();
</script>


<p>&nbsp;

<jsp:include page= "/jsp/footer.jsp" />

</BODY>
</HTML>


<%!
void java2css( javax.servlet.jsp.JspWriter out, ScalarQueryTemplate template)
	throws java.io.IOException, AnalyseException {
		
	// print css
	for( Iterator iter = template.getFonts(); iter.hasNext(); )
	{
		FontForm font = (FontForm) iter.next();
		
		out.println( "." + cssName( template, font.getID() ) + " { " );
		out.println( "\tfont-family: \"" + font.getName() + "\";" );
		out.println( "\tfont-size: " + font.getSize() + "px;" );
		if( font.isBold() )
			out.println( "\tfont-weight: bold;" );
		
		if( font.isItalic() )
			out.println( "\tfont-style: italic;" );
		
		if( font.isIsUnderline() || font.isStrikeThrough() ) {
			out.print( "\ttext-decoration: " );
			if( font.isIsUnderline() )
				out.print( "underline " );
			if( font.isStrikeThrough() )
				out.print( "line-through" );
			out.println( ";" );
		}
		
		out.println( "\tcolor: " + font.getColor() + ";" );
		out.println( "} " );
	}
}


String cssName( ScalarQueryTemplate template, int fontID )
{
	return "tplFont" + template.getTemplateID().intValue() + fontID;
}
%>