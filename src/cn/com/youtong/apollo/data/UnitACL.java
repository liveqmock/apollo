package cn.com.youtong.apollo.data;

/**
 * �û���ָ������ĵ�λACL
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */

import cn.com.youtong.apollo.usermanager.*;

public interface UnitACL
{
	/**
	 * ȡҪ�жϷ���Ȩ�޵��û�
	 * @return ACL����û�
	 */
	User getUser();

	/**
	 * ��ǰ�û��Ƿ���Բ鿴ָ����λ������
	 * @param unitID ��λID
	 * @return TRUE - ���Զ�ȡ�� FALSE - �����Զ�ȡ
	 */boolean isReadable(String unitID);

	/**
	 * ��λ�����Ƿ��д
	 * @param unitID ��λID
	 * @return TRUE - ��д��FALSE - ����д
	 */
	boolean isWritable(String unitID);
}