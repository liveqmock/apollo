package cn.com.youtong.apollo.analyse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// apache
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.Region;
// apollo
import cn.com.youtong.apollo.analyse.form.*;
import cn.com.youtong.apollo.common.Convertor;
import cn.com.youtong.apollo.common.Util;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * ��excel�ļ������ѯģ��ִ�н����
 *
 * <p>
 * Workbook, WorkSheet����Ϊ����ͨ�����������롣
 * ͬʱָ����WorkSheet��Workbook�е�index��
 *
 * @todo Font��Colorû�п���
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yutop</p>
 * @author wjb
 * @version 1.0
 */
class TemplateResultExporter {
	private static float TASKTIME_ROW_HEIGHT_IN_POINTS = 20.0f;
	private static float INDEX_COL_WIDTH_IN_POINTS = 150.0f;
	private static float UNITNAME_COL_WIDTH_IN_POINTS = 400.0f;

	private ScalarQueryTemplate template;
	//private OutputStream out;
	private TaskTime[] taskTimes;
	/** ˮƽ��������ݾ��� */
	private Object[][] tableData;
	private boolean showIndex;
	private boolean showUnitName;
	private UnitTreeNode[] units;

	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private int sheetIndex;
	private Map styleMap;

	public TemplateResultExporter( ScalarQueryTemplate template,
								   //OutputStream out,
								   TaskTime[] taskTimes, Object[][] tableData,
								   boolean showIndex, boolean showUnitName,
								   UnitTreeNode[] units,
								   HSSFWorkbook workbook,
								   HSSFSheet sheet,
								   int sheetIndex ) {
		this.template = template;
		//this.out = out;
		this.taskTimes = taskTimes;
		this.tableData = tableData;
		this.showIndex = showIndex;
		this.showUnitName = showUnitName;
		this.units = units;
		this.workbook = workbook;
		this.sheet = sheet;
		this.sheetIndex = sheetIndex;
	}

	/**public static void main( String[] args )
		throws Exception {
		cn.com.youtong.apollo.MockService.init();

		AnalyseManager anlyMng = ( ( AnalyseManagerFactory )
								   cn.com.youtong.apollo.services.Factory.
								   getInstance(
			AnalyseManagerFactory.class.getName() ) ).createAnalyseManager();
		ScalarQueryTemplate template = anlyMng.getScalarQueryTemplate( new
			Integer( 9 ) );
		//TemplateResultExporter templateResultExporter1 = new TemplateResultExporter();
		cn.com.youtong.apollo.task.db.DBTaskManager taskMng =
			new cn.com.youtong.apollo.task.db.DBTaskManager();
		Task task = taskMng.getTaskByID( "QYKB" );
		TaskTime[] times = new TaskTime[] {task.getTaskTime( new Integer( 134 ) ),
						   task.getTaskTime( new Integer( 135 ) ),
						   task.getTaskTime( new Integer( 136 ) )};
		OutputStream out = new java.io.FileOutputStream( "f:\\test.xls" );

		cn.com.youtong.apollo.data.UnitTreeManager treeMng =
			( ( cn.com.youtong.apollo.data.ModelManagerFactory )
			  cn.com.youtong.apollo.services.Factory.getInstance(
			cn.com.youtong.apollo.data.ModelManagerFactory.class.getName() ) ).
			createModelManager( "QYKB" ).getUnitTreeManager();

		Collection coll = treeMng.getUnits( new String[] {"1000168510",
											"1000168519", "1031277410"} );
		UnitTreeNode[] units = null;
		units = ( UnitTreeNode[] ) coll.toArray( new UnitTreeNode[0] );

		Object[][] tableData = new String[][] { {"12.5", "20.5", "45", "55",
							   "22", "33", "44", "556",
							   "43", "32", "21", "10"},
							   {"13.5", "56.5", "45.2", "34",
							   "22", "33", "44", "556",
							   "43", "32", "21", "10"},
							   {"12.5", "56.5", "45.2", "34",
							   "22", "55", "44", "556",
							   "43", "32", "21", "10"} };

		TemplateResultExporter export
			= new TemplateResultExporter( template, times, tableData, true, true,
										  units );
		export.exportExcel( out );
		out.close();

		cn.com.youtong.apollo.MockService.shutdown();
	}*/

	   /**
		* ����sheet�����ƣ�Ȼ������sheet��Ҫ�õ��ĸ���Font
		*/
	void prepareSheet()
		throws AnalyseException {

		workbook.setSheetName( sheetIndex, template.getName(),
							   HSSFWorkbook.ENCODING_UTF_16 );

		// �����õ����������
		Map fontMap = new HashMap();
		for ( Iterator fontIter = template.getFonts(); fontIter.hasNext(); ) {
			FontForm form = ( FontForm ) fontIter.next();
			HSSFFont font = workbook.createFont();
			font.setFontName( form.getName() );
			font.setFontHeightInPoints( ( short ) form.getSize() );
			if ( form.isBold() )
				font.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );
			if ( form.isIsUnderline() )
				font.setUnderline( HSSFFont.U_SINGLE );
			font.setItalic( form.isItalic() );
			font.setStrikeout( form.isStrikeThrough() );
			/**@todo ������ɫ */
			//font.setColor( );

			fontMap.put( new Integer( form.getID() ), font );
		}

		//  ����styleMap������Ϊnew String( rowIndex, cellIndex )��
		styleMap = new HashMap();
		int rowIndex = 0;
		int cellIndex = 0;

		// head
		HeadForm headForm = template.getHead();
		String defaultColor = headForm.getDefaultColor();
		HSSFFont defaultFont = ( HSSFFont ) fontMap.get( new Integer( headForm.
			getDefaultFontID() ) );

		RowForm[] rowForms = headForm.getRows();

		for ( int i = 0; i < rowForms.length; i++ ) {
			cellIndex = 0;
			if ( showIndex ) {
				if ( i == 0 ) {
					HSSFCellStyle style = workbook.createCellStyle();
					if ( defaultFont != null )
						style.setFont( defaultFont );

						/**@todo color */
					style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
					style.setVerticalAlignment( HSSFCellStyle.
												VERTICAL_BOTTOM );
					style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
					style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
					style.setBorderRight( HSSFCellStyle.BORDER_THIN );
					style.setBorderTop( HSSFCellStyle.BORDER_THIN );
					styleMap.put( rowIndex + "," + cellIndex, style );
				}

				cellIndex++;
			}

			if ( showUnitName ) {
				if ( i == 0 ) {
					HSSFCellStyle style = workbook.createCellStyle();
					if ( defaultFont != null )
						style.setFont( defaultFont );

						/**@todo color */
					style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
					style.setVerticalAlignment( HSSFCellStyle.
												VERTICAL_BOTTOM );
					style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
					style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
					style.setBorderRight( HSSFCellStyle.BORDER_THIN );
					style.setBorderTop( HSSFCellStyle.BORDER_THIN );

					styleMap.put( rowIndex + "," + cellIndex, style );
				}

				cellIndex++;
			}

			CellForm[] cellForms = rowForms[i].getCells();
			for ( int j = 0; j < cellForms.length; j++ ) {
				CellForm cellForm = cellForms[j];
				HSSFFont font = ( HSSFFont ) fontMap.get( new Integer( cellForm.getFontID() ) );
				if( font == null )
					font = defaultFont;

				HSSFCellStyle style = workbook.createCellStyle();
				if( font != null )
					style.setFont( font );
				cellForm.getBgColor();
				/**@todo color*/

				String halign = cellForm.getHalign();
				if( halign == null )
					style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
				else if( halign.equalsIgnoreCase( "center" ) )
					style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
				else if( halign.equalsIgnoreCase( "left" ) )
					style.setAlignment( HSSFCellStyle.ALIGN_LEFT );
				else if( halign.equalsIgnoreCase( "right" ) )
					style.setAlignment( HSSFCellStyle.ALIGN_RIGHT );

				String valign = cellForm.getValign();
				if( valign == null )
					style.setVerticalAlignment( HSSFCellStyle.VERTICAL_BOTTOM );
				else if( valign.equalsIgnoreCase( "bottom" ) )
					style.setVerticalAlignment( HSSFCellStyle.VERTICAL_BOTTOM );
				else if( valign.equalsIgnoreCase( "top" ) )
					style.setVerticalAlignment( HSSFCellStyle.VERTICAL_TOP );
				else if( valign.equalsIgnoreCase( "center" ) )
					style.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );

				style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
				style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
				style.setBorderRight( HSSFCellStyle.BORDER_THIN );
				style.setBorderTop( HSSFCellStyle.BORDER_THIN );

				styleMap.put( rowIndex + "," + cellIndex, style );

				cellIndex+=(taskTimes.length*cellForm.getColspan());
			}

			rowIndex++;
		}

		// body
		BodyForm bodyForm = template.getBody();
		defaultColor = bodyForm.getDefaultColor();
		defaultFont = ( HSSFFont ) fontMap.get( new Integer( bodyForm.
			getDefaultFontID() ) );

		rowIndex = headForm.getRows().length + 1;
		if( taskTimes.length == 1 )
			rowIndex--; // ����ʾ����ʱ����

		cellIndex = 0;

		if ( showIndex ) {
			HSSFCellStyle style = workbook.createCellStyle();
			if ( defaultFont != null )
				style.setFont( defaultFont );

				/**@todo color */
			style.setAlignment( HSSFCellStyle.ALIGN_RIGHT );
			style.setVerticalAlignment( HSSFCellStyle.
										VERTICAL_BOTTOM );

			style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
			style.setBorderRight( HSSFCellStyle.BORDER_THIN );
			style.setBorderTop( HSSFCellStyle.BORDER_THIN );

			styleMap.put( rowIndex + "," + cellIndex, style );
			cellIndex++;
		}

		if ( showUnitName ) {

			HSSFCellStyle style = workbook.createCellStyle();
			if ( defaultFont != null )
				style.setFont( defaultFont );

				/**@todo color */
			style.setAlignment( HSSFCellStyle.ALIGN_LEFT );
			style.setVerticalAlignment( HSSFCellStyle.
										VERTICAL_BOTTOM );
			style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
			style.setBorderRight( HSSFCellStyle.BORDER_THIN );
			style.setBorderTop( HSSFCellStyle.BORDER_THIN );

			styleMap.put( rowIndex + "," + cellIndex, style );
			cellIndex++;
		}

		CellForm[] cellForms = null;
		rowForms = bodyForm.getRows();
		for( int i=0; i<rowForms.length; i++ )
		{
			if( !rowForms[i].isTotalRow() )
			{
				cellForms = rowForms[i].getCells();
				break;
			}
		}

		for ( int j = 0; j < cellForms.length; j++ ) {
			CellForm cellForm = cellForms[j];
			HSSFFont font = ( HSSFFont ) fontMap.get( new Integer( cellForm.getFontID() ) );
			if( font == null )
				font = defaultFont;

			HSSFCellStyle style = workbook.createCellStyle();
			if( font != null )
				style.setFont( font );
			cellForm.getBgColor();
			/**@todo color*/

			String halign = cellForm.getHalign();
			if( halign == null )
				style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			else if( halign.equalsIgnoreCase( "center" ) )
				style.setAlignment( HSSFCellStyle.ALIGN_CENTER );
			else if( halign.equalsIgnoreCase( "left" ) )
				style.setAlignment( HSSFCellStyle.ALIGN_LEFT );
			else if( halign.equalsIgnoreCase( "right" ) )
				style.setAlignment( HSSFCellStyle.ALIGN_RIGHT );

			String valign = cellForm.getValign();
			if( valign == null )
				style.setVerticalAlignment( HSSFCellStyle.VERTICAL_BOTTOM );
			else if( valign.equalsIgnoreCase( "bottom" ) )
				style.setVerticalAlignment( HSSFCellStyle.VERTICAL_BOTTOM );
			else if( valign.equalsIgnoreCase( "top" ) )
				style.setVerticalAlignment( HSSFCellStyle.VERTICAL_TOP );
			else if( valign.equalsIgnoreCase( "center" ) )
				style.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );

			style.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			style.setBorderLeft( HSSFCellStyle.BORDER_THIN );
			style.setBorderRight( HSSFCellStyle.BORDER_THIN );
			style.setBorderTop( HSSFCellStyle.BORDER_THIN );

			for( int i=0; i<taskTimes.length; i++ )
			{
				styleMap.put( rowIndex + "," + cellIndex, style );
				cellIndex++;
			}
		}
	}

	/**
	 * �����ͷ��Ϣ
	 * @throws AnalyseException
	 */
	void printHead()
		throws AnalyseException {
		// declare a row object reference
		HSSFRow r = null;
// declare a cell object reference
		HSSFCell c = null;

		int rowIndex = 0;
// д��ͷ
		RowForm[] rows = template.getHead().getRows();
		int[] colWidths = template.getColWidths();
		int[] rowHeights = template.getRowHeights();

		RowForm rowForm = rows[rowIndex];
		// create a row
		r = sheet.createRow( rowIndex );
		r.setHeightInPoints( ( short ) rowHeights[rowIndex] );

		CellForm[] cellForms = rowForm.getCells();
		int cellIndex = 0;
		int totalCells = cellForms.length;

		String defaultColor = template.getHead().getDefaultColor();

		// ��ź͵�λ����
		if ( showIndex ) {
			totalCells += 1;
			c = r.createCell( ( short ) cellIndex );
			c.setCellType( HSSFCell.CELL_TYPE_STRING );
			c.setEncoding( HSSFCell.ENCODING_UTF_16 );
			c.setCellValue( "���" );

			HSSFCellStyle style = getCellStyle( rowIndex, cellIndex );
			if ( style != null )
				c.setCellStyle( style );

			int endRowIndex = taskTimes.length == 1? rows.length -1: rows.length;
			Region region = new Region( ( short ) rowIndex,
											   ( short ) cellIndex,
											   ( short ) endRowIndex,
											   ( short ) cellIndex );

			// ���ÿ���
			sheet.addMergedRegion( region );

			try
			{
				HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
			}
			catch( Exception ex )
			{
				throw new AnalyseException( "�ϲ���Ԫ�����", ex );
			}

			cellIndex++;
		}

		if ( showUnitName ) {
			totalCells += 1;
			c = r.createCell( ( short ) cellIndex );
			c.setEncoding( HSSFCell.ENCODING_UTF_16 );
			c.setCellValue( "��λ����" );

			HSSFCellStyle style = getCellStyle( rowIndex, cellIndex );
			if ( style != null )
				c.setCellStyle( style );

			int endRowIndex = taskTimes.length == 1? rows.length -1: rows.length;
			Region region = new Region( ( short ) rowIndex,
											   ( short ) cellIndex,
											   ( short ) endRowIndex,
											   ( short ) cellIndex );
				// ���ÿ���
			sheet.addMergedRegion( region );

			try
			{
				HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
			}
			catch( Exception ex )
			{
				throw new AnalyseException( "�ϲ���Ԫ�����", ex );
			}

			cellIndex++;
		}

		for ( int i = 0; i < cellForms.length; i++ ) {
			CellForm cellForm = cellForms[i];
			c = r.createCell( ( short ) cellIndex );
			c.setEncoding( HSSFCell.ENCODING_UTF_16 );
			c.setCellValue( cellForm.getLabel() );

			HSSFCellStyle style = getCellStyle( rowIndex, cellIndex );
			if ( style != null )
				c.setCellStyle( style );

			// ���ÿ���
			int colspan = taskTimes.length*cellForm.getColspan();
			Region region = new Region( ( short ) rowIndex, ( short ) cellIndex,
											   ( short ) rowIndex-1 + cellForm.getRowspan(),
											   ( short ) ( cellIndex + colspan -1 ) );

			sheet.addMergedRegion( region );

			try
			{
				HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
				HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
												region, sheet, workbook );
			}
			catch( Exception ex )
			{
				throw new AnalyseException( "�ϲ���Ԫ�����", ex );
			}

			cellIndex += colspan;
		}
		rowIndex++;

		// ��ͷ��������
		for ( ; rowIndex < rows.length; ) {
			rowForm = rows[rowIndex];
			// create a row
			r = sheet.createRow( rowIndex );
			r.setHeightInPoints( ( short ) rowHeights[rowIndex] );

			cellForms = rowForm.getCells();
			cellIndex = 0;
			totalCells = cellForms.length;

			// ��ź͵�λ����
			if ( showIndex ) {
				totalCells += 1;
				cellIndex++;
			}

			if ( showUnitName ) {
				totalCells += 1;
				cellIndex++;
			}

			for ( int i = 0; i < cellForms.length; i++ ) {
				CellForm cellForm = cellForms[i];
				c = r.createCell( ( short ) cellIndex );
				c.setEncoding( HSSFCell.ENCODING_UTF_16 );
				c.setCellValue( cellForm.getLabel() );

				HSSFCellStyle style = getCellStyle( rowIndex, cellIndex );
				if ( style != null )
					c.setCellStyle( style );

				// ���ÿ���
				int colspan = taskTimes.length*cellForm.getColspan();

				Region region = new Region( ( short ) rowIndex,
											( short ) cellIndex,
											( short ) rowIndex-1 + cellForm.getRowspan(),
											( short ) ( cellIndex + colspan - 1) );
				sheet.addMergedRegion( region );

				try
				{
					HSSFRegionUtil.setBorderBottom( HSSFCellStyle.BORDER_THIN,
						region, sheet, workbook );
					HSSFRegionUtil.setBorderLeft( HSSFCellStyle.BORDER_THIN,
												  region, sheet, workbook );
					HSSFRegionUtil.setBorderRight( HSSFCellStyle.BORDER_THIN,
						region, sheet, workbook );
					HSSFRegionUtil.setBorderTop( HSSFCellStyle.BORDER_THIN,
												 region, sheet, workbook );
			}
			catch( Exception ex )
			{
				throw new AnalyseException( "�ϲ���Ԫ�����", ex );
			}

				cellIndex += colspan;
			}

			rowIndex++;
		}

		if( taskTimes.length > 1 )
		{
			// ���ñ�ͷ���һ�е�����ʱ��
			r = sheet.createRow( rowIndex );
			r.setHeightInPoints( TASKTIME_ROW_HEIGHT_IN_POINTS ); //

			cellIndex = 0;
			if( showIndex )
				cellIndex++;
			if( showUnitName )
				cellIndex++;

			HSSFCellStyle taskTimeStyle = workbook.createCellStyle();
			taskTimeStyle.setBorderBottom( HSSFCellStyle.BORDER_THIN );
			taskTimeStyle.setBorderLeft( HSSFCellStyle.BORDER_THIN );
			taskTimeStyle.setBorderRight( HSSFCellStyle.BORDER_THIN );
			taskTimeStyle.setBorderTop( HSSFCellStyle.BORDER_THIN );

			for ( int i = 0; i < template.getHead().getColnum(); i++ )
			{
				for ( int j = 0; j < taskTimes.length; j++ )
				{
					c = r.createCell( ( short ) cellIndex );
					c.setCellType( HSSFCell.CELL_TYPE_STRING );
					c.setEncoding( HSSFCell.ENCODING_UTF_16 );

					c.setCellValue( Convertor.date2MonthlyString( taskTimes[j].
						getFromTime() ) );

					c.setCellStyle( taskTimeStyle );
					cellIndex++;
				}
			}
		}
	}

	/**
	 * ����岿��
	 * @throws AnalyseException
	 */
	void printBody()
		throws AnalyseException
	{
		RowForm[] rowForms = template.getBody().getRows();
// �Ҹ�һ���ǻ��ܺ���cellForms
		CellForm[] cellForms = null;
		for( int iRowIndex = 0; iRowIndex < rowForms.length; iRowIndex++ )
		{
			if( !rowForms[iRowIndex].isTotalRow() )
			{
				cellForms = rowForms[iRowIndex].getCells();
				break;
			}
		}

// ����Format
		java.text.DecimalFormat[] decFmts = new java.text.DecimalFormat[
											cellForms.length];
		for( int iCellIndex = 0; iCellIndex < cellForms.length; iCellIndex++ )
		{
			if( cellForms[iCellIndex].getContentStyle().equals( "number" ) )
			{
				if( Util.isEmptyString( cellForms[iCellIndex].getFormatStyle() ) )
				{
					decFmts[iCellIndex] = new java.text.DecimalFormat( "0.##" ); // ȱʡ����������λ��Ч����
				} else
				{
					decFmts[iCellIndex] = new java.text.DecimalFormat(
						cellForms[iCellIndex].getFormatStyle() );
				}
			}
		}

		int beginRowIndex = template.getHead().getRows().length + 1;
		if( taskTimes.length == 1 )
			beginRowIndex--; // ����ʱ���в���ʾ

		int rowIndex = beginRowIndex;
		int cellIndex = 0;

		int[] rowHeights = template.getRowHeights();
		int[] colWidths = template.getColWidths();

		// declare a row object reference
		HSSFRow r = null;
// declare a cell object reference
		HSSFCell c = null;

		// ��ӡ���岿��
		for( int k=0; k<rowForms.length; k++ )
		{
			RowForm rowForm = rowForms[k];
			if( rowForm.isTotalRow() )
			{
				short rowHeight = (short) template.getRowHeights()[ template.getHead().getRows().length + k ];

				r = sheet.createRow( rowIndex );
				r.setHeightInPoints( rowHeight );
				/** @todo total row */
				// Ŀǰhssf����֧�ֻ��ܹ���
				// �����ֶ�����
				cellIndex = 0;
				if( showIndex || showUnitName )
				{
					c = r.createCell( (short) cellIndex );
					c.setEncoding( HSSFCell.ENCODING_UTF_16 );
					c.setCellValue( "�ϼ�" );

					HSSFCellStyle style = getCellStyle( beginRowIndex,
						cellIndex );
					if ( style != null )
						c.setCellStyle( style );

					if( showIndex && showUnitName )
					{
						sheet.addMergedRegion( new Region( rowIndex, (short)cellIndex,
							rowIndex, (short)1 ) );
						cellIndex += 2;
					}
					else
					{
						cellIndex++;
					}
				}

				// ��ʾ��������
				for ( int j = 0; j < tableData[0].length; j++ )
				{
					int scalarIndex = j/taskTimes.length;
					c = r.createCell( ( short ) cellIndex );
					if ( cellForms[ scalarIndex ]
						 .getContentStyle().equals(	"string" ) )
					{
					}
					else if ( cellForms[ scalarIndex ].getContentStyle().
							  equals( "number" ) )
					{
						// �ֹ�����
						double sum = 0;
						for( int i=0; i<tableData.length; i++ )
						{
							sum+=Double.parseDouble( tableData[i][j].toString() );
						}

						c.setCellType( HSSFCell.CELL_TYPE_NUMERIC );
						c.setCellValue( Double.parseDouble( decFmts[ scalarIndex ].format( sum ) ) );
					}

					HSSFCellStyle style = getCellStyle( beginRowIndex,
						cellIndex );
					if ( style != null )
						c.setCellStyle( style );

					cellIndex++;
				}

				rowIndex++;
			}
			else
			{
				short rowHeight = (short) template.getRowHeights()[ template.getHead().getRows().length + k ];

				//CellForm[] cellForms = rowForm.getCells();
				for ( int i = 0; i < tableData.length; i++ )
				{
					cellIndex = 0;
					r = sheet.createRow( rowIndex );
					r.setHeightInPoints( rowHeight );

					if ( showIndex )
					{
						c = r.createCell( ( short ) cellIndex );
						c.setCellType( HSSFCell.CELL_TYPE_NUMERIC );
						c.setCellValue( i + 1 );

						HSSFCellStyle style = getCellStyle( beginRowIndex,
							cellIndex );
						if ( style != null )
							c.setCellStyle( style );

						cellIndex++;
					}

					if ( showUnitName )
					{
						c = r.createCell( ( short ) cellIndex );
						c.setCellType( HSSFCell.CELL_TYPE_STRING );
						c.setEncoding( HSSFCell.ENCODING_UTF_16 );
						c.setCellValue( units[i].getUnitName() );

						HSSFCellStyle style = getCellStyle( beginRowIndex,
							cellIndex );
						if ( style != null )
							c.setCellStyle( style );

						cellIndex++;
					}

					for ( int j = 0; j < tableData[i].length; j++ )
					{
						int scalarIndex = j/taskTimes.length;
						c = r.createCell( ( short ) cellIndex );
						if ( cellForms[ scalarIndex ]
							 .getContentStyle().equals(	"string" ) )
						{
							c.setCellType( HSSFCell.CELL_TYPE_STRING );
							c.setEncoding( HSSFCell.ENCODING_UTF_16 );
							c.setCellValue( tableData[i][j].toString() );
						}
						else if ( cellForms[ scalarIndex ].getContentStyle().
								  equals( "number" ) )
						{
							c.setCellType( HSSFCell.CELL_TYPE_NUMERIC );
							c.setCellValue( Double.parseDouble( decFmts[ scalarIndex ].format( Double.parseDouble( tableData[i][j].
								toString() ) ) ) );
						}

						HSSFCellStyle style = getCellStyle( beginRowIndex,
							cellIndex );
						if ( style != null )
							c.setCellStyle( style );

						cellIndex++;
					}

					rowIndex++;
				}
			}
		}

		// set colwidth
		cellIndex = 0;
		if( showIndex )
		{
			sheet.setColumnWidth( ( short ) cellIndex,
								  ( short ) ( INDEX_COL_WIDTH_IN_POINTS * 20 ) );
			cellIndex++;
		}

		if( showUnitName )
		{
			sheet.setColumnWidth( (short) cellIndex,
								  (short) ( UNITNAME_COL_WIDTH_IN_POINTS * 20 ) );
			cellIndex++;
		}

		// ����ÿ��ָ�ꡢÿ������ʱ����п�
		for ( int j = 0; j < colWidths.length; j++ )
		{
			for( int i=0; i<taskTimes.length; i++ )
			{
				sheet.setColumnWidth( (short) cellIndex, (short) ( colWidths[j] * 20 ) );
				cellIndex++;
			}
		}
	}

	/**
	 * ����Ϊ��
	 * @param fontID
	 * @return      ����ֵ����Ϊ��
	 */
	private HSSFCellStyle getCellStyle( int rowIndex, int cellIndex ) {
		return ( HSSFCellStyle ) styleMap.get( rowIndex + "," + cellIndex );
	}

	/**
	 * ���ҳü��Ϣ
	 * @throws AnalyseException
	 */
	void printHeader() throws AnalyseException
	{
		PrintInformationForm printInfo = template.getPrintInformation();

		HSSFHeader header = sheet.getHeader();

		header.setLeft( printInfo.getLeftHeader() );
		header.setCenter(  printInfo.getMiddleHeader() );
		header.setRight( printInfo.getRightHeader() );
	}

	/**
	 * ���ҳ����Ϣ
	 * @throws AnalyseException
	 */
	void printFooter() throws AnalyseException
	{
		PrintInformationForm printInfo = template.getPrintInformation();

		HSSFFooter footer = sheet.getFooter();

		footer.setLeft( printInfo.getLeftFooter() );
		footer.setCenter( printInfo.getMiddleFooter() );
		footer.setRight( printInfo.getRightFooter() );
	}
}