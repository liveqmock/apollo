package cn.com.youtong.apollo.authentication;

public class AuthEnvironment
{

	private java.util.HashMap mapEnv = null;

	private static final Object ENVIRONMENT_SECURITY_PRINCIPAL = (Object) "e1";

	private static final Object ENVIRONMENT_SECURITY_CREDENTIALS = (Object) "e2";

	private static final Object ENVIRONMENT_AUTHENTICATION_TYPE = (Object) "e3";

	/**认证方式*/
	public static final String AUTHENTICATION_TYPE_RDBMS = "RDBMS";

	/**
	 * 构造函数
	 * @param principal 用户标识
	 * @param credentials 认证数据
	 */
	public AuthEnvironment(String principal, String credentials)
	{
		this(principal, credentials, true);
	}

	/**
	 * 构造函数
	 * @param principal 用户标识
	 * @param credentials 认证数据
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
	 * 环境参数是否为空
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return(mapEnv.isEmpty());
	}

	/**
	 * 获得认证标识(用户名)
	 * @return 字符串
	 */
	public String getSecurityPrincipal()
	{
		return(String)this.mapEnv.get(ENVIRONMENT_SECURITY_PRINCIPAL);
	}

	/**
	 * 设置认证标识(用户名)
	 * @param principal 认证标识字符串
	 */
	public void setSecurityPrincipal(String principal)
	{
		this.mapEnv.put(ENVIRONMENT_SECURITY_PRINCIPAL, principal);
	}

	/**
	 * 设置认证依据(密码)
	 * @param credentials 认证依据
	 */
	public void setSecurityCredentials(java.lang.Object credentials)
	{
		this.mapEnv.put(ENVIRONMENT_SECURITY_CREDENTIALS, credentials);
	}

	/**
	 * 获得认证依据(密码)
	 * @return Object对象
	 */
	public java.lang.Object getSecurityCredentials()
	{
		return this.mapEnv.get(ENVIRONMENT_SECURITY_CREDENTIALS);
	}

	/**
	 * 设置认证类型
	 * @param authenticationType 认证类型(常量)
	 */
	public void setAuthenticationType(String authenticationType)
	{
		this.mapEnv.put(ENVIRONMENT_AUTHENTICATION_TYPE, authenticationType);
	}

	/**
	 * 获得认证类型
	 * @return 字符串
	 */
	public String getAuthenticationType()
	{
		return(String)this.mapEnv.get(ENVIRONMENT_AUTHENTICATION_TYPE);
	}
}