package cn.com.youtong.apollo.data.db;

// java
import java.io.*;
import java.util.*;
// apache
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
// apollo
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.usermanager.Role;
import cn.com.youtong.apollo.usermanager.SetOfPrivileges;
import cn.com.youtong.apollo.usermanager.User;

/**
 * DataSource的数据库实现
 */
class DBDataSource
	implements DataSource
{
	/** DBUnitTreeManager实例 */
	private DBUnitTreeManager treeMng;

	/** log对象 */
	private static Log log = LogFactory.getLog( DBDataSource.class );

	/** 报表任务定义 */
	private Task task;

	/** 数据导出器 */
	private DBDataExporter exporter;

	private DBDataStorer storer;
	/** 当前已经更新了多少个单位数据 */
	private int updateUnits;
	private String NEW_LINE = "\n";

	public DBDataSource( Task task, DBUnitTreeManager treeManager)
	{
		this.task = task;
		this.treeMng = treeManager;
		this.exporter = new DBDataExporter( task, treeMng);
		// storer 等到调用update的时候,才初始化
	}

	public Task getTask()
	{
		return task;
	}


	/**
	 * 得到指定的任务数据.
	 *
	 * <p>
	 * 如果单位不存在,返回Null.
	 * </p>
	 * @param unitID 单位ID
	 * @param taskTime 任务时间
	 * @return 任务数据对象
	 * @throws ModelException
	 *
	 * @seee {@link DataSource#get( String , TaskTime )
	 */
	public TaskData get( String unitID, TaskTime taskTime )
		throws ModelException
	{
		StringWriter writer = new StringWriter();

		List list = new ArrayList( 1 );
		list.add( unitID );

		exporter.exportData( task, taskTime, list.iterator(), writer,DBDataExporter.EDITDATA);

		ByteArrayInputStream in = null;
		try
		{
			in = new ByteArrayInputStream(writer.toString().getBytes("gb2312"));
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new ModelException(ex);
		}

		TaskData td = TaskDataParser.parse( this, in, task, taskTime );

		Util.close( writer );
		Util.close( in );

		return td;
	}

	/**
	 * 得到缺省的TaskData。
	 * 该TaskData里面的文字型字段使用""代替，数值型字段使用new Double( 0 )代替，
	 * 日期型使用new Date()代替。
	 * @param unitID           单位id
	 * @return                 TaskData
	 * @throws ModelException
	 */
	public TaskData getEmptyTaskData( String unitID )
		throws ModelException
	{
		// key/value = tableID/TableData
		Map tables = new HashMap();
		Iterator tableIter = null;
		try
		{
			task.getAllTables();
		}
		catch( TaskException ex )
		{
			// 事实上程序无法到这里
			throw new ModelException( "读取任务定义出错", ex );
		}

		while( tableIter.hasNext() )
		{
			Table table = (Table) tableIter.next();
			Map field2valueMap = new HashMap();

			for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
			{
				Row row = (Row) rowIter.next();
				if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					continue; // 浮动行

				for( Iterator cellIter = row.getCells(); cellIter.hasNext(); )
				{
					Cell cell = (Cell) cellIter.next();

					int dataType = cell.getDataType();
					String field = cell.getDBFieldName().toUpperCase();
					if( dataType == Cell.TYPE_NUMERIC )
						field2valueMap.put( field, new Double( 0 ) );
					else if( dataType == Cell.TYPE_DATE )
						field2valueMap.put( field, new Date() );
					else
						field2valueMap.put( field, "" );
				}
			}

			// 如果field2valueMap的大小为0，表示该表包含的全部是浮动行
			if( field2valueMap.size() != 0 )
				tables.put( table.id(), new TableData( field2valueMap ) );

		}

		TaskData taskData = new TaskData( this, task, unitID, null, tables );
		return taskData;
	}

	/**
	 * 得到指定的任务数据
	 * @param unitIDs 单位ID集合
	 * @param taskTime 任务时间
	 * @return 任务数据对象TaskData集合的Iterator
	 * @throws ModelException
	 */
	public Iterator get( Collection unitIDs, TaskTime taskTime )
		throws ModelException
	{
		String file = outputTaskData2File( unitIDs, taskTime );

		return new TaskDataIterator( this,
									 task, taskTime,
									 file, true );
	}

	/**
	 * 将单位集合unitIDs在任务时间TaskTime里面的数据，
	 * 导出到文件。
	 * @param unitIDs          单位集合，元素类型java.lang.String
	 * @param taskTime         任务时间
	 * @return                 导出的文件名
	 * @throws ModelException
	 */
	private String outputTaskData2File( Collection unitIDs, TaskTime taskTime )
		throws ModelException
	{
		String file = tempFileName();

		Writer writer = null;
		try
		{
			writer = new OutputStreamWriter(new FileOutputStream( file ), "gb2312");

			exporter.exportData( task, taskTime, unitIDs.iterator(), writer,DBDataExporter.EDITDATA);
		} catch( IOException ex )
		{
			throw new ModelException( "IO错误", ex );
		} finally
		{
			Util.close( writer );
		}
		return file;
	}

	/**
	 * 得到指定的任务数据
	 * @param unitIDs 单位ID集合
	 * @param taskTimes 任务时间TaskTime集合
	 * @return 任务数据对象TaskData集合的Iterator
	 * @throws ModelException
	 */
	public Iterator get( Collection unitIDs, Collection taskTimes )
		throws ModelException
	{
		// 每个taskTime写一个文件
		// 对应的Iterator，依次遍历

		List files = new ArrayList( taskTimes.size() );
		for( Iterator iter = taskTimes.iterator(); iter.hasNext(); )
		{
			TaskTime taskTime = ( TaskTime ) iter.next();

			String file = outputTaskData2File( unitIDs, taskTime );
		}

		return new TaskDataIterator( this,
									 task, taskTimes.iterator(),
									 files.iterator(), true );
	}

	/**
	 * 更新指定的任务数据,此更新方法没有立即更新到数据库.
	 * 如果要立即更新到数据库调用{@link commit}方法.
	 * 否则,在调用了Config.getInt( "cn.com.youtong.apollo.loaddata.units.percommit", 1)
	 * 次数后,会进行一次数据库提交.
	 *
	 * <p><b>
	 * Note: 调用了更新后,至少要显示的调用一次{@link commit}方法,确保所有的数据提交到数据库.
	 * </b><p>
	 * @param taskData 要更新的任务数据对象
	 * @throws ModelException
	 */
	public void update( TaskData taskData )
		throws ModelException
	{
		if( storer == null )
		{
			synchronized( this )
			{
				storer = new DBDataStorer( task, taskData.getTaskTime() );
				Map map = new HashMap();
				map.put( treeMng.getClass().getName(), treeMng );
				storer.init( map );
			}
		}

		// 查看是否要切换任务时间
		if( !storer.getTaskTime().getTaskTimeID().equals(
				  taskData.getTaskTime() ) )
			storer.setTaskTime( taskData.getTaskTime() );

		StringWriter writer = new StringWriter();

		try
		{
			marshal( taskData, writer );
			ByteArrayInputStream in = new ByteArrayInputStream( writer.toString().getBytes("gb2312"));

			storer.store( in );

			updateUnits++;
		} catch( IOException ex )
		{
			log.error( "IO错误", ex );
			throw new ModelException( "IO错误", ex );
		}

		if( updateUnits ==
			Config.getInt( "cn.com.youtong.apollo.loaddata.units.percommit", 1) )
		{
			commit();
		}
	}

	/**
	 * update方法,修改了数据内容.
	 * 但没有立即更新到数据库.
	 *
	 * commit方法,将update的修改内容提交到数据库.
	 * @throws ModelException
	 */
	public void commit()
		throws ModelException
	{
		updateUnits = 0; // 重置导入的单位
		if( storer != null )
		{
			storer.commit(); // 可能没调用update方法,所有storer还没有初始化
			treeMng.update();
		}
	}

	/**
	 * update方法,开启了一些数据库资源, 使用close方法可以关闭update开启的资源.
	 * @throws ModelException
	 */
	public synchronized void close()
		throws ModelException
	{
		commit();
		if( storer != null )
			storer.release(); // 可能没调用update方法,所有storer还没有初始化
		storer = null; // 以便下次调用update的时候能初始化
	}

	/**
	 * 将taskData marshal到输出流out中
	 * @param taskData         任务数据
	 * @param out              输出流
	 * @throws IOException
	 */
	private void marshal( TaskData taskData, Writer writer )
		throws IOException
	{
		exporter.writeHead( writer, taskData.getTask().id(),
							taskData.getTaskTime().getFromTime() );

		String unitID = taskData.getUnitID();
		writer.write( "<unit ID=\"");
		writer.write( unitID);
		writer.write( "\">");
		writer.write( NEW_LINE );

		for( Iterator iter = taskData.getTables().entrySet().iterator();
							 iter.hasNext(); )
		{
			Map.Entry entry = ( Map.Entry ) iter.next();
			String tableID = ( String ) entry.getKey();
			Map field2dataMap = ( ( TableData ) entry.getValue() ).
								getDbfield2valueMap();

			writer.write( "<table ID=\"");
			writer.write( tableID);
			writer.write( "\">");
			writer.write( NEW_LINE );

			for( Iterator values = field2dataMap.entrySet().iterator();
								   values.hasNext(); )
			{
				Map.Entry temp = ( Map.Entry ) values.next();
				String field = ( String ) temp.getKey();
				String value = temp.getValue().toString();

				writer.write( "<cell field=\"");
				writer.write( field);
				writer.write( "\" value=\"");
				writer.write( value);
				writer.write( "\"/>");
				writer.write( NEW_LINE );
			}

			writer.write( "</table>");
			writer.write( NEW_LINE );
		}

		writer.write( "</unit>");
		writer.write( NEW_LINE );
		exporter.writeEnd( writer, "" );
	}

	private String tempFileName()
	{
		String tempDir = Config.getString(
			"cn.com.youtong.apollo.data.export.tempdir" );

		long currTimeInMs = System.currentTimeMillis();

		return tempDir + File.separator + currTimeInMs + "_" +
			Util.generateRandom();
	}
}