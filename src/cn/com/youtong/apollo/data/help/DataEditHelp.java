package cn.com.youtong.apollo.data.help;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.*;

import cn.com.youtong.apollo.common.Warning;


public class DataEditHelp {

  /**
   * 得到客户端定义的javascript对象
   * 形如：
   *    var aa={name:"Gabby", breed:"Lab", color:"chocolate", sex:"girl"};
   * @param xml
   * @return
   */
  public  static String getEditScriptObject(String xml) throws Warning{
    StringBuffer result = new StringBuffer();
    try {
      SAXReader reader = new SAXReader();
      Document doc = reader.read(new StringReader(xml));

      Element taskTime = doc.getRootElement().element("taskTime");

      List units = taskTime.elements("unit");

      Iterator unitItr = units.iterator();

      while (unitItr.hasNext()) {
        Element unit = (Element) unitItr.next();
        List tables = unit.elements();
        Iterator itr = tables.iterator();

        while (itr.hasNext()) {
          Element table = (Element) itr.next();
          if(table.attributeValue("ID")!=null){
	          String id = table.attributeValue("ID").toLowerCase();
	
	          result.append(" var " + id + "={");
	
	          List cells = table.elements("cell");
	          Iterator cellItr = cells.iterator();
	          while (cellItr.hasNext()) {
	            Element cell = (Element) cellItr.next();
	            String cellName = cell.attributeValue("field").toLowerCase();
	            String cellValue = cell.attributeValue("value");
	            result.append(cellName + ":\"" + cellValue + "\"");
	            if(cellItr.hasNext())
	                 result.append(",");
	          }
	
	
	          result.append(" };\n");
          }
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new Warning("数据输出格式有误，导致构造客户端javascript对象不能构造");
    }

    return result.toString();
  }

}