/*
 * Created on 2003-10-14
 */
package cn.com.youtong.apollo.common.mail;

/**
 * �ʼ�����
 * @author wjb
 */
public class Attachment
{
	/** �����ļ��� */
	private String fileName;
	/** �������� */
	private byte[] fileContent;

	/**
	 * ��������
	 * @return		��������
	 */
	public byte[] getFileContent()
	{
		return fileContent;
	}

	/**
	 * �����ļ���
	 * @return		�����ļ���
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * ���ø�������
	 * @param bs		��������
	 */
	public void setFileContent(byte[] bs)
	{
		fileContent = bs;
	}

	/**
	 * ���ø����ļ���
	 * @param string	�����ļ���
	 */
	public void setFileName(String string)
	{
		fileName = string;
	}

}
