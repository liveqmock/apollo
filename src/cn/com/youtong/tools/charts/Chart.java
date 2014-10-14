package cn.com.youtong.tools.charts;

import java.io.*;
import java.util.*;

import org.jfree.chart.*;
import cn.com.youtong.tools.charts.form.*;

/**
 * <p>Title: 抽象类 图形类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public abstract class Chart
{
	/**保存一幅图表*/
	private JFreeChart _chart;

	/**
	 * 在指定的位置生成JPG文件
	 * @param filePath    指定生成的文件路径与文件名
	 * @throws ChartException  创建失败则抛出 ChartException
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
			throw new ChartException("写入文件失败", ex);
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
	 * 设置图表
	 * @param chart 传入的JFreeChart对象
	 */
	public Chart(JFreeChart chart)
	{
		_chart = chart;
	}

	/**
	 * 设置 图表的显示外观
	 * @param chart  保存图表的对象
	 * @return       将传入的对象修改后返回来
	 */
	abstract protected JFreeChart setFaceForChart(JFreeChart chart);

	public static void main(String[] args)
	{

		/**********************生成饼图示例*****************************/

//    PieForm pieForm=new PieForm();
//    pieForm.addData("苹果", new Integer(100));
//    pieForm.addData("梨子", new Integer(200));
//    pieForm.addData("葡萄", new Integer(300));
//    pieForm.addData("香蕉", new Integer(400));
//    pieForm.addData("荔枝", new Integer(500));
//
//    Chart pieChart=ChartsFactory.createPieChart("测试数据",pieForm);
//
//    try {
//      pieChart.draw("D:/test.jpg");//设置文件路径与文件名称
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************生成柱图示例*****************************/

//    Collection datas=new ArrayList();
//    BarForm barForm1=new BarForm("北京");
//    BarForm barForm2=new BarForm("上海");
//    BarForm barForm3=new BarForm("天津");
//
//    barForm1.addData("苹果",new Integer(100));
//    barForm1.addData("梨子",new Integer(200));
//    barForm1.addData("葡萄",new Integer(300));
//    barForm1.addData("香蕉",new Integer(400));
//    barForm1.addData("荔枝",new Integer(500));
//
//    barForm2.addData("苹果",new Integer(200));
//    barForm2.addData("梨子",new Integer(400));
//    barForm2.addData("葡萄",new Integer(600));
//    barForm2.addData("香蕉",new Integer(300));
//    barForm2.addData("荔枝",new Integer(400));
//
//    barForm3.addData("苹果",new Integer(600));
//    barForm3.addData("梨子",new Integer(300));
//    barForm3.addData("葡萄",new Integer(100));
//    barForm3.addData("香蕉",new Integer(400));
//    barForm3.addData("荔枝",new Integer(200));
//
//    datas.add(barForm1);
//    datas.add(barForm2);
//    datas.add(barForm3);

//    Chart barChart=ChartsFactory.createBarChart("柱形图","产地","产量",datas);
//
//    try {
//      barChart.draw("D:/test.jpg");//设置文件路径与文件名称 d:/test.jpg
//    }
//    catch (ChartException ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************生成线图示例*****************************/

//    Collection datas=new ArrayList();
//    LineForm lineForm1=new LineForm("北京");
//    LineForm lineForm2=new LineForm("上海");
//    LineForm lineForm3=new LineForm("天津");
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
//      Chart lineChart=ChartsFactory.createLineChart("线形图","月份","产量",datas);
//      lineChart.draw("D:/test.jpg");//设置文件路径与文件名称
//    }
//    catch (Exception ex) {
//      ex.printStackTrace();
//    }

		/*************************************************************/

		/**********************生成时间线图 示例*****************************/

		Collection datas = new ArrayList();
		TimeForm timeForm1 = new TimeForm("单位1");
		TimeForm timeForm2 = new TimeForm("单位2");

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
			lineChart = ChartsFactory.createTimeChart("时间线形图", "日期", "指标值", datas, TimeChart.MONTH);
		}
		catch(ChartException ex)
		{
			ex.printStackTrace();
		}

		try
		{
			lineChart.draw("D:/test.jpg"); //设置文件路径与文件名称
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		/*************************************************************/
	}
}