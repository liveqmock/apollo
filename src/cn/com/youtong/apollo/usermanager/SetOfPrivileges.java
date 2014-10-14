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

	/**Ȩ�� -- �û����� */
	public static final int MANAGE_USER = 0;

	/**Ȩ�� -- �������*/
	public static final int MANAGE_TASK = 1;

	/**Ȩ�� -- �������*/
	public static final int MANAGE_REPORT = 2;

	/**Ȩ�� -- ִ��ѡ����ܷ���*/
	public static final int EXECUTE_SELECT_SUM_SCHEMA = 3;

	/**Ȩ�� -- ��ѯ�����*/
	public static final int QUERY_FILL_STATE = 4;

	/**Ȩ�� -- ǿ�Ƶ�������*/
	public static final int FORCE_IMPORT_OVERDUE_DATA = 5;

	/**Ȩ�� -- ��˵�λ*/
	public static final int PRIVILEGE_AUDIT = 6;

	/**Ȩ�� -- ����û�*/
	public static final int PRIVILEGE_AUDIT_USER = 7;

	/**Ȩ��9*/
	public static final int USER_PRIVILEGE_RESERVED_9 = 8;

	/**Ȩ��10*/
	public static final int USER_PRIVILEGE_RESERVED_10 = 9;

	/**Ȩ��11*/
	public static final int USER_PRIVILEGE_RESERVED_11 = 10;

	/**Ȩ��12*/
	public static final int USER_PRIVILEGE_RESERVED_12 = 11;

	/**Ȩ��13*/
	public static final int USER_PRIVILEGE_RESERVED_13 = 12;

	/**Ȩ��14*/
	public static final int USER_PRIVILEGE_RESERVED_14 = 13;

	/**Ȩ��15*/
	public static final int USER_PRIVILEGE_RESERVED_15 = 14;

	/**Ȩ��16*/
	public static final int USER_PRIVILEGE_RESERVED_16 = 15;

	/**Ȩ��17*/
	public static final int USER_PRIVILEGE_RESERVED_17 = 16;

	/**Ȩ��18*/
	public static final int USER_PRIVILEGE_RESERVED_18 = 17;

	/**Ȩ��19*/
	public static final int USER_PRIVILEGE_RESERVED_19 = 18;

	/**Ȩ��20*/
	public static final int USER_PRIVILEGE_RESERVED_20 = 19;

	/**Ȩ��21*/
	public static final int USER_PRIVILEGE_RESERVED_21 = 20;

	/**Ȩ��22*/
	public static final int USER_PRIVILEGE_RESERVED_22 = 21;

	/**Ȩ��23*/
	public static final int USER_PRIVILEGE_RESERVED_23 = 22;

	/**Ȩ��24*/
	public static final int USER_PRIVILEGE_RESERVED_24 = 23;

	/**Ȩ��25*/
	public static final int USER_PRIVILEGE_RESERVED_25 = 24;

	/**Ȩ��26*/
	public static final int USER_PRIVILEGE_RESERVED_26 = 25;

	/**Ȩ��27*/
	public static final int USER_PRIVILEGE_RESERVED_27 = 26;

	/**Ȩ��28*/
	public static final int USER_PRIVILEGE_RESERVED_28 = 27;

	/**Ȩ��29*/
	public static final int USER_PRIVILEGE_RESERVED_29 = 28;

	/**Ȩ��30*/
	public static final int USER_PRIVILEGE_RESERVED_30 = 29;

	/**Ȩ��31*/
	public static final int USER_PRIVILEGE_RESERVED_31 = 30;

	/**Ȩ��32*/
	public static final int USER_PRIVILEGE_RESERVED_32 = 31;

	/**Ȩ������*/
	public static final int USER_PRIVILEGES_SIZE = 32;

	/**���õ�Ȩ������*/
	public static final int AVAILABLE_PRIVILEGES_SIZE = 8;

	/**Ȩ�޼���*/
	private boolean[] setOfPrivileges = null;

	/**Ȩ����������*/
	public static final String[] PRIVILEGE_NAMES =
	{
		"�����û�", "��������", "������", "ִ��ѡ����ܷ���", "�鿴����",
		"ǿ�Ƶ����������", "���", "����쵼", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", "", "", "", "",
		"", ""
	};

	/**Ȩ��ע������*/
	public static final String[] PRIVILEGE_MEMOS =
	{
		"���ӡ��޸ġ�ɾ���û�����ͽ�ɫ���Լ�Ϊ�û����������Դ����Ȩ��",
		"������ɾ�����񣬱������ű���ѡ����ܷ����Լ������ֵ�",
		"�ϱ����ݣ�ָ���ѯ�ͻ���",
		"ִ��ѡ����ܷ���",
		"�鿴����",
		"ǿ�Ƶ������ݣ����������Ƿ����",
		"�������",
		"�Բ����쵼�������",
		"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
	};

	/**���캯��*/
	public SetOfPrivileges()
	{
		setOfPrivileges = new boolean[USER_PRIVILEGES_SIZE];
	}

	/**
	 * ���캯��
	 * @param privileges Ȩ���ַ���
	 */
	public SetOfPrivileges(String privileges)
	{
		this();
		setPrivileges(privileges);
	}

	/**
	 * ���캯��
	 * @param privileges  Ȩ������
	 */
	public SetOfPrivileges(boolean[] privileges)
	{
		this();
		setPrivileges(privileges);
	}

	/**
	 * ���Ȩ�޲�������
	 * @return Ȩ�޲�������
	 */
	public boolean[] toBooleanArray()
	{

		return this.setOfPrivileges;
	}

	/**
	 * ����Ȩ�޼�
	 * @param privileges Ȩ�޼�
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
	 * ����Ȩ�޼�
	 * @param privileges  Ȩ�޼�
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
	 * �鿴������Ȩ���Ƿ���Ȩ�޷�Χ��
	 * @param p  Ȩ�޶�Ӧ�� int ֵ
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
	 * ���ظ�����Ȩ��
	 * @param privilege Ȩ�޶�Ӧ�� int ֵ
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
	 * ����Ȩ��
	 * @param privilege Ȩ�޶�Ӧ�� int ֵ
	 * @param right Ȩ��ֵboolean
	 */
	public void setPrivilege(int privilege, boolean right)
	{
		if(inRange(privilege))
		{
			setOfPrivileges[privilege] = right;
		}
	}

	/**�鿴Ȩ��*/
	public void print()
	{

		for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
		{
			System.out.println("setOfPrivileges[" + i + "]=" + setOfPrivileges[i]);
		}
	}

	/**
	 * �������е�Ȩ��
	 * @param right  Ȩ��ֵ  boolean
	 */
	public void setAllPrivileges(boolean right)
	{
		for(int i = 0; i < USER_PRIVILEGES_SIZE; i++)
		{
			setOfPrivileges[i] = right;
		}
	}

	/**
	 * ��Ȩ����Ϊһ���ַ�������
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
