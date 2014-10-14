package cn.com.youtong.apollo.data;

import java.util.*;

/**
 * <p>Title: 组权限集对象，用位控制权限</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author mk_008
 * @version 1.0
 */

public class UnitPermission
{
	public UnitPermission()
	{
		_rights = 1;
	}

	public UnitPermission(int permissions)
	{
		_rights = permissions;
	}

	/**权限1*/
	public static final int UNIT_PERMISSION_READ = 0x00000001;

	/**权限2*/
	public static final int UNIT_PERMISSION_WRITE = 0x00000002;

	/**权限3*/
	public static final int UNIT_PERMISSION_RESERVED_3 = 0x00000004;

	/**权限4*/
	public static final int UNIT_PERMISSION_RESERVED_4 = 0x00000008;

	/**权限5*/
	public static final int UNIT_PERMISSION_RESERVED_5 = 0x00000010;

	/**权限6*/
	public static final int UNIT_PERMISSION_RESERVED_6 = 0x00000020;

	/**权限7*/
	public static final int UNIT_PERMISSION_RESERVED_7 = 0x00000040;

	/**权限8*/
	public static final int UNIT_PERMISSION_RESERVED_8 = 0x00000080;

	/**权限9*/
	public static final int UNIT_PERMISSION_RESERVED_9 = 0x00000100;

	/**权限10*/
	public static final int UNIT_PERMISSION_RESERVED_10 = 0x00000200;

	/**权限11*/
	public static final int UNIT_PERMISSION_RESERVED_11 = 0x00000400;

	/**权限12*/
	public static final int UNIT_PERMISSION_RESERVED_12 = 0x00000800;

	/**权限13*/
	public static final int UNIT_PERMISSION_RESERVED_13 = 0x00001000;

	/**权限14*/
	public static final int UNIT_PERMISSION_RESERVED_14 = 0x00002000;

	/**权限15*/
	public static final int UNIT_PERMISSION_RESERVED_15 = 0x00004000;

	/**权限16*/
	public static final int UNIT_PERMISSION_RESERVED_16 = 0x00008000;

	/**权限17*/
	public static final int UNIT_PERMISSION_RESERVED_17 = 0x00010000;

	/**权限18*/
	public static final int UNIT_PERMISSION_RESERVED_18 = 0x00020000;

	/**权限19*/
	public static final int UNIT_PERMISSION_RESERVED_19 = 0x00040000;

	/**权限20*/
	public static final int UNIT_PERMISSION_RESERVED_20 = 0x00080000;

	/**权限21*/
	public static final int UNIT_PERMISSION_RESERVED_21 = 0x00100000;

	/**权限22*/
	public static final int UNIT_PERMISSION_RESERVED_22 = 0x00200000;

	/**权限23*/
	public static final int UNIT_PERMISSION_RESERVED_23 = 0x00400000;

	/**权限24*/
	public static final int UNIT_PERMISSION_RESERVED_24 = 0x00800000;

	/**权限25*/
	public static final int UNIT_PERMISSION_RESERVED_25 = 0x01000000;

	/**权限26*/
	public static final int UNIT_PERMISSION_RESERVED_26 = 0x02000000;

	/**权限27*/
	public static final int UNIT_PERMISSION_RESERVED_27 = 0x04000000;

	/**权限28*/
	public static final int UNIT_PERMISSION_RESERVED_28 = 0x08000000;

	/**权限29*/
	public static final int UNIT_PERMISSION_RESERVED_29 = 0x10000000;

	/**权限30*/
	public static final int UNIT_PERMISSION_RESERVED_30 = 0x20000000;

	/**权限31*/
	public static final int UNIT_PERMISSION_RESERVED_31 = 0x40000000;

	/**权限32*/
	public static final int UNIT_PERMISSION_RESERVED_32 = 0x80000000;

	/**权限总数*/
	private static final int USER_PERMISSIONS_SIZE = 32;

	/**可用的权限总数*/
	private static final int AVAILABLE_PERMISSIONS_SIZE = 3;

	/**保存权限*/
	private int _rights;

	/**权限名称数组*/
	private static final Hashtable PERMISSION_NAMES = new Hashtable();
	{
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_READ), "读操作");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_WRITE), "写操作");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_3), "权限3名称");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_4), "权限4名称");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_5), "权限5名称");
	}

	public String getPermissionName(int permission)
	{
		return(String) PERMISSION_NAMES.get(new Integer(permission));
	}

	/**
	 * 返回给定的权限
	 * @param rightNO 权限对应的常量值
	 * @return boolean
	 */
	public boolean getPermission(int permission)
	{
		if((permission & _rights) == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 设置所有的权限
	 * @param right  权限值  boolean
	 */
	public void setAllPermissions(boolean permissionValue)
	{
		if(permissionValue == false)
		{
			_rights = 0;
		}
		else
		{
			_rights = 0xFFFFFFFF;
		}
	}

	/**
	 * 设置权限
	 * @param privilege 权限对应的 int 值
	 * @param right 权限值boolean
	 */
	public void setPermission(int permission, boolean permissionValue)
	{
		if(permissionValue == true)
		{
			_rights = _rights | permission;
		}
		else
		{
			_rights = (~permission) & _rights;
		}
	}

	/**
	 * 返回权限的int值
	 * @return int值
	 */
	public int intValue()
	{
		return _rights;
	}

	/**
	 * 将权限int型转化为字符串
	 * 1则转化为 T ，0则转化为 F
	 * @return
	 */
	public String toString()
	{
		String result = "";
		for(int i = 0; i < 32; i++)
		{
			if(((_rights << i) & UnitPermission.UNIT_PERMISSION_RESERVED_32) == 0)
			{
				result += "F";
			}
			else
			{
				result += "T";
			}
		}
		return result;
	}
}