package cn.com.youtong.tools.jsptree;

/**
 * Title:���Ĺ����ࡣ
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;

public class TreeUtil
{
	public Tree treeData = new DefaultTree();

	/**��ǰչ���Ľڵ��·����*/
	protected String currNodeKey;
	/**ѡ�еĽڵ�id*/
	protected String checkedItems[];
	/**���ڵ���Ҽ��˵���*/
	protected Item[] menuItems = null;
	/**�ڵ�ǰ����ʾ��ͼƬ����*/
	protected int itemIcon = 0;

	/**�ڵ�ǰ����ʾ��ͼƬΪ�����ӽڵ�Ľڵ���ʾΪ�ļ���*/
	public static final int ICON_HAVE_FOLDER = 0;
	/**�ڵ�ǰ����ʾ��ͼƬΪ�����нڵ���ʾ��Ч����Ҷ�ӽڵ�һ��*/
	public static final int ICON_NO_FOLDER = 1;
	/**�ڵ�ǰ�治��ʾͼƬ*/
	public static final int ICON_NONE = 2;

	/**�ڵ�ǰ��ʾHTML�ؼ������ԡ�*/
	protected int control = CONTROL_NONE;
	/**�ڵ�ǰ��ʾ��ѡ��*/
	public static final int CONTROL_NONE = 10;

	/**�ڵ�ǰ��ʾ��ѡ��*/
	public static final int CONTROL_CHECK_BOX = 11;
	/**�ڵ�ǰ��ʾ��ѡ��*/
	public static final int CONTROL_RADIO_BUTTON = 12;

	public TreeUtil()
	{

	}

	/**
	 *
	 * @param currNodeKey ��ǰչ���ڵ��·��
	 * @param checkedItems ��������ѡ�нڵ��id�������id����html��id���ǽڵ������ݿ����id��
	 * @param menuItems ���ڵ���Ҽ��˵�
	 * @param control �ڵ���ʾ�Ŀؼ�,ʹ�ñ����еĳ�����
	 * @param itemIcon �ڵ�ǰ����ʾ��ͼƬ����,ʹ�ñ����еĳ�����
	 *
	 * ��ҳ���е���javascript��getAllCheckedID()���ѡ�е�ֵ���ο�tree_js.properties��
	 */
	public TreeUtil(String currNodeKey, String checkedItems[], Item[] menuItems, int control, int itemIcon)
	{
		this.currNodeKey = currNodeKey;
		this.menuItems = menuItems;
		this.control = control;
		this.itemIcon = itemIcon;
		this.checkedItems = checkedItems;
	}

	/**
	 * �ο�����Ĺ������ĵ���
	 */
	public TreeUtil(String currNodeKey, Item[] menuItems, int control)
	{
		this.currNodeKey = currNodeKey;
		this.menuItems = menuItems;
		this.control = control;
	}

	/**
	 * ������ṹ��HTML���롣
	 * @return
	 * @throws IOException
	 * @throws TreeException
	 */
	public String generateHTMLCode()
		throws IOException, TreeException
	{
		//��ȡjavascript�ļ�
		TreeUtil t = new TreeUtil();
		InputStream in = t.getClass().getClassLoader().getResourceAsStream("cn/com/youtong/tools/jsptree/tree_js.properties");
		long length = 0;
		in.mark(1024 * 1024); //1M
		while(in.read() > 0)
		{
			length++;
		}
		byte[] buffer = new byte[(int) length];
		in.reset();
		in.read(buffer);
		String result = new String(buffer);

		result = "<html><head>" + result + "</head>" + (menuItems == null ? "" : "<body oncontextmenu=\"return false;\">");
		//���������б�����
		result += preOrderVisitAll("0", 0, true, "");

		if(currNodeKey != null && currNodeKey.length() > 0)
		{
			//����չ��ָ���ڵ��javascript����
			result += "<script language=javascript>\r\n" + "currNode='" + getIDFromKey(currNodeKey) + "';\r\n" + "</script>\r\n";
		}
		/******************************���ϵ����˵�********************************/
		if(menuItems != null && menuItems.length > 0)
		{
			result += "\r\n<SCRIPT LANGUAGE=\"JavaScript1.2\">\r\n" + "window.myMenu = new Menu();\r\n" + "myMenu.menuWidth=100;\r\n" + "myMenu.menuItemWidth=100;\r\n";
			for(int i = 0; i < menuItems.length; i++)
			{
				result += "myMenu.addMenuItem(\"" + menuItems[i].text + "\",\"" + menuItems[i].action + "\");\r\n";
			}

			//��selectedID����javascript����Item��idֵ;
			result += "myMenu.writeMenus();\r\n" + "var selectedID;\r\n" + "</script>\r\n";
		}
		/*************************************************************************/
		/*********************���õ�ǰѡ�еĽڵ�***********************************/
		if(checkedItems != null)
		{
			String array = "";
			for(int i = 0; i < checkedItems.length; i++)
			{
				if(i > 0)
				{
					array += ",";
				}
				array += "'" + checkedItems[i] + "'";
			}
			result += "\r\n<SCRIPT LANGUAGE=\"JavaScript1.2\">\r\n" + "var checkedItems=new Array(" + array + ");\r\n" + "setCheckedItems(checkedItems);\r\n" + "</script>\r\n";
		}
		/*************************************************************************/
		result += "</body></html>";
		return result;

	}

	/**
	 * �������JTree��JAVA GUI���͵����ݡ�
	 * @param isRootVisible
	 * @return
	 * @throws TreeException
	 */
	public JTree generateJTree(boolean isRootVisible)
		throws TreeException
	{
		//Create the nodes.
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Root");
		DefaultTreeModel treeModel1 = new DefaultTreeModel(top);

		//Create a tree that allows one selection at a time.
		//JTree tree1 = new JTree(treeModel1);
		JTree tree1 = new JTree(top);
		tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree1.setRootVisible(isRootVisible);
		getAllWithPreOrder(top);

		return tree1;
	}

	protected void getAllWithPreOrder(DefaultMutableTreeNode rootNode)
		throws TreeException
	{
		preOrderVisitAll("0", 0, rootNode);
	}

	/**
	 * ���������������ݹ飩�㷨,����JTree
	 * @param key
	 * @param level
	 * @param parentNode
	 * @throws TreeException
	 */
	protected void preOrderVisitAll(String key, int level, DefaultMutableTreeNode parentNode)
		throws TreeException
	{
		if(key == null)
		{
			throw new TreeException("parameter wrong.");
		}
		Object temp = treeData.get(key);
		DefaultMutableTreeNode childNode = parentNode;

		if(temp != null)
		{
			if(!key.equals("0"))
			{ //�Ǹ��ڵ�
				childNode = new DefaultMutableTreeNode(temp);
				parentNode.add(childNode);
			}
			Vector children = treeData.getChildrenKey(key);

			//*********�ݹ�**********
			 for(int i = 0; i < children.size(); i++)
			{
				preOrderVisitAll((String) children.elementAt(i), level + 1, childNode);
			}
			//************************
		}
	}

	/**
	 * �����������������HTML��
	 * @param key �ڵ��key
	 * @param level �ڵ�Ĳ�Σ����ڵ�ֵΪ0
	 * @param isLastInSameLevel ��ǰ�ڵ��Ƿ��Ǹ��ڵ������ӽڵ������һ���ӽڵ�
	 * @param preImg ��ǰ�ڵ��ǰ���ͼƬ
	 * @return HTML�ַ���
	 * @throws TreeException
	 */
	protected String preOrderVisitAll(String key, int level, boolean isLastInSameLevel, String preImg)
		throws TreeException
	{
		if(key == null)
		{
			throw new TreeException("parameter wrong.");
		}

		//ͼƬ����
		String MINUS_IMG = "../img/folderopen.gif";
		String PLUS_IMG = "../img/folderclosed.gif";
		String PRE_MINUS_IMG = "../img/mnode.gif";
		String PRE_PLUS_IMG = "../img/pnode.gif";
		String PRE_LAST_MINUS_IMG = "../img/mlastnode.gif";
		String PRE_LAST_PLUS_IMG = "../img/plastnode.gif";
		String LEAF_IMG = "../img/leaf.gif";
		String PRE_LEAF_IMG = "../img/node.gif";
		String PRE_LAST_LEAF_IMG = "../img/lastnode.gif";
		String LINE_IMG = "../img/vertline.gif";
		String BLANK_IMG = "../img/blank.gif";

		//����ֵ
		String result = "";
		//��ǰ�ڵ����
		Item currNode;
		try
		{
			currNode = (Item) treeData.get(key); //
		}
		catch(java.lang.ClassCastException ex)
		{
			throw new TreeException("Tree node value must be Item type.The wrong node path is " + key + ".", ex);
		}

		//�ڵ���Ҽ��˵�����selectedID����javascript����Item��idֵ
		String contextMenu = " onmousedown=\"if(event.button==2){selectedID=" + currNode.id + "; window.showMenu(window.myMenu);} return false;\"" +
			" onmouseup=\"window.event.cancelBubble = true ;return false;\"" + " oncontextmenu=\"return false;\" ";
		//�ڽڵ�ǰ����ʾ��ͼ��
		String icon = "";
		if(this.itemIcon == this.ICON_NO_FOLDER)
		{
			MINUS_IMG = LEAF_IMG;
			PLUS_IMG = LEAF_IMG;
		}
		if(this.itemIcon == this.ICON_NONE)
		{
			icon = "";
		}
		else
		{
			icon = (treeData.isLeafNode(key) ? "" : "<a HREF=\"#\" onClick=\"expandIt('" + getIDFromKey(key) + "'); return false;\">") + "<img SRC=\"" +
				(treeData.isLeafNode(key) ? LEAF_IMG : PLUS_IMG) + "\" BORDER=\"0\"  ID=\"" + getIDFromKey(key) + "Img\" align='absmiddle'>" + (treeData.isLeafNode(key) ? "" : "</a>");
		}
		//�ڽڵ�ǰ����ʾ�Ŀ���ͼƬ�������Ӻż��ź�Ҷ�ӽڵ�ǰ��Ķ��ߡ�
		String controlImg = "";
		if(treeData.isLeafNode(key))
		{
			controlImg = "<img SRC=\"" + (isLastInSameLevel ? PRE_LAST_LEAF_IMG : PRE_LEAF_IMG) + "\" BORDER=\"0\" ID=\"" + getIDFromKey(key) + "PreImg\" align='absmiddle'>";
		}
		else
		{
			controlImg = "<a HREF=\"#\" onClick=\"expandIt('" + getIDFromKey(key) + "'); return false;\">" + "<img SRC=\"" + (isLastInSameLevel ? PRE_LAST_PLUS_IMG : PRE_PLUS_IMG) +
				"\" BORDER=\"0\" ID=\"" + getIDFromKey(key) + "PreImg\" align='absmiddle'></a>";
		}
		//�ڽڵ�ǰ����ʾ��html�ؼ�
		String htmlControl = "";
		if(this.control == this.CONTROL_CHECK_BOX)
		{
			htmlControl = "<input type=\"checkbox\" name=\"check\" onclick=\"selectChildren(this);\" value=\"" + currNode.id + "\">";
		}
		else if(this.control == this.CONTROL_RADIO_BUTTON)
		{
			htmlControl = "<input type=\"radio\" name=\"check\" value=\"" + currNode.id + "\">";
		}
		else
		{
			htmlControl = "";
		}
		if(currNode.text != null)
		{
			if(!key.equals("0"))
			{ //�Ǹ��ڵ�
				//���ɵ�ǰ�ڵ��HTML�ַ���
				result += "" + "<div id=\"" + getIDFromKey(key) + "\"" + " CLASS=\"ParentLevel" + level + "\">" + preImg + controlImg + icon + htmlControl + "<a " +
					(currNode.action == null ? "#" : currNode.action) + " " + contextMenu + ">" + currNode.text + "</a>" + "</div>";
			}
			Vector children = treeData.getChildrenKey(key);

			//�ֽڵ�ǰ���ͼƬ
			String childPreImg = "";
			//����˽ڵ��Ǹ��ڵ�����һ���ֽڵ㣬��˽ڵ����е�����ڵ�ǰ�涼�ӿհ�
			if(isLastInSameLevel)
			{
				childPreImg = BLANK_IMG;
				//�������е�����ڵ�ǰ�涼������
			}
			else
			{
				childPreImg = LINE_IMG;
			}
			if(level == 0)
			{
				childPreImg = "";
			}
			else
			{
				childPreImg = preImg + "<img src=\"" + childPreImg + "\" border='0'  align='absmiddle'>";

				//******************************�ݹ鲿��**********************************
			}
			if(!key.equals("0") && !treeData.isLeafNode(key))
			{
				result += "\r\n" + "<div id=\"" + getIDFromKey(key) + "Child\"" + " CLASS=\"ChildLevel" + level + "\">";
			}
			//��ʾ�˽ڵ���ֽڵ��У��Ƿ��Ǵ˽ڵ�����һ���ֽڵ�
			boolean isLast = false;
			for(int i = 0; i < children.size(); i++)
			{
				if(i == children.size() - 1)
				{ //�����һ���ӽڵ�
					isLast = true;
				}
				else
				{
					isLast = false;
				}

				result += preOrderVisitAll((String) children.elementAt(i), level + 1, isLast, childPreImg);
			}
			if(!key.equals("0") && !treeData.isLeafNode(key))
			{
				result += "\r\n</div>";
			}
			//************************************************************************
		}
		return result;
	}

	/**
	 * �ӽڵ��key���html�е�id
	 * @param key
	 * @return
	 */
	protected String getIDFromKey(String key)
	{
		return "P" + key.replace(',', '_');
	}

	public static void main(String[] args)
		throws Exception
	{
		/**���嵯���˵�*/
		Item[] menu = new Item[3];
		Item menuItem = null;
		menu[0] = new Item("aa", "alert('selectedID'+selectedID)");
		menu[1] = new Item("bb", "top.window.location='http://www.google.com'");
		menu[2] = new Item("cc", "alert('ok')");

		String[] checkedItems =
			{
			"1", "3", "5"};
		TreeUtil tree = new TreeUtil("", checkedItems, null, TreeUtil.CONTROL_CHECK_BOX, TreeUtil.ICON_NO_FOLDER);
		Item nodeValue = null;
		//**********�����������****************
		 nodeValue = new Item("a01", "TARGET=\"main\" HREF=\"1.htm\"", "s1s");
		tree.treeData.add("0,1", nodeValue);

		nodeValue = new Item("a02", "TARGET=\"main\" HREF=\"2.htm\"", "s2s");
		tree.treeData.add("0,1,1", nodeValue);

		nodeValue = new Item("a03", "TARGET=\"main\" HREF=\"1.htm\"", "s3s");
		tree.treeData.add("0,1,2", nodeValue);

		nodeValue = new Item("a04", "TARGET=\"main\" HREF=\"2.htm\"", "s4s");
		tree.treeData.add("0,1,2,1", nodeValue);

		nodeValue = new Item("a05", "TARGET=\"main\" HREF=\"2.htm\"", "s5s");
		tree.treeData.add("0,1,2,1,1", nodeValue);

		nodeValue = new Item("a02", "TARGET=\"main\" HREF=\"2.htm\"", "s7s");
		tree.treeData.add("0,1,3", nodeValue);

		nodeValue = new Item("a07", "TARGET=\"main\" HREF=\"2.htm\"", "s8s");
		tree.treeData.add("0,1,4", nodeValue);

		nodeValue = new Item("a08", "TARGET=\"main\" HREF=\"2.htm\"", "s9s");
		tree.treeData.add("0,2", nodeValue);

		nodeValue = new Item("a09", "TARGET=\"main\" HREF=\"2.htm\"", "s10s");
		tree.treeData.add("0,2,1", nodeValue);

//    nodeValue=new Item("a06","TARGET=\"main\" HREF=\"2.htm\"",6);
//    tree.treeData.add("0,3",nodeValue);

		//*************************************

		 //����HTML����
//    String temp=tree.generateHTMLCode();

		String temp = tree.generateHTMLCode();

		//*************дHTML�ļ�*************
		 final String destination = "d:/TestTree.htm";

		File fl = new File(destination);
		fl.delete();
		FileOutputStream file1 = new FileOutputStream(destination);

		byte[] temp1 = temp.getBytes();
		file1.write(temp1);
		file1.close();
		//*************************************

		 //����JTree
		 //tree.generateJTree(true);
		 System.out.println("Succeed!");
	}
}