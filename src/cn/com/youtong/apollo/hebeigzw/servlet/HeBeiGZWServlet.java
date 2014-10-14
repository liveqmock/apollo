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
	 * 页面常数 -- 选择汇总方案页面
	 **/
	public static final String SELECT_SUMMODE_PAGE = "/jsp/hebeigzw/selectSumSchemaManage.jsp";
	
	
	/**
	 * 请求常数 --  根据请求的值跳转到选择汇总方案页面
	 **/
	public static final String SHOW_SELECT_SUMMODE_PAGE="showSelectSumModePage";
	/**
	 * 请求常数 --  根据请求的值利用AJAX返回数据
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
	 *获得汇总的reslut即HTML形式的数据 
	 **/
	public void getSummaryInfoByTimeAndTypeAndTaskId(HttpServletRequest req,HttpServletResponse res)
	{
		System.out.println("可以的");
		String summarytype = req.getParameter("summarytype");
		System.out.println("summarytype====>"+summarytype);
		String summarytime = req.getParameter("summarytime");
		System.out.println("summarytime====>"+summarytime);
		
		
	  	outString("可以的",res);
	}
	
	/**
	 * 用户的请求处理与分发
	 **/
	@Override
	public void perform(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, Warning {
		//获取task放到request作用域里面
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
	 * 跳转到选择汇总页面
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
