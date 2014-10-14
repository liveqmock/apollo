package cn.com.youtong.tools.charts.form;

import java.util.*;

/**
 * <p>Title: LineForm���ڴ洢Lineͼ��һ������(һ����)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class LineForm
{

	/**�����ߵ�����*/
	private String _title;
	/**������������*/
	private Collection _lineData = new ArrayList();

	/**
	 * �����������������ݶ�
	 * ÿһ�����ݶ�Ӧ�Ÿ��������һ����
	 * @param x_value  ������ֵ
	 * @param y_value  ������ֵ
	 */
	public void addData(Double x_value, Double y_value)
	{
		ArrayList item = new ArrayList();
		item.add(x_value);
		item.add(y_value);
		_lineData.add(item);
	}

	/**
	 * ���캯��
	 * @param title ���ߵ�����
	 */
	public LineForm(String title)
	{
		_title = title;
	}

	/**
	 * �������ݼ���
	 * @return Collection ��������ArrayList ÿ��ArrayList����һ�����ݶ�( Double,Double)
	 */
	public Collection getDatas()
	{
		return _lineData;
	}

	/**
	 * ���ظ��ߵ�����
	 * @return String�ߵ�����
	 */
	public String getTitle()
	{
		return _title;
	}
}