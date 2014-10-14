package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * 修正指数曲线
 * 方程： Y = k + a * Math.pow( b, t )；
 *
 * <p>
 * 输入：连续的各月（或者季度）的指标值
 * 输出：对应的修正曲线拟合方程
 * 图形表示：散点和修正曲线图
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class ReviseExponent implements CurveEquation
{


	private final double[] yArray;

	private final double b, k, a;

    public ReviseExponent( final double[] yArray )
	{
		// 检验参数
		if( yArray.length < 3 )
			throw new IllegalArgumentException( "Array's size should larger than 3" );

		this.yArray = yArray;

		// 把数组均匀分成三等分，然后求出各等分的和
		int partSize = yArray.length / 3;
		double dS1 = 0;
		double dS2 = 0;
		double dS3 = 0;

		for( int i=0; i<partSize; i++ )
		{
			dS1 = dS1 + yArray[ i ];
			dS2 = dS2 + yArray[ i + partSize ];
			dS3 = dS3 + yArray[ i + 2*partSize ];
		}

		double bnpow = ( dS3 - dS2 ) / ( dS2 - dS1 ); // b^partSize（b的partSize次方）
		b = Math.pow( bnpow, 1.0/partSize );

		k = ( 1.0/partSize ) * (dS1 - ( dS2 - dS1 )/( bnpow - 1 ) );

		a = ( dS2 - dS1 ) * ( ( b - 1 )/ Math.pow( bnpow -1, 2 ) );
    }

	/**
	 * 作修正指数曲线图
	 * @param width                 图片宽度
	 * @param height                图片高度
	 * @param title                 图片标题
	 * @param xLabel                x轴名称
	 * @param yLabel                y轴名称
	 * @return                      修正指数曲线图
	 * @throws SAException
	 */
	public CurveDrawer getCurveDrawer( int width, int height, String title,
									String xLabel, String yLabel )
	   throws SAException
	{
		int minX = 0;
		int maxX = yArray.length - 1;

		double bottonSideY = value( 0 );
		double topSideY = value( yArray.length + 1 );

		double minY = Math.min( bottonSideY, topSideY );
		double maxY = Math.max( bottonSideY, topSideY );

		double[] xArray = new double[ yArray.length ];
		for( int i=0; i<xArray.length; i++ )
		{
			xArray[i] = i;
		}

		CurveDrawer helper = new CurveDrawer( width, height,
											this,
											minX, maxX,
											minY, maxY,
											xArray, yArray,
											title,
											xLabel, yLabel);

		return helper;
	}

	private void testAffineTransform(AffineTransform tf, double deltaX, double deltaY )
	{
		System.out.println("(0, 0) ===>?" );
		Point2D ptSrc = new Point2D.Double( 0, 0 );
		Point2D ptDst = new Point2D.Double();
		tf.transform( ptSrc, ptDst );
		System.out.println( "ptDst.X " + ptDst.getX()
							   + " ptDst.Y " + ptDst.getY() );

		System.out.println("(0, deltaY) ===>?");
		ptSrc = new Point2D.Double( 0, deltaY );
		ptDst = new Point2D.Double();
		tf.transform( ptSrc, ptDst );
		System.out.println( "ptDst.X " + ptDst.getX()
							   + " ptDst.Y " + ptDst.getY() );

		System.out.println("(-deltaX,0) ====>?");
		ptSrc = new Point2D.Double( -deltaX, 0 );
		ptDst = new Point2D.Double();
		tf.transform( ptSrc, ptDst );
		System.out.println( "ptDst.X " + ptDst.getX()
							   + " ptDst.Y " + ptDst.getY() );

		System.out.println("(-deltaX, deltaY) ===>");
		ptSrc = new Point2D.Double( -deltaX, deltaY );
		ptDst = new Point2D.Double();
		tf.transform( ptSrc, ptDst );
		System.out.println( "ptDst.X " + ptDst.getX()
							   + " ptDst.Y " + ptDst.getY() );

	}

	/**
	 * 返回方程中的k值，常数项
	 * @return           k值
	 */
	public double constantTerm()
	{
		return k;
	}

	/**
	 * 对应方程中的b值，底数
	 * @return        b值
	 */
	public double base()
	{
		return b;
	}

	/**
	 * 对应方程中的a值，幂系数
	 * @return        a值
	 */
	public double coefficient()
	{
		return a;
	}

	public double value( double x )
	{
		return ( k + a * Math.pow( b, x ) );
	}

	public static void main( String[] args )
	{
		double[] xArray = { 9.0, 15.0, 17.0, 20.0, 22.0,
						  23.5, 24.0, 26.8, 27.6, 27.0,
						  29.0, 28.4};
		ReviseExponent re = new ReviseExponent( xArray );

		try
		{
			CurveDrawer drawer = re.getCurveDrawer( 300, 300, "第一张图", "时间（年）", "收入（元）" );

			drawer.getAxisProperties().setAxisColor( Color.red );

			drawer.getCurveProperties().setCurveColor( Color.GREEN );
			drawer.getCurveProperties().setStartPointX( -2 );
			drawer.getCurveProperties().setEndPointX( 24 );

			drawer.getDistributePointProperties().setDisPointColor( Color.YELLOW );
			drawer.getDistributePointProperties().setPointRadius( 5 );


			Image image = drawer.draw();

			javax.imageio.ImageIO.write( ( BufferedImage ) image, "jpg",
										 new java.io.File( "re.jpg" ) );
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}

	}
}