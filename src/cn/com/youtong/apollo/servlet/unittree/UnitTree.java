package cn.com.youtong.apollo.servlet.unittree;

import java.util.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.servlet.unittree.xml.*;
import java.io.*;
import org.exolab.castor.xml.*;
import cn.com.youtong.apollo.common.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.task.*;

abstract public class UnitTree
{
    private Log log = LogFactory.getLog(this.getClass());
    private String taskID;

    /**checkbox框的动态加载树*/
    public static final String CHECKBOX_XLOAD_TREE = "WebFXCheckBoxLoadTree";
    /**radio框的动态加载树*/
    public static final String RADIO_XLOAD_TREE = "WebFXRadioLoadTree";
    /**默认树的形式（无选择框动态加载）*/
    public static final String DEFAULT_TREE = "WebFXLoadTree";

    /**
     * 基建并企业表
     */
    private static final String BASIC_MERGE_ICON = "";

    /**
     * 事业并企业表
     */
    private static final String ENTERPRISE_MERGE_ICON = "../img/icon_4.gif";

    /**
     * 境外并企业表
     */
    private static final String FOREIGN_MERGE_ICON = "../img/icon_3.gif";

    /**
     * 金融并企业表
     */
    private static final String FINANCE_MERGE_ICON = "../img/icon_2.gif";

    /**
     * 集团差额表
     */
    private static final String GROUP_DIFF_ICON = "../img/icon_1.gif";

    /**
     * 单户表
     */
    private static final String GRASS_ROOT_ICON = "../img/icon_0.gif";

    /**
     * 完全汇总表
     */
    private static final String FULL_GATHER_ICON = "../img/icon_7.gif";

    /**
     * 集团汇总表
     */
    private static final String GROUP_GATHER_ICON = "../img/icon_9.gif";

    /**
     * 选择汇总表
     */
    private static final String SELECT_GATHER_ICON = "../img/icon_h.gif";

    public UnitTree(String taskID)
    {
        this.taskID = taskID;
    }

    /**
     * 获得子树的xml代码
     * @param unitTree 单位树对象
     * @param checkedUnitIDs 要选中的树节点的ID号
     * @return XML字符串
     * @throws Warning
     */
    public String getUnitTree(UnitTreeNode unitTree, String[] checkedUnitIDs) throws
        Warning
    {
        TreeRoot treeRoot = new TreeRoot();
        StringWriter strWriter = null;

        try
        {
            Iterator itrSubTree = unitTree.getChildren();
            while (itrSubTree.hasNext())
            {
                UnitTreeNode tempNode = (UnitTreeNode) itrSubTree.next();
                Tree tree = new Tree();
                tree.setText(tempNode.getUnitName());
                if (tempNode.getChildren().hasNext())
                {
                    tree.setSrc(this.servletOperation(tempNode, checkedUnitIDs));
                }
                tree.setAction(this.onClickAction(tempNode));
                boolean isShow =false;
                if((treeStyle().equals(CHECKBOX_XLOAD_TREE))||(!treeStyle().equals(CHECKBOX_XLOAD_TREE)&&tempNode.getDisplay()==1)) isShow=true;
                if(isShow){
	                boolean tempBool = false;
	                if(treeStyle().equals(CHECKBOX_XLOAD_TREE)){
		                if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
		                {
		                    for (int j = 0; j < checkedUnitIDs.length; j++)
		                    {
		                        if (tempNode.id().equals(checkedUnitIDs[j]))
		                        {
		                            tempBool = true;
		                        }
		                    }
		                    tree.setChecked(tempBool);
		                }
		                else
		                {
		                	if(tempNode.getDisplay()==1) tempBool=true;
		                    tree.setChecked(tempBool);
		                }
	                }
	
	                tree.setCheckValue(this.checkValue(tempNode));
	                String icon = getTreeIcon(tempNode);
	                tree.setIcon(icon);
	                tree.setOpenIcon(icon);
	                treeRoot.addTree(tree);
                }
            }
            strWriter = new StringWriter();
            treeRoot.marshal(strWriter);
        }
        catch (ValidationException ex)
        {
            throw new Warning(ex);
        }
        catch (MarshalException ex)
        {
            throw new Warning(ex);
        }
        log.debug(strWriter.toString());
        String result = null;
        try
        {
            if (strWriter != null)
            {
                result = strWriter.toString();
                strWriter.close();
            }
        }
        catch (IOException ex1)
        {
            ex1.printStackTrace();
        }
        return result;
    }
    
    public String getUnitTreeOfShow(UnitTreeNode unitTree, String[] checkedUnitIDs) throws
    Warning
{
    TreeRoot treeRoot = new TreeRoot();
    StringWriter strWriter = null;

    try
    {
        Iterator itrSubTree = unitTree.getChildren();
        while (itrSubTree.hasNext())
        {
            UnitTreeNode tempNode = (UnitTreeNode) itrSubTree.next();
            Tree tree = new Tree();
            tree.setText(tempNode.getUnitName());
            if (tempNode.getChildren().hasNext())
            {
                tree.setSrc(this.servletOperationOfShow(tempNode, checkedUnitIDs));
            }
            tree.setAction(this.onClickAction(tempNode));
            boolean isShow =false;
            if((treeStyle().equals(CHECKBOX_XLOAD_TREE))||(!treeStyle().equals(CHECKBOX_XLOAD_TREE)&&tempNode.getDisplay()==1)) isShow=true;
            if(isShow){
                if(treeStyle().equals(CHECKBOX_XLOAD_TREE)){
	                if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
	                {
	                    for (int j = 0; j < checkedUnitIDs.length; j++)
	                    {
	                        if (tempNode.id().equals(checkedUnitIDs[j]))
	                        {
	                        	tree.setCheckValue(this.checkValue(tempNode));
	                            String icon = getTreeIcon(tempNode);
	                            tree.setIcon(icon);
	                            tree.setOpenIcon(icon);
	                            treeRoot.addTree(tree);
	                        }
	                    }
	                }
	                else
	                {
	                	if(tempNode.getDisplay()==1) {
	                		tree.setCheckValue(this.checkValue(tempNode));
		                    String icon = getTreeIcon(tempNode);
		                    tree.setIcon(icon);
		                    tree.setOpenIcon(icon);
		                    treeRoot.addTree(tree);
	                	}
	                }
                }
            }
        }
        strWriter = new StringWriter();
        treeRoot.marshal(strWriter);
    }
    catch (ValidationException ex)
    {
        throw new Warning(ex);
    }
    catch (MarshalException ex)
    {
        throw new Warning(ex);
    }
    log.debug(strWriter.toString());
    String result = null;
    try
    {
        if (strWriter != null)
        {
            result = strWriter.toString();
            strWriter.close();
        }
    }
    catch (IOException ex1)
    {
        ex1.printStackTrace();
    }
    return result;
}

    /**
     * 得到单位树节点的图标
     * @param unit 单位
     * @return 图标路径
     */
    private String getTreeIcon(UnitTreeNode unit)
    {
		String reportType= unit.getReportType();
		if(reportType == null)
		{
			return "";
		}
        else if (reportType.equals(ReportType.BASIC_MERGE_TYPE))
        {
            return BASIC_MERGE_ICON;
        }
        else if (reportType.equals(ReportType.ENTERPRISE_MERGE_TYPE))
        {
            return ENTERPRISE_MERGE_ICON;
        }
        else if (reportType.equals(ReportType.FINANCE_MERGE_TYPE))
        {
            return FINANCE_MERGE_ICON;
        }
        else if (reportType.equals(ReportType.FOREIGN_MERGE_TYPE))
        {
            return FOREIGN_MERGE_ICON;
        }
        else if (reportType.equals(ReportType.FULL_GATHER_TYPE))
        {
            return FULL_GATHER_ICON;
        }
        else if (reportType.equals(ReportType.GRASS_ROOT_TYPT))
        {
            return GRASS_ROOT_ICON;
        }
        else if (reportType.equals(ReportType.GROUP_DIFF_TYPE))
        {
            return GROUP_DIFF_ICON;
        }
        else if (reportType.equals(ReportType.GROUP_GATHER_TYPE))
        {
            return GROUP_GATHER_ICON;
        }
        else if (reportType.equals(ReportType.SELECT_GATHER_TYPE))
        {
            return SELECT_GATHER_ICON;
        }
        else
        {
            return "";
        }
    }

    public String getUnitForest(Iterator unitTreeNodes, String[] checkedUnitIDs) throws
        TreeException
    {
        return getUnitForest(unitTreeNodes, checkedUnitIDs, "");
    }

    /**
     * 获得树结构的HTML代码。
     * @param unitTreeNodes 树对象
     * @param checkedUnitIDs 选中的树节点的ID号（单位ID号）可以为null，也可以为""
     * @param postFix 获得获得选项值的方法名的后缀　　getCheckedValues + postFix
     *                postFix可以为""也可为null，为""时和null时　方法名即为 getCheckedValues
     * @return HTML代码
     * @throws TreeException
     */
    public String getUnitForest(Iterator unitTreeNodes, String[] checkedUnitIDs,
                                String postFix) throws TreeException
    {
        StringBuffer result = new StringBuffer( "" );

        result.append("<script type=\"text/javascript\">\r\n");

        int i = 0; //树的名字 （tree + i ， 例如：tree1,tree2,tree3）
        while (unitTreeNodes.hasNext())
        {
            i++;
            UnitTreeNode tempNode = (UnitTreeNode) unitTreeNodes.next();
            boolean isShow =false;
            if((treeStyle().equals(CHECKBOX_XLOAD_TREE))||(!treeStyle().equals(CHECKBOX_XLOAD_TREE)&&tempNode.getDisplay()==1)) isShow=true;
            
            if(isShow){
	            String treeName = "tree" + i;
				result.append( "var " )
					.append( treeName )
					.append( " = new " )
					.append( treeStyle() )
					.append( "(\"" )
					.append( tempNode.getUnitName() )
					.append( "\"," );
	
	            if (!treeStyle().equals(DEFAULT_TREE))
	            {
	            	boolean tempBool = false;
	                if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
	                {
	                    for (int j = 0; j < checkedUnitIDs.length; j++)
	                    {
	                        if (tempNode.id().equals(checkedUnitIDs[j]))
	                        {
	                            tempBool = true;
	                        }
	                    }
	                    result.append( String.valueOf(tempBool) )
							.append( ",\"" )
							.append( checkValue(tempNode) )
							.append( "\"," );
	                }
	                else
	                {
	                	if(tempNode.getDisplay()==1) tempBool=true;
	                    result.append(tempBool+",\"" )
							.append( checkValue(tempNode) )
							.append( "\"," );
	                }
	            }
	
	            result.append( "\"" )
					.append( ( (tempNode.getChildren().hasNext()) ?
							   servletOperation(tempNode, checkedUnitIDs) : "") )
					.append( "\",\"" )
					.append( this.onClickAction(tempNode) )
					.append( "\");\r\n" );
	
	            String icon = getTreeIcon(tempNode);
	            result.append( treeName )
					.append( ".icon = \"" )
					.append( icon )
					.append( "\";\r\n" );
	            result.append( treeName )
					.append( ".openIcon = \"" )
					.append( icon )
					.append( "\";\r\n" );
	
	            result.append( treeName )
					.append( ".setBehavior('classic');\r\n" );
	            result.append( "document.write(" )
					.append( treeName )
					.append( ");\r\n" );
            }
        }
        result.append( "</script >\r\n" );

        //添加获得选择项值的方法
        result.append( "<script language='javascript'>\r\n" );
        result.append( "//获得选项值的方法，返回一个数组，没有选择任何值则返回空\r\n" );
        result.append( "function getCheckedValues" )
			.append( (postFix == null ? "" : postFix) )
			.append( "()\r\n{\r\n result = new Array();\r\n" );
        for (int j = 1; j <= i; j++){
        	result.append("if(typeof tree").append(j).append("!= 'undefined'){\r\n");
            result.append( "result = result.concat(tree").append(j).append( ".getCheckedValue());\r\n" );
            result.append("}");
        }
        result.append( "return result;\r\n}\r\n</script>\r\n" );

        return result.toString();
    }
    
    public String getUnitForestOfShow(Iterator unitTreeNodes, String[] checkedUnitIDs,String postFix) throws TreeException
	{
	StringBuffer result = new StringBuffer( "" );
	
	result.append("<script type=\"text/javascript\">\r\n");
	
	int i = 0; //树的名字 （tree + i ， 例如：tree1,tree2,tree3）
	while (unitTreeNodes.hasNext())
	{
	i++;
	UnitTreeNode tempNode = (UnitTreeNode) unitTreeNodes.next();
	boolean isShow =false;
	if((treeStyle().equals(CHECKBOX_XLOAD_TREE))||(!treeStyle().equals(CHECKBOX_XLOAD_TREE)&&tempNode.getDisplay()==1)) isShow=true;
	if(isShow){
	String treeName = "tree" + i;
	result.append( "var " )
	.append( treeName )
	.append( " = new " )
	.append( treeStyle() )
	.append( "(\"" )
	.append( tempNode.getUnitName() )
	.append( "\"," );
	
	if (!treeStyle().equals(DEFAULT_TREE))
	{
	boolean tempBool = false;
	if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
	{
	    for (int j = 0; j < checkedUnitIDs.length; j++)
	    {
	        if (tempNode.id().equals(checkedUnitIDs[j]))
	        {
	            tempBool = true;
	        }
	    }
	    result.append( String.valueOf(tempBool) )
			.append( ",\"" )
			.append( checkValue(tempNode) )
			.append( "\"," );
	}
	else
	{
		if(tempNode.getDisplay()==1) tempBool=true;
//	    result.append(tempBool+",\"" )
	    result.append(false+",\"" )
			.append( checkValue(tempNode) )
			.append( "\"," );
	}
	}
	
	result.append( "\"" )
	.append( ( (tempNode.getChildren().hasNext()) ?
			   servletOperationOfShow(tempNode, checkedUnitIDs) : "") )
	.append( "\",\"" )
	.append( this.onClickAction(tempNode) )
	.append( "\");\r\n" );
	
	String icon = getTreeIcon(tempNode);
	result.append( treeName )
	.append( ".icon = \"" )
	.append( icon )
	.append( "\";\r\n" );
	result.append( treeName )
	.append( ".openIcon = \"" )
	.append( icon )
	.append( "\";\r\n" );
	
	result.append( treeName )
	.append( ".setBehavior('classic');\r\n" );
	result.append( "document.write(" )
	.append( treeName )
	.append( ");\r\n" );
	}
	}
	result.append( "</script >\r\n" );
	
	//添加获得选择项值的方法
	result.append( "<script language='javascript'>\r\n" );
	result.append( "//获得选项值的方法，返回一个数组，没有选择任何值则返回空\r\n" );
	result.append( "function getCheckedValues" )
	.append( (postFix == null ? "" : postFix) )
	.append( "()\r\n{\r\n result = new Array();\r\n" );
	for (int j = 1; j <= i; j++)
	{
	result.append( "result = result.concat(tree" )
	.append( j )
	.append( ".getCheckedValue());\r\n" );
	}
	result.append( "return result;\r\n}\r\n</script>\r\n" );
	
	return result.toString();
	}



    /**
     * 指定节点获得下级节点的XML文件的 URL　请求
     * @param node 节点对象
     * @param checkedUnitIDs 选中的节点ID号
     * @return 请示字符串
     */
    protected String servletOperation(UnitTreeNode node,
                                      String[] checkedUnitIDs)
    {
        String treeName = this.getClass().getName();
		StringBuffer urlBuff = new StringBuffer();

        urlBuff.append( "utilServlet?operation=showTree&unitID=" )
			.append( node.id() )
			.append( "&taskID=" )
			.append( taskID )
			.append( "&treeName=" )
			.append( treeName );
        if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
        {
			try
			{
				for( int i = 0; i < checkedUnitIDs.length; i++ )
				{
					urlBuff.append( "&checkedUnitIDs=" )
						.append( java.net.URLEncoder.encode( checkedUnitIDs[i],
								 "utf8" ) );
				}
			}
			catch( java.io.UnsupportedEncodingException ex )
			{}
        }

        return urlBuff.toString();
    }
    
    protected String servletOperationOfShow(UnitTreeNode node,
            String[] checkedUnitIDs)
{
String treeName = this.getClass().getName();
StringBuffer urlBuff = new StringBuffer();

urlBuff.append( "utilServlet?operation=showTreeOfShow&unitID=" )
.append( node.id() )
.append( "&taskID=" )
.append( taskID )
.append( "&treeName=" )
.append( treeName );
if (checkedUnitIDs != null && !checkedUnitIDs.equals(""))
{
try
{
for( int i = 0; i < checkedUnitIDs.length; i++ )
{
urlBuff.append( "&checkedUnitIDs=" )
.append( java.net.URLEncoder.encode( checkedUnitIDs[i],
		 "utf8" ) );
}
}
catch( java.io.UnsupportedEncodingException ex )
{}
}

return urlBuff.toString();
}

    /**
     * 指定点击节点时，所发生的动作（一般是父窗体的javascript 函数）
     * @param node 节点对象
     * @return 动作字符串
     */
    abstract protected String onClickAction(UnitTreeNode node);

    /**
     * 树的类型
     * this.CHECKBOX_STATIC_TREE  checkbox框的静态树（即一次性装载）
     * this.CHECKBOX_XLOAD_TREE   checkbox框的动态树（即多次装载）
     * this.RADIO_STATIC_TREE     radio框的静态树
     * this.RADIO_XLOAD_TREE      radio框的动态树
     * this.DEFAULT_TREE          默认树的形式（无选择框）
     * @return 类型字符串
     */
    abstract protected String treeStyle();

    /**
     * 针对默认树以外的形式，选择框的值
     * @param node 　节点对象
     * @return 选择框的值
     */
    abstract protected String checkValue(UnitTreeNode node);
}