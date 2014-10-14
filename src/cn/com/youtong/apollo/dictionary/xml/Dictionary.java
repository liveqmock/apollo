/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.dictionary.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 代码字典
 *
 * @version $Revision$ $Date$
**/
public class Dictionary implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _memo;

    /**
     * 代码字典分级位置，如2,2,2
    **/
    private java.lang.String _levelPosition;

    private int _keyLength;

    /**
     * keeps track of state for field: _keyLength
    **/
    private boolean _has_keyLength;

    /**
     * 代码字典标识，在整个系统中唯一
    **/
    private java.lang.String _ID;

    /**
     * 代码字典标志
    **/
    private int _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    /**
     * 代码字典的最后修改时间
    **/
    private java.util.Date _dateModified;

    /**
     * 代码字典条目
    **/
    private java.util.ArrayList _itemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Dictionary() {
        super();
        _itemList = new ArrayList();
    } //-- cn.com.youtong.apollo.dictionary.xml.Dictionary()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vItem
    **/
    public void addItem(Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(vItem);
    } //-- void addItem(Item)

    /**
     *
     *
     * @param index
     * @param vItem
    **/
    public void addItem(int index, Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _itemList.add(index, vItem);
    } //-- void addItem(int, Item)

    /**
    **/
    public void clearItem()
    {
        _itemList.clear();
    } //-- void clearItem()

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag()

    /**
    **/
    public java.util.Enumeration enumerateItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_itemList.iterator());
    } //-- java.util.Enumeration enumerateItem()

    /**
     * Returns the value of field 'dateModified'. The field
     * 'dateModified' has the following description: 代码字典的最后修改时间
     *
     * @return the value of field 'dateModified'.
    **/
    public java.util.Date getDateModified()
    {
        return this._dateModified;
    } //-- java.util.Date getDateModified()

    /**
     * Returns the value of field 'flag'. The field 'flag' has the
     * following description: 代码字典标志
     *
     * @return the value of field 'flag'.
    **/
    public int getFlag()
    {
        return this._flag;
    } //-- int getFlag()

    /**
     * Returns the value of field 'ID'. The field 'ID' has the
     * following description: 代码字典标识，在整个系统中唯一
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
    public Item getItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Item) _itemList.get(index);
    } //-- Item getItem(int)

    /**
    **/
    public Item[] getItem()
    {
        int size = _itemList.size();
        Item[] mArray = new Item[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Item) _itemList.get(index);
        }
        return mArray;
    } //-- Item[] getItem()

    /**
    **/
    public int getItemCount()
    {
        return _itemList.size();
    } //-- int getItemCount()

    /**
     * Returns the value of field 'keyLength'.
     *
     * @return the value of field 'keyLength'.
    **/
    public int getKeyLength()
    {
        return this._keyLength;
    } //-- int getKeyLength()

    /**
     * Returns the value of field 'levelPosition'. The field
     * 'levelPosition' has the following description:
     * 代码字典分级位置，如2,2,2
     *
     * @return the value of field 'levelPosition'.
    **/
    public java.lang.String getLevelPosition()
    {
        return this._levelPosition;
    } //-- java.lang.String getLevelPosition()

    /**
     * Returns the value of field 'memo'.
     *
     * @return the value of field 'memo'.
    **/
    public java.lang.String getMemo()
    {
        return this._memo;
    } //-- java.lang.String getMemo()

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
    **/
    public boolean hasFlag()
    {
        return this._has_flag;
    } //-- boolean hasFlag()

    /**
    **/
    public boolean hasKeyLength()
    {
        return this._has_keyLength;
    } //-- boolean hasKeyLength()

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
		try
		{
			Marshaller marshaller = new Marshaller(out);
			marshaller.setEncoding("gb2312");
			marshaller.marshal(this);
		}
		catch (IOException ex)
		{
			throw new MarshalException(ex);
		}
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
     * @param vItem
    **/
    public boolean removeItem(Item vItem)
    {
        boolean removed = _itemList.remove(vItem);
        return removed;
    } //-- boolean removeItem(Item)

    /**
     * Sets the value of field 'dateModified'. The field
     * 'dateModified' has the following description: 代码字典的最后修改时间
     *
     * @param dateModified the value of field 'dateModified'.
    **/
    public void setDateModified(java.util.Date dateModified)
    {
        this._dateModified = dateModified;
    } //-- void setDateModified(java.util.Date)

    /**
     * Sets the value of field 'flag'. The field 'flag' has the
     * following description: 代码字典标志
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
     * following description: 代码字典标识，在整个系统中唯一
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
     * @param vItem
    **/
    public void setItem(int index, Item vItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _itemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _itemList.set(index, vItem);
    } //-- void setItem(int, Item)

    /**
     *
     *
     * @param itemArray
    **/
    public void setItem(Item[] itemArray)
    {
        //-- copy array
        _itemList.clear();
        for (int i = 0; i < itemArray.length; i++) {
            _itemList.add(itemArray[i]);
        }
    } //-- void setItem(Item)

    /**
     * Sets the value of field 'keyLength'.
     *
     * @param keyLength the value of field 'keyLength'.
    **/
    public void setKeyLength(int keyLength)
    {
        this._keyLength = keyLength;
        this._has_keyLength = true;
    } //-- void setKeyLength(int)

    /**
     * Sets the value of field 'levelPosition'. The field
     * 'levelPosition' has the following description:
     * 代码字典分级位置，如2,2,2
     *
     * @param levelPosition the value of field 'levelPosition'.
    **/
    public void setLevelPosition(java.lang.String levelPosition)
    {
        this._levelPosition = levelPosition;
    } //-- void setLevelPosition(java.lang.String)

    /**
     * Sets the value of field 'memo'.
     *
     * @param memo the value of field 'memo'.
    **/
    public void setMemo(java.lang.String memo)
    {
        this._memo = memo;
    } //-- void setMemo(java.lang.String)

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
     * @param reader
    **/
    public static cn.com.youtong.apollo.dictionary.xml.Dictionary unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.dictionary.xml.Dictionary) Unmarshaller.unmarshal(cn.com.youtong.apollo.dictionary.xml.Dictionary.class, reader);
    } //-- cn.com.youtong.apollo.dictionary.xml.Dictionary unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
