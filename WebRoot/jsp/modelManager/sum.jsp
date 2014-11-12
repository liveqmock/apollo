<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
Task task = (Task)request.getAttribute("task");
String CTX_PATH= request.getContextPath();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<title>汇总</title>
</head>

<script language= "javascript">
function getTaskTimeid()
{

	//alert(document.getElementById("taskTimeIDSelect"));
    <%if(task.getTaskType() == task.REPORT_OF_MONTH){%>
      if(document.getElementById("taskTimeIDSelect").value=="")
      {
        alert("请选择时间");
        return "";
      }
      return document.getElementById("taskTimeIDSelect").value;
    <%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
	  
      if(document.getElementById('timeOfTask').value==""){
        alert("请选择时间");
        return;
      }
      var flag=false;
      for(var i=0;i<dateInfos.length;i++){
        if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
           flag = true;
           return dateInfos[i][0];

        }
      }
      

      if(!flag){
           alert(document.getElementById('timeOfTask').value+"没有数据,请重新选择一个日期!");
           document.getElementById('timeOfTask').value = "";
           return "";
        }
    <%}%>
}

function adjustNodeDiff()
{
	if(document.all.dispinfo.style.display=="block") {alert('上一步处理还未结束');return;}
    var unitID = getCheckedValues();
	
    if(unitID == null || unitID == "")
    {
        alert("请选择单位节点");
        return false;
    }

    var node = webFXTreeHandler.all[checkedNodeID];
    var hasChild = false;

    if(checkedNodeID == "")
    {
        //通过ie的“返回”按钮回到该页面的情况
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }
	
    if(!isGroupSumUnit(unitID))
    {
        alert("该节点不是集团合并表类型（9），无法调整差额表");
        return false;
    }

    if(!hasChild)
    {
        alert("该节点没有子节点，无法调整差额表");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.ADJUST_NODE_DIFF %>";
    //alert(91);
	var taskTimeID= getTaskTimeid();
	//alert(taskTimeID);
	if(taskTimeID=="")
		return;

    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;
	document.all.infoid.innerText = "正在调整差额表...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

function sumNode()
{
	if(document.all.dispinfo.style.display=="block") {alert('上一步处理还未结束');return;}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("请选择单位节点");
        return ;
    }

    var node = webFXTreeHandler.all[checkedNodeID];

    var hasChild = false;

    if(checkedNodeID == "")
    {
        //通过ie的“返回”按钮回到该页面的情况
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }

    if(!hasChild)
    {
        alert("该节点没有子节点，无法进行节点汇总");
        return false;
    }

    if(!isGroupSumUnit(unitID) && !isSumAllUnit(unitID))
    {
        alert("该节点报表类型的值不是可汇总类型“7”或“9”，不允许进行节点汇总");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.SUM_NODE %>";
	var taskTimeID= getTaskTimeid();
	if(taskTimeID=="")
		return;
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;

	document.all.infoid.innerText = "正在汇总信息...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

function validateNodeSum()
{
	if(document.all.dispinfo.style.display=="block") {alert('上一步处理还未结束');return;}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("请选择单位节点");
        return false;
    }

    var node = webFXTreeHandler.all[checkedNodeID];

    var hasChild = false;

    if(checkedNodeID == "")
    {
        //通过ie的“返回”按钮回到该页面的情况
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }

    if(!hasChild)
    {
        alert("该节点没有子节点，无法进行节点检查");
        return false;
    }

    if(!isGroupSumUnit(unitID) && !isSumAllUnit(unitID))
    {
        alert("该节点报表类型的值不是可汇总类型“7”或“9”，不允许进行节点检查");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.VALIDATE_NODE_SUM %>";
	var taskTimeID= getTaskTimeid();
	if(taskTimeID=="")
		return;
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;

	document.all.infoid.innerText = "正在检查汇总结果...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

/**
 * 判断指定的单位ID的单位类型是否是集团汇总单位
 * @return 是集团汇总单位，返回true；否则返回false
 */
function isGroupSumUnit(unitID)
{
    var ID = "" + unitID;
    var GROUP_SUM_TYPE = "9";
    return (ID.substr(ID.length - 1) == GROUP_SUM_TYPE);
}

/**
 * 判断指定的单位ID的单位类型是否是选择汇总单位
 * @return 是选择汇总单位，返回true；否则返回false
 */
function isSelectSumUnit(unitID)
{
    var ID = "" + unitID;
    var SELECT_SUM_TYPE = "H";
    return (ID.substr(ID.length - 1) == SELECT_SUM_TYPE);
}

/**
 * 判断指定的单位ID的单位类型是否是完全汇总单位
 * @return 是完全汇总单位，返回true；否则返回false
 */
function isSumAllUnit(unitID)
{
    var ID = "" + unitID;
    var SUM_ALL_TYPE = "7";
    return (ID.substr(ID.length - 1) == SUM_ALL_TYPE);
}

//选择汇总
function selectSum()
{
	var url= "model?operation=selectSum&taskTimeID="+getTaskTimeid();
	window.open(url,"selectSumWin","status=no,height=570,width=500,top=75,left=250");
}

</script>

<script language="javascript">

//被选中的树节点ID
var checkedNodeID = "";

/**
 * 点击树节点的radio button触发此方法
 * @param nodeID 被点击的节点id
 * @param checked 是否被选中
 */
function checkRadioTreeNode(nodeID, checked)
{
    if(checked)
    {
        checkedNodeID = nodeID;
    }
}

function submitForm()
{
}

//启封数据
function unEnvlopSubmitData()
{
    if(document.all.dispinfo.style.display=="block")
	{
		 alert('上一步处理还未结束');
		 return;
	}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("请选择单位节点");
        return false;
    }

	 var retValue=new Array();
     retValue=window.showModalDialog('<%=CTX_PATH%>/jsp/modelManager/unenvlopdatatype.jsp','','dialogheight=300px;dialogwidth=400px;status=no');
	   if(retValue!=null)
		{
            form1.flag.value=retValue[0];
		    form1.operation.value = "<%= ModelServlet.UNENVELOPSUBMITDATA %>";
	    	var taskTimeID= getTaskTimeid();
		    if(taskTimeID=="")
    		return;
			form1.taskTimeID.value = getTaskTimeid();
			form1.unitID.value = unitID;
			form1.submit();
		}
}

//封存数据
function envlopSubmitData()
{
	if(document.all.dispinfo.style.display=="block")
	{
		 alert('上一步处理还未结束');
		 return;
	}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("请选择单位节点");
        return false;
    }
    form1.operation.value = "<%= ModelServlet.ENVELOPSUBMITDATA %>";
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;
    form1.submit();

}

</script>

<body >
	<div id="dispinfo"  style="filter:alpha(opacity=80);z-index:10px;border:2;BACKGROUND-COLOR:#7d7baa;position:absolute;left:expression((body.clientWidth-300)/2);top:expression((body.clientHeight-100)/2);width:300px;height:20px;display:none">
		<TABLE WIDTH=100% height=70 BORDER=0 CELLSPACING=2 CELLPADDING=0><TR><td bgcolor=#eeeeee align=center id = infoid></td></tr></table></td><td width=30%></td></tr></table>
	</div>
	<jsp:include page= "/jsp/logo.jsp" />
	<jsp:include page= "/jsp/navigation.jsp" />
	<form name="form1" action="model" method="post">
	<input type="hidden" name="operation"/>
	<input type="hidden" name="unitID"/>
	<input type="hidden" name="taskTimeID"/>
    <input type="hidden" name="flag"/>
	<table class="clsContentTable" >
		<tr class="clsTrContext">
			<td align=right width=60%>
				<table height=100% width=350>
					<tr align=left>
						<td align=left>
								任务时间:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
								<input type="checkbox" name="isRecursive" value="true"/>包括所有下级单位
						</td>
					</tr>
					<tr height=100% align=left>
					   <td class="TdLight">
						  <div class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
					   </td>
				   </tr>
				</table>
			</td>
			<td align=left valign=top width=40%>
				<br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" 节点汇总 " onclick="sumNode()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value="调整差额表" onclick="adjustNodeDiff()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" 节点检查 " onclick="validateNodeSum()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" 选择汇总 " onclick="selectSum()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<!--input type="button" value=" 数据封存 " onclick="envlopSubmitData()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;-->
				<input type="button" value=" 数据启封 " onclick="unEnvlopSubmitData()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;


			</td>
		</tr>
	</table>
	</form>
	<jsp:include page= "/jsp/footer.jsp" />
</body>
</html>