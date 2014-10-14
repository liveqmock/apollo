package cn.com.youtong.apollo.common;

import java.util.*;
import java.io.*;

/**
 * 加上一些文件的同步处理
 */
public class FileManager {
  public static final FileManager getInstance(String filename)
  {
    FileManager instance = (FileManager)fileMap.get(filename);
    if(instance==null) {instance = new FileManager(filename);fileMap.put(filename,instance);}
    return instance;
  }
  public byte[] Load()
  {
    synchronized(this.filename)
    {
      FileInputStream in = null;
      try
      {
        in = new FileInputStream(this.filename);
        byte[] retdata = new byte[in.available()];
        in.read(retdata);
        return retdata;
      }
      catch(java.io.IOException e)
      {
        return null;
      }
      finally
      {
        if(in!=null)
        {
          try
          {
            in.close();
          }
          catch(java.io.IOException ex)
          {}
        }
      }
    }
  }
  public void Save(byte[] data) throws java.io.IOException
  {
    synchronized(this.filename)
    {
      FileOutputStream out = null;
      try
      {
        out = new FileOutputStream(this.filename);
        out.write(data);
      }
      finally
      {
        if(out!=null)
        {
          try
          {
            out.close();
          }
          catch(java.io.IOException ex)
          {}
        }
      }
    }
  }
  public void close()
  {
    synchronized(this.filename)
    {
      fileMap.remove(this.filename);
    }
  }
  private FileManager(String filename)
  {
    this.filename = filename;
  }
  private String filename;
  private static Map fileMap = new HashMap();
}