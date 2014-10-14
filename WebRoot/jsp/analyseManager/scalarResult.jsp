<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<HTML>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<link type="text/css" rel="StyleSheet" href="../csslib/sortabletable.css" />
<script  language= "javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/sortabletable.js"></script>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<TITLE>查询结果</TITLE>
</HEAD>

<%
    String webRoot = request.getContextPath();
    String operation = request.getParameter("operation");

    //得到服务器传来的数据，把它转换成合适的数组
    ScalarResultForm scalarResultForm = (ScalarResultForm)request.getAttribute("result"); // 查询结果
    ScalarQueryForm condition = (ScalarQueryForm)request.getAttribute("condition"); // 查询条件
%>

<SCRIPT language="javascript">
	//指标值三维数组定义
	//第一维，代表各个单位
	//第二维，代表各个所属期
	//第三维，代表各个指标名称
	var scareArray = new Array;
	//指标名称数组定义
	var scareNames = new Array;
	//单位名称数组定义
	var userNames  = new Array;
	//所属期名字数组定义
	var periods = new Array;

	var titleRowArray = new Array; // 表格标题
<%
	//给指标名称数组赋值
	ScalarForm [] scalars = scalarResultForm.getScalars();
	java2javascript( out, scalars, "scareNames" );

	//给单位名称数组赋值

	//转换指标名称数组
	UnitTreeNode[] units = scalarResultForm.getUnits();
	java2javascript( out, units, "userNames" );

	//给所属期名字数组赋值
	TaskTime[] taskTimes = scalarResultForm.getTaskTimes();
	Task task = (Task)request.getAttribute("task");

	java2javascript( out, taskTimes, "periods", task.getTaskType() );

	//给指标值三维数组赋值
	Object[][][] result = scalarResultForm.getResult();
	java2javascript( out, result, "scareArray" );

%>
	//结果数组
	var convertArray;
	//指标值索引标记
	var scareFlag;
	//查询方式（维度）索引标记
	var mothodFlag;
	//转换标记(用来转换指标结果的行列)
	var changeFlag = false;
</SCRIPT>

<SCRIPT language="javascript">

//根据传入的二维数据数组打印表格
function printTable( sourceArray ) {
	var oRow, oCell;
	for ( var i = 0; i < sourceArray.length; i++ ) {
		if ( i == 0 ) {
			//thead
			oRow = oTHead0.insertRow();
		} else {
			//tbody
			oRow = oTBody0.insertRow();
		}
		for ( var j = 0; j < sourceArray[i].length; j++ ) {
			oCell = oRow.insertCell();
			oCell.innerText = sourceArray[i][j];
		}
	}

	var oTable = document.getElementById( "oTable" );
	var oTypes = new Array();

	var columns = oTable.rows( 0 ).cells.length;
	for ( var i = 0; i < columns; i++ ) {
		if ( i == 0 ) {
			oTypes[i] = "String";
		} else {
			oTypes[i] = "Number";
		}
	}

	var st = new SortableTable(oTable, oTypes);
}

//删除动态表格
function deleteTable() {
	//thead
	j=oTHead0.rows.length;
	for(var i=0; i<j; i++){
		oTHead0.deleteRow(0);
	}

	//tbody
	j=oTBody0.rows.length;
	for(var i=0; i<j; i++){
		oTBody0.deleteRow(0);
	}
}

//初始化显示结果的二维数组（装载指标名称，单位名称等标题头）
function iniTitleNameOfTable( lineTitle,rowTitle ) {
	//根据行数初始化数组
	var lineLength = lineTitle.length;
	var rowLength = rowTitle.length;
	convertArray = new Array;

	for ( var i = 0; i <= rowLength; i++ ) {
		convertArray[i] = new Array;
	}

	convertArray[0][0] = "------";
	//初始化标题值（行标，列标）
	for ( var i = 0; i < lineLength; i++ ) {
		convertArray[0][i + 1] = lineTitle[i];
	}
	for ( var i = 0; i < rowLength; i++ ) {
		convertArray[i + 1][0] = rowTitle[i];
	}
}

//以指标为固定维度，所属期为行，用户为列，显示数据
function displayDataByScareByPU( scareValue ) {
	iniTitleNameOfTable( userNames,periods );

	for(var i = 0; i < scareArray[0].length; i++)
	{
		for(var j = 0; j < scareArray.length; j++)
		{
			convertArray[i+1][j+1] = scareArray[j][i][scareValue];
		}
	}
	printTable(convertArray);
}

//以指标为固定维度，用户为行，所属期为列，显示数据
function displayDataByScareByUP( scareValue ) {
	iniTitleNameOfTable( periods,userNames );
	for( var i = 0; i < scareArray.length; i++ )
	{
		for(var j = 0; j < scareArray[0].length; j++)
		{
			convertArray[i+1][j+1] = scareArray[i][j][scareValue];
		}
	}
	printTable(convertArray);
}

//以用户为固定维度，指标为行，所属期为列，显示数据
function displayDataByuserBySP( user ) {
	iniTitleNameOfTable(periods,scareNames);
	for(var i = 0; i < scareArray[0][0].length; i++)
	{
		for(var j = 0; j < scareArray[0].length; j++)
		{
			convertArray[i+1][j+1] = scareArray[user][j][i];
		}
	}
	printTable(convertArray);
}

//以用户为固定维度，所属期为行，指标为列，显示数据
function displayDataByuserByPS(user) {
	iniTitleNameOfTable(scareNames,periods);
	for(var i = 0; i < scareArray[0].length; i++)
	{
		for(var j = 0; j < scareArray[0][0].length; j++)
		{
			convertArray[i+1][j+1] = scareArray[user][i][j];
		}
	}
	printTable(convertArray);
}
//以所属期为固定维度，指标为行，用户为列，显示数据
function displayDataByperiodBySU(period){
	iniTitleNameOfTable(userNames,scareNames);
	for(var i = 0; i < scareArray[0][0].length; i++)
	{
		for(var j = 0; j < scareArray.length; j++)
		{
			convertArray[i+1][j+1] = scareArray[j][period][i];
		}
	}
	printTable(convertArray);
}
//以所属期为固定维度，用户为行，指标为列，显示数据
function displayDataByperiodByUS(period){
	iniTitleNameOfTable(scareNames,userNames);
	for(var i = 0; i < scareArray.length; i++)
	{
		for(var j = 0; j < scareArray[0][0].length; j++)
		{
			convertArray[i+1][j+1] = scareArray[i][period][j];
		}
	}
	printTable(convertArray);
}

//根据不同的维度创建维度下拉列表框
function createDropDownList(names,indexName){
	document.form1.select1.style.display="inline";
	deleteTable();
	clearSelect(document.form1.select1);

	for(var i=0;i<names.length;i++){
		var oOption = new Option(names[i],i);
		document.form1.select1.add(oOption);
	}
        createTableByListValue(document.form1.select1);
    	//doform1();
}
//根据用户在下拉列表框选择的不同维度值生成不同的表格
function createTableByListValue(obj){
	deleteTable();
	//维度下拉列表框索引值（标识固定那一个维度查询）
	selectIndexOpt = obj.options[obj.selectedIndex].value;
	scareFlag = selectIndexOpt;
	
	//查询方式索引值（标识使用那一种方式查询）
	for(var i=0;i<document.form1.radios.length;i++){
		if (document.form1.radios[i].checked) {
			var justValue = document.form1.radios[i].value;
			
			mothodFlag = justValue;
			
			if(justValue==1) {
				displayDataByperiodByUS(selectIndexOpt);
			}
			
			if(justValue==2)	{
				displayDataByuserByPS(selectIndexOpt);
			}
			
			if(justValue==3)	{
				displayDataByScareByPU(selectIndexOpt);
			}
		}
	}
	changeFlag=false;
}
//交换行列
function exchangeRowAndColumn(){
    if(oTBody0.rows.length==0){
       alert("请选择结果显示方式");
       return false;
    }
	deleteTable();

    if(mothodFlag==1){
        if(changeFlag){
          displayDataByperiodByUS(scareFlag);
          changeFlag=false;
          }
        else{
		  displayDataByperiodBySU(scareFlag);
		  changeFlag=true;
        }
    }
    if(mothodFlag==2){
        if(changeFlag){
          displayDataByuserByPS(scareFlag);
          changeFlag=false;
          }
        else{
		  displayDataByuserBySP(scareFlag);
		  changeFlag=true;
        }
    }
    if(mothodFlag==3){
        if(changeFlag){
          displayDataByScareByPU(scareFlag);
          changeFlag=false;
          }
        else{
          displayDataByScareByUP(scareFlag);
		  changeFlag=true;
        }
    }
// doform1();
}

/**
* 向form1表单创建scalarDatas隐藏域，这些域值是排列过后的scalarDatas值
*/
function createHiddenScalarDatas() {
        removeHiddenScalarDatas();

	var resultHead = document.getElementById("oTHead0");
	var resultHeadRow;
	var resultHead2Row;
	if( mothodFlag == 4 )
	{
		resultHeadRow = resultHead.rows( resultHead.rows.length - 2 );
		resultHead2Row= resultHead.rows( resultHead.rows.length - 1 );
	} else
	{
		resultHeadRow = resultHead.rows( resultHead.rows.length - 1 );
	}
	var resultHeadRowCells = resultHeadRow.cells;
	var resultHeadRow2Cells;
	if(resultHead2Row!=undefined) resultHeadRow2Cells=resultHead2Row.cells;
	var str="";
	var strstr="";
	if(resultHeadRowCells[1].colSpan>1){
		for(var ii=0;ii<resultHeadRowCells[1].colSpan;ii++){
			strstr+=", ";
		}
	}
	for(var j=0; j<resultHeadRowCells.length-1; j++) {
		//alert('333----->'+str)
    	if(j==0){
    	    str = str+resultHeadRowCells[j].innerText+",";
    	   // alert('337---->'+str);
        }else{
            str = str+resultHeadRowCells[j].innerText+",";
    	}
    }
		str = str + resultHeadRowCells[j].innerText;
		oInput =document.createElement("input");
        oInput.type = "hidden";
        oInput.name = "scalarDatas";
       // alert('344----->'+str);
        oInput.value = str;
       // alert('344---oInput.value-->'+oInput.value);
        document.form1.appendChild(oInput);
    if(resultHeadRow2Cells!=undefined&&resultHeadRow2Cells.length>0){
        var stt=", ";
        for(var j=0; j<resultHeadRow2Cells.length-1; j++) {
        	stt = stt+resultHeadRow2Cells[j].innerText+", ";
        }
        stt = stt + resultHeadRow2Cells[j].innerText;
    	var ooInput =document.createElement("input");
        ooInput.type = "hidden";
        ooInput.name = "scalarDatas";
      //  alert('sthead---'+stt);
        ooInput.value = stt;
     //   alert('--stt--359--->'+stt);
        document.form1.appendChild(ooInput);
    }
	var resultTBody = document.getElementById("oTBody0");
	var resultTBodyRows = resultTBody.rows;
		 for (var i=0; i<resultTBodyRows.length; i++)  {
            var str="";
            var rowCells = resultTBodyRows[i].cells;
            for(var j=0; j<rowCells.length; j++){
            	if(j==rowCells.length-1){
                	str = str+rowCells[j].innerText;
             	}else{
                	str = str+rowCells[j].innerText+",";
             	}
         	}
         oInput =document.createElement("input");
	     oInput.type = "hidden";
    	 oInput.name = "scalarDatas";
    	// alert('str---'+str);
    	 oInput.value = str;
    	 document.form1.appendChild(oInput);
   }
}

/**
* 清空form1表单中所有的sacalrDatas隐藏域
*/
function removeHiddenScalarDatas() {
	var cols = new Array();
	var count = 0;

	// 查找隐藏域
	for(var i = 0; i < form1.children.length; i++)
	{
	        if(form1.children[i].name == "scalarDatas")
	        {
			cols[count] = form1.children[i];
			count++;
		}
	}

	// 删除隐藏域
	for(var i = 0; i < cols.length; i++)
	{
		form1.removeChild(cols[i]);
	}
}
//把指标查询结果导出成excel
function buildExcel(){
    if(oTBody0.rows.length==0){
       alert("请选择结果显示方式");
       return false;
    }
	createHiddenScalarDatas();
    form1.action="analyse?operation=<%=AnalyseServlet.SHOW_SCALAR_EXCEL%>";
	form1.target = "_blank";
	form1.submit();
	//removeHiddenScalarDatas();
}
//把指标查询结果导出成图表
function buildBar(){
    if(oTBody0.rows.length==0){
       alert("请选择结果显示方式");
       return false;
    }
    var url="analyse?operation=<%=AnalyseServlet.SHOW_SCALAR_CHART_PAGE%>&chartType=bar";
	var radios = document.getElementsByName('radios');
   	var _typeflag = '';
   	for(var i=0;i<radios.length;i++)
   	{
   		if(radios[i].checked==true){
   			_typeflag = radios[i].value;
   			break;
   		}
   	}
   if(_typeflag=='4')
   {
   	 alert('系统无法生成柱状图,请换成其他查询结果!');
   	 return;
   }
   createHiddenScalarDatas();
   form1.action=url;
   form1.target = "_blank";
   form1.submit();
}
//把指标查询结果导出成图表
function buildPie(){
    if(oTBody0.rows.length==0){
       alert("请选择结果显示方式");
       return false;
    }
    var url="analyse?operation=<%=AnalyseServlet.SHOW_SCALAR_CHART_PAGE%>&chartType=pie";
	var radios = document.getElementsByName('radios');
	   	var _typeflag = '';
	   	for(var i=0;i<radios.length;i++)
	   	{
	   		if(radios[i].checked==true){
	   			_typeflag = radios[i].value;
	   			break;
	   		}
	   	}
	   if(_typeflag=='4')
	   {
	   	 alert('系统无法生成饼状图,请换成其他查询结果!');
	   	 return;
	   }
    createHiddenScalarDatas();
    form1.action=url;
    form1.target = "_blank";
    form1.submit();
}
//把指标查询结果导出成图表
function buildLinear(){
    if(oTBody0.rows.length==0){
       alert("请选择结果显示方式");
       return false;
    }
    var url="analyse?operation=<%=AnalyseServlet.SHOW_SCALAR_CHART_PAGE%>&chartType=linear";

    createHiddenScalarDatas();
    form1.action=url;
    form1.target = "_blank";
    form1.submit();
}

function showScalarConditionPage()
{
    form1.action = "analyse?operation=<%= AnalyseServlet.SHOW_SCALAR_CONDITION_PAGE %>";
    form1.submit();
}


// 显示所有的数据，而且显示表头
function displayAllData()
{
    mothodFlag = 4;
	deleteTable(); // 删除表格内容
	clearSelect(document.form1.select1); // 下拉框失效
    document.form1.select1.style.display="none";
	// scareArray, scareNames, userNames, periods, convertArray

	// print title
	for(var i=0; i<titleRowArray.length; i++ )
	{
		var oTitleRow = oTHead0.insertRow();

		for( var j=0; j<titleRowArray[i].length; j++ )
		{
			var oTitleCell = oTitleRow.insertCell();
			oTitleCell.innerText = titleRowArray[i][j][0];

			oTitleCell.colSpan= titleRowArray[i][j][2]*periods.length;
			oTitleCell.rowSpan = titleRowArray[i][j][3];
		}
	}

	// init table titles
	var oFirstRow = oTHead0.insertRow();
	var oCell = oFirstRow.insertCell();
	oCell.innerText = "------";

	var oTimeRow = oTHead0.insertRow();
	var oTimeCell = oTimeRow.insertCell();
	oTimeCell.insertText="&nbsp;&nbsp;";

	for(var i=0; i< scareNames.length; i++ )
	{
		oCell = oFirstRow.insertCell();
		oCell.colSpan=periods.length;
		oCell.innerText = scareNames[i];

		for( var k=0; k<periods.length; k++ )
		{
			oTimeCell = oTimeRow.insertCell();
			oTimeCell.innerText = periods[k];
		}
	}

	// 初始化其他行，行头是单位名称，其他列是数字
	for(var i=0; i<userNames.length; i++ )
	{
		var oRow = oTBody0.insertRow();
		var oCell = oRow.insertCell();
		oCell.innerText=userNames[i];

		for( var j=0; j<scareNames.length; j++ )
		{
			for( var k=0; k<periods.length; k++ )
			{
				oCell = oRow.insertCell();
				oCell.innerText = scareArray[i][k][j];
			}
		}
	}

	var oTable = document.getElementById( "oTable" );
	var oTypes = new Array();

	oTypes[0] = "String";
	for ( var i = 0; i < scareNames.length; i++ ) {
		oTypes[i] = "Number";
	}

	var st = new SortableTable(oTable, oTypes);

	//doform1();
}

</SCRIPT>
<body>

  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

<form name="form1" method="post">
  <table >
      <tr align=left>
            <td>查询结果：</td>
            <td><INPUT  type=radio name=radios value="1"
            		onclick="createDropDownList(periods,1)">
            	按任务时间</td>
            <td><INPUT  type=radio name=radios value="2"
            		onclick="createDropDownList(userNames,2)">
            	按单位</td>
            <td><INPUT type=radio name=radios value="3"
            		onclick="createDropDownList(scareNames,3)">
            	按指标</td>
            <td><INPUT type="radio" name="radios" value="4"
            		onclick="displayAllData()" checked="checked">
            	所有数据&nbsp;&nbsp;&nbsp;
            </td>

              <td>
              <SELECT onChange="createTableByListValue(this);" name=select1>
              </SELECT>
              </td>
      </tr>
      <tr align=left>
             <td>
                <INPUT onclick="exchangeRowAndColumn()" type=button value="行列转置">
             </td>
             <td>
                <INPUT onclick="buildExcel()" type="button" value="导出到EXCEL">
             </td>
             <td>
                <INPUT onclick="buildBar()" type="button" value="直方图表示">
             </td>
             <td>
                <INPUT onclick="buildPie()" type="button" value="饼图表示">
             </td>
                <!--td>
                <INPUT onclick="buildLinear()" type="button" value="线图表示">
                </td-->
       </tr>
  </table>
<!-- hr noshade size="1"/>


<table>
	<tr align="left">
		<td>
		分析方法：
		</td>
		<td>
		<select name="method" onchange="changeDefaultTitleValue( this )">
			<option value="LinearRegression">一元线性回归</option>
			<option value="Correlation">相关系数</option>
			<option value="ReviseExponent">修正指数曲线</option>
			<option value="GongBoZi">龚柏兹曲线</option>
			<option value="SimpleAverage">简单平均</option>
			<option value="MovingAverage">季节移动平均</option>
		</select>
		</td>

		<td>
		指标一：
		</td>
		<td>
		<select name="scalarx">
		</select>
		</td>

		<td>
		指标二：
		</td>
		<td>
		<select name="scalary">
		</select>
		</td>

	</tr>
	<tr>
		<td>
		主题：
		</td>

		<td>
		<input type="text" name="title" value="一元线性回归"/>
		</td>

		<td colspan="4">
		&nbsp;&nbsp;
		<input type="button" value="统计分析" onclick="return submitform1()"/>
		</td>
	</tr>
</table-->

<hr noshade size="1"/>


<TABLE  class="sort-table" id="oTable" border="1">

  <THead id="oTHead0"></THead>
  <TBODY id="oTBody0"></TBODY>
</TABLE>

</form>
<script language="javascript">
//初始按按任务时间方式显示结果
//form1.radios[1].click();
displayAllData();
</script>
  <jsp:include page= "/jsp/footer.jsp" />

</BODY>
</HTML>


<%!
void java2javascript( javax.servlet.jsp.JspWriter  out,
		      ScalarForm [] scalars,
		      String scareNames )
	throws java.io.IOException
{
	for( int i=0;i<scalars.length;i++ ) {
		out.print( scareNames );
		out.print( "[" );
		out.print( i );
		out.print( "]=\"" );
		out.print( scalars[i].getName() );
		out.println( "\";" );
	}
}

void java2javascript( javax.servlet.jsp.JspWriter  out,
		      UnitTreeNode [] units,
		      String userNames )
	throws java.io.IOException
{
	for( int i=0;i<units.length;i++ ) {
		out.print( userNames );
		out.print( "[" );
		out.print( i );
		out.print( "]=\"" );
		out.print( units[i].getUnitName() );
		out.println( "\";" );
	}
}

void java2javascript( javax.servlet.jsp.JspWriter  out,
		      TaskTime [] taskTimes,
		      String periods,
		      int reportType )
	throws java.io.IOException
{
	for( int i=0;i<taskTimes.length;i++ ) {
		out.print( periods );
		out.print( "[" );
		out.print( i );
		out.print( "]=\"" );

		Date fromTime = taskTimes[i].getFromTime();

		if( reportType == Task.REPORT_OF_MONTH )
		{
			out.print( Convertor.date2MonthlyString( fromTime ) );
		} else if( reportType == Task.REPORT_OF_DAY )
		{
			out.print( Convertor.date2ShortString( fromTime ) );
		}

		out.println( "\";" );
	}
}


void java2javascript( javax.servlet.jsp.JspWriter  out,
		      Object[][][] result,
		      String scareArray )
	throws java.io.IOException
{
	for( int i=0;i<result.length;i++ ) {

		out.print( scareArray );
		out.print( "[" );
		out.print( i );
		out.print( "]" );
		out.println( "= new Array;" );

     		for( int j=0;j<result[0].length; j++ ) {

     			out.print( scareArray );
     			out.print( "[" );
			out.print( i );
			out.print( "]" );
			out.print( "[" );
			out.print( j );
			out.print( "]" );
     			out.println( "= new Array;" );

			for( int k=0;k<result[0][0].length;k++ ) {

				out.print( scareArray );
				out.print( "[" );
				out.print( i );
				out.print( "]" );
				out.print( "[" );
				out.print( j );
				out.print( "]" );
				out.print( "[" );
				out.print( k );
				out.print( "]" );
				out.print( "=\"" );

				String valueStr = " ";
				if( result[i][j][k] == null || result[i][j][k].equals("") ) {
					//out.println( " " );
				} else {
                			//格式化double数据
                			Object value = result[i][j][k];
                			if(value instanceof Double) {
                    				valueStr = Convertor.formatDouble((Double)value);
               				} else
			                {
			                    valueStr = value.toString();
			                }
			        }

			        out.print( valueStr );
			        out.println( "\";" );
			}
		}
	}
}
%>