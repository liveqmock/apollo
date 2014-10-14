package cn.com.youtong.apollo.javabean;

// java
import java.util.Calendar;

// apollo
import cn.com.youtong.apollo.task.*;

public class TaskTimeBean
{
	/**
	 * ���ص�ǰʱ���ȥ28�죬��Ӧ������ʱ�䡣
	 * ���û���ҵ������ص�һ������ʱ�䡣
	 * �������������ʱ�䣬����null��
	 *
	 * @param task
	 * @return
	 * @throws TaskException
	 */
	public static TaskTime suitableTaskTime( Task task )
		throws TaskException
	{
		// ���㵱ǰʱ���ȥ28�������ڣ���Ӧ������ʱ��
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