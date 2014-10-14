package cn.com.youtong.apollo.data.db;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.form.*;

/**
 * ѡ����ܷ��������ݿ�ʵ��
 */
public class DBSelectSumSchema implements SelectSumSchema
{
    /**
     * ѡ����ܷ���form
     */
    private SelectSumSchemaForm form;

    /**
     * ѡ����ܵ�λDBUnitTreeNodeɭ��
     */
    private java.util.Collection selectSumUnitForest;

    public DBSelectSumSchema(SelectSumSchemaForm form, java.util.Collection selectSumUnitForest)
    {
        this.form = form;
        this.selectSumUnitForest = selectSumUnitForest;
    }

    /**
     * ����id
     * @return ����id
     */
    public java.lang.Integer getSchemaID()
    {
        return form.getSchemaID();
    }

    /**
     * ����id
     * @return ����id
     */
    public java.lang.String getTaskID()
    {
        return form.getTaskID();
    }

    /**
     * ��������
     * @return ��������
     */
    public java.lang.String getName()
    {
        return form.getName();
    }

    /**
     * ��ע
     * @return ��ע
     */
    public java.lang.String getMemo()
    {
        return form.getMemo();
    }

    /**
     * ����ʱ��
     * @return ����ʱ��
     */
    public java.util.Date getDateCreated()
    {
        return form.getDateCreated();
    }

    /**
     * �޸�ʱ��
     * @return �޸�ʱ��
     */
    public java.util.Date getDateModified()
    {
        return form.getDateModified();
    }

    /**
     * ѡ����ܵ�λUnitTreeNodeɭ��
     * @return ѡ����ܵ�λUnitTreeNodeɭ��
     */
    public java.util.Iterator getSelectSumUnitForest()
    {
        return selectSumUnitForest.iterator();
    }
}