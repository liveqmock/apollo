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
<script language="JavaScript">


function submitForm()
{
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
    if(form3.schemaID.value != null && form3.schemaID.value != "")
    {
        <%  Task task = (Task)request.getAttribute("task");
          if(task.getTaskType() == task.REPORT_OF_MONTH){%>
          if(form3.taskTimeIDSelect.value=="" || form3.taskTimeIDSelect.value=="-1")
          {
            alert("��ѡ��ʱ��");
            return;
          }
          form3.taskTimeID.value=form3.taskTimeIDSelect.value;
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
        form3.submit();
    }
    else
    {
        alert("��ѡ��Ҫִ�е�ѡ����ܷ���");
        return false;
    }
}
</script>
<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />
<%
Collection schemas = Convertor.collection((Iterator)request.getAttribute("schemaItr"));

User rightUser = RootServlet.getLoginUser(request);
SetOfPrivileges privilege = rightUser.getRole().getPrivileges();

if(privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK))
{
%>

<table class="clsContentListTable" border=0>
<form action="model?operation=<%= ModelServlet.CREATE_SELECT_SUM_SCHEMA %>" method="post" enctype="multipart/form-data" name="form1">

  <tr height=20 colspan=2>
                 <td align=left colspan=8>
                    ѡ����ܷ�����<input type="file" name="file"/>
                    <input type="submit" value="����" onclick="return createSelectSumSchema()"/>
                 </td>
  </tr>

</form>

<form action="model?operation=<%= ModelServlet.DELETE_SELECT_SUM_SCHEMA %>" method="post" name="form2">
<input type="hidden" name="schemaID"/>

  <tr height=20 class="clsTrContext">
    <td align=center colspan=2>ѡ����ܷ����б�</td>
  </tr>
  <tr height=20 class="clsTrHeader">
      <td width=33%>ѡ����ܷ���</td>
      <td width=67%>����</td>
  </tr>
<%
int count=0;//����������
for(Iterator itr = schemas.iterator(); itr.hasNext();)
{
    count++;
    SelectSumSchema schema = (SelectSumSchema)itr.next();
%>
  <tr  height=20 <%=(((count%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
      <td><%= schema.getName() %></td>
      <td><a href="javascript:deleteSelectSumSchema(<%= schema.getSchemaID() %>, '<%= schema.getName() %>')">ɾ��</a></td>
  </tr>
<%
}
%>
</form>

</table>

<%
}
if(privilege.getPrivilege(SetOfPrivileges.EXECUTE_SELECT_SUM_SCHEMA))
{
%>

<form action="model?operation=<%= ModelServlet.EXECUTE_SELECT_SUM_SCHEMA %>" method="post" name="form3">
	<input type="hidden" name="taskTimeID" value="">
	<table class="clsContentListTable" width=100%>
		<tr bgcolor="Gainsboro">
			<td>
			����ʱ�䣺<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />

			&nbsp;&nbsp;&nbsp;������<select name="schemaID">
	<%
		for(Iterator itr = schemas.iterator(); itr.hasNext();)
		{
			SelectSumSchema schema = (SelectSumSchema)itr.next();

			out.write("<option value='" + schema.getSchemaID() + "'>" + schema.getName() + "</option>");
		}
	%>
			</select>
			&nbsp;&nbsp;&nbsp;<button onclick="executeSelectSumSchema()">ִ��</button>
			</td>
		</tr>
	</table>
</form>


<%
}
%>
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
