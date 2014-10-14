package cn.com.youtong.tools.charts;

import java.text.*;
import java.util.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.data.*;
import org.jfree.data.time.*;
import cn.com.youtong.tools.charts.form.*;

/**
 * <p>Title: ͼ�ι������������� ����ͼ��</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public abstract class ChartsFactory
{

	/**
	 * ���ɱ�ͼ����
	 * @param chartTitle  ��ͼ������
	 * @param pieForm     ����ı�ͼ����
	 * @return            Chart
	 * @throws ChartException ͼ���쳣
	 */
	public static Chart createPieChart(String chartTitle, PieForm pieForm)
		throws ChartException
	{

		DefaultPieDataset data = getPieData(pieForm);
		JFreeChart jFreeChart = ChartFactory.createPie3DChart(chartTitle, data, true, false, false); // �Ƿ���ʾͼ��
		Chart chart = new PieChart(jFreeChart);
		return chart;
	}

	/**
	 * ���� ����ͼ����
	 * @param chartTitle  ��ͼ������
	 * @param x_Name      ���������
	 * @param y_Name      ���������
	 * @param colBarForm  �������ͼ����
	 * @return            Chart
	 * @throws ChartException ͼ���쳣
	 */
	public static Chart createBarChart(String chartTitle, String x_Name, String y_Name, Collection colBarForm)
		throws ChartException
	{

		DefaultCategoryDataset dataset = getBarData(colBarForm);
		JFreeChart jFreeChart = ChartFactory.createBarChart3D(chartTitle, x_Name, y_Name, dataset, PlotOrientation.VERTICAL, true, false, false); // �Ƿ�����URL����
		Chart chart = new BarChart(jFreeChart);
		return chart;
	}

	/**
	 * ���� ����ͼ����
	 * @param chartTitle  ��ͼ������
	 * @param x_Name      ���������
	 * @param y_Name      ���������
	 * @param colLineForm ��ͼ������
	 * @return            Chart
	 * @throws ChartException ͼ���쳣
	 */
	public static Chart createLineChart(String chartTitle, String x_Name, String y_Name, Collection colLineForm)
		throws ChartException
	{

		XYSeriesCollection dataset = getLineData(colLineForm);

		JFreeChart jFreeChart = ChartFactory.createXYLineChart(chartTitle, x_Name, y_Name, dataset, PlotOrientation.VERTICAL, true, true, false); // urls
		Chart chart = new LineChart(jFreeChart);
		return chart;
	}

	/**
	 * ���� ʱ������ͼ����
	 * @param chartTitle  ʱ����ͼ������
	 * @param x_Name      ���������
	 * @param y_Name      ���������
	 * @param colTimeForms ʱ����ͼ������
	 * @param dateType    ��������
	 * @return            Chart
	 * @throws ChartException ͼ���쳣
	 */
	public static Chart createTimeChart(String chartTitle, String x_Name, String y_Name, Collection colTimeForms, String dateType)
		throws ChartException
	{

		//װ������
		XYDataset dataset = null;
		try
		{
			dataset = getTimeData(colTimeForms, dateType);
		}
		catch(ChartException ex)
		{
			throw new ChartException("����ʱ������ͼʧ��", ex);
		}

		//����ͼ��
		JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(chartTitle, x_Name, y_Name, dataset, true, true, false);

		//����������ʾ��ʽ
		XYPlot plot = jFreeChart.getXYPlot();
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if(dateType.equals(TimeChart.YEAR))
		{
			axis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy��"));
		}
		else if(dateType.equals(TimeChart.MONTH))
		{
			axis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM"));
		}
		else if(dateType.equals(TimeChart.DAY))
		{
			axis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
			axis.setVerticalTickLabels(true);
		}
		else if(dateType.equals(TimeChart.QUARTER))
		{
			axis.setTickUnit(new DateTickUnit(DateTickUnit.MONTH, 3));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy��MMMM"));
		}
		else if(dateType.equals(TimeChart.WEEK))
		{
//      axis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy��w��"));
		}

		Chart chart = new TimeChart(jFreeChart);
		return chart;
	}

	/**
	 * ��ñ�ͼ����
	 * @param pieForm ��ͼ������
	 * @return        ����JFreeChart��Ҫ�����ݸ�ʽ
	 */
	private static DefaultPieDataset getPieData(PieForm pieForm)
	{
		DefaultPieDataset result = new DefaultPieDataset();
		Collection datas = pieForm.getDates();
		Iterator itr = datas.iterator();

		while(itr.hasNext())
		{
			ArrayList tempList = (ArrayList) itr.next();
			result.setValue((String) tempList.get(0), ((Integer) tempList.get(1)).intValue());
		}
		return result;
	}

	/**
	 * ��� ����ͼ����
	 * @param colBarForm  BarForm �ļ���
	 * @return        ����JFreeChart��Ҫ�����ݸ�ʽ
	 */
	private static DefaultCategoryDataset getBarData(Collection colBarForm)
	{
		DefaultCategoryDataset result = new DefaultCategoryDataset();

		Iterator itrBarForm = colBarForm.iterator();
		while(itrBarForm.hasNext())
		{
			BarForm barForm = (BarForm) itrBarForm.next();

			Collection datas = barForm.getDatas();
			Iterator itr = datas.iterator();
			while(itr.hasNext())
			{
				ArrayList tempList = (ArrayList) itr.next();
				result.setValue(((Integer) tempList.get(1)).intValue(), barForm.getTitle(), (String) tempList.get(0));
			}
		}
		return result;
	}

	/**
	 * ��� ����ͼ����
	 * @param colLineForms  LineForm�ļ���
	 * @return        ����JFreeChart��Ҫ�����ݸ�ʽ
	 */
	private static XYSeriesCollection getLineData(Collection colLineForms)
	{
		XYSeriesCollection result = new XYSeriesCollection();

		Iterator itrLineForm = colLineForms.iterator();
		while(itrLineForm.hasNext())
		{
			LineForm lineForm = (LineForm) itrLineForm.next();

			Collection datas = lineForm.getDatas();
			Iterator itr = datas.iterator();

			XYSeries series = new XYSeries(lineForm.getTitle());
			while(itr.hasNext())
			{
				ArrayList tempList = (ArrayList) itr.next();
				series.add(((Double) tempList.get(0)).doubleValue(), ((Double) tempList.get(1)).doubleValue());
			}
			result.addSeries(series);
		}
		return result;
	}

	/**
	 * ��� ʱ������ͼ����
	 * @param colTimeForms  LineForm�ļ���
	 * @param dateType  ��������
	 * @return        ����JFreeChart��Ҫ�����ݸ�ʽ
	 * @throws ChartException ͼ���쳣
	 */
	private static XYDataset getTimeData(Collection colTimeForms, String dateType)
		throws ChartException
	{

		TimeSeriesCollection result = new TimeSeriesCollection();

		Iterator itrTimeForms = colTimeForms.iterator();
		while(itrTimeForms.hasNext())
		{
			TimeForm timeForm = (TimeForm) itrTimeForms.next();
			//���һ��ʱ���ߵ�����
			try
			{
				result.addSeries(getTimeSeries(timeForm, dateType));
			}
			catch(ChartException ex)
			{
				throw new ChartException(ex);
			}
		}

		result.setDomainIsPointsInTime(true);
		return result;
	}

	/**
	 * ���һ��ʱ���ߵ�����
	 * @param timeForm  ʱ���ߵ�Form
	 * @param dateType   ��������
	 * @return  TimeSeries ʱ��������
	 * @throws ChartException ͼ���쳣
	 */
	private static TimeSeries getTimeSeries(TimeForm timeForm, String dateType)
		throws ChartException
	{

		Collection datas = timeForm.getDatas();
		Iterator itrDatas = datas.iterator();
		TimeSeries result = null;

		//�ж�ʱ������
		if(dateType.equals(TimeChart.YEAR))
		{
			result = new TimeSeries(timeForm.getTitle(), Year.class);
		}
		else if(dateType.equals(TimeChart.MONTH))
		{
			result = new TimeSeries(timeForm.getTitle(), Month.class);
		}
		else if(dateType.equals(TimeChart.DAY))
		{
			result = new TimeSeries(timeForm.getTitle(), Day.class);
		}
		else if(dateType.equals(TimeChart.QUARTER))
		{
			result = new TimeSeries(timeForm.getTitle(), Quarter.class);
		}
		else if(dateType.equals(TimeChart.WEEK))
		{
			result = new TimeSeries(timeForm.getTitle(), Week.class);
		}
		else
		{
			throw new ChartException("ʱ�����Ͳ���ȷ");
		}

		while(itrDatas.hasNext())
		{
			ArrayList tempList = (ArrayList) itrDatas.next();

			Date date = (Date) tempList.get(0);
			Double value = (Double) tempList.get(1);

			if(dateType.equals(TimeChart.YEAR))
			{
				result.add(new Year(date), value.doubleValue());
			}
			else if(dateType.equals(TimeChart.MONTH))
			{
				result.add(new Month(date), value.doubleValue());
			}
			else if(dateType.equals(TimeChart.DAY))
			{
				result.add(new Day(date), value.doubleValue());
			}
			else if(dateType.equals(TimeChart.QUARTER))
			{
				result.add(new Quarter(date), value.doubleValue());
			}
			else if(dateType.equals(TimeChart.WEEK))
			{
				result.add(new Week(date), value.doubleValue());
			}
		}
		return result;
	}
}