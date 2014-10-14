<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<link href="../csslib/reportmanage.css" rel="stylesheet" type="text/css">
<title>报表管理</title>
</head>
<%
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
  String tid = request.getParameter("tid");
  if(tid==null) tid="";
%>
<script language="JavaScript">
	function _reportManager()
	{
		reportframe.location.replace('../do/report?action=reportlist');
	}
	function _publishManager()
	{
		reportframe.location.replace('../do/report?action=reportpublish');
	}
	function _deleteManager()
	{
	        if(reportform.tid.value=="") {alert('没有选择模板');return;}
		reportform.target= "_self";
		reportform.action = '../do/report?action=delete';
		reportform.submit();
	}
</script>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
  <div style="width:100%;height:expression(body.clientHeight-120)">
  <table cellpadding=0 cellspacing=0 border=0 style="width:100%;height:100%">
	<tr>
		<td style="width:220px" class="bglefttoolbar">
			<table cellspacing=0 cellpadding=5px border=0 style="width:100%;height:100%">
				<tr>
					<td style="height:150px">
						<div style="width:100%;height:100%;border-style:solid;border-width:1px;border-color:white">
						<table cellspacing=0 cellpadding=0 border=0 style="width:100%;height:100%;border-collapse:collapse;">
							<tr style="height:20px">
								<td style="font-size:12px;color:blue;font-weight:bolder;background-color:#aaaae2">&nbsp;功能选项
								</td>
							</tr>
							<tr>
								<td>
									<table cellspacing=2 cellpadding=0 border=0 style="width:100%;height:100%">
										<tr style="height:20px">
											<td width="10px">&nbsp;</td>
											<td width="20px"><img src="../img/audit.gif">
											</td>
											<td style="color:blue;cursor:hand" onclick="javascript:_reportManager();">报表查看
											</td>
										</tr>
										<tr style="height:20px">
											<td>&nbsp;</td>
											<td><img src="../img/audit.gif">
											</td>
											<td style="color:blue;cursor:hand" onclick="javascript:_publishManager();">报表发布
											</td>
										</tr>
										<tr style="height:20px">
											<td>&nbsp;</td>
											<td><img src="../img/audit.gif">
											</td>
											<td style="color:blue;cursor:hand" onclick="javascript:_deleteManager();">删除报表
											</td>
										</tr>
										<tr>
											<td colspan=3>&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div style="width:100%;height:100%;border-style:solid;border-width:1px;border-color:white;background-color:#aaaae2">
						<table cellspacing=0 cellpadding=0 border=0 style="width:100%;height:100%;border-collapse:collapse;">
							<tr style="height:20px">
								<td id="optionid" style="font-size:12px;color:blue;font-weight:bolder">&nbsp;报表预览
								</td>
							</tr>
							<tr>
								<td id="contentid">
								</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>
		</td>
		<td><iframe width="100%" height="100%" frameborder="0" name="reportframe" src="../do/report?action=view" scrolling="auto"></iframe></td>
	</tr>
  </table>
  </div>
  <form name=reportform target=reportframe method=post>
	<input type=hidden name=tid value="<%=tid%>" />
  </form>
  <jsp:include page= "/jsp/footer.jsp" />
</body>
</html>
