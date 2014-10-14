package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;

/**
 * 报表数据导入器，负责执行报表数据上报的各个流程：保存，运行脚本，记录填报情况等
 */
public interface DataImporter
{
	/**
	 * 对指定的单位执行脚本
	 * @param unitIDItr 单位ID的Iterator
	 * @param taskTime 任务时间
	 * @throws ModelException
	 */
	public void excuteScripts(Iterator unitIDItr, TaskTime taskTime) throws
		ModelException;

	/**
	 * 导入数据
	 * @param xmlInputStream 数据流
	 * @param acl UnitACL
	 * @return 上报结果
	 * @throws ModelException
	 */
	public LoadResult importData(InputStream xmlInputStream, UnitACL acl) throws
		ModelException;
}