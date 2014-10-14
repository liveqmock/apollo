<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.FillStateForm" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.form.*" %>
<%@ page import="cn.com.youtong.apollo.analyse.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!-- <%@ page import="com.cc.framework.ui.control.TreelistControl"%>
 -->
<%@ page import="cn.com.youtong.apollo.tabtree.presentation.dsp.RegionGroupDsp"%>
<%@ taglib uri="/WEB-INF/cc-controls.tld" prefix="ctrl" %>

<html>
<base>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/csslib/main.css">
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>

<script language="JavaScript1.2" src="<%=request.getContextPath()%>/fw/global/jscript/common.js"></script>
<script language="JavaScript1.2" src="<%=request.getContextPath()%>/fw/global/jscript/environment.js"></script>
<link rel='stylesheet' href='<%=request.getContextPath()%>/fw/def/style/default.css' charset='GBK' type='text/css'>
<script language='JavaScript' src='<%=request.getContextPath()%>/fw/def/jscript/functions.js'></script>
<script language='JavaScript' src='<%=request.getContextPath()%>/fw/def/jscript/controls.js'></script>
<script language='JavaScript' src='<%=request.getContextPath()%>/fw/def/jscript/tabset.js'></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/calendar/public.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/calendar/lw_menu.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/calendar/lw_layers.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/calendar/popcalendar.js"></script>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>查询单位填报情况</title>
<style type="text/css">
<!--
.style1 {color: #00FF00}
.style2 {color: #FF0000}
-->
</style>
</head>
<body>

<jsp:include page= "/jsp/analyseManager/fillstateMenu.jsp" />
<div id="maindiv" style="height:88%;OVERFLOW:auto">
  <table width=100% border=0>
    <tr>
      <td>
<%
   Collection coll = (Collection)session.getAttribute("collection");
   int count = 1;
   if(coll.size()>0) count=coll.size();
   String fillstateTitle = (String)session.getAttribute("fillstateTitle");
%>
<ctrl:treelist
	id="tl1"
	name="regions"
	action="regionBrowse"
	title="<%=fillstateTitle%>"
	rows="<%=new Integer(count+1).toString()%>"
	refreshButton="false"
	expandMode="multiple"
	root="true">

	<ctrl:columntree title="单位代码" filter="false" property="id" width="180" imageProperty="type"/>
	<ctrl:columntext title="单位名称" filter="false" property="name" width="200"/>
        <ctrl:columntext title="是否填报" filter="false" property="fillState" width="70" />
        <ctrl:columntext title="填报时间" filter="false" property="date" width="130" />
        <ctrl:columntext title="是否审核" filter="false" property="auditState" width="60" />
        <ctrl:columntext title="审核时间" filter="false" property="auditDate" width="130" />
        <ctrl:columntext title="审核者" filter="false" property="auditUser" width="50" />

</ctrl:treelist>

       </td>
     </tr>
</table>
</div>

<SCRIPT LANGUAGE="JavaScript">
<!--
<%
  String param=request.getParameter("param");
%>
try{
var selNode=document.all('tree<%=param%>');
var offsetTop = selNode.offsetTop;
 while( selNode = selNode.offsetParent ) 
    { 
        offsetTop += selNode.offsetTop; 
    } 

document.all('maindiv').scrollTop=offsetTop-100;
}
catch(e)
{
}

//-->
</SCRIPT>

</body>
</html>

<%
   Task task = (Task) session.getAttribute("task");
   request.setAttribute("task",task);
%>
<script language="JavaScript1.2">
<!--
function message() {
	return false;
}
	function show()
        {
          var e = event.srcElement;
          window.open("servlet/model?operation=<%=ModelServlet.SHOW_DATA%>&unitID="+e.unit+"&taskTimeID=<%=request.getSession().getAttribute("taskTimeID")%>","_blank","height=500,width=525,resizable=yes,scrollbars=yes");
        }

document.oncontextmenu=Function("return false;");
//-->
</script>
