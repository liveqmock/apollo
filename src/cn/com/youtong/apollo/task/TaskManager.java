package cn.com.youtong.apollo.task;

import java.io.*;
import java.util.*;

/**
 * Task manager
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 北京世纪友通科技有限公司</p>
 * @author zhou
 * @version 1.0
 */
public interface TaskManager
{
    /**
     * Get all tasks
     * @return each element is a Task object
     * @throws TaskException throws exception if failed
     * @see Task
     */
    Iterator getAllTasks()
            throws TaskException;

    /**
     * Get task by task ID
     * @param id task id
     * @return Task if specfic task existed, else null
     * @throws TaskException throws exception if failed
     */
    Task getTaskByID(String id)
            throws TaskException;

    /**
     * Delete task
     * @param task task to be deleted
     * @throws TaskException throws exception if failed to delete exception
     */
    void deleteTask(Task task)
            throws TaskException;

    /**
     * 发布任务
     * @param xmlInputStream 要发布的任务参数的xml流
     * @return 成功发布的任务
     * @throws TaskException 发布失败时抛出
     */
    Task publishTask(InputStream xmlInputStream)
            throws TaskException;
    /**
     * 创建任务时间
     * @param taskID 任务ID
     * @param year 任务时间
     * @return 已经创建人任务时间
     * @throws TaskException 发布脚本组失败
     */
    public List<TaskTime> publishTaskTime(String taskID,int year)throws
    TaskException;
    /**
     * 发布脚本组，如果存在，覆盖
     * @param taskID 任务ID
     * @param xmlInputStream 脚本组xml流
     * @return 成功发布的脚本组
     * @throws TaskException 发布脚本组失败
     */
    public ScriptSuit publishScriptSuit(String taskID,
                                        InputStream xmlInputStream)
            throws
            TaskException;

    /**
     * 发布XSLT文件，如果存在，覆盖
     * @param xmlInputStream xslt流
     * @return 成功发布的脚本组
     * @throws TaskException 发布脚本组失败
     */
    public Task publishXSLT(InputStream xmlInputStream)
            throws TaskException;


    /**
     * 删除脚本组
     * @param taskID 脚本组所属的任务ID
     * @param suitName 脚本组名称ID
     * @throws TaskException 删除脚本组失败
     */
    public void deleteScriptSuit(String taskID, String suitName)
            throws
            TaskException;

    /**
     * 删除XSLT
     * @param taskID 任务ID号
     * @param viewID viewID
     * @throws TaskException 删除失败
     */
    public void deleteXSLT(String taskID,Integer viewID)
            throws TaskException;


    /**
     * 查找任务的所有的XSLT文件
     * 若没找到则返回　size 为0　的Collection
     * @param taskID 任务ID号
     * @return 返回XSLT文件的集合(TaskView类型)
     * @throws TaskException
     */
//    public Iterator getAllViews(String taskID)
//            throws TaskException;

    /**
     * 查找任务的XSLT文件
     * @param taskID 任务ID号
     * @param tableID 表ID号
     * @return TaskView对象
     * @throws TaskException
     */
//    public TableView getView(String taskID, String tableID)
//            throws TaskException;


    /**
     * 设置任务现行使用的脚本组
     * @param taskID 脚本组所属的任务ID
     * @param suitName 脚本组名称ID
     * @throws TaskException 操作失败
     */
    public void setActiveScriptSuit(String taskID, String suitName)
            throws TaskException;

    /**
     * 导出脚本组
     * @param taskID 脚本组所属的任务ID
     * @param suitName 脚本组名称ID
     * @param out 输出的流
     * @throws TaskException 操作失败
     */
    public void outputScriptSuit(String taskID, String suitName,
                                 OutputStream out)
            throws TaskException;

}