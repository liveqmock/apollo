package cn.com.youtong.apollo.data.db;

import java.util.List;
import cn.com.youtong.apollo.task.Task;
import java.io.IOException;
import cn.com.youtong.apollo.common.Warning;
import javax.servlet.ServletException;
import cn.com.youtong.apollo.data.ModelManager;
import cn.com.youtong.apollo.data.ModelManagerFactory;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.task.TaskTime;
import cn.com.youtong.apollo.script.ScriptEngine;
import cn.com.youtong.apollo.script.AuditResult;
import cn.com.youtong.apollo.data.DataSource;
import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.analyse.form.AuditStateForm;
import java.util.Iterator;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.data.UnitTreeNodeUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.*;
import net.sf.hibernate.*;


/**
 * 辅助工具包
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBUtils {
  private static Log log = LogFactory.getLog( ScriptEngine.class );
  /**
   * 校验单个节点
   * @param unitID
   * @param task
   * @auditUser 审核人
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  public static List singleAudit(String unitID, Task task,Integer itaskTime,List scripts,String auditUser) throws
      Warning,
      IOException, ServletException

  {
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
            ModelManagerFactory.class.getName())).createModelManager(task.id());
    TaskTime taskTime = modelManager.getTask().getTaskTime(itaskTime);
    if (taskTime == null)
    {
            throw new Warning("指定的任务时间不存在");
    }

    AuditStateForm auditInfo = new AuditStateForm();
    auditInfo.setUnitID(unitID);
    auditInfo.setTaskID(task.id());
    auditInfo.setTaskTimeID(itaskTime);
    auditInfo.setAuditor(auditUser);

    ScriptEngine scriptEngine = new ScriptEngine();
    List auditScripts = scriptEngine.compile2(scripts);
    DBUnitTreeManager treeManager = new DBUnitTreeManager(task);
    DataSource dataSource = new DBDataSource(task,treeManager);
    TaskData taskData = dataSource.get(unitID,taskTime);
    List results = new ArrayList();
    try
    {
      List result =  scriptEngine.execAuditScript2(
            taskData, auditScripts);
      Map map = new HashMap();
      UnitTreeNode root = treeManager.getUnitTree(unitID);
      map.put("unitID",unitID);
      map.put("unitName",root.getUnitName());
      map.put("result",result);
      results.add(map);

      auditInfo.setFlag(new Integer(1));
    }
    catch(Exception ex)
    {
      log.error(ex);
      auditInfo.setFlag(new Integer(0));
      return null;
     }finally{
             auditInfo.setAuditDate(new java.util.Date());
        try {
                cn.com.youtong.apollo.analyse.db.DBAnalyseHelper.addAuditInfo(auditInfo);
        }
        catch (HibernateException ex1) {}
     }
    return results;
  }

  /**
 * 校验单个节点
 * @param unitID
 * @param task
 * @throws Warning
 * @throws IOException
 * @throws ServletException
 */
public static void singleExec(String unitID, Task task,Integer itaskTime,List scripts) throws
    Warning,
    IOException, ServletException

{
  ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
          ModelManagerFactory.class.getName())).createModelManager(task.id());
  TaskTime taskTime = modelManager.getTask().getTaskTime(itaskTime);
  if (taskTime == null)
  {
          throw new Warning("指定的任务时间不存在");
  }


  ScriptEngine scriptEngine = new ScriptEngine();
  List execScripts = scriptEngine.compile2(scripts);
  DBUnitTreeManager treeManager = new DBUnitTreeManager(task);
  DataSource dataSource = new DBDataSource(task,treeManager);
  TaskData taskData = dataSource.get(unitID,taskTime);
  List results = new ArrayList();
  try
  {
    scriptEngine.execCalculateScript2(taskData, execScripts);
  }
  catch(Exception ex)
  {log.error(ex); }
}

  /**
   * 校验节点包括子节点
   * @param unitID
   * @param task
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  public static List allAudit(String unitID, Task task,Integer itaskTime,List scripts) throws
      Warning,
      IOException, ServletException

  {
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
            ModelManagerFactory.class.getName())).createModelManager(task.id());
    TaskTime taskTime = modelManager.getTask().getTaskTime(itaskTime);
    if (taskTime == null)
    {
            throw new Warning("指定的任务时间不存在");
    }


    ScriptEngine scriptEngine = new ScriptEngine();
    List auditScripts = scriptEngine.compile2(scripts);
    DBUnitTreeManager treeManager = new DBUnitTreeManager(task);
    DataSource dataSource = new DBDataSource(task,treeManager);
    UnitTreeNode root = treeManager.getUnitTree(unitID);
    List results = new ArrayList();
    List children = new ArrayList();
    UnitTreeNodeUtil.getAllChildren(children,root);
    Iterator iter = children.iterator();
    while(iter.hasNext())
    {
      UnitTreeNode node = (UnitTreeNode)iter.next();
      String uid = node.id();
      TaskData taskData = dataSource.get(uid,taskTime);
      try
      {
       List result =  scriptEngine.execAuditScript2(
              taskData, auditScripts);
       Map map = new HashMap();
       map.put("unitID",uid);
       map.put("unitName",node.getUnitName());
       map.put("result",result);
       results.add(map);
      }
      catch(Exception ex)
      {log.error(ex); return null;}
    }
    AuditResult result = null;
    return results;
  }

  /**
   * 计算节点包括子节点
   * @param unitID
   * @param task
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  public static void allExec(String unitID, Task task,Integer itaskTime,List scripts) throws
      Warning,Exception
  {
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
            ModelManagerFactory.class.getName())).createModelManager(task.id());
    TaskTime taskTime = modelManager.getTask().getTaskTime(itaskTime);
    if (taskTime == null)
    {
            throw new Warning("指定的任务时间不存在");
    }


    ScriptEngine scriptEngine = new ScriptEngine();
    List calculateScripts = scriptEngine.compile2(scripts);
    DBUnitTreeManager treeManager = new DBUnitTreeManager(task);
    DataSource dataSource = new DBDataSource(task,treeManager);
    UnitTreeNode root = treeManager.getUnitTree(unitID);
    List children = new ArrayList();
    UnitTreeNodeUtil.getAllChildren(children,root);
    Iterator iter = children.iterator();
    while(iter.hasNext())
    {
      UnitTreeNode node = (UnitTreeNode)iter.next();
      String uid = node.id();
      TaskData taskData = dataSource.get(uid,taskTime);
      try
      {
       scriptEngine.execCalculateScript2(taskData,calculateScripts);
      }
      catch(Exception ex)
      {log.error(ex); throw ex;}
    }
  }

}
