package cn.com.youtong.apollo.usermanager.db;

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

public class DBRole
	implements Role
{
	/**
	 * ��ɫ��Ϣ����
	 */
	private RoleForm roleForm;

	public DBRole(RoleForm role)
	{
		this.roleForm = role;
	}

	/**
	 * ��ɫ��ID��
	 * @return ID��
	 */
	public Integer getRoleID()
	{
		return roleForm.getRoleID();
	}

	/**
	 * ��ô�������
	 * @return ��������
	 */
	public java.util.Date getDateCreated()
	{
		return roleForm.getDateCreated();
	}

	/**
	 * �������޸�����
	 * @return ����޸�����
	 */
	public java.util.Date getDateModified()
	{
		return roleForm.getDateModified();
	}

	/**
	 * ��ý�ɫ����
	 * @return ����
	 */
	public String getName()
	{
		return roleForm.getName();
	}

	/**
	 * ���øý�ɫ������
	 * @param name ��ɫ��
	 */
//  public void setName(String name)
//  {
//    roleForm.setName(name);
//  }

	/**
	 * ��ñ�ע
	 * @return ��ע
	 */
	public String getMemo()
	{
		return roleForm.getMemo();
	}

	/**
	 * ���ñ�ע
	 * @param memo ��ע
	 */
//  public void setMemo(String memo)
//  {
//    roleForm.setMemo(memo);
//  }

	/**
	 * ����Ȩ��
	 * @param set Ȩ�޼�����
	 */
//  public void setPrivileges(SetOfPrivileges set)
//  {
//    roleForm.setUserRights(set.toString());
//  }

	/**
	 * ���Ȩ��
	 * @return Ȩ�޼�
	 */
	public SetOfPrivileges getPrivileges()
	{
		return new SetOfPrivileges(roleForm.getUserRights());
	}

}