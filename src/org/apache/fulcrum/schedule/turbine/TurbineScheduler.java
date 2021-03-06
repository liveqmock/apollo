package org.apache.fulcrum.schedule.turbine;
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Turbine" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Turbine", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
import java.util.List;
import java.util.Iterator;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.fulcrum.schedule.ScheduleService;
/**
 * Service for a cron like scheduler.
 *
 * @author <a href="mailto:epugh@upstate.com">epugh@upstate.com</a>
 * @author <a href="mailto:mbryson@mont.mindspring.com">Dave Bryson</a>
 * @version $Id: TurbineScheduler.java,v 1.1 2003/08/16 14:49:12 epugh Exp $
 */
public abstract class TurbineScheduler extends AbstractLogEnabled implements ScheduleService
{
    /**
     * The queue.
     */
    protected JobQueue scheduleQueue = null;
    /**
     * The main loop for starting jobs.
     */
    protected MainLoop mainLoop;
    /**
     * The thread used to process commands.
     */
    protected Thread thread;
    /**
     * Creates a new instance.
     */
    public TurbineScheduler()
    {
        mainLoop = null;
        thread = null;
    }
    /**
     * Called the first time the Service is used.<br>
     *
     * Load all the jobs from cold storage.  Add jobs to the queue
     * (sorted in ascending order by runtime) and start the scheduler
     * thread.
     *
     */
    public void configure(Configuration conf) throws ConfigurationException
    {
        try
        {
            scheduleQueue = new JobQueue();
            mainLoop = new MainLoop();
            // Load all from cold storage.
            List jobs = loadJobs(conf);
            if (jobs != null && jobs.size() > 0)
            {
                Iterator it = jobs.iterator();
                while (it.hasNext())
                {
                    ((JobEntry) it.next()).calcRunTime();
                }
                scheduleQueue.batchLoad(jobs);
            }
        }
        catch (Exception e)
        {
            getLogger().error("Could not configure scheduler.", e);
            throw new ConfigurationException(e.getMessage());
        }
    }
    /**
    	* Called the first time the Service is used.<br>
    	*
    	* Load all the jobs from cold storage.  Add jobs to the queue
    	* (sorted in ascending order by runtime) and start the scheduler
    	* thread.
    	*/
    public void initialize() throws Exception
    {
        try
        {
            restart();
        }
        catch (Exception e)
        {
            getLogger().error("Cannot initialize NonPersistentSchedulerService!: ", e);
        }
    }
    /**
     * Shutdowns the service.
     *
     * This methods interrupts the housekeeping thread.
     */
    public void shutdown()
    {
        if (getThread() != null)
        {
            getThread().interrupt();
        }
    }
    /**
     * List jobs in the queue.  This is used by the scheduler UI.
     *
     * @return A List of jobs.
     */
    public List listJobs()
    {
        return scheduleQueue.list();
    }
    /**
     * Return the thread being used to process commands, or null if
     * there is no such thread.  You can use this to invoke any
     * special methods on the thread, for example, to interrupt it.
     *
     * @return A Thread.
     */
    public synchronized Thread getThread()
    {
        return thread;
    }
    /**
     * Set thread to null to indicate termination.
     */
    private synchronized void clearThread()
    {
        thread = null;
    }
    /**
     * Start (or restart) a thread to process commands, or wake up an
     * existing thread if one is already running.  This method can be
     * invoked if the background thread crashed due to an
     * unrecoverable exception in an executed command.
     */
    public synchronized void restart()
    {
        if (thread == null)
        {
            // Create the the housekeeping thread of the scheduler. It will wait
            // for the time when the next task needs to be started, and then
            // launch a worker thread to execute the task.
            thread = new Thread(mainLoop, ROLE);
            // Indicate that this is a system thread. JVM will quit only when there
            // are no more active user threads. Settings threads spawned internally
            // by Turbine as daemons allows commandline applications using Turbine
            // to terminate in an orderly manner.
            thread.setDaemon(true);
            thread.start();
        }
        else
        {
            notify();
        }
    }
    /**
     *  Return the next Job to execute, or null if thread is
     *  interrupted.
     *
     * @return A JobEntry.
     * @exception Exception, a generic exception.
     */
    private synchronized JobEntry nextJob() throws Exception
    {
        try
        {
            while (!Thread.interrupted())
            {
                // Grab the next job off the queue.
                JobEntry je = scheduleQueue.getNext();
                if (je == null)
                {
                    // Queue must be empty. Wait on it.
                    wait();
                }
                else
                {
                    long now = System.currentTimeMillis();
                    long when = je.getNextRuntime();
                    if (when > now)
                    {
                        // Wait till next runtime.
                        wait(when - now);
                    }
                    else
                    {
                        // Update the next runtime for the job.
                        scheduleQueue.updateQueue(je);
                        // Return the job to run it.
                        return je;
                    }
                }
            }
        }
        catch (InterruptedException ex)
        {
        }
        // On interrupt.
        return null;
    }
    /**
     * Inner class.  This is isolated in its own Runnable class just
     * so that the main class need not implement Runnable, which would
     * allow others to directly invoke run, which is not supported.
     */
    protected class MainLoop implements Runnable
    {
        /**
         * Method to run the class.
         */
        public void run()
        {
            try
            {
                for (;;)
                {
                    JobEntry je = nextJob();
                    if (je != null)
                    {
                        // Start the thread to run the job.
                        Runnable wt = new WorkerThread(je);
                        Thread helper = new Thread(wt);
                        helper.start();
                    }
                    else
                    {
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                // Log error.
                getLogger().error("Error running a Scheduled Job: " + e);
            }
            finally
            {
                clearThread();
            }
        }
    }
    protected abstract List loadJobs(Configuration conf) throws ConfigurationException;
}
