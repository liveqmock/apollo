package cn.com.youtong.apollo.servlet;

import cn.com.youtong.apollo.common.*;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;
import cn.com.youtong.apollo.dictionary.*;
import java.util.*;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import cn.com.youtong.apollo.services.*;
import java.net.URLDecoder;

/**
 * �����ֵ����servlet
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DictionaryServlet extends RootServlet
{
	/**
	 * ҳ�泣�� -- �����ֵ䷢��ҳ��
	 */
	public static final String DICTIONARY_PUBLISH_PAGE = "/jsp/dictionaryManager/publishDictionary.jsp";
	/**
	 * ҳ�泣�� -- �����ֵ���ʾҳ��
	 */
	public static final String DICTIONARY_DISPLAY_PAGE = "/jsp/dictionaryManager/dictionaryDisplay.jsp";

	/**
	 * ҳ�泣�� -- �����ֵ���ʾҳ��
	 */
	public static final String DICTIONARY_ENTRIES_PAGE = "/jsp/dictionaryManager/dictionaryEntry.jsp";

	/**
	 * �������ͳ��� -- ��ʾ�����ֵ䷢��ҳ��
	 */
	public static final String SHOW_DICTIONARY_PUBLISH_PAGE = "showDictionaryPublish";
	/**
	 * �������ͳ��� -- ��ʾ�����ֵ���ʾҳ��
	 */
	public static final String SHOW_DICTIONARY_DISPLAY_PAGE = "showDictionaryDisplay";
	/**
	 * �������ͳ��� -- ��ʾ�����ֵ����ʵ��ҳ��
	 */
	public static final String SHOW_DICTIONARY_ENTRY_PAGE = "showDictionaryEntry";
	/**
	 * �������ͳ��� -- ��ʾ�����ֵ�����ҳ��
	 */
	public static final String SHOW_DICTIONARY_ADD_PAGE = "showAddDictionary";

	/**
	 * �������ͳ��� -- ɾ�������ֵ���Ŀ
	 */
	public static final String DELETE__DICTIONARY_ITEM = "deleteDictionaryItem";

	/**
	 * �������ͳ��� -- ���������ֵ���Ŀ
	 */
	public static final String ADD_DICTIONARY = "addDictionary";
        /**
         * �������ͳ��� -- ���������ֵ���
         */
        public static final String SHOW_DICTIONARY_TREE = "showDictionaryTree";

	/**
	 *��ʾ�����ֵ���Ŀ
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showDictionaryDisplay(HttpServletRequest request, HttpServletResponse response)
		throws Warning, IOException, ServletException
	{
		DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
		Iterator dicIter = dictionaryManager.getAllDictionaries();
		request.setAttribute("dictionaries", dicIter);
		this.go2UrlWithAttibute(request, response, DICTIONARY_DISPLAY_PAGE);
	}

	/**
	 *��ʾ�����ֵ���Ŀ��ʵ����Ŀ
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showDictionaryEntry(HttpServletRequest request, HttpServletResponse response)
		throws Warning, IOException, ServletException
	{
		String id = request.getParameter("dictionaryID");
                request.setAttribute("dictionaryName", URLDecoder.decode(request.getParameter("dictionaryName"),"utf8"));
		DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
		request.setAttribute("dictionaryID", id);
		Iterator dicIter = dictionaryManager.getAllDictionaries();
		request.setAttribute("dictionaries", dicIter);

		cn.com.youtong.apollo.dictionary.Dictionary dictionary = dictionaryManager.getDictionaryByID(id);
		Iterator entryItr = dictionary.entrySet().iterator();
		request.setAttribute("entries", entryItr);
                request.setAttribute("displayMode","list");
		this.go2UrlWithAttibute(request, response, DICTIONARY_ENTRIES_PAGE);
		//RequestDispatcher rq = request.getRequestDispatcher(DICTIONARY_DISPLAY_PAGE);
		//rq.include(request, response);

	}
        /**
         *��ʾ�����ֵ���Ŀ��ʵ����Ŀ
         * @param request
         * @param response
         * @throws Warning
         * @throws IOException
         * @throws ServletException
         */
        private void showDictionaryTree(HttpServletRequest request, HttpServletResponse response)
                throws Warning, IOException, ServletException
        {
                String id = request.getParameter("dictionaryID");
                request.setAttribute("dictionaryName", URLDecoder.decode(request.getParameter("dictionaryName"),"utf8"));
                DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
                UtilServlet.showDefaultDictionaryTree(request,response);
                request.setAttribute("dictionaryID",id);
                request.setAttribute("displayMode","tree");
                this.go2UrlWithAttibute(request, response, DICTIONARY_ENTRIES_PAGE);
                //RequestDispatcher rq = request.getRequestDispatcher(DICTIONARY_DISPLAY_PAGE);
                //rq.include(request, response);

        }

	/**
	 *��ʾ �����ֵ䷢��ҳ��
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void showPublishDictionary(HttpServletRequest request, HttpServletResponse response)
		throws Warning, IOException, ServletException
	{
          DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
          Iterator dicIter = dictionaryManager.getAllDictionaries();
          request.setAttribute("dictionaries", dicIter);
	  this.go2UrlWithAttibute(request, response, DICTIONARY_PUBLISH_PAGE);
	}

	/**
	 * ɾ��ָ�������ֵ���Ŀ
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void deleteDictionaryItem(HttpServletRequest request, HttpServletResponse response)
		throws Warning, IOException, ServletException
	{
		String id = request.getParameter("dictionaryID");
		DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
		dictionaryManager.deleteDictionary(id);
		Iterator dicIter = dictionaryManager.getAllDictionaries();
		request.setAttribute("dictionaries", dicIter);
		this.go2UrlWithAttibute(request, response, DICTIONARY_PUBLISH_PAGE);
	}

	/**
	 * ���������ֵ�����
	 * @param request
	 * @param response
	 * @throws Warning
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addDictionary(HttpServletRequest request, HttpServletResponse response)
		throws Warning, IOException, ServletException
	{
		try
		{
			//�õ��ϴ��������ֵ�xml�ĵ�����
            UploadBean upload = new UploadBean(getServletConfig(), request, response);
			InputStream dictionaryContent = upload.getXmlInputStreamUploaded();
			DictionaryManager dictionaryManager = ((DictionaryManagerFactory)Factory.getInstance(DictionaryManagerFactory.class.getName())).createDictionaryManager();
			dictionaryManager.updateDictionary(dictionaryContent);
			showPublishDictionary(request, response);
		}
		catch(Warning w)
		{
			throw w;
		}

	}

	/**
	 * ҳ��ַ�
	 * @param req
	 * @param res
	 * @throws cn.com.youtong.apollo.common.Warning
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	public void perform(HttpServletRequest request, HttpServletResponse response)
		throws cn.com.youtong.apollo.common.Warning, java.io.IOException, javax.servlet.ServletException
	{
		String operation = request.getParameter("operation");

		if(operation != null && operation.equalsIgnoreCase(SHOW_DICTIONARY_PUBLISH_PAGE))
		{
			showPublishDictionary(request, response);
			return;
		}
		else if(operation != null && operation.equalsIgnoreCase(SHOW_DICTIONARY_DISPLAY_PAGE))
		{
			showDictionaryDisplay(request, response);
			return;
		}
		else if(operation != null && operation.equalsIgnoreCase(SHOW_DICTIONARY_ENTRY_PAGE))
		{
			showDictionaryEntry(request, response);
			return;
		}
		else if(operation != null && operation.equalsIgnoreCase(DELETE__DICTIONARY_ITEM))
		{
			deleteDictionaryItem(request, response);
			return;
		}
		else if(operation != null && operation.equalsIgnoreCase(ADD_DICTIONARY))
		{
			addDictionary(request, response);
			return;
		}
                else if(operation != null && operation.equalsIgnoreCase(SHOW_DICTIONARY_TREE))
                {
                        showDictionaryTree(request, response);
                        return;
                }

		else
		{
			throw new Warning("��Ч�Ĳ���operation = " + operation);
		}
	}

}
