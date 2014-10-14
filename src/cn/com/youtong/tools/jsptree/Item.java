package cn.com.youtong.tools.jsptree;

/**
 * 树或菜单中的项目。
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 世纪友通</p>
 * @author 李巍
 * @version 1.0
 */
public class Item
{

	public Item()
	{
	}

	public Item(String text, String action)
	{
		this.text = text;
		this.action = action;
	}

	public Item(String text, String action, String id)
	{
		this.text = text;
		this.action = action;
		this.id = id;
	}

	/**项目显示的文字*/
	public String text;
	/**项目要执行的动作，一般是超链接或javascript*/
	public String action;
	/**项目ID*/
	public String id;
}