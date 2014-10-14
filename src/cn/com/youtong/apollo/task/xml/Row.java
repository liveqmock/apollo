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
 * 行
 * 
 * @version $Revision$ $Date$
**/
public class Row implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 行标志
    **/
    private int _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    /**
     * 行标识，在同一表中唯一
    **/
    private java.lang.String _ID;

    /**
     * 单元格
    **/
    private java.util.ArrayList _cellList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Row() {
        super();
        _cellList = new ArrayList();
    } //-- cn.com.youtong.apollo.task.xml.Row()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCell
    **/
    public void addCell(Cell vCell)
        throws java.lang.IndexOutOfBoundsException
    {
        _cellList.add(vCell);
    } //-- void addCell(Cell) 

    /**
     * 
     * 
     * @param index
     * @param vCell
    **/
    public void addCell(int index, Cell vCell)
        throws java.lang.IndexOutOfBoundsException
    {
        _cellList.add(index, vCell);
    } //-- void addCell(int, Cell) 

    /**
    **/
    public void clearCell()
    {
        _cellList.clear();
    } //-- void clearCell() 

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag() 

    /**
    **/
    public java.util.Enumeration enumerateCell()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_cellList.iterator());
    } //-- java.util.Enumeration enumerateCell() 

    /**
     * 
     * 
     * @param index
    **/
    public Cell getCell(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _cellList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Cell) _cellList.get(index);
    } //-- Cell getCell(int) 

    /**
    **/
    public Cell[] getCell()
    {
        int size = _cellList.size();
        Cell[] mArray = new Cell[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Cell) _cellList.get(index);
        }
        return mArray;
    } //-- Cell[] getCell() 

    /**
    **/
    public int getCellCount()
    {
        return _cellList.size();
    } //-- int getCellCount() 

    /**
     * Returns the value of field 'flag'. The field 'flag' has the
     * following description: 行标志
     * 
     * @return the value of field 'flag'.
    **/
    public int getFlag()
    {
        return this._flag;
    } //-- int getFlag() 

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 行标识，在同一表中唯一
     * 
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
    **/
    public boolean hasFlag()
    {
        return this._has_flag;
    } //-- boolean hasFlag() 

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
     * @param vCell
    **/
    public boolean removeCell(Cell vCell)
    {
        boolean removed = _cellList.remove(vCell);
        return removed;
    } //-- boolean removeCell(Cell) 

    /**
     * 
     * 
     * @param index
     * @param vCell
    **/
    public void setCell(int index, Cell vCell)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _cellList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _cellList.set(index, vCell);
    } //-- void setCell(int, Cell) 

    /**
     * 
     * 
     * @param cellArray
    **/
    public void setCell(Cell[] cellArray)
    {
        //-- copy array
        _cellList.clear();
        for (int i = 0; i < cellArray.length; i++) {
            _cellList.add(cellArray[i]);
        }
    } //-- void setCell(Cell) 

    /**
     * Sets the value of field 'flag'. The field 'flag' has the
     * following description: 行标志
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
     * following description: 行标识，在同一表中唯一
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
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.Row unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.Row) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.Row.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.Row unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
