package cn.com.youtong.tools.jsptree;

/**
 * Title:树的缺省实现。
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.util.*;

public class DefaultTree
	implements Tree
{
	private boolean debug = false;
	private Hashtable treeHt = new Hashtable(); //树形数据的容器

	public DefaultTree()
	{
		Item root = new Item("", "");
		treeHt.put("0", root); //填写根节点内容
	}

	public void add(String key, Object value)
		throws TreeException
	{
		Vector keyVec = split(key);
		checkIntegrality(keyVec);
		treeHt.put(key, value);
	}

	public Object get(String key)
	{
		return treeHt.get(key);
	}

	public void set(String key, Object val)
	{
		treeHt.remove(key);
		treeHt.put(key, val);
	}

	/*
	   private Vector getParentPath(Vector key){
	  int[] temp=new int[key.length-1];
	  for(int i=0;i<key.length-1;i++){
		temp[i]=key[i];
	  }
	  return temp;
	   }
	 */
	public Vector getChildrenValue(String key)
		throws TreeException
	{
		return getChildren(key, false);
	}

	public Vector getChildrenKey(String key)
		throws TreeException
	{
		return getChildren(key, true);
	}

	private Vector getChildren(String key, boolean returnKeys)
		throws TreeException
	{
		if(key == null)
		{
			throw new TreeException("parameter wrong.");
		}
		Vector rs = new Vector();
		String cursor = "";
		Object temp = "";
		int i = 1;
		cursor = key + "," + i;
		temp = (Object) get(cursor);
		while(temp != null)
		{
			if(returnKeys)
			{
				rs.add(cursor);
			}
			else
			{
				rs.add(temp);
			}
			i++;
			cursor = key + "," + i;
			if(debug)
			{
				System.out.println("child:" + cursor + ":" + temp);
			}
			temp = (Object) get(cursor);
		}
		return rs;
	}

	public boolean isLeafNode(String key)
		throws TreeException
	{ //是否是叶子节点
		if(key == null)
		{
			throw new TreeException("parameter wrong.");
		}
		String firstChildKey = key + ",1";
		if(treeHt.get(firstChildKey) == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void checkIntegrality(Vector keyVec)
		throws TreeException
	{
		if(keyVec.size() < 2)
		{ //路径长度不小于2
			throw new TreeException("path length error");

		}
		else if((Integer.parseInt((String) keyVec.elementAt(0)) != 0))
		{ //根节点比为0
			throw new TreeException("root error");

		}
		else if(keyVec.size() > 1 && (Integer.parseInt((String) keyVec.elementAt(keyVec.size() - 1))) < 1)
		{ //非根节点编码从1开始
			throw new TreeException("path code must begin with 1");

		}

		Vector tempkeyVec = (Vector) keyVec.clone();
		int size = tempkeyVec.size();
		if(Integer.parseInt((String) tempkeyVec.elementAt(size - 1)) != 1)
		{
			tempkeyVec.set(size - 1, new Integer(Integer.parseInt((String) tempkeyVec.elementAt(size - 1)) - 1)); //取得同层次,上一个节点的编码

			if(debug)
			{
				System.out.println("tempkeyVec" + tempkeyVec);
			}
			if(get(join(tempkeyVec)) == null)
			{ //同一层次上节点编码是连续的自然数
				throw new TreeException("path code must be continuous");
			}
		}
		tempkeyVec = (Vector) keyVec.clone();
		for(int i = keyVec.size() - 1; i > 1; i--)
		{ //路径上每一父节点(除开根节点)都不能为空
			//tempkeyVec=getParentPath(tempkeyVec);
			tempkeyVec.remove(i);
			for(int j = 0; j < tempkeyVec.size(); j++)
			{
				if(debug)
				{
					System.out.println(j + ":[" + tempkeyVec.elementAt(j) + "] ");
				}
			}
			if(debug)
			{
				System.out.println(tempkeyVec.size() + " " + i + " " + get(join(tempkeyVec)));
			}
			if(get(join(tempkeyVec)) == null)
			{
				throw new TreeException("every parent node must be not null in the path.");
			}
		}

	}

	private Vector split(String elements)
		throws TreeException
	{
		// 将字符串按","分离数据
		Vector param = new Vector();
		int i = 0;
		String temp = "";

		int j = elements.indexOf(",", i);
		while(j > -1)
		{
			temp = elements.substring(i, j);
			param.add(temp);
			i = j + 1;
			j = elements.indexOf(",", i);
		}
		temp = elements.substring(i, elements.length());
		param.add(temp);
		return param;
	}

	private String join(Vector vec)
	{
		String rs = "";
		for(int i = 0; i < vec.size(); i++)
		{
			if(i == 0)
			{
				rs += vec.elementAt(i);
			}
			else
			{
				rs += "," + vec.elementAt(i);
			}
		}
		return rs;
	}

	public static void main(String[] args)
		throws Exception
	{

		DefaultTree treeData1 = new DefaultTree();
		/*
			treeData1.add("0,1",new String("a01"));
			treeData1.add("0,1,1","a011");
			treeData1.add("0,1,2","a012");
			treeData1.add("0,1,2,1","a0121");
			treeData1.add("0,1,2,1,1","a01211");
			treeData1.add("0,1,2,2","a0122");
			treeData1.add("0,1,3","a013");
			treeData1.add("0,1,4","a014");
			treeData1.add("0,2","a02");
			treeData1.add("0,2,1","a021");
			treeData1.add("0,3","a03");
		 */System.out.println("get:" + treeData1.get("0,1,4"));
		//System.out.println( "children:"+ treeData1.getChildrenValue("0,1"));
		//
		/**/

		/*
			Hashtable temp=new Hashtable();
			Vector vec=new Vector();
			vec.add("2");
			vec.add("3");
//    vec.remove(0);
			System.out.println(vec.size() +" "+   vec.elementAt(0));
			Vector vec2=new Vector();
			temp.put(vec,new String("abc"));
			temp.put(vec2,new String("bbb"));
			System.out.println(temp.get(vec));
				System.out.println("vec.hashCode():"+vec.hashCode());
		 */System.out.println("succeed!");

	}
}
