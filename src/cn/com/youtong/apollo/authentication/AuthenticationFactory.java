package cn.com.youtong.apollo.authentication;

import cn.com.youtong.apollo.authentication.db.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface AuthenticationFactory
{
	/**
	 * 返回Authentication
	 * @param env 环境参数
	 * @return Authentication
	 * @throws UserManagerException
	 */
	public Authentication getAuthentication(AuthEnvironment env)
		throws UserManagerException;
}