package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.log.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * 日志管理servlet
 */
public class LogServlet
    extends RootServlet {
  /**
   * 页面常数 -- 事件列表页面
   */
  public static final String EVENT_PAGE =
      "/jsp/logManager/event.jsp";

  public static final String SECURITY_EVENT_PAGE =
      "/jsp/logManager/securityevent.jsp";

  /**
   * 页面常数 -- 事件查询页面
   */
  public static final String QUERY_EVENT_PAGE =
      "/jsp/logManager/queryEvent.jsp";

  /**
   * 请求类型常量 -- 显示安全日志页面
   */
  public static final String SHOW_SECURITY_LOG_PAGE = "showSecurityLogPage";

  /**
   * 请求类型常量 -- 显示数据日志页面
   */
  public static final String SHOW_DATA_LOG_PAGE = "showDataLogPage";

  /**
   * 请求类型常量 -- 显示事件查询页面
   */
  public static final String SHOW_QUERY_EVENT_PAGE = "showQueryEventPage";

  /**
   * 请求类型常量 -- 查询事件
   */
  public static final String QUERY_EVENT = "queryEvent";

  /**
   * 显示安全日志页面
   * @param request
   * @param response
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showSecurityLogPage(HttpServletRequest request,
                                   HttpServletResponse response) throws
      Warning, IOException, ServletException {
    LogManager logManager = ( (LogManagerFactory) Factory.
                             getInstance(LogManagerFactory.class.
                                         getName())).createLogManager();

    //当前是第几页
    int curPage = 1;
    if (request.getParameter("curPage") != null) {
      try{
        curPage = Integer.parseInt(request.getParameter("curPage"));
      }
      catch(Exception ex){}
    }
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行

    Collection eventCol = Convertor.collection(logManager.getSecurityEvents((curPage-1)*rowsPerPage+1,
        rowsPerPage));

    //显示页面
    int maxRowCount =logManager.getSecurityEventsCount(); //一共有多少行

    int maxPage = 1; //一共有多少页
    if (maxRowCount % rowsPerPage == 0) {
      maxPage = maxRowCount / rowsPerPage;
    }
    else {
      maxPage = maxRowCount / rowsPerPage + 1;
    }
    request.setAttribute("maxRowCount", String.valueOf(maxRowCount));
    request.setAttribute("curPage", String.valueOf(curPage));
    request.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    request.setAttribute("maxPage", String.valueOf(maxPage));
    request.setAttribute("eventCol", eventCol);
    go2UrlWithAttibute(request, response, SECURITY_EVENT_PAGE);
  }

  /**
   * 显示数据日志页面
   * @param request
   * @param response
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showDataLogPage(HttpServletRequest request,
                               HttpServletResponse response) throws
      Warning, IOException, ServletException {
    LogManager logManager = ( (LogManagerFactory) Factory.
                             getInstance(LogManagerFactory.class.
                                         getName())).createLogManager();

    Collection eventCol = Convertor.collection(logManager.getAllDataEvents());
    //当前是第几页
    String curPage = "1";
    if (request.getParameter("curPage") != null) {
      curPage = request.getParameter("curPage");
    }
    //显示页面
    int maxRowCount = eventCol.size(); //一共有多少行
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    int maxPage = 1; //一共有多少页
    if (maxRowCount % rowsPerPage == 0) {
      maxPage = maxRowCount / rowsPerPage;
    }
    else {
      maxPage = maxRowCount / rowsPerPage + 1;
    }
    request.setAttribute("maxRowCount", String.valueOf(maxRowCount));
    request.setAttribute("curPage", curPage);
    request.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    request.setAttribute("maxPage", String.valueOf(maxPage));
    request.setAttribute("eventCol", eventCol);

    go2UrlWithAttibute(request, response, EVENT_PAGE);
  }

  /**
   * 显示事件查询页面
   * @param request
   * @param response
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showQueryEventPage(HttpServletRequest request,
                                  HttpServletResponse response) throws
      Warning, IOException, ServletException {
    go2UrlWithAttibute(request, response, QUERY_EVENT_PAGE);
  }

  /**
   * 查询事件
   * @param request
   * @param response
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void queryEvent(HttpServletRequest request,
                          HttpServletResponse response) throws
      Warning, IOException, ServletException {

    LogManager logManager = ( (LogManagerFactory) Factory.
                             getInstance(LogManagerFactory.class.
                                         getName())).createLogManager();
    EventQueryCondition condition = null;
    if (request.getParameter("flag") == null) {
      condition = getEventQueryCondition(request);
      request.getSession().setAttribute("condition", condition);
    }
    else {
      condition = (EventQueryCondition) request.getSession().getAttribute(
          "condition");
    }
    Collection eventCol = Convertor.collection(logManager.queryEvent(condition));
    //当前是第几页
    String curPage = "1";
    if (request.getParameter("curPage") != null) {
      curPage = request.getParameter("curPage");
    }
    //显示页面
    int maxRowCount = eventCol.size(); //一共有多少行
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    int maxPage = 1; //一共有多少页
    if (maxRowCount % rowsPerPage == 0) {
      maxPage = maxRowCount / rowsPerPage;
    }
    else {
      maxPage = maxRowCount / rowsPerPage + 1;
    }
    request.setAttribute("maxRowCount", String.valueOf(maxRowCount));
    request.setAttribute("curPage", curPage);
    request.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    request.setAttribute("maxPage", String.valueOf(maxPage));
    request.setAttribute("eventCol", eventCol);

    request.setAttribute("condition", condition);
    go2UrlWithAttibute(request, response, QUERY_EVENT_PAGE);
  }

  /**
   * 从rquest参数中得到事件查询条件
   * @param request
   * @return 事件查询条件
   */
  private EventQueryCondition getEventQueryCondition(HttpServletRequest
      request) {
    int[] types = new int[0];
    Date startTime = null;
    Date endTime = null;
    String userName = null;
    String source = null;
    String memo = null;

    //types
    String[] typeStrs = request.getParameterValues("types");
    if (typeStrs != null) {
      types = new int[typeStrs.length];
      for (int i = 0; i < typeStrs.length; i++) {
        types[i] = Integer.parseInt(typeStrs[i]);
      }
    }

    //startTime
    String startTimeStr = request.getParameter("startTime");
    if (startTimeStr != null && !startTimeStr.trim().equals("")) {
      try {
        startTime = Convertor.string2Date(startTimeStr);
      }
      catch (ParseException ex) {
      }
    }

    //endTime
    String endTimeStr = request.getParameter("endTime");
    if (endTimeStr != null && !endTimeStr.trim().equals("")) {
      try {
        endTime = Convertor.string2Date(endTimeStr);
      }
      catch (ParseException ex1) {
      }
    }

    //userName
    userName = request.getParameter("userName");

    //source
    source = request.getParameter("source");

    //memo
    memo = request.getParameter("memo");

    return new EventQueryCondition(types,
                                   startTime, endTime, source, userName,
                                   memo);
  }

  /**
   * 页面分发
   * @param req
   * @param res
   * @throws cn.com.youtong.apollo.common.Warning
   * @throws java.io.IOException
   * @throws javax.servlet.ServletException
   */
  public void perform(HttpServletRequest request,
                      HttpServletResponse response) throws cn.com.youtong.
      apollo.common.Warning, java.io.IOException,
      javax.servlet.ServletException {
    String operation = request.getParameter("operation");

    //权限判断
    if (!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK)) {
      throw new Warning("您没有执行该操作的权限");
    }

    if (operation != null && operation.equalsIgnoreCase(SHOW_DATA_LOG_PAGE)) {
      showDataLogPage(request, response);
      return;
    }
    if (operation != null &&
        operation.equalsIgnoreCase(SHOW_QUERY_EVENT_PAGE)) {
      showQueryEventPage(request, response);
      return;
    }
    if (operation != null &&
        operation.equalsIgnoreCase(SHOW_SECURITY_LOG_PAGE)) {
      showSecurityLogPage(request, response);
      return;
    }
    if (operation != null && operation.equalsIgnoreCase(QUERY_EVENT)) {
      queryEvent(request, response);
      return;
    }
    else {
      throw new Warning("无效的参数operation = " + operation);
    }
  }
}
