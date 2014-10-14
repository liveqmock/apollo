package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

class DrawUtil {
	public DrawUtil()
	{
	}

	// deltaX, deltaY,��ʾ���ͼ�����ƫ���˶���
	// Ŀǰû�п��ǵ�ֱ������ϵ���Ǵ�ԭ������»������
	/**
	 * ��������
	 *
	 * <p>
	 * ���ջ����Ĵ�С��XYֵ��ʾ������minX,maxX,minY,maxYȷ������
	 * ��ʾ�ʵ������ߡ�
	 *
	 *
	 * @param g               Graphics
	 * @param deltaX          ��������ϵԭ����Ի�������ϵ��ԭ��Xƫ��������������ϵ��λ��
	 * @param deltaY          ��������ϵԭ����Ի�������ϵ��ԭ��Yƫ��������������ϵ��λ��
	 * @param minX            double
	 * @param maxX            double
	 * @param minY            double
	 * @param maxY            double
	 * @param xLabel          x����ʾ��ǩ
	 * @param yLabel          y����ʾ��ǩ
	 * @param width           �����쳤�����߿��
	 * @param height          �����쳤�����߸߶�
	 * @param tf              ��������ϵ�򻭲�����ϵת������
	 */
	public static void drawAxis( Graphics2D g,
								 double deltaX, double deltaY,
								 double minX, double maxX,
								 double minY, double maxY,
								 String xLabel, String yLabel,
								 int width, int height,
								 AffineTransform tf,
								 AxisProperties axisProp )
	{
		// ��ͷ����
		int arrowLength = axisProp.getArrowLength(); // ����
		int arrowHalfWidth = axisProp.getArrowHalfWidth(); // ���

		// ��label������
		int labelAxisSpace = axisProp.getLabelAxisSpace(); // label�����߼��
		int labelMarginSpace = axisProp.getLabelMarginSpace(); // label�ͱ߿���

		int expectPointNum = axisProp.getExpectPointNum(); // ����������ף�ϱ�ע������
		int axisPointHeight = axisProp.getAxisPointHeight(); // �������ϱ�ע�ĵ�߶�

		boolean displayZeroInAxisX = displayZeroInAxis( minX, maxX );
		boolean displayZeroInAxisY = displayZeroInAxis( minY, maxY );

		// ��X���Y�ύ���ĺ�������
		double injectPointX = 0;
		double injectPointY = 0;

		if( !displayZeroInAxisX )
			injectPointX = minX;

		if( !displayZeroInAxisY )
			injectPointY = minY;

		Point2D injectPointSrc = new Point2D.Double( injectPointX, injectPointY );
		Point2D injectPointDst = new Point2D.Double( 0, 0 );

		tf.transform( injectPointSrc, injectPointDst );

		Point2D leftP = new Point2D.Double( 0, injectPointDst.getY() );
		Point2D rightP = new Point2D.Double( width, injectPointDst.getY() );
		Point2D bottonP = new Point2D.Double( injectPointDst.getX(), height );
		Point2D topP = new Point2D.Double( injectPointDst.getX(), 0 );

		Color axisColor = axisProp.getAxisColor();
		if( axisColor == null )
			axisColor = g.getColor();

		g.setColor( axisColor );

		GeneralPath path = new GeneralPath();
		// x��
		path.moveTo( (float) leftP.getX(), (float) leftP.getY() );
		path.lineTo( (float) rightP.getX(), (float) rightP.getY() );
		// ��ͷ
		path.lineTo( (float) rightP.getX() - arrowLength, (float) rightP.getY() + arrowHalfWidth );
		path.moveTo( (float) rightP.getX(), (float) rightP.getY() );
		path.lineTo( (float) rightP.getX() - arrowLength, (float) rightP.getY() -arrowHalfWidth );

		// y��
		path.moveTo( (float) bottonP.getX(), (float) bottonP.getY() );
		path.lineTo( (float) topP.getX(), (float) topP.getY() );
		// ��ͷ
		path.lineTo( (float) topP.getX() + arrowHalfWidth, (float) topP.getY() + arrowLength );
		path.moveTo( (float) topP.getX(), (float) topP.getY() );
		path.lineTo( (float) topP.getX() -arrowHalfWidth, (float) topP.getY() + arrowLength );

		g.draw( path );

		// ��label
		Font originFont = g.getFont();

		Font labelFont = axisProp.getLabelFont();
		Font axisPointFont = axisProp.getAxisPointFont();

		if( labelFont == null )
			labelFont = originFont;
		if( axisPointFont == null )
			axisPointFont = originFont;

		Color labelColor = axisProp.getLabelColor();
		if( labelColor == null )
			labelColor = g.getColor();

		g.setColor( labelColor );

		// x��label
		Rectangle2D bounds = labelFont.getStringBounds( xLabel, g.getFontRenderContext() );
		g.setFont( labelFont );
		g.drawString( xLabel, (float) (rightP.getX() - labelMarginSpace - bounds.getWidth() ),
					  (float) (rightP.getY() - labelAxisSpace ) );

		// ��x���ϵ������
		double[] drawAxisPointX = getPerfectLocation( minX, maxX, expectPointNum );
		Point2D[] axisPointXSrc = new Point2D[ drawAxisPointX.length ];
		Point2D[] axisPointXDst = new Point2D[ drawAxisPointX.length ];

		for( int i=0; i<axisPointXSrc.length; i++ )
		{
			axisPointXSrc[i] = new Point2D.Double( drawAxisPointX[i], injectPointSrc.getY() );
		}

		tf.transform( axisPointXSrc, 0, axisPointXDst, 0, axisPointXSrc.length );

		// ��X���ϱ�ע�ĵ�����
		String[] pointNamesX = SAMath.format( drawAxisPointX );

		g.setFont( axisPointFont );
		for( int i=0; i<axisPointXDst.length; i++ )
		{
			Point2D p = axisPointXDst[i];

			g.setColor( axisColor );
			g.drawLine( (int) p.getX(), (int) p.getY(),
						(int) p.getX(), (int) p.getY() - axisPointHeight );

			g.setColor( labelColor );
			String pointName = pointNamesX[i];
			Rectangle2D r = axisPointFont.getStringBounds( pointName, g.getFontRenderContext() );

			if( r.getWidth() > (double) width*0.667 / axisPointXDst.length )
			{
				//double m00, double m10, double m01, double m11, double m02, double m12)
				AffineTransform xform = new AffineTransform( 0, 1, -1, 0,0,0);
				g.setFont( axisPointFont.deriveFont( Font.LAYOUT_LEFT_TO_RIGHT, xform ) );

				Point2D src = new Point2D.Double( p.getX(), p.getY() + labelAxisSpace );

				g.drawString( pointName,
							  ( float ) src.getX() ,
							  ( float ) src.getY()  );

				//g.setFont( axisPointFont );
			}
			else
			{
				g.drawString( pointName, ( float ) p.getX(),
							  ( float ) ( p.getY() + labelAxisSpace +
										  r.getHeight() ) );
			}
		}

		// y��label
		bounds = labelFont.getStringBounds( yLabel, g.getFontRenderContext() );
		g.setFont( labelFont );
		g.drawString( yLabel, (float) (topP.getX() + labelAxisSpace ),
					 (float) (topP.getY() + labelMarginSpace + bounds.getHeight() ) );

		// ��y���ϵ������
		double[] drawAxisPointY = getPerfectLocation( minY, maxY, expectPointNum );
		Point2D[] axisPointYSrc = new Point2D[ drawAxisPointY.length ];
		Point2D[] axisPointYDst = new Point2D[ drawAxisPointY.length ];

		for( int i=0; i<axisPointYSrc.length; i++ )
		{
			axisPointYSrc[i] = new Point2D.Double( injectPointSrc.getX(), drawAxisPointY[i] );
		}

		tf.transform( axisPointYSrc, 0, axisPointYDst, 0, axisPointYSrc.length );

		// ��Y���ϱ�ע�ĵ�����
		String[] pointNamesY = SAMath.format( drawAxisPointY );

		g.setFont( axisPointFont );
		for( int i=0; i<axisPointYDst.length; i++ )
		{
			Point2D p = axisPointYDst[i];

			g.setColor( axisColor );
			g.drawLine( (int) p.getX(), (int) p.getY(),
						(int) p.getX() + axisPointHeight, (int) p.getY() );

			String pointName = pointNamesY[i];
			Rectangle2D r = axisPointFont.getStringBounds( pointName, g.getFontRenderContext() );

			g.setColor( labelColor );
			g.drawString( pointName, (float) (p.getX()-labelAxisSpace-r.getWidth()), (float) p.getY() );
		}

		// ����ԭ����Font
		g.setFont( originFont );
	}

	/**
	 * �������ֵ����Сֵ���������е��͵ĵ�ֲ����顣
	 *
	 * <p>
	 * ������Сֵ0�����ֵ18�������õ�10������㣬���Ը���0,2,4,6,8,10,12,14,16,18����������
	 *
	 * <p>
	 * @todo ��ξ����зֲ�����0��5������
	 *
	 * @param min             ��Сֵ
	 * @param max             ���ֵ
	 * @param expectNum       �����ֲ�����
	 * @return                �ֲ�������
	 */
	private static double[] getPerfectLocation( double min, double max, int expectNum )
	{
		boolean displayZero = displayZeroInAxis( min, max );

		double space = 0;
		if( displayZero )
		{
			if( min<=0 && max>=0 )
			{
				space = ( max - min ) /expectNum;

				space = SAMath.retainOneSignificantDigit( space );

				int leftNum = (int) ( -1.0 * min / space );
				int rightNum = (int) ( max / space );

				int num = leftNum + rightNum + 1;

				double[] result = new double[num];
				result[0] = 0 - space*leftNum;
				for( int i=1; i<result.length; i++ )
				{
					result[i] = result[i-1] + space;
				}

				return result;
			}
			else
			{
				// ͬ��ֲ�
				if( min >=0 )
				{
					space = max / expectNum;
					space = SAMath.retainOneSignificantDigit( space );

					int num = (int) (max / space) + 1;
					double[] result = new double[num];

					result[0] = 0;
					for( int i=1; i<num; i++ )
					{
						result[i] = result[i-1] + space;
					}

					return result;
				}
				else
				{
					space = -1.0 * min / expectNum;
					space = SAMath.retainOneSignificantDigit( space );

					int num = (int) (-1.0*min / space) + 1;
					double[] result = new double[num];

					result[0] = 0;
					for( int i=1; i<num; i++ )
					{
						result[i] = result[i-1] + space;
					}

					return result;
				}
			}
		}
		else
		{
			space = ( max - min ) / expectNum;
			space = SAMath.retainOneSignificantDigit( space );

			int num = (int) ( (max-min)/space ) + 1;
			double[] result = new double[num];
			result[0] = min;
			for( int i=0; i<num; i++ )
			{
				result[i] = result[i-1] + space;
			}

			return result;
		}
	}

	// ��x0��ʼ�����ߣ� x0��ʾ��������ϵ��x0ֵ������ָ��������ϵ��x0
	public static void drawCurve( Graphics2D g,
								  CurveEquation curveObj,
								  AffineTransform tf,
								  CurveProperties curveProp )
	{
		g.setColor( curveProp.getCurveColor() );

		double deltaX = curveProp.getDeltaX();
		double startPointX = curveProp.getStartPointX();
		double endPointX = curveProp.getEndPointX();

		double nextX = startPointX + deltaX;
		double nextY;

		double startPointY = curveObj.value( startPointX );
		GeneralPath path = new GeneralPath();
		path.moveTo( (float) startPointX, (float) startPointY );

		for ( ; nextX < endPointX; nextX += deltaX )
		{
			nextY = curveObj.value( nextX );
			path.lineTo( (float) nextX, (float) nextY );
		}

		path.transform( tf );
		g.draw( path );
	}

	/**
	 * ��ɢ��
	 * @param g             Graphics
 	 * @param xArray        ɢ���xֵ����
	 * @param yArray        ɢ���yֵ����
	 * @param tf            ��������ϵ�򻭲�����ϵת������
	 */
	public static void drawPoint( Graphics2D g,
								  double[] xArray, double[] yArray,
								  AffineTransform tf,
								  DistributePointProperties disPointProp ) {
		g.setColor( disPointProp.getDisPointColor() );

		// ��������
		int pointRadius = disPointProp.getPointRadius();

		// �任ǰ�ĵ�
		Point2D[] ptSrc = new Point2D[ xArray.length ];
		// �任��ĵ�
		Point2D[] ptDst = new Point2D[ xArray.length ];

		for ( int i = 0; i < xArray.length; i++ ) {
			Point2D p = new Point2D.Double( xArray[i], yArray[i] );
			ptSrc[i] = p;
		}

		tf.transform( ptSrc, 0, ptDst, 0, ptSrc.length );

		for( int i=0; i<ptDst.length; i++ )
		{
			Point2D p = ptDst[i];
			g.fillArc( ( int ) p.getX(), (int) p.getY(), pointRadius, pointRadius, 0, 360 );
		}
	}

	/**
	 * ��width, heigth�����ڣ�д�ϱ���title
	 * @param g
	 * @param title
	 * @param width
	 * @param height
	 */
	public static void drawTitle( Graphics2D g,
								  String title,
								  int width, int height,
								  TitleProperties titleProp )
	{
		if( titleProp.getTitleColor() != null )
			g.setColor( titleProp.getTitleColor() );

		Font originFont = g.getFont();

		Font font = titleProp.getTitleFont();
		int fontSize = font.getSize();
		int fontStyle = font.getStyle();
		String fontName = font.getFontName();

		g.setFont( font );

		// �ļ��߿�
		Rectangle2D bounds = null;

		boolean changeFontSize = true;
		while( changeFontSize )
		{
			bounds = font.getStringBounds( title,
				g.getFontRenderContext() );
			if ( bounds.getHeight() > height / 4 )
			{
				// Ŀǰѡ�õ�����̫����
				// �������ִ�С����������
				fontSize--;
				font = new Font( fontName, fontStyle, fontSize );
				g.setFont( font );
			}
			else
			{
				changeFontSize = false;
			}
		}

		// ���ֿ�������ϱ�Ե1/10�߶ȳ���ˮƽ����
		int baseX = (int) ( titleProp.getCenterX() - bounds.getWidth()/2 ) ;
		int baseY = (int) ( titleProp.getTopY() + bounds.getHeight() );

		g.drawString( title, baseX, baseY );

		// ����ԭ����Font
		g.setFont( originFont );
	}

	static double[] deltaAndScaleX( double width,
									double minX, double maxX,
									boolean displayZeroInAxisX )
	{
		return deltaAndScale( width, minX, maxX, displayZeroInAxisX );
	}

	static double[] deltaAndScaleY( double height,
									double minY, double maxY,
									boolean displayZeroInAxisY )
	{
		double[] deltaAndScaleY = deltaAndScale( height, minY, maxY,
												 displayZeroInAxisY );

		// Y���ƫ������Ҫ���¼���
		double deltaY = deltaAndScaleY[0]; // Y��ƫ����
		double scaleY = deltaAndScaleY[1];

		deltaAndScaleY[0] = height * scaleY - deltaY;

		return deltaAndScaleY;
	}

	/**
	 * �����߷��ڳ���Ϊlength���߶Σ����߻������ϣ�
	 * Ϊ�˸��õ���ʾ����ʾ����ռ�ܳ��ȵ�2/3������Ҫһ����ƽ��delta������scale��
	 *
	 * @param length              �ڳ���length������ʾ
	 * @param min                 ��ʾ����Сֵ
	 * @param max                 ��ʾ�����ֵ
	 * @param displayZero         �Ƿ���ʾ0��
	 * @return                    [0]=delta, [1]=scale
	 */
	private static double[] deltaAndScale( double length, double min,
								   double max, boolean displayZero ) {
		double scale = 1; // ÿͼƬ��λ��Ӧ����scale��λ
		double delta = 0;

		if ( displayZero ) {
			// ����������ʾ0��
			if ( min <= 0 && max >= 0 ) {
				// ����ֵ������
				double space = max - min;
				scale = space / ( length * ( 2d / 3 ) );

				delta = length * ( 1d / 6 ) * scale - min;
			} else {
				// ����ֲ�

				if ( min <= 0 ) {
					scale = -1 * min / ( length * ( 2d / 3 ) );
					delta = length * ( 1d / 6 ) * scale - min;
				} else {
					scale = max / ( length * ( 2d / 3 ) );
					delta = length * ( 1d / 6 ) * scale;
				}
			}
		} else {
			// ����ʾ0��
			double space = max - min;
			scale = space / ( length * ( 2d / 3 ) );

			delta = length * ( 1d / 6 ) * scale - min;
		}

		return new double[] {
			delta, scale};
	}

	/**
	 * �ж��Ƿ�Ҫ��ʾ0�㣬����һ��׼��
	 * <ol>
	 * <li>������ֲַ���0�����࣬��ʾ0�㣻</li>
	 * <li>������鵥��ֲ������ֵ����Сֵ��࣬��������0�����С���Ĳ�𲻴���ʾ0�㣻</li>
	 * <li>ʣ�¾��ǵ���ֲ������ֵ����Сֵ��࣬��������0�����С���Ĳ��ϴ󣬲���ʾ0�㡣</li>
	 * </ol>
	 *
	 * <p>
	 * ����ѡ������СΪ1.5�������������0�����С��࣬�������ּ���1.5����
	 *
	 * @param min         ��Сֵ
	 * @param max         ���ֵ
	 * @return            �Ƿ���ʾ0��
	 */
	static boolean displayZeroInAxis( double min, double max ) {
		boolean displayZero = true;
		// ������ֳ������ֲ�
		if ( min <= 0 && max >= 0 ) {
			displayZero = true;
		} else {
			double absoluteMax = Math.max( Math.abs( min ), Math.abs( max ) );
			double absoluteMin = Math.min( Math.abs( min ), Math.abs( max ) );

			double space = ( absoluteMax - absoluteMin );
			if ( space * 1.5 < absoluteMin ) {
				displayZero = false;
			}
		}
		return displayZero;
	}

}