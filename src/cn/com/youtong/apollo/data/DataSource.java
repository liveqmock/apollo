package cn.com.youtong.apollo.data;

import java.util.Collection;
import java.util.Iterator;

import cn.com.youtong.apollo.script.Time;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * 数据源接口
 */
public interface DataSource
{
	public Task getTask();
	/**
	 * 得到指定的任务数据.
	 *
	 * 如果该单位不存在,返回null.
	 * @param unitID 单位ID
	 * @param taskTime 任务时间
	 * @return 任务数据对象
	 * @throws ModelException
	 */
	public TaskData get(String unitID, TaskTime taskTime) throws ModelException;

	/**
	 * 得到指定的任务数据。不能保证返回的Iterator顺序和unitIDs的Iterator顺序一致。
	 *
	 * 如果单位集合里面某个或者某些单位不存在,返回的结果集里面就没有相应的TaskData.
	 * @param unitIDs 单位ID集合
	 * @param taskTime 任务时间
	 * @return 任务数据对象TaskData集合的Iterator
	 * @throws ModelException
	 */
	public Iterator get(Collection unitIDs, TaskTime taskTime) throws
		ModelException;

	/**
	 * 得到指定的任务数据。
	 * 不能保证返回的Iterator顺序和unitIDs的Iterator顺序一致，
	 * 返回的Iterator排列顺序和taskTimes的Iterator顺序一致。
	 *
	 * 如果单位集合里面某个或者某些单位不存在,返回的结果集里面就没有相应的TaskData.
	 * <p>
	 * 由于DataExporter获取数据，每次只能得到一个TaskTime，所以这个方法意义不大。
	 * 代替方法是：多次调用get(Collection unitIDs, TaskTime taskTime)
	 * </p>
	 * @param unitIDs 单位ID集合
	 * @param taskTimes 任务时间TaskTime集合
	 * @return 任务数据对象TaskData集合的Iterator
	 * @throws ModelException
	 */
	public Iterator get(Collection unitIDs, Collection taskTimes) throws
		ModelException;

	/**
	 * 更新指定的任务数据.
	 * <p>
	 * 更新不一定要马上反应到数据库.
	 * 如果要求马上反应到数据库,调用{@link commit}方法.
	 * 但是update方法,要求能在一定的时候,自己隐式的调用commit方法.
	 * 比如数据库方式实现,就能在一定的次数,比如10次update方法后,向数据库提交记录.
	 * </p>
	 * @param taskData 要更新的任务数据对象
	 * @throws ModelException
	 */
	public void update(TaskData taskData) throws ModelException;

	/**
	 * 提交数据记录,使之永久性保存.
	 * @throws ModelException
	 */
	public void commit() throws ModelException;
	/**
	 * 关闭相关资源.
	 *
	 *  DataSource关闭,能自动提交修改的记录.
	 * @throws ModelException
	 */
	public void close() throws ModelException;
	/**
	 * 得到缺省的TaskData。
	 * 该TaskData里面的文字型字段使用""代替，数值型字段使用new Double( 0 )代替，
	 * 日期型使用new Date()代替。
	 * @param unitID           单位id
	 * @return                 TaskData
	 * @throws ModelException
	 */
	public TaskData getEmptyTaskData( String unitID )
		throws ModelException;
}