/*
 * 表格行管理 Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import java.util.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;

/**
 * @author wjb
 */
public class DBRow
	implements Row
{
	private String _id;
	private int _isFloat;
	private int _flag;

	// 元素为cn.com.youtong.apollo.task.Cell
	private Collection _cells;
	/**
	 * 构造函数
	 *
	 * @param rowForm
	 *            form of hibernate
	 */
	public DBRow(RowForm rowForm)
	{
		this._id = rowForm.getID();
		this._isFloat = rowForm.getIsFloat();
		this._flag = rowForm.getFlag();

		_cells = new LinkedList();
		Iterator iter = rowForm.getCells().iterator();
		while(iter.hasNext())
		{
			CellForm cellForm = (CellForm) iter.next();
			_cells.add( new DBCell( cellForm ) );
		}

	}

	/**
	 * 得到此行的所有单元格
	 */
	public Iterator getCells()
	{
		return _cells.iterator();
	}

	/**
	 * 得到标识
	 */
	public boolean getFlag(int flag)
	{
		//对浮动行标识单独处理
		if(flag == Row.FLAG_FLOAT_ROW)
		{
			return( _isFloat == 1);
		}
		//其他标识
		boolean ret = false;
		if( ( _flag & flag ) > 0)
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * rowID
	 * @return		返回rowID
	 */
	public String id()
	{
		return _id;
	}

}
