package cn.com.youtong.apollo.authentication.db;

import cn.com.youtong.apollo.authentication.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.usermanager.db.*;
import cn.com.youtong.apollo.services.*;
import org.apache.fulcrum.factory.FactoryException;
import org.apache.commons.logging.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DBAuthentication
	implements Authentication
{
	private Log log = LogFactory.getLog(this.getClass());
	/**�����û����ʺ�*/
	private static String account;

	/**�����û�������*/
	private static String password;

	/**
	 * ���캯��
	 * @param env ��������
	 * @throws UserManagerException
	 */
	public DBAuthentication(AuthEnvironment env)
		throws UserManagerException
	{

		if(env == null)
		{
			throw new UserManagerException("��֤��ϢΪ��");
		}

		this.account = env.getSecurityPrincipal();
		if((this.account == null) || (this.account.length() == 0))
		{
			throw new UserManagerException("�û��ʺ�Ϊ��");
		}

		this.account = this.account;
		this.password = (String) env.getSecurityCredentials();
	}

	/**
	 * ��֤�û�
	 * @return��boolean
	 * @throws UserManagerException
	 */
	public boolean authenticate()
		throws UserManagerException
	{
        User user = getUser();

		if(user == null || !user.isValidated())
		{
			return false;
		}

		String dbPassword = user.getPassword();
		boolean verified = false;
		verified = verifyPassword(password, dbPassword);
		if(!verified)
		{
			return false;
		}
		return true;
	}

	/**
	 * ����û�����
	 * @return User
	 * @throws UserManagerException
	 */
	public User getUser()
		throws UserManagerException
	{
		UserManager userManager = null;
		try
		{
			userManager = ((UserManagerFactory) Factory.getInstance(UserManagerFactory.class.getName())).createUserManager();
		}
		catch(FactoryException ex)
		{
			log.error(ex);
			throw new UserManagerException(ex);
		}
		return userManager.getUserByName(account);
	}

	/**
	 * ��֤����
	 * @param userPassword ��������
	 * @param dbPassword ���ݿ�洢������
	 * @return boolean
	 */
	private boolean verifyPassword(String userPassword, String dbPassword)
	{

		if(userPassword == null)
		{
			if(dbPassword == null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if(dbPassword == null)
			{
				return false;
			}
			else
			{

				org.apache.java.security.MD5 md5 = new org.apache.java.security.MD5();
				String md5Password = org.apache.java.lang.Bytes.toString(md5.digest(password.getBytes()));

				return md5Password.equals(dbPassword);
			}
		}
	}

	public static void main(String[] args)
	{
		/*****************�û���֤����*****************/
		AuthEnvironment env = new AuthEnvironment("account1", "password");
		Authentication a = null;
		try
		{
			a = ((AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.getName())).getAuthentication(env);
			if(a.authenticate())
			{

				User u = a.getUser();

				System.out.println(u.getDateCreated());
				System.out.println("��֤ͨ��");
			}
			else
			{

				System.out.println("��֤δͨ��");
			}
		}
		catch(Exception ex)
		{
			System.out.println("��֤δͨ��");
		}
		/*********************************************/
	}
}