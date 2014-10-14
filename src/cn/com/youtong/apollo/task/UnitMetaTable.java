package cn.com.youtong.apollo.task;

/**
 * �û�ԭ���ݱ�.
 * �Ƿ�������
 * <ul> ������Ϣ
 * <li>1��	��ͨ�����������
 * <li>2��	��λ����
 * <li>3��	��������
 * <li>4��	�ϼ���λ����
 * <li>5.   ���Ŵ���
 * <li>6��	��λ����
 * </ul>
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ����������ͨ�Ƽ����޹�˾</p>
 * @author zhou
 * @version 1.0
 */
public interface UnitMetaTable extends Table
{
	/**
	 * �����ϼ���λid�ֶ�
	 */
	public final static String P_PARENT = "P_PARENT";

	/**
	 * ��λ���뵥Ԫ��
	 * @return
	 */
	Cell getUnitCodeCell();

	/**
	 * �������͵�Ԫ��
	 * @return
	 */Cell getReportTypeCell();

	/**
	 * �ϼ���λ���뵥Ԫ��
	 * @return
	 */Cell getParentUnitCodeCell();

	/**
	 * �����ܲ����뵥Ԫ��
	 * @return
	 */Cell getHQCodeCell();

	/**
	 * ��λ���Ƶ�Ԫ��
	 * @return
	 */Cell getUnitNameCell();
	 
	 /**
	  * �Ƿ���ʾ
	  */
	 Cell getDisplayCell();
}