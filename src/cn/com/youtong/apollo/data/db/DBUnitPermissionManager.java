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
 * UnitPermissionManager�����ݿ�ʵ��
 */
class DBUnitPermissionManager
	implements UnitPermissionManager
{
	/** DBUnitTreeManagerʵ�� */
	private DBUnitTreeManager treeManager;

	private Log log = LogFactory.getLog(this.getClass());
	;

	/** ���������� */
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
	 * ��ʼ��Ȩ��
	 * @param in �����ļ���
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
			log.error("����ϵͳ����ʧ�ܣ���Ч��XML�ļ� ", ex);
			throw new ModelException(ex);
		}

		if (permissions == null)
		{
			throw new ModelException("���ò���ʧ�ܣ����������ļ�����");
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
				log.error("��ʼ��Ȩ��ʱʧ�ܣ�ָ�����鲻���� ", ex1);
				throw new ModelException("ָ�����鲻���� ", ex1);
			}
			catch (FactoryException ex1)
			{
				log.error(ex1);
				throw new ModelException(ex1);
			}

			//��֤��λ����
			UnitTreeNode unit = treeManager.getUnitTree(tempPermission.
				getUnitID());
            if(unit==null)
            {
            	log.error("��ʼ��Ȩ��ʱʧ�ܣ�ָ���ĵ�λ������ ��λ��:" +tempPermission.getUnitID());
            	throw new ModelException("��ʼ��Ȩ��ʱʧ�ܣ�ָ���ĵ�λ������ ��λ��:" +tempPermission.getUnitID());
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
	 * �������Ȩ��
	 * ����ʧ�����׳��쳣
	 * @param groupID ���ID��
	 * @param unitID ��λID��
	 * @param right  Ȩ�޶��� ��Ȩ�޶�����Ϊ�գ����� new UnitPermission();�õ��ö�����ʱ��Ȩ��ΪĬ����СȨ��
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

			//���ȡ��������Ȩ�ޣ��������ݿ���ɾ����Ӧ�ļ�¼�����Ҳ��ټ���ִ��
			if (right.intValue() == 0)
			{
				deleteUnitPermission(groupID, unitID);
				return;
			}

			Connection con = session.connection();

			//�ж����Ƿ����
			if (!isGroupDefined(groupID, con))
			{
				log.error("����Ȩ��ʱָ�����鲻���ڣ���ID��" + groupID);
				throw new ModelException("����Ȩ��ʱָ�����鲻���ڣ���ID��" + groupID);
			}

			//��������������Ȩ��
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
			throw new ModelException("�����û���ʧ��");
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
	 * �ж����Ƿ����
	 * @param groupID ��ID
	 * @param con ����
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
	 * ȡ���Ѿ��������ĵ�λȨ��
	 * ȡ��ʧ�����׳��쳣
	 * @param groupID ��ID��
	 * @param unitID ��λID
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
			log.info("ɾ��Ȩ�޳������⣺" + ex.getMessage());
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
			throw new ModelException("�����û���ʧ��");
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
	 * ֱ�Ӳ�ѯ���Ȩ��
	 * @param groupID ��ID��
	 * @param unitID ��λID��
	 * @param permission Ȩ�ޱ�ʶ
	 * @param session Hibernate����
	 * @return boolean�ͱ�����true or false
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
					//�ڵ�ǰ�ڵ��ҵ���Ȩ�޷�����Ϣ
					return unitPermission.getPermission(permission);
				}
				else
				{
					//�ظ��ڵ����
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
	 * ֱ�Ӳ�ѯ���Ȩ��
	 * @param groupID ��ID��
	 * @param unitID ��λID��
	 * @param permission Ȩ�ޱ�ʶ
	 * @return boolean�ͱ�����true or false
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
			String message = "���ܵõ�Ȩ��";
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
	 * �������ָ����λ��Ȩ��
	 * ���ڸ�����û��ָ���õ�λ��Ȩ�ޣ��������ڸ�����¼���򷵻�����Ȩ�޾�Ϊ ��
	 * @param groupID ��ID��
	 * @param unitID ��λID��
	 * @param session Hibernate����
	 * @return Ȩ�޶������û��Ȩ����Ϣ������null
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
	 * ���ݸ����ĵ�λID���ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param unitID ��λID��
	 * @return UntiAssignmentֵ����ļ���
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
			throw new ModelException("����ʧ�ܣ�" + ex.getMessage());
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
	 * ���ݸ�������ID���ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param groupID ��ID��
	 * @return UntiAssignmentֵ����ļ���
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
			throw new ModelException("����ʧ�ܣ�" + ex.getMessage());
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
	 * ���ݸ����ĵ�λ���뷵�ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param unitID ��λID��
	 * @param session Session
	 * @return UntiAssignmentֵ����ļ���
	 * @throws ModelException
	 */
	private Collection getUnitAssignmentInfo(String unitID,
											 Session session) throws
		ModelException
	{

		Collection result = new ArrayList();

		try
		{
			//��ѯ
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

				//����UnitAssignmentֵ����
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

				//��ӵ������
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
			throw new ModelException("����ʧ�ܣ�" + ex.getMessage());
		}
		catch (UserManagerException uex)
		{
			uex.printStackTrace();
			throw new ModelException(uex.getMessage());
		}

		return result;
	}

	/**
	 * ���ݸ����ĵ�λ���뷵�ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param unitID ��λID��
	 * @param session Session
	 * @return UntiAssignmentֵ����ļ���
	 * @throws ModelException
	 */
	private Collection getUnitAssignmentInfo(Integer groupID,
											 Session session) throws
		ModelException
	{
		Collection result = new ArrayList();

		try
		{
			//��ѯ
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

				//����UnitAssignmentֵ����
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
					//��ӵ������
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
			throw new ModelException("����ʧ�ܣ�" + ex.getMessage());
		}
		catch (UserManagerException uex)
		{
			uex.printStackTrace();
			throw new ModelException(uex.getMessage());
		}
		return result;
	}

	/**
	 * �жϸõ�λ��Ȩ���Ƿ��Ѿ�����
	 * @param groupID ��ID��
	 * @param unitID ��λID��
	 * @param con ����
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