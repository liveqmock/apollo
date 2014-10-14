package cn.com.youtong.apollo.servlet.unittree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * 给组分配单位权限的html树
 */
public class AddressInfoTree extends UnitTree
{
	private String taskID;
	public AddressInfoTree(String taskID)
	{
		super(taskID);
		this.taskID = taskID;
	}


	/**
	 * 指定点击节点时，所发生的动作（一般是父窗体的javascript 函数）
	 * @param node 节点对象
	 * @return 动作字符串
	 */
	protected String onClickAction(UnitTreeNode node)
	{
		return "javascript:changeUnit('" + node.id() + "','" + node.getUnitName() + "')";
	}

	/**
	 * 树的类型
	 * this.CHECKBOX_STATIC_TREE  checkbox框的静态树（即一次性装载）
	 * this.CHECKBOX_XLOAD_TREE   checkbox框的动态树（即多次装载）
	 * this.RADIO_STATIC_TREE     radio框的静态树
	 * this.RADIO_XLOAD_TREE      radio框的动态树
	 * this.DEFAULT_TREE          默认树的形式（无选择框）
	 * @return 类型字符串
	 */
	protected String treeStyle()
	{
		return this.DEFAULT_TREE;
	}

	/**
	 * 针对默认树以外的形式，选择框的值
	 * @param node 　节点对象
	 * @return 选择框的值
	 */
	protected String checkValue(UnitTreeNode node)
	{
		return node.id();
	}
}
