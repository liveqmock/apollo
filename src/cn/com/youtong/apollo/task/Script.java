package cn.com.youtong.apollo.task;

import java.util.*;

/**
 * �ű��ӿ�
 */
public interface Script
{
    /**
     * ������˽ű�����
     */
    public int AUDIT_IN_TABLE = 0;

    /**
     * ��������ű�����
     */
    public int CALCULATE_IN_TABLE = 1;

    /**
     * �����˽ű�����
     */
    public int AUDIT_CROSS_TABLE = 2;

    /**
     * �������ű�����
     */
    public int CALCULATE_CROSS_TABLE = 3;

    /**
     * �õ��ű����ͣ����Ͷ�������ӿڳ���
     * @return �ű����ͳ���
     */
    public int getType();

    /**
     * �õ��ű�������
     * @return �ű�������
     */
    public String getName();

    /**
     * �õ��ű�������
     * @return �ű�������
     */
    public String getContent();
}