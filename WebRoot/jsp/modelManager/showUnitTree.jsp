<%@ page contentType="text/html; charset=GBK" %>

<% //��ֹ��������汾ҳ��
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

<title>ѡ��λ</title>
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
 * ������ڵ��check box�����˷���
 */
function checkTreeNode(nodeID,checked)
{
  setCheckedTreeNode(nodeID,checked,document.all.isContainChildren.checked);
}

/**
 * check������ɺ�Ļص�����������ȱʡ��ʵ��
 * ��������unitIDselected
 */
function afterCheck()
{
    //���浱ǰ��unitIDselected
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
    //״̬�ı�ĵ�λ�ڵ�
    var nodes = checkProperty.nodes;
    
		
    // ���ˣ���ô�ظ��ĵ�λ
    var newNodes = new Array();
    for( var i=0; i<nodes.length; i++ )
    {
    	var index = -1;
    	for( var j=0; j<newNodes.length; j++ )
    	{
    		if( newNodes[j]._checkValue == nodes[i]._checkValue
    				&& newNodes[j].text == nodes[i].text )
    		{
    			// ��ʵֻ��Ҫ�ж�_checkValueֵ�Ϳ����ˣ�
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
    
		
    //��ʼ����unitIDselected
    for(var i = 0; i < nodes.length; i++)
    {
        //״̬�ı�ĵ�λ�ڵ��unitID������oldUnitIDs�е�index
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
                //����λID�ӵ�unitIDselected��
                unitIDselected[unitIDselected.length] = nodes[i]._checkValue;
                unitNameselected[unitNameselected.length] = nodes[i].text;
            }
        }
        else
        {
            if(!nodes[i]._checked)
            {
                //��unitIDselected��ȥ����Ӧ�ĵ�λID
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
			<input type="checkbox" name="isContainChildren" checked>�Ƿ�����¼�
			</td>
		
			<td>
			<input type="button" value="ȷ��" onclick="doneSelect()"/>
			&nbsp;
			<input type="reset" value="ȡ��" onclick="javascript:window.close()"/>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>			
</span>

</body>
</html>
