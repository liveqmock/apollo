package cn.com.youtong.apollo.task;

/**
 * <p>Title: ����.</p>
 * <p>Description:
 * <ul>����
 * <li>1.ID
 * <li>2.����
 * <li><ul>3.ʱ������
 *     <li>3.1���ͣ��ձ����±����������걨
 *     <li>3.2�ϱ�ʱ�����䣬�߱�ʱ������
 *       	�ձ��� Сʱ��Сʱ
 *       	�±����������걨�� ���ڣ�����
 *      	�����ϱ�ʱ�䲻���ϱ����ɹ���Ա����
 *     </ul>
 * <li>4.����
 * <li>5.�汾
 * <li>6.����ʱ�䣨ϵͳ�Զ�ά����
 * <li>7.����ʱ�䣨ϵͳ�Զ�ά����
 * </ul>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author zhou
 * @version 1.0
 */
import java.util.*;

public interface Task
{
    /**
     * ����ID
     * @return
     */
    String id();

    /**
     * ��������
     * @return
     */
    String getName();

    /**
     * get created date
     * @return
     */
    Date getCreatedDate();

    /**
     * Get Modified Date
     * @return
     */
    Date getModifiedDate();

    /**
     * ��������
     * @return
     */
    String getMemo();

    /**
     * �汾
     * @return
     */
    int getVersion();
    /**
     * �������� -- �ձ�
     */
    public static final int REPORT_OF_DAY = 0;

    /**
     * �������� -- �±�
     */
    public static final int REPORT_OF_MONTH = 1;

    /**
     * �������� -- ����
     */
    public static final int REPORT_OF_QUARTER = 2;

    /**
     * �������� -- �걨
     */
    public static final int REPORT_OF_YEAR = 3;

    /**
     * �������� -- Ѯ��
     */
    public static final int REPORT_OF_XUN = 4;

    /**
     * �õ���������(�ձ����±����������걨)
     * @return each iterator memeber is a Table object
     * @throws TaskException if error throw TaskException
     */
    public int getTaskType() throws TaskException;

    /**
     * ȡ���б�������λ������Ϣ��
     * @return each iterator memeber is a Table object
     * @throws TaskException if error throw TaskException
     */
    Iterator getAllTables() throws TaskException;

    /**
     * Get table by table id.
     * @param id
     * @return Task if specfic id task existed, else null
     * @throws TaskException if error throw TaskException
     */
    Table getTableByID(String id) throws TaskException;

    /**
     * ȡ��������ʱ��.
     * @return each iterator member is a TaskTime object
     * @throws TaskException if error throw TaskException
     */
    Iterator getTaskTimes() throws TaskException;

    /**
     * ȡ���û������
     * @return �û������
     */
    public UnitMetaTable getUnitMetaTable();

    /**
     * �õ���������ʹ�õĽű���
     * @return ��������ʹ�õĽű��飬 û�ҵ�����null
     * @throws TaskException ����ʧ��
     */
    public ScriptSuit getActiveScriptSuit() throws TaskException;

    /**
     * ȡ����������ʱ��
     * @param time any time
     * @return if time belong to some task time return TaskTime else null
     * @throws TaskException if error throw TaskException
     */
    TaskTime getTaskTime(Date time) throws TaskException;

    /**
     * ȡ��ָ��ID������ʱ��
     * @param taskTimeID ����ʱ��ID
     * @return ָ��ID������ʱ�䡣û���ҵ�������null
     * @throws TaskException if error throw TaskException
     */
    TaskTime getTaskTime(Integer taskTimeID) throws TaskException;

    /**
     * �õ��������еĽű���
     * @return �������еĽű���ScriptSuit���ϵ�Iterator
     * @throws TaskException ����ʧ��
     */
    public Iterator getAllScriptSuits() throws TaskException;

    /**
     * �õ�ָ�����ƵĽű���
     * @param name �ű�������
     * @return ָ���Ľű��飬 û�ҵ�����null
     * @throws TaskException ����ʧ��
     */
    public ScriptSuit getScriptSuit(String name) throws TaskException;

}