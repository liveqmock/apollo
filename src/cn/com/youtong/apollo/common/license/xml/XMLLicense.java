package cn.com.youtong.apollo.common.license.xml;


/**
 * �����ʵ����,��һ����д�밲װ����
 * �ڶ����Ǿ������ܵĻ�����(���а�����װ������Ϣ,��������޸�)
 * ������ע������Ϣ.
 * ������һ��license.xml��ʽ:���license.xsd
 * <code>
 * <pre>
 * &lt? xml version="1.0" encoding="UTF-8"? &gt
 * &lt license &gt
 *	   &lt company &gt abc.com corp &lt /company &gt
 *	   &lt installdate &gt 2004-03-08 &lt /installdate &gt
 *	   &lt machinecode &gt IetKJmmEoOMpQ1A7Zy4FTQ== &lt /machinecode &gt
 * &lt /license &gt
 * </pre>
 * </code>
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.com.youtong.apollo.common.Convertor;
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.license.LicenseException;
import cn.com.youtong.apollo.common.net.NetworkInfo;

import org.apache.java.security.MD5;

public class XMLLicense implements cn.com.youtong.apollo.common.license.License
{
	/** license�ļ����� */
	private final String m_file;
	private final License m_license; // castor ���������Ķ���

	/**
	 * ָ��license�ļ�·��
	 * @param file         license�ļ�·��
	 * throws LicenseException
	 */
	public XMLLicense( final String file )
		throws LicenseException
	{
		if( Util.isEmptyString( file ) )
			throw new LicenseException( "ע���ļ�������" );
		m_file = file;

		Reader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( m_file ) );

			m_license = License.unmarshal( reader );

			if( Util.isEmptyString( m_license.getMachinecode() ) )
				throw new LicenseException( "license�ļ�����ȷ" );

			// �鿴������Ͱ�װ�����Ƿ�ƥ��
			String secret = new String( secretMachineCode( m_license.getInstalldate().toDate() ) );
			if( !secret.equals( m_license.getMachinecode() ) )
			{
				secret = null;
				// license�ļ����޸�
				throw new LicenseException( "license�ļ�����ȷ" );
			}

			secret = null;
		}
		catch( IOException ex )
		{
			throw new LicenseException( "��ȡע����Ϣ����" );
		}
		catch( org.exolab.castor.xml.MarshalException me )
		{
			throw new LicenseException( "ע���ļ�XML��ʽ����ȷ" );
		}
		catch( org.exolab.castor.xml.ValidationException ve )
		{
			throw new LicenseException( "ע���ļ�XML��ʽ����ȷ" );
		}
		finally
		{
			Util.close( reader );
			System.gc();
		}
	}

	public Date installDate()
	{
		return m_license.getInstalldate().toDate();
	}
	/**
	 * �鿴license�Ƿ���Ч
	 * @return           true or false
	 * @throws LicenseException
	 */
	public boolean isValid()
		throws LicenseException
	{
		if( trailDay() < 0 )
		{
			// ����
			return checkLicense();
		}
		else
		{
			// û�й���
			return true;
		}
	}

	/**
	 * �鿴license�Ƿ����
	 * @return     true or false
	 * @throws LicenseException
	 */
	private boolean checkLicense()
		throws LicenseException
	{
		if( Util.isEmptyString( m_license.getRegkey() ) )
			return false;

		return validateKey( m_license.getRegkey() );
	}

	/**
	 * �鿴���ж೤ʱ�������ʱ��,���Ϊ-1,��ʾ����.
	 * @return          ʣ����������
	 * @throws LicenseException
	 */
	public int trailDay()
		throws LicenseException
	{

		long l_install = m_license.getInstalldate().toDate().getTime();
		long l_now = new Date().getTime();
		long l_dayInMis = 24 * 60 * 60 * 1000;

		int used = ( int ) ( ( l_now - l_install ) / l_dayInMis );
		int left = ( 0 - used );
		if( left <= 0 )
			left = -1;

		return left;
	}

	/**
	 * ע��license
	 * @param key          ע����
	 * @return             ע���Ƿ�ɹ�
	 * @throws LicenseException
	 */
	public boolean regedit( String key )
		throws LicenseException
	{
		if( isRegedited() )
			return true; // ����Ѿ�ע��,û�б�Ҫ�ٴ�ע��

		boolean aKey = validateKey( key); // �Ƿ�һ��ע����
		if( aKey )
		{
			m_license.setRegkey( key );
			Writer writer = null;
			try
			{
				writer = new BufferedWriter( new FileWriter( m_file ) );
				m_license.marshal( writer );
			}
			catch( IOException ex )
			{
				throw new LicenseException( "дע����Ϣ����" );
			}
			catch( org.exolab.castor.xml.MarshalException me )
			{
				throw new LicenseException( "дע����Ϣ����" );
			}
			catch( org.exolab.castor.xml.ValidationException ve )
			{
				throw new LicenseException( "дע����Ϣ����" );
			}
			finally
			{
				Util.close( writer );
			}
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * ���ػ�����,������һ���ļ����㷨
	 * @return           ������
	 * @throws LicenseException
	 */
	public String machineCode()
		throws LicenseException
	{
		return m_license.getMachinecode();
	}

	public boolean isRegedited()
		throws LicenseException
	{
		return checkLicense();
	}
	/**
	 * �Ƚ�key�Ƿ���ȷ
	 * @param key         key
	 * @return            true or false
	 * @throws LicenseException
	 */
	private boolean validateKey( String key )
		throws LicenseException
	{
		boolean valid = false;

		if( Util.isEmptyString( key ) )
			return valid;

		try
		{
			String machineCode = machineCode();
			byte[] barray = secretRegeditKey( machineCode.getBytes() );

			if( key.equals( new String( barray ) ) )
				valid = true;
		}
		catch (NoSuchAlgorithmException nsae)
		{
			throw new LicenseException( "ȱ�ټ����㷨" );
		}
		finally
		{
			System.gc();
		}

		return valid;
	}

	/**
	 * ȡ����ԭʼ��Ϣ,�Ͱ�װ������Ϣ,����һ���ļ���,�õ�������
	 * @param date           ��װ����
	 * @return               ������
	 * @throws LicenseException
	 */
	private byte[] secretMachineCode( Date date )
		throws LicenseException
	{
		try
		{
			String mac = NetworkInfo.getMacAddress();
			String sDate = Convertor.date2ShortString( date );

			MessageDigest sha = MessageDigest.getInstance( "SHA" );

			byte[] hash = sha.digest( ( mac + sDate ).getBytes() );

			MD5 md = new MD5();
			hash = md.digest(hash);

			return Util.encodeBase64( hash );
		}
		catch( IOException ex )
		{
			throw new LicenseException( "ȡ������Ϣ����" );
		}
		catch( NoSuchAlgorithmException nsae )
		{
			throw new LicenseException( "ȱ�ټ����㷨" );
		}
	}

	/** �Ի�����,����һ���ļ���,����ע���� */
	private byte[] secretRegeditKey( byte[] barr )
		throws NoSuchAlgorithmException
	{
		MessageDigest sha = MessageDigest.getInstance( "SHA" );

		byte[] hash = sha.digest( barr );

		MD5 md = new MD5();
		hash = md.digest(hash);

		return Util.encodeBase64( hash );
	}

}
