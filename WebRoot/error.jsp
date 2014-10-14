<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.common.*,
		cn.com.youtong.apollo.servlet.RootServlet" %>
<%@ page import="java.io.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>
系统信息
</title>
<script language='javascript'>
    function showDetail(){
        if (document.all("detail").style.visibility == "hidden")
            document.all("detail").style.visibility = "";
        else
            document.all("detail").style.visibility = "hidden";
    }
</script>
</head>
<body>


     <table width=100% height=50% border=0>
   <tr>
      <td align="left">

错误信息:<%= (String)request.getAttribute("info") %>
<hr>
<span style="font-size:16px;color:#b70000;font-weight:bold;letter-spacing:2px">
<%
try{
  Warning w=(Warning)request.getAttribute("warning");
if(w!=null){
  String weblogicStr="Start server side stack trace";
  String output=w.getMessage();
  //去除weblogic加在message前的字符串
  if(output!=null && output.indexOf(weblogicStr)>-1)
    output=output.substring(0,output.indexOf(weblogicStr));

  //记录日志
//  Log.error("用户：" + ((SystemAccountForm)request.getSession().getAttribute("LoginAccount")).accountName + " "  + output );

  out.println(output);
}
%>
</span><br>
<br>
<%
if(request.getAttribute(RootServlet.RETURN_URL) == null)
    out.print("<a href='javascript:window.history.back(1)'>返回</a>");
else
    out.print("<a href='" + request.getAttribute(RootServlet.RETURN_URL) + "'>返回</a>");
%>
&nbsp;&nbsp;
<%if (w!=null){%>
<a href="javascript:showDetail();">显示详细错误信息</a>

<br>
<div id="detail" style="visibility:hidden">
<pre>
<%
  w.printStackTrace(new PrintWriter(out));
  w.printStackTrace();
}
}catch(Throwable t){
	t.printStackTrace();
}
%>
</pre>
</div>

      </td>
  </tr>
</table>


</body>

</html>
