package cn.com.youtong.apollo.analyse.form;

import cn.com.youtong.apollo.analyse.xml.PrintInformation;

public class PrintInformationForm
{
	private String leftHeader;
	private String middleHeader;
	private String rightHeader;

	private String leftFooter;
	private String middleFooter;
	private String rightFooter;

	public PrintInformationForm()
	{
	}

	public PrintInformationForm( PrintInformation info )
	{
		this.leftHeader = info.getHeader().getLeft();
		this.middleHeader = info.getHeader().getMiddle();
		this.rightHeader = info.getHeader().getRight();

		this.leftFooter = info.getFooter().getLeft();
		this.middleFooter = info.getFooter().getMiddle();
		this.rightFooter = info.getFooter().getRight();
	}

	public String getLeftFooter()
	{
		return leftFooter;
	}

	public String getLeftHeader()
	{
		return leftHeader;
	}

	public String getMiddleFooter()
	{
		return middleFooter;
	}

	public String getMiddleHeader()
	{
		return middleHeader;
	}

	public String getRightFooter()
	{
		return rightFooter;
	}

	public String getRightHeader()
	{
		return rightHeader;
	}

	public void setLeftFooter( String leftFooter )
	{
		this.leftFooter = leftFooter;
	}

	public void setLeftHeader( String leftHeader )
	{
		this.leftHeader = leftHeader;
	}

	public void setMiddleFooter( String middleFooter )
	{
		this.middleFooter = middleFooter;
	}

	public void setMiddleHeader( String middleHeader )
	{
		this.middleHeader = middleHeader;
	}

	public void setRightFooter( String rightFooter )
	{
		this.rightFooter = rightFooter;
	}

	public void setRightHeader( String rightHeader )
	{
		this.rightHeader = rightHeader;
	}

}