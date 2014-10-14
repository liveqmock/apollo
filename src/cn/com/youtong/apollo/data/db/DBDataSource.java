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
 * DataSource�����ݿ�ʵ��
 */
class DBDataSource
	implements DataSource
{
	/** DBUnitTreeManagerʵ�� */
	private DBUnitTreeManager treeMng;

	/** log���� */
	private static Log log = LogFactory.getLog( DBDataSource.class );

	/** ���������� */
	private Task task;

	/** ���ݵ����� */
	private DBDataExporter exporter;

	private DBDataStorer storer;
	/** ��ǰ�Ѿ������˶��ٸ���λ���� */
	private int updateUnits;
	private String NEW_LINE = "\n";

	public DBDataSource( Task task, DBUnitTreeManager treeManager)
	{
		this.task = task;
		this.treeMng = treeManager;
		this.exporter = new DBDataExporter( task, treeMng);
		// storer �ȵ�����update��ʱ��,�ų�ʼ��
	}

	public Task getTask()
	{
		return task;
	}


	/**
	 * �õ�ָ������������.
	 *
	 * <p>
	 * �����λ������,����Null.
	 * </p>
	 * @param unitID ��λID
	 * @param taskTime ����ʱ��
	 * @return �������ݶ���
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
	 * �õ�ȱʡ��TaskData��
	 * ��TaskData������������ֶ�ʹ��""���棬��ֵ���ֶ�ʹ��new Double( 0 )���棬
	 * ������ʹ��new Date()���档
	 * @param unitID           ��λid
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
			// ��ʵ�ϳ����޷�������
			throw new ModelException( "��ȡ���������", ex );
		}

		while( tableIter.hasNext() )
		{
			Table table = (Table) tableIter.next();
			Map field2valueMap = new HashMap();

			for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
			{
				Row row = (Row) rowIter.next();
				if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					continue; // ������

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

			// ���field2valueMap�Ĵ�СΪ0����ʾ�ñ������ȫ���Ǹ�����
			if( field2valueMap.size() != 0 )
				tables.put( table.id(), new TableData( field2valueMap ) );

		}

		TaskData taskData = new TaskData( this, task, unitID, null, tables );
		return taskData;
	}

	/**
	 * �õ�ָ������������
	 * @param unitIDs ��λID����
	 * @param taskTime ����ʱ��
	 * @return �������ݶ���TaskData���ϵ�Iterator
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
	 * ����λ����unitIDs������ʱ��TaskTime��������ݣ�
	 * �������ļ���
	 * @param unitIDs          ��λ���ϣ�Ԫ������java.lang.String
	 * @param taskTime         ����ʱ��
	 * @return                 �������ļ���
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
			throw new ModelException( "IO����", ex );
		} finally
		{
			Util.close( writer );
		}
		return file;
	}

	/**
	 * �õ�ָ������������
	 * @param unitIDs ��λID����
	 * @param taskTimes ����ʱ��TaskTime����
	 * @return �������ݶ���TaskData���ϵ�Iterator
	 * @throws ModelException
	 */
	public Iterator get( Collection unitIDs, Collection taskTimes )
		throws ModelException
	{
		// ÿ��taskTimeдһ���ļ�
		// ��Ӧ��Iterator�����α���

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
	 * ����ָ������������,�˸��·���û���������µ����ݿ�.
	 * ���Ҫ�������µ����ݿ����{@link commit}����.
	 * ����,�ڵ�����Config.getInt( "cn.com.youtong.apollo.loaddata.units.percommit", 1)
	 * ������,�����һ�����ݿ��ύ.
	 *
	 * <p><b>
	 * Note: �����˸��º�,����Ҫ��ʾ�ĵ���һ��{@link commit}����,ȷ�����е������ύ�����ݿ�.
	 * </b><p>
	 * @param taskData Ҫ���µ��������ݶ���
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

		// �鿴�Ƿ�Ҫ�л�����ʱ��
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
			log.error( "IO����", ex );
			throw new ModelException( "IO����", ex );
		}

		if( updateUnits ==
			Config.getInt( "cn.com.youtong.apollo.loaddata.units.percommit", 1) )
		{
			commit();
		}
	}

	/**
	 * update����,�޸�����������.
	 * ��û���������µ����ݿ�.
	 *
	 * commit����,��update���޸������ύ�����ݿ�.
	 * @throws ModelException
	 */
	public void commit()
		throws ModelException
	{
		updateUnits = 0; // ���õ���ĵ�λ
		if( storer != null )
		{
			storer.commit(); // ����û����update����,����storer��û�г�ʼ��
			treeMng.update();
		}
	}

	/**
	 * update����,������һЩ���ݿ���Դ, ʹ��close�������Թر�update��������Դ.
	 * @throws ModelException
	 */
	public synchronized void close()
		throws ModelException
	{
		commit();
		if( storer != null )
			storer.release(); // ����û����update����,����storer��û�г�ʼ��
		storer = null; // �Ա��´ε���update��ʱ���ܳ�ʼ��
	}

	/**
	 * ��taskData marshal�������out��
	 * @param taskData         ��������
	 * @param out              �����
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