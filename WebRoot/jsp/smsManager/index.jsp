<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="java.util.*" %>

<%
//防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/csslib/main.css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<title>数据传送</title>
<STYLE TYPE='text/css'>
   A:hover{text-decoration:underline}
.clsDataMenu{
    BACKGROUND-COLOR: #e7e7e4;
    BORDER-right: 1 solid #c9cfe9;
    BORDER-left: 1 solid #c9cfe9;
}
</STYLE>
</head>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />


<table style="height:82%;width=100%" border=0 cellpadding=0>
<tr>
  <td width=14% valign=top class="clsDataMenu">
          <table border=0 width=100% cellpadding=4 style="border-collapse: collapse">
            <tr height=30>
              <td align="left">
                &nbsp;&nbsp;&nbsp;短信传送
              </td>
            </tr>
            <tr>
              <td align="left" style='BACKGROUND-COLOR: #f2f2f0'>
                &nbsp;<font id='one' color='#f2f2f0'>→</font><a href="javascript:fillStateCondition()" style="color:blue">短信发送</a>&nbsp;&nbsp;
              </td>
            </tr>
            <tr>
              <td align="left" style='BACKGROUND-COLOR: #f2f2f0'>
                &nbsp;<font id='two' color='#f2f2f0'>→</font><a href="javascript:receivemsghistory()" style="color:blue">短信接收</a>&nbsp;&nbsp;
              </td>
            </tr>
           <tr>
              <td align="left" style='BACKGROUND-COLOR: #f2f2f0'>
                &nbsp;<font id='three' color='#f2f2f0'>→</font><a href="javascript:sendmesghistory()" style="color:blue">短信记录</a>&nbsp;&nbsp;
              </td>
            </tr>
      <!--  <tr>
              <td align="left" style='BACKGROUND-COLOR: #f2f2f0'>
                &nbsp;<font id='four' color='#f2f2f0'>→</font><a href="javascript:upload2Server()" style="color:blue">向服务器上报数据</a>&nbsp;&nbsp;
              </td>
            </tr>
     --> 
         </table>

    </td>

    <td height=100%>
         <iframe id="dataFrame" width=100% FRAMEBORDER=0 height=100% scrolling=yes></iframe>
     </td>


</table>
<jsp:include page= "/jsp/footer.jsp" />
<SCRIPT language="javascript">
function fillStateCondition()
{
   document.getElementById("dataFrame").src = '../servlet/smsoperation?operation=<%=cn.com.youtong.apollo.sms.servlet.SMSServlet.SHOW_SENDING_PAGE%>';
}
function receivemsghistory(){
  // setArrow("two");
  
   document.getElementById("dataFrame").src = '../servlet/smsoperation?operation=<%=cn.com.youtong.apollo.sms.servlet.SMSServlet.SHOW_RECEIVEMSGHISTORY%>';
}

function sendmesghistory()
{
 //  setArrow("three");
   document.getElementById("dataFrame").src  = '../servlet/smsoperation?operation=<%=cn.com.youtong.apollo.sms.servlet.SMSServlet.SHOW_SENDMESGHISTORY_PAGE %>';
}
function upload2Server(){
 //  setArrow("four");
  dataFrame.location = "../servlet/task?operation=<%=cn.com.youtong.apollo.servlet.TaskServlet.SHOW_UPLOADDATA_PAGE%>";
}
function setArrow(index){
  /* var oFonts = document.body.all.tags("FONT");
   for(var i=0;i<oFonts.length;i++){
         if(oFonts[i].innerText == "→"){
           oFonts[i].color = "#f2f2f0";
         }
   }
   document.getElementById(index).color = "#darkorange";
*/
}
fillStateCondition();
</SCRIPT>


</body>
</html>
