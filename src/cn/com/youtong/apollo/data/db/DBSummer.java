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
 * �������ݻ�������ִ�и��ֻ��ܲ���
 */
class DBSummer
    implements Summer {

  /** DBUnitTreeManagerʵ�� */
  private DBUnitTreeManager treeManager;

  private Log log = LogFactory.getLog(this.getClass());
  ;

  /** ���������� */
  private Task task;

  public DBSummer(Task task, DBUnitTreeManager treeManager) {
    this.task = task;
    this.treeManager = treeManager;
  }

  /**
   * ִ��ѡ����ܷ���
   * @param schemaID ����id
   * @param taskTime ����ʱ��id
   * @param unitACL ��λACL
   * @throws ModelException
   */
  public void executeSelectSumSchema(Integer schemaID,
                                     cn.com.youtong.apollo.task.TaskTime
                                     taskTime, UnitACL unitACL) throws
      ModelException {
    Session session = null;
    Transaction tx = null;

    //ѡ����ܵ�λid����
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      if (!isSelectSumSchemaDefined(schemaID, session)) {
        throw new ModelException("ָ����ѡ����ܷ���������");
      }

      log.info("��ʼִ��ѡ����ܷ���");
      DBSelectSumSchema schema = getSelectSumSchema(schemaID, session);

      Collection forest = treeManager.
          buildSelectSumUnitForest(schema,
                                   taskTime, unitACL);

      for (Iterator itr = forest.iterator(); itr.hasNext(); ) {
        UnitTreeNode unitTree = (UnitTreeNode) itr.next();
        Collection sumNodeUnits = getSelectSumUnits(unitTree);

        //��ÿ��ѡ����ܽڵ���л���
        Iterator unitItr = sumNodeUnits.iterator();
        while (unitItr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) unitItr.next();
          unitIDs.add(unit.id());
          sumNode(unit, taskTime, session);
        }
      }
      tx.commit();
      log.info("�ɹ�ִ��ѡ����ܷ���");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "ִ��ѡ����ܷ���ʧ��";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //�Ի��ܵ�λִ�нű�
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), taskTime);
  }

  /**
   * ���ڵ���ܵ�λ�µ����нڵ���ܽ���Ƿ���ȷ
   * @param unitID �ڵ���ܵ�λID
   * @param time ����ʱ��
   * @param isRecursive �Ƿ�ݹ���������ӽڵ�
   * @return �����ValidateResult���󼯺ϵ�Iterator
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
        throw new ModelException("���ܶ�ָ���ĵ�λ��" + unitTree.id() +
                                 "�������ܽ��");
      }

      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //���ÿ���ڵ�Ļ��ܽ��
        Iterator itr = sumNodeUnits.iterator();
        while (itr.hasNext()) {
          UnitTreeNode unit = (UnitTreeNode) itr.next();
          log.info("��ʼ��鵥λ��" + unit.id() + "���Ļ��ܽ��");
          result.add(validateNodeSum(unit, time, session));
        }
      }
      else {
        log.info("��ʼ��鵥λ��" + unitTree.id() + "���Ļ��ܽ��");
        result.add(validateNodeSum(unitTree, time, session));
      }

      tx.commit();
      return result.iterator();
    }
    catch (HibernateException ex) {
      String message = "��鵥λ��" + unitID + "���Ļ��ܽ��ʧ��";
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
   * �ڵ����
   * @param unitID ���ܽڵ㵥λID
   * @param time ����ʱ��
   * @param isRecursive �Ƿ�ݹ���������ӽڵ�
   * @throws ModelException
   */
  public void sumNode(String unitID, cn.com.youtong.apollo.task.TaskTime time,
                      boolean isRecursive) throws ModelException {
    Session session = null;
    Transaction tx = null;
    //���ܵ�λID����
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      UnitTreeNode unitTree = treeManager.getUnitTree(unitID);
      if (!isSumNodeUnit(unitTree)) {
        throw new ModelException("���ܶ�ָ���ĵ�λ��" + unitTree.id() +
                                 "�����нڵ����");
      }
      log.info("��ʼ�Ե�λ��" + unitID + "���ڵ����");
      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //��ÿ���ڵ���л���
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

      log.info("�ɹ���ɶԵ�λ��" + unitID + "���ڵ����");
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
      String message = "�Ե�λ��" + unitID + "���ڵ����ʧ��";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //�Ի��ܵ�λִ�нű�
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), time);
  }

  /**
   * ����ʱ��
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
   * ѡ�����
   * @param unitID ������ܽ���ĵ�λID
   * @param unitIDs Ҫ���ܵĵ�λID����
   * @param time ���ܵ�����ʱ��
   * @throws ModelException
   */
  public void sumUnits(String unitID, Collection unitIDs,
                       cn.com.youtong.apollo.task.TaskTime time) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      String unitIDStr = getUnitIDsString(unitIDs);
      log.info("��ʼѡ����ܣ�����λ��" + unitIDStr + "�����ܵ���λ��" +
               unitID + "��");
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      sum(unitID2Unit(unitID), unitID2Unit(unitIDs), time, session);
      tx.commit();
      log.info("�ɹ����ѡ����ܣ�����λ��" + unitIDStr + "�����ܵ���λ��" +
               unitID + "��");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "ѡ�����ʧ��";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //�Ի��ܵ�λִ�нű�
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    LinkedList IDs = new LinkedList();
    IDs.add(unitID);
    importer.excuteScripts(IDs.iterator(), time);

  }

  /**
   * ��ȫ���ܡ��������з�ѡ�����,��ȫ���ܺͽڵ���ܵ�λ
   * @param unitID ������ܽ���ĵ�λID
   * @param time ���ܵ�����ʱ��
   * @throws ModelException
   */
  public void sumAll(String unitID, cn.com.youtong.apollo.task.TaskTime time) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      log.info("��ʼ��ȫ����");
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      Collection units = getSumAllUnits();
      sum(unitID2Unit(unitID), units, time, session);
      tx.commit();
      log.info("�ɹ������ȫ����");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "��ȫ����ʧ��";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //�Ի��ܵ�λִ�нű�
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    LinkedList unitIDs = new LinkedList();
    unitIDs.add(unitID);
    importer.excuteScripts(unitIDs.iterator(), time);

  }

  /**
   * �����ڵ����
   * @param unitID ���ܽڵ㵥λID
   * @param time ����ʱ��
   * @param isRecursive �Ƿ�ݹ���������ӽڵ�
   * @throws ModelException
   */
  public void adjustNodeDiff(String unitID,
                             cn.com.youtong.apollo.task.TaskTime time,
                             boolean isRecursive) throws ModelException {
    Session session = null;
    Transaction tx = null;

    //��������ĵ�λID����
    LinkedList unitIDs = new LinkedList();

    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      UnitTreeNode unitTree = treeManager.getUnitTree(unitID);
      if (!isSumNodeUnit(unitTree)) {
        throw new ModelException("���ܶ�ָ���ĵ�λ��" + unitTree.id() +
                                 "�����е����������");
      }
      log.info("��ʼ�Ե�λ��" + unitID + "���������");
      if (isRecursive) {
        Collection sumNodeUnits = getSumNodeUnits(unitTree);
        //��ÿ���ڵ���е�������
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
      log.info("�ɹ���ɶԵ�λ��" + unitID + "���������");
    }
    catch (HibernateException ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "�Ե�λ��" + unitID + "���������ʧ��";
      log.error(message, ex);
      throw new ModelException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

    //�Ի��ܵ�λִ�нű�
    DBDataImporter importer = new DBDataImporter(task, treeManager);

    importer.excuteScripts(unitIDs.iterator(), time);
  }

  /**
   * �ж�ָ���ĵ�λ�Ƿ�����ȫ���ܵ�λ
   * @param unit ��λ����
   * @return ����ȫ���ܵ�λ������true�����򷵻�false
   */
  private boolean isSumAllUnit(UnitTreeNode unit) {
    return ReportType.FULL_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * �ж�ָ���ĵ�λ�Ƿ��ǽڵ���ܵ�λ
   * @param unit ��λ����
   * @return �ǽڵ���ܵ�λ������true�����򷵻�false
   */
  private boolean isSumNodeUnit(UnitTreeNode unit) {
    return unit.getChildren().hasNext();
  }

  /**
   * �ж�ָ���ĵ�λ�Ƿ���ѡ����ܵ�λ
   * @param unit ��λ����
   * @return ��ѡ����ܵ�λ������true�����򷵻�false
   */
  private boolean isSelectSumUnit(UnitTreeNode unit) {
    return ReportType.SELECT_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * �ж�ָ���ĵ�λ�Ƿ��Ǽ��Ż��ܵ�λ
   * @param unit ��λ����
   * @return �Ǽ��Ż��ܵ�λ������true�����򷵻�false
   */
  private boolean isGroupSumUnit(UnitTreeNode unit) {
    return ReportType.GROUP_GATHER_TYPE.equals(unit.getReportType());
  }

  /**
   * �õ�ָ����λ��������Ҫ������ܵļ��Ż������ͽڵ�
   * @param unitTree ��λ��
   * @return ��λ���ϣ��Ѿ�������˳���ź�
   */
  private Collection getGroupSumUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addGroupSumUnit(unitTree, units);
    return units;
  }

  /**
   * ����λ���еļ��Ż������ͽڵ���뵽ָ����λ������
   * @param unitTree ��λ��
   * @param units ��ŵ�λ��Ŀ�꼯��
   */
  private void addGroupSumUnit(UnitTreeNode unitTree, LinkedList units) {
    if (ReportType.GROUP_GATHER_TYPE.equals(unitTree.getReportType())) {
      //��Ϊ���ϵĵ�һ��Ԫ�أ�������֤����˳���Ǵӵײ㼯�Ż��ܵ�λ��ʼ
      units.add(0, unitTree);
    }
    //��������
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addGroupSumUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * �����ڵ���ܣ��ǵݹ飩
   * @param unit ���ܽڵ㵥λ
   * @param time ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void sumNode(UnitTreeNode unit,
                       cn.com.youtong.apollo.task.TaskTime time,
                       Session session) throws HibernateException {
    Collection units = getUnitsInSumNode(unit);
    sum(unit, units, time, session);
  }

  /**
   * ��ָ���ĵ�λ���Ͻ��л��ܣ�����������浽ָ����λ��
   * @param unit ������ܽ���ĵ�λ
   * @param units ������ܵĵ�λ����
   * @param time ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void sum(UnitTreeNode unit, Collection units,
                   cn.com.youtong.apollo.task.TaskTime time, Session session) throws
      HibernateException {
    String tempTableName = null;
    try {
      tempTableName = createUnitTempTable(units, session);
      //�ֱ��ÿ�Ż��ܱ���л���
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
   * �����ڵ���������ǵݹ飩
   * @param unit ���ܽڵ㵥λ
   * @param time ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void adjustNodeDiff(UnitTreeNode unit,
                              cn.com.youtong.apollo.task.TaskTime time,
                              Session session) throws HibernateException {
    String tempTableName = null;
    try {
      Collection units = getUnitsInAdjustNodeDiff(unit);
      tempTableName = createUnitTempTable(units, session);
      //�ֱ��ÿ�Ż��ܱ���е������
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
   * ����ѡ����ܷ���
   * @param schemaStream ������
   * @throws ModelException
   */
  public void createSelectSumSchema(InputStream schemaStream) throws
      ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      Selsum schema = Selsum.unmarshal(new InputStreamReader(schemaStream,
          "gb2312"));
      log.info("��ʼ����ѡ����ܷ���");

      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();

      //����ѡ����ܵ�λ��
      treeManager.createSelectSumUnitForest(schema, session);

      //���淽��
      createSelectSumSchema(schema, session);

      tx.commit();

      //���µ�ǰUnitTreeManager
      this.treeManager.update();

      log.info("�ɹ�����ѡ����ܷ���");
    }
    catch (Exception ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex1) {
      }
      String message = "��������ģ��ʧ��";
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
   * ��ѡ����ܷ������浽���ݿ���
   * @param schema ����
   * @param session Session����
   * @throws HibernateException
   */
  private void createSelectSumSchema(Selsum schema, Session session) throws
      HibernateException {
    try {
      //��ɾ��
      session.delete("select schema from SelectSumSchemaForm schema where schema.name = ? and schema.taskID = ?",
                     new Object[] {schema.getName(), task.id()}
                     , new Type[] {Hibernate.STRING, Hibernate.STRING});

      //�󴴽�
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
   * �õ�ָ�����������ѡ����ܷ���
   * @taskID ����ID
   * @return ѡ����ܷ���Iterator
   * @throws ModelException
   */
  public Iterator getAllSelectSumSchemas(String taskID) throws ModelException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      return getAllSelectSumSchemas(taskID, session).iterator();
    }
    catch (HibernateException ex) {
      String message = "���ܵõ�ѡ�����ģ��";
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
   * �õ�ָ����ѡ����ܷ���
   * @param schemaID ����id
   * @return ѡ����ܷ���
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
      throw new ModelException("ָ����ѡ����ܷ���������");
    }
    catch (HibernateException ex) {
      String message = "���ܵõ�ѡ�����ģ��";
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
   * �õ�ָ�����������ѡ����ܷ���
   * @taskID ����ID
   * @param session Session����
   * @return ѡ����ܷ���SelectSumSchema����
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
   * �õ�ָ����ѡ����ܷ���
   * @param schemaID ����id
   * @param session Session����
   * @return ѡ����ܷ���
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
   * ɾ��ѡ����ܷ���
   * @param schemaID ����id
   * @throws ModelException
   */
  public void deleteSelectSumSchema(Integer schemaID) throws ModelException {
    Session session = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isSelectSumSchemaDefined(schemaID, session)) {
        log.info("��ʼɾ��ѡ����ܷ���");
        tx = session.beginTransaction();
        session.delete(
            "select schema from SelectSumSchemaForm schema where schema.schemaID = ?",
            schemaID, Hibernate.INTEGER);
        tx.commit();
        log.info("�ɹ�ɾ��ѡ����ܷ���");
      }
      else {
        throw new ModelException("ָ����ѡ����ܷ���������");
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
      String message = "��������ģ��ʧ��";
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
   * �õ�ָ����ѡ����ܷ���
   * @param schemaForm ����Form
   * @return ѡ����ܷ���
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
      throw new HibernateException("���ܵõ�ѡ����ܷ���", ex);
    }
  }

  /**
   * ��ָ������л��ܣ��������ܽ����Ϊָ����λ������
   * @param sumTable Ҫ���ܵı�
   * @param unit ��λ
   * @param time ����ʱ��
   * @param unitTempTableName ���������ܵĵ�λ��Ϣ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void sumTable(Table sumTable, UnitTreeNode unit,
                        cn.com.youtong.apollo.task.TaskTime time,
                        String unitTempTableName, Session session) throws
      HibernateException {
    Collection sumCells = getSumCells(sumTable);
    //���̶ܹ���Ԫ��
    if (sumCells.size() > 0) {
      clearResult(sumTable, unit, time, session);
      HashMap result = getSumResult(sumTable, time, unitTempTableName,
                                    session);
      saveResult(sumTable, unit, time, result, session);
    }
    //���ܸ�����
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
   * ���ָ����Ľ��
   * @param table ��
   * @param unit ��λ
   * @param time ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
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
   * ���ָ�������еĽ��
   * @param floatRow ������
   * @param unit ��λ
   * @param time ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void clearResult(Row floatRow, UnitTreeNode unit,
                           cn.com.youtong.apollo.task.TaskTime time,
                           Session session) throws HibernateException {

  }

  /**
   * �õ�ָ������ܽ��
   * @param sumTable ���ܱ�
   * @param time ����ʱ��
   * @param tempTableName ��Ų�����ܵĵ�λID����ʱ����
   * @param session Session����
   * @return ���ܽ������DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws HibernateException �ײ��쳣
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
   * �õ�ָ�����ܱ�������Ľ��
   * @param groupSumUnit ���Ż��ܵ�λ
   * @param sumTable ���ܱ�
   * @param time ����ʱ��
   * @param tempTableName ��Ų���������ĵ�λID����ʱ����
   * @param session Session����
   * @return ���ܽ������DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws HibernateException �ײ��쳣
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
   * �ж�ָ����ѡ����ܷ����Ƿ����
   * @param schemaID ѡ����ܷ���id
   * @param session Session����
   * @return ָ����ѡ����ܷ����Ƿ���ڣ�����true�����򷵻�false
   * @throws HibernateException
   */
  private boolean isSelectSumSchemaDefined(Integer schemaID, Session session) throws
      HibernateException {
    return ( (Integer) session.iterate(
        "SELECT COUNT(*) from SelectSumSchemaForm as schema WHERE schema.schemaID = ?",
        schemaID, Hibernate.INTEGER).next()).intValue() > 0;
  }

  /**
   * �õ����Ż��ܵ�λ��ָ��������л��ܵ�Ԫ���ֵ
   * @param groupSumUnit ���Ż��ܵ�λ
   * @param sumTable ���ܱ�
   * @param time ����ʱ��
   * @param session Session����
   * @return ���ܵ�Ԫ���ֵ����DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws HibernateException �ײ��쳣
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
   * �õ�����HashMap�Ĳ�
   * @param value1 ������
   * @param value2 ����
   * @return ��
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
   * �õ�ָ�������л��ܽ��
   * @param sumFloatRow ���ܸ�����
   * @param time ����ʱ��
   * @param tempTableName ��Ų�����ܵĵ�λID����ʱ����
   * @param session Session����
   * @return ���ܽ������DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws HibernateException �ײ��쳣
   */
  private HashMap getSumResult(Row sumFloatRow,
                               cn.com.youtong.apollo.task.TaskTime time,
                               String tempTableName, Session session) throws
      HibernateException {
    return null;
  }

  /**
   * �õ�ָ�������е������Ľ��
   * @param sumFloatRow ������
   * @param time ����ʱ��
   * @param tempTableName ��Ų���������ĵ�λID����ʱ����
   * @param session Session����
   * @return �������������DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws HibernateException �ײ��쳣
   */
  private HashMap getAdjustNodeDiffResult(Row sumFloatRow,
                                          cn.com.youtong.apollo.task.TaskTime
                                          time, String tempTableName,
                                          Session session) throws
      HibernateException {
    return null;
  }

  /**
   * ��ResultSet��ȡ�ý��
   * @param rs ResultSet����
   * @param cells Cell���ϰ�����DBFieldName��Ϣ
   * @return �������DBFieldName(String), Value(Double)��ֵ�Ա�ʾ
   * @throws SQLException �ײ��쳣
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
   * �õ��������ܽ����sql
   * @param cells cells �μӻ��ܵĵ�Ԫ�񼯺�
   * @param time ����ʱ��
   * @param sumTableName ���ܱ������
   * @param unitTempTableName ��Ż��ܵ�λ��Ϣ����ʱ�������
   * @return ����sql
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
   * �õ�������sql
   * @param tableName �������
   * @param result Ҫ����Ľ��
   * @return ������sql
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
   * �õ���λID�ַ���
   * @param unitIDs ��λID����
   * @return ��λID�ַ���
   */
  private String getUnitIDsString(Collection unitIDs) {
    String result = "";
    Iterator itr = unitIDs.iterator();
    int count = 0;
    while (itr.hasNext()) {
      if (count > 0) {
        result += "��";
      }
      result += itr.next();
      count++;
    }
    return result;
  }

  /**
   * ��鵥�����Ż��ܽڵ㵥λ�Ľڵ���ܽ���Ƿ���ȷ
   * @param unit ���ܽڵ㵥λ
   * @param time ����ʱ��
   * @param session Session����
   * @return �����
   * @throws HibernateException �ײ��쳣
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
      //�ֱ��ÿ�Ż��ܱ�����ܽ��
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
   * ��ָ�����ܱ�������
   * @param sumTable Ҫ���ܵı�
   * @param unit ���ܽڵ㵥λ
   * @param time ����ʱ��
   * @param unitTempTableName ��Ų�������ܽ���ĵ�λID����ʱ��
   * @param session Session����
   * @return ����ָ��ValidateResult.ErrorScalar����
   * @throws HibernateException �ײ��쳣
   */
  private Collection validateNodeSumTable(Table sumTable, UnitTreeNode unit,
                                          cn.com.youtong.apollo.task.TaskTime
                                          time, String unitTempTableName,
                                          Session session) throws
      HibernateException {
    Collection result = new LinkedList();
    Collection sumCells = getSumCells(sumTable);
    //���̶����ܵ�Ԫ��Ļ��ܽ��
    if (sumCells.size() > 0) {
      result.addAll(validateNodeSumResult(unit, sumTable, time,
                                          unitTempTableName,
                                          session));
    }
    //�������ܵĸ����еĻ��ܽ��
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
   * ���ָ�������нڵ���ܵĽ��
   * @param sumFloatRow ������
   * @param time ����ʱ��
   * @param tempTableName ��Ų�������ܽ���ĵ�λID����ʱ��
   * @param session Session����
   * @return ����ָ��ValidateResult.ErrorScalar����
   * @throws HibernateException �ײ��쳣
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
   * ���ָ�����ܱ�ڵ���ܵĽ��
   * @param groupSumUnit ���Ż��ܵ�λ
   * @param sumTable ���ܱ�
   * @param time ����ʱ��
   * @param tempTableName ��Ų�������ܽ���ĵ�λID����ʱ��
   * @param session Session����
   * @return ����ָ��ValidateResult.ErrorScalar����
   * @throws HibernateException �ײ��쳣
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
   * �ж�����HashMap�е�ָ��ֵ�Ƿ����
   * @param expectedMap ����ֵMap
   * @param actualMap ʵ��ֵMap
   * @return ����ָ��ValidateResult.ErrorScalar����
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
   * �ж�����double�Ƿ����
   * @param double1 double1
   * @param double2 double2
   * @return ��ȣ�����true�����򷵻�false
   */
  private boolean doubleEqauls(Double double1, Double double2) {
    Double zero = new Double(0);
    Double abs1 = new Double(Math.abs(double1.doubleValue()));
    Double abs2 = new Double(Math.abs(double2.doubleValue()));

    if (zero.equals(abs1) && zero.equals(abs2)) {
      //-0.00��0.00�Ƚϵ���������������Ϊ�������
      return true;
    }
    else {
      return double1.equals(double2);
    }
  }

  /**
   * ��������浽ָ���ı���
   * @param table ��
   * @param unit ��λ
   * @param time ����ʱ��
   * @param result ���ܽ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
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
   * ��������浽ָ���ĸ�������
   * @param floatRow ���ܸ�����
   * @param unit ��λ
   * @param time ����ʱ��
   * @param result ���
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void saveResult(Row floatRow, UnitTreeNode unit,
                          cn.com.youtong.apollo.task.TaskTime time,
                          HashMap result, Session session) throws
      HibernateException {

  }

  /**
   * ��ָ�����ܱ�������
   * @param sumTable Ҫ���ܵı�
   * @param unit ���ܽڵ㵥λ
   * @param time ����ʱ��
   * @param unitTempTableName ������������λ��Ϣ����ʱ��
   * @param session Session����
   * @throws HibernateException �ײ��쳣
   */
  private void adjustNodeDiffTable(Table sumTable, UnitTreeNode unit,
                                   cn.com.youtong.apollo.task.TaskTime time,
                                   String unitTempTableName, Session session) throws
      HibernateException {
    Collection sumCells = getSumCells(sumTable);
    UnitTreeNode diffUnit = getGroupDiffUnit(unit);
    //�Թ̶����ܵ�Ԫ��������
    if (sumCells.size() > 0) {
      clearResult(sumTable, diffUnit, time, session);
      HashMap result = getAdjustNodeDiffResult(unit, sumTable, time,
                                               unitTempTableName, session);
      saveResult(sumTable, diffUnit, time, result, session);
    }
    //������ܵĸ����е������
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
   * �õ�ָ����λ�ļ��Ų�λ
   * @param unit ��λ
   * @return ���Ų�λ
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
    throw new HibernateException("ָ���ĵ�λ" + unit.id() + "û�м��Ų�λ");
  }

  /**
   * �ж�ָ���ĵ�λ�Ƿ��Ǽ��Ų�λ
   * @param unit ��λ
   * @return �Ǽ��Ų�λ������true�����򷵻�fasle
   */
  private boolean isGroupDiffUnit(UnitTreeNode unit) {
    return ReportType.GROUP_DIFF_TYPE.equals(unit.getReportType());
  }

  /**
   * �õ�ָ�������������ܵĸ�����
   * @param table ��
   * @return ָ�������������ܵĸ�����Row����
   */
  private Collection getSumFloatRows(Table table) {
    return new ArrayList();
  }

  /**
   * �õ����в�����ȫ���ܵĵ�λUnitTreeNode����
   * @return ���в�����ȫ���ܵĵ�λUnitTreeNode����
   * @throws ModelException ҵ���쳣
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
   * ����λ���еĿɲ�����ȫ���ܵĵ�λ���뵽ָ����λ������
   * @param unitTree ��λ��
   * @param units ��ŵ�λ��Ŀ�꼯��
   */
  private void addSumAllUnit(UnitTreeNode unitTree, Collection units) {
    if (canSumAll(unitTree)) {
      units.add(unitTree);
    }
    //��������
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSumAllUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * �ж�ָ����λ�Ƿ��ܲ�����ȫ����
   * @param unit ��λ
   * @return ���ָ����λ�ܲ�����ȫ���ܣ�����true�����򷵻�false
   */
  private boolean canSumAll(UnitTreeNode unit) {
    String reportType = unit.getReportType();
    return (!ReportType.SELECT_GATHER_TYPE.equals(reportType) &&
            !ReportType.GROUP_GATHER_TYPE.equals(reportType));
  }

  /**
   * �õ���ǰ��task��������Ҫ���ܵı�
   * @return ��Ҫ���ܵı���
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
   * ����λID���Ϸ�װ�ɵ�λ���󼯺�
   * @param unitIDs ��λID����
   * @return ��λ���󼯺�
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
   * �ж�ָ���ı��Ƿ��ܹ��������
   * @param table ��
   * @return ���ܹ�������ܣ�����true�����򷵻�false
   */
  private boolean isSumableTable(Table table) {
    return (table != task.getUnitMetaTable() &&
            table.getFlag(Table.FLAG_SUM));
  }

  /**
   * ������ŵ�λ��Ϣ����ʱ�������뵥λ��Ϣ��
   * @param units ��λ����
   * @param session Session����
   * @return ��ʱ�������
   * @throws HibernateException �ײ��쳣
   */
  private String createUnitTempTable(Collection units, Session session) throws
      HibernateException {
    PreparedStatement pst = null;
    try {
      //������
      String tableName = Config.getCurrentDatabase().
          generateTempTableName();
      String columnDefinitions = "(unitID varchar(100))";
      Connection conn = session.connection();
      pst = conn.prepareStatement(Config.getCurrentDatabase().
                                  getTempTableCreateSql(tableName,
          columnDefinitions));
      pst.execute();
      pst.close();
      //����λ��Ϣ���浽����
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
   * ɾ����λ��ʱ��
   * @param tableName ��ʱ������
   * @param session Session����
   * @throws HibernateException �ײ��쳣
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
   * �õ����ܽڵ㵥λ���¼���λ�в���ڵ���ܵĵ�λ����
   * @param unit ���ܽڵ㵥λ
   * @return ����ڵ���ܵĵ�λUnitTreeNode����
   */
  private Collection getUnitsInSumNode(UnitTreeNode unit) {
    ArrayList result = new ArrayList();
    Iterator itr = unit.getChildren();
    while (itr.hasNext()) {
      UnitTreeNode child = (UnitTreeNode) itr.next();

      //���˵�ѡ����ܵ�λ
      if (!isSelectSumUnit(child)) {
        result.add(child);
      }
    }
    return result;
  }

  /**
   * ����λID��װ�ɵ�λ����
   * @param unitID ��λID
   * @return ��λ����
   */
  private UnitTreeNode unitID2Unit(String unitID) {
    DBUnitTreeNode unit = new DBUnitTreeNode();
    unit.setID(unitID);
    return unit;
  }

  /**
   * �õ����ܽڵ㵥λ���¼���λ�в����������ĵ�λ����
   * @param unit ���ܽڵ㵥λ
   * @return �����������ĵ�λUnitTreeNode����
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
   * �ж�ָ����λ�Ƿ��ܲ����������
   * @param unit ��λ
   * @return ���ָ����λ�ܲ��������������true�����򷵻�false
   */
  private boolean canAdjustNodeDiff(UnitTreeNode unit) {
    String reportType = unit.getReportType();
    return (!ReportType.SELECT_GATHER_TYPE.equals(reportType) &&
            !ReportType.FULL_GATHER_TYPE.equals(reportType) &&
            !ReportType.GROUP_DIFF_TYPE.equals(reportType));
  }

  /**
   * �õ�ָ����λ����������Ҫ�ڵ���ܵĵ�λ
   * @param unitTree ��λ��
   * @return ��λ���ϣ��Ѿ�������˳���ź�
   */
  private Collection getSumNodeUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addSumNodeUnit(unitTree, units);
    return units;
  }

  /**
   * �õ�ָ����λ��������ѡ����ܵ�λ
   * @param unitTree ��λ��
   * @return ��λ���ϣ��Ѿ�������˳���ź�
   */
  private Collection getSelectSumUnits(UnitTreeNode unitTree) {
    LinkedList units = new LinkedList();
    addSelectSumUnit(unitTree, units);
    return units;
  }

  /**
   * ����λ���еĽڵ���ܵ�λ���뵽ָ����λ������
   * @param unitTree ��λ��
   * @param units ��ŵ�λ��Ŀ�꼯��
   */
  private void addSumNodeUnit(UnitTreeNode unitTree, LinkedList units) {
    if (isSumNodeUnit(unitTree)) {
      //��Ϊ���ϵĵ�һ��Ԫ�أ�������֤����˳���Ǵӵײ㼯�Ż��ܵ�λ��ʼ
      units.add(0, unitTree);
    }
    //��������
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSumNodeUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * ����λ���е�ѡ����ܵ�λ���뵽ָ����λ������
   * @param unitTree ��λ��
   * @param units ��ŵ�λ��Ŀ�꼯��
   */
  private void addSelectSumUnit(UnitTreeNode unitTree, LinkedList units) {
    if (isSelectSumUnit(unitTree)) {
      //��Ϊ���ϵĵ�һ��Ԫ�أ�������֤����˳���Ǵӵײ�ѡ����ܵ�λ��ʼ
      units.add(0, unitTree);
    }
    //��������
    Iterator itr = unitTree.getChildren();
    while (itr.hasNext()) {
      addSelectSumUnit( (UnitTreeNode) itr.next(), units);
    }
  }

  /**
   * �õ�ָ�����е����л��ܵ�Ԫ��
   * @param table ��
   * @return ���ܵ�Ԫ��Cell����
   */
  private Collection getSumCells(Table table) {
    ArrayList result = new ArrayList();
    Iterator rowItr = table.getRows();
    while (rowItr.hasNext()) {
      Row row = (Row) rowItr.next();
      //�Ǹ�����
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