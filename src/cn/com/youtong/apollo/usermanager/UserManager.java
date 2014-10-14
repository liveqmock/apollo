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
   * 创建用户并分配ROLE与GROUP  创建成功则返回用户对象，创建失败则抛出异常
   * @param name 帐号（not null）
   * @param password 密码（not null）
   * @param enterpriseName 企业名称（not null）
   * @param lawPersionCode 法人代码（not null）
   * @param lawPersionName 法人代表（not null）
   * @param lawPersionPhone 法人代表电话（not null）
   * @param contactPersionName 联系人（not null）
   * @param contactPersionPhone 联系人电话（not null）
   * @param contactPersionMobile 联系人手机
   * @param contactAddress 联系人地址（not null）
   * @param postcode 邮编（not null）
   * @param email 电子邮件
   * @param fax 传真
   * @param isValidate 是否开通
   * @param memo 备注
   * @param roleID 角色ID号
   * @param groupIDs 组ID号
   * @return User接口
   * @throws UserManagerException 创建失败抛出
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
   * 帮用户找回密码
   * @param name 用户的name
   * @throws UserManagerException 失败
   */
  public void findPassword(String name) throws UserManagerException;

  /**
   * 批量设置用户是否开通
   * @param userIDs 用户ID的数组
   * @param isValidate true or false
   * @throws UserManagerException
   */
  public void batchSetUserFlag(Integer[] userIDs, boolean isValidate) throws
      UserManagerException;

  /**
   * 设置单个用户是否开通
   * @param userID 用户ID
   * @param isValidate true or false
   * @throws UserManagerException
   */
  public void setUserFlag(Integer userID, boolean isValidate) throws
      UserManagerException;

  /**
   * 删除用户  删除失败则抛出异常
   * @param userID 用户ID
   * @throws UserManagerException
   */
  public void deleteUser(Integer userID) throws UserManagerException;

  /**
   * 修改用户（不修改密码）    失败则抛出异常
   * @param userID  用户ID号（not null）
   * @param name 帐号（not null）
   * @param enterpriseName 企业名称（not null）
   * @param lawPersionCode 法人代码（not null）
   * @param lawPersionName 法人代表（not null）
   * @param lawPersionPhone 法人代表电话（not null）
   * @param contactPersionName 联系人（not null）
   * @param contactPersionPhone 联系人电话（not null）
   * @param contactPersionMobile 联系人手机
   * @param contactAddress 联系人地址（not null）
   * @param postcode 邮编（not null）
   * @param email 电子邮件
   * @param fax 传真
   * @param isValidate 是否开通
   * @param memo 备注
   * @param roleID 角色ID号
   * @param groupIDs 组ID号
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
   * 修改用户（不修改密码）    失败则抛出异常
   * @param userID  用户ID号（not null）
   * @param name 帐号（not null）
   * @param enterpriseName 企业名称（not null）
   * @param lawPersionCode 法人代码（not null）
   * @param lawPersionName 法人代表（not null）
   * @param lawPersionPhone 法人代表电话（not null）
   * @param contactPersionName 联系人（not null）
   * @param contactPersionPhone 联系人电话（not null）
   * @param contactPersionMobile 联系人手机
   * @param contactAddress 联系人地址（not null）
   * @param postcode 邮编（not null）
   * @param email 电子邮件
   * @param fax 传真
   * @param isValidate 是否开通
   * @param memo 备注
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
   * 修改用户
   * @param userID 用户ID号（not null）
   * @param name 帐号（not null）
   * @param password 密码（not null）
   * @param enterpriseName 企业名称（not null）
   * @param lawPersionCode 法人代码（not null）
   * @param lawPersionName 法人代表（not null）
   * @param lawPersionPhone 法人代表电话（not null）
   * @param contactPersionName 联系人（not null）
   * @param contactPersionPhone 联系人电话（not null）
   * @param contactPersionMobile 联系人手机
   * @param contactAddress 联系人地址（not null）
   * @param postcode 邮编（not null）
   * @param email 电子邮件
   * @param fax 传真
   * @param isValidate 是否开通
   * @param memo 备注
   * @param roleID 角色ID号
   * @param groupIDs 组ID号
   * @throws UserManagerException 修改失败抛出
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
   * 根据标志查询用户（用户是否通过验证）
   * @param isValidate true or false
   * @return User对象的迭代器
   * @throws UserManagerException
   */
  public Collection getUserByFlag(boolean isValidate) throws
      UserManagerException;

  /**
   * 查询用户（模糊查询），查询条件均可以为空null
   * @param name 用户名
   * @param enterpriseName 企业名称
   * @return User对象的迭代器
   * @throws UserManagerException
   */
  public Collection getUser(String name, String enterpriseName) throws
      UserManagerException;

  /**
   * 修改用户
   * @param userID 用户ID号（not null）
   * @param name 帐号（not null）
   * @param password 密码（not null）
   * @param enterpriseName 企业名称（not null）
   * @param lawPersionCode 法人代码（not null）
   * @param lawPersionName 法人代表（not null）
   * @param lawPersionPhone 法人代表电话（not null）
   * @param contactPersionName 联系人（not null）
   * @param contactPersionPhone 联系人电话（not null）
   * @param contactPersionMobile 联系人手机
   * @param contactAddress 联系人地址（not null）
   * @param postcode 邮编（not null）
   * @param email 电子邮件
   * @param fax 传真
   * @param isValidate 是否开通
   * @param memo 备注
   * @throws UserManagerException 修改失败抛出
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
   * 根据帐号名查找用户  成功则返回用户对象，失败则抛出异常
   * @param name 帐号
   * @return 用户
   * @throws UserManagerException
   */
  public User getUserByName(String name) throws UserManagerException;

  /**
   * 查找用户  成功则返回用户对象，失败则抛出异常
   * @param userID 用户ID号
   * @return the 用户
   * @throws UserManagerException
   */
  public User getUserByID(Integer userID) throws UserManagerException;

  /**
   * 查找所有的用户   成功则返回用户的集合，失败则返回Collection.size()为0的集合
   * @return User的集合
   * @throws UserManagerException
   */
  public Collection getAllUsers() throws UserManagerException;

  /**
   * 查询不在该组里面的用户   成功则返回用户的集合，失败则返回Collection.size()为0的集合
   * @param groupID 组的ID号
   * @return UserForm集合
   * @throws UserManagerException
   */
  public Collection getUsersNotInGroup(Integer groupID) throws
      UserManagerException;

  /**
   * 创建组 ， 创建成功返回组对象，创建失败则抛出异常
   * @param name 组名
   * @param memo 备注
   * @param userIDs 分配给该组的用户id数组
   * @return Group接口
   * @throws UserManagerException
   */
  public Group createGroup(String name, String memo, Integer[] userIDs) throws
      UserManagerException;

  /**
   * 从初始化文件中读取，并初始化用户的各种信息
   * @param in 输入流
   * @throws UserManagerException
   */
  public void initFromFile(InputStream in) throws UserManagerException;

  /**
   * 删除组  删除失败则抛出异常
   * @param group 要删除的组
   * @throws UserManagerException
   */
//  public void deleteGroup(Group group) throws UserManagerException;

  /**
   * 删除组  删除失败则抛出异常
   * @param groupID 组ID号
   * @throws UserManagerException
   */
  public void deleteGroup(Integer groupID) throws UserManagerException;

  /**
   * 修改组   失败则抛出异常
   * @param groupID 组ID号
   * @param name 组名
   * @param memo 备注
   * @param userIDs 分配给该组的用户id数组
   * @throws UserManagerException
   */
  public void updateGroup(Integer groupID, String name, String memo,
                          Integer[] userIDs) throws UserManagerException;

  /**
   * 查找组  成功则返回组对象，失败则抛出一个异常
   * @param groupID 组的ID号
   * @return 组
   * @throws UserManagerException
   */
  public Group getGroupByID(Integer groupID) throws UserManagerException;

  /**
   * 查找组  成功则返回组对象，失败则返回 null
   * @param groupName 组名
   * @return 组
   * @throws UserManagerException
   */
  public Group getGroupByName(String groupName) throws UserManagerException;

  /**
   * 查找组  成功则返回组对象，失败则返回 null
   * @param groupName
   * @return
   * @throws UserManagerException
   */
  public List queryGroupByName(String groupName) throws UserManagerException;

  /**
   * 查找所有组  成功则返回组的集合，失败则返回Collection.()为0size的集合
   * @return Gruop 的集合
   * @throws UserManagerException
   */
  public Collection getAllGroups() throws UserManagerException;

  /**
   * 查询不包含该用户的组  成功则返回组的集合，失败则返回Collection.size()为0的集合
   * @param userID 用户ID号
   * @return Group集合
   * @throws UserManagerException
   */
  public Collection getGroupsNotContainUser(Integer userID) throws
      UserManagerException;

  /**
   * 创建Role  创建成功则返回角色对象，创建失败则抛出异常
   * @param roleName 角色名
   * @param roleMemo 备注
   * @param privileges 权限集
   * @return Role接口 RoleForm实现
   * @throws UserManagerException
   */
  public Role createRole(String roleName, String roleMemo,
                         SetOfPrivileges privileges) throws
      UserManagerException;

  /**
   * 删除Role
   * @param role 角色
   * @throws UserManagerException
   */
//  public void deleteRole(Role role) throws UserManagerException;

  /**
   * 删除Role  删除失败则抛出异常
   * @param roleID RoleID号
   * @throws UserManagerException
   */
  public void deleteRole(Integer roleID) throws UserManagerException;

  /**
   * 修改Role   失败则抛出异常
   * @param roleID 角色ID号
   * @param roleName 角色名
   * @param roleMemo 备注
   * @param privileges 权限
   * @throws UserManagerException
   */
  public void updateRole(Integer roleID, String roleName, String roleMemo,
                         SetOfPrivileges privileges) throws
      UserManagerException;

  /**
   * 给角色分配用户
   * @param roleID  角色ID号
   * @param userIDs 用户ID数组
   * @throws UserManagerException
   */
//  public void assignUserToRole(Integer roleID,Integer[] userIDs) throws UserManagerException;

  /**
   * 把用户分配给组，分配失败则抛出异常
   * @param groupID 组ID号
   * @param userIDs 用户ID数组
   * @throws UserManagerException
   */
//  public void assignUserToGroup(Integer groupID, Integer[] userIDs) throws
//      UserManagerException;

  /**
   * 建立组到用户的关系  把组分配给用户，分配失败则抛出异常
   * @param userID 组ID号
   * @param groupIDs 用户ID数组
   * @throws UserManagerException
   */
//  public void assignGroupToUser(Integer userID, Integer[] groupIDs) throws
//      UserManagerException;

  /**
   * 建立角色到用户的关系  把角色分配给用户，分配失败则抛出异常
   * @param userID 组ID号
   * @param roleID 用户ID数组
   * @throws UserManagerException
   */
//  public void assignRoleToUser(Integer userID, Integer roleID) throws
//      UserManagerException;

  /**
   * 查找Role  成功则返回角色对象，失败则抛出一个异常
   * @param roleID 角色ID号
   * @return a role
   * @throws UserManagerException
   */
  public Role getRoleByID(Integer roleID) throws UserManagerException;

  /**
   * 查找ROLE  成功则返回角色对象，失败则返回 null
   * @param roleName 角色名称
   * @return a role
   * @throws UserManagerException
   */
  public Role getRoleByName(String roleName) throws UserManagerException;

  /**
   * 查找所有角色   成功则返回角色的集合，失败则返回Collection.size()为0的集合
   * @return Role 的集合
   * @throws UserManagerException
   */
  public Collection getAllRoles() throws UserManagerException;

  /**
   * 判断用户是否存在
   * @param userID 用户ID号
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isUserDefined(Integer userID) throws UserManagerException;

  /**
   * 判断用户是否存在
   * @param userName 用户名称
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isUserDefined(String userName) throws UserManagerException;

  /**
   * 判断组是否存在
   * @param groupID  组ID号
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isGroupDefined(Integer groupID) throws UserManagerException;

  /**
   * 判断组是否存在
   * @param groupName 组名称
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isGroupDefined(String groupName) throws UserManagerException;

  /**
   * 判断该ID的Role是否存在
   * @param roleID  RoleID号
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isRoleDefined(Integer roleID) throws UserManagerException;

  /**
   * 判断该名称的Role是否存在
   * @param roleName Role名称
   * @return boolean
   * @throws UserManagerException
   */
//  public boolean isRoleDefined(String roleName) throws UserManagerException;

}