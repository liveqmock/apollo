package cn.com.youtong.apollo.script;

import cn.com.youtong.apollo.task.*;
import java.util.*;

/**
 * 审核结果。
 * 信息提示中可以嵌入字符串：&1 - 第一个操作数，&2 - 第二个操作数，...
 */
public final class AuditResult
{
	public AuditResult()
	{
	}

	/**
	 * idc, copy from client C++
	 * if failed return ""
	 */
	public static final String idc(String csCode)
	{
		int w[] =
			{
			3, 7, 9, 10, 5, 8, 4, 2};

		//代码字符串长度不对
		if (csCode.length() != 8)
		{
			return "";
		}

		for (int i = 0; i < csCode.length(); i++)
		{
			char c = csCode.charAt(i);
			if ( (c <= 'z' && c >= 'a') || (c <= 'Z' || c >= 'A') ||
				(c <= '9' || c >= '0') || c == '#')
			{
			}
			else
			{
				//输入非法字符
				return "";
			}
		}

		int value = 0;
		for (int i = 0; i < csCode.length(); i++)
		{
			char c = csCode.charAt(i);
			if (c <= '9' || c >= '0')
			{
				value += (c - '0') * w[i];
			}
			else if ( (c <= 'z' && c >= 'a') || (c <= 'Z' || c >= 'A'))
			{
				value += (c - 'A' + 10) * w[i];
			}
			else
			{
				value = value + 36 * w[i];
			}
		}

		value = 11 - (value % 11);
		if (value > 0 && value <= 9)
		{
			return "" + value;
		}
		else if (11 == value)
		{
			return "0";
		}
		else
		{
			return "X";
		}
	}

	/**
	 * 记录错误信息的StringBuffer
	 */
	private List errors = new LinkedList();

	/**
	 * 记录警告信息的StringBuffer
	 */
	private List warnings = new LinkedList();

	/**
	 * 浮点数比较相等的差额
	 */
	private double deltaEq = 0.0001;

	/**
	 * 浮点数比较相等的差额
	 */
	public void setDeltaEq(double delta)
	{
		this.deltaEq = delta;
	}

	/**
	 * 浮点数比较相等的差额
	 */
	public double getDeltaEq()
	{
		return this.deltaEq;
	}

	/**
	 * 记录错误信息
	 * @param message 错误信息
	 */
	public void error(String message)
	{
		errors.add(message);
	}

	/**
	 * error缩写
	 * @see error
	 */
	public void e(String message)
	{
		error(message);
	}

	/**
	 * 记录警告信息
	 * @param message 警告信息
	 */
	public void warning(String message)
	{
		warnings.add(message);
	}

	/**
	 * warning缩写
	 * @see warning
	 */
	public void w(String message)
	{
		warning(message);
	}

	/**
	 * 得到错误信息
	 * @return 错误信息
	 */
	public List getErrors()
	{
		return errors;
	}

	/**
	 * 得到警告信息
	 * @return 警告信息
	 */
	public List getWarnings()
	{
		return warnings;
	}

	//================= error =======================================
	/**
	 * op1 == op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void eq(Object op1, Object op2, String message)
	{
		if (op1 == null)
		{
			if (op2 != null)
			{
				message = replace(message, op1, op2);
				error(message);
			}
		}
		else if (!op1.equals(op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 == op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void eq(double op1, double op2, String message)
	{
		if (Math.abs(op1 - op2) > deltaEq)
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 < op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void lt(double op1, double op2, String message)
	{
		if (! (op1 < op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 > op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void gt(double op1, double op2, String message)
	{
		if (! (op1 > op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 <= op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void le(double op1, double op2, String message)
	{
		if (! (op1 <= op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 >= op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void ge(double op1, double op2, String message)
	{
		if (! (op1 >= op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * op1 != op2
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void ne(double op1, double op2, String message)
	{
		if (! (op1 != op2))
		{
			message = replace(message, op1, op2);
			error(message);
		}
	}

	/**
	 * expr is true?
	 * @param op1
	 * @param op2
	 * @param message error message
	 */
	public void a(boolean expr, String message)
	{
		if (!expr)
		{
			message = replace(message, new Boolean(expr), null);
			error(message);
		}
	}

	//============================warning================================
	/**
	 * op1 == op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void weq(Object op1, Object op2, String message)
	{
		if (op1 == null)
		{
			if (op2 != null)
			{
				message = replace(message, op1, op2);
				warning(message);
			}
		}
		else if (!op1.equals(op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 == op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void weq(double op1, double op2, String message)
	{
		if (Math.abs(op1 - op2) > deltaEq)
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 < op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wlt(double op1, double op2, String message)
	{
		if (! (op1 < op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 > op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wgt(double op1, double op2, String message)
	{
		if (! (op1 > op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 <= op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wle(double op1, double op2, String message)
	{
		if (! (op1 <= op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 >= op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wge(double op1, double op2, String message)
	{
		if (! (op1 >= op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * op1 != op2
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wne(double op1, double op2, String message)
	{
		if (! (op1 != op2))
		{
			message = replace(message, op1, op2);
			warning(message);
		}
	}

	/**
	 * expr is true?
	 * @param op1
	 * @param op2
	 * @param message warning message
	 */
	public void wa(boolean expr, String message)
	{
		if (!expr)
		{
			message = replace(message, new Boolean(expr), null);
			warning(message);
		}
	}

	private String replace(String message, double op1, double op2)
	{
		message = message.replaceAll("&1", op1 + "");
		message = message.replaceAll("&2", op2 + "");
		return message;
	}

	private String replace(String message, Object op1, Object op2)
	{
		message = message.replaceAll("&1", op1 == null ? "" : op1.toString());
		message = message.replaceAll("&2", op2 == null ? "" : op2.toString());
		return message;
	}

	public static void main(String[] args)
	{
		String a = idc("11111111");
		String b = idc("22222222");
		String c = idc("23456789");
		System.out.println(a + " " + b + " " + c);
		System.out.println("7 3 4");
	}
}