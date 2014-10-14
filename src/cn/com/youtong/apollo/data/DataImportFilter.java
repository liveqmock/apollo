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
 * �����ĵ������������ʵ������ݡ�
 * �ʵ�����˼������
 * <ul>
 *   <li>����͹������������ƥ��</li>
 *   <li>��ǰϵͳ�������ʱ������</li>
 *   <li>��������Ȩ�޵���ĳ��λ����</li>
 * </ul>
 *
 * <p>
 * �÷���Ҫ��¼������������������
 * ���屣�����ݵĹ�����DataStorer��ɡ�
 * ��Ϣ������DataImport
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
	/** �ĵ���������Ԫ������ */
	private static final String TASK_ELE_NAME = "taskModel";
	private static final String TASK_TIME_ELE_NAME = "taskTime";
	private static final String UNIT_ELE_NAME = "unit";

	/** ACL */
	private UnitACL acl;
	/** ��ǰ������������ص���������Ϣ */
	private Task task;
	/** ���������ʱ�䣬�ڽ���taskTimeԪ��ʱ��ֵ */
	private TaskTime taskTime;
	private Log log = LogFactory.getLog( DataImportFilter.class );
	public DataImportFilter( Task task,
							 UnitACL acl )
	{
		this.task = task;
		this.acl = acl;
	}

/**
	 * ����Ԫ�ؿ�ͷ��
	 * ֻ��taskModel,taskTime,unitԪ�ظ���Ȥ��
	 *
	 * <p>
	 * taskModel������ID��ʾ����ID���������ID��һ�£�ֹͣ������
	 * </p>
	 *
	 * <p>
	 * taskTime������taskTime���ԣ���ʾĳʱ������ݡ�����2004-03-01+08:00:00��ʾ
	 * 2004��3��1�յ����ݡ�
	 * �����ǰ��������taskTime�����ݣ���ôֹͣ������
	 * </p>
	 *
	 * <p>
	 * unit������ID���ԣ���ʾ��λ���롣
	 * ���acl��Ȩ�޲��㣬����������
	 * ���򣬵��ñ���ģ�����Ŀǰ�ĵ�λ��
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
		String eName = qName; // ��ǰ��ȡԪ����
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
		String eName = qName; // ��ǰ��ȡԪ����
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
	 * ���渽��
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
