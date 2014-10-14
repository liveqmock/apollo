<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.services.Config" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<html>
<STYLE type=text/css>
.clsConfigTbl{
    BACKGROUND-COLOR: white;
    border-collapse:collapse
}
</STYLE>
<head>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>服务器配置</title>
</head>
<script language="JavaScript">

  //发布前检查
  function checkInit()
  {
    if(form1.file.value == "")
    {
        alert("请选择参数文件！");
        return false;
    }
    return true;
  }
</script>
<body bgcolor="#ffffff">
  <jsp:include page= "/jsp/logo.jsp" />
   <jsp:include page= "/jsp/navigation.jsp" />

<table class="clsConfigTbl" border=1 align=center>
    <tr>
      <form action="task?operation=<%=TaskServlet.DO_INIT %>" method="post" enctype="multipart/form-data" name="form1">
        <td align=center>参数文件：</td>
        <td colspan="3">
          <input type="file" name="file"/>
          <input type="submit" value="提　交" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
	<tr>
		<th colspan="2">数据库配置</th>
	</tr>

	<tr>
		<td>数据库类型</td>
		<td><%= Config.getString("cn.com.youtong.apollo.database") %></td>
	</tr>
	<tr>
		<td>Hibernate配置文件</td>
		<td><%= Config.getString("cn.com.youtong.apollo.hibernate.cfg.xml") %></td>
	</tr>
	<tr>
		<th colspan="2">邮件配置</th>
	</tr>
	<tr>
		<td>smpt服务器地址</td>
		<td><%= Config.getString("mail.smtp.host") %></td>
	</tr>
	<tr>
		<td>用户名 / 密码</td>
		<td><%= Config.getString("mail.smtp.user") %> / <%= Config.getString("mail.smtp.password") %></td>
	</tr>
	<tr>
		<td>管理员邮箱</td>
		<td><%= Config.getString("mail.admin.address") %></td>
	</tr>
	<tr>
		<td>发件人邮箱</td>
		<td><%= Config.getString("mail.post.address") %></td>
	</tr>
	<tr>
		<td>邮件线程池最小线程数</td>
		<td><%= Config.getString("min.mail.size") %></td>
	</tr>
	<tr>
		<td>邮件线程池最大线程数</td>
		<td><%= Config.getString("max.mail.size") %></td>
	</tr>
	<tr>
		<th colspan="2">邮件上报配置</th>
	</tr>
	<%
	String[] pop3Hosts = Config.getStringArray("mail.pop3.host");
	String[] users = Config.getStringArray("mail.pop3.user");
	String[] passes = Config.getStringArray("mail.pop3.password");
	for (int i=0; i<pop3Hosts.length; i++) {
	%>
	<tr>
		<td>服务器地址<%=i+1%></td>
		<td><%=pop3Hosts[i]%></td>
	</tr>
	<tr>
		<td>用户名 / 密码<%=i+1%></td>
		<td><%=users[i]%> / <%=passes[i]%></td>
	</tr>
	<% } %>

	<tr>
		<th colspan="2">FTP上报设置</th>
	</tr>
	<%
	String[] ftpHosts = Config.getStringArray("cn.com.youtong.ftp.host");
	String[] ftpUsers = Config.getStringArray("cn.com.youtong.ftp.user");
	String[] ftpPasses = Config.getStringArray("cn.com.youtong.ftp.password");
	for (int i=0; i<ftpHosts.length; i++) {
	%>
	<tr>
		<td>服务器地址<%=i+1%></td>
		<td><%=ftpHosts[i]%></td>
	</tr>
	<tr>
		<td>用户名 / 密码<%=i+1%></td>
		<td><%=ftpUsers[i]%> / <%=ftpPasses[i]%></td>
	</tr>
	<% } %>

	<tr>
		<th colspan="2">文件夹接收配置</th>
	</tr>
	<tr>
		<td></td>
		<td><%=Config.getString("cn.com.youtong.apollo.zipdata.directory")%></td>
	</tr>

	<tr>
		<th colspan="2">数据装入配置</th>
	</tr>
	<tr>
		<td>装入新单位</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.data.import.loadnewunit")%></td>
	</tr>
	<tr>
		<td>修改封面表固定字段</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.modify.fixcode", false)%></td>
	</tr>
	<tr>
		<td>数据导入成功通知方式</td>
		<td>
			<%String[] succNotifies = Config.getStringArray("cn.com.youtong.apollo.loaddata.notify.success");
			if (succNotifies == null) {
				out.println("没有设置催报方式");
			} else {
				printArray(out, succNotifies);
			}
			%>
		</td>
	</tr>
	<tr>
		<td>数据导入失败通知方式</td>
		<td>
			<%String[] failNotifies = Config.getStringArray("cn.com.youtong.apollo.loaddata.notify.failure");
			if (failNotifies == null) {
				out.println("没有设置催报方式");
			} else {
				printArray(out, failNotifies);
			}
			%>
		</td>
	</tr>
	<tr>
		<td>数据导入成功通知管理员</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.notify.success.toadmin", true)%></td>
	</tr>
	<tr>
		<td>数据导入失败通知管理员</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.notify.failure.toadmin", true)%></td>
	</tr>

	<tr>
		<th colspan="2">催报信息配置</th>
	</tr>
	<%
	String[] notifies = Config.getStringArray("cn.com.youtong.apollo.notifiers");
	if (notifies == null) {
		out.println("<tr><td colspan='2'>没有设置催报方式</td></tr>");
	} else {
	%>
	<tr>
		<td>催报方式</td>
		<td>
		<%
		printArray(out, notifies);
		%>
		</td>
	</tr>
	<tr>
		<td>催报开始时间</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.day.begintime", "9:00")%></td>
	</tr>
	<tr>
		<td>催报开始时间</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.day.endtime", "17:00")%></td>
	</tr>
	<tr>
		<td>非工作日催报</td>
		<td><%=!Config.getBoolean("cn.com.youtong.apollo.notify.workday.only")%></td>
	</tr>
	<tr>
		<td>第一天催报次数</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.base")%></td>
	</tr>
	<tr>
		<td>每天增加催报次数</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.increment")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td>邮件缓存文件</td>
		<td><%=Config.getString("email.outputfile")%></td>
	</tr>
	<tr>
		<td>短信缓存文件</td>
		<td><%=Config.getString("sms.outputfile")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<% // 催报任务，可能存在多个任务需要催报
	String[] tasks = Config.getStringArray("cn.com.youtong.apollo.notify.task");

	if (tasks != null) {
		for (int i=0; i<tasks.length; i++) {
			String taskID = tasks[i];
			String[] taskNotifies = Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".notifiers");

			// 如果没有指定催报方式，缺省使用所有的催报方式
			if (taskNotifies == null) {
				taskNotifies = notifies;
			}
			out.println("<tr><td>任务" + taskID + "催报方式</td>");
			out.print("<td>");

			printArray(out, taskNotifies);
			out.println("</td></tr>");

			// 任务催报的单位domain
			String[] domains = Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".domain");

			for (int j=0; j<domains.length; j++) {
				String domain = domains[j];
				out.println("<tr><td>任务" + taskID + " domain " + domain + "</td>");
				int depth = Config.getInt("cn.com.youtong.apollo.notify." + taskID + "." + domain + ".depth", 1);
				out.println("<td>" + depth + "</td></tr>");
			}
			out.println("<tr><td colspan='2'></td></tr>");
		}
	}
	%>
	<% } // End else (如果没有设置催报方式结束) %>

	<tr>
		<th colspan="2">其他配置</th>
	</tr>
	<tr>
		<td>图表文件存放目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.chart.directory")%></td>
	</tr>
	<tr>
		<td>excel文件存放目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.excel.directory")%></td>
	</tr>
	<tr>
		<td>上报数据fap文件接收目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.zipdata.directory")%></td>
	</tr><tr>
		<td>fap释放目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.extractdata.directory")%></td>
	</tr>
	<tr>
		<td>数据备份目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.backupdata.directory")%></td>
	</tr>
	<tr>
		<td>过期数据备份的目录</td>
		<td><%=Config.getString("cn.com.youtong.apollo.outoftimelimitdata.directory")%></td>
	</tr>
	<tr>
		<td>web页分页显示中的每页的数据行数</td>
		<td><%=Config.getString("cn.com.youtong.apollo.webconfig.pageNum")%></td>
	</tr>

    <tr><td>&nbsp;</td></tr>


</table>

<jsp:include page= "/jsp/footer.jsp" />
</body>
</html>



<%!
/**
  * 打印数组，数组各项中间用","隔开，打印完毕，光标位于起始行
  * @param out   打印目的流
  * @param array 要打印的String型数组
  * @throws java.io.IOException
  */
private void printArray(javax.servlet.jsp.JspWriter out, String[] array)
		throws java.io.IOException {
	if (array == null || array.length == 0)
		return;

	out.print(array[0]);

	for (int i=1; i<array.length; i++) {
		out.print(",");
		out.print(array[i]);
	}
	out.println("");
}
%>