/*
 * 单元格管理
 * Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;

/**
 * @author wjb
 */
public class DBCell
	implements Cell
{
	private String _label;
	private int _dataType;
	private String _dbfieldName;
	private int _flag;
	private String _dictionaryID;
	private String _scalarName;

	/**
	 * 构造函数
	 * @param cellForm form of hibernate
	 */
	public DBCell(CellForm cellForm)
	{
		_label = cellForm.getLabel();
		_dataType = cellForm.getDataType();
		_dbfieldName = cellForm.getDbfieldName();
		_flag = cellForm.getFlag();
		_dictionaryID = cellForm.getDictionaryID();
		_scalarName = cellForm.getScalarName();
	}

    /**
     * 标签
     * @return 标签
     */
    public String getLabel()
    {
        return _label;
    }

	/**
	 * 得到单元格数据类型
	 */
	public int getDataType()
	{
		return _dataType;
	}

	/**
	 * 得到数据库字段名称.
	 */
	public String getDBFieldName()
	{
		return _dbfieldName;
	}

	/**
	 * 得到单元格标志
	 */
	public boolean getFlag(int flag)
	{
		boolean ret = false;
		if( ( _flag & flag ) > 0 )
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * 得到代码字典ID
	 */
	public String getDictionaryID()
	{
		return _dictionaryID;
	}

	/**
	 * 用户可读的名字，如："货币资金 账面数"
	 * @return
	 */
	public String getReadableName()
	{
		return _scalarName;
	}
}
