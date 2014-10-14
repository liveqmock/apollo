/*
 * Created on 2003-11-5
 */
package cn.com.youtong.apollo.data.db;

import java.sql.*;
import java.util.*;

import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.form.*;
import cn.com.youtong.apollo.task.*;
import net.sf.hibernate.*;

import cn.com.youtong.apollo.services.*;
import org.apache.fulcrum.factory.FactoryException;
import org.apache.commons.logging.*;


/**
 * xml���ͽṹ�����ݿ�ƽ���ϣ����ڲ��졣
 * �����ڱ����������ݵ�ʱ����Ҫ��Ӧ�ı������Ϣ���Ա�������ݡ�
 * �õ������ݿ�����Ϣ���ֶ����������������Ƿ����ִ�Сд�����صĶ��Ǵ�д��
 *
 * DBModelHelper�࣬����ͨ���ṩString�͵�taskID����Task����
 * ���� Task �����ϱ����������ݿ��еı��淽ʽ����
 *
 * @see cn.com.youtong.apollo.common.sql.NameGenerator
 * @author wjb
 */
public class DBModelHelper
{
	private Log log = LogFactory.getLog(DBModelHelper.class);

	/**
	 * ��������id��ȡ������id����Ӧ�Ĵ洢���ݱ����Ϣ
	 * @param taskID	����id
	 * @return			�洢���ݱ����Ϣ
	 * @throws TaskException
	 */
	public HashMap getDBTableInfos(String taskID)
		throws TaskException
	{
		// Create TaskManager
		TaskManager taskMng = null;
		try
		{
			taskMng = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		}
		catch(FactoryException ex)
		{
			log.error("", ex);
			throw new TaskException(ex.getMessage());
		}

		// �ҵ�����
		Task task = taskMng.getTaskByID(taskID);
		return getDBTableInfos(task);
	}

	/**
	 * ��������ȡ������Ӧ�Ĵ洢���ݱ����Ϣ
	 * @param task		����
	 * @return			�洢���ݱ����Ϣ��HashMap��key/valueֵ�� = id/TableInfo��
	 * 					TableInfo�����unitID,taskID����Ϣ
	 * @throws TaskException
	 */
	public HashMap getDBTableInfos(Task task)
		throws TaskException
	{
		HashMap infos = new HashMap();

		// ����ID
		String taskID = task.id();

		// �û������
		// UnitMetaTable unitMetaTable = task.getUnitMetaTable();
		// String unitMetaTableID = unitMetaTable.id();

		Iterator allTable = task.getAllTables();

		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			for(; allTable.hasNext(); )
			{
				Table table = (Table) allTable.next();
				String tableID = table.id();

				TableInfo info = getTableInfo(session, table, taskID);

				infos.put(tableID, info);
			}
		}
		catch(Exception e)
		{
			throw new TaskException("��ȡ��������Ϣ����");
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

		return infos;
	}

	/**
	 * �õ����������Ϣ
	 * @param session
	 * @param table
	 * @param taskID
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private TableInfo getTableInfo(Session session, Table table, String taskID)
		throws HibernateException, SQLException
	{
		TableInfo info = new TableInfo();

		HashMap cellInfos = new HashMap();
		HashMap floatTables = new HashMap();

		// ��ID
		String tableID = table.id();

		String tableName = NameGenerator.generateDataTableName(taskID, tableID);
		info.setTableName(tableName);

		boolean hasNormalCells = false; // �Ƿ�ȫ���Ǹ����У�û�������̶���
		Iterator rows = table.getRows();
		for(; rows.hasNext(); )
		{
			Row row = (Row) rows.next();
			if(row.getFlag(Row.FLAG_FLOAT_ROW))
			{
				TableInfo floatTableInfo = new TableInfo();

				String floatTableName = NameGenerator.generateFloatDataTableName(taskID, tableID, row.id());
				floatTableInfo.setTableName(floatTableName);

				floatTableInfo.setCellIndex(descTable(session, floatTableName)[0]);
				floatTableInfo.setCellType(descTable(session, floatTableName)[1]);

				floatTables.put(row.id(), floatTableInfo);
			}
			else
			{
				hasNormalCells = true;
			}
		}

		if (hasNormalCells)
		{
			info.setExist(true);
			info.setCellIndex(descTable(session, tableName)[0]);
			info.setCellType(descTable(session, tableName)[1]);
		}
		else
		{
			info.setExist(false);
			info.setCellIndex( new HashMap() );
			info.setCellType( new HashMap() );
		}

		info.setFloatTables(floatTables);
		return info;
	}

	/**
	 * ���ر�����������˳��Map���ͱ����������SQL����
	 * @param session
	 * @param tableName
	 * @return
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private Map[] descTable(Session session, String tableName)
		throws HibernateException, SQLException
	{
		//Vector v = new Vector();
		Map cellIndexMap = new Hashtable();
		Map cellTypeMap = new Hashtable();
		//		------------------ �õ����ݿ���ʵ�ʵı����ʽ
		Connection con = session.connection();
		PreparedStatement ps = null;

		try
		{
			ps = con.prepareStatement("SELECT * FROM " + tableName + " WHERE unitID = null");
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			for(int i = 1; i <= rsmd.getColumnCount(); i++)
			{
				String name = rsmd.getColumnName(i);
				int type = rsmd.getColumnType(i);
				name = name.toUpperCase();
				cellIndexMap.put(name, new Integer(i));
				cellTypeMap.put(name, new Integer(type));
			}
		}
		catch(SQLException sqle)
		{
			throw sqle;
		}
		finally
		{
			if(ps != null)
			{
				try
				{
					ps.close();
				}
				catch(SQLException e)
				{}
			}
		}

		return new Map[] {cellIndexMap, cellTypeMap};
	}
}
