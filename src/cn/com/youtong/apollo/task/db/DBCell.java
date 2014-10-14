/*
 * ��Ԫ�����
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
	 * ���캯��
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
     * ��ǩ
     * @return ��ǩ
     */
    public String getLabel()
    {
        return _label;
    }

	/**
	 * �õ���Ԫ����������
	 */
	public int getDataType()
	{
		return _dataType;
	}

	/**
	 * �õ����ݿ��ֶ�����.
	 */
	public String getDBFieldName()
	{
		return _dbfieldName;
	}

	/**
	 * �õ���Ԫ���־
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
	 * �õ������ֵ�ID
	 */
	public String getDictionaryID()
	{
		return _dictionaryID;
	}

	/**
	 * �û��ɶ������֣��磺"�����ʽ� ������"
	 * @return
	 */
	public String getReadableName()
	{
		return _scalarName;
	}
}
