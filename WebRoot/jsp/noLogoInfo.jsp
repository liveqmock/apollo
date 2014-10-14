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
  <div style="height:100%;OVERFLOW: auto">
  <table height=100% width=100%>
      <tr align="center"><td align="center">
                <pre>
                        <%= request.getAttribute(RootServlet.INFO_MESSAGE) %>
                </pre>

                <%
                if(request.getAttribute(RootServlet.RETURN_URL) == null || request.getAttribute(RootServlet.RETURN_URL).equals("")){

                }else{
                          out.print("<a href='" + request.getAttribute(RootServlet.RETURN_URL) + "'>返回</a>");
                }
                %>
          &nbsp;&nbsp;
          <script language="javascript">
                if(window.opener!=null){
                //document.write('<a href="javascript:window.close();">关闭窗口</a>');
                }
          </script>
      </td>
      </tr>
  </table>
  </div>
</body>
</html>
