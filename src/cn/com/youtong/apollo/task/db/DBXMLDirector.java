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
   * ��TaskForm���������ݿ�
   * �����ɹ��򷵻ء������ID��taskID��������ʧ�����׳��쳣
   * @param xmlBuilder DBXMLBuilder���󣬰���������������Ҫ�ĸ�����Ϣ
   * @return �����������ID��taskID
   * @throws TaskException ����ʧ�����׳��쳣
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
        throw new TaskException("���������ƻ�ID�Ѿ�����");
      }

      //����task
      session.save(xmlBuilder.getTaskForm());
      //����TaskTime
      saveTaskTimes(xmlBuilder.getTaskForm(), session);
      //����table,Row,Cell
      saveTables(xmlBuilder.getTaskForm(), session);
      //����UnitMetaFrom
      session.save(xmlBuilder.getTaskForm().getUnitMeta());

      //�������ݱ�
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
      throw new TaskException("��������ʧ��", ex);
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
   * ���޸ĺ��TaskForm���������ݿ�
   * ����ʧ�����׳��쳣
   * @param taskForm Hibernate������
   * @throws TaskException ����ʧ�����׳��쳣
   */
  public void updateTask(TaskForm taskForm)
      throws TaskException
  {
    throw new UnsupportedOperationException("updateTask����Ŀǰ����֧��");
  }

  /**
   * �����
   * @param taskForm �������
   * @param session Session����
   * @throws HibernateException
   */
  private static void saveTables(TaskForm taskForm, Session session)
      throws HibernateException
  {

    Iterator itrTables = taskForm.getTables().iterator();
    while (itrTables.hasNext())
    { //ѭ������ÿһ�ű�
      TableForm tableForm = (TableForm) itrTables.next();
      //�����
      session.save(tableForm);
      //������
      saveRows(tableForm, session);
    }
  }

  /**
   * ������
   * @param tableForm �����
   * @param session Session����
   * @throws HibernateException
   */
  private static void saveRows(TableForm tableForm, Session session)
      throws HibernateException
  {
    Iterator itrRows = tableForm.getRows().iterator();
    while (itrRows.hasNext())
    { //ѭ������ÿһ��
      RowForm rowForm = (RowForm) itrRows.next();
      //������
      session.save(rowForm);
      //���浥Ԫ��
      saveCells(rowForm, session);
    }
  }

  /**
   * ���浥Ԫ��
   * @param rowForm �ж���
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
   * ��������ʱ���
   * @param taskForm �������
   * @param session Session����
   * @throws HibernateException
   */
  private static void saveTaskTimes(TaskForm taskForm, Session session)
      throws HibernateException
  {

    Iterator itrTaskTime = taskForm.getTaskTimes().iterator();
    while (itrTaskTime.hasNext())
    {
      //����TaskTime
      TaskTimeForm taskTimeForm = (TaskTimeForm) itrTaskTime.next();
      session.save(taskTimeForm);
    }
  }

  /**
   * ִ��SQL��䴴���������ݱ�
   * @param sqls �������ݱ�sql����
   * @param conn ���ݿ�����
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
        throw new HibernateException("δ�ҵ�sql���");
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
   * �ж������Ƿ���ڡ�
   * @param taskForm �������
   * @param session Session
   * @return �����򷵻�true,���򷵻�false
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