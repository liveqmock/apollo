package cn.com.youtong.apollo.usermanager.db;

import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.services.*;

public class DBUserManagerFactory extends DefaultFactory implements UserManagerFactory
{
    public DBUserManagerFactory()
    {
    }

	public UserManager createUserManager()
		throws UserManagerException
	{
		UserManager result = null;
		//Ĭ����RDBMS����
		return new DBUserManager();
	}
}