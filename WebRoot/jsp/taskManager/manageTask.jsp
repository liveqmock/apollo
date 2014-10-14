<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<STYLE TYPE='text/css'>
.parent {visibility:visible;MARGIN: 0px;BACKGROUND-COLOR: #f2f2f0}
.child {display:none;BACKGROUND-COLOR: #f2f2f0;margin-top:5}
.clsTaskMenu{
    BACKGROUND-COLOR: #e7e7e4;
    BORDER-right: 1 solid #c9cfe9;
    BORDER-left: 1 solid #c9cfe9;
    margin:0;
    OVERFLOW: auto;
    WIDTH: 98%;
    HEIGHT: 100%
}
.clsMain{
             FONT-SIZE: 9pt;margin:0;
             COLOR: black
}
.clsTop{
             FONT-SIZFDFDE: 9pt;BACKGROUND-COLOR: #e7e7e4;
             COLOR: black;margin:0
}
.clsChild{
             margin:0;FONT-SIZE: 9pt;COLOR: blue

}
A:hover{text-decoration:underline;color: black}
</STYLE>
<title>任务管理</title>
</head>
<script language="JavaScript">
function initIt(){
        divColl = document.all.tags("DIV");
        for (var i=0; i<divColl.length; i++) {
            whichEl = divColl(i);
            if (whichEl.className == "child") whichEl.style.display = "block";
        }

}
function expandIt(el,name,id) {
        whichEl = eval(el + "Child");
        if (whichEl.style.display == "none") {
            whichEl.style.display = "block";
        }
        else {
            whichEl.style.display = "none";
        }
}

var menuOn = "";
var blank = "&nbsp;&nbsp;&nbsp;";
var table_head = "<table border=0 width=100% style='margin:0' cellspacing=0 cellpadding=0><tr height=25 style='BACKGROUND-COLOR: #f2f2f0'><td style='BORDER-top: 1 solid #b1b1a8'>"
var table_middle = "</td></tr><tr style='BACKGROUND-COLOR: #f2f2f0;color:black'><td>";
var table_end = "</td></tr></table>"

</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

<table class="clsContentTable" border=0 cellpadding=0>
<tr>
  <td width=14%>
    <div class="clsTaskMenu" align=center>


       <div class="clsTop">
          <table border=0 width=100%><tr height=25><td align="center">
            <font id='arrowHead' color='#e7e7e4'>→</font><a href="javascript:showPublishTaskPage()" style="color:blue">设置任务</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          </td></tr></table>
       </div>



<script language="javascript">
    <%  Iterator taskItr = (Iterator)request.getAttribute("taskItr");
        int i = 1;
        while(taskItr.hasNext()){
          Task task = (Task)taskItr.next();
    %>

//document.write('<hr NOSHADE size=1 style="margin:0">');
          document.write(table_head+"<div id='p" + "<%=String.valueOf(i)%>" + "Parent'" + " class='parent'>");
          document.write("&nbsp;&nbsp;&nbsp;"+"<a class='clsMain' href='#' onClick=\"expandIt("+"'p<%=String.valueOf(i)%>'"+"); return false\">"+menuOn+"<%=task.getName()%>"+"</a>"+table_middle);
          document.write("<div id='p" + "<%=String.valueOf(i)%>" + "Child'" + " class='child'>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>1'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:addressInfoManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">催报管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>2'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:unitManger("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">单位管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>3'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:permissionManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">权限管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>4'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:scriptManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">脚本管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>7'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:templateManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">模板管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>8'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:paramManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">参数管理</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>5'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:xsltManger("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">表样发布</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>6'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:initPermissionManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">初始化权限</a><br>");
          document.write(blank+"<font id='arrow"+"<%=String.valueOf(i)%>9'"+" color='#f2f2f0'>→</font>"+"<a class='clsChild' href=\"javascript:newsManager("+"'<%=task.getName()%>'"+",'<%=task.id()%>'"+",'<%=String.valueOf(i)%>'"+")\">信息管理</a></div>"+table_end);

       　　　
    <%i++;}%>
</script>　
   </div>

  </td>
  <td>
       <iframe id="taskFrame" width=100% FRAMEBORDER=0 height=100% scrolling=auto></iframe>
   </td>

</tr>
</table>




  <jsp:include page= "/jsp/footer.jsp" />
<script language="javascript">
var arrowNum = 10;

function setArrow(arrow,index){
   document.getElementById("arrowHead").color = "#e7e7e4";
   for(var i=1;i<<%=i%>;i++){
       for(var j=1;j<arrowNum;j++){
         document.getElementById("arrow"+i+j).color = "#f2f2f0";
       }
   }
   document.getElementById("arrow"+index+arrow).color = "darkorange";
}
function setArrowHead(){
if(<%=i%>>1){
   for(var i=1;i<<%=i%>;i++){
       for(var j=1;j<arrowNum;j++){
         document.getElementById("arrow"+i+j).color = "#f2f2f0";
       }
   }
}
   document.getElementById("arrowHead").color = "darkorange";
}
function showPublishTaskPage(){
 setArrowHead()
 taskFrame.location = "../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_PUBLISHTASK_PAGE %>";
}
function scriptManager(taskName,taskID,index){
   setArrow(4,index);
   url = "../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_MANAGER_SCRIPT_PAGE %>";
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName)+"&flag="+true,"_self";
   taskFrame.location = encodeURI(url);
}
function initPermissionManager(taskName, taskID, index)
{
   setArrow(6,index);
   url = "../servlet/unitPermission?operation=<%=cn.com.youtong.apollo.servlet.UnitPermissionServlet.SHOW_INIT_PERMISSION%>";
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName)+"&flag="+true,"_self";
   taskFrame.location = encodeURI(url);
}

function newsManager(taskName, taskID, index)
{
   setArrow(9,index);
   url = "../news/shownewss.jsp?taskID="+taskID;
   taskFrame.location = encodeURI(url);
}

function addressInfoManager(taskName,taskID,index){
   setArrow(1,index);
   url = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_ADDRESS_INFO_PAGE%>";
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);
   taskFrame.location = encodeURI(url);
}

function xsltManger(taskName,taskID,index)
{
   setArrow(5,index);
   url = '../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_MANAGE_TASKVIEW_PAGE %>';
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);
   taskFrame.location = encodeURI(url);
}

function permissionManager(taskName,taskID,index)
{
   setArrow(3,index);
   url = '../servlet/unitPermission?operation=<%=cn.com.youtong.apollo.servlet.UnitPermissionServlet.SHOW_UNIT_INFO%>';
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);
   taskFrame.location = encodeURI(url);
}

function unitManger(taskName,taskID,index)
{
   setArrow(2,index);
   url = '../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_MANAGE_UNIT_PAGE%>';
   url += "&managerTaskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);
   taskFrame.location = encodeURI(url);
}

function paramManager( taskName, taskID, index)
{
	setArrow( 8, index );

	url = '../servlet/analyse?operation=<%=cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_MANAGE_PARAM_PAGE%>';
	url += "&taskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);

   	taskFrame.location = encodeURI(url);
}

function templateManager( taskName, taskID, index)
{
	setArrow( 7, index );

	url = '../servlet/analyse?operation=<%=cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_MANAGE_SCALAR_QUERY_TEMPLATE_PAGE%>';
	url += "&taskID="+taskID+"&managerTaskName="+encodeURIComponent(taskName);

   	taskFrame.location = encodeURI(url);
}

//初试化子菜单div
initIt();
//条件显示发布任务页面
if("<%=(String)request.getAttribute("taskFlag")%>"=="true"){
   showPublishTaskPage();
}
</script>
</body>
</html>
