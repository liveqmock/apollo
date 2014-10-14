package cn.com.youtong.apollo.task;

import java.util.*;
/**
 * �ű���ӿ�
 */

public interface ScriptSuit
{
    /**
     * �õ��ű���Ĵ���ʱ��
     * @return �ű���Ĵ���ʱ��
     */
    public Date getDateCreated();

    /**
     * �õ��ű�����޸�ʱ��
     * @return �ű�����޸�ʱ��
     */
    public Date getDateModified();

    /**
     * �õ��ű����п����е�����ű�Script����
     * @return �ű����п����е�����ű�Script����
     * @throws TaskException
     */
    public List getCalculateScriptToExec() throws TaskException;

    /**
     * �õ��ű����п����е���˽ű�Script����
     * @return �ű����п����е���˽ű�Script����
     * @throws TaskException
     */
    public List getAuditScriptToExec() throws TaskException;


    /**
     * �õ����нű�Script���ϵ�Iterator
     * @return ���нű�Script���ϵ�Iterator
     * @throws TaskException ����ʧ��
     */
    public Iterator getAllScripts() throws TaskException;

    /**
     * �õ��ű��������
     * @return �ű�������
     */
    public String getName();

    /**
     * �õ���ע
     * @return ��ע
     */
    public String getMemo();
}