package cn.com.youtong.apollo.data;

/**
 * 选择汇总方案
 */

public interface SelectSumSchema
{
    /**
     * 方案id
     * @return 方案id
     */
    public java.lang.Integer getSchemaID();

    /**
     * 任务id
     * @return 任务id
     */
    public java.lang.String getTaskID();

    /**
     * 方案名称
     * @return 方案名称
     */
    public java.lang.String getName();

    /**
     * 备注
     * @return 备注
     */
    public java.lang.String getMemo();

    /**
     * 创建时间
     * @return 创建时间
     */
    public java.util.Date getDateCreated();

    /**
     * 修改时间
     * @return 修改时间
     */
    public java.util.Date getDateModified();

    /**
     * 选择汇总单位UnitTreeNode森林
     * @return 选择汇总单位UnitTreeNode森林
     */
    public java.util.Iterator getSelectSumUnitForest();
}