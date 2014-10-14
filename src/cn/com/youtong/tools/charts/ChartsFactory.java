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
 * <p>Title: 图形工厂，用于生产 各种图形</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public abstract class ChartsFactory
{

	/**
	 * 生成饼图对象
	 * @param chartTitle  饼图的名称
	 * @param pieForm     传入的饼图数据
	 * @return            Chart
	 * @throws ChartException 图表异常
	 */
	public static Chart createPieChart(String chartTitle, PieForm pieForm)
		throws ChartException
	{

		DefaultPieDataset data = getPieData(pieForm);
		JFreeChart jFreeChart = ChartFactory.createPie3DChart(chartTitle, data, true, false, false); // 是否显示图例
		Chart chart = new PieChart(jFreeChart);
		return chart;
	}

	/**
	 * 生成 柱形图对象
	 * @param chartTitle  柱图的名称
	 * @param x_Name      横轴的名称
	 * @param y_Name      纵轴的名称
	 * @param colBarForm  传入的柱图数据
	 * @return            Chart
	 * @throws ChartException 图表异常
	 */
	public static Chart createBarChart(String chartTitle, String x_Name, String y_Name, Collection colBarForm)
		throws ChartException
	{

		DefaultCategoryDataset dataset = getBarData(colBarForm);
		JFreeChart jFreeChart = ChartFactory.createBarChart3D(chartTitle, x_Name, y_Name, dataset, PlotOrientation.VERTICAL, true, false, false); // 是否生成URL链接
		Chart chart = new BarChart(jFreeChart);
		return chart;
	}

	/**
	 * 生成 线形图对象
	 * @param chartTitle  线图的名称
	 * @param x_Name      横轴的名称
	 * @param y_Name      纵轴的名称
	 * @param colLineForm 线图的数据
	 * @return            Chart
	 * @throws ChartException 图表异常
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
	 * 生成 时间线形图对象
	 * @param chartTitle  时间线图的名称
	 * @param x_Name      横轴的名称
	 * @param y_Name      纵轴的名称
	 * @param colTimeForms 时间线图的数据
	 * @param dateType    日期类型
	 * @return            Chart
	 * @throws ChartException 图表异常
	 */
	public static Chart createTimeChart(String chartTitle, String x_Name, String y_Name, Collection colTimeForms, String dateType)
		throws ChartException
	{

		//装入数据
		XYDataset dataset = null;
		try
		{
			dataset = getTimeData(colTimeForms, dateType);
		}
		catch(ChartException ex)
		{
			throw new ChartException("生成时间线形图失败", ex);
		}

		//生成图表
		JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(chartTitle, x_Name, y_Name, dataset, true, true, false);

		//设置日期显示格式
		XYPlot plot = jFreeChart.getXYPlot();
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if(dateType.equals(TimeChart.YEAR))
		{
			axis.setTickUnit(new DateTickUnit(DateTickUnit.YEAR, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy年"));
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
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy年MMMM"));
		}
		else if(dateType.equals(TimeChart.WEEK))
		{
//      axis.setTickUnit(new DateTickUnit(DateTickUnit.MINUTE, 1));
			axis.setDateFormatOverride(new SimpleDateFormat("yyyy年w周"));
		}

		Chart chart = new TimeChart(jFreeChart);
		return chart;
	}

	/**
	 * 获得饼图数据
	 * @param pieForm 饼图的数据
	 * @return        创建JFreeChart需要的数据格式
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
	 * 获得 柱形图数据
	 * @param colBarForm  BarForm 的集合
	 * @return        创建JFreeChart需要的数据格式
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
	 * 获得 线形图数据
	 * @param colLineForms  LineForm的集合
	 * @return        创建JFreeChart需要的数据格式
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
	 * 获得 时间线形图数据
	 * @param colTimeForms  LineForm的集合
	 * @param dateType  日期类型
	 * @return        创建JFreeChart需要的数据格式
	 * @throws ChartException 图表异常
	 */
	private static XYDataset getTimeData(Collection colTimeForms, String dateType)
		throws ChartException
	{

		TimeSeriesCollection result = new TimeSeriesCollection();

		Iterator itrTimeForms = colTimeForms.iterator();
		while(itrTimeForms.hasNext())
		{
			TimeForm timeForm = (TimeForm) itrTimeForms.next();
			//添加一条时间线的数据
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
	 * 获得一条时间线的数据
	 * @param timeForm  时间线的Form
	 * @param dateType   日期类型
	 * @return  TimeSeries 时间线数据
	 * @throws ChartException 图表异常
	 */
	private static TimeSeries getTimeSeries(TimeForm timeForm, String dateType)
		throws ChartException
	{

		Collection datas = timeForm.getDatas();
		Iterator itrDatas = datas.iterator();
		TimeSeries result = null;

		//判断时间类型
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
			throw new ChartException("时间类型不正确");
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