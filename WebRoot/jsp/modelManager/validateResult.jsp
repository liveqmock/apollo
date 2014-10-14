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
�����
</title>
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
</head>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

 <table class="clsContentListTable">
  <tr height=20 class="clsTrContext">
    <td align=center colspan=5>�����</td>
  </tr>
 <tr class="clsTrHeader" height=20px>
    <td nowrap>���</td>
    <td nowrap>��λ</td>
    <td nowrap>��һ�ų���ı�</td>
    <td nowrap>��һ�������ָ��</td>
    <td nowrap>����</td>
  </tr>
<%
int count = 0;
Iterator resultItr = (Iterator)request.getAttribute("resultItr");
while(resultItr.hasNext())
{
    //��λ
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
    //��һ�ű�
    Table table = (Table)tableItr.next();
    Collection scalars = (Collection)tables.get(table);
%>
    <td><%= table.getName() %><br>(<%= table.id() %>)</td>
<%
    Iterator scalarItr = scalars.iterator();
    //��һ��ָ��
    ValidateResult.ErrorScalar scalar = (ValidateResult.ErrorScalar)scalarItr.next();
%>
    <td><%= scalar.getName() %></td>
    <td><%= "��ֵ��" + Convertor.formatDouble(scalar.getDifference()) + "�� �� ���ܱ�" + Convertor.formatDouble(scalar.getActual()) + "�� �� �ӵ�λ���ܽ����" + Convertor.formatDouble(scalar.getExpected()) + "��" %></td>
  </tr>
<%
    }
}
if(count == 0)
{
%>
  <tr height=20 class="clsTrContext">
    <td align=center colspan=5>ȫ��ͨ��</td>
  </tr>
<%
}
%>
</table>
  <jsp:include page= "/jsp/footer.jsp" />
<%!
/**
 * �õ���λ�е�rowspanֵ
 * @param result ��λ�Ľڵ�����
 * @return ��λ�е�rowspanֵ
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
