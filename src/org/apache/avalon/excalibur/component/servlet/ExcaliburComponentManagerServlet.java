/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Jakarta", "Avalon", "Excalibur" and "Apache Software Foundation"
    must not be used to endorse or promote products derived from this  software
    without  prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation. For more  information on the
 Apache Software Foundation, please see <http://www.apache.org/>.

*/
package org.apache.avalon.excalibur.component.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;

import org.apache.avalon.excalibur.component.ExcaliburComponentManagerCreator;
import org.apache.avalon.excalibur.logger.LoggerManager;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.excalibur.instrument.InstrumentManager;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.DefaultContext;

import org.apache.fulcrum.schedule.ScheduleService;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationUtils;
import java.util.Iterator;
import java.util.Vector;
import java.util.Properties;

import cn.com.youtong.apollo.services.ApolloService;
import cn.com.youtong.apollo.license.*;

/**
 * Makes it possible for servlets to work with Avalon components
 *  without having to do any coding to setup and manage the
 *  lifecycle of the ServiceManager (ComponentManager).
 * <p>
 * To make use of the ExcaliburComponentManagerServet.  You will
 *  need to define the servlet in your web.xml file as follows:
 * <pre>
 *  &lt;!-- ExcaliburComponentManagerServlet (for initializing ComponentManager).   --&gt;
 *  &lt;servlet&gt;
 *      &lt;servlet-name&gt;ExcaliburComponentManagerServlet&lt;/servlet-name&gt;
 *      &lt;display-name&gt;ExcaliburComponentManagerServlet&lt;/display-name&gt;
 *      &lt;description&gt;Creates component manager, does not service requests.&lt;/description&gt;
 *      &lt;servlet-class&gt;
 *          org.apache.avalon.excalibur.component.servlet.ExcaliburComponentManagerServlet
 *      &lt;/servlet-class&gt;
 *
 *      &lt;!-- This parameter points to the logkit configuration file.             --&gt;
 *      &lt;!-- Note that the path is specified in absolute notation but it will be --&gt;
 *      &lt;!-- resolved relative to the servlets webapp context path               --&gt;
 *      &lt;init-param&gt;
 *          &lt;param-name&gt;logkit&lt;/param-name&gt;
 *          &lt;param-value&gt;/WEB-INF/logkit.xml&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *
 *      &lt;!-- This parameter points to the components configuration file.         --&gt;
 *      &lt;init-param&gt;
 *          &lt;param-name&gt;components&lt;/param-name&gt;
 *          &lt;param-value&gt;/WEB-INF/components.xml&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *
 *      &lt;!-- Roles file supplements configuration file to make the latter        --&gt;
 *      &lt;!-- more readable. Most likely you don't want to change the roles       --&gt;
 *      &lt;!-- file --&gt;
 *      &lt;init-param&gt;
 *          &lt;param-name&gt;roles&lt;/param-name&gt;
 *          &lt;param-value&gt;/WEB-INF/roles.xml&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *
 *      &lt;!-- This parameter points to the instrument manager configuration file. --&gt;
 *      &lt;init-param&gt;
 *          &lt;param-name&gt;instrument&lt;/param-name&gt;
 *          &lt;param-value&gt;/WEB-INF/instrument.xml&lt;/param-value&gt;
 *      &lt;/init-param&gt;
 *
 *      &lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 *  &lt;/servlet&gt;
 * </pre>
 * Please pay particular attention to the load-on-startup element.  It is used
 * to control the order in which servlets are started by the servlet engine.
 * It must have a value which is less than any other servlets making use of
 * the ServiceManager.  This is to ensure that the ServiceManager is
 * initialized before any other servlets attempt to start using it.
 * <p>
 * All of the configuration files are located in the WEB-INF directory by
 * default.  The instrument configuration file is optional.  Please see the
 * {@link org.apache.avalon.excalibur.component.ExcaliburComponentManagerCreator}
 * class for details on what goes into these configuration files.  Note that
 * the lifecycle of the ExcaliburComponentManagerCreator is managed automatically
 * by this servlet, so there is no need to access the class directly.
 * <p>
 * Once the servlet has been configured, other servlets may gain access to
 * the ServiceManager (ComponentManager), InstrumentManager and LoggerManager
 * via the ServletContext using the following code within a servlet:
 * <pre>
 *  // Get a reference to the ServletContext
 *  ServletContext servletContext = getServletContext();
 *
 *  // Acquire the LoggerManager
 *  LoggerManager loggerManager =
 *      (LoggerManager)m_servletContext.getAttribute( LoggerManager.class.getName() );
 *
 *  // Acquire the InstrumentManager
 *  InstrumentManager instrumentManager =
 *      (InstrumentManager)m_servletContext.getAttribute( InstrumentManager.class.getName() );
 *
 *  // Acquire the ServiceManager
 *  ServiceManager serviceManager =
 *      (ServiceManager)m_servletContext.getAttribute( ServiceManager.class.getName() );
 *
 *  // Acquire the ComponentManager ( Deprecated )
 *  ComponentManager componentManager =
 *      (ComponentManager)m_servletContext.getAttribute( ComponentManager.class.getName() );
 * </pre>
 * The ExcaliburComponentManagerServlet makes use of a proxy system to manage
 * reference to the above managers, so it is not necessary to release them
 * when a servlet is done using them.
 * <p>
 * It may be necessary to add the following code to the end of the dispose method of any
 *  servlets referencing any of the above proxies.  This is because on some containers,
 *  like Tomcat, the classloader is immediately invalidated after the last servlet is
 *  disposed.  If this happens before the managers have all been disposed, then you may
 *  see errors in the console like: <code>WebappClassLoader: Lifecycle error : CL stopped</code>
 * <pre>
 *  System.gc();
 *  try
 *  {
 *      Thread.sleep(250);
 *  }
 *  catch ( InterruptedException e ) {}
 * </pre>
 * Note that servlets which extend the AbstractComponentManagerServlet will behave correctly.
 *
 * @author <a href="mailto:leif@apache.org">Leif Mortenson</a>
 * @version CVS $Revision: 1.8 $ $Date: 2003/02/25 16:28:41 $
 * @since 4.2
 */
public class ExcaliburComponentManagerServlet
    extends GenericServlet
{
    private ExcaliburComponentManagerCreator m_componentManagerCreator;

    /** Latch used to shutdown the ExcaliburComponentManagerCreator cleanly. */
    private Latch m_latch;

    /*---------------------------------------------------------------
     * Constructors
     *-------------------------------------------------------------*/

    /*---------------------------------------------------------------
     * GenericServlet Methods
     *-------------------------------------------------------------*/
    /**
     * Builds the component manager and stores references to the
     *  ComponentManager, LoggerManager, and InstrumentManager into
     *  the ServletContext.
     *
     * @param servletConfig Servlet configuration
     *
     * @throws ServletException If there are any problems initializing the
     *                          servlet.
     */
    public void init( ServletConfig servletConfig ) throws ServletException
    {
        super.init( servletConfig );

        //System.out.println( "ExcaliburComponentManagerServlet.init() BEGIN" );
        ServletContext servletContext = getServletContext();

		initLog4j(); //zhou added

        InputStream loggerManagerConfigStream = null;
        InputStream roleManagerConfigStream = null;
        InputStream componentManagerConfigStream = null;
        InputStream instrumentManagerConfigStream = null;

        try
        {
            loggerManagerConfigStream =
                getStreamFromParameter( servletConfig, "logkit", true );
            roleManagerConfigStream =
                getStreamFromParameter( servletConfig, "roles", true );
            componentManagerConfigStream =
                getStreamFromParameter( servletConfig, "components", true );
            instrumentManagerConfigStream =
                getStreamFromParameter( servletConfig, "instrument", false );

            // Create the ComponentManagerCreator
            try
            {
				Context ctx= createDefaultContext(servletConfig);
                m_componentManagerCreator = new ExcaliburComponentManagerCreator(
                    ctx,//null,
                    loggerManagerConfigStream,
                    roleManagerConfigStream,
                    componentManagerConfigStream,
                    instrumentManagerConfigStream );
            }
            catch( Exception e )
            {
                String msg = "Unable to create the ComponentManagerCreator.  "
                    + "Most likely a comfiguration problem.";
                throw new ServletException( msg, e );
            }
        }
        finally
        {
            // Close the resource streams
            try
            {
                if( loggerManagerConfigStream != null )
                {
                    loggerManagerConfigStream.close();
                }
                if( roleManagerConfigStream != null )
                {
                    roleManagerConfigStream.close();
                }
                if( componentManagerConfigStream != null )
                {
                    componentManagerConfigStream.close();
                }
                if( instrumentManagerConfigStream != null )
                {
                    instrumentManagerConfigStream.close();
                }
            }
            catch( IOException e )
            {
                throw new ServletException( "Encountered an error closing resource streams.", e );
            }
        }

        LoggerManager loggerManager = m_componentManagerCreator.getLoggerManager();

        // A series of ReferenceProxies which will be used to access the ComponentManager
        //  and other managers created by the ComponentManagerCreator must be created.
        //  This is necessary because the order in which servlets are shut down by a
        //  ServletContainer can not be controlled.  If a manager is disposed before all
        //  servlets have released their references to it, then errors can result.

        // Create the latch which will manager the ReferenceProxies.
        m_latch = new Latch( m_componentManagerCreator );
        m_latch.enableLogging( loggerManager.getLoggerForCategory( "system.ecmservlet" ) );

        // Create the actual ReferenceProxies.
        ReferenceProxy loggerManagerProxy = m_latch.createProxy(
            loggerManager, "LoggerManager" );
        ReferenceProxy serviceManagerProxy = m_latch.createProxy(
            m_componentManagerCreator.getServiceManager(), "ServiceManager" );
        ReferenceProxy componentManagerProxy = m_latch.createProxy(
            m_componentManagerCreator.getComponentManager(), "ComponentManager" );
        ReferenceProxy instrumentManagerProxy = m_latch.createProxy(
            m_componentManagerCreator.getInstrumentManager(), "InstrumentManager" );

        // Store references to the proxies in the ServletContext so that other servlets can gain
        //  access to them
        servletContext.setAttribute( LoggerManager.class.getName(),     loggerManagerProxy );
        servletContext.setAttribute( ServiceManager.class.getName(),    serviceManagerProxy );
        servletContext.setAttribute( ComponentManager.class.getName(),  componentManagerProxy );
        servletContext.setAttribute( InstrumentManager.class.getName(), instrumentManagerProxy );

		ApolloService.setManager((ServiceManager)serviceManagerProxy);

//		initLicenseJob(); // Added by jbwang, cause write them in component.xml can be modified.
        //System.out.println( "ExcaliburComponentManagerServlet.init() END" );
    }

	/**
	 * 如果已经注册,返回;
	 * 如果没有注册,同时还在试用期,那么添加license服务,5个小时检查一次license.
	 * 否则,提示license失效.关闭ApolloService.
	 * 没有放在component.xml文件里面描述,是因为component.xml可以被随意修改
	 */
	private void initLicenseJob()
	{
		StringBuffer buff = new StringBuffer();
		buff.append( "\n" );
		buff.append( "\n" );
		buff.append( "==============================================================" );
		buff.append( "\n" );
		buff.append( "==============================================================" );
		buff.append( "\n" );
		buff.append( "                  友通捷报网络数据处理系统               " );
		buff.append( "\n" );
		buff.append( "\n" );

		buff.append( "              E-mail:webmaster@youtong.com.cn           " );
		buff.append( "\n" );
		buff.append( "                       support@youtong.com.cn          " );
		buff.append( "\n" );
		buff.append( "             服务热线：010-68002026/7/8/9 转             " );
		buff.append( "\n" );
		buff.append( "                            209/212/215/216/217/218  " );
		buff.append( "\n" );
		buff.append( "\n" );

		try
		{
			if( ApolloLicense.isRegedited() )
			{
		buff.append( "                       软件已注册                   " );
		buff.append( "\n" );

				// 注册,就不添加license job
			}
			else if( ApolloLicense.isValid() )
			{
				int trailDay = ApolloLicense.trailDay();
				String sDay = "";
				if( trailDay < 10 )
					sDay = " " + trailDay;
				else
					sDay = "" + trailDay;
		buff.append( "                        软件试用剩余天数" + sDay + "天           " );
		buff.append( "\n" );

				ScheduleService ss = ( ScheduleService ) ApolloService.lookup(
					ScheduleService.ROLE );
				ss.addJob( new org.apache.fulcrum.schedule.turbine.JobEntry(
					0, 300, -1, -1, -1, LicenseScheduledJob.ROLE ) );
			}
			else
			{
				// 超出了试用期,也没有注册
		buff.append( "                             license过期                           " );
		buff.append( "\n" );
				ApolloService.setManager( (ServiceManager) null );
			}
		}
		catch( org.apache.avalon.framework.service.ServiceException ex )
		{
		buff.append( "                            无法取得系统服务                        " );
		buff.append( "\n" );
			ApolloService.setManager( (ServiceManager) null );
		}
		catch( cn.com.youtong.apollo.common.license.LicenseException le )
		{
		buff.append( "                             license文件出错                       " );
		buff.append( "\n" );
			ApolloService.setManager( (ServiceManager) null );
		}
		catch( Exception ex )
		{
		buff.append( "                              无法预见错误:                          " );
		buff.append( "\n" );
		buff.append( ex.getMessage() );
			ApolloService.setManager( (ServiceManager) null );
		}
		//buff.append( "                                                        " );
		buff.append( "\n" );
		//buff.append( "                                                        " );
		//buff.append( "\n" );
		buff.append( "              北京中普友通软件技术有限公司版权所有           " );
		buff.append( "\n" );
		buff.append( "                   Copyright 2003-2007                 " );
		buff.append( "\n" );
		buff.append( "                http://www.youtong.com.cn                  " );
		buff.append( "\n" );
		buff.append( "==============================================================" );
		buff.append( "\n" );
		buff.append( "==============================================================" );
		buff.append( "\n" );

		System.out.println( buff.toString() );
	}

	/**
	 * Create default avalon context
	 * urn:avalon:home  java.io.File  The working directory.
	 * urn:avalon:temp  java.io.File  The temporary directory that will be destroyed at the end of the session.
	 * urn:avalon:name  java.lang.String  The name assigned to the component.
	 * urn:avalon:partition  java.lang.String  The assigned partition name.
	 * webappRoot       java.lang.String web应用程序的路径
	 * @param servletConfig
	 * @return context
	 * @author zhou
	 */
	Context createDefaultContext(ServletConfig servletConfig)
	{
		ServletContext servletContext= servletConfig.getServletContext() ;
		DefaultContext context = new DefaultContext();
		String path= servletConfig.getInitParameter("urn:avalon:home");
		path= servletContext.getRealPath(path);
		context.put("urn:avalon:home", path);
		path= servletConfig.getInitParameter("webappRoot");
		path= servletContext.getRealPath(path);
		context.put("webappRoot", path);
		path= servletConfig.getInitParameter("applicationRoot");
		path= servletContext.getRealPath(path);
		context.put("applicationRoot", path);
		//context.makeReadOnly();

		return context;
	}

	void initLog4j()
	{
		try
		{
			ServletContext servletContext = getServletContext();
			String log4jConfiguration = getInitParameter("log4j");
			String log4jFile = servletContext.getRealPath(log4jConfiguration);

			PropertiesConfiguration configuration =	new PropertiesConfiguration(log4jFile);

			// webappRoot
			String path= getInitParameter("webappRoot");
			String webAppRoot = servletContext.getRealPath(path);
			//System.out.println("webappRoot ==== " + webAppRoot);
			configuration.setProperty("webappRoot", webAppRoot);

			// applicationRoot
			path= getInitParameter("applicationRoot");
			String applicationRoot = servletContext.getRealPath(path);
			//System.out.println("applicationRoot ==== " + applicationRoot);
			configuration.setProperty("applicationRoot", applicationRoot);

			Properties props= new Properties();
			//System.out.println("===================Properties========================");
			for(Iterator ite= configuration.getKeys(); ite.hasNext(); )
			{
				Object key= ite.next() ;
				Object value= configuration.getProperty(key.toString());
				if(value instanceof Vector)
				{
					StringBuffer buf= new StringBuffer();
					Vector v= (Vector)value;
					for(int i=0; i<v.size() ; i++)
					{
						buf.append(v.get(i));
						buf.append(", ");
					}
					if(buf.length() > 0)
						buf.delete(buf.length() - 2, buf.length());
					value = buf.toString() ;
				}
				else if(value instanceof String)
				{
					value = configuration.getString(key.toString());
				}
				else
				{
					System.out.println("Error!!!!!!!!!!");
				}

				props.put(key, value);
				//System.out.println(key + " = " + value);
			}
			//System.out.println("===================Configuration========================");
			//ConfigurationUtils.dump(configuration, System.out);

			org.apache.log4j.PropertyConfigurator.configure(props);
		}
		catch(Exception e)
		{

		}

	}

    /**
     * Called by the servlet container to destroy the servlet.
     */
    public void destroy()
    {
        //System.out.println( "ExcaliburComponentManagerServlet.destroy() BEGIN" );
		ApolloService.setManager( null );

        ServletContext servletContext = getServletContext();

        // Remove the references to the managers from the servlet context.
        servletContext.removeAttribute( LoggerManager.class.getName() );
        servletContext.removeAttribute( ServiceManager.class.getName() );
        servletContext.removeAttribute( ComponentManager.class.getName() );
        servletContext.removeAttribute( InstrumentManager.class.getName() );

        // Tell the latch that we are ready for it do dispose of the ECMC
        m_latch.requestTrigger();

        //System.out.println( "ExcaliburComponentManagerServlet.destroy() END" );
    }

    /**
     * This servlet does not accept requests.  It will complain if called.
     *
     * @param req servlet request
     * @param res servlet response
     *
     * @throws UnavailableException always
     */
    public void service( ServletRequest servletRequest, ServletResponse servletResponse )
        throws UnavailableException
    {
        throw new UnavailableException( getClass().getName() + " does not except service requests." );
    }

    /*---------------------------------------------------------------
     * Methods
     *-------------------------------------------------------------*/
    /**
     * Looks up a specified resource name and returns it as an InputStream.
     *  It is the responsibility of the caller to close the stream.
     *
     * @param servletConfig ServletConfig.
     * @param resourceName Name of the resource to be loaded.
     * @param required True if an error should be thrown if the property is missing.
     *
     * @return InputStream used to read the contents of the resource.
     *
     * @throws ServletException If the specified resource does not exist,
     *                          or could not be opened.
     */
    private InputStream getStreamFromParameter( ServletConfig servletConfig,
                                                String resourceName,
                                                boolean required )
        throws ServletException
    {

        String configFileName = servletConfig.getInitParameter( resourceName );

        if( configFileName == null )
        {
            if( required )
            {
                throw new ServletException( resourceName
                                            + " parameter must be provided in servlet configuration." );
            }
            else
            {
                return null;
            }
        }

        ServletContext servletContext = servletConfig.getServletContext();

        log( "Attempting to access resource: " + configFileName );

        InputStream is = servletContext.getResourceAsStream( configFileName );

        if( is == null )
        {
            throw new ServletException( "Resource '" + configFileName + "' is not available." );
        }

        return is;
    }

    /*---------------------------------------------------------------
     * Private Classes
     *-------------------------------------------------------------*/
    private static class Latch
        extends AbstractReferenceProxyLatch
    {
        ExcaliburComponentManagerCreator m_componentManagerCreator;

        /*---------------------------------------------------------------
         * Constructors
         *-------------------------------------------------------------*/
        /**
         * Create a new Latch.
         *
         * @param ecmc The ExcaliburComponentManagerCreator to be disposed
         *             when all proxies are done.
         */
        Latch( ExcaliburComponentManagerCreator componentManagerCreator )
        {
            m_componentManagerCreator = componentManagerCreator;
        }

        /*---------------------------------------------------------------
         * AbstractReferenceProxyLatch Methods
         *-------------------------------------------------------------*/
        /**
         * Called when all of the proxies have notified that they are done.
         */
        public void triggered()
            throws Exception
        {
            //System.out.println( "ExcaliburComponentManagerServlet.Latch.triggered() BEGIN" );
            ContainerUtil.shutdown( m_componentManagerCreator );
            //System.out.println( "ExcaliburComponentManagerServlet.Latch.triggered() END" );
        }
    }
}
