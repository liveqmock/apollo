package cn.com.youtong.apollo.script;

/**
 * �ű�����
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
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
   * ����Դ��������
   */
  public static final String OBJECT_DATASOURCE = "ds";

  /**
   * �����������
   */
  public static final String OBJECT_TASK = "task";

  /**
   * ��˶�������
   */
  public static final String OBJECT_AUDIT = "audit";

  /**
   * ��ǰ��λ
   */
  public static final String OBJECT_UNIT = "unit";

  /**
   * ��ǰ����ʱ��
   */
  public static final String OBJECT_TIME = "time";

  public ScriptEngine() {
  }

  /**
   * ִ�м���ű�.
   * �������ĸ���,������������taskData{@link TaskData#getDataSource}
   * ������DataSource���и��²���{@link DataSource#update}.
   * ����,�˷������ú�,���ܱ�֤���еĸ��±��ύ�����ݿ�.
   * ��һ�λ��߶��ִ�нű���,����{@link DataSource#commit}ȷ�����±��ύ.
   * @param taskData  ��������
   * @param scripts �ű��飬����Ԫ������ΪScript
   * @throws ScriptException
   * @see DataSource#update, DataSource#commit, DBDataImporter#excuteScripts
   */
  public void execCalculateScript(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
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
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    catch (ModelException e) {
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ִ�м���ű�.
   * �������ĸ���,������������taskData{@link TaskData#getDataSource}
   * ������DataSource���и��²���{@link DataSource#update}.
   * ����,�˷������ú�,���ܱ�֤���еĸ��±��ύ�����ݿ�.
   * ��һ�λ��߶��ִ�нű���,����{@link DataSource#commit}ȷ�����±��ύ.
   * @param taskData  ��������
   * @param scripts �ű��飬����Ԫ������ΪScript
   * @throws ScriptException
   * @see DataSource#update, DataSource#commit, DBDataImporter#excuteScripts
   */
  public void execCalculateScript2(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
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
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    catch (ModelException e) {
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ִ����˽ű�
   * @param taskData ��������
   * @param scripts �ű��飬����Ԫ������ΪScript
   * @return ��˽��
   * @throws ScriptException
   */
  public AuditResult execAuditScript(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
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
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ִ����˽ű�
   * @param taskData ��������
   * @param scripts �ű��飬����Ԫ������ΪMap,contentΪScript
   * @return ��˽��
   * @throws ScriptException
   */
  public List execAuditScript2(TaskData taskData, List scripts) throws
      ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
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

      //���巵�ص�List
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
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ������ʽ
   * @param taskData ��������
   * @param exps ���ʽ���ϣ�����Ԫ������ΪScript
   * @return ���ر��ʽ������
   * @throws ScriptException
   */
  public List eval(TaskData taskData, List exps) throws ScriptException {
    try {
      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
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
      log.error("�ű�����ʧ��", e);
      throw new ScriptException("�ű�����ʧ��", e);
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
   * ����ű�
   * @param sources ������Ľű�source���ϣ�Ԫ������ΪString
   * @return �����Ľű����ϣ�Ԫ������ΪScript
   */
  public List compile(List sources) throws ScriptException {
    try {
      List result = new LinkedList();

      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      for (Iterator itr = sources.iterator(); itr.hasNext(); ) {
        //��Сд������
        String source = ytScript2Lower( (String) itr.next());
        Script script = cx.compileReader(scope, new StringReader(source), "�ű�",
                                         0, null);
        result.add(script);
      }
      return result;
    }
    catch (IOException e) {
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ����ű�
   * @param sources ������Ľű�source���ϣ�Ԫ������ΪMap,contentΪString
   * @return �����Ľű����ϣ�Ԫ������ΪMap,contentΪorg.mozilla.javascript.Script
   */
  public List compile2(List sources) throws ScriptException {
    try {
      List result = new LinkedList();

      Context cx = Context.enter();
      Scriptable scope = cx.initStandardObjects(null);

      //�ڽű��У���JavaPrimitiveWrap������Primitiveʹ��
      cx.getWrapFactory().setJavaPrimitiveWrap(false);

      for (Iterator itr = sources.iterator(); itr.hasNext(); ) {
        Map map = (Map) itr.next();
        //��Сд������
        String source = ytScript2Lower( (String) map.get("content"));
        Script script = cx.compileReader(scope, new StringReader(source), "�ű�",
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
      throw new ScriptException("�ű�����ʧ��", e);
    }
    finally {
      Context.exit();
    }
  }

  /**
   * ���ӽ��ű���Scopeȥ��
   */
  public void finalize() {

  }

}