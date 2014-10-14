package cn.com.youtong.apollo.data;

/**
 * ѡ����ܷ���
 */

public interface SelectSumSchema
{
    /**
     * ����id
     * @return ����id
     */
    public java.lang.Integer getSchemaID();

    /**
     * ����id
     * @return ����id
     */
    public java.lang.String getTaskID();

    /**
     * ��������
     * @return ��������
     */
    public java.lang.String getName();

    /**
     * ��ע
     * @return ��ע
     */
    public java.lang.String getMemo();

    /**
     * ����ʱ��
     * @return ����ʱ��
     */
    public java.util.Date getDateCreated();

    /**
     * �޸�ʱ��
     * @return �޸�ʱ��
     */
    public java.util.Date getDateModified();

    /**
     * ѡ����ܵ�λUnitTreeNodeɭ��
     * @return ѡ����ܵ�λUnitTreeNodeɭ��
     */
    public java.util.Iterator getSelectSumUnitForest();
}