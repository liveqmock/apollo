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
 *  账号管理Servlet
 */
public class SystemOperationServlet
    extends RootServlet
    implements Serializable {

  /*页面路径常数定义*/
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
   * 请求类型常量
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
   * 根据接收的参数选择执行的方法
   * @param req  HttpServletRequest
   * @param res  HttpServletResponse
   * @throws ServletException  异常
   * @throws IOException       异常
   * @throws Warning           异常
   */
  public void perform(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    if (req.getParameter("operation") != null) {

      if (req.getParameter("operation").equals(SHOW_USER_MODIFY_SELF_PAGE)) { //显示修改本人信息页面
        showUserSelfPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_MODIFY_SELF)) { //修改本人信息
        userModifySelf(req, res);
        return;
      }
      //权限判断
      if (!hasPrivilege(req, SetOfPrivileges.MANAGE_USER)) {
        throw new Warning("您没有执行该操作的权限");
      }
      if (req.getParameter("operation").equals(PASS_ALL_CHECKED_USER)) { //批量审核
        passAllCheckedUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(
          DISPLAY_ALL_NOT_PASS_USER)) { //显示所有未开通的用户
        displayAllNotPassUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(UNVALIDATE_USER)) { //停用帐号
        unvalidateUser(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(QUERY_USER_INFO)) { //根据帐号查询用户
        queryUserInfo(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(QUERY_GROUP_INFO)) { //查询组
        queryGroupInfo(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_INFO)) { //用户信息列表
        showUserInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_INFO)) { //角色信息列表
        showRoleInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_INFO)) { //组信息列表
        showGroupInfoPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_INIT_INFO)) { //显示初始化信息页面
        showInitPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(DO_INIT)) {
        doInit(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_MANAGER)) { //显示用户管理的页面
        showUserManagerPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_USER_ADD)) { //显示添加用户的页面
        showUserAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_ADD)) { //添加用户
        userAdd(req, res);
        return;
      }

      else if (req.getParameter("operation").equals(SHOW_USER_MODIFY)) { //显示修改用户的页面
        showUserModifyPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_MODIFY)) { //修改用户
        userModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(USER_DELETE)) { //删除用户
        userDelete(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_ADD)) { //显示添加角色的页面
        showRoleAddPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_ADD)) { //添加角色的处理
        roleAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_ROLE_MODIFY)) { //显示修改角色的页面
        showRoleModifyPage(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_MODIFY)) { //修改角色的处理
        roleModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(ROLE_DELETE)) { //删除角色的处理
        roleDelete(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_ADD)) { //显示添加组的页面
        showGroupAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_ADD)) { //添加组的处理
        groupAdd(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(SHOW_GROUP_MODIFY)) { //显示修改组的页面
        showGroupModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_MODIFY)) { //修改组的处理
        groupModify(req, res);
        return;
      }
      else if (req.getParameter("operation").equals(GROUP_DELETE)) { //删除组的处理
        groupDelete(req, res);
        return;
      }
      //导出用户Excel
      else if (req.getParameter("operation").equals(EXPORT_USER_INFO)) {
          exportUser(req,res);
          return;
      }
      //导出角色Excel
      else if (req.getParameter("operation").equals(EXPORT_ROLE_INFO)) {
           exportRole(req,res);
           return;
      }

      //导出组Excel
      else if (req.getParameter("operation").equals(EXPORT_GROUP_INFO)) {
           exportGroup(req,res);
           return;
      }

    }
    throw new Warning("无效的参数" + req.getParameter("operation"));
  }

  /**
   * 输出用户
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
   * 输出组
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
   * 输出角色
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
   * 初始化
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void doInit(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //得到上传的脚本xml文档内容
    UploadBean upload = new UploadBean(getServletConfig(), req, res);
    InputStream in = upload.getXmlInputStreamUploaded();

    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    userManager.initFromFile(in);
    req.setAttribute("succeed", "true");

    this.go2UrlWithAttibute(req, res, INIT_INFO_PAGE);
  }

  /**
   * 显示初始化信息页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showInitPage(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //显示页面
    this.go2UrlWithAttibute(req, res, INIT_INFO_PAGE);
  }

  /**
   * 停用帐号
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
    //显示页面
    this.showUserInfoPage(req, res);

  }

  /**
   * 批量审核
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
    //显示页面
    this.showUserInfoPage(req, res);

  }

  /**
   * 显示所有未开通用户
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
    //显示页面
    showUserInfoPage(req, res); ;

  }

  /**
   * 根据帐号查询用户信息
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
    //显示页面
    showUserInfoPage(req, res);

  }

  /**
   * 根据ID查询组信息
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
    //显示页面
    showGroupInfoPage(req, res);

  }

  /**
   * 显示修改本人信息页面
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void showUserSelfPage(HttpServletRequest req,
                                HttpServletResponse res) throws Warning,
      IOException, ServletException {
    //得到当前用户信息
    UserManager userManager = null;
    userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    User user = userManager.getUserByID(getLoginUser(req).getUserID());
    req.setAttribute("userInfo", user);
    //得到所有用户的信息
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    //得到在用户的组
    Collection partGroupInfo = userManager.getUserByID(user.getUserID()).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //得到不在用户的所有组
    Collection groupInfo = userManager.getGroupsNotContainUser(user.
        getUserID());
    req.setAttribute("groupInfo", groupInfo);
    //得到所有角色
    Collection allRole = userManager.getAllRoles();
    req.setAttribute("allRole", allRole);
    //取得所有权限
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

    //显示本人信息修改页面
    this.go2UrlWithAttibute(req, res, USER_PAGE);
  }

  /**
   * 修改本人信息
   * @param req
   * @param res
   * @throws Warning
   * @throws IOException
   * @throws ServletException
   */
  private void userModifySelf(HttpServletRequest req, HttpServletResponse res) throws
      Warning, IOException, ServletException {
    //得到新的用户信息
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

    //修改信息
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
    //显示页面
    User user = userManager.getUserByID(getLoginUser(req).getUserID());
    req.setAttribute("userInfo", user);
    //得到所有用户的信息
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    //得到在用户的组
    Collection partGroupInfo = userManager.getUserByID(user.getUserID()).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //得到不在用户的所有组
    Collection groupInfo = userManager.getGroupsNotContainUser(user.
        getUserID());
    req.setAttribute("groupInfo", groupInfo);
    //得到所有角色
    Collection allRole = userManager.getAllRoles();
    req.setAttribute("allRole", allRole);
    //取得所有权限
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
    //取得所有权限
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

    //得到在用户的组
    req.setAttribute("partGroupInfo", new LinkedList());
    //得到不在用户的所有组
    Collection allGroup = databaseUser.getAllGroups();
    req.setAttribute("groupInfo", allGroup);

    Collection allUser = databaseUser.getAllUsers();
    req.setAttribute("allUser", allUser);
    //显示页面
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
    //取得参数
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

    //角色
    Integer roleID = new Integer(req.getParameter("roleID"));
    //得到要给该用户分配的组
    Integer[] groupIDs = new Integer[0];
    String theGroups = req.getParameter("groupIDs");
    if (!theGroups.equals("")) {
      String[] aryGroups = StringUtils.split(theGroups, ",");
      groupIDs = convertStringToInt(aryGroups);
    }
    //创建用户
    User user = userManager.createUser(userName, password, enterpriseName,
                                       lawPersonCode, lawPersonName,
                                       lawPersonPhone, contactPerson,
                                       contactPersonPhone,
                                       contactPersonMobile,
                                       contactPersonAddress, postcode,
                                       email, fax, validated, memo, roleID,
                                       groupIDs);

    //显示页面
    Collection allUser = userManager.getAllUsers();
    req.setAttribute("allUser", allUser);
    go2Url(req, res, "systemOperation?operation=" + SHOW_USER_INFO);
  }

  /**
   * 删除单位信息
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @throws ServletException 异常
   * @throws IOException      异常
   * @throws Warning          异常
   */
  private void userDelete(HttpServletRequest req, HttpServletResponse res) throws
      ServletException, IOException, Warning {
    Integer userID = new Integer(req.getParameter("userID"));
    //不能删除自己
    if (getLoginUser(req).getUserID().equals(userID)) {
      throw new Warning("您不能删除自己");
    }
    UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();

    userManager.deleteUser(userID);
    //显示页面
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
    //得到和ID相对应的用户信息
    User userInfo = databaseUser.getUserByID(new Integer(userID));
    req.setAttribute("userInfo", userInfo);
    //得到在用户的组
    Collection partGroupInfo = databaseUser.getUserByID(new Integer(userID)).
        getGroups();
    req.setAttribute("partGroupInfo", partGroupInfo);
    //得到不在用户的所有组
    Collection groupInfo = databaseUser.getGroupsNotContainUser(new Integer(
        userID));
    req.setAttribute("groupInfo", groupInfo);
    //得到所有角色
    Collection allRole = databaseUser.getAllRoles();
    req.setAttribute("allRole", allRole);
    //取得所有权限
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

    //得到所有用户的信息
    Collection allUser = databaseUser.getAllUsers();
    req.setAttribute("allUser", allUser);
    //显示页面
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
    //得到要给该用户分配的组
    Integer[] groupIDs = new Integer[0];
    String theGroups = req.getParameter("groupIDs");
    if (!theGroups.equals("")) {
      String[] aryGroups = StringUtils.split(theGroups, ",");
      groupIDs = convertStringToInt(aryGroups);
    }
    //修改用户
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
    //显示页面
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
    //当前是第几页
    String curPage = "1";
    if (req.getParameter("curPage") != null) {
      curPage = req.getParameter("curPage");
    }
    //显示页面
    int maxRowCount = allUser.size(); //一共有多少行
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    int maxPage = 1; //一共有多少页
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

    //当前是第几页
    String curPage = "1";
    if (req.getParameter("curPage") != null) {
      curPage = req.getParameter("curPage");
    }
    //显示页面
    int maxRowCount = allGroup.size(); //一共有多少行
    int rowsPerPage = cn.com.youtong.apollo.services.Config.getInt(
        "cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
    int maxPage = 1; //一共有多少页
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
    //显示页面
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
    //显示页面
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
    //显示页面
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
    //得到和ID相对应的角色信息
    Role roleInfo = databaseRole.getRoleByID(new Integer(roleID));
    req.setAttribute("roleInfo", roleInfo);
    req.setAttribute("privilegesInfo", privilegesInfo);
    //得到所有角色的信息
    Collection allRole1 = databaseRole.getAllRoles();
    req.setAttribute("allRole", allRole1);
    //显示页面
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
    //得到有关输入
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
    //显示页面
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
    //显示页面
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

    //得到不在组的用户
    req.setAttribute("partUserInfo", allUser);
    //得到在组的用户
    req.setAttribute("userInfo", new LinkedList());

    //得到所有组的信息
    Collection allGroup = databaseGroup.getAllGroups();
    req.setAttribute("allGroup", allGroup);

    //显示页面
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
    //得到有关输入
    UserManager databaseGroup = ( (UserManagerFactory) Factory.getInstance(
        UserManagerFactory.class.getName())).createUserManager();
    String groupName = req.getParameter("groupName");
    String groupMemo = req.getParameter("groupMemo");
    //得到分配给该组的用户
    Collection colUser = new ArrayList();
    String theUsers = req.getParameter("userIDs");
    Integer[] userIDs = new Integer[0];
    if (!theUsers.equals("")) {
      String[] aryUsers = StringUtils.split(theUsers, ",");
      userIDs = convertStringToInt(aryUsers);
    }
    //创建组
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
    //得到不在组的用户
    Collection parUserInfo = databaseGroup.getUsersNotInGroup(new Integer(
        groupID));
    req.setAttribute("partUserInfo", parUserInfo);
    //得到在组的用户
    Collection userInfo = databaseGroup.getGroupByID(new Integer(groupID)).
        getMembers();
    req.setAttribute("userInfo", userInfo);
    //得到和ID相对应的组信息
    Group groupInfo = databaseGroup.getGroupByID(new Integer(groupID));
    req.setAttribute("groupInfo", groupInfo);
    //得到所有组的信息
    Collection allGroup = databaseGroup.getAllGroups();
    req.setAttribute("allGroup", allGroup);
    //显示页面
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
    //得到分配到该组的用户
    Integer[] userIDs = new Integer[0];
    String theUsers = req.getParameter("userIDs");
    if (!theUsers.equals("")) {
      String[] aryUsers = StringUtils.split(theUsers, ",");
      userIDs = convertStringToInt(aryUsers);
    }
    //更新组
    databaseGroup.updateGroup(new Integer(groupID), groupName, groupMemo,
                              userIDs);
   String curPage = req.getParameter("curPage");
    go2Url(req, res, "systemOperation?operation=" + SHOW_GROUP_INFO+"&curPage="+curPage);
  }

  /**
   * 删除组信息
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   * @throws ServletException 异常
   * @throws IOException      异常
   * @throws Warning          异常
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
