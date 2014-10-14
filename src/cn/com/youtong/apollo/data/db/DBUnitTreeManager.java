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
 * UnitTreeManager的数据库实现
 */
class DBUnitTreeManager	implements UnitTreeManager
{
	private Log log = LogFactory.getLog(DBUnitTreeManager.class);

	/**
	 * 单位森林
	 */
	private Set unitForest;

	/**
	 * 对应的任务实例
	 */
	private Task task;

	//封面表中的字段名
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
	 * 修改数据表中单位id的sql集合
	 */
	private Collection updateUnitIDSqls;

	/**
	 * 创建单位的sql
	 */
	private String createUnitSql;

	/**
	 * 更新单位的sql
	 */
	private String updateUnitSql;

	/**
	 * 删除单位的sql
	 */
	private String deleteUnitSql;

	public DBUnitTreeManager(Task task) throws ModelException
	{
		this.task = task;
		init();
	}

	/**
	 * 更新当前UnitTreeManager
	 * @throws ModelException
	 */
	public synchronized void update() throws ModelException
	{
		init();
	}
	
	/**
	 * 初始化
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

			//取得数据表中的各字段的index
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

			//加载单位森林
			loadUnitForest(session);
		}
		catch (Exception ex)
		{
			log.error("构造单位树失败", ex);
			throw new ModelException("构造单位树失败", ex);
		}
		finally
		{
			SQLUtil.close( rs, ps, null );
			HibernateUtil.close( session );
		}
	}

	/**
	 * 创建单位
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @return 新创建的单位节点对象
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
			throw new ModelException("要创建的单位已经存在");
		}

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection conn = session.connection();

			createPst = conn.prepareStatement(getCreateUnitSql());

			createUnit(code, name, reportType, parentCode, HQCode, p_Parent,createPst);
			tx.commit();
			log.info("成功创建单位" + unitID);

			//更新当前UnitTreeManager
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
			String message = "创建单位" + unitID + "失败";
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
	 * 更新单位
	 * @param unitID 单位id
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @return 更新后的单位节点对象
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
			throw new ModelException("要更新的单位不存在");
		}

		String newID = generateUnitID(code, reportType);
		if (!newID.equals(unitID) && isUnitExist(newID))
		{
			throw new ModelException("单位代码为“" + code + "”且报表类型为“" + reportType +
									 "”的单位已经存在");
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
			log.info("成功更新单位" + unitID);

			//更新当前UnitTreeManager
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
			String message = "更新单位" + unitID + "失败";
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
	 * 删除单位
	 * @param unitID 单位id
	 * @throws ModelException
	 */
	public void deleteUnit(String unitID) throws ModelException
	{
		Session session = null;
		Transaction tx = null;
		PreparedStatement deletePst = null;

		if (!isUnitExist(unitID))
		{
			throw new ModelException("要删除的单位不存在");
		}

		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Connection conn = session.connection();

			deletePst = conn.prepareStatement(getDeleteUnitSql());

			deleteUnit(unitID, deletePst, session);
			tx.commit();
			log.info("成功删除单位" + unitID);

			//更新当前UnitTreeManager
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
			String message = "删除单位" + unitID + "失败";
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
	 * 获得单位数据对象(单独的对象，不包括下级节点)
	 * @param unitIDs 单位ID集合
	 * @return 单位对象集合(UnitTreeNode)
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
	 * 创建选择汇总单位DBUnitTreeNode森林
	 * @param forest 选择汇总单位DBUnitTreeNode森林
	 * @param deletePst 删除选择汇总单位的Statement
	 * @param createPst 创建选择汇总单位的Statement
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
	 * 创建选择汇总单位DBUnitTreeNode森林
	 * @param schema 选择汇总方案
	 * @param session Session对象
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
	 * 创建选择汇总单位树
	 * @param unitTree 选择汇总单位树
	 * @param deletePst 删除选择汇总单位的Statement
	 * @param createPst 创建选择汇总单位的Statement
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
			//设置P_PARENT属性
			subTree.setP_Parent(unitTree.id());
			//第归
			createSelectSumUnitTree(subTree, deletePst, createPst, session);
		}
	}

	/**
	 * 创建选择汇总单位单位
	 * @param unit 选择汇总单位
	 * @param deletePst 删除单位的Statement
	 * @param createPst 创建单位的Statement
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
		//先删除
		deleteUnit(unit.id(), deletePst, session);

		//后创建
		createUnit(unit.getUnitCode(), unit.getUnitName(), unit.getReportType(),
				   unit.getParentUnitCode(), unit.getHQCode(), unit.getP_Parent(),
				   createPst);
	}

	/**
	 * 创建单位
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @param createPst 创建单位的PreparedStatement
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
	 * 更新单位
	 * @param unitID 单位id
	 * @param code 单位代码
	 * @param name 单位名称
	 * @param reportType 报表类型
	 * @param parentCode 上级代码
	 * @param HQCode 集团代码
	 * @param p_Parent p_Parenet
	 * @param updatePst 更新单位的PreparedStatement
	 * @param updateDataPsts 更新数据表中单位id的PreparedStatement数组
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
		//更新封面表
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

		//如果单位的id改变，更新数据表中该单位的id
		if (!newID.equals(unitID))
		{
			for (Iterator itr = updateDataPsts.iterator(); itr.hasNext(); )
			{
				PreparedStatement pst = (PreparedStatement) itr.next();
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.execute();
			}

			//更新相关信息
			PreparedStatement pst = null;
			try
			{
				Connection conn = session.connection();

				//更新与该单位相关的权限
				pst = conn.prepareStatement(
					"UPDATE ytapl_unitpermissions SET unitID = ? WHERE unitID = ? AND taskID = ?");
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.setString(3, task.id());
				pst.execute();
				pst.close();

				//更新与该单位相关的联系方式
				pst = conn.prepareStatement(
					"UPDATE ytapl_addressinfo SET unitID = ? WHERE unitID = ? AND taskID = ?");
				pst.setString(1, newID);
				pst.setString(2, unitID);
				pst.setString(3, task.id());
				pst.execute();
				pst.close();

				//更新与该单位相关的填报情况
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
	 * 删除单位
	 * @param unitID 单位id
	 * @param deletePst 删除单位的PreparedStatement
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

		//删除与该单位相关的权限
		session.delete("select permission from UnitPermissionForm permission where permission.comp_id.taskID = ? and permission.comp_id.unitID = ?",
					   new Object[]
					   {task.id(), unitID}
					   , new Type[]
					   {Hibernate.STRING, Hibernate.STRING});

		//删除与该单位相关的联系方式信息
		session.delete("select address from AddressInfoForm address where address.comp_id.taskID = ? and address.comp_id.unitID = ?",
					   new Object[]
					   {task.id(), unitID}
					   , new Type[]
					   {Hibernate.STRING, Hibernate.STRING});

		//删除与该单位相关的填报情况
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
	 * 得到创建单位的sql
	 * @return 创建单位的sql
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
	 * 得到删除单位的sql
	 * @return 删除单位的sql
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
	 * 得到更新单位的sql
	 * @return 更新单位的sql
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
	 * 得到修改数据表中单位id的PrepareStatement集合
	 * @param session Session对象
	 * @return 修改数据表中单位id的PrepareStatement集合
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
	 * 得到修改数据表中单位id的sql集合
	 * @return 修改数据表中单位id的sql集合
	 * @throws HibernateException
	 */
	private Collection getUpdateUnitIDSqls() throws HibernateException
	{
		try
		{
			if (updateUnitIDSqls == null)
			{
				updateUnitIDSqls = new LinkedList();

				//构造更新每张数据表的sql
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

				//附件表
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
	 * 根据选择汇总方案得到选择汇总森林
	 * @param schema 选择汇总方案
	 * @return 选择汇总单位DBUnitTreeNode森林
	 */
	public Collection getSelectSumUnitForest(Selsum schema)
	{
		Collection result = new TreeSet();
		//构建树节点
		for (int i = 0; i < schema.getNodeCount(); i++)
		{
			Node root = schema.getNode(i);
			DBUnitTreeNode tree = getSelectSumUnitTree(root, schema);
			result.add(tree);
		}
		return result;
	}

	/**
	 * 得到指定选择汇总单位的选择汇总条件
	 * @param unit 选择汇总单位
	 * @param schema 选择汇总方案
	 * @return 选择汇总条件
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
	 * 根据指定根节点的选择汇总单位树
	 * @param root 根节点
	 * @param schema 选择汇总方案
	 * @return 单位树
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
			//第归
			unit.addChild(getSelectSumUnitTree(root.getNode(i), schema));
		}

		return unit;
	}

	/**
	 * 获得指定任务的单位树，以unitID指定的单位为根 若unitID不存在则返回null
	 * @param unitID 单位ID号
	 * @return 单位树
	 * @throws ModelException
	 */
	public UnitTreeNode getUnitTree(String unitID) throws ModelException
	{
		DBUnitTreeNode result = getTree(unitID, unitForest);
//		if (result == null)
//		{
//			throw new ModelException("指定的单位没有找到，unitID=“" + unitID + "”");
//		}
		return result;
	}

	/**
	 * 精简单位，将不是根的单位去掉
	 * @param units 需要精简的单位DBUnitTreeNode集合
	 * @return 精简后的单位DBUnitTreeNode集合
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
	 * 构造选择汇总单位森林，将单位森林中满足选择条件的单位加到选择汇总单位森林中
	 * @param schema 选择汇总方案
	 * @param taskTime 任务时间
	 * @param unitACL 单位ACL
	 * @return 选择汇总单位DBUnitTreeNode森林
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
	 * 从单位森林中得到指定单位为根的单位树
	 * @param unitID 指定单位的id
	 * @param forest 单位森林
	 * @return 以指定单位为根的单位树。如果没有找到，返回null
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
	 * 从单位树中得到指定单位子树
	 * @param unitID 单位id
	 * @param tree 单位树
	 * @return 指定单位子树，如果没找到，返回null
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
	 * 构造选择汇总单位树，将单位森林满足选择条件的单位加到选择汇总单位树中
	 * @param tree 选择汇总单位树
	 * @param forest 单位森林
	 * @param taskTime 任务时间
	 * @return 选择汇总单位树
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
			getUnits(forest, id2nodeMap); // 这个递归不知道是否有问题
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
					//判断是否满足选择条件
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
	 * 从选择汇总树中得到选择汇总单位集合
	 * @param tree 选择汇总树
	 * @return 选择汇总单位DBUnitTreeNode集合
	 */
	private TreeSet getSelecSumtUnits(DBUnitTreeNode tree)
	{
		TreeSet units = new TreeSet();

		//加入根节点
		units.add(tree);

		//对子树第归
		for (Iterator itr = tree.getChildren(); itr.hasNext(); )
		{
			DBUnitTreeNode subTree = (DBUnitTreeNode) itr.next();
			units.addAll(getSelecSumtUnits(subTree));
		}

		return units;
	}

	/**
	 * 从单位DBUnitTreeNode森林中得到所有的单位
	 * @param forest 单位DBUnitTreeNode森林
	 * @param map 森林中所有单位的unitID/DBUnitTreeNode Map
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
	 * 从单位树中得到所有的单位
	 * @param tree 单位树
	 * @return 单位树中所有的单位DBUnitTreeNode集合
	 */
	private TreeSet getUnits(DBUnitTreeNode tree)
	{
		TreeSet units = new TreeSet();
		units.add(tree);

		for (Iterator itr = tree.getChildren(); itr.hasNext(); )
		{
			//第归
			units.addAll(getUnits( (DBUnitTreeNode) itr.next()));
		}
		return units;
	}

	/**
	 * 得到单位森林
	 * @param unitACL 访问控制权限
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest(UnitACL unitACL) throws ModelException
	{
		Session session = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			//根据得到所有该用户有权限表中存在的单位
			String[] permissionUnitIDs = getUnitIDs(unitACL, session);
			//得到单位对象
			Collection units = getUnits(permissionUnitIDs);
			//精简单位，将不是根的单位去掉
			Collection roots = reduceUnit(units);

			return roots.iterator();
		}
		catch (HibernateException ex)
		{
			throw new ModelException("查询用户森林失败", ex);
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
	 * 根据访问控制列表，得到所有可访问的单位id
	 * @param unitACL 单位访问控制列表
	 * @param session Hibernate对象
	 * @return 单位id的数组
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
	 * 根据paramname加载单位森林
	 * @param session Hibernate Session
	 * @throws HibernateException
	 */
	private void loadUnitForest(Session session,String paramname) throws HibernateException
	{
		//查找，并将查找的结果转换为Unit对象，从而得到对象集合
		TreeMap allUnits = getAllUnitTreeNodes(task.id(), session,paramname);
		//得到森林(即以所有的根节点为根的树所组成的森林)
		unitForest = getForest(allUnits);
	}
	
	/**
	 * 根据给定的taskID 查找该任务中的所有的单位(以单位对象集合的形式返回)
	 * @param taskID 任务ID号
	 * @param session Hibernate对象
	 * @return 单位对象的集合
	 * @throws HibernateException
	 */
	private TreeMap getAllUnitTreeNodes(String taskID, Session session,String paramname) throws
		HibernateException
	{
		TreeMap result = null;

		//查找该任务的所有单位获得结果集 ResultSet
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			con = session.connection();
			String sql = "select * from " + unitMetaTableName +" where  qymc like '%"+paramname+"%'";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			//将结果集转化为　DBUnitTreeNode 对象集合
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
	 * 得到单位森林
	 * @return each iterator element is a UnitTreeNode object
	 * @throws ModelException
	 */
	public Iterator getUnitForest() throws ModelException
	{
		return unitForest.iterator();
	}
	/**
	 * 获得单位数据对象(单独的对象，不包括下级节点)
	 * @param unitIDs 单位ID集合
	 * @return 单位对象集合(UnitTreeNode)
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
	 * 根据给定的taskID 查找该任务中的所有的单位(以单位对象集合的形式返回)
	 * 根据给的paramname  得到单元数据信息
	 * @param taskID 任务ID号
	 * @param session Hibernate对象
	 * @return 单位对象的集合
	 * @throws HibernateException
	 */
	public List<DBUnitTreeNode> getAllUnitTreeNodes(String taskID,String paramname,UnitACL unitACL) {
		List<DBUnitTreeNode> list = new ArrayList<DBUnitTreeNode>();
		Session session = null;
		try{
			session = HibernateUtil.getSessionFactory().openSession();
			//1.获取该用户权限数据信息
			String[] permissionUnitIDs = getUnitIDs(unitACL, session);
			//2.获取根据paramname得到的单元数据 得到单位对象
			//2.1 得到树形结构信息
			TreeMap allUnits = getAllUnitTreeNodes(taskID, session,paramname);
			Set unitForest = getForest(allUnits);
			Collection result = getUnits(permissionUnitIDs,paramname,unitForest);
			Iterator iterator = result.iterator();
			while (iterator.hasNext()) {
				DBUnitTreeNode elem = (DBUnitTreeNode) iterator.next();
				list.add(elem);
//				System.out.println("--1257---->"+elem.getUnitName()+"  "+elem.getReportType()+"<------");
			}
			//3.组合成单元森林然后返回
//			TreeMap allUnits = getAllUnitTreeNodes(taskID, session,paramname);
//			unitForest = getForest(allUnits);
//			result = getForest(allUnits);
//			result = unitforest.iterator();
			/*	//得到单位对象
				Collection units = getUnits(permissionUnitIDs);
				//精简单位，将不是根的单位去掉
				Collection roots = reduceUnit(units);
				return roots.iterator();*/
			}
			catch (HibernateException ex){
				log.error("查询单位名称数据信息",ex);
			}
			finally{
				if (session != null){
					try{
						session.close();
					}
					catch (HibernateException ex2){
						log.error("关闭查询单位名称数据信息",ex2);
					}
				}
			}
		return list;
	}
	
	/**
	 * 加载单位森林
	 * @param session Hibernate Session
	 * @throws HibernateException
	 */
	private void loadUnitForest(Session session) throws HibernateException
	{
		//查找，并将查找的结果转换为Unit对象，从而得到对象集合
		TreeMap allUnits = getAllUnitTreeNodes(task.id(), session);
		//得到森林(即以所有的根节点为根的树所组成的森林)
		unitForest = getForest(allUnits);
	}

	/**
	 * 判断单位是否存在
	 * @param unitID 单位ID号
	 * @return 布尔值
	 */
	public boolean isUnitExist(String unitID)
	{
		return (getTree(unitID, unitForest) != null);
	}

	/**
	 * 得到森林(即以所有的根节点为根的树所组成的森林)
	 * @param allUnits 所有的单位的集合
	 * @return 森林
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

			//尝试找该节点的父节点
			if (parentID != null && !parentID.trim().equals("") &&
				allUnits.get(parentID) != null)
			{
				//设置父子关系
				DBUnitTreeNode parent = ( (DBUnitTreeNode) allUnits.get(
					parentID));
				parent.addChild(unit);
				unit.setParent(parent);
			}
			else if (p_ParaentID != null && !p_ParaentID.trim().equals("") &&
					 allUnits.get(p_ParaentID) != null)
			{
				//设置父子关系
				DBUnitTreeNode parent = ( (DBUnitTreeNode) allUnits.get(
					p_ParaentID));
				parent.addChild(unit);
				unit.setParent(parent);
			}
			else
			{
				//不存在父节点，该节点为根
				result.add(unit);
			}
		}
		if( log.isDebugEnabled() )
			log.debug( "生成森林消耗时间(单位: 毫秒)："
					  + (System.currentTimeMillis() - start) );

		return result;
	}

	
	
	
	/**
	 * 根据给定的taskID 查找该任务中的所有的单位(以单位对象集合的形式返回)
	 * @param taskID 任务ID号
	 * @param session Hibernate对象
	 * @return 单位对象的集合
	 * @throws HibernateException
	 */
	private TreeMap getAllUnitTreeNodes(String taskID, Session session) throws
		HibernateException
	{
		TreeMap result = null;

		//查找该任务的所有单位获得结果集 ResultSet
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try
		{
			con = session.connection();
			String sql = "select * from " + unitMetaTableName;
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			//将结果集转化为　DBUnitTreeNode 对象集合
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
	 * 将结果集转化为对象的集合
	 * @param rs 单位表的结果集
	 * @return 对象集合
	 * @throws SQLException
	 */
	private TreeMap resultSet2DBUnitTreeNodeMap(ResultSet rs) throws
		SQLException
	{
		TreeMap result = new TreeMap();
		while (rs.next())
		{
			DBUnitTreeNode dbUnitTreeNode = resultSet2DBUnitTreeNode(rs);
			//将Unit对象添加到结果中
			result.put(dbUnitTreeNode.id(), dbUnitTreeNode);
		}
		return result;
	}

	private DBUnitTreeNode resultSet2DBUnitTreeNode(ResultSet rs) throws
		SQLException
	{
		DBUnitTreeNode dbUnitTreeNode = new DBUnitTreeNode();
		//设置unit对象的成员变量　
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
	 * 创建单位ID
	 * @param unitCode 单位代码
	 * @param reportType 报表类型
	 * @return 单位ID
	 */
	private String generateUnitID(String unitCode, String reportType)
	{
		return unitCode + reportType;
	}
}
