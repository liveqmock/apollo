<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.news.*" %>
<%@ page import="cn.com.youtong.apollo.news.dao.*" %>
<html>
<head>
<title></title>
</head>
<body>
<%String taskID = request.getParameter("taskID");
  YtaplNewsDAO newsDAO = new YtaplNewsDAO();
  String opr = request.getParameter("opr");
  if(opr!=null&&!"".equals(opr)){
  	String newsID = request.getParameter("newsID");
  	YtaplNews nws = new YtaplNews();
  	nws.setTaskID(taskID);
  	nws.setId(new Integer(newsID));
  	newsDAO.delete(nws);
%><script>alert('删除成功！');</script><%
  }

  List newss = newsDAO.findAll();
  int toEnd = newss.size();
  if(newss.size()>9) toEnd=9;
  newss = newss.subList(0,toEnd);
%>
<table>
	<tr><td><a href="../news/add.jsp?taskID=<%=taskID%>">增加信息</a></td></tr>
	<tr><td height=30></td></tr>
  <%for(Iterator it=newss.iterator();it.hasNext();){ 
      YtaplNews news = (YtaplNews)it.next();
  %>
	<tr>
	   <td><a href="../news/add.jsp?taskID=<%=taskID%>&newsID=<%=news.getId().intValue()%>"><%=news.getTitle()%></a></td>
	   <td width=60%></td>
	   <td align="right"><a name="del" href="javascript:ondel('<%=taskID%>','<%=news.getId().intValue()%>');">删除</a></td>
	</tr>
  <%} %>
</table>
</body>
</html>
<script language="javascript">
   function ondel(taskID,newsID){
      if(confirm('确定删除吗?')){
      	window.location="../news/shownewss.jsp?taskID="+taskID+"&newsID="+newsID+"&opr=del";
      }else{
      	return;
      }
   }
</script>