/*
 * Created on 2003-10-10
 */
package cn.com.youtong.apollo.common;

import java.util.*;

public class ThreadQueue
{
	private LinkedList elements = new LinkedList();
	private boolean closed = false;
	private boolean reject_enqueue_requests = false;
	private int waiting_threads = 0;

	/*******************************************************************
	 * The Closed exception is thrown if you try to used an explicitly
	 * closed queue. See {@link #close}.
	 */

	public class Closed extends RuntimeException
	{
		private Closed()
		{
			super("Tried to access closed Blocking_queue");
		}
	}

	/*******************************************************************
	 *	Enqueue an object
	 **/
	public synchronized final void enqueue(Object new_element)
		throws Closed
	{
		if(closed || reject_enqueue_requests)
		{
			throw new Closed();
		}
		elements.addLast(new_element);
		notify(); //#notify
	}

	/*******************************************************************
	 * Enqueue an item, and thereafter reject any requests to enqueue
	 * additional items. The queue is closed automatically when the
	 * final item is dequeued.
	 */

	public synchronized final void enqueue_final_item(Object new_element) //#final.start
		throws Closed
	{
		enqueue(new_element);
		reject_enqueue_requests = true;
	} //#final.end

	/*******************************************************************
	 *	Dequeues an element; blocks if the queue is empty
	 *	(until something is enqueued). Be careful of nested-monitor
	 *  lockout if you call this function. You must ensure that
	 *	there's a way to get something into the queue that does
	 *  not involve calling a synchronized method of whatever
	 *  class is blocked, waiting to dequeue something. A timeout is
	 *	not supported because of a potential race condition (see text).
	 *  You can {@link Thread#interrupt interrupt} the dequeueing thread
	 *  to break it out of a blocked dequeue operation, however.
	 *
	 *  @see #enqueue
	 *  @see #drain
	 *  @see #nonblocking_dequeue
	 *	@return the dequeued object or null if the wait timed out and
	 *			nothing was dequeued.
	 */

	public synchronized final Object dequeue()
		throws InterruptedException, Closed
	{
		if(closed)
		{
			throw new Closed();
		}
		try
		{
			// If the queue is empty, wait. I've put the spin lock inside
			// an if so that the waiting_threads count doesn't jitter
			// while inside the spin lock. A thread is not considered to
			// be done waiting until it's actually acquired an element

			if(elements.size() <= 0)
			{
				++waiting_threads;
				while(elements.size() <= 0)
				{
					wait(); //#wait
					if(closed)
					{
						--waiting_threads;
						throw new Closed();
					}
				}
				--waiting_threads;
			}

			Object head = elements.removeFirst();

			if(elements.size() == 0 && reject_enqueue_requests)
			{
				close(); // just removed final item, close the queue.

			}
			return head;
		}
		catch(NoSuchElementException e) // Shouldn't happen
		{
			throw new Error("Internal error (com.holub.asynch.Blocking_queue)");
		}
	}

	/*******************************************************************
	 *	The is_empty() method is inherently unreliable in a
	 *  multithreaded situation. In code like the following,
	 *	it's possible for a thread to sneak in after the test but before
	 *	the dequeue operation and steal the element you thought you
	 *	were dequeueing.
	 *	<PRE>
	 *	Blocking_queue queue = new Blocking_queue();
	 *	//...
	 *	if( !some_queue.is_empty() )
	 *		some_queue.dequeue();
	 *	</PRE>
	 *	To do the forgoing reliably, you must synchronize on the
	 *	queue as follows:
	 *	<PRE>
	 *	Blocking_queue queue = new Blocking_queue();
	 *	//...
	 *	synchronized( queue )
	 *	{   if( !some_queue.is_empty() )
	 *			some_queue.dequeue();
	 *	}
	 *	</PRE>
	 *	The same effect can be achieved if the test/dequeue operation
	 *	is done inside a synchronized method, and the only way to
	 *	add or remove queue elements is from other synchronized
	 *	methods.
	 */

	public final boolean is_empty()
	{
		return elements.size() <= 0;
	}

	/*******************************************************************
	 * Return the number of threads waiting for a message on the
	 * current queue. See {@link is_empty} for warnings about
	 * synchronization.
	 */

	public final int waiting_threads()
	{
		return waiting_threads;
	}

	/*******************************************************************
	 * Close the blocking queue. All threads that are blocked
	 * [waiting in dequeue() for items to be enqueued] are released.
	 * The {@link dequeue()} call will throw a {@link Blocking_queue.Closed}
	 * runtime
	 * exception instead of returning normally in this case.
	 * Once a queue is closed, any attempt to enqueue() an item will
	 * also result in a Blocking_queue.Closed exception toss.
	 *
	 * The queue is emptied when it's closed, so if the only references
	 * to a given object are those stored on the queue, the object will
	 * become garbage collectable.
	 */

	public synchronized void close()
	{
		closed = true;
		elements = null;
		notifyAll();
	}
}