package cn.com.youtong.tools.excel;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;

/**
 * <p>Title: </p>
 * <p>Description:excel文件操作 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: 世纪友通</p>
 * @author unascribed
 * @version 1.0
 */

public class Excel
{

	public Excel()
	{
	}

	/**
	 * 根据路径，文件名和数据生成指定的Excel文件
	 * @param filePath 路径名(包括文件名)
	 * @param data 数据(data集合里的对象需要封装成一个Collection,用来表示Excel中的一行数据)
	 * @throws IOException
	 */
	public static void generateExcelFile(String filePath, Collection data)
		throws IOException
	{
		try {
			//创建一个新文件
			FileOutputStream fileOut = new FileOutputStream(filePath);
			//创建工作表
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("sheet1");
			sheet.setDefaultColumnWidth((short) 14);
			//导入数据
			Iterator iterRow = data.iterator();
			short rowCount = (short) 0;
			while(iterRow.hasNext())
			{
				Collection col = (Collection) iterRow.next();
				Iterator iterCell = col.iterator();
				HSSFRow row = sheet.createRow(rowCount);
				short cellCount = (short) 0;
				while(iterCell.hasNext())
				{
					HSSFCell cell = row.createCell(cellCount);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16); //设置cell编码解决中文高位字节截断
	                String value = iterCell.next().toString();           
	              if(value.indexOf(".")>0){
//	            	  cell.setCellValue(Double.parseDouble(value));  
	            	  cell.setCellValue(value);  
	              } 
	              else
	              {
	            	  cell.setCellValue(value);  
	              } 
					cellCount++;
				}
				rowCount++;
			}
			//将数据写入指定的Excel文件
			workBook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}