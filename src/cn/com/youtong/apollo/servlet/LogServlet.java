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
 * ��־����servlet
 */
public class LogServlet
    extends RootServlet {
  /**
   * ҳ�泣�� -- �¼��б�ҳ��
   */
  public static final String EVENT_PAGE =
      "/jsp/logManager/event.jsp";

  public static final String SECURITY_EVENT_PAGE =
      "/jsp/logManager/securityevent.jsp";

  /**
   * ҳ�泣�� -- �¼���ѯҳ��
   */
  public static final String QUERY_EVENT_PAGE =
      "/jsp/logManager/queryEvent.jsp";

  /**
   * �������ͳ��� -- ��ʾ��ȫ��־ҳ��
   */
  public static final String SHOW_SECURITY_LOG_PAGE = "showSecurityLogPage";

  /**
   * �������ͳ��� -- ��ʾ������־ҳ��
   */
  public static final String SHOW_DATA_LOG_PAGE = "showDataLogPage";

  /**
   * �������ͳ��� -- ��ʾ�¼���ѯҳ��
   */
  public static final String SHOW_QUERY_EVENT_PAGE = "showQueryEventPage";

  /**
   * �������ͳ��� -- ��ѯ�¼�
   */
  public static final String QUERY_EVENT = "queryEvent";

  /**
   * ��ʾ��ȫ��־ҳ��
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

    //��ǰ�ǵڼ�ҳ
    int curPage = 1;
    if (request.getParameter("curPage") != null) {
      try{
        curPage = Integer.parseInt(request.getParameter("curPage"));
      }
      catch(Exception ex){}
    }
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����

    Collection eventCol = Convertor.collection(logManager.getSecurityEvents((curPage-1)*rowsPerPage+1,
        rowsPerPage));

    //��ʾҳ��
    int maxRowCount =logManager.getSecurityEventsCount(); //һ���ж�����

    int maxPage = 1; //һ���ж���ҳ
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
   * ��ʾ������־ҳ��
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
    //��ǰ�ǵڼ�ҳ
    String curPage = "1";
    if (request.getParameter("curPage") != null) {
      curPage = request.getParameter("curPage");
    }
    //��ʾҳ��
    int maxRowCount = eventCol.size(); //һ���ж�����
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����
    int maxPage = 1; //һ���ж���ҳ
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
   * ��ʾ�¼���ѯҳ��
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
   * ��ѯ�¼�
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
    //��ǰ�ǵڼ�ҳ
    String curPage = "1";
    if (request.getParameter("curPage") != null) {
      curPage = request.getParameter("curPage");
    }
    //��ʾҳ��
    int maxRowCount = eventCol.size(); //һ���ж�����
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����
    int maxPage = 1; //һ���ж���ҳ
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
   * ��rquest�����еõ��¼���ѯ����
   * @param request
   * @return �¼���ѯ����
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
   * ҳ��ַ�
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

    //Ȩ���ж�
    if (!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK)) {
      throw new Warning("��û��ִ�иò�����Ȩ��");
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
      throw new Warning("��Ч�Ĳ���operation = " + operation);
    }
  }
}
