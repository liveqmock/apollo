package cn.com.youtong.apollo.common;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.jspsmart.upload.*;
import com.jspsmart.upload.File;

/**
 * �ļ��ϴ�����Bean
 */
public class UploadBean
{
    private SmartUpload upload;

    public UploadBean(ServletConfig config, HttpServletRequest request,
                      HttpServletResponse response) throws Warning
    {
        try
        {
            upload = new SmartUpload();
            upload.initialize(config, request, response);
            upload.upload();
        }
        catch (Exception ex)
        {
            throw new Warning("�ϴ�ʧ��", ex);
        }
    }

    /**
     * �õ��ϴ���xml��
     * @return �ϴ���xml��
     * @throws Warning
     */
    public InputStream getXmlInputStreamUploaded() throws Warning
    {
        File file = getUploadFile();
//        if (file.getContentType().indexOf("text/xml") < 0)
//        {
//            throw new Warning("�ϴ����ļ���ʽ����ȷ");
//        }
        return file.getInputStream();
    }

    /**
     * ��xml�ļ��еõ��ϴ��ļ���xml��
     * @param xmlFile xml�ļ�
     * @return �ϴ���xml��
     * @throws Warning
     */
    public InputStream getXmlInputStreamUploaded(File xmlFile) throws Warning
    {
        if (xmlFile.getContentType().indexOf("text/xml") < 0)
        {
            throw new Warning("�ϴ����ļ���ʽ����ȷ");
        }
        return xmlFile.getInputStream();
    }

	/**
	 * �õ��ϴ����ļ�
	 * @return �ϴ����ļ��������û���ϴ����󣬷���null
	 */
	public File getUploadFile()
	{
		File file = upload.getFiles().getFile(0);
		if(file.isMissing())
		{
			return null;
		}
		return file;
	}

    /**
     * �õ�����ֵ
     * @param parameter ��������
     * @return ����ֵ
     */
    public String getParameter(String parameter)
    {
        return upload.getRequest().getParameter(parameter);
    }


}