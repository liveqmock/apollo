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

    /**checkbox��Ķ�̬������*/
    public static final String CHECKBOX_XLOAD_TREE = "WebFXCheckBoxLoadTree";
    /**radio��Ķ�̬������*/
    public static final String RADIO_XLOAD_TREE = "WebFXRadioLoadTree";
    /**Ĭ��������ʽ����ѡ���̬���أ�*/
    public static final String DEFAULT_TREE = "WebFXLoadTree";

    /**
     * ��������ҵ��
     */
    private static final String BASIC_MERGE_ICON = "";

    /**
     * ��ҵ����ҵ��
     */
    private static final String ENTERPRISE_MERGE_ICON = "../img/icon_4.gif";

    /**
     * ���Ⲣ��ҵ��
     */
    private static final String FOREIGN_MERGE_ICON = "../img/icon_3.gif";

    /**
     * ���ڲ���ҵ��
     */
    private static final String FINANCE_MERGE_ICON = "../img/icon_2.gif";

    /**
     * ���Ų���
     */
    private static final String GROUP_DIFF_ICON = "../img/icon_1.gif";

    /**
     * ������
     */
    private static final String GRASS_ROOT_ICON = "../img/icon_0.gif";

    /**
     * ��ȫ���ܱ�
     */
    private static final String FULL_GATHER_ICON = "../img/icon_7.gif";

    /**
     * ���Ż��ܱ�
     */
    private static final String GROUP_GATHER_ICON = "../img/icon_9.gif";

    /**
     * ѡ����ܱ�
     */
    private static final String SELECT_GATHER_ICON = "../img/icon_h.gif";

    public UnitTree(String taskID)
    {
        this.taskID = taskID;
    }

    /**
     * ���������xml����
     * @param unitTree ��λ������
     * @param checkedUnitIDs Ҫѡ�е����ڵ��ID��
     * @return XML�ַ���
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
     * �õ���λ���ڵ��ͼ��
     * @param unit ��λ
     * @return ͼ��·��
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
     * ������ṹ��HTML���롣
     * @param unitTreeNodes ������
     * @param checkedUnitIDs ѡ�е����ڵ��ID�ţ���λID�ţ�����Ϊnull��Ҳ����Ϊ""
     * @param postFix ��û��ѡ��ֵ�ķ������ĺ�׺����getCheckedValues + postFix
     *                postFix����Ϊ""Ҳ��Ϊnull��Ϊ""ʱ��nullʱ����������Ϊ getCheckedValues
     * @return HTML����
     * @throws TreeException
     */
    public String getUnitForest(Iterator unitTreeNodes, String[] checkedUnitIDs,
                                String postFix) throws TreeException
    {
        StringBuffer result = new StringBuffer( "" );

        result.append("<script type=\"text/javascript\">\r\n");

        int i = 0; //�������� ��tree + i �� ���磺tree1,tree2,tree3��
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

        //��ӻ��ѡ����ֵ�ķ���
        result.append( "<script language='javascript'>\r\n" );
        result.append( "//���ѡ��ֵ�ķ���������һ�����飬û��ѡ���κ�ֵ�򷵻ؿ�\r\n" );
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
	
	int i = 0; //�������� ��tree + i �� ���磺tree1,tree2,tree3��
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
	
	//��ӻ��ѡ����ֵ�ķ���
	result.append( "<script language='javascript'>\r\n" );
	result.append( "//���ѡ��ֵ�ķ���������һ�����飬û��ѡ���κ�ֵ�򷵻ؿ�\r\n" );
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
     * ָ���ڵ����¼��ڵ��XML�ļ��� URL������
     * @param node �ڵ����
     * @param checkedUnitIDs ѡ�еĽڵ�ID��
     * @return ��ʾ�ַ���
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
     * ָ������ڵ�ʱ���������Ķ�����һ���Ǹ������javascript ������
     * @param node �ڵ����
     * @return �����ַ���
     */
    abstract protected String onClickAction(UnitTreeNode node);

    /**
     * ��������
     * this.CHECKBOX_STATIC_TREE  checkbox��ľ�̬������һ����װ�أ�
     * this.CHECKBOX_XLOAD_TREE   checkbox��Ķ�̬���������װ�أ�
     * this.RADIO_STATIC_TREE     radio��ľ�̬��
     * this.RADIO_XLOAD_TREE      radio��Ķ�̬��
     * this.DEFAULT_TREE          Ĭ��������ʽ����ѡ���
     * @return �����ַ���
     */
    abstract protected String treeStyle();

    /**
     * ���Ĭ�����������ʽ��ѡ����ֵ
     * @param node ���ڵ����
     * @return ѡ����ֵ
     */
    abstract protected String checkValue(UnitTreeNode node);
}