<%@ page contentType="text/html; charset=GBK" %>

<%@ page import="cn.com.youtong.apollo.task.*,
				cn.com.youtong.apollo.services.Factory,
				cn.com.youtong.apollo.usermanager.*,
				cn.com.youtong.apollo.servlet.*,
				cn.com.youtong.apollo.news.*,
				cn.com.youtong.apollo.news.dao.*,
				java.util.*"
				%>
<%@page import="cn.com.youtong.apollo.sms.servlet.SMSServlet;"%>
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

<%
//��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

//���˵��������б�˵���״̬
String curMainMenuItem= (String)request.getSession().getAttribute("mainMenuStatus");

//�Ӳ˵�״̬
String subMenuStatus = (String)request.getSession().getAttribute("subMenuStatus");

User rightUser = RootServlet.getLoginUser(request);
SetOfPrivileges privilege = rightUser.getRole().getPrivileges();

String selected= "";

//--------------------------------------���˵���--------------------------------------------
ArrayList mainMenuNames= new ArrayList();
ArrayList mainMenuIDs= new ArrayList();
ArrayList mainMenuStates= new ArrayList();

//��ʾ��������
if(privilege.getPrivilege(SetOfPrivileges.MANAGE_REPORT))
{
	TaskManagerFactory factory= (TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName());
	TaskManager taskManager = factory.createTaskManager();
	Iterator ite = taskManager.getAllTasks();
	while(ite.hasNext())
	{
		Task task = (Task)ite.next();
		mainMenuNames.add(task.getName());
		mainMenuIDs.add(task.id());
		selected= task.id().equals(request.getSession().getAttribute("taskID")) ? "selected" : "";
		mainMenuStates.add(selected);
	}
}

//ϵͳ��ȫ����
if(privilege.getPrivilege(SetOfPrivileges.MANAGE_USER))
{
	mainMenuNames.add("ϵͳ��ȫ����");
	mainMenuIDs.add("30");
	selected= curMainMenuItem.equals("mainSecurityMenuTD") ? "selected" : "";
	mainMenuStates.add(selected);
}

//��������
if(privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK))
{


	selected= curMainMenuItem.equals("mainTaskMenuTD") ? "selected" : "";
	mainMenuNames.add("�������");
	mainMenuIDs.add("31");
	mainMenuStates.add(selected);

	selected= curMainMenuItem.equals("mainDictionaryMenuTD") ? "selected" : "";
	mainMenuNames.add("�����ֵ����");
	mainMenuIDs.add("32");
	mainMenuStates.add(selected);

//	selected= curMainMenuItem.equals("reportManageTD") ? "selected" : "";
//	mainMenuNames.add("�������");
//	mainMenuIDs.add("33");
//	mainMenuStates.add(selected);

	selected= curMainMenuItem.equals("mainLogMenuTD") ? "selected" : "";
	mainMenuNames.add("��־����");
	mainMenuIDs.add("34");
	mainMenuStates.add(selected);

	//selected= curMainMenuItem.equals("configMenuTD") ? "selected" : "";
	//mainMenuNames.add("����������");
	//mainMenuIDs.add("35");
	//mainMenuStates.add(selected);

}

//------------------------------�Ӳ˵���-----------------------------------
String []names= null;
String []actions= null;
boolean []states= null;

if("reportTD".equals(curMainMenuItem))//��������
{
	names	=new String[]{"�鿴����",
					"���ݴ���",
	
					null,//privilege.getPrivilege(SetOfPrivileges.PRIVILEGE_AUDIT) ? "���" : null,
					"����",
					privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK)||privilege.getPrivilege(SetOfPrivileges.EXECUTE_SELECT_SUM_SCHEMA) ? "ѡ����ܷ���" : null,
					"ָ���ѯ",
					"��ѯģ��",
					null,//privilege.getPrivilege(SetOfPrivileges.PRIVILEGE_AUDIT_USER) ? "��˲����쵼" : null,
//					"����",
                   null //privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK) ? "��ά����" : null
                  ,privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK)||privilege.getPrivilege(SetOfPrivileges.MANAGE_TASK) ? "��������" : null
                    };
	actions	=new String[]{"javascript: showDataManger()",
					"javascript: dataExchangeManager()",
		            "javascript: dataAuditManager()",
					"javascript: sumManager()",
					"javascript: selectSumSchema()",
					"javascript: scalarCondition()",
					"javascript: scalarQueryTemplate()",
					"javascript: auditUser()",
//					"javascript: reportManager()",
					"javascript: showManageOlapPage()"
					,"javascript: showSendSMSWindow()"
					};
	states	=new boolean[]{subMenuStatus.equals("showDataTD"),
					subMenuStatus.equals("dataExchangeTD"),
		            subMenuStatus.equals("dataAuditTD"),
					subMenuStatus.equals("sumTD"),
					subMenuStatus.equals("selectSumSchemaTD"),
					subMenuStatus.equals("scalarTD"),
					subMenuStatus.equals("templateTD"),
					subMenuStatus.equals("auditUserTD"),
//					subMenuStatus.equals("reportTD"),
					subMenuStatus.equals("olapTD"),subMenuStatus.equals("sendSMSTD")};
}
else if("mainLogMenuTD".equals(curMainMenuItem))//��־�����Ӳ˵�
{
	names	=new String[]{"�¼���ѯ", "������־", "��ȫ��־"};
	actions	=new String[]{"javascript: showEventQueryPage()",
					"javascript: showDataLogPage()",
					"javascript: showSecurityLogPage()"};
	states	=new boolean[]{subMenuStatus.equals("queryTD"),
					subMenuStatus.equals("dataLogTD"),
					subMenuStatus.equals("securityLogTD")};
}
else if("mainSecurityMenuTD".equals(curMainMenuItem))//ϵͳ��ȫ�Ӳ˵�
{
	names	=new String[]{"�û�����", "��ɫ����", "�����", "��ʼ���û�"};
	actions	=new String[]{"javascript: userManger()",
					"javascript: roleManger()",
					"javascript: groupManger()",
					"javascript: initManger()"};
	states	=new boolean[]{subMenuStatus.equals("userTD"),
					subMenuStatus.equals("roleTD"),
					subMenuStatus.equals("groupTD"),
					subMenuStatus.equals("initTD")};
}
else if("reportManageTD".equals(curMainMenuItem))//ϵͳ��ȫ�Ӳ˵�
{
	names	=new String[]{"ģ�����", "�������", "����Դ����", "Ԥ�������"};
	actions	=new String[]{
					"javascript: reportTemplate()",
					"javascript: reportReport()",
					"javascript: reportDataSource()",
					"javascript: reportPreValue()"
					};
	states	=new boolean[]{
					subMenuStatus.equals("reportTemplateTD"),
					subMenuStatus.equals("reportReportTD"),
					subMenuStatus.equals("reportDataSourceTD"),
					subMenuStatus.equals("reportPreValueTD")
						};
}

%>

<!-- �˵����� -->
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr><td colspan=4 height=2></td></tr>
	<tr>

		<!-- ��ǰ�Ӳ˵� -->
		<td height="100%" width=2>
		<%
			if(names != null)
			{
		%>
			<table cellspacing="0" border="0" cellpadding="0" height="100%">
				<tr>
				<%
				for(int i=0; i<names.length; i++)
				{
					if(names[i] == null)
						continue;
				%>
					<td>&nbsp;</td>
				<% if(states[i]) {%>
					<td valign=top class=clsSelectedToolTab>
						<img height=6 src='../img/tab_angle.gif' width=6 align=top border=0>
					</td>
				<% } else { %>
					<td>&nbsp;</td>
				<% } %>

					<td valign="bottom" nowrap class="<%= states[i] ? "clsSelectedToolTab" : "clsToolTabs"%>">
						<A href="<%= actions[i] %>" class= "<%=states[i]? "clsAlight":"clsAdark"%>">
							<%=names[i]%>
						</A>
						&nbsp;
					</td>
					<td vAlign="center"><IMG height=9 src="../img/tab_separator.gif" width=9 align=absMiddle border=0></td>
				<% } %>
				</tr>
			</table>

			<%}%>
		 </td>

		<td background ="../img/tab_stripes.jpg">
			<marquee behavior="scroll" scrollAmount=2 direction=left onMouseOver="this.stop();" onMouseOut="this.start()">
			  <%YtaplNewsDAO newsDAO = new YtaplNewsDAO();
                List newss = newsDAO.findAll();
                int toEnd =newss.size();
                if(newss.size()>=9) toEnd = 9;
                for(Iterator it=newss.iterator();it.hasNext();){
                	YtaplNews news = (YtaplNews)it.next();
              %>
				<a href="../news/showNews.jsp?newsID=<%=news.getId().intValue()%>" target="_blank"><%=news.getTitle()%></a>&nbsp;&nbsp;
			  <%} %>
			</marquee>
		</td>
		<td width=2></td>
		 <!-- �������˵� -->
         <td valign="top" width=10 nowrap>
				<select id="mainToolSelect" onchange="displaySubMenu(this)" style="font-size:9pt">
				<%
					for(int i=0; i<mainMenuNames.size(); i++)
					{
						String name= (String)mainMenuNames.get(i);
						String id= (String)mainMenuIDs.get(i);
						String state= (String)mainMenuStates.get(i);
						out.write("<option value='" + id + "' " +  state + " >" +name);
					}
				%>
				</select>
         </td>
	</tr>
	<tr><td colspan=4 height="2"  style="background-color: black"></td></tr>
</table>

<script language="javascript">

function displaySubMenu(obj){
   var selectIndexOpt = obj.options[obj.selectedIndex].value;
   if(selectIndexOpt==30){
      SecurityManager();
   }
   else if(selectIndexOpt==31){
      TaskManager();
   }
   else if(selectIndexOpt==32){
     dictionaryManager();
   }
   else if(selectIndexOpt==33){
     showReportManagePage();
   }
   else if(selectIndexOpt==34){
     showEventQueryPage();
   }
   else if(selectIndexOpt==35){
     displayServerConfig();
   }
   else{
       viewTaskSubMenu(selectIndexOpt,obj.options[obj.selectedIndex].text);
   }
}

//�����Ӳ˵�action---------------------------------------------------------------
function showSendSMSWindow(){

	 window.location = '../servlet/smsoperation?operation=<%=cn.com.youtong.apollo.sms.servlet.SMSServlet.SHOW_SENDMESSAGE_PAGE%>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"sendSMSTD";
}
function scalarCondition()
{
   window.location = '../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_SCALAR_CONDITION_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"scalarTD";
}

function scalarQueryTemplate()
{
   window.location = '../servlet/analyse?operation=<%= cn.com.youtong.apollo.servlet.AnalyseServlet.SHOW_SCALAR_QUERY_TEMPLATE_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"templateTD";
}

function auditUser()
{
	  window.location = '../servlet/analyse?operation=auditUser'
		+'&mainMenuStatus=reportTD&subMenuStatus=auditUserTD';
}

function showManageOlapPage()
{
   window.location = '../servlet/olap?operation=<%= cn.com.youtong.apollo.servlet.OlapServlet.SHOW_MANAGE_OLAP_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"olapTD";
}

function dataExchangeManager()
{
   window.location = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_DATA_EXCHANGE_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"dataExchangeTD";
}

function dataAuditManager()
{
   window.location = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_DATA_AUDIT_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"dataAuditTD";
}

function sumManager()
{
   window.location = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_SUM_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"sumTD";
}
function sumNodeSelectManager()
{
   window.location = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_SUMNODE_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"sumNodeSelectTD";
}
function scriptManager(){
  
   urlStr = "../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_MANAGER_SCRIPT_PAGE %>"
            +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"scriptTD";
   flag = <%= (request.getSession().getAttribute("taskID")==null)? true:false%>
   if(flag==false){
      urlStr = urlStr+"&taskID="+<%='"'+(String)request.getSession().getAttribute("taskID")+'"'%>;
      window.location=urlStr;
   }else{
      window.location=urlStr;
   }
   
   
   
}

function selectSumSchema(){
   urlStr = "../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_SELECT_SUM_SCHEMA_MANAGE_PAGE %>"
            +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"selectSumSchemaTD";
   flag = <%= (request.getSession().getAttribute("taskID")==null)? true:false%>
   if(flag==false){
      urlStr = urlStr+"&taskID="+<%='"'+(String)request.getSession().getAttribute("taskID")+'"'%>;
      window.location=urlStr;
   }else{
      window.location=urlStr;
   }
}

function initPermissionManager()
{
   window.location = "../servlet/unitPermission?operation=<%=cn.com.youtong.apollo.servlet.UnitPermissionServlet.SHOW_INIT_PERMISSION%>"
            +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"initPermissionTD";
}

function addressInfoManager(){
   urlStr = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.SHOW_ADDRESS_INFO_PAGE%>"
            +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"addressTD";
   flag = <%= (request.getSession().getAttribute("taskID")==null)? true:false%>
   if(flag==false){
      urlStr = urlStr+"&taskID="+<%='"'+(String)request.getSession().getAttribute("taskID")+'"'%>;
      window.location=urlStr;
   }else{
      window.location=urlStr;
   }
}

function xsltManger()
{
   window.location = '../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_MANAGE_TASKVIEW_PAGE %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"xsltTD";
}
//Ĭ���Ӳ˵�
function showDataManger()
{
   window.location = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_DATA_MANAGER %>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"showDataTD";
}
function unitManger()
{
   window.location = '../servlet/unitPermission?operation=<%=cn.com.youtong.apollo.servlet.UnitPermissionServlet.SHOW_UNIT_INFO%>'
   +"&mainMenuStatus="+"reportTD"+"&subMenuStatus="+"unitTD";
}

// ϵͳ��ȫ����
function SecurityManager(){
   window.location = '../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_USER_INFO%>'
   +"&mainMenuStatus="+"mainSecurityMenuTD"+"&subMenuStatus="+"userTD";
}
//�������
function TaskManager(){
   url = '../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_MANAGE_TASK_PAGE %>';
   url += "&mainMenuStatus="+"mainTaskMenuTD"+"&subMenuStatus="+""+"&taskFlag="+true;
   window.location = url;
}
//�����ֵ����
function dictionaryManager(){
   url = '../servlet/dictionary?operation=<%= cn.com.youtong.apollo.servlet.DictionaryServlet.SHOW_DICTIONARY_DISPLAY_PAGE %>';
   url += "&mainMenuStatus="+"mainDictionaryMenuTD"+"&subMenuStatus="+"";
   window.location = url;
}
//�������
function showReportManagePage()
{
	window.location = "../do/report?mainMenuStatus=reportManageTD";
}
//��־����
function showEventQueryPage(){
   url = '../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_QUERY_EVENT_PAGE %>';
   url += "&mainMenuStatus="+"mainLogMenuTD"+"&subMenuStatus="+"queryTD";
   window.location = url;
}

function showDataLogPage(){
   url = '../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_DATA_LOG_PAGE %>';
   url += "&mainMenuStatus="+"mainLogMenuTD"+"&subMenuStatus="+"dataLogTD";
   window.location = url;
}

function showSecurityLogPage(){
   url = '../servlet/log?operation=<%= cn.com.youtong.apollo.servlet.LogServlet.SHOW_SECURITY_LOG_PAGE %>';
   url += "&mainMenuStatus="+"mainLogMenuTD"+"&subMenuStatus="+"securityLogTD";
   window.location = url;
}

//ϵͳ����
function displayServerConfig(){
   window.location = '../servlet/task?operation=<%= cn.com.youtong.apollo.servlet.TaskServlet.SHOW_CONFIG_PAGE %>'
   +"&mainMenuStatus="+"configMenuTD"+"&subMenuStatus="+"";
}

function viewTaskSubMenu(taskID,taskName){
   url = '../servlet/model?operation=<%= cn.com.youtong.apollo.servlet.ModelServlet.SHOW_DATA_MANAGER %>';
   url += "&mainMenuStatus="+"reportTD"
	+"&subMenuStatus="+"showDataTD"
	+"&sessionTaskID="+taskID
 	+"&sessionTaskName="+encodeURIComponent(taskName);
        window.location = encodeURI(url);
}
//��ȫ�Ӳ˵�action------------------------------------------------------------------------
function initManger()
{
   window.location = '../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_INIT_INFO%>'
   +"&mainMenuStatus="+"mainSecurityMenuTD"+"&subMenuStatus="+"initTD";
}
function groupManger()
{
   window.location = '../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_GROUP_INFO%>'
   +"&mainMenuStatus="+"mainSecurityMenuTD"+"&subMenuStatus="+"groupTD";
}
function roleManger()
{
   window.location = '../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_ROLE_INFO%>'
   +"&mainMenuStatus="+"mainSecurityMenuTD"+"&subMenuStatus="+"roleTD";
}
function userManger()
{
   window.location = '../servlet/systemOperation?operation=<%=cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_USER_INFO%>'
   +"&mainMenuStatus="+"mainSecurityMenuTD"+"&subMenuStatus="+"userTD";
}

function reportManager()
{
	window.location = "../do/report";
}
//��������Ӳ˵�action------------------------------------------------------------------
function reportDataSource()
{
}
function reportPreValue()
{
}
function reportTemplate()
{
	window.location = "../do/report?action=templateindex"+"&mainMenuStatus="+"reportManageTD"+"&subMenuStatus="+"reportReportTD";
}
function reportReport()
{
	window.location = "../do/report?action=reportindex"+"&mainMenuStatus="+"reportManageTD"+"&subMenuStatus="+"reportReportTD";
}


<%
if("".equals(curMainMenuItem) || curMainMenuItem == null)
{
%>
if(mainToolSelect.options.length > 0)
{
	displaySubMenu(mainToolSelect);
}
<%}%>
</script>