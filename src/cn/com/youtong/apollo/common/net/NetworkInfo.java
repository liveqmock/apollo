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
	 * ��configResponse�ı���Parse��MAC��ַ��Ϣ
	 * @param configResponse           String
	 * @return                         MAC��ַ��Ϣ
	 * @throws ParseException          ƥ�䲻����,�׳��쳣,���߲�����
	 */
	abstract String parseMacAddress(String configResponse)
		throws ParseException;

	/**
	 * ����ƽ̨��صĲ鿴MAC��������Ϣ������
	 * @return                     �����������
	 * @throws IOException
	 */
	abstract String runNetworkInfoCommand()
		throws IOException;


	/**
	 * ���п���̨����õ��ķ��ؽ��,����̨��ӡ�Ľ��
	 * @param command      ���е�����;
	 *                     �������Ϊnull����Ϊ�յ��ַ���,���ؿյ��ַ���
	 * @return             ���н��
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
