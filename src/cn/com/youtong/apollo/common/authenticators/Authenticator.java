package cn.com.youtong.apollo.common.authenticators;

import java.util.Map;

/**
 * <p>Title: Credential</p>
 * <p>Description: SSO Credential abstract class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class Authenticator
{
	public static final String KEY_CREDENTIAL_DATA="SSO_Credential_Data";

	/**
	 * whether Credential is initialized.
	 */
	private boolean _bInitialized = false;

	/**
	 * Initialize credentials, before credentil be used it must be initialzed.
	 * @param props Credential parameters
	 */
	public void init(Map props) throws Exception
	{
		_bInitialized = true;
	}

	/**
	 * check whether credential id initialzed.
	 */
	protected void checkInitialized()
	{
		if(!_bInitialized)
			throw new RuntimeException("You must call init() before use Credential.");
	}

	/**
	 * Whether authentication is automate.
	 * If true Credential hides all credential secrets from user,
	 * otherwise the user should do authentication himself.
	 * @return true if auto.
	 */
	public abstract boolean isAuto();

	/**
	 * @return credential parameters
	 */
	public Map getParameters()
	{
		return new java.util.HashMap();
	}
}