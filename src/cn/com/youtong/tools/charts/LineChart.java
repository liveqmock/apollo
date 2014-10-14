package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;

/**
 * <p>Title: 设置线图的外观</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class LineChart extends Chart
{

	/**
	 * 设置 图表的显示外观
	 * @param chart  保存图表的对象
	 * @return       将传入的对象修改后返回来
	 */
	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("黑体", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setItemFont(font);

		XYPlot plot = chart.getXYPlot();

		//对纵坐标操作
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);
		vaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//对横坐标操作
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font);
		domainAxis.setTickLabelFont(font);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//对线型进行操作
		StandardXYItemRenderer renderer = (StandardXYItemRenderer) plot.getRenderer();
		renderer.setPlotShapes(true); //在线的转折处加点
		renderer.setShapesFilled(true); //填充该点
//    renderer.setItemLabelsVisible(true);

//    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	/**
	 * 构造函数
	 * @param chart 传入的JFreeChart对象
	 */
	public LineChart(JFreeChart chart)
	{
		super(chart);
	}
}