<%@ page contentType="text/html; charset=GBK" %>

<% //��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">


<title>ѡ��λ</title>

<style type="text/css">
A {
	color: darkblue
}

</style>

</head>




<body onload="javascript:parent.doOnload()">




<table width="100%" height="13" cellspacing="0" border="0" cellpadding="0">
	<tr height="2">
		<td colspan="6">&nbsp;</td>
	</tr>

	<tr height="5">
		<td>&nbsp;</td>
		<td width="80" id="listTd">
			<a href="javascript:parent.byList()" id="listA">���б�ѡ��</a>
			&nbsp;
		</td>

		<td width="8" vertical-align="middle">
			<IMG src="../img/tab_separator.gif" align="absMiddle" border="0"/>
		</td>

		<td>&nbsp;</td>
		<td width="70" id="treeTd">
			<a href="javascript:parent.byTree()" id="treeA">����ѡ��</a>
			&nbsp;
		</td>

		<td BACKGROUND ="../img/tab_stripes.jpg" width="75%"></td>

	</tr>

	<tr height="1">
		<td colspan="6" style="background-color: black">
		</td>
	</tr>
</table>

<% /**������ʾ��ͬ��ʽҳ���form�� ��ҪĿ�����ύѡ���unitIDs*/ %>
<form id="listForm" method="post" action="utilServlet" target="mainFrame">
	<input type="hidden" name="operation" value="<%=cn.com.youtong.apollo.servlet.UtilServlet.SHOW_SEARCH_UNIT%>"/>
	<input type="hidden" name="doAfterDone" value="parent.doneSelectByList()"/>
	<input type="hidden" name="closeAfterDone" value="false"/>
</form>

<form id="treeForm" method="post" action="model" target="mainFrame">
	<input type="hidden" name="operation" value="<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_UNIT_TREE%>"/>
	<input type="hidden" name="doAfterDone" value="parent.doneSelectByTree()"/>
	<input type="hidden" name="closeAfterDone" value="false"/>
</form>

</body>
</html>
