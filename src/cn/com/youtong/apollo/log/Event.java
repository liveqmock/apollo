package cn.com.youtong.apollo.log;

import java.util.*;

/**
 * �¼��ӿ�
 */
public interface Event
{
	/**
	 * ��������
	 */
	public final int ERROR = 0;

	/**
	 * ��������
	 */
	public final int WARNING = 1;

	/**
	 * ��Ϣ����
	 */
	public final int INFO = 2;

	/**
	 * �ɹ��������
	 */
	public final int AUDIT_SUCCESS = 3;

	/**
	 * ʧ���������
	 */
	public final int AUDIT_FAIL = 4;

	/**
	 * �õ��¼�����
	 * @return �¼�����
	 */
	public int getType();

	/**
	 * �õ��¼�������ʱ��
	 * @return �¼�������ʱ��
	 */
	public Date getTimeOccured();

	/**
	 * �õ������¼��Ŀͻ��������IP��ַ
	 * @return �����¼��Ŀͻ��������IP��ַ
	 */
	public String getSource();

	/**
	 * �õ������¼��ĵ�¼�û�������
	 * @return �����¼��ĵ�¼�û�������
	 */
	public String getUserName();

	/**
	 * �õ��¼�ID��ֵԽ��ʱ�䷢����ʱ��Խ��
	 * @return �¼�ID
	 */
	public Integer getEventID();

	/**
	 * �õ��¼�����ϸ����
	 * @return �¼�����ϸ����
	 */
	public String getMemo();
}