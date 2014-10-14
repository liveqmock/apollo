package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import cn.com.youtong.apollo.common.*;

import cn.com.youtong.apollo.usermanager.*;
import com.chinatec.util.*;

import cn.com.youtong.apollo.services.*;
import org.apache.fulcrum.factory.FactoryException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
/**
 *  �˺Ź���Servlet
 */
public class SystemOperationServlet
    extends RootServlet
    implements Serializable {

  /*ҳ��·����������*/
  public static final String USER_INFO_PAGE =
      "/jsp/userManager/userIndex.jsp";
  public static final String ROLE_INFO_PAGE =
      "/jsp/userManager/roleIndex.jsp";
  public static final String GROUP_INFO_PAGE =
      "/jsp/userManager/groupIndex.jsp";
  public static final String INIT_INFO_PAGE = "/jsp/userManager/initPage.jsp";

  private static final String SHOW_USERMANAGER_PAGE =
      "/jsp/userManager/userManagerFrame.jsp";

  public static final String USER_PAGE =
      "/jsp/userManager/user/user.jsp";

  private static final String ROLE_ADD_PAGE =
      "/jsp/userManager/role/roleAdd.jsp";
  private static final String ROLE_MODIFY_PAGE =
      "/jsp/userManager/role/roleModify.jsp";

  private static final String GROUP_PAGE =
      "/jsp/userManager/group/group.jsp";

  private static final String ROLE_EXPORT_PAGE =
      "/jsp/userManager/role/roleExport.jsp";
  private static final String USER_EXPORT_PAGE =
      "/jsp/userManager/user/userExport.jsp";
  private static final String GROUP_EXPORT_PAGE =
      "/jsp/userManager/group/groupExport.jsp";

  /**
   * �������ͳ���
   */

  public static final String SHOW_USER_INFO = "showUserInfo";
  public static final String SHOW_ROLE_INFO = "showRoleInfo";
  public static final String SHOW_GROUP_INFO = "showGroupInfo";
  public static final String SHOW_INIT_INFO = "showInitInfo";

  public static final String EXPORT_USER_INFO = "exportUserInfo";
  public static final String EXPORT_ROLE_INFO = "exportRoleInfo";
  public static final String EXPORT_GROUP_INFO = "exportGroupInfo";

  public static final String SHOW_USER_MANAGER = "showUserManagerPage";

  public static final String DO_INIT = "doInit";
  public static final String SHOW_USER_ADD = "showUserAdd";
  public static final String USER_ADD = "userAdd";
  public static final String SHOW_USER_MODIFY = "showModifyUser";
  public static final String USER_MODIFY = "userModify";
  public static final String USER_DELETE = "userDelete";

  public static final String SHOW_ROLE_ADD = "showRoleAdd";
  public static final String ROLE_ADD = "roleAdd";
  public static final String SHOW_ROLE_MODIFY = "showRoleModify";
  public static final String ROLE_MODIFY = "roleModify";
  public static final String ROLE_DELETE = "roleDelete";

  public static final String SHOW_GROUP_ADD = "showGroupAdd";
  public static final String GROUP_ADD = "groupAdd";
  public static final String SHOW_GROUP_MODIFY = "showGroupModify";
  public static final String GROUP_MODIFY = "groupModify";
  public static final String GROUP_DELETE = "groupDelete";
  public static final String SHOW_USER_MODIFY_SELF_PAGE =
      "showUserModifySelfPage";
  public static final String USER_MODIFY_SELF = "userModifySelf";
  public static final String QUERY_USER_INFO = "queryUserInfo";
  public static final String QUERY_GROUP_INFO = "queryGroupInfo";
  public static final String PASS_ALL_CHECKED_USER = "passAllCheckedUser";
  public static final String DISPLAY_ALL_NOT_PASS_USER =
      "displayAllNotPassUser";
  public static final String UNVALIDATE_USER = "unvalidateUser";

  /**
   * ���ݽ��յĲ���ѡ��ִ�еķ���
   * @param req  HttpServletRequest
   * @param res  HttpServletResponse
   * @throws ServletException  �쳣
   * @throws IOException       �쳣
   * @throws Warning           �쳣
   */
  public void perform(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    if (req.getParameter("operation") != null) {

      if (req.getParameter("operation").equals(SHOW_USER_MODIFY_SELF_PAGE)) { //��ʾ�޸ı�����Ϣҳ��
        showUserSelfPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_MODIFY_SELF)) { //�޸ı�����Ϣ
        userModifySelf(req, res);
        return;
      }
      //Ȩ���ж�
      if (!hasPrivilege(req, SetOfPrivileges.MANAGE_USER)) {
        throw new Warning("��û��ִ�иò�����Ȩ��");
      }
      if (req.getParameter("operation").equals(PASS_ALL_CHECKED_USER)) { //�������
        passAllCheckedUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(
          DISPLAY_ALL_NOT_PASS_USER)) { //��ʾ����δ��ͨ���û�
        displayAllNotPassUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(UNVALIDATE_USER)) { //ͣ���ʺ�
        unvalidateUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(QUERY_USER_INFO)) { //�����ʺŲ�ѯ�û�
        queryUserInfo(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(QUERY_GROUP_INFO)) { //��ѯ��
        queryGroupInfo(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_INFO)) { //�û���Ϣ�б�
        showUserInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_INFO)) { //��ɫ��Ϣ�б�
        showRoleInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_INFO)) { //����Ϣ�б�
        showGroupInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_INIT_INFO)) { //��ʾ��ʼ����Ϣҳ��
        showInitPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(DO_INIT)) {
        doInit(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_MANAGER)) { //��ʾ�û������ҳ��
        showUserManagerPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_ADD)) { //��ʾ����û���ҳ��
        showUserAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_ADD)) { //����û�
        userAdd(req, res);
        return;
      }

      else if (req.getParameter("operation").equals(SHOW_USER_MODIFY)) { //��ʾ�޸��û���ҳ��
        showUserModifyPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_MODIFY)) { //�޸��û�
        userModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_DELETE)) { //ɾ���û�
        userDelete(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_ADD)) { //��ʾ��ӽ�ɫ��ҳ��
        showRoleAddPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_ADD)) { //��ӽ�ɫ�Ĵ���
        roleAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_MODIFY)) { //��ʾ�޸Ľ�ɫ��ҳ��
        showRoleModifyPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_MODIFY)) { //�޸Ľ�ɫ�Ĵ���
        roleModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_DELETE)) { //ɾ����ɫ�Ĵ���
        roleDelete(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_ADD)) { //��ʾ������ҳ��
        showGroupAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_ADD)) { //�����Ĵ���
        groupAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_MODIFY)) { //��ʾ�޸����ҳ��
        showGroupModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_MODIFY)) { //�޸���Ĵ���
        groupModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_DELETE)) { //ɾ����Ĵ���
        groupDelete(req, res);
        return;
      }
      //�����û�Excel
      else if (req.getParameter("operation").equals(EXPORT_USER_INFO)) {
          exportUser(req,res);
          return;
      }
      //������ɫExcel
      else if (req.getParameter("operation").equals(EXPORT_ROLE_INFO)) {
           exportRole(req,res);
           return;
      }

      //������Excel
      else if (req.getParameter("operation").equals(EXPORT_GROUP_INFO)) {
           exportGroup(req,res);
           return;
      }

    }
    throw new Warning("��Ч�Ĳ���" + req.getParameter("operation"));
  }

  /**
   * ����û�
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void exportUser(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {

    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection allUser = null;
    allUser = databaseUser.getAllUsers();
    req.setAttribute("alluser", allUser);
    go2UrlWithAttibute(req, res, this.USER_EXPORT_PAGE);

  }

  /**
   * �����
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void exportGroup(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {

    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection allGroup = null;
    allGroup = databaseUser.getAllGroups();
    req.setAttribute("allGroup", allGroup);
    go2UrlWithAttibute(req, res, this.GROUP_EXPORT_PAGE);

  }

  /**
   * �����ɫ
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void exportRole(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {

    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection allRole= null;
    allRole = databaseUser.getAllRoles();
    req.setAttribute("allRole", allRole);
    go2UrlWithAttibute(req, res, this.ROLE_EXPORT_PAGE);

  }

  /**
   * ��ʼ��
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void doInit(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //�õ��ϴ��Ľű�xml�ĵ�����
    UploadBean upload = new UploadBean(getServletConfig(), req, res);
    InputStream in = upload.getXmlInputStreamUploaded();

    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    userManager.initFromFile(in);
    req.setAttribute("succeed", "true");

    this.go2UrlWithAttibute(req, res, INIT_INFO_PAGE);
  }

  /**
   * ��ʾ��ʼ����Ϣҳ��
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showInitPage(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //��ʾҳ��
    this.go2UrlWithAttibute(req, res, INIT_INFO_PAGE);
  }

  /**
   * ͣ���ʺ�
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void unvalidateUser(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    String userID = req.getParameter("userID");
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    databaseUser.setUserFlag(new Integer(userID), false);
    //��ʾҳ��
    this.showUserInfoPage(req, res);

  }

  /**
   * �������
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void passAllCheckedUser(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {
    String[] userFlags = req.getParameterValues("validate");
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    databaseUser.batchSetUserFlag(convertStringToInt(userFlags), true);
    //��ʾҳ��
    this.showUserInfoPage(req, res);

  }

  /**
   * ��ʾ����δ��ͨ�û�
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void displayAllNotPassUser(HttpServletRequest req,
                                     HttpServletResponse res) throws Warning,
      IOException, ServletException {
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection users = databaseUser.getUserByFlag(false);
    req.setAttribute("allUser", users);
    //��ʾҳ��
    showUserInfoPage(req, res); ;

  }

  /**
   * �����ʺŲ�ѯ�û���Ϣ
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void queryUserInfo(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    String userName = req.getParameter("userName");
    String enterpriseInfo = req.getParameter("enterpriseInfo");
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection users = databaseUser.getUser(userName, enterpriseInfo);
    req.setAttribute("allUser", users);
    //��ʾҳ��
    showUserInfoPage(req, res);

  }

  /**
   * ����ID��ѯ����Ϣ
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void queryGroupInfo(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    String groupName = req.getParameter("groupName");
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection ListGroup = new ArrayList();
    //List  ListGroup = new ArrayList();
    try {
      ListGroup = databaseUser.queryGroupByName(groupName);
    }
    catch (UserManagerException ex) {
    }
    req.setAttribute("allGroup", ListGroup);
    //��ʾҳ��
    showGroupInfoPage(req, res);

  }

  /**
   * ��ʾ�޸ı�����Ϣҳ��
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUserSelfPage(HttpServletRequest req,
                                HttpServletResponse res) throws Warning,
      IOException, ServletException {
    //�õ���ǰ�û���Ϣ
    UserManager userManager = null;
    userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    User user = userManager.getUserByID(getLoginUser(req).getUserID());
    req.setAttribute("userInfo", user);
    //�õ������û�����Ϣ
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    //�õ����û�����
    Collection partGroupInfo = userManager.getUserByID(user.getUserID()).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //�õ������û���������
    Collection groupInfo = userManager.getGroupsNotContainUser(user.
        getUserID());
    req.setAttribute("groupInfo", groupInfo);
    //�õ����н�ɫ
    Collection allRole = userManager.getAllRoles();
    req.setAttribute("allRole", allRole);
    //ȡ������Ȩ��
    Collection roleRights = new ArrayList();
    Iterator allRoleItr = allRole.iterator();
    while (allRoleItr.hasNext()) {
      Role role = (Role) allRoleItr.next();
      Collection roleRight = new ArrayList();
      roleRight.add(role.getRoleID().toString());
      boolean[] right = role.getPrivileges().toBooleanArray();
      for (int i = 0; i < role.getPrivileges().AVAILABLE_PRIVILEGES_SIZE;
           i++) {
        if (right[i]) {
          roleRight.add("true");
        }
        else {
          roleRight.add("false");
        }
      }
      roleRights.add(roleRight);
    }
    req.setAttribute("roleRights", roleRights);

    //��ʾ������Ϣ�޸�ҳ��
    this.go2UrlWithAttibute(req, res, USER_PAGE);
  }

  /**
   * �޸ı�����Ϣ
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void userModifySelf(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //�õ��µ��û���Ϣ
    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String userID = req.getParameter("userID");
    String userName = req.getParameter("userName");
    String password = req.getParameter("password");
    String enterpriseName = req.getParameter("enterpriseName");
    String lawPersonCode = req.getParameter("lawPersonCode");
    String lawPersonName = req.getParameter("lawPersonName");
    String lawPersonPhone = req.getParameter("lawPersonPhone");
    String contactPerson = req.getParameter("contactPerson");
    String contactPersonPhone = req.getParameter("contactPersonPhone");
    String contactPersonMobile = "";
    if (req.getParameter("contactPersonMobile") != null) {
      contactPersonMobile = req.getParameter("contactPersonMobile");
    }
    String contactPersonAddress = req.getParameter("contactPersonAddress");
    String postcode = req.getParameter("postcode");
    String memo = "";
    if (req.getParameter("memo") != null) {
      memo = req.getParameter("memo");
    }
    String fax = req.getParameter("fax");
    String email = req.getParameter("email");

    //�޸���Ϣ
    if (req.getParameter("modifyPassword").equals("true")) {
      userManager.updateUser(new Integer(userID), userName, password,
                             enterpriseName, lawPersonCode, lawPersonName,
                             lawPersonPhone, contactPerson,
                             contactPersonPhone, contactPersonMobile,
                             contactPersonAddress, postcode, email, fax, true,
                             memo);

    }
    else {

      userManager.updateUser(new Integer(userID), userName,
                             enterpriseName, lawPersonCode, lawPersonName,
                             lawPersonPhone, contactPerson,
                             contactPersonPhone, contactPersonMobile,
                             contactPersonAddress, postcode, email, fax, true,
                             memo);

    }
    //��ʾҳ��
    User user = userManager.getUserByID(getLoginUser(req).getUserID());
    req.setAttribute("userInfo", user);
    //�õ������û�����Ϣ
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    //�õ����û�����
    Collection partGroupInfo = userManager.getUserByID(user.getUserID()).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //�õ������û���������
    Collection groupInfo = userManager.getGroupsNotContainUser(user.
        getUserID());
    req.setAttribute("groupInfo", groupInfo);
    //�õ����н�ɫ
    Collection allRole = userManager.getAllRoles();
    req.setAttribute("allRole", allRole);
    //ȡ������Ȩ��
    Collection roleRights = new ArrayList();
    Iterator allRoleItr = allRole.iterator();
    while (allRoleItr.hasNext()) {
      Role role = (Role) allRoleItr.next();
      Collection roleRight = new ArrayList();
      roleRight.add(role.getRoleID().toString());
      boolean[] right = role.getPrivileges().toBooleanArray();
      for (int i = 0; i < role.getPrivileges().AVAILABLE_PRIVILEGES_SIZE;
           i++) {
        if (right[i]) {
          roleRight.add("true");
        }
        else {
          roleRight.add("false");
        }
      }
      roleRights.add(roleRight);
    }
    req.setAttribute("roleRights", roleRights);

    req.setAttribute("success", "true");
    this.go2UrlWithAttibute(req, res, USER_PAGE);

  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUserAdd(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    //ȡ������Ȩ��
    Collection roleRights = new ArrayList();
    Collection allRole = databaseUser.getAllRoles();
    req.setAttribute("allRole", allRole);
    Iterator allRoleItr = allRole.iterator();
    while (allRoleItr.hasNext()) {
      Role role = (Role) allRoleItr.next();
      Collection roleRight = new ArrayList();
      roleRight.add(role.getRoleID().toString());
      boolean[] right = role.getPrivileges().toBooleanArray();
      for (int i = 0; i < role.getPrivileges().AVAILABLE_PRIVILEGES_SIZE;
           i++) {
        if (right[i]) {
          roleRight.add("true");
        }
        else {
          roleRight.add("false");
        }
      }
      roleRights.add(roleRight);
    }
    req.setAttribute("roleRights", roleRights);

    //�õ����û�����
    req.setAttribute("partGroupInfo", new LinkedList());
    //�õ������û���������
    Collection allGroup = databaseUser.getAllGroups();
    req.setAttribute("groupInfo", allGroup);

    Collection allUser = databaseUser.getAllUsers();
    req.setAttribute("allUser", allUser);
    //��ʾҳ��
    this.go2UrlWithAttibute(req, res, USER_PAGE);

  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void userAdd(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //ȡ�ò���
    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String userName = req.getParameter("userName");
    boolean validated = false;
    if (req.getParameter("validated") != null) {
      validated = true;
    }

    String password = req.getParameter("password");
    String enterpriseName = req.getParameter("enterpriseName");
    String lawPersonCode = req.getParameter("lawPersonCode");
    String lawPersonName = req.getParameter("lawPersonName");
    String lawPersonPhone = req.getParameter("lawPersonPhone");
    String contactPerson = req.getParameter("contactPerson");
    String contactPersonPhone = req.getParameter("contactPersonPhone");
    String contactPersonMobile = "";
    if (req.getParameter("contactPersonMobile") != null) {
      contactPersonMobile = req.getParameter("contactPersonMobile");
    }
    String contactPersonAddress = req.getParameter("contactPersonAddress");
    String postcode = req.getParameter("postcode");
    String memo = "";
    if (req.getParameter("memo") != null) {
      memo = req.getParameter("memo");
    }
    String fax = req.getParameter("fax");
    String email = req.getParameter("email");

    //��ɫ
    Integer roleID = new Integer(req.getParameter("roleID"));
    //�õ�Ҫ�����û��������
    Integer[] groupIDs = new Integer[0];
    String theGroups = req.getParameter("groupIDs");
    if (!theGroups.equals("")) {
      String[] aryGroups = StringUtils.split(theGroups, ",");
      groupIDs = convertStringToInt(aryGroups);
    }
    //�����û�
    User user = userManager.createUser(userName, password, enterpriseName,
                                       lawPersonCode, lawPersonName,
                                       lawPersonPhone, contactPerson,
                                       contactPersonPhone,
                                       contactPersonMobile,
                                       contactPersonAddress, postcode,
                                       email, fax, validated, memo, roleID,
                                       groupIDs);

    //��ʾҳ��
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    go2Url(req, res, "systemOperation?operation=" + SHOW_USER_INFO);
  }

  /**
   * ɾ����λ��Ϣ
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @throws ServletException �쳣
   * @throws IOException      �쳣
   * @throws Warning          �쳣
   */
  private void userDelete(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    Integer userID = new Integer(req.getParameter("userID"));
    //����ɾ���Լ�
    if (getLoginUser(req).getUserID().equals(userID)) {
      throw new Warning("������ɾ���Լ�");
    }
    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    userManager.deleteUser(userID);
    //��ʾҳ��
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    String curPage = req.getParameter("curPage");
    go2Url(req, res,
           "systemOperation?operation=" + SHOW_USER_INFO + "&curPage=" +
           curPage);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUserModifyPage(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {
    String userID = req.getParameter("userID");
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    //�õ���ID���Ӧ���û���Ϣ
    User userInfo = databaseUser.getUserByID(new Integer(userID));
    req.setAttribute("userInfo", userInfo);
    //�õ����û�����
    Collection partGroupInfo = databaseUser.getUserByID(new Integer(userID)).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //�õ������û���������
    Collection groupInfo = databaseUser.getGroupsNotContainUser(new Integer(
        userID));
    req.setAttribute("groupInfo", groupInfo);
    //�õ����н�ɫ
    Collection allRole = databaseUser.getAllRoles();
    req.setAttribute("allRole", allRole);
    //ȡ������Ȩ��
    Collection roleRights = new ArrayList();
    Iterator allRoleItr = allRole.iterator();
    while (allRoleItr.hasNext()) {
      Role role = (Role) allRoleItr.next();
      Collection roleRight = new ArrayList();
      roleRight.add(role.getRoleID().toString());
      boolean[] right = role.getPrivileges().toBooleanArray();
      for (int i = 0; i < role.getPrivileges().AVAILABLE_PRIVILEGES_SIZE;
           i++) {
        if (right[i]) {
          roleRight.add("true");
        }
        else {
          roleRight.add("false");
        }
      }
      roleRights.add(roleRight);
    }
    req.setAttribute("roleRights", roleRights);

    //�õ������û�����Ϣ
    Collection allUser = databaseUser.getAllUsers();
    req.setAttribute("allUser", allUser);
    //��ʾҳ��
    go2UrlWithAttibute(req, res, USER_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void userModify(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String userID = req.getParameter("userID");
    boolean validated = false;
    if (req.getParameter("validated") != null) {
      validated = true;
    }
    String userName = req.getParameter("userName");
    String password = req.getParameter("password");
    String enterpriseName = req.getParameter("enterpriseName");
    String lawPersonCode = req.getParameter("lawPersonCode");
    String lawPersonName = req.getParameter("lawPersonName");
    String lawPersonPhone = req.getParameter("lawPersonPhone");
    String contactPerson = req.getParameter("contactPerson");
    String contactPersonPhone = req.getParameter("contactPersonPhone");
    String contactPersonMobile = "";
    if (req.getParameter("contactPersonMobile") != null) {
      contactPersonMobile = req.getParameter("contactPersonMobile");
    }
    String contactPersonAddress = req.getParameter("contactPersonAddress");
    String postcode = req.getParameter("postcode");
    String memo = "";
    if (req.getParameter("memo") != null) {
      memo = req.getParameter("memo");
    }
    String fax = req.getParameter("fax");
    String email = req.getParameter("email");

    Integer roleID = new Integer(req.getParameter("roleID"));
    //�õ�Ҫ�����û��������
    Integer[] groupIDs = new Integer[0];
    String theGroups = req.getParameter("groupIDs");
    if (!theGroups.equals("")) {
      String[] aryGroups = StringUtils.split(theGroups, ",");
      groupIDs = convertStringToInt(aryGroups);
    }
    //�޸��û�
    if (req.getParameter("modifyPassword").equals("true")) {
      userManager.updateUser(new Integer(userID), userName, password,
                             enterpriseName, lawPersonCode, lawPersonName,
                             lawPersonPhone, contactPerson,
                             contactPersonPhone, contactPersonMobile,
                             contactPersonAddress, postcode, email, fax,
                             validated, memo, roleID, groupIDs);
    }
    else {
      userManager.updateUser(new Integer(userID), userName,
                             enterpriseName, lawPersonCode, lawPersonName,
                             lawPersonPhone, contactPerson,
                             contactPersonPhone, contactPersonMobile,
                             contactPersonAddress, postcode, email, fax,
                             validated, memo, roleID, groupIDs);
    }

    Collection allUser = userManager.getAllUsers();

    String curPage = req.getParameter("curPage");

    req.setAttribute("allUser", allUser);
    //��ʾҳ��
    go2Url(req, res,
           "systemOperation?operation=" + SHOW_USER_INFO + "&curPage=" +
           curPage);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUserInfoPage(HttpServletRequest req,
                                HttpServletResponse res) throws Warning,
      IOException, ServletException {
    UserManager databaseUser = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    Collection allUser = null;
    if (req.getAttribute("allUser") != null) {
      allUser = (Collection) req.getAttribute("allUser");
    }
    else {
      allUser = databaseUser.getAllUsers();
      req.setAttribute("allUser", allUser);
    }
    //��ǰ�ǵڼ�ҳ
    String curPage = "1";
    if (req.getParameter("curPage") != null) {
      curPage = req.getParameter("curPage");
    }
    //��ʾҳ��
    int maxRowCount = allUser.size(); //һ���ж�����
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����
    int maxPage = 1; //һ���ж���ҳ
    if (maxRowCount % rowsPerPage == 0) {
      maxPage = maxRowCount / rowsPerPage;
    }
    else {
      maxPage = maxRowCount / rowsPerPage + 1;
    }

    try {
      int tmpCurPage = Integer.parseInt(curPage);
      if (tmpCurPage > maxPage) {
        curPage = String.valueOf(maxPage);
      }
    }
    catch (Exception ex) {

    }

    req.setAttribute("maxRowCount", String.valueOf(maxRowCount));
    req.setAttribute("curPage", curPage);
    req.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    req.setAttribute("maxPage", String.valueOf(maxPage));
    go2UrlWithAttibute(req, res, USER_INFO_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */

  private void showRoleInfoPage(HttpServletRequest req,
                                HttpServletResponse res) throws Warning,
      IOException, ServletException {

    UserManagerFactory u = null;
    try {
      u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.
          getName());
    }
    catch (FactoryException ex) {
      throw new Warning(ex);
    }
    UserManager databaseRole = u.createUserManager();
    Collection allRole = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole);
    go2UrlWithAttibute(req, res, ROLE_INFO_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */

  private void showGroupInfoPage(HttpServletRequest req,
                                 HttpServletResponse res) throws Warning,
      IOException, ServletException {
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    Collection allGroup = null;
    if (req.getAttribute("allGroup") != null) {
      allGroup = (Collection) req.getAttribute("allGroup");
    }
    else {
      allGroup = databaseGroup.getAllGroups();
      req.setAttribute("allGroup", allGroup);
    }

    //��ǰ�ǵڼ�ҳ
    String curPage = "1";
    if (req.getParameter("curPage") != null) {
      curPage = req.getParameter("curPage");
    }
    //��ʾҳ��
    int maxRowCount = allGroup.size(); //һ���ж�����
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����
    int maxPage = 1; //һ���ж���ҳ
    if (maxRowCount % rowsPerPage == 0) {
      maxPage = maxRowCount / rowsPerPage;
    }
    else {
      maxPage = maxRowCount / rowsPerPage + 1;
    }
    req.setAttribute("maxRowCount", String.valueOf(maxRowCount));
    req.setAttribute("curPage", curPage);
    req.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
    req.setAttribute("maxPage", String.valueOf(maxPage));

    go2UrlWithAttibute(req, res, GROUP_INFO_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */

  private void showUserManagerPage(HttpServletRequest req,
                                   HttpServletResponse res) throws Warning,
      IOException, ServletException {
    //��ʾҳ��
    go2UrlWithAttibute(req, res, SHOW_USERMANAGER_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */

  private void showRoleAddPage(HttpServletRequest req,
                               HttpServletResponse res) throws Warning,
      IOException, ServletException {
    UserManager databaseRole = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    SetOfPrivileges privileges = new SetOfPrivileges();
    req.setAttribute("privileges", privileges);

    Collection allRole = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole);
    //��ʾҳ��
    go2UrlWithAttibute(req, res, ROLE_ADD_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void roleAdd(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager databaseRole = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    String roleName = req.getParameter("roleName");
    String roleMemo = req.getParameter("roleMemo");
    String[] thePrivileges = req.getParameterValues("privileges");
    if (!thePrivileges.equals("")) {
      Integer[] intPrivileges = convertStringToInt(thePrivileges);
      SetOfPrivileges privileges = new SetOfPrivileges();
      for (int i = 0; i < intPrivileges.length; i++) {
        privileges.setPrivilege(intPrivileges[i].intValue(), true);
      }
      databaseRole.createRole(roleName, roleMemo, privileges);
    }
    Collection allRole = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole);
    //��ʾҳ��
    go2Url(req, res, "systemOperation?operation=" + SHOW_ROLE_INFO);

  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showRoleModifyPage(HttpServletRequest req,
                                  HttpServletResponse res) throws Warning,
      IOException, ServletException {
    boolean right;
    String roleID = req.getParameter("roleID");
    UserManager databaseRole = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    SetOfPrivileges privilegesInfo = databaseRole.getRoleByID(new Integer(
        roleID)).getPrivileges();
    //�õ���ID���Ӧ�Ľ�ɫ��Ϣ
    Role roleInfo = databaseRole.getRoleByID(new Integer(roleID));
    req.setAttribute("roleInfo", roleInfo);
    req.setAttribute("privilegesInfo", privilegesInfo);
    //�õ����н�ɫ����Ϣ
    Collection allRole1 = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole1);
    //��ʾҳ��
    go2UrlWithAttibute(req, res, ROLE_MODIFY_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void roleModify(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager databaseRole = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    //�õ��й�����
    String roleID = req.getParameter("roleID");
    String roleName = req.getParameter("roleName");
    String roleMemo = req.getParameter("roleMemo");
    String[] thePrivileges = req.getParameterValues("privileges");
    SetOfPrivileges privileges = new SetOfPrivileges();
    if (!thePrivileges.equals("")) {
      Integer[] intPrivileges = convertStringToInt(thePrivileges);
      for (int i = 0; i < intPrivileges.length; i++) {

        privileges.setPrivilege(intPrivileges[i].intValue(), true);
      }
      databaseRole.updateRole(new Integer(roleID), roleName, roleMemo,
                              privileges);
    }

    Collection allRole = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole);
    //��ʾҳ��
    go2Url(req, res, "systemOperation?operation=" + SHOW_ROLE_INFO);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void roleDelete(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    Integer deleteRole = new Integer(req.getParameter("roleID"));
    UserManager databaseRole = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    databaseRole.deleteRole(deleteRole);
    Collection allRole = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole);
    //��ʾҳ��
    go2Url(req, res, "systemOperation?operation=" + SHOW_ROLE_INFO);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showGroupAdd(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    Collection allUser = databaseGroup.getAllUsers();

    //�õ���������û�
    req.setAttribute("partUserInfo", allUser);
    //�õ�������û�
    req.setAttribute("userInfo", new LinkedList());

    //�õ����������Ϣ
    Collection allGroup = databaseGroup.getAllGroups();
    req.setAttribute("allGroup", allGroup);

    //��ʾҳ��
    go2UrlWithAttibute(req, res, GROUP_PAGE);

  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void groupAdd(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //�õ��й�����
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String groupName = req.getParameter("groupName");
    String groupMemo = req.getParameter("groupMemo");
    //�õ������������û�
    Collection colUser = new ArrayList();
    String theUsers = req.getParameter("userIDs");
    Integer[] userIDs = new Integer[0];
    if (!theUsers.equals("")) {
      String[] aryUsers = StringUtils.split(theUsers, ",");
      userIDs = convertStringToInt(aryUsers);
    }
    //������
    Group group = databaseGroup.createGroup(groupName, groupMemo, userIDs);

    go2Url(req, res, "systemOperation?operation=" + SHOW_GROUP_INFO);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showGroupModify(HttpServletRequest req,
                               HttpServletResponse res) throws Warning,
      IOException, ServletException {
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String groupID = req.getParameter("groupID");
    //�õ���������û�
    Collection parUserInfo = databaseGroup.getUsersNotInGroup(new Integer(
        groupID));
    req.setAttribute("partUserInfo", parUserInfo);
    //�õ�������û�
    Collection userInfo = databaseGroup.getGroupByID(new Integer(groupID)).
        getMembers();
    req.setAttribute("userInfo", userInfo);
    //�õ���ID���Ӧ������Ϣ
    Group groupInfo = databaseGroup.getGroupByID(new Integer(groupID));
    req.setAttribute("groupInfo", groupInfo);
    //�õ����������Ϣ
    Collection allGroup = databaseGroup.getAllGroups();
    req.setAttribute("allGroup", allGroup);
    //��ʾҳ��
    go2UrlWithAttibute(req, res, GROUP_PAGE);
  }

  /**
   *
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void groupModify(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String groupID = req.getParameter("groupID");
    String groupName = req.getParameter("groupName");
    String groupMemo = req.getParameter("groupMemo");
    //�õ����䵽������û�
    Integer[] userIDs = new Integer[0];
    String theUsers = req.getParameter("userIDs");
    if (!theUsers.equals("")) {
      String[] aryUsers = StringUtils.split(theUsers, ",");
      userIDs = convertStringToInt(aryUsers);
    }
    //������
    databaseGroup.updateGroup(new Integer(groupID), groupName, groupMemo,
                              userIDs);
   String curPage = req.getParameter("curPage");
    go2Url(req, res, "systemOperation?operation=" + SHOW_GROUP_INFO+"&curPage="+curPage);
  }

  /**
   * ɾ������Ϣ
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @throws ServletException �쳣
   * @throws IOException      �쳣
   * @throws Warning          �쳣
   */
  private void groupDelete(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    String groupID = req.getParameter("groupID");
    Integer deleteGroup = new Integer(groupID);

    UserManager databaseGroup = ( (UserManagerFactory) Factory.
                                 getInstance(UserManagerFactory.class.
                                             getName())).createUserManager();
    databaseGroup.deleteGroup(deleteGroup);
    String curPage = req.getParameter("curPage");
    go2Url(req, res, "systemOperation?operation=" + SHOW_GROUP_INFO+"&curPage="+curPage);
  }

  private Integer[] convertStringToInt(String[] strIDs) {
    Integer[] result = new Integer[strIDs.length];
    for (int i = 0; i < strIDs.length; i++) {
      result[i] = new Integer(strIDs[i]);
    }
    return result;
  }

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }

  private void readObject(ObjectInputStream ois) throws
      ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
}
