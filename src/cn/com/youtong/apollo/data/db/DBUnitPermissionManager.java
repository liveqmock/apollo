package cn.com.youtong.apollo.data.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.*;
import org.exolab.castor.xml.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.form.*;
import cn.com.youtong.apollo.data.xml.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.*;
import cn.com.youtong.apollo.usermanager.*;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;

/**
 * UnitPermissionManager的数据库实现
 */
class DBUnitPermissionManager
	implements UnitPermissionManager
{
	/** DBUnitTreeManager实例 */
	private DBUnitTreeManager treeManager;

	private Log log = LogFactory.getLog(this.getClass());
	;

	/** 报表任务定义 */
	private Task task;

	private static String SQL_DELETE_PERMISSION = "delete from YTAPL_UnitPermissions where groupID = ? and taskID = ? and unitID = ?";

	private static String SQL_IS_GROUP_DEFINED =
		"select * from YTAPL_Groups where groupID = ?";

	private static String SQL_IS_PERMISSION_DEFINED = "select * from YTAPL_UnitPermissions where groupID = ? and taskID = ? and unitID = ?";

	private static String SQL_UPDATE_PERMISSION = "update YTAPL_UnitPermissions set permission = ? where groupID = ? and taskID = ? and unitID = ?";

	private static String SQL_INSERT_PERMISSION = "insert into YTAPL_UnitPermissions (permission,groupID,taskID,unitID) values (?,?,?,?)";

	public DBUnitPermissionManager(Task task, DBUnitTreeManager treeManager)
	{
		this.task = task;
		this.treeManager = treeManager;
	}

	/**
	 * 初始化权限
	 * @param in 输入文件流
	 * @throws ModelException
	 */
	public void initFromFile(InputStream in) throws ModelException
	{
		Permissions permissions = null;
		try
		{
			InputStreamReader reader = new InputStreamReader(in, "gb2312");
			permissions = Permissions.unmarshal(reader);
		}
		catch (Exception ex)
		{
			log.error("设置系统参数失败，无效的XML文件 ", ex);
			throw new ModelException(ex);
		}

		if (permissions == null)
		{
			throw new ModelException("设置参数失败，请检查输入文件！　");
		}

		Permission[] arrayPermission = permissions.getPermission();
		for (int i = 0; i < arrayPermission.length; i++)
		{
			Permission tempPermission = arrayPermission[i];

			Group group = null;
			try
			{
				UserManager userManager = ( (UserManagerFactory) Factory.
										   getInstance(UserManagerFactory.class.
					getName())).createUserManager();
				group = userManager.getGroupByName(tempPermission.getGroupName());
			}
			catch (UserManagerException ex1)
			{
				log.error("初始化权限时失败：指定的组不存在 ", ex1);
				throw new ModelException("指定的组不存在 ", ex1);
			}
			catch (FactoryException ex1)
			{
				log.error(ex1);
				throw new ModelException(ex1);
			}

			//保证单位存在
			UnitTreeNode unit = treeManager.getUnitTree(tempPermission.
				getUnitID());
            if(unit==null)
            {
            	log.error("初始化权限时失败：指定的单位不存在 单位号:" +tempPermission.getUnitID());
            	throw new ModelException("初始化权限时失败：指定的单位不存在 单位号:" +tempPermission.getUnitID());
            }
            else
            {
			setUnitPermission(group.getGroupID(),
							  unit.id(),
							  new UnitPermission(tempPermission.getPermit()));
            }
		}
	}

	/**
	 * 给组分配权限
	 * 分配失败则抛出异常
	 * @param groupID 组的ID号
	 * @param unitID 单位ID号
	 * @param right  权限对象 ，权限对象不能为空，可以 new UnitPermission();得到该对象，这时的权限为默认最小权限
	 * @throws ModelException
	 */
	public void setUnitPermission(Integer groupID, String unitID,
								  UnitPermission right) throws ModelException
	{

		Session session = null;
		Transaction tx = null;
		PreparedStatement updatePs = null;
		PreparedStatement insertPs = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			//如果取消了所有权限，则在数据库中删除对应的记录，并且不再继续执行
			if (right.intValue() == 0)
			{
				deleteUnitPermission(groupID, unitID);
				return;
			}

			Connection con = session.connection();

			//判断组是否存在
			if (!isGroupDefined(groupID, con))
			{
				log.error("分配权限时指定的组不存在！组ID：" + groupID);
				throw new ModelException("分配权限时指定的组不存在！组ID：" + groupID);
			}

			//遍历树，并分配权限
			if (isUnitPermissionDefined(groupID, unitID, con))
			{
				updatePs = con.prepareStatement(SQL_UPDATE_PERMISSION);
				updatePs.setInt(1, right.intValue());
				updatePs.setInt(2, groupID.intValue());
				updatePs.setString(3, task.id());
				updatePs.setString(4, unitID);
				updatePs.execute();
				updatePs.close();
			}
			else
			{
				insertPs = con.prepareStatement(SQL_INSERT_PERMISSION);
				insertPs.setInt(1, right.intValue());
				insertPs.setInt(2, groupID.intValue());
				insertPs.setString(3, task.id());
				insertPs.setString(4, unitID);
				insertPs.execute();
				insertPs.close();
			}
			tx.commit();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new ModelException("创建用户组失败");
		}
		finally
		{
			if (updatePs != null)
			{
				try
				{
					updatePs.close();
				}
				catch (SQLException ex2)
				{
				}
			}
			if (insertPs != null)
			{
				try
				{
					insertPs.close();
				}
				catch (SQLException ex2)
				{
				}
			}
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * 判断组是否存在
	 * @param groupID 组ID
	 * @param con 集合
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean isGroupDefined(Integer groupID, Connection con) throws
		SQLException
	{
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(SQL_IS_GROUP_DEFINED);
			ps.setInt(1, groupID.intValue());
			rs = ps.executeQuery();
			if (rs.next())
			{
				result = true;
			}
			ps.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			if (ps != null)
			{
				ps.close();
			}
		}

		return result;
	}

	/**
	 * 取消已经分配给组的单位权限
	 * 取消失败则抛出异常
	 * @param groupID 组ID号
	 * @param unitID 单位ID
	 * @throws ModelException
	 */
	public void deleteUnitPermission(Integer groupID,
									 String unitID) throws ModelException
	{
		Session session = null;
		Transaction tx = null;
		PreparedStatement ps = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection con = session.connection();
			ps = con.prepareStatement(SQL_DELETE_PERMISSION);
			ps.setInt(1, groupID.intValue());
			ps.setString(2, task.id());
			ps.setString(3, unitID);
			ps.execute();
			ps.close();
			tx.commit();
		}
		catch (SQLException ex)
		{
			log.info("删除权限出现问题：" + ex.getMessage());
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}
		catch (HibernateException ex)
		{
			log.error("", ex);
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex1)
			{
			}
			throw new ModelException("创建用户组失败");
		}
		finally
		{
			if (ps != null)
			{
				try
				{
					ps.close();
				}
				catch (SQLException ex3)
				{
				}
			}
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
	}

	/**
	 * 直接查询组的权限
	 * @param groupID 组ID号
	 * @param unitID 单位ID号
	 * @param permission 权限标识
	 * @param session Hibernate对象
	 * @return boolean型变量，true or false
	 * @throws HibernateException
	 */
	private boolean getPermission(Integer groupID, String unitID,
								  int permission, Session session) throws
		HibernateException
	{
		try
		{
			UnitTreeNode unit = treeManager.getUnitTree(unitID);

			while (unit != null)
			{
				UnitPermission unitPermission = getUnitPermission(groupID,
					unit.id(),
					session);
				if (unitPermission != null)
				{
					//在当前节点找到了权限分配信息
					return unitPermission.getPermission(permission);
				}
				else
				{
					//沿父节点查找
					unit = unit.getParent();
				}
			}

			return false;
		}
		catch (ModelException ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * 直接查询组的权限
	 * @param groupID 组ID号
	 * @param unitID 单位ID号
	 * @param permission 权限标识
	 * @return boolean型变量，true or false
	 * @throws ModelException
	 */
	public boolean getPermission(Integer groupID, String unitID,
								 int permission) throws ModelException
	{
		String s = "Get Permission: \n"
			+ "\n groupID ==>" + groupID
			+ "\n taskid ==> " + task.id()
			+ "\n  unitID ==>" + unitID
			+ "\n  permission ==>" + permission;
		log.debug(s);

		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			return getPermission(groupID, unitID, permission, session);
		}
		catch (Exception ex)
		{
			String message = "不能得到权限";
			log.info(message, ex);
			throw new ModelException(message, ex);
		}
		finally
		{
			try
			{
				if (session != null)
				{
					session.close();
				}
			}
			catch (HibernateException ex2)
			{
			}
		}
	}

	/**
	 * 获得组中指定单位的权限
	 * 若在该组中没有指定该单位的权限，即不存在该条记录，则返回所有权限均为 否
	 * @param groupID 组ID号
	 * @param unitID 单位ID号
	 * @param session Hibernate对象
	 * @return 权限对象，如果没有权限信息，返回null
	 * @throws HibernateException
	 */
	private UnitPermission getUnitPermission(Integer groupID,
											 String unitID, Session session) throws
		HibernateException
	{
		UnitPermission result = new UnitPermission();
		List list = session.find("select unitPerm from UnitPermissionForm unitPerm where unitPerm.comp_id.groupID = ? and unitPerm.comp_id.taskID = ? and unitPerm.comp_id.unitID = ?",
								 new Object[]
								 {groupID, task.id(), unitID}
								 , new Type[]
								 {Hibernate.INTEGER, Hibernate.STRING,
								 Hibernate.STRING});
		if (list.size() == 0)
		{
			return null;
		}
		else
		{
			UnitPermissionForm unitPerm = (UnitPermissionForm) list.iterator().
				next();
			return new UnitPermission(unitPerm.getPermission());
		}
	}

	/**
	 * 根据给定的单位ID返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param unitID 单位ID号
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(String unitID) throws
		ModelException
	{

		Collection result = new ArrayList();

		Session session = null;

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			result = getUnitAssignmentInfo(unitID, session);
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
			throw new ModelException("查找失败：" + ex.getMessage());
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
		return result;
	}

	/**
	 * 根据给定的组ID返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param groupID 组ID号
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(Integer groupID) throws
		ModelException
	{
		Collection result = new ArrayList();

		Session session = null;

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			result = getUnitAssignmentInfo(groupID, session);
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
			throw new ModelException("查找失败：" + ex.getMessage());
		}
		finally
		{
			if (session != null)
			{
				try
				{
					session.close();
				}
				catch (HibernateException ex2)
				{
				}
			}
		}
		return result;
	}

	/**
	 * 根据给定的单位代码返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param unitID 单位ID号
	 * @param session Session
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	private Collection getUnitAssignmentInfo(String unitID,
											 Session session) throws
		ModelException
	{

		Collection result = new ArrayList();

		try
		{
			//查询
			List queryResult = session.find("select unitPerm from UnitPermissionForm unitPerm where unitPerm.comp_id.taskID = ? and unitPerm.comp_id.unitID = ?",
											new Object[]
											{task.id(), unitID}
											, new Type[]
											{Hibernate.STRING, Hibernate.STRING});

			Iterator itrResult = queryResult.iterator();
			while (itrResult.hasNext())
			{
				UnitPermissionForm tempU = (UnitPermissionForm) itrResult.next();

				UserManagerFactory u = (UserManagerFactory) Factory.getInstance(
					UserManagerFactory.class.getName());
				UserManager userManager = u.createUserManager();

				//设置UnitAssignment值对象
				String[] tempUnitID =
					{
					tempU.getComp_id().getUnitID()};
				Iterator itr = treeManager.getUnits(tempUnitID).iterator();
				DBUnitTreeNode node = (DBUnitTreeNode) itr.next();
				UnitAssignment unitAssignment = new UnitAssignment(tempU.
					getComp_id().getUnitID(),
					node.getUnitName(),
					userManager.getGroupByID(tempU.getComp_id().getGroupID()),
					new UnitPermission(tempU.
									   getPermission()));

				//添加到结果中
				result.add(unitAssignment);
			}
		}
		catch (FactoryException ex)
		{
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
			throw new ModelException("查找失败：" + ex.getMessage());
		}
		catch (UserManagerException uex)
		{
			uex.printStackTrace();
			throw new ModelException(uex.getMessage());
		}

		return result;
	}

	/**
	 * 根据给定的单位代码返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param unitID 单位ID号
	 * @param session Session
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	private Collection getUnitAssignmentInfo(Integer groupID,
											 Session session) throws
		ModelException
	{
		Collection result = new ArrayList();

		try
		{
			//查询
			List queryResult = session.find("select unitPerm from UnitPermissionForm unitPerm where unitPerm.comp_id.taskID = ? and unitPerm.comp_id.groupID = ?",
											new Object[]
											{task.id(), groupID}
											, new Type[]
											{Hibernate.STRING,
											Hibernate.INTEGER});

			Iterator itrResult = queryResult.iterator();
			while (itrResult.hasNext())
			{
				UnitPermissionForm tempU = (UnitPermissionForm) itrResult.next();

				UserManagerFactory u = (UserManagerFactory) Factory.getInstance(
					UserManagerFactory.class.getName());
				UserManager userManager = u.createUserManager();

				//设置UnitAssignment值对象
				String[] tempUnitID =
					{
					tempU.getComp_id().getUnitID()};
				Iterator itr = treeManager.getUnits(tempUnitID).iterator();
				if(itr.hasNext()){
					DBUnitTreeNode node = (DBUnitTreeNode) itr.next();
					UnitAssignment unitAssignment = new UnitAssignment(tempU.
						getComp_id().getUnitID(),
						node.getUnitName(),
						userManager.getGroupByID(tempU.getComp_id().getGroupID()),
						new UnitPermission(tempU.
										   getPermission()));
					//添加到结果中
					result.add(unitAssignment);
				}
			}
		}
		catch (FactoryException ex)
		{
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}
		catch (HibernateException ex)
		{
			ex.printStackTrace();
			throw new ModelException("查找失败：" + ex.getMessage());
		}
		catch (UserManagerException uex)
		{
			uex.printStackTrace();
			throw new ModelException(uex.getMessage());
		}
		return result;
	}

	/**
	 * 判断该单位的权限是否已经存在
	 * @param groupID 组ID号
	 * @param unitID 单位ID号
	 * @param con 集合
	 * @return boolean
	 * @throws SQLException
	 */
	private boolean isUnitPermissionDefined(Integer groupID,
											String unitID, Connection con) throws
		SQLException
	{
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(SQL_IS_PERMISSION_DEFINED);
			ps.setInt(1, groupID.intValue());
			ps.setString(2, task.id());
			ps.setString(3, unitID);
			rs = ps.executeQuery();
			if (rs.next())
			{
				result = true;
			}
			ps.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLException(ex.getMessage());
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			if (ps != null)
			{
				ps.close();
			}
		}
		return result;
	}

}