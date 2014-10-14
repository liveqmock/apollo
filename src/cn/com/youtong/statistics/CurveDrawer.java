package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * �����߷��̣����ҿ���ͨ��
 * {@link AxisProperties}
 * {@link CurveProperties}
 * {@link DistributePointProperties}
 * {@link TitleProperties}
 * ����ͼƬ����
 *
 * <p>
 * ����Ĭ�ϵ������У�
 * <code>
 * <pre>
  // ��ͷ����
  axisProp.setArrowLength( 15 ); // ����
  axisProp.setArrowHalfWidth( 2 ); // ���

   // ��label������
  axisProp.setLabelAxisSpace( height/25 ); // label�����߼��
  axisProp.setLabelMarginSpace( width/25 ); // label�ͱ߿���
  axisProp.setExpectPointNum( 8 ); // ����������ף�ϱ�ע������
  axisProp.setAxisPointHeight( 5 ); // �������ϱ�ע�ĵ�߶�

   disPointProp.setPointRadius( 5 );

   titleProp.setCenterX( width/2 );
   titleProp.setTopY( height/10 );
   </pre>
 * </code>
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class CurveDrawer
{
	private int width;
	private int height;
	private CurveEquation curveObj;
	private double minX, maxX, minY, maxY;
	private double[] dxArray, dyArray;
	private String title;
	private String xLabel;
	private String yLabel;

	private final static Color DEFAULT_AXIS_COLOR = Color.BLACK;
	private final static Color DEFAULT_BG_COLOR = Color.WHITE;
	private final static Color DEFAULT_CURVE_COLOR = Color.BLUE;
	private final static Color DEFAULT_DIS_POINT_COLOR = Color.RED;
	private final static Color DEFAULT_TITLE_COLOR = Color.BLACK;

	private Color bgColor;
	private AxisProperties axisProp;
	private CurveProperties curveProp;
	private DistributePointProperties disPointProp;
	private TitleProperties titleProp;

	public CurveDrawer(
		   int width, int height,
		   CurveEquation curveObj,
		   double minX, double maxX,
		   double minY, double maxY,
		   double[] dxArray, double[] dyArray,
		   String title,
		   String xLabel, String yLabel )
	{
		this.width = width;
		this.height = height;

		this.curveObj = curveObj;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.dxArray = dxArray;
		this.dyArray = dyArray;

		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;

		axisProp = new AxisProperties();

		// ��ͷ����
		axisProp.setArrowLength( 15 ); // ����
		axisProp.setArrowHalfWidth( 2 ); // ���

		// ��label������
		axisProp.setLabelAxisSpace( height/25 ); // label�����߼��
		axisProp.setLabelMarginSpace( width/25 ); // label�ͱ߿���
		axisProp.setExpectPointNum( 8 ); // ����������ף�ϱ�ע������
		axisProp.setAxisPointHeight( 5 ); // �������ϱ�ע�ĵ�߶�

		curveProp = new CurveProperties();
		curveProp.setStartPointX( minX );
		curveProp.setEndPointX( maxX );
		curveProp.setDeltaX( ( maxX - minX ) / 10000 );

		disPointProp = new DistributePointProperties();
		disPointProp.setPointRadius( 5 );

		titleProp = new TitleProperties();
		titleProp.setCenterX( width/2 );
		titleProp.setTopY( height/10 );
	}

	/**
	 * ����ͼƬ
	 * @return        ͼƬ
	 */
	public Image draw()
	{
		BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		Graphics2D g = image.createGraphics();

		// ���ñ���ɫ
		if( getBackgroundColor() != null )
			g.setBackground( getBackgroundColor() );
		else
			g.setBackground( DEFAULT_BG_COLOR );

		g.fillRect( 0, 0, width, height );

		// ����ƫ�����ͱ�������
		boolean displayZeroInAxisX = DrawUtil.displayZeroInAxis( minX, maxX );
		boolean displayZeroInAxisY = DrawUtil.displayZeroInAxis( minY, maxY );

		double[] deltaAndScaleX = DrawUtil.deltaAndScaleX( width, minX, maxX, displayZeroInAxisX );
		double[] deltaAndScaleY = DrawUtil.deltaAndScaleY( height, minY, maxY, displayZeroInAxisY );

		double deltaX = deltaAndScaleX[0]; // X��ƫ����
		double deltaY = deltaAndScaleY[0]; // Y��ƫ����

		double scaleX = deltaAndScaleX[1];
		double scaleY = deltaAndScaleY[1];

		// �任����
		// [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
		// [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
		// [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
		// AffineTransform(float m00, float m10, float m01, float m11, float m02, float m12)
		AffineTransform tf = new AffineTransform( 1/scaleX, 0, 0, -1/scaleY, deltaX/scaleX, deltaY/scaleY );

		// ��������
		// ���û������һЩ���ԣ�����ȱʡ����
		fetchDefaultAxisProperties( g );

		DrawUtil.drawAxis( g,
						   deltaX/scaleX, deltaY/scaleY,
						   minX, maxX,
						   minY, maxY,
						   xLabel, yLabel,
						   width, height,
						   tf, axisProp );

		// ������
		fetchDefaultCurveProperties( g );
		DrawUtil.drawCurve( g, curveObj, tf, curveProp );

		// ��ɢ��
		if( dxArray != null )
		{
			fetchDefaultDistributePointProperties( g );
			DrawUtil.drawPoint( g, dxArray, dyArray, tf, disPointProp );
		}

		// ������
		fetchDefaultTitleProperties( g );
		DrawUtil.drawTitle( g, title, width, height, titleProp );

		return image;
	}

	public AxisProperties getAxisProperties()
	{
		return axisProp;
	}

	public void setAxisProperties( AxisProperties axisProp )
	{
		this.axisProp = axisProp;
	}

	public void setCurveProperties( CurveProperties curveProp )
	{
		this.curveProp = curveProp;
	}

	public CurveProperties getCurveProperties()
	{
		return curveProp;
	}

	public void setDistributePointProperties( DistributePointProperties disPointProp )
	{
		this.disPointProp = disPointProp;
	}
	public DistributePointProperties getDistributePointProperties()
	{
		return disPointProp;
	}

	public TitleProperties getTitleProperties()
	{
		return titleProp;
	}
	public void setTitleProperties( TitleProperties titleProp )
	{
		this.titleProp = titleProp;
	}

	private void fetchDefaultAxisProperties( Graphics2D g )
	{
		if( axisProp.getLabelFont() == null )
		{
			int labelFontSize = width/25; // label�������С

			Font font = new Font( g.getFont().getFontName(), Font.PLAIN, labelFontSize );
			axisProp.setLabelFont( font );
		}
		if( axisProp.getAxisPointFont() == null )
		{
			int axisPointFontSize = width/25 - 1;  //��ע�������ִ�С

			Font font = new Font( g.getFont().getFontName(), Font.PLAIN, axisPointFontSize );
			axisProp.setAxisPointFont( font );
		}
		if( axisProp.getAxisColor() == null )
		{
			axisProp.setAxisColor( DEFAULT_AXIS_COLOR );
		}
		if( axisProp.getLabelColor() == null )
		{
			axisProp.setLabelColor( DEFAULT_AXIS_COLOR );
		}
	}

	private void fetchDefaultCurveProperties( Graphics2D g )
	{
		if( curveProp.getCurveColor() == null )
			curveProp.setCurveColor( DEFAULT_CURVE_COLOR );
	}

	private void fetchDefaultDistributePointProperties( Graphics2D g )
	{
		if( disPointProp.getDisPointColor() == null )
		{
			disPointProp.setDisPointColor( DEFAULT_DIS_POINT_COLOR );
		}
	}

	private void fetchDefaultTitleProperties( Graphics2D g )
	{
		if( titleProp.getTitleColor() == null )
		{
			titleProp.setTitleColor( DEFAULT_TITLE_COLOR );
		}
		if( titleProp.getTitleFont() == null )
		{
			int fontSize = height/20;
			Font font = new Font( g.getFont().getFontName(),
								  Font.PLAIN, fontSize );
			titleProp.setTitleFont( font );
		}
	}

	public void setBackgroundColor( Color color )
	{
		bgColor = color;
	}
	public Color getBackgroundColor()
	{
		return bgColor;
	}
}