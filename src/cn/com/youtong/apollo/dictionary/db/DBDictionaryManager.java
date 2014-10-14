/*
 * Created on 2003-10-23
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package cn.com.youtong.apollo.dictionary.db;

import cn.com.youtong.apollo.dictionary.*;
import cn.com.youtong.apollo.dictionary.Dictionary;
import cn.com.youtong.apollo.dictionary.db.form.*;
import net.sf.hibernate.*;
import cn.com.youtong.apollo.common.sql.*;
import java.util.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.dictionary.xml.*;
import org.exolab.castor.xml.*;
import java.io.*;
import java.sql.*;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DBDictionaryManager
	implements DictionaryManager
{

	/**
	 * ����dictionary���󣬲���dictionary�����ʱ�����ȴӸ�Map����ȡ��
	 * ��������ڣ���ô�����ݿ��ж�ȡ��
	 * ��dictionary�������ɾ������ʱ����dictionaryMap��ɾ����Ӧ�������
	 * �⼴�������������ڵĴ����ֵ������µġ�
	 * key/value=dictionary.id()/dictionary��
	 */
	private Map dictionaryMap = new Hashtable();

	/**
	 * �����ֵ���Ŀ���ݿ������Ŀkey���ֶ�����
	 */
	private final String ENTRY_KEY = "entryKey";

	/**
	 * �����ֵ���Ŀ���ݿ������Ŀvalue���ֶ�����
	 */
	private final String ENTRY_VALUE = "entryValue";

	/**
	 * log object
	 */
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * ���´����ֵ䣬��������ֵ䲻�����򴴽��µĴ����ֵ�.
	 * ������Ϊxml������dictionary.xsd
	 * @param in input xml stream
	 * @throws DictionaryException throws exception if dictionary already existed,
	 * or internal errors.
	 * @throws IOException read stream failed.
	 */
	public void updateDictionary(InputStream in)
		throws DictionaryException, IOException
	{
		//����XML
		cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary = parseXML(in);
		//����
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			save(dictionary, session);
			tx.commit();
		}
		catch(HibernateException ex)
		{
			String message = "��������ֵ�ʧ��";
			log.error(message, ex);
			try
			{
				tx.rollback();
			}
			catch(HibernateException ex1)
			{
			}
			throw new DictionaryException(message, ex);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * �õ����е��ֵ���Ŀ
	 * @return �����ֵ���ĿIterator
	 * @throws DictionaryException ҵ���쳣
	 */
	public Iterator getAllDictionaries()
		throws DictionaryException
	{
		// �ȶ�ȡû����cache�д��ڵ�task�����һ���
		loadAllDictionaryAndCache();
		return dictionaryMap.values().iterator();
	}

	private void loadAllDictionaryAndCache()
		throws DictionaryException
	{

		/* �Ѿ���cache�е�dictionary.id,ƴ��Ϊ'dictionaryID1', 'dictionaryID2'�������ַ���
		 * �����ڲ�ѯ����ʱ��ɲ�ѯ�ַ���
		 */
		Set dictionaryIDSet = dictionaryMap.keySet();
		StringBuffer buff = new StringBuffer();
		for(Iterator dictionaryIDIter = dictionaryIDSet.iterator(); dictionaryIDIter.hasNext(); )
		{
			String dictionaryID = (String) dictionaryIDIter.next();
			buff.append("'").append(dictionaryID).append("'").append(",");
		}
		// ���buff�������ַ�����ôɾ�����һ��","�ַ�
		if(buff.length() > 1)
		{
			buff.deleteCharAt(buff.length() - 1);
			if(log.isDebugEnabled())
			{
				log.debug("Dictionarys load From Cache not Database: " + buff);
			}
		}

		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			String querySQL = "select dict from DictionaryForm dict";

			// ��������Ѿ����ˣ���ô���ش����ݿ��ȡ
			if(buff.length() > 0)
			{
				querySQL = querySQL + " where dict.dictid not in ( " + buff + " )";
			}

			Query query = session.createQuery(querySQL);
			Iterator iter = query.iterate();

			while(iter.hasNext())
			{
				DictionaryForm dictionary = (DictionaryForm) iter.next();
				loadEntries(dictionary, session);

				putCache(new DBDictionary(dictionary));
			}
		}
		catch(HibernateException e)
		{
			log.error("��ȡ���������ֵ���Ŀ����", e);
			throw new DictionaryException("��ȡ���������ֵ���Ŀ����", e);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e)
				{
					log.error("Close session happen exception", e);
				}
			}
		}
	}

	/**
	 * ��ӻ����¼key=dictionary.id(),value=dictionary
	 * @param dictionary �����ֵ����
	 */
	private void putCache(Dictionary dictionary)
	{
		String dictionaryID = dictionary.id();

		if(log.isDebugEnabled())
		{
			log.debug("Put Dictionary into Cache: dictionary.id() = " + dictionaryID);
		}
		dictionaryMap.put(dictionaryID, dictionary);
	}

	/**
	 * ���ش����ֵ����Ŀ��Ϣ
	 * @param dictionary �����ֵ����
	 * @param session Session����
	 * @throws HibernateException �ײ��쳣
	 */
	private void loadEntries(DictionaryForm dictionary, Session session)
		throws HibernateException
	{
		Connection conn = session.connection();
		PreparedStatement pst = null;
		String sql = "SELECT " + ENTRY_KEY + ", " + ENTRY_VALUE + " FROM " + NameGenerator.generateDictionaryTableName(dictionary.getDictid());
		try
		{
			Set entries = new HashSet();
			pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while(rs.next())
			{
				entries.add(new DictionaryEntryForm(rs.getString(1), rs.getString(2)));
			}
			dictionary.setDictionaryEntries(entries);
		}
		catch(SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
			if(pst != null)
			{
				try
				{
					pst.close();
				}
				catch(SQLException ex1)
				{
				}
			}
		}
	}

	/**
	 * ����id���Ҵ����ֵ�
	 * @param id �����ֵ�id
	 * @param session session����
	 * @return ָ���Ĵ����ֵ�
	 * @throws HibernateException ����ʧ���׳�
	 */
	private DictionaryForm findByID(String id, Session session)
		throws HibernateException
	{
		List result = session.find("from DictionaryForm dict where dict.dictid = ?", id, Hibernate.STRING);
		DictionaryForm dictionary = (DictionaryForm) result.get(0);
		loadEntries(dictionary, session);
		return dictionary;
	}

	/**
	 * ���������ֵ�
	 * @param id  �ֵ���Ŀ��ID��
	 * @return �����ֵ�Dictionary�ӿ�
	 * @throws DictionaryException  throws exception if failed
	 */
	public Dictionary getDictionaryByID(String id)
		throws DictionaryException
	{

		if(dictionaryMap.containsKey(id))
		{
			return(Dictionary) dictionaryMap.get(id);
		}
		else
		{
			Session session = null;
			try
			{
				session = HibernateUtil.getSessionFactory().openSession();
				if(!isDictionaryDefined(id, session))
				{
					throw new DictionaryException("ָ���Ĵ����ֵ䲻����");
				}
				DictionaryForm dictionary = findByID(id, session);
				Dictionary dic = new DBDictionary(dictionary);
				putCache(dic);
				return dic;
			}
			catch(HibernateException e)
			{
				log.error("��ȡ�����ֵ����", e);
				throw new DictionaryException("��ȡ�����ֵ����", e);
			}
			finally
			{
				if(session != null)
				{
					try
					{
						session.close();
					}
					catch(HibernateException e)
					{
						log.error("Close session happen exception", e);
					}
				}
			}
		}
	}

	/**
	 * ����ID��ɾ���ֵ���Ŀ
	 * @param id  �ֵ���Ŀ��ID��
	 * @throws DictionaryException  throws exception if failed
	 */
	public void deleteDictionary(String id)
		throws DictionaryException
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(!isDictionaryDefined(id, session))
			{
				throw new DictionaryException("ָ���Ĵ����ֵ䲻����");
			}
			delete(id, session);
			transaction.commit();
		}
		catch(HibernateException e)
		{
			log.error("ɾ�������ֵ����dictionaryID=" + id + ")", e);
			if(transaction != null)
			{
				try
				{
					transaction.rollback();
				}
				catch(HibernateException e1)
				{
				}
			}
			throw new DictionaryException("ɾ�������ֵ����(dictionaryID=" + id + ")", e);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e)
				{
				}
			}
		}
	}

	/**
	 * ɾ�������ֵ�
	 * @param id �����ֵ�ids
	 * @param session session����
	 * @throws HibernateException
	 */
	private void delete(String id, Session session)
		throws HibernateException
	{
		DictionaryForm dictionary = findByID(id, session);
		//ɾ�������ֵ�
		session.delete(dictionary);
		//ɾ�������ֵ���Ŀ
		deleteEntries(id, session);
		//ɾ���������ֵ���Ŀ
		removeFromCache(id);
	}

	/**
	 * ���dictionary������key=dictionaryID�ļ�¼
	 * @param dictionaryID �����ֵ�ID��
	 */
	private void removeFromCache(String dictionaryID)
	{
		if(log.isDebugEnabled())
		{
			log.debug("Remove Dictionary from Cache: dictionary.id() = " + dictionaryID);
		}
		dictionaryMap.remove(dictionaryID);
	}

	/**
	 * ɾ�������ֵ����Ŀ
	 * @param id �����ֵ�id
	 * @param session Session����
	 * @throws HibernateException ɾ��ʧ���׳�
	 */
	private void deleteEntries(String id, Session session)
		throws HibernateException
	{
		Connection conn = session.connection();
		PreparedStatement pst = null;
		//ɾ����Ŵ����ֵ���Ŀ�����ݱ�
		String sql = "DROP TABLE " + NameGenerator.generateDictionaryTableName(id);
		try
		{
			pst = conn.prepareStatement(sql);
			pst.execute();
		}
		catch(SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
			if(pst != null)
			{
				try
				{
					pst.close();
				}
				catch(SQLException ex1)
				{
				}
			}
		}
	}

	/**
	 * ��XML�ļ�������XML����
	 * @param xmlInputStream XML��
	 * @return XML����Dictionary
	 * @throws DictionaryException
	 */
	private cn.com.youtong.apollo.dictionary.xml.Dictionary parseXML(InputStream xmlInputStream)
		throws DictionaryException
	{

		/** ����XML�ļ� */
		cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary = null;
		try
		{
			//��Castor��XML�ļ�ӳ�䵽������
			dictionary = cn.com.youtong.apollo.dictionary.xml.Dictionary.unmarshal(new InputStreamReader(xmlInputStream, "gb2312"));
		}
		catch(Exception ex)
		{
			throw new DictionaryException("��Ч��XML�ļ�", ex);
		}
		return dictionary;
	}

	/**
	 * ��XML����ת��ΪHibernate����
	 * @param dictionary XML����Dictionary
	 * @return Hibernate����DictionaryForm
	 * @throws DictionaryException
	 */
	private DictionaryForm getDictionaryForm(cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary)
	{
		DictionaryForm dictionaryForm = new DictionaryForm();
		dictionaryForm.setDictid(dictionary.getID());
		dictionaryForm.setName(dictionary.getName());
		dictionaryForm.setMemo(dictionary.getMemo());
		dictionaryForm.setLevelPosition(dictionary.getLevelPosition());
		dictionaryForm.setKeyLength(dictionary.getKeyLength());
		dictionaryForm.setFlag(dictionary.getFlag());
		dictionaryForm.setDictionaryEntries(getDictionaryEntryForms(dictionary));
		return dictionaryForm;
	}

	/**
	 * ��XML�н�������Dictionary����õ�����DictionaryEntryForm�ļ���
	 * @param dictionary XML ����
	 * @return DictionaryEntryForm����
	 * @throws DictionaryException
	 */
	private Set getDictionaryEntryForms(cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary)
	{
		Set result = new HashSet();
		for(int i = 0; i < dictionary.getItemCount(); i++)
		{
			Item item = dictionary.getItem(i);
			DictionaryEntryForm dictionaryEntryForm = new DictionaryEntryForm();
			//���û�����Ϣ
			dictionaryEntryForm.setEntryKey(item.getKey());
			dictionaryEntryForm.setEntryValue(item.getValue());
			//���浽����
			result.add(dictionaryEntryForm);
		}
		return result;
	}

	/**
	 * ��Hibernate����DictionaryForm���������ݿ�
	 * �����ɹ��򷵻ء�Dictionary��ID�ţ�������ʧ�����׳��쳣
	 * @param dictionaryForm Hibernate������
	 * @return �����Ĵ����ֵ��ID��dictionaryID
	 * @throws TaskException ����ʧ�����׳��쳣
	 */
	/**
	 * �������ֵ�������ݿ�
	 * @param dictionary �����ֵ����
	 * @param session Session����
	 * @throws HibernateException
	 */
	private void save(cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary, Session session)
		throws HibernateException
	{
		if(isDictionaryDefined(dictionary.getID(), session))
		{
			delete(dictionary.getID(), session);
		}
		//����Dictionary
		DictionaryForm form = getDictionaryForm(dictionary);
		form.setDateCreated(new java.util.Date());
		form.setDateModified(new java.util.Date());
		session.save(form);
		//����DictionaryEntry
		saveEntries(form, session);
	}

	/**
	 * ��������ֵ�ʵ�����
	 * @param dictionaryForm �����ֵ����
	 * @param session Session����
	 * @throws HibernateException �ײ��쳣
	 */
	private void saveEntries(DictionaryForm dictionaryForm, Session session)
		throws HibernateException
	{
		String tableName = NameGenerator.generateDictionaryTableName(dictionaryForm.getDictid());
		createTable(tableName, session);
		insertEntries(tableName, dictionaryForm.getDictionaryEntries(), session);
	}

	/**
	 * ������Ŵ����ֵ���Ŀ�����ݿ��
	 * @param tableName ���ݿ����
	 * @param session Session����
	 * @throws HibernateException �ײ��쳣
	 */
	private void createTable(String tableName, Session session)
		throws HibernateException
	{
		Connection conn = session.connection();
		PreparedStatement pst = null;
		String createTableSql = "CREATE TABLE " + tableName + " (" + ENTRY_KEY + " varchar(100)  NOT NULL, " + ENTRY_VALUE + " varchar(100) )";
		try
		{
			pst = conn.prepareStatement(createTableSql);
			pst.execute();
		}
		catch(SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
                  SQLUtil.close(pst);
		}
	}

	/**
	 * �������ֵ���Ŀ���뵽���ݿ����
	 * @param tableName ���ݿ����
	 * @param entries �����ֵ���Ŀ����
	 * @param session Session����
	 * @throws HibernateException �ײ��쳣
	 */
	private void insertEntries(String tableName, Set entries, Session session)
		throws HibernateException
	{
		String insertSql = "INSERT INTO " + tableName + " VALUES (?,?) ";
		Connection conn = session.connection();
		PreparedStatement pst = null;
		try
		{
			Iterator itr = entries.iterator();
                        pst = conn.prepareStatement(insertSql);
			while(itr.hasNext())
			{
				DictionaryEntryForm entry = (DictionaryEntryForm) itr.next();
				pst.setString(1, entry.getEntryKey());
				pst.setString(2, entry.getEntryValue());
                                pst.addBatch();
			}
                        pst.executeBatch();
		}
		catch(SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
                  SQLUtil.close(pst);
		}
	}

	/**
	 * �жϴ����ֵ��Ƿ����
	 * @param id �����ֵ�id
	 * @param session Session����
	 * @return �����򷵻�true,���򷵻�false
	 * @throws HibernateException �ײ��쳣
	 */
	private boolean isDictionaryDefined(String id, Session session)
		throws HibernateException
	{
		return((Integer) session.iterate("SELECT COUNT(*) from DictionaryForm as dictionary WHERE dictionary.dictid = ?", id, Hibernate.STRING).next()).intValue() > 0;
	}

	/**
	 * Output specfic dictionary
	 * @param id dictionary id
	 * @param out output stream
	 * @throws DictionaryException
	 */
	public void outPutDictionary(String id, OutputStream out)
		throws DictionaryException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(!isDictionaryDefined(id, session))
			{
				throw new DictionaryException("ָ���Ĵ����ֵ䲻����");
			}
			log.info("��ʼ���������ֵ�dictionaryID=" + id + "");
			DictionaryForm form = findByID(id, session);
			getCatorDictionary(form, session).marshal(new OutputStreamWriter(out, "gb2312"));
		}
		catch(Exception e)
		{
			String message = "���������ֵ�dictionaryID=" + id + "ʧ��";
			log.error(message, e);
			throw new DictionaryException(message, e);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e)
				{
				}
			}
		}
	}

	/**
	 * �õ������ֵ��castor����
	 * @param dictionary �����ֵ�
	 * @param session Session����
	 * @return �����ֵ��castor����
	 * @throws HibernateException
	 */
	private cn.com.youtong.apollo.dictionary.xml.Dictionary getCatorDictionary(DictionaryForm dictionary, Session session)
		throws HibernateException
	{
		cn.com.youtong.apollo.dictionary.xml.Dictionary result = new cn.com.youtong.apollo.dictionary.xml.Dictionary();
		//��������
		result.setDateModified(dictionary.getDateModified());
		result.setFlag(dictionary.getFlag());
		result.setID(dictionary.getDictid());
		result.setKeyLength(dictionary.getKeyLength());
		result.setLevelPosition(dictionary.getLevelPosition());
		result.setMemo(dictionary.getMemo());
		result.setName(dictionary.getName());
		//������Ŀ
		for(Iterator itr = dictionary.getDictionaryEntries().iterator(); itr.hasNext(); )
		{
			Item item = new Item();
			DictionaryEntryForm form = (DictionaryEntryForm) itr.next();
			item.setKey(form.getEntryKey());
			item.setValue(form.getEntryValue());
			result.addItem(item);
		}

		return result;
	}
}
