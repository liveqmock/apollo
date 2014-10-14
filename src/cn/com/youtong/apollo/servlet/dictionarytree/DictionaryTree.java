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
    /**Ĭ��������ʽ����ѡ���̬���أ�*/
    public static final String DEFAULT_TREE = "WebFXLoadTree";

    /**
     * -ͼ��
     */
    private static final String SELECT_GATHER_ICON = "../img/icon_0.gif";
    private static final String ROOT_ICON = "../img/icon_7.gif";

    public DictionaryTree()
    {
    }

    /**
     * ���������xml����
     * @param unitTree ��λ������
     * @param checkedUnitIDs Ҫѡ�е����ڵ��ID��
     * @return XML�ַ���
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
     * �õ���λ���ڵ��ͼ��
     * @param unit ��λ
     * @return ͼ��·��
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
     * ������ṹ��HTML���롣
     * @param unitTreeNodes ������
     * @param checkedUnitIDs ѡ�е����ڵ��ID�ţ���λID�ţ�����Ϊnull��Ҳ����Ϊ""
     * @param postFix ��û��ѡ��ֵ�ķ������ĺ�׺����getCheckedValues + postFix
     *                postFix����Ϊ""Ҳ��Ϊnull��Ϊ""ʱ��nullʱ����������Ϊ getCheckedValues
     * @return HTML����
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
     * ָ���ڵ����¼��ڵ��XML�ļ��� URL������
     * @param node �ڵ����
     * @param checkedUnitIDs ѡ�еĽڵ�ID��
     * @return ��ʾ�ַ���
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
     * ָ������ڵ�ʱ���������Ķ�����һ���Ǹ������javascript ������
     * @param node �ڵ����
     * @return �����ַ���
     */
    abstract protected String onClickAction(String id);

}