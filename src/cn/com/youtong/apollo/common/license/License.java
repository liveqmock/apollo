package cn.com.youtong.apollo.common.license;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface License
{
	/**
	 * 返回license文件安装日期
	 * @return           license文件安装日期
	 * @throws LicenseException
	 */
	public java.util.Date installDate()
			throws LicenseException;

	/**
	 * 查看license是否有效,试用期,返回true;
	 * 如果试用期已过,没有注册,返回false;
	 * 注册用户,返回true
	 * @return           true or false
	 * @throws LicenseException
	 */
	public boolean isValid()
		throws LicenseException;

	/**
	 * 注册license
	 * @param key          注册码
	 * @return             注册是否成功
	 * @throws LicenseException
	 */
	public boolean regedit( String key )
		throws LicenseException;

	/**
	 * 查看还有多长时间的试用时间,如果为-1,表示过期.
	 * @return          剩下试用天数
	 * @throws LicenseException
	 */
	public int trailDay()
		throws LicenseException;

	/**
	 * 返回机器码,根据此机器码,通过算法得到注册值
	 * @return           机器码
	 * @throws LicenseException
	 */
	public String machineCode()
		throws LicenseException;

	/**
	 * 软件是否已注册
	 * @return         true or false
	 * @throws LicenseException
	 */
	public boolean isRegedited()
		throws LicenseException;
}