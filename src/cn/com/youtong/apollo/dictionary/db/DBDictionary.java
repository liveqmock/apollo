/*
 * Created on 2003-10-23
 * 单元描述：数据字典管理
 *
 * 数据字典树生成算法
 * 创建代码条目森林算法（在DBDictionary的构造器中创建代码条目树）：
 *  1、将DBDictionary的所有Entry封装成DBDictionaryEntry，并放入一个TreeSet中（可排序），该TreeSet代表代码条目森林forest
 *  2、创建代码条目森林
 *     计算分级位置数组levelPostion
 *     for(int i = 0; i < levelPostion.length; i ++)
 *     {
 *         forest = buildForest(forest，levelPostion[i],i + 1，1);
 *     }
 * 		TreeSet  buildForest(TreeSet entries,int postion, int groupByCount,treeLevel)
 *		{
 *		   1。如果entries.size=0，或者groupByCount  < treeLevel返回entries
 *		   2。new一个TreeSet，该TreeSet代表将entries按positon创建的代码字典森林forest
 *		   3。 将entries按postion分组，将每一组的entry放在一个TreeSet中
 *		   4。for(每一组)
 *		      {
 *		           将该组的第一个entry从组中取出，被作为一棵树的根
 *		        if(组中剩下元素的size > 0)
 *		                {
 *		                       将该组的entry作为该树的孩子节点；
 *		        }
 *		        递归调用buildForest（该树的孩子，postion, groupByCount,treeLevel+1），将返回值作为该树的孩子；
 *		         将该树加入到forest中
 *		      }
 *		   5。返回forest
 *		}
 */
package cn.com.youtong.apollo.dictionary.db;

import java.util.*;

import cn.com.youtong.apollo.dictionary.Dictionary;
import cn.com.youtong.apollo.dictionary.db.form.*;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DBDictionary
	implements Dictionary
{

	/**
	 * 代码字典森林
	 */
	private TreeSet forest = new TreeSet();
	private boolean lookChildFlag = false;
	private DictionaryForm dictionaryForm;
        private HashMap dicMap=new HashMap();
	/**
	 * 构造函数（生成字典森林）
	 * @param dictionaryForm
	 */
	public DBDictionary(DictionaryForm dictionaryForm)
	{
		this.dictionaryForm = dictionaryForm;
		int[] levelPostion = getLevels();
		initForest();
		for(int i = 0; i < levelPostion.length; i++)
		{
			forest = buildForest(forest, levelPostion[i], i + 1, 1);
		}

              Set set= dictionaryForm.getDictionaryEntries();
              Iterator itr=set.iterator();
              while(itr.hasNext())
              {
                    DictionaryEntryForm entry=(DictionaryEntryForm)itr.next();
                    dicMap.put(entry.getEntryKey(),entry.getEntryValue());

              }

//        printForest(forest);
	}

	/**
	 * 打印代码字典森林
	 * @param forest 字典森林
	 */
	private void printForest(TreeSet forest)
	{
		Iterator treeItr = forest.iterator();
		while(treeItr.hasNext())
		{
			DBDictionaryEntry tree = (DBDictionaryEntry) treeItr.next();
			//System.out.println("树-------");
			printTree(tree, 1);
		}
	}

	/**
	 * 打印树
	 * @param tree  树
	 * @param treeLevel  分级标识
	 */
	private void printTree(DBDictionaryEntry tree, int treeLevel)
	{
		for(int i = 0; i < treeLevel; i++)
		{
			System.out.print("   ");
		}
		System.out.println(tree.getKey());
		Iterator itr = tree.getChildren().iterator();
		//打印子树
		while(itr.hasNext())
		{
			DBDictionaryEntry childTree = (DBDictionaryEntry) itr.next();
			printTree(childTree, treeLevel + 1);
		}
	}

	/**
	 * 初始化代码字典森林
	 *
	 */
	private void initForest()
	{
		Iterator entriesItr = dictionaryForm.getDictionaryEntries().iterator();
		while(entriesItr.hasNext())
		{
			DictionaryEntryForm dictionaryEntryForm = (DictionaryEntryForm) entriesItr.next();
			forest.add(new DBDictionaryEntry(dictionaryEntryForm));
		}
	}

	/**
	 * 对字典条目进行分组
	 * @param entries 字典条目
	 * @param levelposition 分组标识
	 * @return 返回一个集合，里面是根据分组标识已经分好组的TreeSet
	 */
	private Collection buildGroup(TreeSet entries, int levelposition)
	{
		//键值比较
		String compareStr = null;
		//组集合
		TreeSet groupTreeSet = null;
		//用来存储的集合
		Collection saveEntries = new ArrayList();
		Iterator itrEntries = entries.iterator();
		//遍历所有条目
		while(itrEntries.hasNext())
		{
			DBDictionaryEntry dBDictionaryEntry = (DBDictionaryEntry) itrEntries.next();
			//第一次进入时，先在集合中加入一个条目
			if(compareStr == null)
			{
				groupTreeSet = new TreeSet();
				groupTreeSet.add(dBDictionaryEntry);
				compareStr = dBDictionaryEntry.getKey().toString();
				continue;
				//比较键值
			}
			else
			{
				boolean flag = this.compLevel(compareStr, dBDictionaryEntry.getKey().toString(), levelposition);
				//键值相同加入同一个集合，不同即将之加入一个新集合

				if(flag)
				{
					groupTreeSet.add(dBDictionaryEntry);
				}
				else
				{
					saveEntries.add(groupTreeSet);
					groupTreeSet = new TreeSet();
					groupTreeSet.add(dBDictionaryEntry);
				}
			}
			//给键值比较变量赋值
			compareStr = dBDictionaryEntry.getKey().toString();
		}
		//将分好的的集合加入存储集合
		saveEntries.add(groupTreeSet);
		//返回存储集合
		return saveEntries;
	}

	/**
	 * 构造字典森林
	 * @param entries  被构建的的字典条目
	 * @param Position  字典森林分组标识
	 * @param groupByCount  分组深度
	 * @param treeLevel  分组控制
	 * @return 字典森林
	 */
	private TreeSet buildForest(TreeSet entries, int postion, int groupByCount, int treeLevel)
	{
		//如果entries.size=0或分组控制递归到大于分组深度退出
		if(entries.isEmpty() || (groupByCount < treeLevel))
		{
			return entries;
		}
		TreeSet retForest = new TreeSet();
		//将entries按postion分组，将每一组的entry放在一个TreeSet中
		Collection groups = this.buildGroup(entries, postion);
		Iterator groupItr = groups.iterator();
		while(groupItr.hasNext())
		{
			//将该组的第一个entry从组中取出，被作为一棵树的根
			TreeSet group = (TreeSet) groupItr.next();
			DBDictionaryEntry tree = null;
			Iterator noteItr = group.iterator();
			tree = (DBDictionaryEntry) noteItr.next();
			noteItr.remove();
			if(group.size() > 0)
			{
				tree.setChildren(group);
			}
			//递归调用buildForest（该树的孩子，postion, groupByCount,treeLevel+1），将返回值作为该树的孩子
			tree.setChildren(buildForest(tree.getChildren(), postion, groupByCount, treeLevel + 1));
			//将该树加入到forest中
			retForest.add(tree);

		}
		return retForest;
	}

	/**
	 * 根据字典条目键值得到此条目的所有子节点
	 * @param key 需要查找子节点的键值
	 * @return 此键值的所以子节点集合
	 */
	public Iterator getChildItems(String key)
	{
		DBDictionaryEntry entry = lookForDictionaryEntry(forest, key);
		if(entry == null)
		{
			return new TreeSet().iterator();
		}
		else
		{
			return entry.getChildren().iterator();
		}
	}

	/**
	 * 从代码字典条目集合中查找指定key的字典条目
	 * @param entries 代码字典条目集合
	 * @param key 要查找的代码字典条目的key
	 * @return 指定key的字典条目，如果没找到，返回null
	 */
	private DBDictionaryEntry lookForDictionaryEntry(TreeSet entries, String key)
	{
		Object[] entryArray = entries.toArray();
		for(int i = 0; i < entryArray.length; i++)
		{
			DBDictionaryEntry entry = (DBDictionaryEntry) entryArray[i];
			if(key.compareTo(entry.getKey().toString()) < 0)
			{
				return null;
			}
			else if(key.compareTo(entry.getKey().toString()) == 0)
			{
				return entry;
			}
			else
			{
				if(isNearestEntry(entryArray, i, key))
				{
					//在该条目的子条目中查找
					return lookForDictionaryEntry(entry.getChildren(), key);
				}
				//继续下一个代码字典条目
			}
		}
		//entries的size为0
		return null;
	}

	/**
	 * 判断代码字典条目数组中指定的条目是否是最接近指定条目key的代码字典条目
	 * @param entryArray 指定的代码字典条目数组
	 * @param index 指定的条目在代码字典条目数组中的位置
	 * @param key 指定的代码字典条目key
	 * @return 如果该条目是最接近key的条目，返回true；否则返回false
	 */
	private boolean isNearestEntry(Object[] entryArray, int index, String key)
	{
		//指定的条目是否是数组中最后一个条目
		boolean isLastEntry = (index == entryArray.length - 1);
		if(isLastEntry)
		{
			return true;
		}
		//key是否小于指定条目的下一个条目的key值
		boolean lessThanNextEntry = (key.compareTo(((DBDictionaryEntry) entryArray[index + 1]).getKey().toString()) < 0);
		return lessThanNextEntry;
	}

	/**
	 * 比较字符数据字典的键值字符串
	 * @param retForm 数据字典的一个条目
	 * @param entForm 数据字典的一个条目
	 * @param level 分组关键字
	 * @return true ：两个条目是一个组
	 * 		   false：两个条目不是一个组
	 */
	private boolean compLevel(String retForm, String entForm, int level)
	{
        return (retForm.substring(0, level).equals(entForm.substring(0, level)));
	}

	/**
	 * 得到数据字典的根
	 */
	public Iterator getRootItems()
	{
		return forest.iterator();
	}

	/**
	 * 判断标志符
	 */
	public boolean getFlag(int flag)
	{
		boolean ret = false;
		if((dictionaryForm.getFlag() & flag) > 0)
		{
			ret = true;
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.Dictionary#getKeyLegth()
	 */
	public int getKeyLegth()
	{
		return dictionaryForm.getKeyLength();
	}

	/**
	 * 转换字符串为整型数组
	 * @param levels 需要转换的字符串，字符串“2,3,4”
	 * @return 整型数组
	 */
	private int[] convertStrToInts(String levels)
	{
		StringTokenizer token = new StringTokenizer(levels, ",");
		int[] result = new int[token.countTokens()];
		int i = 0;
		while(token.hasMoreTokens())
		{
			result[i] = new Integer(token.nextToken()).intValue();
			i++;
		}
		return result;
	}

	/**
	 * 得到字典分组标识
	 */
	public int[] getLevels()
	{
		String levels = dictionaryForm.getLevelPosition();
		if(levels == null || levels.trim().length() == 0)
		{
			return new int[0];
		}
		return this.convertStrToInts(levels);
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.Dictionary#getMemo()
	 */
	public String getMemo()
	{
		return dictionaryForm.getMemo();
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.task.Dictionary#getName()
	 */
	public String getName()
	{
		return dictionaryForm.getName();
	}

	/* (non-Javadoc)
	 * @see cn.com.youtong.apollo.dictionary.Dictionary#id()
	 */
	public String id()
	{
		return dictionaryForm.getDictid();
	}

        /*
	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */

	public Object get(Object key)
	{
	    return dicMap.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map t)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	public void clear()
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	public Set keySet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	public Collection values()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	public Set entrySet()
	{
		Set entrySet = new TreeSet();
		Iterator iter = dictionaryForm.getDictionaryEntries().iterator();
		while(iter.hasNext())
		{
			DictionaryEntryForm dictionaryEntryForm = (DictionaryEntryForm) iter.next();
			entrySet.add(new DBDictionaryEntry(dictionaryEntryForm));
		}
		return entrySet;
	}

}
