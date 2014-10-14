package cn.com.youtong.tools.charts.form;

import java.util.*;

/**
 * <p>Title: TimeForm用于存储Time图的一组数据(用一条Time线来表示)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TimeForm
{

	/**保存Time线的名称*/
	private String _title;
	/**保存所有数据*/
	private Collection _lineData = new ArrayList();

	/**
	 * 向该组数据中添加数据对
	 * 每一对数据对应着该Time线上面的一个点
	 * @param x_dateValue  横坐标值  (注意：横坐标不能重复)
	 * @param y_value  纵坐标值
	 */
	public void addData(Date x_dateValue, Double y_value)
	{
		ArrayList item = new ArrayList();
		item.add(x_dateValue);
		item.add(y_value);
		_lineData.add(item);
	}

	/**
	 * 构造函数
	 * @param title 该线的名称
	 */
	public TimeForm(String title)
	{
		_title = title;
	}

	/**
	 * 返回数据集合
	 * @return Collection 包含若干ArrayList 每个ArrayList包含一个数据对( Double,Double)
	 */
	public Collection getDatas()
	{
		return _lineData;
	}

	/**
	 * 返回该时间线的名称
	 * @return String时间线的名称
	 */
	public String getTitle()
	{
		return _title;
	}
}