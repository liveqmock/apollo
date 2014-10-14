package cn.com.youtong.apollo.common.authenticators;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;

import cn.com.youtong.apollo.common.HttpUtils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HttpFormAuthenticator extends UserPasswordAuthenticator
{
	private static final int HTTP_STATUS_OK = 200;
	private static final int HTTP_STATUS_REDIRECT = 302;

	public static final String KEY_USER_ATTRIBUTE_NAME = "__USER_ATT_NAME__";
	public static final String KEY_PASSWORD_ATTRIBUTE_NAME = "__PASSWORD_ATT_NAME";
	public static final String KEY_FORM_ADDTIONAL_DATA = "__FORM_ADDTIONAL_DATA__";
	public static final String KEY_LOGIN_URL = "__LOGIN_URL__";
	public static final String KEY_LOGOUT_URL = "__LOGOUT_URL__";
	public static final String KEY_USES_COOKIE = "__USES_COOKIE__";

	private String _userAttName;
	private String _passwordAttName;
	private String _formAddtionalData;
	private String _loginUrl;
	private String _logoutUrl;
	private boolean _usesCookie;

	private ArrayList _cookies;
	private String _urlParameters;
	private boolean _hasLoggedIn = false;

	public void init(Map props)
		throws Exception
	{
		super.init(props);

		_userAttName = (String) props.get(KEY_USER_ATTRIBUTE_NAME);
		_passwordAttName = (String) props.get(KEY_PASSWORD_ATTRIBUTE_NAME);
		_formAddtionalData = (String) props.get(KEY_FORM_ADDTIONAL_DATA);
		_loginUrl = (String) props.get(KEY_LOGIN_URL);
		_logoutUrl = (String) props.get(KEY_LOGOUT_URL);
		_usesCookie = ((Boolean) props.get(KEY_USES_COOKIE)).booleanValue();

		if(_userAttName == null || _passwordAttName == null || _loginUrl == null)
		{
			throw new Exception("HttpFormAuthCredential must set user attribute name, password attribute name and login url.");
		}
	}

	protected String getUserAttributeName()
	{
		return _userAttName;
	}

	protected String getPasswordAttributeName()
	{
		return _passwordAttName;
	}

	protected String getLoginUrl()
	{
		return _loginUrl;
	}

	protected String getLogoutUrl()
	{
		return _logoutUrl;
	}

	protected String getFormAddtionalData()
	{
		return _formAddtionalData;
	}

	protected boolean isUsesCookie()
	{
		return _usesCookie;
	}

	/**
	 * Post login url to signin.
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection login()
		throws IOException, Exception
	{
		checkInitialized();

		//open longin url
		URL loginURL = new URL(getLoginUrl());
		HttpURLConnection httpConn = (HttpURLConnection) loginURL.openConnection();

		//prepare post data
		String postData = getUserAttributeName() + "=" + getUserID() + "&" + getPasswordAttributeName() + "=" + getPassword();
		if(_formAddtionalData != null && _formAddtionalData.length() > 0)
		{
			postData = postData + "&" + _formAddtionalData;

		}
		httpConn.setRequestMethod("POST");
		httpConn.setInstanceFollowRedirects(false);
		httpConn.setAllowUserInteraction(false);
		httpConn.setDoOutput(true);

		//send http POST request
		HttpUtils.httpPost(httpConn, postData);

		int responseCode = httpConn.getResponseCode();
		if(responseCode != HTTP_STATUS_OK && responseCode != HTTP_STATUS_REDIRECT)
		{
			throw new Exception("HTTP Response code is " + responseCode);
		}

		//get cookies from http response header
		_cookies = HttpUtils.extractCookies(httpConn);
		_urlParameters = httpConn.getHeaderField("Location");
		if(_urlParameters != null)
		{
			int index = _urlParameters.indexOf("?");
			if(index >= 0)
			{
				_urlParameters = _urlParameters.substring(index + 1);
			}
			else
			{
				_urlParameters = null;
			}
		}

		if(isUsesCookie())
		{
			if(_cookies.size() < 1)
			{
				throw new Exception("No cookies were sent back from the server.");
			}
		}
		else if(_urlParameters == null)
		{
			throw new Exception("No url parameters found in redirect url.");
		}

		_hasLoggedIn = true;

		return httpConn;
	}

	/**
	 * Logout Http connection
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection logout()
		throws IOException, Exception
	{
		if(!_hasLoggedIn)
		{
			throw new IOException("Not logged in yet!");
		}
		if(_logoutUrl == null || _logoutUrl.length() == 0)
		{
			return null;
		}

		//open logout url connection
		URL logoutURL = new URL(_logoutUrl);
		HttpURLConnection httpConn = (HttpURLConnection) logoutURL.openConnection();

		//add cookies from login POST action to request header
		addCookies(httpConn);

		httpConn.connect();
		int responseCode = httpConn.getResponseCode();
		if(responseCode != HTTP_STATUS_OK && responseCode != HTTP_STATUS_REDIRECT)
		{
			throw new Exception("Got no HTTP_OK, http status code=" + httpConn.getResponseCode());
		}
		_hasLoggedIn = false;

		return httpConn;
	}

	/**
	 * Add cookies to http request header
	 * @param connection Http connection
	 * @throws IOException
	 */
	private void addCookies(HttpURLConnection connection)
		throws IOException
	{
		String cookie = "";
		for(int i = 0; i < _cookies.size(); i++)
		{
			if(i > 0)
			{
				cookie = cookie + "; ";
			}
			cookie = cookie + _cookies.get(i);
		}

		if(cookie.length() > 0)
		{
			connection.setRequestProperty("Cookie", cookie);
		}
	}

	/**
	 * Add cookies to request header or login post response LOCATION parameters to request url
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public HttpURLConnection getAuthenticatedConnection(String url)
		throws IOException, Exception
	{
		checkInitialized();
		if(!_hasLoggedIn)
		{
			login();
		}
		if(!isUsesCookie())
		{
			if(url.indexOf("?") == -1)
			{
				url = url + "?";
			}
			else
			{
				url = url + "&";
			}
			url = url + _urlParameters;
		}
		URL loginURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) loginURL.openConnection();
		addCookies(connection);
		return connection;
	}

	public boolean isAuto()
	{
		return true;
	}
}