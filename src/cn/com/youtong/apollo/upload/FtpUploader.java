package cn.com.youtong.apollo.upload;

// java
import java.io.*;
import java.util.List;

// apache
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
// apollo
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.Task;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * 采用ftp方式上报数据文件和用户信息文件的压缩文件。
 *
 * <p>
 * 使用了apache commons里面的net包。
 * 上传了一个文件后，立即删除本地临时文件。
 * </p>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class FtpUploader implements Uploader
{
	private static Log log = LogFactory.getLog( FtpUploader.class.getName() );
	private String HOST_USER_PASSWORD_NULL =
		   "ftp服务器，登录用户名和密码不能为空";

    public FtpUploader()
    {
    }

	/**
	 * 根据配置文件的任务，ftp地址，登录用户名和密码，上报数据文件
	 *
	 * <p>
	 * 上报ftp地址，ftp://可以写也可以不写。
	 * 如：ftp://myftp.company.com 可以书写为 myftp.company.com
	 * </p>
	 */
	public void upload()
	{
		String[] taskIDs = Config.getStringArray(
			"cn.com.youtong.apollo.upload.task" );
		String server = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.host" );
		String ftpUsername = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.user" );
		String ftpPassword = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.password" );

		if( Util.isEmptyString( server )
			|| Util.isEmptyString( ftpUsername )
			|| Util.isEmptyString( ftpPassword ) )
		{
			log.error( HOST_USER_PASSWORD_NULL );
			return;
		}

		// 如果ftp server配置的时候前面添加了ftp://前缀
		if( server.startsWith( "ftp://" ) )
			server = server.substring( 6 );

		FTPClient ftp = new FTPClient();


		if( login( ftp, server, ftpUsername, ftpPassword ) != null )
		{
			// 没有登录成功，或者拒绝服务等
			logout( ftp );
			return;
		}

		File file = null;

		UploaderUtil ru = new UploaderUtil();
		for( int i = 0; i < taskIDs.length; i++ )
		{
			try
			{
				file = ru.buildUploadFile( taskIDs[i] );

				if( checkConnected( ftp, server, ftpUsername, ftpPassword ) != null )
				{
					logout( ftp );
					return;
				}

				uploadFile( ftp, file );
			} catch( Exception ex )
			{
				log.error( "", ex );
			} finally
			{
				if( file != null )
					file.delete();
			}
		}

		logout( ftp );
	}

	public void upload( Task task, TaskTime taskTime, List unitIDs,
						String username, String password )
		throws UploadException
	{

		String server = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.host" );
		String ftpUsername = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.user" );
		String ftpPassword = Config.getString(
			"cn.com.youtong.apollo.upload.ftp.password" );

		if( Util.isEmptyString( server )
			|| Util.isEmptyString( ftpUsername )
			|| Util.isEmptyString( ftpPassword ) )
		{
			log.error( HOST_USER_PASSWORD_NULL );
			throw new UploadException( HOST_USER_PASSWORD_NULL );
		}


		FTPClient ftp = new FTPClient();

		String msg = login( ftp, server, ftpUsername, ftpPassword );
		if( msg != null )
		{
			// 没有登录成功，或者拒绝服务等
			logout( ftp );

			throw new UploadException( msg );
		}

		File file = null;

		UploaderUtil ru = new UploaderUtil();
		try
		{
			file = ru.buildUploadFile( task, taskTime, unitIDs, username, password );

			msg = checkConnected( ftp, server, ftpUsername, ftpPassword );
			if( msg != null )
			{
				logout( ftp );

				throw new UploadException( msg );
			}

			uploadFile( ftp, file );
		} catch( Exception ex )
		{
			msg = "FTP上报时出错";
			log.error( msg, ex );

			throw new UploadException( msg, ex );
		} finally
		{
			if( file != null )
				file.delete();
		}

		logout( ftp );
	}

	// 如果有错误，返回错误信息；成功登录返回null
	private String login( FTPClient ftp, String server,
						 String ftpUsername, String ftpPassword )
	{
		// 如果ftp server配置的时候前面添加了fpt://前缀
		if( server.startsWith( "ftp://" ) )
			server = server.substring( 6 );

		try
		{
			int reply;
			ftp.connect( server );

			// After connection attempt, check the reply code to verify
			// success.
			reply = ftp.getReplyCode();
			log.debug( "FTP server reply: " + reply );
			if( !FTPReply.isPositiveCompletion( reply ) )
			{
				//ftp.disconnect();
				String msg = "FTP 服务器拒绝连接.";
				log.info( msg );
				return msg;
			}

			if( !ftp.login( ftpUsername, ftpPassword ) )
			{
				//ftp.logout();
				String msg = "用户名和密码被服务器拒绝登录";
				log.error( msg );
				return msg;
			}
		}
		catch( Exception ex )
		{
			return ex.getMessage() == null ? "不能登录FTP服务器": ex.getMessage();
		}

		return null;
	}

	private String checkConnected( FTPClient ftp, String server,
								 String ftpUsername, String ftpPassword )
	{
		try
		{
			// 查看是否服务器断开
			ftp.getReply();
		} catch( IOException ioe )
		{
			// 重新连接
			return login( ftp, server, ftpUsername, ftpPassword );
		}

		return null;
	}

	private void uploadFile( FTPClient ftp, File file )
		throws IOException
	{
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		// Use passive mode as default because most of us are
		// behind firewalls these days.
		ftp.enterLocalPassiveMode();

		InputStream input = null;

		try
		{
			input = new FileInputStream( file );

			ftp.storeFile( file.getName(), input );
		}
		catch( IOException ex )
		{
			log.error( "", ex );
		}
		finally
		{
			Util.close( input );
		}

	}
	private void logout( FTPClient ftp )
	{
		if( ftp != null )
		{
			try
			{
				ftp.logout();
			} catch( Exception ex )
			{
			}

			if( ftp.isConnected() )
			{
				try
				{
					ftp.disconnect();
				} catch( IOException f )
				{
					// do nothing
				}
			}
		}
	}

}