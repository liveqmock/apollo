<%@ page contentType="text/html; charset=GBK" %>

<% //防止浏览器缓存本页面
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />
<link rel="stylesheet" type="text/css" href="../csslib/main.css">
<link type="text/css" rel="stylesheet" href="../csslib/xtree.css" />

<style type="text/css">

.dynamic-tab-pane-control .tab-page {
	height:		450px;
	overflow:	visible;
}

.dynamic-tab-pane-control .tab-page .dynamic-tab-pane-control .tab-page {
	height:		420px;
       overflow:	visible;
}


form {
	margin:		4;
	padding:	1;
}

/* over ride styles from webfxlayout */

body {
	margin:		0px;
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

<script type="text/javascript" src="../jslib/public.js"></script>
<script type="text/javascript" src="../jslib/function.js"></script>
<script type="text/javascript" src="../jslib/tabpane.js"></script>

<script>
var unitIDselected = new Array();
var unitNameselected = new Array();

<%
java.util.Collection units = (java.util.Collection)
			request.getAttribute( "units" );
if( units != null )
{
	int index = 0;
	for( java.util.Iterator iter = units.iterator(); iter.hasNext(); )
	{
		cn.com.youtong.apollo.data.UnitTreeNode node = 
			(cn.com.youtong.apollo.data.UnitTreeNode) iter.next();
		
		%>
unitIDselected[<%=index%>] = "<%=node.id()%>";
unitNameselected[<%=index%>] = "<%=node.getUnitName()%>";
		<%
		index++;
	}
} 
%>

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

</script>

<title>选择单位</title>
</head>




<body>

<script type="text/javascript" src="../jslib/xtree.js"></script>
<script type="text/javascript" src="../jslib/xmlextras.js"></script>
<script type="text/javascript" src="../jslib/xloadtree.js"></script>
<script type="text/javascript" src="../jslib/xradiotree.js"></script>
<script type="text/javascript" src="../jslib/xradioloadtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxtree.js"></script>
<script type="text/javascript" src="../jslib/xcheckboxloadtree.js"></script>
<script type="text/javascript" src="../jslib/calendar/popcalendar.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_layers.js"></script>
<script type="text/javascript" src="../jslib/calendar/lw_menu.js"></script>

<script language="javascript">

/**
 * 点击树节点的check box触发此方法
 */
function checkTreeNode(nodeID,checked)
{
  setCheckedTreeNode(nodeID,checked,document.all.isContainChildren.checked);
}

/**
 * check操作完成后的回调函数，覆盖缺省的实现
 * 更新数组unitIDselected
 */
function afterCheck()
{
    //保存当前的unitIDselected
    var oldUnitIDs = new Array();
    for(var i = 0; i < unitIDselected.length; i++)
    {
        if(unitIDselected[i] != null)
        {
            oldUnitIDs[oldUnitIDs.length] = unitIDselected[i];
        }
    }
    
    var oldNames = new Array();
    for(var i = 0; i < unitNameselected.length; i++)
    {
        if(unitNameselected[i] != null)
        {
            oldNames[oldNames.length] = unitNameselected[i];
        }
    }
    //状态改变的单位节点
    var nodes = checkProperty.nodes;
    
		
    // 过滤，那么重复的单位
    var newNodes = new Array();
    for( var i=0; i<nodes.length; i++ )
    {
    	var index = -1;
    	for( var j=0; j<newNodes.length; j++ )
    	{
    		if( newNodes[j]._checkValue == nodes[i]._checkValue
    				&& newNodes[j].text == nodes[i].text )
    		{
    			// 其实只需要判断_checkValue值就可以了，
    			index = j;
    			break;
    		}
    	}
    	
    	if( index == -1 )
    	{
    		newNodes[newNodes.length] = nodes[i];
    	}
    }
    
    nodes = newNodes;
    
		
    //开始更新unitIDselected
    for(var i = 0; i < nodes.length; i++)
    {
        //状态改变的单位节点的unitID在数组oldUnitIDs中的index
        var index = null;
        
        for(var j = 0; j < oldUnitIDs.length; j++)
        {
            if(nodes[i]._checkValue == oldUnitIDs[j])
            {
                index = j;
                break;
            }
        }

        if(index == null)
        {
            if(nodes[i]._checked)
            {
                //将单位ID加到unitIDselected中
                unitIDselected[unitIDselected.length] = nodes[i]._checkValue;
                unitNameselected[unitNameselected.length] = nodes[i].text;
            }
        }
        else
        {
            if(!nodes[i]._checked)
            {
                //从unitIDselected中去掉对应的单位ID
                for(k = 0; k < unitIDselected.length; k++)
                {
                    if(unitIDselected[k] == nodes[i]._checkValue)
                    {
                        unitIDselected[k] = null;
                        unitNameselected[k] = null;
                    }
                }
            }
        }
    }
}

</script>

<%
//cn.com.youtong.apollo.servlet.UtilServlet.showCheckboxUnitTree( request, response, null, "checkboxUnitTree" );
%>
<span id="selectUnits">
	<table width="99%" height="85%">		
		<tr height="80%">
			<td colspan="2" width="100%">				
			<div style="width: 100%; height: 100%; overflow: auto"><%=(String)request.getAttribute("checkboxUnitTree")%></div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2"><hr color="#c4c9e1" size="1"/></td>
		</tr>
		
		<tr>
			<td>
			<input type="checkbox" name="isContainChildren" checked>是否包含下级
			</td>
		
			<td>
			<input type="button" value="确定" onclick="doneSelect()"/>
			&nbsp;
			<input type="reset" value="取消" onclick="javascript:window.close()"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>			
</span>

</body>
</html>
