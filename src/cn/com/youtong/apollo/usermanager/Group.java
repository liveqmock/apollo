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

public interface Group
{

	/**
	 * ���ظ����ID��
	 * @return ID��
	 */
	public Integer getGroupID();

	/**
	 * �����Ĵ���ʱ��
	 * @return ʱ��
	 */
	public java.util.Date getDateCreated();

	/**
	 * ����������޸�ʱ��
	 * @return ����޸�ʱ��
	 */
	public java.util.Date getDateModified();

	/**
	 * �����������
	 * @return �������
	 */
	public String getName();

	/**
	 * ������ı�ע
	 * @return ��ע.
	 */
	public String getMemo();

	/**
	 * ��������û�
	 * @return �û��ļ��� User�ļ���
	 */
	public Collection getMembers();
}