package cn.com.youtong.tools.charts.form;

import java.util.*;

/**
 * <p>Title: BarForm用于存储Bar图上面的一组同色柱</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class BarForm
{

	/**本组数据的名称*/
	private String _title;
	/**保存所有数据*/
	private Collection _barData = new ArrayList();

	/**
	 * 向该组数据中添加数据对
	 * 每个数据对对应着 Bar 图上面 该组柱中的一根柱
	 * @param itemName    一项数据的名称
	 * @param itemValue   对应数据的值
	 */
	public void addData(String itemName, Integer itemValue)
	{
		ArrayList item = new ArrayList();
		item.add(itemName);
		item.add(itemValue);
		_barData.add(item);
	}

	/**
	 * 构造函数
	 * @param title 该组数据的名称
	 */
	public BarForm(String title)
	{
		_title = title;
	}

	/**
	 * 返回数据集合
	 * @return Collection 包含若干ArrayList 每个ArrayList包含一个数据对( String,Integer)
	 */
	public Collection getDatas()
	{
		return _barData;
	}

	/**
	 * 返回本组数据的名称
	 * @return String 本组数据的名称
	 */
	public String getTitle()
	{
		return _title;
	}
}