<%@ page contentType="text/html; charset=GBK" %>
<%@ page import="cn.com.youtong.apollo.servlet.*" %>
<%@ page import="java.util.*" %>
<%@ page import="cn.com.youtong.apollo.data.*" %>
<%@ page import="cn.com.youtong.apollo.common.ParamUtils"%>

<html>
<title>显示数据</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link id="webfx-tab-style-sheet" type="text/css" rel="stylesheet" href="../csslib/tab.webfx.css" />
<link type="text/css" rel="stylesheet" href="../csslib/webfxlayout.css" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>

<script type="text/javascript" src="../jslib/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../jslib/ext/ext-all.js"></script>
<script type="text/javascript" src="../jslib/ext/ui/Util.js"></script>
<link rel="stylesheet" type="text/css" href="../jslib/ext/resources/css/ext-all.css">

<script type="text/javascript" src="../jslib/jquery-1.5.1.min.js"></script>
<script type="text/javascript" src="../jslib/model/editfmtable.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
 function downExcel(type)
  {
	    if(!checkparam())
	  return;

   var selectedTableID = getSelectedID();

   if(type == "single")
   { 
      frmmain.action="model?operation=<%=ModelServlet.GET_EXCEL_FOR_SINGLE_TABLE%>&tableID="+selectedTableID;
   }
   else
   {
	  frmmain.action="model?operation=<%=ModelServlet.GET_EXCEL_FOR_ALL_TABLE%>";
   }

   frmmain.target="_blank";
   frmmain.submit();
  }
  
  function printPdf(type)
  {
	    if(!checkparam())
	  return;

    frmmain.action="model?operation=<%=ModelServlet.GET_PDF_FOR_ALL_TABLE%>";
    frmmain.target="_blank";
    frmmain.submit();
  }
  
  function printPriviewOfAll()
  {
    if(!checkparam())
	  return;

  if(type == "single")
   { 
      frmmain.action="model?operation=<%=ModelServlet.GET_PDF_FOR_SINGLE_TABLE%>&tableID="+selectedTableID;
   }
   else
   {
	      frmmain.action="model?operation=<%=ModelServlet.PRINT_ALL_TABLE%>";
   }

    frmmain.target="_blank";
    frmmain.submit();
   
  }

  function printPriviewOfOne()
  {
	 if(!checkparam())
	  return;

	  var selectedTableID = getSelectedID();
	  if(selectedTableID != "attachment")
	  {
		frmmain.action="model?operation=<%=ModelServlet.PRINT_ONE_TABLE%>&tableID="+selectedTableID;
		frmmain.target="_blank";
		frmmain.submit();
	  }
	  else
	  {
		alert("请选择数据标签页");
		return;
	  }
  }


function checkparam()
{  
	  if(frmmain.unitID.value=="null")
   {
      alert("请选择单位");
      return false;
   }
   if(frmmain.taskTimeID.value=="null")
   {
      alert("请选择时间");
      return false;
   }
   return true;
}

  function editData(readonly)
{
  if(!checkparam())
	  return;
  frmmain.readonly.value=readonly;
  frmmain.action = "model?operation=<%=ModelServlet.EIDT_DATA%>"
  frmmain.target = "_blank";
  frmmain.submit();
}

//-->
</SCRIPT>

<%
String canEditor="0";
if(request.getAttribute("canEditor")!=null)
{
	canEditor=(String)request.getAttribute("canEditor");
}

Iterator itrData = (Iterator)request.getAttribute(ModelServlet.HTML_DATA);
%>

<style type="text/css">

.dynamic-tab-pane-control .tab-page {

   overflow : hidden
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {

}

body {
	margin:		1px;
	width:		auto;
	height:		auto;
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
<body>


<form name="frmmain" method="post">
<input type="hidden" name="readonly">
<input type="hidden" name="unitID" value="<%=request.getParameter("unitID")%>">
<input type="hidden" name="taskTimeID" value="<%=request.getParameter("taskTimeID")%>">

</form>

 <table  width=100% border=0>
               <tr>
                   <td align="right">

<a href='#' onclick="openFmTable('<%=request.getParameter("unitID")%>')"><img src='../img/home.png' alt='编辑封面表' border="0" align="absmiddle" title='编辑封面表'></a>
&nbsp;
<%if(canEditor!=null&&canEditor.equals("1")){%>
<a href='javascript:editData(1);'><img src='../img/qtsw.gif' alt='编辑数据' border="0" align="absmiddle" title='编辑数据'></a>
&nbsp;
<%}else{%>
<a href='javascript:editData(0);'><img src='../img/printall.bmp' alt='打印数据' border="0" align="absmiddle" title='打印数据'></a>
&nbsp;
<%}%>


<a href='javascript:downExcel("single");'><img src='../img/excel.png' alt='导出Excel单张表' border="0" align="absmiddle" title='导出Excel单张表'></a>
&nbsp;
<a href='javascript:downExcel("all");'><img src='../img/excelAll.bmp' alt='导出Excel整套表' border="0" align="absmiddle" title='导出Excel整套表'></a>
&nbsp;
<a href='javascript:printPdf("single");'><img src='../img/pdf.bmp' alt='导出PDF单张表' border="0" align="absmiddle"title='导出PDF单张表'></a>
&nbsp;
<a href='javascript:printPdf("all");'><img src='../img/pdfAll.bmp' alt='导出PDF整套表' border="0" align="absmiddle"title='导出PDF整套表'></a>
&nbsp;
<!--
<a href='javascript:printPriviewOfOne();'><img src='../img/printall.bmp' alt='单表打印' border="0" align="absmiddle"title='单表打印'></a>
&nbsp;
<a href='javascript:printPriviewOfAll();'><img src='../img/printone.bmp' alt='套表打印' border="0" align="absmiddle"title='套表打印'></a>
-->
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>
</tr>
</table>

<div class="tab-pane" id="taskPane">

<script type="text/javascript">
var a=new Array();
var taskPane = new WebFXTabPane( document.getElementById( "taskPane" ),false);

function getSelectedID()
{
  if(taskPane!=null && taskPane.selectedIndex!=null)
  {
    return taskPane.pages[taskPane.selectedIndex].element.id;
  }else
  {
    return "";
  }
}

function downloadAttatchment()
{
  window.open("model?operation=<%=ModelServlet.DOWNLOAD_ATTACHMENT%>&taskTimeID=<%= request.getParameter("taskTimeID") %>&unitID=<%= request.getParameter("unitID") %>", "_self");
}

</script>

    <%
    	int i = 1;
      while(itrData.hasNext())
      {
         TableViewer tableViewer = (TableViewer) itrData.next();
    %>
     <div class="tab-page" id="<%=tableViewer.getTableID()%>">
        <h2 class="tab"><%=tableViewer.getTableName()%>&nbsp;</h2>
      <script type="text/javascript">
      var <%=tableViewer.getTableID()%> = taskPane.addTabPage( document.getElementById( "<%=tableViewer.getTableID()%>" ) );
      </script>
        <table  width="100%" height="100%" border="0">
          <tr><td>
           <%=tableViewer.getHtmlString()%>
          </td></tr>
        </table>
      </div>
     <%i++;}%>
			
    	<div class="tab-page" id="attachment">
				<h2 class="tab">
					附件&nbsp;
				</h2>

				<script type="text/javascript">
      a[<%=i%>] = taskPane.addTabPage(document.getElementById( "attachment"));
      </script>
				<table width="100%" height="430px;" border="0">
					<tr>
						<td>
							<%
										int tasktimeID = ParamUtils.getIntParameter(request, "taskTimeID",
										-1);
								String tmpunitID = ParamUtils.getParameter(request, "unitID");
								String url = request.getContextPath() + "/servlet/model"
										+ "?operation=" + ModelServlet.LIST_ATTACH + "&taskTimeID="
										+ tasktimeID + "&unitID=" + tmpunitID;
							%>
							<iframe width="100%" height="100%" frameborder=0 src="<%=url%>"></iframe>

							<!--<%-->
<!--if(request.getAttribute("hasAttachment") != null)-->
<!--{-->
<!--%>-->
							<!--<button onclick="downloadAttatchment();">下载</button>-->
							<!--<%-->
<!--}else{-->
<!--%>-->
							<!--没有附件-->
							<!--<%-->
<!--}-->
<!--%>-->
						</td>
					</tr>
				</table>
			</div>
</div>
<script type="text/javascript">
setupAllTabs();
</script>
</body>
</html>