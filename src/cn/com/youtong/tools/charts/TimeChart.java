package cn.com.youtong.tools.charts;

import java.awt.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;

/**
 * <p>Title: ������ͼ�����</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TimeChart extends Chart
{

	/**Timeͼ������---��*/
	public static final String YEAR = "year";
	/**Timeͼ������---��*/
	public static final String QUARTER = "quarter";
	/**Timeͼ������---��*/
	public static final String MONTH = "month";
	/**Timeͼ������---��*/
	public static final String WEEK = "week";
	/**Timeͼ������---��*/
	public static final String DAY = "day";

	/**
	 * ���� ͼ�����ʾ���
	 * @param chart  ����ͼ��Ķ���
	 * @return       ������Ķ����޸ĺ󷵻���
	 */
	protected JFreeChart setFaceForChart(JFreeChart chart)
	{

		chart.setBackgroundPaint(Color.white);

		chart.setBackgroundPaint(Color.white);
		Font font = new Font("����", Font.TRUETYPE_FONT, 20);
		//chart.setTitle(new TextTitle(chart.getTitle().getText(), font));

		StandardLegend legend = (StandardLegend) chart.getLegend();
		legend.setDisplaySeriesShapes(true);
		legend.setItemFont(font);

		XYPlot plot = chart.getXYPlot();
//    plot.setBackgroundPaint(Color.lightGray);
//    plot.setDomainGridlinePaint(Color.white);
//    plot.setRangeGridlinePaint(Color.BLUE);
//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0,5.0));

		//�����������
		ValueAxis vaxis = plot.getRangeAxis();
		vaxis.setLabelFont(font);
		vaxis.setTickLabelFont(font);
		vaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		//�����ͽ��в���
		XYItemRenderer renderer = plot.getRenderer();
		if(renderer instanceof StandardXYItemRenderer)
		{
			StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
			rr.setPlotShapes(true); //���ߵ�ת�۴��ӵ�
			rr.setShapesFilled(true); //���õ�
//      rr.setItemLabelsVisible(true);
//      rr.setItemLabelAnchor(ItemLabelAnchor.CENTER);
//      rr.setItemLabelFont(new Font("����",Font.TRUETYPE_FONT,30));
//      rr.setBaseItemLabelAnchor(ItemLabelAnchor.INSIDE1);
//      rr.setSeriesItemLabelsVisible(1,Boolean.TRUE);

		}

		//�Ժ��������
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLabelFont(font);
		font = new Font("����", Font.TRUETYPE_FONT, 12);
		domainAxis.setTickLabelFont(font);

//    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
//        rangeAxis.setTickUnit(new NumberTickUnit(2.0));
//        rangeAxis.setAutoRangeIncludesZero(true);//�������Ƿ���㿪ʼ��ʾ

		return chart;
	}

	/**
	 * ���캯��
	 * @param chart �����JFreeChart����
	 */
	public TimeChart(JFreeChart chart)
	{
		super(chart);
	}
}
