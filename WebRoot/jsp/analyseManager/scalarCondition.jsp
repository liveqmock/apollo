<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<link type="text/css" rel="stylesheet" href="../csslib/webfxlayout.css" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
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
<title>指标查询条件</title>
</head>
<%
    String operation = request.getParameter("operation");

    Task task = (Task)request.getAttribute("task");
    Iterator tableViewerItr = (Iterator)request.getAttribute("tableViewerItr");
    ScalarQueryForm condition = (ScalarQueryForm)request.getAttribute("condition");
    //已选择的指标
    Collection scalarsSelected = new LinkedList();
    ScalarForm [] scalars = condition.getScalars();
    if(scalars != null)
    {
        for(int i = 0; i < scalars.length; i++)
        {
            scalarsSelected.add(scalars[i]);
        }
    }
    //已选择的任务时间
    Collection taskTimeSelected = new LinkedList();
    Integer [] taskTimeIDs = condition.getTaskTimeIDs();
    if(taskTimeIDs != null)
    {
        for(int i = 0; i < taskTimeIDs.length; i++)
        {
            taskTimeSelected.add(taskTimeIDs[i]);
        }
    }

%>
<script language= "javascript">

var selectedFlag = "☆";

//当前所有被选择的单位ID集合
var unitIDselected = new Array();
<%
    String [] unitIDs = condition.getUnitIDs();
    if(unitIDs != null)
    {
        for(int i = 0; i < unitIDs.length; i++)
        {
%>
unitIDselected[<%= i %>] = "<%= unitIDs[i] %>";
<%
        }
    }
%>

/**
 * 验证指标查询条件的有效性
 * @return 有效，返回true；否则，返回false
 */
function validCondition()
{

    //创建日报任务时间元素
    createTaskTimeOfDaily();
    //判断是否选择了单位
    if(unitIDselected == null || unitIDselected.length == 0)
    {
        alert("请选择单位");
        unitTabPage.select();
        return false;
    }

    //判断是否选择了指标
    if(!hasSelectedScalar() && !hasSelectedItem(document.getElementsByName("defineScalars")))
    {
        alert("请选择指标");
        scalarTabPage.select();
        return false;
    }

    //判断是否选择了任务时间
    if(typeof(taskTimeTabPage) != "undefined" && !hasSelectedItem(document.getElementsByName("taskTimeIDs")))
    {
        alert("请选择任务时间");
        taskTimeTabPage.select();
        return false;
    }

    //产生选中的单位hidden input
    createUnitIDHidden(unitIDselected);

    //产生选中的指标hidden input
    createScalarHidden();

    return true;
}

/**
 * 判断指标是否被选中
 * @param oTd 代表指标的<td>对象
 * @return 指标被选中，返回true；否则返回false
 */
function isScalarSelected(oTd)
{
    return (oTd.expression != null && oTd.innerText == selectedFlag);
}

/**
 * 是否从表中选择了指标
 * @return 从表中选择了指标，返回true；否则返回false
 */
function hasSelectedScalar()
{
    var oTds = document.getElementsByTagName("td");
    for(var i = 0; i < oTds.length; i++)
    {
        if(isScalarSelected(oTds[i]))
        {
            return true;
        }
    }

    return false;
}

function queryScalar()
{
    if(validCondition())
    {
        //提交请求
        form1.action = "analyse?operation=<%= AnalyseServlet.QUERY_SCALAR %>";
        form1.submit();
    }
}

function createScalarQueryTemplate()
{
    if(validTemplateName() && validCondition())
    {
        form1.action = "analyse?operation=<%= AnalyseServlet.CREATE_SCALAR_QUERY_TEMPLATE %>";
        form1.submit();
    }
}

function updateScalarQueryTemplate()
{
    if(validTemplateName() && validCondition())
    {
        form1.action = "analyse?operation=<%= AnalyseServlet.UPDATE_SCALAR_QUERY_TEMPLATE %>";
        form1.submit();
    }
}

function validTemplateName()
{
    form1.name.value = trim(form1.name.value);
    if(form1.name.value == "")
    {
        alert("请输入模板名称");
        return false;
    }
    return true;
}

function createUnitIDHidden(unitIDs)
{
    var oInput;
    for(var i = 0;i < unitIDs.length;i++)
    {
        if(unitIDs[i] != null)
        {
            oInput =document.createElement("input");
            oInput.type = "hidden";
            oInput.name = "unitIDs";
            oInput.value = unitIDs[i];
            document.form1.appendChild(oInput);
        }
    }
}

function createScalarHidden()
{
    var oInput;
    var oTds = document.getElementsByTagName("td");
    for(var i = 0; i < oTds.length; i++)
    {
        if(isScalarSelected(oTds[i]))
        {
            //scalarName
            oInput =document.createElement("input");
            oInput.type = "hidden";
            oInput.name = "scalarNames";
            oInput.value = oTds[i].scalarName;
            document.form1.appendChild(oInput);
            //expressions
            oInput =document.createElement("input");
            oInput.type = "hidden";
            oInput.name = "expressions";
            oInput.value = oTds[i].expression;
            document.form1.appendChild(oInput);
        }
    }
}

/**
 * 显示表的指标时，预选中指定的指标
 */
function preSelectScalar()
{
<%
for(Iterator itr = scalarsSelected.iterator(); itr.hasNext();)
{
    ScalarForm scalar = (ScalarForm)itr.next();
%>
    var selected = false;
    //在指标表中找匹配的单元格
    var oTds = document.getElementsByTagName("td");
    for(var i = 0; i < oTds.length; i++)
    {
        if(oTds[i].expression == "<%= scalar.getExpression() %>" && oTds[i].scalarName == "<%= scalar.getName() %>")
        {
            selectScalar(oTds[i]);
            selected = true;
        }
    }
    //如果没有找到匹配的单元格，则为自定义指标
    if(!selected)
    {
        insertDefineScalar("<%= scalar.getName() %>", "<%= scalar.getExpression() %>");
    }
<%
}
%>
}

function pressAKey()
{
	//按了回车键
	if(window.event.keyCode == 13)
	{
		createDefineScalar();
	}
}

/**
 * 创建自定义指标
 */
function createDefineScalar()
{
	var name = document.getElementById("definde_name");
	var expression = document.getElementById("definde_expression");

	if(!validateInput(name, expression))
	{
		return false;
	}

    insertDefineScalar(name.value, expression.value);

	//清空输入框
	name.value = "";
	expression.value = "";
	name.focus();
}

/**
 * 验证自定义指标是否正确
 * param name 指标名称输入框对象
 * param expression 指标表达式输入框对象
 * return 正确，返回true；否则返回false
 */
function validateInput(name, expression)
{
    name.value = trim(name.value);
    expression.value = trim(expression.value);

	if(name.value == "")
	{
		alert("请输入指标名称");
		name.focus();
		return false;
	}

	if(expression.value == "")
	{
		alert("请输入指标表达式");
		expression.focus();
		return false;
	}

	return true;
}

function selectScalar(oTd)
{
	if(oTd.expression != null)
	{
		if(oTd.innerText == selectedFlag)
		{
			oTd.innerText = "　";
		}
		else
		{
			oTd.innerHTML = "<font color='red'>" + selectedFlag + "</font>";
		}
	}
}

/**
 * 在自定义指标表中加入一条记录
 */
function insertDefineScalar(name, expression)
{
    var checkBoxHTML = "<input type='checkbox' checked name='defineScalars' value='" + (name + "," + expression) +"'/>";

	var oTable = document.getElementById("table_defineScalar");
	var oRow = oTable.insertRow();
        oRow.style.border = "black 1pt solid";
	var oCell = oRow.insertCell();
        oCell.style.border = "black 1pt solid";
	oCell.innerHTML = checkBoxHTML;
	oCell = oRow.insertCell();
	oCell.innerText = name;
	oCell = oRow.insertCell();
	oCell.innerText = expression;
}
  
  function changeUnit(unitID,unitName){
  }
  
</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

<script language="javascript">
/**
 * 点击树节点的check box触发此方法
 */
function checkTreeNode(nodeID,checked)
{
  setCheckedTreeNode(nodeID,checked,document.all.isContainChildren.checked);
}

/**
 * check操作完成后的回调函数，覆盖缺省的实现
 * 更新数组unitIDselected
 */
function afterCheck()
{
    //保存当前的unitIDselected
    var oldUnitIDs = new Array();
    for(var i = 0; i < unitIDselected.length; i++)
    {
        if(unitIDselected[i] != null)
        {
            oldUnitIDs[oldUnitIDs.length] = unitIDselected[i];
        }
    }
    //状态改变的单位节点
    var nodes = checkProperty.nodes;
    //开始更新unitIDselected
    for(var i = 0; i < nodes.length; i++)
    {
        //状态改变的单位节点的unitID在数组oldUnitIDs中的index
        var index = null;

        for(var j = 0; j < oldUnitIDs.length; j++)
        {
            if(nodes[i]._checkValue == oldUnitIDs[j])
            {
                index = j;
                break;
            }
        }

        if(index == null)
        {
            if(nodes[i]._checked)
            {
                //将单位ID加到unitIDselected中
                unitIDselected[unitIDselected.length] = nodes[i]._checkValue;
            }
        }
        else
        {
            if(!nodes[i]._checked)
            {
                //从unitIDselected中去掉对应的单位ID
                for(k = 0; k < unitIDselected.length; k++)
                {
                    if(unitIDselected[k] == nodes[i]._checkValue)
                    {
                        unitIDselected[k] = null;
                    }
                }
            }
        }
    }
}

</script>
<form name="form1" method="post">
<div class="tab-pane" id="conditionPane">

<script type="text/javascript">
    var conditionPane = new WebFXTabPane( document.getElementById( "conditionPane" ),false);
</script>

   <div class="tab-page"  id="unit-tab-page">
      <h2 class="tab">单位&nbsp;</h2>
      <script type="text/javascript">
           var unitTabPage = conditionPane.addTabPage( document.getElementById( "unit-tab-page" ) );
      </script>
      <table height="100%" width="100%">
            <tr>
                <td class="TdLight" width=20% height=100%>
                    <div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
                </td>
            </tr>
            <tr><td><input type="checkbox" name="isContainChildren" checked>是否包含下级</td></tr>
      </table>

   </div>

   <div class="tab-page" id="scalar-tab-page">
      <h2 class="tab">指标&nbsp;</h2>
      <script type="text/javascript">
      var scalarTabPage = conditionPane.addTabPage( document.getElementById( "scalar-tab-page" ) );
      </script>

		<div class="tab-pane" id="scalarPane" onclick="selectScalar(window.event.srcElement)">

<script type="text/javascript">
var scalarPane = new WebFXTabPane( document.getElementById( "scalarPane" ),false);
</script>
<%
//显示每一个表
while(tableViewerItr.hasNext())
{
    TableViewer tableViewer = (TableViewer)tableViewerItr.next();
%>
			<div class="tab-page" id="table_<%= tableViewer.getTableID() %>">
				<h2 class="tab"><%= tableViewer.getTableName() %>&nbsp;</h2>

      <script type="text/javascript">
      scalarPane.addTabPage( document.getElementById( "table_<%= tableViewer.getTableID() %>" ) );
      </script>

<%= tableViewer.getHtmlString() %>

			</div>
<%
}
%>

			<div class="tab-page" id="page_defineScalar">
				<h2 class="tab">自定义指标&nbsp;</h2>

      <script type="text/javascript">
      scalarPane.addTabPage( document.getElementById( "page_defineScalar" ) );
      </script>

<table id = "table_defineScalar" border=1 style="border-collapse:collapse">
<tr>
<td>&nbsp;</td>
<td>指标名称</td>
<td>指标表达式</td>
</tr>
</table>

<br>
<table border=0>
<tr>
    <td>&nbsp;名称:</td>
    <td valign="top"><input style="HEIGHT: 22px; WIDTH: 180px " id="definde_name"

    onkeypress="pressAKey()"/></td>
</tr>
<tr>
    <td valign=top>&nbsp;表达式:&nbsp; </td>
    <td><textarea id="definde_expression" rows="7" style="width:450" cols="30"></textarea></td>
</tr>
<tr>
     <td align="left" colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <input type="button" value="添加"

     onclick="createDefineScalar()"></td>
</tr>
</table>

		</div>
     </div>
   </div>

<script language="javascript">
preSelectScalar();
</script>
<%
//显示任务时间
if(!operation.equals(AnalyseServlet.SHOW_CREATE_SCALAR_QUERY_TEMPLATE_PAGE) &&
   !operation.equals(AnalyseServlet.SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE))
{
%>

   <div class="tab-page" id="taskTime-tab-page">
      <h2 class="tab">任务时间&nbsp;</h2>
      <script type="text/javascript">
      var taskTimeTabPage = conditionPane.addTabPage( document.getElementById( "taskTime-tab-page" ) );
      </script>
<!--月报：日期选择－－－－－－－－－－－－－－-->
<%
    if(task.getTaskType() == task.REPORT_OF_MONTH){
%>
    <table border=0 cellSpacing=0 cellPadding=8>
    <tr>
          <%
          int i = 0;
          for(Iterator itr = task.getTaskTimes(); itr.hasNext();)
          {
              TaskTime taskTime = (TaskTime)itr.next();
              i++;
              if(taskTimeSelected.contains(taskTime.getTaskTimeID()))
              {
          %>
          <td><input type="checkbox" checked name="taskTimeIDs" value="<%= taskTime.getTaskTimeID() %>"/><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></td>
          <%
              }else{
          %>
          <td><input type="checkbox" name="taskTimeIDs" value="<%= taskTime.getTaskTimeID() %>"/><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></td>
          <%}if(i%6==0){%>
             </tr><tr>
          <%}}%>
    </tr>
    </table>
<!--日报：日期选择－－－－－－－－－－－－－－-->
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
<br>
<font style="background-color : AliceBlue;font-size : 9pt">
&nbsp;<img src="../img/infomation.bmp">&nbsp;点击[↓]按钮在弹出的日历中选择所需的日期,然后再点击[添加按钮]添加此任务日期.
</font>
<br><br>
    <table>
        <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;时间选择：&nbsp;&nbsp;&nbsp;</td>
          <td>&nbsp;</td>
          <td>
            <input name="oneTime" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.oneTime, 'yyyy-mm-dd');" value="↓"/>
          </td>
          <td>&nbsp;<input type="button" onClick="addDate()" value="添加"/></td>
          <td>

          </td>
          <td>

          </td>
        </tr>
        <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;任务时间段选择：&nbsp;&nbsp;&nbsp;</td>
          <td>从</td>
          <td>
            <input name="start" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.start, 'yyyy-mm-dd');" value="↓"/>
          </td>
          <td>...&nbsp;&nbsp;到</td>
          <td>
             <input name="end" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.end, 'yyyy-mm-dd');" value="↓"/>
          </td>
          <td>
            &nbsp;<input type="button" onClick="addDatePeriod()" value="添加"/>
          </td>
          <td>
             &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" onClick="clearTableOfTime()" value="清空已选择时间列表"/>
          </td>
         </tr>
    </table>
<hr size=1>
<center style="background-color : aliceblue">已选择的任务时间列表</center>
    <table align=center id="oTable" cellspacing="0" border="1" cellpadding="6">
         <THead id="oTHead0"></THead>
         <TBODY id="oTBody0"></TBODY>
    </table>
<div id="dataOfForm" style="visibility:hidden"></div>
<%
}}
%>


</div>
     </td>
  </tr>
  <tr class="TrDark">
    <td colspan="2" align="middle">
<%
if(operation.equals(AnalyseServlet.SHOW_CREATE_SCALAR_QUERY_TEMPLATE_PAGE))
{
%>
<input name="name" value="" >
<input type="button" value="创建模板" onclick="createScalarQueryTemplate()">
<%
}
else if(operation.equals(AnalyseServlet.SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE))
{
    ScalarQueryTemplate template = (ScalarQueryTemplate)request.getAttribute("template");
%>
<input type="hidden" name="templateID" value="<%= template.getTemplateID() %>">
<input name="name" value="<%= template.getName() %>">
<input type="button" value="更新模板" onclick="updateScalarQueryTemplate()">
<%
}
else
{
%>
<input type="button" value="查询" onclick="queryScalar()">
<%
}
%>

</form>

<script language="javascript">
//设置首先显示的tab
<%
//如果是执行模板，首先显示任务时间选择Tab
if(operation.equals(AnalyseServlet.EXECUTE_SCALAR_QUERY_TEMPLATE))
{
%>
    taskTimeTabPage.select();
<%
}
else
{
%>
    unitTabPage.select();
<%
}
%>
//日报－>任务日期脚本
//任务日期数组
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
//表格换行标识
var newTableflag = 0;
var newRowflag = 0;
//创建表格
   var oRow, oCell;
  function createTable(dateText){

  if(newTableflag==0){
     oRow = oTHead0.insertRow();
     newTableflag=1;
    }
    if(newRowflag==0){
     oRow = oTBody0.insertRow();
    }
  oCell = oRow.insertCell();
  oCell.innerText = dateText;
  newRowflag++;
  if(newRowflag==10){
    newRowflag=0;
  }
 }
//判断日期是否在数据库里
function judgeDate(date){
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==date){
      return true;
    }
  }
  return false;
}
//判断日期是不是已经添加到表格里面
 function judgeDateInTable(date){
 oTbl = document.getElementById("oTable");
 oCells = oTbl.cells;
  for(var j=0;j<oCells.length;j++){
   if(oCells[j].innerText==date){
      return true;
   }
  }
  return false;
 }
//添加一个日期
function addDate(){
  if(form1.oneTime.value==""){
    alert("请选择日期!");
    return;
  }
  if(!judgeDate(form1.oneTime.value)){
    alert("所选日期没有数据，请重新选择!");
    return;
  }
  if(judgeDateInTable(form1.oneTime.value)){
    alert("您已经添加了此日期，请重新选择!");
    return;
  }
  createTable(form1.oneTime.value);
}
//添加时间段
function addDatePeriod(){
  if(form1.start.value==""){
    alert("请选择开始日期!");
    return;
  }
  if(form1.end.value==""){
    alert("请选择结束日期!");
    return;
  }
    var startTime = isDate(form1.start.value);
    var endTime = isDate(form1.end.value);

    if(startTime && endTime)
    {
        if(compareDate(startTime, endTime) > 0)
        {
            alert("开始时间必须在结束时间之前");
            return false;
        }
    }
    flag = false;
    for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==form1.start.value){
       flag=true;
    }
    if(flag){
        if(!judgeDateInTable(dateInfos[i][1])){
            createTable(dateInfos[i][1]);
        }

    }
    if(dateInfos[i][1]==form1.end.value){
      return;
    }
  }
}
//根据日期文本找到id
function lookForTaskTimeId(date){
    for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==date){
       return dateInfos[i][0];
    }
  }
}
//创建日报任务时间
function createTaskTimeOfDaily(){
<%if(task.getTaskType() != task.REPORT_OF_DAY){%>
  return;
<%}%>
   oTbl = document.getElementById("oTable");
   oCells = oTbl.cells;
   var taskTimeid;
   for(var i=0;i<oCells.length;i++){
       for(var j=0;j<dateInfos.length;j++){
          if(dateInfos[j][1]==oCells[i].innerText){
          taskTimeid = dateInfos[j][0];
        }
      }
     createFormData(taskTimeid);
   }

}
function createFormData(value){

            oInput =document.createElement("<input name='taskTimeIDs' checked/>");
            oInput.type = "checkbox";
            oInput.value = value;


oDiv = document.getElementById( "dataOfForm" );
oDiv.appendChild(oInput);

}
function clearTableOfTime(){
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
  newTableflag=0;
}

unitIDselected=getCheckedValues();
</script>

  <jsp:include page= "/jsp/footer.jsp" />

</body>
</html>

