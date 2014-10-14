package cn.com.youtong.apollo.news.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.*;
import net.sf.hibernate.expression.*;


public abstract class _BaseRootDAO {

	protected static Map sessionFactoryMap;
	protected static SessionFactory sessionFactory;
	protected static ThreadLocal mappedSessions;
	protected static ThreadLocal sessions;

	/**
	 * Configure the session factory by reading hibernate config file
	 */
	public static void initialize () {
		cn.com.youtong.apollo.news.dao._RootDAO.initialize(
			(String) null);
	}
	
	public static void initialize (String configFileName) {
		if (null == configFileName && null != sessionFactory) return;
//		else if (null != sessionFactoryMap && null != sessionFactoryMap.get(configFileName)) return;
//		else {
//			if (null == configFileName) {
//				configuration.configure();
//				cn.com.youtong.apollo.news.dao._RootDAO.setSessionFactory(
//					configuration.buildSessionFactory());
//			}
//			else {
//				configuration.configure(
//					configFileName);
//				cn.com.youtong.apollo.news.dao._RootDAO.setSessionFactory(
//					configFileName,
//					configuration.buildSessionFactory());
//			}
//		}
		try {
			cn.com.youtong.apollo.news.dao._RootDAO.setSessionFactory(cn.com.youtong.apollo.common.sql.HibernateUtil.getSessionFactory());
		} catch (net.sf.hibernate.HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Set the session factory
	 */
	protected static void setSessionFactory (SessionFactory sessionFactory) {
		setSessionFactory(
			(String) null,
			sessionFactory);
	}

	/**
	 * Set the session factory
	 */
	protected static void setSessionFactory (String configFileName, SessionFactory sf) {
		if (null == configFileName) {
			sessionFactory = sf;
		}
		else {
			if (null == sessionFactoryMap) sessionFactoryMap = new HashMap();
			sessionFactoryMap.put(
				configFileName,
				sessionFactory);
		}
	}

	/**
	 * Return the SessionFactory that is to be used by these DAOs.  Change this
	 * and implement your own strategy if you, for example, want to pull the SessionFactory
	 * from the JNDI tree.
	 */
	protected SessionFactory getSessionFactory() {
		return getSessionFactory(
		getConfigurationFileName());
	}

	protected SessionFactory getSessionFactory(String configFile) {
		if (null == configFile) {
			if (null == sessionFactory)
				throw new RuntimeException("The session factory has not been initialized (or an error occured during initialization)");
			else
				return sessionFactory;
		}
		else {
			if (null == sessionFactoryMap)
				throw new RuntimeException("The session factory for '" + configFile + "' has not been initialized (or an error occured during initialization)");
			else {
				SessionFactory sf = (SessionFactory) sessionFactoryMap.get(configFile);
				if (null == sf)
					throw new RuntimeException("The session factory for '" + configFile + "' has not been initialized (or an error occured during initialization)");
				else
					return sf;
			}
		}
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @return the active Session
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public Session getSession() throws net.sf.hibernate.HibernateException {
		return getSession(getConfigurationFileName(),false);
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @return the active Session
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public Session createNewSession() throws net.sf.hibernate.HibernateException {
		return getSession(getConfigurationFileName(),true);
	}

	/**
	 * Return a new Session object that must be closed when the work has been completed.
	 * @param configFile the config file must match the meta attribute "config-file" in the hibernate mapping file
	 * @return the active Session
	 * @throws net.sf.hibernate.HibernateException 
	 */
	protected Session getSession(String configFile, boolean createNew) throws net.sf.hibernate.HibernateException {
		if (createNew) {
			return getSessionFactory(configFile).openSession();
		}
		else {
			if (null == configFile) {
				if (null == sessions) sessions = new ThreadLocal();
				Session session = (Session)sessions.get();
				if (null == session || !session.isOpen()) {
					session = getSessionFactory(null).openSession();
					sessions.set(session);
				}
				return session;
			}
			else {
				if (null == mappedSessions) mappedSessions = new ThreadLocal();
				Map map = (Map)mappedSessions.get();
				if (null == map) {
					map = new HashMap(1);
					mappedSessions.set(map);
				}
				Session session = (Session)map.get(configFile);
				if (null == session || !session.isOpen()) {
					session = getSessionFactory(configFile).openSession();
					map.put(configFile, session);
				}
				return session;
			}
		}
	}

	/**
	 * Close all sessions for the current thread
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public static void closeCurrentThreadSessions () throws net.sf.hibernate.HibernateException {
		Session session =null;
		if (null != sessions) {
			session = (Session) sessions.get();
			if (null != session && session.isOpen()) {
				session.close();
				sessions.set(null);
			}
		}
		if (null != mappedSessions) {
			Map map = (Map)mappedSessions.get();
			if (null != map) {
				HibernateException thrownException = null;
				for(Iterator it=map.values().iterator();it.hasNext();){
					session = (Session)it.next();
//				for (Session session : map.values()) {
					try {
						if (null != session && session.isOpen()) {
							session.close();
						}
					}
					catch (HibernateException e) {
						thrownException = e;
					}
				}
				map.clear();
				if (null != thrownException) throw thrownException;
			}
		}
	}

	/**
	 * Close the session
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public void closeSession (Session session) throws net.sf.hibernate.HibernateException {
		if (null != session) session.close();
	}

	/**
	 * Begin the transaction related to the session
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public Transaction beginTransaction(Session s) throws net.sf.hibernate.HibernateException {
		return s.beginTransaction();
	}

	/**
	 * Commit the given transaction
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public void commitTransaction(Transaction t) throws net.sf.hibernate.HibernateException {
		t.commit();
	}

	/**
	 * Return the name of the configuration file to be used with this DAO or null if default
	 */
	public String getConfigurationFileName () {
		return null;
	}

	/**
	 * Return the specific Object class that will be used for class-specific
	 * implementation of this DAO.
	 * @return the reference Class
	 */
	protected abstract Class getReferenceClass();

	/**
	 * Used by the base DAO classes but here for your modification
	 * Get object matching the given key and return it.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	protected Object get(Class refClass, Serializable key) throws net.sf.hibernate.HibernateException {
		Session s = null;
		try {
			s = getSession();
			return get(refClass, key, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Get object matching the given key and return it.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	protected Object get(Class refClass, Serializable key, Session s) throws net.sf.hibernate.HibernateException {
		return s.get(refClass, key);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	protected Object load(Class refClass, Serializable key) throws net.sf.hibernate.HibernateException {
		Session s = null;
		try {
			s = getSession();
			return load(refClass, key, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Load object matching the given key and return it.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	protected Object load(Class refClass, Serializable key, Session s) throws net.sf.hibernate.HibernateException {
		return s.load(refClass, key);
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public java.util.List findAll () throws net.sf.hibernate.HibernateException {
		Session s = null;
		try {
			s = getSession();
    		return findAll(s);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 * @throws HibernateException 
	 */
	public java.util.List findAll (Session s) throws HibernateException {
   		return findAll(s, getDefaultOrder());
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * @throws net.sf.hibernate.HibernateException 
	 */
	public java.util.List findAll (Order defaultOrder) throws net.sf.hibernate.HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findAll(s, defaultOrder);
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Return all objects related to the implementation of this DAO with no filter.
	 * Use the session given.
	 * @param s the Session
	 * @throws HibernateException 
	 */
	public java.util.List findAll (Session s, Order defaultOrder) throws HibernateException {
		Criteria crit = s.createCriteria(getReferenceClass());
		if (null != defaultOrder) crit.addOrder(defaultOrder);
		return crit.list();
	}

	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 * @throws HibernateException 
	 */
	protected Criteria findFiltered (String propName, Object filter) throws HibernateException {
		return findFiltered(propName, filter, getDefaultOrder());
	}

	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 * @param orderProperty the name of the property used for ordering
	 * @throws HibernateException 
	 */
	protected Criteria findFiltered (String propName, Object filter, Order order) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return findFiltered(s, propName, filter, order);
		}
		finally {
			closeSession(s);
		}
	}
	
	/**
	 * Return all objects related to the implementation of this DAO with a filter.
	 * Use the session given.
	 * @param s the Session
	 * @param propName the name of the property to use for filtering
	 * @param filter the value of the filter
	 * @param orderProperty the name of the property used for ordering
	 */
	protected Criteria findFiltered (Session s, String propName, Object filter, Order order) {
		Criteria crit = s.createCriteria(getReferenceClass());
//		crit.add(Expression.eq(propName, filter));
		if (null != order) crit.addOrder(order);
		return crit;
	}
	
	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the session given.
	 * @param name the name of a query defined externally 
	 * @param s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * @param name the name of a query defined externally 
	 * @param param the first parameter to set
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Serializable param) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, param, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the session given.
	 * @param name the name of a query defined externally 
	 * @param param the first parameter to set
	 * @param s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Serializable param, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		q.setParameter(0, param);
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Serializable[] params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter array
	 * @s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Serializable[] params, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Map params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getNamedQuery(name, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param name the name of a query defined externally 
	 * @param params the parameter Map
	 * @s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getNamedQuery(String name, Map params, Session s) throws HibernateException {
		Query q = s.getNamedQuery(name);
		if (null != params) {
			for (Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				q.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
		return q;
	}

	/**
	 * Execute a query. 
	 * @param queryStr a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 * @throws HibernateException 
	 */
	public Query getQuery(String queryStr) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @throws HibernateException 
	 * @s the Session to use
	 */
	public Query getQuery(String queryStr, Session s) throws HibernateException {
		return s.createQuery(queryStr);
	}

	/**
	 * Execute a query. 
	 * @param query a query expressed in Hibernate's query language
	 * @param queryStr the name of a query defined externally 
	 * @param param the first parameter to set
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Serializable param) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, param, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param param the first parameter to set
	 * @s the Session to use
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Serializable param, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		q.setParameter(0, param);
		return q;
	}

	/**
	 * Execute a query. 
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter array
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Serializable[] params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Execute a query but use the session given instead of creating a new one.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter array
	 * @s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Serializable[] params, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i, params[i]);
			}
		}
		return q;
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter Map
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Map params) throws HibernateException {
		Session s = null;
		try {
			s = getSession();
			return getQuery(queryStr, params, s);
		} finally {
			closeSession(s);
		}
	}

	/**
	 * Obtain an instance of Query for a named query string defined in the mapping file.
	 * Use the parameters given and the Session given.
	 * @param queryStr a query expressed in Hibernate's query language
	 * @param params the parameter Map
	 * @s the Session
	 * @return Query
	 * @throws HibernateException 
	 */
	protected Query getQuery(String queryStr, Map params, Session s) throws HibernateException {
		Query q = getQuery(queryStr, s);
		if (null != params) {
			for (Iterator i=params.entrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				q.setParameter((String) entry.getKey(), entry.getValue());
			}
		}
		return q;
	}

	protected Order getDefaultOrder () {
		return null;
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 * @throws HibernateException 
	 */
	protected Serializable save(final Object obj) throws HibernateException {
		return (Serializable) run (
			new TransactionRunnable () {
				public Object run (Session s) throws HibernateException {
					return save(obj, s);
				}
			});
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 * @throws HibernateException 
	 */
	protected Serializable save(Object obj, Session s) throws HibernateException {
		return s.save(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 * @throws HibernateException 
	 */
	protected void saveOrUpdate(final Object obj) throws HibernateException {
		run (
			new TransactionRunnable () {
				public Object run (Session s) throws HibernateException {
					saveOrUpdate(obj, s);
					return null;
				}
			});
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Either save() or update() the given instance, depending upon the value of its
	 * identifier property.
	 * @throws HibernateException 
	 */
	protected void saveOrUpdate(Object obj, Session s) throws HibernateException {
		s.saveOrUpdate(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 * @throws HibernateException 
	 */
	protected void update(final Object obj) throws HibernateException {
		run (
			new TransactionRunnable () {
				public Object run (Session s) throws HibernateException {
					update(obj, s);
					return null;
				}
			});
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 * @param s the Session
	 * @throws HibernateException 
	 */
	protected void update(Object obj, Session s) throws HibernateException {
		s.update(obj);
	}

	/**
	 * Delete all objects returned by the query
	 * @throws HibernateException 
	 */
	protected int delete (final Query query) throws HibernateException {
		Integer rtn = (Integer) run (
			new TransactionRunnable () {
				public Object run (Session s) throws HibernateException {
					return new Integer(delete((Query) query, s));
				}
			});
		return rtn.intValue();
	}

	/**
	 * Delete all objects returned by the query
	 * @throws HibernateException 
	 */
	protected int delete (Query query, Session s) throws HibernateException {
		List list = query.list();
		for (Iterator i=list.iterator(); i.hasNext(); ) {
			delete(i.next(), s);
		}
		return list.size();
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @throws HibernateException 
	 */
	protected void delete(final Object obj) throws HibernateException {
		run (
			new TransactionRunnable () {
				public Object run (Session s) throws HibernateException {
					delete(obj, s);
					return null;
				}
			});
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 * @throws HibernateException 
	 */
	protected void delete(Object obj, Session s) throws HibernateException {
		s.delete(obj);
	}

	/**
	 * Used by the base DAO classes but here for your modification
	 * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
	 * long-running sessions that span many business tasks. This method is, however, useful in certain special circumstances.
	 * @throws HibernateException 
	 */
	protected void refresh(Object obj, Session s) throws HibernateException {
		s.refresh(obj);
	}

	protected void throwException (Throwable t) throws HibernateException {
		if (t instanceof HibernateException) throw (HibernateException) t;
		else if (t instanceof RuntimeException) throw (RuntimeException) t;
		else throw new HibernateException(t);
	}

	/**
	 * Execute the given transaction runnable.
	 * @throws HibernateException 
	 */
	protected Object run (TransactionRunnable transactionRunnable) throws HibernateException {
		Transaction t = null;
		Session s = null;
		try {
			s = getSession();
			t = beginTransaction(s);
			Object obj = transactionRunnable.run(s);
			commitTransaction(t);
			return obj;
		}
		catch (Throwable throwable) {
			if (null != t) {
				try {
					t.rollback();
				}
				catch (HibernateException e) {handleError(e);}
			}
			if (transactionRunnable instanceof TransactionFailHandler) {
				try {
					((TransactionFailHandler) transactionRunnable).onFail(s);
				}
				catch (Throwable e) {handleError(e);}
			}
            throwException(throwable);
            return null;
		}
		finally {
			closeSession(s);
		}
	}

	/**
	 * Execute the given transaction runnable.
	 */
	protected TransactionPointer runAsnyc (TransactionRunnable transactionRunnable) {
		final TransactionPointer transactionPointer = new TransactionPointer(transactionRunnable);
		ThreadRunner threadRunner = new ThreadRunner(transactionPointer);
		threadRunner.start();
		return transactionPointer;
	}

	/**
	 * This class can be used to encapsulate logic used for a single transaction.
	 */
	public abstract class TransactionRunnable {
		public abstract Object run (Session s) throws Exception;
	}

	/**
	 * This class can be used to handle any error that has occured during a transaction
	 */
	public interface TransactionFailHandler {
		public void onFail (Session s);
	}

	/**
	 * This class can be used to handle failed transactions
	 */
	public abstract class TransactionRunnableFailHandler extends TransactionRunnable implements TransactionFailHandler {
	}

	public class TransactionPointer {
		private TransactionRunnable transactionRunnable;
		private Throwable thrownException;
		private Object returnValue;
		private boolean hasCompleted = false;
		
		public TransactionPointer (TransactionRunnable transactionRunnable) {
			this.transactionRunnable = transactionRunnable;
		}

		public boolean hasCompleted() {
			return hasCompleted;
		}
		public void complete() {
			this.hasCompleted = true;
		}
		
		public Object getReturnValue() {
			return returnValue;
		}
		public void setReturnValue(Object returnValue) {
			this.returnValue = returnValue;
		}

		public Throwable getThrownException() {
			return thrownException;
		}
		public void setThrownException(Throwable thrownException) {
			this.thrownException = thrownException;
		}
		public TransactionRunnable getTransactionRunnable() {
			return transactionRunnable;
		}
		public void setTransactionRunnable(TransactionRunnable transactionRunnable) {
			this.transactionRunnable = transactionRunnable;
		}

		/**
		 * Wait until the transaction completes and return the value returned from the run method of the TransactionRunnable.
		 * If the transaction throws an Exception, throw that Exception.
		 * @param timeout the timeout in milliseconds (or 0 for no timeout)
		 * @return the return value from the TransactionRunnable
		 * @throws TimeLimitExcededException if the timeout has been reached before transaction completion
		 * @throws Throwable the thrown Throwable
		 */
		public Object waitUntilFinish (long timeout) throws Throwable {
			long killTime = -1;
			if (timeout > 0) killTime = System.currentTimeMillis() + timeout;
			do {
				try {
					Thread.sleep(50);
				}
				catch (InterruptedException e) {}
			}
			while (!hasCompleted && ((killTime > 0 && System.currentTimeMillis() < killTime) || killTime <= 0));
			if (!hasCompleted) throw new javax.naming.TimeLimitExceededException();
			if (null != thrownException) throw thrownException;
			else return returnValue;
		}
	}

	private class ThreadRunner extends Thread {
		private TransactionPointer transactionPointer;
		
		public ThreadRunner (TransactionPointer transactionPointer) {
			this.transactionPointer = transactionPointer;
		}
		
		public void run () {
			Transaction t = null;
			Session s = null;
			try {
				s = getSession();
				t = beginTransaction(s);
				Object obj = transactionPointer.getTransactionRunnable().run(s);
				t.commit();
				transactionPointer.setReturnValue(obj);
			}
			catch (Throwable throwable) {
				if (null != t) {
					try {
						t.rollback();
					}
					catch (HibernateException e) {handleError(e);}
				}
				if (transactionPointer.getTransactionRunnable() instanceof TransactionFailHandler) {
					try {
						((TransactionFailHandler) transactionPointer.getTransactionRunnable()).onFail(s);
					}
					catch (Throwable e) {handleError(e);}
				}
	            transactionPointer.setThrownException(throwable);
			}
			finally {
				transactionPointer.complete();
				try {
					closeSession(s);
				}
				catch (HibernateException e) {
					transactionPointer.setThrownException(e);
				}
			}
		}
	}

	protected void handleError (Throwable t) {
	}


}