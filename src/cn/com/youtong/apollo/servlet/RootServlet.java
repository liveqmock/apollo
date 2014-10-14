package cn.com.youtong.apollo.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.rmi.*;
import java.util.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.common.Warning;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.net.URLDecoder;
/**
 * 所有servlet的父类。
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 世纪友通</p>
 * @author 李巍
 * @version 1.0
 */
public abstract class RootServlet extends HttpServlet
{
	/*页面路径常数定义*/
	public static final String LOGIN_PAGE = "../index.jsp";
	/**成功页面*/
	public static final String INFO_PAGE = "/jsp/info.jsp";
	/**错误页面*/
	public static final String ERROR_PAGE = "/jsp/error.jsp";
	/**首页*/
	public static final String SHOW_HOME_PAGE = "/jsp/mainForm.jsp";
	/**单位树*/
	public static final String SHOW_UNIT_TREE = "/jsp/userManager/unit/unitTree.jsp";
	/**无logo信息页面*/
	public static final String NO_LOGO_INFO_PAGE = "/jsp/noLogoInfo.jsp";
	/**登录用户参数*/
	public static final String LOGIN_USER = "loginUser";
	/**成功信息*/
	public static final String INFO_MESSAGE = "infomation";

	/**返回的url*/
	public static final String RETURN_URL = "returnUrl";

	/**
	 * 单位树常量
	 */
	public static final String UNIT_TREE = "unitTree";

	private static Log log = LogFactory.getLog("AppoloServlet");

	/**
	 * 用户请求的处理和分发
	 */
	public abstract void perform(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning;

	/**
	 * 转到出错页面。
	 * @param req
	 * @param res
	 */
	public void go2ErrorPage(String info,HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
          	req.setAttribute("info",info);
		go2UrlWithAttibute(req, res, ERROR_PAGE);
	}

	/**
	 * 检查用户身份是否已验证（即登录）.如有需要，子类应该覆盖此方法。
	 * @param req
	 * @return 已登录或不用登录则返回true，否则返回false
	 */
	protected boolean isAuthenticated(HttpServletRequest req)
	{
		if(!this.getServletName().equalsIgnoreCase("login") && req.getSession().getAttribute(LOGIN_USER) == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 得到当前登录的用户
	 * @param req
	 * @return 当前登录的用户
	 */
	public static User getLoginUser(HttpServletRequest req)
	{
		return(User) req.getSession().getAttribute(LOGIN_USER);
	}

	public void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		try
		{
            req.setCharacterEncoding("GB2312");
			if(!isAuthenticated(req))
			{
				this.go2Url(req, res, this.LOGIN_PAGE);
				return;
			}
            //任务管理session设置
            if(req.getParameter("managerTaskID") != null){
                   req.getSession().setAttribute("taskID", req.getParameter("managerTaskID"));
                   String taskName = URLDecoder.decode(req.getParameter("managerTaskName"),"utf8");
                   req.getSession().setAttribute("taskName",taskName);
            }
			//菜单状态设置
			if(req.getParameter("sessionTaskID") != null)
			{
				req.getSession().setAttribute("taskID", req.getParameter("sessionTaskID"));
                                String taskName = URLDecoder.decode(req.getParameter("sessionTaskName"),"utf8");
				req.getSession().setAttribute("taskName",taskName);
				req.getSession().setAttribute("mainMenuStatus", req.getParameter("mainMenuStatus"));
				req.getSession().setAttribute("subMenuStatus", req.getParameter("subMenuStatus"));
			}
			else
			{
				if(req.getParameter("mainMenuStatus") != null)
				{
					if(req.getParameter("mainMenuStatus").equals("reportTD"))
					{
						req.getSession().setAttribute("mainMenuStatus", req.getParameter("mainMenuStatus"));
						req.getSession().setAttribute("subMenuStatus", req.getParameter("subMenuStatus"));
					}
					else
					{
						req.getSession().setAttribute("taskID", "");
						req.getSession().setAttribute("taskName", "");
						req.getSession().setAttribute("mainMenuStatus", req.getParameter("mainMenuStatus"));
						req.getSession().setAttribute("subMenuStatus", req.getParameter("subMenuStatus"));
					}
				}
			}

			//分发请求
			perform(req, res);
		}
		catch(Warning w)
		{
			log.debug("RootServlet error", w);
			req.setAttribute("warning", w);
			this.go2ErrorPage(w.getMessage(),req, res);
			return;
		}

	}

	/**
	 * 显示信息页面
	 * @param message 要显示的信息
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void go2InfoPage(String message, HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		go2InfoPage(message, "javascript:window.close()", req, res);
		return;
	}

	/**
	 * 显示信息页面
	 * @param message 要显示的信息
	 * @param returnUrl 要返回的url,
	 *              如果(returnUrl=="" || returnUrl == null)，表明没又返回url
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void go2InfoPage(String message, String returnUrl, HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		req.setAttribute(INFO_MESSAGE, message);
		req.setAttribute(RETURN_URL, returnUrl);
		go2UrlWithAttibute(req, res, INFO_PAGE);
	}
	/**
	 * 显示无Logo信息页面(带滚动条)
	 * @param message 要显示的信息
	 * @param returnUrl 要返回的url,
	 *              如果(returnUrl=="" || returnUrl == null)，表明没又返回url
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void go2NoLogoInfoPage(String message, String returnUrl, HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		req.setAttribute(INFO_MESSAGE, message);
		req.setAttribute(RETURN_URL, returnUrl);
		go2UrlWithAttibute(req, res, this.NO_LOGO_INFO_PAGE);
	}
	/**
	 * 转向指定的Url,会显示在浏览器上显示出新url地址。
	 * @param req
	 * @param res
	 * @param targetUrl
	 * @throws ServletException
	 * @throws IOException
	 */
	public void go2Url(HttpServletRequest req, HttpServletResponse res, String targetUrl)
		throws ServletException, IOException
	{
		res.sendRedirect(targetUrl);
		return;
	}

	/**
	 * 转向指定的Url，并保留request属性表，不会显示在浏览器上显示出新url地址。
	 */
	public void go2UrlWithAttibute(HttpServletRequest req, HttpServletResponse res, String targetUrl)
		throws ServletException, IOException
	{
		if(log.isDebugEnabled())
		{
			log.debug("go2UrlWithAttibute url =" + targetUrl);
		}
		RequestDispatcher rq = req.getRequestDispatcher(targetUrl);
		rq.forward(req, res);
		return;
	}

	/**
	 * 返回主页面
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void go2HomePage(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException
	{
		go2UrlWithAttibute(req, res, SHOW_HOME_PAGE);
	}

	/**
	 * 判断当前登录用户是否有指定的权限
	 * @param privilege 权限常量
	 * @return boolean 有则返回true,否则返回false
	 * @throws Warning 业务异常
	 */
	public static boolean hasPrivilege(HttpServletRequest request, int privilege)
		throws Warning
	{
		Role role = getLoginUser(request).getRole();
		SetOfPrivileges setOfPrivileges = role.getPrivileges();
		//返回权限
		return setOfPrivileges.getPrivilege(privilege);
	}

	/**
	 * javascript返回上一页面的url
	 * @return javascript返回上一页面的url
	 */
	public String javascriptReturnUrl()
	{
		return "javascript:window.history.back(1)";
	}

}
