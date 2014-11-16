<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="java.util.*" %>
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
<title>封面表信息管理</title>
</head>
<script language="JavaScript">
//选择单位树，得到单位的催报信息
function changeUnit(unitID,unitName){
     thisForm.action = "model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.GET_UNITMETA_INFO_BYTASKID%>";
     thisForm.taskID.value = '<%=(String)request.getSession().getAttribute("taskID")%>';
     thisForm.unitID.value = unitID;
     thisForm.unitName.value = unitName;
     thisForm.submit();
}
</script>
<body>


<form name="thisForm" method="post" style="margin:0" target="addressInfo">
<input type="hidden" name="taskID">
<input type="hidden" name="unitID">
<input type="hidden" name="unitName">
  <table width=99% height=99% bordercolordark="white" bordercolorlight="black">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=2 valign="middle">
            任务管理-><%=(String)request.getSession().getAttribute("taskName")%>-><font class="clsLightColorOfTask">封面表管理</font>
         </td>
       </tr>
                <tr >
                     <td height=100% width=28%><div class="clsTreeDiv"><%=(String)request.getAttribute("unitTree")%></div></td>
                     <td height=100%>
                         <table  valign=top width=100% height=100% bordercolordark="white" bordercolorlight="black">
                            <tr class="clsTrContext">
                              <td>
                              	 <h1>&lt;-点击左侧单位名称，维护封面表信息</h1>
                                 <iframe name="addressInfo" width=100% FRAMEBORDER=0 height=100%></iframe>
                             </td>
                            </tr>
                         </table>
                      </td>
                </tr>
      </table>
</form>
</body>
</html>
