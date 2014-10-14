<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.servlet.UnitPermissionServlet"%>
<%@ page import="cn.com.youtong.apollo.task.*"%>
<%@ page import="cn.com.youtong.apollo.data.*"%>
<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>�༭�û�</title>
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
</head>
<script language="JavaScript">
<%String taskID = (String)request.getSession().getAttribute("taskID");%>
//������
var clearFlag=false;
function addGroup()
{
  window.open("unitPermission?operation=<%=UnitPermissionServlet.SHOW_UNIT_ASSIGNMENTINFO%>","_self");
}
function deleteGroup()
{
  window.open("unitPermission?operation=<%=UnitPermissionServlet.DELETE_UNIT_PERMISSION%>&GroupID="+thisForm.groupID.value,"_self");
}

//ѡ���������ƣ��޸ĵ�λ��
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

//ѡ��λ�����޸���
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
	//�ύ��д����
	if(document.thisForm.unitRightRead.checked==true&&document.thisForm.unitRightWrite.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
   
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightRead="+thisForm.unitRightRead.value+"&unitRightWrite="+thisForm.unitRightWrite.value,"frmGroup";
		return;
	}

	//�ύ������
	if(document.thisForm.unitRightRead.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightRead="+thisForm.unitRightRead.value,"frmGroup";
	}

	//�ύд����
	if(document.thisForm.unitRightWrite.checked==true)
	{
		if(frmGroup.thisForm.groupID.value=="")
		{
			return;
		}
		frmGroup.location = "unitPermission?operation=<%=UnitPermissionServlet.UNIT_ASSIGN_PERMISSION%>&taskID=" +'<%=taskID%>'+"&unitID="+frmGroup.thisForm.unitID.value+"&groupID="+frmGroup.thisForm.groupID.value+"&unitRightWrite="+thisForm.unitRightWrite.value,"frmGroup";
	}

	//�ύ��д����,�����Ϊfalse����ɾ������
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
			  <td align=left colspan=2><img src="../img/infomation.bmp">&nbsp;ѡ����൥λ������Ȩ��.</td>
			</tr>
			<tr>
			   <td width=60% >
				 <iframe id="frmGroup" SCROLLING=no height=100% frameborder="0" bordercolordark="white" bordercolorlight="black" width="100%"></iframe>
			   </td>
			   <td width=40% valign="top" align="left"><br><br>
				  <input type="checkbox" style="border: 0px" name="unitRightRead" style="width:50px" value="<%=UnitPermission.UNIT_PERMISSION_READ%>" disabled><%=unitPermission.getPermissionName(UnitPermission.UNIT_PERMISSION_READ)%>
				  <br><input type="checkbox" style="border: 0px" name="unitRightWrite" style="width:50px" value="<%=UnitPermission.UNIT_PERMISSION_WRITE%>" disabled><%=unitPermission.getPermissionName(UnitPermission.UNIT_PERMISSION_WRITE)%>
				  <br><br>&nbsp;&nbsp;<button onmouseover="this.className='OnButton'" onmouseout="this.className='OutButton'" onclick="submit_onclick()" id="button1" name="btnChange" disabled>�ı�Ȩ��</button>
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