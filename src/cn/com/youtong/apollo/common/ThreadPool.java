/*
 * Created on 2003-10-10
 */
package cn.com.youtong.apollo.common;

import org.apache.commons.logging.*;

public class ThreadPool extends ThreadGroup
{
	private Log log = LogFactory.getLog(this.getClass());
	
	private final ThreadQueue pool = new ThreadQueue();
	private
	/*final*/ int maximum_size;
	private int pool_size;
	private boolean has_closed = false;

	private static int group_number = 0;
	private static int thread_id = 0;

	/******************************************************************
	 * These are the objects that wait to be activiated. They are
	 * typically blocked on an empty queue. You post a Runnable
	 * object to the queue to release a thread, which will execute
	 * the run() method from that object. All Pooled-thread objects
	 * will be members of the thread group the comprises the tread
	 * pool.
	 */

	private class WorkThread extends Thread
	{
		public WorkThread()
		{
			super(ThreadPool.this, "T" + thread_id);
			thread_id++;
		}

		public void run()
		{
			try
			{
				while(!has_closed)
				{
					((Runnable) (pool.dequeue())).run();
				}
			}
			catch(InterruptedException e)
			{ /* ignore it, stop thread */
			}
			catch(ThreadQueue.Closed e)
			{ /* ignore it, stop thread */
			}
		}
	}

	/******************************************************************
	 *	Create a thread pool with initial_thread_count threads
	 *	in it. The pool can expand to contain additional threads
	 *  if they are needed.
	 *
	 *	@param <b>initial_thread_count</b>	The initial thread count.
	 *			If the initial count is greater than the maximum, it is
	 *			silently truncated to the maximum.
	 *  @param <b>maximum_thread_count</b> specifies the maximum number
	 *			of threads that can be in the pool. A maximum of 0
	 *			indicates that the 	pool will be permitted to grow
	 *			indefinately.
	 */

	public ThreadPool(int initial_thread_count, int maximum_thread_count)
	{
		super("Thread_pool" + group_number++);

		maximum_size = (maximum_thread_count > 0) ? maximum_thread_count : Integer.MAX_VALUE;

			pool_size = Math.min(initial_thread_count, maximum_size);

		for(int i = pool_size; --i >= 0; )
		{
			new WorkThread().start();
		}
	}

	/******************************************************************
	 *	Create a dynamic Thread pool as if you had used
	 *	<code>Thread_pool(0, true);</code>
	 **/

	public ThreadPool()
	{
		super("Thread_pool" + group_number++);
		this.maximum_size = 0;
	}

	/******************************************************************
	 *  Execute the run() method of the Runnable object on a thread
	 *	in the pool. A new thread is created if the pool is
	 *	empty and the number of threads in the pool is not at the
	 *  maximum.
	 *
	 * 	@throws Thread_pool.Closed if you try to execute an action
	 *			on a pool to which a close() request has been sent.
	 */

	public synchronized void execute(Runnable action)
		throws Closed
	{
		// You must synchronize on the pool because the Pooled_thread's
		// run method is asychronously dequeueing elements. If we
		// didn't synchronize, it would be possilbe for the is_empty
		// query to return false, and then have a Pooled_thread sneak
		// in and put a thread on the queue (by doing a blocking dequeue).
		// In this scenario, the number of threads in the pool could
		// exceede the maximum specified in the constructor. The
		// test for pool_size < maximum_size is outside the synchronized
		// block because I didn't want to incur the overhead of
		// synchronization unnecessarily. This means that I could
		// occasionally create an extra thread unnecessarily, but
		// the pool size will never exceed the maximum.
		
		
		if(has_closed)
		{
			throw new Closed();
		}

		if(pool_size < maximum_size)
		{
			synchronized(pool)
			{
				if(pool.is_empty())
				{
					++pool_size;
					new WorkThread().start(); // Add thread to pool
				}
			}
		}

		pool.enqueue(action); // Attach action to it.
	}

	/******************************************************************
	 * Objects of class Thread_pool.Closed are thrown if you try to
	 * execute an action on a closed Thread_pool.
	 */

	public class Closed extends RuntimeException
	{
		Closed()
		{
			super("Tried to execute operation on a closed Thread_pool");
		}
	}

	/******************************************************************
	 *	Kill all the threads waiting in the thread pool, and arrange
	 *	for all threads that came out of the pool, but which are working,
	 *	to die natural deaths when they're finished with whatever they're
	 *	doing. Actions that have been passed to exectute() but which
	 *	have not been assigned to a thread for execution are discarded.
	 *	<p>
	 *  No further operations are permitted on a closed pool, though
	 *	closing a closed pool is a harmless no-op.
	 */

	public synchronized void close()
	{
		log.debug("Close pool");
		has_closed = true;
		pool.close(); // release all waiting threads
	}

}