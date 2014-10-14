package cn.com.youtong.apollo.task.db;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import cn.com.youtong.apollo.common.*;
import java.sql.*;
import java.io.*;

public class DBTableView
        implements TableView
{
	private Integer _type;
	private String _xslt;
    public DBTableView(TableViewForm tableViewForm)
    {
		this._type = tableViewForm.getType();
		_xslt = getXSLTString( tableViewForm );
    }

    /**
     * ���XSLT����;����
     * @return ��;����
     *  1--HTML
     *  2--EXCEL
     *  3--PDF
     */
    public Integer getType()
    {
        return _type;
    }


    /**
     * XSML�ļ�
     * @return XSML�ļ�
     */
	public String getXSLTString()
	{
		return _xslt;
	}

    private String getXSLTString(TableViewForm tableViewForm)
    {
        try {
            return Convertor.Clob2String(tableViewForm.getContent());
        }
        catch (SQLException ex) {
            return "";
        }
        catch (IOException ex) {
            return "";
        }
    }
}