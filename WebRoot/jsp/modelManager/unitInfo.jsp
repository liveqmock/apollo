<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<% //��ֹ��������汾ҳ��
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
 * ��֤��λ��Ϣ�������ȷ��
 * @return ��֤ͨ��������true�����򷵻�false
 */
function validate(){
    unitForm.unitName.value = trim(unitForm.unitName.value);
    if(unitForm.unitName.value == "")
    {
        alert("����д��λ����");
        unitForm.unitName.focus();
        return false;
    }

    unitForm.code.value = trim(unitForm.code.value);
    if(unitForm.code.value == "")
    {
        alert("����д��λ����");
        unitForm.code.focus();
        return false;
    }

    unitForm.parentCode.value = trim(unitForm.parentCode.value);

    unitForm.HQCode.value = trim(unitForm.HQCode.value);

    unitForm.reportType.value = trim(unitForm.reportType.value);
    if(unitForm.reportType.value == "")
    {
        alert("��ѡ�񱨱�����");
        unitForm.reportType.focus();
        return false;
    }

    unitForm.p_Parent.value = trim(unitForm.p_Parent.value);

    return true;
}

/**
 * ��ʾ������λ����
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

    if(confirm("�Ƿ����Ҫɾ����ǰ��λ��"))
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
                       <div id="createLink"><a href="javascript:showCreateUnitUI()" style="TEXT-DECORATION: underline">������λ</a></div>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center" width="25%">����:</td>
                   <td><input type="text" name="unitName" size=30 maxlength=20></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">����:</td>
                   <td><input type="text" name="code" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">�ϼ�����:</td>
                   <td><input type="text" name="parentCode" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">���Ŵ���:</td>
                   <td><input type="text" name="HQCode" size=30 maxlength=9></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">��������:</td>
                   <td>
                       <select name="reportType">
                          <option value="0">0 ��������</option>
                          <option value="1">1 ���Ų���</option>
                          <option value="2">2 ���ڲ���ҵ��</option>
                          <option value="3">3 ���Ⲣ��ҵ��</option>
                          <option value="4">4 ��ҵ����ҵ��</option>
                          <option value="5">5 ��������ҵ��</option>
                          <option value="7">7 ��ȫ���ܱ�</option>
                          <option value="9">9 ���źϲ���</option>
                          <option value="H">H ѡ����ܱ�</option>
                       </select>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">����λID:</td>
                   <td><input type="text" name="p_Parent" size=30 maxlength=10></td>
               </tr>
               <tr class="xsltTr">
                   <td >&nbsp;</td>
                   <td >
                       <div id="create">
                          <input type="button" value="����" onclick="createUnit()"/>
                       </div>
                       <div id="updateAndDelete">
                           <input type="button" value="����" onclick="updateUnit()"/>
                           <input type="button" value="ɾ��" onclick="deleteUnit()"/>
                       </div>
                   </td>
               </tr>
          </table>
</form>



<script language="JavaScript">
<%
//��ʼ������
UnitTreeNode unit = (UnitTreeNode)request.getAttribute("unit");
if(unit != null)
{
//��ʾ���½���
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
//��ʾ��������
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
