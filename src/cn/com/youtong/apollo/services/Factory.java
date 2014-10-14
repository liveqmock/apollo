package cn.com.youtong.apollo.services;

import org.apache.fulcrum.factory.*;
import org.apache.commons.logging.*;

public final class Factory
{
	private static FactoryService factoryService;
	static
	{
		try
		{
			factoryService = (FactoryService) ApolloService.lookup(FactoryService.class.getName());
		}
		catch(Exception e)
		{
			Log log = LogFactory.getLog(Config.class.getName());
			log.fatal("∂¡»°≈‰÷√ ß∞‹.", e);
		}
	}

	/**
	 * Gets an instance of a class.
	 *
	 * @param clazz the name of the class.
	 * @return the instance.
	 * @throws ServiceException if instantiation fails.
	 */
	public static Object getInstance(Class clazz) throws FactoryException
	{
		return factoryService.getInstance(clazz);
	}

	/**
	 * Gets an instance of a named class.
	 *
	 * @param className the name of the class.
	 * @return the instance.
	 * @throws ServiceException if instantiation fails.
	 */
	public static Object getInstance(String className)
		throws FactoryException
	{
		return factoryService.getInstance(className);
	}

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
	public static Object getInstance(String className, ClassLoader loader)
		throws FactoryException
	{
		return factoryService.getInstance(className,loader);
	}

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
	public static Object getInstance(String className, Object[] params, String[] signature)
		throws FactoryException
	{
		return factoryService.getInstance(className,params,signature);
	}

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
	public static Object getInstance(String className, ClassLoader loader, Object[] params, String[] signature)
		throws FactoryException
	{
		return factoryService.getInstance(className,loader,params,signature);
	}

	/**
	 * Tests if specified class loaders are supported for a named class.
	 *
	 * @param className the name of the class.
	 * @return true if class loaders are supported, false otherwise.
	 * @throws ServiceException if test fails.
	 */
	public static boolean isLoaderSupported(String className)
		throws FactoryException
	{
		return factoryService.isLoaderSupported(className);
	}

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
	public static Class[] getSignature(Class clazz, Object params[], String signature[])
		throws ClassNotFoundException
	{
		return factoryService.getSignature(clazz,params,signature);
	}
}