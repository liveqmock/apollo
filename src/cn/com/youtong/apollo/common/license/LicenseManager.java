package cn.com.youtong.apollo.common.license;

import cn.com.youtong.apollo.common.license.xml.XMLLicense;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class LicenseManager
{
    public LicenseManager()
    {
    }

	/**
	 * ����license����
	 * @param file           license�ļ�����λ��
	 * @return               license����
	 * @throws LicenseException
	 */
	public static License license( String file )
		throws LicenseException
	{
		return new XMLLicense( file );
	}
}