/*
 * Created on 2003-11-5
 */
package cn.com.youtong.apollo.data.form;

import java.util.*;

/**
 * ����Ϣ����������Լ���������ɵı�����
 * �ֶ���Ϣ��DBFieldName�������ݿ�洢��˳Map���ֶ�����Map�������������java.sql.Types����Ϊ׼��
 * ��������Ϣ��Ҳ�Ǳ���Ϣ��HashMap��key/valueֵ�� = ID/TableInfo��
 * 
 * @see cn.com.youtong.apollo.common.sql.NameGenerator
 * @author wjb
 */
public class TableInfo {
	/** �ų������еĵ�Ԫ�� DBFieldName��Ӧ������������ֶ�˳�� */
	private Map cellIndexMap;

	/** �ų������еĵ�Ԫ�� DBFieldName��Ӧ�ĵ�Ԫ������(java.sql.Types������) */
	private Map cellTypeMap;

	/** ���� (����Լ����������ı���) */
	private String tableName;

	/** ��Ӧ������Ƿ���ڣ�������Щ��ֻ���и�������������Ҫ������� */
	private boolean exist;

	/**
	 * �����ж�Ӧ�ı���Ϣ key/value = (row)ID/TableInfo ���б����ǰ�װԼ����������ı���
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
	 * ������Ƿ���ڣ���Ϊ��Щ��ֻ���и�������û�������
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
