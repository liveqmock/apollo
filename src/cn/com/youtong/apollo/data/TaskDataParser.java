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
 * ����������������������TaskData��
 * ����ģ�ͼ�TaskModel.xsd��
 * ��Ԫ�ؿ�����taskModel��taskTime����unit���֡�
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
	 * ��InputStream�������TaskData����
	 * ��Ԫ�ؿ�����taskModel��taskTime����unit���֡�
	 * <p>
	 * ʹ��sax������û�п��Ǹ���������XML���������ڸ�������Ӱ������ͽ��������
	 * ����TaskData�����TableData�����������ֵ��������ȱʡֵ��
	 * ����ȱʡֵ���ڽ���������ɺ���С�
	 * <br/>
	 * <b>Note: ���ĳ��Table����ֵ����Ԫ�صĸ���Ϊ0�����ߣ���Table���зǸ����г����
	 * ������</b>
	 * <br/>
	 * ���磬TableA��10�У���ôTableA����Ԫ�ظ�����Ϊ0������10��
	 * </p>
	 *
	 * <p>
	 * ���������TaskData��tableData Map��С����XML�����������tableԪ�ظ�����
	 * ����XML������Ӧ��֤��������tableԪ�ء�
	 * </p>
	 *
	 * <p>
	 *  ����������򲻸����жϣ��ñ��Ƿ���ڸ��С�
	 * ���ص�fieldֵ������д.
	 * </p>
	 *
	 * @param dataSource       ����Դ
	 * @param in               ����TaskModel.xsd����������XML����
	 *                         ��Ԫ�ؿ�����taskModel��taskTime����unit���֡�
	 * @param task             �����漰��������
	 * @param taskTime         �����漰��������ʱ��
	 * @return                 �������TaskData����
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

			// ���¼ӹ�tables��������ݣ�������������ݸ�ʽΪ
			// key/value=java.lang.String/java.lang.String��
			// ����valueΪfield���ڵ�ֵ���ͣ�����DateΪDate��
			Map tableDatas = buildTablesData( task, tables );

			TaskData taskData = new TaskData( dataSource, task, unitID, taskTime, tableDatas );

			return taskData;
		}
		catch( ParserConfigurationException ex )
		{
			throw new ModelException( "ϵͳSAX���������ô���", ex );
		}
		catch( SAXException ex )
		{
			throw new ModelException( "��������", ex );
		}
		catch( IOException ex )
		{
			throw new ModelException( "IO����", ex );
		}
		catch( TaskException ex )
		{
			throw new ModelException( "������Ϣ����", ex );
		}
	}

	/**
	 * �鿴tables�����Ƿ������ݴ���.
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
	 * tables��������ݸ�ʽΪkey/value=field[java.lang.String]/value[java.lang.String]��
	 * ��Ҫ����field��ֵ���ͣ�ת��Ϊ��Ӧ�Ķ���
	 *
	 * <p>
	 * �����ĳ���������������ݸ���Ϊ0����ô��дȱʡ���ݡ�
	 * ������ڵ�field������������û���ҵ���ɾ�����ֶΡ�
	 * </p>
	 *
	 * @param task    ������
	 * @param tables  ���ݣ����ݸ�ʽΪkey/value=tableID/Map;����Map��ʽΪkey/value=field/value
	 * @return        Map��ʽΪkey/value=tableID/TableData
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

			// ����ÿ����
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
	 * ���쵥����һ����
	 * ��Ҫ�Ǹ����������ͺ��������͡�
	 * ����������Table����ı��DataType��
	 * ������ñ�����ݸ���Ϊ0����ô��дȱʡ���ݡ�
	 * <p>
	 * field2valueMap���ڵ�field��Table������û�г��֣���ô��field2valueMap��ɾ����field��
	 * �����Ӧ�����ڸ�ʽ����ȷ��ʹ�õ�ǰ���ڡ�����Ȼ������������������У�Ӧ�ò�����֣���Ϊ���ݶ����������ݿ��^_^��
	 * </p>
	 *
	 * @param table                 �������Ϣ
	 * @param field2valueMap        Map��ʽΪkey/value=field/value
	 */
	private static void buildTableData( Table table, Map field2valueMap )
	{
		if( field2valueMap.size() == 0 )
		{
			// û�����ݣ���дȱʡ����
			for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
			{
				Row row = (Row) rowIter.next();
				if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					continue; // ������

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

			// ����Cell
			Cell cell = table.getCellByDBFieldName( field );

			if( cell == null )
			{
				// Cell �����ڣ� �����������л������ٳ���
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
 * TaskData �� SAX ������������
 * ���������ʱ�䲻���н��������Ƕ��Ǵӹ��캯���л�á�
 * �������fieldֵ������д!
 */
class TaskDataHandler extends DefaultHandler
{
	/** �ĵ���������Ԫ������ */
	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String UNIT_ELE_NAME = "unit";
	private static final String TABLE_ELE_NAME = "table";
	private static final String CELL_ELE_NAME = "cell";
	private static final String FIELD_ATTR_NAME = "field";
	private static final String VALUE_ATTR_NAME = "value";

	private String unitID;
	private Map tables = new HashMap();

	private String tableID; // ��ʱ���������浱ǰ����ı��ID
	private Map dbfield2valueMap; // ��ʱ���������浱ǰ����ı��ֵ�Թ�ϵ

	private boolean normalCell = false; // ָʾ��ǰ�Ƿ���Զ�ȡcell��ֵ�����⸡���еĸ���

	// ������ͬ��Ԫ�ؽ��в�ͬ�Ĵ���
    public void startElement(String uri, String localName,
							 String qName, Attributes attrs)
		throws org.xml.sax.SAXException
    {
		String eName = qName; // ��ǰ��ȡ��Ԫ������
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

	// ������ͬ��Ԫ�ؽ�β�����в�ͬ�Ĵ���
    public void endElement(String uri, String localName,
						   String qName)
		throws org.xml.sax.SAXException
    {
		String eName = qName; // ��ǰ��ȡ��Ԫ������
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