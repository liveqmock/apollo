package cn.com.youtong.apollo.task;

/**
 * 报表类型.
 */
public interface ReportType
{
	/** 单户表 */
	static final String GRASS_ROOT_TYPT = "0";
	/** 集团差额表 */
	static final String GROUP_DIFF_TYPE = "1";
	/** 金融并企业表 */
	static final String FINANCE_MERGE_TYPE = "2";
	/** 境外并企业表 */
	static final String FOREIGN_MERGE_TYPE = "3";
	/** 事业并企业表 */
	static final String ENTERPRISE_MERGE_TYPE = "4";
	/** 基建并企业表 */
	static final String BASIC_MERGE_TYPE = "5";
	/** 完全汇总表 */
	static final String FULL_GATHER_TYPE = "7";
	/** 集团汇总表 */
	static final String GROUP_GATHER_TYPE = "9";
	/** 选择汇总表 */
	static final String SELECT_GATHER_TYPE = "H";
}
