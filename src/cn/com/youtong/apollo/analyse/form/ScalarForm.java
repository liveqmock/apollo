package cn.com.youtong.apollo.analyse.form;

/**
 * ָ��Form
 */
public class ScalarForm
{
    /**
     * ���캯��
     * @param name ָ������
     * @param expression ���ʽ
     */
    public ScalarForm(String name, String expression)
    {
        this.name = name;
        this.expression = expression;
    }

    /**
     * ָ������
     */
    private String name;

    /**
     * ���ʽ
     */
    private String expression;

    public String getExpression()
    {
        return expression;
    }

    public String getName()
    {
        return name;
    }

    public void setExpression(String expression)
    {
        this.expression = expression;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(Object other)
    {
        if(!(other instanceof ScalarForm))
        {
            return false;
        }

        return (name.equals(((ScalarForm)other).getName()) && expression.equals(((ScalarForm)other).getExpression()));
    }

}