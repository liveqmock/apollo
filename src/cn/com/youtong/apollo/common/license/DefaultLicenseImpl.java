package cn.com.youtong.apollo.common.license;

/**
 * �����ʵ����,��һ����д�밲װ����
 * �ڶ����Ǿ������ܵĻ�����(���а�����װ������Ϣ,��������޸�)
 * ������ע������Ϣ.
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
import cn.com.youtong.apollo.common.net.NetworkInfo;

import org.apache.java.security.MD5;

public class DefaultLicenseImpl implements License
{
	/** license�ļ����� */
	private final String m_file;
	/** license�ļ���װ���� */
	private final Date m_installDate;
	/** license������ */
	private final String m_machineCode;

	/**
	 * ָ��license�ļ�·��
	 * @param file         license�ļ�·��
	 * throws LicenseException
	 */
	public DefaultLicenseImpl( final String file )
		throws LicenseException
	{
		if( Util.isEmptyString( file ) )
			throw new LicenseException( "ע���ļ�������" );
		m_file = file;

		LineNumberReader reader = null;
		try
		{
			reader = new LineNumberReader( new FileReader( m_file ) );
			String line = reader.readLine();

			m_installDate = Convertor.string2Date( line );

			m_machineCode = reader.readLine(); // ��ȡ������
			if( Util.isEmptyString( m_machineCode ) )
				throw new LicenseException( "license�ļ�����ȷ" );

			// �鿴������Ͱ�װ�����Ƿ�ƥ��
			String secret = new String( secretMachineCode( m_installDate ) );
			if( !secret.equals( m_machineCode ) )
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
		catch( ParseException pe )
		{
			// ע���ļ��İ�װ���ڲ���ȷ
			throw new LicenseException( "��װ���ڸ�ʽ����ȷ" );
		}
		finally
		{
			Util.close( reader );
			System.gc();
		}
	}

	public Date installDate()
	{
		return m_installDate;
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
		LineNumberReader reader = null;
		try
		{
			reader = new LineNumberReader( new FileReader( m_file ) );
			reader.readLine(); // ��װ����
			reader.readLine(); // ������
			String key = reader.readLine();

			return validateKey( key );
		}
		catch( IOException ioe )
		{
			throw new LicenseException( "��ȡlicense�ļ�����" );
		}
		finally
		{
			Util.close( reader );
		}
	}

	/**
	 * �鿴���ж೤ʱ�������ʱ��,���Ϊ-1,��ʾ����.
	 * @return          ʣ����������
	 * @throws LicenseException
	 */
	public int trailDay()
		throws LicenseException
	{

		long l_install = m_installDate.getTime();
		long l_now = new Date().getTime();
		long l_dayInMis = 24 * 60 * 60 * 1000;

		int used = ( int ) ( ( l_now - l_install ) / l_dayInMis );
		int left = ( 30 - used );
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

		RandomAccessFile raf = null;
		try
		{
			if( validateKey( key ) )
			{
				raf = new RandomAccessFile( m_file, "rw" );
				raf.readLine(); // ��װ����
				raf.readLine(); // ������
				raf.write( "\n".getBytes() );
				raf.write( key.getBytes() ); // ע����
				raf.write( "\n".getBytes() );

				return true;
			}
			else
			{
				return false;
			}
		}
		catch( IOException ex )
		{
			throw new LicenseException( "ע��ʱ����IO�쳣" );
		}
		finally
		{
			Util.close( raf );
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
		return m_machineCode;
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