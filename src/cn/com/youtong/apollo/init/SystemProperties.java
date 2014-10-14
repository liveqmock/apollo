package cn.com.youtong.apollo.init;

import java.io.*;
import java.util.*;
import cn.com.youtong.apollo.services.*;
import cn.com.youtong.apollo.init.systemxml.*;
import org.exolab.castor.xml.*;
import org.apache.commons.logging.*;

public class SystemProperties
{
	private static Log log = LogFactory.getLog(SystemProperties.class);

	public SystemProperties()
	{
	}

	/**
	 * 设置系统的属性为输入属性文件的属性
	 * @param in 输入的属性文件
	 * @throws InitException 设置异常
	 */
	public static void setProperties(InputStream in)
		throws InitException
	{
		InputStreamReader reader = new InputStreamReader(in);
		Sysproperties sysProperties = null;
		try
		{
			sysProperties = Sysproperties.unmarshal(reader);
		}
		catch(ValidationException ex)
		{
			log.error("设置系统参数失败，无效的XML文件 ", ex);
			throw new InitException(ex);
		}
		catch(MarshalException ex)
		{
			log.error("设置系统参数失败，XML文件解析失败 ", ex);
			throw new InitException(ex);
		}

		if(sysProperties == null)
		{
			throw new InitException("设置参数失败，请检查输入文件！　");
		}
		setProperties(sysProperties);
		saveProperties();
	}

	/**
	 * 设置系统的属性
	 * @param sysProperties 输入的属性文件
	 */
	private static void setProperties(Sysproperties sysProperties)
	{
		Sysproperty[] arraySysProperty = sysProperties.getSysproperty();
		for(int i = 0; i < arraySysProperty.length; i++)
		{
			Sysproperty sysProperty = arraySysProperty[i];
			String key = sysProperty.getKey();
			String value = sysProperty.getValue();

			if(containKey(Config.getKeys(), key))
			{
				Config.setProperty(key, value);
			}
			else
			{
				Config.addProperty(key, value);
			}
		}
	}

	/**
	 * 判断在系统属性集合里是否包含要操作的属性
	 * @param itrKeys 属性集合
	 * @param key 要操作的属性
	 * @return boolean 是则返回　true ,否则 false
	 */
	private static boolean containKey(Iterator itrKeys, String key)
	{
		while(itrKeys.hasNext())
		{
			String tempKey = (String) itrKeys.next();
			if(tempKey.equals(key))
			{
				return true;
			}
		}
		return false;
	}

	private static void saveProperties()
		throws InitException
	{
		try
		{
			saveFile(Config.getString("webappRoot") + "WEB-INF\\conf\\config.properties");
			saveFile(Config.getString("webappRoot") + "WEB-INF\\conf\\sms_config.properties");
		}
		catch(IOException ex)
		{
			log.error("操作配置文件失败", ex);
			throw new InitException(ex);
		}

//		Properties properties = new Properties();
//		Iterator itrKeys = Config.getKeys();
//		while(itrKeys.hasNext())
//		{
//			String key = (String)itrKeys.next();
//			String[] values = Config.getStringArray(key);
//			if(key.equals("webappRoot") || key.equals("applicationRoot"))
//			{
//				continue;
//			}
//			if(values.length == 1)
//			{
//				properties.put(key, values[0]);
//			}else if(values.length > 1)
//			{
//				String value = values[0];
//				for(int i=1; i<values.length;i++)
//				{
//					value = value + "," + values[i];
//				}
//				properties.put(key,value);
//			}
//		}
//		FileOutputStream o = null;
//		try
//		{
//			File file = new File(Config.getString("webappRoot") + "WEB-INF\\conf\\config.properties");
//			 o = new FileOutputStream(file);
//			properties.store(o,null);
//		}
//		catch(FileNotFoundException ex)
//		{
//			log.error("写入配置文件时失败 ",ex);
//			throw new InitException(ex);
//		}
//		catch(IOException ex)
//		{
//			log.error("写入配置文件时失败 ",ex);
//			throw new InitException(ex);
//		}
//		finally
//		{
//			if(o != null)
//			{
//				try
//				{
//					o.close();
//				}
//				catch(IOException ex1)
//				{
//				}
//			}
//		}
	}

	private static void saveFile(String fileName)
		throws IOException
	{
		BufferedReader bReader = null;
		Vector v = null;
		try
		{
			bReader = new BufferedReader(new FileReader(fileName));
			v = new Vector();
			String line;
			while((line = bReader.readLine()) != null)
			{
				v.add(line);
			}
		}
		catch(IOException ex)
		{
			log.error("读取配置文件失败", ex);
			throw new IOException(ex.getMessage());
		}
		finally
		{
			if(bReader != null)
			{
				try
				{
					bReader.close();
				}
				catch(IOException ex3)
				{
				}
			}
		}

		FileWriter w = null;
		try
		{
			w = new FileWriter(fileName);
			for(int i = 0; i < v.size(); i++)
			{
				String temp = (String) v.get(i);
				if(!temp.startsWith("#") && !temp.equals(""))
				{
					String key = temp.substring(0, temp.indexOf("="));
					String value = "";
					String[] values = Config.getStringArray(key);
					if(values.length == 1)
					{
						value = values[0];
					}
					else if(values.length > 1)
					{
						value = values[0];
						for(int j = 1; j < values.length; j++)
						{
							value = value + "," + values[j];
						}
					}
					if(value.indexOf(Config.getString("webappRoot")) != -1)
					{
						value = "${webappRoot}" + value.substring(value.indexOf("/"));
					}
					temp = key + "=" + value;
				}
				w.write(temp + "\r\n");
			}
		}
		catch(IOException ex1)
		{
			log.error("写入配置文件失败", ex1);
			throw new IOException(ex1.getMessage());
		}
		finally
		{
			if(w != null)
			{
				try
				{
					w.close();
				}
				catch(IOException ex2)
				{
				}
			}
		}
	}
        /**
         * 写属性文件
         * @param fileName
         * @throws IOException
         */
	public static void savePropertyFile(String fileName)
		throws IOException
	{
		BufferedReader bReader = null;
		Vector v = null;
		try
		{
			bReader = new BufferedReader(new FileReader(fileName));
			v = new Vector();
			String line;
			while((line = bReader.readLine()) != null)
			{
				v.add(line);
			}
		}
		catch(IOException ex)
		{
			log.error("读取配置文件失败", ex);
			throw new IOException(ex.getMessage());
		}
		finally
		{
			if(bReader != null)
			{
				try
				{
					bReader.close();
				}
				catch(IOException ex3)
				{
				}
			}
		}

		FileWriter w = null;
		try
		{
			w = new FileWriter(fileName);
			for(int i = 0; i < v.size(); i++)
			{
				String temp = (String) v.get(i);
				if(!temp.startsWith("#") && !temp.equals(""))
				{
					String key = temp.substring(0, temp.indexOf("="));
					String value = temp.substring(temp.indexOf("=")+1);;
					String memoryValue = Config.getString(key);
					if(!value.equals(memoryValue)){
                                      	   	temp = key + "=" + memoryValue;
                                        }
				}
				w.write(temp + "\r\n");
			}
		}
		catch(IOException ex1)
		{
			log.error("写入配置文件失败", ex1);
			throw new IOException(ex1.getMessage());
		}
		finally
		{
			if(w != null)
			{
				try
				{
					w.close();
				}
				catch(IOException ex2)
				{
				}
			}
		}
	}
	public static void main(String[] args)
	{
		SystemProperties systemProperties1 = new SystemProperties();
	}
}