package cn.com.youtong.apollo.task;

/**
 * <p>Title: 任务.</p>
 * <p>Description:
 * <ul>属性
 * <li>1.ID
 * <li>2.名称
 * <li><ul>3.时间属性
 *     <li>3.1类型：日报，月报，季报，年报
 *     <li>3.2上报时间区间，催报时间区间
 *       	日报— 小时，小时
 *       	月报，季报，年报— 日期，日期
 *      	过了上报时间不能上报，由管理员调整
 *     </ul>
 * <li>4.描述
 * <li>5.版本
 * <li>6.创建时间（系统自动维护）
 * <li>7.更新时间（系统自动维护）
 * </ul>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Task
{
    /**
     * 任务ID
     * @return
     */
    String id();

    /**
     * 任务名称
     * @return
     */
    String getName();

    /**
     * get created date
     * @return
     */
    Date getCreatedDate();

    /**
     * Get Modified Date
     * @return
     */
    Date getModifiedDate();

    /**
     * 任务描述
     * @return
     */
    String getMemo();

    /**
     * 版本
     * @return
     */
    int getVersion();
    /**
     * 任务类型 -- 日报
     */
    public static final int REPORT_OF_DAY = 0;

    /**
     * 任务类型 -- 月报
     */
    public static final int REPORT_OF_MONTH = 1;

    /**
     * 任务类型 -- 季报
     */
    public static final int REPORT_OF_QUARTER = 2;

    /**
     * 任务类型 -- 年报
     */
    public static final int REPORT_OF_YEAR = 3;

    /**
     * 任务类型 -- 旬报
     */
    public static final int REPORT_OF_XUN = 4;

    /**
     * 得到任务类型(日报，月报，季报，年报)
     * @return each iterator memeber is a Table object
     * @throws TaskException if error throw TaskException
     */
    public int getTaskType() throws TaskException;

    /**
     * 取所有表，包括单位基础信息表
     * @return each iterator memeber is a Table object
     * @throws TaskException if error throw TaskException
     */
    Iterator getAllTables() throws TaskException;

    /**
     * Get table by table id.
     * @param id
     * @return Task if specfic id task existed, else null
     * @throws TaskException if error throw TaskException
     */
    Table getTableByID(String id) throws TaskException;

    /**
     * 取所有任务时间.
     * @return each iterator member is a TaskTime object
     * @throws TaskException if error throw TaskException
     */
    Iterator getTaskTimes() throws TaskException;

    /**
     * 取得用户封面表
     * @return 用户封面表
     */
    public UnitMetaTable getUnitMetaTable();

    /**
     * 得到任务现行使用的脚本组
     * @return 任务现行使用的脚本组， 没找到返回null
     * @throws TaskException 操作失败
     */
    public ScriptSuit getActiveScriptSuit() throws TaskException;

    /**
     * 取所属的任务时间
     * @param time any time
     * @return if time belong to some task time return TaskTime else null
     * @throws TaskException if error throw TaskException
     */
    TaskTime getTaskTime(Date time) throws TaskException;

    /**
     * 取得指定ID的任务时间
     * @param taskTimeID 任务时间ID
     * @return 指定ID的任务时间。没有找到，返回null
     * @throws TaskException if error throw TaskException
     */
    TaskTime getTaskTime(Integer taskTimeID) throws TaskException;

    /**
     * 得到任务所有的脚本组
     * @return 任务所有的脚本组ScriptSuit集合的Iterator
     * @throws TaskException 操作失败
     */
    public Iterator getAllScriptSuits() throws TaskException;

    /**
     * 得到指定名称的脚本组
     * @param name 脚本组名称
     * @return 指定的脚本组， 没找到返回null
     * @throws TaskException 操作失败
     */
    public ScriptSuit getScriptSuit(String name) throws TaskException;

}