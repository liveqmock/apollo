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
 * 脚本的执行顺序
 * 
 * @version $Revision$ $Date$
**/
public class Sequence implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * 脚本条目
    **/
    private java.util.ArrayList _scriptEntryList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sequence() {
        super();
        _scriptEntryList = new ArrayList();
    } //-- cn.com.youtong.apollo.task.xml.Sequence()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vScriptEntry
    **/
    public void addScriptEntry(ScriptEntry vScriptEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _scriptEntryList.add(vScriptEntry);
    } //-- void addScriptEntry(ScriptEntry) 

    /**
     * 
     * 
     * @param index
     * @param vScriptEntry
    **/
    public void addScriptEntry(int index, ScriptEntry vScriptEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        _scriptEntryList.add(index, vScriptEntry);
    } //-- void addScriptEntry(int, ScriptEntry) 

    /**
    **/
    public void clearScriptEntry()
    {
        _scriptEntryList.clear();
    } //-- void clearScriptEntry() 

    /**
    **/
    public java.util.Enumeration enumerateScriptEntry()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_scriptEntryList.iterator());
    } //-- java.util.Enumeration enumerateScriptEntry() 

    /**
     * 
     * 
     * @param index
    **/
    public ScriptEntry getScriptEntry(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _scriptEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ScriptEntry) _scriptEntryList.get(index);
    } //-- ScriptEntry getScriptEntry(int) 

    /**
    **/
    public ScriptEntry[] getScriptEntry()
    {
        int size = _scriptEntryList.size();
        ScriptEntry[] mArray = new ScriptEntry[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ScriptEntry) _scriptEntryList.get(index);
        }
        return mArray;
    } //-- ScriptEntry[] getScriptEntry() 

    /**
    **/
    public int getScriptEntryCount()
    {
        return _scriptEntryList.size();
    } //-- int getScriptEntryCount() 

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
     * @param vScriptEntry
    **/
    public boolean removeScriptEntry(ScriptEntry vScriptEntry)
    {
        boolean removed = _scriptEntryList.remove(vScriptEntry);
        return removed;
    } //-- boolean removeScriptEntry(ScriptEntry) 

    /**
     * 
     * 
     * @param index
     * @param vScriptEntry
    **/
    public void setScriptEntry(int index, ScriptEntry vScriptEntry)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _scriptEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _scriptEntryList.set(index, vScriptEntry);
    } //-- void setScriptEntry(int, ScriptEntry) 

    /**
     * 
     * 
     * @param scriptEntryArray
    **/
    public void setScriptEntry(ScriptEntry[] scriptEntryArray)
    {
        //-- copy array
        _scriptEntryList.clear();
        for (int i = 0; i < scriptEntryArray.length; i++) {
            _scriptEntryList.add(scriptEntryArray[i]);
        }
    } //-- void setScriptEntry(ScriptEntry) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.task.xml.Sequence unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.task.xml.Sequence) Unmarshaller.unmarshal(cn.com.youtong.apollo.task.xml.Sequence.class, reader);
    } //-- cn.com.youtong.apollo.task.xml.Sequence unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
