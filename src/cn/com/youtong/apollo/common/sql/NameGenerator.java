package cn.com.youtong.apollo.common.sql;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.task.*;

/**
 * 名称生成器
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author wjb & mk
 * @version 1.0
 */
public class NameGenerator
{
	private static Log log = LogFactory.getLog(NameGenerator.class);
	/** 单元格的类型---数值型 */
	public static final int CELL_TYPE_NUMBER = 1;
	/** 单元格的类型---文本型 */
	public static final int CELL_TYPE_STRING = 2;
	/** 单元格的类型---二进制 */
	public static final int CELL_TYPE_BINARY = 3;
	/** 单元格的类型---大文本 */
	public static final int CELL_TYPE_BLOCK = 4;
	/** 单元格的类型---日期型 */
	public static final int CELL_TYPE_DATETIME = 5;

	/**
	 * 根据任务ID与表ID生成数据表的名称
	 *
	 * @param taskID
	 *            任务ID
	 * @param tableID
	 *            表ID
	 * @return 数据表的名称
	 */
	public static String generateDataTableName(String taskID, String tableID)
	{
		return "YTAPL_" + taskID + "_" + tableID;
	}

    /**
     * 根据任务ID生成存放报表数据附件的表的名称
     *
     * @param taskID 任务ID
     *
     * @return 存放报表数据附件的表的名称
     */
    public static String generateAttachmentTableName(String taskID)
    {
        return "YTAPL_" + taskID + "_Attachement";
    }

	/**
	 * 根据代码字典id生成代码字典表的名称
	 *
	 * @param dictid
	 *            代码字典id
	 * @return 代码字典表的名称
	 */
	public static String generateDictionaryTableName(String dictid)
	{
		return "YTAPL_Dict" + "_" + dictid;
	}

	/**
	 * 产生浮动表表名
	 *
	 * @param taskID
	 *            任务标识
	 * @param tableID
	 *            表标识
	 * @param rowID
	 *            行标识
	 * @return 按照一定规则拼凑的浮动表名
	 */
	public static String generateFloatDataTableName(String taskID, String tableID, String rowID)
	{
		return "YTAPL_" + taskID + "_" + tableID + "_" + rowID;
	}

	/**
	 * 提供单位代码和报表类型，产生(单位树)单位代码
	 *
	 * @param unitCode
	 *            单位代码
	 * @param reportType
	 *            报表类型
	 * @return (单位树)单位代码
	 */
	public static String generateTreeUnitID(String unitCode, String reportType)
	{
		return unitCode + reportType;
	}

	/**
	 * 提供单位代码和报表类型，产生(单位树)上级单位代码
	 *
	 * @param parentUnitCode
	 *            上级单位代码
	 * @return (单位树)上级单位代码
	 */
	public static String generateTreeParentUnitID(String parentUnitCode)
	{
		return parentUnitCode + ReportType.GROUP_GATHER_TYPE;
	}

	/**
	 * 生成上报的压缩文件的文件名
	 * @param fileName 上报的文件名
	 * @return 创建的新文件名
	 */
	public static String gererateZipDataFileName(String fileName)
	{
		log.info("上报后的文件名为：" + fileName + "_YTAPL_" + System.currentTimeMillis());
		return fileName + "_YTAPL_" + System.currentTimeMillis();
	}

	/**
	 * 获得上报文件的原名
	 * @param fileName 修改后的文件名
	 * @return 原文件名
	 */
	public static String getZipDataFileName(String fileName)
		throws Warning
	{

		int position = fileName.indexOf("_YTAPL_");
		if(position <= 0)
		{
			throw new Warning("文件名不合法：" + fileName);
		}
		System.out.println("获得的文件名为：" + fileName.substring(0, position));
		return fileName.substring(0, position);
	}
}
