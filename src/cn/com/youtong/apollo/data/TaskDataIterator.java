package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.task.*;

/**
 * ���ļ��������ȡ��λ�����ݣ�
 * ת��ΪTaskData���������
 *
 * <b>Note: �ļ��������<unit ID="UNITID">��</unit>
 * �����뵥������һ�У����ҵ���ռ�ø���</b>
 * �磺
 * <pre>
 * <!-- This is valid -->
 * <unit ID="yyy">
 * <table></table>
 * </unit>
 *
 * <!-- This is invalid -->
 * <unit ID="xxx"><table>
 * </table></unit>
 *
 * <!-- This is invalid -->
 * <unit ID="zzz">
 * <table></table>
 * </unit><unit ID="www">
 * ...</unit>
 *
 * </pre>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author wjb
 * @version 1.0
 */
public class TaskDataIterator implements Iterator
{
	private String currentFile;
	private Iterator files;

	private DataSource dataSource;
	private Task task;
	private TaskTime taskTime;
	private Iterator taskTimes;

	private boolean isTemp; // �Ƿ�����ʱ�ļ���������ϻ��߶�������ʱ����ɾ����ʱ�ļ�

	private LineNumberReader reader; // ��ǰ��ȡ��file�����
	private TaskData next;

	private boolean finished = false; /** ֻ���Ƿ���Iterator��ĩβ */

	private static Log log = LogFactory.getLog( TaskDataIterator.class );

	/**
	 * �Ӹ����ļ���ȡ���ݣ�������ȡTaskData
	 * @param dataSource           ����Դ
	 * @param task                 ������
	 * @param taskTime             ����ʱ��
	 * @param file                 �����ļ���ָ�����Ե�ַ
	 * @param isTemp               ���ļ��Ƿ�����ʱ�ļ�����ʱ�ļ���ʹ�ú󣬽�ɾ��
	 * @param log                  ��־��¼
	 * @see cn.com.youtong.apollo.data.TaskDataParser
	 */
    public TaskDataIterator( DataSource dataSource,
							 Task task, TaskTime taskTime,
							 String file, boolean isTemp )
    {
		this.dataSource = dataSource;
		this.task = task;
		this.taskTime = taskTime;

		this.currentFile = file;
		this.isTemp = isTemp;

		initReader();
    }

	/**
	 * �Ӹ�������ļ���ȡ���ݣ�������ȡTaskData��
	 * ��Щ�ļ�һ�ζ�ӦtaskTimes��˳��
	 * @param dataSource           ����Դ
	 * @param task                 ������
	 * @param taskTimes            ����ʱ��
	 * @param files                �����ļ���ָ�����Ե�ַ
	 * @param isTemp               ���ļ��Ƿ�����ʱ�ļ�����ʱ�ļ���ʹ�ú󣬽�ɾ��
	 * @param log                  ��־��¼
	 * @see cn.com.youtong.apollo.data.TaskDataParser
	 */
	public TaskDataIterator( DataSource dataSource,
							 Task task, Iterator taskTimes,
							 Iterator files, boolean isTemp )
	{
		this.dataSource = dataSource;
		this.task = task;
		this.taskTimes = taskTimes;

		this.files = files;
		this.isTemp = isTemp;

		if( this.files.hasNext() )
			currentFile = (String) this.files.next();

		if( this.taskTimes.hasNext() )
			taskTime = (TaskTime) this.taskTimes.next();

		initReader();
	}

	/**
	 * ��ʼ��reader�����û�гɹ�������readerΪnull
	 */
	private void initReader()
	{
		if( currentFile != null )
		{
			try
			{
				reader = new LineNumberReader(
								new BufferedReader(
								new InputStreamReader(new FileInputStream( currentFile ), "gb2312" )) );
			}
			catch( FileNotFoundException ex )
			{
				log.error( "�ļ�������" + currentFile );
				// eat exception and let reader be null;
			}
			catch( UnsupportedEncodingException ex )
			{
				log.error("", ex);
				// eat exception and let reader be null;
			}

		}
	}

	/**
	 * �鿴�Ƿ�����һ��
	 *
	 * @return   true or false
	 */
    public boolean hasNext()
    {
		if( next != null )
			return true;

		tryNotNullNext();

		if( next == null )
			return false;
		else
			return true;
    }

	/**
	 * ������һ��
	 * @return   TaskData
	 */
    public Object next()
    {
		if( hasNext() )
		{
		     Object result = next;
			 next = null;

			return result;
		}

		// throw exception
		throw new NoSuchElementException();
    }

	/**
	 * Ŀǰ��֧���������
	 */
    public void remove()
	{
		//throw new java.lang.UnsupportedOperationException("Method remove() not yet supported.");
    }

	private void tryNext()
	{
		if( reader == null )
			return; // ��ʾĿǰû���ļ����Ա���

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		try
		{
			out.write( "<?xml version=\"1.0\" encoding=\"gb2312\"?>".getBytes("gb2312") );
			readUnitData( out );
			byte[] buf = out.toByteArray();

			if( log.isDebugEnabled() )
			{
				log.debug( "Paser Xml to TaskData:" );
				log.debug( new String( buf ) );
				log.debug( "" );
			}

			if( buf.length == "<?xml version=\"1.0\" encoding=\"gb2312\"?>".getBytes("gb2312").length )
			{
				// ����ļ�û�пɶ�ȡ�Ķ����ˣ� �鿴�Ƿ�����һ���ļ���ȡ
				if( this.files != null && files.hasNext() )
				{
					// �ͷ���Դ
					release();

					currentFile = (String) files.next();
					taskTime = (TaskTime) taskTimes.next();

					/** @todo ����ʹ��tryNext(),�ظ������˶��ByteArrayOutputStream */

					initReader();

					tryNext();
				}
				else
				{
					// ȷʵû����
					release();
					reader = null;

					finished = true;
				}
			}
			else
			{
				in = new ByteArrayInputStream( out.
					toByteArray() );
				next = TaskDataParser.parse( dataSource, in, task, taskTime );
			}
		}
		catch( Exception ex )
		{
			log.error( "����TaskData���ִ���", ex );
		}
		finally
		{
			Util.close( in );
			Util.close( out );
		}
	}

	/**
	 * ��ΪTaskDataParser.parse�������ܷ���null(�ڴ����������,���������ݵ������).
	 * �������ǲ�ϣ����Iterator����"����" null.���������ȡ��null,������һ����null��TaskData,
	 * ֱ���ļ�ĩβ.
	 */
	private void tryNotNullNext()
	{
		tryNext();
		if( next == null && !finished )
			tryNotNullNext();
	}

	private void readUnitData( OutputStream out )
		throws IOException
	{
		boolean inUnit = false; // ָʾ�Ƿ�ʼ��ȡĳunit���ݣ���������<unit
		boolean continueRead = true; // ָʾ�Ƿ������ȡĳunit������ϣ���û��������</unit>

		while( continueRead )
		{
			String line = reader.readLine();
			if( line == null )
			{
				continueRead = false;
			}
			else
			{
				line = line.trim();
				if( inUnit )
				{
					out.write( line.getBytes("gb2312") );

					if( line.equals( "</unit>" ) )
					{
						continueRead = false;
					}
				}
				else if( line.startsWith( "<unit " ) )
				{
					inUnit = true;
					out.write( line.getBytes("gb2312") );
				}
			}
		}
	}

	protected void finalize()
	{
		release();
		if( files != null && isTemp )
		{
			while( files.hasNext() )
			{
				deleteFile( (String) files.next() );
			}
		}
	}

	private void release()
	{
		Util.close( reader );
		if( isTemp )
		{
			deleteFile( currentFile );
		}
	}

	private void deleteFile( String file )
	{
		File f = new File( file );
		f.delete();
	}
}