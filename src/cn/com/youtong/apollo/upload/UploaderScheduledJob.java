package cn.com.youtong.apollo.upload;

import org.apache.fulcrum.schedule.turbine.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UploaderScheduledJob
	extends ScheduledJob
{
	public UploaderScheduledJob()
	{
	}

	public void run( JobEntry job )
		throws java.lang.Exception
	{
		new UploaderManager().upload();
	}

}