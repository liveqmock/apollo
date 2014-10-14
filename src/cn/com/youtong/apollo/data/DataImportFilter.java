package cn.com.youtong.apollo.data;

// java
import java.io.*;
import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

// common log
import org.apache.commons.logging.*;

// apollo
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.task.*;

/**
 * 分析文档，决定导入适当的数据。
 * 适当的意思包含：
 * <ul>
 *   <li>任务和构造器传入参数匹配</li>
 *   <li>当前系统允许导入该时间数据</li>
 *   <li>导入者有权限导入某单位数据</li>
 * </ul>
 *
 * <p>
 * 该方法要记录导入情况（即结果）。
 * 具体保存数据的工作由DataStorer完成。
 * 消息发送由DataImport
 * </p>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yutop</p>
 * @author wjb
 * @version 1.0
 */
class DataImportFilter extends DefaultHandler
{
	/** 文档中遇到的元素名称 */
	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String UNIT_ELE_NAME = "unit";

	/** ACL */
	private UnitACL acl;
	/** 当前解析的数据相关的任务定义信息 */
	private Task task;
	/** 导入任务的时间，在解析taskTime元素时赋值 */
	private TaskTime taskTime;
	private Log log = LogFactory.getLog( DataImportFilter.class );
	public DataImportFilter( Task task,
							 UnitACL acl )
	{
		this.task = task;
		this.acl = acl;
	}

/**
	 * 遇到元素开头。
	 * 只对taskModel,taskTime,unit元素感兴趣。
	 *
	 * <p>
	 * taskModel，存在ID表示任务ID，如果任务ID不一致，停止解析。
	 * </p>
	 *
	 * <p>
	 * taskTime，存在taskTime属性，表示某时间的数据。比如2004-03-01+08:00:00表示
	 * 2004年3月1日的数据。
	 * 如果当前不允许导入taskTime的数据，那么停止解析。
	 * </p>
	 *
	 * <p>
	 * unit，存在ID属性，表示单位代码。
	 * 如果acl，权限不足，继续解析；
	 * 否则，调用保存模块解析目前的单位。
	 * </p>
	 *
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws org.xml.sax.SAXException
	 */
	public void startElement( String uri, String localName, String qName,
							  Attributes attributes )
		throws SAXException
	{
		String eName = qName; // 当前读取元素名
		if( !Util.isEmptyString( localName ) )
		{
			eName = localName;
		}

		if( log.isDebugEnabled() )
		{
			StringBuffer buff = new StringBuffer( "<" ).append( eName );

			for( int i = 0, size = attributes.getLength(); i < size; i++ )
			{
				buff.append( " " )
					.append( attributes.getQName( i ) )
					.append( "=\"" )
					.append( attributes.getValue( i ) )
					.append( "\"" );
			}
			buff.append( ">" );
			log.debug( buff.toString() );
		}

	}

	public void endElement( String uri, String localName, String qName )
		throws SAXException
	{
		String eName = qName; // 当前读取元素名
		if( !Util.isEmptyString( localName ) )
		{
			eName = localName;
		}

		if( log.isDebugEnabled() )
		{
			log.debug( "</" + eName + ">" );
		}

	}

	/**
	 * 保存附件
	 * @param parm1
	 * @param parm2
	 * @param parm3
	 * @throws org.xml.sax.SAXException
	 */
	public void characters( char[] parm1, int parm2, int parm3 )
		throws SAXException
	{
		/**@todo Override this org.xml.sax.helpers.DefaultHandler method*/
		super.characters( parm1, parm2, parm3 );
	}

	public static void main( String[] args ) throws Exception
	{
		ByteArrayInputStream in = new ByteArrayInputStream( "<a b=\"c\"><hahh/></a>".getBytes() );
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse( in, new DataImportFilter( null, null ) );
	}
}
