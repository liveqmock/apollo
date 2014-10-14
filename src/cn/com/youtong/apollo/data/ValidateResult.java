package cn.com.youtong.apollo.data;

import java.util.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.common.*;

/**
 * 节点检查结果
 */
public class ValidateResult
{
    /**
     * 单位
     */
    private UnitTreeNode unit;

    /**
     * 为通过检查的表Map，key为Table对象,value为ErrorScalar对象组成的集合
     */
    private Map errorTables;

    /**
     * 检查未通过的指标
     */
    public static class ErrorScalar
    {
        public ErrorScalar(String name, Double actual,
                           Double expected)
        {
            this.name = name;
            this.actual = actual;
            this.expected = expected;
        }

        /**
         * 名称
         */
        private String name;

        /**
         * 实际值
         */
        private Double actual;

        /**
         * 期望值
         */
        private Double expected;

        public String getName()
        {
            return name;
        }

        public Double getActual()
        {
            return actual;
        }

        public Double getExpected()
        {
            return expected;
        }

        /**
         * 实际值与期望值的差额
         * @return 实际值与期望值的差额
         */
        public Double getDifference()
        {
            Double difference = new Double(actual.doubleValue() - expected.doubleValue());
            return difference;
        }
    }

    public ValidateResult(UnitTreeNode unit, Map errorTables)
    {
        this.unit = unit;
        this.errorTables = errorTables;
    }

    public Map getErrorTables()
    {
        return errorTables;
    }

    public UnitTreeNode getUnit()
    {
        return unit;
    }

}