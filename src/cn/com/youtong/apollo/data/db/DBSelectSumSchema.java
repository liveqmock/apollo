package cn.com.youtong.apollo.data.db;

import cn.com.youtong.apollo.data.*;
import cn.com.youtong.apollo.data.db.form.*;

/**
 * 选择汇总方案的数据库实现
 */
public class DBSelectSumSchema implements SelectSumSchema
{
    /**
     * 选择汇总方案form
     */
    private SelectSumSchemaForm form;

    /**
     * 选择汇总单位DBUnitTreeNode森林
     */
    private java.util.Collection selectSumUnitForest;

    public DBSelectSumSchema(SelectSumSchemaForm form, java.util.Collection selectSumUnitForest)
    {
        this.form = form;
        this.selectSumUnitForest = selectSumUnitForest;
    }

    /**
     * 方案id
     * @return 方案id
     */
    public java.lang.Integer getSchemaID()
    {
        return form.getSchemaID();
    }

    /**
     * 任务id
     * @return 任务id
     */
    public java.lang.String getTaskID()
    {
        return form.getTaskID();
    }

    /**
     * 方案名称
     * @return 方案名称
     */
    public java.lang.String getName()
    {
        return form.getName();
    }

    /**
     * 备注
     * @return 备注
     */
    public java.lang.String getMemo()
    {
        return form.getMemo();
    }

    /**
     * 创建时间
     * @return 创建时间
     */
    public java.util.Date getDateCreated()
    {
        return form.getDateCreated();
    }

    /**
     * 修改时间
     * @return 修改时间
     */
    public java.util.Date getDateModified()
    {
        return form.getDateModified();
    }

    /**
     * 选择汇总单位UnitTreeNode森林
     * @return 选择汇总单位UnitTreeNode森林
     */
    public java.util.Iterator getSelectSumUnitForest()
    {
        return selectSumUnitForest.iterator();
    }
}