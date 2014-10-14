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
 * 单位
 *
 * @version $Revision$ $Date$
**/
public class Unit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 单位ID
    **/
    private java.lang.String _ID;

    /**
     * 数据表
    **/
    private java.util.ArrayList _tableList;

    /**
     * 附件，用base64编码
    **/
    private Attachment _attachment;


      //----------------/
     //- Constructors -/
    //----------------/

    public Unit() {
        super();
        _tableList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.Unit()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vTable
    **/
    public void addTable(TableModel vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(vTable);
    } //-- void addTable(Table)

    /**
     *
     *
     * @param index
     * @param vTable
    **/
    public void addTable(int index, TableModel vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        _tableList.add(index, vTable);
    } //-- void addTable(int, Table)

    /**
    **/
    public void clearTable()
    {
        _tableList.clear();
    } //-- void clearTable()

    /**
    **/
    public java.util.Enumeration enumerateTable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_tableList.iterator());
    } //-- java.util.Enumeration enumerateTable()

    /**
     * Returns the value of field 'attachment'. The field
     * 'attachment' has the following description: 附件，用base64编码
     *
     * @return the value of field 'attachment'.
    **/
    public Attachment getAttachment()
    {
        return this._attachment;
    } //-- Attachment getAttachment()

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 单位ID
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
    public TableModel getTable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (TableModel) _tableList.get(index);
    } //-- Table getTable(int)

    /**
    **/
    public TableModel[] getTable()
    {
        int size = _tableList.size();
        TableModel[] mArray = new TableModel[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (TableModel) _tableList.get(index);
        }
        return mArray;
    } //-- Table[] getTable()

    /**
    **/
    public int getTableCount()
    {
        return _tableList.size();
    } //-- int getTableCount()

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
     * @param vTable
    **/
    public boolean removeTable(TableModel vTable)
    {
        boolean removed = _tableList.remove(vTable);
        return removed;
    } //-- boolean removeTable(Table)

    /**
     * Sets the value of field 'attachment'. The field 'attachment'
     * has the following description: 附件，用base64编码
     *
     * @param attachment the value of field 'attachment'.
    **/
    public void setAttachment(Attachment attachment)
    {
        this._attachment = attachment;
    } //-- void setAttachment(Attachment)

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 单位ID
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
     * @param vTable
    **/
    public void setTable(int index, TableModel vTable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _tableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _tableList.set(index, vTable);
    } //-- void setTable(int, Table)

    /**
     *
     *
     * @param tableArray
    **/
    public void setTable(TableModel[] tableArray)
    {
        //-- copy array
        _tableList.clear();
        for (int i = 0; i < tableArray.length; i++) {
            _tableList.add(tableArray[i]);
        }
    } //-- void setTable(Table)

    /**
     *
     *
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.Unit unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.Unit) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.Unit.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Unit unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
