/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.analyse.xml;

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
 * 表头
 * 
 * @version $Revision$ $Date$
**/
public class Head implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 总列数
    **/
    private int _colnum;

    /**
     * keeps track of state for field: _colnum
    **/
    private boolean _has_colnum;

    /**
     * 总行数
    **/
    private int _rownum;

    /**
     * keeps track of state for field: _rownum
    **/
    private boolean _has_rownum;

    /**
     * 设置缺省Cell颜色，引用Color定义里面的ID
    **/
    private java.lang.String _defaultColor = "#FFFFFF";

    /**
     * 缺省使用的字体，引用Font定义里面的ID
    **/
    private int _defaultFontID;

    /**
     * keeps track of state for field: _defaultFontID
    **/
    private boolean _has_defaultFontID;

    private java.util.ArrayList _rowList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Head() {
        super();
        setDefaultColor("#FFFFFF");
        _rowList = new ArrayList();
    } //-- cn.com.youtong.apollo.analyse.xml.Head()


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
    **/
    public void clearRow()
    {
        _rowList.clear();
    } //-- void clearRow() 

    /**
    **/
    public void deleteDefaultFontID()
    {
        this._has_defaultFontID= false;
    } //-- void deleteDefaultFontID() 

    /**
    **/
    public java.util.Enumeration enumerateRow()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_rowList.iterator());
    } //-- java.util.Enumeration enumerateRow() 

    /**
     * Returns the value of field 'colnum'. The field 'colnum' has
     * the following description: 总列数
     * 
     * @return the value of field 'colnum'.
    **/
    public int getColnum()
    {
        return this._colnum;
    } //-- int getColnum() 

    /**
     * Returns the value of field 'defaultColor'. The field
     * 'defaultColor' has the following description:
     * 设置缺省Cell颜色，引用Color定义里面的ID
     * 
     * @return the value of field 'defaultColor'.
    **/
    public java.lang.String getDefaultColor()
    {
        return this._defaultColor;
    } //-- java.lang.String getDefaultColor() 

    /**
     * Returns the value of field 'defaultFontID'. The field
     * 'defaultFontID' has the following description:
     * 缺省使用的字体，引用Font定义里面的ID
     * 
     * @return the value of field 'defaultFontID'.
    **/
    public int getDefaultFontID()
    {
        return this._defaultFontID;
    } //-- int getDefaultFontID() 

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
     * Returns the value of field 'rownum'. The field 'rownum' has
     * the following description: 总行数
     * 
     * @return the value of field 'rownum'.
    **/
    public int getRownum()
    {
        return this._rownum;
    } //-- int getRownum() 

    /**
    **/
    public boolean hasColnum()
    {
        return this._has_colnum;
    } //-- boolean hasColnum() 

    /**
    **/
    public boolean hasDefaultFontID()
    {
        return this._has_defaultFontID;
    } //-- boolean hasDefaultFontID() 

    /**
    **/
    public boolean hasRownum()
    {
        return this._has_rownum;
    } //-- boolean hasRownum() 

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
     * Sets the value of field 'colnum'. The field 'colnum' has the
     * following description: 总列数
     * 
     * @param colnum the value of field 'colnum'.
    **/
    public void setColnum(int colnum)
    {
        this._colnum = colnum;
        this._has_colnum = true;
    } //-- void setColnum(int) 

    /**
     * Sets the value of field 'defaultColor'. The field
     * 'defaultColor' has the following description:
     * 设置缺省Cell颜色，引用Color定义里面的ID
     * 
     * @param defaultColor the value of field 'defaultColor'.
    **/
    public void setDefaultColor(java.lang.String defaultColor)
    {
        this._defaultColor = defaultColor;
    } //-- void setDefaultColor(java.lang.String) 

    /**
     * Sets the value of field 'defaultFontID'. The field
     * 'defaultFontID' has the following description:
     * 缺省使用的字体，引用Font定义里面的ID
     * 
     * @param defaultFontID the value of field 'defaultFontID'.
    **/
    public void setDefaultFontID(int defaultFontID)
    {
        this._defaultFontID = defaultFontID;
        this._has_defaultFontID = true;
    } //-- void setDefaultFontID(int) 

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
     * Sets the value of field 'rownum'. The field 'rownum' has the
     * following description: 总行数
     * 
     * @param rownum the value of field 'rownum'.
    **/
    public void setRownum(int rownum)
    {
        this._rownum = rownum;
        this._has_rownum = true;
    } //-- void setRownum(int) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Head unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Head) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Head.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Head unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
