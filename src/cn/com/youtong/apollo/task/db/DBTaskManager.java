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
 * TaskManager�����ݿ�ʵ��
 * <p> Copyright: Copyright (c) 2003 </p>
 * <p> Company: ����������ͨ�Ƽ����޹�˾ </p>
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
	 * ����task���󣬲���task�����ʱ�����ȴӸ�Map����ȡ��
	 * ��������ڣ���ô�����ݿ��ж�ȡ��
	 * ��task��������޸ĺ�ɾ����������ô��taskMap��ɾ����Ӧ�������
	 * �⼴�����������Դ��ڵ����������µġ�
	 * key/value=task.id()/task��
	 */
	private Map taskMap = new Hashtable();

	/** ������������Task�Ƿ����(Task���ڵı�Ӳ����ΪYtapl_Tasks) */
	private static String SELECT_TASK_BY_ID = "SELECT COUNT(*) FROM Ytapl_Tasks WHERE taskID=?";
	/** ����String��id����Task�Ƿ����(Task���ڵı�Ӳ����ΪYtapl_Tasks) */
	private static String SELECT_TASK_BY_STRID = "SELECT COUNT(*) FROM Ytapl_Tasks WHERE strID=?";
	/** ɾ�������Ӧ�ĵ�λ�״�� */
	private static final String DELETE_FILLSTATE_SQL = "DELETE FROM Ytapl_FillState WHERE taskID=?";
	/** ɾ�������Ӧ�ı��� */
	private static final String DELETE_XSLT_SQL = "DELETE FROM Ytapl_TaskView WHERE taskID=?";
	/** ɾ�������Ӧ�ĵ�λ��ַ��¼ */
	private static final String DELETE_ADDRESS_INFO_SQL = "DELETE FROM Ytapl_AddressInfo WHERE taskID=?";
	/**
	 * �õ���������
	 *
	 * @return ��������
	 * @throws TaskException
	 * @see cn.com.youtong.apollo.task.TaskManager#getAllTasks()
	 */
	public Iterator getAllTasks()
		throws TaskException
	{
		return taskMap.values().iterator();
	}

	/**
	 * �������е����񣬲����浽TaskManager��
	 * @throws TaskException
	 */
	private void loadAllTaskAndCache()
		throws TaskException
	{
		// �Ѿ���cache�е�task id,ƴ��Ϊ'taskID1', 'taskID2'�������ַ���

		Set taskIDSet = taskMap.keySet();
		StringBuffer buff = new StringBuffer();
		for(Iterator taskIDIter = taskIDSet.iterator(); taskIDIter.hasNext(); )
		{
			String taskID = (String) taskIDIter.next();
			buff.append("'").append(taskID).append("'").append(",");
		}
		// ���buff�������ַ�����ôɾ�����һ��","�ַ�
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
			// ��������Ѿ����ˣ���ô���ش����ݿ��ȡ
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
			log.error("��ȡ�����������", e);
			throw new TaskException("��ȡ�����������" + e.getMessage());
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
	 * ������������ʹ�õĽű���
	 * @param taskID �ű�������������ID
	 * @param suitName �ű�������ID
	 * @throws TaskException ����ʧ��
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
					log.info("��ʼ���ű��顰" + suitName + "������Ϊ����" + taskID + "�������нű���");
					task = getTask(taskID, session);
					task.setActiveScriptSuitName(suitName);
					session.update(task);
					tx.commit();
					session.refresh(task);
					log.info("���ű��顰" + suitName + "������Ϊ����" + taskID + "�������нű���ɹ�");
				}
				else
				{
					throw new TaskException("ָ���Ľű��顰" + suitName + "��������");
				}
			}
			else
			{
				throw new TaskException("ָ��������" + taskID + "��������");
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
			String message = "���ű��顰" + suitName + "������Ϊ����" + taskID + "�������нű���ʧ��";
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
		// ���»����ж�Ӧ��task
		putCache(taskID);
	}

	/**
	 * ��������id��������
	 *
	 * @param id ����id
	 * @return ����
	 * @throws TaskException
	 * @see cn.com.youtong.apollo.task.TaskManager#getTaskByID(java.lang.String)
	 */
	public Task getTaskByID(String id)
		throws TaskException
	{
		// ���ػ����е�task
		Task task = (Task) taskMap.get(id);
		if(task == null)
		{
			throw new TaskException("ָ�������񲻴���[taskID=" + id + "]");
		}
		return task;
	}

	/**
	 * �õ�ָ��������
	 * @param taskID ����id
	 * @param session Session����
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
	 * �õ�ָ��������
	 * @param ID ����ID
	 * @param session Session����
	 * @return TaskForm Hibernate����
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
	 * ɾ������
	 * @param ID ����ID
	 * @throws TaskException �����쳣
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
				throw new TaskException("ָ�������񲻴���");
			}

			// log
			if(log.isInfoEnabled())
			{
				log.info("ɾ������[taskID=" + ID + "]");
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

			// ɾ�������ж�Ӧ��task
			removeFromCache(ID);
		}
		catch(Exception e)
		{
			log.error("ɾ���������(ID=" + ID + ")", e);
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
			throw new TaskException("ɾ���������(ID=" + ID + ")", e);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * ɾ���������Ӧ�ĵ�λ�״��.
	 * �˷���û�п�������,�����޸��ڴ˷�������û���ύ�����ݿ�.
	 * @param taskID              ����ID
	 * @param session             Hibernate�Ự
	 */
	private void deleteFillState(String taskID, Session session)
	{
		if(log.isInfoEnabled())
		{
			log.info("ɾ�������Ӧ�ĵ�λ�ϱ�״��[taskID=" + taskID + "]");
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
			String msg = "ɾ�������Ӧ�ĵ�λ�״������[taskID=" + taskID + "]";
			log.error(msg, ex);
			// ingore it
		}
		finally
		{
			SQLUtil.close(stmt);
		}
	}

	/**
	 * ɾ���������Ӧ�ĵ�λ��ַ��Ϣ.
	 * �˷���û�п�������,�����޸��ڴ˷�������û���ύ�����ݿ�.
	 * @param taskID              ����ID
	 * @param session             Hibernate�Ự
	 */
	private void deleteAddressInfo(String taskID, Session session)
	{
		if(log.isInfoEnabled())
		{
			log.info("ɾ�������Ӧ�ĵ�λ��ַ��¼[taskID=" + taskID + "]");
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
			String msg = "ɾ�������Ӧ�ĵ�λ��ַ��¼����[taskID=" + taskID + "]";
			log.error(msg, ex);
			// ingore it
		}
		finally
		{
			SQLUtil.close(stmt);
		}
	}

	/**
	 * ɾ������
	 * @param task ����
	 * @throws TaskException �����쳣
	 * @see cn.com.youtong.apollo.task.TaskManager#deleteTask(cn.com.youtong.apollo.task.Task)
	 */
	public void deleteTask(Task task)
		throws TaskException
	{
		String taskID = task.id();
		deleteTask(taskID);
	}

	/**
	 * ��������
	 * @param xmlInputStream Ҫ���������������xml��
	 * @return �ɹ�����������
	 * @throws TaskException ����ʧ��ʱ�׳�
	 */
	public Task publishTask(InputStream xmlInputStream)
		throws TaskException
	{
		//����������
		DBXMLBuilder dbx = null;
		try
		{
			dbx = new DBXMLBuilder(new InputStreamReader(xmlInputStream, "gb2312"));
		}
		catch(UnsupportedEncodingException ex)
		{
			throw new TaskException(ex);
		}
		//������������ݿ⣬���������ݱ�
		DBXMLDirector.create(dbx);
		// ����XSLT��ʽ��Ϣ
		publishXSLTs(dbx.getTableViews());
		//���»����е��������
		String taskID = dbx.getTaskForm().getID();
		putCache(taskID);

		if(log.isInfoEnabled())
		{
			log.info("��������[taskID=" + taskID + "]");
		}
		return this.getTaskByID(taskID);
	}

	/**
	 * ������ʽ��ϢXSLTs
	 * @param itrTableViews ��ʽ��Ϣ�Ķ����������
	 * �������еĽṹΪ��
	 * ����ArrayList����
	 * ÿ��ArrayList���������������ṹΪ��
	 * 0λ����Integer�͡�tableID
	 * 1λ����Integer��  type
	 * 2λ����String��   xslt�ı�
	 * @throws TaskException ҵ���쳣
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
	 * �ж������Ƿ������ݿ��д���
	 *
	 * @param taskID
	 *            ����id
	 * @param session
	 *            hibernate�Ự
	 * @return ���ڷ���true������false
	 * @throws HibernateException
	 *             ����hibernate�쳣
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
	 * �ж������Ƿ������ݿ��д���
	 *
	 * @param id
	 *            ����id
	 * @param session
	 *            hibernate�Ự
	 * @return ���ڷ���true������false
	 * @throws HibernateException
	 *             ����hibernate�쳣
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
	 * ɾ�����񴴽������д����������������ݵı�
	 *
	 * @param form
	 *            ����
	 * @param con
	 *            ���ݿ�����(�˷���û�йر�����)
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
			// ɾ���ñ�������ڣ�ɾ�������
			//     dropTable(tableName, con);
			//  }

			// ������
			HashMap floatTableInfos = tableInfo.getFloatTables();

			// ɾ��������
			dropFloatTables(floatTableInfos, con);
		}

		// ɾ��������
		dropAttachmentTable(form.getID(), con);
	}

	/**
	 * �������ڸ����У���ô���������������ݿ����е����Ĵ�ű� dropFloatTables()��������ɾ����Щ���ݿ��
	 *
	 * @param tableInfos
	 *            ��������Ϣ
	 * @param con
	 *            ���ݿ�����(�˷���û�йر�)
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
			// ɾ���ñ�
			dropTable(tableName, con);
		}

	}

	/**
	 * ɾ����
	 *
	 * @param tableName
	 *            ɾ���ı���
	 * @param con
	 *            ���ݿ�����(�˷���û�йر�����)
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
				log.info("ɾ�����" + tableName);
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
	 * ɾ��������
	 *
	 * @param taskID ����id
	 * @param conn ���ݿ�����(�˷���û�йر�����)
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
	 * ����XSLT�ļ���������ڣ�����
	 * @param xmlInputStream ����XML��
	 * @return Task �ɹ������Ľű���
	 * @throws TaskException �����ű���ʧ��
	 */
	public Task publishXSLT(InputStream xmlInputStream)
		throws TaskException
	{
		//���������������������ļ��Ի����Ҫ�ĸ��ֶ���
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
				throw new TaskException("ָ��������" + taskForm.getID() + "��������");
			}

			//ɾ����������ԭ������ʽ��Ϣ
			Iterator itrTables = getTaskByID(taskForm.getID()).getAllTables();
			HashMap tempTableMap = new HashMap();
			while(itrTables.hasNext())
			{
				DBTable table = (DBTable) itrTables.next();
				tempTableMap.put(table.id(), table.getTableID());
				session.delete("select xslt from TableViewForm xslt where xslt.table.tableID = ?", table.getTableID(), Hibernate.INTEGER);
			}

			//����������Ϣ
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
					throw new TaskException("������ʽ�ı�\"" + strTableID + "\"������");
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
			throw new TaskException("����ʧ��", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		//���»����е��������
		putCache(taskForm.getID());
		return getTaskByID(taskForm.getID());
	}

	/**
	 * ɾ��XSLT
	 * @param taskID ����ID��
	 * @param viewID viewID
	 * @throws TaskException ɾ��ʧ��
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
				log.info("ɾ������[taskID=" + taskID + "] �ű�[viewID=" + viewID);
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
			throw new TaskException("ɾ��ʧ��");
		}
		finally
		{
			HibernateUtil.close(session);
		}
		//���»����е��������
		putCache(taskID);
	}

	/**
	 * ���������XSLT�ļ�
	 * @param viewID tableViewID��
	 * @return TaskView����
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
			throw new TaskException("û�ҵ�XSLT����");
		}
		finally
		{
			HibernateUtil.close(session);
		}
		return result;
	}

	/**
	 * �����ű��飬������ڣ�����
	 * @param taskID ����ID
	 * @param xmlInputStream �ű���xml��
	 * @return �ɹ������Ľű���
	 * @throws TaskException �����ű���ʧ��
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
				throw new TaskException("ָ��������" + taskID + "��������");
			}
			TaskForm task = getTask(taskID, session);
			//log.info("��ʼ�����ű��顰" + xmlSuit.getName() + "��");
			if(isScriptSuitDefined(taskID, xmlSuit.getName(), session))
			{
				result = new DBScriptSuit(updateScriptSuit(taskID, xmlSuit, session));
			}
			else
			{
				result = new DBScriptSuit(createScriptSuit(taskID, xmlSuit, session));
			}
			//log.info("�����ű��顰" + xmlSuit.getName() + "���ɹ�");
			tx.commit();

			// log
			if(log.isInfoEnabled())
			{
				log.info("��������[taskID=" + taskID + "] �ű���[suitName=" + xmlSuit.getName() + "]");
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
			String message = "�����ű���ʧ��";
			log.error(message, ex);
			throw new TaskException(message, ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		// ���»����ж�Ӧ��task
		putCache(taskID);
		return result;
	}

	/**
	 * ���������ӻ�����ɾ����ͬʱɾ�������������Ӧ��modelManager
	 * @param taskID ����ID��
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
			//ɾ��������task��Ӧ��ModelManager
			ModelManagerFactory factory = (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName());
			factory.deleteModelManager(taskID);
		}
		catch(Exception ex)
		{
			log.error("���»���ʧ��", ex);
			throw new TaskException("���»���ʧ��", ex);
		}
	}

	/**
	 * �����������ӵ����棬ͬʱ���»����������Ӧ��modelManager
	 * @param taskID ����
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

			//���»�����task��Ӧ��ModelManager
			ModelManagerFactory factory = (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName());
			factory.updateModelManager(taskID);
		}
		catch(Exception ex)
		{
			log.error("���»���ʧ��", ex);
			throw new TaskException("���»���ʧ��", ex);
		}
		finally
		{
			HibernateUtil.close(session);
		}
	}

	/**
	 * ����XSLT�ļ�
	 * @param tableID ��ID
	 * @param type XSLT����;
	 * @param xsltReader XSLT������Դ
	 * @param session Hibernate����
	 * @return �ļ�����
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
	 * ����XSLT�ļ�
	 * @param tableID ��ID
	 * @param type XSLT��;
	 * @param xsltReader ����Դ
	 * @param session Hibernate����
	 * @return �ļ�����
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
	 * ���½ű���
	 * @param taskID ����ID
	 * @param xmlSuit �ű����xml����
	 * @param session Hibernate����
	 * @return ���º�Ľű�����
	 * @throws HibernateException
	 */
	private ScriptSuitForm updateScriptSuit(String taskID, cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Session session)
		throws HibernateException
	{
		ScriptSuitForm suit = getScriptSuitForm(taskID, xmlSuit.getName(), session);
		//ɾ��ԭ��scripts
		deleteScripts(suit, session);
		//�����µ�scripts
		List scripts = createScripts(xmlSuit, suit.getSuitID(), session);

		//����ScriptSuit
		suit.setDateModified(new java.util.Date());
		suit.setExecSeqence(getExecSequence(xmlSuit));
		suit.setMemo(xmlSuit.getMemo());
		suit.setName(xmlSuit.getName());
		suit.setScripts(new HashSet(scripts));
		session.update(suit);

		if(log.isInfoEnabled())
		{
			log.info("��������[taskID=" + taskID + "] �ű�[suitName=" + xmlSuit.getName() + "]");
		}

		return suit;
	}

	/**
	 * ɾ���ű���Ľű�
	 * @param suit �ű���
	 * @param session Session����
	 * @throws HibernateException
	 */
	private void deleteScripts(ScriptSuitForm suit, Session session)
		throws HibernateException
	{
		session.delete("select script from ScriptForm script where script.suitID =?", suit.getSuitID(), Hibernate.INTEGER);
	}

	/**
	 * �õ�ָ���Ľű���
	 * @param taskID ����ID
	 * @param name �ű�������
	 * @param session Session����
	 * @return ScriptSuitFormָ���Ľű���
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
	 * �õ�ָ���Ľű���
	 * @param taskID ����ID
	 * @param name �ű�������
	 * @param session Session����
	 * @return ָ���Ľű���
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
	 * ���½ű���
	 * @param taskID ����ID
	 * @param xmlSuit �ű����xml����
	 * @param session Hibernate����
	 * @return ���º�Ľű�����
	 * @throws HibernateException
	 */
	private ScriptSuitForm createScriptSuit(String taskID, cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Session session)
		throws HibernateException
	{
		TaskForm task = getTask(taskID, session);
		ScriptSuitForm suit = new ScriptSuitForm();
		//����ScriptSuit
		suit.setDateCreated(new java.util.Date());
		suit.setDateModified(new java.util.Date());
		suit.setExecSeqence(getExecSequence(xmlSuit));
		suit.setMemo(xmlSuit.getMemo());
		suit.setName(xmlSuit.getName());
		suit.setSuitID(HibernateUtil.getNextScriptSuitID());
		suit.setTaskID(task.getTaskID());
		session.save(suit);
		//����scripts
		List scripts = createScripts(xmlSuit, suit.getSuitID(), session);

		//����ScriptSuit
		suit.setScripts(new HashSet(scripts));
		session.update(suit);

		return suit;
	}

	/**
	 * �õ��ű�ִ��˳���ַ������ַ���Ϊ�ű��������У�ÿ������֮���ԡ�,������
	 * @param xmlSuit �ű���xml����
	 * @return �ű�ִ��˳���ַ���
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
	 * �����ű�ScriptForm����
	 * @param xmlSuit �ű���xml����
	 * @param suitID �ű���ID
	 * @param session Session����
	 * @return �ű�ScriptForm����
	 * @throws HibernateException
	 */
	private List createScripts(cn.com.youtong.apollo.task.xml.ScriptSuit xmlSuit, Integer suitID, Session session)
		throws HibernateException
	{
		List result = new LinkedList();
		//�����˽ű�
		if(xmlSuit.getAuditCrossTable() != null)
		{
			result.add(createScriptForm(xmlSuit.getAuditCrossTable(), suitID, session));
		}
		//�������ű�
		if(xmlSuit.getCalculateCrossTable() != null)
		{
			result.add(createScriptForm(xmlSuit.getCalculateCrossTable(), suitID, session));
		}
		//������˽ű�
		cn.com.youtong.apollo.task.xml.AuditInTable[] auditInTables = xmlSuit.getAuditInTable();
		for(int i = 0; i < auditInTables.length; i++)
		{
			result.add(createScriptForm(auditInTables[i], suitID, session));
		}
		//��������ű�
		cn.com.youtong.apollo.task.xml.CalculateInTable[] calculateInTables = xmlSuit.getCalculateInTable();
		for(int i = 0; i < calculateInTables.length; i++)
		{
			result.add(createScriptForm(calculateInTables[i], suitID, session));
		}
		return result;
	}

	/**
	 * �����ű�Form����
	 * @param auditCrossTable �����˽ű�
	 * @param suitID �ű���ID
	 * @param session Session����
	 * @return �ű�Form����
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
	 * �����ű�Form����
	 * @param auditInTable ������˽ű�
	 * @param suitID �ű���ID
	 * @param session Session����
	 * @return �ű�Form����
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
	 * �����ű�Form����
	 * @param calculateCrossTable �������ű�
	 * @param suitID �ű���ID
	 * @param session Session����
	 * @return �ű�Form����
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
	 * �����ű�Form����
	 * @param calculateInTable ��������ű�
	 * @param suitID �ű���ID
	 * @param session Session����
	 * @return �ű�Form����
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
	 * ɾ���ű���
	 * @param taskID �ű�������������ID
	 * @param suitName �ű�������ID
	 * @throws TaskException ɾ���ű���ʧ��
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
				//���ɾ���Ľű�������������нű��飬��������
				if(task.getActiveScriptSuitName() != null && task.getActiveScriptSuitName().equals(suitName))
				{
					task.setActiveScriptSuitName(null);
					session.update(task);
				}
				//log.info("ɾ���ű��顰" + suitName + "���ɹ�");
				tx.commit();

				// log
				if(log.isInfoEnabled())
				{
					log.info("ɾ������[taskID=" + taskID + "] �ű���[suitName=" + suitName + "]");
				}
				session.refresh(task);
			}
			else
			{
				throw new TaskException("ָ���Ľű��鲻����");
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
			String message = "ɾ���ű��顰" + suitName + "��ʧ��";
			log.error(message, e);
			throw new TaskException(message, e);
		}
		finally
		{
			HibernateUtil.close(session);
		}
		// ���»����ж�Ӧ��task
		putCache(taskID);
	}

	/**
	 * ɾ���ű���
	 * @param taskID �ű�������������ID
	 * @param suitName �ű�������
	 * @param session Session����
	 * @throws HibernateException
	 */
	private void deleteScriptSuit(Integer taskID, String suitName, Session session)
		throws HibernateException
	{
		ScriptSuitForm suit = getScriptSuitForm(taskID, suitName, session);
		//ɾ���ű����е����нű�
		deleteScripts(suit, session);
		//ɾ���ű���
		session.delete(suit);
	}

	/**
	 * �ж�ָ���Ľű����Ƿ����
	 * @param taskID �ű�������������ID
	 * @param suitName �ű��������
	 * @param session Session����
	 * @return boolean ���ڷ���true, ���򷵻�false
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
	 * �ж�ָ����XSLT�Ƿ����
	 * @param tableID ��ID
	 * @param type xslt��;
	 * @param session Session����
	 * @return boolean ���ڷ���true, ���򷵻�false
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
	 * �ж�ָ����XSLT�Ƿ����
	 * @param viewID viewID
	 * @param session Session����
	 * @return boolean ���ڷ���true, ���򷵻�false
	 * @throws HibernateException
	 */
	private boolean isXSLTDefined(Integer viewID, Session session)
		throws HibernateException
	{
		return((Integer) session.iterate("SELECT COUNT(*) from TableViewForm as form WHERE form.viewID = ?", viewID, Hibernate.INTEGER).next()).intValue() > 0;
	}

	/**
	 * �����ű���
	 * @param taskID �ű�������������ID
	 * @param suitName �ű�������ID
	 * @param out �������
	 * @throws TaskException ����ʧ��
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
				log.info("��ʼ�����ű��顰" + suitName + "��");
				ScriptSuitForm suit = getScriptSuitForm(taskID, suitName, session);
				getCastorScriptSuit(suit, session).marshal(new OutputStreamWriter(out, "gb2312"));
				log.info("ɾ�������ű��顰" + suitName + "���ɹ�");
			}
			else
			{
				throw new TaskException("ָ���Ľű��鲻����");
			}
		}
		catch(Exception e)
		{
			String message = "�����ű��顰" + suitName + "��ʧ��";
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
	 * �õ��ű����castor����
	 * @param suit �ű���
	 * @param session Session����
	 * @return �ű����castor����
	 * @throws HibernateException
	 */
	private cn.com.youtong.apollo.task.xml.ScriptSuit getCastorScriptSuit(ScriptSuitForm suit, Session session)
		throws HibernateException
	{
		try
		{
			cn.com.youtong.apollo.task.xml.ScriptSuit result = new cn.com.youtong.apollo.task.xml.ScriptSuit();
			//��������
			result.setDateModified(suit.getDateModified());
			result.setMemo(suit.getMemo());
			result.setName(suit.getName());
			//�ű�
			for(Iterator itr = suit.getScripts().iterator(); itr.hasNext(); )
			{
				ScriptForm script = (ScriptForm) itr.next();
				switch(script.getType())
				{
					//�����˽ű�
					case Script.AUDIT_CROSS_TABLE:
						AuditCrossTable auditCrossTable = new AuditCrossTable();
						auditCrossTable.setContent(Convertor.Clob2String(script.getContent()));
						auditCrossTable.setName(script.getName());
						result.setAuditCrossTable(auditCrossTable);
						break;
						//������˽ű�
					case Script.AUDIT_IN_TABLE:
						AuditInTable auditInTable = new AuditInTable();
						auditInTable.setContent(Convertor.Clob2String(script.getContent()));
						auditInTable.setName(script.getName());
						result.addAuditInTable(auditInTable);
						break;
						//�������ű�
					case Script.CALCULATE_CROSS_TABLE:
						CalculateCrossTable calculateCrossTable = new CalculateCrossTable();
						calculateCrossTable.setContent(Convertor.Clob2String(script.getContent()));
						calculateCrossTable.setName(script.getName());
						result.setCalculateCrossTable(calculateCrossTable);
						break;
						//��������ű�
					case Script.CALCULATE_IN_TABLE:
						CalculateInTable calculateInTable = new CalculateInTable();
						calculateInTable.setContent(Convertor.Clob2String(script.getContent()));
						calculateInTable.setName(script.getName());
						result.addCalculateInTable(calculateInTable);
						break;
				}
			}
			//�ű�ִ��˳��
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