package cn.com.youtong.apollo.common.license;

/**
 * 这里的实现是,第一个行写入安装日期
 * 第二行是经过加密的机器码(其中包含安装日期信息,以免恶意修改)
 * 第三行注册码信息.
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
	/** license文件名称 */
	private final String m_file;
	/** license文件安装日期 */
	private final Date m_installDate;
	/** license机器码 */
	private final String m_machineCode;

	/**
	 * 指定license文件路径
	 * @param file         license文件路径
	 * throws LicenseException
	 */
	public DefaultLicenseImpl( final String file )
		throws LicenseException
	{
		if( Util.isEmptyString( file ) )
			throw new LicenseException( "注册文件不存在" );
		m_file = file;

		LineNumberReader reader = null;
		try
		{
			reader = new LineNumberReader( new FileReader( m_file ) );
			String line = reader.readLine();

			m_installDate = Convertor.string2Date( line );

			m_machineCode = reader.readLine(); // 读取机器码
			if( Util.isEmptyString( m_machineCode ) )
				throw new LicenseException( "license文件不正确" );

			// 查看机器码和安装日期是否匹配
			String secret = new String( secretMachineCode( m_installDate ) );
			if( !secret.equals( m_machineCode ) )
			{
				secret = null;
				// license文件被修改
				throw new LicenseException( "license文件不正确" );
			}

			secret = null;
		}
		catch( IOException ex )
		{
			throw new LicenseException( "读取注册信息出错" );
		}
		catch( ParseException pe )
		{
			// 注册文件的安装日期不正确
			throw new LicenseException( "安装日期格式不正确" );
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
	 * 查看license是否有效
	 * @return           true or false
	 * @throws LicenseException
	 */
	public boolean isValid()
		throws LicenseException
	{
		if( trailDay() < 0 )
		{
			// 过期
			return checkLicense();
		}
		else
		{
			// 没有过期
			return true;
		}
	}

	/**
	 * 查看license是否过期
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
			reader.readLine(); // 安装日期
			reader.readLine(); // 机器码
			String key = reader.readLine();

			return validateKey( key );
		}
		catch( IOException ioe )
		{
			throw new LicenseException( "读取license文件出错" );
		}
		finally
		{
			Util.close( reader );
		}
	}

	/**
	 * 查看还有多长时间的试用时间,如果为-1,表示过期.
	 * @return          剩下试用天数
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
	 * 注册license
	 * @param key          注册码
	 * @return             注册是否成功
	 * @throws LicenseException
	 */
	public boolean regedit( String key )
		throws LicenseException
	{
		if( isRegedited() )
			return true; // 如果已经注册,没有必要再次注册

		RandomAccessFile raf = null;
		try
		{
			if( validateKey( key ) )
			{
				raf = new RandomAccessFile( m_file, "rw" );
				raf.readLine(); // 安装日期
				raf.readLine(); // 机器码
				raf.write( "\n".getBytes() );
				raf.write( key.getBytes() ); // 注册码
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
			throw new LicenseException( "注册时发生IO异常" );
		}
		finally
		{
			Util.close( raf );
		}
	}

	/**
	 * 返回机器码,经过了一定的加密算法
	 * @return           机器码
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
	 * 比较key是否正确
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
			throw new LicenseException( "缺少加密算法" );
		}
		finally
		{
			System.gc();
		}

		return valid;
	}

	/**
	 * 取机器原始信息,和安装日期信息,进行一定的加密,得到机器码
	 * @param date           安装日期
	 * @return               机器码
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
			throw new LicenseException( "取机器信息出错" );
		}
		catch( NoSuchAlgorithmException nsae )
		{
			throw new LicenseException( "缺少加密算法" );
		}
	}

	/** 对机器码,进行一定的加密,产生注册码 */
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