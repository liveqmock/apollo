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
 * ����ftp��ʽ�ϱ������ļ����û���Ϣ�ļ���ѹ���ļ���
 *
 * <p>
 * ʹ����apache commons�����net����
 * �ϴ���һ���ļ�������ɾ��������ʱ�ļ���
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
		   "ftp����������¼�û��������벻��Ϊ��";

    public FtpUploader()
    {
    }

	/**
	 * ���������ļ�������ftp��ַ����¼�û��������룬�ϱ������ļ�
	 *
	 * <p>
	 * �ϱ�ftp��ַ��ftp://����дҲ���Բ�д��
	 * �磺ftp://myftp.company.com ������дΪ myftp.company.com
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

		// ���ftp server���õ�ʱ��ǰ�������ftp://ǰ׺
		if( server.startsWith( "ftp://" ) )
			server = server.substring( 6 );

		FTPClient ftp = new FTPClient();


		if( login( ftp, server, ftpUsername, ftpPassword ) != null )
		{
			// û�е�¼�ɹ������߾ܾ������
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
			// û�е�¼�ɹ������߾ܾ������
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
			msg = "FTP�ϱ�ʱ����";
			log.error( msg, ex );

			throw new UploadException( msg, ex );
		} finally
		{
			if( file != null )
				file.delete();
		}

		logout( ftp );
	}

	// ����д��󣬷��ش�����Ϣ���ɹ���¼����null
	private String login( FTPClient ftp, String server,
						 String ftpUsername, String ftpPassword )
	{
		// ���ftp server���õ�ʱ��ǰ�������fpt://ǰ׺
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
				String msg = "FTP �������ܾ�����.";
				log.info( msg );
				return msg;
			}

			if( !ftp.login( ftpUsername, ftpPassword ) )
			{
				//ftp.logout();
				String msg = "�û��������뱻�������ܾ���¼";
				log.error( msg );
				return msg;
			}
		}
		catch( Exception ex )
		{
			return ex.getMessage() == null ? "���ܵ�¼FTP������": ex.getMessage();
		}

		return null;
	}

	private String checkConnected( FTPClient ftp, String server,
								 String ftpUsername, String ftpPassword )
	{
		try
		{
			// �鿴�Ƿ�������Ͽ�
			ftp.getReply();
		} catch( IOException ioe )
		{
			// ��������
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