package cn.com.youtong.tools.jsptree;

/**
 * ����˵��е���Ŀ��
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������ͨ</p>
 * @author ��Ρ
 * @version 1.0
 */
public class Item
{

	public Item()
	{
	}

	public Item(String text, String action)
	{
		this.text = text;
		this.action = action;
	}

	public Item(String text, String action, String id)
	{
		this.text = text;
		this.action = action;
		this.id = id;
	}

	/**��Ŀ��ʾ������*/
	public String text;
	/**��ĿҪִ�еĶ�����һ���ǳ����ӻ�javascript*/
	public String action;
	/**��ĿID*/
	public String id;
}