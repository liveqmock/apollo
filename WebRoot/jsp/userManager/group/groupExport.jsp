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
  
%>




 <table class="clsContentListTable">
                  <tr class="clsTrHeader">
                      <td>ID号</td>
                      <td>组名称</td>
                      <td>创建日期</td>
                      <td>修改日期</td>
                      <td>备注</td>
                    
                    </tr>
                            <%
                             if(((Collection)request.getAttribute("allGroup")).size()!=0){
                               Collection colGroupInfo=(Collection)request.getAttribute("allGroup");
                               Object[] userObject= colGroupInfo.toArray();
                             
                               
                               for(int j=0;j<userObject.length;j++)
                               {
                                 Group rdf=(Group)userObject[j];
                            %>
                      <tr >
                            <td width=8%><%= Convertor.getHTMLString(rdf.getGroupID().toString())%>&nbsp;</td>
                            <td width=25% nowrap><%= Convertor.getHTMLString(rdf.getName()) %>&nbsp;</td>
                            <td width=13%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateCreated())) %>&nbsp;</td>
                            <td width=13%><%= Convertor.getHTMLString(Convertor.date2String(rdf.getDateModified())) %>&nbsp;</td>
                            <td width=17%><%= Convertor.getHTMLString(rdf.getMemo()) %>&nbsp;</td>
                          
                     </tr>
						  <%}%>
					  <%}%>
          </table>
