package cn.com.youtong.apollo.usermanager;

import java.util.*;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface UserManager {

  /**
   * �����û�������ROLE��GROUP  �����ɹ��򷵻��û����󣬴���ʧ�����׳��쳣
   * @param name �ʺţ�not null��
   * @param password ���루not null��
   * @param enterpriseName ��ҵ���ƣ�not null��
   * @param lawPersionCode ���˴��루not null��
   * @param lawPersionName ���˴���not null��
   * @param lawPersionPhone ���˴���绰��not null��
   * @param contactPersionName ��ϵ�ˣ�not null��
   * @param contactPersionPhone ��ϵ�˵绰��not null��
   * @param contactPersionMobile ��ϵ���ֻ�
   * @param contactAddress ��ϵ�˵�ַ��not null��
   * @param postcode �ʱࣨnot null��
   * @param email �����ʼ�
   * @param fax ����
   * @param isValidate �Ƿ�ͨ
   * @param memo ��ע
   * @param roleID ��ɫID��
   * @param groupIDs ��ID��
   * @return User�ӿ�
   * @throws UserManagerException ����ʧ���׳�
   */
  public User createUser(String name, String password, String enterpriseName,
                         String lawPersionCode, String lawPersionName,
                         String lawPersionPhone, String contactPersionName,
                         String contactPersionPhone,
                         String contactPersionMobile, String contactAddress,
                         String postcode, String email, String fax,
                         boolean isValidate, String memo, Integer roleID,
                         Integer[] groupIDs) throws UserManagerException;

  /**
   * ���û��һ�����
   * @param name �û���name
   * @throws UserManagerException ʧ��
   */
  public void findPassword(String name) throws UserManagerException;

  /**
   * ���������û��Ƿ�ͨ
   * @param userIDs �û�ID������
   * @param isValidate true or false
   * @throws UserManagerException
   */
  public void batchSetUserFlag(Integer[] userIDs, boolean isValidate) throws
      UserManagerException;

  /**
   * ���õ����û��Ƿ�ͨ
   * @param userID �û�ID
   * @param isValidate true or false
   * @throws UserManagerException
   */
  public void setUserFlag(Integer userID, boolean isValidate) throws
      UserManagerException;

  /**
   * ɾ���û�  ɾ��ʧ�����׳��쳣
   * @param userID �û�ID
   * @throws UserManagerException
   */
  public void deleteUser(Integer userID) throws UserManagerException;

  /**
   * �޸��û������޸����룩    ʧ�����׳��쳣
   * @param userID  �û�ID�ţ�not null��
   * @param name �ʺţ�not null��
   * @param enterpriseName ��ҵ���ƣ�not null��
   * @param lawPersionCode ���˴��루not null��
   * @param lawPersionName ���˴���not null��
   * @param lawPersionPhone ���˴���绰��not null��
   * @param contactPersionName ��ϵ�ˣ�not null��
   * @param contactPersionPhone ��ϵ�˵绰��not null��
   * @param contactPersionMobile ��ϵ���ֻ�
   * @param contactAddress ��ϵ�˵�ַ��not null��
   * @param postcode �ʱࣨnot null��
   * @param email �����ʼ�
   * @param fax ����
   * @param isValidate �Ƿ�ͨ
   * @param memo ��ע
   * @param roleID ��ɫID��
   * @param groupIDs ��ID��
   * @throws UserManagerException
   */
  public void updateUser(Integer userID, String name, String enterpriseName,
                         String lawPersionCode, String lawPersionName,
                         String lawPersionPhone, String contactPersionName,
                         String contactPersionPhone,
                         String contactPersionMobile, String contactAddress,
                         String postcode, String email, String fax,
                         boolean isValidate, String memo, Integer roleID,
                         Integer[] groupIDs) throws UserManagerException;

  /**
   * �޸��û������޸����룩    ʧ�����׳��쳣
   * @param userID  �û�ID�ţ�not null��
   * @param name �ʺţ�not null��
   * @param enterpriseName ��ҵ���ƣ�not null��
   * @param lawPersionCode ���˴��루not null��
   * @param lawPersionName ���˴���not null��
   * @param lawPersionPhone ���˴���绰��not null��
   * @param contactPersionName ��ϵ�ˣ�not null��
   * @param contactPersionPhone ��ϵ�˵绰��not null��
   * @param contactPersionMobile ��ϵ���ֻ�
   * @param contactAddress ��ϵ�˵�ַ��not null��
   * @param postcode �ʱࣨnot null��
   * @param email �����ʼ�
   * @param fax ����
   * @param isValidate �Ƿ�ͨ
   * @param memo ��ע
   * @throws UserManagerException
   */
  public void updateUser(Integer userID, String name, String enterpriseName,
                         String lawPersionCode, String lawPersionName,
                         String lawPersionPhone, String contactPersionName,
                         String contactPersionPhone,
                         String contactPersionMobile, String contactAddress,
                         String postcode, String email, String fax,
                         boolean isValidate, String memo) throws
      UserManagerException;

  /**
   * �޸��û�
   * @param userID �û�ID�ţ�not null��
   * @param name �ʺţ�not null��
   * @param password ���루not null��
   * @param enterpriseName ��ҵ���ƣ�not null��
   * @param lawPersionCode ���˴��루not null��
   * @param lawPersionName ���˴���not null��
   * @param lawPersionPhone ���˴���绰��not null��
   * @param contactPersionName ��ϵ�ˣ�not null��
   * @param contactPersionPhone ��ϵ�˵绰��not null��
   * @param contactPersionMobile ��ϵ���ֻ�
   * @param contactAddress ��ϵ�˵�ַ��not null��
   * @param postcode �ʱࣨnot null��
   * @param email �����ʼ�
   * @param fax ����
   * @param isValidate �Ƿ�ͨ
   * @param memo ��ע
   * @param roleID ��ɫID��
   * @param groupIDs ��ID��
   * @throws UserManagerException �޸�ʧ���׳�
   */
  public void updateUser(Integer userID, String name, String password,
                         String enterpriseName, String lawPersionCode,
                         String lawPersionName, String lawPersionPhone,
                         String contactPersionName,
                         String contactPersionPhone,
                         String contactPersionMobile, String contactAddress,
                         String postcode, String email, String fax,
                         boolean isValidate, String memo, Integer roleID,
                         Integer[] groupIDs) throws UserManagerException;

  /**
   * ���ݱ�־��ѯ�û����û��Ƿ�ͨ����֤��
   * @param isValidate true or false
   * @return User����ĵ�����
   * @throws UserManagerException
   */
  public Collection getUserByFlag(boolean isValidate) throws
      UserManagerException;

  /**
   * ��ѯ�û���ģ����ѯ������ѯ����������Ϊ��null
   * @param name �û���
   * @param enterpriseName ��ҵ����
   * @return User����ĵ�����
   * @throws UserManagerException
   */
  public Collection getUser(String name, String enterpriseName) throws
      UserManagerException;

  /**
   * �޸��û�
   * @param userID �û�ID�ţ�not null��
   * @param name �ʺţ�not null��
   * @param password ���루not null��
   * @param enterpriseName ��ҵ���ƣ�not null��
   * @param lawPersionCode ���˴��루not null��
   * @param lawPersionName ���˴���not null��
   * @param lawPersionPhone ���˴���绰��not null��
   * @param contactPersionName ��ϵ�ˣ�not null��
   * @param contactPersionPhone ��ϵ�˵绰��not null��
   * @param contactPersionMobile ��ϵ���ֻ�
   * @param contactAddress ��ϵ�˵�ַ��not null��
   * @param postcode �ʱࣨnot null��
   * @param email �����ʼ�
   * @param fax ����
   * @param isValidate �Ƿ�ͨ
   * @param memo ��ע
   * @throws UserManagerException �޸�ʧ���׳�
   */
  public void updateUser(Integer userID, String name, String password,
                         String enterpriseName, String lawPersionCode,
                         String lawPersionName, String lawPersionPhone,
                         String contactPersionName,
                         String contactPersionPhone,
                         String contactPersionMobile, String contactAddress,
                         String postcode, String email, String fax,
                         boolean isValidate, String memo) throws
      UserManagerException;

  /**
   * �����ʺ��������û�  �ɹ��򷵻��û�����ʧ�����׳��쳣
   * @param name �ʺ�
   * @return �û�
   * @throws UserManagerException
   */
  public User getUserByName(String name) throws UserManagerException;

  /**
   * �����û�  �ɹ��򷵻��û�����ʧ�����׳��쳣
   * @param userID �û�ID��
   * @return the �û�
   * @throws UserManagerException
   */
  public User getUserByID(Integer userID) throws UserManagerException;

  /**
   * �������е��û�   �ɹ��򷵻��û��ļ��ϣ�ʧ���򷵻�Collection.size()Ϊ0�ļ���
   * @return User�ļ���
   * @throws UserManagerException
   */
  public Collection getAllUsers() throws UserManagerException;

  /**
   * ��ѯ���ڸ���������û�   �ɹ��򷵻��û��ļ��ϣ�ʧ���򷵻�Collection.size()Ϊ0�ļ���
   * @param groupID ���ID��
   * @return UserForm����
   * @throws UserManagerException
   */
  public Collection getUsersNotInGroup(Integer groupID) throws
      UserManagerException;

  /**
   * ������ �� �����ɹ���������󣬴���ʧ�����׳��쳣
   * @param name ����
   * @param memo ��ע
   * @param userIDs �����������û�id����
   * @return Group�ӿ�
   * @throws UserManagerException
   */
  public Group createGroup(String name, String memo, Integer[] userIDs) throws
      UserManagerException;

  /**
   * �ӳ�ʼ���ļ��ж�ȡ������ʼ���û��ĸ�����Ϣ
   * @param in ������
   * @throws UserManagerException
   */
  public void initFromFile(InputStream in) throws UserManagerException;

  /**
   * ɾ����  ɾ��ʧ�����׳��쳣
   * @param group Ҫɾ������
   * @throws UserManagerException
   */
//  public void deleteGroup(Group group) throws UserManagerException;

  /**
   * ɾ����  ɾ��ʧ�����׳��쳣
   * @param groupID ��ID��
   * @throws UserManagerException
   */
  public void deleteGroup(Integer groupID) throws UserManagerException;

  /**
   * �޸���   ʧ�����׳��쳣
   * @param groupID ��ID��
   * @param name ����
   * @param memo ��ע
   * @param userIDs �����������û�id����
   * @throws UserManagerException
   */
  public void updateGroup(Integer groupID, String name, String memo,
                          Integer[] userIDs) throws UserManagerException;

  /**
   * ������  �ɹ��򷵻������ʧ�����׳�һ���쳣
   * @param groupID ���ID��
   * @return ��
   * @throws UserManagerException
   */
  public Group getGroupByID(Integer groupID) throws UserManagerException;

  /**
   * ������  �ɹ��򷵻������ʧ���򷵻� null
   * @param groupName ����
   * @return ��
   * @throws UserManagerException
   */
  public Group getGroupByName(String groupName) throws UserManagerException;

  /**
   * ������  �ɹ��򷵻������ʧ���򷵻� null
   * @param groupName
   * @return
   * @throws UserManagerException
   */
  public List queryGroupByName(String groupName) throws UserManagerException;

  /**
   * ����������  �ɹ��򷵻���ļ��ϣ�ʧ���򷵻�Collection.()Ϊ0size�ļ���
   * @return Gruop �ļ���
   * @throws UserManagerException
   */
  public Collection getAllGroups() throws UserManagerException;

  /**
   * ��ѯ���������û�����  �ɹ��򷵻���ļ��ϣ�ʧ���򷵻�Collection.size()Ϊ0�ļ���
   * @param userID �û�ID��
   * @return Group����
   * @throws UserManagerException
   */
  public Collection getGroupsNotContainUser(Integer userID) throws
      UserManagerException;

  /**
   * ����Role  �����ɹ��򷵻ؽ�ɫ���󣬴���ʧ�����׳��쳣
   * @param roleName ��ɫ��
   * @param roleMemo ��ע
   * @param privileges Ȩ�޼�
   * @return Role�ӿ� RoleFormʵ��
   * @throws UserManagerException
   */
  public Role createRole(String roleName, String roleMemo,
                         SetOfPrivileges privileges) throws
      UserManagerException;

  /**
   * ɾ��Role
   * @param role ��ɫ
   * @throws UserManagerException
   */
//  public void deleteRole(Role role) throws UserManagerException;

  /**
   * ɾ��Role  ɾ��ʧ�����׳��쳣
   * @param roleID RoleID��
   * @throws UserManagerException
   */
  public void deleteRole(Integer roleID) throws UserManagerException;

  /**
   * �޸�Role   ʧ�����׳��쳣
   * @param roleID ��ɫID��
   * @param roleName ��ɫ��
   * @param roleMemo ��ע
   * @param privileges Ȩ��
   * @throws UserManagerException
   */
  public void updateRole(Integer roleID, String roleName, String roleMemo,
                         SetOfPrivileges privileges) throws
      UserManagerException;

  /**
   * ����ɫ�����û�
   * @param roleID  ��ɫID��
   * @param userIDs �û�ID����
   * @throws UserManagerException
   */
//  public void assignUserToRole(Integer roleID,Integer[] userIDs) throws UserManagerException;

  /**
   * ���û�������飬����ʧ�����׳��쳣
   * @param groupID ��ID��
   * @param userIDs �û�ID����
   * @throws UserManagerException
   */
//  public void assignUserToGroup(Integer groupID, Integer[] userIDs) throws
//      UserManagerException;

  /**
   * �����鵽�û��Ĺ�ϵ  ���������û�������ʧ�����׳��쳣
   * @param userID ��ID��
   * @param groupIDs �û�ID����
   * @throws UserManagerException
   */
//  public void assignGroupToUser(Integer userID, Integer[] groupIDs) throws
//      UserManagerException;

  /**
   * ������ɫ���û��Ĺ�ϵ  �ѽ�ɫ������û�������ʧ�����׳��쳣
   * @param userID ��ID��
   * @param roleID �û�ID����
   * @throws UserManagerException
   */
//  public void assignRoleToUser(Integer userID, Integer roleID) throws
//      UserManagerException;

  /**
   * ����Role  �ɹ��򷵻ؽ�ɫ����ʧ�����׳�һ���쳣
   * @param roleID ��ɫID��
   * @return a role
   * @throws UserManagerException
   */
  public Role getRoleByID(Integer roleID) throws UserManagerException;

  /**
   * ����ROLE  �ɹ��򷵻ؽ�ɫ����ʧ���򷵻� null
   * @param roleName ��ɫ����
   * @return a role
   * @throws UserManagerException
   */
  public Role getRoleByName(String roleName) throws UserManagerException;

  /**
   * �������н�ɫ   �ɹ��򷵻ؽ�ɫ�ļ��ϣ�ʧ���򷵻�Collection.size()Ϊ0�ļ���
   * @return Role �ļ���
   * @throws UserManagerException
   */
  public Collection getAllRoles() throws UserManagerException;

  /**
   * �ж��û��Ƿ����
   * @param userID �û�ID��
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isUserDefined(Integer userID) throws UserManagerException;

  /**
   * �ж��û��Ƿ����
   * @param userName �û�����
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isUserDefined(String userName) throws UserManagerException;

  /**
   * �ж����Ƿ����
   * @param groupID  ��ID��
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isGroupDefined(Integer groupID) throws UserManagerException;

  /**
   * �ж����Ƿ����
   * @param groupName ������
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isGroupDefined(String groupName) throws UserManagerException;

  /**
   * �жϸ�ID��Role�Ƿ����
   * @param roleID  RoleID��
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isRoleDefined(Integer roleID) throws UserManagerException;

  /**
   * �жϸ����Ƶ�Role�Ƿ����
   * @param roleName Role����
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isRoleDefined(String roleName) throws UserManagerException;

}