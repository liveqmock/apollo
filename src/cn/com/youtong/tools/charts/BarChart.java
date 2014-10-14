package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;

/**
 * <p>Title: 用于设置Bar图的外观</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class BarChart extends Chart
{

	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("黑体", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setItemFont(font);

		CategoryPlot plot = chart.getCategoryPlot();
		//对横坐标操作
		CategoryAxis axis = plot.getDomainAxis();
		axis.setVerticalCategoryLabels(false);
		axis.setLabelFont(font);
		axis.setTickLabelFont(font);
		//对纵坐标操作
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);

		return chart;
	}

	/**
	 * 构造函数
	 * @param chart 传入的JFreeChart对象
	 */
	public BarChart(JFreeChart chart)
	{
		super(chart);
	}
}