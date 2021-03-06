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
package org.apache.avalon.excalibur.logger.factory;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.ContextException;
import org.apache.log.LogTarget;
import org.apache.log.format.Formatter;
import org.apache.log.output.io.FileTarget;
import org.apache.log.output.io.rotate.FileStrategy;
import org.apache.log.output.io.rotate.OrRotateStrategy;
import org.apache.log.output.io.rotate.RevolvingFileStrategy;
import org.apache.log.output.io.rotate.RotateStrategy;
import org.apache.log.output.io.rotate.RotateStrategyByDate;
import org.apache.log.output.io.rotate.RotateStrategyBySize;
import org.apache.log.output.io.rotate.RotateStrategyByTime;
import org.apache.log.output.io.rotate.RotatingFileTarget;
import org.apache.log.output.io.rotate.UniqueFileStrategy;

/**
 * FileTargetFactory class.
 *
 * This factory is able to create different FileLogTargets according to the following
 * configuration syntax:
 *
 * <pre>
 * &lt;file id="foo"&gt;
 *  &lt;filename&gt;${context-key}/real-name/...&lt;/filename&gt;
 *  &lt;format type="raw|pattern|extended"&gt;pattern to be used if needed&lt;/format&gt;
 *  &lt;append&gt;true|false&lt;/append&gt;
 *  &lt;rotation type="revolving" init="5" max="10"&gt;
 *
 * or
 *
 *  &lt;rotation type="unique" pattern="yyyy-MM-dd-hh-mm-ss" suffix=".log"&gt;
 *   &lt;or&gt;
 *    &lt;size&gt;10000000&lt;/size&gt;
 *    &lt;time&gt;24:00:00&lt;/time&gt;
 *    &lt;time&gt;12:00:00&lt;/time&gt;
 *   &lt;/or&gt;
 *  &lt;/rotation&gt;
 * &lt;/file&gt;
 * </pre>
 *
 * <p>Some explanations about the Elements used in the configuration:</p>
 * <dl>
 *  <dt>&lt;filename&gt;</dt>
 *  <dd>
 *   This denotes the name of the file to log to. It can be constructed
 *   out of entries in the passed Context object as ${context-key}.
 *   This element is required.
 *  </dd>
 *  <dt>&lt;format&gt;</dt>
 *  <dd>
 *   The type attribute of the pattern element denotes the type of
 *   Formatter to be used and according to it the pattern to use for.
 *   This elements defaults to:
 *   <p>
 *    %7.7{priority} %5.5{time}   [%8.8{category}] (%{context}): %{message}\\n%{throwable}
 *   </p>
 *  </dd>
 *  <dt>&lt;append&gt;<dt>
 *  <dd>
 *   If the log file should be deleted every time the logger is creates
 *   (normally at the start of the applcation) or not and thus the log
 *   entries will be appended. This elements defaults to false.
 *  </dd>
 *  <dt>&lt;rotation&gt;</dt>
 *  <dd>
 *   This is an optional element.
 *   The type attribute determines which FileStrategy to user
 *   (revolving=RevolvingFileStrategy, unique=UniqueFileStrategy).
 *   The required init and max attribute are used to determine the initial and
 *   maximum rotation to use on a type="revolving" attribute.
 *   The optional pattern and suffix attribute are used to form filenames on
 *   a type="unique" attribute.
 *   <p> The initial rotation
 *   can be set to -1 in which case the system will first create the maximum
 *   number of file rotations by selecting the next available rotation and thereafter
 *   will overwrite the oldest log file.
 *  </dd>
 *  <dt>&lt;or&gt;</dt>
 *  <dd>uses the OrRotateStrategy to combine the children</dd>
 *  <dt>&lt;size&gt;</dt>
 *  <dd>
 *   The number of bytes if no suffix used or kilo bytes (1024) if suffixed with
 *   'k' or mega bytes (1024k) if suffixed with 'm' when a file rotation should
 *   occur. It doesn't make sense to specify more than one.
 *  </dd>
 *  <dt>&lt;time&gt;</dt>
 *  <dd>
 *   The time as HH:MM:SS when a rotation should occur. If you like to rotate
 *   a logfile more than once a day put an &lt;or&gt; element immediately after the
 *   &lt;rotation&gt; element and specify the times (and one size, too) inside the
 *   &lt;or&gt; element.
 *  </dd>
 *  <dt>&lt;date&gt;</dt>
 *  <dd>
 *   Rotation occur when string formatted date changed. Specify date formatting pattern.
 *  </dd>
 * </dl>
 *
 * @author <a href="mailto:giacomo@apache.org">Giacomo Pati</a>
 * @version CVS $Revision: 1.5 $ $Date: 2003/02/25 16:28:18 $
 * @since 4.0
 */
public class FileTargetFactory
    extends AbstractTargetFactory
{

    /**
     * Create a LogTarget based on a Configuration
     */
    public final LogTarget createTarget( final Configuration configuration )
        throws ConfigurationException
    {
        final Configuration confFilename = configuration.getChild( "filename" );
        final String filename = getFilename( confFilename.getValue() );

        final Configuration confRotation = configuration.getChild( "rotation", false );

        final Configuration confFormat = configuration.getChild( "format" );

        final Configuration confAppend = configuration.getChild( "append" );
        final boolean append = confAppend.getValueAsBoolean( false );

        final LogTarget logtarget;

        final File file = new File( filename );
        final Formatter formatter = getFormatter( confFormat );

        try
        {
            if( null == confRotation )
            {
                logtarget = new FileTarget( file, append, formatter );
            }
            else
            {
                final Configuration confStrategy = confRotation.getChildren()[ 0 ];
                final RotateStrategy rotateStrategy = getRotateStrategy( confStrategy );
                final FileStrategy fileStrategy = getFileStrategy( confRotation, file );

                logtarget =
                    new RotatingFileTarget( append, formatter, rotateStrategy, fileStrategy );
            }
        }
        catch( final IOException ioe )
        {
            throw new ConfigurationException( "cannot create FileTarget", ioe );
        }

        return logtarget;
    }

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    private static final long KILOBYTE = 1000;
    private static final long MEGABYTE = 1000 * KILOBYTE;

    private RotateStrategy getRotateStrategy( final Configuration conf )
    {
        final String type = conf.getName();

        if( "or".equals( type ) )
        {
            final Configuration[] configurations = conf.getChildren();
            final int size = configurations.length;

            final RotateStrategy[] strategies = new RotateStrategy[ size ];
            for( int i = 0; i < size; i++ )
            {
                strategies[ i ] = getRotateStrategy( configurations[ i ] );
            }

            return new OrRotateStrategy( strategies );
        }

        if( "size".equals( type ) )
        {
            final String value = conf.getValue( "2m" );

            final int count = value.length();
            final char end = value.charAt( count - 1 );
            final long no;
            final long size;

            switch( end )
            {
                case 'm':
                    no = Long.parseLong( value.substring( 0, count - 1 ) );
                    size = no * MEGABYTE;
                    break;
                case 'k':
                    no = Long.parseLong( value.substring( 0, count - 1 ) );
                    size = no * KILOBYTE;
                    break;
                default:
                    size = Long.parseLong( value );
            }

            return new RotateStrategyBySize( size );
        }

        if( "date".equals( type ) )
        {
            final String value = conf.getValue( "yyyyMMdd" );
            return new RotateStrategyByDate( value );
        }

        // default rotate strategy
        final String value = conf.getValue( "24:00:00" );

        // interpret a string like: ddd:hh:mm:ss ...
        final StringTokenizer tokenizer = new StringTokenizer( value, ":" );
        final int count = tokenizer.countTokens();
        long time = 0;
        for( int i = count; i > 0; i-- )
        {
            final long no = Long.parseLong( tokenizer.nextToken() );
            if( 4 == i )
            {
                time += no * DAY;
            }
            if( 3 == i )
            {
                time += no * HOUR;
            }
            if( 2 == i )
            {
                time += no * MINUTE;
            }
            if( 1 == i )
            {
                time += no * SECOND;
            }
        }

        return new RotateStrategyByTime( time );
    }

    protected FileStrategy getFileStrategy( final Configuration conf, final File file )
    {
        final String type = conf.getAttribute( "type", "unique" );

        if( "revolving".equals( type ) )
        {
            final int initialRotation =
                conf.getAttributeAsInteger( "init", 5 );
            final int maxRotation =
                conf.getAttributeAsInteger( "max", 10 );

            return new RevolvingFileStrategy( file, initialRotation, maxRotation );
        }

        // default file strategy
        final String pattern = conf.getAttribute( "pattern", null );
        final String suffix = conf.getAttribute( "suffix", null );
        if( pattern == null )
        {
            return new UniqueFileStrategy( file );
        }
        else
        {
            if( suffix == null )
            {
                return new UniqueFileStrategy( file, pattern );
            }
            else
            {
                return new UniqueFileStrategy( file, pattern, suffix );
            }
        }
    }

    protected Formatter getFormatter( final Configuration conf )
    {
        Formatter formatter = null;

        if( null != conf )
        {
            final FormatterFactory formatterFactory = new FormatterFactory();
            formatter = formatterFactory.createFormatter( conf );
        }

        return formatter;
    }

    /**
     * Process the file name.
     *
     * This method scans the file name passed for occurrences of
     * ${foo}. These strings get replaced by values from the Context
     * object indexed by the name (here foo).
     *
     * @param rawFilename The filename with substitutable placeholders
     * @return The processed file name
     * @throws ConfigurationException if substitutable values are not in the
     * Context object.
     */
    protected final String getFilename( String rawFilename )
        throws ConfigurationException
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        int j = -1;
        while( ( j = rawFilename.indexOf( "${", i ) ) > -1 )
        {
            if( m_context == null )
            {
                throw new ConfigurationException( "Context not available." );
            }
            if( i < j )
            {
                sb.append( rawFilename.substring( i, j ) );
            }
            int k = rawFilename.indexOf( '}', j );
            final String ctxName = rawFilename.substring( j + 2, k );
            final Object ctx;
            try
            {
                ctx = m_context.get( ctxName );
            }
            catch( final ContextException ce )
            {
                throw new ConfigurationException(
                    "missing entry '" + ctxName + "' in Context." );
            }
            sb.append( ctx.toString() );
            i = k + 1;
        }
        if( i < rawFilename.length() )
        {
            sb.append( rawFilename.substring( i, rawFilename.length() ) );
        }
        return sb.toString();
    }
}

