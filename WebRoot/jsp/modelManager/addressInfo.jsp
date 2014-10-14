<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.address.*" %>
<%@ page import="java.util.*" %>
<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script Language="JavaScript" src="../jslib/public.js"></script>
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
  var taskID = <%='"'+(String)request.getAttribute("taskID")+'"'%>;
  var unitID = <%='"'+(String)request.getAttribute("unitID")+'"'%>;
function addAddressInfo(){
    addressForm.action = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.ADD_ADDRESS_INFO%>";
    if(addressForm.phone.value==""){
        alert("请填写电话号码！");
        addressForm.phone.focus();
        return false;
    }
    if(isNaN(addressForm.phone.value)==true){
        alert("电话号码格式不正确(只能填写数字)！");
        addressForm.phone.focus();
        return false;
    }
    if(addressForm.mobile.value==""){
        alert("请填写手机号码！");
        addressForm.mobile.focus();
        return false;
    }
    if(isNaN(addressForm.mobile.value)==true){
        alert("手机号码格式不正确(只能填写数字)！");
        addressForm.mobile.focus();
        return false;
    }
    if(addressForm.fax.value==""){
        alert("请填写传真号码！");
        addressForm.fax.focus();
        return false;
    }
    if(isNaN(addressForm.fax.value)==true){
        alert("传真号码格式不正确(只能填写数字)！");
        addressForm.fax.focus();
        return false;
    }
    if(addressForm.email.value==""){
        alert("请填写电子邮件地址！");
        addressForm.email.focus();
        return false;
    }
    if(ismail(addressForm.email.value)!=true){
        alert("电子邮件格式不正确(示例:tom@tom.com)!");
        addressForm.email.focus();
        return false;
    }
    if(taskID==null){
        alert("没有任务信息！");
        return false;
    }
    if(unitID==null){
        alert("没有单位信息！");
        return false;
    }
    addressForm.taskID.value = taskID;
    addressForm.unitID.value = unitID;
    addressForm.submit();
}

function displayAllAddressInfo(){
     url = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>";
     window.location = url;
}
</script>

<body>
 <form name="addressForm" method="post" style="margin:0">
<input type="hidden" name="unitName" value=<%='"'+(String)request.getAttribute("unitName")+'"'%>>
<input type="hidden" name="unitID">
<input type="hidden" name="taskID">
<table height=90% width=90% border=1 style="margin-left:1cm" class="xsltTable">
      <tr class="xsltTr"><td colspan=2 align=center>新增催报信息</td>
      </tr>
      <tr class="xsltTr"><td width=25% align=center>单位名称:</td>
         <td ><%=(String)request.getAttribute("unitName")%></td>
      </tr>
      <tr class="xsltTr"><td align=center>电话:</td>
         <td><input type="text" name="phone" value="" size=30 maxlength=20></td>
      </tr>
      <tr class="xsltTr"><td align=center>手机:</td>
         <td><input type="text" name="mobile" value="" size=30 maxlength=20></td>
      </tr>
      <tr class="xsltTr"><td align=center>传真:</td>
         <td><input type="text" name="fax" value="" size=30 maxlength=20></td>
      </tr>
      <tr class="xsltTr"><td align=center>电子邮件:</td>
         <td><input type="text" name="email" value="" size=40 maxlength=20></td>
      </tr>
      <tr class="xsltTr">
             <td align=center>&nbsp;</td>
             <td>
                  <!-- input type="button" onclick=deleteAddressInfo() name="deleteInfo" value=" 删  除 " disabled -->
                 <input type="button" onclick=addAddressInfo() name="submitInfo" value=" 保 存 ">
                  &nbsp;&nbsp;&nbsp;&nbsp;
                 <a href="javascript:displayAllAddressInfo()">显示所有催报信息</a>
             </td>
      </tr>
 </table>
</form>
             <%
                AddressInfo addressInfoFlag = (AddressInfo)request.getAttribute("addressInfo");
                if(addressInfoFlag!=null){
             %>
                     <script language="JavaScript">
                          //addressForm.submitInfo.value = " 修  改 ";
                          addressForm.email.value=<%='"'+addressInfoFlag.getEmail()+'"'%>;
                          addressForm.phone.value=<%='"'+addressInfoFlag.getPhone()+'"'%>;
                          addressForm.mobile.value=<%='"'+addressInfoFlag.getMobile()+'"'%>;
                          addressForm.fax.value=<%='"'+addressInfoFlag.getFax()+'"'%>;
                          //addressForm.deleteInfo.disabled = false;
                       <%if(request.getAttribute("flag")!=null){%>
                          alert("催报信息保存成功!");
                       <%}%>
                     </script>
            <%}%>
</body>
<script language="javascript">
   addressForm.phone.focus();
</script>
</html>
