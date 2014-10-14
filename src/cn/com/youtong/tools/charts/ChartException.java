package cn.com.youtong.tools.charts;

/**
 * <p>Title: Õº–Œ“Ï≥£</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ChartException
    extends Exception
{
    public ChartException()
    {
    }

    public ChartException(String s)
    {
        super(s);
    }

    public ChartException(String s, Exception ex)
    {
        super(s, ex);
    }

    public ChartException(Exception ex)
    {
        super(ex);
    }

}