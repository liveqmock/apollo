package cn.com.youtong.apollo.webservice;

public class ServiceResult
{
	private int errCode;
	//private int iReserved1;
	//private int iReserved2;
	private String content;
	//private String sReserved1;
	//private String sReserved2;

	public ServiceResult()
	{
		errCode = -100;
		//iReserved1 = 0;
		//iReserved2 = 0;
		content ="";
		//sReserved1 = "";
		//sReserved2 = "";
	}

	/**
	 * 返回内容
	 * @return     内容，比如报表数据，代码字典等等
	 */
	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	/**public int getIReserved1()
	{
		return iReserved1;
	}

	public int getIReserved2()
	{
		return iReserved2;
	}*/

	/**public String getSReserved1()
	{
		return sReserved1;
	}

	public String getSReserved2()
	{
		return sReserved2;
	}*/

	/**public void setIReserved1(int iReserved1)
	{
		this.iReserved1 = iReserved1;
	}

	public void setIReserved2(int iReserved2)
	{
		this.iReserved2 = iReserved2;
	}*/

	/**public void setSReserved1(String sReserved1)
	{
		this.sReserved1 = sReserved1;
	}

	public void setSReserved2(String sReserved2)
	{
		this.sReserved2 = sReserved2;
	}*/
    public int getErrCode()
    {
	return errCode;
    }
    public void setErrCode(int errCode)
    {
	this.errCode = errCode;
    }

}