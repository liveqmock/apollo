<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.services.Config"%>
<%@ page import="cn.com.youtong.apollo.servlet.TaskServlet"%>
<html>
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/function.js"></script>
<style type="text/css">
.xsltTr
{
    BORDER-RIGHT: black 1pt solid;
    PADDING-RIGHT: 1pt;
    BORDER-TOP: black 1pt solid;
    PADDING-LEFT: 1pt;
    PADDING-BOTTOM: 1pt;
    BORDER-LEFT: black 1pt solid;
    PADDING-TOP: 1pt;
    BORDER-BOTTOM: black 1pt solid;
    BACKGROUND-COLOR: #fbfbfb
}
</style>
<body style="BACKGROUND-COLOR: #fbfbfb">
<form name="form1" action="task?operation=<%=TaskServlet.UPLOAD_SETUP%>" method="post">
<input type="hidden" name="uploadType" value="<%=(String)request.getAttribute("uploadType")%>">
	<table id="setupTbl" width="100%" height="100%" border=0>
<%

        String userName = Config.getString("cn.com.youtong.apollo.upload.user");
        String password = Config.getString("cn.com.youtong.apollo.upload.password");
	String wsURL=Config.getString("cn.com.youtong.apollo.upload.webservice.url");
	String email=Config.getString("cn.com.youtong.apollo.upload.mail.to");
	if(request.getAttribute("uploadType").equals("webservice")){
%>
		<tr class="xsltTr">
			<td  align="center" width=30%>
				直报地址:
			</td>
			<td id="wsURL">
				<input type="text" name="wsURL" value="<%=wsURL%>" size=33>
			</td>
		</tr>
<%	}else if(request.getAttribute("uploadType").equals("email")){
%>
		<tr class="xsltTr">
			<td  align="center" width=30%>
				上级邮件地址:
			</td>
			<td id="email">
				<input type="text" name="email" value="<%=email%>" size=33>
			</td>
		</tr>

<%	}%>
		<tr class="xsltTr">
			<td align="center" width=30%>
				用户名:
			</td>
			<td>
				<input type="text" name="userName" value="<%=userName%>">
			</td>
		</tr>
		<tr class="xsltTr">
			<td align="center" width=30%>
				密码:
			</td>
			<td>
				<input type="text" name="password" value="<%=password%>">
			</td>
		</tr>
		<tr class="xsltTr">
			<td>

			</td>
			<td>
				<input type="submit" value="更新配置" onclick="return checkValue()">
			</td>
		</tr>
	</table>
</form>
<script language="javascript">
function checkValue(){
	var tdName = document.all("setupTbl").cells(1).id;
    	var tdContext = document.all("setupTbl").cells(0).innerText;
	if(isNull(trim(eval("form1."+tdName+".value")))){
		alert(tdContext+"不能为空!");
		eval("form1."+tdName).focus();
		return false;
	}
	if(isNull(trim(form1.userName.value))){
		alert("用户名不能为空!");
		form1.userName.focus();
		return false;
	}
	if(isNull(trim(form1.password.value))){
		alert("密码不能为空!");
		form1.password.focus();
		return false;
	}
	var value = trim(eval("form1."+tdName+".value"));
        if((tdName=="wsURL" && value=='<%=wsURL%>') || (tdName=="email" && value=='<%=email%>')){
		if(trim(form1.userName.value)=='<%=userName%>' && trim(form1.password.value)=='<%=password%>'){
			alert("值未改变，不需要更新!");
			return false;
		}
        }
	return true;

}
if(<%=(String)request.getAttribute("successFlag")%>==true){
	alert("系统配置更新成功!");
}
</script>
</body>
</html>