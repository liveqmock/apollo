package cn.com.youtong.apollo.log;

import cn.com.youtong.apollo.log.db.*;

/**
 * LogManager工厂接口
 */
public interface LogManagerFactory
{
	/**
	 * Create log manager
	 * @return log manager
	 * @throws LogException
	 */
	public LogManager createLogManager() throws LogException;
}
