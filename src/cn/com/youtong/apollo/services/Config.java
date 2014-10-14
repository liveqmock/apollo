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
	/** ϵͳʹ�õ����ݿ����� */
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
			log.fatal("��ȡ����ʧ��.", e);
		}

		initStorge();
	}

	private static void initStorge()
	{
		//����������� .zip �ļ���Ŀ¼
		new File(getString("cn.com.youtong.apollo.zipdata.directory")).mkdir();
		//����������� .xml �ļ���Ŀ¼
		new File(getString("cn.com.youtong.apollo.extractdata.directory")).mkdir();
		//������ű������ݵ�Ŀ¼
		new File(getString("cn.com.youtong.apollo.backupdata.directory")).mkdir();
		//������Ź������ݵ�Ŀ¼
		new File(getString("cn.com.youtong.apollo.outoftimelimitdata.directory")).mkdir();

		// chart dictionary
		new File(getString("cn.com.youtong.apollo.chart.directory")).mkdir();
		// excel dictionary
		new File(getString("cn.com.youtong.apollo.excel.directory")).mkdir();

		// ����������ʱ�ļ����Ŀ¼
		new File(getString("cn.com.youtong.apollo.data.export.tempdir")).mkdirs();

		// ��������email���ݵ��ļ�
		String mailOutputPath = getString("email.outputfile");
		try
		{
			File mail = new File(mailOutputPath);
			mail.createNewFile(); //����������ļ�����ô���������������ô���Ḳ�ǵ�
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		// ��������sms���ݵ��ļ�
		String smsOutputPath = getString("sms.outputfile");
		try
		{
			File sms = new File(smsOutputPath);
			sms.createNewFile(); //����������ļ�����ô���������������ô���Ḳ�ǵ�
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
	 * �õ���ǰʹ�õ����ݿ����Ͷ���
	 * @return ���ݿ����Ͷ���
	 * @throws ConfigException �����ж�ʹ�õ����ݿ⣬�׳�
	 */
	public static DataBase getCurrentDatabase()
		throws ConfigException
	{
		if(database == null)
		{
			/** ϵͳʹ�õ����ݿ�����Property */
			String databaseProp = getString("cn.com.youtong.apollo.database");
			//����������Ϣ������Ӧ��DataBaseʵ��
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
					throw new ConfigException("�����жϵ�ǰϵͳʹ�õ����������ݿ�");
				}
			}
		}
		return database;
	}

}