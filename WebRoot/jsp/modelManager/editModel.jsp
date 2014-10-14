<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.usermanager.User" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.GregorianCalendar" %>


<%
    String readonly=(String)request.getParameter("readonly");
	if(readonly!=null&& readonly.equals("1"))
	   readonly="false";
	 else
	   readonly="true";
	 
	String param = (String)request.getAttribute("param");
	String server = (String)request.getAttribute("server");
	String taskTime = (String)request.getAttribute("taskTime");
	String unitID = request.getParameter("unitID");
	String userid = request.getParameter("userid");
	String error = request.getParameter("error");
	User user = (User)session.getAttribute(cn.com.youtong.apollo.servlet.RootServlet.LOGIN_USER);
	String accountPassword = (String)session.getAttribute("password");
	String serverpath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>
<%
	if(error!=null)
	{
		out.print("<script language=javascript>alert('没有装载表样');window.close();</script>");
		return;
	}
%>
<html>
<title>编辑数据<%=unitID%></title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<body style="margin:0" scroll="no">
<OBJECT ID="yttable" WIDTH="100%" HEIGHT="100%"
	CLASSID="CLSID:582D8295-8EDE-41C2-9C18-2FB5BFB4D8CD"
	CODEBASE="<%=serverpath%>/cab/YotopTableCtrl.cab#Version=1,0,0,30">
</OBJECT>
<SCRIPT LANGUAGE="JavaScript">
<!--
<%
   Calendar curDate=new GregorianCalendar();

%>

yttable.taskContent = "<%=param%>";
yttable.ServerUrl = "<%=server%>";
yttable.User = "<%=user.getName()%>";
yttable.Password = "<%=accountPassword%>";
yttable.ReadOnly = <%=readonly%>;
yttable.DownloadByWebservice("<%=taskTime%>","<%=unitID%>");
yttable.EvalScript("var yt_curdate=\"填报日期:<%=curDate.get(Calendar.YEAR)%>年<%=curDate.get(Calendar.MONTH)+1%>月<%=curDate.get(Calendar.DATE)%>日\";");

//-->
</SCRIPT>
</body>
</html>