package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;

/**
 * <p>Title: 设置饼图的外观</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class PieChart extends Chart
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

//    plot.setItemLabelGenerator(new StandardPieItemLabelGenerator());

		chart.setBackgroundPaint(Color.white); //设置有无背景 及 背景的颜色
//    plot.setBackgroundPaint(null);

		Pie3DPlot plot = (Pie3DPlot) chart.getPlot();
		plot.setStartAngle(270); //设置开始的角度
//    plot.setSectionLabelType(PiePlot.NAME_AND_PERCENT_LABELS);//设置显示区域中的label显示类型,名称+百分比
//    plot.setSectionLabelType(PiePlot.NO_LABELS);//不显示 显示区域中的 Label
		plot.setSectionLabelType(PiePlot.PERCENT_LABELS); //设置显示区域中的label显示类型,百分比
		font = new Font("黑体", Font.TRUETYPE_FONT, 20);
		plot.setSectionLabelFont(font); //设置显示区域中的字体

//    plot.setDirection(Rotation.CLOCKWISE);

		plot.setOutlinePaint(null); //设置有无外边框 及 外边框的颜色
//    plot.setOutlinePaint(Color.YELLOW);

		plot.setForegroundAlpha(0.5f); //设置透明度
//    plot.setNoDataMessage("No data to display");
		return chart;
	}

	/**
	 * 构造函数
	 * @param chart 传入的JFreeChart对象
	 */
	public PieChart(JFreeChart chart)
	{
		super(chart);
	}
}