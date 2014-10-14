package cn.com.youtong.statistics;

/**
 * ���ϵ��
 *
 * <p>
 * ������֮����س̶ȡ�
 *
 * <br/>
 * ע�⣺����������ǿ�����ϵ������һ��������������ʵ�����о���
 * ��Ȼ����ϵ��
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class Correlation
{
	private final double coefficient; // ���ϵ��

    public Correlation( final double[] xArray, final double[] yArray )
	{
		// �������
		if( xArray.length != yArray.length )
			throw new IllegalArgumentException( "Argument Arrays Should Be Same Size" );

		// xy��Э���x�����y����
		double dSxy = SAMath.covariance( xArray, yArray );
		double dSxx = SAMath.variance( xArray );
		double dSyy = SAMath.variance( yArray );

		coefficient = dSxy / ( Math.sqrt( dSxx) * Math.sqrt( dSyy ) );
    }

	/**
	 * �������ϵ��
	 * @return              ���ϵ��
	 */
	public double coefficient()
	{
		return coefficient;
	}
}