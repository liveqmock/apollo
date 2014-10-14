package cn.com.youtong.apollo.data;

import java.util.*;
import cn.com.youtong.apollo.task.*;
import cn.com.youtong.apollo.common.*;

/**
 * �ڵ�����
 */
public class ValidateResult
{
    /**
     * ��λ
     */
    private UnitTreeNode unit;

    /**
     * Ϊͨ�����ı�Map��keyΪTable����,valueΪErrorScalar������ɵļ���
     */
    private Map errorTables;

    /**
     * ���δͨ����ָ��
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
         * ����
         */
        private String name;

        /**
         * ʵ��ֵ
         */
        private Double actual;

        /**
         * ����ֵ
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
         * ʵ��ֵ������ֵ�Ĳ��
         * @return ʵ��ֵ������ֵ�Ĳ��
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