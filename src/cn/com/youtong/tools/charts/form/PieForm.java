package cn.com.youtong.tools.charts.form;

/**
 * <p>Title: 保存Pie图的数据</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.*;

public class PieForm
{
	/**保存所有数据*/
	private Collection _barData = new ArrayList();
	/**
	 * 向该组数据中添加数据对
	 * 该数据对 对应 饼图中的一个扇形区域 sector
	 * @param itemName   扇形区的名称
	 * @param itemValue  扇形区的值
	 */
	public void addData(String itemName, Integer itemValue)
	{
		ArrayList item = new ArrayList();
		item.add(itemName);
		item.add(itemValue);
		_barData.add(item);
	}

	/**
	 * 返回数据集合
	 * @return Collection 包含若干ArrayList 每个ArrayList包含一个数据对( String,Integer)
	 */
	public Collection getDates()
	{
		return _barData;
	}
}