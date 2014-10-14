<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.task.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<title>ѡ�����</title>
</head>
<%
Task task = (Task)request.getAttribute("task");
%>

<body>
<script language= "javascript">

//���� Url�������Ĳ���
function getParameter(){
  var url="";
  //�õ�����ʱ��ID
  url += "&taskTimeID=" + document.all.taskTimeID.value;
  //�õ���ѡ��ĵ�λID
  var IDs = getCheckedValuesOfCheckboxTree();
  if(IDs == "" || IDs.length == 0)
  {
    alert("��ѡ�������ܵĵ�λ");
    return false;
  }
 var ID = getCheckedValuesOfRadioTree();
  if(ID == "" || ID.length == 0)
  {
    alert("��ָ����Ż��ܽ���ĵ�λ");
    return false;
  }
    url += "&unitID=" + ID;
  //�õ���ѡ��ĵ�λIDs

  if(!isSelectSumUnit(ID))
  {
    alert("��Ż��ܽ���ĵ�λ������ѡ��������͵ĵ�λ");
    return false;
  }
  for(var i = 0;i < IDs.length;i++)
  {
    url += "&unitIDs=" + IDs[i];
  }
  return url;
}

function sumUnits()
{
  if(getParameter()){
    var url = "model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SUM_UNITS%>";
    url += getParameter();
    window.location=url;
  }
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
</script>
  <jsp:include page= "/jsp/logo.jsp" />
  <jsp:include page= "/jsp/navigation.jsp" />


   <table class="clsContentTable">
               <tr height=5 class="clsTrContext">
                     <td class="TdDark" colspan=2 align=center>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;����ʱ��:
                          <select name="taskTimeID">
                          <%
                              for(Iterator taskTimeItr = task.getTaskTimes(); taskTimeItr.hasNext();)
                              {
                                TaskTime taskTime = (TaskTime)taskTimeItr.next();
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat monthly = new SimpleDateFormat("yyyy��MM��");

                                if(monthly.format(calendar.getTime()).equals(monthly.format(taskTime.getFromTime()))){
                          %>
                              <option value="<%= taskTime.getTaskTimeID() %>" selected><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></option>
                          <%}else{%>
                              <option value="<%= taskTime.getTaskTimeID() %>"><%= Convertor.date2MonthlyString(taskTime.getFromTime()) %></option>
                          <%}}%>
                          </select>
                          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      </td>
                      <td class="TdDark" colspan=2 align=center>
                        <input type="button" value=" �� �� " onclick="sumUnits()"/>&nbsp;&nbsp;
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      </td>
               </tr>
               <tr class="clsTrContext">
                   <td align=center class="TdDark" width=15%>ѡ�������ܵĵ�λ:</td>
                   <td class="TdLight" width=35%>
                        <div class="clsTreeDiv"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
                   </td>
                   <td align=center class="TdDark" width=15%>
                         ָ����Ż��ܽ���ĵ�λ
                   </td>
                   <td class="TdLight" width=35%>
                        <div id="unitTreeDiv" class="clsTreeDiv"><%=(String)request.getAttribute("radioUnitTree")%></div>
                       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   </td>
               </tr>
     </table>

  <jsp:include page= "/jsp/footer.jsp" />
<script language="javascript">
function expandTree(){
    unitTreeDiv.style.overflowY = 'visible';
//    unitTreeDiv.style.overflowY = 'auto';
}
</script>
</body>
</html>