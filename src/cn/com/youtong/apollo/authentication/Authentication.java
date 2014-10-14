package cn.com.youtong.apollo.authentication;

import cn.com.youtong.apollo.usermanager.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Authentication
{

	/**
	 * ��֤�û�
	 * @return��boolean
	 * @throws UserManagerException
	 */
	public boolean authenticate()
		throws UserManagerException;

	/**
	 * ����û�����
	 * @return User
	 * @throws UserManagerException
	 */
	public User getUser()
		throws UserManagerException;
}