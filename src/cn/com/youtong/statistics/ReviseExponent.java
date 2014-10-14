package cn.com.youtong.statistics;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * ����ָ������
 * ���̣� Y = k + a * Math.pow( b, t )��
 *
 * <p>
 * ���룺�����ĸ��£����߼��ȣ���ָ��ֵ
 * �������Ӧ������������Ϸ���
 * ͼ�α�ʾ��ɢ�����������ͼ
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
		// �������
		if( yArray.length < 3 )
			throw new IllegalArgumentException( "Array's size should larger than 3" );

		this.yArray = yArray;

		// ��������ȷֳ����ȷ֣�Ȼ��������ȷֵĺ�
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

		double bnpow = ( dS3 - dS2 ) / ( dS2 - dS1 ); // b^partSize��b��partSize�η���
		b = Math.pow( bnpow, 1.0/partSize );

		k = ( 1.0/partSize ) * (dS1 - ( dS2 - dS1 )/( bnpow - 1 ) );

		a = ( dS2 - dS1 ) * ( ( b - 1 )/ Math.pow( bnpow -1, 2 ) );
    }

	/**
	 * ������ָ������ͼ
	 * @param width                 ͼƬ���
	 * @param height                ͼƬ�߶�
	 * @param title                 ͼƬ����
	 * @param xLabel                x������
	 * @param yLabel                y������
	 * @return                      ����ָ������ͼ
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
	 * ���ط����е�kֵ��������
	 * @return           kֵ
	 */
	public double constantTerm()
	{
		return k;
	}

	/**
	 * ��Ӧ�����е�bֵ������
	 * @return        bֵ
	 */
	public double base()
	{
		return b;
	}

	/**
	 * ��Ӧ�����е�aֵ����ϵ��
	 * @return        aֵ
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
			CurveDrawer drawer = re.getCurveDrawer( 300, 300, "��һ��ͼ", "ʱ�䣨�꣩", "���루Ԫ��" );

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