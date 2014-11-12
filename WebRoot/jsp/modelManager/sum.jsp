<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
Task task = (Task)request.getAttribute("task");
String CTX_PATH= request.getContextPath();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<title>����</title>
</head>

<script language= "javascript">
function getTaskTimeid()
{

	//alert(document.getElementById("taskTimeIDSelect"));
    <%if(task.getTaskType() == task.REPORT_OF_MONTH){%>
      if(document.getElementById("taskTimeIDSelect").value=="")
      {
        alert("��ѡ��ʱ��");
        return "";
      }
      return document.getElementById("taskTimeIDSelect").value;
    <%}else if(task.getTaskType() == task.REPORT_OF_DAY){%>
	  
      if(document.getElementById('timeOfTask').value==""){
        alert("��ѡ��ʱ��");
        return;
      }
      var flag=false;
      for(var i=0;i<dateInfos.length;i++){
        if(dateInfos[i][1]==document.getElementById('timeOfTask').value){
           flag = true;
           return dateInfos[i][0];

        }
      }
      

      if(!flag){
           alert(document.getElementById('timeOfTask').value+"û������,������ѡ��һ������!");
           document.getElementById('timeOfTask').value = "";
           return "";
        }
    <%}%>
}

function adjustNodeDiff()
{
	if(document.all.dispinfo.style.display=="block") {alert('��һ������δ����');return;}
    var unitID = getCheckedValues();
	
    if(unitID == null || unitID == "")
    {
        alert("��ѡ��λ�ڵ�");
        return false;
    }

    var node = webFXTreeHandler.all[checkedNodeID];
    var hasChild = false;

    if(checkedNodeID == "")
    {
        //ͨ��ie�ġ����ء���ť�ص���ҳ������
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }
	
    if(!isGroupSumUnit(unitID))
    {
        alert("�ýڵ㲻�Ǽ��źϲ������ͣ�9�����޷���������");
        return false;
    }

    if(!hasChild)
    {
        alert("�ýڵ�û���ӽڵ㣬�޷���������");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.ADJUST_NODE_DIFF %>";
    //alert(91);
	var taskTimeID= getTaskTimeid();
	//alert(taskTimeID);
	if(taskTimeID=="")
		return;

    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;
	document.all.infoid.innerText = "���ڵ�������...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

function sumNode()
{
	if(document.all.dispinfo.style.display=="block") {alert('��һ������δ����');return;}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("��ѡ��λ�ڵ�");
        return ;
    }

    var node = webFXTreeHandler.all[checkedNodeID];

    var hasChild = false;

    if(checkedNodeID == "")
    {
        //ͨ��ie�ġ����ء���ť�ص���ҳ������
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }

    if(!hasChild)
    {
        alert("�ýڵ�û���ӽڵ㣬�޷����нڵ����");
        return false;
    }

    if(!isGroupSumUnit(unitID) && !isSumAllUnit(unitID))
    {
        alert("�ýڵ㱨�����͵�ֵ���ǿɻ������͡�7����9������������нڵ����");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.SUM_NODE %>";
	var taskTimeID= getTaskTimeid();
	if(taskTimeID=="")
		return;
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;

	document.all.infoid.innerText = "���ڻ�����Ϣ...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

function validateNodeSum()
{
	if(document.all.dispinfo.style.display=="block") {alert('��һ������δ����');return;}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("��ѡ��λ�ڵ�");
        return false;
    }

    var node = webFXTreeHandler.all[checkedNodeID];

    var hasChild = false;

    if(checkedNodeID == "")
    {
        //ͨ��ie�ġ����ء���ť�ص���ҳ������
        hasChild = true;
    }
    else
    {
        hasChild = (node.src !=null && node.src != "");
    }

    if(!hasChild)
    {
        alert("�ýڵ�û���ӽڵ㣬�޷����нڵ���");
        return false;
    }

    if(!isGroupSumUnit(unitID) && !isSumAllUnit(unitID))
    {
        alert("�ýڵ㱨�����͵�ֵ���ǿɻ������͡�7����9������������нڵ���");
        return false;
    }

    form1.operation.value = "<%= ModelServlet.VALIDATE_NODE_SUM %>";
	var taskTimeID= getTaskTimeid();
	if(taskTimeID=="")
		return;
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;

	document.all.infoid.innerText = "���ڼ����ܽ��...";
	document.all.dispinfo.style.display = "block";
    form1.submit();
}

/**
 * �ж�ָ���ĵ�λID�ĵ�λ�����Ƿ��Ǽ��Ż��ܵ�λ
 * @return �Ǽ��Ż��ܵ�λ������true�����򷵻�false
 */
function isGroupSumUnit(unitID)
{
    var ID = "" + unitID;
    var GROUP_SUM_TYPE = "9";
    return (ID.substr(ID.length - 1) == GROUP_SUM_TYPE);
}

/**
 * �ж�ָ���ĵ�λID�ĵ�λ�����Ƿ���ѡ����ܵ�λ
 * @return ��ѡ����ܵ�λ������true�����򷵻�false
 */
function isSelectSumUnit(unitID)
{
    var ID = "" + unitID;
    var SELECT_SUM_TYPE = "H";
    return (ID.substr(ID.length - 1) == SELECT_SUM_TYPE);
}

/**
 * �ж�ָ���ĵ�λID�ĵ�λ�����Ƿ�����ȫ���ܵ�λ
 * @return ����ȫ���ܵ�λ������true�����򷵻�false
 */
function isSumAllUnit(unitID)
{
    var ID = "" + unitID;
    var SUM_ALL_TYPE = "7";
    return (ID.substr(ID.length - 1) == SUM_ALL_TYPE);
}

//ѡ�����
function selectSum()
{
	var url= "model?operation=selectSum&taskTimeID="+getTaskTimeid();
	window.open(url,"selectSumWin","status=no,height=570,width=500,top=75,left=250");
}

</script>

<script language="javascript">

//��ѡ�е����ڵ�ID
var checkedNodeID = "";

/**
 * ������ڵ��radio button�����˷���
 * @param nodeID ������Ľڵ�id
 * @param checked �Ƿ�ѡ��
 */
function checkRadioTreeNode(nodeID, checked)
{
    if(checked)
    {
        checkedNodeID = nodeID;
    }
}

function submitForm()
{
}

//��������
function unEnvlopSubmitData()
{
    if(document.all.dispinfo.style.display=="block")
	{
		 alert('��һ������δ����');
		 return;
	}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("��ѡ��λ�ڵ�");
        return false;
    }

	 var retValue=new Array();
     retValue=window.showModalDialog('<%=CTX_PATH%>/jsp/modelManager/unenvlopdatatype.jsp','','dialogheight=300px;dialogwidth=400px;status=no');
	   if(retValue!=null)
		{
            form1.flag.value=retValue[0];
		    form1.operation.value = "<%= ModelServlet.UNENVELOPSUBMITDATA %>";
	    	var taskTimeID= getTaskTimeid();
		    if(taskTimeID=="")
    		return;
			form1.taskTimeID.value = getTaskTimeid();
			form1.unitID.value = unitID;
			form1.submit();
		}
}

//�������
function envlopSubmitData()
{
	if(document.all.dispinfo.style.display=="block")
	{
		 alert('��һ������δ����');
		 return;
	}
    var unitID = getCheckedValues();

    if(unitID == null || unitID == "")
    {
        alert("��ѡ��λ�ڵ�");
        return false;
    }
    form1.operation.value = "<%= ModelServlet.ENVELOPSUBMITDATA %>";
    form1.taskTimeID.value = getTaskTimeid();
    form1.unitID.value = unitID;
    form1.submit();

}

</script>

<body >
	<div id="dispinfo"  style="filter:alpha(opacity=80);z-index:10px;border:2;BACKGROUND-COLOR:#7d7baa;position:absolute;left:expression((body.clientWidth-300)/2);top:expression((body.clientHeight-100)/2);width:300px;height:20px;display:none">
		<TABLE WIDTH=100% height=70 BORDER=0 CELLSPACING=2 CELLPADDING=0><TR><td bgcolor=#eeeeee align=center id = infoid></td></tr></table></td><td width=30%></td></tr></table>
	</div>
	<jsp:include page= "/jsp/logo.jsp" />
	<jsp:include page= "/jsp/navigation.jsp" />
	<form name="form1" action="model" method="post">
	<input type="hidden" name="operation"/>
	<input type="hidden" name="unitID"/>
	<input type="hidden" name="taskTimeID"/>
    <input type="hidden" name="flag"/>
	<table class="clsContentTable" >
		<tr class="clsTrContext">
			<td align=right width=60%>
				<table height=100% width=350>
					<tr align=left>
						<td align=left>
								����ʱ��:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
								<input type="checkbox" name="isRecursive" value="true"/>���������¼���λ
						</td>
					</tr>
					<tr height=100% align=left>
					   <td class="TdLight">
						  <div class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
					   </td>
				   </tr>
				</table>
			</td>
			<td align=left valign=top width=40%>
				<br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" �ڵ���� " onclick="sumNode()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value="��������" onclick="adjustNodeDiff()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" �ڵ��� " onclick="validateNodeSum()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<input type="button" value=" ѡ����� " onclick="selectSum()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;
				<!--input type="button" value=" ���ݷ�� " onclick="envlopSubmitData()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;-->
				<input type="button" value=" �������� " onclick="unEnvlopSubmitData()"/><br>&nbsp;&nbsp;<br>&nbsp;&nbsp;


			</td>
		</tr>
	</table>
	</form>
	<jsp:include page= "/jsp/footer.jsp" />
</body>
</html>