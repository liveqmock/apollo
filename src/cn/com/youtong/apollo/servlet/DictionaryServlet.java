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
 * 数据字典管理servlet
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
	 * 页面常数 -- 代码字典发布页面
	 */
	public static final String DICTIONARY_PUBLISH_PAGE = "/jsp/dictionaryManager/publishDictionary.jsp";
	/**
	 * 页面常数 -- 代码字典显示页面
	 */
	public static final String DICTIONARY_DISPLAY_PAGE = "/jsp/dictionaryManager/dictionaryDisplay.jsp";

	/**
	 * 页面常数 -- 代码字典显示页面
	 */
	public static final String DICTIONARY_ENTRIES_PAGE = "/jsp/dictionaryManager/dictionaryEntry.jsp";

	/**
	 * 请求类型常量 -- 显示代码字典发布页面
	 */
	public static final String SHOW_DICTIONARY_PUBLISH_PAGE = "showDictionaryPublish";
	/**
	 * 请求类型常量 -- 显示代码字典显示页面
	 */
	public static final String SHOW_DICTIONARY_DISPLAY_PAGE = "showDictionaryDisplay";
	/**
	 * 请求类型常量 -- 显示数据字典管理实体页面
	 */
	public static final String SHOW_DICTIONARY_ENTRY_PAGE = "showDictionaryEntry";
	/**
	 * 请求类型常量 -- 显示数据字典增加页面
	 */
	public static final String SHOW_DICTIONARY_ADD_PAGE = "showAddDictionary";

	/**
	 * 请求类型常量 -- 删除数据字典项目
	 */
	public static final String DELETE__DICTIONARY_ITEM = "deleteDictionaryItem";

	/**
	 * 请求类型常量 -- 增加数据字典项目
	 */
	public static final String ADD_DICTIONARY = "addDictionary";
        /**
         * 请求类型常量 -- 增加数据字典树
         */
        public static final String SHOW_DICTIONARY_TREE = "showDictionaryTree";

	/**
	 *显示数据字典项目
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
	 *显示数据字典项目的实体项目
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
         *显示数据字典项目的实体项目
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
	 *显示 数据字典发布页面
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
	 * 删除指定数据字典项目
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
	 * 增加数据字典数据
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
			//得到上传的数据字典xml文档内容
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
	 * 页面分发
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
			throw new Warning("无效的参数operation = " + operation);
		}
	}

}
