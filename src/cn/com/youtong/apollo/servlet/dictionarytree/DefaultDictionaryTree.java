package cn.com.youtong.apollo.servlet.dictionarytree;

import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.*;

/**
 * 给组分配单位权限的html树
 */
public class DefaultDictionaryTree extends DictionaryTree
{
	public DefaultDictionaryTree()
	{
		super();
	}


	/**
	 * 指定点击节点时，所发生的动作（一般是父窗体的javascript 函数）
	 * @param node 节点对象
	 * @return 动作字符串
	 */
	protected String onClickAction(String dictionaryID)
	{
		return "javascript:changeUnit('" + dictionaryID+ "')";
	}

}
