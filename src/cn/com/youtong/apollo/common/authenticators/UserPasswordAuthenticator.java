package cn.com.youtong.apollo.common.authenticators;

import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UserPasswordAuthenticator extends Authenticator
{
	private String _userID;
	private String _password;

	public void init(Map props) throws Exception
	{
		super.init(props);

		if(props == null)
			throw new Exception("Credential properties cann't be null.");
		CredentialData secret= (CredentialData)props.get(Authenticator.KEY_CREDENTIAL_DATA);
		if(secret == null)
			throw new Exception("Credential data not set.");

		_userID= secret.getAppUserID() ;
		_password= secret.getAppPassword() ;
	}

	public String getUserID()
	{
		return _userID;
	}

	public String getPassword()
	{
		return _password;
	}

	/**
	 * @return false always
	 */
	public boolean isAuto()
	{
		return false;
	}
}