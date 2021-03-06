<%@ page contentType="text/html; charset=GBK" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../../csslib/main.css">
<title>增加选择汇总单位</title>
</head>

<script language="javascript">
function submitAddUnit()
{
	if(document.unitForm.unitName.value == "")
	{
		alert("请输入选择汇总单位名称。");
		document.unitForm.unitName.focus();
		return false;
	}

	if(document.unitForm.code.value == "" || document.unitForm.code.value.length < 9)
	{
		alert("单位代码长度为9位。");
		document.unitForm.code.focus();
		return false;
	}
	return true;
}
</script>


<body>
<span name="oError"/>
<form name="unitForm" action="../../servlet/model" method="post" 
	target="frmSubmit" onsubmit="return submitAddUnit();">
	<input type="hidden" name="redirectPage" value="/jsp/modelManager/addselectsumunit_ok.jsp">
	<input type="hidden" name="operation" value="<%=cn.com.youtong.apollo.servlet.ModelServlet.CREATE_UNIT%>">
	<input type="hidden" name="unitID" />
	<input type="hidden" name="parentCode" value="">
	<input type="hidden" name="HQCode" value="">
	<input type="hidden" name="reportType" value="H">
	<input type="hidden" name="p_Parent" value="">
	<table>
	<tr>
		<td>名称:</td>
		<td><input type="text" name="unitName" size=30 maxlength=30></td>
	</tr>
	<tr>
		<td>代码:</td>
		<td><input type="text" name="code" size=30 maxlength=9></td>
	</tr>
	<tr>
		<td colspan=2>
			<input TYPE="submit">
		</td>
	</tr>
	</table>
</form>

<iframe style="display:none" name="frmSubmit"></iframe>

</body>
</html>