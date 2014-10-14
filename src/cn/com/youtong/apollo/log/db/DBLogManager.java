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
 * log�������ӿڵ����ݿ�ʵ��
 */
public class DBLogManager
    implements LogManager {
  /**
   * ��¼��ȫ��־
   * @param timeOccured �¼�������ʱ��
   * @param type �¼�����
   * @param source �����¼��Ŀͻ��������IP��ַ
   * @param userName �����¼��ĵ�¼�û�������
   * @param memo �¼�����ϸ����
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
      throw new LogException("��¼��ȫ��־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ��¼�����ϱ���־
   * @param loadResult �ϱ����
   * @param user �ϱ����ݵ��û�
   * @param source ��Դ���ͻ���ip��
   * @param loadMode �ϱ���ʽ ��0 -- web�� 1 -- webservice�� 2 -- �ʼ����ļ�����
   * @throws LogException
   */
  public void logLoadDataEvent(LoadResult loadResult, User user,
                               String source,
                               int loadMode) throws LogException {
    //��¼������־

    String loadModeStr = "�ϱ���ʽ��";
    switch (loadMode) {
      case WEB_MODE:
        loadModeStr += "����ֱ���� ";
        break;
      case CLIENT_MODE:
        loadModeStr += "�ͻ���ֱ���� ";
        break;
      case MAIL_MODE:
        loadModeStr += "�ʼ����ļ����ϱ��� ";
        break;
      default:
        break;
    }

    String taskTime = "�ϱ����ݵ�����ʱ�䣺" +
        Convertor.date2MonthlyString(loadResult.getTaskTime().getFromTime()) +
        "�� ";

    if (loadResult.getOverdueUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.ERROR,
                   source,
                   user.getName(),
                   "�ϱ��������ݡ� " + loadModeStr + taskTime +
                   "�������������ĵ�λID��" +
                   getUnitListStr(loadResult.getOverdueUnitIDs()));
    }

    if (loadResult.getProhibitUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.ERROR,
                   source,
                   user.getName(),
                   "�ϱ��������а���û��Ȩ���ϱ������ݡ� " + loadModeStr + taskTime +
                   "��Ȩ�ϱ����������ĵ�λID��" +
                   getUnitListStr(loadResult.getProhibitUnitIDs()));

    }

    if (loadResult.getAuditFailedUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.WARNING,
                   source,
                   user.getName(),
                   "�ϱ��������д�������û��ͨ����ˡ� " + loadModeStr + taskTime +
                   "���δͨ�����������ĵ�λID��" +
                   getUnitListStr(loadResult.getAuditFailedUnitIDs()));

    }

    if (loadResult.getStoredUnitIDs().hasNext()) {
      logDataEvent(new java.util.Date(), Event.INFO,
                   source,
                   user.getName(),
                   "�ɹ��ϱ������ݡ� " + loadModeStr + taskTime +
                   "�ɹ��ϱ����������ĵ�λID��" +
                   getUnitListStr(loadResult.getStoredUnitIDs()));

    }

  }

  /**
   * �õ�unitID�б��ַ���
   * @param unitIDItr unitID(String����)��Iterator
   * @return unitID�б��ַ���
   */
  private String getUnitListStr(Iterator unitIDItr) {
    StringBuffer buf = new StringBuffer();
    buf.append("��");
    while (unitIDItr.hasNext()) {
      buf.append(unitIDItr.next());
      if (unitIDItr.hasNext()) {
        buf.append("��");
      }
    }
    buf.append("��");
    return buf.toString();
  }

  /**
   * ��¼������־
   * @param timeOccured �¼�������ʱ��
   * @param type �¼�����
   * @param source �����¼��Ŀͻ��������IP��ַ
   * @param userName �����¼��ĵ�¼�û�������
   * @param memo �¼�����ϸ����
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
      throw new LogException("��¼������־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * ��ѯ�¼�
   * @param condition ��ѯ����
   * @return �����������¼�Event���ϵ�Iterator
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

      //���������İ�ȫ�¼�
      result.addAll(session.find("from SecurityEventForm event " +
                                 whereClause, parameters.toArray(), types));

      //���������������¼�
      result.addAll(session.find("from DataEventForm event" + whereClause,
                                 parameters.toArray(), types));

      return result.iterator();
    }
    catch (HibernateException ex) {
      throw new LogException("��ѯ��ȫ��־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * �����¼���ѯ���������ѯhibernate-ql�е�where�Ӿ�
   * @param condition �¼���ѯ����
   * @param parameters �洢hibernate-ql�����ļ���
   * @param paramTypes �洢hibernate-ql�������͵ļ���
   * @return ��ѯsql�е�where�Ӿ�
   */
  private String buildWhereClause(EventQueryCondition condition,
                                  Collection parameters,
                                  Collection paramTypes) {
    //��¼�Ƿ���where�Ӿ�
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
    throw new LogException("��ѯ��ȫ��־ʧ��", ex);
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
      throw new LogException("��ѯ��ȫ��־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }

  }


  /**
   * �õ����а�ȫ�¼�
   * @return ��ȫ�¼�Event���ϵ�Iterator
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
      throw new LogException("��ѯ��ȫ��־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }

  /**
   * �õ����������¼�
   * @return �����¼�Event���ϵ�Iterator
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
      throw new LogException("��ѯ������־ʧ��", ex);
    }
    finally {
      HibernateUtil.close(session);
    }
  }
}
