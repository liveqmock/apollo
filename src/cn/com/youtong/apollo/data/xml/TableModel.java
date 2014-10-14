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
 * 数据表
 * 
 * @version $Revision$ $Date$
**/
public class TableModel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 数据表的ID，对应于任务参数中表的ID
    **/
    private java.lang.String _ID;

    /**
     * 单元格
    **/
    private java.util.ArrayList _cellList;

    /**
     * 浮动行
    **/
    private java.util.ArrayList _floatRowList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableModel() {
        super();
        _cellList = new ArrayList();
        _floatRowList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.Table()


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
     * 
     * 
     * @param vFloatRow
    **/
    public void addFloatRow(FloatRow vFloatRow)
        throws java.lang.IndexOutOfBoundsException
    {
        _floatRowList.add(vFloatRow);
    } //-- void addFloatRow(FloatRow) 

    /**
     * 
     * 
     * @param index
     * @param vFloatRow
    **/
    public void addFloatRow(int index, FloatRow vFloatRow)
        throws java.lang.IndexOutOfBoundsException
    {
        _floatRowList.add(index, vFloatRow);
    } //-- void addFloatRow(int, FloatRow) 

    /**
    **/
    public void clearCell()
    {
        _cellList.clear();
    } //-- void clearCell() 

    /**
    **/
    public void clearFloatRow()
    {
        _floatRowList.clear();
    } //-- void clearFloatRow() 

    /**
    **/
    public java.util.Enumeration enumerateCell()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_cellList.iterator());
    } //-- java.util.Enumeration enumerateCell() 

    /**
    **/
    public java.util.Enumeration enumerateFloatRow()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_floatRowList.iterator());
    } //-- java.util.Enumeration enumerateFloatRow() 

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
     * 
     * 
     * @param index
    **/
    public FloatRow getFloatRow(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _floatRowList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (FloatRow) _floatRowList.get(index);
    } //-- FloatRow getFloatRow(int) 

    /**
    **/
    public FloatRow[] getFloatRow()
    {
        int size = _floatRowList.size();
        FloatRow[] mArray = new FloatRow[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (FloatRow) _floatRowList.get(index);
        }
        return mArray;
    } //-- FloatRow[] getFloatRow() 

    /**
    **/
    public int getFloatRowCount()
    {
        return _floatRowList.size();
    } //-- int getFloatRowCount() 

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 数据表的ID，对应于任务参数中表的ID
     * 
     * @return the value of field 'ID'.
    **/
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

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
     * @param vFloatRow
    **/
    public boolean removeFloatRow(FloatRow vFloatRow)
    {
        boolean removed = _floatRowList.remove(vFloatRow);
        return removed;
    } //-- boolean removeFloatRow(FloatRow) 

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
     * @param index
     * @param vFloatRow
    **/
    public void setFloatRow(int index, FloatRow vFloatRow)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _floatRowList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _floatRowList.set(index, vFloatRow);
    } //-- void setFloatRow(int, FloatRow) 

    /**
     * 
     * 
     * @param floatRowArray
    **/
    public void setFloatRow(FloatRow[] floatRowArray)
    {
        //-- copy array
        _floatRowList.clear();
        for (int i = 0; i < floatRowArray.length; i++) {
            _floatRowList.add(floatRowArray[i]);
        }
    } //-- void setFloatRow(FloatRow) 

    /**
     * Sets the value of field 'ID'. The field 'ID' has the
     * following description: 数据表的ID，对应于任务参数中表的ID
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
    public static cn.com.youtong.apollo.data.xml.TableModel unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.TableModel) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.TableModel.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Table unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
