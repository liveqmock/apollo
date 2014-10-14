package cn.com.youtong.apollo.analyse.db;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.sql.*;
import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.common.sql.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.analyse.form.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.analyse.*;
import org.apache.fulcrum.factory.*;
import cn.com.youtong.apollo.common.Convertor;

public class DBAnalyseHelper {
  /**
   * SQL -- 查询所有可上报数据的单位
   */
  private static final String QUERY_ALL_UNIT_SQL =
      "select distinct p.unitid from Ytapl_Unitpermissions p "
      //不包括选择汇总类型的单位
      + " where p.unitid NOT like '%" + ReportType.SELECT_GATHER_TYPE +
      "' AND p.taskid=? AND p.groupid in " +
      " (select distinct  g.Groupid from Ytapl_Groupmember g " +
      " where g.userid IN " +
      " (select u.userid from Ytapl_Users u))";

  /**
   * SQL -- 查询单位的上报情况
   */
  private static final String QUERY_FILL_STATE_SQL = "select * from Ytapl_FillState t where t.taskID=? and t.taskTimeID=? and t.unitID in ";
  /**
   * SQL -- 查询所有没有审核的单位
   */
  private static final String QUERY_READY_AUDIT_SQL =
      "select * from Ytapl_FillState t where t.taskID=? and t.flag=? or t.flag=? and t.unitID in ";
  /** 查询Ytapl_Fill_State表中的所有某个任务的所有unitID，mars工程目前没有任务管理，所以暂时采用这种办法*/
  private static final String QUERY_UNIT_FROM_FILL_STATE =
      "select distinct(unitID) from Ytapl_FillState where taskID = ?";
  /** 修改单位的状态 */
  private static final String ALERT_STATE = "update Ytapl_FillState t set t.flag = ? where t.taskID=? and t.unitID=? and t.taskTimeID=?";
  /** 查找单位状态 */
  private static final String SELECT_STATE = "select * from Ytapl_FillState ";
  /** 查找审核状态 */
  private static final String SELECT_AUDIT_STATE =
      "select * from Ytapl_AuditInfo ";

  private static final String QUERY_FILLSTATE_SQL="select * from Ytapl_fillstate where taskID=? and unitID=? and tasktimeID=? ";
  
  private static final String INSERT_DRAFT="insert into ytapl_draft values(?,?,?,?)";
  private static final String SELECT_DRAFT="select * from ytapl_draft where unitID=? and taskID=? and taskTimeID=? ";
  private static final String DELETE_DRAFT="delete from ytapl_draft where unitID=? and taskID=? and taskTimeID=?";
  /** Log */
  private Log log = LogFactory.getLog(DBAnalyseHelper.class.getName());

  public DBAnalyseHelper() {
  }

  //只启封本节点
  public static int SELF_NODE = 0;

  //启封儿子和本节点
  public static int SELF_AND_SON = 1;

  //启封后代和本节点
  public static int SELF_AND_SPRING = 2;

  /**
   * 封存数据
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   * @throws HibernateException
   */
  public void envelopSubmitData(String taskID, int taskTimeID, List unitIDList,
                                Session session) throws
      HibernateException {
    try {
      List sqlLisr = new ArrayList();
      HashMap needEnvelopUnits = this.getEnvelopUnitIDs(taskID, unitIDList);
      Iterator keyItr = needEnvelopUnits.keySet().iterator();
      HashMap submitDataState = getAllSubmitDataState(taskID, taskTimeID,
          session);
      while (keyItr.hasNext()) {
        String unitID = (String) keyItr.next();
        //已经递交数据
        if (submitDataState.containsKey(unitID)) {
          FillStateModel fillStateModel = (FillStateModel) submitDataState.get(
              unitID);
          int flag = fillStateModel.getFlag();
          if(unitID.endsWith("9")){                     //汇总单位都是上报封存
        	  flag = DataStatus.REPORTED_ENVLOP;
          }else if (fillStateModel.getFlag() == DataStatus.REPORTED_UNENVLOP)
            flag = DataStatus.REPORTED_ENVLOP;
          else if (fillStateModel.getFlag() == DataStatus.UNREPORTED_UNENVLOP)
            flag = DataStatus.UNREPORTED_ENVLOP;
          String updateSQl = "update ytapl_fillstate set FLAG='" +
              flag + "' where UNITID='" + unitID +
              "' and TASKID='" + taskID + "' and TASKTIMEID='" + taskTimeID +
              "'";
          sqlLisr.add(updateSQl);
          log.debug("封包updateSQl " + updateSQl);
        }
        else {
          int flag = DataStatus.UNREPORTED_ENVLOP;
          if(unitID.endsWith("9")) flag = DataStatus.REPORTED_ENVLOP;
          String insertSQL = 
              "insert into ytapl_fillstate(UNITID,TASKID,TASKTIMEID,FILLDATE,FLAG) values ('";
          insertSQL = insertSQL + unitID + "','";
          insertSQL = insertSQL + taskID + "','";
          insertSQL = insertSQL + taskTimeID + "','";
          insertSQL = insertSQL + Convertor.date2String(new java.util.Date()) + "','";
          insertSQL = insertSQL + flag + "')";
          sqlLisr.add(insertSQL);
          log.debug("封包insertSQL " + insertSQL);
        }
      }
      HibernateUtil.executeBatch(sqlLisr);
    }
    catch (Exception ex) {
      throw new HibernateException("封存数据失败", ex);
    }
  }

  /**
   *  将单位数据封包
   * @param stateType
   * @param taskID
   * @param unitID
   * @param taskTimeID
   * @param session
   * @throws HibernateException
   */
  private void envelopUnitSubmitData(int stateType, int taskID, String unitID,
                                     int taskTimeID, Session session) throws
      HibernateException {

  }

  /**
   * 启封数据
   * @param taskID
   * @param taskTimeID
   * @param unitIDList
   * @param flag
   * @throws HibernateException
   */
  public void unenvelopSubmitData(String taskID, int taskTimeID,
                                  List unitIDList, int flag, Session session) throws
      HibernateException {
    ArrayList sqlList = new ArrayList();
    try {
      Map unEnvUnitIDs = (Map) getUnenvelopUnitIDs(taskID, taskTimeID,
          unitIDList, flag);
      Set keyset = unEnvUnitIDs.keySet();
      Iterator keyIt = keyset.iterator();
      while (keyIt.hasNext()) {
        String key = (String) keyIt.next();
        int FLAG = ( (Integer) unEnvUnitIDs.get(key)).intValue();
        if (FLAG == DataStatus.UNREPORTED_ENVLOP)
          FLAG = DataStatus.UNREPORTED_UNENVLOP;
        else if (FLAG == DataStatus.REPORTED_ENVLOP)
          FLAG = DataStatus.REPORTED_UNENVLOP;
        String updateSQl = "update ytapl_fillstate set FLAG='" +
            FLAG + "' where UNITID='" + key +
            "' and TASKID='" + taskID + "' and TASKTIMEID='" + taskTimeID + "'";
        sqlList.add(updateSQl);
      }
      HibernateUtil.executeBatch(sqlList);
    }
    catch (Exception ex) {
      throw new HibernateException("启封数据失败", ex);
    }
  }

  /**
   * 得到密封的单位列表
   * @param unitIDList
   * @param flag
   * @return
   * @throws HibernateException
   */
  private HashMap getEnvelopUnitIDs(String taskID, List unitIDList) throws
      HibernateException {
    HashMap totalMap = new HashMap();
    try {
      ModelManager modelManager = ( (ModelManagerFactory)
                                   Factory.getInstance(
          ModelManagerFactory.class.getName())).createModelManager(taskID);
      UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

      for (int i = 0; i < unitIDList.size(); i++) {
        String unitID = (String) unitIDList.get(i);
        UnitTreeNode unitTreeNode = unitTreeManager.getUnitTree(unitID);
        UnitTreeNodeUtil.getAllChildren(totalMap, unitTreeNode);
      }

    }
    catch (Exception ex) {
      throw new HibernateException("封存数据失败", ex);
    }

    return totalMap;
  }

  /**
   * 得到启封的单位列表,
   * 有三种类型
   *   1。启封自个
   *   2。启封自个和自个节点
   *   3。启分自个和所有子节点
   * 注意：启封节点的是时候，他的祖先节点自动启封
   * @param unitIDList
   * @param flag
   * @return
   * @throws HibernateException
   */
  private Map getUnenvelopUnitIDs(String taskID, int taskTimeID,
                                  List unitIDList, int flag) throws
      HibernateException {
    Map map = new HashMap();
    try {

      ModelManager modelManager = ( (ModelManagerFactory)
                                   Factory.getInstance(
          ModelManagerFactory.class.getName())).createModelManager(taskID);
      AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
          getInstance(AnalyseManagerFactory.class.getName());
      AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();

      UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

      if (flag == SELF_NODE) {
        for (int i = 0; i < unitIDList.size(); i++) {
          Collection col = analyseMng.getFillState(taskID,
              new Integer(taskTimeID));
          Iterator itfill = col.iterator();
          String unitID = (String) unitIDList.get(i);
          UnitTreeNode unitTreeNode = unitTreeManager.getUnitTree(unitID);
          boolean flagtmp = true;
          while (itfill.hasNext()) {
            FillStateForm fillstate = (FillStateForm) itfill.next();
            if ( (unitTreeNode.getUnitCode() + unitTreeNode.getReportType()).
                equals(fillstate.getUnit().id())) {
              map.put(fillstate.getUnit().id(), new Integer(fillstate.getFlag()));
              flagtmp = !flagtmp;
              break;
            }
          }
          if (flagtmp)
            map.put(unitTreeNode.getUnitCode() + unitTreeNode.getReportType(),
                    new Integer(DataStatus.UNREPORTED_UNENVLOP));
        }
        return map;
      }

      HashMap mapp = new HashMap();

      for (int i = 0; i < unitIDList.size(); i++) {
        String unitID = (String) unitIDList.get(i);
        UnitTreeNode unitTreeNode = unitTreeManager.getUnitTree(unitID);
        if (flag == SELF_AND_SON) {
          UnitTreeNodeUtil.getAllSon(mapp, unitTreeNode);
        }
        else if (flag == this.SELF_AND_SPRING) {
          UnitTreeNodeUtil.getAllChildren(mapp, unitTreeNode);
        }
        UnitTreeNodeUtil.getAllAncestor(mapp, unitTreeNode);
      }

      Iterator itr = mapp.keySet().iterator();
      while (itr.hasNext()) {
        UnitTreeNode unitTreeNode = unitTreeManager.getUnitTree( (String) itr.
            next());
        Collection col = analyseMng.getFillState(taskID, new Integer(taskTimeID));
        Iterator itfill = col.iterator();
        boolean flagtmp = true;
        while (itfill.hasNext()) {
          FillStateForm fillstate = (FillStateForm) itfill.next();
          if ( (unitTreeNode.getUnitCode() + unitTreeNode.getReportType()).
              equals(fillstate.getUnit().id())) {
            map.put(fillstate.getUnit().id(), new Integer(fillstate.getFlag()));
            flagtmp = !flagtmp;
            break;
          }
          else
            map.put(unitTreeNode.getUnitCode() + unitTreeNode.getReportType(),
                    new Integer(DataStatus.UNREPORTED_UNENVLOP));
        }
      }

    }
    catch (Exception ex) {
      throw new HibernateException("启封数据失败", ex);
    }

    return map;
  }

  /**
   * 是否能够编辑数据
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @param session
   * @return
   * @throws HibernateException
   */
  public boolean canEditorSubmitData(String taskID, int taskTimeID,
                                     String unitID, Session session) throws
      HibernateException {

    PreparedStatement pstmt = null;

    try {

      String sql = "select count(*) as cnt from ytapl_fillstate where TASKID='" +
          taskID +
          "' and TASKTIMEID='" + taskTimeID + "' and (FLAG='" +
          DataStatus.UNREPORTED_ENVLOP + "' or FLAG='" +
          DataStatus.REPORTED_ENVLOP + "') and unitID like '" + unitID + "%'";

      log.debug("canUnenvelopSubmitData sql " + sql);

      Connection con = session.connection();
      pstmt = con.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        if (rs.getInt("cnt") > 0)
          return false;
        else
          return true;
      }

    }
    catch (SQLException e) {
      throw new HibernateException(e);
    }
    catch (Exception ex) {
      throw new HibernateException("判断是否能够启封数据失败", ex);
    }

    finally {
      SQLUtil.close(pstmt);
    }

    return false;

  }

  /**
   * 是否能够启封数据，最上级单位可以启封
   * 如果父亲节点没有启封数据，则子节点不能启封数据
   * @param taskID
   * @param taskTimeID
   * @param unitID
   * @return
   * @throws HibernateException
   */
  public boolean canUnenvelopSubmitData(String taskID, int taskTimeID,
                                        String unitID, Session session) throws
      HibernateException {
    PreparedStatement pstmt = null;

    try {
      ModelManager modelManager = ( (ModelManagerFactory)
                                   Factory.getInstance(
          ModelManagerFactory.class.getName())).createModelManager(taskID);
      UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

      UnitTreeNode unitTreeNode = unitTreeManager.getUnitTree(unitID);
      //最上级单位可以启封数据
      if (unitTreeNode.getParent() == null) {
        return true;
      }

      HashMap map = new HashMap();
      UnitTreeNodeUtil.getAllAncestor(map, unitTreeNode);
      String unitIDs = "";
      Iterator itr = map.keySet().iterator();
      while (itr.hasNext()) {
        unitIDs = unitIDs + (String) itr.next() + ",";
      }
      if (unitIDs.length() > 0) {
        unitIDs = " and  UNITID in(" +
            unitIDs.substring(0, unitIDs.length() - 1) + ")";
      }

      String sql = "select count(*) as cnt from ytapl_fillstate where TASKID='" +
          taskID +
          "' and TASKTIMEID='" + taskTimeID + "' and (FLAG='" +
          DataStatus.REPORTED_ENVLOP + "' or FLAG='" +
          DataStatus.UNREPORTED_ENVLOP + "')";
      sql = sql + unitIDs;

      log.debug("canUnenvelopSubmitData sql " + sql);

      Connection con = session.connection();
      pstmt = con.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        if (rs.getInt("cnt") > 0)
          return false;
        else
          return true;
      }

    }
    catch (SQLException e) {
      throw new HibernateException(e);
    }
    catch (Exception ex) {
      throw new HibernateException("判断是否能够启封数据失败", ex);
    }

    finally {
      SQLUtil.close(pstmt);
    }

    return false;
  }

  /**
   * 得到所有上报数据状态
   * @param taskID
   * @param taskTimeID
   * @param session
   * @return
   * @throws HibernateException
   */
  public HashMap getAllSubmitDataState(String taskID, int taskTimeID,
                                       Session session) throws
      HibernateException {
    HashMap unitStatMap = new HashMap();

    PreparedStatement pstmt = null;
    try {
      String sql =
          "select * from Ytapl_FillState t where t.taskID=? and t.taskTimeID=?";

      Connection con = session.connection();
      pstmt = con.prepareStatement(sql);

      pstmt.setString(1, taskID);
      pstmt.setInt(2, taskTimeID);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        FillStateModel fillStateModel = new FillStateModel();
        String unitID = rs.getString("UNITID");
        fillStateModel.setUntiID(unitID);
        fillStateModel.setTaskID(rs.getString("TASKID"));
        fillStateModel.setTaskTimeID(rs.getInt("TASKTIMEID"));
        fillStateModel.setFillData(rs.getDate("FILLDATE"));
        fillStateModel.setFlag(rs.getInt("FLAG"));
        unitStatMap.put(unitID, fillStateModel);
      }
    }
    catch (SQLException e) {
      throw new HibernateException(e);
    }
    finally {
      SQLUtil.close(pstmt);
    }

    return unitStatMap;
  }

  /**
   * 查看单位是否上报任务taskID,上报时间taskTimeID的数据
   *
   * @param taskID   任务ID
   * @param unitIDList  单位代码链表，元素是java.lang.String型
   * @param taskTimeID  对于的上报时间id
   * @param session  Hibernate Session
   * @throws HibernateException 发生Hibernate异常
   * @return            对应于单位代码的是否上报数据的链表，元素类型是java.lang.Boolean型;
   *                    flag标识位的链表，元素是java.lang.Integer型；
   *                    fillDate的链表，元素是java.util.Date型。
   *                    在填报情况表中，没有记录，那么对应的hasReport就为false；
   *                    对应的flag和fillDate为null
   */
  public List[] hasReportData(String taskID, List unitIDList, int taskTimeID,
                              Session session) throws HibernateException {
    List[] returnList = new List[3];
    for (int i = 0; i < returnList.length; i++) {
      int size = unitIDList.size();
      returnList[i] = new ArrayList();
      setNull(returnList[i], size);
    }

    Map unitMap = new HashMap();
    for (int i = 0, size = unitIDList.size(); i < size; i++) {
      unitMap.put(unitIDList.get(i), new Integer(i));
    }

    // unitIDList为空或者size=0，那么返回
    if (unitIDList == null || unitIDList.size() == 0) {
      return returnList;
    }

    String tempUnitTable = null;

    PreparedStatement pstmt = null;
    try {
      tempUnitTable = TempTableUtil.createTempUnitIDTable(unitIDList.iterator(),
          session);
      StringBuffer buff = new StringBuffer(QUERY_FILL_STATE_SQL);
      buff.append(" ( select unitID from ").append(tempUnitTable).append(" )");
      String sql = buff.toString();

      if (log.isDebugEnabled()) {
        log.debug("创建临时表，且已插入数据");
        log.debug("查询审核状态的SQL\n\t" + sql);
      }

      Connection con = session.connection();
      pstmt = con.prepareStatement(sql);

      pstmt.setString(1, taskID);
      pstmt.setInt(2, taskTimeID);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String unitID = rs.getString("unitID");
        int flag = rs.getInt("flag");
        java.util.Date date = null;
        if (rs.getTimestamp("filldate") != null)
          date = new java.util.Date(rs.getTimestamp("filldate").getTime());
        Integer iValue = ( (Integer) unitMap.get(unitID));
        if (iValue == null) {
          log.error("Null value");
        }
        else {
          int pos = iValue.intValue();

          returnList[0].set(pos, Boolean.TRUE);
          returnList[1].set(pos, new Integer(flag));
          returnList[2].set(pos, date);
        }
      }
    }
    catch (SQLException e) {
      throw new HibernateException(e);
    }
    finally {
      if (tempUnitTable != null) {
        TempTableUtil.dropTempTable(tempUnitTable, session);

        if (log.isDebugEnabled()) {
          log.debug("Drop temp table " + tempUnitTable);
        }
      }

      SQLUtil.close(pstmt);
    }

    // 设置那些为null的元素为Boolean(false)；
    for (int i = 0, size = returnList[0].size(); i < size; i++) {
      if (returnList[0].get(i) == null) {
        returnList[0].set(i, Boolean.FALSE);
      }
    }

    return returnList;
  }

  /**
   *审核数据
   * @param taskID
   * @param unitIDList
   * @param taskTimeID
   * @param session
   * @return
   * @throws HibernateException
   */
  public List hasAuditData(String taskID, int taskTimeID, Session session) throws
      HibernateException {
    List list = new ArrayList();
    Connection conn = session.connection();
    StringBuffer buf = new StringBuffer(SELECT_AUDIT_STATE);
    buf.append(" where taskID=? and taskTimeID=?");
    String sql = buf.toString();
    try {
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, taskID);
      pstmt.setInt(2, taskTimeID);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        AuditStateForm auditForm = new AuditStateForm();
        auditForm.setUnitID(rs.getString(1));
        auditForm.setTaskID(rs.getString(2));
        auditForm.setTaskTimeID(new Integer(rs.getInt(3)));
        auditForm.setAuditDate(new java.util.Date(rs.getTimestamp(4).getTime()));
        auditForm.setFlag(new Integer(rs.getInt(5)));
        auditForm.setAuditor(rs.getString(6));
        list.add(auditForm);
      }
    }
    catch (SQLException ex) {
      throw new HibernateException(ex);
    }

    return list;
  }

  public static String combFillAuditToShow(String unitID, List audits) throws
      FactoryException {
    String auditDate = "";
    for (int i = 0; i < audits.size(); i++) {
      AuditStateForm auditForm = (AuditStateForm) audits.get(i);
      String auditUnitID = auditForm.getUnitID();
      if (auditUnitID.equals(unitID)) {
        auditDate = (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).
            format(auditForm.getAuditDate());
        break;
      }
    }
    return auditDate;
  }

  public static AuditStateForm findAuditInfo(String unitID, String taskID,
                                             int taskTimeID) throws
      HibernateException {
    final String SQL_SELECT_AUDITINFO =
        "select * from Ytapl_AuditInfo where unitID=? and TASKID=? and TASKTIMEID=?;";
    Session session = null;
    Connection conn = null;
    PreparedStatement pstmt = null;
    AuditStateForm auditInfo = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      pstmt = conn.prepareStatement(SQL_SELECT_AUDITINFO);
      pstmt.setString(1, unitID);
      pstmt.setString(2, taskID);
      pstmt.setInt(3, taskTimeID);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        auditInfo = new AuditStateForm();
        auditInfo.setUnitID(rs.getString(1));
        auditInfo.setTaskID(rs.getString(2));
        auditInfo.setTaskTimeID(new Integer(rs.getInt(3)));
        auditInfo.setAuditDate(new java.util.Date(rs.getTimestamp(4).getTime()));
        auditInfo.setFlag(new Integer(rs.getInt(5)));
        auditInfo.setAuditor(rs.getString(6));
      }
      rs.close();
      pstmt.close();
    }
    catch (SQLException se) {
      throw new HibernateException(se);
    }
    finally {
      session.close();
    }
    return auditInfo;
  }

  public static void updateAuditInfo(AuditStateForm auditInfo) throws
      HibernateException {
    final String SQL_UPDATE_AUDITINFO =
        "update Ytapl_auditInfo set auditDate=?,flag=?,auditor=? where unitID=? and taskID=? and taskTimeID=?";
    Session session = null;
    Connection conn = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_AUDITINFO);
      pstmt.setTimestamp(1, new Timestamp(auditInfo.getAuditDate().getTime()));
      pstmt.setInt(2, auditInfo.getFlag().intValue());
      pstmt.setString(3, auditInfo.getAuditor());
      pstmt.setString(4, auditInfo.getUnitID());
      pstmt.setString(5, auditInfo.getTaskID());
      pstmt.setInt(6, auditInfo.getTaskTimeID().intValue());
      pstmt.executeUpdate();
    }
    catch (SQLException sq) {
      throw new HibernateException(sq);
    }
    finally {
      if (session != null)
        session.close();
    }

  }

  public static void addAuditInfo(AuditStateForm auditInfo) throws
      HibernateException {
    //特殊处理，如果用户不在1-99将不做处理
    String auditor = auditInfo.getAuditor();
    int id = -1;
    try {
      id = Integer.parseInt(auditor);
    }
    catch (Exception ex) {
    }

    if (id < 0 || id > 100)
      return;
    final String SQL_INSERT_AUDITINOF =
        "insert into Ytapl_AuditInfo VALUES(?,?,?,?,?,?);";
    Session session = null;
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      if (findAuditInfo(auditInfo.getUnitID(), auditInfo.getTaskID(),
                        auditInfo.getTaskTimeID().intValue()) != null) {
        updateAuditInfo(auditInfo);
      }
      else {
        session = HibernateUtil.getSessionFactory().openSession();
        conn = session.connection();

        pstmt = conn.prepareStatement(SQL_INSERT_AUDITINOF);
        pstmt.setString(1, auditInfo.getUnitID());
        pstmt.setString(2, auditInfo.getTaskID());
        pstmt.setInt(3, auditInfo.getTaskTimeID().intValue());
        pstmt.setTimestamp(4, new Timestamp(auditInfo.getAuditDate().getTime()));
        pstmt.setInt(5, auditInfo.getFlag().intValue());
        pstmt.setString(6, auditInfo.getAuditor());
        pstmt.executeUpdate();
        pstmt.close();
      }
    }
    catch (SQLException sq) {
      throw new HibernateException(sq);
    }
    finally {
      SQLUtil.close(null, pstmt, conn);
      HibernateUtil.close(session);
    }
  }

  /**
   * 根据taskID 找到能上报数据的单位节点集合
   * @param session Hibernate Session
   * @param taskID 任务id
   * @return 单位信息集合。元素是UnitTreeNode型。如果失败返回空的集合。
   * @throws HibernateException 发生Hibernate异常
   */
  public Collection getAllUnitNodes(String taskID, Session session) throws
      HibernateException {
    PreparedStatement ps = null;
    try {
      // 从权限表中查找出所有的有权限的unitID
      List unitIDList = new LinkedList();

      Connection con = session.connection();

      ps = con.prepareStatement(QUERY_ALL_UNIT_SQL);
      ps.setString(1, taskID);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String unitID = rs.getString(1);
        unitIDList.add(unitID);
      }

      // 权限继承，所以得到的节点，它们的子节点也是有权限的
      ModelManagerFactory modelMngFcty = (ModelManagerFactory) Factory.
          getInstance(ModelManagerFactory.class.getName());
      ModelManager modelMng = modelMngFcty.createModelManager(taskID);

      Map unitIDNodeMap = new HashMap();
      for (int i = 0, size = unitIDList.size(); i < size; i++) {
        String unitID = (String) unitIDList.get(i);

        UnitTreeNode node = modelMng.getUnitTreeManager().getUnitTree(unitID);
        if (node != null)
          fetchMap(node, unitIDNodeMap);
      }

      return unitIDNodeMap.values();
    }
    catch (Exception ex) {
      log.error("查找所有单位出错", ex);
      throw new HibernateException(ex);
    }
    finally {
      SQLUtil.close(ps);
    }
  }

  /**
   * 把UnitTreeNode自身和所有深度的子节点，以key/value=node.id()/node映射到map
   * @param node 单位节点树，该节点的所有子节点都已经被加载
   * @param map  映射到的map
   */
  private void fetchMap(UnitTreeNode node, Map map) {
    map.put(node.id(), node);

    //第归
    for (Iterator iter = node.getChildren(); iter.hasNext(); ) {
      UnitTreeNode child = (UnitTreeNode) iter.next();
      fetchMap(child, map);
    }
  }

  private void setNull(List list, int length) {
    for (int i = 0; i < length; i++) {
      list.add(null);
    }
  }

  /**
   *
   * @param stateType
   * @param taskID
   * @param unitIDs
   * @param taskTimeID
   * @param session
   * @return
   * @throws HibernateException
   */
  public List alertState(int stateType, int taskID, List unitIDs,
                         int taskTimeID, Session session) throws
      HibernateException {

    List returnList = new LinkedList();
    PreparedStatement ps = null;

    try {
      Connection con = session.connection();

      ps = con.prepareStatement(ALERT_STATE);

      for (int i = 0, size = unitIDs.size(); i < size; i++) {
        ps.setInt(1, stateType);
        ps.setInt(2, taskID);
        ps.setObject(3, unitIDs.get(i));
        ps.setInt(4, taskTimeID);

        ps.addBatch();
      }

      Transaction tran = session.beginTransaction();
      int[] batchResult = ps.executeBatch();
      tran.commit();

      for (int i = 0; i < batchResult.length; i++) {
        returnList.add(Boolean.TRUE);

      }
    }
    catch (SQLException sqle) {
      log.error("审核通过出错", sqle);
      throw new HibernateException(sqle);
    }
    catch (HibernateException he) {
      log.error("审核通过出错", he);
      throw he;
    }
    finally {
      SQLUtil.close(ps);
    }
    return returnList;
  }

  /**
   * 修改单位上报状态属性
   * @param stateType   修改后的状态值 @cn.com.youtong.apollo.data.UnitStateType
   * @param taskIDs     任务列表
   * @param unitIDs     单位列表
   * @param taskTimeIDs 任务时间列表
   * @param session     会话
   * @return            成功与否（java.lang.Boolean）
   * @throws HibernateException
   */
  public List alertState(int stateType, List taskIDs, List unitIDs,
                         List taskTimeIDs, Session session) throws
      HibernateException {
    List returnList = new LinkedList();
    PreparedStatement ps = null;

    try {
      Connection con = session.connection();

      ps = con.prepareStatement(ALERT_STATE);

      for (int i = 0, size = taskIDs.size(); i < size; i++) {
        ps.setInt(1, stateType);
        ps.setObject(2, taskIDs.get(i));
        ps.setObject(3, unitIDs.get(i));
        ps.setObject(4, taskTimeIDs.get(i));

        ps.addBatch();
      }

      Transaction tran = session.beginTransaction();
      int[] batchResult = ps.executeBatch();
      tran.commit();

      for (int i = 0; i < batchResult.length; i++) {
        returnList.add(Boolean.TRUE);
        // 在oracle数据库使用返回的值是-2，但是值也被修改，还没不能解释这个问题
        /**if (batchResult[i] == 1)
         returnList.add(new Boolean(true));
          else
         returnList.add(new Boolean(false));*/
      }
    }
    catch (SQLException sqle) {
      log.error("审核通过出错", sqle);
      throw new HibernateException(sqle);
    }
    catch (HibernateException he) {
      log.error("审核通过出错", he);
      throw he;
    }
    finally {
      SQLUtil.close(ps);
    }
    return returnList;
  }

  /**
   * 查找单位状态
   * @param taskID             任务标识
   * @param taskTimeID         任务时间；如果为null，表示不参与查询条件
   * @param flag               上报状态；如果为空，表示该字段不参与查询条件
   * @param unitID             单位代码，支持模糊查询；如果为null，表示不参与查询条件
   * @param session            Hibernate 会话
   * @return                   List[0]，单位代码List，元素为java.lang.String；
   *                           List[1]，任务时间List，元素为java.lang.Integer；
   *                           List[2]，填报时间List，元素为java.util.Date；
   *                           List[3]，上报状态List，元素为java.lang.Integer
   * @throws HibernateException
   */
  List[] selectUnitState(String taskID, Integer taskTimeID, Integer flag,
                         String unitID, Session session) throws
      HibernateException {
    PreparedStatement stmt = null;
    try {
      Connection con = session.connection();
      StringBuffer buf = new StringBuffer(SELECT_STATE);

      buf.append(" where taskID = '").append(taskID).append("' ");

      if (taskTimeID != null) {
        buf.append(" and taskTimeID = '").append(taskTimeID.intValue()).append(
            "' ");
      }

      if (unitID != null) {
        buf.append(" and unitID like '%").append(unitID).append("%' ");
      }

      if (flag != null) {
        buf.append(" and flag = '").append(flag).append("' ");
      }

      String sql = buf.toString();
      if (log.isDebugEnabled()) {
        log.debug("查询单位状态SQL ==>\n\t\t" + sql);
      }

      stmt = con.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      List[] listArray = new List[4];
      List unitIDList = new LinkedList();
      List taskTimeIDList = new LinkedList();
      List fillDateList = new LinkedList();
      List flagList = new LinkedList();
      listArray[0] = unitIDList;
      listArray[1] = taskTimeIDList;
      listArray[2] = fillDateList;
      listArray[3] = flagList;

      while (rs.next()) {
        String tUnitID = rs.getString("unitID");
        int tTaskTimeID = rs.getInt("taskTimeID");
        java.sql.Timestamp tDate = rs.getTimestamp("fillDate");
        int tFlag = rs.getInt("flag");

        unitIDList.add(tUnitID);
        taskTimeIDList.add(new Integer(tTaskTimeID));
        fillDateList.add(new java.util.Date(tDate.getTime()));
        flagList.add(new Integer(tFlag));
      }

      return listArray;
    }
    catch (SQLException sqle) {
      log.error("查询填报状态出错", sqle);
      throw new HibernateException("查询填报状态出错", sqle);
    }
    catch (HibernateException he) {
      log.error("查询填报状态出错", he);
      throw he;
    }
    finally {
      SQLUtil.close(stmt);
    }
  }

  /**
   * 查看单位是否上报任务taskID,上报时间taskTimeID的数据
   *
   * @param taskID   任务ID
   * @param unitIDList  单位代码链表，元素是java.lang.String型
   * @param session  Hibernate Session
   * @throws HibernateException 发生Hibernate异常
   * @return            链表数组，依次是：
   *                    单位代码链表，元素是java.lang.String型；因为可能一个单位存在多条记录；
   *                    对应于单位代码的上报数据的任务时间ID，元素类型是java.lang.Integer型;
   *                    flag标识位的链表，元素是java.lang.Integer型；
   *                    fillData的链表，元素是java.util.Date型
   */ List[] readyForAudit(String taskID, List unitIDList, Session session) throws
      HibernateException {
    List[] returnList = new List[4];
    for (int i = 0; i < returnList.length; i++) {
      returnList[i] = new LinkedList();
    }

    // unitIDList为空或者size=0，那么返回
    if (unitIDList == null || unitIDList.size() == 0) {
      return returnList;
    }

    String tempUnitTable = null;

    PreparedStatement pstmt = null;
    try {
      tempUnitTable = TempTableUtil.createTempUnitIDTable(unitIDList.iterator(),
          session);
      StringBuffer buff = new StringBuffer(QUERY_READY_AUDIT_SQL);
      buff.append(" ( select unitID from ").append(tempUnitTable).append(" )");
      buff.append(" order by unitID");

      String sql = buff.toString();

      if (log.isDebugEnabled()) {
        log.debug("创建临时表" + tempUnitTable + "，且已插入数据");
        log.debug("查询审核状态的SQL\n\t" + sql + "\n");
      }

      Connection con = session.connection();
      pstmt = con.prepareStatement(sql);

      pstmt.setString(1, taskID);
      pstmt.setInt(2, DataStatus.REPORTED_ENVLOP);
      pstmt.setInt(3, DataStatus.REPORTED_UNENVLOP);

      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        String unitID = rs.getString("unitID");
        int taskTimeID = rs.getInt("taskTimeID");
        int flag = rs.getInt("flag");
        java.util.Date date = new java.util.Date(rs.getTimestamp("filldate").
                                                 getTime());

        returnList[0].add(unitID);
        returnList[1].add(new Integer(taskTimeID));
        returnList[2].add(new Integer(flag));
        returnList[3].add(date);
      }
    }
    catch (HibernateException he) {
      throw he;
    }
    catch (SQLException e) {
      throw new HibernateException(e);
    }
    finally {
      if (tempUnitTable != null) {
        TempTableUtil.dropTempTable(tempUnitTable, session);

        if (log.isDebugEnabled()) {
          log.debug("Drop temp table " + tempUnitTable);
        }
      }

      SQLUtil.close(pstmt);
    }

    return returnList;
  }
   
   public static void createDraft(String unitID,String taskID,int taskTimeID,String content){
	   Session session = null;
	   PreparedStatement pstmt = null;
	   try {
		session = HibernateUtil.openSession();
		pstmt=session.connection().prepareStatement(INSERT_DRAFT);
		pstmt.setString(1, unitID);
		pstmt.setString(2, taskID);
		pstmt.setInt(3, taskTimeID);
		pstmt.setString(4, content);
		pstmt.executeUpdate();
		
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		HibernateUtil.close(pstmt);
		HibernateUtil.close(session);
	}  
   }
   
   public static String queryDraft(String unitID,String taskID,int taskTimeID){
	   String content=null;
	   Session session = null;
	   PreparedStatement pstmt = null;
	   ResultSet rs = null;
	   try {
		session=HibernateUtil.openSession();
		pstmt=session.connection().prepareStatement(SELECT_DRAFT);
		pstmt.setString(1, unitID);
		pstmt.setString(2, taskID);
		pstmt.setInt(3, taskTimeID);
		rs = pstmt.executeQuery();
		if(rs.next()){
			content=rs.getString("content");
		}
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		HibernateUtil.close(rs, pstmt);
		HibernateUtil.close(session);
	}
	return content;
   }
   
   public static void delDraft(String unitID,String taskID,int taskTimeID){
	   Session session = null;
	   PreparedStatement pstmt = null;
	   try {
		session = HibernateUtil.openSession();
		pstmt = session.connection().prepareStatement(DELETE_DRAFT);
		pstmt.setString(1, unitID);
		pstmt.setString(2, taskID);
		pstmt.setInt(3, taskTimeID);
		pstmt.executeUpdate();
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		HibernateUtil.close(pstmt);
		HibernateUtil.close(session);
	}
   }

}