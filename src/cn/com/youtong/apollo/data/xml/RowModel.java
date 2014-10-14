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
 * 一条浮动行
 * 
 * @version $Revision$ $Date$
**/
public class RowModel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 单元格
    **/
    private java.util.ArrayList _cellList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RowModel() {
        super();
        _cellList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.Row()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCell
    **/
    public void addCell(CellModel vCell)
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
    public void addCell(int index, CellModel vCell)
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
    public java.util.Enumeration enumerateCell()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_cellList.iterator());
    } //-- java.util.Enumeration enumerateCell() 

    /**
     * 
     * 
     * @param index
    **/
    public CellModel getCell(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _cellList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (CellModel) _cellList.get(index);
    } //-- Cell getCell(int) 

    /**
    **/
    public CellModel[] getCell()
    {
        int size = _cellList.size();
        CellModel[] mArray = new CellModel[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (CellModel) _cellList.get(index);
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
    public boolean removeCell(CellModel vCell)
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
    public void setCell(int index, CellModel vCell)
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
    public void setCell(CellModel[] cellArray)
    {
        //-- copy array
        _cellList.clear();
        for (int i = 0; i < cellArray.length; i++) {
            _cellList.add(cellArray[i]);
        }
    } //-- void setCell(Cell) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.RowModel unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.RowModel) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.RowModel.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Row unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
