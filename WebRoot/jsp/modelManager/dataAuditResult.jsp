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
<title>�������</title>
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
	buf.append("<html><head><title>��˽��</title><link rel=stylesheet type=text/css href=../csslib/main.css /></head><body>");
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
        buf.append("><td>����</td><td>");
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
        buf.append("><td>����</td><td>");
	buf.append(name);
	buf.append("</td><td>");
	buf.append(content);
	buf.append("</td></tr>");
}
%>
<%
    //��һ������
    if(flag!=null)
    {
    }
    if(execerror!=null) //�Ǽ���
    {
	if(execerror.equals("ok"))
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('����ɹ�');</script>");
	else
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('����');</script>");
    }
    //���û�����õ�ǰ�Ļ�ű�
    else if(auditError!=null)
    {
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('û�м��ǰ�ű�����');</script>");
    }
    //��ǰ��ű�����
    else if(auditScript!=null)
    {
		out.print("<script language=javascript>parent.document.all.dispinfo.style.display='none';alert('��ǰ��ű�����,�������д�Ľű�����');</script>");
    }
    //����˽�����ڴ���
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
	buf.append("<div style='FONT-SIZE:9pt'>��λ����:<a href=javascript:openData('");
	buf.append(unitID);
	buf.append("','");
	buf.append(taskTimeID);
	buf.append("')>");
	buf.append(unitName);
	buf.append("</a>&nbsp��λ���:");
	buf.append(unitID);
	buf.append("</div><table width=100% cellspacing=1 cellpadding=0 border=0>");
	buf.append("<tr  class=clsTrHeader><td width=50px>����</td><td width=100px>����</td><td>����</td></tr>");
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
