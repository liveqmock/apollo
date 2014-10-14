package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.task.*;

/**
 * 从文件中逐个读取单位的数据，
 * 转换为TaskData对象输出。
 *
 * <b>Note: 文件必须符合<unit ID="UNITID">，</unit>
 * 都必须单独另起一行，而且单独占用该行</b>
 * 如：
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

	private boolean isTemp; // 是否是临时文件。遍历完毕或者对象销毁时，会删除临时文件

	private LineNumberReader reader; // 当前读取的file输出流
	private TaskData next;

	private boolean finished = false; /** 只是是否到了Iterator的末尾 */

	private static Log log = LogFactory.getLog( TaskDataIterator.class );

	/**
	 * 从给定文件读取数据，遍历读取TaskData
	 * @param dataSource           数据源
	 * @param task                 任务定义
	 * @param taskTime             任务时间
	 * @param file                 数据文件，指定绝对地址
	 * @param isTemp               该文件是否是临时文件，临时文件，使用后，将删除
	 * @param log                  日志记录
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
	 * 从给定多个文件读取数据，遍历读取TaskData。
	 * 这些文件一次对应taskTimes的顺序。
	 * @param dataSource           数据源
	 * @param task                 任务定义
	 * @param taskTimes            任务时间
	 * @param files                数据文件，指定绝对地址
	 * @param isTemp               该文件是否是临时文件，临时文件，使用后，将删除
	 * @param log                  日志记录
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
	 * 初始化reader。如果没有成功，保持reader为null
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
				log.error( "文件不存在" + currentFile );
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
	 * 查看是否还有下一个
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
	 * 返回下一个
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
	 * 目前不支持这项操作
	 */
    public void remove()
	{
		//throw new java.lang.UnsupportedOperationException("Method remove() not yet supported.");
    }

	private void tryNext()
	{
		if( reader == null )
			return; // 表示目前没有文件可以遍历

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
				// 这个文件没有可读取的东西了， 查看是否有下一个文件读取
				if( this.files != null && files.hasNext() )
				{
					// 释放资源
					release();

					currentFile = (String) files.next();
					taskTime = (TaskTime) taskTimes.next();

					/** @todo 这里使用tryNext(),重复构造了多个ByteArrayOutputStream */

					initReader();

					tryNext();
				}
				else
				{
					// 确实没有了
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
			log.error( "遍历TaskData出现错误", ex );
		}
		finally
		{
			Util.close( in );
			Util.close( out );
		}
	}

	/**
	 * 因为TaskDataParser.parse方法可能返回null(在传入的数据流,不存在数据的情况下).
	 * 但是我们不希望在Iterator里面"流出" null.所以如果读取到null,尝试下一个非null的TaskData,
	 * 直到文件末尾.
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
		boolean inUnit = false; // 指示是否开始读取某unit数据，即遇到了<unit
		boolean continueRead = true; // 指示是否继续读取某unit数据完毕，即没有遇到了</unit>

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