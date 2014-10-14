/*
 * Created on 2003-10-9
 */
package cn.com.youtong.apollo.services;

/**
 * Config类在读取配置过程中，出现错误抛出ConfigException
 * @author wjb
 */
public class ConfigException extends Exception
{

	public ConfigException()
	{
		super();
	}

	public ConfigException(String message)
	{
		super(message);
	}

}
