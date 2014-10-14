<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="cn.com.youtong.apollo.common.*" %>
<%@ page import="cn.com.youtong.apollo.address.*" %>
<%@ page import="java.util.*" %>
<% //��ֹ��������汾ҳ��
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
</head>
<script language="JavaScript">
  var taskID = <%='"'+(String)request.getAttribute("taskID")+'"'%>;
  var unitID = <%='"'+(String)request.getAttribute("unitID")+'"'%>;

function deleteAddressInfo(){
   url = "../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DELETE_ADDRESS_INFO%>"+"&unitID="+unitID+"&taskID="+taskID+"&unitName="+encodeURIComponent(<%='"'+(String)request.getAttribute("unitName")+'"'%>);
   window.location = encodeURI(url);
}
</script>
<body>
           <table class="clsContentTable">
               <tr height=5% class="clsTrContext">
                   <td align=center>�߱���Ϣ</td>
               </tr>
               <tr valign=top class="clsTrContext">
                   <td>
                            <%
                              Collection col = (Collection)request.getAttribute("addressInfo");
                              if(col.size()!=0){
                            %>
                             <table width="100%">
                              <tr align="center" style="BACKGROUND-COLOR: #e7e7e4">
                                <td align="center" width=20%>��λ����</TD>
                                <td align="center" width=20%>�����ʼ�</TD>
                                <td align="center" width=20%>�绰</TD>
                                <td align="center" width=20%>�ֻ�</TD>
                                <td align="center" width=20%>����</TD>
                              </TR>
                        <%
                       Object[] userObject= col.toArray();
                       String curPage = (String)request.getAttribute("curPage");
                       String maxRowCount = (String)request.getAttribute("maxRowCount");
                       String rowsPerPage = (String)request.getAttribute("rowsPerPage");
                       String maxPage = (String)request.getAttribute("maxPage");
                       int i=0;
                       int curPg = Integer.parseInt(curPage);
                       int maxPg = Integer.parseInt(maxPage);
                       int stopFlag = Integer.parseInt(rowsPerPage)*curPg;
                       if(curPg==maxPg){
                          stopFlag = Integer.parseInt(maxRowCount);
                       }
                       for(int j=Integer.parseInt(rowsPerPage)*(curPg-1);j<stopFlag;j++)
                       {
                                 AddressInfo addressInfo = (AddressInfo)userObject[j];
                        %>
                            <tr <%=(((i%2)==1)?"class=\"TrLight\"":"class=\"TrDark\"")%>>
                              <td align="center" width=20%><%=addressInfo.getUnitTreeNode().getUnitName()%></TD>
                              <td align="center" width=20%><%=addressInfo.getEmail()%></TD>
                              <td align="center" width=20%><%=addressInfo.getPhone()%></TD>
                              <td align="center" width=20%><%=addressInfo.getMobile()%></TD>
                              <td align="center" width=20%><%=addressInfo.getFax()%></TD>
                            </TR>
                            <%
                                i++;}
                             %>
             <tr class="clsContentListTable">
                 <td align="right" colspan=8>
                          �����û�:<%=maxRowCount%>&nbsp;
                          ÿҳ<%=rowsPerPage%>��&nbsp;
                          ��ǰ��<%=curPage%>ҳ/
                          ��<%=maxPage%>ҳ
                          &nbsp;&nbsp;&nbsp;&nbsp;

                    <%if(curPage.equals("1")){
                    %>
                        <A disabled>��ҳ</A>
                        <A disabled>��һҳ</A>
                    <%}else{%>
                        <A HREF="javascript:gotoFirstPage()">��ҳ</A>
                        <A HREF="javascript:gotoPrePage()">��һҳ</A>
                    <%}%>
                    <%if(curPage.equals(maxPage)){
                    %>
                        <A disabled>��һҳ</A>
                        <A disabled>βҳ</A>
                    <%}else{%>
                        <A HREF="javascript:gotoNextPage()">��һҳ</A>
                        <A HREF="javascript:gotoLastPage()">βҳ</A>
                         &nbsp;&nbsp;
                    <%}%>
                 </td>
             </tr>
<script language="javascript">

function gotoFirstPage(){
window.location="../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>";
}
function gotoPrePage(){
window.location="../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>"+"&curPage="+"<%=String.valueOf(curPg-1)%>";
}
function gotoNextPage(){
window.location="../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>"+"&curPage="+"<%=String.valueOf(curPg+1)%>";
}
function gotoLastPage(){
window.location="../servlet/model?operation=<%=cn.com.youtong.apollo.servlet.ModelServlet.DISPLAY_ALL_ADDRESS_INFO%>"+"&curPage="+"<%=maxPage%>";
}
</script>
                           <%
                               } else{
                            %>
                             <table width="100%">
                              <tr style="BACKGROUND-COLOR: #e7e7e4" align="center">
                                <td align="center" width=20%>��λ����</TD>
                                <td align="center" width=20%>�����ʼ�</TD>
                                <td align="center" width=20%>�绰</TD>
                                <td align="center" width=20%>�ֻ�</TD>
                                <td align="center" width=20%>����</TD>
                              </TR>
                               <tr align="center">
                                <td align="center" colspan=5>û�д߱���Ϣ</TD>
                              </TR>
                            <%}%>
                            </TABLE>

                  </td>
               </tr>
          </table>
</body>
</html>
