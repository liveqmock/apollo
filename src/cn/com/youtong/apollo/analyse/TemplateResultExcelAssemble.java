package cn.com.youtong.apollo.analyse;

// apollo
import java.io.IOException;
import java.io.OutputStream;

// apache
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import cn.com.youtong.apollo.data.UnitTreeNode;
import cn.com.youtong.apollo.task.TaskTime;

/**
 * 模板查询结果excel导出器
 *
 * <p>
 * 多个模板查询结果可能导入到同一个excel文件。
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yutop</p>
 * @author wjb
 * @version 1.0
 */
public class TemplateResultExcelAssemble
{
	private HSSFWorkbook workbook;
	private int currSheetIndex = -1;

    public TemplateResultExcelAssemble()
    {
    }

	/**
	 * 创建一个新的workbook
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createWorkbook()
	{
		workbook = new HSSFWorkbook();

		currSheetIndex = -1;

		return workbook;
	}

	/**
	 * 在新创建的workbook里面，创建一个新的HSSFSheet
	 * @return  HSSFSheet
	 */
	private HSSFSheet createSheet()
	{
		currSheetIndex++;
                HSSFSheet sheet=workbook.createSheet();
                HSSFPrintSetup ps = sheet.getPrintSetup();
                ps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                ps.setNoOrientation(true);

		return sheet;
	}

	/**
	 * 当前的worksheet，在workbook中的Index
	 * @return        sheetIndex
	 */
	private int currentSheetIndex()
	{
		return currSheetIndex;
	}

	/**
	 * 向workbook里面添加一个模板查询结果。
	 *
	 * <p>
	 * 程序内部创建worksheet。
	 *
	 * @param template
	 * @param taskTimes
	 * @param tableData
	 * @param showIndex
	 * @param showUnitName
	 * @param units
	 * @throws AnalyseException
	 */
	public void add( ScalarQueryTemplate template,
						   //OutputStream out,
						   TaskTime[] taskTimes, Object[][] tableData,
						   boolean showIndex, boolean showUnitName,
						   UnitTreeNode[] units )
		throws AnalyseException
	{
		HSSFSheet sheet = createSheet();
		int sheetIndex = currentSheetIndex();

		TemplateResultExporter exporter = new TemplateResultExporter(
				  template,	taskTimes, tableData,
				  showIndex,  showUnitName,	units,
				  workbook,	sheet, sheetIndex );

		exporter.prepareSheet();

		exporter.printHead();
		exporter.printBody();

		exporter.printHeader();
		exporter.printFooter();
	}

	/**
	 * 把当前的workbook内容写入excel输出流
	 * @param out                  输出流
	 * @throws IOException
	 */
	public void writeToStream( OutputStream out )
		throws IOException
	{
		workbook.write( out );
	}
}