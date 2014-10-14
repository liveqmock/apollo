<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.usermanager.*" %>
<%@ page import="cn.com.youtong.apollo.servlet.*"%>
<%@ page import="cn.com.youtong.apollo.news.*"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.youtong.apollo.news.dao.*"%>
<table class="clsLogo" cellpadding="0" cellspacing="0" border=0>
<tr>
 <td width=290 background="../img/Logo.gif">
   <marquee height=28  behavior="scroll" scrollAmount=2 direction="up" onMouseOver="this.stop();" onMouseOut="this.start()">
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
 <td align="left">
   <img src="../img/logoTitle.gif">
 </td>
<%
   String loginInfo = "  ";
   if(request.getSession().getAttribute("loginUser")!=null){
      loginInfo = "欢迎您, "+((User)request.getSession().getAttribute("loginUser")).getName();
   }
%>
 <td align=right valign=bottom>
    <table border="0" cellspacing="0" cellpadding="0" border=0>
           <tr>
                  <td valign="buttom">
                      <font color=white><%=loginInfo%></font>&nbsp;&nbsp;&nbsp;
                      <a href='javascript:personManager();'><img src='../img/nav_profile.gif' alt='修改个人信息' border="0" align="absmiddle" width="28" height="25" title='修改个人信息'></a>
                      <a href='../servlet/login?operation=logout'><img src="../img/nav_logoff.gif" alt='退出系统' border="0" align="absmiddle" width="25" height="25" title='退出系统'></a>
                  </td>
          </tr>
      </table>


 </td>
</tr>
</table>
<script language="javascript">
//修改个人信息
function personManager(){
   url = '../servlet/systemOperation?operation=<%= cn.com.youtong.apollo.servlet.SystemOperationServlet.SHOW_USER_MODIFY_SELF_PAGE %>';
   window.open(url,"childAdd","status=no,height=570,width=500,top=75,left=250");
}
</script>
