package cn.com.youtong.apollo.analyse;

import java.util.Iterator;
import cn.com.youtong.apollo.analyse.form.*;

/**
 * ָ���ѯģ��ӿ�
 */
public interface ScalarQueryTemplate
{
    /**
     * �õ�ģ������
     * @return ģ������
     */
    public String getName();

    /**
     * �õ���ѯ����
     * @return ��ѯ����
     * @throws AnalyseException
     */
    public ScalarQueryForm getCondition() throws AnalyseException;

    /**
     * �õ�ģ��ID
     * @return ģ��ID
     */
    public Integer getTemplateID();

	/**
	 * �õ���ͷ
	 * @return           ��ͷ
	 * @throws AnalyseException
	 */
	public HeadForm getHead() throws AnalyseException;

	/** ������� */
	public int[] getColWidths() throws AnalyseException;
	/** �߶����� */
	public int[] getRowHeights() throws AnalyseException;

	/** �岿�� */
	public BodyForm getBody() throws AnalyseException;
	/** ��������ID��ȡ��Font�����û�ж��巵��null */
	public FontForm getFont( Integer fontID ) throws AnalyseException;

	public Iterator getFonts() throws AnalyseException;

	public PrintInformationForm getPrintInformation() throws AnalyseException;
}