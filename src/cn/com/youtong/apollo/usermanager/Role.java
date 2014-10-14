package cn.com.youtong.apollo.usermanager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface Role
{

	/**
	 * 角色的ID号
	 * @return ID号
	 */
	public Integer getRoleID();

	/**
	 * 获得创建日期
	 * @return 创建日期
	 */
	public java.util.Date getDateCreated();

	/**
	 * 获得最后修改日期
	 * @return 最后修改日期
	 */
	public java.util.Date getDateModified();

	/**
	 * 获得角色名称
	 * @return 名称
	 */
	public String getName();

	/**
	 * 获得备注
	 * @return 备注
	 */
	public String getMemo();

	/**
	 * 获得权限
	 * @return 权限集
	 */
	public SetOfPrivileges getPrivileges();
}