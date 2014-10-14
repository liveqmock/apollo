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
 * Olap servlet
 */
public class OlapServlet
	extends RootServlet
{
	/**
	 * 页面常数 -- olap管理页面
	 */
	public static final String MANAGE_OLAP_PAGE =
		"/jsp/olapManager/olapManage.jsp";

	/**
	 * 页面常数 -- OLAP页面
	 */
	public static final String OLAP_PAGE =
		"/jsp/olapManager/olap.jsp";

	/**
	 * 请求类型常量 -- 显示OLAP管理页面
	 */
	public static final String SHOW_MANAGE_OLAP_PAGE = "showManageOlapPage";

	/**
	 * 请求类型常量 -- 显示OLAP页面
	 */
	public static final String SHOW_OLAP_PAGE = "showOlapPage";

	/**
	 * 显示OLAP管理页面
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showManageOlapPage(HttpServletRequest request,
									 HttpServletResponse response) throws
		Warning, IOException, ServletException
	{
		go2UrlWithAttibute(request, response, MANAGE_OLAP_PAGE);
	}

	/**
	 * 显示olap页面
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showOlapPage(HttpServletRequest request,
								 HttpServletResponse response) throws
		Warning, IOException, ServletException
	{
		go2UrlWithAttibute(request, response, OLAP_PAGE);
	}

	/**
	 * 页面分发
	 * @param req
	 * @param res
	 * @throws cn.com.youtong.apollo.common.Warning
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	public void perform(HttpServletRequest request,
						HttpServletResponse response) throws cn.com.youtong.
		apollo.common.Warning, java.io.IOException,
		javax.servlet.ServletException
	{
		String operation = request.getParameter("operation");

		//权限判断
		if (!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK))
		{
			throw new Warning("您没有执行该操作的权限");
		}

		if (operation != null && operation.equalsIgnoreCase(SHOW_MANAGE_OLAP_PAGE))
		{
			showManageOlapPage(request, response);
			return;
		}
		else if (operation != null &&
			operation.equalsIgnoreCase(SHOW_OLAP_PAGE))
		{
			showOlapPage(request, response);
			return;
		}
		else
		{
			throw new Warning("无效的参数operation = " + operation);
		}
	}
}
