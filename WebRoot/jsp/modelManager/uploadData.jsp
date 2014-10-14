<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<title>向上级服务器上报数据</title>
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
</head>
<script language="JavaScript">
function sendData2Server()
{
    var unitID = getCheckedValues();
    if(unitID == "")
    {
        alert("请选择单位");
        return false;
    }
    form1.unitID.value = unitID;
//月报---------------------
<%Task task = (Task)request.getAttribute("task");
  if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("请选择时间");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
//日报----------------
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
  if(document.getElementById('timeOfTask').value==""){
    alert("请选择时间");
    return;
  }
  var flag=false;
  for(var i=0;i<dateInfos.length;i++){
    if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
       form1.taskTimeID.value=dateInfos[i][0];
       flag = true;
    }
  }
  if(!flag){
       alert(document.getElementById('timeOfTask').value+"没有数据,请重新选择一个日期!");
       document.getElementById('timeOfTask').value = "";
       return;
    }
<%}%>

    form1.submit();
}
</script>
<body>




<table align="center" height="100%" border="0" width="99%">
<form action="task?operation=<%= TaskServlet.UPLOAD_DATA_TO_SERVER %>" method="post" name="form1">
<input type="hidden" name="unitID">
<input type="hidden" name="taskTimeID">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            数据传送-><font class="clsLightColorOfTask">向上级服务器上报数据</font>
         </td>
       </tr>
<tr>
    <td width="27%">
       <div class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
    </td>
    <td valign="middle">
           <table height=90% width=90% border=1 style="margin-left:1cm" class="xsltTable">
               <tr class="xsltTr">
                   <td colspan=2 align="center">
                    向上级服务器上报数据
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td colspan=2 align="left">
	1，选择单位。缺省情况是所有单位；<br>
	2，选择任务时间。缺省情况是当前合适上报的时间，月报就是上一个月；<br>
	3，选择上报方式。目前支持电子邮件，FTP，webservice；<br>
	4，要求用户输入用户名和密码，用户名和密码是上级服务器接受数据的验证凭据；<br>
	5，信息反馈。提示操作（当前上报操作，而不是上级服务器是否真正完成导入操作）是否完成。<br>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center" width="25%">任务时间:</td>
                   <td><jsp:include page= "/jsp/modelManager/periodOfTask.jsp" /></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">上报方式:</td>
                   <td>
		     <input type="radio" name="uploadSelect" value="webservice" onclick="netSetup()" checked>网络直报
		     <input type="radio" name="uploadSelect" value="mail" onclick="mailSetup()">邮件上报
                  </td>
               </tr>
               <tr class="xsltTr" height=50%>
                   <td align="center">配置:</td>
                   <td>
			<iframe name="setupFrm" width="100%" FRAMEBORDER=0 height=100% scrolling=no></iframe>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<input type="button" value="上报数据" onclick="sendData2Server()"></td>
               </tr>
          </table>


    </td>
</tr>
</form>
</table>
<script language="javascript">
function netSetup(){
	setupFrm.location = "task?operation=<%=TaskServlet.UPLOAD_SETUP%>"+"&uploadType="+"webservice";
}
function mailSetup(){
	setupFrm.location = "task?operation=<%=TaskServlet.UPLOAD_SETUP%>"+"&uploadType="+"email";
}
netSetup();
if(<%=(String)request.getAttribute("successflag")%>==true){
	alert("上报数据成功!");
}
</script>
</body>
</html>
