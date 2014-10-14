package cn.com.youtong.apollo.data;

import java.util.*;
/**
 * 表数据类
 * dbfield2valueMap的key是大写的，
 * 在这里不用进行大小写转换
 */

public class TableData
{
    /**
     * DBFieldName -- Value 的Map
     */
    private Map dbfield2valueMap;


    public TableData(Map dbfield2valueMap)
    {
        this.dbfield2valueMap = dbfield2valueMap;
    }

    public Map getDbfield2valueMap()
    {
        return dbfield2valueMap;
    }
}