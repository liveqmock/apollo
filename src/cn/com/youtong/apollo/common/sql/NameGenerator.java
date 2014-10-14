package cn.com.youtong.apollo.common.sql;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.task.*;

/**
 * ����������
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author wjb & mk
 * @version 1.0
 */
public class NameGenerator
{
	private static Log log = LogFactory.getLog(NameGenerator.class);
	/** ��Ԫ�������---��ֵ�� */
	public static final int CELL_TYPE_NUMBER = 1;
	/** ��Ԫ�������---�ı��� */
	public static final int CELL_TYPE_STRING = 2;
	/** ��Ԫ�������---������ */
	public static final int CELL_TYPE_BINARY = 3;
	/** ��Ԫ�������---���ı� */
	public static final int CELL_TYPE_BLOCK = 4;
	/** ��Ԫ�������---������ */
	public static final int CELL_TYPE_DATETIME = 5;

	/**
	 * ��������ID���ID�������ݱ������
	 *
	 * @param taskID
	 *            ����ID
	 * @param tableID
	 *            ��ID
	 * @return ���ݱ������
	 */
	public static String generateDataTableName(String taskID, String tableID)
	{
		return "YTAPL_" + taskID + "_" + tableID;
	}

    /**
     * ��������ID���ɴ�ű������ݸ����ı������
     *
     * @param taskID ����ID
     *
     * @return ��ű������ݸ����ı������
     */
    public static String generateAttachmentTableName(String taskID)
    {
        return "YTAPL_" + taskID + "_Attachement";
    }

	/**
	 * ���ݴ����ֵ�id���ɴ����ֵ�������
	 *
	 * @param dictid
	 *            �����ֵ�id
	 * @return �����ֵ�������
	 */
	public static String generateDictionaryTableName(String dictid)
	{
		return "YTAPL_Dict" + "_" + dictid;
	}

	/**
	 * �������������
	 *
	 * @param taskID
	 *            �����ʶ
	 * @param tableID
	 *            ���ʶ
	 * @param rowID
	 *            �б�ʶ
	 * @return ����һ������ƴ�յĸ�������
	 */
	public static String generateFloatDataTableName(String taskID, String tableID, String rowID)
	{
		return "YTAPL_" + taskID + "_" + tableID + "_" + rowID;
	}

	/**
	 * �ṩ��λ����ͱ������ͣ�����(��λ��)��λ����
	 *
	 * @param unitCode
	 *            ��λ����
	 * @param reportType
	 *            ��������
	 * @return (��λ��)��λ����
	 */
	public static String generateTreeUnitID(String unitCode, String reportType)
	{
		return unitCode + reportType;
	}

	/**
	 * �ṩ��λ����ͱ������ͣ�����(��λ��)�ϼ���λ����
	 *
	 * @param parentUnitCode
	 *            �ϼ���λ����
	 * @return (��λ��)�ϼ���λ����
	 */
	public static String generateTreeParentUnitID(String parentUnitCode)
	{
		return parentUnitCode + ReportType.GROUP_GATHER_TYPE;
	}

	/**
	 * �����ϱ���ѹ���ļ����ļ���
	 * @param fileName �ϱ����ļ���
	 * @return ���������ļ���
	 */
	public static String gererateZipDataFileName(String fileName)
	{
		log.info("�ϱ�����ļ���Ϊ��" + fileName + "_YTAPL_" + System.currentTimeMillis());
		return fileName + "_YTAPL_" + System.currentTimeMillis();
	}

	/**
	 * ����ϱ��ļ���ԭ��
	 * @param fileName �޸ĺ���ļ���
	 * @return ԭ�ļ���
	 */
	public static String getZipDataFileName(String fileName)
		throws Warning
	{

		int position = fileName.indexOf("_YTAPL_");
		if(position <= 0)
		{
			throw new Warning("�ļ������Ϸ���" + fileName);
		}
		System.out.println("��õ��ļ���Ϊ��" + fileName.substring(0, position));
		return fileName.substring(0, position);
	}
}
