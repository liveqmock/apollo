package cn.com.youtong.apollo.authentication.db;

import cn.com.youtong.apollo.authentication.*;
import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.services.*;

public class DBAuthenticationFactory  extends DefaultFactory
	implements AuthenticationFactory
{
	public DBAuthenticationFactory()
	{
	}

	/**
	 * ����Authentication
	 * @param env ��������
	 * @return Authentication
	 * @throws UserManagerException
	 */
	public Authentication getAuthentication(AuthEnvironment env)
		throws UserManagerException
	{

		if((env == null) || (env.isEmpty()))
		{
			return null;
		}

		String authType = env.getAuthenticationType();

		if(authType == null)
		{
			throw new UserManagerException("��Ч����֤����");
		}

		if(authType.equals(AuthEnvironment.AUTHENTICATION_TYPE_RDBMS))
		{
			return new DBAuthentication(env);
		}
		else
		{
			throw new UserManagerException("��Ч����֤����");
		}
	}
}