package cn.com.youtong.apollo.license;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import cn.com.youtong.apollo.common.license.*;
import cn.com.youtong.apollo.services.*;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.commons.logging.*;
import org.apache.fulcrum.schedule.turbine.JobEntry;
import org.apache.fulcrum.schedule.turbine.ScheduledJob;

public class LicenseScheduledJob extends ScheduledJob
{
	private Log log = LogFactory.getLog(LicenseScheduledJob.class.getName());
	public static final String ROLE = LicenseScheduledJob.class.getName();

    public LicenseScheduledJob()
    {
    }

	public void run(JobEntry job)
		throws Exception
	{
		doRun();
	}

	private void doRun()
	{
		try
		{
			if( !ApolloLicense.isValid() )
			{
				System.out.println( "license��Ч�����" );
				log.error( "license��Ч�����" );
				ApolloService.setManager( (ServiceManager) null );
			}
		}
		catch (LicenseException ex)
		{
			//ex.printStackTrace();
			log.error( "license���ִ���" );
			ApolloService.setManager( (ServiceManager) null );
		}
	}
}