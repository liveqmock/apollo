<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.news.*" %>
<%@ page import="cn.com.youtong.apollo.news.dao.*" %>

<html>
<head>
<title>添加信息</title>
</head>
<%String taskID = request.getParameter("taskID"); 
  String newsID = request.getParameter("newsID");
  YtaplNewsDAO newsDAO = new YtaplNewsDAO();
  YtaplNews news = null;
  if(newsID!=null) news = newsDAO.findByID(Integer.valueOf(newsID));
%>
<body onload="oncontent();">
<center><br>
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" width="760" bgcolor="#f2f8ff">
  <tr>
    <td width="100%"  colspan="3" class="td7" align="center">
        <form name="ForAddNews" method="post" action="addok.jsp" onsubmit="document.ForAddNews.conten.value=document.ForAddNews.doc_html.value;return isValid(this);">
            <input name="conten" type="hidden" id="conten" value="">
           <table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse"  width="99%">
             <tr>
             </tr>
             <tr>
              <td width="9%">标&nbsp;&nbsp;&nbsp;&nbsp;题：</td>
              <td width="91%">
               <input name="news_title" type="text" id="news_title" size="50" value="<%=news!=null?news.getTitle():""%>">
               <font color="red">*</font></td>
             </tr>
             <tr>
              <td></td><td width="91%"></td>
             </tr>
            <tr>
              <td width="100%" colspan="2">
            <OBJECT id=doc_html style="LEFT: 0px; TOP: 0px" data=newsedit/editor.htm width=544
	        height=600 type=text/x-scriptlet  VIEWASTEXT>
            </OBJECT>
              </td>
            </tr>
            <tr>
              <td width="100%" colspan="2" align="center">
              <input type="hidden" name="newsID" value="<%=news!=null?news.getId().toString():""%>">
              <input type="hidden" name="taskID" value="<%=taskID%>">
              <input type="hidden" name="add" value="提交">
              <br>
              <input type="submit" value="确定">&nbsp;
              <button onclick="javascript:history.go(-1)">取消
              </td>
            </tr>
          </table>
         </form></td>
  </tr>
</table>
</CENTER>
</body>
</html>

<SCRIPT LANGUAGE="JavaScript">
function isValid(var1){
    if(var1.news_title.value==""){
        alert("标题不能为空!");
	var1.news_title.focus();
	return false;
    }
    else if(var1.conten.value==""){
        alert("内容不能为空!");
        return false;
    }
    else
        return true;
    }
    
    function oncontent(){
    	document.ForAddNews.doc_html.value="<%=news!=null?news.getContent():""%>";
    }
</SCRIPT>