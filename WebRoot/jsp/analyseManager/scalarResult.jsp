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
<TITLE>��ѯ���</TITLE>
</HEAD>

<%
    String webRoot = request.getContextPath();
    String operation = request.getParameter("operation");

    //�õ����������������ݣ�����ת���ɺ��ʵ�����
    ScalarResultForm scalarResultForm = (ScalarResultForm)request.getAttribute("result"); // ��ѯ���
    ScalarQueryForm condition = (ScalarQueryForm)request.getAttribute("condition"); // ��ѯ����
%>

<SCRIPT language="javascript">
	//ָ��ֵ��ά���鶨��
	//��һά�����������λ
	//�ڶ�ά���������������
	//����ά���������ָ������
	var scareArray = new Array;
	//ָ���������鶨��
	var scareNames = new Array;
	//��λ�������鶨��
	var userNames  = new Array;
	//�������������鶨��
	var periods = new Array;

	var titleRowArray = new Array; // ������
<%
	//��ָ���������鸳ֵ
	ScalarForm [] scalars = scalarResultForm.getScalars();
	java2javascript( out, scalars, "scareNames" );

	//����λ�������鸳ֵ

	//ת��ָ����������
	UnitTreeNode[] units = scalarResultForm.getUnits();
	java2javascript( out, units, "userNames" );

	//���������������鸳ֵ
	TaskTime[] taskTimes = scalarResultForm.getTaskTimes();
	Task task = (Task)request.getAttribute("task");

	java2javascript( out, taskTimes, "periods", task.getTaskType() );

	//��ָ��ֵ��ά���鸳ֵ
	Object[][][] result = scalarResultForm.getResult();
	java2javascript( out, result, "scareArray" );

%>
	//�������
	var convertArray;
	//ָ��ֵ�������
	var scareFlag;
	//��ѯ��ʽ��ά�ȣ��������
	var mothodFlag;
	//ת�����(����ת��ָ����������)
	var changeFlag = false;
</SCRIPT>

<SCRIPT language="javascript">

//���ݴ���Ķ�ά���������ӡ���
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

//ɾ����̬���
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

//��ʼ����ʾ����Ķ�ά���飨װ��ָ�����ƣ���λ���Ƶȱ���ͷ��
function iniTitleNameOfTable( lineTitle,rowTitle ) {
	//����������ʼ������
	var lineLength = lineTitle.length;
	var rowLength = rowTitle.length;
	convertArray = new Array;

	for ( var i = 0; i <= rowLength; i++ ) {
		convertArray[i] = new Array;
	}

	convertArray[0][0] = "------";
	//��ʼ������ֵ���б꣬�б꣩
	for ( var i = 0; i < lineLength; i++ ) {
		convertArray[0][i + 1] = lineTitle[i];
	}
	for ( var i = 0; i < rowLength; i++ ) {
		convertArray[i + 1][0] = rowTitle[i];
	}
}

//��ָ��Ϊ�̶�ά�ȣ�������Ϊ�У��û�Ϊ�У���ʾ����
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

//��ָ��Ϊ�̶�ά�ȣ��û�Ϊ�У�������Ϊ�У���ʾ����
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

//���û�Ϊ�̶�ά�ȣ�ָ��Ϊ�У�������Ϊ�У���ʾ����
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

//���û�Ϊ�̶�ά�ȣ�������Ϊ�У�ָ��Ϊ�У���ʾ����
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
//��������Ϊ�̶�ά�ȣ�ָ��Ϊ�У��û�Ϊ�У���ʾ����
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
//��������Ϊ�̶�ά�ȣ��û�Ϊ�У�ָ��Ϊ�У���ʾ����
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

//���ݲ�ͬ��ά�ȴ���ά�������б��
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
//�����û��������б��ѡ��Ĳ�ͬά��ֵ���ɲ�ͬ�ı��
function createTableByListValue(obj){
	deleteTable();
	//ά�������б������ֵ����ʶ�̶���һ��ά�Ȳ�ѯ��
	selectIndexOpt = obj.options[obj.selectedIndex].value;
	scareFlag = selectIndexOpt;
	
	//��ѯ��ʽ����ֵ����ʶʹ����һ�ַ�ʽ��ѯ��
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
//��������
function exchangeRowAndColumn(){
    if(oTBody0.rows.length==0){
       alert("��ѡ������ʾ��ʽ");
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
* ��form1������scalarDatas��������Щ��ֵ�����й����scalarDatasֵ
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
* ���form1�������е�sacalrDatas������
*/
function removeHiddenScalarDatas() {
	var cols = new Array();
	var count = 0;

	// ����������
	for(var i = 0; i < form1.children.length; i++)
	{
	        if(form1.children[i].name == "scalarDatas")
	        {
			cols[count] = form1.children[i];
			count++;
		}
	}

	// ɾ��������
	for(var i = 0; i < cols.length; i++)
	{
		form1.removeChild(cols[i]);
	}
}
//��ָ���ѯ���������excel
function buildExcel(){
    if(oTBody0.rows.length==0){
       alert("��ѡ������ʾ��ʽ");
       return false;
    }
	createHiddenScalarDatas();
    form1.action="analyse?operation=<%=AnalyseServlet.SHOW_SCALAR_EXCEL%>";
	form1.target = "_blank";
	form1.submit();
	//removeHiddenScalarDatas();
}
//��ָ���ѯ���������ͼ��
function buildBar(){
    if(oTBody0.rows.length==0){
       alert("��ѡ������ʾ��ʽ");
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
   	 alert('ϵͳ�޷�������״ͼ,�뻻��������ѯ���!');
   	 return;
   }
   createHiddenScalarDatas();
   form1.action=url;
   form1.target = "_blank";
   form1.submit();
}
//��ָ���ѯ���������ͼ��
function buildPie(){
    if(oTBody0.rows.length==0){
       alert("��ѡ������ʾ��ʽ");
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
	   	 alert('ϵͳ�޷����ɱ�״ͼ,�뻻��������ѯ���!');
	   	 return;
	   }
    createHiddenScalarDatas();
    form1.action=url;
    form1.target = "_blank";
    form1.submit();
}
//��ָ���ѯ���������ͼ��
function buildLinear(){
    if(oTBody0.rows.length==0){
       alert("��ѡ������ʾ��ʽ");
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


// ��ʾ���е����ݣ�������ʾ��ͷ
function displayAllData()
{
    mothodFlag = 4;
	deleteTable(); // ɾ���������
	clearSelect(document.form1.select1); // ������ʧЧ
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

	// ��ʼ�������У���ͷ�ǵ�λ���ƣ�������������
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
            <td>��ѯ�����</td>
            <td><INPUT  type=radio name=radios value="1"
            		onclick="createDropDownList(periods,1)">
            	������ʱ��</td>
            <td><INPUT  type=radio name=radios value="2"
            		onclick="createDropDownList(userNames,2)">
            	����λ</td>
            <td><INPUT type=radio name=radios value="3"
            		onclick="createDropDownList(scareNames,3)">
            	��ָ��</td>
            <td><INPUT type="radio" name="radios" value="4"
            		onclick="displayAllData()" checked="checked">
            	��������&nbsp;&nbsp;&nbsp;
            </td>

              <td>
              <SELECT onChange="createTableByListValue(this);" name=select1>
              </SELECT>
              </td>
      </tr>
      <tr align=left>
             <td>
                <INPUT onclick="exchangeRowAndColumn()" type=button value="����ת��">
             </td>
             <td>
                <INPUT onclick="buildExcel()" type="button" value="������EXCEL">
             </td>
             <td>
                <INPUT onclick="buildBar()" type="button" value="ֱ��ͼ��ʾ">
             </td>
             <td>
                <INPUT onclick="buildPie()" type="button" value="��ͼ��ʾ">
             </td>
                <!--td>
                <INPUT onclick="buildLinear()" type="button" value="��ͼ��ʾ">
                </td-->
       </tr>
  </table>
<!-- hr noshade size="1"/>


<table>
	<tr align="left">
		<td>
		����������
		</td>
		<td>
		<select name="method" onchange="changeDefaultTitleValue( this )">
			<option value="LinearRegression">һԪ���Իع�</option>
			<option value="Correlation">���ϵ��</option>
			<option value="ReviseExponent">����ָ������</option>
			<option value="GongBoZi">����������</option>
			<option value="SimpleAverage">��ƽ��</option>
			<option value="MovingAverage">�����ƶ�ƽ��</option>
		</select>
		</td>

		<td>
		ָ��һ��
		</td>
		<td>
		<select name="scalarx">
		</select>
		</td>

		<td>
		ָ�����
		</td>
		<td>
		<select name="scalary">
		</select>
		</td>

	</tr>
	<tr>
		<td>
		���⣺
		</td>

		<td>
		<input type="text" name="title" value="һԪ���Իع�"/>
		</td>

		<td colspan="4">
		&nbsp;&nbsp;
		<input type="button" value="ͳ�Ʒ���" onclick="return submitform1()"/>
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
//��ʼ��������ʱ�䷽ʽ��ʾ���
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
                			//��ʽ��double����
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