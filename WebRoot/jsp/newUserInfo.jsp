<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.common.*, cn.com.youtong.apollo.servlet.RootServlet" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>
提示信息
</title>
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page= "/jsp/logo.jsp" />

 <table class="clsContentTable">
   <tr align="center"><td>

<%= request.getAttribute(RootServlet.INFO_MESSAGE) %>
<p/>
<%
if(request.getAttribute(RootServlet.RETURN_URL) == null || request.getAttribute(RootServlet.RETURN_URL).equals("")){

}else{
    out.print("<a href='" + request.getAttribute(RootServlet.RETURN_URL) + "'>返回</a>");
}
%>
&nbsp;&nbsp;
<script language="javascript">
if(window.opener!=null){
  document.write('<a href="javascript:window.close();">关闭窗口</a>');
}
</script>
      </td>
  </tr>
</table>
<jsp:include page= "/jsp/footer.jsp" />

</body>
</html>
