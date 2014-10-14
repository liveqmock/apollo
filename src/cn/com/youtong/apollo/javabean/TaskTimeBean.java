package cn.com.youtong.apollo.javabean;

// java
import java.util.Calendar;

// apollo
import cn.com.youtong.apollo.task.*;

public class TaskTimeBean
{
	/**
	 * 返回当前时间减去28天，对应的任务时间。
	 * 如果没有找到，返回第一个任务时间。
	 * 如果不存在任务时间，返回null。
	 *
	 * @param task
	 * @return
	 * @throws TaskException
	 */
	public static TaskTime suitableTaskTime( Task task )
		throws TaskException
	{
		// 计算当前时间减去28天后的日期，对应的任务时间
		Calendar cldr = Calendar.getInstance();
		cldr.add( java.util.Calendar.DATE, -28 );

		TaskTime suitableTaskTime = task.getTaskTime( cldr.getTime() );

		if( suitableTaskTime == null )
		{
			java.util.Iterator iter = task.getTaskTimes();
			if( iter.hasNext() )
			{
				suitableTaskTime = (cn.com.youtong.apollo.task.TaskTime) task.getTaskTimes().next();
			}
		}

		return suitableTaskTime;
	}
}