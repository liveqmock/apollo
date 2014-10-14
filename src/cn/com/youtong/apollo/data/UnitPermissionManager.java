package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

/**
 * ��λȨ�޹��������������λȨ�޵ķ���
 */
public interface UnitPermissionManager
{
	/**
	 * ȡ���Ѿ��������ĵ�λȨ��
	 * ȡ��ʧ�����׳��쳣
	 * @param groupID ��ID��
	 * @param unitID ��λID
	 * @throws ModelException
	 */
	public void deleteUnitPermission(Integer groupID,
									 String unitID) throws ModelException;

	/**
	 * ֱ�Ӳ�ѯ���Ȩ��
	 * @param groupID ��ID��
	 * @param unitID ��λID��
	 * @param permission Ȩ�ޱ�ʶ
	 * @return boolean�ͱ�����true or false
	 * @throws ModelException
	 */
	public boolean getPermission(Integer groupID, String unitID,
								 int permission) throws ModelException;

	/**
	 * ���ݸ�������ID���ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param groupID ��ID��
	 * @return UntiAssignmentֵ����ļ���
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(Integer groupID) throws
		ModelException;

	/**
	 * ���ݸ����ĵ�λID���ص�λ������Ϣ UnitAssignment ֵ����ļ���
	 * ��ֵ�����������λ���롢�顢Ȩ����Ϣ��
	 * @param unitID ��λID��
	 * @return UntiAssignmentֵ����ļ���
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(String unitID) throws
		ModelException;

	/**
	 * ��ʼ��Ȩ��
	 * @param in �����ļ���
	 * @throws ModelException
	 */
	public void initFromFile(InputStream in) throws ModelException;

	/**
	 * �������Ȩ��
	 * ����ʧ�����׳��쳣
	 * @param groupID ���ID��
	 * @param unitID ��λID��
	 * @param right  Ȩ�޶��� ��Ȩ�޶�����Ϊ�գ����� new UnitPermission();�õ��ö�����ʱ��Ȩ��ΪĬ����СȨ��
	 * @throws ModelException
	 */
	public void setUnitPermission(Integer groupID, String unitID,
								  UnitPermission right) throws ModelException;

}