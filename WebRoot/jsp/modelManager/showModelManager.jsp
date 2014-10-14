<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="../jslib/model/unittree.js"></script>
<title>显示数据</title>
</head>
<%
  Task task = (Task)request.getAttribute("task");
%>
<script language="JavaScript">

function changeUnit(unitID)
{
form1.unitID.value=unitID;
submitForm();
}

function submitForm()
{
  if(form1.unitID.value=="")
  {
    return;
  }
<%if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("请选择时间");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
  if(document.getElementById('timeOfTask').value==""){
    alert("请选择时间");
    return;
  }
  var flag=false;
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
       form1.taskTimeID.value=dateInfos[i][0];
       flag = true;
    }
  }
  if(!flag){
       alert(document.getElementById('timeOfTask').value+"没有数据,请重新选择一个日期!");
       document.getElementById('timeOfTask').value = "";
       return;
    }
<%}
else if (task.getTaskType() == task.REPORT_OF_QUARTER){ %>
	if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
	  {
	    alert("请选择时间");
	    return;
	  }
	  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
<%}
else if (task.getTaskType() == task.REPORT_OF_YEAR){
%>
		if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
	  {
	    alert("请选择时间");
	    return;
	  }
	  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
<%}
%>
  form1.action = "model?operation=<%=ModelServlet.SHOW_DATA%>";
  form1.submit();
}

function downExcel(type)
{
  if(form1.unitID.value=="")
  {
    alert("请选择单位");
    return;
  }
  var selectedTableID = unitData.getSelectedID();

  if(type == "single")
  {
    if(selectedTableID != "attachment")
    {
      window.open("model?operation=<%=ModelServlet.GET_EXCEL_FOR_SINGLE_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value)+"&tableID="+selectedTableID,"_self");
    }
    else
    {
      alert("请选择数据标签页");
      return;
    }
  }
  else if(type == "all")
  {
      window.open("model?operation=<%=ModelServlet.GET_EXCEL_FOR_ALL_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value),"_self");
  }
}

function printPdf(type)
{
  if(form1.unitID.value=="")
  {
    alert("请选择单位");
    return;
  }
  if(form1.taskTimeID.value=="" || form1.taskTimeID.value=="-1")
  {
    alert("请选择时间");
    return;
  }
  var selectedTableID = unitData.getSelectedID();

  if(type == "single")
  {
    if(selectedTableID != "attachment")
    {
      window.open("model?operation=<%=ModelServlet.GET_PDF_FOR_SINGLE_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value)+"&tableID="+selectedTableID,"_self");
    }
    else
    {
      alert("请选择数据标签页");
      return;
    }
  }
  else if(type = "all")
  {
    window.open("model?operation=<%=ModelServlet.GET_PDF_FOR_ALL_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value),"_self");
  }
}

function editData()
{
  if(form1.unitID.value=="")
   {
      alert("请选择单位");
      return;
   }

   <%if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("请选择时间");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
  if(document.getElementById('timeOfTask').value==""){
    alert("请选择时间");
    return;
  }
  var flag=false;
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
       form1.taskTimeID.value=dateInfos[i][0];
       flag = true;
    }
  }
  if(!flag){
       alert(document.getElementById('timeOfTask').value+"没有数据,请重新选择一个日期!");
       document.getElementById('timeOfTask').value = "";
       return;
    }
<%}%>
  form1.action = "model?operation=<%=ModelServlet.EIDT_DATA%>";
  var t = form1.target;
  form1.target = "_blank";
  form1.submit();
  form1.target = t;
}

//上报数据
function submitData()
{
	if(confirm("您确定要上报数据吗？如果上报将不能进行进行修改，除非上级部门退回重报!!!"))
	{
	}

}

</script>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

     <table class="clsContentTable">
      <tr>
           <td width="23%">
             搜索:<input type="text" name="" id="txtunitname"/><input type="button" value="查找" onclick="findunit()"/><br/>
             <div class="clsTreeDiv" id="treepanel"><%=(String)request.getAttribute("unitTree")%></div>
           </td>
           <form name="form1" id="form1" method="post" target="unitData">
           <input type="hidden" name="unitID" value="">
           <input type="hidden" name="taskTimeID" value="">
           <td height=100%>


             <table height=100% width=100% border=0>
               <tr>
                   <td>
                     任务时间:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
                  </td>
                  <td align="right">


<!--
<a href='javascript:submitData();'><img src='../img/qtsw.gif' alt='上报数据' border="0" align="absmiddle" title='上报数据'></a>
&nbsp;

<a href='javascript:editData();'><img src='../img/qtsw.gif' alt='编辑数据' border="0" align="absmiddle" title='编辑数据'></a>
&nbsp;

<a href='javascript:downExcel("single");'><img src='../img/excel.png' alt='导出Excel单张表' border="0" align="absmiddle" title='导出Excel单张表'></a>
&nbsp;
<a href='javascript:downExcel("all");'><img src='../img/excelAll.bmp' alt='导出Excel整套表' border="0" align="absmiddle" title='导出Excel整套表'></a>
&nbsp;
<a href='javascript:printPdf("single");'><img src='../img/pdf.bmp' alt='导出PDF单张表' border="0" align="absmiddle"title='导出PDF单张表'></a>
&nbsp;
<a href='javascript:printPdf("all");'><img src='../img/pdfAll.bmp' alt='导出PDF整套表' border="0" align="absmiddle"title='导出PDF整套表'></a>
&nbsp;
<a href='javascript:printPriviewOfOne();'><img src='../img/printall.bmp' alt='单表打印' border="0" align="absmiddle"title='单表打印'></a>
&nbsp;
<a href='javascript:printPriviewOfAll();'><img src='../img/printone.bmp' alt='套表打印' border="0" align="absmiddle"title='套表打印'></a>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
-->
                  </td>
              </tr>
              <tr>

                  <td colspan="2" height=100%>
                     <iframe  SCROLLING=auto height="100%" width="100%" FRAMEBORDER=1 id="unitData" name="unitData" ></iframe>
                  </td>
              </tr>
             </table>
            </td>
        </form>
      </tr>
    </table>


<script language="javascript">
//第一次显示空白表样
unitData.location= "model?operation=<%=ModelServlet.SHOW_DATA%>&flag="+"true","_self";
//单表打印
function printPriviewOfOne()
{
  if(form1.unitID.value=="")
  {
    alert("请选择单位");
    return;
  }
  var selectedTableID = unitData.getSelectedID();
  if(selectedTableID != "attachment")
  {
    window.open("model?operation=<%=ModelServlet.PRINT_ONE_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value)+"&tableID="+selectedTableID,"_blank","height=700,width=800");
  }
  else
  {
    alert("请选择数据标签页");
    return;
  }

}
//套表打印
function printPriviewOfAll()
{
  if(form1.unitID.value=="")
  {
    alert("请选择单位");
    return;
  }

  window.open("model?operation=<%=ModelServlet.PRINT_ALL_TABLE%>&taskTimeID="+form1.taskTimeID.value+"&unitID="+encodeURIComponent(form1.unitID.value),"_blank","height=700,width=800");
}
//初始时显示数据
<%
String unitID = (String)request.getAttribute("unitID");
String taskTimeID = (String)request.getAttribute("taskTimeID");
%>
if(<%= (unitID != null && taskTimeID != null) %>)
{
    form1.unitID.value = "<%= unitID %>";
    form1.taskTimeID.value = "<%= taskTimeID %>";
    submitForm();
}

</script>
  <jsp:include page= "/jsp/footer.jsp" />

</body>
</html>
