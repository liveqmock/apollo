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
 * ����servlet�ĸ��ࡣ
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������ͨ</p>
 * @author ��Ρ
 * @version 1.0
 */
public abstract class RootServlet extends HttpServlet
{
	/*ҳ��·����������*/
	public static final String LOGIN_PAGE = "../index.jsp";
	/**�ɹ�ҳ��*/
	public static final String INFO_PAGE = "/jsp/info.jsp";
	/**����ҳ��*/
	public static final String ERROR_PAGE = "/jsp/error.jsp";
	/**��ҳ*/
	public static final String SHOW_HOME_PAGE = "/jsp/mainForm.jsp";
	/**��λ��*/
	public static final String SHOW_UNIT_TREE = "/jsp/userManager/unit/unitTree.jsp";
	/**��logo��Ϣҳ��*/
	public static final String NO_LOGO_INFO_PAGE = "/jsp/noLogoInfo.jsp";
	/**��¼�û�����*/
	public static final String LOGIN_USER = "loginUser";
	/**�ɹ���Ϣ*/
	public static final String INFO_MESSAGE = "infomation";

	/**���ص�url*/
	public static final String RETURN_URL = "returnUrl";

	/**
	 * ��λ������
	 */
	public static final String UNIT_TREE = "unitTree";

	private static Log log = LogFactory.getLog("AppoloServlet");

	/**
	 * �û�����Ĵ���ͷַ�
	 */
	public abstract void perform(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException, Warning;

	/**
	 * ת������ҳ�档
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
	 * ����û�����Ƿ�����֤������¼��.������Ҫ������Ӧ�ø��Ǵ˷�����
	 * @param req
	 * @return �ѵ�¼���õ�¼�򷵻�true�����򷵻�false
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
	 * �õ���ǰ��¼���û�
	 * @param req
	 * @return ��ǰ��¼���û�
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
            //�������session����
            if(req.getParameter("managerTaskID") != null){
                   req.getSession().setAttribute("taskID", req.getParameter("managerTaskID"));
                   String taskName = URLDecoder.decode(req.getParameter("managerTaskName"),"utf8");
                   req.getSession().setAttribute("taskName",taskName);
            }
			//�˵�״̬����
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

			//�ַ�����
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
	 * ��ʾ��Ϣҳ��
	 * @param message Ҫ��ʾ����Ϣ
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
	 * ��ʾ��Ϣҳ��
	 * @param message Ҫ��ʾ����Ϣ
	 * @param returnUrl Ҫ���ص�url,
	 *              ���(returnUrl=="" || returnUrl == null)������û�ַ���url
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
	 * ��ʾ��Logo��Ϣҳ��(��������)
	 * @param message Ҫ��ʾ����Ϣ
	 * @param returnUrl Ҫ���ص�url,
	 *              ���(returnUrl=="" || returnUrl == null)������û�ַ���url
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
	 * ת��ָ����Url,����ʾ�����������ʾ����url��ַ��
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
	 * ת��ָ����Url��������request���Ա�������ʾ�����������ʾ����url��ַ��
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
	 * ������ҳ��
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
	 * �жϵ�ǰ��¼�û��Ƿ���ָ����Ȩ��
	 * @param privilege Ȩ�޳���
	 * @return boolean ���򷵻�true,���򷵻�false
	 * @throws Warning ҵ���쳣
	 */
	public static boolean hasPrivilege(HttpServletRequest request, int privilege)
		throws Warning
	{
		Role role = getLoginUser(request).getRole();
		SetOfPrivileges setOfPrivileges = role.getPrivileges();
		//����Ȩ��
		return setOfPrivileges.getPrivilege(privilege);
	}

	/**
	 * javascript������һҳ���url
	 * @return javascript������һҳ���url
	 */
	public String javascriptReturnUrl()
	{
		return "javascript:window.history.back(1)";
	}

}
