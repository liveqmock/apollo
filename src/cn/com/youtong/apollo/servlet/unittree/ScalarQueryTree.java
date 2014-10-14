package cn.com.youtong.apollo.servlet.unittree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * 指标查询所使用的单位html树
 */
public class ScalarQueryTree extends UnitTree
{

	private String taskID;
	public ScalarQueryTree(String taskID)
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
		return "";
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
//		return this.CHECKBOX_XLOAD_TREE;
		return this.RADIO_XLOAD_TREE;
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
