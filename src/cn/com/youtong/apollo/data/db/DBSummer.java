package cn.com.youtong.apollo.data.db;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.form.*;
import cn.com.youtong.apollo.data.xml.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.task.*;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;
import cn.com.youtong.apollo.common.Convertor;

/**
 * 报表数据汇总器，执行各种汇总操作
 */
class DBSummer
    implements Summer {

  /** DBUnitTreeManager实例 */
  private DBUnitTreeManager treeManager;

  private Log log = LogFactory.getLog(this.getClass());
  ;

  /** 报表任务定义 */
  private Task task;

  public DBSummer(Task task, DBUnitTreeManager treeManager) {
    this.task = task;
    this.treeManager = treeManager;
  }

  /**
   * 执行选择汇总方案
   * @param schemaID 方案id
   * @param taskTime 任务时间id
   * @param unitACL 单位ACL
   * @throws ModelException
   */
  public void executeSelectSumSchema(Integer schemaID,
                                     cn.com.youtong.apollo.task.TaskTime
                                     taskTime, UnitACL unitACL) throws
      ModelException {
    Session session = null;
    Transaction tx = null;

    //选择汇总单位id集合
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      if (!isSelectSumSchemaDefined(schemaID, session)) {
        throw new ModelException("指定的选择汇总方案不存在");
      }

      log.info("开始执行选择汇总方案");
      DBSelectSumSchema schema = getSelectSumSchema(schemaID, session);

      Collection forest = treeManager.
          buildSelectSumUnitForest(schema,
                                   taskTime, unitACL);

      for (Iterator itr = forest.iterator(); itr.hasNext(); ) {
        UnitTreeNode unitTree = (UnitTreeNode) itr.next();
        Collection sumNodeUnits = getSelectSumUnits(unitTree);

        //对每个选择汇总节点进行汇总
        Iterator unitItr = sumNodeUnits.iterator();
        while (unitItr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) unitItr.next();
          unitIDs.add(unit.id());
          sumNode(unit, taskTime, session);
        }
      }
      tx.commit();
      log.info("成功执行选择汇总方案");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "执行选择汇总方案失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //对汇总单位执行脚本
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), taskTime);
  }

  /**
   * 检查节点汇总单位下的所有节点汇总结果是否正确
   * @param unitID 节点汇总单位ID
   * @param time 任务时间
   * @param isRecursive 是否递归汇总所有子节点
   * @return 检查结果ValidateResult对象集合的Iterator
   * @throws ModelException
   */
  public Iterator validateNodeSum(String unitID,
                                  cn.com.youtong.apollo.task.TaskTime time,
                                  boolean isRecursive) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      LinkedList result = new LinkedList();
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      UnitTreeNode unitTree = treeManager.getUnitTree(unitID);
      if (!isSumNodeUnit(unitTree)) {
        throw new ModelException("不能对指定的单位“" + unitTree.id() +
                                 "”检查汇总结果");
      }

      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //检查每个节点的汇总结果
        Iterator itr = sumNodeUnits.iterator();
        while (itr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) itr.next();
          log.info("开始检查单位“" + unit.id() + "”的汇总结果");
          result.add(validateNodeSum(unit, time, session));
        }
      }
      else {
        log.info("开始检查单位“" + unitTree.id() + "”的汇总结果");
        result.add(validateNodeSum(unitTree, time, session));
      }

      tx.commit();
      return result.iterator();
    }
    catch (HibernateException ex) {
      String message = "检查单位“" + unitID + "”的汇总结果失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      try {
        if (session != null) {
          session.close();
        }
      }
      catch (HibernateException ex1) {
      }
    }
  }

  /**
   * 节点汇总
   * @param unitID 汇总节点单位ID
   * @param time 任务时间
   * @param isRecursive 是否递归汇总所有子节点
   * @throws ModelException
   */
  public void sumNode(String unitID, cn.com.youtong.apollo.task.TaskTime time,
                      boolean isRecursive) throws ModelException {
    Session session = null;
    Transaction tx = null;
    //汇总单位ID集合
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      UnitTreeNode unitTree = treeManager.getUnitTree(unitID);
      if (!isSumNodeUnit(unitTree)) {
        throw new ModelException("不能对指定的单位“" + unitTree.id() +
                                 "”进行节点汇总");
      }
      log.info("开始对单位“" + unitID + "”节点汇总");
      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //对每个节点进行汇总
        Iterator itr = sumNodeUnits.iterator();
        while (itr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) itr.next();
          unitIDs.add(unit.id());
          sumNode(unit, time, session);
        }
      }
      else {
        unitIDs.add(unitTree.id());
        sumNode(unitTree, time, session);
      }

      updateSubmitData(unitID,time,session);

      tx.commit();

      log.info("成功完成对单位“" + unitID + "”节点汇总");
    }
    catch (HibernateException ex) {
     ex.printStackTrace(); 
     try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "对单位“" + unitID + "”节点汇总失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //对汇总单位执行脚本
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), time);
  }

  /**
   * 更新时间
   * @param unitID
   * @param time
   */
  private void updateSubmitData(String unitID,
                                cn.com.youtong.apollo.task.TaskTime time,
                                Session session) {
    String taskID = task.id();
    String now = Convertor.date2String(new java.util.Date());
    String sql = "update ytapl_fillstate set FILLDATE='" + now +
        "' where UNITID='" + unitID + "' and TASKID='" + taskID +
        "' and TASKTIMEID='" + time.getTaskTimeID().toString() + "'";
    try {
      Connection conn = session.connection();
      Statement pst = conn.createStatement();
      pst.execute(sql);
      pst.close();
    }
    catch (Exception ex) {
        ex.printStackTrace();
    }

  }

  /**
   * 选择汇总
   * @param unitID 保存汇总结果的单位ID
   * @param unitIDs 要汇总的单位ID集合
   * @param time 汇总的任务时间
   * @throws ModelException
   */
  public void sumUnits(String unitID, Collection unitIDs,
                       cn.com.youtong.apollo.task.TaskTime time) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      String unitIDStr = getUnitIDsString(unitIDs);
      log.info("开始选择汇总，将单位“" + unitIDStr + "”汇总到单位“" +
               unitID + "”");
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      sum(unitID2Unit(unitID), unitID2Unit(unitIDs), time, session);
      tx.commit();
      log.info("成功完成选择汇总，将单位“" + unitIDStr + "”汇总到单位“" +
               unitID + "”");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "选择汇总失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //对汇总单位执行脚本
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    LinkedList IDs = new LinkedList();
    IDs.add(unitID);
    importer.excuteScripts(IDs.iterator(), time);

  }

  /**
   * 完全汇总。汇总所有非选择汇总,完全汇总和节点汇总单位
   * @param unitID 保存汇总结果的单位ID
   * @param time 汇总的任务时间
   * @throws ModelException
   */
  public void sumAll(String unitID, cn.com.youtong.apollo.task.TaskTime time) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      log.info("开始完全汇总");
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      Collection units = getSumAllUnits();
      sum(unitID2Unit(unitID), units, time, session);
      tx.commit();
      log.info("成功完成完全汇总");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "完全汇总失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //对汇总单位执行脚本
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    LinkedList unitIDs = new LinkedList();
    unitIDs.add(unitID);
    importer.excuteScripts(unitIDs.iterator(), time);

  }

  /**
   * 调整节点差额表
   * @param unitID 汇总节点单位ID
   * @param time 任务时间
   * @param isRecursive 是否递归调整所有子节点
   * @throws ModelException
   */
  public void adjustNodeDiff(String unitID,
                             cn.com.youtong.apollo.task.TaskTime time,
                             boolean isRecursive) throws ModelException {
    Session session = null;
    Transaction tx = null;

    //调整差额表的单位ID集合
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      UnitTreeNode unitTree = treeManager.getUnitTree(unitID);
      if (!isSumNodeUnit(unitTree)) {
        throw new ModelException("不能对指定的单位“" + unitTree.id() +
                                 "”进行调整差额表操作");
      }
      log.info("开始对单位“" + unitID + "”调整差额");
      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //对每个节点进行调整差额表
        Iterator itr = sumNodeUnits.iterator();
        while (itr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) itr.next();
          if (isGroupSumUnit(unit)) {
            unitIDs.add(getGroupDiffUnit(unitTree).id());
            adjustNodeDiff(unit, time, session);
          }
        }
      }
      else {
        if (isGroupSumUnit(unitTree)) {
          unitIDs.add(getGroupDiffUnit(unitTree).id());
          adjustNodeDiff(unitTree, time, session);
        }
      }
      tx.commit();
      log.info("成功完成对单位“" + unitID + "”调整差额");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "对单位“" + unitID + "”调整差额失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //对汇总单位执行脚本
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), time);
  }

  /**
   * 判断指定的单位是否是完全汇总单位
   * @param unit 单位对象
   * @return 是完全汇总单位，返回true；否则返回false
   */
  private boolean isSumAllUnit(UnitTreeNode unit) {
    return ReportType.FULL_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * 判断指定的单位是否是节点汇总单位
   * @param unit 单位对象
   * @return 是节点汇总单位，返回true；否则返回false
   */
  private boolean isSumNodeUnit(UnitTreeNode unit) {
    return unit.getChildren().hasNext();
  }

  /**
   * 判断指定的单位是否是选择汇总单位
   * @param unit 单位对象
   * @return 是选择汇总单位，返回true；否则返回false
   */
  private boolean isSelectSumUnit(UnitTreeNode unit) {
    return ReportType.SELECT_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * 判断指定的单位是否是集团汇总单位
   * @param unit 单位对象
   * @return 是集团汇总单位，返回true；否则返回false
   */
  private boolean isGroupSumUnit(UnitTreeNode unit) {
    return ReportType.GROUP_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * 得到指定单位树中所有要参与汇总的集团汇总类型节点
   * @param unitTree 单位树
   * @return 单位集合，已经按汇总顺序排好
   */
  private Collection getGroupSumUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addGroupSumUnit(unitTree, units);
    return units;
  }

  /**
   * 将单位树中的集团汇总类型节点加入到指定单位集合中
   * @param unitTree 单位树
   * @param units 存放单位的目标集合
   */
  private void addGroupSumUnit(UnitTreeNode unitTree, LinkedList units) {
    if (ReportType.GROUP_GATHER_TYPE.equals(unitTree.getReportType())) {
      //作为集合的第一个元素，这样保证汇总顺序是从底层集团汇总单位开始
      units.add(0, unitTree);
    }
    //遍历子树
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addGroupSumUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * 单个节点汇总（非递归）
   * @param unit 汇总节点单位
   * @param time 任务时间
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void sumNode(UnitTreeNode unit,
                       cn.com.youtong.apollo.task.TaskTime time,
                       Session session) throws HibernateException {
    Collection units = getUnitsInSumNode(unit);
    sum(unit, units, time, session);
  }

  /**
   * 对指定的单位集合进行汇总，并将结果保存到指定单位中
   * @param unit 保存汇总结果的单位
   * @param units 参与汇总的单位集合
   * @param time 任务时间
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void sum(UnitTreeNode unit, Collection units,
                   cn.com.youtong.apollo.task.TaskTime time, Session session) throws
      HibernateException {
    String tempTableName = null;
    try {
      tempTableName = createUnitTempTable(units, session);
      //分别对每张汇总表进行汇总
      Collection tables = getSumTables();
      Iterator itr = tables.iterator();
      while (itr.hasNext()) {
        Table sumTable = (Table) itr.next();
        sumTable(sumTable, unit, time, tempTableName, session);
      }
    }
    finally {
      try {
        dropUnitTempTable(tempTableName, session);
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * 单个节点调整差额表（非递归）
   * @param unit 汇总节点单位
   * @param time 任务时间
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void adjustNodeDiff(UnitTreeNode unit,
                              cn.com.youtong.apollo.task.TaskTime time,
                              Session session) throws HibernateException {
    String tempTableName = null;
    try {
      Collection units = getUnitsInAdjustNodeDiff(unit);
      tempTableName = createUnitTempTable(units, session);
      //分别对每张汇总表进行调整差额
      Collection tables = getSumTables();
      Iterator itr = tables.iterator();
      while (itr.hasNext()) {
        Table sumTable = (Table) itr.next();
        adjustNodeDiffTable(sumTable, unit, time, tempTableName,
                            session);
      }
    }
    finally {
      try {
        dropUnitTempTable(tempTableName, session);
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * 创建选择汇总方案
   * @param schemaStream 方案流
   * @throws ModelException
   */
  public void createSelectSumSchema(InputStream schemaStream) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      Selsum schema = Selsum.unmarshal(new InputStreamReader(schemaStream,
          "gb2312"));
      log.info("开始创建选择汇总方案");

      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();

      //创建选择汇总单位树
      treeManager.createSelectSumUnitForest(schema, session);

      //保存方案
      createSelectSumSchema(schema, session);

      tx.commit();

      //更新当前UnitTreeManager
      this.treeManager.update();

      log.info("成功创建选择汇总方案");
    }
    catch (Exception ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex1) {
      }
      String message = "创建汇总模板失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      try {
        if (session != null) {
          session.close();
        }
      }
      catch (Exception ex2) {
      }
    }
  }

  /**
   * 将选择汇总方案保存到数据库中
   * @param schema 方案
   * @param session Session对象
   * @throws HibernateException
   */
  private void createSelectSumSchema(Selsum schema, Session session) throws
      HibernateException {
    try {
      //先删除
      session.delete("select schema from SelectSumSchemaForm schema where schema.name = ? and schema.taskID = ?",
                     new Object[] {schema.getName(), task.id()}
                     , new Type[] {Hibernate.STRING, Hibernate.STRING});

      //后创建
      SelectSumSchemaForm schemaForm = new SelectSumSchemaForm(
          HibernateUtil.getNextSelectSumSchemaID(),
          schema.getName(),
          task.id(),
          "", new java.util.Date(), new java.util.Date(),
          Hibernate.createClob(" "));
      session.save(schemaForm);
      session.flush();
      session.refresh(schemaForm, LockMode.UPGRADE);

      //update clob
      String updateSql =
          "UPDATE ytapl_selectsumschemas SET content = ? WHERE schemaID = " +
          schemaForm.getSchemaID();
      StringWriter sw = new StringWriter();
      schema.marshal(sw);
      sw.close();
      Config.getCurrentDatabase().UpdateClob(schemaForm.getContent(),
                                             sw.toString(), updateSql,
                                             session.connection());
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
  }

  /**
   * 得到指定任务的所有选择汇总方案
   * @taskID 任务ID
   * @return 选择汇总方案Iterator
   * @throws ModelException
   */
  public Iterator getAllSelectSumSchemas(String taskID) throws ModelException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      return getAllSelectSumSchemas(taskID, session).iterator();
    }
    catch (HibernateException ex) {
      String message = "不能得到选择汇总模板";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      try {
        if (session != null) {
          session.close();
        }
      }
      catch (Exception ex2) {
      }
    }
  }

  /**
   * 得到指定的选择汇总方案
   * @param schemaID 方案id
   * @return 选择汇总方案
   * @throws ModelException
   */
  public SelectSumSchema getSelectSumSchema(Integer schemaID) throws
      ModelException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      if (isSelectSumSchemaDefined(schemaID, session)) {
        return getSelectSumSchema(schemaID, session);
      }
      throw new ModelException("指定的选择汇总方案不存在");
    }
    catch (HibernateException ex) {
      String message = "不能得到选择汇总模板";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      try {
        if (session != null) {
          session.close();
        }
      }
      catch (Exception ex2) {
      }
    }
  }

  /**
   * 得到指定任务的所有选择汇总方案
   * @taskID 任务ID
   * @param session Session对象
   * @return 选择汇总方案SelectSumSchema集合
   * @throws HibernateException
   */
  private Collection getAllSelectSumSchemas(String taskID, Session session) throws
      HibernateException {
    LinkedList result = new LinkedList();
    List list = session.find(
        "from SelectSumSchemaForm schema where schema.taskID = ?", taskID,
        Hibernate.STRING);
    for (int i = 0; i < list.size(); i++) {
      SelectSumSchemaForm schemaForm = (SelectSumSchemaForm) list.get(i);
      result.add(getSelectSumSchema(schemaForm));
    }
    return result;
  }

  /**
   * 得到指定的选择汇总方案
   * @param schemaID 方案id
   * @param session Session对象
   * @return 选择汇总方案
   * @throws HibernateException
   */
  private DBSelectSumSchema getSelectSumSchema(Integer schemaID,
                                               Session session) throws
      HibernateException {
    SelectSumSchemaForm schemaForm = new SelectSumSchemaForm();
    session.load(schemaForm, schemaID);
    return getSelectSumSchema(schemaForm);
  }

  /**
   * 删除选择汇总方案
   * @param schemaID 方案id
   * @throws ModelException
   */
  public void deleteSelectSumSchema(Integer schemaID) throws ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isSelectSumSchemaDefined(schemaID, session)) {
        log.info("开始删除选择汇总方案");
        tx = session.beginTransaction();
        session.delete(
            "select schema from SelectSumSchemaForm schema where schema.schemaID = ?",
            schemaID, Hibernate.INTEGER);
        tx.commit();
        log.info("成功删除选择汇总方案");
      }
      else {
        throw new ModelException("指定的选择汇总方案不存在");
      }
    }
    catch (Exception ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex1) {
      }
      String message = "创建汇总模板失败";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      try {
        if (session != null) {
          session.close();
        }
      }
      catch (Exception ex2) {
      }
    }
  }

  /**
   * 得到指定的选择汇总方案
   * @param schemaForm 方案Form
   * @return 选择汇总方案
   * @throws HibernateException
   */
  private DBSelectSumSchema getSelectSumSchema(SelectSumSchemaForm schemaForm) throws
      HibernateException {
    try {
      Clob clob = schemaForm.getContent();
      Selsum schema = Selsum.unmarshal(clob.getCharacterStream());

      return new DBSelectSumSchema(schemaForm,
                                   treeManager.
                                   getSelectSumUnitForest(schema));
    }
    catch (Exception ex) {
      throw new HibernateException("不能得到选择汇总方案", ex);
    }
  }

  /**
   * 对指定表进行汇总，并将汇总结果作为指定单位的数据
   * @param sumTable 要汇总的表
   * @param unit 单位
   * @param time 任务时间
   * @param unitTempTableName 保存参与汇总的单位信息的临时表
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void sumTable(Table sumTable, UnitTreeNode unit,
                        cn.com.youtong.apollo.task.TaskTime time,
                        String unitTempTableName, Session session) throws
      HibernateException {
    Collection sumCells = getSumCells(sumTable);
    //汇总固定单元格
    if (sumCells.size() > 0) {
      clearResult(sumTable, unit, time, session);
      HashMap result = getSumResult(sumTable, time, unitTempTableName,
                                    session);
      saveResult(sumTable, unit, time, result, session);
    }
    //汇总浮动行
    Collection sumFloatRows = getSumFloatRows(sumTable);
    Iterator itr = sumFloatRows.iterator();
    while (itr.hasNext()) {
      Row row = (Row) itr.next();
      clearResult(row, unit, time, session);
      HashMap result = getSumResult(row, time, unitTempTableName, session);
      saveResult(row, unit, time, result, session);
    }
  }

  /**
   * 清空指定表的结果
   * @param table 表
   * @param unit 单位
   * @param time 任务时间
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void clearResult(Table table, UnitTreeNode unit,
                           cn.com.youtong.apollo.task.TaskTime time,
                           Session session) throws HibernateException {
    PreparedStatement pst = null;
    try {
      String tableName = NameGenerator.generateDataTableName(
          task.id(),
          table.id());
      String sql = "DELETE FROM " + tableName +
          " WHERE unitID = ? AND taskTimeID = ?";
      Connection conn = session.connection();
      pst = conn.prepareStatement(sql);
      pst.setString(1, unit.id());
      pst.setString(2, time.getTaskTimeID().toString());
      pst.execute();
      pst.close();
    }
    catch (SQLException ex) {
      throw new HibernateException(ex);
    }
    finally {
      if (pst != null) {
        try {
          pst.close();
        }
        catch (SQLException ex1) {
        }
      }
    }
  }

  /**
   * 清空指定浮动行的结果
   * @param floatRow 浮动行
   * @param unit 单位
   * @param time 任务时间
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void clearResult(Row floatRow, UnitTreeNode unit,
                           cn.com.youtong.apollo.task.TaskTime time,
                           Session session) throws HibernateException {

  }

  /**
   * 得到指定表汇总结果
   * @param sumTable 汇总表
   * @param time 任务时间
   * @param tempTableName 存放参与汇总的单位ID的临时表名
   * @param session Session对象
   * @return 汇总结果，以DBFieldName(String), Value(Double)的值对表示
   * @throws HibernateException 底层异常
   */
  private HashMap getSumResult(Table sumTable,
                               cn.com.youtong.apollo.task.TaskTime time,
                               String tempTableName, Session session) throws
      HibernateException {
    PreparedStatement pst = null;
    try {
      Collection sumCells = getSumCells(sumTable);
      String tableName = NameGenerator.generateDataTableName(
          task.id(),
          sumTable.id());
      String sql = getSumResultSql(sumCells, time,
                                   NameGenerator.
                                   generateDataTableName(
          task.id(),
          sumTable.id()), tempTableName);
      Connection conn = session.connection();
      pst = conn.prepareStatement(sql);
      ResultSet rs = pst.executeQuery();
      rs.next();
      HashMap result = getResultFromResultSet(rs, sumCells);
      pst.close();
      return result;
    }
    catch (SQLException ex) {
      throw new HibernateException(ex);
    }
    finally {
      if (pst != null) {
        try {
          pst.close();
        }
        catch (SQLException ex1) {
        }
      }
    }
  }

  /**
   * 得到指定汇总表调整差额的结果
   * @param groupSumUnit 集团汇总单位
   * @param sumTable 汇总表
   * @param time 任务时间
   * @param tempTableName 存放参与调整差额的单位ID的临时表名
   * @param session Session对象
   * @return 汇总结果，以DBFieldName(String), Value(Double)的值对表示
   * @throws HibernateException 底层异常
   */
  private HashMap getAdjustNodeDiffResult(UnitTreeNode groupSumUnit,
                                          Table sumTable,
                                          cn.com.youtong.apollo.task.TaskTime
                                          time, String tempTableName,
                                          Session session) throws
      HibernateException {
    HashMap groupSumValue = getGroupSumUnitValue(groupSumUnit, sumTable,
                                                 time, session);
    HashMap adjustNodeDiffUnitsValue = getSumResult(sumTable, time,
        tempTableName, session);
    return getDiffValue(groupSumValue, adjustNodeDiffUnitsValue);
  }

  /**
   * 判断指定的选择汇总方案是否存在
   * @param schemaID 选择汇总方案id
   * @param session Session对象
   * @return 指定的选择汇总方案是否存在，返回true；否则返回false
   * @throws HibernateException
   */
  private boolean isSelectSumSchemaDefined(Integer schemaID, Session session) throws
      HibernateException {
    return ( (Integer) session.iterate(
        "SELECT COUNT(*) from SelectSumSchemaForm as schema WHERE schema.schemaID = ?",
        schemaID, Hibernate.INTEGER).next()).intValue() > 0;
  }

  /**
   * 得到集团汇总单位的指定表的所有汇总单元格的值
   * @param groupSumUnit 集团汇总单位
   * @param sumTable 汇总表
   * @param time 任务时间
   * @param session Session对象
   * @return 汇总单元格的值，以DBFieldName(String), Value(Double)的值对表示
   * @throws HibernateException 底层异常
   */
  private HashMap getGroupSumUnitValue(UnitTreeNode groupSumUnit,
                                       Table sumTable,
                                       cn.com.youtong.apollo.task.TaskTime
                                       time, Session session) throws
      HibernateException {
    String tempTableName = null;
    try {
      ArrayList units = new ArrayList();
      units.add(groupSumUnit);
      tempTableName = createUnitTempTable(units, session);
      return getSumResult(sumTable, time, tempTableName, session);
    }
    finally {
      try {
        dropUnitTempTable(tempTableName, session);
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * 得到两个HashMap的差
   * @param value1 被减数
   * @param value2 减数
   * @return 差
   */
  private HashMap getDiffValue(HashMap value1, HashMap value2) {
    HashMap result = new HashMap();
    Set keys = value1.keySet();
    Iterator itr = keys.iterator();
    while (itr.hasNext()) {
      Object key = itr.next();
      result.put(key,
                 new Double( ( (Double) value1.get(key)).doubleValue() -
                            ( (Double) value2.get(key)).doubleValue()));
    }
    return result;
  }

  /**
   * 得到指定浮动行汇总结果
   * @param sumFloatRow 汇总浮动行
   * @param time 任务时间
   * @param tempTableName 存放参与汇总的单位ID的临时表名
   * @param session Session对象
   * @return 汇总结果，以DBFieldName(String), Value(Double)的值对表示
   * @throws HibernateException 底层异常
   */
  private HashMap getSumResult(Row sumFloatRow,
                               cn.com.youtong.apollo.task.TaskTime time,
                               String tempTableName, Session session) throws
      HibernateException {
    return null;
  }

  /**
   * 得到指定浮动行调整差额的结果
   * @param sumFloatRow 浮动行
   * @param time 任务时间
   * @param tempTableName 存放参与调整差额的单位ID的临时表名
   * @param session Session对象
   * @return 调整差额结果，以DBFieldName(String), Value(Double)的值对表示
   * @throws HibernateException 底层异常
   */
  private HashMap getAdjustNodeDiffResult(Row sumFloatRow,
                                          cn.com.youtong.apollo.task.TaskTime
                                          time, String tempTableName,
                                          Session session) throws
      HibernateException {
    return null;
  }

  /**
   * 从ResultSet中取得结果
   * @param rs ResultSet对象
   * @param cells Cell集合包含了DBFieldName信息
   * @return 结果，以DBFieldName(String), Value(Double)的值对表示
   * @throws SQLException 底层异常
   */
  private HashMap getResultFromResultSet(ResultSet rs, Collection cells) throws
      SQLException {
    HashMap result = new HashMap();
    Iterator itr = cells.iterator();
    int count = 0;
    while (itr.hasNext()) {
      count++;
      result.put( ( (Cell) itr.next()).getDBFieldName(),
                 new Double(rs.getDouble(count)));
    }
    return result;
  }

  /**
   * 得到产生汇总结果的sql
   * @param cells cells 参加汇总的单元格集合
   * @param time 任务时间
   * @param sumTableName 汇总表的名称
   * @param unitTempTableName 存放汇总单位信息的临时表的名称
   * @return 汇总sql
   */
  private String getSumResultSql(Collection cells,
                                 cn.com.youtong.apollo.task.TaskTime time,
                                 String sumTableName,
                                 String unitTempTableName) {
    String sql = " SELECT ";
    Iterator itr = cells.iterator();
    int count = 0;
    while (itr.hasNext()) {
      if (count > 0) {
        sql += " , ";
      }
      sql = sql + " SUM(" + ( (Cell) itr.next()).getDBFieldName() + ") "; // AS expr" + count + " ";
      count++;
    }
    return sql + " FROM " + sumTableName + " WHERE taskTimeID = " +
        time.getTaskTimeID() + " AND unitID IN (SELECT unitID FROM " +
        unitTempTableName + ")";
  }

  /**
   * 得到保存结果sql
   * @param tableName 表的名称
   * @param result 要保存的结果
   * @return 保存结果sql
   */
  private String getSaveResultSql(String tableName, HashMap result) {
    String sql = " INSERT INTO " + tableName;
    String fields = " unitID, taskTimeID ";
    String values = " ?, ? ";
    Iterator itr = result.keySet().iterator();
    while (itr.hasNext()) {
      fields += ", " + itr.next();
      values += ", ?";
    }
    return sql + " (" + fields + ") VALUES (" + values + ")";
  }

  /**
   * 得到单位ID字符串
   * @param unitIDs 单位ID集合
   * @return 单位ID字符串
   */
  private String getUnitIDsString(Collection unitIDs) {
    String result = "";
    Iterator itr = unitIDs.iterator();
    int count = 0;
    while (itr.hasNext()) {
      if (count > 0) {
        result += "，";
      }
      result += itr.next();
      count++;
    }
    return result;
  }

  /**
   * 检查单个集团汇总节点单位的节点汇总结果是否正确
   * @param unit 汇总节点单位
   * @param time 任务时间
   * @param session Session对象
   * @return 检查结果
   * @throws HibernateException 底层异常
   */
  private ValidateResult validateNodeSum(UnitTreeNode unit,
                                         cn.com.youtong.apollo.task.TaskTime
                                         time,
                                         Session session) throws
      HibernateException {
    String tempTableName = null;
    try {
      Collection units = getUnitsInSumNode(unit);
      tempTableName = createUnitTempTable(units, session);
      //分别对每张汇总表检查汇总结果
      Map errorTables = new HashMap();
      Collection tables = getSumTables();
      Iterator itr = tables.iterator();
      while (itr.hasNext()) {
        Table sumTable = (Table) itr.next();
        Collection errorScalars = validateNodeSumTable(sumTable, unit,
            time, tempTableName, session);
        if (errorScalars.size() > 0) {
          errorTables.put(sumTable, errorScalars);
        }
      }
      return new ValidateResult(unit, errorTables);
    }
    finally {
      try {
        dropUnitTempTable(tempTableName, session);
      }
      catch (Exception ex) {
      }
    }
  }

  /**
   * 对指定汇总表调整差额
   * @param sumTable 要汇总的表
   * @param unit 汇总节点单位
   * @param time 任务时间
   * @param unitTempTableName 存放参与检查汇总结果的单位ID的临时表
   * @param session Session对象
   * @return 错误指标ValidateResult.ErrorScalar集合
   * @throws HibernateException 底层异常
   */
  private Collection validateNodeSumTable(Table sumTable, UnitTreeNode unit,
                                          cn.com.youtong.apollo.task.TaskTime
                                          time, String unitTempTableName,
                                          Session session) throws
      HibernateException {
    Collection result = new LinkedList();
    Collection sumCells = getSumCells(sumTable);
    //检查固定汇总单元格的汇总结果
    if (sumCells.size() > 0) {
      result.addAll(validateNodeSumResult(unit, sumTable, time,
                                          unitTempTableName,
                                          session));
    }
    //检查需汇总的浮动行的汇总结果
    Collection sumFloatRows = getSumFloatRows(sumTable);
    Iterator itr = sumFloatRows.iterator();
    while (itr.hasNext()) {
      Row row = (Row) itr.next();
      result.addAll(validateNodeSumResult(row, time, unitTempTableName,
                                          session));
    }
    return result;
  }

  /**
   * 检查指定浮动行节点汇总的结果
   * @param sumFloatRow 浮动行
   * @param time 任务时间
   * @param tempTableName 存放参与检查汇总结果的单位ID的临时表
   * @param session Session对象
   * @return 错误指标ValidateResult.ErrorScalar集合
   * @throws HibernateException 底层异常
   */
  private Collection validateNodeSumResult(Row sumFloatRow,
                                           cn.com.youtong.apollo.task.
                                           TaskTime
                                           time, String tempTableName,
                                           Session session) throws
      HibernateException {
    return null;
  }

  /**
   * 检查指定汇总表节点汇总的结果
   * @param groupSumUnit 集团汇总单位
   * @param sumTable 汇总表
   * @param time 任务时间
   * @param tempTableName 存放参与检查汇总结果的单位ID的临时表
   * @param session Session对象
   * @return 错误指标ValidateResult.ErrorScalar集合
   * @throws HibernateException 底层异常
   */
  private Collection validateNodeSumResult(UnitTreeNode groupSumUnit,
                                           Table sumTable,
                                           cn.com.youtong.apollo.task.
                                           TaskTime
                                           time, String tempTableName,
                                           Session session) throws
      HibernateException {
    HashMap nodeSumValue = getSumResult(sumTable, time, tempTableName,
                                        session);
    HashMap groupSumValue = getGroupSumUnitValue(groupSumUnit, sumTable,
                                                 time, session);

    return validateScalar(nodeSumValue, groupSumValue);
  }

  /**
   * 判断两个HashMap中的指标值是否相等
   * @param expectedMap 期望值Map
   * @param actualMap 实际值Map
   * @return 错误指标ValidateResult.ErrorScalar集合
   */
  private Collection validateScalar(HashMap expectedMap, HashMap actualMap) {
    Collection result = new LinkedList();
    Set keys = expectedMap.keySet();
    Iterator itr = keys.iterator();
    while (itr.hasNext()) {
      String key = (String) itr.next();
      Double expected = (Double) expectedMap.get(key);
      Double actual = (Double) actualMap.get(key);
      if (!doubleEqauls(actual, expected)) {
        result.add(new ValidateResult.ErrorScalar(key, actual, expected));
      }
    }
    return result;
  }

  /**
   * 判断两个double是否相等
   * @param double1 double1
   * @param double2 double2
   * @return 相等，返回true；否则返回false
   */
  private boolean doubleEqauls(Double double1, Double double2) {
    Double zero = new Double(0);
    Double abs1 = new Double(Math.abs(double1.doubleValue()));
    Double abs2 = new Double(Math.abs(double2.doubleValue()));

    if (zero.equals(abs1) && zero.equals(abs2)) {
      //-0.00与0.00比较的情况，这种情况认为两者相等
      return true;
    }
    else {
      return double1.equals(double2);
    }
  }

  /**
   * 将结果保存到指定的表中
   * @param table 表
   * @param unit 单位
   * @param time 任务时间
   * @param result 汇总结果
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void saveResult(Table table, UnitTreeNode unit,
                          cn.com.youtong.apollo.task.TaskTime time,
                          HashMap result, Session session) throws
      HibernateException {
    PreparedStatement pst = null;
    try {
      Collection sumCells = getSumCells(table);
      String tableName = NameGenerator.generateDataTableName(
          task.id(),
          table.id());
      String sql = getSaveResultSql(NameGenerator.generateDataTableName(
          task.id(), table.id()), result);
      Connection conn = session.connection();
      pst = conn.prepareStatement(sql);
      pst.setString(1, unit.id());
      pst.setInt(2, time.getTaskTimeID().intValue());
      Iterator itr = result.keySet().iterator();
      int index = 2;
      while (itr.hasNext()) {
        index++;
        pst.setDouble(index,
                      new Double(result.get(itr.next()).toString()).
                      doubleValue());
      }
      pst.execute();
      pst.close();
    }
    catch (SQLException ex) {
      throw new HibernateException(ex);
    }
    finally {
      if (pst != null) {
        try {
          pst.close();
        }
        catch (SQLException ex1) {
        }
      }
    }
  }

  /**
   * 将结果保存到指定的浮动行中
   * @param floatRow 汇总浮动行
   * @param unit 单位
   * @param time 任务时间
   * @param result 结果
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void saveResult(Row floatRow, UnitTreeNode unit,
                          cn.com.youtong.apollo.task.TaskTime time,
                          HashMap result, Session session) throws
      HibernateException {

  }

  /**
   * 对指定汇总表调整差额
   * @param sumTable 要汇总的表
   * @param unit 汇总节点单位
   * @param time 任务时间
   * @param unitTempTableName 保存参与调整差额单位信息的临时表
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void adjustNodeDiffTable(Table sumTable, UnitTreeNode unit,
                                   cn.com.youtong.apollo.task.TaskTime time,
                                   String unitTempTableName, Session session) throws
      HibernateException {
    Collection sumCells = getSumCells(sumTable);
    UnitTreeNode diffUnit = getGroupDiffUnit(unit);
    //对固定汇总单元格调整差额
    if (sumCells.size() > 0) {
      clearResult(sumTable, diffUnit, time, session);
      HashMap result = getAdjustNodeDiffResult(unit, sumTable, time,
                                               unitTempTableName, session);
      saveResult(sumTable, diffUnit, time, result, session);
    }
    //对需汇总的浮动行调整差额
    Collection sumFloatRows = getSumFloatRows(sumTable);
    Iterator itr = sumFloatRows.iterator();
    while (itr.hasNext()) {
      Row row = (Row) itr.next();
      clearResult(row, unit, time, session);
      HashMap result = getAdjustNodeDiffResult(row, time,
                                               unitTempTableName, session);
      saveResult(row, unit, time, result, session);
    }
  }

  /**
   * 得到指定单位的集团差额单位
   * @param unit 单位
   * @return 集团差额单位
   * @throws HibernateException
   */
  private UnitTreeNode getGroupDiffUnit(UnitTreeNode unit) throws
      HibernateException {
    Iterator itr = unit.getChildren();
    while (itr.hasNext()) {
      UnitTreeNode child = (UnitTreeNode) itr.next();
      if (isGroupDiffUnit(child)) {
        return child;
      }
    }
    throw new HibernateException("指定的单位" + unit.id() + "没有集团差额单位");
  }

  /**
   * 判断指定的单位是否是集团差额单位
   * @param unit 单位
   * @return 是集团差额单位，返回true；否则返回fasle
   */
  private boolean isGroupDiffUnit(UnitTreeNode unit) {
    return ReportType.GROUP_DIFF_TYPE.equals(unit.getReportType());
  }

  /**
   * 得到指定表的所有需汇总的浮动行
   * @param table 表
   * @return 指定表的所有需汇总的浮动行Row集合
   */
  private Collection getSumFloatRows(Table table) {
    return new ArrayList();
  }

  /**
   * 得到所有参与完全汇总的单位UnitTreeNode集合
   * @return 所有参与完全汇总的单位UnitTreeNode集合
   * @throws ModelException 业务异常
   */
  private Collection getSumAllUnits() throws ModelException {
    ArrayList result = new ArrayList();
    Iterator itr = treeManager.getUnitForest();
    while (itr.hasNext()) {
      addSumAllUnit( (UnitTreeNode) itr.next(), result);
    }
    return result;
  }

  /**
   * 将单位树中的可参与完全汇总的单位加入到指定单位集合中
   * @param unitTree 单位树
   * @param units 存放单位的目标集合
   */
  private void addSumAllUnit(UnitTreeNode unitTree, Collection units) {
    if (canSumAll(unitTree)) {
      units.add(unitTree);
    }
    //遍历子树
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSumAllUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * 判断指定单位是否能参与完全汇总
   * @param unit 单位
   * @return 如果指定单位能参与完全汇总，返回true；否则返回false
   */
  private boolean canSumAll(UnitTreeNode unit) {
    String reportType = unit.getReportType();
    return (!ReportType.SELECT_GATHER_TYPE.equals(reportType) &&
            !ReportType.GROUP_GATHER_TYPE.equals(reportType));
  }

  /**
   * 得到当前的task中所有需要汇总的表
   * @return 需要汇总的表集合
   * @throws HibernateException
   */
  private Collection getSumTables() throws HibernateException {
    try {
      ArrayList result = new ArrayList();
      Iterator itr = task.getAllTables();
      while (itr.hasNext()) {
        Table table = (Table) itr.next();
        if (isSumableTable(table)) {
          result.add(table);
        }
      }
      return result;
    }
    catch (TaskException ex) {
      throw new HibernateException(ex);
    }
  }

  /**
   * 将单位ID集合封装成单位对象集合
   * @param unitIDs 单位ID集合
   * @return 单位对象集合
   */
  private Collection unitID2Unit(Collection unitIDs) {
    ArrayList units = new ArrayList();
    Iterator itr = unitIDs.iterator();
    while (itr.hasNext()) {
      units.add(unitID2Unit( (String) itr.next()));
    }
    return units;
  }

  /**
   * 判断指定的表是否能够参与汇总
   * @param table 表
   * @return 表能够参与汇总，返回true；否则返回false
   */
  private boolean isSumableTable(Table table) {
    return (table != task.getUnitMetaTable() &&
            table.getFlag(Table.FLAG_SUM));
  }

  /**
   * 创建存放单位信息的临时表，并存入单位信息将
   * @param units 单位集合
   * @param session Session对象
   * @return 临时表的名称
   * @throws HibernateException 底层异常
   */
  private String createUnitTempTable(Collection units, Session session) throws
      HibernateException {
    PreparedStatement pst = null;
    try {
      //创建表
      String tableName = Config.getCurrentDatabase().
          generateTempTableName();
      String columnDefinitions = "(unitID varchar(100))";
      Connection conn = session.connection();
      pst = conn.prepareStatement(Config.getCurrentDatabase().
                                  getTempTableCreateSql(tableName,
          columnDefinitions));
      pst.execute();
      pst.close();
      //将单位信息保存到表中
      String insertSql = "INSERT INTO " + tableName + " VALUES(?) ";
      Iterator itr = units.iterator();
      while (itr.hasNext()) {
        UnitTreeNode unit = (UnitTreeNode) itr.next();
        pst = session.connection().prepareStatement(insertSql);
        pst.setString(1, unit.id());
        pst.execute();
        pst.close();
      }
      return tableName;
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
    finally {
      if (pst != null) {
        try {
          pst.close();
        }
        catch (SQLException ex1) {
        }
      }
    }
  }

  /**
   * 删除单位临时表
   * @param tableName 临时表名称
   * @param session Session对象
   * @throws HibernateException 底层异常
   */
  private void dropUnitTempTable(String tableName, Session session) throws
      HibernateException {
    PreparedStatement pst = null;
    try {
      Connection conn = session.connection();
      pst = conn.prepareStatement(Config.getCurrentDatabase().
                                  getTempTableDropSql(tableName));
      pst.execute();
      pst.close();
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
    finally {
      if (pst != null) {
        try {
          pst.close();
        }
        catch (SQLException ex1) {
        }
      }
    }
  }

  /**
   * 得到汇总节点单位的下级单位中参与节点汇总的单位集合
   * @param unit 汇总节点单位
   * @return 参与节点汇总的单位UnitTreeNode集合
   */
  private Collection getUnitsInSumNode(UnitTreeNode unit) {
    ArrayList result = new ArrayList();
    Iterator itr = unit.getChildren();
    while (itr.hasNext()) {
      UnitTreeNode child = (UnitTreeNode) itr.next();

      //过滤掉选择汇总单位
      if (!isSelectSumUnit(child)) {
        result.add(child);
      }
    }
    return result;
  }

  /**
   * 将单位ID封装成单位对象
   * @param unitID 单位ID
   * @return 单位对象
   */
  private UnitTreeNode unitID2Unit(String unitID) {
    DBUnitTreeNode unit = new DBUnitTreeNode();
    unit.setID(unitID);
    return unit;
  }

  /**
   * 得到汇总节点单位的下级单位中参与调整差额表的单位集合
   * @param unit 汇总节点单位
   * @return 参与调整差额表的单位UnitTreeNode集合
   */
  private Collection getUnitsInAdjustNodeDiff(UnitTreeNode unit) {
    ArrayList result = new ArrayList();
    Iterator itr = unit.getChildren();
    while (itr.hasNext()) {
      UnitTreeNode child = (UnitTreeNode) itr.next();
      if (canAdjustNodeDiff(child)) {
        result.add(child);
      }
    }
    return result;
  }

  /**
   * 判断指定单位是否能参与调整差额表
   * @param unit 单位
   * @return 如果指定单位能参与调整差额表，返回true；否则返回false
   */
  private boolean canAdjustNodeDiff(UnitTreeNode unit) {
    String reportType = unit.getReportType();
    return (!ReportType.SELECT_GATHER_TYPE.equals(reportType) &&
            !ReportType.FULL_GATHER_TYPE.equals(reportType) &&
            !ReportType.GROUP_DIFF_TYPE.equals(reportType));
  }

  /**
   * 得到指定单位树中所有需要节点汇总的单位
   * @param unitTree 单位树
   * @return 单位集合，已经按汇总顺序排好
   */
  private Collection getSumNodeUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addSumNodeUnit(unitTree, units);
    return units;
  }

  /**
   * 得到指定单位树中所有选择汇总单位
   * @param unitTree 单位树
   * @return 单位集合，已经按汇总顺序排好
   */
  private Collection getSelectSumUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addSelectSumUnit(unitTree, units);
    return units;
  }

  /**
   * 将单位树中的节点汇总单位加入到指定单位集合中
   * @param unitTree 单位树
   * @param units 存放单位的目标集合
   */
  private void addSumNodeUnit(UnitTreeNode unitTree, LinkedList units) {
    if (isSumNodeUnit(unitTree)) {
      //作为集合的第一个元素，这样保证汇总顺序是从底层集团汇总单位开始
      units.add(0, unitTree);
    }
    //遍历子树
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSumNodeUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * 将单位树中的选择汇总单位加入到指定单位集合中
   * @param unitTree 单位树
   * @param units 存放单位的目标集合
   */
  private void addSelectSumUnit(UnitTreeNode unitTree, LinkedList units) {
    if (isSelectSumUnit(unitTree)) {
      //作为集合的第一个元素，这样保证汇总顺序是从底层选择汇总单位开始
      units.add(0, unitTree);
    }
    //遍历子树
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSelectSumUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * 得到指定表中的所有汇总单元格
   * @param table 表
   * @return 汇总单元格Cell集合
   */
  private Collection getSumCells(Table table) {
    ArrayList result = new ArrayList();
    Iterator rowItr = table.getRows();
    while (rowItr.hasNext()) {
      Row row = (Row) rowItr.next();
      //非浮动行
      if (!row.getFlag(Row.FLAG_FLOAT_ROW)) {
        Iterator cellItr = row.getCells();
        while (cellItr.hasNext()) {
          Cell cell = (Cell) cellItr.next();
          if (cell.getFlag(Cell.FLAG_SUM)) {
            result.add(cell);
          }
        }
      }
    }
    return result;
  }

}