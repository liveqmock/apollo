package cn.com.youtong.tools.zip;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.apache.commons.logging.*;
/**
 * ѹ���ͽ�ѹ��ZIP��ʽ�ļ��Ĺ��ߡ�
 */
public class Zip
{
	private static Log log= LogFactory.getLog(Zip.class);

	public Zip()
	{
	}

	/**Ϊtrueʱ���ӡ������Ϣ*/
	public static boolean debug = false;

	/**
	 * ѹ����
	 * @param src Դ�ļ�����Ŀ¼
	 * @param dest ѹ���ļ�·��
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
	 * �ݹ�ѹ���ļ���Ŀ¼
	 * @param out ѹ�����������
	 * @param fileOrDirectory Ҫѹ�����ļ���Ŀ¼����
	 * @param curPath ��ǰѹ����Ŀ��·��������ָ����Ŀ���Ƶ�ǰ׺
	 * @throws IOException
	 */
	private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
		throws IOException
	{
		if(!fileOrDirectory.isDirectory())
		{
			//ѹ���ļ�
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
			//ѹ��Ŀ¼
			File[] entries = fileOrDirectory.listFiles();
			for(int i = 0; i < entries.length; i++)
			{
				//�ݹ�ѹ��������curPaths
				zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");
			}
		}
	}

	/**
	 * ��ѹ����
	 * @param src Դ�ļ�
	 * @param destDir ��ѹ�����ļ���ŵ�Ŀ¼
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
				throw new IOException("��Դ���ڱ�ռ�ã�" + src + ex.getMessage());
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