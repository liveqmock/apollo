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
	 * ҳ�泣�� -- olap����ҳ��
	 */
	public static final String MANAGE_OLAP_PAGE =
		"/jsp/olapManager/olapManage.jsp";

	/**
	 * ҳ�泣�� -- OLAPҳ��
	 */
	public static final String OLAP_PAGE =
		"/jsp/olapManager/olap.jsp";

	/**
	 * �������ͳ��� -- ��ʾOLAP����ҳ��
	 */
	public static final String SHOW_MANAGE_OLAP_PAGE = "showManageOlapPage";

	/**
	 * �������ͳ��� -- ��ʾOLAPҳ��
	 */
	public static final String SHOW_OLAP_PAGE = "showOlapPage";

	/**
	 * ��ʾOLAP����ҳ��
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
	 * ��ʾolapҳ��
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
	 * ҳ��ַ�
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

		//Ȩ���ж�
		if (!hasPrivilege(request, SetOfPrivileges.MANAGE_TASK))
		{
			throw new Warning("��û��ִ�иò�����Ȩ��");
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
			throw new Warning("��Ч�Ĳ���operation = " + operation);
		}
	}
}
