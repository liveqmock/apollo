package cn.com.youtong.apollo.task;

/**
 * ��Ԫ����������.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
public interface CellDataType
{
	/** ��ֵ�� */
	static final int TYPE_NUMERIC = 1;
	/** �ı��� */
	static final int TYPE_TEXT = 2;
	/** ������ */
	static final int TYPE_BLOB = 3;
	/** ���ı� */
	static final int TYPE_CLOB = 4;
	/** ������ */
	static final int TYPE_DATE = 5;
}