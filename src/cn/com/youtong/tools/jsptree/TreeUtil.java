package cn.com.youtong.tools.jsptree;

/**
 * Title:树的工具类。
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

	/**当前展开的节点的路径。*/
	protected String currNodeKey;
	/**选中的节点id*/
	protected String checkedItems[];
	/**树节点的右键菜单。*/
	protected Item[] menuItems = null;
	/**节点前面显示的图片设置*/
	protected int itemIcon = 0;

	/**节点前面显示的图片为，有子节点的节点显示为文件夹*/
	public static final int ICON_HAVE_FOLDER = 0;
	/**节点前面显示的图片为，所有节点显示的效果跟叶子节点一致*/
	public static final int ICON_NO_FOLDER = 1;
	/**节点前面不显示图片*/
	public static final int ICON_NONE = 2;

	/**节点前显示HTML控件的属性。*/
	protected int control = CONTROL_NONE;
	/**节点前显示复选框*/
	public static final int CONTROL_NONE = 10;

	/**节点前显示复选框*/
	public static final int CONTROL_CHECK_BOX = 11;
	/**节点前显示单选框*/
	public static final int CONTROL_RADIO_BUTTON = 12;

	public TreeUtil()
	{

	}

	/**
	 *
	 * @param currNodeKey 当前展开节点的路径
	 * @param checkedItems 包含所有选中节点的id（这里的id不是html的id，是节点在数据库里的id）
	 * @param menuItems 树节点的右键菜单
	 * @param control 节点显示的控件,使用本类中的常量。
	 * @param itemIcon 节点前面显示的图片设置,使用本类中的常量。
	 *
	 * 在页面中调用javascript的getAllCheckedID()获得选中的值，参考tree_js.properties。
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
	 * 参考上面的构造器文档。
	 */
	public TreeUtil(String currNodeKey, Item[] menuItems, int control)
	{
		this.currNodeKey = currNodeKey;
		this.menuItems = menuItems;
		this.control = control;
	}

	/**
	 * 获得树结构的HTML代码。
	 * @return
	 * @throws IOException
	 * @throws TreeException
	 */
	public String generateHTMLCode()
		throws IOException, TreeException
	{
		//读取javascript文件
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
		//加入树形列表数据
		result += preOrderVisitAll("0", 0, true, "");

		if(currNodeKey != null && currNodeKey.length() > 0)
		{
			//加上展开指定节点的javascript代码
			result += "<script language=javascript>\r\n" + "currNode='" + getIDFromKey(currNodeKey) + "';\r\n" + "</script>\r\n";
		}
		/******************************加上弹出菜单********************************/
		if(menuItems != null && menuItems.length > 0)
		{
			result += "\r\n<SCRIPT LANGUAGE=\"JavaScript1.2\">\r\n" + "window.myMenu = new Menu();\r\n" + "myMenu.menuWidth=100;\r\n" + "myMenu.menuItemWidth=100;\r\n";
			for(int i = 0; i < menuItems.length; i++)
			{
				result += "myMenu.addMenuItem(\"" + menuItems[i].text + "\",\"" + menuItems[i].action + "\");\r\n";
			}

			//用selectedID来向javascript返回Item的id值;
			result += "myMenu.writeMenus();\r\n" + "var selectedID;\r\n" + "</script>\r\n";
		}
		/*************************************************************************/
		/*********************设置当前选中的节点***********************************/
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
	 * 获得树的JTree（JAVA GUI）型的数据。
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
	 * 树的先续遍历（递归）算法,生成JTree
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
			{ //非根节点
				childNode = new DefaultMutableTreeNode(temp);
				parentNode.add(childNode);
			}
			Vector children = treeData.getChildrenKey(key);

			//*********递归**********
			 for(int i = 0; i < children.size(); i++)
			{
				preOrderVisitAll((String) children.elementAt(i), level + 1, childNode);
			}
			//************************
		}
	}

	/**
	 * 树的先序遍历，生成HTML。
	 * @param key 节点的key
	 * @param level 节点的层次，根节点值为0
	 * @param isLastInSameLevel 当前节点是否是父节点所有子节点中最后一个子节点
	 * @param preImg 当前节点的前面的图片
	 * @return HTML字符串
	 * @throws TreeException
	 */
	protected String preOrderVisitAll(String key, int level, boolean isLastInSameLevel, String preImg)
		throws TreeException
	{
		if(key == null)
		{
			throw new TreeException("parameter wrong.");
		}

		//图片常量
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

		//返回值
		String result = "";
		//当前节点对象
		Item currNode;
		try
		{
			currNode = (Item) treeData.get(key); //
		}
		catch(java.lang.ClassCastException ex)
		{
			throw new TreeException("Tree node value must be Item type.The wrong node path is " + key + ".", ex);
		}

		//节点的右键菜单。用selectedID来向javascript返回Item的id值
		String contextMenu = " onmousedown=\"if(event.button==2){selectedID=" + currNode.id + "; window.showMenu(window.myMenu);} return false;\"" +
			" onmouseup=\"window.event.cancelBubble = true ;return false;\"" + " oncontextmenu=\"return false;\" ";
		//在节点前面显示的图标
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
		//在节点前面显示的控制图片，包括加号减号和叶子节点前面的短线。
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
		//在节点前面显示的html控件
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
			{ //非根节点
				//生成当前节点的HTML字符串
				result += "" + "<div id=\"" + getIDFromKey(key) + "\"" + " CLASS=\"ParentLevel" + level + "\">" + preImg + controlImg + icon + htmlControl + "<a " +
					(currNode.action == null ? "#" : currNode.action) + " " + contextMenu + ">" + currNode.text + "</a>" + "</div>";
			}
			Vector children = treeData.getChildrenKey(key);

			//字节点前面的图片
			String childPreImg = "";
			//如果此节点是父节点的最后一个字节点，则此节点所有的子孙节点前面都加空白
			if(isLastInSameLevel)
			{
				childPreImg = BLANK_IMG;
				//否则所有的子孙节点前面都加竖线
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

				//******************************递归部分**********************************
			}
			if(!key.equals("0") && !treeData.isLeafNode(key))
			{
				result += "\r\n" + "<div id=\"" + getIDFromKey(key) + "Child\"" + " CLASS=\"ChildLevel" + level + "\">";
			}
			//表示此节点的字节点中，是否是此节点的最后一个字节点
			boolean isLast = false;
			for(int i = 0; i < children.size(); i++)
			{
				if(i == children.size() - 1)
				{ //是最后一个子节点
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
	 * 从节点的key获得html中的id
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
		/**定义弹出菜单*/
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
		//**********添加树形数据****************
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

		 //生成HTML代码
//    String temp=tree.generateHTMLCode();

		String temp = tree.generateHTMLCode();

		//*************写HTML文件*************
		 final String destination = "d:/TestTree.htm";

		File fl = new File(destination);
		fl.delete();
		FileOutputStream file1 = new FileOutputStream(destination);

		byte[] temp1 = temp.getBytes();
		file1.write(temp1);
		file1.close();
		//*************************************

		 //生成JTree
		 //tree.generateJTree(true);
		 System.out.println("Succeed!");
	}
}