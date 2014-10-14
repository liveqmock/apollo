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
 * ָ���ѯģ��ӿڵ����ݿ�ʵ��
 */
public class DBScalarQueryTemplate
    implements ScalarQueryTemplate
{
/**
	 * ģ��form
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
			throw new AnalyseException( "ģ���ʽ���Ʋ���ȷ", ex );
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
	 * �õ�ģ��ID
	 * @return ģ��ID
	 */
	public Integer getTemplateID() {
		return templateID;
	}

	/**
	 * �õ�ģ������
	 * @return ģ������
	 */
	public String getName() {
		return name;
	}

	/**
	 * �õ���ѯ����
	 * @return ��ѯ����
	 * @throws AnalyseException
	 */
	public ScalarQueryForm getCondition()
		throws AnalyseException {
		return this.condition;
	}

	/**
	 * ��ָ���ѯģ��castor����ת��Ϊ��ѯ��������
	 * @param castorTemplate ָ���ѯģ��castor����
	 * @return ��ѯ��������
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

	/** ������� */
	public int[] getColWidths()
		throws AnalyseException {
		return this.colWidths;
	}

	/** �߶����� */
	public int[] getRowHeights()
		throws AnalyseException {
		return this.rowHeigths;
	}

	/** �岿�� */
	public BodyForm getBody()
		throws AnalyseException {
		return body;
	}

	/** ��������ID��ȡ��Font�����û�ж��巵��null */
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
