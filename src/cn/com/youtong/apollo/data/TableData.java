package cn.com.youtong.apollo.data;

import java.util.*;
/**
 * ��������
 * dbfield2valueMap��key�Ǵ�д�ģ�
 * �����ﲻ�ý��д�Сдת��
 */

public class TableData
{
    /**
     * DBFieldName -- Value ��Map
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