package cn.com.youtong.apollo.data;

import java.io.*;
import java.util.*;

import cn.com.youtong.apollo.task.*;

/**
 * �������ݵ�����������ִ�б��������ϱ��ĸ������̣����棬���нű�����¼������
 */
public interface DataImporter
{
	/**
	 * ��ָ���ĵ�λִ�нű�
	 * @param unitIDItr ��λID��Iterator
	 * @param taskTime ����ʱ��
	 * @throws ModelException
	 */
	public void excuteScripts(Iterator unitIDItr, TaskTime taskTime) throws
		ModelException;

	/**
	 * ��������
	 * @param xmlInputStream ������
	 * @param acl UnitACL
	 * @return �ϱ����
	 * @throws ModelException
	 */
	public LoadResult importData(InputStream xmlInputStream, UnitACL acl) throws
		ModelException;
}