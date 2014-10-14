package cn.com.youtong.apollo.servlet.dictionarytree;

import java.util.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.servlet.unittree.xml.*;
import java.io.*;
import org.exolab.castor.xml.*;
import cn.com.youtong.apollo.common.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.dictionary.db.DBDictionaryEntry;

abstract public class DictionaryTree
{
    private Log log = LogFactory.getLog(this.getClass());
    /**默认树的形式（无选择框动态加载）*/
    public static final String DEFAULT_TREE = "WebFXLoadTree";

    /**
     * -图标
     */
    private static final String SELECT_GATHER_ICON = "../img/icon_0.gif";
    private static final String ROOT_ICON = "../img/icon_7.gif";

    public DictionaryTree()
    {
    }

    /**
     * 获得子树的xml代码
     * @param unitTree 单位树对象
     * @param checkedUnitIDs 要选中的树节点的ID号
     * @return XML字符串
     * @throws Warning
     */
    public String getDictionaryTree(String dictionaryID,Iterator dictionaryTree) throws
        Warning
    {
        TreeRoot treeRoot = new TreeRoot();
        StringWriter strWriter = null;

        try
        {
            while (dictionaryTree.hasNext())
            {
                DBDictionaryEntry tempNode = (DBDictionaryEntry) dictionaryTree.next();
                Tree tree = new Tree();
                tree.setText(tempNode.getKey().toString()+" "+tempNode.getValue().toString());
                if (tempNode.getChildren().iterator().hasNext())
                {
                    tree.setSrc(this.servletOperation(dictionaryID,tempNode.getKey().toString()));
                }
                tree.setAction(this.onClickAction(dictionaryID));
                String icon = getTreeIcon(2);
                tree.setIcon(icon);
                tree.setOpenIcon(icon);
                treeRoot.addTree(tree);
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
    private String getTreeIcon(int index)
    {
            if(index==1){
              return this.ROOT_ICON;
            }else{
              return this.SELECT_GATHER_ICON;
            }
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
    public String getDictionaryForest(String rootID,String rootName,boolean flag) throws DictionaryTreeException
    {
        StringBuffer result = new StringBuffer();
        result.append("<script type=\"text/javascript\">\r\n");
        result.append("var tree"+" = new WebFXLoadTree" + "(\"" + rootName + "\",");
        result.append("\"" +
            ( (flag) ?
             servletOperation(rootID, "") : "") + "\",\"" +
            this.onClickAction(rootID) + "\");\r\n");

       String icon = getTreeIcon(1);
       result.append("tree.icon = \"" + icon + "\";\r\n");
       result.append("tree.openIcon = \"" + icon + "\";\r\n");

        result.append("tree.setBehavior('classic');\r\n");
        result.append("document.write(tree);\r\n");

        result.append("</script >\r\n");

        return result.toString();
    }

    /**
     * 指定节点获得下级节点的XML文件的 URL　请求
     * @param node 节点对象
     * @param checkedUnitIDs 选中的节点ID号
     * @return 请示字符串
     */
    protected String servletOperation(String dictionaryID,
                                      String key)
    {
      String url = "utilServlet?operation=showDictionaryTree" + "&dictionaryID=" + dictionaryID;
      if(key!=""){
        url += "&key="+key;
      }
       return url.toString();
    }

    /**
     * 指定点击节点时，所发生的动作（一般是父窗体的javascript 函数）
     * @param node 节点对象
     * @return 动作字符串
     */
    abstract protected String onClickAction(String id);

}