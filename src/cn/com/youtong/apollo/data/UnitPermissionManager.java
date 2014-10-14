package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

/**
 * 单位权限管理器，负责管理单位权限的分配
 */
public interface UnitPermissionManager
{
	/**
	 * 取消已经分配给组的单位权限
	 * 取消失败则抛出异常
	 * @param groupID 组ID号
	 * @param unitID 单位ID
	 * @throws ModelException
	 */
	public void deleteUnitPermission(Integer groupID,
									 String unitID) throws ModelException;

	/**
	 * 直接查询组的权限
	 * @param groupID 组ID号
	 * @param unitID 单位ID号
	 * @param permission 权限标识
	 * @return boolean型变量，true or false
	 * @throws ModelException
	 */
	public boolean getPermission(Integer groupID, String unitID,
								 int permission) throws ModelException;

	/**
	 * 根据给定的组ID返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param groupID 组ID号
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(Integer groupID) throws
		ModelException;

	/**
	 * 根据给定的单位ID返回单位分配信息 UnitAssignment 值对象的集合
	 * 该值对象包括（单位代码、组、权限信息）
	 * @param unitID 单位ID号
	 * @return UntiAssignment值对象的集合
	 * @throws ModelException
	 */
	public Collection getUnitAssignmentInfo(String unitID) throws
		ModelException;

	/**
	 * 初始化权限
	 * @param in 输入文件流
	 * @throws ModelException
	 */
	public void initFromFile(InputStream in) throws ModelException;

	/**
	 * 给组分配权限
	 * 分配失败则抛出异常
	 * @param groupID 组的ID号
	 * @param unitID 单位ID号
	 * @param right  权限对象 ，权限对象不能为空，可以 new UnitPermission();得到该对象，这时的权限为默认最小权限
	 * @throws ModelException
	 */
	public void setUnitPermission(Integer groupID, String unitID,
								  UnitPermission right) throws ModelException;

}