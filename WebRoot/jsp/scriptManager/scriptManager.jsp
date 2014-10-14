<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.tools.Java2xhtml" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>脚本管理</title>
</head>

<script language="JavaScript">
//发布脚本检查
function publishScript()
{
    if(form1.file.value == "")
    {
        alert("请选择要发布脚本参数文件！");
        return false;
    }
    return true;
}
//显示脚本组信息
function displayScript(obj){
 if(obj.value==""){
    return false;
 }
   var selectedTaskID = obj.options[obj.selectedIndex].value;
   url = "task?operation=<%= TaskServlet.GET_ALL_SCRIPTS%>"+"&scriptName="+encodeURIComponent(selectedTaskID)+"&taskID="+'<%=(String)request.getSession().getAttribute("taskID")%>',"_self";
   window.location = encodeURI(url);
}
//显示脚本组
function getAllScriptSuits(obj){
   window.location = "task?operation=<%= TaskServlet.SHOW_MANAGER_SCRIPT_PAGE%>"+"&taskID="+'<%=(String)request.getSession().getAttribute("taskID")%>',"_self";
}

</script>
<body>


<table width=100% border="0">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=3 valign="middle">
            任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">脚本管理</font>
         </td>
       </tr>
  <tr>
        <form action="task?operation=<%= TaskServlet.PUBLISH_SCRIPT_SUIT %>" method="post" enctype="multipart/form-data" name="form1">
              <td valign="middle" colspan=3>&nbsp;&nbsp;请选择需要发布的脚本文件：&nbsp;&nbsp;
                  <input type="file" name="file"/>
                 <input type="hidden" name="taskID" value='<%=(String)request.getSession().getAttribute("taskID")%>'>
                 <input type="submit" value="发布脚本" onclick="return publishScript()"/>
               </td>
        </form>

  </tr>
   <tr>
                           <td colspan=2 nowrap>
                                   <SELECT id=seclect1 onChange="displayScript(this);" >
                                   <%
                                       Iterator _ScriptSuits = (Iterator)request.getAttribute("ScriptSuits");
                                       if(!(_ScriptSuits==null)){
                                       for(int i=0; _ScriptSuits.hasNext(); i++)
                                       {
                                          ScriptSuit scriptSuit = (ScriptSuit)_ScriptSuits.next();
                                          String scriptName = (String)request.getAttribute("scriptName");
                                          if(scriptName!=null && scriptName.equals(scriptSuit.getName())){
                                   %>
                                   <OPTION VALUE='<%=scriptSuit.getName()%>' selected><%=scriptSuit.getName()%></OPTION>
                                   <%
                                       }else{
                                   %>
                                   <OPTION VALUE='<%=scriptSuit.getName()%>' ><%=scriptSuit.getName()%></OPTION>
                                   <%}}}%>
                                   </SELECT>
                          <input type=button onclick="activeScriptSuits()" name="activeScriptSuit" value="激活">
                          <input type=button onclick="deleteScriptSuits()" name="deleteScriptSuit" value="删除">

                    </td>
                          <td colspan=1 nowrap align="right">
                           <%
                             String strScriptName = Convertor.getHTMLString((String)request.getAttribute("activeScriptSuit"));
                             if(strScriptName!=""){
                            %>
                                <%="当前激活脚本:"+strScriptName%>&nbsp;&nbsp;
                            <%}%>
                          <a id="imgA" style="display:none" href="javascript:displayAll()"><img id="imgCtl" src="../img/down.gif" border="0" alt="展开/收缩"></a>

                          &nbsp;
                    </td>
   </tr>
</table>
               <%
                 if(request.getAttribute("scriptContents")!=null){
                   String[] scriptContents = (String[])request.getAttribute("scriptContents");
               %>
				   <%=scriptContents[0]%>
				   <%=scriptContents[1]%>
				   <%=scriptContents[2]%>
				   <%=scriptContents[3]%>
			<%
					}
			%>

<script language="javascript">

 if(document.getElementById("seclect1").value==""){
     document.getElementById("imgCtl").style.display = "none";
 }

function displayAll(){
//判断状态
var flag=false;
for(var i=1;i<5;i++){
   if(eval("document.all.advshow"+i).checked == true){
      flag = true;
   }
}
if(flag){
  for(var i=1;i<5;i++){
    eval("t" + i).style.display = "none";
    eval("document.all.advshow"+i).checked = false;
  }
  document.getElementById("imgCtl").src = "../img/up.gif";
}else{
  for(var i=1;i<5;i++){
    eval("t" + i).style.display = "";
    eval("document.all.advshow"+i).checked = true;
  }
  document.getElementById("imgCtl").src = "../img/down.gif";
}
}
//设置（展开/收缩）图标
function imgStatus(){
    var flag=false;
    for(var i=1;i<5;i++){
       if(eval("document.all.advshow"+i).checked == true){
          flag = true;
       }
    }
  if(flag){
    document.getElementById("imgCtl").src = "../img/down.gif";
  }else{
    document.getElementById("imgCtl").src = "../img/up.gif";
  }
}
function showadv1(){
    if (document.all.advshow1.checked == true) {
    t1.style.display = "";
    }else{
    t1.style.display = "none";
    }
    imgStatus();
}
function showadv2(){
    if (document.all.advshow2.checked == true) {
    t2.style.display = "";
    }else{
    t2.style.display = "none";
    }
    imgStatus();
}
function showadv3(){
    if (document.all.advshow3.checked == true) {
    t3.style.display = "";
    }else{
    t3.style.display = "none";
    }
    imgStatus();
}
function showadv4(){
    if (document.all.advshow4.checked == true) {
    t4.style.display = "";
    }else{
    t4.style.display = "none";
    }
    imgStatus();
}
//激活脚本组
function activeScriptSuits(){
 if(document.getElementById("seclect1").value==""){
    alert("请选择需要激活的脚本组！");
    return;
 }
   url = "task?operation=<%= TaskServlet.ACTIVE_SCRIPTSUITS%>";
   url += "&taskID="+'<%=(String)request.getSession().getAttribute("taskID")%>'+"&scriptSuitName="+encodeURIComponent(document.getElementById("seclect1").value)+"&flag="+true;
   window.location = encodeURI(url);
}
//删除脚本组
function deleteScriptSuits(){
 if(document.getElementById("seclect1").value==""){
    alert("请选择需要删除的脚本组！");
    return;
 }
   if(confirm("确认要删除<"+document.getElementById("seclect1").value+">脚本组吗？")){
     url = "task?operation=<%= TaskServlet.DELETE_SCRIPT_SUIT%>"+"&taskID="+'<%=(String)request.getSession().getAttribute("taskID")%>'+"&scriptSuitName="+encodeURIComponent(document.getElementById("seclect1").value)+"&flag="+true;
     window.location = encodeURI(url);
   }
}
 if(<%=request.getAttribute("flag")%>!=null){
   displayScript(document.getElementById("seclect1"));
}
</script>

</body>
</html>
