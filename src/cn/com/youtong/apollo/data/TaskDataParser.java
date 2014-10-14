package cn.com.youtong.apollo.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import cn.com.youtong.apollo.common.Convertor;
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.task.*;

/**
 * 根据任务数据流，解析出TaskData。
 * 任务模型见TaskModel.xsd。
 * 根元素可以是taskModel，taskTime，和unit三种。
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author wjb
 * @version 1.0
 */
public class TaskDataParser
{
	private static SAXParserFactory saxParserFcty = SAXParserFactory.newInstance();

    public TaskDataParser()
    {
    }

	/**
	 * 从InputStream，构造出TaskData对象。
	 * 根元素可以是taskModel，taskTime，和unit三种。
	 * <p>
	 * 使用sax解析，没有考虑浮动表，但是XML数据流存在浮动表，不影响解析和解析结果。
	 * 对于TaskData里面的TableData，如果不存在值，不返回缺省值。
	 * 构造缺省值，在解析步骤完成后进行。
	 * <br/>
	 * <b>Note: 如果某个Table存在值，子元素的个数为0；或者，该Table所有非浮动行除外的
	 * 列数。</b>
	 * <br/>
	 * 例如，TableA有10列，那么TableA的子元素个数，为0，或者10。
	 * </p>
	 *
	 * <p>
	 * 解析处理的TaskData的tableData Map大小等于XML数据流里面的table元素个数。
	 * 所以XML数据流应保证包含所有table元素。
	 * </p>
	 *
	 * <p>
	 *  另外解析程序不负责判断，该表是否存在该列。
	 * 返回的field值都被大写.
	 * </p>
	 *
	 * @param dataSource       数据源
	 * @param in               符合TaskModel.xsd的任务数据XML流。
	 *                         根元素可以是taskModel，taskTime，和unit三种。
	 * @param task             数据涉及到的任务
	 * @param taskTime         数据涉及到的任务时间
	 * @return                 构造出的TaskData对象
	 * @throws ModelException
	 */
	public static TaskData parse( DataSource dataSource, InputStream in ,
								  Task task, TaskTime taskTime)
		throws ModelException
	{
		SAXParser parser = null;
		try
		{
			parser = saxParserFcty.newSAXParser();
			TaskDataHandler handler = new TaskDataHandler();
			parser.parse( in,  handler );

			Map tables = handler.getTables();
			if( !checkHasData( tables ) )
				return null;

			String unitID = handler.getUnitID();

			// 重新加工tables里面的数据，现在里面的数据格式为
			// key/value=java.lang.String/java.lang.String，
			// 改造value为field对于的值类型，比如Date为Date型
			Map tableDatas = buildTablesData( task, tables );

			TaskData taskData = new TaskData( dataSource, task, unitID, taskTime, tableDatas );

			return taskData;
		}
		catch( ParserConfigurationException ex )
		{
			throw new ModelException( "系统SAX解析器配置错误", ex );
		}
		catch( SAXException ex )
		{
			throw new ModelException( "解析错误", ex );
		}
		catch( IOException ex )
		{
			throw new ModelException( "IO错误", ex );
		}
		catch( TaskException ex )
		{
			throw new ModelException( "任务信息操作", ex );
		}
	}

	/**
	 * 查看tables里面是否有数据存在.
	 * tables:key/value=tableID/java.util.Map[key/value=field/value]
	 * @param tables
	 * @return
	 */
	private static boolean checkHasData( Map tables )
	{
		if( tables == null)
			return false;

		if( tables.size() == 0 )
			return false;

		for( Iterator iter = tables.values().iterator(); iter.hasNext(); )
		{
			Map dbfield2valueMap = (Map) iter.next();
			if( dbfield2valueMap.size() != 0 )
				return true;
		}

		return false;
	}
	/**
	 * tables里面的数据格式为key/value=field[java.lang.String]/value[java.lang.String]。
	 * 需要根据field的值类型，转换为相应的对象。
	 *
	 * <p>
	 * 如果，某个表格解析到的数据个数为0，那么填写缺省数据。
	 * 如果对于的field在任务定义里面没有找到，删除该字段。
	 * </p>
	 *
	 * @param task    任务定义
	 * @param tables  数据，数据格式为key/value=tableID/Map;其中Map格式为key/value=field/value
	 * @return        Map格式为key/value=tableID/TableData
	 * @throws TaskException
	 */
	private static Map buildTablesData( Task task, Map tables )
		throws TaskException
	{
		Map tableDatas = new HashMap();
		for( Iterator iter = tables.entrySet().iterator(); iter.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String tableID = (String) entry.getKey();
			Map field2valueMap = (Map) entry.getValue();

			// 打造每个表
			Table table = task.getTableByID( tableID );
			if( table != null )
			{
				buildTableData( table, field2valueMap );
				tableDatas.put( tableID, new TableData( field2valueMap ) );
			}
		}

		return tableDatas;
	}

	/**
	 * 打造单独的一个表。
	 * 主要是改造数字类型和日期类型。
	 * 改造依据是Table定义的表格DataType。
	 * 如果，该表格数据个数为0，那么填写缺省数据。
	 * <p>
	 * field2valueMap存在的field在Table定义中没有出现，那么从field2valueMap中删除该field；
	 * 如果对应的日期格式不正确，使用当前日期。（当然这种情况在正常流程中，应该不会出现，因为数据都是来自数据库的^_^）
	 * </p>
	 *
	 * @param table                 表格定义信息
	 * @param field2valueMap        Map格式为key/value=field/value
	 */
	private static void buildTableData( Table table, Map field2valueMap )
	{
		if( field2valueMap.size() == 0 )
		{
			// 没有数据，填写缺省数据
			for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
			{
				Row row = (Row) rowIter.next();
				if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					continue; // 浮动行

				for( Iterator cellIter = row.getCells(); cellIter.hasNext(); )
				{
					Cell cell = (Cell) cellIter.next();

					int dataType = cell.getDataType();
					if( dataType == Cell.TYPE_NUMERIC )
						field2valueMap.put( cell.getDBFieldName(), new Double( 0 ) );
					else if( dataType == Cell.TYPE_DATE )
						field2valueMap.put( cell.getDBFieldName(), new Date() );
					else
						field2valueMap.put( cell.getDBFieldName(), "" );
				}
			}

			return;
		}

		for( Iterator iter = field2valueMap.entrySet().iterator(); iter.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String field = (String) entry.getKey();
			String value = (String) entry.getValue();

			// 查找Cell
			Cell cell = table.getCellByDBFieldName( field );

			if( cell == null )
			{
				// Cell 不存在， 在正常流程中基本很少出现
				iter.remove();
			}
			else
			{
				int dataType = cell.getDataType();
				if( dataType == Cell.TYPE_NUMERIC )
				{
					if( Util.isEmptyString( value ) )
					{
						entry.setValue( new Double( 0 ) );
					}
					else
					{
						entry.setValue( new Double( value ) );
					}
				}
				else if( dataType == Cell.TYPE_DATE )
				{
					Date date = null;
					try
					{
						date = Convertor.string2Date(value);
					}
					catch( Exception ex )
					{ // Eat Exception
					}

					if( date == null )
					{
						date = new Date();
					}

					entry.setValue( date );
				}
			}
		}
	}
}


/**
 * TaskData 的 SAX 解析处理器。
 * 任务和任务时间不进行解析，它们都是从构造函数中获得。
 * 这里面的field值都被大写!
 */
class TaskDataHandler extends DefaultHandler
{
	/** 文档中遇到的元素名称 */
	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String UNIT_ELE_NAME = "unit";
	private static final String TABLE_ELE_NAME = "table";
	private static final String CELL_ELE_NAME = "cell";
	private static final String FIELD_ATTR_NAME = "field";
	private static final String VALUE_ATTR_NAME = "value";

	private String unitID;
	private Map tables = new HashMap();

	private String tableID; // 临时变量，保存当前处理的表格ID
	private Map dbfield2valueMap; // 临时变量，保存当前处理的表格值对关系

	private boolean normalCell = false; // 指示当前是否可以读取cell的值，避免浮动行的干扰

	// 遇到不同的元素进行不同的处理
    public void startElement(String uri, String localName,
							 String qName, Attributes attrs)
		throws org.xml.sax.SAXException
    {
		String eName = qName; // 当前读取的元素名称
		if( eName.equals( CELL_ELE_NAME ) )
		{
			if( normalCell )
			{
				String field = attrs.getValue( FIELD_ATTR_NAME );
				String value = attrs.getValue( VALUE_ATTR_NAME );
				if( value == null )
					value = "";

                // put into map
				dbfield2valueMap.put( field.toUpperCase(), value );
			}
			return;
		}
		else if( eName.equals( TABLE_ELE_NAME ) )
		{
			normalCell = true;
			tableID = attrs.getValue( 0 );
			dbfield2valueMap = new HashMap();
			return;
		}
		else if( eName.equals( UNIT_ELE_NAME ) )
		{
			unitID = attrs.getValue(0);
			return;
		}
		else
		{
			normalCell = false;
		}
    }

	// 遇到不同的元素结尾，进行不同的处理
    public void endElement(String uri, String localName,
						   String qName)
		throws org.xml.sax.SAXException
    {
		String eName = qName; // 当前读取的元素名称
		if( eName.equals( CELL_ELE_NAME ) )
		{
			return;
		}
		else if( eName.equals( TABLE_ELE_NAME ) )
		{
			// put dbfield2valueMap to tables
			tables.put( tableID, dbfield2valueMap );
			return;
		}
		else if( eName.equals( UNIT_ELE_NAME ) )
		{
			return;
		}
    }

	String getUnitID() {
		return unitID;
	}

	/**
	 * key/value = field[java.lang.String]/value[java.lang.String]
	 * @return   Map
	 */
	Map getTables() {
		return tables;
	}
}