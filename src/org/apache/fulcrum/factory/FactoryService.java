package org.apache.fulcrum.factory;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Turbine" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Turbine", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


/**
 * The Factory Service instantiates objects using either default
 * class loaders or a specified one. Whether specified class
 * loaders are supported for a class depends on implementation
 * and can be tested with the isLoaderSupported method.
 *
 * @author <a href="mailto:epugh@upstate.com">Eric Pugh</a>
 * @author <a href="mailto:ilkka.priha@simsoft.fi">Ilkka Priha</a>
 * @version $Id: FactoryService.java,v 1.1 2003/11/15 16:13:23 epugh Exp $
 */
public interface FactoryService
{
    /**
     * The key under which this component is known by an avalon container.
     */
    String ROLE = FactoryService.class.getName();


	/**
	* Gets an instance of a class.
	*
	* @param clazz the name of the class.
	* @return the instance.
	* @throws ServiceException if instantiation fails.
	*/
   public Object getInstance(Class clazz)
	   throws FactoryException;

    /**
     * Gets an instance of a named class.
     *
     * @param className the name of the class.
     * @return the instance.
     * @throws ServiceException if instantiation fails.
     */
    public Object getInstance(String className)
        throws FactoryException;

    /**
     * Gets an instance of a named class using a specified class loader.
     *
     * <p>Class loaders are supported only if the isLoaderSupported
     * method returns true. Otherwise the loader parameter is ignored.
     *
     * @param className the name of the class.
     * @param loader the class loader.
     * @return the instance.
     * @throws ServiceException if instantiation fails.
     */
    public Object getInstance(String className,
                              ClassLoader loader)
        throws FactoryException;

    /**
     * Gets an instance of a named class.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * @param className the name of the class.
     * @param params an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     * @return the instance.
     * @throws ServiceException if instantiation fails.
     */
    public Object getInstance(String className,
                              Object[] params,
                              String[] signature)
        throws FactoryException;

    /**
     * Gets an instance of a named class using a specified class loader.
     * Parameters for its constructor are given as an array of objects,
     * primitive types must be wrapped with a corresponding class.
     *
     * <p>Class loaders are supported only if the isLoaderSupported
     * method returns true. Otherwise the loader parameter is ignored.
     *
     * @param className the name of the class.
     * @param loader the class loader.
     * @param params an array containing the parameters of the constructor.
     * @param signature an array containing the signature of the constructor.
     * @return the instance.
     * @throws ServiceException if instantiation fails.
     */
    public Object getInstance(String className,
                              ClassLoader loader,
                              Object[] params,
                              String[] signature)
        throws FactoryException;

    /**
     * Tests if specified class loaders are supported for a named class.
     *
     * @param className the name of the class.
     * @return true if class loaders are supported, false otherwise.
     * @throws ServiceException if test fails.
     */
    public boolean isLoaderSupported(String className)
        throws FactoryException;

    /**
     * Gets the signature classes for parameters of a method of a class.
     *
     * @param clazz the class.
     * @param params an array containing the parameters of the method.
     * @param signature an array containing the signature of the method.
     * @return an array of signature classes. Note that in some cases
     * objects in the parameter array can be switched to the context
     * of a different class loader.
     * @throws ClassNotFoundException if any of the classes is not found.
     */
    Class[] getSignature(Class clazz,
            Object params[],
            String signature[])
            throws ClassNotFoundException;
}
