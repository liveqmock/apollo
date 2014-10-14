<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="cn.com.youtong.apollo.data.help.*" %>
<%@ page import="cn.com.youtong.apollo.common.ParamUtils" %>

<%

    String contentType = request.getContentType();
    String unitID = "";
    String taskTimeID = "";
    if (contentType != null && contentType.startsWith("multipart/form-data;")) {
       unitID = (String)request.getAttribute("unitID");
       taskTimeID= (String)request.getAttribute("taskTimeID");
    }
    else {
      unitID = request.getParameter("unitID");
      taskTimeID = request.getParameter("taskTimeID");
    }
	
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/csslib/tab.webfx.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/csslib/webfxlayout.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/csslib/main.css">
<body>

<%
   ArrayList attachList=(ArrayList)request.getAttribute("attachlist");
%>
<form method="post" action="attachsave.jsp" enctype="multipart/form-data" name="frmattachments">
<input type="hidden"  name="taskTimeID" value="<%=taskTimeID%>">
<input type="hidden"  name="unitID" value="<%=unitID%>">
<input type="hidden"  name="attachID" >


<input  name="fileupload"  style="width:200" type="file"> 
<input  name="upload" style="height:20;width:60" type="button" onClick="uploadclick()" value="上传">
</form>

<table  width="100%" id="table1" class="xsltTable">
  <%for(int i=0;i<attachList.size();i++){
	  AttachModel attachModel=(AttachModel)attachList.get(i);
    %>
	<tr class="xsltTr">
		<td width="70%" align="center" class="clsCellStore">
		  <a onClick="seefile(<%=attachModel.getID()%>);" style="cursor:hand" title="下载" href="#"><%=attachModel.getFileName()%></a>
		</td>
	   <td width="30%" align="center" class="clsCellStore">
	   <a onClick="delattach(<%=attachModel.getID()%>);" style="cursor:hand" href="#">删除</a>
	   </td>
	</tr>
<%}%>
</table>


<SCRIPT LANGUAGE=javascript>
<!--
   
   function seefile(id)
   {
       
        window.open("<%=request.getContextPath()%>/servlet/model"+ "?operation=<%=ModelServlet.SHOW_ATTACH%>&attachID="+id);
	   
   }

 function delattach(id)
   {
         frmattachments.attachID.value=id;
        frmattachments.action="<%=request.getContextPath()%>/servlet/model"+ "?operation=<%=ModelServlet.DELETE_ATTACH%>";
	     frmattachments.submit();
   }

  

   function uploadclick()
   {
	
	if(frmattachments.taskTimeID.value=="-1"&&frmattachments.unitID.value=="null")
	   {
		 alert("请选择单位和时间");
		 return;
	   }


	var namevalue = new Array();
	var n,f,l;
	var str=frmattachments.fileupload.value;
	namevalue=str.split("\\");		
	n=namevalue.length;	
	str=namevalue[n-1];	
	f=str.indexOf(",");
	
	if (f>0)
	{
		alert("文件名不能含有逗号!");
		return;		
	}
	
	f=str.indexOf(".");
	l=str.lastIndexOf(".");
	
	if (f!=l)
	{
		alert("文件名不能含有1个以上的.号!");
		return;
	}
	
	if (f<0)
	{
		alert("文件不能没有扩展名!");
		return;
	}
	
    if(frmattachments.fileupload.value=="")
      {
      alert("上载的文件为空");
      return;
      }
      
     if(frmattachments.fileupload.value!="")
      {
         flag=true;
         frmattachments.action="<%=request.getContextPath()%>/servlet/model"+ "?operation=<%=ModelServlet.UPLOAD_ATTACH%>";
	     frmattachments.submit();
      }
   
   
   }
</SCRIPT>
</body>
</html>