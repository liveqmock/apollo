package cn.com.youtong.apollo.usermanager;

import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface User
{

	/**
	 * 返回该用户的ID号
	 * @return Integer
	 */
	public Integer getUserID();

	/**
	 * 返回该用户的帐号
	 * @return 字符串
	 */
	public String getName();

	/**
	 * 获得用户密码
	 * @return 用户密码
	 */
	public String getPassword();

	/**
	 * 获得企业名称
	 * @return 企业名称
	 */
	public String	getEnterpriseName();

	/**
	 * 获得法人代码
	 * @return 法人代码
	 */
	public String	getLawPersionCode();

	/**
	 * 获得法人代表
	 * @return 法人代表
	 */
	public String	getLawPersionName();

	/**
	 * 获得法人代表电话
	 * @return 法人代表电话
	 */
	public String	getLawPersionPhone();

	/**
	 * 获得联系人名称
	 * @return 联系人名称
	 */
	public String	getContactPersionName();

	/**
	 * 获得联系人电话
	 * @return 联系人电话
	 */
	public String	getContactPersionPhone();

	/**
	 * 获得联系人手机
	 * @return 联系人手机
	 */
	public String getContactPersionMobile();

	/**
	 * 获得联系人地址
	 * @return 联系人地址
	 */
	public String	getContactAddress();

	/**
	 * 获得邮编
	 * @return 邮编
	 */
	public String	getPostcode();

	/**
	 * 返回该用户的电子邮件
	 * @return 电子邮件
	 */
	public String getEmail();

	/**
	 * 获得传真
	 * @return 传真
	 */
	public String	getFax();

	/**
	 * 是否有效（是否通过验证）
	 * @return 通过则返回true 否则flase
	 */
	public boolean isValidated();

	/**
	 * 返回该用户的备注
	 * @return 字符串
	 */
	public String getMemo();


	/**
	 * 返回该用户的角色
	 * @return 接口Role
	 */
	public Role getRole();

	/**
	 * 创建时间
	 * @return 创建时间
	 */
	public java.util.Date getDateCreated();

	/**
	 * 最后修改时间
	 * @return 最后修改时间
	 */
	public java.util.Date getDateModified();

	/**
	 * 获得用户所在组的集合
	 * @return Group集合
	 */
	public Collection getGroups();
}