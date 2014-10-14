package cn.com.youtong.apollo.log;

import java.util.*;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.usermanager.*;

/**
 * log�������ӿ�
 */
public interface LogManager
{
	/**
	 * �����ϱ���ʽ -- ����ֱ��
	 */
	public static final int WEB_MODE = 0;

	/**
	 * �����ϱ���ʽ -- �ͻ���ֱ��
	 */
	public static final int CLIENT_MODE = 1;

	/**
	 * �����ϱ���ʽ -- �ʼ����ļ����ϱ�
	 */
	public static final int MAIL_MODE = 2;

	/**
	 * ��¼��ȫ��־
	 * @param timeOccured �¼�������ʱ��
	 * @param type �¼�����
	 * @param source �����¼��Ŀͻ��������IP��ַ
	 * @param userName �����¼��ĵ�¼�û�������
	 * @param memo �¼�����ϸ����
	 * @throws LogException
	 */
	public void logSecurityEvent(Date timeOccured, int type, String source,
								 String userName, String memo) throws
		LogException;

	/**
	 * ��¼������־
	 * @param timeOccured �¼�������ʱ��
	 * @param type �¼�����
	 * @param source �����¼��Ŀͻ��������IP��ַ
	 * @param userName �����¼��ĵ�¼�û�������
	 * @param memo �¼�����ϸ����
	 * @throws LogException
	 */
	public void logDataEvent(Date timeOccured, int type, String source,
							 String userName, String memo) throws LogException;

	/**
	 * ��ѯ�¼�
	 * @param condition ��ѯ����
	 * @return �����������¼�Event���ϵ�Iterator
	 * @throws LogException
	 */
	public Iterator queryEvent(EventQueryCondition condition) throws
		LogException;

	/**
	 * �õ����а�ȫ�¼�
	 * @return ��ȫ�¼�Event���ϵ�Iterator
	 * @throws LogException
	 */
	public Iterator getAllSecurityEvents() throws LogException;

        /**
         *
         * @param startPage
         * @param pageNumber
         * @return
         * @throws LogException
         */
        public Iterator getSecurityEvents(int startPage,int pageNumber) throws LogException;

        /**
         * �õ�ȫ������
         * @return
         * @throws LogException
         */
        public int getSecurityEventsCount() throws LogException;

	/**
	 * �õ����������¼�
	 * @return �����¼�Event���ϵ�Iterator
	 * @throws LogException
	 */
	public Iterator getAllDataEvents() throws LogException;

	/**
	 * ��¼�����ϱ���־
	 * @param loadResult �ϱ����
	 * @param user �ϱ����ݵ��û�
	 * @param source ��Դ���ͻ���ip��
	 * @param loadMode �ϱ���ʽ ��0 -- web�� 1 -- webservice�� 2 -- �ʼ����ļ�����
	 * @throws LogException
	 */
	public void logLoadDataEvent(LoadResult loadResult, User user,
								 String source,
								 int loadMode) throws LogException;

}