package cn.com.youtong.tools.jsptree;

/**
 * Title:Ê÷Å×³öµÄÒì³£¡£
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TreeException extends Exception
{

	public TreeException()
	{
	}

	public TreeException(String descript)
	{
		super(descript);
	}

    public TreeException(String descript, Exception ex)
    {
        super(descript, ex);
    }

    public TreeException(Exception ex)
    {
        super(ex);
    }

}