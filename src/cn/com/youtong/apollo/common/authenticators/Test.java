package cn.com.youtong.apollo.common.authenticators;

import java.util.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import cn.com.youtong.apollo.common.HttpUtils;

public class Test
{
	public static void main(String[] args)
	{
		testTomSMS();
	}

	public static void testTomSMS()
	{
		//tomÍøÕ¾¶ÌÐÅ
		try
		{
			String url = "http://sms.tom.com/free_send.php";
			ArrayList fields= new ArrayList();
			String enc="gb2312";
			fields.add("idiograph=&submit=1&login_name=&password=&flag=&submit.x=95&submit.y=14");
			fields.add("rec_num=" + URLEncoder.encode("13693220395", enc));
			fields.add("content=" + URLEncoder.encode("³ÌÐò·¢¶ÌÐÅ²âÊÔ¡£¡£¡£¹þ¹þ¹þ¹þ", enc));
			fields.add("new_login_name=" + URLEncoder.encode("13911310372", enc));
			fields.add("new_password=" + URLEncoder.encode("9507118", enc));

			URL smsURL = new URL(url);
			HttpURLConnection httpSmsConn = (HttpURLConnection) smsURL.openConnection();
			httpSmsConn.setRequestMethod("POST");
			httpSmsConn.setInstanceFollowRedirects(false);
			httpSmsConn.setAllowUserInteraction(false);
			httpSmsConn.setDoOutput(true);
			HttpUtils.httpPost(httpSmsConn, fields);
			HttpUtils.dumpResponse(httpSmsConn);
			httpSmsConn.disconnect() ;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public static void testConn()
	{
		HttpFormAuthenticator auth = new HttpFormAuthenticator();
		HashMap props = new HashMap();
		props.put(HttpFormAuthenticator.KEY_LOGIN_URL, "http://sms.tom.com/deal.php");
		props.put(HttpFormAuthenticator.KEY_USER_ATTRIBUTE_NAME, "login_name");
		props.put(HttpFormAuthenticator.KEY_PASSWORD_ATTRIBUTE_NAME, "password");
		props.put(HttpFormAuthenticator.KEY_FORM_ADDTIONAL_DATA, "action=login");
		props.put(HttpFormAuthenticator.KEY_USES_COOKIE, Boolean.TRUE);
		CredentialData secret = new CredentialDataImpl("13911310372", "9507118", null);
		props.put(Authenticator.KEY_CREDENTIAL_DATA, secret);
		try
		{
			/*
			 content=justtest&rec_num=13522785115&idiograph=&new_login_name=13911310372&new_password=9507118&submit=1&login_name=&password=&flag=&submit.x=95&submit.y=14
			 */
			auth.init(props);
			HttpURLConnection httpLoginConn;
			//httpLoginConn= auth.login();
			String url = "http://sms.tom.com/free_send.php";
			String data = "content=justtest&rec_num=13522785115&idiograph=&new_login_name=13911310372&new_password=9507118&submit=1&login_name=&password=&flag=&submit.x=95&submit.y=14";
			URL smsURL = new URL(url);
			HttpURLConnection httpSmsConn = (HttpURLConnection) smsURL.openConnection();
			httpSmsConn.setRequestMethod("POST");
			httpSmsConn.setInstanceFollowRedirects(false);
			httpSmsConn.setAllowUserInteraction(false);
			httpSmsConn.setDoOutput(true);
			//auth.addCookies(httpSmsConn);
			//httpSmsConn.connect();
			HttpUtils.httpPost(httpSmsConn, data);
			HttpUtils.dumpResponse(httpSmsConn);
			//auth.logout() ;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}