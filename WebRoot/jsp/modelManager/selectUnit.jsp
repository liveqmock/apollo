<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>选择单位</title>
</head>

<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>

<script>
var unitIDselected = new Array();
var unitNameselected = new Array();
var selectType = "";
</script>

<script>

function doneSelect()
{
	<%
	String strCloseAfterDone = request.getParameter( "closeAfterDone" );
	String doAfterDone = request.getParameter( "doAfterDone" );
	
	boolean closeAfterDone = true;
	if( strCloseAfterDone != null 
			&& strCloseAfterDone.equalsIgnoreCase( "false" ) )
		closeAfterDone = false;
	
	
	%>
	
	<%
	if( doAfterDone != null ) {
	%>
	<%=doAfterDone%>;
	<% } %>
	
	<%
	if( closeAfterDone ) {
	%>
	window.close();
	<% } %>
}

function byList()
{
	selectType = "byList";
	
	var oListA = topFrame.document.getElementById( "listA" );
	var oTreeA = topFrame.document.getElementById( "treeA" );
	
	oListA.className = "clsAlight";
	oTreeA.className = "clsAdark";
	
	var oListTd = topFrame.document.getElementById( "listTd" );
	var oTreeTd = topFrame.document.getElementById( "treeTd" );
	
	oListTd.className = "clsSelectedToolTab";
	oTreeTd.className = "clsToolTabs";
	
	var oForm = topFrame.document.getElementById( "listForm" );
	
	/**removeHiddenField( oForm, "unitID" );	
	
	for( var i=0; i<unitIDselected.length; i++ )
	{
		var oInput = document.createElement( "input" );
		oInput.type = "hidden";
		oInput.name = "unitID";
		oInput.value = unitIDselected[i];
		
		oForm.appendChild( oInput );
	}*/
	
	var sAction = "utilServlet";
	if( unitIDselected.length > 0 )
	{
		sAction = sAction + "?unitID=" + encodeURIComponent( unitIDselected[0] );
	}
	for( var i=1; i<unitIDselected.length; i++ )
	{
		sAction = sAction + "&unitID=" + encodeURIComponent( unitIDselected[i] );
	}
	
	oForm.action = sAction;
	oForm.submit();
}

function doneSelectByList()
{
	unitIDselected = mainFrame.getSelectedUnitID();
	unitNameselected = mainFrame.getSelectedUnitName();	
	
	doneSelect();
}

function byTree()
{
	selectType = "byTree";
	
	var oListA = topFrame.document.getElementById( "listA" );
	var oTreeA = topFrame.document.getElementById( "treeA" );
	
	oListA.className = "clsAdark";
	oTreeA.className = "clsAlight";
	
	var oListTd = topFrame.document.getElementById( "listTd" );
	var oTreeTd = topFrame.document.getElementById( "treeTd" );
	
	oListTd.className = "clsToolTabs";
	oTreeTd.className = "clsSelectedToolTab";
	
	var oForm = topFrame.document.getElementById( "treeForm" );
	/**removeHiddenField( oForm, "unitID" );	
	
	for( var i=0; i<unitIDselected.length; i++ )
	{
		var oInput = document.createElement( "input" );
		oInput.type = "hidden";
		oInput.name = "unitID";
		oInput.value = unitIDselected[i];
		
		oForm.appendChild( oInput );
	}*/
	
	var sAction = "model";
	if( unitIDselected.length > 0 )
	{
		sAction = sAction + "?unitID=" + encodeURIComponent( unitIDselected[0] );
	}
	for( var i=1; i<unitIDselected.length; i++ )
	{
		sAction = sAction + "&unitID=" + encodeURIComponent( unitIDselected[i] );
	}
	
	oForm.action = sAction;
	
	oForm.submit();
}

function doneSelectByTree()
{
	unitIDselected = mainFrame.unitIDselected;
	unitNameselected = mainFrame.unitNameselected;
	
	doneSelect();
}

function doOnload()
{
	unitIDselected = window.opener.unitIDselected;
	unitNameselected = window.opener.unitNameselected;
	
	byList();
}

</script>

<frameset rows="10%,90%" frameborder="NO" border="1" framespacing="0" resize="true">
  <frame src="utilServlet?operation=<%=cn.com.youtong.apollo.servlet.UtilServlet.SELECT_UNIT_HEAD%>" name="topFrame" scrolling="NO" noresize >
  <frame src="" name="mainFrame">
</frameset>
<noframes>

	<body>
	您的浏览器不支持框架集
	</body>
</noframes>
</html>