package cn.com.youtong.apollo.common.authenticators;

/**
 * <p>Title: Credential data implementation</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * Development state: finished.
 * @author not attributable
 * @version 1.0
 */
public class CredentialDataImpl
	implements CredentialData
{
	protected String id;
	protected String password;
	protected byte[] binararyData;

	public CredentialDataImpl(String id, String password, byte[] binararyData)
	{
		this.id = id;
		this.password = password;
		this.binararyData = binararyData;
	}

	public String getAppUserID()
	{
		return id;
	}

	public String getAppPassword()
	{
		return password;
	}

	public byte[] getAppBinaryData()
	{
		return binararyData;
	}
}
