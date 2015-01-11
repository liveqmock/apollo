package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.ModelManager;
import cn.com.youtong.apollo.data.ModelManagerFactory;
import cn.com.youtong.apollo.data.UnitACL;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.data.db.DBUnitTreeNode;
import cn.com.youtong.apollo.init.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.servlet.unittree.AddressInfoTree;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.tools.*;
import cn.com.youtong.apollo.upload.UploaderManager;

public class TaskServlet extends RootServlet
{

	/**
	 * 请求类型常量 -- 根据传递的参数获取封面表所用到的数据字典返回给前台
	 */
	public static final String GET_DICT_STORE = "getDictStore";
	/**
	 * 请求类型常量 -- 创建封面表树形结构
	 */
	public static final String MAKE_UNIT_TREE = "makeUnitTree";
	/**
	 * 请求类型常量 -- 删除任务
	 */
	public static final String DELETE_TASK = "deleteTask";
	/**
	 * 请求类型常量 -- 设置上报参数
	 */
	public static final String UPLOAD_SETUP = "uploadSetup";

	/**
	 * 请求类型常量 -- 初始化系统参数
	 */
	public static final String DO_INIT = "doInit";

	/**
	 * 请求类型常量 -- 发布脚本组
	 */
	public static final String PUBLISH_SCRIPT_SUIT = "publishScriptSuit";

	/**
	 * 请求类型常量 -- 发布脚本组
	 */
	public static final String DELETE_SCRIPT_SUIT = "deleteScriptSuit";

	/**
	 * 请求类型常量 -- 打开任务
	 */
	public static final String OPEN_TASK = "openTask";

	/**
	 * 请求类型常量 -- 显示任务管理页面
	 */
	public static final String SHOW_MANAGE_TASK_PAGE = "showManageTaskPage";

	/**
	 * 请求类型常量 -- 显示脚本管理一页面
	 */
	public static final String SHOW_MANAGER_SCRIPT_PAGE = "showManagerScriptPage";
	/**
	 * 请求类型常量 --  根据任务id得到此任务的所以脚本
	 */
	public static final String ACTIVE_SCRIPTSUITS = "activeScriptSuits";
	/**
	 * 请求类型常量 --  根据脚本组名称得到所有脚本
	 */
	public static final String GET_ALL_SCRIPTS = "getAllScripts";

	/**
	 * 请求类型常量 --  显示管理XSLT页面
	 */
	public static final String SHOW_MANAGE_TASKVIEW_PAGE = "showManageTaskViewPage";

	/**
	 * 请求类型常量 --  发布XSLT
	 */
	public static final String PUBLISH_XSLT = "publishXSLT";

	/**
	 * 请求类型常量 --  显示服务器配置页面
	 */
	public static final String SHOW_CONFIG_PAGE = "showConfigPage";
	/**
	 * 发布任务
	 */
	public static final String PUBLISH_TASK = "publishTask";
	/**
	 * 显示发布任务页面
	 */
	public static final String SHOW_PUBLISHTASK_PAGE = "showPublishTaskPage";

	/**
	 * 任务管理的url
	 */
	public static final String MANAGE_TASK_URL = "task?operation=showManageTaskPage";

	/**
	 * 管理任务页面
	 */
	public static final String MANAGE_TASK_PAGE = "/jsp/taskManager/manageTask.jsp";

	/**
	 * 脚本管理页面
	 */
	public static final String MANAGER_SCRIPT_PAGE = "/jsp/scriptManager/scriptManager.jsp";

	/**
	 * XSLT管理页面
	 */
	public static final String MANAGER_XSLT_PAGE = "/jsp/taskManager/manageTaskView.jsp";
	/**
	 * 服务器浏览页面
	 */
	public static final String DISPLAY_CONFIG_PAGE = "/jsp/config.jsp";
	/**
	 * 任务发布页面
	 */
	public static final String PUBLISH_TASK_PAGE = "/jsp/taskManager/publishTask.jsp";
	/**
	 * 向上级服务器上报数据页面
	 */
	public static final String UPLOAD_DATA_PAGE = "/jsp/modelManager/uploadData.jsp";
	/**
	 * 向上级服务器上报数据页面
	 */
	public static final String UPLOAD_SETUP_PAGE = "/jsp/modelManager/uploadSetup.jsp";
	/**
	   /**
	  * 显示向上级服务器上报数据页面
	  */
	 public static final String SHOW_UPLOADDATA_PAGE = "showUploadDataPage";
	/**
	 * 向上级服务器上报数据
	 */
	public static final String UPLOAD_DATA_TO_SERVER = "uploadData2Server";

	/**
	 * perform   根据接收的参数选择执行的方法
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning 业务异常
	 */
	public void perform(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//权限判断
		if(!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK))
		{
			throw new Warning("您没有执行该操作的权限");
		}
		String operation = request.getParameter("operation");
		//分发请求

		if(operation == null)
		{
			throw new Warning("无效的参数operation = " + operation);
		}
		if(operation.equals(MAKE_UNIT_TREE)){
			this.makeUnitTree(request,response);
			return;
		}
		
		if(operation.equals(GET_DICT_STORE)){
			this.getDictStore(request,response);
			return;
		}
		
		if(operation.equals(SHOW_UPLOADDATA_PAGE))
		{
			this.showUploadDataPage(request, response);
			return;
		}
		if(operation.equals(UPLOAD_SETUP))
		{
			this.uploadSetup(request, response);
			return;
		}

		if(operation.equals(UPLOAD_DATA_TO_SERVER))
		{
			this.uploadData2Server(request, response);
			return;
		}

		if(operation.equals(SHOW_CONFIG_PAGE))
		{
			this.showConfigPage(request, response);
			return;
		}
		if(operation.equals(SHOW_PUBLISHTASK_PAGE))
		{
			this.showPublishTaskPage(request, response);
			return;
		}

		if(operation.equals(this.DELETE_TASK))
		{
			this.deleteTask(request, response);
			return;
		}

		else if(operation.equals(this.PUBLISH_TASK))
		{
			this.publishTask(request, response);
			return;
		}
		else if(operation.equals(this.SHOW_MANAGE_TASK_PAGE))
		{
			this.showManageTaskPage(request, response);
			return;
		}
		else if(operation.equals(DO_INIT))
		{
			doInit(request, response);
			return;
		}
		else if(operation.equals(this.SHOW_MANAGER_SCRIPT_PAGE))
		{
			this.showManagerScriptPage(request, response);
			return;
		}
		else if(operation.equals(this.OPEN_TASK))
		{
			this.openTask(request, response);
			return;
		}
		else if(operation.equals(this.PUBLISH_SCRIPT_SUIT))
		{
			this.publishScriptSuit(request, response);
			return;
		}
		else if(operation.equals(this.DELETE_SCRIPT_SUIT))
		{
			this.deleteScriptSuit(request, response);
			return;
		}
		else if(operation.equals(this.ACTIVE_SCRIPTSUITS))
		{
			this.activeScriptSuits(request, response);
			return;
		}
		else if(operation.equals(this.GET_ALL_SCRIPTS))
		{
			this.getAllScripts(request, response);
			return;
		}
		else if(operation.equals(SHOW_MANAGE_TASKVIEW_PAGE))
		{
			showManageTaskViewPage(request, response);
			return;
		}
		else if(operation.equals(PUBLISH_XSLT))
		{
			publishXSLT(request, response);
			return;
		}
		else
		{
			throw new Warning("无效的参数operation = " + operation);
		}
	}
	/**
	 *{header:'编号',dataIndex:'id',menuDisabled:true},
	 *{header:'性别',dataIndex:'name',menuDisabled:true},
	 *{header:'名称',dataIndex:'descn',menuDisabled:true},
	 *{header:'描述',dataIndex:'date',menuDisabled:true}
	 **/
	private void getDictStore(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException, Warning{
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("utf-8");
		String jsonstr = "{totalCount:20,data:[{id:'1',name:'AA',descn:'AAA',date:'2014-11-11'},{id:'1',name:'BB',descn:'BBB',date:'2014-11-12'}]}";
		response.getWriter().print(jsonstr);
		response.getWriter().flush();	
	}
	private void makeUnitTree(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException, Warning{
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("utf-8");
		String jsonstr = "[{text:'单位1',id:1,pid:1}]";
		String taskID = request.getParameter("taskid");
		//当unitid是空的时候
	    ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
	    AddressInfoTree tree = new AddressInfoTree(taskID);
	    UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(request));
	    Iterator iterator = modelManager.getUnitTreeManager().getUnitForest(unitACL);
	    jsonstr = JSONArray.toJSONString(iterator);
	    System.out.println(jsonstr);
	    while (iterator.hasNext()) {
			DBUnitTreeNode elem = (DBUnitTreeNode) iterator.next();
			System.out.println(elem.getUnitCode()+"-code-----"+elem.getUnitName()+"--name----");
		}
	    String unitTree = tree.getUnitTree(modelManager.getUnitTreeManager().getUnitTree("7777157509"), null);
	    UnitTreeNode utn = modelManager.getUnitTreeManager().getUnitTree("7777157509");
	    System.out.println("=====>"+JSONObject.toJSONString(utn));
	    System.out.println("=====>"+unitTree);
	    
//	    unitTree = tree.getUnitForest(modelManager.getUnitTreeManager().getUnitForest(unitACL), null);	
		response.getWriter().print(jsonstr);
		response.getWriter().flush();	
	}
	/**
	 * 设置上报参数
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void uploadSetup(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//取得参数
		//保存旧值,用于异常回滚
		String oldAddress = "", oldUserName = "", oldPassword = "";
		String uploadType = request.getParameter("uploadType").trim();
		if(request.getParameter("userName") != null)
		{
			String userName = request.getParameter("userName").trim();
			String password = request.getParameter("password").trim();
			String address = "";
			if(uploadType.equals("webservice"))
			{
				address = request.getParameter("wsURL").trim();
				oldAddress = Config.getString("cn.com.youtong.apollo.upload.webservice.url");
			}
			else if(uploadType.equals("email"))
			{
				address = request.getParameter("email").trim();
				oldAddress = Config.getString("cn.com.youtong.apollo.upload.mail.to");
			}
			oldUserName = Config.getString("cn.com.youtong.apollo.upload.user");
			oldPassword = Config.getString("cn.com.youtong.apollo.upload.password");
			try
			{
				setupUploadData(uploadType, address, userName, password);
				SystemProperties.savePropertyFile(Config.getString("webappRoot") + "WEB-INF\\conf\\upload.properties");
			}
			catch(IOException ex)
			{
				setupUploadData(uploadType, oldAddress, oldUserName, oldPassword);
				this.go2ErrorPage("配置文件upload.properties不存在或者只读.", request, response);
			}
			request.setAttribute("successFlag", "true");
		}
		request.setAttribute("uploadType", uploadType);
		this.go2UrlWithAttibute(request, response, this.UPLOAD_SETUP_PAGE);
	}

	/**
	 * 设定上报参数
	 * @param uploadType
	 * @param address
	 * @param userName
	 * @param password
	 */
	private void setupUploadData(String uploadType, String address, String userName, String password)
	{
		if(uploadType.equals("webservice"))
		{
			Config.setProperty("cn.com.youtong.apollo.upload.webservice.url", address);
		}
		else if(uploadType.equals("email"))
		{
			Config.setProperty("cn.com.youtong.apollo.upload.mail.to", address);
		}
		Config.setProperty("cn.com.youtong.apollo.upload.user", userName);
		Config.setProperty("cn.com.youtong.apollo.upload.password", password);
	}

	/**
	 * 向上级服务器上报数据
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void uploadData2Server(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//取得参数
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		String taskID = (String) request.getSession().getAttribute("taskID");
		Task task = taskManager.getTaskByID(taskID);
		String userName = Config.getString("cn.com.youtong.apollo.upload.user");
		String password = Config.getString("cn.com.youtong.apollo.upload.password");
		String unitID = request.getParameter("unitID");
		TaskTime taskTime = task.getTaskTime(new Integer(request.getParameter("taskTimeID")));
		String uploadType = request.getParameter("uploadSelect");
		List unitIDs = new LinkedList();
		unitIDs.add(unitID);
		//上报数据
		UploaderManager uploaderManager = new UploaderManager();
		uploaderManager.upload(uploadType, task, taskTime, unitIDs, userName, password);
		//设置返回参数
		UtilServlet.showRadioTree(request, response, "");
		request.setAttribute("task", task);
		request.setAttribute("successflag", "true");
		//返回页面
		this.go2UrlWithAttibute(request, response, this.UPLOAD_DATA_PAGE);
	}

	/**
	 * 显示向上级服务器上报数据页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showUploadDataPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		String taskID = (String) request.getSession().getAttribute("taskID");

		request.setAttribute("task", taskManager.getTaskByID(taskID));

		UtilServlet.showRadioTree(request, response, "");
		this.go2UrlWithAttibute(request, response, this.UPLOAD_DATA_PAGE);
	}

	/**
	 * 显示发布任务页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showPublishTaskPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		request.setAttribute("taskItr", taskManager.getAllTasks());
		this.go2UrlWithAttibute(request, response, this.PUBLISH_TASK_PAGE);
	}

	/**
	 * 配置参数
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void doInit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//得到上传的脚本xml文档内容
		UploadBean upload = new UploadBean(getServletConfig(), request, response);
		InputStream in = upload.getXmlInputStreamUploaded();

		try
		{
			SystemProperties.setProperties(in);
		}
		catch(InitException ex)
		{
			throw new Warning("初始化失败 ", ex);
		}
		this.showConfigPage(request, response);
	}

	/**
	 * 显示服务器配置页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showConfigPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		go2UrlWithAttibute(request, response, DISPLAY_CONFIG_PAGE);
	}

	/**
	 * 显示发布XSLT页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void publishXSLT(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//得到上传的任务参数文档内容
		UploadBean upload = new UploadBean(getServletConfig(), request, response);
		InputStream xmlInputSteam = upload.getXmlInputStreamUploaded();
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();

		//得到上传的脚本xml文档内容
		Task task = taskManager.publishXSLT(xmlInputSteam);
		xmlInputSteam.close();
		//显示发布成功页面
		this.go2InfoPage("任务 " + task.id() + " 的样式信息发布成功", request, response);
	}

	/**
	 * 显示管理XSLT页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void showManageTaskViewPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		go2UrlWithAttibute(request, response, MANAGER_XSLT_PAGE);
	}

	/**
	 * @J2EE_METHOD  --  activeScriptSuits
	 * 激活任务脚本
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void activeScriptSuits(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		String taskID = request.getParameter("taskID");
		String scriptName = java.net.URLDecoder.decode(request.getParameter("scriptSuitName"), "utf8");
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		taskManager.setActiveScriptSuit(taskID, scriptName);
		request.setAttribute("taskID", taskID);
		if(request.getParameter("flag") != null)
		{
			request.setAttribute("flag", "true");
		}
		request.setAttribute("scriptName", scriptName);
		this.showManagerScriptPage(request, response);
	}

	/**
	 * @J2EE_METHOD  --  getAllScripts
	 * 根据脚本组得到所有脚本
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void getAllScripts(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		String scriptName = java.net.URLDecoder.decode(request.getParameter("scriptName"), "utf8");
		String taskID = request.getParameter("taskID");
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		Task task = taskManager.getTaskByID(taskID);
		request.setAttribute("ScriptSuits", task.getAllScriptSuits());
		request.setAttribute("scriptName", scriptName);

		String scriptContents[] = new String[]
			{
			"", "", "", ""};
		Iterator _scripts = task.getScriptSuit(scriptName).getAllScripts();
		Java2xhtml java2xhtml = new Java2xhtml(null);
		for(int i = 0; _scripts.hasNext(); i++)
		{
			Script script = (Script) _scripts.next();
			int scriptType = script.getType();
			String name = script.getName();
			String content = "";

			if(!Util.isEmptyString(script.getContent()))
			{
				content = java2xhtml.makeHTML(new StringBuffer(script.getContent()), "test");
			}

			switch(scriptType)
			{
				case 0:
				{
					scriptContents[0] += script.getName() + content;
					break;
				}
				case 1:
				{
					scriptContents[1] += script.getName() + content;
					break;
				}
				case 2:
				{
					scriptContents[2] += script.getName() + content;
					break;
				}
				case 3:
				{
					scriptContents[3] += script.getName() + content;
					break;
				}

			}
		}

		request.setAttribute("scriptContents", scriptContents);
		this.showManagerScriptPage(request, response);
	}

	/**
	 * @J2EE_METHOD  --  openTask
	 * 打开任务
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void openTask(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		String taskID = request.getParameter("taskID");
		String taskName = request.getParameter("taskName");
		request.getSession().setAttribute("taskID", taskID);
		request.getSession().setAttribute("taskName", taskName);
		showManageTaskPage(request, response);
	}

	/**
	 * 发布脚本组
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void publishScriptSuit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//得到上传的脚本xml文档内容
		UploadBean upload = new UploadBean(getServletConfig(), request, response);
		String taskID = null;
		try
		{
			taskID = upload.getParameter("taskID");
		}
		catch(Exception ex)
		{
		}
		InputStream scriptContent = upload.getXmlInputStreamUploaded();
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		taskManager.publishScriptSuit(taskID, scriptContent);
		request.setAttribute("taskID", taskID);
		request.setAttribute("flag", "true");
		this.showManagerScriptPage(request, response);
	}

	/**
	 * 删除脚本组
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void deleteScriptSuit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		String taskID = request.getParameter("taskID");
		String scriptSuitName = java.net.URLDecoder.decode(request.getParameter("scriptSuitName"), "utf8");
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		taskManager.deleteScriptSuit(taskID, scriptSuitName);
		request.setAttribute("taskID", taskID);
		if(request.getParameter("flag") != null)
		{
			request.setAttribute("flag", "true");
		}
		request.setAttribute("scriptName", scriptSuitName);
		this.showManagerScriptPage(request, response);

	}

	/**
	 * @J2EE_METHOD  --  showManagerScriptPage
	 * 显示脚本管理页面
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void showManagerScriptPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		String taskID = (String) request.getSession().getAttribute("taskID");
		Task task = taskManager.getTaskByID(taskID);
		if(task.getActiveScriptSuit() != null)
		{
			request.setAttribute("activeScriptSuit", task.getActiveScriptSuit().getName());
		}
		request.setAttribute("ScriptSuits", task.getAllScriptSuits());
		if(request.getParameter("flag") != null)
		{
			request.setAttribute("flag", "true");
		}

		go2UrlWithAttibute(request, response, MANAGER_SCRIPT_PAGE);
	}

	/**
	 * @J2EE_METHOD  --  showManageTaskPage
	 * 显示任务管理页面
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void showManageTaskPage(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		request.setAttribute("taskItr", taskManager.getAllTasks());
		if(request.getParameter("taskFlag") != null)
		{
			request.setAttribute("taskFlag", "true");
		}
		go2UrlWithAttibute(request, response, MANAGE_TASK_PAGE);
	}

	/**
	 * @J2EE_METHOD  --  publishTask
	 * 发布任务
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void publishTask(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		try
		{
			//得到上传的任务参数文档内容
			UploadBean upload = new UploadBean(getServletConfig(), request, response);
			InputStream xmlInputSteam = upload.getXmlInputStreamUploaded();
			TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
			taskManager.publishTask(xmlInputSteam);
			xmlInputSteam.close();
			request.setAttribute("taskFlag", "true");
			//显示任务管理页面
			this.showManageTaskPage(request, response);
		}
		catch(Warning w)
		{
			//设置返回页面，返回到SHOW_MANAGE_TASK_PAGE
			request.setAttribute(RETURN_URL, "task?operation=" + SHOW_MANAGE_TASK_PAGE);
			throw w;
		}
	}

	/**
	 * @J2EE_METHOD  --  deleteTask
	 * 删除任务
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning 业务异常
	 */
	private void deleteTask(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		ServletContext application = getServletContext();
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
		Task task = taskManager.getTaskByID(request.getParameter("taskID"));
		taskManager.deleteTask(task);
		request.setAttribute("taskFlag", "true");
		showManageTaskPage(request, response);
	}
}
