package cn.com.youtong.apollo.webservice;

import java.util.Date;

/**
 * WebService接口，这里定义 service 方法
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: YouTong</p>
 * @author wjb
 * @version 1.0
 */
public interface FrontService
{
	/**
	 * 根据用户名和密码导入数据。
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导入；</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 *  <li> 11 表示 没有对应任务 </li>
	 * </ul>
	 * @param data               上报的数据（XML格式）
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult uploadData(String data, String username, String password);

	/**
	 * 导出数据
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 *
	 * @param taskID  任务标识
	 * @param unitID  单位代码
	 * @param date    任务数据所在时间段中的一天
	 * @param username 用户名
	 * @param password 密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadData(String taskID, String unitID, Date date, String username, String password);

	/**
	 * 导出所有数据
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 *
	 * @param taskID  任务标识
	 * @param date    任务数据所在时间段中的一天
	 * @param username 用户名
	 * @param password 密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadAllData(String taskID, Date date, String username, String password);

	/**
	 * 导出数据
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 *
	 * @param taskID  任务标识
	 * @param unitID  单位代码
	 * @param date    任务数据所在时间段中的一天
	 * @param username 用户名
	 * @param password 密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadDataByTree(String taskID, String unitID, Date date, String username, String password);

	/**
	 * 发布任务
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 *
	 * @param definition  任务定义
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult publishTask(String definition, String username, String password);

	/**
	 *
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param id                 任务id
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadTask(String id, String username, String password);

	/**
	 * 删除任务
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param taskID             任务标识
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult deleteTask(String taskID, String username, String password);

	/**
	 * 发布代码字典
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param content           字典内容
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult publishDictionary(String content, String username, String password);

	/**
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param id                 字典标识
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadDictionary(String id, String username, String password);

	/**
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param id                 字典标识
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult deleteDictionary(String id, String username, String password);

	/**
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param taskID           任务标识
	 * @param script           脚本内容
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult publishScriptSuit(String taskID, String script, String username, String password);

	/**
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param taskID            任务标识
	 * @param suitName           脚本名称
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult downloadScriptSuit(String taskID, String suitName, String username, String password);

	/**
	 * 可能的返回值如下：
	 * <ul>
	 *  <li> 0  表示 成功导出；content值为数据的xml表示</li>
	 *  <li> 1  表示 用户名和密码不匹配；</li>
	 *  <li> -1 表示 系统内部错误；content值结果为:系统内部错误: + 异常提示 </li>
	 * </ul>
	 * @param taskID            任务标识
	 * @param suitName          脚本名称
	 * @param username           用户名
	 * @param password           密码
	 * @return ServiceResult      约定的返回值
	 */
	ServiceResult deleteScriptSuit(String taskID, String suitName, String username, String password);
}