package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;

/**
 * 报表数据汇总器，执行各种汇总操作
 */
public interface Summer
{
	/**
	 * 完全汇总单位ID
	 */
	public final static String SUM_ALL_UNIT_ID = "00000000";

	/**
	 * 调整节点差额表
	 * @param unitID 汇总节点单位ID
	 * @param time 任务时间
	 * @param isRecursive 是否递归调整所有子节点
	 * @throws ModelException
	 */
	void adjustNodeDiff(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;

	/**
	 * 创建选择汇总方案
	 * @param schemaStream 方案流
	 * @throws ModelException
	 */
	public void createSelectSumSchema(InputStream schemaStream) throws
		ModelException;

	/**
	 * 删除选择汇总方案
	 * @param schemaID 方案id
	 * @throws ModelException
	 */
	public void deleteSelectSumSchema(Integer schemaID) throws ModelException;

	/**
	 * 执行选择汇总方案
	 * @param schemaID 方案id
	 * @param taskTimeID 任务时间id
	 * @param unitACL 单位ACL
	 * @throws ModelException
	 */
	public void executeSelectSumSchema(Integer schemaID,
									   cn.com.youtong.apollo.task.TaskTime
									   taskTimeID, UnitACL unitACL) throws
		ModelException;

	/**
	 * 得到指定任务的所有选择汇总方案
	 * @taskID 任务ID
	 * @return 选择汇总方案Iterator
	 * @throws ModelException
	 */
	public Iterator getAllSelectSumSchemas(String taskID) throws ModelException;

	/**
	 * 得到指定的选择汇总方案
	 * @param schemaID 方案id
	 * @return 选择汇总方案
	 * @throws ModelException
	 */
	public SelectSumSchema getSelectSumSchema(Integer schemaID) throws
		ModelException;

	/**
	 * 节点汇总
	 * @param unitID 汇总节点单位ID
	 * @param time 任务时间
	 * @param isRecursive 是否递归汇总所有子节点
	 * @throws ModelException
	 */
	void sumNode(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;

	/**
	 * 选择汇总
	 * @param unitID 保存汇总结果的单位ID
	 * @param unitIDs 要汇总的单位ID集合
	 * @param time 汇总的任务时间
	 * @throws ModelException
	 */
	void sumUnits(String unitID, Collection unitIDs, TaskTime time) throws
		ModelException;

	/**
	 * 完全汇总。汇总所有非选择汇总,完全汇总和节点汇总单位
	 * @param unitID 保存汇总结果的单位ID
	 * @param time 汇总的任务时间
	 * @throws ModelException
	 */
	void sumAll(String unitID, TaskTime time) throws ModelException;

	/**
	 * 检查集团汇总节点单位下的所有节点汇总结果是否正确
	 * @param unitID 集团汇总节点单位ID
	 * @param time 任务时间
	 * @param isRecursive 是否递归汇总所有子节点
	 * @return 检查结果ValidateResult对象集合的Iterator
	 * @throws ModelException
	 */
	Iterator validateNodeSum(String unitID, TaskTime time, boolean isRecursive) throws
		ModelException;
}