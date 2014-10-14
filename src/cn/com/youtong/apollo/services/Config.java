package cn.com.youtong.apollo.services;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.io.*;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.database.*;

public final class Config
{
	/** 系统使用的数据库类型 */
	private static DataBase database;
	private static Configuration configuration;
	static
	{
		try
		{
			configuration = (Configuration) ApolloService.lookup(Configuration.class.getName());
		}
		catch(Exception e)
		{
			Log log = LogFactory.getLog(Config.class.getName());
			log.fatal("读取配置失败.", e);
		}

		initStorge();
	}

	private static void initStorge()
	{
		//创建存放数据 .zip 文件的目录
		new File(getString("cn.com.youtong.apollo.zipdata.directory")).mkdir();
		//创建存放数据 .xml 文件的目录
		new File(getString("cn.com.youtong.apollo.extractdata.directory")).mkdir();
		//创建存放备份数据的目录
		new File(getString("cn.com.youtong.apollo.backupdata.directory")).mkdir();
		//创建存放过期数据的目录
		new File(getString("cn.com.youtong.apollo.outoftimelimitdata.directory")).mkdir();

		// chart dictionary
		new File(getString("cn.com.youtong.apollo.chart.directory")).mkdir();
		// excel dictionary
		new File(getString("cn.com.youtong.apollo.excel.directory")).mkdir();

		// 导出数据临时文件存放目录
		new File(getString("cn.com.youtong.apollo.data.export.tempdir")).mkdirs();

		// 创建缓存email数据的文件
		String mailOutputPath = getString("email.outputfile");
		try
		{
			File mail = new File(mailOutputPath);
			mail.createNewFile(); //如果不存在文件，那么创建，如果存在那么不会覆盖的
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		// 创建缓存sms数据的文件
		String smsOutputPath = getString("sms.outputfile");
		try
		{
			File sms = new File(smsOutputPath);
			sms.createNewFile(); //如果不存在文件，那么创建，如果存在那么不会覆盖的
		}
		catch(IOException e)
		{

		}
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 */
	public static void addProperty(String arg0, Object arg1)
	{
		configuration.addProperty(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 */
	public static void clearProperty(String arg0)
	{
		configuration.clearProperty(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static boolean containsKey(String arg0)
	{
		return configuration.containsKey(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static boolean getBoolean(String arg0)
	{
		return configuration.getBoolean(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static boolean getBoolean(String arg0, boolean arg1)
	{
		return configuration.getBoolean(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Boolean getBoolean(String arg0, Boolean arg1)
	{
		return configuration.getBoolean(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static byte getByte(String arg0)
	{
		return configuration.getByte(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static byte getByte(String arg0, byte arg1)
	{
		return configuration.getByte(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Byte getByte(String arg0, Byte arg1)
	{
		return configuration.getByte(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static double getDouble(String arg0)
	{
		return configuration.getDouble(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static double getDouble(String arg0, double arg1)
	{
		return configuration.getDouble(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Double getDouble(String arg0, Double arg1)
	{
		return configuration.getDouble(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static float getFloat(String arg0)
	{
		return configuration.getFloat(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static float getFloat(String arg0, float arg1)
	{
		return configuration.getFloat(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Float getFloat(String arg0, Float arg1)
	{
		return configuration.getFloat(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static int getInt(String arg0)
	{
		return configuration.getInt(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static int getInt(String arg0, int arg1)
	{
		return configuration.getInt(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Integer getInteger(String arg0, Integer arg1)
	{
		return configuration.getInteger(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Iterator getKeys()
	{
		return configuration.getKeys();
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Iterator getKeys(String arg0)
	{
		return configuration.getKeys(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static long getLong(String arg0)
	{
		return configuration.getLong(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Long getLong(String arg0, Long arg1)
	{
		return configuration.getLong(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static long getLong(String arg0, long arg1)
	{
		return configuration.getLong(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Properties getProperties(String arg0)
	{
		return configuration.getProperties(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Object getProperty(String arg0)
	{
		return configuration.getProperty(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static short getShort(String arg0)
	{
		return configuration.getShort(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Short getShort(String arg0, Short arg1)
	{
		return configuration.getShort(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static short getShort(String arg0, short arg1)
	{
		return configuration.getShort(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static String getString(String arg0)
	{
		return configuration.getString(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static String getString(String arg0, String arg1)
	{
		return configuration.getString(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static String[] getStringArray(String arg0)
	{
		return configuration.getStringArray(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Vector getVector(String arg0)
	{
		return configuration.getVector(arg0);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static Vector getVector(String arg0, Vector arg1)
	{
		return configuration.getVector(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 * @return
	 */
	public static boolean isEmpty()
	{
		return configuration.isEmpty();
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 */
	public static void setProperty(String arg0, Object arg1)
	{
		configuration.setProperty(arg0, arg1);
	}

	/**
	 * @see org.apache.commons.configuration.Configuration
	 */
	public static org.apache.commons.configuration.Configuration subset(String arg0)
	{
		return configuration.subset(arg0);
	}

	/**
	 * 得到当前使用的数据库类型对象
	 * @return 数据库类型对象
	 * @throws ConfigException 不能判断使用的数据库，抛出
	 */
	public static DataBase getCurrentDatabase()
		throws ConfigException
	{
		if(database == null)
		{
			/** 系统使用的数据库类型Property */
			String databaseProp = getString("cn.com.youtong.apollo.database");
			//根据配置信息创建相应的DataBase实例
			if(databaseProp != null)
			{
				if(databaseProp.equalsIgnoreCase("oracle"))
				{
					database = new Oracle();
				}
				else if(databaseProp.equalsIgnoreCase("sqlserver"))
				{
					database = new SqlServer();
				}
				else if(databaseProp.equalsIgnoreCase("mysql"))
				{
					database = new MySql();
				}
				else
				{
					throw new ConfigException("不能判断当前系统使用的是哪种数据库");
				}
			}
		}
		return database;
	}

}