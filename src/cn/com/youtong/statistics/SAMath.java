package cn.com.youtong.statistics;

import java.text.DecimalFormat;

/**
 * 提高一些基础运算的实现。
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
final class SAMath
{
	/**
	 * 对给定数组进行求和
	 * @param array           数组
	 * @return                和
	 */
	public static final double sum( final double[] array )
	{
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			sum += array[i];
		}

		return sum;
	}

	/**
	 * 求出数组的数学平均值
	 * @param array              数组
	 * @return                   平均值
	 */
	public static final double mean( final double[] array )
	{
		return sum( array ) / ( array.length );
	}

	/**
	 * 平均差，Average Deviation。
	 *
	 * <p>
	 *
	 * 计算步骤：
	 * <ol>
	 * <li>求出数组平均值；</li>
	 * <li>求出数组每项和平均值的差值的绝对值和；</li>
	 * <li>绝对值和除以数组大小。</li>
	 * </ol>
	 *
	 * @param array      数组
	 * @return           平均差
	 */
	public static final double averageDeviation( final double[] array )
	{
		double meanValue = mean( array );

		// 相加数组每项和平均值的差的绝对值
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			sum = sum + Math.abs( array[i] - meanValue );
		}

		return sum / ( array.length );
	}

	/**
	 * 方差。
	 *
	 * <p>
	 *
	 * 计算步骤：
	 * <ol>
	 * <li>计算平均值；</li>
	 * <li>数组每项和平均值的差的平方和；</li>
	 * <li>平方和除以数组大小减去1。</li>
	 * </ol>
	 *
	 * @param array       数组
	 * @return            方差
	 */
	public static final double variance( final double[] array )
	{
		double mean = mean( array );

		// 把数组每项和平均值的差值，平方，然后全部相加
		double sum = 0;
		for( int i=0; i<array.length; i++ )
		{
			double diff = array[i] - mean;
			sum = sum + diff * diff;
		}

		return sum / ( array.length - 1 );
	}

	/**
	 * 样本协方差。
	 * @param xArray                数组x
	 * @param yArray                数组y
	 * @return                      样本协方差
	 */
	public static final double covariance( final double[] xArray,
										   final double[] yArray )
	{
		double xMean = mean( xArray );
		double yMean = mean( yArray );

		double[] xDev = new double[ xArray.length ];
		double[] yDev = new double[ yArray.length ];

		for( int i=0; i<xDev.length; i++ )
		{
			xDev[i] = xArray[i] - xMean;
			yDev[i] = yArray[i] - yMean;
		}

		double lxy = 0;

		for( int i=0; i<xDev.length; i++ )
		{
			lxy = lxy + ( xDev[i] * yDev[i] );
		}

		return lxy / ( xArray.length - 1 );
	}

	/**
	 * 标准方差（Standard Deviation）。又叫S<font size="-1">n-1</font>
	 *
	 * <p>
	 *
	 * 计算步骤：
	 * <ol>
	 * <li>求出方差；</li>
	 * <li>开平方。</li>
	 * </ol>
	 *
	 * @param array              数组
	 * @return                   标准方差
	 */
	public static final double standardDeviation( final double[] array )
	{
		double variance = variance( array );

		return Math.sqrt( variance );
	}

	/**
	 * 对dValue保留一位有效数字
	 * @param dValue        原数值
	 * @return    保留一位有效数字的值
	 */
	public static double retainOneSignificantDigit( double dValue )
	{
		String s = String.valueOf( dValue );
		StringBuffer buff = new StringBuffer();

		for( int i=0, size=s.length(); i<size; i++ )
		{
			char current = s.charAt( i );

			if( current<='9' && current>='1' )
			{
				if( i == size-1 )
				{
					// 字符串解析完毕
					buff.append( current );
				}
				else
				{
					// 对紧接着的字符，进行四舍五入
					char next = s.charAt( i + 1 );
					if( next =='.' )
					{
						++i;
						next = s.charAt( i + 1 );
					}

					if( next < '5' )
					{
						buff.append( current );
					} else
					{
						current = ( char ) ( current + 1 );
						buff.append( current );
					}

					// 查看是否是在小数部分
					int indexOfDot = s.indexOf( '.' );
					if( indexOfDot > i )
					{
						// 表示还没有到小数部分
						for( int j = i + 1; j < indexOfDot; j++ )
						{
							buff.append( '0' );
						}
					}

					// 查看是否有科学技术法
					int indexOfE = s.indexOf( 'E' );
					if( indexOfE == -1 )
					{
						// 查看字符e
						indexOfE = s.indexOf( 'e' );
					}

					if( indexOfE > 0 )
					{
						buff.append( s.substring( indexOfE ) );
					}
				}

				break;
			}
			else
			{
				buff.append( current );
			}
		}

		String result = buff.toString();

		double dResult = Double.parseDouble( result );

		return dResult;
	}

	/**
	 * 查找数组中最大值和最小值
	 * @param array       数组
	 * @return            最大值=double[0]；最小值=double[1]
	 */
	public static double[] maxAndMin( double[] array )
	{
		// 数组中最大值和最小值
		double min = array[0];
		double max = array[0];

		for( int i=1; i<array.length; i++ )
		{
			if ( array[i] > max )
			{
				max = array[i];
			}
			else if( array[i] < min )
			{
				min = array[i];
			}
		}

		return new double[]{ max, min };
	}

	/**
	 * 对输入数组进行适当的格式化，最多保留4位有效数字
	 * @param input          连续均匀分布的double数组
	 * @return               格式化的字符串数组
	 */
	public static String[] format( double[] input )
	{
		String[] output = new String[ input.length ];

		String pattern = "0.####";
		if( output.length >= 1 )
		{
			double step = input[1] - input[0];
			step = retainOneSignificantDigit( step );

			// 如果有科学计数法，使用科学计数法
			String sStep = Double.toString( step );
			if( sStep.indexOf( 'E' ) > 0
				|| sStep.indexOf( 'e' ) > 0 )
			{
				pattern = pattern + "E0";
			}
		}

		DecimalFormat dFormat = new DecimalFormat( pattern );
		for( int i=0; i<input.length; i++ )
		{
			output[i] = dFormat.format( input[i] );
		}

		return output;
	}
	/**
	 * （修正的 Bias Corrected Standard Deviation）标准方差。
	 * 又叫S<font size="-1">n-1</font>
	 *
	 * <p>
	 *
	 * 和标准方差的区别是，标准方差得到的是除以n的差平方和，然后开平方根。
	 * 修正标准方差，是除以n-1。
	 *
	 * @param array                  数组
	 * @return                       （修正的）标准方差
	 */
	/**public static final double bcStandardDeviation( final double[] array )
	{
		double bcvariance = variance( array ) * ( array.length ) / ( array.length - 1 );

		return Math.sqrt( bcvariance );
	}*/
}