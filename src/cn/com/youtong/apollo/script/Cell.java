package cn.com.youtong.apollo.script;

import java.util.*;

/**
 * 脚本Cell
 */
public class Cell
	implements ScriptObject
{
	/**
	 * 该cell相关的table对象
	 */
	private Table table;

	/**
	 * 该cell所在的行数
	 */
	public final int row;

	/**
	 * 该cell所在的列数
	 */
	public final int col;

	/**
	 * 数据类型：1 -- 数值型  2 -- 文本型 	3 -- 二进制  4 -- 大文本 5 -- 日期型
	 */
	public final int type;

	Cell(Table table, int row, int col, int type)
	{
		this.table = table;
		this.row = row;
		this.col = col;
		this.type = type;
	}

	/**
	 * Get dynamic fields
	 * @return
	 */
	public Iterator s_getFields()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * is field of object
	 * @param field field name
	 * @return true - if is field of object
	 */
	public boolean isField(String field)
	{
		//只有value是动态属性
		return field.equals("value");
	}

	/**
	 * Get field value
	 * @param key
	 * @return
	 */
	public Object s_getField(String key)
	{
		String label = table.getLabel(row, col);
		return table.s_getField(label);
	}

	/**
	 * set field value
	 * @param key
	 * @param value
	 */
	public void s_setField(String key, Object value)
	{
		String label = table.getLabel(row, col);
		table.s_setField(label, value);
	}

}
