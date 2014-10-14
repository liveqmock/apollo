/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.usermanager.db.xml;

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
public class Group implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private boolean _flag;

    /**
     * keeps track of state for field: _flag
    **/
    private boolean _has_flag;

    private java.lang.String _memo;

    private java.util.ArrayList _memberList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Group() {
        super();
        _memberList = new ArrayList();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Group()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vMember
    **/
    public void addMember(Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        _memberList.add(vMember);
    } //-- void addMember(Member) 

    /**
     * 
     * 
     * @param index
     * @param vMember
    **/
    public void addMember(int index, Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        _memberList.add(index, vMember);
    } //-- void addMember(int, Member) 

    /**
    **/
    public void clearMember()
    {
        _memberList.clear();
    } //-- void clearMember() 

    /**
    **/
    public void deleteFlag()
    {
        this._has_flag= false;
    } //-- void deleteFlag() 

    /**
    **/
    public java.util.Enumeration enumerateMember()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_memberList.iterator());
    } //-- java.util.Enumeration enumerateMember() 

    /**
     * Returns the value of field 'flag'.
     * 
     * @return the value of field 'flag'.
    **/
    public boolean getFlag()
    {
        return this._flag;
    } //-- boolean getFlag() 

    /**
     * 
     * 
     * @param index
    **/
    public Member getMember(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _memberList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Member) _memberList.get(index);
    } //-- Member getMember(int) 

    /**
    **/
    public Member[] getMember()
    {
        int size = _memberList.size();
        Member[] mArray = new Member[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Member) _memberList.get(index);
        }
        return mArray;
    } //-- Member[] getMember() 

    /**
    **/
    public int getMemberCount()
    {
        return _memberList.size();
    } //-- int getMemberCount() 

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
     * @param vMember
    **/
    public boolean removeMember(Member vMember)
    {
        boolean removed = _memberList.remove(vMember);
        return removed;
    } //-- boolean removeMember(Member) 

    /**
     * Sets the value of field 'flag'.
     * 
     * @param flag the value of field 'flag'.
    **/
    public void setFlag(boolean flag)
    {
        this._flag = flag;
        this._has_flag = true;
    } //-- void setFlag(boolean) 

    /**
     * 
     * 
     * @param index
     * @param vMember
    **/
    public void setMember(int index, Member vMember)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _memberList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _memberList.set(index, vMember);
    } //-- void setMember(int, Member) 

    /**
     * 
     * 
     * @param memberArray
    **/
    public void setMember(Member[] memberArray)
    {
        //-- copy array
        _memberList.clear();
        for (int i = 0; i < memberArray.length; i++) {
            _memberList.add(memberArray[i]);
        }
    } //-- void setMember(Member) 

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
    public static cn.com.youtong.apollo.usermanager.db.xml.Group unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Group) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Group.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Group unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
