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
	 * �������ͳ��� -- ���ݴ��ݵĲ�����ȡ��������õ��������ֵ䷵�ظ�ǰ̨
	 */
	public static final String GET_DICT_STORE = "getDictStore";
	/**
	 * �������ͳ��� -- ������������νṹ
	 */
	public static final String MAKE_UNIT_TREE = "makeUnitTree";
	/**
	 * �������ͳ��� -- ɾ������
	 */
	public static final String DELETE_TASK = "deleteTask";
	/**
	 * �������ͳ��� -- �����ϱ�����
	 */
	public static final String UPLOAD_SETUP = "uploadSetup";

	/**
	 * �������ͳ��� -- ��ʼ��ϵͳ����
	 */
	public static final String DO_INIT = "doInit";

	/**
	 * �������ͳ��� -- �����ű���
	 */
	public static final String PUBLISH_SCRIPT_SUIT = "publishScriptSuit";

	/**
	 * �������ͳ��� -- �����ű���
	 */
	public static final String DELETE_SCRIPT_SUIT = "deleteScriptSuit";

	/**
	 * �������ͳ��� -- ������
	 */
	public static final String OPEN_TASK = "openTask";

	/**
	 * �������ͳ��� -- ��ʾ�������ҳ��
	 */
	public static final String SHOW_MANAGE_TASK_PAGE = "showManageTaskPage";

	/**
	 * �������ͳ��� -- ��ʾ�ű�����һҳ��
	 */
	public static final String SHOW_MANAGER_SCRIPT_PAGE = "showManagerScriptPage";
	/**
	 * �������ͳ��� --  ��������id�õ�����������Խű�
	 */
	public static final String ACTIVE_SCRIPTSUITS = "activeScriptSuits";
	/**
	 * �������ͳ��� --  ���ݽű������Ƶõ����нű�
	 */
	public static final String GET_ALL_SCRIPTS = "getAllScripts";

	/**
	 * �������ͳ��� --  ��ʾ����XSLTҳ��
	 */
	public static final String SHOW_MANAGE_TASKVIEW_PAGE = "showManageTaskViewPage";

	/**
	 * �������ͳ��� --  ����XSLT
	 */
	public static final String PUBLISH_XSLT = "publishXSLT";

	/**
	 * �������ͳ��� --  ��ʾ����������ҳ��
	 */
	public static final String SHOW_CONFIG_PAGE = "showConfigPage";
	/**
	 * ��������
	 */
	public static final String PUBLISH_TASK = "publishTask";
	/**
	 * ��ʾ��������ҳ��
	 */
	public static final String SHOW_PUBLISHTASK_PAGE = "showPublishTaskPage";

	/**
	 * ��������url
	 */
	public static final String MANAGE_TASK_URL = "task?operation=showManageTaskPage";

	/**
	 * ��������ҳ��
	 */
	public static final String MANAGE_TASK_PAGE = "/jsp/taskManager/manageTask.jsp";

	/**
	 * �ű�����ҳ��
	 */
	public static final String MANAGER_SCRIPT_PAGE = "/jsp/scriptManager/scriptManager.jsp";

	/**
	 * XSLT����ҳ��
	 */
	public static final String MANAGER_XSLT_PAGE = "/jsp/taskManager/manageTaskView.jsp";
	/**
	 * ���������ҳ��
	 */
	public static final String DISPLAY_CONFIG_PAGE = "/jsp/config.jsp";
	/**
	 * ���񷢲�ҳ��
	 */
	public static final String PUBLISH_TASK_PAGE = "/jsp/taskManager/publishTask.jsp";
	/**
	 * ���ϼ��������ϱ�����ҳ��
	 */
	public static final String UPLOAD_DATA_PAGE = "/jsp/modelManager/uploadData.jsp";
	/**
	 * ���ϼ��������ϱ�����ҳ��
	 */
	public static final String UPLOAD_SETUP_PAGE = "/jsp/modelManager/uploadSetup.jsp";
	/**
	   /**
	  * ��ʾ���ϼ��������ϱ�����ҳ��
	  */
	 public static final String SHOW_UPLOADDATA_PAGE = "showUploadDataPage";
	/**
	 * ���ϼ��������ϱ�����
	 */
	public static final String UPLOAD_DATA_TO_SERVER = "uploadData2Server";

	/**
	 * perform   ���ݽ��յĲ���ѡ��ִ�еķ���
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning ҵ���쳣
	 */
	public void perform(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//Ȩ���ж�
		if(!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK))
		{
			throw new Warning("��û��ִ�иò�����Ȩ��");
		}
		String operation = request.getParameter("operation");
		//�ַ�����

		if(operation == null)
		{
			throw new Warning("��Ч�Ĳ���operation = " + operation);
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
			throw new Warning("��Ч�Ĳ���operation = " + operation);
		}
	}
	/**
	 *{header:'���',dataIndex:'id',menuDisabled:true},
	 *{header:'�Ա�',dataIndex:'name',menuDisabled:true},
	 *{header:'����',dataIndex:'descn',menuDisabled:true},
	 *{header:'����',dataIndex:'date',menuDisabled:true}
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
		String jsonstr = "[{text:'��λ1',id:1,pid:1}]";
		String taskID = request.getParameter("taskid");
		//��unitid�ǿյ�ʱ��
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
	 * �����ϱ�����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void uploadSetup(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//ȡ�ò���
		//�����ֵ,�����쳣�ع�
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
				this.go2ErrorPage("�����ļ�upload.properties�����ڻ���ֻ��.", request, response);
			}
			request.setAttribute("successFlag", "true");
		}
		request.setAttribute("uploadType", uploadType);
		this.go2UrlWithAttibute(request, response, this.UPLOAD_SETUP_PAGE);
	}

	/**
	 * �趨�ϱ�����
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
	 * ���ϼ��������ϱ�����
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void uploadData2Server(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//ȡ�ò���
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
		//�ϱ�����
		UploaderManager uploaderManager = new UploaderManager();
		uploaderManager.upload(uploadType, task, taskTime, unitIDs, userName, password);
		//���÷��ز���
		UtilServlet.showRadioTree(request, response, "");
		request.setAttribute("task", task);
		request.setAttribute("successflag", "true");
		//����ҳ��
		this.go2UrlWithAttibute(request, response, this.UPLOAD_DATA_PAGE);
	}

	/**
	 * ��ʾ���ϼ��������ϱ�����ҳ��
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
	 * ��ʾ��������ҳ��
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
	 * ���ò���
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void doInit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//�õ��ϴ��Ľű�xml�ĵ�����
		UploadBean upload = new UploadBean(getServletConfig(), request, response);
		InputStream in = upload.getXmlInputStreamUploaded();

		try
		{
			SystemProperties.setProperties(in);
		}
		catch(InitException ex)
		{
			throw new Warning("��ʼ��ʧ�� ", ex);
		}
		this.showConfigPage(request, response);
	}

	/**
	 * ��ʾ����������ҳ��
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
	 * ��ʾ����XSLTҳ��
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void publishXSLT(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//�õ��ϴ�����������ĵ�����
		UploadBean upload = new UploadBean(getServletConfig(), request, response);
		InputStream xmlInputSteam = upload.getXmlInputStreamUploaded();
		TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();

		//�õ��ϴ��Ľű�xml�ĵ�����
		Task task = taskManager.publishXSLT(xmlInputSteam);
		xmlInputSteam.close();
		//��ʾ�����ɹ�ҳ��
		this.go2InfoPage("���� " + task.id() + " ����ʽ��Ϣ�����ɹ�", request, response);
	}

	/**
	 * ��ʾ����XSLTҳ��
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
	 * ��������ű�
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * ���ݽű���õ����нű�
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * ������
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * �����ű���
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
	 */
	private void publishScriptSuit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		//�õ��ϴ��Ľű�xml�ĵ�����
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
	 * ɾ���ű���
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * ��ʾ�ű�����ҳ��
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * ��ʾ�������ҳ��
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
	 * ��������
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
	 */
	private void publishTask(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException, Warning
	{
		try
		{
			//�õ��ϴ�����������ĵ�����
			UploadBean upload = new UploadBean(getServletConfig(), request, response);
			InputStream xmlInputSteam = upload.getXmlInputStreamUploaded();
			TaskManager taskManager = ((TaskManagerFactory) Factory.getInstance(TaskManagerFactory.class.getName())).createTaskManager();
			taskManager.publishTask(xmlInputSteam);
			xmlInputSteam.close();
			request.setAttribute("taskFlag", "true");
			//��ʾ�������ҳ��
			this.showManageTaskPage(request, response);
		}
		catch(Warning w)
		{
			//���÷���ҳ�棬���ص�SHOW_MANAGE_TASK_PAGE
			request.setAttribute(RETURN_URL, "task?operation=" + SHOW_MANAGE_TASK_PAGE);
			throw w;
		}
	}

	/**
	 * @J2EE_METHOD  --  deleteTask
	 * ɾ������
	 *
	 * @param request
	 * @param response
	 * @throws javax.servlet.ServletException
	 * @throws java.io.IOException
	 * @throws Warning ҵ���쳣
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
