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
<title>����������</title>
</head>
<script language="JavaScript">

  //����ǰ���
  function checkInit()
  {
    if(form1.file.value == "")
    {
        alert("��ѡ������ļ���");
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
        <td align=center>�����ļ���</td>
        <td colspan="3">
          <input type="file" name="file"/>
          <input type="submit" value="�ᡡ��" onclick="return checkInit()"/>
        </td>
      </form>
    </tr>
	<tr>
		<th colspan="2">���ݿ�����</th>
	</tr>

	<tr>
		<td>���ݿ�����</td>
		<td><%= Config.getString("cn.com.youtong.apollo.database") %></td>
	</tr>
	<tr>
		<td>Hibernate�����ļ�</td>
		<td><%= Config.getString("cn.com.youtong.apollo.hibernate.cfg.xml") %></td>
	</tr>
	<tr>
		<th colspan="2">�ʼ�����</th>
	</tr>
	<tr>
		<td>smpt��������ַ</td>
		<td><%= Config.getString("mail.smtp.host") %></td>
	</tr>
	<tr>
		<td>�û��� / ����</td>
		<td><%= Config.getString("mail.smtp.user") %> / <%= Config.getString("mail.smtp.password") %></td>
	</tr>
	<tr>
		<td>����Ա����</td>
		<td><%= Config.getString("mail.admin.address") %></td>
	</tr>
	<tr>
		<td>����������</td>
		<td><%= Config.getString("mail.post.address") %></td>
	</tr>
	<tr>
		<td>�ʼ��̳߳���С�߳���</td>
		<td><%= Config.getString("min.mail.size") %></td>
	</tr>
	<tr>
		<td>�ʼ��̳߳�����߳���</td>
		<td><%= Config.getString("max.mail.size") %></td>
	</tr>
	<tr>
		<th colspan="2">�ʼ��ϱ�����</th>
	</tr>
	<%
	String[] pop3Hosts = Config.getStringArray("mail.pop3.host");
	String[] users = Config.getStringArray("mail.pop3.user");
	String[] passes = Config.getStringArray("mail.pop3.password");
	for (int i=0; i<pop3Hosts.length; i++) {
	%>
	<tr>
		<td>��������ַ<%=i+1%></td>
		<td><%=pop3Hosts[i]%></td>
	</tr>
	<tr>
		<td>�û��� / ����<%=i+1%></td>
		<td><%=users[i]%> / <%=passes[i]%></td>
	</tr>
	<% } %>

	<tr>
		<th colspan="2">FTP�ϱ�����</th>
	</tr>
	<%
	String[] ftpHosts = Config.getStringArray("cn.com.youtong.ftp.host");
	String[] ftpUsers = Config.getStringArray("cn.com.youtong.ftp.user");
	String[] ftpPasses = Config.getStringArray("cn.com.youtong.ftp.password");
	for (int i=0; i<ftpHosts.length; i++) {
	%>
	<tr>
		<td>��������ַ<%=i+1%></td>
		<td><%=ftpHosts[i]%></td>
	</tr>
	<tr>
		<td>�û��� / ����<%=i+1%></td>
		<td><%=ftpUsers[i]%> / <%=ftpPasses[i]%></td>
	</tr>
	<% } %>

	<tr>
		<th colspan="2">�ļ��н�������</th>
	</tr>
	<tr>
		<td></td>
		<td><%=Config.getString("cn.com.youtong.apollo.zipdata.directory")%></td>
	</tr>

	<tr>
		<th colspan="2">����װ������</th>
	</tr>
	<tr>
		<td>װ���µ�λ</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.data.import.loadnewunit")%></td>
	</tr>
	<tr>
		<td>�޸ķ����̶��ֶ�</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.modify.fixcode", false)%></td>
	</tr>
	<tr>
		<td>���ݵ���ɹ�֪ͨ��ʽ</td>
		<td>
			<%String[] succNotifies = Config.getStringArray("cn.com.youtong.apollo.loaddata.notify.success");
			if (succNotifies == null) {
				out.println("û�����ô߱���ʽ");
			} else {
				printArray(out, succNotifies);
			}
			%>
		</td>
	</tr>
	<tr>
		<td>���ݵ���ʧ��֪ͨ��ʽ</td>
		<td>
			<%String[] failNotifies = Config.getStringArray("cn.com.youtong.apollo.loaddata.notify.failure");
			if (failNotifies == null) {
				out.println("û�����ô߱���ʽ");
			} else {
				printArray(out, failNotifies);
			}
			%>
		</td>
	</tr>
	<tr>
		<td>���ݵ���ɹ�֪ͨ����Ա</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.notify.success.toadmin", true)%></td>
	</tr>
	<tr>
		<td>���ݵ���ʧ��֪ͨ����Ա</td>
		<td><%=Config.getBoolean("cn.com.youtong.apollo.loaddata.notify.failure.toadmin", true)%></td>
	</tr>

	<tr>
		<th colspan="2">�߱���Ϣ����</th>
	</tr>
	<%
	String[] notifies = Config.getStringArray("cn.com.youtong.apollo.notifiers");
	if (notifies == null) {
		out.println("<tr><td colspan='2'>û�����ô߱���ʽ</td></tr>");
	} else {
	%>
	<tr>
		<td>�߱���ʽ</td>
		<td>
		<%
		printArray(out, notifies);
		%>
		</td>
	</tr>
	<tr>
		<td>�߱���ʼʱ��</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.day.begintime", "9:00")%></td>
	</tr>
	<tr>
		<td>�߱���ʼʱ��</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.day.endtime", "17:00")%></td>
	</tr>
	<tr>
		<td>�ǹ����մ߱�</td>
		<td><%=!Config.getBoolean("cn.com.youtong.apollo.notify.workday.only")%></td>
	</tr>
	<tr>
		<td>��һ��߱�����</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.base")%></td>
	</tr>
	<tr>
		<td>ÿ�����Ӵ߱�����</td>
		<td><%=Config.getString("cn.com.youtong.apollo.notify.increment")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr>
		<td>�ʼ������ļ�</td>
		<td><%=Config.getString("email.outputfile")%></td>
	</tr>
	<tr>
		<td>���Ż����ļ�</td>
		<td><%=Config.getString("sms.outputfile")%></td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<% // �߱����񣬿��ܴ��ڶ��������Ҫ�߱�
	String[] tasks = Config.getStringArray("cn.com.youtong.apollo.notify.task");

	if (tasks != null) {
		for (int i=0; i<tasks.length; i++) {
			String taskID = tasks[i];
			String[] taskNotifies = Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".notifiers");

			// ���û��ָ���߱���ʽ��ȱʡʹ�����еĴ߱���ʽ
			if (taskNotifies == null) {
				taskNotifies = notifies;
			}
			out.println("<tr><td>����" + taskID + "�߱���ʽ</td>");
			out.print("<td>");

			printArray(out, taskNotifies);
			out.println("</td></tr>");

			// ����߱��ĵ�λdomain
			String[] domains = Config.getStringArray("cn.com.youtong.apollo.notify." + taskID + ".domain");

			for (int j=0; j<domains.length; j++) {
				String domain = domains[j];
				out.println("<tr><td>����" + taskID + " domain " + domain + "</td>");
				int depth = Config.getInt("cn.com.youtong.apollo.notify." + taskID + "." + domain + ".depth", 1);
				out.println("<td>" + depth + "</td></tr>");
			}
			out.println("<tr><td colspan='2'></td></tr>");
		}
	}
	%>
	<% } // End else (���û�����ô߱���ʽ����) %>

	<tr>
		<th colspan="2">��������</th>
	</tr>
	<tr>
		<td>ͼ���ļ����Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.chart.directory")%></td>
	</tr>
	<tr>
		<td>excel�ļ����Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.excel.directory")%></td>
	</tr>
	<tr>
		<td>�ϱ�����fap�ļ�����Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.zipdata.directory")%></td>
	</tr><tr>
		<td>fap�ͷ�Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.extractdata.directory")%></td>
	</tr>
	<tr>
		<td>���ݱ���Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.backupdata.directory")%></td>
	</tr>
	<tr>
		<td>�������ݱ��ݵ�Ŀ¼</td>
		<td><%=Config.getString("cn.com.youtong.apollo.outoftimelimitdata.directory")%></td>
	</tr>
	<tr>
		<td>webҳ��ҳ��ʾ�е�ÿҳ����������</td>
		<td><%=Config.getString("cn.com.youtong.apollo.webconfig.pageNum")%></td>
	</tr>

    <tr><td>&nbsp;</td></tr>


</table>

<jsp:include page= "/jsp/footer.jsp" />
</body>
</html>



<%!
/**
  * ��ӡ���飬��������м���","��������ӡ��ϣ����λ����ʼ��
  * @param out   ��ӡĿ����
  * @param array Ҫ��ӡ��String������
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