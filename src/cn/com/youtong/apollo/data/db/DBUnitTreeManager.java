package cn.com.youtong.apollo.data.db;

import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.xml.*;
import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.TaskTime;
import cn.com.youtong.apollo.usermanager.*;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;

/**
 * UnitTreeManager�����ݿ�ʵ��
 */
class DBUnitTreeManager	implements UnitTreeManager
{
	private Log log = LogFactory.getLog(DBUnitTreeManager.class);

	/**
	 * ��λɭ��
	 */
	private Set unitForest;

	/**
	 * ��Ӧ������ʵ��
	 */
	private Task task;

	//������е��ֶ���
	private int unitIDDBField = 1;
	private int unitCodeDBField;
	private int parentUnitCodeDBField;
	private int headquarterCodeDBField;
	private int unitNameDBField;
	private int reportTypeField;
	private String unitMetaTableName;
	private int p_Parent;
	private int display;

	/**
	 * �޸����ݱ��е�λid��sql����
	 */
	private Collection updateUnitIDSqls;

	/**
	 * ������λ��sql
	 */
	private String createUnitSql;

	/**
	 * ���µ�λ��sql
	 */
	private String updateUnitSql;

	/**
	 * ɾ����λ��sql
	 */
	private String deleteUnitSql;

	public DBUnitTreeManager(Task task) throws ModelException
	{
		this.task = task;
		init();
	}

	/**
	 * ���µ�ǰUnitTreeManager
	 * @throws ModelException
	 */
	public synchronized void update() throws ModelException
	{
		init();
	}
	
	/**
	 * ��ʼ��
	 * @throws ModelException
	 */
	private void init() throws ModelException
	{
		Session session = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();

			UnitMetaTable unitMetaTable = task.getUnitMetaTable();
			unitMetaTableName = NameGenerator.generateDataTableName(task.id(),
				unitMetaTable.id());

			con = session.connection();
			String sql = "select * from " + unitMetaTableName + " where 1=0";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			//ȡ�����ݱ��еĸ��ֶε�index
			unitIDDBField = 1;
			unitCodeDBField = rs.findColumn(unitMetaTable.getUnitCodeCell().
											getDBFieldName());
			parentUnitCodeDBField = rs.findColumn(unitMetaTable.
												  getParentUnitCodeCell().
												  getDBFieldName());
			headquarterCodeDBField = rs.findColumn(unitMetaTable.getHQCodeCell().
				getDBFieldName());
			unitNameDBField = rs.findColumn(unitMetaTable.getUnitNameCell().
											getDBFieldName());
			reportTypeField = rs.findColumn(unitMetaTable.getReportTypeCell().
											getDBFieldName());
			p_Parent = rs.findColumn(UnitMetaTable.P_PARENT);
			display = rs.findColumn("display");//rs.findColumn(unitMetaTable.getDisplayCell().getDBFieldName());

			//ps.close();

			//���ص�λɭ��
			loadUnitForest(session);
		}
		catch (Exception ex)
		{
			log.error("���쵥λ��ʧ��", ex);
			throw new ModelException("���쵥λ��ʧ��", ex);
		}
		finally
		{
			SQLUtil.close( rs, ps, null );
			HibernateUtil.close( session );
		}
	}

	/**
	 * ������λ
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @return �´����ĵ�λ�ڵ����
	 * @throws ModelException
	 */
	public UnitTreeNode createUnit(String code, String name, String reportType,
								   String parentCode, String HQCode,
								   String p_Parent) throws
		ModelException
	{
		Session session = null;
		Transaction tx = null;
		PreparedStatement createPst = null;

		String unitID = generateUnitID(code, reportType);
		if (isUnitExist(unitID))
		{
			throw new ModelException("Ҫ�����ĵ�λ�Ѿ�����");
		}

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection conn = session.connection();

			createPst = conn.prepareStatement(getCreateUnitSql());

			createUnit(code, name, reportType, parentCode, HQCode, p_Parent,createPst);
			tx.commit();
			log.info("�ɹ�������λ" + unitID);

			//���µ�ǰUnitTreeManager
			this.update();

			return getUnitTree(unitID);
		}
		catch (Exception e)
		{
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex)
			{
			}
			String message = "������λ" + unitID + "ʧ��";
			log.error(message, e);
			throw new ModelException(message, e);
		}
		finally
		{
			SQLUtil.close(createPst);
			HibernateUtil.close(session);
		}
	}

	/**
	 * ���µ�λ
	 * @param unitID ��λid
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @return ���º�ĵ�λ�ڵ����
	 * @throws ModelException
	 */
	public UnitTreeNode updateUnit(String unitID, String code, String name,
								   String reportType, String parentCode,
								   String HQCode,
								   String p_Parent,int display) throws ModelException
	{
		Session session = null;
		Transaction tx = null;
		PreparedStatement updatePst = null;
		Collection updateDataPsts = null;

		if (!isUnitExist(unitID))
		{
			throw new ModelException("Ҫ���µĵ�λ������");
		}

		String newID = generateUnitID(code, reportType);
		if (!newID.equals(unitID) && isUnitExist(newID))
		{
			throw new ModelException("��λ����Ϊ��" + code + "���ұ�������Ϊ��" + reportType +
									 "���ĵ�λ�Ѿ�����");
		}

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection conn = session.connection();

			updatePst = conn.prepareStatement(getUpdateUnitSql());
			updateDataPsts = getUpdateUnitDataStatements(session);

			updateUnit(unitID, code, name, reportType, parentCode, HQCode,
					   p_Parent,display,
					   updatePst, updateDataPsts, session);
			tx.commit();
			log.info("�ɹ����µ�λ" + unitID);

			//���µ�ǰUnitTreeManager
			this.update();

			return getUnitTree(newID);
		}
		catch (Exception e)
		{
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex)
			{
			}
			String message = "���µ�λ" + unitID + "ʧ��";
			log.error(message, e);
			throw new ModelException(message, e);
		}
		finally
		{
			SQLUtil.close(updatePst);
			for (Iterator itr = updateDataPsts.iterator(); itr.hasNext(); )
			{
				SQLUtil.close( (PreparedStatement) itr.next());
			}
			HibernateUtil.close(session);
		}
	}

	/**
	 * ɾ����λ
	 * @param unitID ��λid
	 * @throws ModelException
	 */
	public void deleteUnit(String unitID) throws ModelException
	{
		Session session = null;
		Transaction tx = null;
		PreparedStatement deletePst = null;

		if (!isUnitExist(unitID))
		{
			throw new ModelException("Ҫɾ���ĵ�λ������");
		}

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection conn = session.connection();

			deletePst = conn.prepareStatement(getDeleteUnitSql());

			deleteUnit(unitID, deletePst, session);
			tx.commit();
			log.info("�ɹ�ɾ����λ" + unitID);

			//���µ�ǰUnitTreeManager
			this.update();
		}
		catch (Exception e)
		{
			try
			{
				tx.rollback();
			}
			catch (HibernateException ex)
			{
			}
			String message = "ɾ����λ" + unitID + "ʧ��";
			log.error(message, e);
			throw new ModelException(message, e);
		}
		finally
		{
			SQLUtil.close(deletePst);
			HibernateUtil.close(session);
		}
	}

	/**
	 * ��õ�λ���ݶ���(�����Ķ��󣬲������¼��ڵ�)
	 * @param unitIDs ��λID����
	 * @return ��λ���󼯺�(UnitTreeNode)
	 * @throws ModelException
	 */
	public Collection getUnits(String[] unitIDs)
	{
		TreeSet result = new TreeSet();
		for (int i = 0; i < unitIDs.length; i++)
		{
			DBUnitTreeNode unit = getTree(unitIDs[i], unitForest);
			if (unit != null)
			{
				result.add(unit);
			}
		}
		return result;
	}

	/**
	 * ����ѡ����ܵ�λDBUnitTreeNodeɭ��
	 * @param forest ѡ����ܵ�λDBUnitTreeNodeɭ��
	 * @param deletePst ɾ��ѡ����ܵ�λ��Statement
	 * @param createPst ����ѡ����ܵ�λ��Statement
	 * @param session session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private void createSelectSumUnitForest(Collection forest,
										   PreparedStatement deletePst,
										   PreparedStatement createPst,
										   Session session) throws
		HibernateException, SQLException
	{
		for (Iterator itr = forest.iterator(); itr.hasNext(); )
		{
			DBUnitTreeNode unit = (DBUnitTreeNode) itr.next();
			createSelectSumUnitTree(unit, deletePst, createPst, session);
		}
	}

	/**
	 * ����ѡ����ܵ�λDBUnitTreeNodeɭ��
	 * @param schema ѡ����ܷ���
	 * @param session Session����
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void createSelectSumUnitForest(Selsum schema, Session session) throws
		HibernateException, SQLException
	{
		PreparedStatement deletePst = null;
		PreparedStatement createPst = null;
		try
		{
			Collection forest = getSelectSumUnitForest(schema);

			Connection conn = session.connection();

			createPst = conn.prepareStatement(getCreateUnitSql());
			deletePst = conn.prepareStatement(getDeleteUnitSql());

			createSelectSumUnitForest(forest, deletePst, createPst, session);
		}
		finally
		{
			SQLUtil.close(deletePst);
			SQLUtil.close(createPst);
		}
	}

	/**
	 * ����ѡ����ܵ�λ��
	 * @param unitTree ѡ����ܵ�λ��
	 * @param deletePst ɾ��ѡ����ܵ�λ��Statement
	 * @param createPst ����ѡ����ܵ�λ��Statement
	 * @param session Session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private void createSelectSumUnitTree(DBUnitTreeNode unitTree,
										 PreparedStatement deletePst,
										 PreparedStatement createPst,
										 Session session) throws
		HibernateException, SQLException
	{
		createSelectSumUnit(unitTree, deletePst, createPst, session);
		for (Iterator itr = unitTree.getChildren(); itr.hasNext(); )
		{
			DBUnitTreeNode subTree = (DBUnitTreeNode) itr.next();
			//����P_PARENT����
			subTree.setP_Parent(unitTree.id());
			//�ڹ�
			createSelectSumUnitTree(subTree, deletePst, createPst, session);
		}
	}

	/**
	 * ����ѡ����ܵ�λ��λ
	 * @param unit ѡ����ܵ�λ
	 * @param deletePst ɾ����λ��Statement
	 * @param createPst ������λ��Statement
	 * @param session Session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private void createSelectSumUnit(DBUnitTreeNode unit,
									 PreparedStatement deletePst,
									 PreparedStatement createPst,
									 Session session) throws
		HibernateException, SQLException
	{
		//��ɾ��
		deleteUnit(unit.id(), deletePst, session);

		//�󴴽�
		createUnit(unit.getUnitCode(), unit.getUnitName(), unit.getReportType(),
				   unit.getParentUnitCode(), unit.getHQCode(), unit.getP_Parent(),
				   createPst);
	}

	/**
	 * ������λ
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @param createPst ������λ��PreparedStatement
	 * @throws SQLException
	 */
	private void createUnit(String code, String name, String reportType,
							String parentCode, String HQCode, String p_Parent,
							PreparedStatement createPst) throws
		SQLException
	{
		createPst.setString(1, HQCode);
		createPst.setString(2, p_Parent);
		createPst.setString(3, parentCode);
		createPst.setString(4, reportType);
		createPst.setString(5, code);
		createPst.setString(6, name);
		createPst.setString(7, generateUnitID(code, reportType));
		createPst.execute();
	}

	/**
	 * ���µ�λ
	 * @param unitID ��λid
	 * @param code ��λ����
	 * @param name ��λ����
	 * @param reportType ��������
	 * @param parentCode �ϼ�����
	 * @param HQCode ���Ŵ���
	 * @param p_Parent p_Parenet
	 * @param updatePst ���µ�λ��PreparedStatement
	 * @param updateDataPsts �������ݱ��е�λid��PreparedStatement����
	 * @param session Session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private void updateUnit(String unitID, String code, String name,
							String reportType, String parentCode, String HQCode,
							String p_Parent,int display, PreparedStatement updatePst,
							Collection updateDataPsts, Session session) throws
		HibernateException, SQLException
	{
		String newID = generateUnitID(code, reportType);
		//���·����
		updatePst.setString(1, HQCode);
		updatePst.setString(2, p_Parent);
		updatePst.setString(3, parentCode);
		updatePst.setString(4, reportType);
		updatePst.setString(5, code);
		updatePst.setString(6, name);
		updatePst.setString(7, newID);
		updatePst.setInt(8, display);
		updatePst.setString(9, unitID);
		updatePst.execute();

		//�����λ��id�ı䣬�������ݱ��иõ�λ��id
		if (!newID.equals(unitID))
		{
			for (Iterator itr = updateDataPsts.iterator(); itr.hasNext(); )
			{
				PreparedStatement pst = (PreparedStatement) itr.next();
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.execute();
			}

			//���������Ϣ
			PreparedStatement pst = null;
			try
			{
				Connection conn = session.connection();

				//������õ�λ��ص�Ȩ��
				pst = conn.prepareStatement(
					"UPDATE ytapl_unitpermissions SET unitID = ? WHERE unitID = ? AND taskID = ?");
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.setString(3, task.id());
				pst.execute();
				pst.close();

				//������õ�λ��ص���ϵ��ʽ
				pst = conn.prepareStatement(
					"UPDATE ytapl_addressinfo SET unitID = ? WHERE unitID = ? AND taskID = ?");
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.setString(3, task.id());
				pst.execute();
				pst.close();

				//������õ�λ��ص�����
				pst = conn.prepareStatement(
					"UPDATE ytapl_fillstate SET unitID = ? WHERE unitID = ? AND taskID = ?");
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.setString(3, task.id());
				pst.execute();
				pst.close();
			}
			finally
			{
				SQLUtil.close(pst);
			}
		}
	}

	/**
	 * ɾ����λ
	 * @param unitID ��λid
	 * @param deletePst ɾ����λ��PreparedStatement
	 * @param session Session
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private void deleteUnit(String unitID, PreparedStatement deletePst,
							Session session) throws
		HibernateException, SQLException
	{
		deletePst.setString(1, unitID);
		deletePst.execute();

		//ɾ����õ�λ��ص�Ȩ��
		session.delete("select permission from UnitPermissionForm permission where permission.comp_id.taskID = ? and permission.comp_id.unitID = ?",
					   new Object[]
					   {task.id(), unitID}
					   , new Type[]
					   {Hibernate.STRING, Hibernate.STRING});

		//ɾ����õ�λ��ص���ϵ��ʽ��Ϣ
		session.delete("select address from AddressInfoForm address where address.comp_id.taskID = ? and address.comp_id.unitID = ?",
					   new Object[]
					   {task.id(), unitID}
					   , new Type[]
					   {Hibernate.STRING, Hibernate.STRING});

		//ɾ����õ�λ��ص�����
		PreparedStatement pst = null;
		try
		{
			Connection conn = session.connection();
			pst = conn.prepareStatement(
				"DELETE FROM ytapl_fillstate WHERE unitID = ? AND taskID = ?");
			pst.setString(1, unitID);
			pst.setString(2, task.id());
			pst.execute();
		}
		finally
		{
			SQLUtil.close(pst);
		}
	}

	/**
	 * �õ�������λ��sql
	 * @return ������λ��sql
	 */
	private String getCreateUnitSql()
	{
		if (createUnitSql == null)
		{
			UnitMetaTable metaTable = task.getUnitMetaTable();
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO ");
			sql.append(NameGenerator.generateDataTableName(task.id(),metaTable.id()));
			sql.append(" ( ");
			sql.append(metaTable.getHQCodeCell().getDBFieldName());
			sql.append(" , ");
			sql.append(UnitMetaTable.P_PARENT);
			sql.append(" , ");
			sql.append(metaTable.getParentUnitCodeCell().getDBFieldName());
			sql.append(" , ");
			sql.append(metaTable.getReportTypeCell().getDBFieldName());
			sql.append(" , ");
			sql.append(metaTable.getUnitCodeCell().getDBFieldName());
			sql.append(" , ");
			sql.append(metaTable.getUnitNameCell().getDBFieldName());
			sql.append(", UNITID) VALUES (?,?,?,?,?,?,?)");

			createUnitSql = sql.toString();
		}

		return createUnitSql;
	}

	/**
	 * �õ�ɾ����λ��sql
	 * @return ɾ����λ��sql
	 */
	private String getDeleteUnitSql()
	{
		if (deleteUnitSql == null)
		{
			UnitMetaTable metaTable = task.getUnitMetaTable();
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM ");
			sql.append(NameGenerator.generateDataTableName(task.id(),
				metaTable.id()));
			sql.append(" WHERE  UNITID = ? ");

			deleteUnitSql = sql.toString();
		}
		return deleteUnitSql;
	}

	/**
	 * �õ����µ�λ��sql
	 * @return ���µ�λ��sql
	 */
	private String getUpdateUnitSql()
	{
		if (updateUnitSql == null)
		{
			UnitMetaTable metaTable = task.getUnitMetaTable();
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE ");
			sql.append(NameGenerator.generateDataTableName(task.id(),
				metaTable.id()));
			sql.append(" SET ");
			sql.append(metaTable.getHQCodeCell().getDBFieldName());
			sql.append(" = ? , ");
			sql.append(UnitMetaTable.P_PARENT);
			sql.append(" = ? , ");
			sql.append(metaTable.getParentUnitCodeCell().getDBFieldName());
			sql.append(" = ? , ");
			sql.append(metaTable.getReportTypeCell().getDBFieldName());
			sql.append(" = ? , ");
			sql.append(metaTable.getUnitCodeCell().getDBFieldName());
			sql.append(" = ? , ");
			sql.append(metaTable.getUnitNameCell().getDBFieldName());
			sql.append(" = ? , UNITID = ? ,display=? WHERE UNITID = ? ");

			updateUnitSql = sql.toString();
		}
		return updateUnitSql;
	}

	/**
	 * �õ��޸����ݱ��е�λid��PrepareStatement����
	 * @param session Session����
	 * @return �޸����ݱ��е�λid��PrepareStatement����
	 * @throws HibernateException
	 * @throws SQLException
	 */
	private Collection getUpdateUnitDataStatements(Session session) throws
		HibernateException, SQLException
	{
		LinkedList result = new LinkedList();

		for (Iterator itr = getUpdateUnitIDSqls().iterator(); itr.hasNext(); )
		{
			PreparedStatement pst = session.connection().prepareStatement( (
				String) itr.next());
			result.add(pst);
		}
		return result;
	}

	/**
	 * �õ��޸����ݱ��е�λid��sql����
	 * @return �޸����ݱ��е�λid��sql����
	 * @throws HibernateException
	 */
	private Collection getUpdateUnitIDSqls() throws HibernateException
	{
		try
		{
			if (updateUnitIDSqls == null)
			{
				updateUnitIDSqls = new LinkedList();

				//�������ÿ�����ݱ��sql
				for (Iterator itr = task.getAllTables(); itr.hasNext(); )
				{
					cn.com.youtong.apollo.task.Table table = (cn.com.youtong.
						apollo.
						task.Table) itr.next();
					StringBuffer sql = new StringBuffer();
					sql.append("UPDATE ");
					sql.append(NameGenerator.generateDataTableName(task.id(),
						table.id()));
					sql.append(" SET UNITID = ? WHERE UNITID = ?");

					updateUnitIDSqls.add(sql.toString());
				}

				//������
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE ");
				sql.append(NameGenerator.generateAttachmentTableName(task.id()));
				sql.append(" SET UNITID = ? WHERE UNITID = ?");

				updateUnitIDSqls.add(sql.toString());
			}
			return updateUnitIDSqls;
		}
		catch (TaskException ex)
		{
			throw new HibernateException(ex);
		}
	}

	/**
	 * ����ѡ����ܷ����õ�ѡ�����ɭ��
	 * @param schema ѡ����ܷ���
	 * @return ѡ����ܵ�λDBUnitTreeNodeɭ��
	 */
	public Collection getSelectSumUnitForest(Selsum schema)
	{
		Collection result = new TreeSet();
		//�������ڵ�
		for (int i = 0; i < schema.getNodeCount(); i++)
		{
			Node root = schema.getNode(i);
			DBUnitTreeNode tree = getSelectSumUnitTree(root, schema);
			result.add(tree);
		}
		return result;
	}

	/**
	 * �õ�ָ��ѡ����ܵ�λ��ѡ���������
	 * @param unit ѡ����ܵ�λ
	 * @param schema ѡ����ܷ���
	 * @return ѡ���������
	 */
	private String getSelectSumCondition(Node unit, Selsum schema)
	{
		String condition = schema.getScript() + " \n ";

		if (schema.getScriptglobalcondition() != null &&
			!schema.getScriptglobalcondition().trim().equals(""))
		{
			condition = condition + " ( " +
				schema.getScriptglobalcondition() + " ) && ";
		}

		condition = condition + " ( " + unit.getLogicexpr() + " ) ";

		return condition;
	}

	/**
	 * ����ָ�����ڵ��ѡ����ܵ�λ��
	 * @param root ���ڵ�
	 * @param schema ѡ����ܷ���
	 * @return ��λ��
	 */
	private DBUnitTreeNode getSelectSumUnitTree(Node root, Selsum schema)
	{
		DBUnitTreeNode unit = new DBUnitTreeNode();
		unit.setUnitCode(root.getId());
		unit.setUnitName(root.getName());
		unit.setReportType(ReportType.SELECT_GATHER_TYPE);
		String unitID = generateUnitID(unit.getUnitCode(), unit.getReportType());
		unit.setID(unitID);
		unit.setSelectSumCondition(getSelectSumCondition(root, schema));

		for (int i = 0; i < root.getNodeCount(); i++)
		{
			//�ڹ�
			unit.addChild(getSelectSumUnitTree(root.getNode(i), schema));
		}

		return unit;
	}

	/**
	 * ���ָ������ĵ�λ������unitIDָ���ĵ�λΪ�� ��unitID�������򷵻�null
	 * @param unitID ��λID��
	 * @return ��λ��
	 * @throws ModelException
	 */
	public UnitTreeNode getUnitTree(String unitID) throws ModelException
	{
		DBUnitTreeNode result = getTree(unitID, unitForest);
//		if (result == null)
//		{
//			throw new ModelException("ָ���ĵ�λû���ҵ���unitID=��" + unitID + "��");
//		}
		return result;
	}

	/**
	 * ����λ�������Ǹ��ĵ�λȥ��
	 * @param units ��Ҫ����ĵ�λDBUnitTreeNode����
	 * @return �����ĵ�λDBUnitTreeNode����
	 */
	private Collection reduceUnit(Collection units)
	{
		Collection result = new TreeSet();

		Iterator itrUnits = units.iterator();
		while (itrUnits.hasNext())
		{
			DBUnitTreeNode unit = (DBUnitTreeNode) itrUnits.next();
			if (!unit.isDescendantOf(units))
			{
				result.add(unit);
			}
		}
		return result;
	}

	/**
	 * ����ѡ����ܵ�λɭ�֣�����λɭ��������ѡ�������ĵ�λ�ӵ�ѡ����ܵ�λɭ����
	 * @param schema ѡ����ܷ���
	 * @param taskTime ����ʱ��
	 * @param unitACL ��λACL
	 * @return ѡ����ܵ�λDBUnitTreeNodeɭ��
	 * @throws ModelException
	 */
	public Collection buildSelectSumUnitForest(DBSelectSumSchema schema,
										TaskTime taskTime,
										UnitACL unitACL) throws
		ModelException
	{
		TreeSet result = new TreeSet();
		Collection forest = Convertor.collection(getUnitForest(unitACL));
		for (Iterator itr = schema.getSelectSumUnitForest(); itr.hasNext(); )
		{
			DBUnitTreeNode tree = (DBUnitTreeNode) itr.next();
			result.add(buildSelectSumTree(tree, forest, taskTime));
		}
		return result;
	}

	/**
	 * �ӵ�λɭ���еõ�ָ����λΪ���ĵ�λ��
	 * @param unitID ָ����λ��id
	 * @param forest ��λɭ��
	 * @return ��ָ����λΪ���ĵ�λ�������û���ҵ�������null
	 */
	private DBUnitTreeNode getTree(String unitID, Collection forest)
	{
		for (Iterator itr = forest.iterator(); itr.hasNext(); )
		{
			DBUnitTreeNode tree = (DBUnitTreeNode) itr.next();
			DBUnitTreeNode result = getTree(unitID, tree);
			if (result != null)
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * �ӵ�λ���еõ�ָ����λ����
	 * @param unitID ��λid
	 * @param tree ��λ��
	 * @return ָ����λ���������û�ҵ�������null
	 */
	private DBUnitTreeNode getTree(String unitID, DBUnitTreeNode tree)
	{
		if (tree.id().equalsIgnoreCase(unitID))
		{
			return tree;
		}
		else
		{
			for (Iterator itr = tree.getChildren(); itr.hasNext(); )
			{
				DBUnitTreeNode subTree = (DBUnitTreeNode) itr.next();
				DBUnitTreeNode result = getTree(unitID, subTree);
				if (result != null)
				{
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * ����ѡ����ܵ�λ��������λɭ������ѡ�������ĵ�λ�ӵ�ѡ����ܵ�λ����
	 * @param tree ѡ����ܵ�λ��
	 * @param forest ��λɭ��
	 * @param taskTime ����ʱ��
	 * @return ѡ����ܵ�λ��
	 * @throws ModelException
	 */
	private DBUnitTreeNode buildSelectSumTree(DBUnitTreeNode tree,
											  Collection forest,
											  TaskTime taskTime) throws
		ModelException
	{
		try
		{
			ScriptEngine scriptEngine = new ScriptEngine();

			DataSource dataSource = new DBDataSource(task, this);

			Map id2nodeMap = new HashMap();
			getUnits(forest, id2nodeMap); // ����ݹ鲻֪���Ƿ�������
			TreeSet selectSumUnits = getSelecSumtUnits(tree);

			Iterator taskDataIter = dataSource.get(id2nodeMap.keySet(),
				taskTime);
			while (taskDataIter.hasNext())
			{
				TaskData taskData = (TaskData) taskDataIter.next();

				String unitID = taskData.getUnitID();
				DBUnitTreeNode unit = (DBUnitTreeNode) id2nodeMap.get(unitID);
				if (unit == null)
				{
					continue;
				}

				for (Iterator selectUnitItr = selectSumUnits.iterator();
					 selectUnitItr.hasNext(); )
				{
					DBUnitTreeNode selectSumUnit = (DBUnitTreeNode)
						selectUnitItr.next();
					LinkedList selectSumCondition = new LinkedList();
					selectSumCondition.add(selectSumUnit.getSelectSumCondition());
					//compile
					List exps = scriptEngine.compile(selectSumCondition);
					//�ж��Ƿ�����ѡ������
					List result = scriptEngine.eval(taskData, exps);

					Object value = result.get(0);

					boolean isMatch = Convertor.object2Boolean(value);

					if (isMatch)
					{
						selectSumUnit.addChild(unit);
					}
				}
			}

			return tree;
		}
		catch (ScriptException ex)
		{
			throw new ModelException(ex);
		}
	}

	/**
	 * ��ѡ��������еõ�ѡ����ܵ�λ����
	 * @param tree ѡ�������
	 * @return ѡ����ܵ�λDBUnitTreeNode����
	 */
	private TreeSet getSelecSumtUnits(DBUnitTreeNode tree)
	{
		TreeSet units = new TreeSet();

		//������ڵ�
		units.add(tree);

		//�������ڹ�
		for (Iterator itr = tree.getChildren(); itr.hasNext(); )
		{
			DBUnitTreeNode subTree = (DBUnitTreeNode) itr.next();
			units.addAll(getSelecSumtUnits(subTree));
		}

		return units;
	}

	/**
	 * �ӵ�λDBUnitTreeNodeɭ���еõ����еĵ�λ
	 * @param forest ��λDBUnitTreeNodeɭ��
	 * @param map ɭ�������е�λ��unitID/DBUnitTreeNode Map
	 */
	private void getUnits(Collection forest, Map map)
	{
		for (Iterator itr = forest.iterator(); itr.hasNext(); )
		{
			DBUnitTreeNode node = (DBUnitTreeNode) itr.next();
			getUnits(node, map);
		}
	}

	public void getUnits(UnitTreeNode node, Map map)
	{
		map.put(node.id(), node);
		for (Iterator itr = node.getChildren(); itr.hasNext(); )
		{
			getUnits( (DBUnitTreeNode) itr.next(), map);
			map.put(node.id(), node);
		}
	}

	/**
	 * �ӵ�λ���еõ����еĵ�λ
	 * @param tree ��λ��
	 * @return ��λ�������еĵ�λDBUnitTreeNode����
	 */
	private TreeSet getUnits(DBUnitTreeNode tree)
	{
		TreeSet units = new TreeSet();
		units.add(tree);

		for (Iterator itr = tree.getChildren(); itr.hasNext(); )
		{
			//�ڹ�
			units.addAll(getUnits( (DBUnitTreeNode) itr.next()));
		}
		return units;
	}

	/**
	 * �õ���λɭ��
	 * @param unitACL ���ʿ���Ȩ��
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest(UnitACL unitACL) throws ModelException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			//���ݵõ����и��û���Ȩ�ޱ��д��ڵĵ�λ
			String[] permissionUnitIDs = getUnitIDs(unitACL, session);
			//�õ���λ����
			Collection units = getUnits(permissionUnitIDs);
			//����λ�������Ǹ��ĵ�λȥ��
			Collection roots = reduceUnit(units);

			return roots.iterator();
		}
		catch (HibernateException ex)
		{
			throw new ModelException("��ѯ�û�ɭ��ʧ��", ex);
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
	}

	/**
	 * ���ݷ��ʿ����б��õ����пɷ��ʵĵ�λid
	 * @param unitACL ��λ���ʿ����б�
	 * @param session Hibernate����
	 * @return ��λid������
	 * @throws HibernateException
	 */
	private String[] getUnitIDs(UnitACL unitACL, Session session) throws
		HibernateException
	{
		Collection unitIDs = new LinkedList();
		User user = unitACL.getUser();
		Iterator itrGroups = user.getGroups().iterator();

		PreparedStatement ps = null;
		try
		{
			Connection con = session.connection();
			String sql =
				"select unitID from YTAPL_UnitPermissions where taskID = ? and groupID = ?";
			ps = con.prepareStatement(sql);

			while (itrGroups.hasNext())
			{
				Group group = (Group) itrGroups.next();

				ps.setString(1, task.id());
				ps.setInt(2, group.getGroupID().intValue());
				ResultSet rs = ps.executeQuery();
				while (rs.next())
				{
					unitIDs.add(rs.getString(1));
				}
				rs.close();
			}
		}
		catch (SQLException ex)
		{
			log.error("", ex);
			throw new HibernateException(ex);
		}
		finally
		{
			SQLUtil.close( ps );
		}
		String[] str = new String[unitIDs.size()];
		return (String[]) unitIDs.toArray(str);
	}
	/**
	 * ����paramname���ص�λɭ��
	 * @param session Hibernate Session
	 * @throws HibernateException
	 */
	private void loadUnitForest(Session session,String paramname) throws HibernateException
	{
		//���ң��������ҵĽ��ת��ΪUnit���󣬴Ӷ��õ����󼯺�
		TreeMap allUnits = getAllUnitTreeNodes(task.id(), session,paramname);
		//�õ�ɭ��(�������еĸ��ڵ�Ϊ����������ɵ�ɭ��)
		unitForest = getForest(allUnits);
	}
	
	/**
	 * ���ݸ�����taskID ���Ҹ������е����еĵ�λ(�Ե�λ���󼯺ϵ���ʽ����)
	 * @param taskID ����ID��
	 * @param session Hibernate����
	 * @return ��λ����ļ���
	 * @throws HibernateException
	 */
	private TreeMap getAllUnitTreeNodes(String taskID, Session session,String paramname) throws
		HibernateException
	{
		TreeMap result = null;

		//���Ҹ���������е�λ��ý���� ResultSet
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			con = session.connection();
			String sql = "select * from " + unitMetaTableName +" where  qymc like '%"+paramname+"%'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			//�������ת��Ϊ��DBUnitTreeNode ���󼯺�
			result = resultSet2DBUnitTreeNodeMap(rs);
			//ps.close();
		}
		catch (SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
			SQLUtil.close( rs, ps, null );
		}
		return result;
	}
	/**
	 * �õ���λɭ��
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest() throws ModelException
	{
		return unitForest.iterator();
	}
	/**
	 * ��õ�λ���ݶ���(�����Ķ��󣬲������¼��ڵ�)
	 * @param unitIDs ��λID����
	 * @return ��λ���󼯺�(UnitTreeNode)
	 * @throws ModelException
	 */
	private Collection getUnits(String[] unitIDs,String paramname,Set unitforest)
	{
		TreeSet result = new TreeSet();
		for (int i = 0; i < unitIDs.length; i++)
		{
			DBUnitTreeNode unit = getTree(unitIDs[i], unitforest);
			if (unit != null)
			{
				result.add(unit);
			}
		}
		return result;
	}
	/**
	 * ���ݸ�����taskID ���Ҹ������е����еĵ�λ(�Ե�λ���󼯺ϵ���ʽ����)
	 * ���ݸ���paramname  �õ���Ԫ������Ϣ
	 * @param taskID ����ID��
	 * @param session Hibernate����
	 * @return ��λ����ļ���
	 * @throws HibernateException
	 */
	public List<DBUnitTreeNode> getAllUnitTreeNodes(String taskID,String paramname,UnitACL unitACL) {
		List<DBUnitTreeNode> list = new ArrayList<DBUnitTreeNode>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			//1.��ȡ���û�Ȩ��������Ϣ
			String[] permissionUnitIDs = getUnitIDs(unitACL, session);
			//2.��ȡ����paramname�õ��ĵ�Ԫ���� �õ���λ����
			//2.1 �õ����νṹ��Ϣ
			TreeMap allUnits = getAllUnitTreeNodes(taskID, session,paramname);
			Set unitForest = getForest(allUnits);
			Collection result = getUnits(permissionUnitIDs,paramname,unitForest);
			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				DBUnitTreeNode elem = (DBUnitTreeNode) iterator.next();
				list.add(elem);
//				System.out.println("--1257---->"+elem.getUnitName()+"  "+elem.getReportType()+"<------");
			}
			//3.��ϳɵ�Ԫɭ��Ȼ�󷵻�
//			TreeMap allUnits = getAllUnitTreeNodes(taskID, session,paramname);
//			unitForest = getForest(allUnits);
//			result = getForest(allUnits);
//			result = unitforest.iterator();
			/*	//�õ���λ����
				Collection units = getUnits(permissionUnitIDs);
				//����λ�������Ǹ��ĵ�λȥ��
				Collection roots = reduceUnit(units);
				return roots.iterator();*/
			}
			catch (HibernateException ex){
				log.error("��ѯ��λ����������Ϣ",ex);
			}
			finally{
				if (session != null){
					try{
						session.close();
					}
					catch (HibernateException ex2){
						log.error("�رղ�ѯ��λ����������Ϣ",ex2);
					}
				}
			}
		return list;
	}
	
	/**
	 * ���ص�λɭ��
	 * @param session Hibernate Session
	 * @throws HibernateException
	 */
	private void loadUnitForest(Session session) throws HibernateException
	{
		//���ң��������ҵĽ��ת��ΪUnit���󣬴Ӷ��õ����󼯺�
		TreeMap allUnits = getAllUnitTreeNodes(task.id(), session);
		//�õ�ɭ��(�������еĸ��ڵ�Ϊ����������ɵ�ɭ��)
		unitForest = getForest(allUnits);
	}

	/**
	 * �жϵ�λ�Ƿ����
	 * @param unitID ��λID��
	 * @return ����ֵ
	 */
	public boolean isUnitExist(String unitID)
	{
		return (getTree(unitID, unitForest) != null);
	}

	/**
	 * �õ�ɭ��(�������еĸ��ڵ�Ϊ����������ɵ�ɭ��)
	 * @param allUnits ���еĵ�λ�ļ���
	 * @return ɭ��
	 * @throws ModelException
	 */
	private Set getForest(TreeMap allUnits)
	{
		Set result = new TreeSet();

		long start = System.currentTimeMillis();
		Iterator itrTree = allUnits.keySet().iterator();
		while (itrTree.hasNext())
		{
			String unitID = (String) itrTree.next();
			DBUnitTreeNode unit = (DBUnitTreeNode) allUnits.get(unitID);

			String parentID = NameGenerator.generateTreeParentUnitID(unit.
				getParentUnitCode());
			String p_ParaentID = unit.getP_Parent();

			//�����Ҹýڵ�ĸ��ڵ�
			if (parentID != null && !parentID.trim().equals("") &&
				allUnits.get(parentID) != null)
			{
				//���ø��ӹ�ϵ
				DBUnitTreeNode parent = ( (DBUnitTreeNode) allUnits.get(
					parentID));
				parent.addChild(unit);
				unit.setParent(parent);
			}
			else if (p_ParaentID != null && !p_ParaentID.trim().equals("") &&
					 allUnits.get(p_ParaentID) != null)
			{
				//���ø��ӹ�ϵ
				DBUnitTreeNode parent = ( (DBUnitTreeNode) allUnits.get(
					p_ParaentID));
				parent.addChild(unit);
				unit.setParent(parent);
			}
			else
			{
				//�����ڸ��ڵ㣬�ýڵ�Ϊ��
				result.add(unit);
			}
		}
		if( log.isDebugEnabled() )
			log.debug( "����ɭ������ʱ��(��λ: ����)��"
					  + (System.currentTimeMillis() - start) );

		return result;
	}

	
	
	
	/**
	 * ���ݸ�����taskID ���Ҹ������е����еĵ�λ(�Ե�λ���󼯺ϵ���ʽ����)
	 * @param taskID ����ID��
	 * @param session Hibernate����
	 * @return ��λ����ļ���
	 * @throws HibernateException
	 */
	private TreeMap getAllUnitTreeNodes(String taskID, Session session) throws
		HibernateException
	{
		TreeMap result = null;

		//���Ҹ���������е�λ��ý���� ResultSet
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			con = session.connection();
			String sql = "select * from " + unitMetaTableName;
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			//�������ת��Ϊ��DBUnitTreeNode ���󼯺�
			result = resultSet2DBUnitTreeNodeMap(rs);
			//ps.close();
		}
		catch (SQLException ex)
		{
			throw new HibernateException(ex);
		}
		finally
		{
			SQLUtil.close( rs, ps, null );
		}
		return result;
	}

	/**
	 * �������ת��Ϊ����ļ���
	 * @param rs ��λ��Ľ����
	 * @return ���󼯺�
	 * @throws SQLException
	 */
	private TreeMap resultSet2DBUnitTreeNodeMap(ResultSet rs) throws
		SQLException
	{
		TreeMap result = new TreeMap();
		while (rs.next())
		{
			DBUnitTreeNode dbUnitTreeNode = resultSet2DBUnitTreeNode(rs);
			//��Unit������ӵ������
			result.put(dbUnitTreeNode.id(), dbUnitTreeNode);
		}
		return result;
	}

	private DBUnitTreeNode resultSet2DBUnitTreeNode(ResultSet rs) throws
		SQLException
	{
		DBUnitTreeNode dbUnitTreeNode = new DBUnitTreeNode();
		//����unit����ĳ�Ա������
		dbUnitTreeNode.setHQCode(rs.getString(headquarterCodeDBField));
		dbUnitTreeNode.setUnitCode(rs.getString(unitCodeDBField));
		dbUnitTreeNode.setParentUnitCode(rs.getString(parentUnitCodeDBField));
		dbUnitTreeNode.setUnitName(rs.getString(unitNameDBField));
		dbUnitTreeNode.setReportType(rs.getString(reportTypeField));
		dbUnitTreeNode.setP_Parent(rs.getString(p_Parent));
		dbUnitTreeNode.setID(rs.getString(unitIDDBField));
		dbUnitTreeNode.setDisplay(rs.getInt(display));
		return dbUnitTreeNode;
	}

	/**
	 * ������λID
	 * @param unitCode ��λ����
	 * @param reportType ��������
	 * @return ��λID
	 */
	private String generateUnitID(String unitCode, String reportType)
	{
		return unitCode + reportType;
	}
}
