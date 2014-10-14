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
 * ģ���ѯ���excel������
 *
 * <p>
 * ���ģ���ѯ������ܵ��뵽ͬһ��excel�ļ���
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
	 * ����һ���µ�workbook
	 * @return HSSFWorkbook
	 */
	public HSSFWorkbook createWorkbook()
	{
		workbook = new HSSFWorkbook();

		currSheetIndex = -1;

		return workbook;
	}

	/**
	 * ���´�����workbook���棬����һ���µ�HSSFSheet
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
	 * ��ǰ��worksheet����workbook�е�Index
	 * @return        sheetIndex
	 */
	private int currentSheetIndex()
	{
		return currSheetIndex;
	}

	/**
	 * ��workbook�������һ��ģ���ѯ�����
	 *
	 * <p>
	 * �����ڲ�����worksheet��
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
	 * �ѵ�ǰ��workbook����д��excel�����
	 * @param out                  �����
	 * @throws IOException
	 */
	public void writeToStream( OutputStream out )
		throws IOException
	{
		workbook.write( out );
	}
}