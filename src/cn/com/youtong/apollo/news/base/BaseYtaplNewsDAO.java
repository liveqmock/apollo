package cn.com.youtong.apollo.news.base;

import net.sf.hibernate.*;
import cn.com.youtong.apollo.news.dao.YtaplNewsDAO;
import net.sf.hibernate.expression.*;

/**
 * This is an automatically generated DAO class which should not be edited.
 */
public abstract class BaseYtaplNewsDAO extends cn.com.youtong.apollo.news.dao._RootDAO {

	// query name references


	public static YtaplNewsDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static YtaplNewsDAO getInstance () {
		if (null == instance) instance = new YtaplNewsDAO();
		return instance;
	}

	public Class getReferenceClass () {
		return cn.com.youtong.apollo.news.YtaplNews.class;
	}

    public Order getDefaultOrder () {
		return Order.asc("Title");
    }

	/**
	 * Cast the object as a cn.com.youtong.apollo.news.YtaplNews
	 */
	public cn.com.youtong.apollo.news.YtaplNews cast (Object object) {
		return (cn.com.youtong.apollo.news.YtaplNews) object;
	}

	public cn.com.youtong.apollo.news.YtaplNews get(java.lang.Integer key) throws HibernateException
	{
		return (cn.com.youtong.apollo.news.YtaplNews) get(getReferenceClass(), key);
	}

	public cn.com.youtong.apollo.news.YtaplNews get(java.lang.Integer key, Session s) throws HibernateException
	{
		return (cn.com.youtong.apollo.news.YtaplNews) get(getReferenceClass(), key, s);
	}

	public cn.com.youtong.apollo.news.YtaplNews load(java.lang.Integer key) throws HibernateException
	{
		return (cn.com.youtong.apollo.news.YtaplNews) load(getReferenceClass(), key);
	}

	public cn.com.youtong.apollo.news.YtaplNews load(java.lang.Integer key, Session s) throws HibernateException
	{
		return (cn.com.youtong.apollo.news.YtaplNews) load(getReferenceClass(), key, s);
	}

	public cn.com.youtong.apollo.news.YtaplNews loadInitialize(java.lang.Integer key, Session s) throws HibernateException 
	{ 
		cn.com.youtong.apollo.news.YtaplNews obj = load(key, s); 
		if (!Hibernate.isInitialized(obj)) {
			Hibernate.initialize(obj);
		} 
		return obj; 
	}

/* Generic methods */

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * @throws HibernateException 
	 */
	public java.util.List findAll () throws HibernateException {
		return super.findAll();
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * @throws HibernateException 
	 */
	public java.util.List findAll (Order defaultOrder) throws HibernateException {
		return super.findAll(defaultOrder);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 * @throws HibernateException 
	 */
	public java.util.List findAll (Session s, Order defaultOrder) throws HibernateException {
		return super.findAll(s, defaultOrder);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * @param ytaplNews a transient instance of a persistent class 
	 * @return the class identifier
	 * @throws HibernateException 
	 */
	public java.lang.Integer save(cn.com.youtong.apollo.news.YtaplNews ytaplNews) throws HibernateException
	{
		return (java.lang.Integer) super.save(ytaplNews);
	}

	/**
	 * Persist the given transient instance, first assigning a generated identifier. (Or using the current value
	 * of the identifier property if the assigned generator is used.) 
	 * Use the Session given.
	 * @param ytaplNews a transient instance of a persistent class
	 * @param s the Session
	 * @return the class identifier
	 * @throws HibernateException 
	 */
	public java.lang.Integer save(cn.com.youtong.apollo.news.YtaplNews ytaplNews, Session s) throws HibernateException
	{
		return (java.lang.Integer) save((Object) ytaplNews, s);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default
	 * the instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the
	 * identifier property mapping. 
	 * @param ytaplNews a transient instance containing new or updated state 
	 * @throws HibernateException 
	 */
	public void saveOrUpdate(cn.com.youtong.apollo.news.YtaplNews ytaplNews) throws HibernateException
	{
		saveOrUpdate((Object) ytaplNews);
	}

	/**
	 * Either save() or update() the given instance, depending upon the value of its identifier property. By default the
	 * instance is always saved. This behaviour may be adjusted by specifying an unsaved-value attribute of the identifier
	 * property mapping. 
	 * Use the Session given.
	 * @param ytaplNews a transient instance containing new or updated state.
	 * @param s the Session.
	 * @throws HibernateException 
	 */
	public void saveOrUpdate(cn.com.youtong.apollo.news.YtaplNews ytaplNews, Session s) throws HibernateException
	{
		saveOrUpdate((Object) ytaplNews, s);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param ytaplNews a transient instance containing updated state
	 * @throws HibernateException 
	 */
	public void update(cn.com.youtong.apollo.news.YtaplNews ytaplNews) throws HibernateException 
	{
		update((Object) ytaplNews);
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * Use the Session given.
	 * @param ytaplNews a transient instance containing updated state
	 * @param the Session
	 * @throws HibernateException 
	 */
	public void update(cn.com.youtong.apollo.news.YtaplNews ytaplNews, Session s) throws HibernateException
	{
		update((Object) ytaplNews, s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param id the instance ID to be removed
	 * @throws HibernateException 
	 */
	public void delete(java.lang.Integer id) throws HibernateException
	{
		delete((Object) load(id));
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param id the instance ID to be removed
	 * @param s the Session
	 * @throws HibernateException 
	 */
	public void delete(java.lang.Integer id, Session s) throws HibernateException
	{
		delete((Object) load(id, s), s);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @param ytaplNews the instance to be removed
	 * @throws HibernateException 
	 */
	public void delete(cn.com.youtong.apollo.news.YtaplNews ytaplNews) throws HibernateException
	{
		delete((Object) ytaplNews);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * Use the Session given.
	 * @param ytaplNews the instance to be removed
	 * @param s the Session
	 * @throws HibernateException 
	 */
	public void delete(cn.com.youtong.apollo.news.YtaplNews ytaplNews, Session s) throws HibernateException
	{
		delete((Object) ytaplNews, s);
	}
	
	/**
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * For example 
	 * <ul> 
	 * <li>where a database trigger alters the object state upon insert or update</li>
	 * <li>after executing direct SQL (eg. a mass update) in the same session</li>
	 * <li>after inserting a Blob or Clob</li>
	 * </ul>
	 * @throws HibernateException 
	 */
	public void refresh (cn.com.youtong.apollo.news.YtaplNews ytaplNews, Session s) throws HibernateException
	{
		refresh((Object) ytaplNews, s);
	}


}