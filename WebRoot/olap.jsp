<%@ page session="true" contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib uri="http://www.tonbeller.com/wcf" prefix="wcf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
  <title>OLAP</title>
  <meta http-equiv="Content-Type" content="text/html; charset=GBK">
  <link rel="stylesheet" type="text/css" href="jpivot/table/mdxtable.css">
  <link rel="stylesheet" type="text/css" href="jpivot/navi/mdxnavi.css">
  <link rel="stylesheet" type="text/css" href="wcf/form/xform.css">
  <link rel="stylesheet" type="text/css" href="wcf/table/xtable.css">
</head>
<body bgcolor=white>
<%
    User user = RootServlet.getLoginUser(request);
    if(user == null)
    {
%>
<jsp:forward page="/index.jsp"/>
<%
    }
    else if(RootServlet.hasPrivilege(request, SetOfPrivileges.MANAGE_TASK))
    {
%>
<form action="olap.jsp" method="post">

<%-- include query and title, so this jsp may be used with different queries --%>
<wcf:include id="include01" httpParam="query" prefix="/WEB-INF/queries/" suffix=".jsp"/>
<c:if test="${query01 == null}">
  <jsp:forward page="/olapIndex.jsp"/>
</c:if>

<%-- define table, navigator and forms --%>
<jp:table id="table01" query="${query01}"/>
<jp:navigator id="navi01" query="${query01}" visible="false"/>
<wcf:form id="mdxedit01" xmlUri="/WEB-INF/jpivot/table/mdxedit.xml" model="${query01}" visible="false"/>
<wcf:form id="sortform01" xmlUri="/WEB-INF/jpivot/table/sortform.xml" model="${table01}" visible="false"/>

<jp:chart id="chart01" query="${query01}" visible="false"/>
<wcf:form id="chartform01" xmlUri="/WEB-INF/jpivot/chart/chartpropertiesform.xml" model="${chart01}" visible="false"/>
<wcf:table id="query01.drillthroughtable" visible="false"/>

<h2><c:out value="${title01}"/></h2>


<%-- define a toolbar --%>
<wcf:toolbar id="toolbar01" bundle="com.tonbeller.jpivot.toolbar.resources">
  <wcf:scriptbutton id="cubeNaviButton" tooltip="toolb.cube" img="cube" model="${navi01.visible}"/>
<%--
  <wcf:scriptbutton id="mdxEditButton" tooltip="toolb.mdx.edit" img="mdx-edit" model="${mdxedit01.visible}"/>
  <wcf:scriptbutton id="sortConfigButton" tooltip="toolb.table.config" img="sort-asc" model="${sortform01.visible}"/>
  <wcf:separator/>
  <wcf:scriptbutton id="levelStyle" tooltip="toolb.level.style" img="level-style" model="${table01.extensions.axisStyle.levelStyle}"/>
  <wcf:scriptbutton id="hideSpans" tooltip="toolb.hide.spans" img="hide-spans" model="${table01.extensions.axisStyle.hideSpans}"/>
  <wcf:scriptbutton id="propertiesButton" tooltip="toolb.properties"  img="properties" model="${table01.rowAxisBuilder.axisConfig.propertyConfig.showProperties}"/>
  <wcf:scriptbutton id="nonEmpty" tooltip="toolb.non.empty" img="non-empty" model="${table01.extensions.nonEmpty.buttonPressed}"/>
--%>
  <wcf:scriptbutton id="swapAxes" tooltip="toolb.swap.axes"  img="swap-axes" model="${table01.extensions.swapAxes.buttonPressed}"/>
<%--
  <wcf:separator/>
  <wcf:scriptbutton model="${table01.extensions.drillMember.enabled}"	 tooltip="toolb.navi.member" radioGroup="navi" id="drillMember"   img="navi-member"/>
  <wcf:scriptbutton model="${table01.extensions.drillPosition.enabled}" tooltip="toolb.navi.position" radioGroup="navi" id="drillPosition" img="navi-position"/>
  <wcf:scriptbutton model="${table01.extensions.drillReplace.enabled}"	 tooltip="toolb.navi.replace" radioGroup="navi" id="drillReplace"  img="navi-replace"/>
  <wcf:scriptbutton model="${table01.extensions.drillThrough.enabled}"  tooltip="toolb.navi.drillthru" id="drillThrough01"  img="navi-through"/>
  <wcf:separator/>
  <wcf:scriptbutton id="chartButton01" tooltip="toolb.chart" img="chart" model="${chart01.visible}"/>
  <wcf:scriptbutton id="chartPropertiesButton01" tooltip="toolb.chart.config" img="chart-config" model="${chartform01.visible}"/>
--%>
</wcf:toolbar>

<%-- render toolbar --%>
<wcf:render ref="toolbar01" xslUri="/WEB-INF/jpivot/toolbar/htoolbar.xsl" xslCache="true"/>
<p>

<%-- render navigator --%>
<wcf:render ref="navi01" xslUri="/WEB-INF/jpivot/navi/navigator.xsl" xslCache="true"/>

<%-- edit mdx --%>
<c:if test="${mdxedit01.visible}">
<h3>MDX Query Editor</h3>
<wcf:render ref="mdxedit01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>
</c:if>

<%-- sort properties --%>
<wcf:render ref="sortform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<%-- chart properties --%>
<wcf:render ref="chartform01" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>


<!-- render the table -->
<p>
<%-- wcf:render ref="table01" xslUri="/WEB-INF/wcf/showxml.xsl" xslCache="true"/ --%>
<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxtable.xsl" xslCache="true"/>
<p>
<%--
Slicer:
<wcf:render ref="table01" xslUri="/WEB-INF/jpivot/table/mdxslicer.xsl" xslCache="true"/>
--%>
<p>
<!-- drill through table -->
<wcf:render ref="query01.drillthroughtable" xslUri="/WEB-INF/wcf/wcf.xsl" xslCache="true"/>

<p>
<!-- render chart -->
<wcf:render ref="chart01" xslUri="/WEB-INF/jpivot/chart/chart.xsl" xslCache="true"/>


</form>

<%
    }
    else
    {
%>
您没有权限执行此操作！
<%
    }
%>
</body>
</html>
