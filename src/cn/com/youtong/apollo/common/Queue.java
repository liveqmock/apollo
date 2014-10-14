package cn.com.youtong.apollo.common;

import java.util.LinkedList;
import java.util.List;

/**
 * 线程安全的队列
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: YouTong</p>
 * @author wjb
 * @version 1.0
 */
public class Queue
{
	private List queue = null;

	public Queue()
	{
		queue = new LinkedList();
	}

	/**
	 * Return the next object off the top of the queue, or <code>null</code> if
	 * there are no objects in the queue.
	 *
	 * @return The next object in the queue.
	 */
	public Object getNext()
	{
		if ( queue.size() > 0 )
		{
			return queue.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * List objects in the queue.
	 * @return A List of <code>Object</code> objects.
	 */
	public List list()
	{
		return queue;
	}

	/**
	 * Add a object to the queue.
	 *
	 * @param obj A Object.
	 */
	public synchronized void add(Object obj)
	{
		queue.add(obj);
	}

	/**
	 * Remove a object from the queue.
	 *
	 * @param obj A Object to remove.
	 */
	public synchronized void remove(Object obj)
	{
		queue.remove(obj);
	}
}