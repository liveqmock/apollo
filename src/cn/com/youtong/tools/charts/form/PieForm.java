package cn.com.youtong.tools.charts.form;

/**
 * <p>Title: ����Pieͼ������</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.*;

public class PieForm
{
	/**������������*/
	private Collection _barData = new ArrayList();
	/**
	 * �����������������ݶ�
	 * �����ݶ� ��Ӧ ��ͼ�е�һ���������� sector
	 * @param itemName   ������������
	 * @param itemValue  ��������ֵ
	 */
	public void addData(String itemName, Integer itemValue)
	{
		ArrayList item = new ArrayList();
		item.add(itemName);
		item.add(itemValue);
		_barData.add(item);
	}

	/**
	 * �������ݼ���
	 * @return Collection ��������ArrayList ÿ��ArrayList����һ�����ݶ�( String,Integer)
	 */
	public Collection getDates()
	{
		return _barData;
	}
}