package cn.com.youtong.apollo.receiver;

import org.apache.fulcrum.schedule.turbine.*;


public class MailReceiveScheduledJob extends ScheduledJob
{
	public MailReceiveScheduledJob()
	{
	}

	public void run(JobEntry job)
		throws Exception
	{
		MailReceiver m = MailReceiver.getInstance();
		m.receive();
	}
}