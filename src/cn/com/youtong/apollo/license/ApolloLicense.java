package cn.com.youtong.apollo.license;

import cn.com.youtong.apollo.common.license.*;
import cn.com.youtong.apollo.services.Config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ApolloLicense
{
	private static final String m_file = Config.getString( "cn.com.youtong.apollo.license" );

    public ApolloLicense()
    {
    }

	/**
	 * 从config目录读取cn.com.youtong.apollo.license读取license文件路径名以及文件名
	 * @return                license是否有效
	 * @throws LicenseException
	 */
	public static boolean isValid()
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.isValid();
	}

	/**
	 * 软件是否已注册
	 * @return       true or false
	 * @throws LicenseException
	 */
	public static boolean isRegedited()
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.isRegedited();
	}

	/**
	 * 注册软件
	 * @param key        注册号
	 * @return           是否注册成功
	 * @throws LicenseException
	 */
	public static boolean regedit( String key )
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.regedit( key );
	}

	/**
	 * 还剩下多少天试用时间
	 * @return       剩余试用天数
	 * @throws LicenseException
	 */
	public static int trailDay()
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.trailDay();
	}
}