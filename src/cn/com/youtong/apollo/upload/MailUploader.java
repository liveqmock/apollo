package cn.com.youtong.apollo.upload;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.mail.*;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * 使用UploaderUtil产生上报数据文件和用户信息文件的压缩文件，然后作为邮件
 * 附件上传。
 * <p>
 * Note: Mail上报，采用了此线程技术，目前没有实现删除临时Zip文件。
 * </p>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MailUploader implements Uploader
{
	private static Log log = LogFactory.getLog( MailUploader.class.getName() );

	private String MAIL_FROM_TO_NULL = "发件人和收件人不能为空";
    public MailUploader()
    {
    }

	public void upload()
	{
		String[] taskIDs = Config.getStringArray( "cn.com.youtong.apollo.upload.task" );
		String postfix = Config.getString( "cn.com.youtong.apollo.upload.mail.postfix" );
		String subject = Config.getString( "cn.com.youtong.apollo.upload.mail.subject", "subject:" ) + postfix;

		String from = Config.getString( "mail.post.address" );
		String to = Config.getString( "cn.com.youtong.apollo.upload.mail.to" );

		if( Util.isEmptyString( from )
			|| Util.isEmptyString( to ) )
		{
			log.error( MAIL_FROM_TO_NULL );
		}
		UploaderUtil ru = new UploaderUtil();
		for( int i=0; i<taskIDs.length; i++ )
		{
			try
			{
				File file = ru.buildUploadFile( taskIDs[i] );
				Mail.send( from, to, null, null, subject, "",
						   file.getAbsolutePath() );
			}
			catch( Exception ex )
			{
				String msg = "邮件上报出错";
				log.error( msg, ex );
			}
		}
	}

	public void upload( Task task, TaskTime taskTime, List unitIDs,
						String username, String password )
		throws UploadException
	{
		String postfix = Config.getString( "cn.com.youtong.apollo.upload.mail.postfix" );
		String subject = Config.getString( "cn.com.youtong.apollo.upload.mail.subject", "subject:" ) + postfix;

		String from = Config.getString( "mail.post.address" );
		String to = Config.getString( "cn.com.youtong.apollo.upload.mail.to" );

		if( Util.isEmptyString( from )
			|| Util.isEmptyString( to ) )
		{
			log.error( MAIL_FROM_TO_NULL );
		}
		UploaderUtil ru = new UploaderUtil();
		try
		{
			File file = ru.buildUploadFile( task, taskTime, unitIDs, username,
											password );
			Mail.send( from, to, null, null, subject, "",
					   file.getAbsolutePath() );
		} catch( Exception ex )
		{
			String msg = "邮件上报出错";
			log.error( msg, ex );
			throw new UploadException( msg, ex );
		}
	}
}