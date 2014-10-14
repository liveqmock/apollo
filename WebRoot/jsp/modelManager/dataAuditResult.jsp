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
<title>数据审核</title>
</head>
<%
  response.setHeader("Pragma","No-cache");
  response.setHeader("Cache-Control","no-cache");
  response.setDateHeader("Expires", 0);
  Task task = (Task)request.getAttribute("task");
  Object flag = request.getAttribute("flag");
  Object auditError = request.getAttribute("auditError");
  Object auditScript = request.getAttribute("auditScript");
  List auditResultList = (List)request.getAttribute("auditResultList");
  String taskTimeID = request.getParameter("taskTimeID");
  String msgtype = (String)request.getParameter("msgtype");
  String execerror = (String)request.getAttribute("execerror");
%>
<%!
StringBuffer initBuffer()
{
	StringBuffer buf = new StringBuffer();
	buf.append("<html><head><title>审核结果</title><link rel=stylesheet type=text/css href=../csslib/main.css /></head><body>");
	buf.append("<script language=javascript>parent.document.all.dispinfo.style.display='none';");
	buf.append("function openData(unitID,timeID){");
	buf.append("window.open('model?operation=");
	buf.append(ModelServlet.SHOW_DATA);
	buf.append("&unitID='+");
	buf.append("unitID+'&taskTimeID='+");
	buf.append("timeID");
	buf.append(",'_blank');");
	buf.append("}");
	buf.append("</script>");
	return buf;
}
void appendError(StringBuffer buf,String name,String content,int pos)
{
	String cls;
	if(pos%2==1) cls="TrDark";
	else cls="TrLight";
	buf.append("<tr class=");
	buf.append(cls);
        buf.append("><td>错误</td><td>");
	buf.append(name);
	buf.append("</td><td>");
	buf.append(content);
	buf.append("</td></tr>");
}
void appendWarning(StringBuffer buf,String name,String content,int pos)
{
	String cls;
	if(pos%2==1) cls="TrDark";
	else cls="TrLight";
	buf.append("<tr class=");
	buf.append(cls);
        buf.append("><td>警告</td><td>");
	buf.append(name);
	buf.append("</td><td>");
	buf.append(content);
	buf.append("</td></tr>");
}
%>
<%
    //第一次运行
    if(flag!=null)
    {
    }
    if(execerror!=null) //是计算
    {
	if(execerror.equals("ok"))
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('计算成功');</script>");
	else
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('计算');</script>");
    }
    //如果没有设置当前的活动脚本
    else if(auditError!=null)
    {
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('没有激活当前脚本方案');</script>");
    }
    //当前活动脚本错误
    else if(auditScript!=null)
    {
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('当前活动脚本错误,请检查你编写的脚本内容');</script>");
    }
    //有审核结果存在存在
    else if(auditResultList!=null)
    {
	StringBuffer buf = initBuffer();
	Iterator iterResult = null;
	Iterator iter = null;
	String content = null;
	iterResult = auditResultList.iterator();
	while(iterResult.hasNext())
	{
	Map map = (Map)iterResult.next();
	String unitID = (String)map.get("unitID");
	String unitName = (String)map.get("unitName");
	buf.append("<div style='FONT-SIZE:9pt'>单位名称:<a href=javascript:openData('");
	buf.append(unitID);
	buf.append("','");
	buf.append(taskTimeID);
	buf.append("')>");
	buf.append(unitName);
	buf.append("</a>&nbsp单位编号:");
	buf.append(unitID);
	buf.append("</div><table width=100% cellspacing=1 cellpadding=0 border=0>");
	buf.append("<tr  class=clsTrHeader><td width=50px>类型</td><td width=100px>表名</td><td>内容</td></tr>");
	List results = (List)map.get("result");
	Iterator iter2 = results.iterator();
	while(iter2.hasNext())
	{
		Map resultmap = (Map)iter2.next();
		AuditResult auditResult = (AuditResult)resultmap.get("content");
		String name = (String)resultmap.get("name");
		int i=0;
		if(!msgtype.equals("warning"))
                {
		List errors = auditResult.getErrors();
		if(errors.size()>0)
		{
        		iter = errors.iterator();
			while(iter.hasNext())
			{
				content = (String)iter.next();
				appendError(buf,name,content,i);
				i++;
			}
                }
                }
		if(!msgtype.equals("error"))
                {
		List warnings = auditResult.getWarnings();
		if(warnings.size()>0)
		{
        		iter = warnings.iterator();
			while(iter.hasNext())
			{
				content = (String)iter.next();
				appendWarning(buf,name,content,i);
				i++;
			}
		}
                }
        }
        buf.append("</table><br/><br/>");
	}
	if(buf!=null)
	{
	 buf.append("</body></html>");
	 out.print(buf.toString());
	}
    }
%>
