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
 * 这这里可以不考虑权限, 但是必须能识别一定的单位范围.
 *
 * 比如, 给定某个单位范围, 查看该范围内的单位,上报情况.
 * 分析Manager的DB实现
 *
 *
 */
public class DBAnalyseManager
    implements AnalyseManager {
  /** log */
  Log log = LogFactory.getLog(DBAnalyseManager.class);

  /**
   * 导入指标查询模板
   * @param userID 用户id
   * @param templateIn 模板输入流
   * @throws AnalyseException
   */
  public void importScalarQueryTemplate(Integer userID,
                                        InputStream templateIn) throws
      AnalyseException {
    Session session = null;
    Transaction tx = null;
    try {
      log.info("开始导入指标查询模板");
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
      log.info("成功导入指标查询模板");
    }
    catch (Exception ex) {
      try {
        if (tx != null) {
          tx.rollback();
        }
      }
      catch (HibernateException ex2) {
      }
      String message = "导入指标查询模板失败";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 导出指标查询模板
   * @param templateID 模板ID
   * @param out 导出模板的输出流
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(Integer templateID, OutputStream out) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isScalarQueryTemplateDefined(templateID, session)) {
        log.info("开始导出指标查询模板templateID=" + templateID);
        ScalarQueryTemplateForm template = getScalarQueryTemplate(
            templateID, session);
        exportScalarQueryTemplate(template, out);
        log.info("成功导出指标查询模板templateID=" + templateID);
      }
      else {
        throw new AnalyseException("指定的指标查询模板不存在");
      }
    }
    catch (HibernateException ex) {
      String message = "导出指标查询模板templateID=" + templateID + "失败";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 导出指标查询模板
   * @param template 模板对象
   * @param out 导出模板的输出流
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
   * 导出指标查询模板
   * @param taskID 任务ID
   * @param userID 用户ID
   * @param name 模板名称
   * @param out 导出模板的输出流
   * @throws AnalyseException
   */
  public void exportScalarQueryTemplate(String taskID, Integer userID,
                                        String name, OutputStream out) throws
      AnalyseException {
    Session session = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();

      if (isScalarQueryTemplateDefined(taskID, userID, name, session)) {
        log.info("开始导出指标查询模板" + name);
        ScalarQueryTemplateForm template = getScalarQueryTemplate(
            taskID, userID, name, session);
        exportScalarQueryTemplate(template, out);
        log.info("成功导出指标查询模板" + name);
      }
      else {
        throw new AnalyseException("指定的指标查询模板不存在");
      }
    }
    catch (HibernateException ex) {
      String message = "导出指标查询模板" + name + "失败";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 按条件进行指标查询
   * @param condition 查询条件
   * @return 查询结果
   * @throws AnalyseException 查询失败
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
      throw new AnalyseException("指标查询失败", ex);
    }
  }

  /**
   * 指定需要查询的单位进行模板查询
   * @param condition           查询条件
   * @param unitIDs               指定的查询单位
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
      throw new AnalyseException("指标查询失败", ex);
    }
  }

  /**
   * 得到单位数组
   * @param units 单位集合
   * @return 单位数组
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
   * 得到指定任务的任务时间数组
   * @param task 任务
   * @param taskTimeIDs 任务时间ID数组
   * @return 任务时间数组
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
   * 得到指标查询结果数组
   * @param task 任务
   * @param units 单位数组
   * @param taskTimes 所属期数组
   * @param scalars 指标数组
   * @return 指标查询结果数组
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
      Map unitID2PositionMap = new HashMap(); // 标识unitID和它在数组中的位置
      for (int i = 0; i < units.length; i++) {
        unitID2PositionMap.put(units[i].id(), new Integer(i));
      }

      Iterator[] iters = new Iterator[taskTimes.length];
      for (int i = 0; i < iters.length; i++) {
        // 对于每个taskTime都取得对应的Iterator
        iters[i] = dataSource.get(unitID2PositionMap.keySet(),
                                  taskTimes[i]);
      }

      // 进行脚本计算
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
      log.error("不能获得ModelManagerFactory", ex);
      throw new ScriptException("", ex);
    }
    catch (ModelException ex) {
      throw new ScriptException("取数据发生异常", ex);
    }
  }

  /**
   * 得到指标表达式列表
   * @param scalars 指标数组
   * @return 指标表达式列表
   */
  private List getExpressions(ScalarForm[] scalars) {
    List expressions = new LinkedList();
    for (int i = 0; i < scalars.length; i++) {
      expressions.add(scalars[i].getExpression());
    }
    return expressions;
  }

  /**
   * 按模板进行指标查询
   * @param templateID 查询模板ID
   * @param taskTimeIDs 任务时间ID数组
   * @return 查询结果
   * @throws AnalyseException 查询失败
   */
//	public ScalarResultForm queryScalar(Integer templateID,
//										Integer[] taskTimeIDs) throws
//		AnalyseException
//	{
//		ScalarQueryTemplate template = getScalarQueryTemplate(templateID);
//		//设置任务时间条件
//		template.getCondition().setTaskTimeIDs(taskTimeIDs);
//
//		return queryScalar(template.getCondition());
//	}

  /**
   * 指定单位，和任务时间，执行标识为templateID的查询模板
   * @param templateID                 查询模板id
   * @param taskTimeIDs                任务时间
   * @param unitIDs                    单位代码
   * @return                           ScalarResultForm
   * @throws AnalyseException
   */
  public ScalarResultForm queryScalar(Integer templateID,
                                      Integer[] taskTimeIDs,
                                      String[] unitIDs) throws AnalyseException {
    ScalarQueryTemplate template = getScalarQueryTemplate(templateID);
    //设置任务时间条件
    template.getCondition().setTaskTimeIDs(taskTimeIDs);

    return queryScalar(template.getCondition(), unitIDs);
  }

  /**
   * 得到指定用户，指定任务的指标查询模板
   * @param userID 用户id
   * @param taskID 任务id
   * @return 指标查询模板ScalarQueryTemplate的Iterator
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
      throw new AnalyseException("查询指标查询模板失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 得到指定的指标查询模板
   * @param templateID 模板ID
   * @return 指标查询模板
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
        throw new AnalyseException("指定的指标查询模板不存在");
      }
    }
    catch (HibernateException ex) {
      throw new AnalyseException("查询指标查询模板失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 保存指标查询模板
   * @param userID 用户ID
   * @param name 模板名称
   * @param condition 查询条件
   * @return 已保存的模板
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
     throw new AnalyseException("已经存在相同名字的指标查询模板");
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
    throw new AnalyseException("保存指标查询模板失败", ex);
   }
   finally
   {
    HibernateUtil.close( session );
   }
    }*/

  /**
   * 更新指标查询模板
   * @param templateID 模板ID
   * @param name 模板名称
   * @param condition 查询条件
   * @return 已保存的模板
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
      throw new AnalyseException("已经存在相同名字的指标查询模板");
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
     throw new AnalyseException("指定的指标查询模板不存在");
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
    throw new AnalyseException("更新指标查询模板失败", ex);
   }
   finally
   {
    HibernateUtil.close( session );
   }
    }*/

  /**
   * 判断指标查询模板是否存在
   * @param taskID 任务id
   * @param userID 用户id
   * @param name 模板名称
   * @param session Session对象
   * @return 存在，返回true；否则返回false
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
   * 判断指标查询模板是否存在
   * @param templateID 模板id
   * @param session Session对象
   * @return 存在，返回true；否则返回false
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
   * 创建指标查询模板
   * @param userID 用户id
   * @param castorTemplate Castor模板对象
   * @param session Session对象
   * @return 已创建的模板
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
   * 更新指标查询模板
   * @param template 要更新的模板对象
   * @param castorTemplate 存放更新信息的Castor模板对象
   * @param session Session对象
   * @return 已更新的模板
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
   * 得到模板form
   * @param taskID 任务id
   * @param userID 用户id
   * @param name 模板名称
   * @param session Session对象
   * @return 模板form
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
   * 得到模板form
   * @param templateID 模板id
   * @param session Session对象
   * @return 模板form
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
   * 将指标查询条件转换为castor模板对象
   * @param name 模板名称
   * @param condition 指标查询条件
   * @return castor模板对象
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
   // 目前这种做法只是暂时的，没有道理和根据的。
   // 到时候创建的时候，不采用这种办法
   /**@todo FixMe: 逻辑非常有错误！ */
   /**result.getHeader().addRow( row );
      result.getHeader().setRownum( result.getHeader().getRownum() + 1 );
      result.getHeader().setColnum( scalars.length + 1 );
      return result;
     }*/

   /**
    * 删除指定的指标查询模板
    * @param templateID 模板id
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
         throw new AnalyseException("指定的指标查询模板不存在");
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
       throw new AnalyseException("删除指标查询模板失败", ex);
     }
     finally {
       HibernateUtil.close(session);
     }
   }

  /**
   * 查询指定单位的填报情况
   * @param taskID 任务id
   * @param taskTimeID 任务时间id
   * @param unitNodes 单位信息集合
   * @param session Hibernate Session
   * @return 填报情况FillStateForm的集合
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

      // 单位id列表
      List unitIDs = new LinkedList();
      for (Iterator iter = unitNodes.iterator(); iter.hasNext(); ) {
        UnitTreeNode node = (UnitTreeNode) iter.next();
        unitIDs.add(node.id());
      }

      // 查看单位上报数据情况
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
      String message = "查询单位的填报情况出错";
      throw new AnalyseException(message, ex);
    }
  }

  /**
   * 查询所有填报单位的填报情况
   * @param taskID 任务id
   * @param taskTimeID 任务时间id
   * @return 填报情况FillStateForm的集合
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
      String message = "查询单位的填报情况出错";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 查询指定代码或名称的填报单位的填报情况（模糊查询）
   * @param taskID 任务id
   * @param taskTimeID 任务时间id
   * @param codeOrName 单位的代码或名称
   * @return 填报情况FillStateForm的集合
   * @throws AnalyseException
   */
  public Collection getFillState(String taskID, Integer taskTimeID,
                                 String codeOrName) throws AnalyseException {
    Collection result = getFillState(taskID, taskTimeID);
    //从结果中删除出不满足条件codeOrName的记录
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
    //从结果中删除出不满足条件codeOrName的记录
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
   * 修改填报状态
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
      String message = "查询单位的填报情况出错";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 是否能够封存数据
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
      String message = unitID + " 不能能够封存数据";
      log.error(message, ex);
      //throw new AnalyseException(message, ex);
      return false;
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 封存数据
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
      String message = "是否能够封存数据";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 得到数据状态
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
      String message = "是否能够封存数据";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 能编辑数据
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
      String message = unitID + "有问题,不能编辑数据";
      log.error(message, ex);
      //throw new AnalyseException(message, ex);
      return false;
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 启封数据
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
      String message = "是否能够封存数据";
      log.error(message, ex);
      throw new AnalyseException(message, ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }

  /**
   * 查询所有单位的审核情况
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
      String message = "查询单位的审核情况出错";
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
     "F:\\APOLLO\\www\\schema\\资产负债率.xml" );
    mng.importScalarQueryTemplate( new Integer( 1 ), input );
    input.close();
    FileOutputStream output = new FileOutputStream( "f:/test.xml" );
    mng.exportScalarQueryTemplate( new Integer( 3 ), output );
    output.close();
    output = new FileOutputStream( "f:/test2.xml" );
    mng.exportScalarQueryTemplate( "QYKB", new Integer( 1 ), "资产负债率", output );
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