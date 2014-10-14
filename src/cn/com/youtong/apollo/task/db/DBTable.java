/*
 * 表格管理
 * Created on 2003-10-20
 */
package cn.com.youtong.apollo.task.db;

import java.util.*;

import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.task.db.form.*;

/**
 * @author wjb
 */
public class DBTable
	implements Table
{
	// key/value=label[java.lang.String]/dbfield[java.lang.String]
	private Map _label2dbfieldMap;
	// key/value=dbfield[java.lang.String]/cell[cn.com.youtong.apollo.task.Cell]
	private Map _dbfield2cellMap;
	//rows[cn.com.youtong.apollo.task.Row]
	private Collection _rows;
	//views[cn.com.youtong.apollo.task.TableView]
	private Collection _views;

	private String _id;
	private Integer _tableID;
	private int _flag;
	private String _name;
	/**
	 * 构造函数
	 * @param tableForm	form of hibernate
	 */
	public DBTable( TableForm tableForm )
	{
		_id = tableForm.getID();
		_tableID = tableForm.getTableID();
		_flag = tableForm.getFlag();
		_name = tableForm.getName();

		// 初始化dbfield2cellMap, label2dbfieldMap
		_dbfield2cellMap = new HashMap();
		_label2dbfieldMap = new HashMap();
		_rows = new LinkedList();

		Set rows = tableForm.getRows();
		Iterator rowIter = rows.iterator();
		while( rowIter.hasNext() )
		{
			RowForm rowForm = ( RowForm ) rowIter.next();
			// add row to rows
			_rows.add( new DBRow( rowForm ) );

			// put into dbfield2cell & label2dbfield map
			Set cellSet = (  rowForm ).getCells();
			Iterator cellIter = cellSet.iterator();
			while( cellIter.hasNext() )
			{
				CellForm cellForm = ( CellForm ) cellIter.next();

				String dbfieldName = cellForm.getDbfieldName().toUpperCase();
				_dbfield2cellMap.put( dbfieldName,
									 new DBCell( cellForm ) );
				_label2dbfieldMap.put( cellForm.getLabel().toUpperCase(),
									  dbfieldName );
			}
		}

		// table views
		_views = new LinkedList();
		Iterator iter = tableForm.getTableViews().iterator();
		while( iter.hasNext() )
		{
			DBTableView view = new DBTableView( ( TableViewForm ) iter.next() );
			_views.add( view );
		}
	}

	/**
	 * 得到Label -- DBFieldName 的Map
	 * Label和DBFieldName都是大写
	 */
	public Map getLabel2dbfieldMap()
	{
		return _label2dbfieldMap;
	}

	/**
	 * 得到表名
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * 得到标识
	 */
	public boolean getFlag( int flag )
	{
		boolean ret = false;
		if( ( this._flag & flag ) > 0 )
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * 得到此表所有的单元格
	 */
	public Iterator getAllCells()
	{
		return _dbfield2cellMap.values().iterator();
	}

	/**
	 * 根据标签名称得到所对应的单元格接口
	 */
	public Cell getCellByLabel( String label )
	{
		Object field = _label2dbfieldMap.get( label );
		if( field == null )
			return null;

		return( Cell ) _dbfield2cellMap.get( field );
	}

	/**
	 * 根据字段名称得到所对应的单元格接口
	 */
	public Cell getCellByDBFieldName( String fieldName )
	{
		return ( Cell ) _dbfield2cellMap.get( fieldName );
	}

	/**
	 * 得到此表所有的行
	 */
	public Iterator getRows()
	{
		return _rows.iterator();
	}

	/**
	 * 得到此表所有的视图
	 */
	public Iterator getViews()
	{
		return _views.iterator();
	}

	/**
	 * 得到表ID
	 */
	public String id()
	{
		return _id;
	}

	/**
	 * get table integer ID
	 * @return table integer ID
	 */
	public Integer getTableID()
	{
		return _tableID;
	}

}