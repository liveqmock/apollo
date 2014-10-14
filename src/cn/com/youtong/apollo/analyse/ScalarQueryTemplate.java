package cn.com.youtong.apollo.analyse;

import java.util.Iterator;
import cn.com.youtong.apollo.analyse.form.*;

/**
 * 指标查询模板接口
 */
public interface ScalarQueryTemplate
{
    /**
     * 得到模板名称
     * @return 模板名称
     */
    public String getName();

    /**
     * 得到查询条件
     * @return 查询条件
     * @throws AnalyseException
     */
    public ScalarQueryForm getCondition() throws AnalyseException;

    /**
     * 得到模板ID
     * @return 模板ID
     */
    public Integer getTemplateID();

	/**
	 * 得到表头
	 * @return           表头
	 * @throws AnalyseException
	 */
	public HeadForm getHead() throws AnalyseException;

	/** 宽度数组 */
	public int[] getColWidths() throws AnalyseException;
	/** 高度数组 */
	public int[] getRowHeights() throws AnalyseException;

	/** 体部分 */
	public BodyForm getBody() throws AnalyseException;
	/** 根据字体ID，取得Font。如果没有定义返回null */
	public FontForm getFont( Integer fontID ) throws AnalyseException;

	public Iterator getFonts() throws AnalyseException;

	public PrintInformationForm getPrintInformation() throws AnalyseException;
}