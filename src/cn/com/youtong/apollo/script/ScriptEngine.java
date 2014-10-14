package cn.com.youtong.apollo.script;

/**
 * 脚本引擎
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.*;

import org.mozilla.javascript.*;
import org.mozilla.javascript.Script;
import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.TaskData;
import cn.com.youtong.apollo.task.Task;

public class ScriptEngine {
  private static Log log = LogFactory.getLog(ScriptEngine.class);

  private static String SCRIPT = "javascript";

  /**
   * 数据源对象名称
   */
  public static final String OBJECT_DATASOURCE = "ds";

  /**
   * 任务对象名称
   */
  public static final String OBJECT_TASK = "task";

  /**
   * 审核对象名称
   */
  public static final String OBJECT_AUDIT = "audit";

  /**
   * 当前单位
   */
  public static final String OBJECT_UNIT = "unit";

  /**
   * 当前任务时间
   */
  public static final String OBJECT_TIME = "time";

  public ScriptEngine() {
  }

  /**
   * 执行计算脚本.
   * 计算结果的更新,采用任务数据taskData{@link TaskData#getDataSource}
   * 包含的DataSource进行更新操作{@link DataSource#update}.
   * 所以,此方法调用后,不能保证所有的更新被提交到数据库.
   * 在一次或者多次执行脚本后,调用{@link DataSource#commit}确保更新被提交.
   * @param taskData  任务数据
   * @param scripts 脚本组，集合元素类型为Script
   * @throws ScriptException
   * @see DataSource#update, DataSource#commit, DBDataImporter#excuteScripts
   */
  public void execCalculateScript(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      //declare data source object to be used in script
      scope.put(OBJECT_DATASOURCE, scope, taskData.getDataSource());

      //declare task object to be used in script
      scope.put(OBJECT_TASK, scope, new TaskBean(taskData));

      //declare table objects to be used in script
      Task task = taskData.getTask();
      List tables = new LinkedList();
      for (Iterator iter = taskData.getScriptTables().values().iterator();
           iter.hasNext(); ) {
        Table table = (Table) iter.next();
        tables.add(table);
        scope.put(table.tableid.toLowerCase(), scope, table);
      }

      //declare default time
      scope.put(OBJECT_TIME, scope, taskData.getTaskTime());

      //declare default unit
      scope.put(OBJECT_UNIT, scope, taskData.getUnitID());

      //excute script
      for (Iterator ite = scripts.iterator(); ite.hasNext(); ) {
        Script script = (Script) ite.next();
        script.exec(cx, scope);
      }

      //update data
      taskData.getDataSource().update(taskData);
    }
    catch (JavaScriptException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    catch (ModelException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 执行计算脚本.
   * 计算结果的更新,采用任务数据taskData{@link TaskData#getDataSource}
   * 包含的DataSource进行更新操作{@link DataSource#update}.
   * 所以,此方法调用后,不能保证所有的更新被提交到数据库.
   * 在一次或者多次执行脚本后,调用{@link DataSource#commit}确保更新被提交.
   * @param taskData  任务数据
   * @param scripts 脚本组，集合元素类型为Script
   * @throws ScriptException
   * @see DataSource#update, DataSource#commit, DBDataImporter#excuteScripts
   */
  public void execCalculateScript2(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      //declare data source object to be used in script
      scope.put(OBJECT_DATASOURCE, scope, taskData.getDataSource());

      //declare task object to be used in script
      scope.put(OBJECT_TASK, scope, new TaskBean(taskData));

      //declare table objects to be used in script
      Task task = taskData.getTask();
      List tables = new LinkedList();
      for (Iterator iter = taskData.getScriptTables().values().iterator();
           iter.hasNext(); ) {
        Table table = (Table) iter.next();
        tables.add(table);
        scope.put(table.tableid.toLowerCase(), scope, table);
      }

      //declare default time
      scope.put(OBJECT_TIME, scope, taskData.getTaskTime());

      //declare default unit
      scope.put(OBJECT_UNIT, scope, taskData.getUnitID());

      //excute script
      for (Iterator ite = scripts.iterator(); ite.hasNext(); ) {
        Map map = (Map) ite.next();
        Script script = (Script) map.get("content");
        script.exec(cx, scope);
      }

      //update data
      taskData.getDataSource().update(taskData);
    }
    catch (JavaScriptException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    catch (ModelException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 执行审核脚本
   * @param taskData 任务数据
   * @param scripts 脚本组，集合元素类型为Script
   * @return 审核结果
   * @throws ScriptException
   */
  public AuditResult execAuditScript(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      //declare data source object to be used in script
      scope.put(OBJECT_DATASOURCE, scope, taskData.getDataSource());

      //declare task object to be used in script
      scope.put(OBJECT_TASK, scope, new TaskBean(taskData));

      //declare table objects to be used in script
      Task task = taskData.getTask();
      List tables = new LinkedList();
      for (Iterator iter = taskData.getScriptTables().values().iterator();
           iter.hasNext(); ) {
        Table table = (Table) iter.next();
        tables.add(table);
        scope.put(table.tableid.toLowerCase(), scope, table);
      }

      //declare default time
      scope.put(OBJECT_TIME, scope, taskData.getTaskTime());

      //declare default unit
      scope.put(OBJECT_UNIT, scope, taskData.getUnitID());

      //declare result bean
      AuditResult result = new AuditResult();
      scope.put(OBJECT_AUDIT, scope, result);

      //excute script
      for (Iterator ite = scripts.iterator(); ite.hasNext(); ) {
        Script script = (Script) ite.next();
        script.exec(cx, scope);
      }

      return result;
    }
    catch (JavaScriptException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 执行审核脚本
   * @param taskData 任务数据
   * @param scripts 脚本组，集合元素类型为Map,content为Script
   * @return 审核结果
   * @throws ScriptException
   */
  public List execAuditScript2(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      //declare data source object to be used in script
      scope.put(OBJECT_DATASOURCE, scope, taskData.getDataSource());

      //declare task object to be used in script
      scope.put(OBJECT_TASK, scope, new TaskBean(taskData));

      //declare table objects to be used in script
      Task task = taskData.getTask();
      List tables = new LinkedList();
      for (Iterator iter = taskData.getScriptTables().values().iterator();
           iter.hasNext(); ) {
        Table table = (Table) iter.next();
        tables.add(table);
        scope.put(table.tableid.toLowerCase(), scope, table);
      }

      //declare default time
      scope.put(OBJECT_TIME, scope, taskData.getTaskTime());

      //declare default unit
      scope.put(OBJECT_UNIT, scope, taskData.getUnitID());

      //定义返回的List
      List results = new LinkedList();

      //excute script
      for (Iterator ite = scripts.iterator(); ite.hasNext(); ) {
        //declare result bean
        AuditResult result = new AuditResult();
        scope.put(OBJECT_AUDIT, scope, result);
        Map map = (Map) ite.next();
        Script script = (Script) map.get("content");
        script.exec(cx, scope);
        Map resultmap = new HashMap();
        resultmap.put("name", map.get("name"));
        resultmap.put("content", result);
        results.add(resultmap);
      }

      return results;
    }
    catch (JavaScriptException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 计算表达式
   * @param taskData 任务数据
   * @param exps 表达式集合，集合元素类型为Script
   * @return 返回表达式计算结果
   * @throws ScriptException
   */
  public List eval(TaskData taskData, List exps) throws ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      //declare data source object to be used in script
      scope.put(OBJECT_DATASOURCE, scope, taskData.getDataSource());

      //declare task object to be used in script
      scope.put(OBJECT_TASK, scope, new TaskBean(taskData));

      //declare table objects to be used in script
      Task task = taskData.getTask();
      List tables = new LinkedList();
      for (Iterator iter = taskData.getScriptTables().values().iterator();
           iter.hasNext(); ) {
        Table table = (Table) iter.next();
        tables.add(table);
        scope.put(table.tableid.toLowerCase(), scope, table);
      }

      //declare default time
      scope.put(OBJECT_TIME, scope, taskData.getTaskTime());

      //declare default unit
      scope.put(OBJECT_UNIT, scope, taskData.getUnitID());

      //eval script
      List results = new LinkedList();
      for (Iterator ite = exps.iterator(); ite.hasNext(); ) {
        Script expr = (Script) ite.next();
        Object value = expr.exec(cx, scope);
        results.add(value);
      }

      return results;
    }
    catch (JavaScriptException e) {
      log.error("脚本运行失败", e);
      throw new ScriptException("脚本运行失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * Translate YouTong audit script to standard javascript.
   * @param script
   * @return
   * @throws ScriptException
   */
  public static String ytauditScript2Js(String script) throws IOException {
    StringReader reader = new StringReader(script);
    StringWriter out = new StringWriter();
    Script2Js js = new Script2Js(reader);
    js.setOutput(out);
    js.yylex();

    return out.toString();
  }

  /**
   * Translate YouTong script to lower case
   * @param script
   * @return lower case script
   * @throws ScriptException
   */
  public static String ytScript2Lower(String script) throws IOException {
    StringReader reader = new StringReader(script);
    StringWriter out = new StringWriter();
    Script2Lowwer lowwer = new Script2Lowwer(reader);
    lowwer.setOutput(out);
    lowwer.yylex();

    return out.toString();
  }

  /**
   * 编译脚本
   * @param sources 待编译的脚本source集合，元素类型为String
   * @return 编译后的脚本集合，元素类型为Script
   */
  public List compile(List sources) throws ScriptException {
    try {
      List result = new LinkedList();

      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      for (Iterator itr = sources.iterator(); itr.hasNext(); ) {
        //大小写不敏感
        String source = ytScript2Lower( (String) itr.next());
        Script script = cx.compileReader(scope, new StringReader(source), "脚本",
                                         0, null);
        result.add(script);
      }
      return result;
    }
    catch (IOException e) {
      throw new ScriptException("脚本编译失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 编译脚本
   * @param sources 待编译的脚本source集合，元素类型为Map,content为String
   * @return 编译后的脚本集合，元素类型为Map,content为org.mozilla.javascript.Script
   */
  public List compile2(List sources) throws ScriptException {
    try {
      List result = new LinkedList();

      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //在脚本中，把JavaPrimitiveWrap对象当作Primitive使用
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      for (Iterator itr = sources.iterator(); itr.hasNext(); ) {
        Map map = (Map) itr.next();
        //大小写不敏感
        String source = ytScript2Lower( (String) map.get("content"));
        Script script = cx.compileReader(scope, new StringReader(source), "脚本",
                                         0, null);
        Map retmap = new HashMap();
        map.put("name", map.get("name"));
        map.put("content", script);
        result.add(map);
      }
      return result;
    }
    catch (IOException e) {
      log.error(e);
      throw new ScriptException("脚本编译失败", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * 增加将脚本的Scope去掉
   */
  public void finalize() {

  }

}