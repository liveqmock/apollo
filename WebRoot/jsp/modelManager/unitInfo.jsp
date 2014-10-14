<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script type="text/javascript" src="../jslib/function.js"></script>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<style type="text/css">
.xsltTr
{
    BORDER-RIGHT: black 1pt solid;
    PADDING-RIGHT: 1pt;
    BORDER-TOP: black 1pt solid;
    PADDING-LEFT: 1pt;
    PADDING-BOTTOM: 1pt;
    BORDER-LEFT: black 1pt solid;
    PADDING-TOP: 1pt;
    BORDER-BOTTOM: black 1pt solid;
    BACKGROUND-COLOR: #fbfbfb
}
</style>
</head>
<script language="JavaScript">
/**
 * 验证单位信息输入的正确性
 * @return 验证通过，返回true；否则返回false
 */
function validate(){
    unitForm.unitName.value = trim(unitForm.unitName.value);
    if(unitForm.unitName.value == "")
    {
        alert("请填写单位名称");
        unitForm.unitName.focus();
        return false;
    }

    unitForm.code.value = trim(unitForm.code.value);
    if(unitForm.code.value == "")
    {
        alert("请填写单位代码");
        unitForm.code.focus();
        return false;
    }

    unitForm.parentCode.value = trim(unitForm.parentCode.value);

    unitForm.HQCode.value = trim(unitForm.HQCode.value);

    unitForm.reportType.value = trim(unitForm.reportType.value);
    if(unitForm.reportType.value == "")
    {
        alert("请选择报表类型");
        unitForm.reportType.focus();
        return false;
    }

    unitForm.p_Parent.value = trim(unitForm.p_Parent.value);

    return true;
}

/**
 * 显示新增单位界面
 */
function showCreateUnitUI()
{
    unitForm.unitID.value="";
    unitForm.p_Parent.value="";
    unitForm.unitName.value="";
    unitForm.code.value="";
    unitForm.parentCode.value="";
    unitForm.HQCode.value="";

    createLink.style.visibility = "hidden";
    create.style.visibility = "visible";
    updateAndDelete.style.visibility = "hidden";
}

function createUnit()
{
    if(!validate())
    {
        return false;
    }
    window.parent.name = "manageUnit";
    unitForm.target = "manageUnit";
    unitForm.operation.value = "<%=cn.com.youtong.apollo.servlet.ModelServlet.CREATE_UNIT%>";

    unitForm.submit();
}

function updateUnit()
{
    if(!validate())
    {
        return false;
    }
    window.parent.name = "manageUnit";
    unitForm.target = "manageUnit";
    unitForm.operation.value = "<%=cn.com.youtong.apollo.servlet.ModelServlet.UPDATE_UNIT%>";

    unitForm.submit();
}

function deleteUnit()
{
    window.parent.name = "manageUnit";
    unitForm.target = "manageUnit";
    unitForm.operation.value = "<%=cn.com.youtong.apollo.servlet.ModelServlet.DELETE_UNIT%>";

    if(confirm("是否真的要删除当前单位？"))
    {
        unitForm.submit();
    }
}

</script>

<body>
<form name="unitForm" action="model" method="post">
<input type="hidden" name="operation" />
<input type="hidden" name="unitID" />
           <table height=90% width=90% border=1 style="margin-left:1cm" class="xsltTable">
               <tr class="xsltTr">
                   <td colspan=2 align="left">
                       <div id="createLink"><a href="javascript:showCreateUnitUI()" style="TEXT-DECORATION: underline">新增单位</a></div>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center" width="25%">名称:</td>
                   <td><input type="text" name="unitName" size=30 maxlength=20></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">代码:</td>
                   <td><input type="text" name="code" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">上级代码:</td>
                   <td><input type="text" name="parentCode" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">集团代码:</td>
                   <td><input type="text" name="HQCode" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">报表类型:</td>
                   <td>
                       <select name="reportType">
                          <option value="0">0 单户报表</option>
                          <option value="1">1 集团差额表</option>
                          <option value="2">2 金融并企业表</option>
                          <option value="3">3 境外并企业表</option>
                          <option value="4">4 事业并企业表</option>
                          <option value="5">5 基建并企业表</option>
                          <option value="7">7 完全汇总表</option>
                          <option value="9">9 集团合并表</option>
                          <option value="H">H 选择汇总表</option>
                       </select>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">父单位ID:</td>
                   <td><input type="text" name="p_Parent" size=30 maxlength=10></td>
               </tr>
               <tr class="xsltTr">
                   <td >&nbsp;</td>
                   <td >
                       <div id="create">
                          <input type="button" value="新增" onclick="createUnit()"/>
                       </div>
                       <div id="updateAndDelete">
                           <input type="button" value="更新" onclick="updateUnit()"/>
                           <input type="button" value="删除" onclick="deleteUnit()"/>
                       </div>
                   </td>
               </tr>
          </table>
</form>



<script language="JavaScript">
<%
//初始化数据
UnitTreeNode unit = (UnitTreeNode)request.getAttribute("unit");
if(unit != null)
{
//显示更新界面
%>
unitForm.unitID.value="<%= Convertor.getHTMLString(unit.id()) %>";
unitForm.p_Parent.value="<%= Convertor.getHTMLString(unit.getP_Parent()) %>";
unitForm.unitName.value="<%= Convertor.getHTMLString(unit.getUnitName()) %>";
unitForm.code.value="<%= Convertor.getHTMLString(unit.getUnitCode()) %>";
unitForm.parentCode.value="<%= Convertor.getHTMLString(unit.getParentUnitCode()) %>";
unitForm.HQCode.value="<%= Convertor.getHTMLString(unit.getHQCode()) %>";
unitForm.reportType.value="<%= Convertor.getHTMLString(unit.getReportType()) %>";

createLink.style.visibility = "visible";
create.style.visibility = "hidden";
updateAndDelete.style.visibility = "visible";
<%
} else {
//显示新增界面
%>
createLink.style.visibility = "hidden";
create.style.visibility = "visible";
updateAndDelete.style.visibility = "hidden";
<%
}
%>
unitForm.unitName.focus();
</script>

</body>

</html>
