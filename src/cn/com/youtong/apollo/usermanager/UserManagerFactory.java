package cn.com.youtong.apollo.usermanager;

import cn.com.youtong.apollo.usermanager.db.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface UserManagerFactory
{

	/**
	 * ´´½¨ UserManager
	 * @return  UserManager
	 * @throws UserManagerException
	 */
	UserManager createUserManager()
		throws UserManagerException;
}