package cn.com.youtong.apollo.task.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.data.form.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.ScriptSuit;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.db.form.*;
import cn.com.youtong.apollo.task.xml.*;
import cn.com.youtong.apollo.task.xml.Sequence;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;
import org.apache.fulcrum.factory.*;
import cn.com.youtong.apollo.services.Factory;

/**
 * TaskManager的数据库实现
 * <p> Copyright: Copyright (c) 2003 </p>
 * <p> Company: 北京世纪友通科技有限公司 </p>
 *
 * @author wjb & mk
 * @version 1.0
 */
public class DBTaskManager
	implements TaskManager
{
	private Log log = LogFactory.getLog(this.getClass());

	public DBTaskManager()
		throws TaskException
	{
		loadAllTaskAndCache();
	}

	/**
	 * 缓存task对象，查找task对象的时候首先从改Map里面取。
	 * 如果不存在，那么在数据库中读取。
	 * 对task对象进行修改和删除操作，那么从taskMap中删除相应缓存对象。
	 * 意即保持里面所以存在的任务是最新的。
	 * key/value=task.id()/task。
	 */
	private Map taskMap = new Hashtable();

	/** 根据主键查找Task是否存在(Task对于的表硬编码为Ytapl_Tasks) */
	private static String SELECT_TASK_BY_ID = "SELECT COUNT(*) FROM Ytapl_Tasks WHERE taskID=?";
	/** 根据String型id查找Task是否存在(Task对于的表硬编码为Ytapl_Tasks) */
	private static String SELECT_TASK_BY_STRID = "SELECT COUNT(*) FROM Ytapl_Tasks WHERE strID=?";
	/** 删除任务对应的单位填报状况 */
	private static final String DELETE_FILLSTATE_SQL = "DELETE FROM Ytapl_FillState WHERE taskID=?";
	/** 删除任务对应的表样 */
	private static final String DELETE_XSLT_SQL = "DELETE FROM Ytapl_TaskView WHERE taskID=?";
	/** 删除任务对应的单位地址记录 */
	private static final String DELETE_ADDRESS_INFO_SQL = "DELETE FROM Ytapl_AddressInfo WHERE taskID=?";
	/**
	 * 得到所有任务
	 *
	 * @return 所有任务
	 * @throws TaskException
	 * @see cn.com.youtong.apollo.task.TaskManager#getAllTasks()
	 */
	public Iterator getAllTasks()
		throws TaskException
	{
		return taskMap.values().iterator();
	}

	/**
	 * 加载所有的任务，并缓存到TaskManager中
	 * @throws TaskException
	 */
	private void loadAllTaskAndCache()
		throws TaskException
	{
		// 已经在cache中的task id,拼凑为'taskID1', 'taskID2'这样的字符串

		Set taskIDSet = taskMap.keySet();
		StringBuffer buff = new StringBuffer();
		for(Iterator taskIDIter = taskIDSet.iterator(); taskIDIter.hasNext(); )
		{
			String taskID = (String) taskIDIter.next();
			buff.append("'").append(taskID).append("'").append(",");
		}
		// 如果buff里面有字符，那么删除最后一个","字符
		if(buff.length() > 1)
		{
			buff.deleteCharAt(buff.length() - 1);
			if(log.isDebugEnabled())
			{
				log.debug("Tasks load From Cache not Database: " + buff);
			}
		}

		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();

			String hql = "select task from TaskForm task";
			// 如果缓存已经有了，那么不必从数据库读取
			if(buff.length() > 0)
			{
				hql = hql + " where task.ID not in ( " + buff + " )";
			}

			Query query = session.createQuery(hql);

			for(Iterator iter = query.iterate(); iter.hasNext(); )
			{
				TaskForm temp = (TaskForm) iter.next();
				Task task = new DBTask(temp);

				// cache task
				taskMap.put(task.id(), task);
			}
		}
		catch(HibernateException e)
		{
			log.error("读取所有任务出错", e);
			throw new TaskException("读取所有任务出错" + e.getMessage());
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
					e.printStackTrace();
					log.error("Close session happen exception", e);
				}
			}
		}
	}

	/**
	 * 设置任务现行使用的脚本组
	 * @param taskID 脚本组所属的任务ID
	 * @param suitName 脚本组名称ID
	 * @throws TaskException 操作失败
	 */
	public void setActiveScriptSuit(String taskID, String suitName)
		throws TaskException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			TaskForm task = null;
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if(isTaskDefined(taskID, session))
			{
				if(isScriptSuitDefined(taskID, suitName, session))
				{
					log.info("开始将脚本组“" + suitName + "”设置为任务“" + taskID + "”的现行脚本组");
					task = getTask(taskID, session);
					task.setActiveScriptSuitName(suitName);
					session.update(task);
					tx.commit();
					session.refresh(task);
					log.info("将脚本组“" + suitName + "”设置为任务“" + taskID + "”的现行脚本组成功");
				}
				else
				{
					throw new TaskException("指定的脚本组“" + suitName + "”不存在");
				}
			}
			else
			{
				throw new TaskException("指定的任务“" + taskID + "”不存在");
			}
			tx.commit();
		}
		catch(HibernateException ex)
		{
			ex.printStackTrace();
			if(tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch(HibernateException ex1)
				{
				}
			}
			String message = "将脚本组“" + suitName + "”设置为任务“" + taskID + "”的现行脚本组失败";
			log.error(message, ex);
			throw new TaskException(message, ex);
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
		// 更新缓存中对应的task
		putCache(taskID);
	}

	/**
	 * 根据任务id查找任务
	 *
	 * @param id 任务id
	 * @return 任务
	 * @throws TaskException
	 * @see cn.com.youtong.apollo.task.TaskManager#getTaskByID(java.lang.String)
	 */
	public Task getTaskByID(String id)
		throws TaskException
	{
		// 返回缓存中的task
		Task task = (Task) taskMap.get(id);
		if(task == null)
		{
			throw new TaskException("指定的任务不存在[taskID=" + id + "]");
		}
		return task;
	}

	/**
	 * 得到指定的任务
	 * @param taskID 任务id
	 * @param session Session对象
	 * @return TaskForm
	 * @throws HibernateException
	 */
	private TaskForm getTask(Integer taskID, Session session)
		throws HibernateException
	{
		List list = session.find("from TaskForm task where task.taskID = ?", taskID, Hibernate.INTEGER);
		return(TaskForm) list.get(0);
	}

	/**
	 * 得到指定的任务
	 * @param ID 任务ID
	 * @param session Session对象
	 * @return TaskForm Hibernate对象
	 * @throws HibernateException
	 */
	private TaskForm getTask(String ID, Session session)
		throws HibernateException
	{
//        List list = session.find("from TaskForm task where task.ID = ?", ID,
//                                 Hibernate.STRING);
		//        return (TaskForm) list.get(0);

		String hql = "select task from TaskForm task where task.ID = '" + ID + "'";
		Query query = session.createQuery(hql);
		Iterator iter = query.iterate();
		return(TaskForm) iter.next();
	}

	/**
	 * 删除任务
	 * @param ID 任务ID
	 * @throws TaskException 发生异常
	 */
	private void deleteTask(String ID)
		throws TaskException
	{
		Session session = null;
		Transaction transaction = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			if(!isTaskDefined(ID, session))
			{
				throw new TaskException("指定的任务不存在");
			}

			// log
			if(log.isInfoEnabled())
			{
				log.info("删除任务[taskID=" + ID + "]");
			}

			TaskForm form = getTask(ID, session);

			// close session will close connection automatically
			Connection con = session.connection();

			dropTables(form, con);

			// delete related permisssion records
			session.delete("select unitPerm from UnitPermissionForm unitPerm where unitPerm.comp_id.taskID =?", form.getID(), Hibernate.STRING);

			// delete task times
			session.delete("select time from TaskTimeForm time where time.task.taskID=?", form.getTaskID(), Hibernate.INTEGER);

			//delete selectSumSchemas
			session.delete("select schema from SelectSumSchemaForm schema where schema.taskID=?", form.getID(), Hibernate.STRING);

			// delete scalar query template
			session.delete("select template from ScalarQueryTemplateForm template where template.taskID=?", form.getID(), Hibernate.STRING);

			// delete scriptSuits
			Set suits = form.getScriptSuits();
			Iterator itr = suits.iterator();
			while(itr.hasNext())
			{
				ScriptSuitForm suit = (ScriptSuitForm) itr.next();
				deleteScriptSuit(form.getTaskID(), suit.getName(), session);
			}

			// delete related records from ytapl_fillstate
			deleteFillState(ID, session);
			// delte related records from ytapl_addressinfo
			deleteAddressInfo(ID, session);

			// begin delete related tables
			Collection tableCol = form.getTables();
			for(Iterator tableIter = tableCol.iterator(); tableIter.hasNext(); )
			{
				TableForm tableForm = (TableForm) tableIter.next();

				// delete related table xslt
				session.delete("select xslt from TableViewForm xslt where xslt.table.tableID = ?", tableForm.getTableID(), Hibernate.INTEGER);

				// begin delete related rows
				Collection rowCol = tableForm.getRows();
				for(Iterator rowIter = rowCol.iterator(); rowIter.hasNext(); )
				{
					RowForm rowForm = (RowForm) rowIter.next();

					// begin delete related cells
					session.delete("select cell from CellForm cell where cell.row.rowID=" + rowForm.getRowID());

					session.delete(rowForm);
				} // end delete related rows

				session.delete(tableForm);
			} // end delete related tables

			// delete unitmetatable
			// ----------------------------------------------------------------------
			// 		Unitmetatable is a Table, TaskForm.getTables() return all |
			// 		tables related with this task, also included Unitmetatable! |
			// 		So UnitmetaTable has been delete when delete |
			// 		TaskForm.getTables() |
			// ----------------------------------------------------------------------

			session.delete(form.getUnitMeta());
			session.delete(form);

			transaction.commit();

			// 删除缓存中对应的task
			removeFromCache(ID);
		}
		catch(Exception e)
		{
			log.error("删除任务出错(ID=" + ID + ")", e);
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
			throw new TaskException("删除任务出错(ID=" + ID + ")", e);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 删除该任务对应的单位填报状况.
	 * 此方法没有开启事务,所以修改在此方法里面没有提交到数据库.
	 * @param taskID              任务ID
	 * @param session             Hibernate会话
	 */
	private void deleteFillState(String taskID, Session session)
	{
		if(log.isInfoEnabled())
		{
			log.info("删除任务对应的单位上报状况[taskID=" + taskID + "]");
		}

		PreparedStatement stmt = null;
		try
		{
			Connection con = session.connection();
			stmt = con.prepareStatement(DELETE_FILLSTATE_SQL);
			stmt.setString(1, taskID);

			stmt.execute();
		}
		catch(Exception ex)
		{
			String msg = "删除任务对应的单位填报状况出错[taskID=" + taskID + "]";
			log.error(msg, ex);
			// ingore it
		}
		finally
		{
			SQLUtil.close(stmt);
		}
	}

	/**
	 * 删除该任务对应的单位地址信息.
	 * 此方法没有开启事务,所以修改在此方法里面没有提交到数据库.
	 * @param taskID              任务ID
	 * @param session             Hibernate会话
	 */
	private void deleteAddressInfo(String taskID, Session session)
	{
		if(log.isInfoEnabled())
		{
			log.info("删除任务对应的单位地址记录[taskID=" + taskID + "]");
		}

		PreparedStatement stmt = null;
		try
		{
			Connection con = session.connection();
			stmt = con.prepareStatement(DELETE_ADDRESS_INFO_SQL);
			stmt.setString(1, taskID);

			stmt.execute();
		}
		catch(Exception ex)
		{
			String msg = "删除任务对应的单位地址记录出错[taskID=" + taskID + "]";
			log.error(msg, ex);
			// ingore it
		}
		finally
		{
			SQLUtil.close(stmt);
		}
	}

	/**
	 * 删除任务
	 * @param task 任务
	 * @throws TaskException 发生异常
	 * @see cn.com.youtong.apollo.task.TaskManager#deleteTask(cn.com.youtong.apollo.task.Task)
	 */
	public void deleteTask(Task task)
		throws TaskException
	{
		String taskID = task.id();
		deleteTask(taskID);
	}

	/**
	 * 发布任务
	 * @param xmlInputStream 要发布的任务参数的xml流
	 * @return 成功发布的任务
	 * @throws TaskException 发布失败时抛出
	 */
	public Task publishTask(InputStream xmlInputStream)
		throws TaskException
	{
		//创建对象树
		DBXMLBuilder dbx = null;
		try
		{
			dbx = new DBXMLBuilder(new InputStreamReader(xmlInputStream, "gb2312"));
		}
		catch(UnsupportedEncodingException ex)
		{
			throw new TaskException(ex);
		}
		//将对象存入数据库，并创建数据表
		DBXMLDirector.create(dbx);
		// 发布XSLT样式信息
		publishXSLTs(dbx.getTableViews());
		//更新缓存中的相关任务
		String taskID = dbx.getTaskForm().getID();
		putCache(taskID);

		if(log.isInfoEnabled())
		{
			log.info("发布任务[taskID=" + taskID + "]");
		}
		return this.getTaskByID(taskID);
	}

	/**
	 * 发布样式信息XSLTs
	 * @param itrTableViews 样式信息的对象迭代器，
	 * 迭代器中的结构为：
	 * 包含ArrayList对象
	 * 每个ArrayList对象包含三个对象结构为：
	 * 0位置是Integer型　tableID
	 * 1位置是Integer型  type
	 * 2位置是String型   xslt文本
	 * @throws TaskException 业务异常
	 */
	private void publishXSLTs(Iterator itrTableViews)
		throws TaskException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			while(itrTableViews.hasNext())
			{
				ArrayList list = (ArrayList) itrTableViews.next();

				Integer tableID = (Integer) list.get(0);
				Integer type = (Integer) list.get(2);
				Reader xsltReader = new StringReader(((String) list.get(3)).trim());

				if(isXSLTDefined(tableID, type, session))
				{
					updateXSLT(tableID, type, xsltReader, session);
				}
				else
				{
					createXSLT(tableID, type, xsltReader, session);
				}
			}
			tx.commit();
		}
		catch(Exception ex)
		{
			try
			{
				tx.rollback();
			}
			catch(HibernateException ex1)
			{
			}
			throw new TaskException(ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 判断任务是否在数据库中存在
	 *
	 * @param taskID
	 *            任务id
	 * @param session
	 *            hibernate会话
	 * @return 存在返回true；否则false
	 * @throws HibernateException
	 *             发生hibernate异常
	 */
	private boolean isTaskDefined(Integer taskID, Session session)
		throws HibernateException
	{
		Connection con = session.connection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			ps = con.prepareStatement(SELECT_TASK_BY_ID);
			ps.setInt(1, taskID.intValue());
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			return count > 0;
		}
		catch(SQLException sqle)
		{
			throw new HibernateException(sqle);
		}
		finally
		{
			if(rs != null)
			{
				try
				{
					rs.close();
				}
				catch(Exception ex)
				{}
			}
			if(ps != null)
			{
				try
				{
					ps.close();
				}
				catch(SQLException ex)
				{}
			}
		}
	}

	/**
	 * 判断任务是否在数据库中存在
	 *
	 * @param id
	 *            任务id
	 * @param session
	 *            hibernate会话
	 * @return 存在返回true；否则false
	 * @throws HibernateException
	 *             发生hibernate异常
	 */
	private boolean isTaskDefined(String id, Session session)
		throws HibernateException
	{
		Connection con = session.connection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			ps = con.prepareStatement(SELECT_TASK_BY_STRID);
			ps.setString(1, id);
			rs = ps.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			return count > 0;
		}
		catch(SQLException sqle)
		{
			throw new HibernateException(sqle);
		}
		finally
		{
			if(rs != null)
			{
				try
				{
					rs.close();
				}
				catch(Exception ex)
				{}
			}
			if(ps != null)
			{
				try
				{
					ps.close();
				}
				catch(SQLException ex)
				{}
			}
		}
	}

	/**
	 * 删除任务创建过程中创建的用来保存数据的表
	 *
	 * @param form
	 *            任务
	 * @param con
	 *            数据库连接(此方法没有关闭连接)
	 * @throws SQLException
	 * @throws TaskException
	 */
	private void dropTables(TaskForm form, Connection con)
		throws SQLException, TaskException
	{

		DBTask task = new DBTask(form);
		DBModelHelper helper = new DBModelHelper();
		HashMap tableInfos = helper.getDBTableInfos(task);

		Set tableIDs = tableInfos.keySet();
		for(Iterator tableIter = tableIDs.iterator(); tableIter.hasNext(); )
		{
			String tableID = (String) tableIter.next();
			TableInfo tableInfo = (TableInfo) tableInfos.get(tableID);

			String tableName = tableInfo.getTableName();
			dropTable(tableName, con);
			//if (tableInfo.isExist()) {
			// 删除该表，如果存在，删除这个表
			//     dropTable(tableName, con);
			//  }

			// 浮动表
			HashMap floatTableInfos = tableInfo.getFloatTables();

			// 删除浮动表
			dropFloatTables(floatTableInfos, con);
		}

		// 删除附件表
		dropAttachmentTable(form.getID(), con);
	}

	/**
	 * 表若存在浮动行，那么浮动行数据在数据库中有单独的存放表。 dropFloatTables()方法就是删除这些数据库表
	 *
	 * @param tableInfos
	 *            浮动表信息
	 * @param con
	 *            数据库连接(此方法没有关闭)
	 * @throws SQLException
	 */
	private void dropFloatTables(HashMap tableInfos, Connection con)
		throws SQLException
	{

		Set tableIDs = tableInfos.keySet();
		for(Iterator tableIter = tableIDs.iterator(); tableIter.hasNext(); )
		{
			String tableID = (String) tableIter.next();
			TableInfo tableInfo = (TableInfo) tableInfos.get(tableID);

			String tableName = tableInfo.getTableName();
			// 删除该表
			dropTable(tableName, con);
		}

	}

	/**
	 * 删除表
	 *
	 * @param tableName
	 *            删除的表名
	 * @param con
	 *            数据库连接(此方法没有关闭连接)
	 * @throws SQLException
	 */
	private void dropTable(String tableName, Connection con)
		throws SQLException
	{
		String sql = "drop table " + tableName;
		PreparedStatement ps = null;

		try
		{
			ps = con.prepareStatement(sql);
			ps.execute();

			// log
			if(log.isInfoEnabled())
			{
				log.info("删除表格" + tableName);
			}
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			SQLUtil.close(ps);
		}
	}

	/**
	 * 删除附件表
	 *
	 * @param taskID 任务id
	 * @param conn 数据库连接(此方法没有关闭连接)
	 *
	 * @throws SQLException
	 */
	private void dropAttachmentTable(String taskID, Connection conn)
		throws SQLException
	{
		String sql = "drop table " + NameGenerator.generateAttachmentTableName(taskID);
		PreparedStatement ps = null;

		try
		{
			ps = conn.prepareStatement(sql);
			ps.execute();
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(ps != null)
			{
				ps.close();
			}
		}
	}

	/**
	 * 发布XSLT文件，如果存在，覆盖
	 * @param xmlInputStream 任务XML流
	 * @return Task 成功发布的脚本组
	 * @throws TaskException 发布脚本组失败
	 */
	public Task publishXSLT(InputStream xmlInputStream)
		throws TaskException
	{
		//创建对象树，解析任务文件以获得需要的各种对象
		DBXMLBuilder dbx = null;
		try
		{
			dbx = new DBXMLBuilder(new InputStreamReader(xmlInputStream, "gb2312"));
		}
		catch(UnsupportedEncodingException ex2)
		{
			throw new TaskException(ex2);
		}

		TaskForm taskForm = dbx.getTaskForm();
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if(!isTaskDefined(taskForm.getID(), session))
			{
				throw new TaskException("指定的任务“" + taskForm.getID() + "”不存在");
			}

			//删除该任务中原来的样式信息
			Iterator itrTables = getTaskByID(taskForm.getID()).getAllTables();
			HashMap tempTableMap = new HashMap();
			while(itrTables.hasNext())
			{
				DBTable table = (DBTable) itrTables.next();
				tempTableMap.put(table.id(), table.getTableID());
				session.delete("select xslt from TableViewForm xslt where xslt.table.tableID = ?", table.getTableID(), Hibernate.INTEGER);
			}

			//发布表样信息
			Iterator itrTableViews = dbx.getTableViews();
			while(itrTableViews.hasNext())
			{
				ArrayList list = (ArrayList) itrTableViews.next();

				String strTableID = (String) list.get(1);
				Integer type = (Integer) list.get(2);
				Reader xsltReader = new StringReader(((String) list.get(3)).trim());

				Integer tableID = (Integer) tempTableMap.get(strTableID);
				if(tableID == null)
				{
					throw new TaskException("发布样式的表\"" + strTableID + "\"不存在");
				}
				createXSLT(tableID, type, xsltReader, session);
			}
			tx.commit();
		}
		catch(HibernateException ex)
		{
			try
			{
				tx.rollback();
			}
			catch(HibernateException ex1)
			{
			}
			log.debug(ex);
			throw new TaskException("保存失败", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		//更新缓存中的相关任务
		putCache(taskForm.getID());
		return getTaskByID(taskForm.getID());
	}

	/**
	 * 删除XSLT
	 * @param taskID 任务ID号
	 * @param viewID viewID
	 * @throws TaskException 删除失败
	 */
	public void deleteXSLT(String taskID, Integer viewID)
		throws TaskException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			if(!this.isXSLTDefined(viewID, session))
			{
				return;
			}

			TableViewForm tableViewForm = new TableViewForm();
			tableViewForm.setViewID(viewID);
			session.delete(tableViewForm);
			tx.commit();

			// log
			if(log.isInfoEnabled())
			{
				log.info("删除任务[taskID=" + taskID + "] 脚本[viewID=" + viewID);
			}
		}
		catch(HibernateException ex)
		{
			log.debug(ex);
			try
			{
				tx.rollback();
			}
			catch(HibernateException ex1)
			{
			}
			throw new TaskException("删除失败");
		}
		finally
		{
			HibernateUtil.close(session);
		}
		//更新缓存中的相关任务
		putCache(taskID);
	}

	/**
	 * 查找任务的XSLT文件
	 * @param viewID tableViewID号
	 * @return TaskView对象
	 * @throws TaskException
	 */
	public cn.com.youtong.apollo.task.TableView getView(Integer viewID)
		throws TaskException
	{
		DBTableView result = null;
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			TableViewForm tableViewForm = new TableViewForm();
			session.load(tableViewForm, viewID);
			tx.commit();
			result = new DBTableView(tableViewForm);
		}
		catch(HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch(HibernateException ex1)
			{
			}
			throw new TaskException("没找到XSLT数据");
		}
		finally
		{
			HibernateUtil.close(session);
		}
		return result;
	}

	/**
	 * 发布脚本组，如果存在，覆盖
	 * @param taskID 任务ID
	 * @param xmlInputStream 脚本组xml流
	 * @return 成功发布的脚本组
	 * @throws TaskException 发布脚本组失败
	 */
	public ScriptSuit publishScriptSuit(String taskID, InputStream xmlInputStream)
		throws TaskException
	{
		ScriptSuit result = null;
		Session session = null;
		Transaction tx = null;
		try
		{
			cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit = cn.com.youtong.apollo.task.xml.ScriptSuit.unmarshal(new InputStreamReader(xmlInputStream, "gb2312"));
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			if(!isTaskDefined(taskID, session))
			{
				throw new TaskException("指定的任务“" + taskID + "”不存在");
			}
			TaskForm task = getTask(taskID, session);
			//log.info("开始发布脚本组“" + xmlSuit.getName() + "”");
			if(isScriptSuitDefined(taskID, xmlSuit.getName(), session))
			{
				result = new DBScriptSuit(updateScriptSuit(taskID, xmlSuit, session));
			}
			else
			{
				result = new DBScriptSuit(createScriptSuit(taskID, xmlSuit, session));
			}
			//log.info("发布脚本组“" + xmlSuit.getName() + "”成功");
			tx.commit();

			// log
			if(log.isInfoEnabled())
			{
				log.info("发布任务[taskID=" + taskID + "] 脚本组[suitName=" + xmlSuit.getName() + "]");
			}
		}
		catch(Exception ex)
		{
			if(tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch(HibernateException ex1)
				{
				}
			}
			String message = "发布脚本组失败";
			log.error(message, ex);
			throw new TaskException(message, ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		// 更新缓存中对应的task
		putCache(taskID);
		return result;
	}

	/**
	 * 将任务对象从缓存中删除，同时删除缓存中任务对应的modelManager
	 * @param taskID 任务ID号
	 * @throws TaskException
	 */
	private void removeFromCache(String taskID)
		throws TaskException
	{
		try
		{
			if(log.isDebugEnabled())
			{
				log.debug("Remove Task from Cache: task.id() = " + taskID);
			}
			taskMap.remove(taskID);
			//删除缓存中task对应的ModelManager
			ModelManagerFactory factory = (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName());
			factory.deleteModelManager(taskID);
		}
		catch(Exception ex)
		{
			log.error("更新缓存失败", ex);
			throw new TaskException("更新缓存失败", ex);
		}
	}

	/**
	 * 将任务对象添加到缓存，同时更新缓存中任务对应的modelManager
	 * @param taskID 任务
	 * @throws TaskException
	 */
	private void putCache(String taskID)
		throws TaskException
	{
		Session session = null;
		Task task = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			task = new DBTask(getTask(taskID, session));

			if(log.isDebugEnabled())
			{
				log.debug("Put Task into Cache: task.id() = " + taskID);
			}
			taskMap.put(taskID, task);

			//更新缓存中task对应的ModelManager
			ModelManagerFactory factory = (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName());
			factory.updateModelManager(taskID);
		}
		catch(Exception ex)
		{
			log.error("更新缓存失败", ex);
			throw new TaskException("更新缓存失败", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * 插入XSLT文件
	 * @param tableID 表ID
	 * @param type XSLT的用途
	 * @param xsltReader XSLT的数据源
	 * @param session Hibernate对象
	 * @return 文件对象
	 * @throws HibernateException
	 */
	private TableViewForm createXSLT(Integer tableID, Integer type, Reader xsltReader, Session session)
		throws HibernateException
	{
		try
		{
			//create XSLT
			TableViewForm tableViewForm = new TableViewForm();
			TableForm tempTableForm = new TableForm();
			tempTableForm.setTableID(tableID);
			tableViewForm.setTable(tempTableForm);
			tableViewForm.setType(type);
			tableViewForm.setContent(Hibernate.createClob(" "));

			session.save(tableViewForm);
			session.flush();
			session.refresh(tableViewForm, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_tableviews SET content = ? WHERE tableID = '" + tableViewForm.getTable().getTableID() + "' AND type = '" + tableViewForm.getType() + "' ";
			Config.getCurrentDatabase().UpdateClob(tableViewForm.getContent(), xsltReader, updateSql, session.connection());

			return tableViewForm;
		}
		catch(Exception ex)
		{
			log.error(ex);
			throw new HibernateException(ex);
		}
	}

	/**
	 * 插入XSLT文件
	 * @param tableID 表ID
	 * @param type XSLT用途
	 * @param xsltReader 数据源
	 * @param session Hibernate对象
	 * @return 文件对象
	 * @throws HibernateException
	 */
	private TableViewForm updateXSLT(Integer tableID, Integer type, Reader xsltReader, Session session)
		throws HibernateException
	{
		try
		{
			//update XSLT
			TableViewForm tableViewForm = new TableViewForm();
			TableForm tempTableForm = new TableForm();
			tempTableForm.setTableID(tableID);
			tableViewForm.setTable(tempTableForm);
			tableViewForm.setType(type);
			tableViewForm.setContent(Hibernate.createClob(" "));
			session.update(tableViewForm);
			session.flush();
			session.refresh(tableViewForm, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_tableviews SET content = ? WHERE tableID = '" + tableViewForm.getTable().getTableID() + "' AND type = '" + tableViewForm.getType() + "' ";
			Config.getCurrentDatabase().UpdateClob(tableViewForm.getContent(), xsltReader, updateSql, session.connection());

			return tableViewForm;
		}
		catch(Exception ex)
		{
			log.error(ex);
			throw new HibernateException(ex);
		}
	}

	/**
	 * 更新脚本组
	 * @param taskID 任务ID
	 * @param xmlSuit 脚本组的xml对象
	 * @param session Hibernate对象
	 * @return 更新后的脚本对象
	 * @throws HibernateException
	 */
	private ScriptSuitForm updateScriptSuit(String taskID, cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Session session)
		throws HibernateException
	{
		ScriptSuitForm suit = getScriptSuitForm(taskID, xmlSuit.getName(), session);
		//删除原有scripts
		deleteScripts(suit, session);
		//创建新的scripts
		List scripts = createScripts(xmlSuit, suit.getSuitID(), session);

		//更新ScriptSuit
		suit.setDateModified(new java.util.Date());
		suit.setExecSeqence(getExecSequence(xmlSuit));
		suit.setMemo(xmlSuit.getMemo());
		suit.setName(xmlSuit.getName());
		suit.setScripts(new HashSet(scripts));
		session.update(suit);

		if(log.isInfoEnabled())
		{
			log.info("更新任务[taskID=" + taskID + "] 脚本[suitName=" + xmlSuit.getName() + "]");
		}

		return suit;
	}

	/**
	 * 删除脚本组的脚本
	 * @param suit 脚本组
	 * @param session Session对象
	 * @throws HibernateException
	 */
	private void deleteScripts(ScriptSuitForm suit, Session session)
		throws HibernateException
	{
		session.delete("select script from ScriptForm script where script.suitID =?", suit.getSuitID(), Hibernate.INTEGER);
	}

	/**
	 * 得到指定的脚本组
	 * @param taskID 任务ID
	 * @param name 脚本组名称
	 * @param session Session对象
	 * @return ScriptSuitForm指定的脚本组
	 * @throws HibernateException
	 */
	private ScriptSuitForm getScriptSuitForm(Integer taskID, String name, Session session)
		throws HibernateException
	{
		List list = session.find("from ScriptSuitForm suit where suit.taskID = ? and suit.name = ?", new Object[]
			{taskID, name}
			, new Type[]
			{Hibernate.INTEGER, Hibernate.STRING});
		return(ScriptSuitForm) list.get(0);
	}

	/**
	 * 得到指定的脚本组
	 * @param taskID 任务ID
	 * @param name 脚本组名称
	 * @param session Session对象
	 * @return 指定的脚本组
	 * @throws HibernateException
	 */
	private ScriptSuitForm getScriptSuitForm(String taskID, String name, Session session)
		throws HibernateException
	{
		List list = session.find("select suit from ScriptSuitForm suit, TaskForm task where suit.taskID = task.taskID and task.ID = ? and suit.name = ?", new Object[]
								 {taskID, name}
								 , new Type[]
								 {Hibernate.STRING, Hibernate.STRING});
		return(ScriptSuitForm) list.get(0);
	}

	/**
	 * 更新脚本组
	 * @param taskID 任务ID
	 * @param xmlSuit 脚本组的xml对象
	 * @param session Hibernate对象
	 * @return 更新后的脚本对象
	 * @throws HibernateException
	 */
	private ScriptSuitForm createScriptSuit(String taskID, cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Session session)
		throws HibernateException
	{
		TaskForm task = getTask(taskID, session);
		ScriptSuitForm suit = new ScriptSuitForm();
		//创建ScriptSuit
		suit.setDateCreated(new java.util.Date());
		suit.setDateModified(new java.util.Date());
		suit.setExecSeqence(getExecSequence(xmlSuit));
		suit.setMemo(xmlSuit.getMemo());
		suit.setName(xmlSuit.getName());
		suit.setSuitID(HibernateUtil.getNextScriptSuitID());
		suit.setTaskID(task.getTaskID());
		session.save(suit);
		//创建scripts
		List scripts = createScripts(xmlSuit, suit.getSuitID(), session);

		//更新ScriptSuit
		suit.setScripts(new HashSet(scripts));
		session.update(suit);

		return suit;
	}

	/**
	 * 得到脚本执行顺序字符串，字符串为脚本名称序列，每个名称之间以“,”隔开
	 * @param xmlSuit 脚本组xml对象
	 * @return 脚本执行顺序字符串
	 */
	private String getExecSequence(cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit)
	{
		String result = "";
		cn.com.youtong.apollo.task.xml.ScriptEntry[] entries = xmlSuit.getSequence().getScriptEntry();
		for(int i = 0; i < entries.length; i++)
		{
			if(i > 0)
			{
				result += ",";
			}
			result += entries[i].getName();
		}
		return result;
	}

	/**
	 * 创建脚本ScriptForm集合
	 * @param xmlSuit 脚本组xml对象
	 * @param suitID 脚本组ID
	 * @param session Session对象
	 * @return 脚本ScriptForm集合
	 * @throws HibernateException
	 */
	private List createScripts(cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Integer suitID, Session session)
		throws HibernateException
	{
		List result = new LinkedList();
		//表间审核脚本
		if(xmlSuit.getAuditCrossTable() != null)
		{
			result.add(createScriptForm(xmlSuit.getAuditCrossTable(), suitID, session));
		}
		//表间运算脚本
		if(xmlSuit.getCalculateCrossTable() != null)
		{
			result.add(createScriptForm(xmlSuit.getCalculateCrossTable(), suitID, session));
		}
		//表内审核脚本
		cn.com.youtong.apollo.task.xml.AuditInTable[] auditInTables = xmlSuit.getAuditInTable();
		for(int i = 0; i < auditInTables.length; i++)
		{
			result.add(createScriptForm(auditInTables[i], suitID, session));
		}
		//表内运算脚本
		cn.com.youtong.apollo.task.xml.CalculateInTable[] calculateInTables = xmlSuit.getCalculateInTable();
		for(int i = 0; i < calculateInTables.length; i++)
		{
			result.add(createScriptForm(calculateInTables[i], suitID, session));
		}
		return result;
	}

	/**
	 * 创建脚本Form对象
	 * @param auditCrossTable 表间审核脚本
	 * @param suitID 脚本组ID
	 * @param session Session对象
	 * @return 脚本Form对象
	 * @throws HibernateException
	 */
	private ScriptForm createScriptForm(cn.com.youtong.apollo.task.xml.AuditCrossTable auditCrossTable, Integer suitID, Session session)
		throws HibernateException
	{
		try
		{
			ScriptForm script = new ScriptForm(HibernateUtil.getNextScriptID(), auditCrossTable.getName(), suitID, Script.AUDIT_CROSS_TABLE, Hibernate.createClob(" "));
			session.save(script);
			session.flush();
			session.refresh(script, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_scripts SET content = ? WHERE scriptID = " + script.getScriptID();
			Config.getCurrentDatabase().UpdateClob(script.getContent(), auditCrossTable.getContent(), updateSql, session.connection());

			return script;
		}
		catch(Exception ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * 创建脚本Form对象
	 * @param auditInTable 表内审核脚本
	 * @param suitID 脚本组ID
	 * @param session Session对象
	 * @return 脚本Form对象
	 * @throws HibernateException
	 */
	private ScriptForm createScriptForm(cn.com.youtong.apollo.task.xml.AuditInTable auditInTable, Integer suitID, Session session)
		throws HibernateException
	{
		try
		{
			ScriptForm script = new ScriptForm(HibernateUtil.getNextScriptID(), auditInTable.getName(), suitID, Script.AUDIT_IN_TABLE, Hibernate.createClob(" "));
			session.save(script);
			session.flush();
			session.refresh(script, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_scripts SET content = ? WHERE scriptID = " + script.getScriptID();
			Config.getCurrentDatabase().UpdateClob(script.getContent(), auditInTable.getContent(), updateSql, session.connection());

			return script;
		}
		catch(Exception ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * 创建脚本Form对象
	 * @param calculateCrossTable 表间运算脚本
	 * @param suitID 脚本组ID
	 * @param session Session对象
	 * @return 脚本Form对象
	 * @throws HibernateException
	 */
	private ScriptForm createScriptForm(cn.com.youtong.apollo.task.xml.CalculateCrossTable calculateCrossTable, Integer suitID, Session session)
		throws HibernateException
	{
		try
		{
			ScriptForm script = new ScriptForm(HibernateUtil.getNextScriptID(), calculateCrossTable.getName(), suitID, Script.CALCULATE_CROSS_TABLE, Hibernate.createClob(" "));
			session.save(script);
			session.flush();
			session.refresh(script, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_scripts SET content = ? WHERE scriptID = " + script.getScriptID();
			Config.getCurrentDatabase().UpdateClob(script.getContent(), calculateCrossTable.getContent(), updateSql, session.connection());

			return script;
		}
		catch(Exception ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * 创建脚本Form对象
	 * @param calculateInTable 表内运算脚本
	 * @param suitID 脚本组ID
	 * @param session Session对象
	 * @return 脚本Form对象
	 * @throws HibernateException
	 */
	private ScriptForm createScriptForm(cn.com.youtong.apollo.task.xml.CalculateInTable calculateInTable, Integer suitID, Session session)
		throws HibernateException
	{
		try
		{
			ScriptForm script = new ScriptForm(HibernateUtil.getNextScriptID(), calculateInTable.getName(), suitID, Script.CALCULATE_IN_TABLE, Hibernate.createClob(" "));
			session.save(script);
			session.flush();
			session.refresh(script, LockMode.UPGRADE);

			//update clob
			String updateSql = "UPDATE ytapl_scripts SET content = ? WHERE scriptID = " + script.getScriptID();
			Config.getCurrentDatabase().UpdateClob(script.getContent(), calculateInTable.getContent(), updateSql, session.connection());

			return script;
		}
		catch(Exception ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * 删除脚本组
	 * @param taskID 脚本组所属的任务ID
	 * @param suitName 脚本组名称ID
	 * @throws TaskException 删除脚本组失败
	 */
	public void deleteScriptSuit(String taskID, String suitName)
		throws TaskException
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			if(isScriptSuitDefined(taskID, suitName, session))
			{
				TaskForm task = getTask(taskID, session);

				deleteScriptSuit(task.getTaskID(), suitName, session);
				//如果删除的脚本组是任务的现行脚本组，更新任务
				if(task.getActiveScriptSuitName() != null && task.getActiveScriptSuitName().equals(suitName))
				{
					task.setActiveScriptSuitName(null);
					session.update(task);
				}
				//log.info("删除脚本组“" + suitName + "”成功");
				tx.commit();

				// log
				if(log.isInfoEnabled())
				{
					log.info("删除任务[taskID=" + taskID + "] 脚本组[suitName=" + suitName + "]");
				}
				session.refresh(task);
			}
			else
			{
				throw new TaskException("指定的脚本组不存在");
			}
		}
		catch(HibernateException e)
		{
			if(tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch(HibernateException ex)
				{
				}
			}
			String message = "删除脚本组“" + suitName + "”失败";
			log.error(message, e);
			throw new TaskException(message, e);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		// 更新缓存中对应的task
		putCache(taskID);
	}

	/**
	 * 删除脚本组
	 * @param taskID 脚本组所属的任务ID
	 * @param suitName 脚本组名称
	 * @param session Session对象
	 * @throws HibernateException
	 */
	private void deleteScriptSuit(Integer taskID, String suitName, Session session)
		throws HibernateException
	{
		ScriptSuitForm suit = getScriptSuitForm(taskID, suitName, session);
		//删除脚本组中的所有脚本
		deleteScripts(suit, session);
		//删除脚本组
		session.delete(suit);
	}

	/**
	 * 判断指定的脚本组是否存在
	 * @param taskID 脚本组所属的任务ID
	 * @param suitName 脚本组的名称
	 * @param session Session对象
	 * @return boolean 存在返回true, 否则返回false
	 * @throws HibernateException
	 */
	private boolean isScriptSuitDefined(String taskID, String suitName, Session session)
		throws HibernateException
	{
		return((Integer) session.iterate("SELECT COUNT(*) from ScriptSuitForm as suit, TaskForm task WHERE suit.taskID = task.taskID AND task.ID = ? AND suit.name = ?", new Object[]
										 {taskID, suitName}
										 , new Type[]
										 {Hibernate.STRING, Hibernate.STRING}).next()).intValue() > 0;
	}

	/**
	 * 判断指定的XSLT是否存在
	 * @param tableID 表ID
	 * @param type xslt用途
	 * @param session Session对象
	 * @return boolean 存在返回true, 否则返回false
	 * @throws HibernateException
	 */
	private boolean isXSLTDefined(Integer tableID, Integer type, Session session)
		throws HibernateException
	{
		return((Integer) session.iterate("SELECT COUNT(*) from TableViewForm as form WHERE form.table.tableID = ? AND form.type = ?", new Object[]
										 {tableID, type}
										 , new Type[]
										 {Hibernate.INTEGER, Hibernate.INTEGER}).next()).intValue() > 0;
	}

	/**
	 * 判断指定的XSLT是否存在
	 * @param viewID viewID
	 * @param session Session对象
	 * @return boolean 存在返回true, 否则返回false
	 * @throws HibernateException
	 */
	private boolean isXSLTDefined(Integer viewID, Session session)
		throws HibernateException
	{
		return((Integer) session.iterate("SELECT COUNT(*) from TableViewForm as form WHERE form.viewID = ?", viewID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * 导出脚本组
	 * @param taskID 脚本组所属的任务ID
	 * @param suitName 脚本组名称ID
	 * @param out 输出的流
	 * @throws TaskException 操作失败
	 */
	public void outputScriptSuit(String taskID, String suitName, OutputStream out)
		throws TaskException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			if(isScriptSuitDefined(taskID, suitName, session))
			{
				log.info("开始导出脚本组“" + suitName + "”");
				ScriptSuitForm suit = getScriptSuitForm(taskID, suitName, session);
				getCastorScriptSuit(suit, session).marshal(new OutputStreamWriter(out, "gb2312"));
				log.info("删除导出脚本组“" + suitName + "”成功");
			}
			else
			{
				throw new TaskException("指定的脚本组不存在");
			}
		}
		catch(Exception e)
		{
			String message = "导出脚本组“" + suitName + "”失败";
			log.error(message, e);
			throw new TaskException(message, e);
		}
		finally
		{
			if(session != null)
			{
				try
				{
					session.close();
				}
				catch(HibernateException e1)
				{
				}
			}
		}
	}

	/**
	 * 得到脚本组的castor对象
	 * @param suit 脚本组
	 * @param session Session对象
	 * @return 脚本组的castor对象
	 * @throws HibernateException
	 */
	private cn.com.youtong.apollo.task.xml.ScriptSuit getCastorScriptSuit(ScriptSuitForm suit, Session session)
		throws HibernateException
	{
		try
		{
			cn.com.youtong.apollo.task.xml.ScriptSuit result = new cn.com.youtong.apollo.task.xml.ScriptSuit();
			//基本属性
			result.setDateModified(suit.getDateModified());
			result.setMemo(suit.getMemo());
			result.setName(suit.getName());
			//脚本
			for(Iterator itr = suit.getScripts().iterator(); itr.hasNext(); )
			{
				ScriptForm script = (ScriptForm) itr.next();
				switch(script.getType())
				{
					//表间审核脚本
					case Script.AUDIT_CROSS_TABLE:
						AuditCrossTable auditCrossTable = new AuditCrossTable();
						auditCrossTable.setContent(Convertor.Clob2String(script.getContent()));
						auditCrossTable.setName(script.getName());
						result.setAuditCrossTable(auditCrossTable);
						break;
						//表内审核脚本
					case Script.AUDIT_IN_TABLE:
						AuditInTable auditInTable = new AuditInTable();
						auditInTable.setContent(Convertor.Clob2String(script.getContent()));
						auditInTable.setName(script.getName());
						result.addAuditInTable(auditInTable);
						break;
						//表间运算脚本
					case Script.CALCULATE_CROSS_TABLE:
						CalculateCrossTable calculateCrossTable = new CalculateCrossTable();
						calculateCrossTable.setContent(Convertor.Clob2String(script.getContent()));
						calculateCrossTable.setName(script.getName());
						result.setCalculateCrossTable(calculateCrossTable);
						break;
						//表内运算脚本
					case Script.CALCULATE_IN_TABLE:
						CalculateInTable calculateInTable = new CalculateInTable();
						calculateInTable.setContent(Convertor.Clob2String(script.getContent()));
						calculateInTable.setName(script.getName());
						result.addCalculateInTable(calculateInTable);
						break;
				}
			}
			//脚本执行顺序
			result.setSequence(new Sequence());
			if(suit.getExecSeqence() != null)
			{
				StringTokenizer tokenizer = new StringTokenizer(suit.getExecSeqence(), ",");
				while(tokenizer.hasMoreTokens())
				{
					ScriptEntry entry = new ScriptEntry();
					entry.setName(tokenizer.nextToken());
					result.getSequence().addScriptEntry(entry);
				}
			}
			return result;
		}
		catch(Exception ex)
		{
			throw new HibernateException(ex);
		}
	}

}