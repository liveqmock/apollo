package cn.com.youtong.tools.charts;

import java.io.*;
import java.util.*;

import org.jfree.chart.*;
import cn.com.youtong.tools.charts.form.*;

/**
 * <p>Title: ������ ͼ����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public abstract class Chart
{
	/**����һ��ͼ��*/
	private JFreeChart _chart;

	/**
	 * ��ָ����λ������JPG�ļ�
	 * @param filePath    ָ�����ɵ��ļ�·�����ļ���
	 * @throws ChartException  ����ʧ�����׳� ChartException
	 */
	public void draw(String filePath)
		throws ChartException
	{
		FileOutputStream fos_jpg = null;
		try
		{
			fos_jpg = new FileOutputStream(filePath);
			ChartUtilities.writeChartAsJPEG(fos_jpg, 200, setFaceForChart(_chart), 800, 600, null);
		}
		catch(Exception ex)
		{
			throw new ChartException("д���ļ�ʧ��", ex);
		}
		finally
		{
			try
			{
				fos_jpg.close();
			}
			catch(Exception e)
			{}
		}
	}

	/**
	 * ����ͼ��
	 * @param chart �����JFreeChart����
	 */
	public Chart(JFreeChart chart)
	{
		_chart = chart;
	}

	/**
	 * ���� ͼ�����ʾ���
	 * @param chart  ����ͼ��Ķ���
	 * @return       ������Ķ����޸ĺ󷵻���
	 */
	abstract protected JFreeChart setFaceForChart(JFreeChart chart);

	public static void main(String[] args)
	{

		/**********************���ɱ�ͼʾ��*****************************/

//    PieForm pieForm=new PieForm();
//    pieForm.addData("ƻ��", new Integer(100));
//    pieForm.addData("����", new Integer(200));
//    pieForm.addData("����", new Integer(300));
//    pieForm.addData("�㽶", new Integer(400));
//    pieForm.addData("��֦", new Integer(500));
//
//    Chart pieChart=ChartsFactory.createPieChart("��������",pieForm);
//
//    try {
//      pieChart.draw("D:/test.jpg");//�����ļ�·�����ļ�����
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************������ͼʾ��*****************************/

//    Collection datas=new ArrayList();
//    BarForm barForm1=new BarForm("����");
//    BarForm barForm2=new BarForm("�Ϻ�");
//    BarForm barForm3=new BarForm("���");
//
//    barForm1.addData("ƻ��",new Integer(100));
//    barForm1.addData("����",new Integer(200));
//    barForm1.addData("����",new Integer(300));
//    barForm1.addData("�㽶",new Integer(400));
//    barForm1.addData("��֦",new Integer(500));
//
//    barForm2.addData("ƻ��",new Integer(200));
//    barForm2.addData("����",new Integer(400));
//    barForm2.addData("����",new Integer(600));
//    barForm2.addData("�㽶",new Integer(300));
//    barForm2.addData("��֦",new Integer(400));
//
//    barForm3.addData("ƻ��",new Integer(600));
//    barForm3.addData("����",new Integer(300));
//    barForm3.addData("����",new Integer(100));
//    barForm3.addData("�㽶",new Integer(400));
//    barForm3.addData("��֦",new Integer(200));
//
//    datas.add(barForm1);
//    datas.add(barForm2);
//    datas.add(barForm3);

//    Chart barChart=ChartsFactory.createBarChart("����ͼ","����","����",datas);
//
//    try {
//      barChart.draw("D:/test.jpg");//�����ļ�·�����ļ����� d:/test.jpg
//    }
//    catch (ChartException ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************������ͼʾ��*****************************/

//    Collection datas=new ArrayList();
//    LineForm lineForm1=new LineForm("����");
//    LineForm lineForm2=new LineForm("�Ϻ�");
//    LineForm lineForm3=new LineForm("���");
//
//    lineForm1.addData(new Double(1),new Double(100));
//    lineForm1.addData(new Double(2),new Double(300));
//    lineForm1.addData(new Double(3),new Double(400));
//    lineForm1.addData(new Double(4),new Double(200));
//    lineForm1.addData(new Double(5),new Double(234));
//    lineForm1.addData(new Double(6),new Double(567));
//    lineForm1.addData(new Double(7),new Double(150));
//    lineForm1.addData(new Double(8),new Double(700));
//
//
//    lineForm2.addData(new Double(3),new Double(100));
//    lineForm2.addData(new Double(4),new Double(400));
//    lineForm2.addData(new Double(5),new Double(334));
//    lineForm2.addData(new Double(6),new Double(667));
//    lineForm2.addData(new Double(7),new Double(350));
//    lineForm2.addData(new Double(8),new Double(200));
//    lineForm2.addData(new Double(9),new Double(300));
//    lineForm2.addData(new Double(10),new Double(100));
//
//    lineForm3.addData(new Double(1),new Double(400));
//    lineForm3.addData(new Double(2),new Double(110));
//    lineForm3.addData(new Double(3),new Double(300));
//    lineForm3.addData(new Double(4),new Double(500));
//    lineForm3.addData(new Double(5),new Double(124));
//    lineForm3.addData(new Double(6),new Double(467));
//    lineForm3.addData(new Double(7),new Double(550));
//    lineForm3.addData(new Double(8),new Double(100));
//
//    datas.add(lineForm1);
//    datas.add(lineForm2);
//    datas.add(lineForm3);
//
//    try {
//      Chart lineChart=ChartsFactory.createLineChart("����ͼ","�·�","����",datas);
//      lineChart.draw("D:/test.jpg");//�����ļ�·�����ļ�����
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************����ʱ����ͼ ʾ��*****************************/

		Collection datas = new ArrayList();
		TimeForm timeForm1 = new TimeForm("��λ1");
		TimeForm timeForm2 = new TimeForm("��λ2");

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date date1 = null;
			date1 = sdf.parse("2003-01-01");
			timeForm1.addData(date1, new Double(100));
			timeForm2.addData(date1, new Double(200));

			Date date2 = null;
			date1 = sdf.parse("2003-04-02");
			timeForm1.addData(date1, new Double(300));
			timeForm2.addData(date1, new Double(100));

			Date date3 = null;
			date1 = sdf.parse("2003-08-03");
			timeForm1.addData(date1, new Double(200));
			timeForm2.addData(date1, new Double(400));

			Date date4 = null;
			date1 = sdf.parse("2003-11-04");
			timeForm1.addData(date1, new Double(400));
			timeForm2.addData(date1, new Double(300));

			Date date5 = null;
			date1 = sdf.parse("2004-01-05");
			timeForm1.addData(date1, new Double(300));
			timeForm2.addData(date1, new Double(200));

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		datas.add(timeForm1);
		datas.add(timeForm2);

		Chart lineChart = null;
		try
		{
			lineChart = ChartsFactory.createTimeChart("ʱ������ͼ", "����", "ָ��ֵ", datas, TimeChart.MONTH);
		}
		catch(ChartException ex)
		{
			ex.printStackTrace();
		}

		try
		{
			lineChart.draw("D:/test.jpg"); //�����ļ�·�����ļ�����
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		/*************************************************************/
	}
}