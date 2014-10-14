package cn.com.youtong.apollo.common.sql;

import java.io.File;
import java.util.List;
import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;
import java.sql.*;
import java.util.ArrayList;
import cn.com.youtong.apollo.services.Config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HibernateUtil {
  public HibernateUtil() {
  }

  private static SessionFactory sessionFactory;

  /**
   * �õ�SessionFactory����
   * @return SessionFactory����
   * @throws HibernateException
   */
  public static SessionFactory getSessionFactory() throws HibernateException {
    if (sessionFactory == null) {
      String path = Config.getString("cn.com.youtong.apollo.hibernate.cfg.xml");

      sessionFactory = new Configuration().configure(new File(path)).
          buildSessionFactory();
    }
    return sessionFactory;
  }

  public static Session openSession() throws HibernateException {
    try {
      if (sessionFactory == null) {
        sessionFactory = getSessionFactory();
      }
      return sessionFactory.openSession();
    }
    catch (HibernateException he) {
      throw new HibernateException(he);
    }
  }

  /**
   * ִ��������SQL
   * @param sqlList
   * @throws HibernateException
   */
  public static void executeBatch(List sqlList) throws HibernateException {
    Session session = null;
    Statement stat = null;
    try {
      session = getSessionFactory().openSession();
      Connection conn = session.connection();
      stat = conn.createStatement();
      stat = conn.createStatement();
      for (int i = 0; i < sqlList.size(); i++) {
        String sql = (String) sqlList.get(i);
        stat.addBatch(sql);
      }
      stat.executeBatch();
    }
    catch (HibernateException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new HibernateException("ִ��SQL������ʱʧ��", ex);
    }
    finally {
      HibernateUtil.close(stat);
      HibernateUtil.close(session);
    }

  }

  /**
   *
   * @param stmt
   */
  public static void close(Statement stmt) {
    if (stmt != null) {
      try {
        stmt.close();
      }
      catch (SQLException sqle) {}
    }

  }

  /**
   * �õ��ű�������һ����¼ID
   * @return �ű�������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextScriptSuitID() throws HibernateException {
    return getNextID("YTAPL_ScriptSuits");
  }

  /**
   * �õ�ѡ����ܷ��������һ����¼ID
   * @return ѡ����ܷ��������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextSelectSumSchemaID() throws HibernateException {
    return getNextID("YTAPL_SelectSumSchemas");
  }

  /**
   * �õ�ָ���ѯģ������һ����¼ID
   * @return ָ���ѯģ������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextScalarQueryTemplateID() throws
      HibernateException {
    return getNextID("YTAPL_ScalarQueryTemplate");
  }

  /**
   * �õ��ű������һ����¼ID
   * @return �ű������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextScriptID() throws HibernateException {
    return getNextID("YTAPL_Scripts");
  }

  /**
   * �õ���ɫ�����һ����¼ID
   * @return ��ɫ�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextRoleID() throws HibernateException {
    return getNextID("YTAPL_Roles");
  }

  /**
   * �õ��û�������һ����¼ID
   * @return �û�������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextGroupID() throws HibernateException {
    return getNextID("YTAPL_Groups");
  }

  /**
   * �õ��û������һ����¼ID
   * @return �û������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextUserID() throws HibernateException {
    return getNextID("YTAPL_Users");
  }

  /**
   * �õ�ģ������һ����¼ID
   * @return ģ������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextTemplateID() throws HibernateException {
    return getNextID("ytapl_report_template");
  }

  /**
   * �õ�TaskTime�����һ��TaskTimeID
   * @return TaskTime�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextTaskTimeID() throws HibernateException {
    return getNextID("YTAPL_TaskTimes");
  }

  /**
   * �õ���������һ��TaskID
   * @return Tasks�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextTaskID() throws HibernateException {
    return getNextID("YTAPL_Tasks");
  }

  /**
   * �õ�Tables�����һ��TableID
   * @return Tables�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextTableID() throws HibernateException {
    return getNextID("YTAPL_Tables");
  }

  /**
   * �õ�Dictionarys�����һ��ID
   * @return Dictionarys�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextDictionaryID() throws HibernateException {
    return getNextID("YTAPL_Dictionarys");
  }

  /**
   * �õ�Rows�����һ��ID
   * @return Rows�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextRowID() throws HibernateException {
    return getNextID("YTAPL_Rows");
  }

  /**
   * �õ�Cells�����һ��ID
   * @return Cells�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextCellID() throws HibernateException {
    return getNextID("YTAPL_Cells");
  }

  /**
   * �õ�DictionaryEntries�����һ��ID
   * @return DictionaryEntries�����һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextDictionaryEntryID() throws HibernateException {
    return getNextID("YTAPL_DictionaryEntries");
  }

  /**
   * �õ���һ��ѡ����ܵ�λ�ĵ�λ����
   * @return ��һ��ѡ����ܵ�λ�ĵ�λ����
   * @throws HibernateException
   */
//    public static String getNextSelectSumUnitCode() throws HibernateException
//    {
//        String code = getNextID("YTAPL_SelectSumUnitCode").toString();
//        //��λ����ǰ�油0������9λ
//        while(code.length() < 9)
//        {
//            code = "0" + code;
//        }
//        return code;
//    }

  /**
   * �ж�ָ���ı��Ƿ��м�¼
   * @param tableName ����
   * @param session session����
   * @return ����м�¼������true�����򷵻�false
   * @throws HibernateException
   */
  private static boolean hasRecord(String tableName, Session session) throws
      HibernateException {
    return ( (Integer) session.iterate(
        "SELECT COUNT(*) FROM Sequence as sequence WHERE sequence.tableName = ?",
        tableName, Hibernate.STRING).next()).intValue() > 0;
  }

  /**
   * �õ�ָ�������һ����¼ID
   * @param tableName ����
   * @return ָ�������һ����¼ID
   * @throws HibernateException
   */
  public static Integer getNextID(String tableName) throws
      HibernateException {
    Session session = null;
    Transaction tx = null;
    try {
      Sequence sequence = new Sequence();
      session = getSessionFactory().openSession();
      tx = session.beginTransaction();
      if (hasRecord(tableName, session)) {
        session.load(sequence, tableName);
        //��¼ID�����ֵ����1
        sequence.setMaxID(new Integer(sequence.getMaxID().intValue() +
                                      1));
        session.update(sequence);
      }
      else {
        //���޼�¼����ʼ��Ϊ1
        sequence = new Sequence(tableName, new Integer(1));
        session.save(sequence);
      }
      tx.commit();
      return sequence.getMaxID();
    }
    catch (HibernateException ex) {
      tx.rollback();
      throw ex;
    }
    finally {
      if (session != null) {
        try {
          session.close();
        }
        catch (HibernateException ex) {
        }
      }
    }
  }

  /**
   * �ر����������Դ
   * @param rs                �����
   * @param pstmt             ��ѯ���
   * @param con               ���ݿ�����
   */
  public static void close(ResultSet rs, Statement pstmt) {
    if (rs != null) {
      try {
        rs.close();
      }
      catch (SQLException sqle) {}
    }
    if (pstmt != null) {
      try {
        pstmt.close();
      }
      catch (SQLException sqle) {}
    }

  }

  /**
   * �ر�Hibernate Session
   * @param session      Hibernate Session
   */
  public static void close(Session session) {
    if (session != null) {
      try {
        session.close();
      }
      catch (HibernateException he) {}
    }
  }

  public static void executeSQL(String sql) throws HibernateException {
    Session session = null;
    Connection conn = null;
    Statement stat = null;

    try {
      session = HibernateUtil.openSession();
      conn = session.connection();
      stat = conn.createStatement();
      stat.execute(sql);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new HibernateException("ϵͳ����:" + ex.getMessage());
    }
    finally {
      HibernateUtil.close(stat);
      HibernateUtil.close(session);
    }
  }

  /**
   * ִ��SQL
   * @param conn
   * @param sql
   */
  public static void executeBatchSQL(ArrayList sqlList) throws
      HibernateException {
    Session session = null;
    Connection conn = null;
    Statement stat = null;

    try {
      session = HibernateUtil.openSession();
      conn = session.connection();

      stat = conn.createStatement();
      for (int i = 0; i < sqlList.size(); i++) {
        String sql = (String) sqlList.get(i);
        stat.addBatch(sql);
      }
      stat.executeBatch();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new HibernateException("ϵͳ����:" + ex.getMessage());
    }
    finally {
      HibernateUtil.close(stat);
      HibernateUtil.close(session);
    }
  }

}