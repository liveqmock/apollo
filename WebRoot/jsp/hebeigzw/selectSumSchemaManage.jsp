<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<title>ѡ����ܷ���</title>
</head>
<script type="text/javascript" src="../jslib/jquery-1.5.1.min.js"></script>

<script language="JavaScript">


function submitForm()
{
var _form = document.forms;
var tasktimeid = _form[0].taskTimeIDSelect.value;
var tasktype = '';
var _summarytype = _form[0].summarytype;
for(var i=0;i<_summarytype.length;i++)
{
	if(_summarytype[i].checked==true)
	{
		tasktype = _summarytype[i].value;
		break;
	}
}

//alert(_from.taskTimeIDSelect.value);
//alert(_from.summarytype.value);
  $.ajax({ //һ��Ajax����  
    type: "post",  //��post��ʽ���̨��ͨ
    url : "hebeigzwservlet?operation=getSummaryInfoByTimeAndTypeAndTaskId", //���servletҳ�湵ͨ
    dataType:'html',//��servlet���ص�ֵ�� JSON��ʽ ����
    data:  'summarytype='+tasktype+'&summarytime='+tasktimeid, //����php������������ֱ������洫����u��p   
    success: function(json){
    	//alert(json+"<---->���ص�����");
    	document.getElementById('resultinfo').innerHTML=json;
    	//alert('1');
    }
   }
  );
}

function deleteSelectSumSchema(schemaID, name)
{
    if(confirm("�Ƿ����Ҫɾ��ѡ����ܷ�����" + name + "��"))
    {
        form2.schemaID.value = schemaID;
        form2.submit();
    }
}

function executeSelectSumSchema()
{
   
}

</script>
<body>
<jsp:include page= "/jsp/logo.jsp" />
<jsp:include page= "/jsp/navigation.jsp" />
<%
Task task = (Task)request.getAttribute("task");
%>
<!-- ��ʼ -->

	<form action="" method="post" name="ssdq">
	  <table align="center">
		<tr>
			<td>
				����ʱ��:	<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" /><br/>
			</td>
		</tr>
		<tr>
			<td>
				���ܷ�����
				<input type="radio" name="summarytype" value="ssdq"/>����������
				<input type="radio" name="summarytype" value="sshy"/>����ҵ����
				<input type="radio" name="summarytype" value="jyqm"/>����Ӫ��ģ����<br/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="��ʼ����" onclick="submitForm()"/> 
				<input type="reset" value="ȡ��"/>  
			</td>
		</tr>
      </table>
	</form>
	<br/><br/><br/><br/><br/><br/>
	
	<div id="resultinfo">
	</div>
<!-- ���� -->
<jsp:include page= "/jsp/footer.jsp" />
<script language="JavaScript">
function createSelectSumSchema()
{
    if(form1.file.value == "")
    {
        alert("��ѡ��Ҫ������ѡ����ܷ���");
        return false;
    }
    return true;
}
</script>
</body>
</html>
