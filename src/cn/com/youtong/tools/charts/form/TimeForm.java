package cn.com.youtong.tools.charts.form;

import java.util.*;

/**
 * <p>Title: TimeForm���ڴ洢Timeͼ��һ������(��һ��Time������ʾ)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TimeForm
{

	/**����Time�ߵ�����*/
	private String _title;
	/**������������*/
	private Collection _lineData = new ArrayList();

	/**
	 * �����������������ݶ�
	 * ÿһ�����ݶ�Ӧ�Ÿ�Time�������һ����
	 * @param x_dateValue  ������ֵ  (ע�⣺�����겻���ظ�)
	 * @param y_value  ������ֵ
	 */
	public void addData(Date x_dateValue, Double y_value)
	{
		ArrayList item = new ArrayList();
		item.add(x_dateValue);
		item.add(y_value);
		_lineData.add(item);
	}

	/**
	 * ���캯��
	 * @param title ���ߵ�����
	 */
	public TimeForm(String title)
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
	 * ���ظ�ʱ���ߵ�����
	 * @return Stringʱ���ߵ�����
	 */
	public String getTitle()
	{
		return _title;
	}
}