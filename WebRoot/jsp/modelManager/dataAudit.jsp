<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.script.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link href="../csslib/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>
<title>���</title>
</head>
<%
 String CTX_PATH= request.getContextPath();
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
  Task task = (Task)request.getAttribute("task");
  String msgtype = (String)request.getParameter("msgtype");
  String alltypechk="",warningchk="",errorchk="";
  if(msgtype==null||msgtype.equals("alltype"))
  {
	alltypechk="checked";
  }
  else if(msgtype.equals("warning"))
  {
	warningchk="checked";
  }
  else
  {
	errorchk="checked";
  }
%>
<script language="JavaScript">

function getTaskTimeid()
{
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


function printData(type)
{
  if(dispinfo.style.display=="block")
  {
	alert("��ǰ�����δ������");
	return;
  }
  /*if(unitData.document.innerText==null||unitData.document.innerText=="")
  {
	alert("û�п��Դ�ӡ������");
	return;
  }*/
  unitData.document.execCommand("print");
}
function dataProcess(type)
{
  if(dispinfo.style.display=="block")
  {
	alert("��ǰ�����δ������");
	return;
  }

  var selUnitsID=getCheckedValues();

  if(selUnitsID=="")
	{
	  alert("��ѡ��λ");
      return;
	}

var retValue=new Array();
     retValue=window.showModalDialog('<%=CTX_PATH%>/jsp/modelManager/selunittype.jsp','','dialogheight=300px;dialogwidth=400px;status=no');
	   if(retValue!=null)
		{

			  form1.unitIDs.value=selUnitsID;
			  //form1.taskTimeID.value=form1.taskTimeIDSelect.value;
			  var taskTimeid=getTaskTimeid();
			  if(taskTimeid=="")
				  return;
			  form1.taskTimeID.value=getTaskTimeid();
			  
			  form1.exectype.value=type;
			  form1.flag.value=retValue[0];


			   if(type==0)
			  {
				infotext.innerText="���ڼ�������...";
			  }
			  else
			  {
				infotext.innerText="�����������...";
			  }

			 form1.action= "model?operation=<%=ModelServlet.DO_DATA_AUDIT%>";
			 dispinfo.style.display="block";
			 form1.submit();

		}

}
function submitForm(){}
function onAllcheck(){
        var flag = allCheck.checked;
	var check = document.getElementsByName("tree-check-box-name");
	for( var i=0; i<check.length; i++ )
	{
		check(i).checked = flag;
	}
}

function changeUnit(id,name){
}
</script>

<body>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />

	 <div id="dispinfo"  style="filter:alpha(opacity=80);z-index:10px;border:2;BACKGROUND-COLOR:#7d7baa;position:absolute;left:expression((body.clientWidth-300)/2);top:expression((body.clientHeight-100)/2);width:300px;height:20px;display:none">
		<TABLE WIDTH=100% height=70 BORDER=0 CELLSPACING=2 CELLPADDING=0><TR><td bgcolor=#eeeeee align=center id=infotext>&nbsp;</td></tr></table></td><td width=30%></td></tr></table>
     </div>
     <table class="clsContentTable">
      <tr>
           <input type="checkbox" name="allCheck" onclick="onAllcheck()">ȫѡ
           <td width="23%">
             <div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
           </td>
           <form name="form1" id="form1" method="post" target="unitData">
           <input type="hidden" name="taskTimeID" value="">
		   <input type="hidden" name="unitIDs">
		   <input type="hidden" name="exectype">
  		   <input type="hidden" name="flag">

           <td height=100%>
             <table height=100% width=100% border=0>
               <tr>
                   <td>
                     ����ʱ��:<jsp:include page= "/jsp/modelManager/periodOfTask.jsp" />
                  </td>
                  <td align="right">
<a href='javascript:printData(0);'><img src='../img/printone.bmp' alt='��ӡ' border="0" align="absmiddle"title='��ӡ'>��ӡ</a>
&nbsp;
<a href='javascript:dataProcess(0);'><img src='../img/allaudit.gif' alt='����' border="0" align="absmiddle"title='����'>����</a>
&nbsp;
<a href='javascript:dataProcess(1);'><img src='../img/audit.gif' alt='���' border="0" align="absmiddle"title='���'>���</a>
&nbsp;
<!--
(<input type="checkbox" name="all" />�����ӽڵ�)
-->
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                  </td>
              </tr>
              <tr>

                  <td colspan="2" height=100%>

                     <iframe  SCROLLING=auto height="100%" width="100%" FRAMEBORDER=0 id="unitData" name="unitData"></iframe>

                  </td>
              </tr>
              <tr height=20 bgcolor="#eeeeee">
                  <td colspan="2" height=100%>
			<input type=radio name="msgtype" value="alltype" <%=alltypechk%>>ȫ��<input type=radio name="msgtype" value="warning" <%=warningchk%>>����<input type=radio name="msgtype" value="error" <%=errorchk%>>����
                  </td>
              </tr>
             </table>
            </td>
        </form>
      </tr>
    </table>


<script language="javascript">
//��һ����ʾ�հױ���
//unitData.location= "model?operation=<%=ModelServlet.DO_DATA_AUDIT%>&flag=no","_self";
</script>

  <jsp:include page= "/jsp/footer.jsp" />
</body>
</html>
