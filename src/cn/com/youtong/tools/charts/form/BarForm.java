package cn.com.youtong.tools.charts.form;

import java.util.*;

/**
 * <p>Title: BarForm���ڴ洢Barͼ�����һ��ͬɫ��</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class BarForm
{

	/**�������ݵ�����*/
	private String _title;
	/**������������*/
	private Collection _barData = new ArrayList();

	/**
	 * �����������������ݶ�
	 * ÿ�����ݶԶ�Ӧ�� Bar ͼ���� �������е�һ����
	 * @param itemName    һ�����ݵ�����
	 * @param itemValue   ��Ӧ���ݵ�ֵ
	 */
	public void addData(String itemName, Integer itemValue)
	{
		ArrayList item = new ArrayList();
		item.add(itemName);
		item.add(itemValue);
		_barData.add(item);
	}

	/**
	 * ���캯��
	 * @param title �������ݵ�����
	 */
	public BarForm(String title)
	{
		_title = title;
	}

	/**
	 * �������ݼ���
	 * @return Collection ��������ArrayList ÿ��ArrayList����һ�����ݶ�( String,Integer)
	 */
	public Collection getDatas()
	{
		return _barData;
	}

	/**
	 * ���ر������ݵ�����
	 * @return String �������ݵ�����
	 */
	public String getTitle()
	{
		return _title;
	}
}