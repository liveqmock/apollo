package cn.com.youtong.apollo.services;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;

/**
 * This is a singleton utility class that acts as a Services broker.
 *
 */
public final class ApolloService
{
	/** The single instance of this class. */
	private static ServiceManager instance ;

	/**
	 * This constructor is protected to force clients to use
	 * getInstance() to access this class.
	 */
	protected ApolloService()
	{
		super();
	}

	/**
	 * Get the <code>Object</code> associated with the given key.  For
	 * instance, If the <code>ServiceManager</code> had a
	 * <code>LoggerComponent</code> stored and referenced by key,
	 * the following could be used:
	 * <pre>
	 * try
	 * {
	 *     LoggerComponent log;
	 *     myComponent = (LoggerComponent) manager.lookup( LoggerComponent.ROLE );
	 * }
	 * catch (...)
	 * {
	 *     ...
	 * }
	 * </pre>
	 *
	 * @param key The lookup key of the <code>Object</code> to retrieve.
	 * @return an <code>Object</code> value
	 * @throws ServiceException if an error occurs
	 */
	public static Object lookup( String key )
		throws ServiceException
	{
		return instance.lookup(key);
	}

	/**
	 * Check to see if a <code>Object</code> exists for a key.
	 *
	 * @param key a string identifying the key to check.
	 * @return True if the object exists, False if it does not.
	 */
	public static boolean hasService( String key )
	{
		return instance.hasService(key);
	}

	/**
	 * Return the <code>Object</code> when you are finished with it.  This
	 * allows the <code>ServiceManager</code> to handle the End-Of-Life Lifecycle
	 * events associated with the <code>Object</code>.  Please note, that no
	 * Exception should be thrown at this point.  This is to allow easy use of the
	 * ServiceManager system without having to trap Exceptions on a release.
	 *
	 * @param object The <code>Object</code> we are releasing.
	 */
	public static void release( Object object )
	{
		instance.release(object);
	}


	/**
	 * The method through which to change the default manager.
	 * @param manager a new service manager.
	 */
	public static synchronized void setManager(ServiceManager manager)
	{
		instance = manager;
	}
}
