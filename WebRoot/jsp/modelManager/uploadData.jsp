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
<title>���ϼ��������ϱ�����</title>
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
        alert("��ѡ��λ");
        return false;
    }
    form1.unitID.value = unitID;
//�±�---------------------
<%Task task = (Task)request.getAttribute("task");
  if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("��ѡ��ʱ��");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
//�ձ�----------------
<%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
  if(document.getElementById('timeOfTask').value==""){
    alert("��ѡ��ʱ��");
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
       alert(document.getElementById('timeOfTask').value+"û������,������ѡ��һ������!");
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
            ���ݴ���-><font class="clsLightColorOfTask">���ϼ��������ϱ�����</font>
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
                    ���ϼ��������ϱ�����
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td colspan=2 align="left">
	1��ѡ��λ��ȱʡ��������е�λ��<br>
	2��ѡ������ʱ�䡣ȱʡ����ǵ�ǰ�����ϱ���ʱ�䣬�±�������һ���£�<br>
	3��ѡ���ϱ���ʽ��Ŀǰ֧�ֵ����ʼ���FTP��webservice��<br>
	4��Ҫ���û������û��������룬�û������������ϼ��������������ݵ���֤ƾ�ݣ�<br>
	5����Ϣ��������ʾ��������ǰ�ϱ��������������ϼ��������Ƿ�������ɵ���������Ƿ���ɡ�<br>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="center" width="25%">����ʱ��:</td>
                   <td><jsp:include page= "/jsp/modelManager/periodOfTask.jsp" /></td>
               </tr>
               <tr class="xsltTr">
                   <td align="center">�ϱ���ʽ:</td>
                   <td>
		     <input type="radio" name="uploadSelect" value="webservice" onclick="netSetup()" checked>����ֱ��
		     <input type="radio" name="uploadSelect" value="mail" onclick="mailSetup()">�ʼ��ϱ�
                  </td>
               </tr>
               <tr class="xsltTr" height=50%>
                   <td align="center">����:</td>
                   <td>
			<iframe name="setupFrm" width="100%" FRAMEBORDER=0 height=100% scrolling=no></iframe>
                   </td>
               </tr>
               <tr class="xsltTr">
                   <td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<input type="button" value="�ϱ�����" onclick="sendData2Server()"></td>
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
	alert("�ϱ����ݳɹ�!");
}
</script>
</body>
</html>
