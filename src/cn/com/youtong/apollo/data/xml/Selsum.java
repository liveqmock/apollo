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
 * 选择汇总方案
 * 
 * @version $Revision$ $Date$
**/
public class Selsum implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _idprefix;

    /**
     * 审核大条件
    **/
    private java.lang.String _scriptglobalcondition;

    /**
     * 公共脚本
    **/
    private java.lang.String _script;

    /**
     * 选择汇总节点
    **/
    private java.util.ArrayList _nodeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Selsum() {
        super();
        _nodeList = new ArrayList();
    } //-- cn.com.youtong.apollo.data.xml.Selsum()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vNode
    **/
    public void addNode(Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _nodeList.add(vNode);
    } //-- void addNode(Node) 

    /**
     * 
     * 
     * @param index
     * @param vNode
    **/
    public void addNode(int index, Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        _nodeList.add(index, vNode);
    } //-- void addNode(int, Node) 

    /**
    **/
    public void clearNode()
    {
        _nodeList.clear();
    } //-- void clearNode() 

    /**
    **/
    public java.util.Enumeration enumerateNode()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_nodeList.iterator());
    } //-- java.util.Enumeration enumerateNode() 

    /**
     * Returns the value of field 'idprefix'.
     * 
     * @return the value of field 'idprefix'.
    **/
    public java.lang.String getIdprefix()
    {
        return this._idprefix;
    } //-- java.lang.String getIdprefix() 

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
     * 
     * 
     * @param index
    **/
    public Node getNode(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Node) _nodeList.get(index);
    } //-- Node getNode(int) 

    /**
    **/
    public Node[] getNode()
    {
        int size = _nodeList.size();
        Node[] mArray = new Node[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Node) _nodeList.get(index);
        }
        return mArray;
    } //-- Node[] getNode() 

    /**
    **/
    public int getNodeCount()
    {
        return _nodeList.size();
    } //-- int getNodeCount() 

    /**
     * Returns the value of field 'script'. The field 'script' has
     * the following description: 公共脚本
     * 
     * @return the value of field 'script'.
    **/
    public java.lang.String getScript()
    {
        return this._script;
    } //-- java.lang.String getScript() 

    /**
     * Returns the value of field 'scriptglobalcondition'. The
     * field 'scriptglobalcondition' has the following description:
     * 审核大条件
     * 
     * @return the value of field 'scriptglobalcondition'.
    **/
    public java.lang.String getScriptglobalcondition()
    {
        return this._scriptglobalcondition;
    } //-- java.lang.String getScriptglobalcondition() 

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
     * @param vNode
    **/
    public boolean removeNode(Node vNode)
    {
        boolean removed = _nodeList.remove(vNode);
        return removed;
    } //-- boolean removeNode(Node) 

    /**
     * Sets the value of field 'idprefix'.
     * 
     * @param idprefix the value of field 'idprefix'.
    **/
    public void setIdprefix(java.lang.String idprefix)
    {
        this._idprefix = idprefix;
    } //-- void setIdprefix(java.lang.String) 

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
     * @param index
     * @param vNode
    **/
    public void setNode(int index, Node vNode)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _nodeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _nodeList.set(index, vNode);
    } //-- void setNode(int, Node) 

    /**
     * 
     * 
     * @param nodeArray
    **/
    public void setNode(Node[] nodeArray)
    {
        //-- copy array
        _nodeList.clear();
        for (int i = 0; i < nodeArray.length; i++) {
            _nodeList.add(nodeArray[i]);
        }
    } //-- void setNode(Node) 

    /**
     * Sets the value of field 'script'. The field 'script' has the
     * following description: 公共脚本
     * 
     * @param script the value of field 'script'.
    **/
    public void setScript(java.lang.String script)
    {
        this._script = script;
    } //-- void setScript(java.lang.String) 

    /**
     * Sets the value of field 'scriptglobalcondition'. The field
     * 'scriptglobalcondition' has the following description: 审核大条件
     * 
     * @param scriptglobalcondition the value of field
     * 'scriptglobalcondition'.
    **/
    public void setScriptglobalcondition(java.lang.String scriptglobalcondition)
    {
        this._scriptglobalcondition = scriptglobalcondition;
    } //-- void setScriptglobalcondition(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static cn.com.youtong.apollo.data.xml.Selsum unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (cn.com.youtong.apollo.data.xml.Selsum) Unmarshaller.unmarshal(cn.com.youtong.apollo.data.xml.Selsum.class, reader);
    } //-- cn.com.youtong.apollo.data.xml.Selsum unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
