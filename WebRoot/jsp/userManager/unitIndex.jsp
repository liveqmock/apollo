<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="cn.com.youtong.apollo.task.*"%>
<%@ page import="cn.com.youtong.apollo.data.*"%>
<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>编辑用户</title>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
</head>
<script language="JavaScript">
<%String taskID = (String)request.getSession().getAttribute("taskID");%>
//清除标记
var clearFlag=false;
function addGroup()
{
  window.open("unitPermission?operation=<%=UnitPermissionServlet.SHOW_UNIT_ASSIGNMENTINFO%>","_self");
}
function deleteGroup()
{
  window.open("unitPermission?operation=<%=UnitPermissionServlet.DELETE_UNIT_PERMISSION%>&GroupID="+thisForm.groupID.value,"_self");
}

//选择任务名称，修改单位树
function seltask_onchange()
{
	if(clearFlag)
	{
		frmGroup.clearPage();
		thisForm.btnChange.disabled=true;
		thisForm.unitRightRead.disabled=true;
		thisForm.unitRightRead.checked=false;
		thisForm.unitRightWrite.disabled=true;
		thisForm.unitRightWrite.checked=false;
		clearFlag = false;
	}
}

//选择单位树，修改组
function changeUnit(unitID,unitName)
{
	var put=document.all.tags("input");
	var button=document.all("btnChange");
	button.disabled=true;
	for (var i=0;i<put.length;i++)
	{
		if (put[i].type=="checkbox")
		{
			put[i].disabled=true;
			put[i].checked=false;
		}
	}

	var url = "unitPermission?operation=<%=UnitPermissionServlet.CHANGE_UNIT%>&taskID=" +'<%=taskID%>'+"&unitID="+unitID+"&unitName="+encodeURIComponent(unitName);
	frmGroup.location = encodeURI(url);
	clearFlag = true;
}

function submit_onclick()
{
	//提交读写操作
	if(document.thisForm.unitRightRead.checked==true&&document.thisForm.unitRightWrite.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
   
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightRead="+thisForm.unitRightRead.value+"&unitRightWrite="+thisForm.unitRightWrite.value,"frmGroup";
		return;
	}

	//提交读操作
	if(document.thisForm.unitRightRead.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightRead="+thisForm.unitRightRead.value,"frmGroup";
	}

	//提交写操作
	if(document.thisForm.unitRightWrite.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightWrite="+thisForm.unitRightWrite.value,"frmGroup";
	}

	//提交读写操作,如果都为false，则删除该组
	if(document.thisForm.unitRightRead.checked==false&&document.thisForm.unitRightWrite.checked==false)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.DELETE_UNIT_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightRead="+thisForm.unitRightRead.value,"frmGroup";
	}
}
</script>

<body>

<%
   UnitPermission unitPermission=new UnitPermission();
%>

<table class="clsContentTable">
  <form name="thisForm">
   <tr>
	   <td width=30%>
		  <div class="clsTreeDiv"><%=(String)request.getAttribute("unitTree")%></div>
	   </td>
	   <td width=70%>
		  <table height=100% width=100% border=0>
			<tr height=5 class="clsTrHeader">
			  <td align=left colspan=2><img src="../img/infomation.bmp">&nbsp;选择左侧单位树管理权限.</td>
			</tr>
			<tr>
			   <td width=60% >
				 <iframe id="frmGroup" SCROLLING=no height=100% frameborder="0" bordercolordark="white" bordercolorlight="black" width="100%"></iframe>
			   </td>
			   <td width=40% valign="top" align="left"><br><br>
				  <input type="checkbox" style="border: 0px" name="unitRightRead" style="width:50px" value="<%=UnitPermission.UNIT_PERMISSION_READ%>" disabled><%=unitPermission.getPermissionName(UnitPermission.UNIT_PERMISSION_READ)%>
				  <br><input type="checkbox" style="border: 0px" name="unitRightWrite" style="width:50px" value="<%=UnitPermission.UNIT_PERMISSION_WRITE%>" disabled><%=unitPermission.getPermissionName(UnitPermission.UNIT_PERMISSION_WRITE)%>
				  <br><br>&nbsp;&nbsp;<button onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="submit_onclick()" id="button1" name="btnChange" disabled>改变权限</button>
			   </td>
			</tr>
		  </table>
	   </td>
	</tr>
	</form>
</table>

<script language="javascript">
	seltask_onchange();
</script>
</body>
</html>