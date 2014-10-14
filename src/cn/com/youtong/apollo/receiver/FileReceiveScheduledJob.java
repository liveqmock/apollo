package cn.com.youtong.apollo.receiver;

import org.apache.fulcrum.schedule.turbine.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FileReceiveScheduledJob extends ScheduledJob
{
	public FileReceiveScheduledJob()
	{
	}

	public void run(JobEntry job)
		throws Exception
	{
		FileReceiver f = FileReceiver.getInstance();
		f.receive();
	}
}