/*
 * Created on 2003-11-5
 */
package cn.com.youtong.apollo.data.form;

import java.util.*;

/**
 * 表信息，包含经过约定规则生成的表名；
 * 字段信息（DBFieldName和在数据库存储的顺Map和字段类型Map，这里的类型是java.sql.Types描述为准）
 * 浮动表信息，也是表信息（HashMap，key/value值对 = ID/TableInfo）
 * 
 * @see cn.com.youtong.apollo.common.sql.NameGenerator
 * @author wjb
 */
public class TableInfo {
	/** 排除浮动行的单元格 DBFieldName对应在数据里面的字段顺序 */
	private Map cellIndexMap;

	/** 排除浮动行的单元格 DBFieldName对应的单元格类型(java.sql.Types描述的) */
	private Map cellTypeMap;

	/** 表名 (经过约定规则产生的表名) */
	private String tableName;

	/** 对应这个表是否存在，比如有些表只含有浮动表，而本身不需要建立表格 */
	private boolean exist;

	/**
	 * 浮动行对应的表信息 key/value = (row)ID/TableInfo 其中表名是安装约定规则产生的表名
	 * 
	 * @see cn.com.youtong.apollo.common.sql.NameGenerator
	 */

	private HashMap floatTables;

	/**
	 * @return Returns the tableName.
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            The tableName to set.
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return Returns the floatTables.
	 */
	public HashMap getFloatTables() {
		return floatTables;
	}

	/**
	 * @param floatTables
	 *            The floatTables to set.
	 */
	public void setFloatTables(HashMap floatTables) {
		this.floatTables = floatTables;
	}

	public Map getCellIndex() {
		return cellIndexMap;
	}

	public Map getCellType() {
		return cellTypeMap;
	}

	public void setCellIndex(Map cellIndex) {
		this.cellIndexMap = cellIndex;
	}

	public void setCellType(Map cellType) {
		this.cellTypeMap = cellType;
	}

	/**
	 * 本身表是否存在，因为有些表只含有浮动表，而没有自身表
	 * 
	 * @return
	 */
	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
}
