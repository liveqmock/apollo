package cn.com.youtong.apollo.analyse.db;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;

import cn.com.youtong.apollo.analyse.AnalyseException;
import cn.com.youtong.apollo.analyse.ScalarQueryTemplate;
import cn.com.youtong.apollo.analyse.form.*;
import cn.com.youtong.apollo.common.*;

import cn.com.youtong.apollo.analyse.xml.*;

import org.exolab.castor.xml.*;

/**
 * 指标查询模板接口的数据库实现
 */
public class DBScalarQueryTemplate
    implements ScalarQueryTemplate
{
/**
	 * 模板form
	 */
	//private ScalarQueryTemplateForm form;
	private Integer templateID;
	private String name;

	private HashMap fontMap;
	private int[] colWidths;
	private int[] rowHeigths;
	private ScalarQueryForm condition;
	private HeadForm head;
	private BodyForm body;
	private PrintInformationForm printInfo;

	public DBScalarQueryTemplate( ScalarQueryTemplateForm form )
		throws AnalyseException
	{
		this.templateID = form.getTemplateID();
		this.name = form.getName();

		cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate xmlTemplate = null;

		try
		{
			xmlTemplate =
				cn.com.youtong.apollo.analyse.xml.ScalarQueryTemplate.unmarshal(
				form.getContent().getCharacterStream() );
		}
		catch( Exception ex )
		{
			throw new AnalyseException( "模板格式估计不正确", ex );
		}

		String strColWidth = xmlTemplate.getColWidth().getValue();
		String strRowHeight = xmlTemplate.getRowHeight().getValue();

		colWidths = Convertor.parseArray( strColWidth, "," );
		rowHeigths = Convertor.parseArray( strRowHeight, "," );

		condition = castor2Condition( xmlTemplate );
		head = new HeadForm( xmlTemplate.getHead() );
		body = new BodyForm( xmlTemplate.getBody() );
		printInfo = new PrintInformationForm( xmlTemplate.getPrintInformation() );

		fontMap = new HashMap();

		Font[] fonts = xmlTemplate.getFonts().getFont();
		for( int i=0; i<fonts.length; i++ )
		{
			Font font = fonts[i];

			fontMap.put( new Integer( font.getID() ), new FontForm( font ) );
		}
	}

	/**
	 * 得到模板ID
	 * @return 模板ID
	 */
	public Integer getTemplateID() {
		return templateID;
	}

	/**
	 * 得到模板名称
	 * @return 模板名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 得到查询条件
	 * @return 查询条件
	 * @throws AnalyseException
	 */
	public ScalarQueryForm getCondition()
		throws AnalyseException {
		return this.condition;
	}

	/**
	 * 将指标查询模板castor对象转换为查询条件对象
	 * @param castorTemplate 指标查询模板castor对象
	 * @return 查询条件对象
	 */
	private ScalarQueryForm castor2Condition( cn.com.youtong.apollo.analyse.xml.
											  ScalarQueryTemplate
											  castorTemplate ) {
		ScalarQueryForm result = new ScalarQueryForm();

		//taskID
		result.setTaskID( castorTemplate.getTaskID() );

		//scalar
		ScalarForm[] scalars = new ScalarForm[0];
		Row[] rows = castorTemplate.getBody().getRow();
		for ( int i = 0; i < rows.length; i++ ) {
			int scalarNum = 0;

			Row row = rows[i];
			Cell[] cells = row.getCell();

			for ( int j = 0; j < cells.length; j++ ) {
				Cell cell = cells[j];
				if ( !cn.com.youtong.apollo.common.Util.isEmptyString(
					cell.getExpression() ) ) {
					scalarNum++;
				}
			}

			if ( scalarNum > 0 ) {
				scalars = new ScalarForm[scalarNum];
				int currIndex = 0;
				for ( int j = 0; j < cells.length; j++ ) {
					Cell cell = cells[j];
					if ( !cn.com.youtong.apollo.common.Util.isEmptyString(
						cell.getExpression() ) ) {
						scalars[currIndex++] = new ScalarForm(
							cell.getLabel(),
							cell.getExpression() );
					}
				}

				break;
			}
		}

		result.setScalars( scalars );

		return result;
	}

	public HeadForm getHead()
		throws AnalyseException {
		return head;
	}

	/** 宽度数组 */
	public int[] getColWidths()
		throws AnalyseException {
		return this.colWidths;
	}

	/** 高度数组 */
	public int[] getRowHeights()
		throws AnalyseException {
		return this.rowHeigths;
	}

	/** 体部分 */
	public BodyForm getBody()
		throws AnalyseException {
		return body;
	}

	/** 根据字体ID，取得Font。如果没有定义返回null */
	public FontForm getFont( Integer fontID )
		throws AnalyseException {

		FontForm font = ( FontForm ) fontMap.get( fontID );

		return font;
	}

	public Iterator getFonts()
		throws AnalyseException {
		return fontMap.values().iterator();
	}

	public PrintInformationForm getPrintInformation()
		throws AnalyseException {
		return printInfo;
	}
}
