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
 * �������ݹ���servlet
 */
public class ModelServlet extends RootServlet {

	/**
	 * ҳ�泣�� -- ���ݴ��Ϳ��ҳ��
	 */
	public static final String DATA_EXCHANGE_PAGE = "/jsp/modelManager/dataExchange.jsp";

	/**
	 * ҳ�泣�� -- ������˿��ҳ��
	 */
	public static final String DATA_AUDIT_PAGE = "/jsp/modelManager/dataAudit.jsp";

	/**
	 * ҳ�泣�� -- ������˽��ҳ��
	 */
	public static final String DATA_AUDITRESULT_PAGE = "/jsp/modelManager/dataAuditResult.jsp";

	/**
	 * ҳ�泣�� -- ���������ϱ�ҳ��
	 */
	public static final String UPLOAD_MODEL_PAGE = "/jsp/modelManager/uploadModel.jsp";

	/**
	 * ҳ�泣�� -- ������������ҳ��
	 */
	public static final String DOWNLOAD_MODEL_PAGE = "/jsp/modelManager/downloadModel.jsp";

	/**
	 * ҳ�泣�� -- ��λ����ҳ��
	 */
	public static final String MANAGE_UNIT_PAGE = "/jsp/modelManager/manageUnit.jsp";

	/**
	 * ҳ�泣�� -- ��λ��Ϣҳ��
	 */
	public static final String UNIT_INFO_PAGE = "/jsp/modelManager/unitInfo.jsp";

	/**
	 * ҳ�泣�� -- �ڵ�����ҳ��
	 */
	public static final String VALIDATE_RESULT_PAGE = "/jsp/modelManager/validateResult.jsp";

	/**
	 * ҳ�泣�� -- �������ݻ���ҳ��
	 */
	public static final String SUM_PAGE = "/jsp/modelManager/sum.jsp";

	/**
	 * ҳ�泣�� -- �������ݽڵ����ҳ��
	 */
	public static final String MODELMANAGER_SUMNODE_PAGE = "/jsp/modelManager/sumNode.jsp";

	/**
	 * ҳ�泣�� -- �������ݽڵ����ҳ��
	 */
	public static final String UNITMETATABLE_MANAGER_PAGE = "/jsp/modelManager/unitMetaTableManager.jsp";
	
	/**
	 * ҳ�泣�� -- �������ݽڵ����ҳ��
	 */
	public static final String ADDRESS_MANAGER_PAGE = "/jsp/modelManager/addressManager.jsp";

	/**
	 * ҳ�泣�� -- �߱���ϸ��Ϣҳ��
	 */
	public static final String ADDRESS_INFO_PAGE = "/jsp/modelManager/addressInfo.jsp";
	/**
	 * ҳ�泣�� -- �߱���ϸ��Ϣҳ��
	 */
	public static final String UNITMETA_INFO_PAGE = "/jsp/modelManager/unitMetaInfo.jsp";

	/**
	 * ҳ�泣�� --
	 */
	public static final String SHOW_DATA_PAGE = "/jsp/modelManager/showModel.jsp";

	/**
	 * ҳ�泣�� --
	 */
	public static final String DATA_MANAGER_PAGE = "/jsp/modelManager/showModelManager.jsp";

	/**
	 * ҳ�泣�� -- ����ѡ����ܷ���ҳ��
	 */
	public static final String SELECT_SUM_SCHEMA_MANAGE_PAGE = "/jsp/modelManager/selectSumSchemaManage.jsp";

	/**
	 * ҳ�泣�� -- ��ʾ����ҳ��߱���Ϣ
	 */
	public static final String ALL_ADDRESS_INFO_PAGE = "/jsp/modelManager/allAddressInfo.jsp";

	/**
	 * �������ͳ��� -- ��ʾ���ݴ���ҳ��
	 */
	public static final String SHOW_DATA_EXCHANGE_PAGE = "showDataExchangePage";

	/**
	 * �������ͳ��� -- �������
	 */
	public static final String SHOW_DATA_AUDIT_PAGE = "dataAuditPage";

	/**
	 * �������ͳ��� -- ��ʾ����ҳ��
	 */
	public static final String SHOW_SUM_PAGE = "showSumPage";

	/**
	 * �������ͳ��� -- ��ȫ����
	 */
	public static final String SHOW_SUMNODE_PAGE = "showSumNodePage";

	/**
	 * �������ͳ��� -- ��ʾ��λ��Ϣҳ��
	 */
	public static final String SHOW_UNIT_INFO_PAGE = "showUnitInfoPage";

	/**
	 * �������ͳ��� -- ��ʾ��λ����ҳ��
	 */
	public static final String SHOW_MANAGE_UNIT_PAGE = "showManageUnitPage";

	/**
	 * �������ͳ��� -- ������λ
	 */
	public static final String CREATE_UNIT = "createUnit";

	/**
	 * �������ͳ��� -- ���µ�λ
	 */
	public static final String UPDATE_UNIT = "updateUnit";

	/**
	 * �������ͳ��� -- ɾ����λ
	 */
	public static final String DELETE_UNIT = "deleteUnit";

	/**
	 * �������ͳ��� -- �ϱ���������
	 */
	public static final String UPLOAD_MODEL = "uploadModel";

	/**
	 * �������ͳ��� -- ��ʾ�ϱ���������ҳ��
	 */
	public static final String SHOW_UPLOAD_MODEL_PAGE = "showUploadModelPage";

	/**
	 * �������ͳ��� -- ���ر�������
	 */
	public static final String DOWNLOAD_MODEL = "downloadModel";

	/**
	 * �������ͳ��� -- ����ѡ����ܷ���
	 */
	public static final String CREATE_SELECT_SUM_SCHEMA = "createSelectSumSchema";

	/**
	 * �������ͳ��� -- ����ѡ����ܷ���
	 */
	public static final String DOWNLOAD_ATTACHMENT = "downloadAttachment";

	/**
	 * �������ͳ��� -- ɾ��ѡ����ܷ���
	 */
	public static final String DELETE_SELECT_SUM_SCHEMA = "deleteSelectSumSchema";

	/**
	 * �������ͳ��� -- ��ʾѡ����ܷ�������ҳ��
	 */
	public static final String SHOW_SELECT_SUM_SCHEMA_MANAGE_PAGE = "showSelectSumSchemaManagePage";

	/**
	 * �������ͳ��� -- ��ʾ���ر�������ҳ��
	 */
	public static final String SHOW_DOWNLOAD_MODEL_PAGE = "showDownloadModelPage";

	/**
	 * �������ͳ��� -- ��ȫ����
	 */
	public static final String SUM_ALL = "sumAll";

	/**
	 * �������ͳ��� -- �����ڵ����
	 */
	public static final String ADJUST_NODE_DIFF = "adjustNodeDiff";

	/**
	 * �������ͳ��� -- �ڵ����
	 */
	public static final String SUM_NODE = "sumNode";

	/**
	 * �������ͳ��� -- ִ��ѡ����ܷ���
	 */
	public static final String EXECUTE_SELECT_SUM_SCHEMA = "executeSelectSumSchema";

	/**
	 * �������ͳ��� -- ��鼯�Ż��ܽڵ㵥λ�µ����нڵ���ܽ���Ƿ���ȷ
	 */
	public static final String VALIDATE_NODE_SUM = "validateNodeSum";
	
	/**
	 * �������ͳ��� -- ��ʾ�������Ϣ����ҳ��
	 */
	public static final String SHOW_UNITMETATABLE_INFO_PAGE = "showUnitMetaTableInfoPage";
	
	/**
	 * �������ͳ��� -- ��ʾ�߱���Ϣ����ҳ��
	 */
	public static final String SHOW_ADDRESS_INFO_PAGE = "showAddressInfoPage";

	/**
	 * �������ͳ��� -- ��Ӵ߱���Ϣ
	 */
	public static final String ADD_ADDRESS_INFO = "addAddressInfo";

	/**
	 * �������ͳ��� -- ��������id�õ��������������Ϣ
	 */
	public static final String GET_ADDRESS_INFO_BYTASKID = "getAddressInfoByTaskID";
	
	/**
	 * �������ͳ��� -- ��������id�õ�������ķ����������Ϣ
	 */
	public static final String GET_UNITMETA_INFO_BYTASKID = "getUnitMetaInfoByTaskID";
	
	
	/**
	 * �������ͳ��� -- ��ʾ����
	 */
	public static final String SHOW_DATA = "showData";

	/**
	 * �������ͳ��� -- ��ʾ����
	 */
	public static final String DOWN_EXCEL = "downExcel";

	/**
	 * �������ͳ��� -- ��ʾ���ݹ���ҳ��
	 */
	public static final String SHOW_DATA_MANAGER = "showDataManager";

	/**
	 * �������� -- ����
	 */
	public static final String HTML_DATA = "htmlData";

	/**
	 * �������ͳ��� -- ��ʾ���ݹ���ҳ��
	 */
	public static final String DELETE_ADDRESS_INFO = "deleteAddressInfo";

	/**
	 * �������ͳ��� -- ��ʾ���д߱���Ϣ
	 */
	public static final String DISPLAY_ALL_ADDRESS_INFO = "displayAllAddressInfo";

	/**
	 * �������ͳ��� -- �����ӡ
	 */
	public static final String PRINT_ONE_TABLE = "printOneTable";

	/**
	 * �������ͳ��� -- �ױ��ӡ
	 */
	public static final String PRINT_ALL_TABLE = "printAllTable";

	public static final String PRINT_PDF = "printPdf";

	/**
	 * �������ͳ��� -- ��ҳ��������
	 */
	public static final String DO_DATA_AUDIT = "doDataAudit";

	/**
	 * �������ͳ��� -- ����EXCEL���ű�
	 */
	public static final String GET_EXCEL_FOR_SINGLE_TABLE = "getExcelForSingleTable";

	/**
	 * �������ͳ��� -- ����EXCEL���ױ�
	 */
	public static final String GET_EXCEL_FOR_ALL_TABLE = "getExcelForAllTable";

	/**
	 * �������ͳ��� -- ����PDF���ű�
	 */
	public static final String GET_PDF_FOR_SINGLE_TABLE = "getPDFForSingleTable";

	/**
	 * �������ͳ��� -- ����PDF���ױ�
	 */
	public static final String GET_PDF_FOR_ALL_TABLE = "getPDFForAllTable";

	//�༭����
	public static final String EIDT_DATA = "edit_data";

	//�༭����ҳ��
	public static final String EIDT_DATA_PAGE = "/jsp/modelManager/editModel.jsp";

	//��������
	public static final String SAVE_DATA = "save_data";

	public static final String SHOW_UNIT_TREE = "showUnitTree";

	private static final String SHOW_UNIT_TREE_PAGE = "/jsp/modelManager/showUnitTree.jsp";

	public static final String SELECT_SUM = "selectSum";

	private static final String SELECT_SUM_PAGE = "/jsp/modelManager/selectsum.jsp";

	public static final String DO_SELECT_SUM = "doSelectSum";
	
	public static final String MODIFY_UNIT_DISPLAY="modify_unit_display";

	public static final String DO_SELECT_SUM_OK_PAGE = "/jsp/modelManager/selectsum_ok.jsp";

	//�������
	public static final String ENVELOPSUBMITDATA = "envelopsubmitdata";

	//��������
	public static final String UNENVELOPSUBMITDATA = "unenvelopsubmitdata";

	//��ӡ��λ��
	public static final String PRINT_UNITTREE = "print_unittree";

	//
	public static final String PRINT_UNITTREE_PAGE = "/jsp/modelManager/printunittree.jsp";

	//���ظ���
	public static final String UPLOAD_ATTACH = "uploadattach";

	//��ʾ����
	public static final String LIST_ATTACH = "listattach";

	//��ʾ����
	public static final String SHOW_ATTACH = "showattach";

	//ɾ������
	public static final String DELETE_ATTACH = "deleteattach";

	//��ʾ����ҳ��
	public static final String LIST_ATTACH_PAGE = "/jsp/modelManager/attachlist.jsp";

	//��ѯ��֯��Ԫ���νṹ
	public static final String SHOW_UNITTREE_BY_NAMEORCODE="showUnitTreeByNameOrCode";
	
	//����UNITID��ѯ������е���֯��Ԫ
	public static final String LOAD_UNITDATA_BY_UNITID = "loadDataByUnitid";
	
	//����UNITID���������е���֯��Ԫ
	public static final String SAVE_UNITDATA_BY_UNITID = "saveUnitByUnitid";
	
	/**
	 *���·�����е���֯��Ԫ 
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
	 *���ݷ�����е���֯��Ԫ 
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
	 * ��������Ĳ�ѯ�������ض�Ӧ�������б�
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
//		String name = "������չ�����������ι�˾";
		String idinfo = request.getParameter("taskID");
		String name = new String(request.getParameter("name").getBytes("iso8859-1"),"utf-8");
//		System.out.println("nameinfo----->"+nameinfo+"<-----");
//		System.out.println("----->"+aa+"<-----");
//		System.out.println(new String(aa.getBytes("iso8859-1"),"gbk"));
//		System.out.println("��ѯ��֯��Ԫ���νṹ������Ϣ!");
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
	 * ��ʾ���ݴ���ҳ��
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
	 * ��ʾ���ҳ��
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
	 * ��ѯ���д߱���Ϣ
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
		//��ǰ�ǵڼ�ҳ
		String curPage = "1";
		if (request.getParameter("curPage") != null) {
			curPage = request.getParameter("curPage");
		}
		//��ʾҳ��
		int maxRowCount = addressInfo.size(); //һ���ж�����
		int rowsPerPage = cn.com.youtong.apollo.services.Config
				.getInt("cn.com.youtong.apollo.webconfig.pageNum"); //ÿҳ�ж�����
		int maxPage = 1; //һ���ж���ҳ
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
	 * ɾ���߱���Ϣ
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
	 * ��ʾ����������Ϣҳ��
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
	 * ��ʾ����߱���Ϣҳ��
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
	 * ��Ӵ߱���Ϣ
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
	 * ��������ID�õ��������Ϣ
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
	 * ��������ID�õ��߱���Ϣ
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
	 * �ڵ����
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

		//��¼������־
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "�Ե�λ��" + unitID + "����"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "����ִ�нڵ���ܲ���";
		if (isRecursive) {
			message += "�������ӽڵ㣩";
		}

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		envelopsubmitData(request, response);

		//go2InfoPage("�ڵ���ܳɹ�", "model?operation=" + SHOW_SUM_PAGE, request,
		// response);

	}

	/**
	 * ִ��ѡ����ܷ���
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

		//��¼������־
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "��"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "����ִ��ѡ����ܷ�����" + schema.getName() + "��";

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2InfoPage("ִ��ѡ����ܷ����ɹ�", javascriptReturnUrl(), request, response);
	}

	/**
	 * ��鼯�Ż��ܽڵ㵥λ�µ����нڵ���ܽ���Ƿ���ȷ
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
	 * ��ʾ�ϱ���������ҳ��
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
	 * ��Zip�ļ��еõ�Model������
	 * 
	 * @param zipFile
	 *            zip�ļ�����
	 * @return Model������
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
			throw new ModelException("��ȡ�����ļ�ʧ��");
		}
		throw new ModelException("û���ҵ������ļ�");
	}

	/**
	 * �ϱ���������
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
		//���ϴ��ı������������еõ�taskID
		UploadBean upload = new UploadBean(getServletConfig(), request,
				response);
		File file = upload.getUploadFile();
		InputStream xmlInputSteam = getModelInputStream(file);
		String taskID = TaskIDFinder.findTaskID(xmlInputSteam);
		if (!task.id().equals(taskID)) {
			throw new Warning("�ϱ��������뵱ǰ����ƥ��");
		}
		xmlInputSteam.close();

		xmlInputSteam = getModelInputStream(file);
		ModelManager modelManager = ((ModelManagerFactory) Factory
				.getInstance(ModelManagerFactory.class.getName()))
				.createModelManager(taskID);

		User user = getLoginUser(request);

		//�ϱ�����
		LoadResult loadResult = modelManager.getDataImporter().importData(
				xmlInputSteam, modelManager.getUnitACL(user));
		xmlInputSteam.close();

		//��¼�ϱ���־
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		logManager.logLoadDataEvent(loadResult, user, request.getRemoteAddr(),
				LogManager.WEB_MODE);

		this.go2NoLogoInfoPage(loadResult.getMessage(), javascriptReturnUrl(),
				request, response);
	}

	/**
	 * �õ��ű�������
	 * 
	 * @param scripts
	 *            �ű�Script����
	 * @return Map�ļ���
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
	 * �õ���˽���Ĵ�����Ϣ
	 * 
	 * @param auditResult
	 *            ��˽��
	 * @return ��˽���Ĵ�����Ϣ
	 */
	private String getErrorMessage(AuditResult auditResult) {
		List errors = auditResult.getErrors();
		StringBuffer errorMessage = new StringBuffer();
		errorMessage.append("������" + errors.size() + ":\n");
		for (int i = 0; i < errors.size(); i++) {
			errorMessage.append("��" + (i + 1) + "��" + errors.get(i) + "\n");
		}
		return errorMessage.toString();
	}

	/**
	 * �õ���˽���ľ�����Ϣ
	 * 
	 * @param auditResult
	 *            ��˽��
	 * @return ��˽���ľ�����Ϣ
	 */
	private String getWarningMessage(AuditResult auditResult) {
		List warnings = auditResult.getWarnings();
		StringBuffer warningMessage = new StringBuffer();
		warningMessage.append("������" + warnings.size() + ":\n");
		for (int i = 0; i < warnings.size(); i++) {
			warningMessage.append("��" + (i + 1) + "��" + warnings.get(i) + "\n");
		}
		return warningMessage.toString();
	}

	/**
	 * ��ʾ���ر�������ҳ��
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
	 * ���ر�������
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

		//����response
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
	 * ���ر��������еĸ���
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

		//����response
		response.setContentType("application/x-zip-compressed; charset=gb2312");
		response.setHeader("Content-disposition", "attachment; filename="
				+ unitID + "-����.zip");
		OutputStream out = response.getOutputStream();

		modelManager.getDataExporter().exportAttachment(unitID, taskTime, out,
				modelManager.getUnitACL(getLoginUser(request)));
		out.close();
	}

	/**
	 * ��ʾѡ����ܷ�������ҳ��
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
	 * ����ѡ����ܷ���
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
	 * ɾ��ѡ����ܷ���
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
	 * �����ڵ����
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

		//��¼������־
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();

		String message = "�Ե�λ��" + unitID + "����"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "����ִ�е����������";
		if (isRecursive) {
			message += "�������ӽڵ㣩";
		}

		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2InfoPage("�����ڵ����ɹ�", "model?operation=" + SHOW_SUM_PAGE, request,
				response);
	}

	/**
	 * ��ȫ����
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

		go2InfoPage("��ȫ���ܳɹ�", javascriptReturnUrl(), request, response);
	}

	/**
	 * ��ʾ������ҳ��
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
	 * ��ʾ�ڵ����ҳ��
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
	 * ��ʾ���� //����һ�����Է���
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
	 * ��ʾ����
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

		//��ʾҳ��
		go2UrlWithAttibute(request, response, SHOW_DATA_PAGE);
	}

	/**
	 * ���ݵȼ��õ���Ҫ��ʾ�ı�
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
	 * �Ƿ���Ȩ�ޱ༭����
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
	 * �Ե���ҳ��������
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

		//û�нű�
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

		//ִ�нű�
		if (isexec == 0) {
			for (int i = 0; i < unitID.length; i++) {
				DBUtils.singleExec(unitID[i], task, new Integer(taskTimeID),
						getScriptContent(suit.getCalculateScriptToExec()));
			}

			request.setAttribute("execerror", "ok");
		}
		//���
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

		//�ű�����
		if (auditResultList == null) {
			request.setAttribute("auditScript", "");
		} else {
			request.setAttribute("auditResultList", auditResultList);
		}

		go2UrlWithAttibute(request, response, this.DATA_AUDITRESULT_PAGE);
	}

	/**
	 * ��EXCEL��ʽ���浥�ű�
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * ��EXCEL��ʽ�������ױ�
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * ��PDF��ʽ���浥�ű�
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * ��PDF��ʽ���浥�ű�
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * ���ر������ݣ�Ȼ���ʽ��Ϊweb ��ʾ���(Table)�� ������ڸ��������浽request���档
	 * 
	 * @param request
	 * @param response
	 * @return ���ظ�ʽ�������ֱ����web��ʾ�ı��
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
				throw new Warning("ָ��������ʱ�䲻����");
			}

			ArrayList showTables = null;

			//����ר��
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
			//��������
			StringWriter out = new StringWriter();

//			modelManager.getDataExporter().exportData(unitID, taskTime, out,acl,showTables);    //����ר��
			modelManager.getDataExporter().exportData(unitID, taskTime, out,acl);
 
			ByteArrayInputStream xmlIn = new ByteArrayInputStream(out
					.toString().getBytes("gb2312"));
			Iterator itr = UtilServlet.getHtmlCodeForShowTable(xmlIn, task);
			out.close();

			//����
			ByteArrayOutputStream attOut = new ByteArrayOutputStream();
			modelManager.getDataExporter().exportAttachment(unitID, taskTime,
					attOut, acl);
			if (attOut.toByteArray().length > 0) {
				request.setAttribute("hasAttachment", "true");
			}
			attOut.close();

			return itr;
		} else {
			//��ʾ�ձ���
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
	 * ��ӡ�������ݣ�Ŀǰ����֧�ָ�����ӡ
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
		out.write("<html>\r\n".getBytes()); // ��ʾ��html��htmlΪ��Ԫ�ء�

		// ������ܴ��ڶ��table
		while (iter.hasNext()) {
			TableViewer tv = (TableViewer) iter.next();
			out.write("<h2>��: ".getBytes());
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
	 * ����excel
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * �����ӡ
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		String tableID = request.getParameter("tableID");

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));

		//����response
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
	 * �õ���ͷHtml
	 * 
	 * @param unit
	 *            ��λ
	 * @param taskTime
	 *            taskTime
	 * @param tableName
	 *            tableName
	 * @return ��ͷHtml
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
	 * ��ʾ��λ����ҳ��
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
	 * ��ʾ��λ��Ϣҳ��
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
	 * ������λ
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
				//����ѡ����ܵ�λ�����µ�λ��Ȩ�޸�����ǰ�û�������
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
	 * ���µ�λ
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
	 * ɾ����λ
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
	 * �ױ��ӡ
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
			throw new Warning("ָ��������ʱ�䲻����");
		}

		StringWriter writer = new StringWriter();
		modelManager.getDataExporter().exportData(unitID, taskTime, writer,
				modelManager.getUnitACL(getLoginUser(request)));
		//����response
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
	 * ҳ��ַ�
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
			throw new Warning("��Ч�Ĳ���operation = " + operation);
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
			throw new Warning("��Ч�Ĳ���operation = " + operation);
		}
	}

	/**
	 * ��ӡ��λ��
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
		strBuf.append("var tree = new WebFXTree('��λ��');\n");
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
	 * �õ���λ��XML�ļ�
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
	 * ��������
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

		//�ж��Ƿ��ܹ�����
		if (!analyseManager.canUnenvelopSubmitData(taskID, tasktimeID, unitID)) {

			go2InfoPage("�����ϼ�û���������ݣ�����" + unitID + "���������ϱ�����",
					"model?operation=" + SHOW_SUM_PAGE, request, response);
			return;
		}

		List unitList = new ArrayList();
		unitList.add(unitID);

		analyseManager.unEnvelopSubmitData(taskID, tasktimeID, unitList, flag);

		go2InfoPage("��������ɹ�", "model?operation=" + SHOW_SUM_PAGE, request,
				response);

	}

	/**
	 * �������
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

		go2InfoPage("���ݷ��ɹ�", "model?operation=" + SHOW_SUM_PAGE, request,
				response);

	}

	/**
	 * ��ʾ�༭ҳ��,�������е�showData��������������֮���XML����������ű�����
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
			throw new Warning("ָ��������ʱ�䲻����");
		}
		request.setAttribute("taskTime", Convertor.date2ShortString(taskTime
				.getFromTime()));
		UnitACL acl = modelManager.getUnitACL(getLoginUser(request));
		//��������
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

		//��ʾҳ��
		go2UrlWithAttibute(request, response, EIDT_DATA_PAGE);

	}

	/**
	 * ��������
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

		//@todo�����ݱ���
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
	 * Ĭ������£���ʾcheckbox��λ��
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

			// ���õ�λ������
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

		//��¼������־
		LogManager logManager = ((LogManagerFactory) Factory
				.getInstance(LogManagerFactory.class.getName()))
				.createLogManager();
		String message = "�Ե�λ��" + unitID + "����"
				+ Convertor.date2MonthlyString(taskTime.getFromTime())
				+ "����ִ��ѡ����ܲ���";
		logManager.logDataEvent(new Date(), Event.INFO,
				request.getRemoteAddr(), getLoginUser(request).getName(),
				message);

		go2UrlWithAttibute(request, response, DO_SELECT_SUM_OK_PAGE);
	}

	/**
	 * ���¸���
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
			throw new Warning("���ش���", e);
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
	 * �����б�
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
	 * ��ʾ�б�
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
	 * ɾ������
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