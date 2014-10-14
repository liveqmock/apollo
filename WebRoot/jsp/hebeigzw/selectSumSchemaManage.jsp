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
<title>选择汇总方案</title>
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
  $.ajax({ //一个Ajax过程  
    type: "post",  //以post方式与后台沟通
    url : "hebeigzwservlet?operation=getSummaryInfoByTimeAndTypeAndTaskId", //与此servlet页面沟通
    dataType:'html',//从servlet返回的值以 JSON方式 解释
    data:  'summarytype='+tasktype+'&summarytime='+tasktimeid, //发给php的数据有两项，分别是上面传来的u和p   
    success: function(json){
    	//alert(json+"<---->返回的数据");
    	document.getElementById('resultinfo').innerHTML=json;
    	//alert('1');
    }
   }
  );
}

function deleteSelectSumSchema(schemaID, name)
{
    if(confirm("是否真的要删除选择汇总方案“" + name + "”"))
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
<!-- 开始 -->

	<form action="" method="post" name="ssdq">
	  <table align="center">
		<tr>
			<td>
				任务时间:	<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" /><br/>
			</td>
		</tr>
		<tr>
			<td>
				汇总方案：
				<input type="radio" name="summarytype" value="ssdq"/>按地区汇总
				<input type="radio" name="summarytype" value="sshy"/>按行业汇总
				<input type="radio" name="summarytype" value="jyqm"/>按经营规模汇总<br/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="button" value="开始汇总" onclick="submitForm()"/> 
				<input type="reset" value="取消"/>  
			</td>
		</tr>
      </table>
	</form>
	<br/><br/><br/><br/><br/><br/>
	
	<div id="resultinfo">
	</div>
<!-- 结束 -->
<jsp:include page= "/jsp/footer.jsp" />
<script language="JavaScript">
function createSelectSumSchema()
{
    if(form1.file.value == "")
    {
        alert("请选择要创建的选择汇总方案");
        return false;
    }
    return true;
}
</script>
</body>
</html>
