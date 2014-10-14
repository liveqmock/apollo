package cn.com.youtong.apollo.task;

/**
 * <p>Title: ��Ԫ��</p>
 * <p>Description:
 * <ul>��������
 * <li>1��	�������ͣ���ֵ���ı������ı���clob�������ڣ�������(blob)
 * <li>2��	���ݿ��ֶ�����
 * <li>3��	�������
 * <li>4��	���ָ�꣨Ԫ����Ԫת����
 * <li>5��	����ָ��
 * </ul>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:����������ͨ�Ƽ����޹�˾ </p>
 * @author zhou
 * @version 1.0
 */
public interface Cell extends CellDataType
{
	/** �Ƿ���� */
	static final int FLAG_SUM = 0x00000001;
	/** �Ƿ������ת�� */
	static final int FLAG_MONEY = 0x00000002;

	/**
	 * ��������.
	 * @see CellDataType
	 * @return
	 */
	int getDataType();

    /**
     * ��ǩ
     * @return ��ǩ
     */
    String getLabel();

	/**
	 * ���ݿ��ֶ�����.
	 * @return
	 */
	String getDBFieldName();

	/**
	 * ��Ԫ���־
	 * @param flag
	 * @return
	 */boolean getFlag(int flag);

	/**
	 * ȡ��Ԫ���Ӧ�Ĵ����ֵ�
	 * @return
	 */
	String getDictionaryID();

	/**
	 * �û��ɶ������֣��磺"�����ʽ� ������"
	 * @return
	 */String getReadableName();
}