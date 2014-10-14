package cn.com.youtong.apollo.common.license.xml;


/**
 * 这里的实现是,第一个行写入安装日期
 * 第二行是经过加密的机器码(其中包含安装日期信息,以免恶意修改)
 * 第三行注册码信息.
 * 以下是一个license.xml样式:详见license.xsd
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
	/** license文件名称 */
	private final String m_file;
	private final License m_license; // castor 解析出来的对象

	/**
	 * 指定license文件路径
	 * @param file         license文件路径
	 * throws LicenseException
	 */
	public XMLLicense( final String file )
		throws LicenseException
	{
		if( Util.isEmptyString( file ) )
			throw new LicenseException( "注册文件不存在" );
		m_file = file;

		Reader reader = null;
		try
		{
			reader = new BufferedReader( new FileReader( m_file ) );

			m_license = License.unmarshal( reader );

			if( Util.isEmptyString( m_license.getMachinecode() ) )
				throw new LicenseException( "license文件不正确" );

			// 查看机器码和安装日期是否匹配
			String secret = new String( secretMachineCode( m_license.getInstalldate().toDate() ) );
			if( !secret.equals( m_license.getMachinecode() ) )
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
		catch( org.exolab.castor.xml.MarshalException me )
		{
			throw new LicenseException( "注册文件XML格式不正确" );
		}
		catch( org.exolab.castor.xml.ValidationException ve )
		{
			throw new LicenseException( "注册文件XML格式不正确" );
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
		if( Util.isEmptyString( m_license.getRegkey() ) )
			return false;

		return validateKey( m_license.getRegkey() );
	}

	/**
	 * 查看还有多长时间的试用时间,如果为-1,表示过期.
	 * @return          剩下试用天数
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

		boolean aKey = validateKey( key); // 是否一个注册码
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
				throw new LicenseException( "写注册信息出错" );
			}
			catch( org.exolab.castor.xml.MarshalException me )
			{
				throw new LicenseException( "写注册信息出错" );
			}
			catch( org.exolab.castor.xml.ValidationException ve )
			{
				throw new LicenseException( "写注册信息出错" );
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
	 * 返回机器码,经过了一定的加密算法
	 * @return           机器码
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
