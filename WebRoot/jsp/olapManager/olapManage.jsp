<%@page contentType="text/html; charset=GBK"%>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*" %>
<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>OLAP</title>
</head>

<script language="javascript">
function showOlapPage(cubeQuery)
{
    olap.location = "../olap.jsp?query=" + cubeQuery;
}
</script>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

     <table class="clsContentTable">
      <tr>
       <td width="10%" vAlign="top">
        <div>
         <table class="clsContentListTable">
          <!--多维分析列表-->
          <tr class="clsTrHeader"><td><a href="javascript:showOlapPage('apollo')">资产负债</a></td></tr>
<!--
          <tr class="TrLight"><td><a href="javascript:showOlapPage('mondrian')">jpivot sample</a></td></tr>
-->
         </table>
        </div>
       </td>
       <td>
         <iframe SCROLLING=auto height="100%" width="100%" FRAMEBORDER=0 id="olap"></iframe>
       </td>
      </tr>
     </table>

  <jsp:include page= "/jsp/footer.jsp" />
</body>
</html>
