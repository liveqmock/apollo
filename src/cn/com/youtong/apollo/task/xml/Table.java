/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.task.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 表
 * 
 * @version $Revision$ $Date$
**/
public class Table implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    /**
     * 表的标识，在同一任务中唯一
    **/
    private java.lang.String _ID;

    /**
     * 该表是否是用户原数据表：是用户原数据表，则为true；否则为false
    **/
    private boolean _isUnitMetaTable;

    /**
     * keeps track of state for field: _isUnitMetaTable
    **/
    private boolean _has_isUnitMetaTable;

    /**
     * 单元格标志，1代表该单元格为汇总单元格；否则为0
    **/
    private int _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    /**
     * 行
    **/
    private java.util.ArrayList _rowList;

    /**
     * 表样信息---XSLT字符串（ 左右尖括号 需要转义为“<”和“>”）
    **/
    private java.util.ArrayList _tableViewList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Table() {
        super();
        _rowList = new ArrayList();
        _tableViewList = new ArrayList();
    } //-- cn.com.youtong.apollo.task.xml.Table()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vRow
    **/
    public void addRow(Row vRow)
        throws java.lang.IndexOutOfBoundsException
    {
        _rowList.add(vRow);
    } //-- void addRow(Row) 

    /**
     * 
     * 
     * @param index
     * @param vRow
    **/
    public void addRow(int index, Row vRow)
        throws java.lang.IndexOutOfBoundsException
    {
        _rowList.add(index, vRow);
    } //-- void addRow(int, Row) 

    /**
     * 
     * 
     * @param vTableView
    **/
    public void addTableView(TableView vTableView)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableViewList.add(vTableView);
    } //-- void addTableView(TableView) 

    /**
     * 
     * 
     * @param index
     * @param vTableView
    **/
    public void addTableView(int index, TableView vTableView)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableViewList.add(index, vTableView);
    } //-- void addTableView(int, TableView) 

    /**
    **/
    public void clearRow()
    {
        _rowList.clear();
    } //-- void clearRow() 

    /**
    **/
    public void clearTableView()
    {
        _tableViewList.clear();
    } //-- void clearTableView() 

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag() 

    /**
    **/
    public java.util.Enumeration enumerateRow()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_rowList.iterator());
    } //-- java.util.Enumeration enumerateRow() 

    /**
    **/
    public java.util.Enumeration enumerateTableView()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_tableViewList.iterator());
    } //-- java.util.Enumeration enumerateTableView() 

    /**
     * Returns the value of field 'flag'. The field 'flag' has the
     * following description: 单元格标志，1代表该单元格为汇总单元格；否则为0
     * 
     * @return the value of field 'flag'.
    **/
    public int getFlag()
    {
        return this._flag;
    } //-- int getFlag() 

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 表的标识，在同一任务中唯一
     * 
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'isUnitMetaTable'. The field
     * 'isUnitMetaTable' has the following description:
     * 该表是否是用户原数据表：是用户原数据表，则为true；否则为false
     * 
     * @return the value of field 'isUnitMetaTable'.
    **/
    public boolean getIsUnitMetaTable()
    {
        return this._isUnitMetaTable;
    } //-- boolean getIsUnitMetaTable() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * 
     * 
     * @param index
    **/
    public Row getRow(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rowList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Row) _rowList.get(index);
    } //-- Row getRow(int) 

    /**
    **/
    public Row[] getRow()
    {
        int size = _rowList.size();
        Row[] mArray = new Row[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Row) _rowList.get(index);
        }
        return mArray;
    } //-- Row[] getRow() 

    /**
    **/
    public int getRowCount()
    {
        return _rowList.size();
    } //-- int getRowCount() 

    /**
     * 
     * 
     * @param index
    **/
    public TableView getTableView(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableViewList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (TableView) _tableViewList.get(index);
    } //-- TableView getTableView(int) 

    /**
    **/
    public TableView[] getTableView()
    {
        int size = _tableViewList.size();
        TableView[] mArray = new TableView[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (TableView) _tableViewList.get(index);
        }
        return mArray;
    } //-- TableView[] getTableView() 

    /**
    **/
    public int getTableViewCount()
    {
        return _tableViewList.size();
    } //-- int getTableViewCount() 

    /**
    **/
    public boolean hasFlag()
    {
        return this._has_flag;
    } //-- boolean hasFlag() 

    /**
    **/
    public boolean hasIsUnitMetaTable()
    {
        return this._has_isUnitMetaTable;
    } //-- boolean hasIsUnitMetaTable() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * 
     * 
     * @param vRow
    **/
    public boolean removeRow(Row vRow)
    {
        boolean removed = _rowList.remove(vRow);
        return removed;
    } //-- boolean removeRow(Row) 

    /**
     * 
     * 
     * @param vTableView
    **/
    public boolean removeTableView(TableView vTableView)
    {
        boolean removed = _tableViewList.remove(vTableView);
        return removed;
    } //-- boolean removeTableView(TableView) 

    /**
     * Sets the value of field 'flag'. The field 'flag' has the
     * following description: 单元格标志，1代表该单元格为汇总单元格；否则为0
     * 
     * @param flag the value of field 'flag'.
    **/
    public void setFlag(int flag)
    {
        this._flag = flag;
        this._has_flag = true;
    } //-- void setFlag(int) 

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 表的标识，在同一任务中唯一
     * 
     * @param ID the value of field 'ID'.
    **/
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'isUnitMetaTable'. The field
     * 'isUnitMetaTable' has the following description:
     * 该表是否是用户原数据表：是用户原数据表，则为true；否则为false
     * 
     * @param isUnitMetaTable the value of field 'isUnitMetaTable'.
    **/
    public void setIsUnitMetaTable(boolean isUnitMetaTable)
    {
        this._isUnitMetaTable = isUnitMetaTable;
        this._has_isUnitMetaTable = true;
    } //-- void setIsUnitMetaTable(boolean) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vRow
    **/
    public void setRow(int index, Row vRow)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rowList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _rowList.set(index, vRow);
    } //-- void setRow(int, Row) 

    /**
     * 
     * 
     * @param rowArray
    **/
    public void setRow(Row[] rowArray)
    {
        //-- copy array
        _rowList.clear();
        for (int i = 0; i < rowArray.length; i++) {
            _rowList.add(rowArray[i]);
        }
    } //-- void setRow(Row) 

    /**
     * 
     * 
     * @param index
     * @param vTableView
    **/
    public void setTableView(int index, TableView vTableView)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableViewList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableViewList.set(index, vTableView);
    } //-- void setTableView(int, TableView) 

    /**
     * 
     * 
     * @param tableViewArray
    **/
    public void setTableView(TableView[] tableViewArray)
    {
        //-- copy array
        _tableViewList.clear();
        for (int i = 0; i < tableViewArray.length; i++) {
            _tableViewList.add(tableViewArray[i]);
        }
    } //-- void setTableView(TableView) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.Table unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.Table) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.Table.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.Table unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
