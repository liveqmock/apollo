<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.news.*" %>
<%@ page import="cn.com.youtong.apollo.news.dao.*" %>
<%@ page errorPage="error.jsp" %>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="../style/style.css">
</head>
<% 
   String newsID = request.getParameter("newsID");
   String title=request.getParameter("news_title");
   String content = request.getParameter("conten");
   String taskID = request.getParameter("taskID");
%>
<body>
  <% YtaplNewsDAO newsDAO = new YtaplNewsDAO();
     YtaplNews news = new YtaplNews();
     news.setTaskID(taskID);
     news.setContent(content);
     news.setTitle(title);
     YtaplNewsDAO newDAO = new YtaplNewsDAO();
     if(!"".equals(newsID)){
    	 news.setId(Integer.valueOf(newsID));
    	 newsDAO.saveOrUpdate(news);
     }else{
     	 newDAO.save(news);
     }
     response.sendRedirect("../news/shownewss.jsp");
  %>
</body>
</html>