/*
 * Created on 2003-10-10
 */
package cn.com.youtong.apollo.common;

import java.io.*;
import java.util.zip.*;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wjb
 */
public class Util
{
	public static boolean isEmptyString( String s )
	{
		if( s == null || s.length() == 0 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 创建随机字符串
	 * @return 随机字符串
	 */
	public static String generateRandom()
	{
		double ran = Math.random() * 1000000000;
		java.text.DecimalFormat df = new java.text.DecimalFormat( "0" );
		return df.format( ran );
	}

	public static boolean validateMailAddress( String addr )
	{
		if( isEmptyString( addr ) )
		{
			return false;
		}
		int atIndex = addr.indexOf( "@" );
		if( atIndex < addr.length() - 1 && atIndex > 1 )
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static String decodeBase64( String s )
	{
		if( s == null )
		{
			return null;
		}

		byte[] b = new Base64().decode( s.getBytes() );

		return new String( b );
	}

	public static byte[] decodeBase64( byte[] b )
	{
		return new Base64().decode( b );
	}

	public static InputStream decodeBase64AndUnGZIP( String s )
		throws IOException
	{
		if( s == null )
		{
			return null;
		}

		//base64
		byte[] b = new Base64().decode( s.getBytes() );

		GZIPInputStream zip = new GZIPInputStream( new ByteArrayInputStream( b ) );

		return zip;
	}

	public static String encodeBase64( String s )
	{
		if( s == null )
		{
			return null;
		}

		byte[] b = new Base64().encode( s.getBytes() );
		return new String( b );
	}

        public static byte[] GZIPAndEncodeBase64(byte[] data) throws java.io.IOException
        {
          //进行GZIP压缩，压缩到bstream中
          ByteArrayOutputStream bstream = new ByteArrayOutputStream();
          GZIPOutputStream out = new GZIPOutputStream(bstream);
          out.write(data);
          out.close();
          byte[] retdata = Base64.encodeBase64(bstream.toByteArray());
          bstream.close();
          return retdata;
        }

	public static byte[] encodeBase64( byte[] b )
	{
		return new Base64().encode( b );
	}

	public static void close( InputStream is )
	{
		if( is != null )
		{
			try
			{
				is.close();
			}
			catch( IOException ioe )
			{}
		}
	}

	public static void close( OutputStream os )
	{
		if( os != null )
		{
			try
			{
				os.close();
			}
			catch( IOException ioe )
			{}
		}
	}

	public static void close( Reader reader )
	{
		if( reader != null )
		{
			try
			{
				reader.close();
			}
			catch( IOException e )
			{}
		}
	}

	public static void close( RandomAccessFile raf )
	{
		if( raf != null )
		{
			try
			{
				raf.close();
			}
			catch( IOException ex)
			{}
		}
	}

	public static void close( Writer r )
	{
		if( r!= null )
		{
			try
			{
				r.close();
			}
			catch( IOException ioe)
			{ }
		}
	}

	public static void println( OutputStream out, String s )
		throws IOException
	{
		out.write( s.getBytes() );
		out.write( "\n".getBytes() );
	}

	public static void print( OutputStream out, String s )
		throws IOException
	{
		out.write( s.getBytes() );
	}
}
