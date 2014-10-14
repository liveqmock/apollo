/*
 * Created on 2003-10-23
 * ��Ԫ�����������ֵ����
 *
 * �����ֵ��������㷨
 * ����������Ŀɭ���㷨����DBDictionary�Ĺ������д���������Ŀ������
 *  1����DBDictionary������Entry��װ��DBDictionaryEntry��������һ��TreeSet�У������򣩣���TreeSet���������Ŀɭ��forest
 *  2������������Ŀɭ��
 *     ����ּ�λ������levelPostion
 *     for(int i = 0; i < levelPostion.length; i ++)
 *     {
 *         forest = buildForest(forest��levelPostion[i],i + 1��1);
 *     }
 * 		TreeSet  buildForest(TreeSet entries,int postion, int groupByCount,treeLevel)
 *		{
 *		   1�����entries.size=0������groupByCount  < treeLevel����entries
 *		   2��newһ��TreeSet����TreeSet����entries��positon�����Ĵ����ֵ�ɭ��forest
 *		   3�� ��entries��postion���飬��ÿһ���entry����һ��TreeSet��
 *		   4��for(ÿһ��)
 *		      {
 *		           ������ĵ�һ��entry������ȡ��������Ϊһ�����ĸ�
 *		        if(����ʣ��Ԫ�ص�size > 0)
 *		                {
 *		                       �������entry��Ϊ�����ĺ��ӽڵ㣻
 *		        }
 *		        �ݹ����buildForest�������ĺ��ӣ�postion, groupByCount,treeLevel+1����������ֵ��Ϊ�����ĺ��ӣ�
 *		         ���������뵽forest��
 *		      }
 *		   5������forest
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
	 * �����ֵ�ɭ��
	 */
	private TreeSet forest = new TreeSet();
	private boolean lookChildFlag = false;
	private DictionaryForm dictionaryForm;
        private HashMap dicMap=new HashMap();
	/**
	 * ���캯���������ֵ�ɭ�֣�
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
	 * ��ӡ�����ֵ�ɭ��
	 * @param forest �ֵ�ɭ��
	 */
	private void printForest(TreeSet forest)
	{
		Iterator treeItr = forest.iterator();
		while(treeItr.hasNext())
		{
			DBDictionaryEntry tree = (DBDictionaryEntry) treeItr.next();
			//System.out.println("��-------");
			printTree(tree, 1);
		}
	}

	/**
	 * ��ӡ��
	 * @param tree  ��
	 * @param treeLevel  �ּ���ʶ
	 */
	private void printTree(DBDictionaryEntry tree, int treeLevel)
	{
		for(int i = 0; i < treeLevel; i++)
		{
			System.out.print("   ");
		}
		System.out.println(tree.getKey());
		Iterator itr = tree.getChildren().iterator();
		//��ӡ����
		while(itr.hasNext())
		{
			DBDictionaryEntry childTree = (DBDictionaryEntry) itr.next();
			printTree(childTree, treeLevel + 1);
		}
	}

	/**
	 * ��ʼ�������ֵ�ɭ��
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
	 * ���ֵ���Ŀ���з���
	 * @param entries �ֵ���Ŀ
	 * @param levelposition �����ʶ
	 * @return ����һ�����ϣ������Ǹ��ݷ����ʶ�Ѿ��ֺ����TreeSet
	 */
	private Collection buildGroup(TreeSet entries, int levelposition)
	{
		//��ֵ�Ƚ�
		String compareStr = null;
		//�鼯��
		TreeSet groupTreeSet = null;
		//�����洢�ļ���
		Collection saveEntries = new ArrayList();
		Iterator itrEntries = entries.iterator();
		//����������Ŀ
		while(itrEntries.hasNext())
		{
			DBDictionaryEntry dBDictionaryEntry = (DBDictionaryEntry) itrEntries.next();
			//��һ�ν���ʱ�����ڼ����м���һ����Ŀ
			if(compareStr == null)
			{
				groupTreeSet = new TreeSet();
				groupTreeSet.add(dBDictionaryEntry);
				compareStr = dBDictionaryEntry.getKey().toString();
				continue;
				//�Ƚϼ�ֵ
			}
			else
			{
				boolean flag = this.compLevel(compareStr, dBDictionaryEntry.getKey().toString(), levelposition);
				//��ֵ��ͬ����ͬһ�����ϣ���ͬ����֮����һ���¼���

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
			//����ֵ�Ƚϱ�����ֵ
			compareStr = dBDictionaryEntry.getKey().toString();
		}
		//���ֺõĵļ��ϼ���洢����
		saveEntries.add(groupTreeSet);
		//���ش洢����
		return saveEntries;
	}

	/**
	 * �����ֵ�ɭ��
	 * @param entries  �������ĵ��ֵ���Ŀ
	 * @param Position  �ֵ�ɭ�ַ����ʶ
	 * @param groupByCount  �������
	 * @param treeLevel  �������
	 * @return �ֵ�ɭ��
	 */
	private TreeSet buildForest(TreeSet entries, int postion, int groupByCount, int treeLevel)
	{
		//���entries.size=0�������Ƶݹ鵽���ڷ�������˳�
		if(entries.isEmpty() || (groupByCount < treeLevel))
		{
			return entries;
		}
		TreeSet retForest = new TreeSet();
		//��entries��postion���飬��ÿһ���entry����һ��TreeSet��
		Collection groups = this.buildGroup(entries, postion);
		Iterator groupItr = groups.iterator();
		while(groupItr.hasNext())
		{
			//������ĵ�һ��entry������ȡ��������Ϊһ�����ĸ�
			TreeSet group = (TreeSet) groupItr.next();
			DBDictionaryEntry tree = null;
			Iterator noteItr = group.iterator();
			tree = (DBDictionaryEntry) noteItr.next();
			noteItr.remove();
			if(group.size() > 0)
			{
				tree.setChildren(group);
			}
			//�ݹ����buildForest�������ĺ��ӣ�postion, groupByCount,treeLevel+1����������ֵ��Ϊ�����ĺ���
			tree.setChildren(buildForest(tree.getChildren(), postion, groupByCount, treeLevel + 1));
			//���������뵽forest��
			retForest.add(tree);

		}
		return retForest;
	}

	/**
	 * �����ֵ���Ŀ��ֵ�õ�����Ŀ�������ӽڵ�
	 * @param key ��Ҫ�����ӽڵ�ļ�ֵ
	 * @return �˼�ֵ�������ӽڵ㼯��
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
	 * �Ӵ����ֵ���Ŀ�����в���ָ��key���ֵ���Ŀ
	 * @param entries �����ֵ���Ŀ����
	 * @param key Ҫ���ҵĴ����ֵ���Ŀ��key
	 * @return ָ��key���ֵ���Ŀ�����û�ҵ�������null
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
					//�ڸ���Ŀ������Ŀ�в���
					return lookForDictionaryEntry(entry.getChildren(), key);
				}
				//������һ�������ֵ���Ŀ
			}
		}
		//entries��sizeΪ0
		return null;
	}

	/**
	 * �жϴ����ֵ���Ŀ������ָ������Ŀ�Ƿ�����ӽ�ָ����Ŀkey�Ĵ����ֵ���Ŀ
	 * @param entryArray ָ���Ĵ����ֵ���Ŀ����
	 * @param index ָ������Ŀ�ڴ����ֵ���Ŀ�����е�λ��
	 * @param key ָ���Ĵ����ֵ���Ŀkey
	 * @return �������Ŀ����ӽ�key����Ŀ������true�����򷵻�false
	 */
	private boolean isNearestEntry(Object[] entryArray, int index, String key)
	{
		//ָ������Ŀ�Ƿ������������һ����Ŀ
		boolean isLastEntry = (index == entryArray.length - 1);
		if(isLastEntry)
		{
			return true;
		}
		//key�Ƿ�С��ָ����Ŀ����һ����Ŀ��keyֵ
		boolean lessThanNextEntry = (key.compareTo(((DBDictionaryEntry) entryArray[index + 1]).getKey().toString()) < 0);
		return lessThanNextEntry;
	}

	/**
	 * �Ƚ��ַ������ֵ�ļ�ֵ�ַ���
	 * @param retForm �����ֵ��һ����Ŀ
	 * @param entForm �����ֵ��һ����Ŀ
	 * @param level ����ؼ���
	 * @return true ��������Ŀ��һ����
	 * 		   false��������Ŀ����һ����
	 */
	private boolean compLevel(String retForm, String entForm, int level)
	{
        return (retForm.substring(0, level).equals(entForm.substring(0, level)));
	}

	/**
	 * �õ������ֵ�ĸ�
	 */
	public Iterator getRootItems()
	{
		return forest.iterator();
	}

	/**
	 * �жϱ�־��
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
	 * ת���ַ���Ϊ��������
	 * @param levels ��Ҫת�����ַ������ַ�����2,3,4��
	 * @return ��������
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
	 * �õ��ֵ�����ʶ
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
