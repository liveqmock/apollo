<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="cn.com.youtong.apollo.servlet.UtilServlet"%>
<%
   String contextPath=request.getContextPath();
%>
<html>
<head>
<title>单位树</title>
<meta http-equiv="Content-Type" content="text/html; charset=干部312" />
<script type="text/javascript" src="<%=contextPath%>/jslib/xtree.js"></script>
<script type="text/javascript" src="<%=contextPath%>/jslib/xmlextras.js"></script>
<script type="text/javascript" src="<%=contextPath%>/jslib/xloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="<%=contextPath%>/csslib/xtree.css" />

<style type="text/css">

body {
	background:	white;
	color:		black;
}

</style>
</head>
<body>
<?IMPORT NAMESPACE='IE' IMPLEMENTATION='#default'>
<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WB width=0></OBJECT>

<script type="text/javascript">

webFXTreeConfig.rootIcon		= "<%=contextPath%>/img/icon_7.gif";
webFXTreeConfig.openRootIcon	= "<%=contextPath%>/img/icon_7.gif";
webFXTreeConfig.folderIcon		= "<%=contextPath%>/img/icon_7.gif";
webFXTreeConfig.openFolderIcon	= "<%=contextPath%>/img/icon_7.gif";
webFXTreeConfig.fileIcon		= "<%=contextPath%>/img/icon_0.gif";
webFXTreeConfig.lMinusIcon		= "<%=contextPath%>/img/Lminus.png";
webFXTreeConfig.lPlusIcon		= "<%=contextPath%>/img/Lplus.png";
webFXTreeConfig.tMinusIcon		= "<%=contextPath%>/img/Tminus.png";
webFXTreeConfig.tPlusIcon		= "<%=contextPath%>/img/Tplus.png";
webFXTreeConfig.iIcon			= "<%=contextPath%>/img/I.png";
webFXTreeConfig.lIcon			= "<%=contextPath%>/img/L.png";
webFXTreeConfig.tIcon			= "<%=contextPath%>/img/T.png";
webFXTreeConfig.blankIcon			= "<%=contextPath%>/img/blank.png";


var tree = new WebFXTree('Root');
tree.setBehavior('classic');
<%
 String unittree=(String)request.getAttribute("unittree");
%>
<%=unittree%>
document.write(tree);
tree.expandAll();
</script>

<script language='javascript'>
WB.ExecWB(7,1);
</script>
</body>
</html>
