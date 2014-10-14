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
<title>ָ���ѯ����</title>
</head>
<%
    String operation = request.getParameter("operation");

    Task task = (Task)request.getAttribute("task");
    Iterator tableViewerItr = (Iterator)request.getAttribute("tableViewerItr");
    ScalarQueryForm condition = (ScalarQueryForm)request.getAttribute("condition");
    //��ѡ���ָ��
    Collection scalarsSelected = new LinkedList();
    ScalarForm [] scalars = condition.getScalars();
    if(scalars != null)
    {
        for(int i = 0; i < scalars.length; i++)
        {
            scalarsSelected.add(scalars[i]);
        }
    }
    //��ѡ�������ʱ��
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

var selectedFlag = "��";

//��ǰ���б�ѡ��ĵ�λID����
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
 * ��ָ֤���ѯ��������Ч��
 * @return ��Ч������true�����򣬷���false
 */
function validCondition()
{

    //�����ձ�����ʱ��Ԫ��
    createTaskTimeOfDaily();
    //�ж��Ƿ�ѡ���˵�λ
    if(unitIDselected == null || unitIDselected.length == 0)
    {
        alert("��ѡ��λ");
        unitTabPage.select();
        return false;
    }

    //�ж��Ƿ�ѡ����ָ��
    if(!hasSelectedScalar() && !hasSelectedItem(document.getElementsByName("defineScalars")))
    {
        alert("��ѡ��ָ��");
        scalarTabPage.select();
        return false;
    }

    //�ж��Ƿ�ѡ��������ʱ��
    if(typeof(taskTimeTabPage) != "undefined" && !hasSelectedItem(document.getElementsByName("taskTimeIDs")))
    {
        alert("��ѡ������ʱ��");
        taskTimeTabPage.select();
        return false;
    }

    //����ѡ�еĵ�λhidden input
    createUnitIDHidden(unitIDselected);

    //����ѡ�е�ָ��hidden input
    createScalarHidden();

    return true;
}

/**
 * �ж�ָ���Ƿ�ѡ��
 * @param oTd ����ָ���<td>����
 * @return ָ�걻ѡ�У�����true�����򷵻�false
 */
function isScalarSelected(oTd)
{
    return (oTd.expression != null && oTd.innerText == selectedFlag);
}

/**
 * �Ƿ�ӱ���ѡ����ָ��
 * @return �ӱ���ѡ����ָ�꣬����true�����򷵻�false
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
        //�ύ����
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
        alert("������ģ������");
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
 * ��ʾ���ָ��ʱ��Ԥѡ��ָ����ָ��
 */
function preSelectScalar()
{
<%
for(Iterator itr = scalarsSelected.iterator(); itr.hasNext();)
{
    ScalarForm scalar = (ScalarForm)itr.next();
%>
    var selected = false;
    //��ָ�������ƥ��ĵ�Ԫ��
    var oTds = document.getElementsByTagName("td");
    for(var i = 0; i < oTds.length; i++)
    {
        if(oTds[i].expression == "<%= scalar.getExpression() %>" && oTds[i].scalarName == "<%= scalar.getName() %>")
        {
            selectScalar(oTds[i]);
            selected = true;
        }
    }
    //���û���ҵ�ƥ��ĵ�Ԫ����Ϊ�Զ���ָ��
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
	//���˻س���
	if(window.event.keyCode == 13)
	{
		createDefineScalar();
	}
}

/**
 * �����Զ���ָ��
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

	//��������
	name.value = "";
	expression.value = "";
	name.focus();
}

/**
 * ��֤�Զ���ָ���Ƿ���ȷ
 * param name ָ��������������
 * param expression ָ����ʽ��������
 * return ��ȷ������true�����򷵻�false
 */
function validateInput(name, expression)
{
    name.value = trim(name.value);
    expression.value = trim(expression.value);

	if(name.value == "")
	{
		alert("������ָ������");
		name.focus();
		return false;
	}

	if(expression.value == "")
	{
		alert("������ָ����ʽ");
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
			oTd.innerText = "��";
		}
		else
		{
			oTd.innerHTML = "<font color='red'>" + selectedFlag + "</font>";
		}
	}
}

/**
 * ���Զ���ָ����м���һ����¼
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
 * ������ڵ��check box�����˷���
 */
function checkTreeNode(nodeID,checked)
{
  setCheckedTreeNode(nodeID,checked,document.all.isContainChildren.checked);
}

/**
 * check������ɺ�Ļص�����������ȱʡ��ʵ��
 * ��������unitIDselected
 */
function afterCheck()
{
    //���浱ǰ��unitIDselected
    var oldUnitIDs = new Array();
    for(var i = 0; i < unitIDselected.length; i++)
    {
        if(unitIDselected[i] != null)
        {
            oldUnitIDs[oldUnitIDs.length] = unitIDselected[i];
        }
    }
    //״̬�ı�ĵ�λ�ڵ�
    var nodes = checkProperty.nodes;
    //��ʼ����unitIDselected
    for(var i = 0; i < nodes.length; i++)
    {
        //״̬�ı�ĵ�λ�ڵ��unitID������oldUnitIDs�е�index
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
                //����λID�ӵ�unitIDselected��
                unitIDselected[unitIDselected.length] = nodes[i]._checkValue;
            }
        }
        else
        {
            if(!nodes[i]._checked)
            {
                //��unitIDselected��ȥ����Ӧ�ĵ�λID
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
      <h2 class="tab">��λ&nbsp;</h2>
      <script type="text/javascript">
           var unitTabPage = conditionPane.addTabPage( document.getElementById( "unit-tab-page" ) );
      </script>
      <table height="100%" width="100%">
            <tr>
                <td class="TdLight" width=20% height=100%>
                    <div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
                </td>
            </tr>
            <tr><td><input type="checkbox" name="isContainChildren" checked>�Ƿ�����¼�</td></tr>
      </table>

   </div>

   <div class="tab-page" id="scalar-tab-page">
      <h2 class="tab">ָ��&nbsp;</h2>
      <script type="text/javascript">
      var scalarTabPage = conditionPane.addTabPage( document.getElementById( "scalar-tab-page" ) );
      </script>

		<div class="tab-pane" id="scalarPane" onclick="selectScalar(window.event.srcElement)">

<script type="text/javascript">
var scalarPane = new WebFXTabPane( document.getElementById( "scalarPane" ),false);
</script>
<%
//��ʾÿһ����
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
				<h2 class="tab">�Զ���ָ��&nbsp;</h2>

      <script type="text/javascript">
      scalarPane.addTabPage( document.getElementById( "page_defineScalar" ) );
      </script>

<table id = "table_defineScalar" border=1 style="border-collapse:collapse">
<tr>
<td>&nbsp;</td>
<td>ָ������</td>
<td>ָ����ʽ</td>
</tr>
</table>

<br>
<table border=0>
<tr>
    <td>&nbsp;����:</td>
    <td valign="top"><input style="HEIGHT: 22px; WIDTH: 180px " id="definde_name"

    onkeypress="pressAKey()"/></td>
</tr>
<tr>
    <td valign=top>&nbsp;���ʽ:&nbsp; </td>
    <td><textarea id="definde_expression" rows="7" style="width:450" cols="30"></textarea></td>
</tr>
<tr>
     <td align="left" colspan=2>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     <input type="button" value="���"

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
//��ʾ����ʱ��
if(!operation.equals(AnalyseServlet.SHOW_CREATE_SCALAR_QUERY_TEMPLATE_PAGE) &&
   !operation.equals(AnalyseServlet.SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE))
{
%>

   <div class="tab-page" id="taskTime-tab-page">
      <h2 class="tab">����ʱ��&nbsp;</h2>
      <script type="text/javascript">
      var taskTimeTabPage = conditionPane.addTabPage( document.getElementById( "taskTime-tab-page" ) );
      </script>
<!--�±�������ѡ�񣭣�������������������������-->
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
<!--�ձ�������ѡ�񣭣�������������������������-->
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
<br>
<font style="background-color : AliceBlue;font-size : 9pt">
&nbsp;<img src="../img/infomation.bmp">&nbsp;���[��]��ť�ڵ�����������ѡ�����������,Ȼ���ٵ��[��Ӱ�ť]��Ӵ���������.
</font>
<br><br>
    <table>
        <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;ʱ��ѡ��&nbsp;&nbsp;&nbsp;</td>
          <td>&nbsp;</td>
          <td>
            <input name="oneTime" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.oneTime, 'yyyy-mm-dd');" value="��"/>
          </td>
          <td>&nbsp;<input type="button" onClick="addDate()" value="���"/></td>
          <td>

          </td>
          <td>

          </td>
        </tr>
        <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;����ʱ���ѡ��&nbsp;&nbsp;&nbsp;</td>
          <td>��</td>
          <td>
            <input name="start" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.start, 'yyyy-mm-dd');" value="��"/>
          </td>
          <td>...&nbsp;&nbsp;��</td>
          <td>
             <input name="end" readonly value="" size=12/>
            <input type="button" onClick="popUpCalendar(this, form1.end, 'yyyy-mm-dd');" value="��"/>
          </td>
          <td>
            &nbsp;<input type="button" onClick="addDatePeriod()" value="���"/>
          </td>
          <td>
             &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" onClick="clearTableOfTime()" value="�����ѡ��ʱ���б�"/>
          </td>
         </tr>
    </table>
<hr size=1>
<center style="background-color : aliceblue">��ѡ�������ʱ���б�</center>
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
<input type="button" value="����ģ��" onclick="createScalarQueryTemplate()">
<%
}
else if(operation.equals(AnalyseServlet.SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE))
{
    ScalarQueryTemplate template = (ScalarQueryTemplate)request.getAttribute("template");
%>
<input type="hidden" name="templateID" value="<%= template.getTemplateID() %>">
<input name="name" value="<%= template.getName() %>">
<input type="button" value="����ģ��" onclick="updateScalarQueryTemplate()">
<%
}
else
{
%>
<input type="button" value="��ѯ" onclick="queryScalar()">
<%
}
%>

</form>

<script language="javascript">
//����������ʾ��tab
<%
//�����ִ��ģ�壬������ʾ����ʱ��ѡ��Tab
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
//�ձ���>�������ڽű�
//������������
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
//����б�ʶ
var newTableflag = 0;
var newRowflag = 0;
//�������
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
//�ж������Ƿ������ݿ���
function judgeDate(date){
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==date){
      return true;
    }
  }
  return false;
}
//�ж������ǲ����Ѿ���ӵ��������
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
//���һ������
function addDate(){
  if(form1.oneTime.value==""){
    alert("��ѡ������!");
    return;
  }
  if(!judgeDate(form1.oneTime.value)){
    alert("��ѡ����û�����ݣ�������ѡ��!");
    return;
  }
  if(judgeDateInTable(form1.oneTime.value)){
    alert("���Ѿ�����˴����ڣ�������ѡ��!");
    return;
  }
  createTable(form1.oneTime.value);
}
//���ʱ���
function addDatePeriod(){
  if(form1.start.value==""){
    alert("��ѡ��ʼ����!");
    return;
  }
  if(form1.end.value==""){
    alert("��ѡ���������!");
    return;
  }
    var startTime = isDate(form1.start.value);
    var endTime = isDate(form1.end.value);

    if(startTime && endTime)
    {
        if(compareDate(startTime, endTime) > 0)
        {
            alert("��ʼʱ������ڽ���ʱ��֮ǰ");
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
//���������ı��ҵ�id
function lookForTaskTimeId(date){
    for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==date){
       return dateInfos[i][0];
    }
  }
}
//�����ձ�����ʱ��
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

