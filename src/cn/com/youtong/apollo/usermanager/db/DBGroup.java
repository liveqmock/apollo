package cn.com.youtong.apollo.usermanager.db;

import java.util.*;

import cn.com.youtong.apollo.usermanager.*;
import cn.com.youtong.apollo.usermanager.db.form.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DBGroup
	implements Group
{

	/**
	 * �û�����Ϣ����
	 */
	private GroupForm groupForm;

	public DBGroup(GroupForm groupForm)
	{
		this.groupForm = groupForm;
	}

	/**
	 * ���ظ����ID��
	 * @return ID��
	 */
	public Integer getGroupID()
	{
		return groupForm.getGroupID();
	}

	/**
	 * �����Ĵ���ʱ��
	 * @return ʱ��
	 */
	public java.util.Date getDateCreated()
	{
		return groupForm.getDateCreated();
	}

	/**
	 * ����������޸�ʱ��
	 * @return ����޸�ʱ��
	 */
	public java.util.Date getDateModified()
	{
		return groupForm.getDateModified();
	}

	/**
	 * �����������
	 * @return �������
	 */
	public String getName()
	{
		return groupForm.getName();
	}

	/**
	 * ������ı�ע
	 * @return ��ע.
	 */
	public String getMemo()
	{
		return groupForm.getMemo();
	}

	/**
	 * �����������
	 * @param name ����
	 * @throws UserManagerException
	 */
//  public void setName(String name)
//  {
//    groupForm.setName(name);
//  }

	/**
	 * ������ı�ע
	 * @param memo ��ע.
	 * @throws UserManagerException
	 */
//  public void setMemo(String memo)
//  {
//    groupForm.setMemo(memo);
//  }

	/**
	 * ��������û�
	 * @return �û��ļ��� User�ļ���
	 */
	public Collection getMembers()
	{
		//��UserForm���Ϸ�װ��DBUser����
		Collection users = new ArrayList();
		Iterator itr = groupForm.getUsers().iterator();
		while(itr.hasNext())
		{
			DBUser user = new DBUser((UserForm) itr.next());
			users.add(user);
		}
		return users;

	}

	public boolean equals(Object other)
	{
		return this.groupForm.equals(((DBGroup) other).groupForm);
	}

}