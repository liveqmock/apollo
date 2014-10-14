package cn.com.youtong.apollo.common.net;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.net.InetAddress;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.StringTokenizer;
import java.util.regex.*;

final public class LinuxNetworkInfo extends NetworkInfo
{
	private static LinuxNetworkInfo instance = new LinuxNetworkInfo();
	private LinuxNetworkInfo()
	{
	}

	static LinuxNetworkInfo instance()
	{
		return instance;
	}

	String parseMacAddress( String configResponse )
		throws ParseException
	{
		if( cn.com.youtong.apollo.common.Util.isEmptyString( configResponse ) )
			throw new ParseException(
						 "cannot read MAC address from [" + configResponse + "]", 0 );

		/**String localHost = null;
		try
		{
			localHost = InetAddress.getLocalHost().getHostAddress();
		}
		catch( java.net.UnknownHostException ex )
		{
			ex.printStackTrace();
			throw new ParseException( ex.getMessage(), 0 );
		}*/

		StringTokenizer tokenizer = new StringTokenizer( configResponse, "\n" );
		//String lastMacAddress = null;

		while( tokenizer.hasMoreTokens() )
		{
			String line = tokenizer.nextToken().trim();
			/**boolean containsLocalHost = line.indexOf( localHost ) >= 0;

			// see if line contains IP address
			if( containsLocalHost && lastMacAddress != null )
			{
				return lastMacAddress;
			}*/

			// see if line contains MAC address
			int macAddressPosition = line.indexOf( "HWaddr" );
			if( macAddressPosition <= 0 )
			{
				continue;
			}

			String macAddressCandidate = line.substring( macAddressPosition + 6 ).
										 trim();
			if( isMacAddress( macAddressCandidate ) )
			{
				//lastMacAddress = macAddressCandidate;
				//continue;
				return macAddressCandidate;
			}
		}

		ParseException ex = new ParseException( "cannot read MAC address for "
												+ " from ["
												+ configResponse + "]", 0 );
		throw ex;
	}

	/**
	 * 在linux上运行命令"ifconfig"得到网络配置信息
	 * @return      网络配置信息
	 * @throws IOException
	 */
	String runNetworkInfoCommand()
		throws IOException
	{
		return runConsoleCommand( "ifconfig" );
	}

	private final static boolean isMacAddress( String macAddressCandidate )
	{
		Pattern macPattern = Pattern.compile( "[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}" );

		Matcher m = macPattern.matcher( macAddressCandidate );

		return m.matches();
	}

}
