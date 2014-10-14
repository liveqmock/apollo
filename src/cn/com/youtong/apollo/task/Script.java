package cn.com.youtong.apollo.task;

import java.util.*;

/**
 * 脚本接口
 */
public interface Script
{
    /**
     * 表内审核脚本类型
     */
    public int AUDIT_IN_TABLE = 0;

    /**
     * 表内运算脚本类型
     */
    public int CALCULATE_IN_TABLE = 1;

    /**
     * 表间审核脚本类型
     */
    public int AUDIT_CROSS_TABLE = 2;

    /**
     * 表间运算脚本类型
     */
    public int CALCULATE_CROSS_TABLE = 3;

    /**
     * 得到脚本类型，类型定义见本接口常量
     * @return 脚本类型常量
     */
    public int getType();

    /**
     * 得到脚本的名称
     * @return 脚本的名称
     */
    public String getName();

    /**
     * 得到脚本的内容
     * @return 脚本的内容
     */
    public String getContent();
}