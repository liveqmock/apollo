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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Queue for the scheduler.
 *
 * @author <a href="mailto:mbryson@mont.mindspring.com">Dave Bryson</a>
 * @version $Id: JobQueue.java,v 1.1 2003/08/16 14:49:12 epugh Exp $
 */
public class JobQueue
{
    /**
     * The queue of <code>JobEntry</code> objects.
     */
    private List queue = null;

    /**
     * Creates a new instance.
     *
     * @exception Exception A generic exception.
     */
    public JobQueue()
        throws Exception
    {
        queue = new ArrayList(10);
    }

    /**
     * Return the next job off the top of the queue, or <code>null</code> if
     * there are no jobs in the queue.
     *
     * @return The next job in the queue.
     */
    public JobEntry getNext()
    {
        if ( queue.size() > 0 )
        {
            return (JobEntry)queue.get(0);
        }
        else
        {
            return null;
        }
    }

    /**
     * Return a specific job.
     *
     * @param je The JobEntry we are looking for.
     * @return A JobEntry.
     */
    public JobEntry getJob(JobEntry je)
    {
        int index = -1;

        if ( je != null )
        {
            index = queue.indexOf(je);
        }

        if ( index < 0 )
        {
            return null;
        }
        else
        {
            return (JobEntry)queue.get(index);
        }
    }

    /**
     * List jobs in the queue.  This is used by the scheduler UI.
     *
     * @return A List of <code>JobEntry</code> objects.
     */
    public List list()
    {
        return queue;
    }

    /**
     * Add a job to the queue.
     *
     * @param je A JobEntry job.
     */
    public synchronized void add(JobEntry je)
    {
        queue.add(je);
        sortQueue();
    }

    /**
     * Batch load jobs.  Retains any already enqueued jobs.  Called on
     * <code>SchedulerService</code> start-up.
     *
     * @param jobEntries A list of the <code>JobEntry</code> objects to load.
     */
    public synchronized void batchLoad(List jobEntries)
    {
        if (jobEntries != null)
        {
            queue.addAll(jobEntries);
            sortQueue();
        }

    }

    /**
     * Remove a job from the queue.
     *
     * @param je A JobEntry with the job to remove.
     */
    public synchronized void remove(JobEntry je)
    {
        queue.remove(je);
        sortQueue();
    }

    /**
     * Modify a job on the queue.
     *
     * @param je A JobEntry with the job to modify
     */
    public synchronized void modify(JobEntry je)
    {
        sortQueue();
    }

    /**
     * Update the job for its next run time.
     *
     * @param je A JobEntry to be updated.
     * @exception Exception, a generic exception.
     */
    public synchronized void updateQueue(JobEntry je)
        throws Exception
    {
        je.calcRunTime();
        sortQueue();
    }

    /**
     * Re-sort the existing queue.  Consumers of this method should be
     * <code>synchronized</code>.
     */
    private void sortQueue()
    {
        Comparator aComparator = new Comparator () 
            {
                public int compare(Object o1, Object o2) 
                {
                    Long time1 = new Long (((JobEntry)o1).getNextRuntime());
                    Long time2 = new Long (((JobEntry)o2).getNextRuntime());
                    return (time1.compareTo(time2));
                }
            };
                                               
        Collections.sort(queue,aComparator);
    }
}
