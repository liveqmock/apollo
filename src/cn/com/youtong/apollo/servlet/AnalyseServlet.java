package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.analyse.*;
import cn.com.youtong.apollo.analyse.form.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.servlet.unittree.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.tools.charts.*;
import cn.com.youtong.tools.charts.form.*;
import cn.com.youtong.tools.excel.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.analyse.db.*;
import cn.com.youtong.apollo.data.db.*;
import cn.com.youtong.apollo.common.sql.SQLUtil;

import net.sf.hibernate.*;

import java.net.URLDecoder;

/**
 * 统计分析Servlet
 */
public class AnalyseServlet    extends RootServlet {
  /** Log */
  private Log log = LogFactory.getLog(AnalyseServlet.class);

  /**
   * 页面常数 -- 指标查询条件选择页面
   */
  public static final String SCALAR_CONDITION_PAGE =
      "/jsp/analyseManager/scalarCondition.jsp";

  /**
   * 页面常数 -- 报表指标查询结果页面
   */
  public static final String SCALAR_RESULT_PAGE =
      "/jsp/analyseManager/scalarResult.jsp";

  /**
   * 页面常数 -- 指标查询模板管理页面
   */
  public static final String MANAGE_TEMPLATE_PAGE =
      "/jsp/analyseManager/manageTemplate.jsp";

  /**
   * 页面常数 -- 参数管理页面
   */
  public static final String PARAM_PAGE = "/jsp/analyseManager/param.jsp";

  /**
   * 页面常数 -- 报表指标查询结果图表显示页面
   */
  public static final String SCALAR_CHART_PAGE =
      "/jsp/analyseManager/scalarChart.jsp";

  /**
   * 请求类型常量 -- 显示指标查询模板管理页面
   */
  public static final String SHOW_MANAGE_SCALAR_QUERY_TEMPLATE_PAGE =
      "showManageScalarQueryTemplatePage";

  /**
   * 请求类型常量 -- 显示参数管理页面
   */
  public static final String SHOW_MANAGE_PARAM_PAGE = "showManageParamPage";

  /**
   * 请求类型常量 -- 导入指标查询模板
   */
  public static final String IMPORT_SCALAR_QUERY_TEMPLATE =
      "importScalarQueryTemplate";

  /**
   * 请求类型常量 -- 导入模板参数
   */
  public static final String IMPORT_PARAM = "importParam";

  /**
   * 请求类型常量 -- 导出指标查询模板
   */
  public static final String EXPORT_SCALAR_QUERY_TEMPLATE =
      "exportScalarQueryTemplate";

  /**
   * 请求类型常量 -- 显示指标查询模板创建页面
   */
  public static final String SHOW_CREATE_SCALAR_QUERY_TEMPLATE_PAGE =
      "showCreateScalarQueryTemplatePage";

  /**
   * 请求类型常量 -- 显示指标查询模板更新页面
   */
  public static final String SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE =
      "showUpdateScalarQueryTemplatePage";

  /**
   * 请求类型常量 -- 删除指标查询模板
   */
  public static final String DELETE_SCALAR_QUERY_TEMPLATE =
      "deleteScalarQueryTemplate";

  /**
   * 请求类型常量 -- 创建指标查询模板
   */
  public static final String CREATE_SCALAR_QUERY_TEMPLATE =
      "createScalarQueryTemplate";

  /**
   * 请求类型常量 -- 执行指标查询模板
   */
  public static final String EXECUTE_SCALAR_QUERY_TEMPLATE =
      "executeScalarQueryTemplate";

  /**
   * 请求类型常量 -- 更新指标查询模板
   */
  public static final String UPDATE_SCALAR_QUERY_TEMPLATE =
      "updateScalarQueryTemplate";

  /**
   * 请求类型常量 -- 显示选择指标页面
   */
  public static final String SHOW_SELECT_SCALAR_PAGE = "showSelectScalarPage";

  /**
   * 请求类型常量 -- 显示指标查询结果图表显示页面
   */
  public static final String SHOW_SCALAR_CHART_PAGE = "showScalarChartPage";

  /**
   * 请求类型常量 -- 显示指标查询结果Excel显示页面
   */
  public static final String SHOW_SCALAR_EXCEL = "showScalarExcel";

  /**
   * 请求类型常量 -- 显示查询报表指标条件选择页面
   */
  public static final String SHOW_SCALAR_CONDITION_PAGE =
      "showScalarConditionPage";
  /**
   * 请求类型常量 -- 按条件查询填报情况
   */
  public static final String QUERY_FILL_STATE = "queryFillState";

  /**
   * 查询单位填报情况结果页面
   */
  public static final String FILL_STATE_PAGE =
      "/jsp/analyseManager/fillstate.jsp";

  /**
   * 请求类型常量 -- 显示没有填报的单位
   */
  public static final String SHOW_NOT_FILLSTATE_RESULT =
      "showNotFillStateResult";

  /**
   * 没有填报的页面
   */
  public static final String NOT_FILLSTATE_RESULT_PAGE =
      "/jsp/analyseManager/notFillResult.jsp";
  /**
   * 请求类型常量 -- 显示已经填报的单位
   */
  public static final String SHOW_ALREADY_FILLSTATE_RESULT =
      "showAlreadyFillStateResult";

  public static final String SHOW_ALL_FILLSTATE_RESULT =
      "showAllFillStateResult";

  /**
   * 已经填报的单位页面
   */
  public static final String ALREADY_FILLSTATE_RESULT_PAGE =
      "/jsp/analyseManager/alreadyfillstateResult.jsp";

  /**
   * 请求类型常量 -- 查询报表指标
   */
  public static final String QUERY_SCALAR = "queryScalar";

  public static final String FILL_STATE_YES = "<span class='style1'>是</span>";
  public static final String FILL_STATE_NO = "<span class='style2'>否</span>";

  public static final String FILL_ENVELOP =
      "<span class='style1'>(封存)</span>";
  public static final String FILL_UNENVLOP =
      "<span class='style2'>(启封)</span>";

  /**
   * 请求类型常量 -- 显示checkbox用户树
   */
  public static final String SHOW_CHART_IMAGE = "showChartImage";

  public static final String SELECT_TASKTIME_PAGE =
      "/jsp/analyseManager/selectTaskTime.jsp";
  public static final String TEMPLATE_QUERY_RESULT_PAGE =
      "/jsp/analyseManager/templateResult.jsp";
  public static final String SHOW_SCALAR_QUERY_TEMPLATE_PAGE =
      "showScalarQueryTemplate";
  public static final String SCALAR_QUERT_TEMPLATE_PAGE =
      "/jsp/analyseManager/showTemplates.jsp";

  /**
   * 显示已填报单位
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showAlreadyFillStateResult(HttpServletRequest req,
                                          HttpServletResponse res) throws
      Warning, IOException, ServletException {
    getAllFillState(req, res);
    go2UrlWithAttibute(req, res, ALREADY_FILLSTATE_RESULT_PAGE);
  }

  private void showAlreadyFillStateResult1(HttpServletRequest req,
                                           HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Collection col = new Vector();

    Collection collection = getFillState(req, res);

    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    req.getSession().setAttribute("seldata",req.getParameter("seldata"));
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator it = task.getTaskTimes();
        if (it.hasNext()) {
          time = (TaskTime) it.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }

    col = addFillState(collection, col, "already", getUnitForeset(req, res),
                       taskID, taskTimeID, req);

    req.getSession().setAttribute("fillstates", col);
    req.getSession().setAttribute("fillstateTitle", "已上报单位");
    res.sendRedirect(req.getContextPath() + "/regionBrowse.do");

  }

  /**
   * 显示所有单位填报情况
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */

  private void showFillStateResult(HttpServletRequest req,
                                   HttpServletResponse res) throws Warning,
      IOException, ServletException {
    Collection col = new Vector();
    req.getSession().setAttribute("codeOrName",req.getParameter("codeOrName"));
    req.getSession().setAttribute("seldata",req.getParameter("seldata"));
    Collection collection = getFillState(req, res);
    col = addAllFillState(collection.iterator(), col, getUnitForeset(req, res),
                          req, res);

    req.getSession().setAttribute("fillstates", col);
    req.getSession().setAttribute("fillstateTitle", "上报情况");

    res.sendRedirect(req.getContextPath() + "/regionBrowse.do");

  }

  /**
   *
   * 显示未填报单位
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showNotFillStateResult(HttpServletRequest req,
                                      HttpServletResponse res) throws Warning,
      IOException, ServletException {
    getAllFillState(req, res);
    go2UrlWithAttibute(req, res, NOT_FILLSTATE_RESULT_PAGE);
  }

  private void showNotFillStateResult1(HttpServletRequest req,
                                       HttpServletResponse res) throws Warning,
      IOException, ServletException {
    Collection col = new Vector();

    Collection collection = getFillState(req, res);
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator it = task.getTaskTimes();
        if (it.hasNext()) {
          time = (TaskTime) it.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }
    req.getSession().setAttribute("codeOrName",req.getParameter("codeOrName"));
    req.getSession().setAttribute("seldata",req.getParameter("seldata"));
    col = addFillState(collection, col, "not", getUnitForeset(req, res), taskID,
                       taskTimeID, req);
    req.getSession().setAttribute("fillstates", col);
    req.getSession().setAttribute("fillstateTitle", "未上报单位");
    res.sendRedirect(req.getContextPath() + "/regionBrowse.do");
  }

  private Collection getFillState(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator it = task.getTaskTimes();
        if (it.hasNext()) {
          time = (TaskTime) it.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }
    req.getSession().setAttribute("taskTimeID", taskTimeID);
    req.getSession().setAttribute("task", task);

    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();

    Collection col = analyseMng.getFillState(taskID, taskTimeID);

    //hexj
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);
    AddressInfoTree tree = new AddressInfoTree(taskID);

    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));

    Iterator it = modelManager.getUnitTreeManager().getUnitForest(unitACL);
    //清除不是该权限的单位
    col = clearOutRigth(col, it);
    //end hexj

    return col;
  }

  private Iterator getUnitForeset(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {

    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator it = task.getTaskTimes();
        if (it.hasNext()) {
          time = (TaskTime) it.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }
    req.getSession().setAttribute("taskTimeID", taskTimeID);
    req.getSession().setAttribute("task", task);

    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(taskID);
    AddressInfoTree tree = new AddressInfoTree(taskID);

    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(req));

    Iterator it = modelManager.getUnitTreeManager().getUnitForest(unitACL);
    return it;
  }

  private Collection clearOutRigth(Collection col, Iterator it) {
    Collection collection = new Vector();
    Iterator iterator = col.iterator();
    List list = new ArrayList();
    while (it.hasNext()) {
      UnitTreeNode treeNode = (UnitTreeNode) it.next();
      String code = (String) treeNode.id();
      list.add(code);
    }
    while (iterator.hasNext()) {
      FillStateForm stateForm = (FillStateForm) iterator.next();
      for (int i = 0; i < list.size(); i++) {
        String unitCode = (String) list.get(i);
        if (parentIs(stateForm.getUnit(), unitCode)) {
          collection.add(stateForm);
        }
      }
    }
    return collection;
  }

  private boolean parentIs(UnitTreeNode unitTreeNode, String id) {
    if (unitTreeNode.id().equals(id)) {
      return true;
    }
    else {
      if (unitTreeNode.getParent() != null) {
        return parentIs(unitTreeNode.getParent(), id);
      }
    }
    return false;
  }

  private Collection addFillState(Collection col, Collection collection,
                                  String strFlag, Iterator itRoot,
                                  String taskID, Integer taskTimeID,
                                  HttpServletRequest req) throws
      Warning, IOException, ServletException {
    //所有审核状态
    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();
    List auditStates = analyseMng.getAuditStates(taskID, taskTimeID.intValue());
    //

    List listRoot = new ArrayList();
    while (itRoot.hasNext()) {
      UnitTreeNode treeNode = (UnitTreeNode) itRoot.next();
      String code = treeNode.id();
      listRoot.add(code);
    }

    Iterator it = col.iterator();
    while (it.hasNext()) {
      boolean bol = false;
      FillStateForm stateForm = (FillStateForm) it.next();
      if(stateForm.getUnit().getDisplay()==1){
      Iterator iterator = collection.iterator();
      while (iterator.hasNext()) {
        List list = (List) iterator.next();
        if ( ( (String) list.get(1)).equals(stateForm.getUnit().id())) {
          if (stateForm.getFlag() == DataStatus.UNREPORTED_ENVLOP)
            list.set(3, FILL_STATE_NO + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP)
            list.set(3, FILL_STATE_YES + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP)
            list.set(3, FILL_STATE_YES + FILL_UNENVLOP);
          else
            list.set(3, FILL_STATE_NO + FILL_UNENVLOP);
          if (strFlag.equals("not")) {
            if (stateForm.getUnit().getParent() != null) {
              boolean tmpFlag = false;
              for (int j = 0; j < listRoot.size(); j++) {
                String unitCodeRoot = (String) listRoot.get(j);
                if (stateForm.getUnit().id().equals(unitCodeRoot)) {
                  tmpFlag = true;
                }
              }
              if (!tmpFlag) {
                list.set(4, stateForm.getUnit().getParent().id());
              }
            }
          }
          //
          bol = true;
          break;
        }
      }
      if (!bol) {
        if (strFlag.equals("already") &&
            (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP ||
             stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP) ||
            strFlag.equals("not") &&
            ! (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP ||
               stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP) &&
            !isParentFilled(col, stateForm.getUnit()) || strFlag.equals("query")) {
          List lst = new ArrayList();
          if (stateForm.getUnit().getChildren() != null) {
            lst.add("G");
          }
          else {
            if (stateForm.getUnit().getParent() != null) {
              lst.add("C");
            }
            else {
              lst.add("G");
            }
          }
          lst.add(stateForm.getUnit().id());
          lst.add(stateForm.getUnit().getUnitName());
          if (stateForm.getFlag() == DataStatus.UNREPORTED_ENVLOP)
            lst.add(3, FILL_STATE_NO + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP)
            lst.add(3, FILL_STATE_YES + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP)
            lst.add(3, FILL_STATE_YES + FILL_UNENVLOP);
          else
            lst.add(3, FILL_STATE_NO + FILL_UNENVLOP);

          String parentCode = null;

          if (stateForm.getUnit().getParent() != null) {
            boolean rootFlag = false;
            for (int j = 0; j < listRoot.size(); j++) {
              String unitCodeRoot = (String) listRoot.get(j);
              if (stateForm.getUnit().id().equals(unitCodeRoot)) {
                rootFlag = true;
              }
            }
            if (rootFlag) {
              lst.set(0, "G");
            }
            else {
              parentCode = stateForm.getUnit().getParent().id();
            }
          }

          lst.add(parentCode);
          lst.add(addIDString(stateForm.getUnit()));
          if (stateForm.getFillDate() != null) {
            lst.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    format(stateForm.getFillDate()));
          }
          else {
            lst.add("");
          }

          //审核标志
          if (stateForm.getFlag() == DataStatus.AUDITED) {
            lst.add(Boolean.TRUE);
          }
          else {
            lst.add(Boolean.FALSE);
          }
          if (hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT)) {
            String auditDate = DBAnalyseHelper.combFillAuditToShow(stateForm.
                getUnit().id(), auditStates);
            if ("".equals(auditDate)) {
              lst.add(FILL_STATE_NO);
            }
            else {
              lst.add(FILL_STATE_YES);
            }
            lst.add(auditDate);
          }
          else {
            lst.add(FILL_STATE_NO);
            lst.add("");
          }

          collection.add(lst);
          if (parentCode != null) {
            collection = addFillState(stateForm.getUnit().getParent(), col,
                                      collection, strFlag, listRoot, taskID,
                                      taskTimeID, auditStates, req);

          }
        }
      }
     }
    }
    return collection;
  }

  private Collection addFillState(UnitTreeNode treeNode, Collection colAll,
                                  Collection collection, String strFlag,
                                  List listRoot, String taskID,
                                  Integer taskTimeID, List auditStates,
                                  HttpServletRequest req) throws
      Warning, IOException, ServletException {
    if (treeNode != null&&treeNode.getDisplay()==1) {
      boolean bol = false;
      Iterator iterator = collection.iterator();
      while (iterator.hasNext()) {
        List list = (List) iterator.next();
        if ( ( (String) list.get(1)).equals(treeNode.id())) {
          bol = true;
          break;
        }
      }
      if (!bol) {
        List lst = new ArrayList();
        if (treeNode.getChildren() != null) {
          lst.add("G");
        }
        else {
          if (treeNode.getParent() != null) {
            lst.add("C");
          }
          else {
            lst.add("G");
          }
        }
        lst.add(treeNode.id());
        lst.add(treeNode.getUnitName());
        String isfill = "";
        if ("alread".equals(strFlag)) {
          isfill = FILL_STATE_NO;
        }
        if ("not".equals(strFlag)) {
          isfill = FILL_STATE_YES;
        }
        lst.add(isfill);

        String parentCode = null;
        if (treeNode.getParent() != null) {
          boolean rootFlag = false;
          for (int j = 0; j < listRoot.size(); j++) {
            String unitCodeRoot = (String) listRoot.get(j);
            if (treeNode.id().equals(unitCodeRoot)) {
              rootFlag = true;
            }
          }
          if (rootFlag) {
            lst.set(0, "G");
          }
          else {
            parentCode = treeNode.getParent().id();
          }
        }

        lst.add(parentCode);
        lst.add(addIDString(treeNode));

        FillStateForm stateForm = getFillStateForm(colAll, treeNode.id());
        if (stateForm != null) {
          if (stateForm.getFillDate() != null) {
            lst.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                    format(
                stateForm.getFillDate()));
          }
          else {
            lst.add("");
          }
          //审核标志
          if (stateForm.getFlag() == DataStatus.AUDITED) {
            lst.add(Boolean.TRUE);
          }
          else {
            lst.add(Boolean.FALSE);
          }
          if (hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT)) {
            String auditDate = DBAnalyseHelper.combFillAuditToShow(stateForm.
                getUnit().id(), auditStates);
            if ("".equals(auditDate)) {
              lst.add(FILL_STATE_NO);
            }
            else {
              lst.add(FILL_STATE_YES);
            }
            lst.add(auditDate);
          }
          else {
            lst.add(FILL_STATE_NO);
            lst.add("");
          }
        }
        else {
          lst.add("");
          lst.add(Boolean.FALSE);
          lst.add(FILL_STATE_NO);
          lst.add("");
        }

        collection.add(lst);
        if (parentCode != null) {
          return treeNode.getParent() != null ?
              addFillState(treeNode.getParent(), colAll, collection, strFlag,
                           listRoot, taskID, taskTimeID, auditStates, req) :
              collection;

        }
      }
    }
    return collection;
  }

  private Collection addAllFillState(Iterator it, Collection collection,
                                     Iterator itRoot, HttpServletRequest req,
                                     HttpServletResponse res) throws Warning,
      IOException, ServletException {
    //所有审核状态
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();
    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator itTime = task.getTaskTimes();
        if (itTime.hasNext()) {
          time = (TaskTime) itTime.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }
    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(
        AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();
    List auditStates = analyseMng.getAuditStates(taskID, taskTimeID.intValue());

    List listRoot = new ArrayList();
    while (itRoot.hasNext()) {
      UnitTreeNode treeNode = (UnitTreeNode) itRoot.next();
      String code = (String) treeNode.id();
    }

    while (it.hasNext()) {
      boolean bol = false;
      FillStateForm stateForm = (FillStateForm) it.next();
      UnitTreeNode unitNode = stateForm.getUnit();
      if(unitNode.getDisplay()==1){
      Iterator iterator = collection.iterator();
      while (iterator.hasNext()) {
        List list = (List) iterator.next();
        String unitID = (String) list.get(1);
        if (unitID.equals(stateForm.getUnit().id())) {
          if (stateForm.getFlag() == DataStatus.UNREPORTED_ENVLOP)
            list.set(3, FILL_STATE_NO + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP)
            list.set(3, FILL_STATE_YES + FILL_ENVELOP);
          else if (stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP)
            list.set(3, FILL_STATE_YES + FILL_UNENVLOP);
          else
            list.set(3, FILL_STATE_NO + FILL_UNENVLOP);

          bol = true;
          break;
        }
      }
      if (!bol) {
        List lst = new ArrayList();
        if (stateForm.getUnit().getChildren() != null) {
          lst.add("G");
        }
        else {
          if (stateForm.getUnit().getParent() != null) {
            lst.add("C");
          }
          else {
            lst.add("G");
          }
        }
        lst.add(stateForm.getUnit().id());
        lst.add(stateForm.getUnit().getUnitName());
        if (stateForm.getFlag() == DataStatus.UNREPORTED_ENVLOP)
          lst.add(3, FILL_STATE_NO + FILL_ENVELOP);
        else if (stateForm.getFlag() == DataStatus.REPORTED_ENVLOP)
          lst.add(3, FILL_STATE_YES + FILL_ENVELOP);
        else if (stateForm.getFlag() == DataStatus.REPORTED_UNENVLOP)
          lst.add(3, FILL_STATE_YES + FILL_UNENVLOP);
        else
          lst.add(3, FILL_STATE_NO + FILL_UNENVLOP);

        String parentCode = null;
        if (stateForm.getUnit().getParent() != null) {
          boolean rootFlag = false;
          for (int j = 0; j < listRoot.size(); j++) {
            String unitCodeRoot = (String) listRoot.get(j);
            if (stateForm.getUnit().id().equals(unitCodeRoot)) {
              rootFlag = true;
            }
          }
          if (rootFlag) {
            lst.set(0, "G");
          }
          else {
            parentCode = stateForm.getUnit().getParent().id();
          }
        }

        lst.add(parentCode);
        lst.add(addIDString(stateForm.getUnit()));
        if (stateForm.getFillDate() != null) {
          lst.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
              stateForm.getFillDate()));
        }
        else {
          lst.add("");
        }
        //审核标志
        if (stateForm.getFlag() == DataStatus.AUDITED) {
          lst.add(Boolean.TRUE);
        }
        else {
          lst.add(Boolean.FALSE);
        }

        //是否有审核权限
        if (hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT)) {
          String auditDate = DBAnalyseHelper.combFillAuditToShow(stateForm.
              getUnit().id(), auditStates);
          if ("".equals(auditDate)) {
            lst.add(FILL_STATE_NO);
          }
          else {
            lst.add(FILL_STATE_YES);
          }
          lst.add(auditDate);
        }
        else {
          lst.add(FILL_STATE_NO);
          lst.add("");
        }
        try {
			AuditStateForm auditForm = DBAnalyseHelper.findAuditInfo(stateForm.getUnit().id(), taskID, taskTimeID.intValue());
			if(auditForm!=null){
				lst.add(auditForm.getAuditor());
			}else{
				lst.add("");
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        collection.add(lst);
      }
     }
    }
    return collection;
  }

  private boolean isParentFilled(Collection col, UnitTreeNode treeNode) {
    boolean flag = false;
    if (treeNode.getParent() != null) {
      Iterator it = col.iterator();
      while (it.hasNext()) {
        FillStateForm stateFormTmp = (FillStateForm) it.next();
        if (stateFormTmp.getUnit().id().equals(treeNode.getParent().id())) {
          if (stateFormTmp.isFilled()) {
            flag = true;
          }
          else {
            flag = false;
          }
          break;
        }
      }
      if (!flag) {
        return isParentFilled(col, treeNode.getParent());
      }
    }
    return flag;
  }

  private FillStateForm getFillStateForm(Collection colAllState, String unitID) {
    FillStateForm stateForm = null;
    Iterator it = colAllState.iterator();
    while (it.hasNext()) {
      FillStateForm form = (FillStateForm) it.next();
      if (form.getUnit().id().equals(unitID)) {
        stateForm = form;
        break;
      }
    }
    return stateForm;
  }

  private String addIDString(UnitTreeNode treeNode) {
    String idstr = treeNode.id();
    String reportType = treeNode.getReportType();
    if (reportType == null)
      reportType = "0";
    if (reportType.equals("9")) {
      idstr = "<img src='fw/def/image/tree/20/icon_9.gif' width='16' height='16' border='0' id='tree" +
          treeNode.id() + "'>" + idstr;
    }
    if (reportType.equals("7")) {
      idstr = "<img src='fw/def/image/tree/20/icon_7.gif' width='16' height='16' border='0' id='tree" +
          treeNode.id() + "'>" + idstr;
    }
    if (reportType.equals("0")) {
      idstr = "<img src='fw/def/image/tree/20/icon_0.gif' width='16' height='16' border='0' id='tree" +
          treeNode.id() + "'>" + idstr;
    }
    if (reportType.equals("1")) {
      idstr = "<img src='fw/def/image/tree/20/icon_1.gif' width='16' height='16' border='0' id='tree" +
          treeNode.id() + "'>" + idstr;
    }
    return idstr;
  }

  /**
   * 执行指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void executeScalarQueryTemplate(HttpServletRequest request,
                                          HttpServletResponse response) throws
      Warning, IOException, ServletException {
    Task task = (Task) request.getAttribute("task");

    AnalyseManager mng = ( (AnalyseManagerFactory) Factory.getInstance(
        AnalyseManagerFactory.class.getName())).createAnalyseManager();

    // 解析选择的模板
    String strTemplateIDs = request.getParameter("tplIDs");
    StringTokenizer token = new StringTokenizer(strTemplateIDs.trim(), ",");

    int count = token.countTokens();
    List templateList = new LinkedList();

    for (int i = 0; i < count; i++) {
      Integer templateID = Integer.valueOf(token.nextToken().trim());
      templateList.add(mng.getScalarQueryTemplate(templateID));
    }

    // 解析选择的任务时间
    String strTaskTimeIDs = request.getParameter("taskTimeIDs");
    token = new StringTokenizer(strTaskTimeIDs.trim(), ",");

    count = token.countTokens();
    List taskTimeList = new LinkedList();

    for (int i = 0; i < count; i++) {
      Integer taskTimeID = Integer.valueOf(token.nextToken().trim());
      TaskTime taskTime = task.getTaskTime(taskTimeID);

      if (taskTime != null) {
        taskTimeList.add(taskTime);
      }
    }

    TaskTime[] taskTimes = (TaskTime[]) taskTimeList.toArray(new TaskTime[0]);
    Integer[] taskTimeIDs = new Integer[taskTimes.length];
    for (int i = 0; i < taskTimeIDs.length; i++) {
      taskTimeIDs[i] = taskTimes[i].getTaskTimeID();

      // 打印选项
    }
    boolean showIndex = UtilServlet.isCheckboxChecked(request, "showIndex"); // 是否显示序号
    boolean showUnitName = UtilServlet.isCheckboxChecked(request,
        "showUnitName"); // 是否显示单位名称
    boolean sameWorkbook = UtilServlet.isCheckboxChecked(request,
        "sameWorkbook");

    // 查看上报来的单位，如果为空，表示使用默认单位
    String[] unitIDs = request.getParameterValues("unitIDs");
    UnitTreeNode[] units = null;

    ModelManager modelMng = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(task.id());
    UnitTreeManager treeMng = modelMng.getUnitTreeManager();

    if (unitIDs == null) {
      // 如果没有选择单位，查询所有能看到的单位
      UnitACL unitACL = modelMng.getUnitACL(RootServlet.getLoginUser(request));

      Collection col = new LinkedList();
      for (Iterator iter = treeMng.getUnitForest(unitACL); iter.hasNext(); ) {
        UnitTreeNode node = (UnitTreeNode) iter.next();
        UnitTreeNodeUtil.getAllChildren(col, node);
      }

      unitIDs = new String[col.size()];
      int index = 0;
      for (Iterator iter = col.iterator(); iter.hasNext(); ) {
        unitIDs[index++] = ( (UnitTreeNode) iter.next()).id();
      }
    }

    Collection unitCol = treeMng.getUnits(unitIDs);
    units = (UnitTreeNode[]) unitCol.toArray(new UnitTreeNode[0]);

    //List templateList = new LinkedList();
    List resultList = new LinkedList(); // 查询结果列表，元素为ScalarResultForm
    List unitList = new LinkedList(); // 单位列表，元素为UnitTreeNode

    String tempFileName = System.currentTimeMillis() + "" +
        (int) (Math.random() * 10);

    // 如果使用同一个workbook，那么页面显示excel；否则下载zip压缩包
    OutputStream out = null;

    if (sameWorkbook) {
      String fileName = Config.getString(
          "cn.com.youtong.apollo.excel.directory") + File.separator +
          tempFileName + ".xls";
      request.getSession().setAttribute("fileName", tempFileName + ".xls");

      out = new BufferedOutputStream(new FileOutputStream(fileName));
    }
    else {
      String fileName = Config.getString(
          "cn.com.youtong.apollo.excel.directory") + File.separator +
          tempFileName + ".zip";
      request.getSession().setAttribute("fileName", tempFileName + ".zip");

      out = new ZipOutputStream(new FileOutputStream(fileName));
    }

    TemplateResultExcelAssemble assemble = new TemplateResultExcelAssemble();
    assemble.createWorkbook();

    for (Iterator iter = templateList.iterator(); iter.hasNext(); ) {
      ScalarQueryTemplate template = (ScalarQueryTemplate) iter.next();

      ScalarResultForm result = null;
      result = mng.queryScalar(template.getTemplateID(), taskTimeIDs, unitIDs);
      resultList.add(result);
      unitList.add(units);

      Object[][] tableData = null;
      if (result == null) {
        tableData = new Object[][] {
            {
            ""}
        };
      }
      else {
        Object[][][] resultData = result.getResult();
        tableData = new Object[resultData.length][resultData[0].length *
            resultData[0][0].length];
        for (int i = 0; i < resultData.length; i++) {
          int index = 0;
          for (int j = 0; j < resultData[i][0].length; j++) {
            for (int k = 0; k < resultData[i].length; k++) {
              tableData[i][index++] = resultData[i][k][j];
            }
          }
        }
      }

      if (unitIDs == null) {
        String[] tempUnitIDs = template.getCondition().getUnitIDs();
        UnitTreeNode[] tempUnits = (UnitTreeNode[]) treeMng.getUnits(
            tempUnitIDs).toArray(new UnitTreeNode[0]);
        assemble.add(template, taskTimes, tableData, showIndex, showUnitName,
                     tempUnits);
      }
      else {
        assemble.add(template, taskTimes, tableData, showIndex, showUnitName,
                     units);

      }
      if (!sameWorkbook) {
        ( (ZipOutputStream) out).putNextEntry(new ZipEntry(template.
            getTemplateID().intValue() + ".xls")); assemble.writeToStream(out);

        if (iter.hasNext()) {
          assemble.createWorkbook(); // 重新建立一个workbook,方便下次打印
        }
      }
    }

    if (sameWorkbook) {
      assemble.writeToStream(out);

    }
    Util.close(out);

    // 设置属性
    request.setAttribute("templateList", templateList);
    request.setAttribute("resultList", resultList);
    request.setAttribute("unitList", unitList);
    request.setAttribute("showIndex", new Boolean(showIndex));
    request.setAttribute("showUnitName", new Boolean(showUnitName));

    go2UrlWithAttibute(request, response, TEMPLATE_QUERY_RESULT_PAGE);
  }

  /**
   * 导入指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void importScalarQueryTemplate(HttpServletRequest req,
                                         HttpServletResponse res) throws
      Warning, IOException, ServletException {
    try {
      AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                       getInstance(AnalyseManagerFactory.class.
          getName())).createAnalyseManager();

      UploadBean upload = new UploadBean(getServletConfig(), req, res);
      analyseManager.importScalarQueryTemplate(getLoginUser(req).getUserID(),
                                               upload.getXmlInputStreamUploaded());
      showManageScalarQueryTemplatePage(req, res);
    }
    catch (Warning ex) {
      //设置返回页面，返回到SHOW_MANAGE_SCALAR_QUERY_TEMPLATE_PAGE
      req.setAttribute(RETURN_URL,
                       "analyse?operation=" +
                       SHOW_MANAGE_SCALAR_QUERY_TEMPLATE_PAGE);
      throw ex;
    }
  }

  /**
   * 导入模板参数
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void importParam(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UploadBean upload = new UploadBean(getServletConfig(), req, res);
    com.jspsmart.upload.File file = upload.getUploadFile();
    if ("ZIP".equalsIgnoreCase(file.getFileExt())) { //没有压缩过了

    }
    String taskID = req.getParameter("taskID");
    byte[] indata = file.getBinaryDatas();
    byte[] data = Util.GZIPAndEncodeBase64(indata);
    String filepath = req.getSession().getServletContext().getRealPath(
        "WEB-INF") + "/params/";
    java.io.File dir = new java.io.File(filepath);
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String filename = req.getSession().getServletContext().getRealPath(
        "WEB-INF") + "/params/" + taskID + ".par";
    FileManager fmanager = FileManager.getInstance(filename);
    try {
      fmanager.Save(data);
    }
    finally {
      fmanager.close();
    }
    req.setAttribute("filename", file.getFilePathName());
    req.setAttribute("filelen", new Integer(indata.length));
    go2UrlWithAttibute(req, res, PARAM_PAGE);
  }

  /**
   * 导出指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void exportScalarQueryTemplate(HttpServletRequest req,
                                         HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Integer templateID = new Integer(req.getParameter("templateID"));
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();
    ScalarQueryTemplate template = analyseManager.getScalarQueryTemplate(
        templateID);
    //设置response
    res.setContentType("text/query; charset=GBK");
    res.setHeader("Content-disposition",
                  "attachment; filename=" +
                  java.net.URLEncoder.encode(template.getName(), "utf8") +
                  ".query");

    analyseManager.exportScalarQueryTemplate(templateID, res.getOutputStream());
  }

  /**
   * 查询报表指标
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void queryScalar(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    ScalarQueryForm condition = getScalarQueryCondition(req, res);

    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    req.setAttribute("result", analyseManager.queryScalar(condition));
    req.setAttribute("condition", condition);
    go2UrlWithAttibute(req, res, SCALAR_RESULT_PAGE);
  }

  /**
   * 从request中得到指标查询条件
   * @param req
   * @param res
   * @return 指标查询条件
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private ScalarQueryForm getScalarQueryCondition(HttpServletRequest req,
                                                  HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();
    String[] taskTimeIDs = req.getParameterValues("taskTimeIDs");
    String[] unitIDs = req.getParameterValues("unitIDs");
    String[] scalarNames = req.getParameterValues("scalarNames");
    String[] expressions = req.getParameterValues("expressions");
    String[] defineScalars = req.getParameterValues("defineScalars");

    ScalarQueryForm condition = new ScalarQueryForm();

    condition.setTaskID(taskID);

    condition.setScalars(getScalars(scalarNames, expressions, defineScalars));

    if (taskTimeIDs != null) {
      Integer[] IDs = new Integer[taskTimeIDs.length];
      for (int i = 0; i < taskTimeIDs.length; i++) {
        IDs[i] = new Integer(taskTimeIDs[i]);
      }
      condition.setTaskTimeIDs(IDs);
    }

    if (unitIDs != null) {
      condition.setUnitIDs(unitIDs);
    }

    return condition;
  }

  /**
   * 得到指标数组
   * @param names 指标名称数组
   * @param expressions 指标表达式数组
   * @param defineScalars 自定义指标
   * @return 指标数组
   */
  private ScalarForm[] getScalars(String[] names, String[] expressions,
                                  String[] defineScalars) {
    int count = 0;
    if (names != null) {
      count += names.length;
    }
    if (defineScalars != null) {
      count += defineScalars.length;
    }

    ScalarForm[] scalars = new ScalarForm[count];

    if (names != null) {
      for (int i = 0; i < names.length; i++) {
        scalars[i] = new ScalarForm(names[i], expressions[i]);
      }
    }

    if (defineScalars != null) {
      for (int i = 0; i < defineScalars.length; i++) {
        scalars[scalars.length - defineScalars.length +
            i] = getDefineScalar(defineScalars[i]);
      }
    }

    return scalars;
  }

  /**
   * 将自定义指标字符串转化为指标对象
   * @param defineScalarStr 自定义指标字符串，格式为“name,expression”
   * @return 指标对象
   */
  private ScalarForm getDefineScalar(String defineScalarStr) {
    int delimIndex = defineScalarStr.indexOf(",");
    String name = defineScalarStr.substring(0, delimIndex);
    String expression = defineScalarStr.substring(delimIndex + 1);
    return new ScalarForm(name, expression);
  }

  /**
   * 得到表的html表示
   * @param req
   * @return TableViewer集合的Iterator
   * @throws Warningn
   * @throws IOException
   * @throws ServletException
   */
  private Iterator getTableViewers(HttpServletRequest req) throws Warning,
      IOException, ServletException {
    Task task = (Task) req.getAttribute("task");
    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
        ModelManagerFactory.class.getName())).createModelManager(task.id());

    //产生一个假的数据xml文件
    String xml = "<x>";

    for (Iterator tableItr = task.getAllTables(); tableItr.hasNext(); ) {
      Table table = (Table) tableItr.next();
      xml = xml + "<table ID=\"" + table.id() + "\"/>";
    }
    xml += "</x>";

    StringBufferInputStream xmlIn = new StringBufferInputStream(xml);

    return UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
  }

  /**
   * 显示查询报表指标条件选择页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showScalarConditionPage(HttpServletRequest req,
                                       HttpServletResponse res) throws Warning,
      IOException, ServletException {
    ScalarQueryForm condition = getScalarQueryCondition(req, res);
    req.setAttribute("condition", condition);
//    UtilServlet.showCheckboxUnitTree(req, res, condition.getUnitIDs(),
    UtilServlet.showCheckboxUnitTreeOfShow(req, res, condition.getUnitIDs(),
                                     "checkboxUnitTree");
    req.setAttribute("tableViewerItr", this.getTableViewers(req));
    go2UrlWithAttibute(req, res, SCALAR_CONDITION_PAGE);
  }

  /**
   * 得到所有填报单位的填报情况
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void getAllFillState(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }

    //记录查询条件
    req.setAttribute("taskTimeID", taskTimeID);

    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();

    Collection col = analyseMng.getFillState(taskID, taskTimeID);
    req.setAttribute("fillstates", col);
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    req.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    if (req.getParameter("curPage") != null) {
      req.setAttribute("curPage", req.getParameter("curPage"));
    }
    else {
      req.setAttribute("curPage", "1");
    }
  }

  /**
   * 按条件查询填报情况
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void queryFillState(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }

    String codeOrName = URLDecoder.decode(req.getParameter("codeOrName"),
                                          "utf8"); ;

    //记录查询条件
    req.setAttribute("codeOrName", codeOrName);
    req.setAttribute("taskTimeID", taskTimeID);

    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();

    Collection col = analyseMng.getFillState(taskID, taskTimeID, codeOrName);
    req.setAttribute("fillstates", col);
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    req.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    if (req.getParameter("curPage") != null) {
      req.setAttribute("curPage", req.getParameter("curPage"));
    }
    else {
      req.setAttribute("curPage", "1");
    }

    go2UrlWithAttibute(req, res, FILL_STATE_PAGE);
  }

  private void queryFillState1(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Collection col = new Vector();
    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }

    String codeOrName = URLDecoder.decode(req.getParameter("codeOrName"),
                                          "utf8"); ;

    //记录查询条件
    req.getSession().setAttribute("codeOrName", codeOrName);
    req.getSession().setAttribute("taskTimeID", taskTimeID);
    req.getSession().setAttribute("task", task);

    AnalyseManagerFactory analyseMngFcty = (AnalyseManagerFactory) Factory.
        getInstance(AnalyseManagerFactory.class.getName());
    AnalyseManager analyseMng = analyseMngFcty.createAnalyseManager();

    Collection collection = analyseMng.getFillState(taskID, taskTimeID,
        codeOrName);

    col = addFillState(collection, col, "query", getUnitForeset(req, res),
                       taskID, taskTimeID, req);

    req.getSession().setAttribute("fillstates", col);
    req.getSession().setAttribute("fillstateTitle", "上报情况");
    res.sendRedirect(req.getContextPath() + "/regionBrowse.do");

  }

  /**
   * 生成一组柱形图
   * @param col 封装单位方式查询报表指标的信息
   * @return生成的bar图的文件名集合
   */
  private Collection drawBar(Collection dataTable) {
    //有关变量的定义和初始化
    boolean flag = true;
    String title = null;
    Collection barFileNames = new ArrayList();
    String barFileName = null;
    try {
      Iterator rowIter = dataTable.iterator();
      //得到Bar 图上面的柱的名称集合
      Collection itemNameCol = (Collection) rowIter.next();
      Iterator itemNameIter = itemNameCol.iterator();
      itemNameIter.next();
      itemNameIter.remove();
      String[] itemNames = new String[itemNameCol.size()];
      int i = 0;
      while (itemNameIter.hasNext()) {
        itemNames[i] = itemNameIter.next().toString();
        i++;
      }
      //把柱的名称集合从datatable中移动出
      rowIter.remove();
      //构着图标表集合
      while (rowIter.hasNext()) {
        flag = true;
        Iterator iterValueItr = ( (Collection) rowIter.next()).iterator();
        Collection colBarForm = new ArrayList();
        int j = 0;
        while (iterValueItr.hasNext()) {
          if (flag) {
            title = iterValueItr.next().toString();
            flag = false;
          }
          else {
            Integer value = new Integer(0);
            try {
              Double valueOfDouble = new Double(iterValueItr.next().toString());
              value = new Integer(valueOfDouble.intValue());
            }
            catch (NumberFormatException ex) {
            }
            BarForm barForm = new BarForm(title);
            barForm.addData(itemNames[j], value);
            colBarForm.add(barForm);
            j++;
          }
        }
        //创建bar图
        Chart chart = ChartsFactory.createBarChart("", "", "", colBarForm);

        barFileName = System.currentTimeMillis() + ".jpg";
        String drawPath = Config.getString(
            "cn.com.youtong.apollo.chart.directory") + File.separator +
            barFileName;
        chart.draw(drawPath);
        barFileNames.add(barFileName);
      }
    }
    catch (ChartException ex) {
      log.error("", ex);
    }
    return barFileNames;
  }

  /**
   * 根据dates数据产生饼图
   * @param datas
   * @return 产生饼图的文件地址，datas没有标题返回空的集合；没有数据也返回空集合。
   */
  private Collection drawPie(Collection datas) {
    Collection fileNames = new LinkedList(); // 生成的文件名

    Iterator dataIter = datas.iterator();
    // 取出各饼图构成部分的标题，即文字描述
    if (!dataIter.hasNext()) {
      return fileNames; // 没有标题返回null
    }

    Collection titleCol = (Collection) dataIter.next();

    try {
      while (dataIter.hasNext()) {
        Iterator titleIter = titleCol.iterator();
        titleIter.next();

        Collection col = (Collection) dataIter.next();
        Iterator iter = col.iterator();

        String chartTitle = iter.next().toString();
        PieForm form = new PieForm();
        while (iter.hasNext()) {
          String temp = iter.next().toString();
          Integer value = new Integer(0);
          try {
            Double valueOfDouble = new Double(temp);
            value = new Integer(valueOfDouble.intValue());
          }
          catch (NumberFormatException ex) {
          }
          form.addData(titleIter.next().toString(), value);
        }

        //创建图
        Chart chart = ChartsFactory.createPieChart(chartTitle, form);

        String fileName = System.currentTimeMillis() + ".jpg";
        String drawPath = Config.getString(
            "cn.com.youtong.apollo.chart.directory") + File.separator +
            fileName;
        chart.draw(drawPath);
        fileNames.add(fileName);
      }
    }
    catch (Exception ex) {
      log.error("创建饼图失败", ex);
    }

    return fileNames;
  }

  /**
   * 根据dates数据产生线图
   * @param datas
   * @return 产生线图的文件地址，datas没有标题返回空的集合；没有数据也返回空集合。
   */
  private Collection drawLinear(Collection datas) {
    Collection fileNames = new LinkedList(); // 生成的文件名

    /**Iterator dataIter = datas.iterator();
       // 取出各饼图构成部分的标题，即文字描述
       if (!dataIter.hasNext())
     return fileNames; // 没有标题返回null
       Collection titleCol = (Collection) dataIter.next();
       try
       {
     while(dataIter.hasNext())
     {
      Iterator titleIter = titleCol.iterator();
      titleIter.next();
      Collection col = (Collection) dataIter.next();
      Iterator iter = col.iterator();
      String chartTitle = iter.next().toString();
      PieForm form = new PieForm();
      while(iter.hasNext())
      {
       String temp = iter.next().toString();
       Integer value = new Integer(0);
       try
       {
            Double valueOfDouble = new Double(temp);
            value = new Integer(valueOfDouble.intValue());
       }
       catch(NumberFormatException ex)
       {
       }
       form.addData(titleIter.next().toString(), value);
      }
      //创建图
      Chart chart = ChartsFactory.createPieChart(chartTitle, form);
      String fileName = System.currentTimeMillis() + ".jpg";
      String drawPath = Config.getString("cn.com.youtong.apollo.chart.directory") + File.separator + fileName;
      chart.draw(drawPath);
      fileNames.add(fileName);
     }
       } catch (Exception ex)
       {
     log.error("创建饼图失败", ex);
       }*/

    return fileNames;
  }

  /**
   * 显示指标查询模板管理页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showManageScalarQueryTemplatePage(HttpServletRequest request,
                                                 HttpServletResponse response) throws
      Warning, IOException, ServletException {
    Task task = (Task) request.getAttribute("task");
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    request.setAttribute("templateItr",
                         analyseManager.getScalarQueryTemplates(
        getLoginUser(request).getUserID(), task.id()));
    go2UrlWithAttibute(request, response, MANAGE_TEMPLATE_PAGE);
  }

  /**
   * 显示参数管理页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showManageParamPage(HttpServletRequest request,
                                   HttpServletResponse response) throws Warning,
      IOException, ServletException {
    go2UrlWithAttibute(request, response, PARAM_PAGE);
  }

  /**
   * 显示模板执行前，条件选择页面
   * @param request
   * @param response
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showScalarQueryTemplatePage(HttpServletRequest request,
                                           HttpServletResponse response) throws
      Warning, IOException, ServletException {
    Task task = (Task) request.getAttribute("task");
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    request.setAttribute("templateItr",
                         analyseManager.getScalarQueryTemplates(
        getLoginUser(request).getUserID(), task.id()));

    go2UrlWithAttibute(request, response, SCALAR_QUERT_TEMPLATE_PAGE);
  }

  /**
   * 显示指标查询模板创建页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showCreateScalarQueryTemplatePage(HttpServletRequest request,
                                                 HttpServletResponse response) throws
      Warning, IOException, ServletException {
    Task task = (Task) request.getAttribute("task");
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    request.setAttribute("templateItr",
                         analyseManager.getScalarQueryTemplates(
        getLoginUser(request).getUserID(), task.id()));

    showScalarConditionPage(request, response);
  }

  /**
   * 将查询条件保存为模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
//    private void saveQueryConditionAsTemplate(HttpServletRequest request,
//                                              HttpServletResponse response) throws
//        Warning, IOException, ServletException
//    {
//        showCreateScalarQueryTemplatePage(request, response);
//    }

  /**
   * 显示指标查询模板更新页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUpdateScalarQueryTemplatePage(HttpServletRequest request,
                                                 HttpServletResponse response) throws
      Warning, IOException, ServletException {
    Task task = (Task) request.getAttribute("task");
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    Integer templateID = new Integer(request.getParameter("templateID"));
    ScalarQueryTemplate template = analyseManager.getScalarQueryTemplate(
        templateID);
    request.setAttribute("template", template);

    ScalarQueryForm condition = template.getCondition();
    request.setAttribute("condition", condition);

    request.setAttribute("templateItr",
                         analyseManager.getScalarQueryTemplates(
        getLoginUser(request).getUserID(), task.id()));

    UtilServlet.showCheckboxUnitTree(request, response, condition.getUnitIDs(),
                                     "checkboxUnitTree");

    request.setAttribute("tableViewerItr", this.getTableViewers(request));

    go2UrlWithAttibute(request, response, SCALAR_CONDITION_PAGE);
  }

  /**
   * 更新指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  /**private void updateScalarQueryTemplate(HttpServletRequest request,
       HttpServletResponse response) throws
          Warning, IOException, ServletException
           {
          AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
       getInstance(AnalyseManagerFactory.class.
                  getName())).createAnalyseManager();
          Integer templateID = new Integer(request.getParameter("templateID"));
          String name = request.getParameter("name");
       ScalarQueryForm condition = getScalarQueryCondition(request, response);
       analyseManager.updateScalarQueryTemplate(templateID, name, condition);
          showManageScalarQueryTemplatePage(request, response);
           }*/

  /**
   * 创建指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  /**private void createScalarQueryTemplate(HttpServletRequest request,
       HttpServletResponse response) throws
          Warning, IOException, ServletException
           {
          AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
       getInstance(AnalyseManagerFactory.class.
                  getName())).createAnalyseManager();
          Integer userID = getLoginUser(request).getUserID();
          String name = request.getParameter("name");
       ScalarQueryForm condition = getScalarQueryCondition(request, response);
          analyseManager.createScalarQueryTemplate(userID, name, condition);
          showManageScalarQueryTemplatePage(request, response);
           }*/

  /**
   * 删除指标查询模板
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void deleteScalarQueryTemplate(HttpServletRequest request,
                                         HttpServletResponse response) throws
      Warning, IOException, ServletException {
    AnalyseManager analyseManager = ( (AnalyseManagerFactory) Factory.
                                     getInstance(AnalyseManagerFactory.class.
                                                 getName())).
        createAnalyseManager();

    Integer templateID = new Integer(request.getParameter("templateID"));
    analyseManager.deleteScalarQueryTemplate(templateID);

    showManageScalarQueryTemplatePage(request, response);
  }

  /**
   * 显示指标结果图表表示页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   *
   * 1。得到结果数据
   * 2。根据数据生成chart
   * 3。显示图表页面
   */
  private void showScalarChart(HttpServletRequest request,
                               HttpServletResponse response) throws Warning,
      IOException, ServletException {
    // 生成的图形类型
    String chartType = request.getParameter("chartType");
    log.debug("chartType = " + chartType);

    String[] scalarDatas = request.getParameterValues("scalarDatas");
    for (String string : scalarDatas) {
		System.out.println("AnalyseServlet --->1917--->"+string);
	}
    Collection tableData = new ArrayList();
    for (int i = 0; i < scalarDatas.length; i++) {
      StringTokenizer st = new StringTokenizer(scalarDatas[i], ",");
      Collection rowData = new ArrayList();
      while (st.hasMoreTokens()) {
        rowData.add(st.nextToken());
      }
      tableData.add(rowData);
    }

    if (chartType.equalsIgnoreCase("bar")) {
      Collection fileNames = drawBar(tableData);
      //显示图表
      request.setAttribute("fileNames", fileNames);
    }
    else if (chartType.equalsIgnoreCase("pie")) {
      Collection fileNames = drawPie(tableData);
      request.setAttribute("fileNames", fileNames);
    }
    else if (chartType.equalsIgnoreCase("linear")) {
      Collection fileNames = drawLinear(tableData);
      request.setAttribute("fileNames", fileNames);
    }

    go2UrlWithAttibute(request, response, SCALAR_CHART_PAGE);
  }

  private void showChartImage(HttpServletRequest request,
                              HttpServletResponse response) throws Warning,
      IOException, ServletException {
    response.setContentType("image/gif");
    OutputStream out = response.getOutputStream();
    String file = request.getParameter("chart");
    file = (Config.getString("cn.com.youtong.apollo.chart.directory") +
            File.separator + file);
    UtilServlet.dumpFileContent(out, file);
  }

  /**
   * 显示指标结果Excel表示
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   *
   * 1。得到结果数据
   * 2。根据数据生成excel
   * 3。显示excel
   */
  private void showScalarExcel(HttpServletRequest request,HttpServletResponse response) throws Warning,
      IOException, ServletException {
	  try {
		Collection tableData = new ArrayList();
		String[] scalarDatas = request.getParameterValues("scalarDatas");
		if (scalarDatas == null) {
		      return;
		}
		for (int i = 0; i < scalarDatas.length; i++) {
		  StringTokenizer st = new StringTokenizer(scalarDatas[i], ",");
		  Collection rowData = new ArrayList();
		  while (st.hasMoreTokens()) {
		     rowData.add(st.nextToken());
		  }
		   tableData.add(rowData);
		}
		String fileName = System.currentTimeMillis() + ".xls";
		String absoluteFileName = Config.getString("cn.com.youtong.apollo.excel.directory") + File.separator + fileName;
		Excel.generateExcelFile(absoluteFileName, tableData);
		//设置excel文件的显示格式
		response.setContentType("application/vnd.ms-excel;charset=GBK");
		OutputStream out = response.getOutputStream();
		// 写入数据流
		UtilServlet.dumpFileContent(out, absoluteFileName);
	  } catch (Exception e) {
		  log.error("导出Excel出现异常",e);
			e.printStackTrace();
	  }
  }

  /**
   * 分发页面请求。
   * @param req
   * @param res
   * @throws cn.com.youtong.formserver.common.Warning
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   */
  public void perform(HttpServletRequest request, HttpServletResponse response) throws
      Warning, IOException, ServletException {
    TaskManager taskManager = ( (TaskManagerFactory) Factory.getInstance(
        TaskManagerFactory.class.getName())).createTaskManager();

    String taskID = request.getParameter("taskID");
    // 如果没有得到，在Session里面查找
    if (Util.isEmptyString(taskID)) {
      taskID = (String) request.getSession().getAttribute("taskID");
    }
    if (!Util.isEmptyString(taskID)) {
      request.setAttribute("task", taskManager.getTaskByID(taskID));

    }
    String operation = request.getParameter("operation");

    if (operation == null) {
      throw new Warning("无效的参数operation = " + operation);
    }
    else if (operation.equalsIgnoreCase(SHOW_SCALAR_CONDITION_PAGE)) {
      showScalarConditionPage(request, response);
    }
    else if (operation.equalsIgnoreCase(QUERY_FILL_STATE)) {
      queryFillState1(request, response);
    }
    else if (operation.equalsIgnoreCase(IMPORT_SCALAR_QUERY_TEMPLATE)) {
      importScalarQueryTemplate(request, response);
    }
    else if (operation.equalsIgnoreCase(IMPORT_PARAM)) {
      importParam(request, response);
    }
    else if (operation.equalsIgnoreCase(EXPORT_SCALAR_QUERY_TEMPLATE)) {
      exportScalarQueryTemplate(request, response);
    }
    else if (operation.equals(SHOW_NOT_FILLSTATE_RESULT)) {
      showNotFillStateResult1(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_ALREADY_FILLSTATE_RESULT)) {
      showAlreadyFillStateResult1(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_ALL_FILLSTATE_RESULT)) {
      showFillStateResult(request, response);
    }
    else if (operation.equalsIgnoreCase(EXECUTE_SCALAR_QUERY_TEMPLATE)) {
      executeScalarQueryTemplate(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_MANAGE_SCALAR_QUERY_TEMPLATE_PAGE)) {
      showManageScalarQueryTemplatePage(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_MANAGE_PARAM_PAGE)) {
      showManageParamPage(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_CREATE_SCALAR_QUERY_TEMPLATE_PAGE)) {
      showCreateScalarQueryTemplatePage(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_UPDATE_SCALAR_QUERY_TEMPLATE_PAGE)) {
      showUpdateScalarQueryTemplatePage(request, response);
    }
    else if (operation.equalsIgnoreCase(DELETE_SCALAR_QUERY_TEMPLATE)) {
      deleteScalarQueryTemplate(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_SCALAR_CHART_PAGE)) {
      showScalarChart(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_SCALAR_EXCEL)) {
      showScalarExcel(request, response);
    }
    else if (operation.equalsIgnoreCase(QUERY_SCALAR)) {
      queryScalar(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_CHART_IMAGE)) {
      showChartImage(request, response);
    }
    else if (operation.equalsIgnoreCase(SHOW_SCALAR_QUERY_TEMPLATE_PAGE)) {
      showScalarQueryTemplatePage(request, response);
    }
    else if (operation.equals("auditUnit")) {
      //审核单位
      auditUnit(request, response);
    }
    else if (operation.equals("auditUser")) {
      //审核用户
      auditUser(request, response);
    }
    else if (operation.equals("doAuditUser")) {
      //审核用户
      doAuditUser(request, response);
    }
    else {
      throw new Warning("无效的参数operation = " + operation);
    }
  }

  /**
   * 得到任务ID的HTML hidden input代码
   * @param taskID 任务ID
   * @return 任务ID的HTML hidden input代码
   */
  public static String getHiddenInputOfTaskID(String taskID) {
    String result = "";
    if (taskID != null) {
      result = getHiddenInput("taskID", taskID);
    }
    return result;
  }

  /**
   * 得到指定的HTML hidden input代码
   * @param name hidden input的名称
   * @param value hidden input的值
   * @return HTML hidden input代码
   */
  private static String getHiddenInput(String name, String value) {
    return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value +
        "\"/>";
  }

  /**
   * 得到单位ID数组的HTML hidden input代码
   * @param unitIDs 单位ID数组
   * @return 单位ID数组的HTML hidden input代码
   */
  public static String getHiddenInputOfUnitIDs(String[] unitIDs) {
    String result = "";
    if (unitIDs != null) {
      for (int i = 0; i < unitIDs.length; i++) {
        result += getHiddenInput("unitIDs", unitIDs[i]);
      }
    }
    return result;
  }

  /**
   * 得到任务时间ID数组的HTML hidden input代码
   * @param taskTimeIDs 任务时间ID数组
   * @return 任务时间ID数组的HTML hidden input代码
   */
  public static String getHiddenInputOfTaskTimeIDs(Integer[] taskTimeIDs) {
    String result = "";
    if (taskTimeIDs != null) {
      for (int i = 0; i < taskTimeIDs.length; i++) {
        result += getHiddenInput("taskTimeIDs", taskTimeIDs[i].toString());
      }
    }
    return result;
  }

  /**
   * 得到指标数组的HTML hidden input代码
   * @param scalars 指标数组
   * @return 指标数组的HTML hidden input代码
   */
  public static String getHiddenInputOfScalars(ScalarForm[] scalars) {
    String result = "";
    if (scalars != null) {
      for (int i = 0; i < scalars.length; i++) {
        result += getHiddenInput("scalarNames", scalars[i].getName());
        result += getHiddenInput("expressions", scalars[i].getExpression());
      }
    }
    return result;
  }

  private void auditUnit(HttpServletRequest req, HttpServletResponse res) throws
          Warning {
          try {
                  Task task = (Task) req.getAttribute("task");
                  String unitAudit = req.getParameter("unitAudit");
                  String audit = req.getParameter("audit");
                  String taskTimeID = req.getParameter("taskTimeID");
                  String taskID = task.id();

                  //判断权限
                  ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(
                          ModelManagerFactory.class.getName())).createModelManager(taskID);
                  User user = RootServlet.getLoginUser(req);
                  UnitACL unitACL = modelManager.getUnitACL(user);
                  if (unitACL.isReadable(unitAudit) &&
                      hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT)) {
                          int auditValue = ("" + DataStatus.AUDITED).equals(audit) ?
                                  DataStatus.AUDITED : DataStatus.REPORTED_ENVLOP;
                          List units = new ArrayList(), tasks = new ArrayList(),
                                  tasktimes = new ArrayList();
                          units.add(unitAudit);
                          tasks.add(taskID);
                          tasktimes.add(taskTimeID);

                          Session session = null;
                          try {
                                  session = HibernateUtil.getSessionFactory().openSession();
                                  DBAnalyseHelper helper = new DBAnalyseHelper();
                                  helper.alertState(auditValue, tasks, units, tasktimes, session);
                          }
                          finally {
                                  HibernateUtil.close(session);
                          }
                  }
          }
          catch (Exception e) {
                  throw new Warning(e);
          }
  }


  private void auditUser(HttpServletRequest req, HttpServletResponse res) throws
      Warning {
    final String SQL_SELECT_LEADER_USER =
        "select userID, name, enterpriseName from YTAPL_Users where userID>1 and userID<100;";

    final String SQL_SELECT_AUDIT_UNPASS_USER =
        "select USERID, AUDITDATE from Ytapl_AuditUser where TASKID=? and TASKTIMEID=?;";

    //判断权限
    if (!hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT_USER)) {
      return;
    }

    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();

    String taskTime = req.getParameter("taskTimeID");
    Integer taskTimeID = null;
    if (taskTime == null) {
      Calendar cldr = new GregorianCalendar();
      cldr.set(Calendar.MONTH, cldr.get(Calendar.MONTH) - 1);
      TaskTime time = task.getTaskTime(cldr.getTime());
      if (time == null) {
        Iterator it = task.getTaskTimes();
        if (it.hasNext()) {
          time = (TaskTime) it.next();
        }
      }
      taskTimeID = time.getTaskTimeID();
    }
    else {
      taskTimeID = new Integer(taskTime);
    }
    req.setAttribute("taskTimeID", taskTimeID);

    Session session = null;
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      //get all users
      ArrayList userIDs = new ArrayList(), userNames = new ArrayList(),
          showNames = new ArrayList();

      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();
      pstmt = conn.prepareStatement(SQL_SELECT_LEADER_USER);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        Integer userID = new Integer(rs.getInt(1));
        String userName = rs.getString(2);
        String showName = rs.getString(3);

        userIDs.add(userID);
        userNames.add(userName);
        showNames.add(showName);
      }

      req.setAttribute("userIDs", userIDs);
      req.setAttribute("userNames", userNames);
      req.setAttribute("showNames", showNames);

      rs.close();
      rs = null;
      pstmt.close();
      pstmt = null;

      //get audit state
      Map unpassedUsers = new HashMap();
      pstmt = conn.prepareStatement(SQL_SELECT_AUDIT_UNPASS_USER);
      pstmt.setString(1, taskID);
      pstmt.setInt(2, taskTimeID.intValue());
      rs = pstmt.executeQuery();
      while (rs.next()) {
        Integer userID = new Integer(rs.getInt(1));
        java.sql.Timestamp timestamp = rs.getTimestamp(2);
        Date dateAudit = new Date(timestamp.getTime());
        unpassedUsers.put(userID, dateAudit);
      }
      req.setAttribute("unpassedUsers", unpassedUsers);

      go2UrlWithAttibute(req, res, "/jsp/analyseManager/auditUser.jsp");
    }
    catch (Exception e) {
      throw new Warning(e);
    }
    finally {
      SQLUtil.close(rs, pstmt, conn);
      HibernateUtil.close(session);
    }
  }

  private void doAuditUser(HttpServletRequest req, HttpServletResponse res) throws
      Warning {
    final String SQL_DELETE_LEADER_USER =
        "delete from Ytapl_AuditUser where USERID=? and TASKID=? and TASKTIMEID=?;";

    final String SQL_INSERT_LEADER_USER =
        "insert into Ytapl_AuditUser VALUES(?,?,?,?,?,?);";

    //判断权限
    if (!hasPrivilege(req, SetOfPrivileges.PRIVILEGE_AUDIT_USER)) {
      return;
    }

    Task task = (Task) req.getAttribute("task");
    String taskID = task.id();
    String taskTime = req.getParameter("taskTimeID");
    String userIDs[] = req.getParameterValues("userID");
    boolean passed = "1".equals(req.getParameter("passState"));

    Session session = null;
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      session = HibernateUtil.getSessionFactory().openSession();
      conn = session.connection();

      //delete from audit table
      pstmt = conn.prepareStatement(SQL_DELETE_LEADER_USER);
      for (int i = 0; i < userIDs.length; i++) {
        pstmt.setInt(1, Integer.parseInt(userIDs[i]));
        pstmt.setString(2, taskID);
        pstmt.setInt(3, Integer.parseInt(taskTime));

        pstmt.addBatch();
      }
      pstmt.executeBatch();

      pstmt.close();
      pstmt = null;

      //insert into audit table
      if (!passed) {
        User auditor = getLoginUser(req);
        Date dateNow = new Date();
        java.sql.Timestamp timestampNow = new java.sql.Timestamp(dateNow.
            getTime());
        pstmt = conn.prepareStatement(SQL_INSERT_LEADER_USER);
        for (int i = 0; i < userIDs.length; i++) {
          pstmt.setInt(1, Integer.parseInt(userIDs[i]));
          pstmt.setString(2, taskID);
          pstmt.setInt(3, Integer.parseInt(taskTime));
          pstmt.setTimestamp(4, timestampNow);
          pstmt.setInt(5, 0);
          pstmt.setInt(6, auditor.getUserID().intValue());
          pstmt.addBatch();
        }
        pstmt.executeBatch();
      }

    }
    catch (Exception e) {
      throw new Warning(e);
    }
    finally {
      SQLUtil.close(null, pstmt, conn);
      HibernateUtil.close(session);
    }

    auditUser(req, res);
  }
}