<%@ page session="true" contentType="text/html; charset=Gb2312"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<html>
<head>
  <title>系统正忙</title>
  <meta http-equiv="refresh" content="1; URL=<c:out value="${requestSynchronizer.resultURI}"/>">
  <link rel="stylesheet" type="text/css" href="../csslib/webstyle.css">
</head>
<body bgcolor=white>

  <h2>系统正忙</h2>
  系统正在计算,请耐心等待. 如果浏览器不支持自动跳转,请点击<a href="<c:out value="${requestSynchronizer.resultURI}"/>">这儿.</a>


</body>
</html>
