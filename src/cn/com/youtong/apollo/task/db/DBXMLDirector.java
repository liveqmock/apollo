package cn.com.youtong.apollo.task.db;

import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;
import net.sf.hibernate.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBXMLDirector
{

	private static Log log = LogFactory.getLog( DBXMLDirector.class );

  public DBXMLDirector()
  {
  }

  /**
   * 将TaskForm树存入数据库
   * 创建成功则返回　任务的ID号taskID，　创建失败则抛出异常
   * @param xmlBuilder DBXMLBuilder对象，包含创建任务所需要的各种信息
   * @return 创建的任务的ID号taskID
   * @throws TaskException 创建失败则抛出异常
   */
  public static Integer create(DBXMLBuilder xmlBuilder)
      throws TaskException
  {

    Session session = null;
    Transaction tx = null;

    try
    {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();
      if (isTaskDefined(xmlBuilder.getTaskForm(), session))
      {
        throw new TaskException("该任务名称或ID已经存在");
      }

      //保存task
      session.save(xmlBuilder.getTaskForm());
      //保存TaskTime
      saveTaskTimes(xmlBuilder.getTaskForm(), session);
      //保存table,Row,Cell
      saveTables(xmlBuilder.getTaskForm(), session);
      //保存UnitMetaFrom
      session.save(xmlBuilder.getTaskForm().getUnitMeta());

      //创建数据表
      executeSQLS(xmlBuilder.getCreateSqls(), session.connection());

      tx.commit();
    }
    catch (HibernateException ex)
    {
      try
      {
        tx.rollback();
      }
      catch (HibernateException ex1)
      {
      }
      throw new TaskException("创建任务失败", ex);
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
    return xmlBuilder.getTaskForm().getTaskID();
  }

  /**
   * 将修改后的TaskForm树存入数据库
   * 创建失败则抛出异常
   * @param taskForm Hibernate对象树
   * @throws TaskException 创建失败则抛出异常
   */
  public void updateTask(TaskForm taskForm)
      throws TaskException
  {
    throw new UnsupportedOperationException("updateTask方法目前还不支持");
  }

  /**
   * 保存表
   * @param taskForm 任务对象
   * @param session Session对象
   * @throws HibernateException
   */
  private static void saveTables(TaskForm taskForm, Session session)
      throws HibernateException
  {

    Iterator itrTables = taskForm.getTables().iterator();
    while (itrTables.hasNext())
    { //循环处理每一张表
      TableForm tableForm = (TableForm) itrTables.next();
      //保存表
      session.save(tableForm);
      //保存行
      saveRows(tableForm, session);
    }
  }

  /**
   * 保存行
   * @param tableForm 表对象
   * @param session Session对象
   * @throws HibernateException
   */
  private static void saveRows(TableForm tableForm, Session session)
      throws HibernateException
  {
    Iterator itrRows = tableForm.getRows().iterator();
    while (itrRows.hasNext())
    { //循环处理每一行
      RowForm rowForm = (RowForm) itrRows.next();
      //保存行
      session.save(rowForm);
      //保存单元格
      saveCells(rowForm, session);
    }
  }

  /**
   * 保存单元格
   * @param rowForm 行对象
   * @param session Session
   * @throws HibernateException
   */
  private static void saveCells(RowForm rowForm, Session session)
      throws HibernateException
  {
    Iterator itrCells = rowForm.getCells().iterator();
    while (itrCells.hasNext())
    {
      CellForm cellForm = (CellForm) itrCells.next();
      session.save(cellForm);
    }
  }

  /**
   * 保存任务时间表
   * @param taskForm 任务对象
   * @param session Session对象
   * @throws HibernateException
   */
  private static void saveTaskTimes(TaskForm taskForm, Session session)
      throws HibernateException
  {

    Iterator itrTaskTime = taskForm.getTaskTimes().iterator();
    while (itrTaskTime.hasNext())
    {
      //保存TaskTime
      TaskTimeForm taskTimeForm = (TaskTimeForm) itrTaskTime.next();
      session.save(taskTimeForm);
    }
  }

  /**
   * 执行SQL语句创建任务数据表
   * @param sqls 创建数据表sql集合
   * @param conn 数据库连接
   * @throws HibernateException
   */
  private static void executeSQLS(Collection sqls, Connection conn)
      throws HibernateException
  {
    Statement st = null;
    try
    {
      st = conn.createStatement();
      Iterator sqlItr = sqls.iterator();
      if (!sqlItr.hasNext())
      {
        throw new HibernateException("未找到sql语句");
      }
      while (sqlItr.hasNext())
      {
        String sql = (String) sqlItr.next();

		log.info( sql );

        st.addBatch(sql);
      }
      st.executeBatch();
    }
    catch (SQLException ex)
    {
      throw new HibernateException(ex);
    }
    finally
    {
      SQLUtil.close(st);
    }
  }

  /**
   * 判断任务是否存在　
   * @param taskForm 任务对象
   * @param session Session
   * @return 存在则返回true,否则返回false
   * @throws HibernateException
   */
  private static boolean isTaskDefined(TaskForm taskForm, Session session)
      throws HibernateException
  {
    boolean result = false;

    boolean isNameDefined = ( (Integer) session.iterate(
        "SELECT COUNT(*) from TaskForm as task WHERE task.name = ?",
        taskForm.getName(), Hibernate.STRING).next()).intValue() > 0;

    boolean isIDDefined = ( (Integer) session.iterate(
        "SELECT COUNT(*) from TaskForm as task WHERE task.ID = ?",
        taskForm.getID(), Hibernate.STRING).next()).intValue() > 0;

    if (isNameDefined || isIDDefined)
    {
      result = true;
    }
    return result;
  }
}