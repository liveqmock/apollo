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
public class Groups implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _groupList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Groups() {
        super();
        _groupList = new ArrayList();
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Groups()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vGroup
    **/
    public void addGroup(Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.add(vGroup);
    } //-- void addGroup(Group) 

    /**
     * 
     * 
     * @param index
     * @param vGroup
    **/
    public void addGroup(int index, Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupList.add(index, vGroup);
    } //-- void addGroup(int, Group) 

    /**
    **/
    public void clearGroup()
    {
        _groupList.clear();
    } //-- void clearGroup() 

    /**
    **/
    public java.util.Enumeration enumerateGroup()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_groupList.iterator());
    } //-- java.util.Enumeration enumerateGroup() 

    /**
     * 
     * 
     * @param index
    **/
    public Group getGroup(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Group) _groupList.get(index);
    } //-- Group getGroup(int) 

    /**
    **/
    public Group[] getGroup()
    {
        int size = _groupList.size();
        Group[] mArray = new Group[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Group) _groupList.get(index);
        }
        return mArray;
    } //-- Group[] getGroup() 

    /**
    **/
    public int getGroupCount()
    {
        return _groupList.size();
    } //-- int getGroupCount() 

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
     * @param vGroup
    **/
    public boolean removeGroup(Group vGroup)
    {
        boolean removed = _groupList.remove(vGroup);
        return removed;
    } //-- boolean removeGroup(Group) 

    /**
     * 
     * 
     * @param index
     * @param vGroup
    **/
    public void setGroup(int index, Group vGroup)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupList.set(index, vGroup);
    } //-- void setGroup(int, Group) 

    /**
     * 
     * 
     * @param groupArray
    **/
    public void setGroup(Group[] groupArray)
    {
        //-- copy array
        _groupList.clear();
        for (int i = 0; i < groupArray.length; i++) {
            _groupList.add(groupArray[i]);
        }
    } //-- void setGroup(Group) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.usermanager.db.xml.Groups unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.usermanager.db.xml.Groups) Unmarshaller.unmarshal(cn.com.youtong.apollo.usermanager.db.xml.Groups.class, reader);
    } //-- cn.com.youtong.apollo.usermanager.db.xml.Groups unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
