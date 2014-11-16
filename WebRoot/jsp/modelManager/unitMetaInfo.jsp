<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.address.*" %>
<%@ page import="java.util.*" %>
<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">

<script type="text/javascript" src="../jslib/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../jslib/ext/ext-all.js"></script>
<script type="text/javascript" src="../jslib/ext/ui/Util.js"></script>
<link rel="stylesheet" type="text/css" href="../jslib/ext/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="../jslib/ext/ui/icon.css">

<script Language="JavaScript" src="../jslib/public.js"></script>
</head>
<script language="JavaScript">
  var taskID = <%='"'+(String)request.getAttribute("taskID")+'"'%>;
  var unitID = <%='"'+(String)request.getAttribute("unitID")+'"'%>;
function addAddressInfo(){
    addressForm.action = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.ADD_ADDRESS_INFO%>";
    addressForm.taskID.value = taskID;
    addressForm.unitID.value = unitID;
    addressForm.submit();
}
function displayAllAddressInfo(){
     url = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>";
     window.location = url;
}
</script>
<body>
	<script type="text/javascript" src="../jslib/task/PluginCombox.js"></script>
	<script type="text/javascript" src="../jslib/model/dict_hy.js"></script>
	<script type="text/javascript" src="../jslib/model/dict_area.js"></script>
	<script type="text/javascript" src="../jslib/model/UnitMetaTableForm.js"></script>
</body>

</html>
