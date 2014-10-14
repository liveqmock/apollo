<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.SystemOperationServlet"%>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.usermanager.User"%>
<%@ page import="cn.com.youtong.apollo.common.Convertor"%>

<html>
<head>
<title>�û�</title>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<script Language="JavaScript" src="../jslib/floatbutton.js"></script>
<script Language="JavaScript" src="../jslib/function.js"></script>
<script Language="JavaScript" src="../jslib/optionfunction.js"></script>
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<link type="text/css" rel="stylesheet" href="../csslib/webfxlayout.css" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<style type="text/css">

.dynamic-tab-pane-control .tab-page {

   overflow : hidden
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
   width:460
}

BODY
{
    MARGIN: 7px;
}
.dynamic-tab-pane-control h2 {
	text-align:	center;
	width:		auto;
}

.dynamic-tab-pane-control h2 a {
	display:	inline;
	width:		auto;
}

.dynamic-tab-pane-control a:hover {
	background: transparent;
}
</style>
</head>
<%
    User user = null;
    if(request.getAttribute("userInfo") != null)
    {
        user = (User)request.getAttribute("userInfo");
    }
    Collection colUser=(Collection)request.getAttribute("allUser");
    Collection groupInfo=(Collection)request.getAttribute("groupInfo");
    Collection colPartGroup=(Collection)request.getAttribute("partGroupInfo");
    String operation = request.getParameter("operation");
%>
<script language="JavaScript">

/**
 * �����޸������ʶ
 */
function setModifyPaswordFlag()
{
    form1.modifyPassword.value = "true";
}
    //����û�������Ϣ
    function validateUser()
    {
      form1.userName.value = trim(form1.userName.value);
      if(isNull(form1.userName.value))
      {
        alert("�˺����Ʋ���Ϊ��");
        setTabIndex(0);
        form1.userName.focus();
        return false;
      }

        if(isNull(form1.password.value))
        {
          alert("���벻��Ϊ��");
          setTabIndex(0);
          form1.password.focus();
          return false;
        }


        if(form1.password.value!=form1.validatePassword.value)
        {
          alert("������������ǰ��һ�£�");
          setTabIndex(0);
          form1.validatePassword.focus();
          return false;
        }

        form1.enterpriseName.value = trim(form1.enterpriseName.value);
        form1.lawPersonCode.value = trim(form1.lawPersonCode.value);
        form1.lawPersonName.value = trim(form1.lawPersonName.value);
        form1.lawPersonPhone.value = trim(form1.lawPersonPhone.value);

        if(!isNull(form1.lawPersonPhone.value) && !isPhone(form1.lawPersonPhone.value))
        {
          alert("���˴���绰��ʽ����ȷ��");
          form1.lawPersonPhone.focus();
          return false;
        }

        form1.contactPerson.value = trim(form1.contactPerson.value);
        form1.contactPersonPhone.value = trim(form1.contactPersonPhone.value);

        if(!isNull(form1.contactPersonPhone.value) && !isPhone(form1.contactPersonPhone.value))
        {
          alert("��ϵ�˵绰��ʽ����ȷ��");
           form1.contactPersonPhone.focus();
          return false;
        }

        form1.contactPersonAddress.value = trim(form1.contactPersonAddress.value);
        form1.postcode.value = trim(form1.postcode.value);

        form1.contactPersonMobile.value = trim(form1.contactPersonMobile.value);
        if(!isNull(form1.contactPersonMobile.value) && !isPhone(form1.contactPersonMobile.value))
        {
          alert("��ϵ���ֻ���ʽ����ȷ��");
          form1.contactPersonMobile.focus();
          return false;
        }

        form1.fax.value = trim(form1.fax.value);

        if(!isNull(form1.fax.value) && !isPhone(form1.fax.value))
        {
          alert("��������ʽ����ȷ��");
          form1.fax.focus();
          return false;
        }

        form1.email.value = trim(form1.email.value);

        if(!isMail(form1.email.value))
        {
          alert("�����ʼ���ʽ����ȷ(ʾ��:tom@tom.com)!");
          form1.email.focus();
          return false;
        }
        return true;
    }

//��֤��
function validateGroup()
{
     if(isNull(form1.roleID.value))
      {
        alert("��ѡ���ɫ");
        setTabIndex(2);
        form1.roleID.focus();
        return false;
      }
     return true;
}

//��֤��ɫ
function validateRole()
{
      form1.groupIDs.value = formAllOptionIds("selSelectedUsers");
      form1.memo.value = trim(form1.memo.value);
      return true;
}

    //�½��û�
    function createUser()
    {
      if(!validateUser() || !validateGroup() || !validateRole())
      {
          return false;
      }

<%

   Iterator itrUser=colUser.iterator();
   while(itrUser.hasNext())
   {
    User rdf=(User)itrUser.next();
%>
//�ж��Ƿ�����
if(form1.userName.value=="<%=rdf.getName()%>")
    {
     alert("���˺��Ѿ����ڣ����������룡")
     return false;
   }
<%}%>

      form1.operation.value = "<%=SystemOperationServlet.USER_ADD%>";
      window.opener.name = "userList";
      form1.submit();
      window.close();
}

function cancel()
{
  close();
}
//��ʾȨ����Ϣ
var rightInfos = new Array();
<%
   Collection roleRights = (Collection)request.getAttribute("roleRights");
   Iterator roleRightsItr = roleRights.iterator();
   for(int i=0;roleRightsItr.hasNext();i++){
%>
   rightInfos[<%=i%>] = new Array;
<%
      Iterator roleRight = ((Collection)roleRightsItr.next()).iterator();
      for(int j=0;roleRight.hasNext();j++){
       String roleValue = roleRight.next().toString();
%>
       rightInfos[<%=i%>][<%=j%>] = <%=roleValue%>;
<%
      }
   }
%>
function displyRoleInfo(obj){
  var selectIndexOpt = obj.options[obj.selectedIndex].value;
  for(var i=0;i<rightInfos.length;i++){
     if(rightInfos[i][0]==selectIndexOpt){
       for(var j=1;j<rightInfos[i].length;j++){
         document.getElementById("privileges"+j).checked = rightInfos[i][j];
       }
     }
  }
     if(selectIndexOpt==""){
       for(var j=1;j<7;j++){
         document.getElementById("privileges"+j).checked = false;
       }
     }
}
</script>
<body>
<form name="form1" action="systemOperation" method="post" target="userList">

<input type="hidden" name="curPage" value="<%=request.getParameter("curPage")%>"/>
<input type="hidden" name="operation"/>
<input type="hidden" name="groupIDs"/>
<input type="hidden" name="userID">
<input type="hidden" name="modifyPassword"/>

<div class="tab-pane" id="taskPane1">
<script language="JavaScript">
var taskPane = new WebFXTabPane( document.getElementById( "taskPane1" ) ,false);
function setTabIndex(index){
  taskPane.setSelectedIndex(index);
}
</script>






     <div class="tab-page">
        <h2 class="tab">������Ϣ&nbsp;</h2>


              <table border="0" class="Tbody">
           
		   <%if(!operation.equals(SystemOperationServlet.SHOW_USER_MODIFY_SELF_PAGE)){%>
                 


				<tr>
                        <td class="TdDark">�ʺ�����:</td>
                        <td class="TdLight"><input type="text" name="userName">*</td>
                   </tr>
                   <tr>
                        <td class="TdDark" width=120>��ͨ:</td>
                        <td class="TdLight"><input type="checkbox" name="validated" checked></td>
                   </tr>
			 <%}else{%>
			      
                      <input type="hidden" name="userName">
					  <input type="hidden" name="validated">
			  <%}%>
                   <tr>
                        <td class="TdDark">����:</td>
                        <td class="TdLight"><input type="password" name="password"  onchange="setModifyPaswordFlag()">*</td>
                   </tr>
                   <tr>
                        <td class="TdDark">ȷ������:</td>
                        <td class="TdLight"><input type="password" name="validatePassword"  onchange="setModifyPaswordFlag()">*</td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ҵ����:</td>
                        <td class="TdLight"><input type="text" name="enterpriseName"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">���˴���:</td>
                        <td class="TdLight"><input type="text" name="lawPersonCode"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">���˴���:</td>
                        <td class="TdLight"><input type="text" name="lawPersonName"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">���˴���绰:</td>
                        <td class="TdLight"><input type="text" name="lawPersonPhone"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ϵ��:</td>
                        <td class="TdLight"><input type="text" name="contactPerson"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ϵ�˵绰:</td>
                        <td class="TdLight"><input type="text" name="contactPersonPhone"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ϵ���ֻ�:</td>
                        <td class="TdLight"><input type="text" name="contactPersonMobile"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ϵ��ַ:</td>
                        <td class="TdLight"><input type="text" name="contactPersonAddress"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">�ʱ�:</td>
                        <td class="TdLight"><input type="text" name="postcode"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">����:</td>
                        <td class="TdLight"><input type="text" name="fax"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">�����ʼ�:</td>
                        <td class="TdLight"><input type="text" name="email"></td>
                   </tr>
                   <tr>
                        <td class="TdDark">��ע:</td>
                        <td class="TdLight"><textarea name="memo" rows="2" cols="30"></textarea></td>
                   </tr>
              </table>
      </div>
<%
if(operation.equals(SystemOperationServlet.SHOW_USER_ADD) || operation.equals(SystemOperationServlet.SHOW_USER_MODIFY))
{
%>
     <div class="tab-page">
        <h2 class="tab">��&nbsp;</h2>

             <table width="100%">
                                        <tr class="THeader">
                                        <td nowrap width="45%" align="center">��ѡ��</td>
                                        </td>
                                        <td nowrap width="10%" align="center">&nbsp;</td>
                                        </td>
                                        <td nowrap width="45%" align="center">��ѡ�����</td>
                                        </td>
                                        </tr>
                                        <tr>
                                        <td nowrap width="45%" align="center">
                                        <select ondblclick="moveOption('selAllUsers','selSelectedUsers')" size="15" style="width:100%" name="selAllUsers" multiple>
                                        <%

                                           Iterator itrAllGroup=groupInfo.iterator();
                                           while(itrAllGroup.hasNext())
                                              {
                                                Group rdf=(Group)itrAllGroup.next();
                                         %>

                                          <option value="<%=rdf.getGroupID().intValue()%>" title="<%=rdf.getName()%>"><%=rdf.getName()%></option>
                                           <%}%>
                                         </select>
                                        </td>
                                        <td nowrap width="10%" align="center">
                                                <button onclick="moveOption('selAllUsers','selSelectedUsers')" id=button3 name=button3 class="btnStaticNarrow">���&gt;</button><br><br>
                                                <button onclick="moveOption('selSelectedUsers','selAllUsers')" id=button4 name=button4 class="btnStaticNarrow">&lt;ɾ��</button>
                                        </td>
                                       <td nowrap width="45%" align="center">
                                        <select size="15" style="width:100%" name="selSelectedUsers" ondblclick="moveOption('selSelectedUsers','selAllUsers')" multiple>
                                         <%
                                           Iterator itrPartGroup=colPartGroup.iterator();
                                           while(itrPartGroup.hasNext())
                                              {
                                                Group rdf=(Group)itrPartGroup.next();
                                         %>
                                          <option value="<%=rdf.getGroupID().intValue()%>"><%=rdf.getName()%></option>
                                           <%}%>
                                        </select>
                                        </td>
                                        </tr>
                                        </table>
                                        </td>
                                        </tr>
                                </table>

      </div>


 <div class="tab-page">
        <h2 class="tab">��ɫ&nbsp;</h2>

         <table border=0 cellspacing=0 cellpadding=0>
            <tr>
             <td>
                 <select name="roleID" style="width: 160px" onchange="displyRoleInfo(this)">
                 <option value="" selected>��ѡ��</option>
                 <%
                 Collection colRole=(Collection)request.getAttribute("allRole");
                 Iterator itrRole=colRole.iterator();
                 while(itrRole.hasNext())
                 {
                  Role rdf=(Role)itrRole.next();
                  %>
                  <option value="<%=rdf.getRoleID().intValue()%>" ><%=rdf.getName()%></option>
                   <%
                   }
                   %>
                   </select>
               </td>
           </tr>
            <tr>
             <td>
                <table width=100% height=100% align=center border=0 class="popTable">
                 <tr class="tdcssone">
                   <td >
							<%
							for(int i = 0;i < SetOfPrivileges.AVAILABLE_PRIVILEGES_SIZE;i++)
							{
							%>
								<input type="checkbox" id="privileges<%=i+1%>" value="<%=i%>" disabled>&nbsp;&nbsp;<%=SetOfPrivileges.PRIVILEGE_NAMES[i]%>��<%= SetOfPrivileges.PRIVILEGE_MEMOS[i] %>��<br><br>
							<%
							}
							%>
                    </td>
                    </tr>

                  </table>
             </td>
           </tr>
         </table>
      </div>
<%
}
%>
</div>


<table border=0 width=100%>
        <tr>
                <td height="26" align="center" class="TTitle">
<%
String onclick = null;
if(operation.equals(SystemOperationServlet.SHOW_USER_ADD))
{
    onclick = "createUser()";
}
else if(operation.equals(SystemOperationServlet.SHOW_USER_MODIFY))
{
    onclick = "modifyUser()";
}
else if(operation.equals(SystemOperationServlet.SHOW_USER_MODIFY_SELF_PAGE))
{
    onclick = "modifySelf()";
}
%>
                <button onclick="<%= onclick %>">��&nbsp;&nbsp;��</button>
&nbsp;&nbsp<button onclick="cancel()">ȡ&nbsp;&nbsp;��</button>
                </td>
        </tr>
</table>
</form>

<script language="javascript">
<%
if(operation.equals(SystemOperationServlet.SHOW_USER_MODIFY) || operation.equals(SystemOperationServlet.SHOW_USER_MODIFY_SELF_PAGE))
{
%>
    //�޸��û�
    function modifyUser()
    {
      if(!validateUser() || !validateGroup() || !validateRole())
      {
          return false;
      }

<%
   itrUser=colUser.iterator();
   while(itrUser.hasNext())
   {
    User rdf=(User)itrUser.next();
%>
if(form1.userName.value=="<%=rdf.getName()%>" && form1.userName.value != "<%= user.getName()%>")
    {
     alert("���˺��Ѿ����ڣ����������룡")
     return false;
   }
<%}%>

      form1.operation.value = "<%=SystemOperationServlet.USER_MODIFY%>";
      window.opener.name = "userList";
      form1.submit();
      window.close();
}

    //�޸ĵ�ǰ��¼�û�
    function modifySelf()
    {
      if(!validateUser())
      {
          return false;
      }
<%
   itrUser=colUser.iterator();
   while(itrUser.hasNext())
   {
    User rdf=(User)itrUser.next();
%>
if(form1.userName.value=="<%=rdf.getName()%>" && form1.userName.value != "<%= user.getName()%>")
    {
     alert("���˺��Ѿ����ڣ����������룡")
     return false;
   }
<%}%>
      form1.operation.value = "<%=SystemOperationServlet.USER_MODIFY_SELF%>";
      form1.target = "_self";
      form1.submit();
}

function initUser(){
  //������Ϣ
  form1.userID.value = "<%= user.getUserID() %>"
  form1.userName.value = "<%= Convertor.getHTMLString(user.getName()) %>";
  form1.validated.checked = <%= user.isValidated()%>;
  form1.password.value = "********";
  form1.validatePassword.value = "********";
  form1.modifyPassword.value = "false";
  form1.enterpriseName.value = "<%= Convertor.getHTMLString(user.getEnterpriseName()) %>";
  form1.lawPersonCode.value = "<%= Convertor.getHTMLString(user.getLawPersionCode()) %>";
  form1.lawPersonName.value = "<%= Convertor.getHTMLString(user.getLawPersionName()) %>";
  form1.lawPersonPhone.value = "<%= Convertor.getHTMLString(user.getLawPersionPhone()) %>";
  form1.contactPerson.value = "<%= Convertor.getHTMLString(user.getContactPersionName()) %>";
  form1.contactPersonPhone.value = "<%= Convertor.getHTMLString(user.getContactPersionPhone()) %>";
  form1.contactPersonAddress.value = "<%= Convertor.getHTMLString(user.getContactAddress()) %>";
  form1.postcode.value = "<%= Convertor.getHTMLString(user.getPostcode()) %>";
  form1.contactPersonMobile.value = "<%= Convertor.getHTMLString(user.getContactPersionMobile()) %>";
  form1.fax.value = "<%= Convertor.getHTMLString(user.getFax()) %>";
  form1.email.value = "<%= Convertor.getHTMLString(user.getEmail()) %>";
  form1.memo.value = "<%= Convertor.getHTMLString(user.getMemo()) %>";
}

function initRole()
{
  //��ɫ
  form1.roleID.value = <%=user.getRole().getRoleID()%>;
  var selectIndexOpt = <%=user.getRole().getRoleID()%>
  for(var i=0;i<rightInfos.length;i++){
     if(rightInfos[i][0]==selectIndexOpt){
       for(var j=1;j<rightInfos[i].length;j++){
         document.getElementById("privileges"+j).checked = rightInfos[i][j];
       }
     }
  }
}
<%
}
%>

<%
if(operation.equals(SystemOperationServlet.SHOW_USER_MODIFY))
{
%>
initUser();
initRole();
<%
}
if(operation.equals(SystemOperationServlet.SHOW_USER_MODIFY_SELF_PAGE))
{
%>
initUser();
<%
}
%>


if(<%= request.getAttribute("success")%>==true){
   alert("������Ϣ�޸ĳɹ�");
   close();
}
</script>
</body>
</html>


