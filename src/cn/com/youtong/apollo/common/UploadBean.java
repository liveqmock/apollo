package cn.com.youtong.apollo.common;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.jspsmart.upload.*;
import com.jspsmart.upload.File;

/**
 * 文件上传处理Bean
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
            throw new Warning("上传失败", ex);
        }
    }

    /**
     * 得到上传的xml流
     * @return 上传的xml流
     * @throws Warning
     */
    public InputStream getXmlInputStreamUploaded() throws Warning
    {
        File file = getUploadFile();
//        if (file.getContentType().indexOf("text/xml") < 0)
//        {
//            throw new Warning("上传的文件格式不正确");
//        }
        return file.getInputStream();
    }

    /**
     * 从xml文件中得到上传文件的xml流
     * @param xmlFile xml文件
     * @return 上传的xml流
     * @throws Warning
     */
    public InputStream getXmlInputStreamUploaded(File xmlFile) throws Warning
    {
        if (xmlFile.getContentType().indexOf("text/xml") < 0)
        {
            throw new Warning("上传的文件格式不正确");
        }
        return xmlFile.getInputStream();
    }

	/**
	 * 得到上传的文件
	 * @return 上传的文件对象，如果没有上传对象，返回null
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
     * 得到参数值
     * @param parameter 参数名称
     * @return 参数值
     */
    public String getParameter(String parameter)
    {
        return upload.getRequest().getParameter(parameter);
    }


}