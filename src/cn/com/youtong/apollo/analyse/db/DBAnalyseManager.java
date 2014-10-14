package cn.com.youtong.apollo.analyse.db;

import java.util.*;
import java.io.*;
import java.sql.*;

import cn.com.youtong.apollo.analyse.*;
import cn.com.youtong.apollo.analyse.form.*;
import cn.com.youtong.apollo.analyse.xml.Cell;
import cn.com.youtong.apollo.analyse.xml.Head;
import cn.com.youtong.apollo.analyse.xml.Row;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.common.*;

import net.sf.hibernate.*;
import net.sf.hibernate.type.*;

import org.exolab.castor.xml.*;
import org.apache.commons.logging.*;
import org.apache.fulcrum.factory.FactoryException;

/**
 * ��������Բ�����Ȩ��, ���Ǳ�����ʶ��һ���ĵ�λ��Χ.
 *
 * ����, ����ĳ����λ��Χ, �鿴�÷�Χ�ڵĵ�λ,�ϱ����.
 * ����Manager��DBʵ��
 *
 *
 */
public class DBAnalyseManager
    implements AnalyseManager {
  /** log */
  Log log = LogFactory.getLog(DBAnalyseManager.class);

  /**
   * ����ָ���ѯģ��
   * @param userID �û�id
   * @param templateIn ģ��������
   * @throws AnalyseException
   */
  public void importScalarQueryTemplate(Integer userID,
                                        InputStream templateIn) throws
      AnalyseException {
    Session session = null;
    Transaction tx = null;
    try {
      log.info("��ʼ����ָ���ѯģ��");
      cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate
          castorTemplate = cn.com.youtong.apollo.analyse.xml.
          ScalarQueryTemplate.unmarshal(new InputStreamReader(templateIn,
          "gb2312"));

      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();

      if (isScalarQueryTemplateDefined(castorTemplate.getTaskID(), userID,
                                       castorTemplate.getName(), session)) {
        ScalarQueryTemplateForm template = getScalarQueryTemplate(
            castorTemplate.getTaskID(), userID,
            castorTemplate.getName(), session);
        updateScalarQueryTemplate(template, castorTemplate, session);
      }
      else {
        createScalarQueryTemplate(userID, castorTemplate, session);
      }
      tx.commit();
      log.info("�ɹ�����ָ���ѯģ��");
    }
    catch (Exception ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "����ָ���ѯģ��ʧ��";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @param out ����ģ��������
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(Integer templateID, OutputStream out) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isScalarQueryTemplateDefined(templateID, session)) {
        log.info("��ʼ����ָ���ѯģ��templateID=" + templateID);
        ScalarQueryTemplateForm template = getScalarQueryTemplate(
            templateID, session);
        exportScalarQueryTemplate(template, out);
        log.info("�ɹ�����ָ���ѯģ��templateID=" + templateID);
      }
      else {
        throw new AnalyseException("ָ����ָ���ѯģ�岻����");
      }
    }
    catch (HibernateException ex) {
      String message = "����ָ���ѯģ��templateID=" + templateID + "ʧ��";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ����ָ���ѯģ��
   * @param template ģ�����
   * @param out ����ģ��������
   * @throws HibernateException
   */
  private void exportScalarQueryTemplate(ScalarQueryTemplateForm template,
                                         OutputStream out) throws
      HibernateException {
    try {
      StringReader reader = new StringReader(Convertor.Clob2String(template.
          getContent()));
      cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate
          .unmarshal(reader).marshal(new OutputStreamWriter(out, "gb2312"));
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
  }

  /**
   * ����ָ���ѯģ��
   * @param taskID ����ID
   * @param userID �û�ID
   * @param name ģ������
   * @param out ����ģ��������
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(String taskID, Integer userID,
                                        String name, OutputStream out) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isScalarQueryTemplateDefined(taskID, userID, name, session)) {
        log.info("��ʼ����ָ���ѯģ��" + name);
        ScalarQueryTemplateForm template = getScalarQueryTemplate(
            taskID, userID, name, session);
        exportScalarQueryTemplate(template, out);
        log.info("�ɹ�����ָ���ѯģ��" + name);
      }
      else {
        throw new AnalyseException("ָ����ָ���ѯģ�岻����");
      }
    }
    catch (HibernateException ex) {
      String message = "����ָ���ѯģ��" + name + "ʧ��";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ����������ָ���ѯ
   * @param condition ��ѯ����
   * @return ��ѯ���
   * @throws AnalyseException ��ѯʧ��
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition) throws
      AnalyseException {
    try {
      ModelManager modelManager = ( (ModelManagerFactory)
                                   Factory.getInstance(
          ModelManagerFactory.class.getName()))
          .createModelManager(condition.getTaskID());

      return queryScalar(condition, condition.getUnitIDs());
    }
    catch (Exception ex) {
      throw new AnalyseException("ָ���ѯʧ��", ex);
    }
  }

  /**
   * ָ����Ҫ��ѯ�ĵ�λ����ģ���ѯ
   * @param condition           ��ѯ����
   * @param unitIDs               ָ���Ĳ�ѯ��λ
   * @return                    ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(ScalarQueryForm condition,
                                      String[] unitIDs) throws AnalyseException {
    try {
      TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
          TaskManagerFactory.class.getName()))
          .createTaskManager();
      ModelManager modelManager = ( (ModelManagerFactory)
                                   Factory.getInstance(
          ModelManagerFactory.class.getName()))
          .createModelManager(condition.getTaskID());

      UnitTreeNode[] units = getUnits(modelManager.getUnitTreeManager().
                                      getUnits(unitIDs));

      Task task = taskManager.getTaskByID(condition.getTaskID());

      TaskTime[] taskTimes = getTaskTimes(task, condition.getTaskTimeIDs());
      Object[][][] result = getResult(task, units, taskTimes,
                                      condition.getScalars());

      return new ScalarResultForm(task, units, taskTimes,
                                  condition.getScalars(), result);
    }
    catch (Exception ex) {
      throw new AnalyseException("ָ���ѯʧ��", ex);
    }
  }

  /**
   * �õ���λ����
   * @param units ��λ����
   * @return ��λ����
   */
  private UnitTreeNode[] getUnits(Collection units) {
    UnitTreeNode[] result = new UnitTreeNode[units.size()];
    int count = 0;
    for (Iterator itr = units.iterator(); itr.hasNext(); ) {
      result[count] = (UnitTreeNode) itr.next();
      count++;
    }
    return result;
  }

  /**
   * �õ�ָ�����������ʱ������
   * @param task ����
   * @param taskTimeIDs ����ʱ��ID����
   * @return ����ʱ������
   * @throws TaskException
   */
  private TaskTime[] getTaskTimes(Task task, Integer[] taskTimeIDs) throws
      TaskException {
    TaskTime[] taskTimes = new TaskTime[taskTimeIDs.length];
    for (int i = 0; i < taskTimeIDs.length; i++) {
      taskTimes[i] = task.getTaskTime(taskTimeIDs[i]);
    }
    return taskTimes;
  }

  /**
   * �õ�ָ���ѯ�������
   * @param task ����
   * @param units ��λ����
   * @param taskTimes ����������
   * @param scalars ָ������
   * @return ָ���ѯ�������
   * @throws ScriptException
   */
  private Object[][][] getResult(Task task, UnitTreeNode[] units,
                                 TaskTime[] taskTimes, ScalarForm[] scalars) throws
      ScriptException {
    try {
      Object[][][] result = new Object[units.length][taskTimes.length][
          scalars.length];

      List expressions = getExpressions(scalars);
      ScriptEngine scriptEngine = new ScriptEngine();
      //compile
      List exps = scriptEngine.compile(expressions);

      ModelManagerFactory modelMngFcty = (ModelManagerFactory)
          Factory.getInstance(
          ModelManagerFactory.class.getName());
      ModelManager modelMng = modelMngFcty.createModelManager(task.id());
      DataSource dataSource = modelMng.getDataSource();

      // get taskdatas and use them to eval scripts
      Map unitID2PositionMap = new HashMap(); // ��ʶunitID�����������е�λ��
      for (int i = 0; i < units.length; i++) {
        unitID2PositionMap.put(units[i].id(), new Integer(i));
      }

      Iterator[] iters = new Iterator[taskTimes.length];
      for (int i = 0; i < iters.length; i++) {
        // ����ÿ��taskTime��ȡ�ö�Ӧ��Iterator
        iters[i] = dataSource.get(unitID2PositionMap.keySet(),
                                  taskTimes[i]);
      }

      // ���нű�����
      for (int i = 0; i < taskTimes.length; i++) {
        Iterator taskDataIter = iters[i];
        while (taskDataIter.hasNext()) {
          TaskData taskData = (TaskData) taskDataIter.next();
          String unitID = taskData.getUnitID();
          Object obj = unitID2PositionMap.get(unitID);
          if (obj == null) {
            continue;
          }

          int position = ( (Integer) obj).intValue();

          List values = scriptEngine.eval(taskData, exps);
          result[position][i] = values.toArray();
        }
      }
      return result;
    }
    catch (FactoryException ex) {
      log.error("���ܻ��ModelManagerFactory", ex);
      throw new ScriptException("", ex);
    }
    catch (ModelException ex) {
      throw new ScriptException("ȡ���ݷ����쳣", ex);
    }
  }

  /**
   * �õ�ָ����ʽ�б�
   * @param scalars ָ������
   * @return ָ����ʽ�б�
   */
  private List getExpressions(ScalarForm[] scalars) {
    List expressions = new LinkedList();
    for (int i = 0; i < scalars.length; i++) {
      expressions.add(scalars[i].getExpression());
    }
    return expressions;
  }

  /**
   * ��ģ�����ָ���ѯ
   * @param templateID ��ѯģ��ID
   * @param taskTimeIDs ����ʱ��ID����
   * @return ��ѯ���
   * @throws AnalyseException ��ѯʧ��
   */
//	public ScalarResultForm queryScalar(Integer templateID,
//										Integer[] taskTimeIDs) throws
//		AnalyseException
//	{
//		ScalarQueryTemplate template = getScalarQueryTemplate(templateID);
//		//��������ʱ������
//		template.getCondition().setTaskTimeIDs(taskTimeIDs);
//
//		return queryScalar(template.getCondition());
//	}

  /**
   * ָ����λ��������ʱ�䣬ִ�б�ʶΪtemplateID�Ĳ�ѯģ��
   * @param templateID                 ��ѯģ��id
   * @param taskTimeIDs                ����ʱ��
   * @param unitIDs                    ��λ����
   * @return                           ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(Integer templateID,
                                      Integer[] taskTimeIDs,
                                      String[] unitIDs) throws AnalyseException {
    ScalarQueryTemplate template = getScalarQueryTemplate(templateID);
    //��������ʱ������
    template.getCondition().setTaskTimeIDs(taskTimeIDs);

    return queryScalar(template.getCondition(), unitIDs);
  }

  /**
   * �õ�ָ���û���ָ�������ָ���ѯģ��
   * @param userID �û�id
   * @param taskID ����id
   * @return ָ���ѯģ��ScalarQueryTemplate��Iterator
   * @throws AnalyseException
   */
  public Iterator getScalarQueryTemplates(Integer userID, String taskID) throws
      AnalyseException {
    Session session = null;
    try {
      LinkedList result = new LinkedList();
      session = HibernateUtil.getSessionFactory().openSession();
      List templates = session.find(
          "FROM ScalarQueryTemplateForm as template WHERE template.taskID = ?",
          new Object[] {taskID}
          , new Type[] {Hibernate.STRING});
      for (Iterator itr = templates.iterator(); itr.hasNext(); ) {
        result.add(new DBScalarQueryTemplate( (ScalarQueryTemplateForm)
                                             itr.next()));
      }
      return result.iterator();
    }
    catch (HibernateException ex) {
      throw new AnalyseException("��ѯָ���ѯģ��ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * �õ�ָ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @return ָ���ѯģ��
   * @throws AnalyseException
   */
  public ScalarQueryTemplate getScalarQueryTemplate(Integer templateID) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      if (isScalarQueryTemplateDefined(templateID, session)) {
        return new DBScalarQueryTemplate(getScalarQueryTemplate(
            templateID, session));
      }
      else {
        throw new AnalyseException("ָ����ָ���ѯģ�岻����");
      }
    }
    catch (HibernateException ex) {
      throw new AnalyseException("��ѯָ���ѯģ��ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ����ָ���ѯģ��
   * @param userID �û�ID
   * @param name ģ������
   * @param condition ��ѯ����
   * @return �ѱ����ģ��
   * @throws AnalyseException
   */
  /**public ScalarQueryTemplate createScalarQueryTemplate(Integer userID,
   String name,
   ScalarQueryForm condition) throws
   AnalyseException
    {
   Session session = null;
   Transaction tx = null;
   try
   {
    session = HibernateUtil.getSessionFactory().openSession();
    tx = session.beginTransaction();
    if (isScalarQueryTemplateDefined(condition.getTaskID(), userID,
             name, session))
    {
     throw new AnalyseException("�Ѿ�������ͬ���ֵ�ָ���ѯģ��");
    }
    else
    {
     cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate
      castorTemplate = condition2Castor(name, condition);
     castorTemplate.setName(name);
     ScalarQueryTemplateForm template = createScalarQueryTemplate(
      userID, castorTemplate, session);
     tx.commit();
     return new DBScalarQueryTemplate(template);
    }
   }
   catch (HibernateException ex)
   {
    try
    {
     if (tx != null)
     {
      tx.rollback();
     }
    }
    catch (HibernateException ex2)
    {
    }
    throw new AnalyseException("����ָ���ѯģ��ʧ��", ex);
   }
   finally
   {
    HibernateUtil.close( session );
   }
    }*/

  /**
   * ����ָ���ѯģ��
   * @param templateID ģ��ID
   * @param name ģ������
   * @param condition ��ѯ����
   * @return �ѱ����ģ��
   * @throws AnalyseException
   */
  /**public ScalarQueryTemplate updateScalarQueryTemplate(Integer templateID,
   String name, ScalarQueryForm condition) throws AnalyseException
    {
   Session session = null;
   Transaction tx = null;
   try
   {
    session = HibernateUtil.getSessionFactory().openSession();
    tx = session.beginTransaction();
    if (isScalarQueryTemplateDefined(templateID, session))
    {
     ScalarQueryTemplateForm oldTemplate = getScalarQueryTemplate(
      templateID, session);
     if (!name.equals(oldTemplate.getName()) &&
      isScalarQueryTemplateDefined(oldTemplate.getTaskID(),
              oldTemplate.getUserID(), name,
              session))
     {
      throw new AnalyseException("�Ѿ�������ͬ���ֵ�ָ���ѯģ��");
     }
     cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate
      castorTemplate = condition2Castor(name, condition);
     ScalarQueryTemplateForm newTemplate = updateScalarQueryTemplate(
      oldTemplate, castorTemplate, session);
     tx.commit();
     return new DBScalarQueryTemplate(newTemplate);
    }
    else
    {
     throw new AnalyseException("ָ����ָ���ѯģ�岻����");
    }
   }
   catch (HibernateException ex)
   {
    try
    {
     if (tx != null)
     {
      tx.rollback();
     }
    }
    catch (HibernateException ex2)
    {
    }
    throw new AnalyseException("����ָ���ѯģ��ʧ��", ex);
   }
   finally
   {
    HibernateUtil.close( session );
   }
    }*/

  /**
   * �ж�ָ���ѯģ���Ƿ����
   * @param taskID ����id
   * @param userID �û�id
   * @param name ģ������
   * @param session Session����
   * @return ���ڣ�����true�����򷵻�false
   * @throws HibernateException
   */
  private boolean isScalarQueryTemplateDefined(String taskID, Integer userID,
                                               String name, Session session) throws
      HibernateException {
    return ( (Integer) session.iterate("SELECT COUNT(*) from ScalarQueryTemplateForm template WHERE template.taskID = ? AND template.userID = ? AND template.name = ?",
                                       new Object[] {taskID, userID, name}
                                       , new Type[] {Hibernate.STRING,
                                       Hibernate.INTEGER,
                                       Hibernate.STRING}
                                       ).next()).intValue() > 0;
  }

  /**
   * �ж�ָ���ѯģ���Ƿ����
   * @param templateID ģ��id
   * @param session Session����
   * @return ���ڣ�����true�����򷵻�false
   * @throws HibernateException
   */
  private boolean isScalarQueryTemplateDefined(Integer templateID,
                                               Session session) throws
      HibernateException {
    return ( (Integer) session.iterate("SELECT COUNT(*) from ScalarQueryTemplateForm as template WHERE template.templateID = ?",
                                       templateID, Hibernate.INTEGER).next()).
        intValue() > 0;
  }

  /**
   * ����ָ���ѯģ��
   * @param userID �û�id
   * @param castorTemplate Castorģ�����
   * @param session Session����
   * @return �Ѵ�����ģ��
   * @throws HibernateException
   */
  private ScalarQueryTemplateForm createScalarQueryTemplate(Integer userID,
      cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate castorTemplate,
      Session session) throws
      HibernateException {
    try {
      ScalarQueryTemplateForm template = new ScalarQueryTemplateForm();
      template.setName(castorTemplate.getName());
      template.setTaskID(castorTemplate.getTaskID());
      template.setTemplateID(HibernateUtil.getNextScalarQueryTemplateID());
      template.setUserID(userID);
      template.setContent(Hibernate.createClob(" "));
      session.save(template);
      session.flush();
      session.refresh(template, LockMode.UPGRADE);

      //update clob
      String updateSql =
          "UPDATE ytapl_scalarquerytemplate SET content = ? WHERE templateID = " +
          template.getTemplateID();
      StringWriter sw = new StringWriter();
      castorTemplate.marshal(sw);
      Config.getCurrentDatabase().UpdateClob(template.getContent(),
                                             sw.toString(), updateSql,
                                             session.connection());

      return template;
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
  }

  /**
   * ����ָ���ѯģ��
   * @param template Ҫ���µ�ģ�����
   * @param castorTemplate ��Ÿ�����Ϣ��Castorģ�����
   * @param session Session����
   * @return �Ѹ��µ�ģ��
   * @throws HibernateException
   */
  private ScalarQueryTemplateForm updateScalarQueryTemplate(
      ScalarQueryTemplateForm template,
      cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate castorTemplate,
      Session session) throws
      HibernateException {
    try {
      template.setName(castorTemplate.getName());
      template.setContent(Hibernate.createClob(" "));
      session.update(template);
      session.flush();
      session.refresh(template, LockMode.UPGRADE);

      //update clob
      String updateSql =
          "UPDATE ytapl_scalarquerytemplate SET content = ? WHERE templateID = " +
          template.getTemplateID();
      StringWriter sw = new StringWriter();
      castorTemplate.marshal(sw);
      Config.getCurrentDatabase().UpdateClob(template.getContent(),
                                             sw.toString(), updateSql,
                                             session.connection());

      return template;
    }
    catch (Exception ex) {
      throw new HibernateException(ex);
    }
  }

  /**
   * �õ�ģ��form
   * @param taskID ����id
   * @param userID �û�id
   * @param name ģ������
   * @param session Session����
   * @return ģ��form
   * @throws HibernateException
   */
  private ScalarQueryTemplateForm getScalarQueryTemplate(String taskID,
      Integer userID, String name, Session session) throws HibernateException {
    List result = session.find("FROM ScalarQueryTemplateForm as template WHERE template.taskID = ? AND template.userID = ? AND template.name = ?",
                               new Object[] {taskID, userID, name}
                               , new Type[] {Hibernate.STRING,
                               Hibernate.INTEGER,
                               Hibernate.STRING});
    return (ScalarQueryTemplateForm) result.get(0);
  }

  /**
   * �õ�ģ��form
   * @param templateID ģ��id
   * @param session Session����
   * @return ģ��form
   * @throws HibernateException
   */
  private ScalarQueryTemplateForm getScalarQueryTemplate(Integer templateID,
      Session session) throws HibernateException {
    List result = session.find(
        "FROM ScalarQueryTemplateForm as template WHERE template.templateID = ?",
        templateID, Hibernate.INTEGER);
    return (ScalarQueryTemplateForm) result.get(0);
  }

  /**
   * ��ָ���ѯ����ת��Ϊcastorģ�����
   * @param name ģ������
   * @param condition ָ���ѯ����
   * @return castorģ�����
   */
  /**private cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate
   condition2Castor(String name, ScalarQueryForm condition)
    {
   cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate result =
       new cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate();
   //taskID
   result.setTaskID(condition.getTaskID());
   //name
   result.setName(name);
   //unitID
   cn.com.youtong.apollo.analyse.xml.Unit unit = new cn.com.youtong.apollo.
    analyse.xml.Unit();
   unit.setUnitID(condition.getUnitIDs());
   result.setUnit(unit);
   //scalars
   ScalarForm[] scalars = condition.getScalars();
   Row row = new Row();
   for (int i = 0; i < scalars.length; i++)
   {
    ScalarForm form = scalars[ i ];
    Cell cell = new Cell();
    cell.setName( form.getName() );
    cell.setExpression( form.getExpression() );
    row.addCell( cell );
   }
   if( result.getHeader() == null )
   {
    result.setHeader( new Header() );
   }
   // Ŀǰ��������ֻ����ʱ�ģ�û�е���͸��ݵġ�
   // ��ʱ�򴴽���ʱ�򣬲��������ְ취
   /**@todo FixMe: �߼��ǳ��д��� */
   /**result.getHeader().addRow( row );
      result.getHeader().setRownum( result.getHeader().getRownum() + 1 );
      result.getHeader().setColnum( scalars.length + 1 );
      return result;
     }*/

   /**
    * ɾ��ָ����ָ���ѯģ��
    * @param templateID ģ��id
    * @throws AnalyseException
    */
   public void deleteScalarQueryTemplate(Integer templateID) throws
       AnalyseException {
     Session session = null;
     Transaction tx = null;
     try {
       session = HibernateUtil.getSessionFactory().openSession();
       tx = session.beginTransaction();
       if (isScalarQueryTemplateDefined(templateID, session)) {
         session.delete("select template from ScalarQueryTemplateForm template where template.templateID =?",
                        templateID, Hibernate.INTEGER);
         tx.commit();
       }
       else {
         throw new AnalyseException("ָ����ָ���ѯģ�岻����");
       }
     }
     catch (HibernateException ex) {
       try {
         if (tx != null) {
           tx.rollback();
         }
       }
       catch (HibernateException ex2) {
       }
       throw new AnalyseException("ɾ��ָ���ѯģ��ʧ��", ex);
     }
     finally {
       HibernateUtil.close(session);
     }
   }

  /**
   * ��ѯָ����λ������
   * @param taskID ����id
   * @param taskTimeID ����ʱ��id
   * @param unitNodes ��λ��Ϣ����
   * @param session Hibernate Session
   * @return ����FillStateForm�ļ���
   * @throws AnalyseException
   */
  private Collection getFillState(String taskID, Integer taskTimeID,
                                  Collection unitNodes, Session session) throws
      AnalyseException {
    try {
      Collection forms = new TreeSet();

      TaskManagerFactory taskMngFcty =
          (TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.
          getName());
      TaskManager taskMng = taskMngFcty.createTaskManager();

      Task task = taskMng.getTaskByID(taskID);

      TaskTime taskTime = task.getTaskTime(taskTimeID);

      ModelManagerFactory modelMngFcty =
          (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.
          getName());
      ModelManager modelMng = modelMngFcty.createModelManager(taskID);

      // ��λid�б�
      List unitIDs = new LinkedList();
      for (Iterator iter = unitNodes.iterator(); iter.hasNext(); ) {
        UnitTreeNode node = (UnitTreeNode) iter.next();
        unitIDs.add(node.id());
      }

      // �鿴��λ�ϱ��������
      DBAnalyseHelper helper = new DBAnalyseHelper();
      List[] returnList = helper.hasReportData(taskID, unitIDs,
                                               taskTimeID.intValue(), session);
      List hasReportList = returnList[0];
      List flagList = returnList[1];
      List dateList = returnList[2];

      Iterator iter = unitNodes.iterator();
      for (int i = 0, size = unitIDs.size(); i < size; i++) {
        UnitTreeNode node = (UnitTreeNode) iter.next();
        Object obj;
        boolean hasReport = false;
        int flag = DataStatus.UNREPORT;
        java.util.Date date = null;

        obj = hasReportList.get(i);
        if (obj != null) {
          hasReport = ( (Boolean) obj).booleanValue();
        }
        obj = flagList.get(i);
        if (obj != null) {
          flag = ( (Integer) obj).intValue();
        }
        date = (java.util.Date) dateList.get(i);
        FillStateForm form = new FillStateForm(task, taskTime,
                                               node, hasReport,
                                               flag, date);
        forms.add(form);
      }

      return forms;
    }
    catch (Exception ex) {
      String message = "��ѯ��λ����������";
      throw new AnalyseException(message, ex);
    }
  }

  /**
   * ��ѯ�������λ������
   * @param taskID ����id
   * @param taskTimeID ����ʱ��id
   * @return ����FillStateForm�ļ���
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      Collection unitNodes = helper.getAllUnitNodes(taskID, session);
      return this.getFillState(taskID, taskTimeID, unitNodes, session);
    }
    catch (HibernateException ex) {
      String message = "��ѯ��λ����������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ��ѯָ����������Ƶ����λ��������ģ����ѯ��
   * @param taskID ����id
   * @param taskTimeID ����ʱ��id
   * @param codeOrName ��λ�Ĵ��������
   * @return ����FillStateForm�ļ���
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID,
                                 String codeOrName) throws AnalyseException {
    Collection result = getFillState(taskID, taskTimeID);
    //�ӽ����ɾ��������������codeOrName�ļ�¼
    for (Iterator itr = result.iterator(); itr.hasNext(); ) {
      FillStateForm form = (FillStateForm) itr.next();
      if (form.getUnit().getUnitCode().indexOf(codeOrName) < 0 &&
          form.getUnit().getUnitName().indexOf(codeOrName) < 0) {
        itr.remove();
      }
    }
    return result;
  }

  public FillStateForm getFillStateByUnitID(String taskID, Integer taskTimeID,
                                            String unitID) throws
      AnalyseException {
    FillStateForm returnForm = null;
    Collection result = getFillState(taskID, taskTimeID);
    //�ӽ����ɾ��������������codeOrName�ļ�¼
    for (Iterator itr = result.iterator(); itr.hasNext(); ) {
      FillStateForm form = (FillStateForm) itr.next();
      if (form.getUnit().getUnitCode().equals(unitID)) {
        returnForm = form;
        break;
      }
    }
    return returnForm;
  }

  /**
   * �޸��״̬
   * @param stateType
   * @param taskIDs
   * @param unitIDs
   * @param taskTimeIDs
   * @param session
   * @return
   * @throws HibernateException
   */
  public List alertState(int stateType, int taskID, List unitIDs,
                         int taskTimeID) throws AnalyseException {

    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      return helper.alertState(stateType, taskID, unitIDs, taskTimeID, session);
    }
    catch (HibernateException ex) {
      String message = "��ѯ��λ����������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * �Ƿ��ܹ��������
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws AnalyseException
   */
  public boolean canUnenvelopSubmitData(String taskID, int taskTimeID,
                                        String unitID) throws AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      return helper.canUnenvelopSubmitData(taskID, taskTimeID, unitID, session);
    }
    catch (HibernateException ex) {
      String message = unitID + " �����ܹ��������";
      log.error(message, ex);
      //throw new AnalyseException(message, ex);
      return false;
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * �������
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   */
  public void envelopSubmitData(String taskID, int taskTimeID, List unitIDList
                                ) throws AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      helper.envelopSubmitData(taskID, taskTimeID, unitIDList, session);
    }
    catch (HibernateException ex) {
      String message = "�Ƿ��ܹ��������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * �õ�����״̬
   * @param taskID
   * @param taskTimeID
   * @return
   */
  public HashMap getAllSubmitDataState(String taskID, int taskTimeID
                                       ) throws AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      return helper.getAllSubmitDataState(taskID, taskTimeID, session);
    }
    catch (HibernateException ex) {
      String message = "�Ƿ��ܹ��������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * �ܱ༭����
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws AnalyseException
   */
  public boolean canEditorSubmitData(String taskID, int taskTimeID,
                                     String unitID) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      return helper.canEditorSubmitData(taskID, taskTimeID, unitID, session);
    }
    catch (HibernateException ex) {
      String message = unitID + "������,���ܱ༭����";
      log.error(message, ex);
      //throw new AnalyseException(message, ex);
      return false;
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * ��������
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   * @param flag
   * @throws AnalyseException
   */
  public void unEnvelopSubmitData(String taskID, int taskTimeID,
                                  List unitIDList, int flag) throws
      AnalyseException {

    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      helper.unenvelopSubmitData(taskID, taskTimeID, unitIDList, flag, session);
    }
    catch (HibernateException ex) {
      String message = "�Ƿ��ܹ��������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * ��ѯ���е�λ��������
   * @param taskID
   * @param taskTimeID
   * @return
   * @throws AnalyseException
   */
  public List getAuditStates(String taskID, int taskTimeID) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      DBAnalyseHelper helper = new DBAnalyseHelper();
      List auditStates = helper.hasAuditData(taskID, taskTimeID, session);
      return auditStates;
    }
    catch (HibernateException ex) {
      String message = "��ѯ��λ������������";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**public static void main( String[] args )
    {
   try
   {
    cn.com.youtong.apollo.MockService.init();
    DBAnalyseManager mng = new DBAnalyseManager();
    FileInputStream input = new FileInputStream(
     "F:\\APOLLO\\www\\schema\\�ʲ���ծ��.xml" );
    mng.importScalarQueryTemplate( new Integer( 1 ), input );
    input.close();
    FileOutputStream output = new FileOutputStream( "f:/test.xml" );
    mng.exportScalarQueryTemplate( new Integer( 3 ), output );
    output.close();
    output = new FileOutputStream( "f:/test2.xml" );
    mng.exportScalarQueryTemplate( "QYKB", new Integer( 1 ), "�ʲ���ծ��", output );
    output.close();
    input = new FileInputStream( "f:/test.xml" );
    mng.importScalarQueryTemplate( new Integer( 1 ), input );
    input.close();
    cn.com.youtong.apollo.MockService.shutdown();
   }catch( Exception ex )
   {
    ex.printStackTrace();
   }
    }*/
}