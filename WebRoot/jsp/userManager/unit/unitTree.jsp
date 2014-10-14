<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%
//·ÀÖ¹ä¯ÀÀÆ÷»º´æ±¾Ò³Ãæ
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<%
String strUserTree=(String)request.getAttribute("unitTree");
%>
<%=strUserTree%>
