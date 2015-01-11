<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script type="text/javascript" src="../../jslib/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../../jslib/ext/ext-all.js"></script>
<script type="text/javascript" src="../../jslib/ext/ui/Util.js"></script>
<link rel="stylesheet" type="text/css" href="../../jslib/ext/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="../../jslib/ext/ui/icon.css">
<link href="../../csslib/main.css" rel="stylesheet" type="text/css">
<title>封面表管理</title>
<script type="text/javascript">
	var taskid = '<%=request.getParameter("taskID")%>';
</script>
</head>
<body>
	<script type="text/javascript" src="../../jslib/ext/ui/rowactions/Ext.ux.grid.RowActions.js"></script>
	<link rel="stylesheet" type="text/css" href="../../jslib/ext/ui/rowactions/Ext.ux.grid.RowActions.css"/>
	<script type="text/javascript" src="../../jslib/task/PluginCombox.js"></script>
	<script type="text/javascript" src="../../jslib/task/FmWindow.js"></script>
	<script type="text/javascript" src="../../jslib/task/FmTreePanel.js"></script>
	<script type="text/javascript" src="../../jslib/task/FmGrid.js"></script>
	<script type="text/javascript" src="../../jslib/task/FmFrame.js"></script>
</body>
</html>
