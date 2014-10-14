/*
 * ����й��� Created on 2003-10-20
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

	// Ԫ��Ϊcn.com.youtong.apollo.task.Cell
	private Collection _cells;
	/**
	 * ���캯��
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
	 * �õ����е����е�Ԫ��
	 */
	public Iterator getCells()
	{
		return _cells.iterator();
	}

	/**
	 * �õ���ʶ
	 */
	public boolean getFlag(int flag)
	{
		//�Ը����б�ʶ��������
		if(flag == Row.FLAG_FLOAT_ROW)
		{
			return( _isFloat == 1);
		}
		//������ʶ
		boolean ret = false;
		if( ( _flag & flag ) > 0)
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * rowID
	 * @return		����rowID
	 */
	public String id()
	{
		return _id;
	}

}
