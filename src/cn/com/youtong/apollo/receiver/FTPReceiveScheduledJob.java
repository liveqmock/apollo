package cn.com.youtong.apollo.receiver;

import org.apache.fulcrum.schedule.turbine.*;


public class FTPReceiveScheduledJob extends ScheduledJob
{
	public FTPReceiveScheduledJob()
	{
	}

	public void run(JobEntry job)
		throws Exception
	{
		FTPReceiver f = FTPReceiver.getInstance();
		f.receive();
	}
}
