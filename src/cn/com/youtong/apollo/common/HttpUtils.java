package cn.com.youtong.apollo.common;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HttpUtils
{
	/**
	 * Send http post request
	 * @param httpConn
	 * @param fields
	 * @throws Exception
	 */
	public static void httpPost(HttpURLConnection httpConn, List fields) throws Exception
	{
		StringBuffer body = new StringBuffer();
		if(fields != null)
		{
			for(int i = 0; i < fields.size(); i++)
			{
				if(i > 0)
				{
					body.append("&");
				}
				body.append(fields.get(i));
			}

		}
		httpPost(httpConn, body.toString());
	}

	/**
	 * Send http post request
	 * @param httpConn
	 * @param body Post data
	 * @throws Exception
	 */
	public static void httpPost(HttpURLConnection httpConn, String body) throws Exception
	{
		httpConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		httpConn.setRequestProperty("Content-length", Integer.toString(body.length()));

		java.io.OutputStream rawOutStream = httpConn.getOutputStream();
		PrintWriter pw = new PrintWriter(rawOutStream);
		pw.println(body);
		pw.flush();
		pw.close();
		httpConn.connect();
		httpConn.getInputStream();
	}

	/**
	 * extract cookies from http response header "Set-Cookie" field
	 * @param httpConn
	 * @return
	 */
	public static ArrayList extractCookies(HttpURLConnection httpConn)
	{
		int i = 0;
		String key = "";
		ArrayList cookies = new ArrayList();
		for(; key != null; key = httpConn.getHeaderFieldKey(i))
		{
			if(key.equals("Set-Cookie"))
			{
				String cookie = httpConn.getHeaderField(i);
				int index = cookie.indexOf(";");
				if(index >= 0)
				{
					cookie = cookie.substring(0, index);
				}
				cookies.add(cookie);
			}
			i++;
		}
		return cookies;
	}

	/**
	 * dump Http response
	 * @param httpConn
	 * @throws Exception
	 */
	public static void dumpResponse(HttpURLConnection httpConn) throws Exception
	{
		int n = 0;
		for(boolean done = false; !done; )
		{
			String headerKey = httpConn.getHeaderFieldKey(n);
			String headerVal = httpConn.getHeaderField(n);
			if(headerKey != null || headerVal != null)
			{
				System.out.println(headerKey + "=" + headerVal);
			}
			else
			{
				done = true;
			}
			n++;
		}

		System.out.println();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		String s;
		while((s = in.readLine()) != null)
		{
			System.out.println(s);
		}
	}

}