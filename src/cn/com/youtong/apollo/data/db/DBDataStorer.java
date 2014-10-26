/*
 * Created on 2004-4-9
 */
package cn.com.youtong.apollo.data.db;

// java
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

// apache
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
// apollo
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.common.sql.HibernateUtil;
import cn.com.youtong.apollo.common.sql.NameGenerator;
import cn.com.youtong.apollo.common.sql.SQLUtil;
import cn.com.youtong.apollo.data.ModelException;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.services.Config;
import cn.com.youtong.apollo.services.ConfigException;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.data.*;
// hibernate
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * ������Ȩ�ޣ������������߷�����Ϣ�Ȳ�������£����������ݵ������ݿ⡣
 * Ҳû�нű�����.
 *
 * storer����һ��ֻ�ܵ���һ����λ������.
 * <b>�����˶��store������,һ��Ҫ����commit����!</b>
 * <p>
 * ��������֮ǰ���������{@link #init()}������
 * �ύ����������{@link #commit()}������
 * ʹ����ϵ���{@link release()}�����ͷ���Դ��
 * </p>
 *
 * <p>
 * ��������<br/>
 * �������������ݿ��Ը��£����ǹ̶��ֶΣ������룬�ϼ����룬���Ŵ��룬P_PARENT�ֶΣ���
 * һ������£������޸ġ�
 * <br/>
 * ���������ļ�cn.com.youtong.apollo.loaddata.modify.fixcode=true����ô�����޸Ĺ̶��ֶΡ�
 * Note���޸���Щ�̶��ֶν����µ�λ���ı䶯��
 * <br/>
 * �����������P_PARENT�ֶα�����
 * <ul>
 *     <li>��ǰ������P_PARENT;</li>
 *     <li>��ǰ���ڵ�P_PARENT��unitID������</li>
 * </ul>
 * </p>
 *
 * @author jbwang
 */
class DBDataStorer
{
	private Task task;
	private TaskTime taskTime;

	// log
	private static Log log = LogFactory.getLog( DBDataStorer.class );

	/** hibernate  */
	private Session session;
	private Transaction tx;
	/** ���ݿ����� */
	private Connection con;
	private SAXParser parser;
	private DefaultHandler handler;

	private DBUnitTreeManager treeMng;

	/** ���·���� pstmt */
	private PreparedStatement updateUnitMetaTablePstmt;
	/** �������� pstmt */
	private PreparedStatement insertUnitMetaTablePstmt;
	/**
	* key/value=tableID[java.lang.String]/tablePstmt[TablePreparedStatement]
	 * ��������Ĳ������PreparedStatement
	 */
	private Map otherTablePstmtMap;
	private Statement deleteTabelStmt;

	private TableCellInfo unitMetaTableCellInfo;
	/** key/value=tableID[java.lang.String]/tableCellInfo[TableCellInfo]
	 *  �����������������Ϣ
	 */
	private Map otherTableCellInfoMap;
	/** ��ʶ�Ƿ������init���� */
	private boolean inited;
	private boolean isNewUnit;
	private boolean allowNewUnit = Config.getBoolean("cn.com.youtong.apollo.data.import.loadnewunit");;


	private StringWriter attachmentWriter;
	private Clob attachmentClob;
	
	/**
	 * @param task Task
	 * @param taskTime TaskTime
	 */
	public DBDataStorer( Task task, TaskTime taskTime )
	{
		this.task = task;
    	this.taskTime = taskTime;
	}

	public Task getTask()
	{
		return task;
	}

	public TaskTime getTaskTime()
	{
		return taskTime;
	}

	/**
	 * �л�һ������ʱ��
	 * @param taskTime
	 */
	public void setTaskTime( TaskTime taskTime )
	{
		this.taskTime = taskTime;
	}

	/**
	 * ��ǰ������Ƿ����µ�λ��
	 * ע�⣬�����µ�λ���ݵ�ǰ�������������ļ���
	 * cn.com.youtong.apollo.data.import.loadnewunit����ֵΪtrue��
	 * @return  �Ƿ����µ�λ
	 */
	public boolean isNewUnit()
	{
		return isNewUnit;
	}

	/**
	 * ��DBDataStorer��ɳ�ʼ��{@link init()}�󣬿��Ե������ݡ�
	 * storer����һ��ֻ�ܵ���һ����λ������.
	 * �������ݣ���û�����Ͼ������Եĵ��������ݿ⡣
	 * �������commit�������������Ե��������ݿ⡣
	 * "����"���Ϊ"����",���ܸ�ȷ��Щ.
	 *
	 * @param in                    �����������磺
	 * <pre>
	 * <?xml version="1.0" encoding="gb2312"?>
	 * <unit ID="unitID1">
	 * <table ID="FM">
	 * <cell field="FIELD1" value="value1"/>
	 * <cell field="FIELD2" value="value2"/>
	 * </table>
	 *
	 * <table ID="TABLE2">
	 * <floatRow ID="ROW1">
	 * <row>
	 * <cell field="FIELD1" value="value1"/>
	 * <cell field="FIELD2" value="value2"/>
	 * </row>
	 * <row>
	 * <cell field="FIELD1 value="value3"/>
	 * <cell field="FIELD2 value="value4"/>
	 * </row>
	 * </floatRow>
	 * </table>
	 *
	 * <attachement name="attachement">
	 * Attachement goes here.
	 * </attachement>
	 * </unitID>
	 * </pre>
	 * @throws ModelException        ������̷����쳣
	 */
	public void store( InputStream in )
		throws ModelException
	{
		if( !inited )
			throw new ModelException( "û����ɳ�ʼ������" );

		log.debug( "��ʼ��������" );
		if( tx == null )
		{
			try
			{
				tx = session.beginTransaction();
			}
			catch( HibernateException he )
			{
				log.error( "�����������", he );
				throw new ModelException( "�����������", he );
			}
		}

		try
		{
			parser.parse( in, handler );
		}
		catch( java.io.IOException ex )
		{
			log.error( "IO�쳣", ex );
			throw new ModelException( "IO�쳣", ex );
		}
		catch( org.xml.sax.SAXException se )
		{
			log.error( "SAX�����쳣", se );
			throw new ModelException( "SAX�����쳣", se );
		}
		log.debug( "�����������" );
	}

	/**
	 * ��ʼ�����á�Ϊ������������׼����
	 * ��Ҫ�ṩDBUnitTreeManagerʵ����
	 *
	 * <p>
	 * ʵ�ֵ������У�
	 * <ul>
	 *    <li>�������ݿ�����</li>
	 *    <li>��ʼ�������Ĳ���PreparedStatement</li>
	 *    <li>������Ĳ���PreparedStatement</li>
	 *    <li>������TableCellInfo</li>
	 *    <li>�������TableCellInfo</li>
	 *    <li>SAX������</li>
	 *    <li>��������Handler</li>
	 * </ul>
	 * </p>
	 * @param configuration Map  key/value��Լ��Ϊinstance.class.getName()/
	 *                                   instance
	 * @throws ModelException
	 */
	public void init( Map configuration )
		throws ModelException
	{
		log.info( "��ʼ��ʼ��" );
		try
		{
			// �鿴DBUnitTreeManagerʵ���Ƿ����
			if( configuration == null )
				throw new ModelException( "ȱ��DBUnitTreeManagerʵ��" );

			Object obj = configuration.get( DBUnitTreeManager.class.getName() );
			if( obj == null || !(obj instanceof DBUnitTreeManager) )
				throw new ModelException( "ȱ��DBUnitTreeManagerʵ��" );

			this.treeMng = (DBUnitTreeManager) obj;

			session = HibernateUtil.getSessionFactory().openSession();
			con = session.connection();

			// ��ʼ�������PreparedStatement��TableCellInfo
			initTableInfos();

			// ��ʼ��SAXParser
			parser = SAXParserFactory.newInstance().newSAXParser();
			// ��ʼ��DefaultHandler
			handler = new InnerHandler();
		}
		catch( ModelException me )
		{
			throw me;
		}
		catch( Exception ex )
		{
			log.error( "DBDataStorer������������", ex );
			throw new ModelException( "DBDataStorer������������", ex );
		}
		log.info( "��ʼ�����" );
		inited = true;
	}

	/**
	 * commit������
	 * �����ݿ�д�����ݣ��Ա������Ա��档
	 * @throws ModelException
	 */
	public void commit() throws ModelException
	{
		log.debug( "��ʼ�ύ����" );
		try
		{
			deleteTabelStmt.executeBatch();

			if( insertUnitMetaTablePstmt != null )
				insertUnitMetaTablePstmt.executeBatch();

			if( otherTablePstmtMap != null )
			{
				for( Iterator iter = otherTablePstmtMap.values().iterator();
									 iter.hasNext(); )
				{
					TablePreparedStatement tps = ( TablePreparedStatement )
												 iter.next();
					if( tps.tablePstmt != null )
						tps.tablePstmt.executeBatch();

					if( tps.floatTablePstmt != null )
					{
						for( Iterator iter2 = tps.floatTablePstmt.values().
											  iterator();
											  iter2.hasNext(); )
						{
							PreparedStatement pstmt = ( PreparedStatement )
								iter2.next();
							if( pstmt != null )
								pstmt.executeBatch();
						}
					}
				}
			}
			if( tx != null )
				tx.commit();

			tx = null; // ��ֵnull,�Ա��ٴα����ʱ�򣬿����µ�transaction
			log.debug( "�ύ�������" );
		}
		catch( HibernateException he )
		{
			log.error( "�ύ�������", he );
			log.debug( "�ύ����˳�����" );
			throw new ModelException( "�ύ�������", he );
		}
		catch( SQLException sqle )
		{
			log.error( "��������ʱ����SQL�쳣", sqle );
			log.debug( "�ύ����˳�����" );
			throw new ModelException( "��������ʱ����SQL�쳣", sqle );
		}
	}

	/**
	 * �ͷſ�������Դ��
	 * �ڳ����finally���棬�ͷ���Դ�Ǹ����õı��ϰ�ߡ�
	 */
	public void release()
	{
		log.info( "��ʼ�ͷ���Դ" );

		// ����������񱻿���,�ύ
		if( tx != null )
			try
			{
				tx.commit();
			}
			catch(Exception ex )
			{ }

		SQLUtil.close( deleteTabelStmt );
		if( updateUnitMetaTablePstmt != null )
		{
			SQLUtil.close( updateUnitMetaTablePstmt );
		}

		SQLUtil.close( insertUnitMetaTablePstmt );

		if( otherTablePstmtMap != null )
		{
			for( Iterator iter = otherTablePstmtMap.values().iterator();
								 iter.hasNext(); )
			{
				TablePreparedStatement tps = (TablePreparedStatement) iter.next();
				SQLUtil.close( tps.tablePstmt );

				if( tps.floatTablePstmt != null )
				{
					for( Iterator iter2 = tps.floatTablePstmt.values().iterator();
										  iter2.hasNext(); )
					{
						PreparedStatement pstmt = (PreparedStatement) iter2.next();
						SQLUtil.close( pstmt );
					}
				}
			}
		}

		HibernateUtil.close( session );
		log.info( "��Դ�ͷ����" );
	}

	/**
	 * ��ʼ�����������PreparedStatement�Լ�TableCellInfo
	 * @throws SQLException
	 */
	private void initTableInfos( )
		throws SQLException
	{
		Task task = getTask();
		String unitMetaTableID = task.getUnitMetaTable().id();

		// delete statement
		deleteTabelStmt = con.createStatement();
		otherTablePstmtMap = new HashMap();
		otherTableCellInfoMap = new HashMap();

		// unit meta table
		initUnitMetaTableInfo();

		// other tables
		initOtherTableInfos();
	}

	/**
	 * ��ʼ�������Ĳ���preparedstatement �� TableCellInfo
	 * @throws SQLException
	 */
	private void initUnitMetaTableInfo( )
		throws SQLException
	{
		String taskID = getTask().id();
		String unitMetaTableID = getTask().getUnitMetaTable().id();

		String unitMetaDBTableName =
						 NameGenerator.generateDataTableName( taskID, unitMetaTableID );

		Object[] result = pstmtAndCellInfoMap( unitMetaDBTableName );
		insertUnitMetaTablePstmt = (PreparedStatement) result[0];
		unitMetaTableCellInfo = new TableCellInfo( (Map) result[1], null );
	}

	/**
	 * ��ʼ����������������Ĳ���preparedstatement �� TableCellInfo
	 * @throws SQLException
	 */
	private void initOtherTableInfos()
		throws SQLException
	{
		Task task = getTask();
		String taskID = task.id();
		String unitMetaTableID = task.getUnitMetaTable().id();

		Iterator iter = null;
		try
		{
			iter = task.getAllTables();
		}
		catch( TaskException te )
		{
			log.error( "��ȡ������Ϣ����", te );
			throw new SQLException( "��ȡ������Ϣ����" );
		}
		while( iter.hasNext() )
		{
			Table table = (Table) iter.next();
			String tableID = table.id();
			if( !tableID.equals( unitMetaTableID ) )
			{
				// ���Ƿ����
				// pstmt
				PreparedStatement insertPstmt = null;
				Map floatInsertPstmtMap = null;

				// cell info
				Map cellInfoMap = null;
				Map floatCellInfoMap = null;
				boolean containAllFloatRow = true; // �ñ�ȫ���ɸ��������
				for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
				{
					Row row = (Row) rowIter.next();
					// �ж��Ƿ�Ϊ������
					if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					{
						// ������
						if( floatInsertPstmtMap == null )
						{
							// ��ǰ��һ������������
							floatInsertPstmtMap = new HashMap();
							floatCellInfoMap = new HashMap();
						}

						String rowID = row.id();
						Object[] result = pstmtAndCellInfoMap(
											  NameGenerator.generateFloatDataTableName(
							taskID,
							tableID,
							rowID ) );

						PreparedStatement pstmtTemp = (PreparedStatement) result[0];
						Map mapTemp = (Map) result[1];

						floatInsertPstmtMap.put( rowID, pstmtTemp );
						floatCellInfoMap.put( rowID, mapTemp );
					}
					else
					{
						containAllFloatRow = false;
					}
				}

				if( !containAllFloatRow )
				{
					Object[] result = pstmtAndCellInfoMap(
									   NameGenerator.generateDataTableName(
						taskID,
						tableID ) );

					insertPstmt = (PreparedStatement) result[0];
					cellInfoMap = (Map) result[1];
				}

				TablePreparedStatement tps =
								new TablePreparedStatement( insertPstmt, floatInsertPstmtMap);
				TableCellInfo tci = new TableCellInfo( cellInfoMap, floatCellInfoMap );

				otherTablePstmtMap.put( tableID, tps );
				otherTableCellInfoMap.put( tableID, tci );
			}
		}
	}

	/**
	 * �������ݱ����ظñ�Ĳ�������PreparedStatement��CellInfoMap
	 * @param dbTableName ���ݿ�������
	 * @throws SQLException
	 * @return Object[]  [0]��������PreparedStatement��
	 *                   [1]CellInfoMap
	 */
	private Object[] pstmtAndCellInfoMap( String dbTableName )
		throws SQLException
	{
		Object[] result = new Object[2];
		PreparedStatement pstmt = null;
		try
		{
			pstmt = con.prepareStatement( "SELECT * FROM "
										  + dbTableName
										  + " WHERE 1 = 0 " );
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			StringBuffer buff = new StringBuffer();
			buff.append( "INSERT INTO " )
				.append( dbTableName )
				.append( " VALUES( ? " );
			for( int i = 1, count = rsmd.getColumnCount();
									i < count; i++ )
			{
				buff.append( ",?" );
			}
			buff.append( ")" );
			String sql = buff.toString();
			if( log.isDebugEnabled() )
			{
				log.debug( "Insert " + dbTableName + " SQL: \n \t\t" + sql );
			}

			// ��ֵ���������PreparedStatement
			PreparedStatement insertPstmt = con.prepareStatement( sql );

			Map cellInfoMap = new HashMap();
			for( int i=1, count = rsmd.getColumnCount(); i <= count; i++ )
			{
				String fieldName = rsmd.getColumnName( i );
				int sqlType = rsmd.getColumnType( i );
				CellInfo info = new CellInfo( i, sqlType );

				cellInfoMap.put( fieldName.toUpperCase(), info );
			}

			result[0] = insertPstmt;
			result[1] = cellInfoMap;
		}
		finally
		{
			SQLUtil.close( pstmt );
		}
		return result;
	}

	/**
	 * {@link DBDataStorer#storer()}������ʹ��InnerHandler����XML���ݡ�
	 * @see {@link DBDataStorer#storer()}
	 */
	private class InnerHandler extends DefaultHandler
	{
		private static final String UNIT_ELE_NAME = "unit";
		private static final String UNIT_ID_ATTR_NAME = "ID";
		private static final String TABLE_ELE_NAME = "table";
		private static final String TABLE_ID_ATTR_NAME = "ID";
		private static final String CELL_ELE_NAME = "cell";
		private static final String FLOAT_ROW_ELE_NAME = "floatRow";
		private static final String FLOAT_ROW_ID_ATTR_NAME = "ID";
		private static final String ROW_ELE_NAME = "row";
		private static final String FIELD_ATTR_NAME = "field";
		private static final String VALUE_ATTR_NAME = "value";
		private static final String ATTACHMENT_ELE_NAME = "attachment";
		private static final String ATTACHMENT_NAME_ATTR_NAME = "name";
		private static final String FLAG_ELE_NAME = "flag";        //�ͻ����ϱ�ǰ����Ϊ�ݸ�ı�־
		private static final String FLAG_OPR_ATTR_NAME="operation";//�ͻ����ϱ�ǰ����Ϊ�ݸ�ı�־ֵ

		private String unitID;
		private String tableID;
		private String floatRowID;
		private String attachmentName;
		private String flagOper;
		private Map fieldValueMap;
		private Map floatFieldValueMap;

		private boolean readFloatRow;

		public void startElement( String uri, String localName, String qName,
								  Attributes attributes )
			throws SAXException
		{

			if( isNewUnit && !allowNewUnit )
				return; // ��ʾ���µ�λ������ϵͳ���ò��ܵ����µ�λ

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

			if( eName.equals( CELL_ELE_NAME ) )
			{
				// ��ӵ���Ӧ��field/value map��ֱ��row����table��ǩ����
				String field = attributes.getValue(FIELD_ATTR_NAME);
				String value = attributes.getValue(VALUE_ATTR_NAME);
				if( readFloatRow )
				{
					floatFieldValueMap.put( field.toUpperCase(), value );
				}
				else
				{
					fieldValueMap.put( field, value );
				}
				return;
			}
			else if( eName.equals( TABLE_ELE_NAME ) )
			{
				// ��ȡtableID����ʼ������Ҫ��field/value map
				tableID = attributes.getValue( TABLE_ID_ATTR_NAME );
				fieldValueMap = new HashMap();
				return;
			}
			else if( eName.equals( ROW_ELE_NAME ) )
			{
				// ���½�����ǰ����������field/value map
				floatFieldValueMap = new HashMap();
				return;
			}
			else if( eName.equals( FLOAT_ROW_ELE_NAME ) )
			{
				// ��ȡ������ID����ʶ��ǰ��ȡ������
				readFloatRow = true;
				floatRowID = attributes.getValue( FLOAT_ROW_ID_ATTR_NAME );
				return;
			}
			else if( eName.equals( UNIT_ELE_NAME ) )
			{
				unitID = attributes.getValue( UNIT_ID_ATTR_NAME );
				doStartUnit( unitID );
				return;
			}
			else if( eName.equals( ATTACHMENT_ELE_NAME ) )
			{
				attachmentName = attributes.getValue( ATTACHMENT_NAME_ATTR_NAME );
				if( log.isDebugEnabled() )
					log.debug("��ʼ���븽��" + attachmentName);
				try
				{
					doStartAttachment();
				}
				catch( SQLException ex )
				{
					ex.printStackTrace();
					log.error( "���渽������SQL�쳣", ex );
				throw new SAXException( "���渽������SQL�쳣", ex );
				}
			}
			else if( eName.equals(FLAG_ELE_NAME))
			{
				flagOper=attributes.getValue(FLAG_OPR_ATTR_NAME);
				try {
					doStartFlag();
				} catch (SQLException e) {
					log.error(e);
					throw new SAXException(e);
				}
			}
		}

		public void endElement( String uri, String localName, String qName )
			throws SAXException
		{
			if( isNewUnit && !allowNewUnit )
				return; // ��ʾ���µ�λ������ϵͳ���ò��ܵ����µ�λ

			String eName = qName; // ��ǰ��ȡԪ����
			if( !Util.isEmptyString( localName ) )
			{
				eName = localName;
			}

			if( log.isDebugEnabled() )
			{
				log.debug( "</" + eName + ">" );
			}

			try
			{
				if( eName.equals( CELL_ELE_NAME ) )
				{
					return;
				}
				else if( eName.equals( TABLE_ELE_NAME ) )
				{
					// do insert table data
					doEndTable();
					return;
				}
				else if( eName.equals( ROW_ELE_NAME ) )
				{
					// do with one line float row data
					doEndRow();
					return;
				}
				else if( eName.equals( FLOAT_ROW_ELE_NAME ) )
				{
					readFloatRow = false;
					return;
				}
				else if( eName.equals( UNIT_ELE_NAME ) )
				{
				}
				else if( eName.equals( ATTACHMENT_ELE_NAME ) )
				{
					doEndAttachment();
					if( log.isDebugEnabled() )
						log.debug("�ɹ����븽��" + attachmentName);

					//clear
					attachmentWriter = null;
					attachmentClob = null;
					attachmentName = null;
				}
				else if(eName.equalsIgnoreCase(FLAG_ELE_NAME)){
				}
			}
			catch( SQLException sqle )
			{
				log.error( "���������з���SQL�쳣", sqle );
				throw new SAXException( "���������з���SQL�쳣", sqle );
			}
			catch( IOException ex )
			{
				log.error( "���������з���IO�쳣", ex );
				throw new SAXException( "���������з���IO�쳣", ex );
			}
			catch( ConfigException ex )
			{
				log.error( "û����Ӧ�����ݿ�����", ex );
				throw new SAXException( "û����Ӧ�����ݿ�����", ex );
			}
		}

		/**
		 * ���渽��
		 * @param parm1
		 * @param parm2
		 * @param parm3
		 * @throws org.xml.sax.SAXException
		 */
		public void characters( char[] ch, int start, int length )
			throws SAXException
		{
			if( isNewUnit && !allowNewUnit )
				return; // ��ʾ���µ�λ������ϵͳ���ò��ܵ����µ�λ

			if (attachmentName != null && attachmentWriter != null)
			{
				attachmentWriter.write(ch, start, length);
			}
		}

		private void doStartUnit( String unitID )
		{
			isNewUnit = !treeMng.isUnitExist(unitID);
		}

		private void doStartAttachment()
			throws SQLException
		{
			PreparedStatement pst = null;

			try
			{
				String tableName = NameGenerator.generateAttachmentTableName(
					getTask().id() );
				String deleteSql = "DELETE FROM " + tableName +
								   " WHERE unitID = ? AND taskTimeID = ? "; ;
				String insertSql = "INSERT INTO " + tableName +
					" (unitID, taskTimeID, name, content) VALUES (?, ?, ?, ' ')";
				String selectSql = "SELECT content FROM " + tableName +
					" WHERE unitID = ? AND taskTimeID = ? FOR UPDATE";

				//ɾ�����и���
				pst = con.prepareStatement( deleteSql );
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				pst.execute();
				pst.close();

				//��������
				pst = con.prepareStatement( insertSql );
//				System.out.println("833=======>"+insertSql);
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				pst.setString( 3, attachmentName );
				pst.execute();
				pst.close();

				//ȡ������
				pst = con.prepareStatement( selectSql );
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				ResultSet rs = pst.executeQuery();

				//���¸���
				if( rs.next() )
				{
					attachmentClob = rs.getClob( 1 );
					attachmentWriter = new StringWriter();
				}
			} finally
			{
				SQLUtil.close( pst );
			}
		}

		/**
		 * ����������д��clob��
		 * @throws SQLException
		 * @throws ConfigException
		 * @throws IOException
		 */
		private void doEndAttachment()
			throws SQLException, ConfigException,
			IOException
		{
			//update clob
			String updateSql = "UPDATE " +
							   NameGenerator.generateAttachmentTableName(
				getTask().id() ) +
							   " SET content = ? WHERE unitID = '" + unitID +
							   "' AND taskTimeID = " +
							   getTaskTime().getTaskTimeID().intValue();
			attachmentWriter.close();
			Config.getCurrentDatabase().UpdateClob( attachmentClob,
				attachmentWriter.toString(),
				updateSql, con );
		}
		
		private void doStartFlag() throws SQLException
		{
//			PreparedStatement pst = null;
//	
//			try
//			{
//				String updateSql = "update ytapl_fillstate set flag=?,filldate=? WHERE unitID = ? and taskid=? AND taskTimeID = ? "; 
//				String insertSql = "INSERT INTO ytapl_fillstate (unitID,taskID,taskTimeID, filldate, flag) VALUES (?, ?, ?, ?,?)";
//				String selectSql = "SELECT * FROM ytapl_fillstate WHERE unitID = ? and taskID=? AND taskTimeID = ? ";
//				
//				boolean flgUpdate=false;
//				pst = con.prepareStatement( selectSql );
//				pst.setString( 1, unitID );
//				pst.setString(2,getTask().id());
//				pst.setInt( 3, getTaskTime().getTaskTimeID().intValue() );
//				ResultSet rs = pst.executeQuery();
//				if( rs.next() )
//				{
//					flgUpdate=!flgUpdate;
//				}
//				rs.close();
//				int flgvalue=DataStatus.REPORTED_UNENVLOP;
//				if(flagOper.equalsIgnoreCase("save")) flgvalue=DataStatus.UNREPORTED_UNENVLOP_UPLOAD;
//				if(flgUpdate){
//					pst = con.prepareStatement(updateSql);
//					pst.setInt( 1, flgvalue );
//					pst.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
//					pst.setString(3, unitID);
//					pst.setString(4, getTask().id());
//					pst.setInt( 5,getTaskTime().getTaskTimeID().intValue() );
//					pst.execute();
//					pst.close();
//				}else{
//					pst = con.prepareStatement(insertSql);
//					pst.setString( 1, unitID );
//					pst.setString(2,getTask().id());
//					pst.setInt( 3, getTaskTime().getTaskTimeID().intValue() );
//					pst.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
//					pst.setInt(5,flgvalue);
//					pst.executeUpdate();
//				}
//			} finally
//			{
//				SQLUtil.close( pst );
//			}
		}

		/**
		 * �����Ĵ���:
		 * 1, ����Ǹ��²���,��ôֱ�Ӹ���,������������;
		 * 2, ����ǲ������,��ôֱ�Ӳ���,��������ִ��;
		 *
		 * ��������:
		 * ��ɾ����Ӧ����,Ȼ�����;
		 * ��������ִ��.
		 * @throws SQLException
		 */
		private void doEndTable()
			throws SQLException
		{
			if( fieldValueMap == null )
				return;

			PreparedStatement pstmt = null;
			Map cellInfoMap = null;
			String unitMetaTable = getTask().getUnitMetaTable().id();
			boolean isUnitMetaTable = unitMetaTable.equals( tableID );

			if( isUnitMetaTable )
			{
				cellInfoMap = unitMetaTableCellInfo.tableCellInfoMap;
				if( isNewUnit )
				{
					// �µĵ�λ
					pstmt = insertUnitMetaTablePstmt;
				}
				else
				{
					updateUnitMetaTableData();
					return;
				}
			}
			else
			{
				// other table
				TablePreparedStatement tps =
							(TablePreparedStatement) otherTablePstmtMap.get( tableID );
				if( tps != null )
				{
					pstmt = tps.tablePstmt;
					cellInfoMap = ((TableCellInfo) otherTableCellInfoMap.get( tableID ))
								  .tableCellInfoMap;
				}
			}
			if( pstmt == null )
			{
				if( log.isWarnEnabled() )
					warnNoTableFound();
				return;
			}

			String dbTableName = NameGenerator.generateDataTableName( getTask().id(), tableID );
			deleteTableData( dbTableName, isUnitMetaTable );
			insertTableData(pstmt, cellInfoMap, fieldValueMap, isUnitMetaTable );
		}

		private void doEndRow()
			throws SQLException
		{
			if( floatFieldValueMap == null )
				return;

			PreparedStatement pstmt = null;
			Map cellInfoMap = null;
			TablePreparedStatement tps =
						 (TablePreparedStatement) otherTablePstmtMap.get( tableID );
			if( tps != null )
			{
				Map floatPstmtMap = tps.floatTablePstmt;
				if( floatPstmtMap != null )
					pstmt = (PreparedStatement) floatPstmtMap.get( floatRowID );

				cellInfoMap = (Map) ((TableCellInfo) otherTableCellInfoMap.get( tableID ))
								  .floatTableCellInfoMap.get( floatRowID );
			}

			if( pstmt == null )
			{
				warnNoRowFound();
				return;
			}

			String dbTableName = NameGenerator.generateFloatDataTableName( getTask().id(),
				tableID, floatRowID );
			deleteTableData( dbTableName, false );
			insertTableData( pstmt, cellInfoMap, floatFieldValueMap, false );
		}

		/**
		 * ���·����,
		 * @throws SQLException
		 */
		private void updateUnitMetaTableData()
			throws SQLException
		{
			UnitMetaTable metaTable = getTask().getUnitMetaTable();
			boolean canModifyFixCode = Config.getBoolean(
				"cn.com.youtong.apollo.loaddata.modify.fixcode", false);

			if (!canModifyFixCode)
			{
				// ���·����ʱ�������룬�ϼ����룬���Ŵ��룬P_PARENT�ֶβ����޸�
				// ����ҵ����
				Cell cell = metaTable.getUnitCodeCell();
				String name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("����ҵ�����ֶβ�����:" + name);
				}
				fieldValueMap.remove(name);

				// �ϼ���ҵ����
				cell = metaTable.getParentUnitCodeCell();
				name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("�ϼ���ҵ�����ֶβ�����:" + name);
				}
				fieldValueMap.remove(name);

				// ���Ŵ���
				cell = metaTable.getHQCodeCell();
				name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("���Ŵ����ֶβ�����:" + name);
				}
				fieldValueMap.remove(name);

				// P_PARENT
				// �����������P_PARENT�ֶα�����
				// 1, ��ǰ������P_PARENT; 2, ��ǰ���ڵ�P_PARENT��unitID������
				if (!modifyP_Parent(unitID))
				{
					fieldValueMap.remove(UnitMetaTable.P_PARENT);
					if (log.isDebugEnabled())
					{
						log.debug("P_PARENT�ֶβ�����:" + UnitMetaTable.P_PARENT);
					}
				}
			}

			Set fieldValueSet = fieldValueMap.entrySet();

			String dbTableName = NameGenerator.generateDataTableName(
						 getTask().id(),
						 metaTable.id() );
			StringBuffer sqlBuff = new StringBuffer( "UPDATE " )
								   .append( dbTableName );
			sqlBuff.append(" SET ");

			int position = 1;
			Map updateUnitMetaTableCellInfo = new HashMap(); // ���浱ǰ���µ��ֶΣ���Ӧ��λ�ú���Ϣ
			for (Iterator iter = fieldValueSet.iterator(); iter.hasNext(); )
			{
				Map.Entry fieldValue = (Map.Entry) iter.next();
				String field = (String) fieldValue.getKey();
				CellInfo info = (CellInfo) unitMetaTableCellInfo.tableCellInfoMap.get( field );

				if( info != null )
				{
					sqlBuff.append( field + "=?," );
					updateUnitMetaTableCellInfo.put( field,
						new CellInfo( position, info.sqlType ) );
					position++;
				}
			}
			sqlBuff.deleteCharAt(sqlBuff.length() - 1);
			sqlBuff.append(" WHERE unitID=?");

			String sql = sqlBuff.toString();
			if (log.isDebugEnabled())
			{
				log.debug("Update " + dbTableName + " SQL: " + sql);
			}

			// ��¼�ڱ���û�ж�����ֶ�
			boolean hasSome2Warn = false;
			StringBuffer buff = null;
			if( log.isWarnEnabled() )
			{
				buff = new StringBuffer(
								"�޷����������Ƭ��\n\tB E G I N\n" );
				buff.append( "��λ[unitID=" )
					.append( unitID )
					.append( "] ��[TableID=" )
					.append( tableID )
					.append( "] ������[taskID=" )
					.append( getTask().id() )
					.append( "] �����ֶ�û�ж���" )
					.append( "\n" );

			}

			updateUnitMetaTablePstmt = con.prepareStatement( sql );

			updateUnitMetaTablePstmt.setString( position, unitID );
			// set values
			for (Iterator iter = fieldValueSet.iterator(); iter.hasNext(); )
			{
				Map.Entry fieldValue = (Map.Entry) iter.next();
				String field = (String) fieldValue.getKey();
				String value = (String) fieldValue.getValue();

				CellInfo info = (CellInfo) updateUnitMetaTableCellInfo.get( field );
				if( info == null && log.isWarnEnabled() )
				{
					hasSome2Warn = true;
					buff.append( "<cell " )
						.append( FIELD_ATTR_NAME )
						.append( "=\"" )
						.append( field )
						.append( "\" " )
						.append( VALUE_ATTR_NAME )
						.append( "=\"" )
						.append( value )
						.append( "\"/>\n" );
				}
				else
				{
					SQLUtil.setParamValue( updateUnitMetaTablePstmt, info.position, info.sqlType,
										   value );
				}
			}

			updateUnitMetaTablePstmt.execute();
			// û��ѡ��finally����������close updateUnitMetaTablePstmt,
			// ��Ϊ���������һ��������pstmt������,��release��������,���᳢�Թرո�pstmt!
			updateUnitMetaTablePstmt.close();
			updateUnitMetaTablePstmt = null; // ˳���رպ�,��ֵnull,����release�����ٴιر�

			if( hasSome2Warn && log.isWarnEnabled() )
			{
				buff.append( "\tE N D" );
				log.warn( buff.toString() );
			}
		}

		/**
		 * �鿴P_Parent�ֶ��Ƿ�����޸�.
		 * �����жϰ취����API�ĵ�.
		 * @param unitID
		 * @return
		 */
		private boolean modifyP_Parent(String unitID)
		{
			boolean modifyP_Parent = false;
			try
			{
				UnitTreeNode node = treeMng.getUnitTree( unitID );
				String p_parent = node.getP_Parent();
				if( p_parent == null )
				{
					return true;
				}

				node = treeMng.getUnitTree( p_parent );

				// ���������ʾP_PARENT���ڶ�Ӧ�ĵ�λ
				return false;
			} catch( ModelException me )
			{
				return true;
			}
	}

		/**
		 * ���浥�����ݿ������
		 * @param pstmt                PreparedStatement
		 * @param cellInfoMap          ���ݿⶨ���ʽ
		 * @param fieldValueMap        ��Ҫ���������field/value map
		 * @param isUnitMetaTable      �Ƿ����������û��tasktimeid�ֶΣ�
		 * @throws SQLException
		 */
		private void insertTableData( PreparedStatement pstmt,
										Map cellInfoMap,
										Map fieldValueMap,
										boolean isUnitMetaTable )
			throws SQLException
		{
			// set all null
			for( Iterator iter = cellInfoMap.values().iterator();
								 iter.hasNext(); )
			{
				CellInfo info = (CellInfo) iter.next();
				pstmt.setNull( info.position, info.sqlType );
			}

			// set unitid and tasktimeid
			pstmt.setString( 1, unitID );
			if( !isUnitMetaTable )
				pstmt.setInt( 2,
							  getTaskTime().getTaskTimeID().intValue() );

			// ��¼�ڱ���û�ж�����ֶ�
			boolean hasSome2Warn = false;
			StringBuffer buff = null;
			if( log.isWarnEnabled() )
			{
				buff = new StringBuffer(
								"�޷����������Ƭ��\n\tB E G I N\n" );
				buff.append( "��λ[unitID=" )
					.append( unitID )
					.append( "] ��[TableID=" )
					.append( tableID )
					.append( "] ������[taskID=" )
					.append( getTask().id() );
				if( readFloatRow )
				{
					buff.append( "] floatRow ID=[")
						.append( floatRowID );
				}
				buff.append( "] �����ֶ�û�ж���" )
					.append( "\n" );

			}
			// ����fieldValueMap
			for( Iterator iter = fieldValueMap.entrySet().iterator();
								 iter.hasNext(); )
			{
				Map.Entry entry = (Map.Entry) iter.next();
				String field = (String) entry.getKey();
				String value = (String) entry.getValue();

				CellInfo info = (CellInfo) cellInfoMap.get( field );
				if( info == null && log.isWarnEnabled() )
				{
					hasSome2Warn = true;
					buff.append( "<cell " )
						.append( FIELD_ATTR_NAME )
						.append( "=\"" )
						.append( field )
						.append( "\" " )
						.append( VALUE_ATTR_NAME )
						.append( "=\"" )
						.append( value )
						.append( "\"/>\n" );
				}
				else
					SQLUtil.setParamValue( pstmt, info.position, info.sqlType, value );
			}

			if( hasSome2Warn && log.isWarnEnabled() )
			{
				buff.append( "\tE N D" );
				log.warn( buff.toString() );
			}
			pstmt.addBatch();
		}

		/**
		 * ɾ��dbTableName�����������ݡ�
		 * ֻ�ǰ�ɾ�������ӵ�Statement���棬һ�������ύ��
		 * ������ɾ�����ύ���⣬������clob��д������������������������ɾ����Ȼ������д�����ݿ⡣
		 * �������ύ�ڣ�store�������������棬û�е�����������
		 * <p>
		 * ��ص���˼�ǣ���ͬ��unitid����ͬ��tasktimeid��
		 * </p>
		 *
		 * @param dbTableName               ���ݿ������ƣ�����NameGenerator���������ݱ������
		 * @param isUnitMetaTable           �Ƿ�����Ŀǰ������ǲ�����߸��£����ԣ���ѡ��Ŀǰû��ʵ�����壩
		 * @throws SQLException
		 */
		private void deleteTableData( String dbTableName, boolean isUnitMetaTable )
			throws SQLException
		{
			StringBuffer buff = new StringBuffer( "DELETE FROM " );
			buff.append( dbTableName );
			buff.append( " WHERE UNITID='" )
				.append( unitID )
				.append( "'" );
			if( !isUnitMetaTable )
				buff.append( " AND TASKTIMEID='" )
					.append( getTaskTime().getTaskTimeID().intValue() )
					.append( "'" );
			String deleteSQL = buff.toString();
			if( log.isDebugEnabled() )
			{
				log.debug( "Delete "
						  + dbTableName
						  + " SQL: "
						  + deleteSQL );
			}
			deleteTabelStmt.addBatch( deleteSQL );
		}

		/**
		 * ��ʾ��û������������û���ҵ�Ҫ�����Ķ��塣
		 */
		private void warnNoTableFound()
		{
			StringBuffer buff = new StringBuffer(
						 "�޷����������Ƭ��\n\tB E G I N\n" );
			buff.append( "��λ[unitID=" )
				.append( unitID )
				.append( "] ��[TableID=" )
				.append( tableID )
				.append( "] ������[taskID=" )
				.append( getTask().id() )
				.append( "] û�ж���" )
				.append( "\n" );

			buff.append( "����Ƭ��:\n" );
			for( Iterator iter = fieldValueMap.entrySet().iterator();
								 iter.hasNext(); )
			{
				Map.Entry entry = ( Map.Entry ) iter.next();
				buff.append( "<cell " )
						.append( FIELD_ATTR_NAME )
						.append( "=\"" )
						.append( entry.getKey() )
						.append( "\" " )
						.append( VALUE_ATTR_NAME )
						.append( "=\"" )
						.append( entry.getValue() )
						.append( "\"/>\n" );

			}
			buff.append( "\tE N D" );
			log.warn( buff.toString() );
		}

		/**
		 * ��ʾ����ǰ����û���ҵ��ø����еĶ���
		 */
		private void warnNoRowFound()
		{
			StringBuffer buff = new StringBuffer(
				"�޷����������Ƭ��\n\tB E G I N" );
			buff.append( "��λ[unitID=" )
				.append( unitID )
				.append( "] ������[floatRow ID=" )
				.append( "]" )
				.append( " �ڱ�[TableID=" )
				.append( tableID )
				.append( "] ������[taskID=" )
				.append( getTask().id() )
				.append( "] û�ж���" )
				.append( "\n" );

			buff.append( "����Ƭ��:\n" );
			for( Iterator iter = fieldValueMap.entrySet().iterator();
								 iter.hasNext(); )
			{
				Map.Entry entry = ( Map.Entry ) iter.next();
				buff.append( "<cell " )
						.append( FIELD_ATTR_NAME )
						.append( "=\"" )
						.append( entry.getKey() )
						.append( "\" " )
						.append( VALUE_ATTR_NAME )
						.append( "=\"" )
						.append( entry.getValue() )
						.append( "\"/>\n" );

			}
			buff.append( "\tE N D" );
			log.warn( buff.toString() );
		}
	}

	private class CellInfo
	{
		/** ���ֶΣ���Ӧ�����ݱ��е�λ�ã���1��ʼ�� */
		public int position;
		/** ���ֶΣ���Ӧ��SQL ����ֵ */
		public int sqlType;

		public CellInfo( int pos, int type )
		{
			this.position = pos;
			this.sqlType = type;
		}
	}

	private class TableCellInfo
	{
		/**
		 * key/value=dbField/CellInfo
		 * ����е�CellInfo������Ϊnull
		 */
		public Map tableCellInfoMap;
		/**
		 * key/value=floatRowID[java.lang.String]/Map
		 * where Map: key/value=dbField/CellInfo
		 * ��ĸ����е�TableCellInfo Map������Ϊ��
		 */
		public Map floatTableCellInfoMap;
		public TableCellInfo(
				  Map tableCellInfoMap,
				  Map floatTableCellInfoMap )
		{
			this.tableCellInfoMap = tableCellInfoMap;
			this.floatTableCellInfoMap = floatTableCellInfoMap;
		}
	}

	private class TablePreparedStatement
	{
		/** �ñ��Ӧ�Ĳ�������PreparedStatement������Ϊ��*/
		public PreparedStatement tablePstmt;
		/** �ñ��Ӧ�ĸ������������PreparedStatement
		 * key/value = floatID[java.lang.String]/pstmt[java.sql.PreparedStatement]
		 * ����Ϊ��
		 */
		public Map floatTablePstmt;
		public TablePreparedStatement(
				  PreparedStatement tablePstmt,
				  Map floatTablePstmt )
		{
			this.tablePstmt = tablePstmt;
			this.floatTablePstmt = floatTablePstmt;
		}
	}
}
