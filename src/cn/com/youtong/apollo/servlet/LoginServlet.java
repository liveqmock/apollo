package cn.com.youtong.apollo.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.authentication.AuthenticationFactory;
import cn.com.youtong.apollo.authentication.Authentication;
import cn.com.youtong.apollo.authentication.AuthEnvironment;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.log.*;
import cn.com.youtong.apollo.servlet.TaskServlet;

import org.apache.commons.logging.*;

public class LoginServlet
	extends RootServlet
{
	/**
	 * 请求参数--登录
	 */
	public static final String LOGIN = "login";

	/**
	 * 求参数--退出
	 */
	public static final String LOGOUT = "logout";
	public static final String SHOW_REGISTER_PAGE =
		"showRegisterPage";
	public static final String REGISTER_USER =
		"registerUser";
	public static final String REGISTER_PAGE =
		"/jsp/register.jsp";

	private Log log= LogFactory.getLog(LoginServlet.class);

	/**
	 * 用户登陆
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void login(HttpServletRequest req, HttpServletResponse res) throws
		ServletException, IOException, Warning
	{
		log.debug("LoginServlet login()...");

		//取得验证信息
		String accountName = req.getParameter("account");
		String accountPassword = req.getParameter("password");
		AuthEnvironment authEnvironment = new AuthEnvironment(accountName,	accountPassword);
		if(log.isDebugEnabled())
		{
			log.debug("  name= " + accountName);
			log.debug("  password= " + accountPassword);
		}

		//判断验证输入
		Authentication authentication = ( (AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.
			getName())).getAuthentication(
			authEnvironment);
		if (authentication.authenticate())
		{
			log.debug("  login ok.");
			//将登录用户信息存放到session中
			req.getSession().setAttribute(LOGIN_USER,authentication.getUser());
                        req.getSession().setAttribute("password",accountPassword);
			//菜单状态
			req.getSession().setAttribute("taskID", "");
			req.getSession().setAttribute("taskName", "");
			req.getSession().setAttribute("mainMenuStatus", "");
			req.getSession().setAttribute("subMenuStatus", "");

			//记录登录成功日志
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logSecurityEvent(new Date(), Event.AUDIT_SUCCESS,
										req.getRemoteAddr(),
										authentication.getUser().getName(),
										"成功登录系统");

			//转到主页
			if(log.isDebugEnabled())
			{
				log.debug(" go to page: " + SHOW_HOME_PAGE);
			}
			this.go2UrlWithAttibute(req, res, SHOW_HOME_PAGE);
		}
		else
		{
			log.debug("  login failed.");
			//记录登录失败日志
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logSecurityEvent(new Date(), Event.AUDIT_FAIL,
										req.getRemoteAddr(),
										authentication.getUser().getName(),
										"用户名密码不匹配，登录失败");

			req.setAttribute("warning",
							 new Warning("用户名或密码不正确，或者该帐号还没有开通，请与管理员联系！"));
			this.go2ErrorPage("",req, res);
		}
	}

	/**
	 * 退出登陆.
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	private void logout(HttpServletRequest req, HttpServletResponse res) throws
		ServletException, IOException, Warning
	{
		HttpSession session = req.getSession(true);
		//session.setAttribute(LOGIN_USER, null);
		session.invalidate();
		this.go2Url(req, res, this.LOGIN_PAGE);
	}

	/**
	 * 显示用户注册页面
	 * @param req
	 * @param res
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showRegisterPage(HttpServletRequest req,
								  HttpServletResponse res) throws
		Warning, IOException, ServletException
	{
		//显示页面
		go2UrlWithAttibute(req, res, REGISTER_PAGE);
	}

	/**
	 * 注册用户
	 * @param req
	 * @param res
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void registerUser(HttpServletRequest req,
							  HttpServletResponse res) throws
		Warning, IOException, ServletException
	{
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		String enterpriseName = req.getParameter("enterpriseName");
		String lawPersonCode = req.getParameter("lawPersonCode");
		String lawPersonName = req.getParameter("lawPersonName");
		String lawPersonPhone = req.getParameter("lawPersonPhone");
		String contactPerson = req.getParameter("contactPerson");
		String contactPersonPhone = req.getParameter("contactPersonPhone");
		String contactPersonMobile = "";
		if (req.getParameter("contactPersonMobile") != null)
		{
			contactPersonMobile = req.getParameter("contactPersonMobile");
		}
		String contactPersonAddress = req.getParameter("contactPersonAddress");
		String postcode = req.getParameter("postcode");
		String memo = "";
		if (req.getParameter("memo") != null)
		{
			memo = req.getParameter("memo");
		}
		String fax = req.getParameter("fax");
		String email = req.getParameter("email");
		UserManager userManager = ( (UserManagerFactory) Factory.getInstance(
			UserManagerFactory.class.getName())).createUserManager();
		User user = userManager.createUser(userName, password, enterpriseName,
										   lawPersonCode, lawPersonName,
										   lawPersonPhone,
										   contactPerson, contactPersonPhone,
										   contactPersonMobile,
										   contactPersonAddress, postcode,
										   email,
										   fax,
										   false, memo, new Integer(2),
										   new Integer[0]);
		req.setAttribute(INFO_MESSAGE,
						 "你的信息已经注册成功，请记住你的注册信息，等待管理员审核开通后即可登录本系统...");
		req.setAttribute(RETURN_URL, this.LOGIN_PAGE);
		go2UrlWithAttibute(req, res, "/jsp/newUserInfo.jsp");
	}

	/**
	 *　根据数选择执行的方法
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 * @throws Warning
	 */
	public void perform(HttpServletRequest req,
						HttpServletResponse res) throws ServletException,
		IOException, Warning
	{
		log.debug("Call login servlet...");
		if (req.getParameter("operation") != null)
		{
			if (req.getParameter("operation").equals(LOGIN))
			{
				login(req, res);
				return;
			}
			else if (req.getParameter("operation").equals(LOGOUT))
			{
				logout(req, res);
				return;
			}
			else if (req.getParameter("operation").equals(SHOW_REGISTER_PAGE))
			{ //显示注册用户信息页面
				showRegisterPage(req, res);
				return;
			}
			else if (req.getParameter("operation").equals(REGISTER_USER))
			{ //注册用户
				registerUser(req, res);
				return;
			}
		}
	}
}
