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
	 * �������--��¼
	 */
	public static final String LOGIN = "login";

	/**
	 * �����--�˳�
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
	 * �û���½
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

		//ȡ����֤��Ϣ
		String accountName = req.getParameter("account");
		String accountPassword = req.getParameter("password");
		AuthEnvironment authEnvironment = new AuthEnvironment(accountName,	accountPassword);
		if(log.isDebugEnabled())
		{
			log.debug("  name= " + accountName);
			log.debug("  password= " + accountPassword);
		}

		//�ж���֤����
		Authentication authentication = ( (AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.
			getName())).getAuthentication(
			authEnvironment);
		if (authentication.authenticate())
		{
			log.debug("  login ok.");
			//����¼�û���Ϣ��ŵ�session��
			req.getSession().setAttribute(LOGIN_USER,authentication.getUser());
                        req.getSession().setAttribute("password",accountPassword);
			//�˵�״̬
			req.getSession().setAttribute("taskID", "");
			req.getSession().setAttribute("taskName", "");
			req.getSession().setAttribute("mainMenuStatus", "");
			req.getSession().setAttribute("subMenuStatus", "");

			//��¼��¼�ɹ���־
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logSecurityEvent(new Date(), Event.AUDIT_SUCCESS,
										req.getRemoteAddr(),
										authentication.getUser().getName(),
										"�ɹ���¼ϵͳ");

			//ת����ҳ
			if(log.isDebugEnabled())
			{
				log.debug(" go to page: " + SHOW_HOME_PAGE);
			}
			this.go2UrlWithAttibute(req, res, SHOW_HOME_PAGE);
		}
		else
		{
			log.debug("  login failed.");
			//��¼��¼ʧ����־
			LogManager logManager = ( (LogManagerFactory) Factory.
									 getInstance(LogManagerFactory.class.
												 getName())).createLogManager();

			logManager.logSecurityEvent(new Date(), Event.AUDIT_FAIL,
										req.getRemoteAddr(),
										authentication.getUser().getName(),
										"�û������벻ƥ�䣬��¼ʧ��");

			req.setAttribute("warning",
							 new Warning("�û��������벻��ȷ�����߸��ʺŻ�û�п�ͨ���������Ա��ϵ��"));
			this.go2ErrorPage("",req, res);
		}
	}

	/**
	 * �˳���½.
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
	 * ��ʾ�û�ע��ҳ��
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
		//��ʾҳ��
		go2UrlWithAttibute(req, res, REGISTER_PAGE);
	}

	/**
	 * ע���û�
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
						 "�����Ϣ�Ѿ�ע��ɹ������ס���ע����Ϣ���ȴ�����Ա��˿�ͨ�󼴿ɵ�¼��ϵͳ...");
		req.setAttribute(RETURN_URL, this.LOGIN_PAGE);
		go2UrlWithAttibute(req, res, "/jsp/newUserInfo.jsp");
	}

	/**
	 *��������ѡ��ִ�еķ���
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
			{ //��ʾע���û���Ϣҳ��
				showRegisterPage(req, res);
				return;
			}
			else if (req.getParameter("operation").equals(REGISTER_USER))
			{ //ע���û�
				registerUser(req, res);
				return;
			}
		}
	}
}
