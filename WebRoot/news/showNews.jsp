<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="cn.com.youtong.apollo.news.*" %>
<%@ page import="cn.com.youtong.apollo.news.dao.*" %>
<html>
<head>
<title></title>
</head>
<body>
<%String newsID = request.getParameter("newsID");
  YtaplNewsDAO newsDAO = new YtaplNewsDAO();
  YtaplNews news = newsDAO.findByID(Integer.valueOf(newsID));
%>
<table width=88%>
  <tr><td align=center><%=news.getTitle()%></td></tr>
  <tr height=30><td align=center></td></tr>
  <tr><td><%=news.getContent()%></td></tr>
</table>
</body>
</html>