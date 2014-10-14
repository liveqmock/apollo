package cn.com.youtong.apollo.common.authenticators;

/**
 * <p>Title: Credential Data</p>
 * <p>Description: Store credential secrets, such as userID/password</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface CredentialData
{
	/**
	 * specific user application id
	 * @return
	 */
	public String getAppUserID();

	/**
	 * specific user application password
	 * @return
	 */
	public String getAppPassword();

	/**
	 * specific user application authentication datas
	 * @return
	 */
	public byte[] getAppBinaryData();
}