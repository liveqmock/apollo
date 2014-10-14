package cn.com.youtong.statistics;

/**
 * 相关系数
 *
 * <p>
 * 两变量之间相关程度。
 *
 * <br/>
 * 注意：两变量具有强的相关系数，不一定代表两者在现实生活中具有
 * 必然的联系。
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
	private final double coefficient; // 相关系数

    public Correlation( final double[] xArray, final double[] yArray )
	{
		// 检验参数
		if( xArray.length != yArray.length )
			throw new IllegalArgumentException( "Argument Arrays Should Be Same Size" );

		// xy的协方差，x方差和y方差
		double dSxy = SAMath.covariance( xArray, yArray );
		double dSxx = SAMath.variance( xArray );
		double dSyy = SAMath.variance( yArray );

		coefficient = dSxy / ( Math.sqrt( dSxx) * Math.sqrt( dSyy ) );
    }

	/**
	 * 返回相关系数
	 * @return              相关系数
	 */
	public double coefficient()
	{
		return coefficient;
	}
}