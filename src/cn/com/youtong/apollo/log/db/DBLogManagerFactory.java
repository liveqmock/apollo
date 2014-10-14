package cn.com.youtong.apollo.log.db;

import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.log.*;

/**
 * LogManagerFactory的数据库实现
 */
public class DBLogManagerFactory
	extends DefaultFactory
	implements LogManagerFactory
{
	/**
	 * Create log manager
	 * @return log manager
	 * @throws LogException
	 */
	public LogManager createLogManager() throws LogException
	{
		return new DBLogManager();
	}
}