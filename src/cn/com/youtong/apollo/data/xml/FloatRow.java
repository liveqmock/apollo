/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.data.xml;

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
 * 浮动行
 *
 * @version $Revision$ $Date$
**/
public class FloatRow implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 浮动行ID，对应于任务参数中行的ID
    **/
    private java.lang.String _ID;

    /**
     * 一条浮动行
    **/
    private java.util.ArrayList _rowList;


      //----------------/
     //- Constructors -/
    //----------------/

    public FloatRow() {
        super();
        _rowList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.FloatRow()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vRow
    **/
    public void addRow(RowModel vRow)
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
    public void addRow(int index, RowModel vRow)
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
    public java.util.Enumeration enumerateRow()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_rowList.iterator());
    } //-- java.util.Enumeration enumerateRow()

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 浮动行ID，对应于任务参数中行的ID
     *
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID()

    /**
     *
     *
     * @param index
    **/
    public RowModel getRow(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rowList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (RowModel) _rowList.get(index);
    } //-- Row getRow(int)

    /**
    **/
    public RowModel[] getRow()
    {
        int size = _rowList.size();
        RowModel[] mArray = new RowModel[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (RowModel) _rowList.get(index);
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
    public boolean removeRow(RowModel vRow)
    {
        boolean removed = _rowList.remove(vRow);
        return removed;
    } //-- boolean removeRow(Row)

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 浮动行ID，对应于任务参数中行的ID
     *
     * @param ID the value of field 'ID'.
    **/
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String)

    /**
     *
     *
     * @param index
     * @param vRow
    **/
    public void setRow(int index, RowModel vRow)
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
    public void setRow(RowModel[] rowArray)
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
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.FloatRow unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.FloatRow) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.FloatRow.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.FloatRow unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
