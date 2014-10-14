package cn.com.youtong.apollo.log.db;

import java.util.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.log.*;
import cn.com.youtong.apollo.log.db.form.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.usermanager.*;
import net.sf.hibernate.*;
import net.sf.hibernate.type.*;

/**
 * log管理器接口的数据库实现
 */
public class DBLogManager
    implements LogManager {
  /**
   * 记录安全日志
   * @param timeOccured 事件发生的时间
   * @param type 事件类型
   * @param source 产生事件的客户计算机的IP地址
   * @param userName 触发事件的登录用户的名称
   * @param memo 事件的详细描述
   * @throws LogException
   */
  public void logSecurityEvent(Date timeOccured, int type, String source,
                               String userName, String memo) throws
      LogException {
    Session session = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();

      SecurityEventForm event = new SecurityEventForm(timeOccured, type,
          source, userName, memo);
      session.save(event);

      tx.commit();
    }
    catch (HibernateException ex) {
      try {
        tx.rollback();
      }
      catch (HibernateException ex1) {
      }
      throw new LogException("记录安全日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 记录数据上报日志
   * @param loadResult 上报结果
   * @param user 上报数据的用户
   * @param source 来源（客户机ip）
   * @param loadMode 上报方式 （0 -- web； 1 -- webservice； 2 -- 邮件（文件））
   * @throws LogException
   */
  public void logLoadDataEvent(LoadResult loadResult, User user,
                               String source,
                               int loadMode) throws LogException {
    //记录数据日志

    String loadModeStr = "上报方式：";
    switch (loadMode) {
      case WEB_MODE:
        loadModeStr += "网上直报。 ";
        break;
      case CLIENT_MODE:
        loadModeStr += "客户端直报。 ";
        break;
      case MAIL_MODE:
        loadModeStr += "邮件（文件）上报。 ";
        break;
      default:
        break;
    }

    String taskTime = "上报数据的任务时间：" +
        Convertor.date2MonthlyString(loadResult.getTaskTime().getFromTime()) +
        "。 ";

    if (loadResult.getOverdueUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.ERROR,
                   source,
                   user.getName(),
                   "上报过期数据。 " + loadModeStr + taskTime +
                   "过期数据所属的单位ID：" +
                   getUnitListStr(loadResult.getOverdueUnitIDs()));
    }

    if (loadResult.getProhibitUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.ERROR,
                   source,
                   user.getName(),
                   "上报的数据中包含没有权限上报的数据。 " + loadModeStr + taskTime +
                   "无权上报数据所属的单位ID：" +
                   getUnitListStr(loadResult.getProhibitUnitIDs()));

    }

    if (loadResult.getAuditFailedUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.WARNING,
                   source,
                   user.getName(),
                   "上报的数据中存在数据没有通过审核。 " + loadModeStr + taskTime +
                   "审核未通过数据所属的单位ID：" +
                   getUnitListStr(loadResult.getAuditFailedUnitIDs()));

    }

    if (loadResult.getStoredUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.INFO,
                   source,
                   user.getName(),
                   "成功上报的数据。 " + loadModeStr + taskTime +
                   "成功上报数据所属的单位ID：" +
                   getUnitListStr(loadResult.getStoredUnitIDs()));

    }

  }

  /**
   * 得到unitID列表字符串
   * @param unitIDItr unitID(String类型)的Iterator
   * @return unitID列表字符串
   */
  private String getUnitListStr(Iterator unitIDItr) {
    StringBuffer buf = new StringBuffer();
    buf.append("（");
    while (unitIDItr.hasNext()) {
      buf.append(unitIDItr.next());
      if (unitIDItr.hasNext()) {
        buf.append("，");
      }
    }
    buf.append("）");
    return buf.toString();
  }

  /**
   * 记录数据日志
   * @param timeOccured 事件发生的时间
   * @param type 事件类型
   * @param source 产生事件的客户计算机的IP地址
   * @param userName 触发事件的登录用户的名称
   * @param memo 事件的详细描述
   * @throws LogException
   */
  public void logDataEvent(Date timeOccured, int type, String source,
                           String userName, String memo) throws LogException {
    Session session = null;
    Transaction tx = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      tx = session.beginTransaction();

      DataEventForm event = new DataEventForm(timeOccured, type, source,
                                              userName,
                                              Hibernate.createClob(" "));
      session.save(event);
      session.flush();
      session.refresh(event, LockMode.UPGRADE);

      //update clob
      String updateSql =
          "UPDATE YTAPL_DataEvents SET memo = ? WHERE eventID = " +
          event.getEventID();
      Config.getCurrentDatabase().UpdateClob(event.getClobMemo(), memo,
                                             updateSql, session.connection());

      tx.commit();
    }
    catch (Exception ex) {
      try {
        tx.rollback();
      }
      catch (HibernateException ex1) {
      }
      throw new LogException("记录数据日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 查询事件
   * @param condition 查询条件
   * @return 满足条件的事件Event集合的Iterator
   * @throws LogException
   */
  public Iterator queryEvent(EventQueryCondition condition) throws
      LogException {
    Session session = null;
    try {
      TreeSet result = new TreeSet();
      session = HibernateUtil.getSessionFactory().openSession();

      Collection parameters = new LinkedList();
      Collection paramTypes = new LinkedList();

      String whereClause = buildWhereClause(condition, parameters,
                                            paramTypes);

      Type[] types = new Type[paramTypes.size()];
      Iterator itr = paramTypes.iterator();
      for (int i = 0; i < types.length; i++) {
        types[i] = (Type) itr.next();
      }

      //满足条件的安全事件
      result.addAll(session.find("from SecurityEventForm event " +
                                 whereClause, parameters.toArray(), types));

      //满足条件的数据事件
      result.addAll(session.find("from DataEventForm event" + whereClause,
                                 parameters.toArray(), types));

      return result.iterator();
    }
    catch (HibernateException ex) {
      throw new LogException("查询安全日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 根据事件查询条件构造查询hibernate-ql中的where子句
   * @param condition 事件查询条件
   * @param parameters 存储hibernate-ql参数的集合
   * @param paramTypes 存储hibernate-ql参数类型的集合
   * @return 查询sql中的where子句
   */
  private String buildWhereClause(EventQueryCondition condition,
                                  Collection parameters,
                                  Collection paramTypes) {
    //记录是否有where子句
    boolean hasWhereClause = false;

    StringBuffer clause = new StringBuffer(" where ");

    //type
    if (condition.getTypes() != null && condition.getTypes().length > 0) {
      int[] types = condition.getTypes();
      clause.append(" event.type in ( ");
      for (int i = 0; i < types.length; i++) {
        if (i != 0) {
          clause.append(" , ");
        }
        clause.append(types[i]);
      }
      clause.append(" ) ");
      hasWhereClause = true;
    }

    //timeOccured -- startTime
    if (condition.getStartTime() != null) {
      if (hasWhereClause) {
        clause.append(" and ");
      }

      clause.append(" event.timeOccured >= ? ");
      parameters.add(condition.getStartTime());
      paramTypes.add(Hibernate.TIMESTAMP);

      hasWhereClause = true;
    }

    //timeOccured -- endTime
    if (condition.getEndTime() != null) {
      if (hasWhereClause) {
        clause.append(" and ");
      }
      clause.append(" event.timeOccured <= ? ");
      parameters.add(condition.getEndTime());
      paramTypes.add(Hibernate.TIMESTAMP);

      hasWhereClause = true;
    }

    //source
    if (condition.getSource() != null &&
        !condition.getSource().trim().equals("")) {
      if (hasWhereClause) {
        clause.append(" and ");
      }
      clause.append(" event.source like ? ");
      parameters.add("%" + condition.getSource() + "%");
      paramTypes.add(Hibernate.STRING);

      hasWhereClause = true;
    }

    //userName
    if (condition.getUserName() != null &&
        !condition.getUserName().trim().equals("")) {
      if (hasWhereClause) {
        clause.append(" and ");
      }
      clause.append(" event.userName like ? ");
      parameters.add("%" + condition.getUserName() + "%");
      paramTypes.add(Hibernate.STRING);

      hasWhereClause = true;
    }

    //memo
    if (condition.getMemo() != null &&
        !condition.getMemo().trim().equals("")) {
      if (hasWhereClause) {
        clause.append(" and ");
      }
      clause.append(" event.memo like ? ");
      parameters.add("%" + condition.getMemo() + "%");
      paramTypes.add(Hibernate.STRING);

      hasWhereClause = true;
    }

    if (hasWhereClause) {
      return clause.toString();
    }
    else {
      return "";
    }
  }

  /**
   *
   * @return
   * @throws LogException
   */
 public int getSecurityEventsCount() throws LogException
 {
   Session session = null;
  try {
    List result = new ArrayList();
    session = HibernateUtil.getSessionFactory().openSession();

    String query="select COUNT(*) from SecurityEventForm";
    return ((Integer)session.iterate(query).next()).intValue();
  }
  catch (HibernateException ex) {
    throw new LogException("查询安全日志失败", ex);
  }
  finally {
    HibernateUtil.close(session);
  }

 }

  /**
   *
   * @param startPage
   * @param pageNumber
   * @return
   * @throws LogException
   */
  public Iterator getSecurityEvents(int begin, int pageNumber) throws
      LogException {
    Session session = null;
    try {
      List result = new ArrayList();
      session = HibernateUtil.getSessionFactory().openSession();

      Query q = session.createQuery("from SecurityEventForm");
      ScrollableResults sr = q.scroll();

      if (sr.first() && sr.scroll(begin)) {
        for (int i = begin; i < begin + pageNumber; i++) {
          result.add(sr.get(0));
          if (!sr.next()) {
            break;
          }

        }
      }
      // result.addAll(session.createQuery("from SecurityEventForm"));
      return result.iterator();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new LogException("查询安全日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }


  /**
   * 得到所有安全事件
   * @return 安全事件Event集合的Iterator
   * @throws LogException
   */
  public Iterator getAllSecurityEvents() throws LogException {
    Session session = null;
    try {
      TreeSet result = new TreeSet();
      session = HibernateUtil.getSessionFactory().openSession();
      result.addAll(session.find("from SecurityEventForm"));
      return result.iterator();
    }
    catch (HibernateException ex) {
      throw new LogException("查询安全日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * 得到所有数据事件
   * @return 数据事件Event集合的Iterator
   * @throws LogException
   */
  public Iterator getAllDataEvents() throws LogException {
    Session session = null;
    try {
      TreeSet result = new TreeSet();
      session = HibernateUtil.getSessionFactory().openSession();
      result.addAll(session.find("from DataEventForm"));
      return result.iterator();
    }
    catch (HibernateException ex) {
      throw new LogException("查询数据日志失败", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }
}
