package cn.com.youtong.apollo.data;

import java.util.*;

/**
 * <p>Title: ��Ȩ�޼�������λ����Ȩ��</p>
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

	/**Ȩ��1*/
	public static final int UNIT_PERMISSION_READ = 0x00000001;

	/**Ȩ��2*/
	public static final int UNIT_PERMISSION_WRITE = 0x00000002;

	/**Ȩ��3*/
	public static final int UNIT_PERMISSION_RESERVED_3 = 0x00000004;

	/**Ȩ��4*/
	public static final int UNIT_PERMISSION_RESERVED_4 = 0x00000008;

	/**Ȩ��5*/
	public static final int UNIT_PERMISSION_RESERVED_5 = 0x00000010;

	/**Ȩ��6*/
	public static final int UNIT_PERMISSION_RESERVED_6 = 0x00000020;

	/**Ȩ��7*/
	public static final int UNIT_PERMISSION_RESERVED_7 = 0x00000040;

	/**Ȩ��8*/
	public static final int UNIT_PERMISSION_RESERVED_8 = 0x00000080;

	/**Ȩ��9*/
	public static final int UNIT_PERMISSION_RESERVED_9 = 0x00000100;

	/**Ȩ��10*/
	public static final int UNIT_PERMISSION_RESERVED_10 = 0x00000200;

	/**Ȩ��11*/
	public static final int UNIT_PERMISSION_RESERVED_11 = 0x00000400;

	/**Ȩ��12*/
	public static final int UNIT_PERMISSION_RESERVED_12 = 0x00000800;

	/**Ȩ��13*/
	public static final int UNIT_PERMISSION_RESERVED_13 = 0x00001000;

	/**Ȩ��14*/
	public static final int UNIT_PERMISSION_RESERVED_14 = 0x00002000;

	/**Ȩ��15*/
	public static final int UNIT_PERMISSION_RESERVED_15 = 0x00004000;

	/**Ȩ��16*/
	public static final int UNIT_PERMISSION_RESERVED_16 = 0x00008000;

	/**Ȩ��17*/
	public static final int UNIT_PERMISSION_RESERVED_17 = 0x00010000;

	/**Ȩ��18*/
	public static final int UNIT_PERMISSION_RESERVED_18 = 0x00020000;

	/**Ȩ��19*/
	public static final int UNIT_PERMISSION_RESERVED_19 = 0x00040000;

	/**Ȩ��20*/
	public static final int UNIT_PERMISSION_RESERVED_20 = 0x00080000;

	/**Ȩ��21*/
	public static final int UNIT_PERMISSION_RESERVED_21 = 0x00100000;

	/**Ȩ��22*/
	public static final int UNIT_PERMISSION_RESERVED_22 = 0x00200000;

	/**Ȩ��23*/
	public static final int UNIT_PERMISSION_RESERVED_23 = 0x00400000;

	/**Ȩ��24*/
	public static final int UNIT_PERMISSION_RESERVED_24 = 0x00800000;

	/**Ȩ��25*/
	public static final int UNIT_PERMISSION_RESERVED_25 = 0x01000000;

	/**Ȩ��26*/
	public static final int UNIT_PERMISSION_RESERVED_26 = 0x02000000;

	/**Ȩ��27*/
	public static final int UNIT_PERMISSION_RESERVED_27 = 0x04000000;

	/**Ȩ��28*/
	public static final int UNIT_PERMISSION_RESERVED_28 = 0x08000000;

	/**Ȩ��29*/
	public static final int UNIT_PERMISSION_RESERVED_29 = 0x10000000;

	/**Ȩ��30*/
	public static final int UNIT_PERMISSION_RESERVED_30 = 0x20000000;

	/**Ȩ��31*/
	public static final int UNIT_PERMISSION_RESERVED_31 = 0x40000000;

	/**Ȩ��32*/
	public static final int UNIT_PERMISSION_RESERVED_32 = 0x80000000;

	/**Ȩ������*/
	private static final int USER_PERMISSIONS_SIZE = 32;

	/**���õ�Ȩ������*/
	private static final int AVAILABLE_PERMISSIONS_SIZE = 3;

	/**����Ȩ��*/
	private int _rights;

	/**Ȩ����������*/
	private static final Hashtable PERMISSION_NAMES = new Hashtable();
	{
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_READ), "������");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_WRITE), "д����");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_3), "Ȩ��3����");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_4), "Ȩ��4����");
		PERMISSION_NAMES.put(new Integer(this.UNIT_PERMISSION_RESERVED_5), "Ȩ��5����");
	}

	public String getPermissionName(int permission)
	{
		return(String) PERMISSION_NAMES.get(new Integer(permission));
	}

	/**
	 * ���ظ�����Ȩ��
	 * @param rightNO Ȩ�޶�Ӧ�ĳ���ֵ
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
	 * �������е�Ȩ��
	 * @param right  Ȩ��ֵ  boolean
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
	 * ����Ȩ��
	 * @param privilege Ȩ�޶�Ӧ�� int ֵ
	 * @param right Ȩ��ֵboolean
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
	 * ����Ȩ�޵�intֵ
	 * @return intֵ
	 */
	public int intValue()
	{
		return _rights;
	}

	/**
	 * ��Ȩ��int��ת��Ϊ�ַ���
	 * 1��ת��Ϊ T ��0��ת��Ϊ F
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