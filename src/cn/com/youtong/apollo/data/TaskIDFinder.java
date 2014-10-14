package cn.com.youtong.apollo.data;

import java.io.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;


public class TaskIDFinder
{
    public TaskIDFinder()
    {
    }

	/**
	 * 从输入流中查找taskModel元素的ID属性值
	 * @param is            输入xml流
	 * @return              ID属性值，如果不存在，那么返回null
	 */
	public static String findTaskID(InputStream is) {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		DefaultHandler handler = new TaskHandler();
		SAXParser saxParser;
		try {
			saxParser = factory.newSAXParser();
			saxParser.parse(is, handler);
		} catch (TaskIDFoundException ex) {
			return ex.getTaskID();
		} catch (SAXException ex) {
		} catch (IOException ioe) {
		} catch (ParserConfigurationException pce) {
		}

		return null;
	}


private static class TaskHandler extends DefaultHandler
{
	private static final String TASK_ELE_NAME = "taskModel";
	public TaskHandler()
	{
	}

	public void startElement(
		String uri,
		String localName,
		String qName,
		Attributes attributes)
		throws SAXException {

		String eName = qName; // 当前读取元素名
		if(eName.equals(TASK_ELE_NAME))
		{
			String taskID = attributes.getValue("ID");
			throw new TaskIDFoundException(taskID);
		}
	}
}

private static class TaskIDFoundException extends SAXException
{
	private String taskID;
	public TaskIDFoundException(String msg)
	{
		super(msg);
		this.taskID = msg;
	}

	public String getTaskID() {
		return taskID;
	}

}
}

