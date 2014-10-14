package cn.com.youtong.apollo.task;

/**
 * <p>Title: 单元格</p>
 * <p>Description:
 * <ul>内容属性
 * <li>1．	数据类型：数值，文本，大文本（clob），日期，二进制(blob)
 * <li>2．	数据库字段名称
 * <li>3．	参与汇总
 * <li>4．	金额指标（元，万元转换）
 * <li>5．	财务指标
 * </ul>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:北京世纪友通科技有限公司 </p>
 * @author zhou
 * @version 1.0
 */
public interface Cell extends CellDataType
{
	/** 是否汇总 */
	static final int FLAG_SUM = 0x00000001;
	/** 是否参与金额转换 */
	static final int FLAG_MONEY = 0x00000002;

	/**
	 * 数据类型.
	 * @see CellDataType
	 * @return
	 */
	int getDataType();

    /**
     * 标签
     * @return 标签
     */
    String getLabel();

	/**
	 * 数据库字段名称.
	 * @return
	 */
	String getDBFieldName();

	/**
	 * 单元格标志
	 * @param flag
	 * @return
	 */boolean getFlag(int flag);

	/**
	 * 取单元格对应的代码字典
	 * @return
	 */
	String getDictionaryID();

	/**
	 * 用户可读的名字，如："货币资金 账面数"
	 * @return
	 */String getReadableName();
}