package cn.com.youtong.apollo.upload;

import java.util.List;

import org.apache.commons.logging.*;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class WebServiceUploader implements Uploader
{
	private static Log log = LogFactory.getLog( WebServiceUploader.class.getName() );
	private static String WEBSERVICE_URL_NULL = "webservice���ӵ�ַ����Ϊ��";
    public WebServiceUploader()
    {
    }

	/**
	 * �������õ��û����������webservice�����ַ���ϱ����õ���������
	 * <p>
	 * webservice�����ַ��ʽΪURI/WEBAPP������webservice����Ϊ:
	 * http://somecompany.com/myweb/services/FrontService��
	 * �����ļ�����cn.com.youtong.apollo.upload.webservice.url=http://somecompany.com/myweb���ɣ�
	 * ����services/FrontServiceʹ�üȶ���Լ����
	 * </p>
	 *
	 * <p>
	 * ���webservice�����ַ��û����http://����https://��ͷ����ô������Ϊhttp://
	 * </p>
	 */
	public void upload()
	{
		String[] taskIDs = Config.getStringArray( "cn.com.youtong.apollo.upload.task" );
		String username = Config.getString( "cn.com.youtong.apollo.upload.user" );
		String password = Config.getString( "cn.com.youtong.apollo.upload.password" );
		String url = Config.getString( "cn.com.youtong.apollo.upload.webservice.url" );

		if( Util.isEmptyString( url ) )
		{
			log.error( WEBSERVICE_URL_NULL);
			return;
		}

		// �����������û���ԡ�http://�����ߡ�https://����ͷ����ӡ�http://��
		if( !url.startsWith( "http" ) )
		{
			url = "http://" + url;
		}

		if( url.endsWith( "/" ) )
		{
			//url = url.substring( 0, url.length() -1 );
			url = url + "services/FrontService";
		}
		else
		{
			url = url + "/services/FrontService";
		}

		log.debug( "webservice.url: " + url );

		cn.com.youtong.apollo.webservice.generated.FrontService binding = null;
		try {
			cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator locator
				= new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator();
			locator.setFrontServiceAddress( url ); // ����WEBSERVICE �����ַ
			binding = locator.getFrontService();
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				log.error( "JAX-RPC ServiceException caught", jre.getLinkedCause() );
		}
		if( binding == null )
		{
			log.error( "���ܽ���webservice����" );
		}

		UploaderUtil ru = new UploaderUtil();
		// �û������������base64����
		String tusername = Util.encodeBase64( username );
		String tpassword = Util.encodeBase64( password );

		for( int i=0; i<taskIDs.length; i++)
		{
			String taskID = taskIDs[i];
			try
			{
				String data = ru.buildUploadData( taskID );
				if( Util.isEmptyString(data) )
				{
					log.info( "û�����ݿ����ϱ�" );
				}
				else
				{
					// ����webservice�ϱ�����
					binding.uploadData( data, tusername, tpassword );
				}
			} catch( java.rmi.RemoteException re )
			{
				log.error( "Remote Exception caught: ", re );
			}
			catch( Exception ex )
			{
				log.error( "", ex );
			}
		}
	}

	public void upload(Task task, TaskTime taskTime, List unitIDs,
						String username, String password )
		throws UploadException
	{
		String url = Config.getString( "cn.com.youtong.apollo.upload.webservice.url" );

		if( Util.isEmptyString( url ) )
		{
			log.error( WEBSERVICE_URL_NULL);
			return;
		}

		// �����������û���ԡ�http://�����ߡ�https://����ͷ����ӡ�http://��
		if( !url.startsWith( "http" ) )
		{
			url = "http://" + url;
		}

		if( url.endsWith( "/" ) )
		{
			//url = url.substring( 0, url.length() -1 );
			url = url + "services/FrontService";
		}
		else
		{
			url = url + "/services/FrontService";
		}

		log.debug( "webservice.url: " + url );

		cn.com.youtong.apollo.webservice.generated.FrontService binding = null;
		try {
			cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator locator
				= new cn.com.youtong.apollo.webservice.generated.FrontServiceServiceLocator();
			locator.setFrontServiceAddress( url ); // ����WEBSERVICE �����ַ
			binding = locator.getFrontService();
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				log.error( "JAX-RPC ServiceException caught", jre.getLinkedCause() );
		}
		if( binding == null )
		{
			String msg = "���ܽ���webservice����";
			log.error( msg );

			throw new UploadException( msg );
		}

		UploaderUtil ru = new UploaderUtil();
		// �û������������base64����
		String tusername = Util.encodeBase64( username );
		String tpassword = Util.encodeBase64( password );


		try
		{
			for( int i = 0, size = unitIDs.size(); i < size; i++ )
			{
				String unitID = ( String ) unitIDs.get( i );
				String data = ru.buildUploadData( task, taskTime, unitID,
												  username, password );
				if( Util.isEmptyString( data ) )
				{
					log.info( "û�����ݿ����ϱ�" );
				} else
				{
					// ����webservice�ϱ�����
					binding.uploadData( data, tusername, tpassword );
				}
			}
		} catch( java.rmi.RemoteException re )
		{
			log.error( "Remote Exception caught: ", re );
			throw new UploadException( "WebService�ϱ�����", re );
		} catch( Exception ex )
		{
			log.error( "", ex );
			throw new UploadException( "WebService�ϱ�����", ex );
		}
	}

}