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
	/**保存用户的帐号*/
	private static String account;

	/**保存用户的密码*/
	private static String password;

	/**
	 * 构造函数
	 * @param env 环境参数
	 * @throws UserManagerException
	 */
	public DBAuthentication(AuthEnvironment env)
		throws UserManagerException
	{

		if(env == null)
		{
			throw new UserManagerException("认证信息为空");
		}

		this.account = env.getSecurityPrincipal();
		if((this.account == null) || (this.account.length() == 0))
		{
			throw new UserManagerException("用户帐号为空");
		}

		this.account = this.account;
		this.password = (String) env.getSecurityCredentials();
	}

	/**
	 * 认证用户
	 * @return　boolean
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
	 * 获得用户对象
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
	 * 验证密码
	 * @param userPassword 输入密码
	 * @param dbPassword 数据库存储的密码
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
		/*****************用户认证例子*****************/
		AuthEnvironment env = new AuthEnvironment("account1", "password");
		Authentication a = null;
		try
		{
			a = ((AuthenticationFactory) Factory.getInstance(AuthenticationFactory.class.getName())).getAuthentication(env);
			if(a.authenticate())
			{

				User u = a.getUser();

				System.out.println(u.getDateCreated());
				System.out.println("认证通过");
			}
			else
			{

				System.out.println("认证未通过");
			}
		}
		catch(Exception ex)
		{
			System.out.println("认证未通过");
		}
		/*********************************************/
	}
}