<%@ page contentType="text/html; charset=GB2312" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*"%>
<%@ page import="cn.com.youtong.apollo.common.*"%>
<%
  String tableName="userinfo";
  response.reset();
  response.setContentType("application/vnd.ms-excel;charset=GB2312");
  response.setHeader("Content-disposition",
                       "attachment; filename=" + tableName +
                       ".xls");
  
   Collection colRole=(Collection)request.getAttribute("allRole");
%>




  <table class="clsContentListTable">
               
                   <tr class="clsTrHeader">
                      <td width=8%>ID号</td>
                      <td width=19%>详细信息</td>
                      <td width=15%>创建日期</td>
                      <td width=15%>修改日期</td>
                      <td width=20%>备注</td>
                    
                    </tr>
                           <% 
                                 Iterator iteRole=colRole.iterator();
                                 int i=0;
                                 while(iteRole.hasNext())
                                 {
                                   Role rdf=(Role)iteRole.next();

                           %>
                  <tr >
                        <td width=15%><%= Convertor.getHTMLString(rdf.getRoleID().toString())%>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(rdf.getName()) %>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateCreated())) %>&nbsp;</td>
                        <td width=15%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateModified())) %>&nbsp;</td>
                        <td width=20%><%= Convertor.getHTMLString(rdf.getMemo()) %>&nbsp;</td>
                      
                 </tr>
					 <%}%>
                
        </table>
