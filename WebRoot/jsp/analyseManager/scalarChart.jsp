<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.AnalyseServlet" %>
<%@ page import="cn.com.youtong.apollo.task.Task" %>
<%@ page import="java.util.*" %>

<html>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>图表显示</title>
</head>
<body>
<%
    String webRoot = request.getContextPath();
    Collection fileNames = (Collection)request.getAttribute("fileNames");
    Iterator _fileNames = fileNames.iterator();
    while(_fileNames.hasNext()){
       String fileName = (String)_fileNames.next();
%>
     <img src=<%=webRoot + "/servlet/analyse?operation=" + AnalyseServlet.SHOW_CHART_IMAGE + "&chart="+fileName%> width="400" height="400">
<%
    }
%>
</body>
</html>

