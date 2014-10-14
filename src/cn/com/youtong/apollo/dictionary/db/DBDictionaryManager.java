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
	 * 缓存dictionary对象，查找dictionary对象的时候首先从改Map里面取。
	 * 如果不存在，那么在数据库中读取。
	 * 对dictionary对象进行删除操作时，从dictionaryMap中删除相应缓存对象。
	 * 意即保持里面所存在的代码字典是最新的。
	 * key/value=dictionary.id()/dictionary。
	 */
	private Map dictionaryMap = new Hashtable();

	/**
	 * 代码字典条目数据库表中条目key的字段名称
	 */
	private final String ENTRY_KEY = "entryKey";

	/**
	 * 代码字典条目数据库表中条目value的字段名称
	 */
	private final String ENTRY_VALUE = "entryValue";

	/**
	 * log object
	 */
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 更新代码字典，如果代码字典不存在则创建新的代码字典.
	 * 输入流为xml流，见dictionary.xsd
	 * @param in input xml stream
	 * @throws DictionaryException throws exception if dictionary already existed,
	 * or internal errors.
	 * @throws IOException read stream failed.
	 */
	public void updateDictionary(InputStream in)
		throws DictionaryException, IOException
	{
		//解析XML
		cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary = parseXML(in);
		//保存
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
			String message = "保存代码字典失败";
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
	 * 得到所有的字典项目
	 * @return 返回字典项目Iterator
	 * @throws DictionaryException 业务异常
	 */
	public Iterator getAllDictionaries()
		throws DictionaryException
	{
		// 先读取没有在cache中存在的task，并且缓存
		loadAllDictionaryAndCache();
		return dictionaryMap.values().iterator();
	}

	private void loadAllDictionaryAndCache()
		throws DictionaryException
	{

		/* 已经在cache中的dictionary.id,拼凑为'dictionaryID1', 'dictionaryID2'这样的字符串
		 * 用来在查询数据时组成查询字符串
		 */
		Set dictionaryIDSet = dictionaryMap.keySet();
		StringBuffer buff = new StringBuffer();
		for(Iterator dictionaryIDIter = dictionaryIDSet.iterator(); dictionaryIDIter.hasNext(); )
		{
			String dictionaryID = (String) dictionaryIDIter.next();
			buff.append("'").append(dictionaryID).append("'").append(",");
		}
		// 如果buff里面有字符，那么删除最后一个","字符
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

			// 如果缓存已经有了，那么不必从数据库读取
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
			log.error("读取所有数据字典条目出错", e);
			throw new DictionaryException("读取所有数据字典条目出错", e);
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
	 * 添加缓存记录key=dictionary.id(),value=dictionary
	 * @param dictionary 代码字典对象
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
	 * 加载代码字典的条目信息
	 * @param dictionary 代码字典对象
	 * @param session Session对象
	 * @throws HibernateException 底层异常
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
	 * 根据id查找代码字典
	 * @param id 代码字典id
	 * @param session session对象
	 * @return 指定的代码字典
	 * @throws HibernateException 操作失败抛出
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
	 * 创建代码字典
	 * @param id  字典项目的ID号
	 * @return 返回字典Dictionary接口
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
					throw new DictionaryException("指定的代码字典不存在");
				}
				DictionaryForm dictionary = findByID(id, session);
				Dictionary dic = new DBDictionary(dictionary);
				putCache(dic);
				return dic;
			}
			catch(HibernateException e)
			{
				log.error("读取数据字典出错", e);
				throw new DictionaryException("读取数据字典出错", e);
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
	 * 根据ID号删除字典项目
	 * @param id  字典项目的ID号
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
				throw new DictionaryException("指定的代码字典不存在");
			}
			delete(id, session);
			transaction.commit();
		}
		catch(HibernateException e)
		{
			log.error("删除代码字典出错dictionaryID=" + id + ")", e);
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
			throw new DictionaryException("删除代码字典出错(dictionaryID=" + id + ")", e);
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
	 * 删除代码字典
	 * @param id 代码字典ids
	 * @param session session对象
	 * @throws HibernateException
	 */
	private void delete(String id, Session session)
		throws HibernateException
	{
		DictionaryForm dictionary = findByID(id, session);
		//删除代码字典
		session.delete(dictionary);
		//删除代码字典条目
		deleteEntries(id, session);
		//删除缓存中字典条目
		removeFromCache(id);
	}

	/**
	 * 清除dictionary缓存中key=dictionaryID的记录
	 * @param dictionaryID 代码字典ID号
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
	 * 删除代码字典的条目
	 * @param id 代码字典id
	 * @param session Session对象
	 * @throws HibernateException 删除失败抛出
	 */
	private void deleteEntries(String id, Session session)
		throws HibernateException
	{
		Connection conn = session.connection();
		PreparedStatement pst = null;
		//删除存放代码字典条目的数据表
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
	 * 将XML文件解析成XML对象
	 * @param xmlInputStream XML流
	 * @return XML对象Dictionary
	 * @throws DictionaryException
	 */
	private cn.com.youtong.apollo.dictionary.xml.Dictionary parseXML(InputStream xmlInputStream)
		throws DictionaryException
	{

		/** 解析XML文件 */
		cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary = null;
		try
		{
			//用Castor将XML文件映射到对象中
			dictionary = cn.com.youtong.apollo.dictionary.xml.Dictionary.unmarshal(new InputStreamReader(xmlInputStream, "gb2312"));
		}
		catch(Exception ex)
		{
			throw new DictionaryException("无效的XML文件", ex);
		}
		return dictionary;
	}

	/**
	 * 将XML对象转换为Hibernate对象
	 * @param dictionary XML对象Dictionary
	 * @return Hibernate对象DictionaryForm
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
	 * 从XML中解析出的Dictionary对象得到对象DictionaryEntryForm的集合
	 * @param dictionary XML 对象
	 * @return DictionaryEntryForm集合
	 * @throws DictionaryException
	 */
	private Set getDictionaryEntryForms(cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary)
	{
		Set result = new HashSet();
		for(int i = 0; i < dictionary.getItemCount(); i++)
		{
			Item item = dictionary.getItem(i);
			DictionaryEntryForm dictionaryEntryForm = new DictionaryEntryForm();
			//设置基本信息
			dictionaryEntryForm.setEntryKey(item.getKey());
			dictionaryEntryForm.setEntryValue(item.getValue());
			//保存到集合
			result.add(dictionaryEntryForm);
		}
		return result;
	}

	/**
	 * 将Hibernate对象　DictionaryForm树存入数据库
	 * 创建成功则返回　Dictionary的ID号，　创建失败则抛出异常
	 * @param dictionaryForm Hibernate对象树
	 * @return 创建的代码字典的ID号dictionaryID
	 * @throws TaskException 创建失败则抛出异常
	 */
	/**
	 * 将代码字典存入数据库
	 * @param dictionary 代码字典对象
	 * @param session Session对象
	 * @throws HibernateException
	 */
	private void save(cn.com.youtong.apollo.dictionary.xml.Dictionary dictionary, Session session)
		throws HibernateException
	{
		if(isDictionaryDefined(dictionary.getID(), session))
		{
			delete(dictionary.getID(), session);
		}
		//保存Dictionary
		DictionaryForm form = getDictionaryForm(dictionary);
		form.setDateCreated(new java.util.Date());
		form.setDateModified(new java.util.Date());
		session.save(form);
		//保存DictionaryEntry
		saveEntries(form, session);
	}

	/**
	 * 保存代码字典实体对象
	 * @param dictionaryForm 代码字典对象
	 * @param session Session对象
	 * @throws HibernateException 底层异常
	 */
	private void saveEntries(DictionaryForm dictionaryForm, Session session)
		throws HibernateException
	{
		String tableName = NameGenerator.generateDictionaryTableName(dictionaryForm.getDictid());
		createTable(tableName, session);
		insertEntries(tableName, dictionaryForm.getDictionaryEntries(), session);
	}

	/**
	 * 创建存放代码字典条目的数据库表
	 * @param tableName 数据库表名
	 * @param session Session对象
	 * @throws HibernateException 底层异常
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
	 * 将代码字典条目插入到数据库表中
	 * @param tableName 数据库表名
	 * @param entries 代码字典条目集合
	 * @param session Session对象
	 * @throws HibernateException 底层异常
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
	 * 判断代码字典是否存在
	 * @param id 代码字典id
	 * @param session Session对象
	 * @return 存在则返回true,否则返回false
	 * @throws HibernateException 底层异常
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
				throw new DictionaryException("指定的代码字典不存在");
			}
			log.info("开始导出代码字典dictionaryID=" + id + "");
			DictionaryForm form = findByID(id, session);
			getCatorDictionary(form, session).marshal(new OutputStreamWriter(out, "gb2312"));
		}
		catch(Exception e)
		{
			String message = "导出代码字典dictionaryID=" + id + "失败";
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
	 * 得到代码字典的castor对象
	 * @param dictionary 代码字典
	 * @param session Session对象
	 * @return 代码字典的castor对象
	 * @throws HibernateException
	 */
	private cn.com.youtong.apollo.dictionary.xml.Dictionary getCatorDictionary(DictionaryForm dictionary, Session session)
		throws HibernateException
	{
		cn.com.youtong.apollo.dictionary.xml.Dictionary result = new cn.com.youtong.apollo.dictionary.xml.Dictionary();
		//基本属性
		result.setDateModified(dictionary.getDateModified());
		result.setFlag(dictionary.getFlag());
		result.setID(dictionary.getDictid());
		result.setKeyLength(dictionary.getKeyLength());
		result.setLevelPosition(dictionary.getLevelPosition());
		result.setMemo(dictionary.getMemo());
		result.setName(dictionary.getName());
		//代码条目
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
