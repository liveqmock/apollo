package cn.com.youtong.apollo.hebeigzw.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.youtong.apollo.common.Warning;
import cn.com.youtong.apollo.services.Factory;
import cn.com.youtong.apollo.servlet.RootServlet;
import cn.com.youtong.apollo.task.TaskManager;
import cn.com.youtong.apollo.task.TaskManagerFactory;

public class HeBeiGZWServlet extends RootServlet {
	/**
	 * ҳ�泣�� -- ѡ����ܷ���ҳ��
	 **/
	public static final String SELECT_SUMMODE_PAGE = "/jsp/hebeigzw/selectSumSchemaManage.jsp";
	
	
	/**
	 * ������ --  ���������ֵ��ת��ѡ����ܷ���ҳ��
	 **/
	public static final String SHOW_SELECT_SUMMODE_PAGE="showSelectSumModePage";
	/**
	 * ������ --  ���������ֵ����AJAX��������
	 **/
	public static final String GET_SummaryInfo_By_Time_Type_TaskId = "getSummaryInfoByTimeAndTypeAndTaskId";
	
	public void outString(String str,HttpServletResponse res) {
		try {
			res.setContentType("text/html;charset=gb2312");
			PrintWriter out = res.getWriter();
			out.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *��û��ܵ�reslut��HTML��ʽ������ 
	 **/
	public void getSummaryInfoByTimeAndTypeAndTaskId(HttpServletRequest req,HttpServletResponse res)
	{
		System.out.println("���Ե�");
		String summarytype = req.getParameter("summarytype");
		System.out.println("summarytype====>"+summarytype);
		String summarytime = req.getParameter("summarytime");
		System.out.println("summarytime====>"+summarytime);
		
		
	  	outString("���Ե�",res);
	}
	
	/**
	 * �û�����������ַ�
	 **/
	@Override
	public void perform(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, Warning {
		//��ȡtask�ŵ�request����������
		TaskManager taskManager = ((TaskManagerFactory) Factory
				.getInstance(TaskManagerFactory.class.getName()))
				.createTaskManager();
		String taskID = (String) req.getSession().getAttribute("taskID");
		req.setAttribute("task", taskManager.getTaskByID(taskID));
		
		String option = req.getParameter("operation");
		
		if (SHOW_SELECT_SUMMODE_PAGE.equalsIgnoreCase(option)) {
			showSelectSumModePage(req,res);
		}else if(GET_SummaryInfo_By_Time_Type_TaskId.equalsIgnoreCase(option)){
			getSummaryInfoByTimeAndTypeAndTaskId(req,res);
		}
		
		
		
		
	}
	/**
	 * ��ת��ѡ�����ҳ��
	 * 
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showSelectSumModePage(HttpServletRequest request,
			HttpServletResponse response) throws Warning, IOException,
			ServletException {
		go2UrlWithAttibute(request, response, SELECT_SUMMODE_PAGE);
	}
	
	
}
