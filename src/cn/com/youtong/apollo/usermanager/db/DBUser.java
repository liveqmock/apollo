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
public class DBUser
	implements User
{
	/**
	 * �û���Ϣ����
	 */
	private UserForm userForm;

	public DBUser(UserForm user)
	{
		this.userForm = user;
	}


	/**
	 * ���ظ��û���ID��
	 * @return Integer
	 */
	public Integer getUserID()
	{
		return userForm.getUserID();
	}

	/**
	 * ���ظ��û����ʺ�
	 * @return �ַ���
	 */
	public String getName()
	{
		return userForm.getName();
	}


	/**
	 * ����û�����
	 * @return �û�����
	 */
	public String getPassword()
	{
		return userForm.getPassword();
	}

	/**
	 * �����ҵ����
	 * @return ��ҵ����
	 */
	public String	getEnterpriseName()
	{
		return userForm.getEnterpriseName();
	}

	/**
	 * ��÷��˴���
	 * @return ���˴���
	 */
	public String	getLawPersionCode()
	{
		return userForm.getLawPersionCode();
	}

	/**
	 * ��÷��˴���
	 * @return ���˴���
	 */
	public String	getLawPersionName()
	{
		return userForm.getLawPersionName();
	}

	/**
	 * ��÷��˴���绰
	 * @return ���˴���绰
	 */
	public String	getLawPersionPhone()
	{
		return userForm.getLawPersionPhone();
	}

	/**
	 * �����ϵ������
	 * @return ��ϵ������
	 */
	public String	getContactPersionName()
	{
		return userForm.getContactPersionName();
	}

	/**
	 * �����ϵ�˵绰
	 * @return ��ϵ�˵绰
	 */
	public String	getContactPersionPhone()
	{
		return userForm.getContactPersionPhone();
	}

	/**
	 * �����ϵ���ֻ�
	 * @return ��ϵ���ֻ�
	 */
	public String getContactPersionMobile()
	{
		return userForm.getContactPersionMobile();
	}

	/**
	 * �����ϵ�˵�ַ
	 * @return ��ϵ�˵�ַ
	 */
	public String	getContactAddress()
	{
		return userForm.getContactAddress();
	}

	/**
	 * ����ʱ�
	 * @return �ʱ�
	 */
	public String	getPostcode()
	{
		return userForm.getPostcode();
	}

	/**
	 * ���ظ��û��ĵ����ʼ�
	 * @return �����ʼ�
	 */
	public String getEmail()
	{
		return userForm.getEmail();
	}

	/**
	 * ��ô���
	 * @return ����
	 */
	public String	getFax()
	{
		return userForm.getFax();
	}

	/**
	 * �Ƿ���Ч���Ƿ�ͨ����֤��
	 * @return ͨ���򷵻�true ����flase
	 */
	public boolean isValidated()
	{
		if(userForm.getFlag()==1)
		{
			return true;
		}
		return false;
	}

	/**
	 * ���ظ��û��ı�ע
	 * @return �ַ���
	 */
	public String getMemo()
	{
		return userForm.getMemo();
	}

	/**
	 * ���ظ��û��Ľ�ɫ
	 * @return �ӿ�Role
	 */
	public Role getRole()
	{
		//��RoleForm�����װ��DBRole����
		RoleForm role= userForm.getRole();
		if(role == null)
			return null;
		else
			return new DBRole(role);
	}

	/**
	 * ����ʱ��
	 * @return ����ʱ��
	 */
	public java.util.Date getDateCreated()
	{
		return userForm.getDateCreated();
	}

	/**
	 * ����޸�ʱ��
	 * @return ����޸�ʱ��
	 */
	public java.util.Date getDateModified()
	{
		return userForm.getDateModified();
	}

	/**
	 * ����û�������ļ���
	 * @return Group����
	 */
	public Collection getGroups()
	{
		//��GroupForm���Ϸ�װ��DBGroup����
		Collection groups = new ArrayList();
		Iterator itr = userForm.getGroups().iterator();
		while(itr.hasNext())
		{
			DBGroup group = new DBGroup((GroupForm) itr.next());
			groups.add(group);
		}
		return groups;
	}
}