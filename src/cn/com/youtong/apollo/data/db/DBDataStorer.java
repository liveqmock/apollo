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
 * 不考虑权限，不考虑向导入者发送消息等操作情况下，将任务数据导入数据库。
 * 也没有脚本计算.
 *
 * storer方法一次只能导入一个单位的数据.
 * <b>调用了多次store方法后,一定要调用commit方法!</b>
 * <p>
 * 导入数据之前，必须调用{@link #init()}方法。
 * 提交保存结果调用{@link #commit()}方法。
 * 使用完毕调用{@link release()}方法释放资源。
 * </p>
 *
 * <p>
 * 导入封面表<br/>
 * 封面表里面的数据可以更新，但是固定字段（本代码，上级代码，集团代码，P_PARENT字段），
 * 一般情况下，不被修改。
 * <br/>
 * 设置配置文件cn.com.youtong.apollo.loaddata.modify.fixcode=true，那么可以修改固定字段。
 * Note：修改这些固定字段将导致单位树的变动。
 * <br/>
 * 以下两种情况P_PARENT字段被更新
 * <ul>
 *     <li>以前不存在P_PARENT;</li>
 *     <li>以前存在的P_PARENT的unitID不存在</li>
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
	/** 数据库连接 */
	private Connection con;
	private SAXParser parser;
	private DefaultHandler handler;

	private DBUnitTreeManager treeMng;

	/** 更新封面表 pstmt */
	private PreparedStatement updateUnitMetaTablePstmt;
	/** 插入封面表 pstmt */
	private PreparedStatement insertUnitMetaTablePstmt;
	/**
	* key/value=tableID[java.lang.String]/tablePstmt[TablePreparedStatement]
	 * 封面表除外的插入语句PreparedStatement
	 */
	private Map otherTablePstmtMap;
	private Statement deleteTabelStmt;

	private TableCellInfo unitMetaTableCellInfo;
	/** key/value=tableID[java.lang.String]/tableCellInfo[TableCellInfo]
	 *  不包含封面表的相关信息
	 */
	private Map otherTableCellInfoMap;
	/** 标识是否完成了init方法 */
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
	 * 切换一个任务时间
	 * @param taskTime
	 */
	public void setTaskTime( TaskTime taskTime )
	{
		this.taskTime = taskTime;
	}

	/**
	 * 当前处理的是否是新单位。
	 * 注意，导入新单位数据的前提条件是配置文件中
	 * cn.com.youtong.apollo.data.import.loadnewunit属性值为true。
	 * @return  是否是新单位
	 */
	public boolean isNewUnit()
	{
		return isNewUnit;
	}

	/**
	 * 在DBDataStorer完成初始化{@link init()}后，可以导入数据。
	 * storer方法一次只能导入一个单位的数据.
	 * 导入数据，并没有马上就永久性的导入了数据库。
	 * 必须调用commit方法，才永久性导入了数据库。
	 * "导入"理解为"解析",可能更确切些.
	 *
	 * @param in                    数据流，形如：
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
	 * @throws ModelException        导入过程发生异常
	 */
	public void store( InputStream in )
		throws ModelException
	{
		if( !inited )
			throw new ModelException( "没有完成初始化过程" );

		log.debug( "开始保存数据" );
		if( tx == null )
		{
			try
			{
				tx = session.beginTransaction();
			}
			catch( HibernateException he )
			{
				log.error( "启动事务出错", he );
				throw new ModelException( "启动事务出错", he );
			}
		}

		try
		{
			parser.parse( in, handler );
		}
		catch( java.io.IOException ex )
		{
			log.error( "IO异常", ex );
			throw new ModelException( "IO异常", ex );
		}
		catch( org.xml.sax.SAXException se )
		{
			log.error( "SAX解析异常", se );
			throw new ModelException( "SAX解析异常", se );
		}
		log.debug( "保存数据完成" );
	}

	/**
	 * 初始化配置。为导入数据做好准备。
	 * 需要提供DBUnitTreeManager实例。
	 *
	 * <p>
	 * 实现的任务有：
	 * <ul>
	 *    <li>或者数据库连接</li>
	 *    <li>初始化封面表的插入PreparedStatement</li>
	 *    <li>其他表的插入PreparedStatement</li>
	 *    <li>封面表的TableCellInfo</li>
	 *    <li>其他表的TableCellInfo</li>
	 *    <li>SAX解析器</li>
	 *    <li>解析器的Handler</li>
	 * </ul>
	 * </p>
	 * @param configuration Map  key/value的约定为instance.class.getName()/
	 *                                   instance
	 * @throws ModelException
	 */
	public void init( Map configuration )
		throws ModelException
	{
		log.info( "开始初始化" );
		try
		{
			// 查看DBUnitTreeManager实例是否存在
			if( configuration == null )
				throw new ModelException( "缺少DBUnitTreeManager实例" );

			Object obj = configuration.get( DBUnitTreeManager.class.getName() );
			if( obj == null || !(obj instanceof DBUnitTreeManager) )
				throw new ModelException( "缺少DBUnitTreeManager实例" );

			this.treeMng = (DBUnitTreeManager) obj;

			session = HibernateUtil.getSessionFactory().openSession();
			con = session.connection();

			// 初始化表相关PreparedStatement和TableCellInfo
			initTableInfos();

			// 初始化SAXParser
			parser = SAXParserFactory.newInstance().newSAXParser();
			// 初始化DefaultHandler
			handler = new InnerHandler();
		}
		catch( ModelException me )
		{
			throw me;
		}
		catch( Exception ex )
		{
			log.error( "DBDataStorer不能正常启动", ex );
			throw new ModelException( "DBDataStorer不能正常启动", ex );
		}
		log.info( "初始化完成" );
		inited = true;
	}

	/**
	 * commit方法。
	 * 向数据库写入数据，以便永久性保存。
	 * @throws ModelException
	 */
	public void commit() throws ModelException
	{
		log.debug( "开始提交事务" );
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

			tx = null; // 赋值null,以便再次保存的时候，开启新的transaction
			log.debug( "提交事务完成" );
		}
		catch( HibernateException he )
		{
			log.error( "提交事务出错", he );
			log.debug( "提交不能顺利完成" );
			throw new ModelException( "提交事务出错", he );
		}
		catch( SQLException sqle )
		{
			log.error( "保存数据时发生SQL异常", sqle );
			log.debug( "提交不能顺利完成" );
			throw new ModelException( "保存数据时发生SQL异常", sqle );
		}
	}

	/**
	 * 释放开启的资源。
	 * 在程序的finally里面，释放资源是个良好的编程习惯。
	 */
	public void release()
	{
		log.info( "开始释放资源" );

		// 如果还有事务被开启,提交
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
		log.info( "资源释放完毕" );
	}

	/**
	 * 初始化表插入语句的PreparedStatement以及TableCellInfo
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
	 * 初始化封面表的插入preparedstatement 和 TableCellInfo
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
	 * 初始化封面表除外其他表的插入preparedstatement 和 TableCellInfo
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
			log.error( "读取任务信息出错", te );
			throw new SQLException( "读取任务信息出错" );
		}
		while( iter.hasNext() )
		{
			Table table = (Table) iter.next();
			String tableID = table.id();
			if( !tableID.equals( unitMetaTableID ) )
			{
				// 不是封面表
				// pstmt
				PreparedStatement insertPstmt = null;
				Map floatInsertPstmtMap = null;

				// cell info
				Map cellInfoMap = null;
				Map floatCellInfoMap = null;
				boolean containAllFloatRow = true; // 该表全部由浮动行组成
				for( Iterator rowIter = table.getRows(); rowIter.hasNext(); )
				{
					Row row = (Row) rowIter.next();
					// 判断是否为浮动行
					if( row.getFlag( Row.FLAG_FLOAT_ROW ) )
					{
						// 浮动行
						if( floatInsertPstmtMap == null )
						{
							// 当前第一次遇到浮动行
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
	 * 给定数据表，返回该表的插入语句的PreparedStatement和CellInfoMap
	 * @param dbTableName 数据库表格名称
	 * @throws SQLException
	 * @return Object[]  [0]插入语句的PreparedStatement，
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

			// 赋值插入封面表的PreparedStatement
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
	 * {@link DBDataStorer#storer()}方法，使用InnerHandler解析XML数据。
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
		private static final String FLAG_ELE_NAME = "flag";        //客户端上报前保存为草稿的标志
		private static final String FLAG_OPR_ATTR_NAME="operation";//客户端上报前保存为草稿的标志值

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
				return; // 表示是新单位，但是系统设置不能导入新单位

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

			if( eName.equals( CELL_ELE_NAME ) )
			{
				// 添加到相应的field/value map，直到row或者table标签处理
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
				// 读取tableID，初始化表需要的field/value map
				tableID = attributes.getValue( TABLE_ID_ATTR_NAME );
				fieldValueMap = new HashMap();
				return;
			}
			else if( eName.equals( ROW_ELE_NAME ) )
			{
				// 重新建立当前浮动行数据field/value map
				floatFieldValueMap = new HashMap();
				return;
			}
			else if( eName.equals( FLOAT_ROW_ELE_NAME ) )
			{
				// 读取浮动行ID，标识当前读取浮动行
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
					log.debug("开始导入附件" + attachmentName);
				try
				{
					doStartAttachment();
				}
				catch( SQLException ex )
				{
					ex.printStackTrace();
					log.error( "保存附件发生SQL异常", ex );
				throw new SAXException( "保存附件发生SQL异常", ex );
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
				return; // 表示是新单位，但是系统设置不能导入新单位

			String eName = qName; // 当前读取元素名
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
						log.debug("成功导入附件" + attachmentName);

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
				log.error( "解析过程中发生SQL异常", sqle );
				throw new SAXException( "解析过程中发生SQL异常", sqle );
			}
			catch( IOException ex )
			{
				log.error( "解析过程中发生IO异常", ex );
				throw new SAXException( "解析过程中发生IO异常", ex );
			}
			catch( ConfigException ex )
			{
				log.error( "没有相应的数据库配置", ex );
				throw new SAXException( "没有相应的数据库配置", ex );
			}
		}

		/**
		 * 保存附件
		 * @param parm1
		 * @param parm2
		 * @param parm3
		 * @throws org.xml.sax.SAXException
		 */
		public void characters( char[] ch, int start, int length )
			throws SAXException
		{
			if( isNewUnit && !allowNewUnit )
				return; // 表示是新单位，但是系统设置不能导入新单位

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

				//删除已有附件
				pst = con.prepareStatement( deleteSql );
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				pst.execute();
				pst.close();

				//创建附件
				pst = con.prepareStatement( insertSql );
//				System.out.println("833=======>"+insertSql);
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				pst.setString( 3, attachmentName );
				pst.execute();
				pst.close();

				//取出附件
				pst = con.prepareStatement( selectSql );
				pst.setString( 1, unitID );
				pst.setInt( 2, getTaskTime().getTaskTimeID().intValue() );
				ResultSet rs = pst.executeQuery();

				//更新附件
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
		 * 将附件内容写入clob流
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
		 * 封面表的处理:
		 * 1, 如果是更新操作,那么直接更新,不是批量操作;
		 * 2, 如果是插入操作,那么直接插入,但是批量执行;
		 *
		 * 其他表处理:
		 * 先删除对应数据,然后插入;
		 * 都是批量执行.
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
					// 新的单位
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
		 * 更新封面表,
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
				// 更新封面表时，本代码，上级代码，集团代码，P_PARENT字段不能修改
				// 本企业代码
				Cell cell = metaTable.getUnitCodeCell();
				String name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("本企业代码字段不更新:" + name);
				}
				fieldValueMap.remove(name);

				// 上级企业代码
				cell = metaTable.getParentUnitCodeCell();
				name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("上级企业代码字段不更新:" + name);
				}
				fieldValueMap.remove(name);

				// 集团代码
				cell = metaTable.getHQCodeCell();
				name = cell.getDBFieldName();
				if (log.isDebugEnabled())
				{
					log.debug("集团代码字段不更新:" + name);
				}
				fieldValueMap.remove(name);

				// P_PARENT
				// 以下两种情况P_PARENT字段被更新
				// 1, 以前不存在P_PARENT; 2, 以前存在的P_PARENT的unitID不存在
				if (!modifyP_Parent(unitID))
				{
					fieldValueMap.remove(UnitMetaTable.P_PARENT);
					if (log.isDebugEnabled())
					{
						log.debug("P_PARENT字段不更新:" + UnitMetaTable.P_PARENT);
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
			Map updateUnitMetaTableCellInfo = new HashMap(); // 保存当前更新的字段，对应的位置和信息
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

			// 记录在表中没有定义的字段
			boolean hasSome2Warn = false;
			StringBuffer buff = null;
			if( log.isWarnEnabled() )
			{
				buff = new StringBuffer(
								"无法导入的数据片断\n\tB E G I N\n" );
				buff.append( "单位[unitID=" )
					.append( unitID )
					.append( "] 表[TableID=" )
					.append( tableID )
					.append( "] 在任务[taskID=" )
					.append( getTask().id() )
					.append( "] 以下字段没有定义" )
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
			// 没有选择finally方法体里面close updateUnitMetaTablePstmt,
			// 因为类变量保存一个这样的pstmt的引用,在release方法里面,还会尝试关闭该pstmt!
			updateUnitMetaTablePstmt.close();
			updateUnitMetaTablePstmt = null; // 顺利关闭后,赋值null,避免release方法再次关闭

			if( hasSome2Warn && log.isWarnEnabled() )
			{
				buff.append( "\tE N D" );
				log.warn( buff.toString() );
			}
		}

		/**
		 * 查看P_Parent字段是否可以修改.
		 * 具体判断办法见类API文档.
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

				// 到了这里表示P_PARENT存在对应的单位
				return false;
			} catch( ModelException me )
			{
				return true;
			}
	}

		/**
		 * 保存单张数据库表数据
		 * @param pstmt                PreparedStatement
		 * @param cellInfoMap          数据库定义格式
		 * @param fieldValueMap        需要保存的数据field/value map
		 * @param isUnitMetaTable      是否封面表（封面表没有tasktimeid字段）
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

			// 记录在表中没有定义的字段
			boolean hasSome2Warn = false;
			StringBuffer buff = null;
			if( log.isWarnEnabled() )
			{
				buff = new StringBuffer(
								"无法导入的数据片断\n\tB E G I N\n" );
				buff.append( "单位[unitID=" )
					.append( unitID )
					.append( "] 表[TableID=" )
					.append( tableID )
					.append( "] 在任务[taskID=" )
					.append( getTask().id() );
				if( readFloatRow )
				{
					buff.append( "] floatRow ID=[")
						.append( floatRowID );
				}
				buff.append( "] 以下字段没有定义" )
					.append( "\n" );

			}
			// 遍历fieldValueMap
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
		 * 删除dbTableName里面的相关数据。
		 * 只是把删除语句添加到Statement里面，一起批量提交。
		 * 附件的删除和提交除外，附件的clob是写数据流，所以是遇到附件先删除，然后马上写如数据库。
		 * 附件的提交在，store开启的事务里面，没有单独开启事务。
		 * <p>
		 * 相关的意思是，相同的unitid和相同的tasktimeid。
		 * </p>
		 *
		 * @param dbTableName               数据库表格名称，经过NameGenerator处理后的数据表格名称
		 * @param isUnitMetaTable           是否封面表（目前封面表都是插入或者更新，所以，该选项目前没有实际意义）
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
		 * 警示，没有在任务定义中没有找到要导入表的定义。
		 */
		private void warnNoTableFound()
		{
			StringBuffer buff = new StringBuffer(
						 "无法导入的数据片断\n\tB E G I N\n" );
			buff.append( "单位[unitID=" )
				.append( unitID )
				.append( "] 表[TableID=" )
				.append( tableID )
				.append( "] 在任务[taskID=" )
				.append( getTask().id() )
				.append( "] 没有定义" )
				.append( "\n" );

			buff.append( "数据片断:\n" );
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
		 * 警示，当前表中没有找到该浮动行的定义
		 */
		private void warnNoRowFound()
		{
			StringBuffer buff = new StringBuffer(
				"无法导入的数据片断\n\tB E G I N" );
			buff.append( "单位[unitID=" )
				.append( unitID )
				.append( "] 浮动行[floatRow ID=" )
				.append( "]" )
				.append( " 在表[TableID=" )
				.append( tableID )
				.append( "] 在任务[taskID=" )
				.append( getTask().id() )
				.append( "] 没有定义" )
				.append( "\n" );

			buff.append( "数据片断:\n" );
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
		/** 该字段，对应在数据表中的位置，从1开始数 */
		public int position;
		/** 该字段，对应的SQL 类型值 */
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
		 * 表具有的CellInfo，可以为null
		 */
		public Map tableCellInfoMap;
		/**
		 * key/value=floatRowID[java.lang.String]/Map
		 * where Map: key/value=dbField/CellInfo
		 * 表的浮动行的TableCellInfo Map，可以为空
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
		/** 该表对应的插入语句的PreparedStatement，可以为空*/
		public PreparedStatement tablePstmt;
		/** 该表对应的浮动表插入语句的PreparedStatement
		 * key/value = floatID[java.lang.String]/pstmt[java.sql.PreparedStatement]
		 * 可以为空
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
