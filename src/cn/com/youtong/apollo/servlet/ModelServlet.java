package cn.com.youtong.apollo.servlet;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.zip.*;
import javax.servlet.*;
import javax.servlet.http.*;

import cn.com.youtong.apollo.address.*;
import cn.com.youtong.apollo.address.db.*;
import cn.com.youtong.apollo.address.db.form.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.log.*;
import cn.com.youtong.apollo.script.*;
import cn.com.youtong.apollo.serialization.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.servlet.unittree.AddressInfoTree;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.DBTaskManager;
import cn.com.youtong.apollo.task.db.DBTaskTime;
import cn.com.youtong.apollo.usermanager.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jspsmart.upload.File;
import com.lowagie.text.*;
import com.lowagie.text.html.*;
import com.lowagie.text.pdf.*;
import cn.com.youtong.apollo.data.help.DataEditHelp;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import net.sf.hibernate.HibernateException;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.*;

import cn.com.youtong.apollo.data.db.DBUnitTreeNode;
import cn.com.youtong.apollo.data.db.DBUtils;
import cn.com.youtong.apollo.analyse.*;
import cn.com.youtong.apollo.common.ParamUtils;
import cn.com.youtong.apollo.common.StringUtil;
import cn.com.youtong.apollo.data.UnitPermissionManager;

import org.apache.struts.upload.MultipartRequestWrapper;
import cn.com.youtong.apollo.data.help.AttachModel;
import cn.com.youtong.apollo.data.help.AttachManager;
import cn.com.youtong.apollo.expand.dao.UnitMetaTableDao;
import cn.com.youtong.apollo.expand.entity.UnitMetaFormInfoEntity;
import cn.com.youtong.apollo.common.sql.NameGenerator;
import cn.com.youtong.apollo.analyse.db.*;
//import cn.com.youtong.apollo.data.ShowReportUtil;

/**
 * 报表数据管理servlet
 */
public class ModelServlet extends RootServlet {

	/**
	 * 页面常数 -- 数据传送框架页面
	 */
	public static final String DATA_EXCHANGE_PAGE = "/jsp/modelManager/dataExchange.jsp";

	/**
	 * 页面常数 -- 数据审核框架页面
	 */
	public static final String DATA_AUDIT_PAGE = "/jsp/modelManager/dataAudit.jsp";

	/**
	 * 页面常数 -- 数据审核结果页面
	 */
	public static final String DATA_AUDITRESULT_PAGE = "/jsp/modelManager/dataAuditResult.jsp";

	/**
	 * 页面常数 -- 报表数据上报页面
	 */
	public static final String UPLOAD_MODEL_PAGE = "/jsp/modelManager/uploadModel.jsp";

	/**
	 * 页面常数 -- 报表数据下载页面
	 */
	public static final String DOWNLOAD_MODEL_PAGE = "/jsp/modelManager/downloadModel.jsp";

	/**
	 * 页面常数 -- 单位管理页面
	 */
	public static final String MANAGE_UNIT_PAGE = "/jsp/modelManager/manageUnit.jsp";

	/**
	 * 页面常数 -- 单位信息页面
	 */
	public static final String UNIT_INFO_PAGE = "/jsp/modelManager/unitInfo.jsp";

	/**
	 * 页面常数 -- 节点检查结果页面
	 */
	public static final String VALIDATE_RESULT_PAGE = "/jsp/modelManager/validateResult.jsp";

	/**
	 * 页面常数 -- 报表数据汇总页面
	 */
	public static final String SUM_PAGE = "/jsp/modelManager/sum.jsp";

	/**
	 * 页面常数 -- 报表数据节点汇总页面
	 */
	public static final String MODELMANAGER_SUMNODE_PAGE = "/jsp/modelManager/sumNode.jsp";

	/**
	 * 页面常数 -- 报表数据节点汇总页面
	 */
	public static final String UNITMETATABLE_MANAGER_PAGE = "/jsp/modelManager/unitMetaTableManager.jsp";
	
	/**
	 * 页面常数 -- 报表数据节点汇总页面
	 */
	public static final String ADDRESS_MANAGER_PAGE = "/jsp/modelManager/addressManager.jsp";

	/**
	 * 页面常数 -- 催报详细信息页面
	 */
	public static final String ADDRESS_INFO_PAGE = "/jsp/modelManager/addressInfo.jsp";
	/**
	 * 页面常数 -- 催报详细信息页面
	 */
	public static final String UNITMETA_INFO_PAGE = "/jsp/modelManager/unitMetaInfo.jsp";

	/**
	 * 页面常数 --
	 */
	public static final String SHOW_DATA_PAGE = "/jsp/modelManager/showModel.jsp";

	/**
	 * 页面常数 --
	 */
	public static final String DATA_MANAGER_PAGE = "/jsp/modelManager/showModelManager.jsp";

	/**
	 * 页面常数 -- 管理选择汇总方案页面
	 */
	public static final String SELECT_SUM_SCHEMA_MANAGE_PAGE = "/jsp/modelManager/selectSumSchemaManage.jsp";

	/**
	 * 页面常数 -- 显示所有页面催报信息
	 */
	public static final String ALL_ADDRESS_INFO_PAGE = "/jsp/modelManager/allAddressInfo.jsp";

	/**
	 * 请求类型常量 -- 显示数据传送页面
	 */
	public static final String SHOW_DATA_EXCHANGE_PAGE = "showDataExchangePage";

	/**
	 * 请求类型常量 -- 审核数据
	 */
	public static final String SHOW_DATA_AUDIT_PAGE = "dataAuditPage";

	/**
	 * 请求类型常量 -- 显示汇总页面
	 */
	public static final String SHOW_SUM_PAGE = "showSumPage";

	/**
	 * 请求类型常量 -- 完全汇总
	 */
	public static final String SHOW_SUMNODE_PAGE = "showSumNodePage";

	/**
	 * 请求类型常量 -- 显示单位信息页面
	 */
	public static final String SHOW_UNIT_INFO_PAGE = "showUnitInfoPage";

	/**
	 * 请求类型常量 -- 显示单位管理页面
	 */
	public static final String SHOW_MANAGE_UNIT_PAGE = "showManageUnitPage";

	/**
	 * 请求类型常量 -- 新增单位
	 */
	public static final String CREATE_UNIT = "createUnit";

	/**
	 * 请求类型常量 -- 更新单位
	 */
	public static final String UPDATE_UNIT = "updateUnit";

	/**
	 * 请求类型常量 -- 删除单位
	 */
	public static final String DELETE_UNIT = "deleteUnit";

	/**
	 * 请求类型常量 -- 上报报表数据
	 */
	public static final String UPLOAD_MODEL = "uploadModel";

	/**
	 * 请求类型常量 -- 显示上报报表数据页面
	 */
	public static final String SHOW_UPLOAD_MODEL_PAGE = "showUploadModelPage";

	/**
	 * 请求类型常量 -- 下载报表数据
	 */
	public static final String DOWNLOAD_MODEL = "downloadModel";

	/**
	 * 请求类型常量 -- 创建选择汇总方案
	 */
	public static final String CREATE_SELECT_SUM_SCHEMA = "createSelectSumSchema";

	/**
	 * 请求类型常量 -- 创建选择汇总方案
	 */
	public static final String DOWNLOAD_ATTACHMENT = "downloadAttachment";

	/**
	 * 请求类型常量 -- 删除选择汇总方案
	 */
	public static final String DELETE_SELECT_SUM_SCHEMA = "deleteSelectSumSchema";

	/**
	 * 请求类型常量 -- 显示选择汇总方案管理页面
	 */
	public static final String SHOW_SELECT_SUM_SCHEMA_MANAGE_PAGE = "showSelectSumSchemaManagePage";

	/**
	 * 请求类型常量 -- 显示下载报表数据页面
	 */
	public static final String SHOW_DOWNLOAD_MODEL_PAGE = "showDownloadModelPage";

	/**
	 * 请求类型常量 -- 完全汇总
	 */
	public static final String SUM_ALL = "sumAll";

	/**
	 * 请求类型常量 -- 调整节点差额表
	 */
	public static final String ADJUST_NODE_DIFF = "adjustNodeDiff";

	/**
	 * 请求类型常量 -- 节点汇总
	 */
	public static final String SUM_NODE = "sumNode";

	/**
	 * 请求类型常量 -- 执行选择汇总方案
	 */
	public static final String EXECUTE_SELECT_SUM_SCHEMA = "executeSelectSumSchema";

	/**
	 * 请求类型常量 -- 检查集团汇总节点单位下的所有节点汇总结果是否正确
	 */
	public static final String VALIDATE_NODE_SUM = "validateNodeSum";
	
	/**
	 * 请求类型常量 -- 显示封面表信息管理页面
	 */
	public static final String SHOW_UNITMETATABLE_INFO_PAGE = "showUnitMetaTableInfoPage";
	
	/**
	 * 请求类型常量 -- 显示催报信息管理页面
	 */
	public static final String SHOW_ADDRESS_INFO_PAGE = "showAddressInfoPage";

	/**
	 * 请求类型常量 -- 添加催报信息
	 */
	public static final String ADD_ADDRESS_INFO = "addAddressInfo";

	/**
	 * 请求类型常量 -- 根据任务id得到此任务的所有信息
	 */
	public static final String GET_ADDRESS_INFO_BYTASKID = "getAddressInfoByTaskID";
	
	/**
	 * 请求类型常量 -- 根据任务id得到此任务的封面表数据信息
	 */
	public static final String GET_UNITMETA_INFO_BYTASKID = "getUnitMetaInfoByTaskID";
	
	
	/**
	 * 请求类型常量 -- 显示数据
	 */
	public static final String SHOW_DATA = "showData";

	/**
	 * 请求类型常量 -- 显示数据
	 */
	public static final String DOWN_EXCEL = "downExcel";

	/**
	 * 请求类型常量 -- 显示数据管理页面
	 */
	public static final String SHOW_DATA_MANAGER = "showDataManager";

	/**
	 * 参数常量 -- 数据
	 */
	public static final String HTML_DATA = "htmlData";

	/**
	 * 请求类型常量 -- 显示数据管理页面
	 */
	public static final String DELETE_ADDRESS_INFO = "deleteAddressInfo";

	/**
	 * 请求类型常量 -- 显示所有催报信息
	 */
	public static final String DISPLAY_ALL_ADDRESS_INFO = "displayAllAddressInfo";

	/**
	 * 请求类型常量 -- 单表打印
	 */
	public static final String PRINT_ONE_TABLE = "printOneTable";

	/**
	 * 请求类型常量 -- 套表打印
	 */
	public static final String PRINT_ALL_TABLE = "printAllTable";

	public static final String PRINT_PDF = "printPdf";

	/**
	 * 请求类型常量 -- 对页面进行审核
	 */
	public static final String DO_DATA_AUDIT = "doDataAudit";

	/**
	 * 请求类型常量 -- 下载EXCEL单张表
	 */
	public static final String GET_EXCEL_FOR_SINGLE_TABLE = "getExcelForSingleTable";

	/**
	 * 请求类型常量 -- 下载EXCEL整套表
	 */
	public static final String GET_EXCEL_FOR_ALL_TABLE = "getExcelForAllTable";

	/**
	 * 请求类型常量 -- 下载PDF单张表
	 */
	public static final String GET_PDF_FOR_SINGLE_TABLE = "getPDFForSingleTable";

	/**
	 * 请求类型常量 -- 下载PDF整套表
	 */
	public static final String GET_PDF_FOR_ALL_TABLE = "getPDFForAllTable";

	//编辑数据
	public static final String EIDT_DATA = "edit_data";

	//编辑数据页面
	public static final String EIDT_DATA_PAGE = "/jsp/modelManager/editModel.jsp";

	//保存数据
	public static final String SAVE_DATA = "save_data";

	public static final String SHOW_UNIT_TREE = "showUnitTree";

	private static final String SHOW_UNIT_TREE_PAGE = "/jsp/modelManager/showUnitTree.jsp";

	public static final String SELECT_SUM = "selectSum";

	private static final String SELECT_SUM_PAGE = "/jsp/modelManager/selectsum.jsp";

	public static final String DO_SELECT_SUM = "doSelectSum";
	
	public static final String MODIFY_UNIT_DISPLAY="modify_unit_display";

	public static final String DO_SELECT_SUM_OK_PAGE = "/jsp/modelManager/selectsum_ok.jsp";

	//封存数据
	public static final String ENVELOPSUBMITDATA = "envelopsubmitdata";

	//启封数据
	public static final String UNENVELOPSUBMITDATA = "unenvelopsubmitdata";

	//打印单位树
	public static final String PRINT_UNITTREE = "print_unittree";

	//
	public static final String PRINT_UNITTREE_PAGE = "/jsp/modelManager/printunittree.jsp";

	//上载附件
	public static final String UPLOAD_ATTACH = "uploadattach";

	//显示附件
	public static final String LIST_ATTACH = "listattach";

	//显示附件
	public static final String SHOW_ATTACH = "showattach";

	//删除附件
	public static final String DELETE_ATTACH = "deleteattach";

	//显示附件页面
	public static final String LIST_ATTACH_PAGE = "/jsp/modelManager/attachlist.jsp";

	//查询组织单元树形结构
	public static final String SHOW_UNITTREE_BY_NAMEORCODE="showUnitTreeByNameOrCode";
	
	//根据UNITID查询封面表中的组织单元
	public static final String LOAD_UNITDATA_BY_UNITID = "loadDataByUnitid";
	
	//根据UNITID保存封面表中的组织单元
	public static final String SAVE_UNITDATA_BY_UNITID = "saveUnitByUnitid";
	
	/**
	 *更新封面表中的组织单元 
	 **/
	private void saveUnitByUnitid(HttpServletRequest request,HttpServletResponse response)
		throws Warning, IOException,ServletException {
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("utf-8");
		String jsonstr = "{failure:true}";
		try {
			String unitid = request.getParameter("unitid");
			String qymc = new String(request.getParameter("qymc").getBytes("iso8859-1"),"utf-8");
			String lxdh = new String(request.getParameter("lxdh").getBytes("iso8859-1"),"utf-8");
			String zbr = new String(request.getParameter("zbr").getBytes("iso8859-1"),"utf-8");
			String qh = new String(request.getParameter("qh").getBytes("iso8859-1"),"utf-8");
			String fj = new String(request.getParameter("fj").getBytes("iso8859-1"),"utf-8");
			
			String jygm = new String(request.getParameter("jygm").getBytes("iso8859-1"),"utf-8");
			String zzxs = new String(request.getParameter("zzxs").getBytes("iso8859-1"),"utf-8");
			String xbys = new String(request.getParameter("xbys").getBytes("iso8859-1"),"utf-8");
			String bblx = new String(request.getParameter("bblx").getBytes("iso8859-1"),"utf-8");
			
			String szdq = new String(request.getParameter("szdq").getBytes("iso8859-1"),"utf-8");
			String sshy = new String(request.getParameter("sshy").getBytes("iso8859-1"),"utf-8");
			
			UnitMetaFormInfoEntity umfie = new UnitMetaFormInfoEntity();
			umfie.setUnitid(unitid);
			umfie.setQymc(qymc);
			umfie.setLxdh(lxdh);
			umfie.setZbr(zbr);
			umfie.setQh(qh);
			umfie.setFj(fj);

			umfie.setJygm(jygm);
			umfie.setZzxs(zzxs);
			umfie.setXbys(xbys);
			umfie.setBblx(bblx);
			
			umfie.setSzdq(szdq);
			umfie.setSshy(sshy);
			
			UnitMetaTableDao umtd = new UnitMetaTableDao();
			umtd.updateUnitMetaForm(umfie);
			jsonstr = "{success:true,data:"+jsonstr+"}";	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		response.getWriter().print(jsonstr);
		response.getWriter().flush();
		
	}
	/**
	 *根据封面表中的组织单元 
	 **/
	private void loadDataByUnitid(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
//		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("utf-8");
		String str = request.getParameter("unitid");
//		System.out.println("str==========>"+str);
		UnitMetaTableDao umtd = new UnitMetaTableDao();
		UnitMetaFormInfoEntity umte = umtd.findUnitMetaByUnitid(str);
		String jsonstr = JSONObject.toJSONString(umte);
//		System.out.println("======>"+jsonstr);
		jsonstr = "{success:true,data:"+jsonstr+"}";
		response.getWriter().print(jsonstr);
		response.getWriter().flush();	
		
	}
	/**
	 * 根据输入的查询条件返回对应的树形列表
	 * @throws HibernateException 
	 **/
	private void showUnitTreeByNameOrCode(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		
		TaskManager taskMng = ( (TaskManagerFactory) Factory.getInstance(
				TaskManagerFactory.class.getName())).createTaskManager();
//		taskMng = new DBTaskManager();
		
//		taskMng.publishTaskTime("NEWQYKB", 2014);
		
		/*Task t = taskMng.getTaskByID("NEWQYKB");
		Iterator i = t.getTaskTimes();
		while (i.hasNext()) {
			DBTaskTime elem = (DBTaskTime) i.next();
			System.out.println(elem.getFromTime()+"-----"+elem.getTaskTimeID());
		}*/
//		request.setAttribute("task", t);
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("utf-8");
//		String name = "冀东发展集团有限责任公司";
		String idinfo = request.getParameter("taskID");
		String name = new String(request.getParameter("name").getBytes("iso8859-1"),"utf-8");
//		System.out.println("nameinfo----->"+nameinfo+"<-----");
//		System.out.println("----->"+aa+"<-----");
//		System.out.println(new String(aa.getBytes("iso8859-1"),"gbk"));
//		System.out.println("查询组织单元树形结构数据信息!");
		String taskID = "NEWQYKB";
//		System.out.println(taskID+"-------");
		ModelManager modelManager = ( (ModelManagerFactory) Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(taskID);
		String unitTree = "";
		UnitTreeManager treeManager =  modelManager.getUnitTreeManager();
		UnitACL unitACL = modelManager.getUnitACL(RootServlet.getLoginUser(request));
		List<DBUnitTreeNode> itrTree = treeManager.getAllUnitTreeNodes(taskID, name,unitACL);
		String str = JSONArray.toJSONString(itrTree);
//		System.out.println("=============");
//		System.out.println(str);
		response.getWriter().print(str);
		response.getWriter().flush();		
	}
	
	
	/**
	 * 显示数据传送页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showDataExchangePage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		go2UrlWithAttibute(request, response, this.DATA_EXCHANGE_PAGE);
	}

	/**
	 * 显示审核页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void dataAuditPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		UtilServlet.showCheckboxTreeOfShow(request, response, "");
		go2UrlWithAttibute(request, response, this.DATA_AUDIT_PAGE);
	}

	/**
	 * 查询所有催报信息
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void displayAllAddressInfo(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String taskID = (String) request.getSession().getAttribute("taskID");
		AddressManager addressManager = ((AddressManagerFactory) Factory
				.getInstance(AddressManagerFactory.class.getName()))
				.createAddressManager();
		Iterator addressInfoItr = addressManager.getAddressInfoByTaskID(taskID);
		request.setAttribute("taskID", taskID);
		Collection addressInfo = new ArrayList();
		while (addressInfoItr.hasNext()) {
			addressInfo.add(addressInfoItr.next());
		}
		request.setAttribute("addressInfo", addressInfo);
		//当前是第几页
		String curPage = "1";
		if (request.getParameter("curPage") != null) {
			curPage = request.getParameter("curPage");
		}
		//显示页面
		int maxRowCount = addressInfo.size(); //一共有多少行
		int rowsPerPage = cn.com.youtong.apollo.services.Config
				.getInt("cn.com.youtong.apollo.webconfig.pageNum"); //每页有多少行
		int maxPage = 1; //一共有多少页
		if (maxRowCount % rowsPerPage == 0) {
			maxPage = maxRowCount / rowsPerPage;
		} else {
			maxPage = maxRowCount / rowsPerPage + 1;
		}
		request.setAttribute("maxRowCount", String.valueOf(maxRowCount));
		request.setAttribute("curPage", curPage);
		request.setAttribute("rowsPerPage", String.valueOf(rowsPerPage));
		request.setAttribute("maxPage", String.valueOf(maxPage));

		go2UrlWithAttibute(request, response, ALL_ADDRESS_INFO_PAGE);
	}

	/**
	 * 删除催报信息
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteAddressInfo(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		String taskID = request.getParameter("taskID");
		String unitName = java.net.URLDecoder.decode(request
				.getParameter("unitName"), "utf8");
		AddressManager addressManager = ((AddressManagerFactory) Factory
				.getInstance(AddressManagerFactory.class.getName()))
				.createAddressManager();
		AddressInfoFormPK pk = new AddressInfoFormPK(taskID, unitID);
		AddressInfoPK InterfacePK = new DBAddressInfoPK(pk);
		addressManager.deleteAddressInfo(InterfacePK);
		request.setAttribute("unitName", unitName);
		request.setAttribute("unitID", unitID);
		request.setAttribute("taskID", taskID);
		go2UrlWithAttibute(request, response, ADDRESS_INFO_PAGE);
	}
	/**
	 * 显示管理封面表信息页面
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showUnitMetaTableInfoPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		request.setAttribute("taskItr", taskManager.getAllTasks());
		UtilServlet.showUnitTree(request, response);
		go2UrlWithAttibute(request, response, UNITMETATABLE_MANAGER_PAGE);
	}
	
	/**
	 * 显示管理催报信息页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showAddressInfoPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		request.setAttribute("taskItr", taskManager.getAllTasks());
		UtilServlet.showUnitTree(request, response);
		go2UrlWithAttibute(request, response, ADDRESS_MANAGER_PAGE);
	}

	/**
	 * 添加催报信息
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addAddressInfo(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String taskID = request.getParameter("taskID");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String fax = request.getParameter("fax");
		String mobile = request.getParameter("mobile");
		String unitID = request.getParameter("unitID");
		String unitName = java.net.URLDecoder.decode(request
				.getParameter("unitName"), "utf8");
		AddressInfoFormPK pk = new AddressInfoFormPK(taskID, unitID);
		AddressInfoForm form = new AddressInfoForm();
		form.setComp_id(pk);
		form.setEmail(email);
		form.setFax(fax);
		form.setMobile(mobile);
		form.setPhone(phone);
		AddressInfo info = new DBAddressInfo(form);
		AddressManager addressManager = ((AddressManagerFactory) Factory
				.getInstance(AddressManagerFactory.class.getName()))
				.createAddressManager();
		AddressInfoPK InterfacePK = new DBAddressInfoPK(pk);
		AddressInfo addressInfo = null;
		try {
			addressInfo = addressManager.findByPK(InterfacePK);
		} catch (AddressException ex) {
		}
		if (addressInfo == null) {
			addressManager.addAddressInfo(info);
		} else {
			addressManager.updateAddressInfo(info);
		}
		addressInfo = addressManager.findByPK(InterfacePK);
		request.setAttribute("addressInfo", addressInfo);
		request.setAttribute("unitName", unitName);
		request.setAttribute("unitID", unitID);
		request.setAttribute("taskID", taskID);
		request.setAttribute("flag", "true");
		go2UrlWithAttibute(request, response, ADDRESS_INFO_PAGE);
	}
	/**
	 * 根据任务ID得到封面表信息
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getUnitMetaInfoByTaskID(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String taskID = request.getParameter("taskID");
		String unitID = request.getParameter("unitID");
		String unitName = java.net.URLDecoder.decode(request
				.getParameter("unitName"), "utf8");
		AddressInfoFormPK pk = new AddressInfoFormPK(taskID, unitID);
		AddressInfoPK InterfacePK = new DBAddressInfoPK(pk);
		AddressManager addressManager = ((AddressManagerFactory) Factory
				.getInstance(AddressManagerFactory.class.getName()))
				.createAddressManager();
		AddressInfo addressInfo = null;
		try {
			addressInfo = addressManager.findByPK(InterfacePK);
		} catch (AddressException ex) {
		}
		request.setAttribute("addressInfo", addressInfo);
		request.setAttribute("taskID", taskID);
		request.setAttribute("unitID", unitID);
		request.setAttribute("unitName", unitName);
		go2UrlWithAttibute(request, response, UNITMETA_INFO_PAGE);
	}
	
	/**
	 * 根据任务ID得到催报信息
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getAddressInfoByTaskID(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String taskID = request.getParameter("taskID");
		String unitID = request.getParameter("unitID");
		String unitName = java.net.URLDecoder.decode(request
				.getParameter("unitName"), "utf8");
		AddressInfoFormPK pk = new AddressInfoFormPK(taskID, unitID);
		AddressInfoPK InterfacePK = new DBAddressInfoPK(pk);
		AddressManager addressManager = ((AddressManagerFactory) Factory
				.getInstance(AddressManagerFactory.class.getName()))
				.createAddressManager();
		AddressInfo addressInfo = null;
		try {
			addressInfo = addressManager.findByPK(InterfacePK);
		} catch (AddressException ex) {
		}
		request.setAttribute("addressInfo", addressInfo);
		request.setAttribute("taskID", taskID);
		request.setAttribute("unitID", unitID);
		request.setAttribute("unitName", unitName);
		go2UrlWithAttibute(request, response, ADDRESS_INFO_PAGE);
	}

	/**
	 * 节点汇总
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void sumNode(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");

		boolean isRecursive = false;
		if (request.getParameter("isRecursive") != null
				&& !request.getParameter("isRecursive").equals("")) {
			isRecursive = true;
		}

		String unitID = request.getParameter("unitID");

		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		modelManager.getSummer().sumNode(unitID, taskTime, isRecursive);

		//记录数据日志
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "对单位“" + unitID + "”的"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "数据执行节点汇总操作";
		if (isRecursive) {
			message += "（包含子节点）";
		}

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		envelopsubmitData(request, response);

		//go2InfoPage("节点汇总成功", "model?operation=" + SHOW_SUM_PAGE, request,
		// response);

	}

	/**
	 * 执行选择汇总方案
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void executeSelectSumSchema(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		Integer schemaID = new Integer(request.getParameter("schemaID"));
		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		Summer summer = modelManager.getSummer();
		SelectSumSchema schema = summer.getSelectSumSchema(schemaID);
		summer.executeSelectSumSchema(schemaID, taskTime, modelManager
				.getUnitACL(getLoginUser(request)));

		//记录数据日志
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "对"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "数据执行选择汇总方案“" + schema.getName() + "”";

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2InfoPage("执行选择汇总方案成功", javascriptReturnUrl(), request, response);
	}

	/**
	 * 检查集团汇总节点单位下的所有节点汇总结果是否正确
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void validateNodeSum(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		boolean isRecursive = false;
		if (request.getParameter("isRecursive") != null
				&& !request.getParameter("isRecursive").equals("")) {
			isRecursive = true;
		}
		Task task = (Task) request.getAttribute("task");
		String unitID = request.getParameter("unitID");

		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());

		Iterator resultItr = modelManager.getSummer().validateNodeSum(unitID,
				taskTime, isRecursive);

		request.setAttribute("resultItr", resultItr);

		go2UrlWithAttibute(request, response, VALIDATE_RESULT_PAGE);
	}

	/**
	 * 显示上报报表数据页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showUploadModelPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		go2UrlWithAttibute(request, response, UPLOAD_MODEL_PAGE);
	}

	/**
	 * 从Zip文件中得到Model输入流
	 * 
	 * @param zipFile
	 *            zip文件对象
	 * @return Model输入流
	 */
	private InputStream getModelInputStream(File zipFile) throws ModelException {
		try {
			ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
			for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis
					.getNextEntry()) {
				if (entry.getName().endsWith(".xml")) {
					return zis;
				}
			}
		} catch (IOException ex) {
			throw new ModelException("提取数据文件失败");
		}
		throw new ModelException("没有找到数据文件");
	}

	/**
	 * 上报报表数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void uploadModel(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		//从上传的报表数据内容中得到taskID
		UploadBean upload = new UploadBean(getServletConfig(), request,
				response);
		File file = upload.getUploadFile();
		InputStream xmlInputSteam = getModelInputStream(file);
		String taskID = TaskIDFinder.findTaskID(xmlInputSteam);
		if (!task.id().equals(taskID)) {
			throw new Warning("上报的数据与当前任务不匹配");
		}
		xmlInputSteam.close();

		xmlInputSteam = getModelInputStream(file);
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(taskID);

		User user = getLoginUser(request);

		//上报数据
		LoadResult loadResult = modelManager.getDataImporter().importData(
				xmlInputSteam, modelManager.getUnitACL(user));
		xmlInputSteam.close();

		//记录上报日志
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		logManager.logLoadDataEvent(loadResult, user, request.getRemoteAddr(),
				LogManager.WEB_MODE);

		this.go2NoLogoInfoPage(loadResult.getMessage(), javascriptReturnUrl(),
				request, response);
	}

	/**
	 * 得到脚本的内容
	 * 
	 * @param scripts
	 *            脚本Script集合
	 * @return Map的集合
	 */
	private List getScriptContent(List scripts) {
		Map map = null;
		List result = new LinkedList();
		for (Iterator itr = scripts.iterator(); itr.hasNext();) {
			Script script = (Script) itr.next();
			map = new HashMap();
			map.put("name", script.getName());
			map.put("content", script.getContent());
			result.add(map);
		}
		return result;
	}

	/**
	 * 得到审核结果的错误信息
	 * 
	 * @param auditResult
	 *            审核结果
	 * @return 审核结果的错误信息
	 */
	private String getErrorMessage(AuditResult auditResult) {
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("错误数" + errors.size() + ":\n");
		for (int i = 0; i < errors.size(); i++) {
			errorMessage.append("（" + (i + 1) + "）" + errors.get(i) + "\n");
		}
		return errorMessage.toString();
	}

	/**
	 * 得到审核结果的警告信息
	 * 
	 * @param auditResult
	 *            审核结果
	 * @return 审核结果的警告信息
	 */
	private String getWarningMessage(AuditResult auditResult) {
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("警告数" + warnings.size() + ":\n");
		for (int i = 0; i < warnings.size(); i++) {
			warningMessage.append("（" + (i + 1) + "）" + warnings.get(i) + "\n");
		}
		return warningMessage.toString();
	}

	/**
	 * 显示下载报表数据页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showDownloadModelPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		UtilServlet.showRadioTree(request, response, "");
		go2UrlWithAttibute(request, response, DOWNLOAD_MODEL_PAGE);
	}

	/**
	 * 下载报表数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void downloadModel(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");

		boolean isRecursive = false;
		if (request.getParameter("isRecursive") != null
				&& !request.getParameter("isRecursive").equals("")) {
			isRecursive = true;
		}

		String unitID = request.getParameter("unitID");

		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());

		//设置response
		response.setContentType("application/fap");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + ".fap");
		ZipOutputStream zipout = new ZipOutputStream(response.getOutputStream());
		OutputStreamWriter writer = new OutputStreamWriter(zipout, "gb2312");
		zipout.putNextEntry(new ZipEntry(unitID + ".xml"));

		if (isRecursive) {
			modelManager.getDataExporter().exportDataByTree(unitID, taskTime,
					writer, modelManager.getUnitACL(getLoginUser(request)));
		} else {
			modelManager.getDataExporter().exportData(unitID, taskTime, writer,
					modelManager.getUnitACL(getLoginUser(request)));
		}

		Util.close(writer);
	}

	/**
	 * 下载报表数据中的附件
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void downloadAttachment(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");

		String unitID = request.getParameter("unitID");

		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());

		//设置response
		response.setContentType("application/x-zip-compressed; charset=gb2312");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + "-附件.zip");
		OutputStream out = response.getOutputStream();

		modelManager.getDataExporter().exportAttachment(unitID, taskTime, out,
				modelManager.getUnitACL(getLoginUser(request)));
		out.close();
	}

	/**
	 * 显示选择汇总方案管理页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showSelectSumSchemaManagePage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		request.setAttribute("schemaItr", modelManager.getSummer().getAllSelectSumSchemas(task.id()));
		go2UrlWithAttibute(request, response, SELECT_SUM_SCHEMA_MANAGE_PAGE);
	}

	/**
	 * 创建选择汇总方案
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void createSelectSumSchema(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		UploadBean upload = new UploadBean(getServletConfig(), request,
				response);
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		modelManager.getSummer().createSelectSumSchema(
				upload.getXmlInputStreamUploaded());
		showSelectSumSchemaManagePage(request, response);
	}

	/**
	 * 删除选择汇总方案
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteSelectSumSchema(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		Integer schemaID = new Integer(request.getParameter("schemaID"));
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		modelManager.getSummer().deleteSelectSumSchema(schemaID);
		showSelectSumSchemaManagePage(request, response);
	}

	/**
	 * 调整节点差额表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void adjustNodeDiff(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");

		boolean isRecursive = false;
		if (request.getParameter("isRecursive") != null
				&& !request.getParameter("isRecursive").equals("")) {
			isRecursive = true;
		}
		String unitID = request.getParameter("unitID");

		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());

		modelManager.getSummer().adjustNodeDiff(unitID, taskTime, isRecursive);

		//记录数据日志
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "对单位“" + unitID + "”的"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "数据执行调整差额表操作";
		if (isRecursive) {
			message += "（包含子节点）";
		}

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2InfoPage("调整节点差额表成功", "model?operation=" + SHOW_SUM_PAGE, request,
				response);
	}

	/**
	 * 完全汇总
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void sumAll(HttpServletRequest request, HttpServletResponse response)
			throws Warning, IOException, ServletException {
		Task task = (Task) request.getAttribute("task");
		TaskTime taskTime = task.getTaskTime(new Integer(request
				.getParameter("taskTimeID")));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		modelManager.getSummer().sumAll(Summer.SUM_ALL_UNIT_ID, taskTime);

		go2InfoPage("完全汇总成功", javascriptReturnUrl(), request, response);
	}

	/**
	 * 显示汇总主页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showSumPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		UtilServlet.showRadioTree(request, response, "");
		go2UrlWithAttibute(request, response, SUM_PAGE);
	}

	/**
	 * 显示节点汇总页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showSumNodePage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		UtilServlet.showRadioTree(request, response, "OfRadioTree");
		UtilServlet.showCheckboxTree(request, response, "OfCheckboxTree");
		go2UrlWithAttibute(request, response, MODELMANAGER_SUMNODE_PAGE);
	}

	/**
	 * 显示数据 //这是一个测试方法
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showDataManager(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		request.setAttribute("unitID", request.getParameter("unitID"));
		request.setAttribute("taskTimeID", request.getParameter("taskTimeID"));
		UtilServlet.showUnitTree(request, response);
		go2UrlWithAttibute(request, response, DATA_MANAGER_PAGE);
	}

	/**
	 * 显示数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showData(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		AnalyseManager analyseManager = ((AnalyseManagerFactory) Factory.getInstance(AnalyseManagerFactory.class.getName())).createAnalyseManager();

		String taskID = task.id();
		int tasktimeID = ParamUtils.getIntParameter(request, "taskTimeID", -1);
		String unitID = ParamUtils.getParameter(request, "unitID");

		if (unitID != null) {
			User user = getLoginUser(request);
			boolean powerEdit = canEditorSubmitData(user, unitID, task.id());

			if (powerEdit
					&& analyseManager.canEditorSubmitData(taskID, tasktimeID,
							unitID)) {
				request.setAttribute("canEditor", "1");
			}
		}

		Iterator itr = getAllPrintableTables(request, response);
		request.setAttribute(HTML_DATA, itr);

		//显示页面
		go2UrlWithAttibute(request, response, SHOW_DATA_PAGE);
	}

	/**
	 * 根据等级得到需要显示的表
	 * 
	 * @param unitnode
	 * @param task
	 * @return
	 */
	private ArrayList getShowTables(UnitTreeNode unitnode, Task task) {

//		int unitLevel = ShowReportUtil.getUnitLevel(unitnode);
//		ArrayList showTableIDs = ShowReportUtil.getLevelShowTables(unitLevel);
		ArrayList showTableIDs = null;
		if (showTableIDs == null)
			return null;
		ArrayList showTables = new ArrayList();
		try {
			for (int i = 0; i < showTableIDs.size(); i++) {
				showTables.add(task.getTableByID((String) showTableIDs.get(i)));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return showTables;
	}

	/**
	 * 是否有权限编辑数据
	 * 
	 * @param user
	 * @param unitID
	 * @return
	 */
	public boolean canEditorSubmitData(User user, String unitID, String taskID) {
		try {
			ModelManager modelManager = ((ModelManagerFactory) Factory
					.getInstance(ModelManagerFactory.class.getName()))
					.createModelManager(taskID);
			UnitPermissionManager unitPermissionManager = modelManager
					.getUnitPermissionManager();

			Iterator itr = user.getGroups().iterator();
			while (itr.hasNext()) {
				Group group = (Group) itr.next();
				if (unitPermissionManager.getPermission(group.getGroupID(),
						unitID, UnitPermission.UNIT_PERMISSION_WRITE)) {
					return true;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return false;
	}

	/**
	 * 对单张页面进行审核
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void doDataAudit(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		String unitIDs = request.getParameter("unitIDs");
		Task task = (Task) request.getAttribute("task");

		String type = request.getParameter("msgtype");
		int isexec = ParamUtils.getIntParameter(request, "exectype", 0);
		int flag = ParamUtils.getIntParameter(request, "flag", 0);

		//没有脚本
		ScriptSuit suit = task.getActiveScriptSuit();
		if (suit == null) {
			request.setAttribute("auditError", "");
			go2UrlWithAttibute(request, response, this.DATA_AUDITRESULT_PAGE);
			return;
		}

		List auditResultList = new ArrayList();

		String[] tmpunitID = StringUtil.split(unitIDs, ",");
		String[] unitID = null;

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

		LinkedHashMap map = new LinkedHashMap();
		for (int i = 0; i < tmpunitID.length; i++) {
			String tmpUnitID = tmpunitID[i];
			if (!tmpUnitID.equals("")) {
				UnitTreeNode unitTreeNode = unitTreeManager
						.getUnitTree(tmpUnitID);
				if (flag == 1) {
					UnitTreeNodeUtil.getAllSon(map, unitTreeNode);
				} else if (flag == 2) {
					UnitTreeNodeUtil.getAllChildren(map, unitTreeNode);
				}
			}
		}

		if (flag == 1 || flag == 2) {
			unitID = (String[]) (map.keySet().toArray(new String[0]));
		} else {
			unitID = tmpunitID;
		}

		int taskTimeID = ParamUtils.getIntParameter(request, "taskTimeID", 0);

		//执行脚本
		if (isexec == 0) {
			for (int i = 0; i < unitID.length; i++) {
				DBUtils.singleExec(unitID[i], task, new Integer(taskTimeID),
						getScriptContent(suit.getCalculateScriptToExec()));
			}

			request.setAttribute("execerror", "ok");
		}
		//审核
		else {
			String userName = getLoginUser(request).getName();
			for (int i = 0; i < unitID.length; i++) {
				List auditList = DBUtils.singleAudit(unitID[i], task,
						new Integer(taskTimeID), getScriptContent(suit
								.getAuditScriptToExec()), userName);
				if (auditList != null) {
					auditResultList.addAll(auditList);
				}
			}
		}

		//脚本错误
		if (auditResultList == null) {
			request.setAttribute("auditScript", "");
		} else {
			request.setAttribute("auditResultList", auditResultList);
		}

		go2UrlWithAttibute(request, response, this.DATA_AUDITRESULT_PAGE);
	}

	/**
	 * 以EXCEL格式保存单张表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getExcelFormatForSingleTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + "_" + tableID + ".xls");

		new EXCELSerializer().serializeTable(xmlIn, task.getTableByID(tableID),
				response.getOutputStream());
		writer.close();
	}

	/**
	 * 以EXCEL格式保存整套表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getExcelFormatForAllTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		writer.close();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + ".xls");

		new EXCELSerializer().serializeTask(xmlIn, task, response
				.getOutputStream());
	}

	/**
	 * 以PDF格式保存单张表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getPDFFormatForSingleTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + "_" + tableID + ".pdf");

		new PDFSerializer().serializeTable(xmlIn, task.getTableByID(tableID),
				response.getOutputStream());
		writer.close();
	}

	/**
	 * 以PDF格式保存单张表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void getPDFFormatForAllTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + ".pdf");

		new PDFSerializer().serializeTask(xmlIn, task, response
				.getOutputStream());
		writer.close();
	}

	/**
	 * 下载报表数据，然后格式化为web 显示表格(Table)， 如果存在附件，保存到request里面。
	 * 
	 * @param request
	 * @param response
	 * @return 返回格式化后可以直接在web显示的表格
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private Iterator getAllPrintableTables(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		if (request.getParameter("flag") == null) {
			String unitID = request.getParameter("unitID");
			TaskTime taskTime = modelManager.getTask().getTaskTime(
					new Integer(request.getParameter("taskTimeID")));
			if (taskTime == null) {
				throw new Warning("指定的任务时间不存在");
			}

			ArrayList showTables = null;

			//粮补专用
			UnitTreeNode unit = modelManager.getUnitTreeManager().getUnitTree(unitID);
//			showTables = getShowTables(unit, task);
//            
//			if(showTables==null)
//			{
//				Iterator itrTable = task.getAllTables();
//				while (itrTable.hasNext()) {
//					showTables.add(itrTable.next());
//				}
//			}

			UnitACL acl = modelManager.getUnitACL(getLoginUser(request));
			//报表数据
			StringWriter out = new StringWriter();

//			modelManager.getDataExporter().exportData(unitID, taskTime, out,acl,showTables);    //粮补专用
			modelManager.getDataExporter().exportData(unitID, taskTime, out,acl);
 
			ByteArrayInputStream xmlIn = new ByteArrayInputStream(out
					.toString().getBytes("gb2312"));
			Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
			out.close();

			//附件
			ByteArrayOutputStream attOut = new ByteArrayOutputStream();
			modelManager.getDataExporter().exportAttachment(unitID, taskTime,
					attOut, acl);
			if (attOut.toByteArray().length > 0) {
				request.setAttribute("hasAttachment", "true");
			}
			attOut.close();

			return itr;
		} else {
			//显示空表样
			String xmlStr = "<?xml version='1.0' encoding='UTF-8'?><taskModel ID='";
			xmlStr += task.id() + "'>";
			Iterator tables = task.getAllTables();
			while (tables.hasNext()) {
				cn.com.youtong.apollo.task.Table tempTable = (cn.com.youtong.apollo.task.Table) tables
						.next();
				xmlStr += "<table ID='" + tempTable.id() + "'></table>";
			}
			xmlStr += "</taskModel>";
			ByteArrayInputStream xmlIn = new ByteArrayInputStream(xmlStr
					.getBytes());
			Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);

			return itr;
		}
	}

	/**
	 * 打印报表数据，目前还不支持附件打印
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void printPdf(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");

		Iterator iter = getAllPrintableTables(request, response);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write("<?xml version=\"1.0\" encoding=\"gbk\"?>".getBytes()); // write
																		  // xml
																		  // declaration
		out.write("\r\n".getBytes()); // new line
		out.write("<html>\r\n".getBytes()); // 表示成html，html为根元素。

		// 这里可能存在多个table
		while (iter.hasNext()) {
			TableViewer tv = (TableViewer) iter.next();
			out.write("<h2>表: ".getBytes());
			out.write(tv.getTableName().getBytes());
			out.write("</h2>".getBytes());

			out.write("\r\n".getBytes());
			String s = tv.getHtmlString();
			out.write(s.getBytes());
			out.write(("<" + com.lowagie.text.ElementTags.NEWPAGE + "/>")
					.getBytes());
		}

		out.write("\r\n</html>".getBytes());

		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		Document document = new Document(PageSize.A4, 80, 50, 30, 65);
		response.setContentType("application/pdf; charset=gb2312");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + ".pdf");

		try {
			PdfWriter.getInstance(document, response.getOutputStream());
		} catch (DocumentException ex) {
			throw new Warning(ex);
		}
		document.open();

		HtmlParser.parse(document, in);

		document.close();
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void downExcel(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
		writer.close();

		StringBuffer result = new StringBuffer();
		result
				.append("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\"\r\n");
		result.append("xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\r\n");
		result.append("xmlns=\"http://www.w3.org/TR/REC-html40\">\r\n");
		result.append("<head>\r\n");
		result
				.append("<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">\r\n");
		result.append("</head>\r\n");
		result.append("<body link=blue vlink=purple>\r\n");

		while (itr.hasNext()) {
			TableViewer tableViewer = (TableViewer) itr.next();
			if (tableViewer.getTableID().equals(tableID)) {
				result.append(tableViewer.getHtmlString());
				break;
			}
		}

		result.append("</body>\r\n");
		result.append("</html>");

		response.setContentType("application/vnd.ms-excel; charset=gb2312");
		response.setHeader("Content-disposition", "attachment; filename="
				+ tableID + ".xls");

		response.getWriter().write(result.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 单表打印
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void printOneTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());

		UnitTreeNode unit = modelManager.getUnitTreeManager().getUnitTree(
				unitID);

		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));

		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
		writer.close();

		StringBuffer result = new StringBuffer();
		result.append("<html XMLNS:IE>\r\n");
		result.append("<head>\r\n");
		result
				.append("<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">\r\n");
		result
				.append("<link href='../csslib/main.css' rel='stylesheet' type='text/css'>\r\n");
		result.append("</head>\r\n");
		result.append("<body>\r\n");
		result.append("<?IMPORT NAMESPACE='IE' IMPLEMENTATION='#default'>\r\n");

		result
				.append("<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WB width=0></OBJECT>\r\n");
		int i = 1;
		while (itr.hasNext()) {
			TableViewer tableViewer = (TableViewer) itr.next();
			if (tableViewer.getTableID().equals(tableID)) {
				result.append(getTableHeadHtml(unit, taskTime, tableViewer
						.getTableName()));
				result.append(tableViewer.getHtmlString());
				break;
			}
			i++;
		}
		result.append("<script language='javascript'>\r\n");
		result.append("WB.ExecWB(7,1);");
		result.append("</script>\r\n");
		result.append("</body>\r\n");
		result.append("</html>");

		response.setContentType("text/html; charset=gb2312");
		//response.setHeader("Content-disposition","attachment; filename=" +
		// tableID + ".xls");

		response.getWriter().write(result.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 得到表头Html
	 * 
	 * @param unit
	 *            单位
	 * @param taskTime
	 *            taskTime
	 * @param tableName
	 *            tableName
	 * @return 表头Html
	 */
	private String getTableHeadHtml(UnitTreeNode unit, TaskTime taskTime,
			String tableName) {
		StringBuffer result = new StringBuffer();
		result.append("<table width='80%'>");
		result.append("<tr><td align='center'><strong><font size='+1'>"
				+ tableName + "</font></strong></td></tr>");
		result.append("</table>");

		return result.toString();
	}

	/**
	 * 显示单位管理页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showManageUnitPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
//		UtilServlet.showAllUnitTree(request, response);
		UtilServlet.showCheckboxTreeOfRight(request, response, "");
		go2UrlWithAttibute(request, response, MANAGE_UNIT_PAGE);
	}

	/**
	 * 显示单位信息页面
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showUnitInfoPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");

		if (unitID != null) {
			Task task = (Task) request.getAttribute("task");
			ModelManager modelManager = ((ModelManagerFactory) Factory
					.getInstance(ModelManagerFactory.class.getName()))
					.createModelManager(task.id());
			UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

			request.setAttribute("unit", unitTreeManager.getUnitTree(unitID));
		}

		go2UrlWithAttibute(request, response, UNIT_INFO_PAGE);
	}

	/**
	 * 创建单位
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void createUnit(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory)Factory.getInstance(ModelManagerFactory.class.getName())).createModelManager(task.id());
		UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();
		String code = request.getParameter("code");
		String name = request.getParameter("unitName");
		String reportType = request.getParameter("reportType");
		String parentCode = request.getParameter("parentCode");
		String HQCode = request.getParameter("HQCode");
		String p_Parent = request.getParameter("p_Parent");
		String redirectPage = request.getParameter("redirectPage");
		try {
			UnitTreeNode unit = unitTreeManager.createUnit(code, name,reportType, parentCode, HQCode, p_Parent);
			request.setAttribute("unit", unit);
			if (redirectPage != null && redirectPage.length() > 0) {
				//创建选择汇总单位，将新单位的权限付给当前用户所在组
				UnitPermissionManager unitPermissionManager = modelManager.getUnitPermissionManager();
				UnitPermission unitPermission = new UnitPermission();
				unitPermission.setPermission(1, true);
				unitPermission.setPermission(2, true);
				User user = RootServlet.getLoginUser(request);
				Collection groups = user.getGroups();
				if (groups != null) {
					Iterator ite = groups.iterator();
					while (ite.hasNext()) {
						Group group = (Group) ite.next();
						unitPermissionManager.setUnitPermission(group.getGroupID(), unit.id(), unitPermission);
					}
				}
				this.go2UrlWithAttibute(request, response, redirectPage);
			} else {
				showManageUnitPage(request, response);
			}
		} catch (Warning e) {
			if (redirectPage != null && redirectPage.length() > 0) {
				request.setAttribute("exception", e);
				this.go2UrlWithAttibute(request, response, redirectPage);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 更新单位
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateUnit(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

		String unitID = request.getParameter("unitID");
		String code = request.getParameter("code");
		String name = request.getParameter("unitName");
		String reportType = request.getParameter("reportType");
		String parentCode = request.getParameter("parentCode");
		String HQCode = request.getParameter("HQCode");
		String p_Parent = request.getParameter("p_Parent");
		int display =1;
		if(request.getParameter("display")!=null) display=Integer.parseInt(request.getParameter("display"));

		UnitTreeNode unit = unitTreeManager.updateUnit(unitID, code, name,
				reportType, parentCode, HQCode, p_Parent,display);
		request.setAttribute("unit", unit);

		showManageUnitPage(request, response);
	}

	/**
	 * 删除单位
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteUnit(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();

		String unitID = request.getParameter("unitID");

		unitTreeManager.deleteUnit(unitID);

		showManageUnitPage(request, response);
	}

	/**
	 * 套表打印
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void printAllTable(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitID = request.getParameter("unitID");
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		UnitTreeNode unit = modelManager.getUnitTreeManager().getUnitTree(
				unitID);
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//设置response
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));
		Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
		writer.close();
		StringBuffer result = new StringBuffer();
		result.append("<html XMLNS:IE>\r\n");
		result.append("<head>\r\n");
		result.append("<?IMPORT NAMESPACE='IE' IMPLEMENTATION='#default'>\r\n");
		result
				.append("<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WB width=0></OBJECT>\r\n");
		result
				.append("<STYLE TYPE='text/css'>.PageStyle{height:22in;}</STYLE>\r\n");
		result
				.append("<meta http-equiv=Content-Type content=\"text/html; charset=gb2312\">\r\n");
		result
				.append("<link href='../csslib/main.css' rel='stylesheet' type='text/css'>\r\n");
		result.append("</head>\r\n");
		result.append("<body>\r\n");
		int i = 1;
		while (itr.hasNext()) {
			TableViewer tableViewer = (TableViewer) itr.next();
			result.append("<IE:DEVICERECT ID='page" + String.valueOf(i)
					+ "' class='PageStyle' MEDIA='print'>");
			result.append(getTableHeadHtml(unit, taskTime, tableViewer
					.getTableName()));
			result.append(tableViewer.getHtmlString());
			result.append("</IE:DEVICERECT>");
			i++;
		}
		result.append("<script language='javascript'>\r\n");
		result.append("WB.ExecWB(7,1);");
		result.append("</script>\r\n");
		result.append("</body>\r\n");
		result.append("</html>");

		response.setContentType("text/html; charset=gb2312");
		//response.setHeader("Content-disposition","attachment; filename=" +
		// tableID + ".xls");

		response.getWriter().write(result.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	private void modifyDisplayOfUnit(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String unitIDs = request.getParameter("result");
		List lstUnitIDs = Arrays.asList(unitIDs.split(","));
		String taskID = (String)request.getSession().getAttribute("taskID");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(taskID);

		UnitACL acl = modelManager.getUnitACL(getLoginUser(request));
		UnitTreeManager unitTreeMng = modelManager.getUnitTreeManager();
		Iterator treeItr = unitTreeMng.getUnitForest(acl);
		while(treeItr.hasNext()){
			Map map = new HashMap();
			UnitTreeNode unitTreeNode =(UnitTreeNode)treeItr.next();
			unitTreeMng.getUnits(unitTreeNode, map);
			Set keyset = map.keySet();
			for(Iterator it= keyset.iterator();it.hasNext();){
				String key =(String)it.next();
				UnitTreeNode unitNode = (UnitTreeNode)map.get(key);
				if(lstUnitIDs.contains(unitNode.id())){
					unitTreeMng.updateUnit(unitNode.id(), unitNode.getUnitCode(), unitNode.getUnitName(), unitNode.getReportType(), unitNode.getParentUnitCode(), unitNode.getHQCode(), unitNode.getP_Parent(), 1);
				}else{
					unitTreeMng.updateUnit(unitNode.id(), unitNode.getUnitCode(), unitNode.getUnitName(), unitNode.getReportType(), unitNode.getParentUnitCode(), unitNode.getHQCode(), unitNode.getP_Parent(), 0);
				}
			}
		}		
		showUnitInfoPage(request, response);
	}

	/**
	 * 页面分发
	 * 
	 * @param req
	 * @param res
	 * @throws cn.com.youtong.apollo.common.Warning
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	public void perform(HttpServletRequest request, HttpServletResponse response)
			throws cn.com.youtong.apollo.common.Warning, java.io.IOException,
			javax.servlet.ServletException {
		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		String taskID = (String) request.getSession().getAttribute("taskID");
//		taskManager = new DBTaskManager();
		Task t = taskManager.getTaskByID(taskID);
		TaskTime tt = t.getTaskTime(565);
//		System.out.println("task time =!!!===>"+tt);
		request.setAttribute("task", taskManager.getTaskByID(taskID));

		String operation = request.getParameter("operation");

		if (operation == null) {
			throw new Warning("无效的参数operation = " + operation);
		}else if(operation.equalsIgnoreCase(SAVE_UNITDATA_BY_UNITID)){
			saveUnitByUnitid(request, response);
			return;
		}else if(operation.equalsIgnoreCase(LOAD_UNITDATA_BY_UNITID)){
			loadDataByUnitid(request, response);
			return;
		} else if (operation.equalsIgnoreCase(UPLOAD_ATTACH)) {
			uploadattach(request, response);
			return;
		} else if (operation.equalsIgnoreCase(LIST_ATTACH)) {
			listattach(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_ATTACH)) {
			showattach(request, response);
			return;
		}

		else if (operation.equalsIgnoreCase(DELETE_ATTACH)) {
			deleteattach(request, response);
			return;
		}

		else if (operation.equalsIgnoreCase(SHOW_DATA_EXCHANGE_PAGE)) {
			showDataExchangePage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_DATA_AUDIT_PAGE)) {
			dataAuditPage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(PRINT_ONE_TABLE)) {
			printOneTable(request, response);
			return;
		} else if (operation.equalsIgnoreCase(PRINT_ALL_TABLE)) {
			printAllTable(request, response);
			return;
		}

		else if (operation.equalsIgnoreCase(ADJUST_NODE_DIFF)) {
			adjustNodeDiff(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DISPLAY_ALL_ADDRESS_INFO)) {
			displayAllAddressInfo(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DELETE_ADDRESS_INFO)) {
			deleteAddressInfo(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DOWNLOAD_ATTACHMENT)) {
			downloadAttachment(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SUM_ALL)) {
			sumAll(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SUM_NODE)) {
			sumNode(request, response);
			return;
		} else if (operation.equalsIgnoreCase(EXECUTE_SELECT_SUM_SCHEMA)) {
			executeSelectSumSchema(request, response);
			return;
		} else if (operation.equalsIgnoreCase(VALIDATE_NODE_SUM)) {
			validateNodeSum(request, response);
			return;
		} else if (operation.equalsIgnoreCase(UPLOAD_MODEL)) {
			uploadModel(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_UPLOAD_MODEL_PAGE)) {
			showUploadModelPage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DOWNLOAD_MODEL)) {
			downloadModel(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_DOWNLOAD_MODEL_PAGE)) {
			showDownloadModelPage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_SUM_PAGE)) {
			showSumPage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_SUMNODE_PAGE)) {
			showSumNodePage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_ADDRESS_INFO_PAGE)) {
			showAddressInfoPage(request, response);
			return;
		} else if(operation.equalsIgnoreCase(SHOW_UNITMETATABLE_INFO_PAGE)){
			showUnitMetaTableInfoPage(request,response);
			return;
		}else if (operation.equalsIgnoreCase(ADD_ADDRESS_INFO)) {
			addAddressInfo(request, response);
			return;
		} else if (operation.equalsIgnoreCase(GET_ADDRESS_INFO_BYTASKID)) {
			getAddressInfoByTaskID(request, response);
			return;
		} else if(operation.equalsIgnoreCase(GET_UNITMETA_INFO_BYTASKID)){
			getUnitMetaInfoByTaskID(request, response);
			return;
		}else if (operation.equalsIgnoreCase(SHOW_DATA)) {
			showData(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SHOW_DATA_MANAGER)) {
			showDataManager(request, response);
			return;
		} else if (operation
				.equalsIgnoreCase(SHOW_SELECT_SUM_SCHEMA_MANAGE_PAGE)) {
			showSelectSumSchemaManagePage(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DELETE_SELECT_SUM_SCHEMA)) {
			deleteSelectSumSchema(request, response);
			return;
		} else if (operation.equalsIgnoreCase(CREATE_SELECT_SUM_SCHEMA)) {
			createSelectSumSchema(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DOWN_EXCEL)) {
			downExcel(request, response);
			return;
		} else if (operation.equalsIgnoreCase(PRINT_PDF)) {
			printPdf(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DO_DATA_AUDIT)) {
			doDataAudit(request, response);
		} else if (operation.equalsIgnoreCase(GET_EXCEL_FOR_SINGLE_TABLE)) {
			getExcelFormatForSingleTable(request, response);
		} else if (operation.equalsIgnoreCase(GET_EXCEL_FOR_ALL_TABLE)) {
			getExcelFormatForAllTable(request, response);
		} else if (operation.equalsIgnoreCase(GET_PDF_FOR_SINGLE_TABLE)) {
			getPDFFormatForSingleTable(request, response);
		} else if (operation.equalsIgnoreCase(GET_PDF_FOR_ALL_TABLE)) {
			getPDFFormatForAllTable(request, response);
		} else if (operation.equalsIgnoreCase(SHOW_MANAGE_UNIT_PAGE)) {
			showManageUnitPage(request, response);
		} else if (operation.equalsIgnoreCase(SHOW_UNIT_INFO_PAGE)) {
			showUnitInfoPage(request, response);
		} else if (operation.equalsIgnoreCase(CREATE_UNIT)) {
			createUnit(request, response);
		} else if (operation.equalsIgnoreCase(UPDATE_UNIT)) {
			updateUnit(request, response);
		} else if (operation.equalsIgnoreCase(DELETE_UNIT)) {
			deleteUnit(request, response);
		}

		else if (operation.equalsIgnoreCase(EIDT_DATA)) {
			showEditPage(request, response);
		}

		else if (operation.equalsIgnoreCase(SAVE_DATA)) {
			saveDate(request, response);
		} else if (operation.equalsIgnoreCase(SHOW_UNIT_TREE)) {
			showUnitTree(request, response);
			return;
		} else if (operation.equalsIgnoreCase(SELECT_SUM)) {
			selectSum(request, response);
			return;
		} else if (operation.equalsIgnoreCase(DO_SELECT_SUM)) {
			doSelectSum(request, response);
			return;
		}

		else if (operation.equalsIgnoreCase(ENVELOPSUBMITDATA)) {
			envelopsubmitData(request, response);
		} else if (operation.equalsIgnoreCase(UNENVELOPSUBMITDATA)) {
			unEnvelopsubmitData(request, response);
		} else if (operation.equalsIgnoreCase(PRINT_UNITTREE)) {
			printUnitTree(request, response);
		} else if(operation.equalsIgnoreCase(MODIFY_UNIT_DISPLAY)){
			modifyDisplayOfUnit(request,response);
		} else if(operation.equalsIgnoreCase(SHOW_UNITTREE_BY_NAMEORCODE)){
			this.showUnitTreeByNameOrCode(request, response);
		}else {
			throw new Warning("无效的参数operation = " + operation);
		}
	}

	/**
	 * 打印单位树
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void printUnitTree(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		String taskID = (String) request.getParameter("taskID");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(taskID);

		Iterator treeItr = modelManager.getUnitTreeManager().getUnitForest();
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("var tree = new WebFXTree('单位树');\n");
		strBuf.append("tree.setBehavior('classic');\n");
		while (treeItr.hasNext()) {
			UnitTreeNode unitTreeNode = (UnitTreeNode) treeItr.next();
			strBuf.append(getUnitTreeNodeScript(unitTreeNode, "tree"));
		}

		request.setAttribute("unittree", strBuf.toString());
		/*
		 * response.reset(); response.setContentType("text/xml;
		 * charset=gb2312"); Writer writer = response.getWriter();
		 * writer.write(strBuf.toString()); writer.flush(); writer.close();
		 */

		this.go2UrlWithAttibute(request, response, this.PRINT_UNITTREE_PAGE);
	}

	/**
	 * 得到单位的XML文件
	 * 
	 * @param unitTreeNode
	 * @return
	 */
	private String getUnitTreeNodeScript(UnitTreeNode unitTreeNode,
			String parent) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("var " + "node" + unitTreeNode.id()
				+ " = new WebFXTreeItem('" + unitTreeNode.getUnitName()
				+ "');\n");
		strBuf.append(parent + ".add(" + "node" + unitTreeNode.id() + ");\n");
		Iterator treeItr = unitTreeNode.getChildren();
		while (treeItr.hasNext()) {
			UnitTreeNode childUnitTreeNode = (UnitTreeNode) treeItr.next();
			strBuf.append(getUnitTreeNodeScript(childUnitTreeNode, "node"
					+ unitTreeNode.id()));
		}
		return strBuf.toString();
	}

	/**
	 * 启封数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void unEnvelopsubmitData(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		AnalyseManager analyseManager = ((AnalyseManagerFactory) Factory
				.getInstance(AnalyseManagerFactory.class.getName()))
				.createAnalyseManager();

		String taskID = task.id();
		int tasktimeID = ParamUtils.getIntParameter(request, "taskTimeID", -1);
		int flag = ParamUtils.getIntParameter(request, "flag", 1);

		String unitID = ParamUtils.getParameter(request, "unitID");

		//判断是否能够启封
		if (!analyseManager.canUnenvelopSubmitData(taskID, tasktimeID, unitID)) {

			go2InfoPage("由于上级没有启封数据，所以" + unitID + "不能启封上报数据",
					"model?operation=" + SHOW_SUM_PAGE, request, response);
			return;
		}

		List unitList = new ArrayList();
		unitList.add(unitID);

		analyseManager.unEnvelopSubmitData(taskID, tasktimeID, unitList, flag);

		go2InfoPage("数据启封成功", "model?operation=" + SHOW_SUM_PAGE, request,
				response);

	}

	/**
	 * 封存数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void envelopsubmitData(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		AnalyseManager analyseManager = ((AnalyseManagerFactory) Factory
				.getInstance(AnalyseManagerFactory.class.getName()))
				.createAnalyseManager();

		String taskID = task.id();
		int tasktimeID = ParamUtils.getIntParameter(request, "taskTimeID", -1);

		String unitID = ParamUtils.getParameter(request, "unitID");
		List unitList = new ArrayList();
		unitList.add(unitID);
		analyseManager.envelopSubmitData(taskID, tasktimeID, unitList);

		go2InfoPage("数据封存成功", "model?operation=" + SHOW_SUM_PAGE, request,
				response);

	}

	/**
	 * 显示编辑页面,借用已有的showData方法，在输出结果之后对XML解析，生存脚本对象
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showEditPage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		String unitID = request.getParameter("unitID");
		TaskTime taskTime = modelManager.getTask().getTaskTime(
				new Integer(request.getParameter("taskTimeID")));
		if (taskTime == null) {
			throw new Warning("指定的任务时间不存在");
		}
		request.setAttribute("taskTime", Convertor.date2ShortString(taskTime
				.getFromTime()));
		UnitACL acl = modelManager.getUnitACL(getLoginUser(request));
		//报表数据
		StringWriter writer = new StringWriter();

		String cnt = DBAnalyseHelper.queryDraft(unitID, task.id(), taskTime.getTaskTimeID().intValue());
		if(cnt!=null){
			writer.write(cnt);
		}else{
			modelManager.getDataExporter().exportEditorData(unitID, taskTime,writer, acl);
		}
		ByteArrayInputStream xmlIn = new ByteArrayInputStream(writer.toString()
				.getBytes("gb2312"));

		Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
		request.setAttribute(HTML_DATA, itr);

		ByteArrayInputStream xmlInparse = new ByteArrayInputStream(writer
				.toString().getBytes("gb2312"));
		String xml = Convertor.Stream2String(xmlInparse);

		String jsObj = DataEditHelp.getEditScriptObject(xml);
		request.setAttribute("JSOBJ", jsObj);

		writer.close();
		String filename = request.getSession().getServletContext().getRealPath(
				"WEB-INF")
				+ "/params/" + task.id() + ".par";
		FileManager fmanager = FileManager.getInstance(filename);
		try {
			byte[] data = fmanager.Load();
			if (data != null) {
				String content = new String(data);
				request.setAttribute("server", "http://"
						+ request.getServerName() + ":"
						+ request.getServerPort() + request.getContextPath());
				request.setAttribute("param", content);
			} else {
				request.setAttribute("error", "");
			}
		} finally {
			fmanager.close();
		}
		User user = getLoginUser(request);
		request.setAttribute("userid", user.getUserID());
		request.setAttribute("password", user.getPassword());

		//显示页面
		go2UrlWithAttibute(request, response, EIDT_DATA_PAGE);

	}

	/**
	 * 保存数据
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void saveDate(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		String unitID = request.getParameter("unitID");
		String taskTimeID = request.getParameter("taskTimeID");

		StringBuffer resultBuf = new StringBuffer();
		resultBuf.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		resultBuf.append("<taskModel ID=\"" + task.id() + "\">");
		resultBuf.append("<taskTime taskTime=\"" + taskTimeID + "\">");
		resultBuf.append("<unit ID=\"" + unitID + "\">");

		Iterator tables = task.getAllTables();
		while (tables.hasNext()) {
			cn.com.youtong.apollo.task.Table tempTable = (cn.com.youtong.apollo.task.Table) tables
					.next();

			resultBuf.append("<table ID=\"" + tempTable.getTableID() + "\">");

			String result = request.getParameter(tempTable.id() + "result");

			if (result != null) {
				String[] arr = split(result, "||@");

				for (int i = 0; i < arr.length; i = i + 2) {
					resultBuf.append("<cell field=\"" + arr[i] + "\" value=\""
							+ arr[i + 1] + "\" />");
				}
			}
		}

		resultBuf.append("</taskTime>");
		resultBuf.append("</taskModel>");

		//@todo将数据保存
//		System.out.println(resultBuf.toString());

	}

	public String[] split(String s, String delimiter) {
		if (s == null || delimiter == null) {
			return new String[0];
		}

		s = s.trim();

		if (!s.endsWith(delimiter)) {
			s += delimiter;
		}

		if (s.equals(delimiter)) {
			return new String[0];
		}

		List nodeValues = new ArrayList();

		if (delimiter.equals("\n") || delimiter.equals("\r")) {
			try {
				BufferedReader br = new BufferedReader(new StringReader(s));

				String line = null;

				while ((line = br.readLine()) != null) {
					nodeValues.add(line);
				}

				br.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		} else {
			int offset = 0;
			int pos = s.indexOf(delimiter, offset);

			while (pos != -1) {
				nodeValues.add(s.substring(offset, pos));

				offset = pos + delimiter.length();
				pos = s.indexOf(delimiter, offset);
			}
		}

		return (String[]) nodeValues.toArray(new String[0]);
	}

	/**
	 * 默认情况下，显示checkbox单位树
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showUnitTree(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		String treeType = request.getParameter("checkboxUnitTree");

		if (treeType == null) {
			treeType = "checkboxUnitTree";
		} else if (treeType.startsWith("radio")) {
			treeType = "radioboxUnitTree";
		} else if (treeType.startsWith("checkbox")) {
			treeType = "checkboxUnitTree";

			// 设置单位树属性
		}
		if (treeType.equals("checkboxUnitTree")) {
			String[] unitIDs = request.getParameterValues("unitID");
			UtilServlet.showCheckboxUnitTree(request, response, unitIDs, null);
		} else if (treeType.equals("radioboxUnitTree")) {
			String functionName = request.getParameter("functionName");
			if (functionName == null) {
				functionName = "";

			}
			UtilServlet.showRadioTree(request, response, functionName);
		}

		go2UrlWithAttibute(request, response, SHOW_UNIT_TREE_PAGE);
	}

	private void selectSum(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		UnitTreeManager unitTreeManager = modelManager.getUnitTreeManager();
		UnitACL unitACL = modelManager.getUnitACL(RootServlet
				.getLoginUser(request));
		Iterator ite = modelManager.getUnitTreeManager().getUnitForest(unitACL);
		List selectSumUnits = new ArrayList();
		while (ite.hasNext()) {
			UnitTreeNode node = (UnitTreeNode) ite.next();
			getSelectSumUnits(node, selectSumUnits);
		}
		request.setAttribute("selectSumUnitTreeNodes", selectSumUnits
				.iterator());
		go2UrlWithAttibute(request, response, SELECT_SUM_PAGE);
	}

	private void getSelectSumUnits(UnitTreeNode node, List selectSumUnits) {
		if (node == null) {
			return;
		}
		if (ReportType.SELECT_GATHER_TYPE.equals(node.getReportType())) {
			selectSumUnits.add(node);
			Iterator ite = node.getChildren();
			if (ite != null) {
				while (ite.hasNext()) {
					UnitTreeNode child = (UnitTreeNode) ite.next();
					getSelectSumUnits(child, selectSumUnits);
				}
			}
		}
	}

	private void doSelectSum(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		String unitID = request.getParameter("unitID");
		String sUnits[] = request.getParameterValues("unitIDs");
		String taskTimeID = request.getParameter("taskTimeID");

		TaskTime taskTime = task.getTaskTime(new Integer(taskTimeID));

		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(task.id());
		Summer summer = modelManager.getSummer();
		Collection childUnits = new ArrayList();
		for (int i = 0; i < sUnits.length; i++) {
			childUnits.add(sUnits[i]);
		}
		summer.sumUnits(unitID, childUnits, taskTime);

		//记录数据日志
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();
		String message = "对单位“" + unitID + "”的"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "数据执行选择汇总操作";
		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2UrlWithAttibute(request, response, DO_SELECT_SUM_OK_PAGE);
	}

	/**
	 * 更新附件
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void uploadattach(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		String tableName = NameGenerator.generateAttachmentTableName(task.id());
		UploadServletRequest requestnew = new UploadServletRequest(request);
		MultipartRequestWrapper mutReq = new MultipartRequestWrapper(requestnew);

		UploadServletRequest uploadReq = (UploadServletRequest) ((MultipartRequestWrapper) mutReq)
				.getRequest();

		ByteArrayDataSource ds = (ByteArrayDataSource) uploadReq
				.getDataSource("fileupload");

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		try {
			InputStream in = ds.getInputStream();
			int c = in.read();
			while (c != -1) {
				outStream.write(c);
				c = in.read();
			}

			in.close();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Warning("上载错误", e);
		}

		byte[] byteArray = outStream.toByteArray();

		String fileSize = String.valueOf(byteArray.length);
		String content = new String(Base64.encode(byteArray).getBytes(),
				"ISO8859-1");
		String fileName = ds.getName();
		String fileType = ds.getContentType();
		String taskTimeID = uploadReq.getParameter("taskTimeID");
		String unitID = uploadReq.getParameter("unitID");

		AttachModel attachModel = new AttachModel();
		attachModel.setFileName(fileName);
		attachModel.setFileSize(fileSize);
		attachModel.setFileType(fileType);
		attachModel.setUnitID(unitID);
		attachModel.setTaskTimeID(taskTimeID);
		attachModel.setContent(content);
		attachModel.setTableName(tableName);
		AttachManager.insertAttach(attachModel);

		request.setAttribute("taskTimeID", taskTimeID);
		request.setAttribute("unitID", unitID);

		listattach(request, response);
	}

	/**
	 * 附件列表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void listattach(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		Task task = (Task) request.getAttribute("task");
		String tableName = NameGenerator.generateAttachmentTableName(task.id());
		String contentType = request.getContentType();
		String unitID = "";
		String taskTimeID = "";
		if (contentType != null
				&& contentType.startsWith("multipart/form-data;")) {
			unitID = (String) request.getAttribute("unitID");
			taskTimeID = (String) request.getAttribute("taskTimeID");
		} else {
			unitID = request.getParameter("unitID");
			taskTimeID = request.getParameter("taskTimeID");
		}
		ArrayList attachList = AttachManager.getAttachList(unitID, taskTimeID,
				tableName);
		request.setAttribute("attachlist", attachList);
		go2UrlWithAttibute(request, response, LIST_ATTACH_PAGE);
	}

	/**
	 * 显示列表
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showattach(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		String tableName = NameGenerator.generateAttachmentTableName(task.id());
		String attachID = request.getParameter("attachID");

		AttachModel attachModel = AttachManager.showAttach(attachID, tableName);

		if (attachModel != null) {
			response.reset();

			//response.setContentType(attachModel.getFileType() +
			// "application/octet-stream; charset=gb2312");
			response
					.setContentType("application/octet-stream; charset=ISO8859-1");
			response.setHeader("Content-disposition", "attachment; filename=\""
					+ attachModel.getFileName() + "\"");

			String countent = new String(Base64
					.decode(attachModel.getContent()), "ISO8859-1");
			PrintWriter out = response.getWriter();
			out.write(countent);
			out.flush();
		}
	}

	/**
	 * 删除附件
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteattach(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {

		Task task = (Task) request.getAttribute("task");
		String tableName = NameGenerator.generateAttachmentTableName(task.id());

		UploadServletRequest requestnew = new UploadServletRequest(request);
		MultipartRequestWrapper mutReq = new MultipartRequestWrapper(requestnew);

		UploadServletRequest uploadReq = (UploadServletRequest) ((MultipartRequestWrapper) mutReq)
				.getRequest();
		String taskTimeID = uploadReq.getParameter("taskTimeID");
		String unitID = uploadReq.getParameter("unitID");

		String attachID = uploadReq.getParameter("attachID");
		AttachManager.deleteAttach(attachID, tableName);

		request.setAttribute("taskTimeID", taskTimeID);
		request.setAttribute("unitID", unitID);
		listattach(request, response);
	}

}