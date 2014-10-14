package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.geom.*;
import java.awt.font.*;

class DrawUtil {
	public DrawUtil()
	{
	}

	// deltaX, deltaY,表示相对图形最顶端偏移了多少
	// 目前没有考虑到直角坐标系不是从原点情况下画的情况
	/**
	 * 画坐标轴
	 *
	 * <p>
	 * 按照画布的大小和XY值显示区域（由minX,maxX,minY,maxY确定），
	 * 显示适当的轴线。
	 *
	 *
	 * @param g               Graphics
	 * @param deltaX          曲线坐标系原点相对画布坐标系的原点X偏移量（画布坐标系单位）
	 * @param deltaY          曲线坐标系原点相对画布坐标系的原点Y偏移量（画布坐标系单位）
	 * @param minX            double
	 * @param maxX            double
	 * @param minY            double
	 * @param maxY            double
	 * @param xLabel          x轴显示标签
	 * @param yLabel          y轴显示标签
	 * @param width           尽量伸长的轴线宽度
	 * @param height          尽量伸长的轴线高度
	 * @param tf              曲线坐标系向画布坐标系转换矩阵
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
		// 箭头属性
		int arrowLength = axisProp.getArrowLength(); // 长度
		int arrowHalfWidth = axisProp.getArrowHalfWidth(); // 宽度

		// 画label的属性
		int labelAxisSpace = axisProp.getLabelAxisSpace(); // label和轴线间距
		int labelMarginSpace = axisProp.getLabelMarginSpace(); // label和边框间距

		int expectPointNum = axisProp.getExpectPointNum(); // 期望在坐标祝上标注几个点
		int axisPointHeight = axisProp.getAxisPointHeight(); // 坐标轴上标注的点高度

		boolean displayZeroInAxisX = displayZeroInAxis( minX, maxX );
		boolean displayZeroInAxisY = displayZeroInAxis( minY, maxY );

		// 从X轴和Y轴交叉点的横竖坐标
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
		// x轴
		path.moveTo( (float) leftP.getX(), (float) leftP.getY() );
		path.lineTo( (float) rightP.getX(), (float) rightP.getY() );
		// 箭头
		path.lineTo( (float) rightP.getX() - arrowLength, (float) rightP.getY() + arrowHalfWidth );
		path.moveTo( (float) rightP.getX(), (float) rightP.getY() );
		path.lineTo( (float) rightP.getX() - arrowLength, (float) rightP.getY() -arrowHalfWidth );

		// y轴
		path.moveTo( (float) bottonP.getX(), (float) bottonP.getY() );
		path.lineTo( (float) topP.getX(), (float) topP.getY() );
		// 箭头
		path.lineTo( (float) topP.getX() + arrowHalfWidth, (float) topP.getY() + arrowLength );
		path.moveTo( (float) topP.getX(), (float) topP.getY() );
		path.lineTo( (float) topP.getX() -arrowHalfWidth, (float) topP.getY() + arrowLength );

		g.draw( path );

		// 画label
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

		// x轴label
		Rectangle2D bounds = labelFont.getStringBounds( xLabel, g.getFontRenderContext() );
		g.setFont( labelFont );
		g.drawString( xLabel, (float) (rightP.getX() - labelMarginSpace - bounds.getWidth() ),
					  (float) (rightP.getY() - labelAxisSpace ) );

		// 画x轴上的坐标点
		double[] drawAxisPointX = getPerfectLocation( minX, maxX, expectPointNum );
		Point2D[] axisPointXSrc = new Point2D[ drawAxisPointX.length ];
		Point2D[] axisPointXDst = new Point2D[ drawAxisPointX.length ];

		for( int i=0; i<axisPointXSrc.length; i++ )
		{
			axisPointXSrc[i] = new Point2D.Double( drawAxisPointX[i], injectPointSrc.getY() );
		}

		tf.transform( axisPointXSrc, 0, axisPointXDst, 0, axisPointXSrc.length );

		// 在X轴上标注的点名称
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

		// y轴label
		bounds = labelFont.getStringBounds( yLabel, g.getFontRenderContext() );
		g.setFont( labelFont );
		g.drawString( yLabel, (float) (topP.getX() + labelAxisSpace ),
					 (float) (topP.getY() + labelMarginSpace + bounds.getHeight() ) );

		// 画y轴上的坐标点
		double[] drawAxisPointY = getPerfectLocation( minY, maxY, expectPointNum );
		Point2D[] axisPointYSrc = new Point2D[ drawAxisPointY.length ];
		Point2D[] axisPointYDst = new Point2D[ drawAxisPointY.length ];

		for( int i=0; i<axisPointYSrc.length; i++ )
		{
			axisPointYSrc[i] = new Point2D.Double( injectPointSrc.getX(), drawAxisPointY[i] );
		}

		tf.transform( axisPointYSrc, 0, axisPointYDst, 0, axisPointYSrc.length );

		// 在Y轴上标注的点名称
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

		// 重置原来的Font
		g.setFont( originFont );
	}

	/**
	 * 给定最大值和最小值，给出具有典型的点分布数组。
	 *
	 * <p>
	 * 比如最小值0和最大值18，期望得到10个代表点，可以给出0,2,4,6,8,10,12,14,16,18这样的数组
	 *
	 * <p>
	 * @todo 如何尽量市分布点向0或5靠近！
	 *
	 * @param min             最小值
	 * @param max             最大值
	 * @param expectNum       期望分布点数
	 * @return                分布点数组
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
				// 同侧分布
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

	// 从x0开始画曲线， x0表示曲线坐标系的x0值，不是指画布坐标系的x0
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
	 * 画散点
	 * @param g             Graphics
 	 * @param xArray        散点的x值数组
	 * @param yArray        散点的y值数组
	 * @param tf            曲线坐标系向画布坐标系转换矩阵
	 */
	public static void drawPoint( Graphics2D g,
								  double[] xArray, double[] yArray,
								  AffineTransform tf,
								  DistributePointProperties disPointProp ) {
		g.setColor( disPointProp.getDisPointColor() );

		// 画点属性
		int pointRadius = disPointProp.getPointRadius();

		// 变换前的点
		Point2D[] ptSrc = new Point2D[ xArray.length ];
		// 变换后的点
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
	 * 在width, heigth画布内，写上标题title
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

		// 文件边框
		Rectangle2D bounds = null;

		boolean changeFontSize = true;
		while( changeFontSize )
		{
			bounds = font.getStringBounds( title,
				g.getFontRenderContext() );
			if ( bounds.getHeight() > height / 4 )
			{
				// 目前选用的字体太大了
				// 减少文字大小，重新再试
				fontSize--;
				font = new Font( fontName, fontStyle, fontSize );
				g.setFont( font );
			}
			else
			{
				changeFontSize = false;
			}
		}

		// 文字框放在离上边缘1/10高度出，水平居中
		int baseX = (int) ( titleProp.getCenterX() - bounds.getWidth()/2 ) ;
		int baseY = (int) ( titleProp.getTopY() + bounds.getHeight() );

		g.drawString( title, baseX, baseY );

		// 重置原来的Font
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

		// Y轴的偏移量需要重新计算
		double deltaY = deltaAndScaleY[0]; // Y轴偏移量
		double scaleY = deltaAndScaleY[1];

		deltaAndScaleY[0] = height * scaleY - deltaY;

		return deltaAndScaleY;
	}

	/**
	 * 把轴线放在长度为length的线段（或者画布）上，
	 * 为了更好的显示（显示长度占总长度的2/3），需要一定的平移delta和缩放scale。
	 *
	 * @param length              在长度length上面显示
	 * @param min                 显示的最小值
	 * @param max                 显示的最大值
	 * @param displayZero         是否显示0点
	 * @return                    [0]=delta, [1]=scale
	 */
	private static double[] deltaAndScale( double length, double min,
								   double max, boolean displayZero ) {
		double scale = 1; // 每图片单位对应轴线scale单位
		double delta = 0;

		if ( displayZero ) {
			// 坐标轴上显示0点
			if ( min <= 0 && max >= 0 ) {
				// 正负值都存在
				double space = max - min;
				scale = space / ( length * ( 2d / 3 ) );

				delta = length * ( 1d / 6 ) * scale - min;
			} else {
				// 单侧分布

				if ( min <= 0 ) {
					scale = -1 * min / ( length * ( 2d / 3 ) );
					delta = length * ( 1d / 6 ) * scale - min;
				} else {
					scale = max / ( length * ( 2d / 3 ) );
					delta = length * ( 1d / 6 ) * scale;
				}
			}
		} else {
			// 不显示0点
			double space = max - min;
			scale = space / ( length * ( 2d / 3 ) );

			delta = length * ( 1d / 6 ) * scale - min;
		}

		return new double[] {
			delta, scale};
	}

	/**
	 * 判断是否要显示0点，根据一下准则：
	 * <ol>
	 * <li>如果数字分布在0的两侧，显示0点；</li>
	 * <li>如果数组单侧分布，最大值和最小值间距，比它们离0点的最小间距的差别不大，显示0点；</li>
	 * <li>剩下就是单侧分布，最大值和最小值间距，比它们离0点的最小间距的差别较大，不显示0点。</li>
	 * </ol>
	 *
	 * <p>
	 * 这里选定差别大小为1.5倍，如果数字离0点的最小间距，大于数字间距的1.5倍。
	 *
	 * @param min         最小值
	 * @param max         最大值
	 * @return            是否显示0点
	 */
	static boolean displayZeroInAxis( double min, double max ) {
		boolean displayZero = true;
		// 如果数字呈正负分布
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