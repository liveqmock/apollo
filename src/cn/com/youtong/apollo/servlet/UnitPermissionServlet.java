package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.servlet.unittree.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;

import cn.com.youtong.apollo.services.*;
import org.apache.fulcrum.factory.FactoryException;

public class UnitPermissionServlet extends RootServlet
{
	/*页面路径常数定义*/
	public static final String UNIT_INFO_PAGE = "/jsp/userManager/unitIndex.jsp";
	public static final String SHOW_UNIT_ASSIGNMENTINFO_PAGE = "/jsp/userManager/unit/showAssignGroupUnit.jsp";
	public static final String CHANGE_UNIT_PAGE = "/jsp/userManager/unit/changeUnit.jsp";
	public static final String INIT_PERMISSION_PAGE = "/jsp/userManager/unit/initPermission.jsp";

	public static final String UNIT_FRAME_PAGE = "/jsp/userManager/unitFrame.jsp";

	/**
	 * 请求类型常量
	 */
	public static final String SHOW_UNIT_INFO = "showUnitInfo";
	public static final String SHOW_UNIT_ASSIGNMENTINFO = "showUnitAssignmentinfoPage";
	public static final String UNIT_ASSIGN = "unitAssign";
	public static final String CHANGE_PERMISSION = "changePermission";
	public static final String DELETE_UNIT_PERMISSION = "deleteUnitPermission";
	public static final String USER_TREE = "userTree";
	public static final String CHANGE_TASK = "changeTask";
	public static final String CHANGE_UNIT = "changeUnit";
	public static final String UNIT_ASSIGN_PERMISSION = "unitAssignPermission";
	public static final String SHOW_TREE = "showTree";
	public static final String SHOW_INIT_PERMISSION = "showInitPermissionPage";
	public static final String DO_INIT = "initPermission";

	public static final String DISPLLAY_GROUP_RIGHT = "displayGroupRight";
	public static final String DELETE_GROUP_RIGHT = "deleteGroupRight";
	public static final String ENDUE_RIGHT = "endueRight";
	public static final String CHANGE_RIGHT = "changeRight";
	/**
	 * 根据接收的参数选择执行的方法
	 * @param req  HttpServletRequest
	 * @param res  HttpServletResponse
	 * @throws ServletException  异常
	 * @throws IOException       异常
	 * @throws Warning           异常
	 */
	public void perform(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//权限判断
		if(!hasPrivilege(req, SetOfPrivileges.MANAGE_USER))
		{
			throw new Warning("您没有执行该操作的权限");
		}
		if(req.getParameter("operation") != null)
		{
			if(req.getParameter("operation").equals(SHOW_UNIT_INFO))
			{ //显示单位列表
				showUnitInfoPage(req, res);
				return;
			}
			if(req.getParameter("operation").equals(DISPLLAY_GROUP_RIGHT))
			{ //显示组权限
				displayGroupRight(req, res);
				return;
			}
			if(req.getParameter("operation").equals(DELETE_GROUP_RIGHT))
			{ //删除组权限
				deleteGroupRight(req, res);
				return;
			}
			if(req.getParameter("operation").equals(ENDUE_RIGHT))
			{ //删除组权限
				endueRight(req, res);
				return;
			}
			if(req.getParameter("operation").equals(CHANGE_RIGHT))
			{ //删除组权限
				changeRight(req, res);
				return;
			}

			if(req.getParameter("operation").equals(SHOW_UNIT_ASSIGNMENTINFO))
			{ //显示组列表
				showUnitAssignmentInfoPage(req, res);
				return;
			}
			if(req.getParameter("operation").equals(UNIT_ASSIGN))
			{ //单位分配处理
				unitAssign(req, res);
				return;
			}

			if(req.getParameter("operation").equals(DELETE_UNIT_PERMISSION))
			{ //删除单位权限分配
				deleteUnitPermission(req, res);
				return;
			}

			if(req.getParameter("operation").equals(CHANGE_UNIT))
			{ //删除单位权限分配
				changeUnit(req, res);
				return;
			}
			if(req.getParameter("operation").equals(UNIT_ASSIGN_PERMISSION))
			{ //删除单位权限分配
				unitAssignPermission(req, res);
				return;
			}
			else if(req.getParameter("operation").equals(SHOW_INIT_PERMISSION))
			{ //删除单位权限分配
				showInitPermission(req, res);
				return;
			}
			else if(req.getParameter("operation").equals(DO_INIT))
			{ //删除单位权限分配
				initPermission(req, res);
				return;
			}
			else if(req.getParameter("operation").equals(SHOW_TREE))
			{
				showTree(req, res);
				return;
			}

		}
	}

	/**
	 * 显示初始化权限页面
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showInitPermission(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		this.go2UrlWithAttibute(req, res, INIT_PERMISSION_PAGE);
	}

	/**
	 * 显示初始化权限页面
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void initPermission(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到上传的脚本xml文档内容
		UploadBean upload = new UploadBean(getServletConfig(), req, res);
		InputStream in = upload.getXmlInputStreamUploaded();

		String taskID = (String) req.getSession().getAttribute("taskID");
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		unitPermissionManager.initFromFile(in);
		req.setAttribute("succeed", "true");

		this.go2UrlWithAttibute(req, res, INIT_PERMISSION_PAGE);
	}

	/**
	 * 显示树
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showTree(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{

		//得到对象树
		String taskID = req.getParameter("taskID");
		PermissionTree tree = new PermissionTree(taskID);
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		String unitTree = tree.getUnitTree(modelManager.getUnitTreeManager().getUnitTree(req.getParameter("unitID")), null);

		res.setContentType("text/xml; charset=gb2312");
		res.getWriter().write(unitTree);
	}

	/**
	 * 显示
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void showUnitInfoPage(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
//		UtilServlet.showCheckboxTreeOfRight(req, res, "OfCheckboxTree");
		UtilServlet.showCheckboxTreeOfShow(req, res, "OfCheckboxTree");
		UserManager userMananger = ((UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName())).createUserManager();
		req.setAttribute("groups", userMananger.getAllGroups());
		this.go2UrlWithAttibute(req, res, UNIT_FRAME_PAGE);

	}

	/**
	 *
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void showUnitAssignmentInfoPage(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		String taskID = req.getParameter("taskID");
		String unitCode = req.getParameter("unitCode");
		UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
		UserManager databaseGroup = u.createUserManager();
		Collection colGroup = databaseGroup.getAllGroups();
		req.setAttribute("colGroup", colGroup);
		req.setAttribute("unitCode", unitCode);
		req.setAttribute("taskID", taskID);
		this.go2UrlWithAttibute(req, res, SHOW_UNIT_ASSIGNMENTINFO_PAGE);
	}

	/**
	 *
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void unitAssign(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到相关参数
		String groupName = java.net.URLDecoder.decode(req.getParameter("groupName"), "utf8");
		UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
		UserManager databaseGroup = u.createUserManager();
		Group groupInfo = databaseGroup.getGroupByName(groupName);
		String taskID = req.getParameter("taskID");
		String unitID = req.getParameter("unitID");
		String unitRight[] = req.getParameterValues("unitRight");
		UnitPermission unitPermission = new UnitPermission();

		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		if(!(unitRight == null))
		{
			for(int i = 0; i < unitRight.length; i++)
			{
				unitPermission.setPermission(new Integer(unitRight[i]).intValue(), true);
			}
		}
		//执行分配操作
		unitPermissionManager.setUnitPermission(groupInfo.getGroupID(), unitID, unitPermission);

		//得到相关的组信息
		Collection partGroupPermission = unitPermissionManager.getUnitAssignmentInfo(unitID);

		//得到没有被分配的组
		Collection partGroup = new ArrayList();
		Iterator itrPartGroupPermission = partGroupPermission.iterator();
		while(itrPartGroupPermission.hasNext())
		{
			UnitAssignment rdf = (UnitAssignment) itrPartGroupPermission.next();
			partGroup.add(rdf.getGroup());
		}
		Collection allGroup = databaseGroup.getAllGroups();
		if(!partGroup.isEmpty())
		{
			allGroup.removeAll(partGroup);
		}
		//设置相关参数
		req.setAttribute("group", allGroup);
		req.setAttribute("assignedGroup", partGroupPermission);
		req.setAttribute("unitID", unitID);
		req.setAttribute("taskID", taskID);
		//跳到页面
		this.go2UrlWithAttibute(req, res, CHANGE_UNIT_PAGE);

	}

	/**
	 *
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void unitAssignPermission(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到相关参数
		String groupID = req.getParameter("groupID");
		String taskID = req.getParameter("taskID");
		String unitID = req.getParameter("unitID");
		String unitRightRead = req.getParameter("unitRightRead");
		String unitRightWrite = req.getParameter("unitRightWrite");
		UnitPermission unitPermission = new UnitPermission();
		if(unitRightRead == null)
		{
			unitPermission.setPermission(UnitPermission.UNIT_PERMISSION_READ, false);
		}
		if(!(unitRightRead == null))
		{
			unitPermission.setPermission(UnitPermission.UNIT_PERMISSION_READ, true);
		}

		if(!(unitRightWrite == null))
		{
			unitPermission.setPermission(UnitPermission.UNIT_PERMISSION_WRITE, true);
		}
		if(unitRightWrite == null)
		{
			unitPermission.setPermission(UnitPermission.UNIT_PERMISSION_WRITE, false);
		}

		//执行分配操作
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();
		unitPermissionManager.setUnitPermission(new Integer(groupID), unitID, unitPermission);

		//得到相关的组信息
		Collection partGroupPermission = unitPermissionManager.getUnitAssignmentInfo(unitID);
		UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
		UserManager databaseGroup = u.createUserManager();
		Collection group = new ArrayList();
		//得到没有被分配的组
		Collection partGroup = new ArrayList();
		Iterator itrPartGroupPermission = partGroupPermission.iterator();
		while(itrPartGroupPermission.hasNext())
		{
			UnitAssignment rdf = (UnitAssignment) itrPartGroupPermission.next();
			partGroup.add(rdf.getGroup());
		}
		Collection allGroup = databaseGroup.getAllGroups();
		if(!partGroup.isEmpty())
		{
			allGroup.removeAll(partGroup);
		}
		//设置相关参数
		req.setAttribute("group", allGroup);
		req.setAttribute("assignedGroup", partGroupPermission);
		req.setAttribute("unitID", unitID);
		req.setAttribute("taskID", taskID);
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();

		changeUnit(req, res);
	}

	/**
	 *
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void deleteUnitPermission(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		String groupID = req.getParameter("groupID");
		String taskID = req.getParameter("taskID");
		String unitID = req.getParameter("unitID");

		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		unitPermissionManager.deleteUnitPermission(new Integer(groupID), unitID);
		//得到相关的组信息
		Collection partGroupPermission = unitPermissionManager.getUnitAssignmentInfo(unitID);
		UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
		UserManager databaseGroup = u.createUserManager();
		Collection group = new ArrayList();
		//得到没有被分配的组
		Collection partGroup = new ArrayList();
		Iterator itrPartGroupPermission = partGroupPermission.iterator();
		while(itrPartGroupPermission.hasNext())
		{
			UnitAssignment rdf = (UnitAssignment) itrPartGroupPermission.next();
			partGroup.add(rdf.getGroup());
		}
		Collection allGroup = databaseGroup.getAllGroups();
		if(!partGroup.isEmpty())
		{
			allGroup.removeAll(partGroup);
		}
		//设置相关参数
		req.setAttribute("group", allGroup);
		req.setAttribute("assignedGroup", partGroupPermission);
		req.setAttribute("unitID", unitID);
		req.setAttribute("taskID", taskID);
		//跳到页面
		this.go2UrlWithAttibute(req, res, CHANGE_UNIT_PAGE);

	}

	/**
	 *组信息随着单位的而同而不同
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void changeUnit(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到有关参数
		String unitID = req.getParameter("unitID");
		String taskID = req.getParameter("taskID");

		//得到相关的组信息
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		Collection partGroupPermission = unitPermissionManager.getUnitAssignmentInfo(unitID);
		UserManagerFactory u = (UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName());
		UserManager databaseGroup = u.createUserManager();
		Collection group = new ArrayList();
		//得到没有被分配的组
		Collection partGroup = new ArrayList();
		Iterator itrPartGroupPermission = partGroupPermission.iterator();
		while(itrPartGroupPermission.hasNext())
		{
			UnitAssignment rdf = (UnitAssignment) itrPartGroupPermission.next();
			partGroup.add(rdf.getGroup());
		}
		Collection allGroup = databaseGroup.getAllGroups();
		if(!partGroup.isEmpty())
		{
			allGroup.removeAll(partGroup);
		}
		//设置相关参数
		req.setAttribute("group", allGroup);
		req.setAttribute("assignedGroup", partGroupPermission);
		req.setAttribute("unitID", unitID);
		req.setAttribute("taskID", taskID);
		if(req.getParameter("unitName") != null)
		{
			req.getSession().setAttribute("unitName", java.net.URLDecoder.decode(req.getParameter("unitName"), "utf8"));
		}
		if(req.getParameter("groupID") != null)
		{
			req.setAttribute("groupID", req.getParameter("groupID"));
		}

		//跳到页面
		this.go2UrlWithAttibute(req, res, CHANGE_UNIT_PAGE);

	}

	/**
	 *显示组权限
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void displayGroupRight(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到有关参数
		String groupID = req.getParameter("groupID");
		String taskID = (String) req.getSession().getAttribute("taskID");
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		Collection groupPermissions = unitPermissionManager.getUnitAssignmentInfo(new Integer(groupID));
		req.setAttribute("groupPermissions", groupPermissions);
		req.setAttribute("groupID", groupID);
		//跳到页面
		this.showUnitInfoPage(req, res);

	}

	/**
	 *更改组权限
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void changeRight(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到有关参数
		boolean w = false;
		String groupID = req.getParameter("groupID");
		String taskID = (String) req.getSession().getAttribute("taskID");
		String write = req.getParameter("writeFlag");
		String unitID = req.getParameter("unitID");
		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();
		if(write.equals("true"))
		{
			w = true;
		}
		UnitPermission unitPermission = new UnitPermission();
		unitPermission.setPermission(1, true);
		unitPermission.setPermission(2, w);

		//执行分配操作
		unitPermissionManager.setUnitPermission(new Integer(groupID), unitID, unitPermission);

		Collection groupPermissions = unitPermissionManager.getUnitAssignmentInfo(new Integer(groupID));
		req.setAttribute("groupPermissions", groupPermissions);
		req.setAttribute("groupID", groupID);
		//跳到页面
		this.showUnitInfoPage(req, res);

	}

	/**
	 *删除组权限
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void deleteGroupRight(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到有关参数
		String groupID = req.getParameter("groupID");
		String unitID = req.getParameter("unitID");
		String taskID = (String) req.getSession().getAttribute("taskID");

		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();
		unitPermissionManager.deleteUnitPermission(new Integer(groupID), unitID);
		Collection groupPermissions = unitPermissionManager.getUnitAssignmentInfo(new Integer(groupID));
		req.setAttribute("groupID", groupID);
		req.setAttribute("groupPermissions", groupPermissions);
		//跳到页面
		this.showUnitInfoPage(req, res);

	}

	/**
	 *赋权限
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void endueRight(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning
	{
		//得到有关参数
		String groupID = req.getParameter("groupID");
		String[] unitIDs = req.getParameterValues("unitArray");
		String taskID = (String) req.getSession().getAttribute("taskID");

		ModelManager modelManager = ((ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();

		UnitPermission unitPermission = new UnitPermission();
		if(req.getParameter("rightFlag").equals("1"))
		{
			unitPermission.setPermission(1, true);
			unitPermission.setPermission(2, false);
		}
		else
		{
			unitPermission.setPermission(1, true);
			unitPermission.setPermission(2, true);
		}

		//执行分配操作
		for(int i = 0; i < unitIDs.length; i++)
		{
			unitPermissionManager.setUnitPermission(new Integer(groupID), unitIDs[i], unitPermission);
		}

		Collection groupPermissions = unitPermissionManager.getUnitAssignmentInfo(new Integer(groupID));
		req.setAttribute("groupID", groupID);
		req.setAttribute("groupPermissions", groupPermissions);
		//跳到页面
		this.showUnitInfoPage(req, res);

	}

}
