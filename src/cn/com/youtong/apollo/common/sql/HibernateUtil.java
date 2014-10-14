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
   * 得到SessionFactory对象
   * @return SessionFactory对象
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
   * 执行批处理SQL
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
      throw new HibernateException("执行SQL批处理时失败", ex);
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
   * 得到脚本组表的下一个记录ID
   * @return 脚本组表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextScriptSuitID() throws HibernateException {
    return getNextID("YTAPL_ScriptSuits");
  }

  /**
   * 得到选择汇总方案表的下一个记录ID
   * @return 选择汇总方案表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextSelectSumSchemaID() throws HibernateException {
    return getNextID("YTAPL_SelectSumSchemas");
  }

  /**
   * 得到指标查询模板表的下一个记录ID
   * @return 指标查询模板表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextScalarQueryTemplateID() throws
      HibernateException {
    return getNextID("YTAPL_ScalarQueryTemplate");
  }

  /**
   * 得到脚本表的下一个记录ID
   * @return 脚本表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextScriptID() throws HibernateException {
    return getNextID("YTAPL_Scripts");
  }

  /**
   * 得到角色表的下一个记录ID
   * @return 角色表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextRoleID() throws HibernateException {
    return getNextID("YTAPL_Roles");
  }

  /**
   * 得到用户组表的下一个记录ID
   * @return 用户组表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextGroupID() throws HibernateException {
    return getNextID("YTAPL_Groups");
  }

  /**
   * 得到用户表的下一个记录ID
   * @return 用户表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextUserID() throws HibernateException {
    return getNextID("YTAPL_Users");
  }

  /**
   * 得到模板表的下一个记录ID
   * @return 模板表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextTemplateID() throws HibernateException {
    return getNextID("ytapl_report_template");
  }

  /**
   * 得到TaskTime表的下一个TaskTimeID
   * @return TaskTime表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextTaskTimeID() throws HibernateException {
    return getNextID("YTAPL_TaskTimes");
  }

  /**
   * 得到任务表的下一个TaskID
   * @return Tasks表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextTaskID() throws HibernateException {
    return getNextID("YTAPL_Tasks");
  }

  /**
   * 得到Tables表的下一个TableID
   * @return Tables表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextTableID() throws HibernateException {
    return getNextID("YTAPL_Tables");
  }

  /**
   * 得到Dictionarys表的下一个ID
   * @return Dictionarys表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextDictionaryID() throws HibernateException {
    return getNextID("YTAPL_Dictionarys");
  }

  /**
   * 得到Rows表的下一个ID
   * @return Rows表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextRowID() throws HibernateException {
    return getNextID("YTAPL_Rows");
  }

  /**
   * 得到Cells表的下一个ID
   * @return Cells表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextCellID() throws HibernateException {
    return getNextID("YTAPL_Cells");
  }

  /**
   * 得到DictionaryEntries表的下一个ID
   * @return DictionaryEntries表的下一个记录ID
   * @throws HibernateException
   */
  public static Integer getNextDictionaryEntryID() throws HibernateException {
    return getNextID("YTAPL_DictionaryEntries");
  }

  /**
   * 得到下一个选择汇总单位的单位代码
   * @return 下一个选择汇总单位的单位代码
   * @throws HibernateException
   */
//    public static String getNextSelectSumUnitCode() throws HibernateException
//    {
//        String code = getNextID("YTAPL_SelectSumUnitCode").toString();
//        //单位代码前面补0，补满9位
//        while(code.length() < 9)
//        {
//            code = "0" + code;
//        }
//        return code;
//    }

  /**
   * 判断指定的表是否有记录
   * @param tableName 表名
   * @param session session对象
   * @return 如果有记录，返回true；否则返回false
   * @throws HibernateException
   */
  private static boolean hasRecord(String tableName, Session session) throws
      HibernateException {
    return ( (Integer) session.iterate(
        "SELECT COUNT(*) FROM Sequence as sequence WHERE sequence.tableName = ?",
        tableName, Hibernate.STRING).next()).intValue() > 0;
  }

  /**
   * 得到指定表的下一个记录ID
   * @param tableName 表名
   * @return 指定表的下一个记录ID
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
        //记录ID的最大值增加1
        sequence.setMaxID(new Integer(sequence.getMaxID().intValue() +
                                      1));
        session.update(sequence);
      }
      else {
        //尚无记录，初始化为1
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
   * 关闭数据相关资源
   * @param rs                结果集
   * @param pstmt             查询语句
   * @param con               数据库连接
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
   * 关闭Hibernate Session
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
      throw new HibernateException("系统错误:" + ex.getMessage());
    }
    finally {
      HibernateUtil.close(stat);
      HibernateUtil.close(session);
    }
  }

  /**
   * 执行SQL
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
      throw new HibernateException("系统错误:" + ex.getMessage());
    }
    finally {
      HibernateUtil.close(stat);
      HibernateUtil.close(session);
    }
  }

}