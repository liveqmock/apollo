package cn.com.youtong.apollo.notify;

import org.apache.fulcrum.schedule.turbine.*;
import org.apache.commons.logging.*;
import cn.com.youtong.apollo.services.*;

public class NotifyScheduledJob extends ScheduledJob
{
	private Log log = LogFactory.getLog(this.getClass());
	/** ����߱������� */
	public final static String NOTIFY_TASK = "cn.com.youtong.apollo.notify.NotifyScheduledJob";
	/** �߱������ID */
	public final static int NOTIFY_JOB_ID = 11;


    public NotifyScheduledJob()
    {
    }

    public void run(JobEntry job) throws Exception
    {
		NotifyManager.instance().notifyTasks(job);
	}
}