package cn.com.youtong.apollo.task;

public interface TableView
{

    /**
     * ���XSLT����;����
     * @return ��;����
     *  1--HTML
     *  2--EXCEL
     *  3--PDF
     */
    Integer getType();
    /**
     * XSLT�ļ�
     * @return XSLT�ļ�
     */
    String getXSLTString();

}