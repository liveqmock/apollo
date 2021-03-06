/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) @year@ The Apache Software Foundation. All rights reserved.

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
package org.apache.avalon.excalibur.logger;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.Log4JLogger;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerRepository;

/**
 * Log4JLoggerManager implementation.  This is the interface used to get instances of
 * a Logger for your system.  This manager does not set up the categories--it
 * leaves that as an excercise for Log4J's construction.
 *
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 * @version CVS $Revision: 1.10 $ $Date: 2003/02/25 16:28:24 $
 * @since 4.1
 */
public class Log4JLoggerManager
    implements LoggerManager, LogEnabled
{
    /** Map for name to logger mapping */
    private final Map m_loggers = new HashMap();

    /** The root logger to configure */
    //private String m_prefix;

    /** The hierarchy private to Log4JManager */
    private LoggerRepository m_hierarchy;

    /** The default logger used for this system */
    private final Logger m_defaultLogger;

    /** The logger used to log output from the logger manager. */
    private Logger m_logger;

    /**
     * Creates a new <code>DefaultLog4JManager</code>. It will use a new <code>Hierarchy</code>.
     */
    public Log4JLoggerManager()
    {
        this( LogManager.getLoggerRepository() );
    }

    /**
     * Creates a new <code>DefaultLog4JManager</code> with an existing <code>Hierarchy</code>.
     */
    public Log4JLoggerManager( final LoggerRepository hierarchy )
    {
        this( null, hierarchy );
    }

    /**
     * Creates a new <code>DefaultLog4JManager</code> using
     * specified logger name as root logger.
     */
    public Log4JLoggerManager( final String prefix )
    {
        this( prefix, LogManager.getLoggerRepository() );
    }

    /**
     * Creates a new <code>DefaultLog4JManager</code> with an existing <code>Hierarchy</code> using
     * specified logger name as root logger.
     */
    public Log4JLoggerManager( final String prefix,
                               final LoggerRepository hierarchy )
    {
        this( prefix, hierarchy,
              new Log4JLogger( hierarchy.getLogger( "" ) ) );
    }

    /**
     * Creates a new <code>DefaultLog4JManager</code> with an existing <code>Hierarchy</code> using
     * specified logger name as root logger.
     */
    public Log4JLoggerManager( final String prefix,
                               final LoggerRepository hierarchy,
                               final Logger defaultLogger )
    {
        this( prefix, hierarchy, defaultLogger, defaultLogger );
    }

    /**
     * Creates a new <code>DefaultLog4JManager</code> with an existing <code>Hierarchy</code> using
     * specified logger name as root logger.
     */
    public Log4JLoggerManager( final String prefix,
                               final LoggerRepository hierarchy,
                               final Logger defaultLogger,
                               final Logger logger )
    {
        //m_prefix = prefix;
        m_hierarchy = hierarchy;
        m_defaultLogger = defaultLogger;
        m_logger = logger;
    }

    /**
     * Provide a logger.
     *
     * @param logger the logger
     **/
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Retrieves a Logger from a category name. Usually
     * the category name refers to a configuration attribute name.  If
     * this Log4JManager does not have the match the default Logger will
     * be returned and a warning is issued.
     *
     * @param categoryName  The category name of a configured Logger.
     * @return the Logger.
     */
    public final Logger getLoggerForCategory( final String categoryName )
    {
        Logger logger = (Logger)m_loggers.get( categoryName );

        if( null != logger )
        {
            if( m_logger.isDebugEnabled() )
            {
                final String message =
                    "Logger for category " + categoryName + " returned";
                m_logger.debug( message );
            }
            return logger;
        }

        if( m_logger.isDebugEnabled() )
        {
            final String message = "Logger for category " + categoryName +
                " not defined in configuration. New Logger created and returned";
            m_logger.debug( message );
        }

        logger = new Log4JLogger( m_hierarchy.getLogger( categoryName ) );
        m_loggers.put( categoryName, logger );
        return logger;
    }

    public final Logger getDefaultLogger()
    {
        return m_defaultLogger;
    }
}
