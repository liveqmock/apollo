package cn.com.youtong.statistics;

import java.awt.Image;

/**
 * 移动平均法。
 * 计算方法：
 * <ol>
 * <li>根据历年各月或者各季的资料，安装步长为12月或者4季的平均移动数，
 *     得到趋势值 T ；（需要两次平移）</li>
 * <li>将实际值 Y 除以趋势值 T ，得到已不包含趋势值的变动数值Y / T ；</li>
 * <li>根据简单平均法计算季节指数</li>
 * </ol>
 *
 * <p>
 * 和简单平均的区别在于，剔除了长期趋势的影响。
 *
 * <p>
 * 输入：连续的各月（或者季度）的指标值
 * 输出：各月（季节）指数
 * 图形表示：表格，给出各个季节指数
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author jbwang
 * @version 1.0
 */
public class MovingAverage
{
	private final double[] seasonIndex;
	/**
	 * 提供季节值，和按月计算还是按季度计算。
	 * @param yArray                      季节值数组
	 * @param period                      如果为4，表示按季度进行平均；
	 *                                    如果为12，表示按月进行平均。
	 */
	public MovingAverage( final double[] yArray, final int period )
	{
		if( yArray.length <= period )
		{
			throw new IllegalArgumentException(
				"Array size should larger than " + period );
		}

		// 平移中间值得到的数组值
		double[] moving1 = new double[yArray.length - period + 1 ];

		// 一次平移
		for( int i = 0; i < moving1.length; i++ )
		{
			// 累加
			for( int j = i, max = i + period; j < max; j++ )
			{
				moving1[i] = moving1[i] + yArray[j];
			}

			// 平均
			moving1[i] = moving1[i] / period;
		}

		// 二次平移
		double[] moving2 = new double[ moving1.length - 1];
		for( int i=0; i<moving2.length; i++ )
		{
			moving2[i] = ( moving1[i] + moving1[i+1] ) / 2;
		}

		// 计算不含趋势变动的值
		for( int i=0; i<moving2.length; i++ )
		{
			moving2[i] = yArray[i + period/2] / moving2[i];
		}

		// 利用简单平均法计算
		SimpleAverage simple = new SimpleAverage( moving2, period );
		double[] result = simple.seaseonIndex();

		// 转移一下数组
		double[] shiftResult = new double[result.length];
		for( int i=0; i<shiftResult.length; i++ )
		{
			int position = i + period/2;
			if( position >= shiftResult.length )
				position -= shiftResult.length;

			shiftResult[i] = result[ position ];
		}

		seasonIndex = shiftResult;
	}


	/**
	 * 季节指数
	 * @return         季节指数
	 */
	public double[] seaseonIndex()
	{
		return seasonIndex;
	}
}