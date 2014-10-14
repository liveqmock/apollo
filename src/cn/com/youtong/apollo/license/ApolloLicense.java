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
	 * ��configĿ¼��ȡcn.com.youtong.apollo.license��ȡlicense�ļ�·�����Լ��ļ���
	 * @return                license�Ƿ���Ч
	 * @throws LicenseException
	 */
	public static boolean isValid()
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.isValid();
	}

	/**
	 * ����Ƿ���ע��
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
	 * ע�����
	 * @param key        ע���
	 * @return           �Ƿ�ע��ɹ�
	 * @throws LicenseException
	 */
	public static boolean regedit( String key )
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.regedit( key );
	}

	/**
	 * ��ʣ�¶���������ʱ��
	 * @return       ʣ����������
	 * @throws LicenseException
	 */
	public static int trailDay()
		throws LicenseException
	{
		License license = LicenseManager.license( m_file );
		return license.trailDay();
	}
}