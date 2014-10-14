package cn.com.youtong.apollo.authentication;

public class AuthEnvironment
{

	private java.util.HashMap mapEnv = null;

	private static final Object ENVIRONMENT_SECURITY_PRINCIPAL = (Object) "e1";

	private static final Object ENVIRONMENT_SECURITY_CREDENTIALS = (Object) "e2";

	private static final Object ENVIRONMENT_AUTHENTICATION_TYPE = (Object) "e3";

	/**��֤��ʽ*/
	public static final String AUTHENTICATION_TYPE_RDBMS = "RDBMS";

	/**
	 * ���캯��
	 * @param principal �û���ʶ
	 * @param credentials ��֤����
	 */
	public AuthEnvironment(String principal, String credentials)
	{
		this(principal, credentials, true);
	}

	/**
	 * ���캯��
	 * @param principal �û���ʶ
	 * @param credentials ��֤����
	 */
	public AuthEnvironment(String principal, Object credentials)
	{
		this(principal, credentials, true);
	}

	private AuthEnvironment(String principal, Object credentials, boolean single)
	{
		mapEnv = new java.util.HashMap();
		mapEnv.put(ENVIRONMENT_SECURITY_PRINCIPAL, principal);
		mapEnv.put(ENVIRONMENT_SECURITY_CREDENTIALS, credentials);
		mapEnv.put(ENVIRONMENT_AUTHENTICATION_TYPE, AUTHENTICATION_TYPE_RDBMS);
	}

	/**
	 * ���������Ƿ�Ϊ��
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return(mapEnv.isEmpty());
	}

	/**
	 * �����֤��ʶ(�û���)
	 * @return �ַ���
	 */
	public String getSecurityPrincipal()
	{
		return(String)this.mapEnv.get(ENVIRONMENT_SECURITY_PRINCIPAL);
	}

	/**
	 * ������֤��ʶ(�û���)
	 * @param principal ��֤��ʶ�ַ���
	 */
	public void setSecurityPrincipal(String principal)
	{
		this.mapEnv.put(ENVIRONMENT_SECURITY_PRINCIPAL, principal);
	}

	/**
	 * ������֤����(����)
	 * @param credentials ��֤����
	 */
	public void setSecurityCredentials(java.lang.Object credentials)
	{
		this.mapEnv.put(ENVIRONMENT_SECURITY_CREDENTIALS, credentials);
	}

	/**
	 * �����֤����(����)
	 * @return Object����
	 */
	public java.lang.Object getSecurityCredentials()
	{
		return this.mapEnv.get(ENVIRONMENT_SECURITY_CREDENTIALS);
	}

	/**
	 * ������֤����
	 * @param authenticationType ��֤����(����)
	 */
	public void setAuthenticationType(String authenticationType)
	{
		this.mapEnv.put(ENVIRONMENT_AUTHENTICATION_TYPE, authenticationType);
	}

	/**
	 * �����֤����
	 * @return �ַ���
	 */
	public String getAuthenticationType()
	{
		return(String)this.mapEnv.get(ENVIRONMENT_AUTHENTICATION_TYPE);
	}
}