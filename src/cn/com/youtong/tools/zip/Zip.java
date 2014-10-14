package cn.com.youtong.tools.zip;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.apache.commons.logging.*;
/**
 * 压缩和解压缩ZIP格式文件的工具。
 */
public class Zip
{
	private static Log log= LogFactory.getLog(Zip.class);

	public Zip()
	{
	}

	/**为true时则打印调试信息*/
	public static boolean debug = false;

	/**
	 * 压缩。
	 * @param src 源文件或者目录
	 * @param dest 压缩文件路径
	 * @throws IOException
	 */
	public static void zip(String src, String dest)
		throws IOException
	{
		FileOutputStream fo = new FileOutputStream(dest);
		ZipOutputStream out = new ZipOutputStream(fo);
		File fileOrDirctory = new File(src);
		zipFileOrDirectory(out, fileOrDirctory, "");
		out.close();
	}

	/**
	 * 递归压缩文件或目录
	 * @param out 压缩输出流对象
	 * @param fileOrDirectory 要压缩的文件或目录对象
	 * @param curPath 当前压缩条目的路径，用于指定条目名称的前缀
	 * @throws IOException
	 */
	private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
		throws IOException
	{
		if(!fileOrDirectory.isDirectory())
		{
			//压缩文件
			byte[] buffer = new byte[4096];
			int bytes_read;
			FileInputStream in = new FileInputStream(fileOrDirectory);
			ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
			out.putNextEntry(entry);
			if(debug)
			{
				System.out.println(entry.getName());
			}
			while((bytes_read = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytes_read);
			}
			in.close();
			return;
		}
		else
		{
			//压缩目录
			File[] entries = fileOrDirectory.listFiles();
			for(int i = 0; i < entries.length; i++)
			{
				//递归压缩，更新curPaths
				zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");
			}
		}
	}

	/**
	 * 解压缩。
	 * @param src 源文件
	 * @param destDir 解压缩后文件存放的目录
	 * @throws IOException
	 */
	public static void unzip(String src, String destDir)
		throws IOException
	{
		log.debug("Try to unzip file: " + src +" to " + destDir);
		ZipInputStream zis= null;
		try
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(new File(src));
			}
			catch(FileNotFoundException ex)
			{
				throw new IOException("资源正在被占用：" + src + ex.getMessage());
			}
			zis = new ZipInputStream(fis);

			if(destDir.endsWith("\\") || destDir.endsWith("//"))
				destDir = destDir.substring(0, destDir.length()-1);
			File dest = new File(destDir);
			dest.mkdirs();

			for(ZipEntry entry= zis.getNextEntry(); entry != null; entry = zis.getNextEntry())
			{
				log.debug("Extracting..." + entry.getName());
				File file= new File(destDir + File.separator +entry.getName());
				if(entry.isDirectory())
				{
					file.mkdirs() ;
				}
				else
				{
					BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(file));
					byte[] buffer = new byte[4096];
					int bytes_read;
					while((bytes_read = zis.read(buffer)) > -1)
					{
						bo.write(buffer, 0, bytes_read);
					}
					bo.close();
				}
			}
		}
		finally
		{
			try
			{
				if(zis != null)
					zis.close();
			}
			catch(Exception e)
			{
			}
		}
	}

	public static void main(String[] args)
	{
		try
		{
			Zip z = new Zip();
			z.unzip("c:\\db_200312.zip", "c:\\temp");
		}
		catch(Exception e)
		{
			e.printStackTrace() ;
		}
	}
}