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
 * 
 * 
 * @version $Revision$ $Date$
**/
public class Row implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private boolean _isTotalRow = false;

    /**
     * keeps track of state for field: _isTotalRow
    **/
    private boolean _has_isTotalRow;

    private java.util.ArrayList _cellList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Row() {
        super();
        _cellList = new ArrayList();
    } //-- cn.com.youtong.apollo.analyse.xml.Row()


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
    public void deleteIsTotalRow()
    {
        this._has_isTotalRow= false;
    } //-- void deleteIsTotalRow() 

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
     * Returns the value of field 'isTotalRow'.
     * 
     * @return the value of field 'isTotalRow'.
    **/
    public boolean getIsTotalRow()
    {
        return this._isTotalRow;
    } //-- boolean getIsTotalRow() 

    /**
    **/
    public boolean hasIsTotalRow()
    {
        return this._has_isTotalRow;
    } //-- boolean hasIsTotalRow() 

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
     * Sets the value of field 'isTotalRow'.
     * 
     * @param isTotalRow the value of field 'isTotalRow'.
    **/
    public void setIsTotalRow(boolean isTotalRow)
    {
        this._isTotalRow = isTotalRow;
        this._has_isTotalRow = true;
    } //-- void setIsTotalRow(boolean) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.analyse.xml.Row unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.analyse.xml.Row) Unmarshaller.unmarshal(cn.com.youtong.apollo.analyse.xml.Row.class, reader);
    } //-- cn.com.youtong.apollo.analyse.xml.Row unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
