package cn.com.youtong.tools.jsptree;

/**
 * Title:�������ݽӿڡ�
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.util.*;

public interface Tree
{
	public void add(String key, Object value)
		throws TreeException;

	public Object get(String key);

	public void set(String key, Object val);

	public boolean isLeafNode(String key)
		throws TreeException;

	public Vector getChildrenKey(String key)
		throws TreeException;
}