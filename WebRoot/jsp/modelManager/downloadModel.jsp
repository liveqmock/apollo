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
<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />
<title>��������</title>
</head>
<script language="JavaScript">
function downloadModel()
{
    var unitID = getCheckedValues();
    if(unitID == "")
    {
        alert("��ѡ��λ");
        return false;
    }
    form1.unitID.value = unitID;
<%Task task = (Task)request.getAttribute("task");
  if(task.getTaskType() == task.REPORT_OF_MONTH){%>
  if(form1.taskTimeIDSelect.value=="" || form1.taskTimeIDSelect.value=="-1")
  {
    alert("��ѡ��ʱ��");
    return;
  }
  form1.taskTimeID.value=form1.taskTimeIDSelect.value;
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
function submitForm(){}
</script>
<body>


<table align=center height=500 height=100% border=0>
   <form action="model?operation=<%= ModelServlet.DOWNLOAD_MODEL %>" method="post" name="form1">
     <input type="hidden" name="taskTimeID" value="">
       <tr height=26 class="clsTrHeader" >
         <td style="BACKGROUND-COLOR: #c7c9b1;color:black" width=100% colspan=8 valign="middle">
            ���ݴ���-><font class="clsLightColorOfTask">��������</font>
         </td>
       </tr>
          <tr height=5% class="clsTrContext">
                    <td width=66% align=center>
              ����ʱ��:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
                        &nbsp;&nbsp;<input type="checkbox" name="isRecursive" value="true"/>���������¼���λ����
&nbsp;&nbsp;&nbsp;
              <button value="���ر�������" onclick="downloadModel()">��������</button>&nbsp;&nbsp;&nbsp;</td>
          </tr>
        <tr height=95% class="clsTrContext">

            <td align="center" width=100% colspan=2>
              <input type="hidden" name="unitID" value=""/>
              <table height=100% width=54%>
                <tr><td align="">
                   <div class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
                </td></tr>
              </table>
            </td>
        </tr>
</form>
</table>

</body>
</html>
