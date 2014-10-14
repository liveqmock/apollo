package cn.com.youtong.apollo.common.net;

/** try to determine MAC address of local network card; this is done
  using a shell to run ifconfig (linux) or ipconfig (windows). The
  output of the processes will be parsed.
  <p>
  To run the whole thing, just type java NetworkInfo
  <p>
  Current restrictions:
  <ul>
	 <li>Will probably not run in applets
	 <li>Tested Windows / Linux only
	 <li>Tested J2SDK 1.4 only
	 <li>If a computer has more than one network adapters, only
	   one MAC address will be returned
	 <li>will not run if user does not have permissions to run
	   ifconfig/ipconfig (e.g. under linux this is typically
	   only permitted for root)
  </ul>
 */

import java.net.InetAddress;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;

import cn.com.youtong.apollo.common.Util;

public abstract class NetworkInfo
{
	public final static String getMacAddress()
		throws IOException
	{
		String os = System.getProperty( "os.name" );

		try
		{
			NetworkInfo info = null;
			if( os.startsWith( "Windows" ) )
			{
				info = WindowsNetworkInfo.instance();
			}
			else if( os.startsWith( "Linux" ) )
			{
				info = LinuxNetworkInfo.instance();
			}
			else
			{
				throw new IOException( "unknown/unsupported operating system: " + os );
			}

			String response = info.runNetworkInfoCommand();
			return info.parseMacAddress( response );
		}
		catch( ParseException ex )
		{
			ex.printStackTrace();
			throw new IOException( ex.getMessage() );
		}
	}

	/**
	 * 从configResponse文本中Parse出MAC地址信息
	 * @param configResponse           String
	 * @return                         MAC地址信息
	 * @throws ParseException          匹配不出来,抛出异常,或者不存在
	 */
	abstract String parseMacAddress(String configResponse)
		throws ParseException;

	/**
	 * 运行平台相关的查看MAC等网络信息的命令
	 * @return                     命令输出内容
	 * @throws IOException
	 */
	abstract String runNetworkInfoCommand()
		throws IOException;


	/**
	 * 运行控制台程序得到的返回结果,控制台打印的结果
	 * @param command      运行的命令;
	 *                     如果命令为null或者为空的字符串,返回空的字符串
	 * @return             运行结果
	 * @throws IOException
	 */
	final String runConsoleCommand(String command)
		throws IOException
	{
		if( cn.com.youtong.apollo.common.Util.isEmptyString(command) )
			return "";

		Process p = Runtime.getRuntime().exec( command );
		InputStream stdoutStream = null;
		try
		{
			stdoutStream = new BufferedInputStream( p.getInputStream() );

			StringBuffer buffer = new StringBuffer();
			for( ; ; )
			{
				int c = stdoutStream.read();
				if( c == -1 )
				{
					break;
				}
				buffer.append( ( char ) c );
			}
			String outputText = buffer.toString();

			return outputText;
		}
		finally
		{
			Util.close( stdoutStream );
		}


	}
	/*
	 * Main
	 */
	public final static void main( String[] args )
	{
		try
		{
			System.out.println( "Network infos" );

			System.out.println( "  Operating System: "
								+ System.getProperty( "os.name" ) );
			System.out.println( "  IP/Localhost: "
								+ InetAddress.getLocalHost().getHostAddress() );
			System.out.println( "  MAC Address: " + getMacAddress() );

			WindowsNetworkInfo.instance().runConsoleCommand("");
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
	}
}
