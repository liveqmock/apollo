<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.UtilServlet" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.data.UnitTreeNode" %>

<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

UtilServlet.showCheckboxUnitTree(request, response, null,"checkboxUnitTree");
Iterator iteSelectSumUnits= (Iterator)request.getAttribute("selectSumUnitTreeNodes");

String taskTimeID= request.getParameter("taskTimeID");
%>

<html>
<head>
<title>选择汇总</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<script type="text/javascript" src="../jslib/calendar/popcalendar.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_layers.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_menu.js"></script>
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
</head>

<body>
	<table height=100% width=100%>
	<tr height=20 nowrap>
		<td>汇总单位:
			<select id="sumUnit">
		<%
			while(iteSelectSumUnits.hasNext())
			{
				UnitTreeNode unit= (UnitTreeNode)iteSelectSumUnits.next();
		%>
				<option value="<%=unit.id()%>"><%=unit.id() + " | " + unit.getUnitName()%>
		<%
			}
		%>
			</select>
			<input type="button" value="新增" onclick="createSelectSumUnit()">
		</td>
		<td>
			<form name="form1" action="model" method="post" onsubmit="return onSelectSumSubmit()">
				<input type=hidden name="operation" value="doSelectSum">
				<input type=hidden name="unitID" value="">
				<input type=hidden name="taskTimeID" value="<%=taskTimeID%>">
				<input type=submit>
			</form>
		</td>
	</tr>
	<tr>
		<td colspan=2>
			<div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
		</td>
	</tr>
	</table>
</body>

<script language="javascript">
function onSelectSumSubmit()
{
	if(sumUnit.selectedIndex  == -1)
	{
		alert("请选择汇总单位，如果没有请创建选择汇总单位。");
		return false;
	}
	form1.unitID.value= sumUnit.options[sumUnit.selectedIndex].value;

	var selectedUnits= getCheckedValues();

	//判断是否选择了单位
    if(selectedUnits == null || selectedUnits.length == 0)
    {
        alert("请选择单位");
        return false;
    }

	//产生选中的单位hidden input
    for(var i = 0;i < selectedUnits.length;i++)
    {
		var oInput =document.createElement("input");
		oInput.type = "hidden";
		oInput.name = "unitIDs";
		oInput.value = selectedUnits[i];
		form1.appendChild(oInput);
    }

	return true;
}

function createSelectSumUnit()
{
	window.open("../jsp/modelManager/addselectsumunit.jsp","addSelectSumUnitWin","status=no,width=500, height=120,top=75,left=300");
}

function appendSelectSumUnit(unitID, unitName)
{
	var oOption = document.createElement("OPTION");
	oOption.text=unitID +" | " + unitName;
	oOption.value=unitID;
	sumUnit.add(oOption);
	sumUnit.selectedIndex= sumUnit.options.length-1;
}

</script>

</html>