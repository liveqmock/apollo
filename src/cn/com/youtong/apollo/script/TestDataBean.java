package cn.com.youtong.apollo.script;

import java.util.*;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.CodeBuffer;
import org.mozilla.javascript.*;
import java.io.*;

public class TestDataBean
	implements ScriptObject
{
	public String name;
	public String tel;
	public int age;
	public boolean isOk;
	public Double dValue1 = new Double(3.3);
	public Double dValue2 = new Double(2.2);
	public Double dValue3;
	public Double[][] array = new Double[2][2];

	public Object list = new String("sss");

	private int revenue;

	Map hashMap = new HashMap();

	private String id;
	public TestDataBean(String id)
	{
		hashMap.put("a1", new Integer(10));
		hashMap.put("b1", new Double(100));
		hashMap.put("c1", new Double(100));
		hashMap.put("d1", new Double(100));

		this.id = id;
	}

	public void setRevenue(int rev)
	{
		revenue = rev;
	}

	public void dump()
	{
		System.out.println("name = " + name);
		System.out.println("tel = " + tel);
		System.out.println("age = " + age);
		System.out.println("revenue = " + revenue);
		System.out.println("isOk = " + isOk);
		System.out.println("address = " + address);
		System.out.println("count = " + count);

		System.out.println("Fields:");
		for (Iterator ite = hashMap.entrySet().iterator(); ite.hasNext(); )
		{
			Map.Entry e = (Map.Entry) ite.next();
			System.out.println(e.getKey() + " = " + e.getValue());
		}
	}

	private String address = "China Beijing";
	public String getAddress()
	{
		return address;
	}

	public boolean isField(String name)
	{
		return true;
	}

	public void setAddress(String addr)
	{
		address = addr;
	}

	private int count;
	public int getCount()
	{
		return count;
	}

	public void setCount(int v)
	{
		count = v;
	}

	public Iterator s_getFields()
	{
		return hashMap.keySet().iterator();
	}

	public Object s_getField(String key)
	{
		return hashMap.get(key);
	}

	public void s_setField(String key, Object value)
	{
		hashMap.put(key, value);
	}

	public String toString()
	{
		return id;
	}

	static void test1()
	{
		String script =
//			"zcfz.name = \"YouTong\";\n"
//			+ "zcfz.age= 1;\n"
//			+ "zcfz.tel= \"6233xxxx\";\n"
//			+ "var revenue= 100*1000+1;\n"
//			+ "zcfz.setRevenue(revenue);\n"
//			+ "zcfz.isOk= audit(revenue);\n"
//			+ "function audit(rev)\n"
//			+ "{\n"
//			+ "	return rev < 10000000;\n"
//			+ "}\n"
//			+ "zcfz.address= \"Beijing.China\";\n"
//			+ "zcfz.count= 5000;\n"
//			+ "zcfz.a1=1000+20;\n"
//			+ "zcfz.B1=zcfz.c1+zcfz.d1\n"
//			+ "ZCFZ.c1=1234+zcfz.d1;\n"
			"with(zcfz)\n"
			+ "{\n"
			+ "    a1 += 200;\n"
//			+ "    count = 22;\n"
			+ "}";
		BSFManager bsf = new BSFManager();
		TestDataBean table = new TestDataBean("zcfz");
		try
		{
			System.out.println("================Script================");
			System.out.println(script.replaceAll("\\r\\n", "\n"));
			System.out.println("================Before================");
			table.dump();
			bsf.declareBean("zcfz", table, TestDataBean.class);
			bsf.exec("javascript", "Test", 0, 0, script);
			System.out.println("================After=================");
			table.dump();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static void test6()
	{
		String script =
//			"zcfz.array[0][0] = zcfz.array[0][1] + zcfz.array[1][0] + zcfz.array[1][1] ;";
			"    audit.eq(100, 10, '100=100'); audit.eq(100, 10, '0 = 100', 'sdf', 'fff');\n";
//			+ "    eq(100, 100.0, '100= 100.0');\n"
//			+ "    le(zcfz.a1, zcfz.b1, '错误 zcfz.a1(&1) 小于< zcfz.b1(&2)');\n"
//			+ "    wle(zcfz.count, 1000, '警告 zcfz.count(&1) 应该小于 &2');";

		TestDataBean zcfz = new TestDataBean("zcfz");
		AuditResult audit = new AuditResult();
		try
		{
			System.out.println("================Script================");
			System.out.println(script.replaceAll("\\r\\n", "\n"));
			System.out.println("================Before================");
			BSFManager bsf = new BSFManager();
			bsf.declareBean("audit", audit, AuditResult.class);
			bsf.declareBean("zcfz", zcfz, TestDataBean.class);

			bsf.exec("javascript", "Test", 0, 0, script);
			System.out.println("================After=================");
			System.out.println(audit.getErrors().toString());
			System.out.println(audit.getWarnings().toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static void test2()
	{
		String script =
			"zcfz.name = \"YouTong\";\n"
			+ "zcfz.age= 1;\n"
			+ "zcfz.tel= \"6233xxxx\";\n"
			+ "var revenue= 100*1000+1;\n"
			+ "zcfz.setRevenue(revenue);\n"
			+ "zcfz.isOk= ftest(revenue);\n"
			+ "function ftest(rev)\n"
			+ "{\n"
			+ "	return rev < 10000000;\n"
			+ "}\n"
			+ "zcfz.address= \"Beijing.China\";\n"
			+ "zcfz.count= 5000;\n"
			+ "zcfz.a1=1000+20;\n"
			+ "zcfz.B1=zcfz.c1+zcfz.d1\n"
			+ "ZCFZ.c1=1234+zcfz.d1;\n"
			+ "with(zcfz)\n"
			+ "{\n"
			+ "    a1 += 100;\n"
			+ "    count += 22;\n"
			+ "};\n"
			+ "with(audit)\n"
			+ "{\n"
			+ "    eq(100, 100, '100 = 100');\n"
			+ "    eq(100, 100.0, '100= 100.0');\n"
			+ "    le(zcfz.a1, zcfz.b1, '错误 zcfz.a1(&1) 小于< zcfz.b1(&2)');\n"
			+ "    wle(zcfz.count, 1000, '警告 zcfz.count(&1) 应该小于 &2');\n"
			+ "}";
		TestDataBean table = new TestDataBean("zcfz");
		AuditResult audit = new AuditResult();
		try
		{
			System.out.println("================Script================");
			System.out.println(script.replaceAll("\\r\\n", "\n"));
			System.out.println("================Before================");
			BSFManager bsf = new BSFManager();
			bsf.declareBean("audit", audit, AuditResult.class);
			bsf.declareBean("zcfz", table, TestDataBean.class);

			bsf.exec("javascript", "Test", 0, 0, script);
			System.out.println("================After=================");
			table.dump();
			System.out.println(audit.getErrors().toString());
			System.out.println(audit.getWarnings().toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static void test3()
	{
		String script =
			"zcfz.name = \"YouTong\";\n"
			+ "zcfz.age= 1;\n"
			+ "zcfz.tel= \"6233xxxx\";\n"
			+ "var revenue= 100*1000+1;\n"
			+ "zcfz.setRevenue(revenue);\n"
			+ "zcfz.isOk= ftest(revenue);\n"
			+ "function ftest(rev)\n"
			+ "{\n"
			+ "	return rev < 10000000;\n"
			+ "}\n"
			+ "zcfz.address= \"Beijing.China\";\n"
			+ "zcfz.count= 5000;\n"
			+ "zcfz.a1=1000+20;\n"
			+ "zcfz.B1=zcfz.c1+zcfz.d1\n"
			+ "ZCFZ.c1=1234+zcfz.d1;\n"
			+ "with(zcfz)\n"
			+ "{\n"
			+ "    a1 += 100;\n"
			+ "    count += 22;\n"
			+ "};\n"
			+ "with(audit)\n"
			+ "{\n"
			+ "    eq(100, 100, '100 = 100');\n"
			+ "    eq(100, 100.0, '100= 100.0');\n"
			+ "    le(zcfz.a1, zcfz.b1, '错误 zcfz.a1(&1) 小于< zcfz.b1(&2)');\n"
			+ "    wle(zcfz.count, 1000, '警告 zcfz.count(&1) 应该小于 &2');\n"
			+ "}";
		try
		{
			BSFManager bsf = new BSFManager();
			CodeBuffer cb = new CodeBuffer();
			bsf.compileScript("javascript", "Test", 0, 0, script, cb);
			System.out.println(cb.toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static void test4()
	{
		TestDataBean zcfz = new TestDataBean("zcfz");
		String script =
			" if(zcfz.count > 1000) \n"
			+ "    zcfz.count = zcfz.count + (10 + 20 * 3)\n"
			+ "else\n"
			+ "    zcfz.count = zcfz.count - (10 + 20 * 3)\n"
			+ "zcfz.count + 999";
		String script2 =
			" if(zcfz.count > 1000) \n"
			+ "    zcfz.count = zcfz.count + (10 + 20 * 3)\n"
			+ "else\n"
			+ "    zcfz.count = zcfz.count - (10 + 20 * 3)\n"
			+ "zcfz.count + 9999";

		try
		{
			BSFManager bsf = new BSFManager();
			zcfz.count = 1000;
			bsf.declareBean("zcfz", zcfz, TestDataBean.class);
			Object obj1 = bsf.eval("javascript", "Test", 0, 0, script);
			Object obj2 = bsf.eval("javascript", "Test", 0, 0, script2);
			System.out.println("obj = " + obj1);
			System.out.println("obj = " + obj2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	static void test5()
	{
		String scriptTetx =
			"zcfz.dValue3 = zcfz.dValue1 + zcfz.dValue2;"; // + zcfz.array[0][1] + zcfz.array[1][0];\n";

		TestDataBean zcfz = new TestDataBean("zcfz");

		try
		{
			Context cx = Context.enter();

			Scriptable scope = cx.initStandardObjects(null);

			cx.getWrapFactory().setJavaPrimitiveWrap(false);

			scope.put("zcfz", scope, zcfz);

			cx.evaluateString(scope, scriptTetx, "脚本", 0, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Context.exit();
		}
	}

	public static void main(String[] args)
	{
		test6();
	}
}