package cn.com.youtong.apollo.usermanager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SetOfPrivileges
{

	/**权限 -- 用户管理 */
	public static final int MANAGE_USER = 0;

	/**权限 -- 任务管理*/
	public static final int MANAGE_TASK = 1;

	/**权限 -- 报表管理*/
	public static final int MANAGE_REPORT = 2;

	/**权限 -- 执行选择汇总方案*/
	public static final int EXECUTE_SELECT_SUM_SCHEMA = 3;

	/**权限 -- 查询添报情况*/
	public static final int QUERY_FILL_STATE = 4;

	/**权限 -- 强制导入数据*/
	public static final int FORCE_IMPORT_OVERDUE_DATA = 5;

	/**权限 -- 审核单位*/
	public static final int PRIVILEGE_AUDIT = 6;

	/**权限 -- 审核用户*/
	public static final int PRIVILEGE_AUDIT_USER = 7;

	/**权限9*/
	public static final int USER_PRIVILEGE_RESERVED_9 = 8;

	/**权限10*/
	public static final int USER_PRIVILEGE_RESERVED_10 = 9;

	/**权限11*/
	public static final int USER_PRIVILEGE_RESERVED_11 = 10;

	/**权限12*/
	public static final int USER_PRIVILEGE_RESERVED_12 = 11;

	/**权限13*/
	public static final int USER_PRIVILEGE_RESERVED_13 = 12;

	/**权限14*/
	public static final int USER_PRIVILEGE_RESERVED_14 = 13;

	/**权限15*/
	public static final int USER_PRIVILEGE_RESERVED_15 = 14;

	/**权限16*/
	public static final int USER_PRIVILEGE_RESERVED_16 = 15;

	/**权限17*/
	public static final int USER_PRIVILEGE_RESERVED_17 = 16;

	/**权限18*/
	public static final int USER_PRIVILEGE_RESERVED_18 = 17;

	/**权限19*/
	public static final int USER_PRIVILEGE_RESERVED_19 = 18;

	/**权限20*/
	public static final int USER_PRIVILEGE_RESERVED_20 = 19;

	/**权限21*/
	public static final int USER_PRIVILEGE_RESERVED_21 = 20;

	/**权限22*/
	public static final int USER_PRIVILEGE_RESERVED_22 = 21;

	/**权限23*/
	public static final int USER_PRIVILEGE_RESERVED_23 = 22;

	/**权限24*/
	public static final int USER_PRIVILEGE_RESERVED_24 = 23;

	/**权限25*/
	public static final int USER_PRIVILEGE_RESERVED_25 = 24;

	/**权限26*/
	public static final int USER_PRIVILEGE_RESERVED_26 = 25;

	/**权限27*/
	public static final int USER_PRIVILEGE_RESERVED_27 = 26;

	/**权限28*/
	public static final int USER_PRIVILEGE_RESERVED_28 = 27;

	/**权限29*/
	public static final int USER_PRIVILEGE_RESERVED_29 = 28;

	/**权限30*/
	public static final int USER_PRIVILEGE_RESERVED_30 = 29;

	/**权限31*/
	public static final int USER_PRIVILEGE_RESERVED_31 = 30;

	/**权限32*/
	public static final int USER_PRIVILEGE_RESERVED_32 = 31;

	/**权限总数*/
	public static final int USER_PRIVILEGES_SIZE = 32;

	/**可用的权限总数*/
	public static final int AVAILABLE_PRIVILEGES_SIZE = 8;

	/**权限集合*/
	private boolean[] setOfPrivileges = null;

	/**权限名称数组*/
	public static final String[] PRIVILEGE_NAMES =
	{
		"管理用户", "管理任务", "管理报表", "执行选择汇总方案", "查看填报情况",
		"强制导入过期数据", "审核", "审核领导", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", ""
	};

	/**权限注释数组*/
	public static final String[] PRIVILEGE_MEMOS =
	{
		"增加、修改、删除用户、组和角色，以及为用户、组分配资源访问权限",
		"发布、删除任务，表样，脚本，选择汇总方案以及代码字典",
		"上报数据，指标查询和汇总",
		"执行选择汇总方案",
		"查看填报情况",
		"强制导入数据，无论数据是否过期",
		"审核数据",
		"对部局领导进行审核",
		"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
	};

	/**构造函数*/
	public SetOfPrivileges()
	{
		setOfPrivileges = new boolean[USER_PRIVILEGES_SIZE];
	}

	/**
	 * 构造函数
	 * @param privileges 权限字符串
	 */
	public SetOfPrivileges(String privileges)
	{
		this();
		setPrivileges(privileges);
	}

	/**
	 * 构造函数
	 * @param privileges  权限数组
	 */
	public SetOfPrivileges(boolean[] privileges)
	{
		this();
		setPrivileges(privileges);
	}

	/**
	 * 获得权限布尔数组
	 * @return 权限布尔数组
	 */
	public boolean[] toBooleanArray()
	{

		return this.setOfPrivileges;
	}

	/**
	 * 设置权限集
	 * @param privileges 权限集
	 */
	private void setPrivileges(boolean[] privileges)
	{

		if((privileges == null) || (privileges.length == 0))
		{

			setAllPrivileges(false);
			return;
		}

		if(privileges.length >= USER_PRIVILEGES_SIZE)
		{
			for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
			{
				this.setOfPrivileges[i] = privileges[i];
			}
		}
		else
		{
			for(int i = 0; i < privileges.length; i++)
			{
				this.setOfPrivileges[i] = privileges[i];
			}
		}
	}

	private static boolean convert(char c)
	{
		if(c == 'T')
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 设置权限集
	 * @param privileges  权限集
	 */
	private void setPrivileges(String privileges)
	{

		if((privileges == null) || (privileges.length() == 0))
		{

			setAllPrivileges(false);
			return;
		}

		char[] chars = privileges.toCharArray();

		if(chars.length >= USER_PRIVILEGES_SIZE)
		{
			for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
			{
				this.setOfPrivileges[i] = convert(chars[i]);
			}
		}
		else
		{
			for(int i = 0; i < chars.length; i++)
			{
				this.setOfPrivileges[i] = convert(chars[i]);
			}
		}
	}

	/**
	 * 查看给定的权限是否在权限范围内
	 * @param p  权限对应的 int 值
	 * @return  boolean
	 */
	private static boolean inRange(int p)
	{
		if((p >= USER_PRIVILEGES_SIZE) || (p < 0))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 返回给定的权限
	 * @param privilege 权限对应的 int 值
	 * @return boolean
	 */
	public boolean getPrivilege(int privilege)
	{
		if(inRange(privilege))
		{
			return setOfPrivileges[privilege];
		}
		else
		{
			return false;
		}
	}

	/**
	 * 设置权限
	 * @param privilege 权限对应的 int 值
	 * @param right 权限值boolean
	 */
	public void setPrivilege(int privilege, boolean right)
	{
		if(inRange(privilege))
		{
			setOfPrivileges[privilege] = right;
		}
	}

	/**查看权限*/
	public void print()
	{

		for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
		{
			System.out.println("setOfPrivileges[" + i + "]=" + setOfPrivileges[i]);
		}
	}

	/**
	 * 设置所有的权限
	 * @param right  权限值  boolean
	 */
	public void setAllPrivileges(boolean right)
	{
		for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
		{
			setOfPrivileges[i] = right;
		}
	}

	/**
	 * 将权限做为一个字符串返回
	 * @return String
	 */
	public String toString()
	{

		StringBuffer result = new StringBuffer(USER_PRIVILEGES_SIZE);

		for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
		{

			if(this.setOfPrivileges[i])
			{
				result.append('T');
			}
			else
			{
				result.append('F');
			}
		}

		return result.toString();
	}

}
