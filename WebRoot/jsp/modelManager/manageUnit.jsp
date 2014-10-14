<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<script type="text/javascript" src="../jslib/calendar/popcalendar.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_layers.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_menu.js"></script>
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<title>单位管理</title>
</head>
<script language="JavaScript">

//选择单位，得到单位信息
function changeUnit(unitID,unitName)
{
     thisForm.unitID.value = unitID;
     thisForm.action = "model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_UNIT_INFO_PAGE%>";
     thisForm.submit();
}

function onModifyDisplay(){
  var result = getCheckedValues();
  thisForm.action="model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.MODIFY_UNIT_DISPLAY%>&result="+result;
  thisForm.submit();
}
</script>
<body scroll=no>


<form name="thisForm" method="post" style="margin:0" target="unitInfo">
<input type="hidden" name="unitID">
</form>
  <table width=100% height=99% bordercolordark="white" cellpadding=0 cellspacing=0 bordercolorlight="black">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black"  colspan=2 valign="middle">
            任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">单位管理</font>
         </td>
		 <td nowrap style="BACKGROUND-COLOR: #c7c9b1;color:black" class="clsLightColorOfTask">
			<a style="cursor:hand" onClick ="window.open('model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.PRINT_UNITTREE%>&taskID=<%=request.getParameter("managerTaskID")%>')")>打印单位树</a>
         </td>
       </tr>
       </tr>
                <tr >
                     <td height=100% width=28%>
                      <div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%><!-- input type="button" value="确定" onclick="javascript:onModifyDisplay();">--></div>
                     </td>
                     <td height=100%>
                         <table  valign=top width=100% height=100% bordercolordark="white" bordercolorlight="black">
                            <tr class="clsTrContext">
                              <td >
                                 <iframe id="unitInfo" name="unitInfo" width=100% FRAMEBORDER=0 height=100%></iframe>
                             </td>
                            </tr>
                         </table>
                      </td>
                </tr>
      </table>

<script language="javascript">
<%
UnitTreeNode unit = (UnitTreeNode)request.getAttribute("unit");
if(unit != null)
{
%>
     thisForm.unitID.value = "<%= unit.id() %>";
     thisForm.action = "model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_UNIT_INFO_PAGE%>";
     thisForm.submit();
<%
} else {
%>
     unitInfo.location = "model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_UNIT_INFO_PAGE%>";
<%
}
%>
</script>
</body>
</html>
