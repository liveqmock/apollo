/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package cn.com.youtong.apollo.servlet.unittree.xml;

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
public class TreeRoot implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.ArrayList _treeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TreeRoot() {
        super();
        _treeList = new ArrayList();
    } //-- cn.com.youtong.apollo.servlet.unittree.xml.TreeRoot()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     *
     *
     * @param vTree
    **/
    public void addTree(Tree vTree)
        throws java.lang.IndexOutOfBoundsException
    {
        _treeList.add(vTree);
    } //-- void addTree(Tree)

    /**
     *
     *
     * @param index
     * @param vTree
    **/
    public void addTree(int index, Tree vTree)
        throws java.lang.IndexOutOfBoundsException
    {
        _treeList.add(index, vTree);
    } //-- void addTree(int, Tree)

    /**
    **/
    public void clearTree()
    {
        _treeList.clear();
    } //-- void clearTree()

    /**
    **/
    public java.util.Enumeration enumerateTree()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_treeList.iterator());
    } //-- java.util.Enumeration enumerateTree()

    /**
     *
     *
     * @param index
    **/
    public Tree getTree(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _treeList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (Tree) _treeList.get(index);
    } //-- Tree getTree(int)

    /**
    **/
    public Tree[] getTree()
    {
        int size = _treeList.size();
        Tree[] mArray = new Tree[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Tree) _treeList.get(index);
        }
        return mArray;
    } //-- Tree[] getTree()

    /**
    **/
    public int getTreeCount()
    {
        return _treeList.size();
    } //-- int getTreeCount()

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
		catch(IOException ex)
		{
			ex.printStackTrace();
			throw new org.exolab.castor.xml.MarshalException(ex.getMessage());
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
     * @param vTree
    **/
    public boolean removeTree(Tree vTree)
    {
        boolean removed = _treeList.remove(vTree);
        return removed;
    } //-- boolean removeTree(Tree)

    /**
     *
     *
     * @param index
     * @param vTree
    **/
    public void setTree(int index, Tree vTree)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _treeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _treeList.set(index, vTree);
    } //-- void setTree(int, Tree)

    /**
     *
     *
     * @param treeArray
    **/
    public void setTree(Tree[] treeArray)
    {
        //-- copy array
        _treeList.clear();
        for (int i = 0; i < treeArray.length; i++) {
            _treeList.add(treeArray[i]);
        }
    } //-- void setTree(Tree)

    /**
     *
     *
     * @param reader
    **/
    public static cn.com.youtong.apollo.servlet.unittree.xml.TreeRoot unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.servlet.unittree.xml.TreeRoot) Unmarshaller.unmarshal(cn.com.youtong.apollo.servlet.unittree.xml.TreeRoot.class, reader);
    } //-- cn.com.youtong.apollo.servlet.unittree.xml.TreeRoot unmarshal(java.io.Reader)

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
