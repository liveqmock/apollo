package cn.com.youtong.apollo.common;

import java.util.*;
import java.io.Serializable;


/**
 * 每页数据对象
 */
public class Page implements Serializable {

    public static final int MAXPAGECOUNT=10000;

    public static final Page EMPTY_PAGE =
        new Page(new ArrayList(), 0,10,0);

    ArrayList objects;

    int start;
    int totalCount;
    int pageSize;


    public Page(ArrayList l, int start,int pageSize,int totalCount) {
        this.objects=l;
        this.start = start;
        this.pageSize=pageSize;
        this.totalCount=totalCount;
    }

    public ArrayList getList() { return objects; }

    public int getStartOfNextPage()
     {
        return start + objects.size();
    }

    public int getStartOfPreviousPage() {
        return Math.max(start-objects.size(), 0);
    }

    public int getLastPageStart()
      {
         if((totalCount%getPageSize())==0)
            return  totalCount-getPageSize();
         else
           return totalCount-totalCount%getPageSize();
  }

  public boolean hasNextPage() {
    return (start +getPageSize()) <getTotalCount();
}

 public boolean hasPreviousPage() {
    return start > 0;
  }

    public int getCurrentPage()
    {
      if(getTotalCount()==0) return 0;
      else
        return start/getPageSize()+1;
    }

    public int getPages()
    {
      if((getTotalCount()%getPageSize())==0)
        return  getTotalCount()/getPageSize();
      else
        return  getTotalCount()/getPageSize()+1;
    }

    public int getPageSize()
    {
      return  pageSize;
    }

    public int getTotalCount(){
     return this.totalCount;
  }
}

