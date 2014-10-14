package cn.com.youtong.apollo.analyse.form;

/**
 * 指标Form
 */
public class ScalarForm
{
    /**
     * 构造函数
     * @param name 指标名称
     * @param expression 表达式
     */
    public ScalarForm(String name, String expression)
    {
        this.name = name;
        this.expression = expression;
    }

    /**
     * 指标名称
     */
    private String name;

    /**
     * 表达式
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