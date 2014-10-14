package cn.com.youtong.apollo.task;

public interface TableView
{

    /**
     * 获得XSLT的用途类型
     * @return 用途类型
     *  1--HTML
     *  2--EXCEL
     *  3--PDF
     */
    Integer getType();
    /**
     * XSLT文件
     * @return XSLT文件
     */
    String getXSLTString();

}