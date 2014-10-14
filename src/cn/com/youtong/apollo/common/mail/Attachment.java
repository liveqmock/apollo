/*
 * Created on 2003-10-14
 */
package cn.com.youtong.apollo.common.mail;

/**
 * 邮件附件
 * @author wjb
 */
public class Attachment
{
	/** 附件文件名 */
	private String fileName;
	/** 附件内容 */
	private byte[] fileContent;

	/**
	 * 附件内容
	 * @return		附件内容
	 */
	public byte[] getFileContent()
	{
		return fileContent;
	}

	/**
	 * 附件文件名
	 * @return		附件文件名
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * 设置附件内容
	 * @param bs		附件内容
	 */
	public void setFileContent(byte[] bs)
	{
		fileContent = bs;
	}

	/**
	 * 设置附件文件名
	 * @param string	附件文件名
	 */
	public void setFileName(String string)
	{
		fileName = string;
	}

}
