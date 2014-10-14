package cn.com.youtong.apollo.services;

import org.apache.fulcrum.factory.Factory;
import org.apache.fulcrum.factory.FactoryException;

public class DefaultFactory implements Factory
{
    public DefaultFactory()
    {
    }
	/**
	 * Initializes the factory. This method is called by
	 * the Factory Service before the factory is used.
	 *
	 * @param className the name of the production class
	 * @throws FactoryException if initialization fails.
	 */
	public void init(String className)
		throws FactoryException
	{

	}

	/**
	 * Gets an instance of a class.
	 *
	 * @return the instance.
	 * @throws FactoryException if instantiation fails.
	 */
	public Object getInstance()
		throws FactoryException
	{
		return this;
	}

	/**
	 * Gets an instance of a class using a specified class loader.
	 *
	 * <p>Class loaders are supported only if the isLoaderSupported
	 * method returns true. Otherwise the loader parameter is ignored.
	 *
	 * @param loader the class loader.
	 * @return the instance.
	 * @throws FactoryException if instantiation fails.
	 */
	public Object getInstance(ClassLoader loader)
		throws FactoryException
	{
		return this;
	}

	/**
	 * Gets an instance of a named class.
	 * Parameters for its constructor are given as an array of objects,
	 * primitive types must be wrapped with a corresponding class.
	 *
	 * @param params an array containing the parameters of the constructor.
	 * @param signature an array containing the signature of the constructor.
	 * @return the instance.
	 * @throws FactoryException if instantiation fails.
	 */
	public Object getInstance(Object[] params,
							  String[] signature)
		throws FactoryException
	{
		return this;
	}

	/**
	 * Gets an instance of a named class using a specified class loader.
	 * Parameters for its constructor are given as an array of objects,
	 * primitive types must be wrapped with a corresponding class.
	 *
	 * <p>Class loaders are supported only if the isLoaderSupported
	 * method returns true. Otherwise the loader parameter is ignored.
	 *
	 * @param loader the class loader.
	 * @param params an array containing the parameters of the constructor.
	 * @param signature an array containing the signature of the constructor.
	 * @return the instance.
	 * @throws FactoryException if instantiation fails.
	 */
	public Object getInstance(ClassLoader loader,
							  Object[] params,
							  String[] signature)
		throws FactoryException
	{
		return this;
	}

	/**
	 * Tests if this object factory supports specified class loaders.
	 *
	 * @return true if class loaders are supported, false otherwise.
	 */
	public boolean isLoaderSupported()
	{
		return false;
	}

}