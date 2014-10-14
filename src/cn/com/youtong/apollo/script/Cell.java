package cn.com.youtong.apollo.script;

import java.util.*;

/**
 * �ű�Cell
 */
public class Cell
	implements ScriptObject
{
	/**
	 * ��cell��ص�table����
	 */
	private Table table;

	/**
	 * ��cell���ڵ�����
	 */
	public final int row;

	/**
	 * ��cell���ڵ�����
	 */
	public final int col;

	/**
	 * �������ͣ�1 -- ��ֵ��  2 -- �ı��� 	3 -- ������  4 -- ���ı� 5 -- ������
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
		//ֻ��value�Ƕ�̬����
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
