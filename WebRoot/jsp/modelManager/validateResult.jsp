<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>
检查结果
</title>
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
</head>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

 <table class="clsContentListTable">
  <tr height=20 class="clsTrContext">
    <td align=center colspan=5>检查结果</td>
  </tr>
 <tr class="clsTrHeader" height=20px>
    <td nowrap>序号</td>
    <td nowrap>单位</td>
    <td nowrap>第一张出错的表</td>
    <td nowrap>第一个错误的指标</td>
    <td nowrap>描述</td>
  </tr>
<%
int count = 0;
Iterator resultItr = (Iterator)request.getAttribute("resultItr");
while(resultItr.hasNext())
{
    //单位
    ValidateResult result = (ValidateResult)resultItr.next();
    UnitTreeNode unit = result.getUnit();
    Map tables = result.getErrorTables();
    if(tables.size() > 0)
    {
        count++;
        String trClass = ((count % 2) == 1) ? "TrLight" : "TrDark";
%>
  <tr class="<%= trClass %>">
    <td><%= count %></td>
    <td><%= unit.getUnitName() %><br>(<%= unit.id() %>)</td>
<%
    Iterator tableItr = tables.keySet().iterator();
    //第一张表
    Table table = (Table)tableItr.next();
    Collection scalars = (Collection)tables.get(table);
%>
    <td><%= table.getName() %><br>(<%= table.id() %>)</td>
<%
    Iterator scalarItr = scalars.iterator();
    //第一个指标
    ValidateResult.ErrorScalar scalar = (ValidateResult.ErrorScalar)scalarItr.next();
%>
    <td><%= scalar.getName() %></td>
    <td><%= "差值（" + Convertor.formatDouble(scalar.getDifference()) + "） ＝ 汇总表（" + Convertor.formatDouble(scalar.getActual()) + "） － 子单位汇总结果（" + Convertor.formatDouble(scalar.getExpected()) + "）" %></td>
  </tr>
<%
    }
}
if(count == 0)
{
%>
  <tr height=20 class="clsTrContext">
    <td align=center colspan=5>全部通过</td>
  </tr>
<%
}
%>
</table>
  <jsp:include page= "/jsp/footer.jsp" />
<%!
/**
 * 得到单位行的rowspan值
 * @param result 单位的节点检查结果
 * @return 单位行的rowspan值
*/
public int getUnitRowspan(ValidateResult result)
{
    int rowspan = 0;
    Map tables = result.getErrorTables();
    for(Iterator itr = tables.keySet().iterator(); itr.hasNext();)
    {
        Collection scalars = (Collection)tables.get(itr.next());
        rowspan = rowspan + scalars.size() + 1;
    }
    rowspan++;
    return rowspan;
}
%>
</body>
</html>
