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
 * xml树型结构和数据库平面结合，存在差异。
 * 所有在保存任务数据的时候，需要对应的表格定义信息，以便插入数据。
 * 得到的数据库表格信息，字段名不管在数据中是否区分大小写，返回的都是大写。
 *
 * DBModelHelper类，可以通过提供String型的taskID或者Task对象，
 * 返回 Task 任务上报数据在数据库中的保存方式定义
 *
 * @see cn.com.youtong.apollo.common.sql.NameGenerator
 * @author wjb
 */
public class DBModelHelper
{
	private Log log = LogFactory.getLog(DBModelHelper.class);

	/**
	 * 根据任务id，取得任务id所对应的存储数据表格信息
	 * @param taskID	任务id
	 * @return			存储数据表格信息
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

		// 找到任务
		Task task = taskMng.getTaskByID(taskID);
		return getDBTableInfos(task);
	}

	/**
	 * 根据任务，取得所对应的存储数据表格信息
	 * @param task		任务
	 * @return			存储数据表格信息；HashMap，key/value值对 = id/TableInfo；
	 * 					TableInfo里包含unitID,taskID等信息
	 * @throws TaskException
	 */
	public HashMap getDBTableInfos(Task task)
		throws TaskException
	{
		HashMap infos = new HashMap();

		// 任务ID
		String taskID = task.id();

		// 用户封面表
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
			throw new TaskException("读取任务定义信息出错");
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
	 * 得到单个表格信息
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

		// 表ID
		String tableID = table.id();

		String tableName = NameGenerator.generateDataTableName(taskID, tableID);
		info.setTableName(tableName);

		boolean hasNormalCells = false; // 是否全部是浮动行，没有其他固定行
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
	 * 返回表格的列名和列顺序Map，和表格列名和列SQL类型
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
		//		------------------ 得到数据库中实际的表格样式
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
