package cn.com.youtong.tools.excel;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;

/**
 * <p>Title: </p>
 * <p>Description:excel�ļ����� </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: ������ͨ</p>
 * @author unascribed
 * @version 1.0
 */

public class Excel
{

	public Excel()
	{
	}

	/**
	 * ����·�����ļ�������������ָ����Excel�ļ�
	 * @param filePath ·����(�����ļ���)
	 * @param data ����(data������Ķ�����Ҫ��װ��һ��Collection,������ʾExcel�е�һ������)
	 * @throws IOException
	 */
	public static void generateExcelFile(String filePath, Collection data)
		throws IOException
	{
		try {
			//����һ�����ļ�
			FileOutputStream fileOut = new FileOutputStream(filePath);
			//����������
			HSSFWorkbook workBook = new HSSFWorkbook();
			HSSFSheet sheet = workBook.createSheet("sheet1");
			sheet.setDefaultColumnWidth((short) 14);
			//��������
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
					cell.setEncoding(HSSFCell.ENCODING_UTF_16); //����cell���������ĸ�λ�ֽڽض�
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
			//������д��ָ����Excel�ļ�
			workBook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}