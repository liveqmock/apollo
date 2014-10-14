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
public class TimeChart extends Chart
{

	/**Time图的类型---年*/
	public static final String YEAR = "year";
	/**Time图的类型---季*/
	public static final String QUARTER = "quarter";
	/**Time图的类型---月*/
	public static final String MONTH = "month";
	/**Time图的类型---周*/
	public static final String WEEK = "week";
	/**Time图的类型---日*/
	public static final String DAY = "day";

	/**
	 * 设置 图表的显示外观
	 * @param chart  保存图表的对象
	 * @return       将传入的对象修改后返回来
	 */
	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("黑体", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setDisplaySeriesShapes(true);
		legend.setItemFont(font);

		XYPlot plot = chart.getXYPlot();
//    plot.setBackgroundPaint(Color.lightGray);
//    plot.setDomainGridlinePaint(Color.white);
//    plot.setRangeGridlinePaint(Color.BLUE);
//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,5.0));

		//对纵坐标操作
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);
		vaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//对线型进行操作
		XYItemRenderer renderer = plot.getRenderer();
		if(renderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			rr.setPlotShapes(true); //在线的转折处加点
			rr.setShapesFilled(true); //填充该点
//      rr.setItemLabelsVisible(true);
//      rr.setItemLabelAnchor(ItemLabelAnchor.CENTER);
//      rr.setItemLabelFont(new Font("黑体",Font.TRUETYPE_FONT,30));
//      rr.setBaseItemLabelAnchor(ItemLabelAnchor.INSIDE1);
//      rr.setSeriesItemLabelsVisible(1,Boolean.TRUE);

		}

		//对横坐标操作
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font);
		font = new Font("黑体", Font.TRUETYPE_FONT, 12);
		domainAxis.setTickLabelFont(font);

//    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//        rangeAxis.setTickUnit(new NumberTickUnit(2.0));
//        rangeAxis.setAutoRangeIncludesZero(true);//纵坐标是否从零开始显示

		return chart;
	}

	/**
	 * 构造函数
	 * @param chart 传入的JFreeChart对象
	 */
	public TimeChart(JFreeChart chart)
	{
		super(chart);
	}
}
