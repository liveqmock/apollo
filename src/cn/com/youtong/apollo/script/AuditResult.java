package cn.com.youtong.apollo.script;

import cn.com.youtong.apollo.task.*;
import java.util.*;

/**
 * ��˽����
 * ��Ϣ��ʾ�п���Ƕ���ַ�����&1 - ��һ����������&2 - �ڶ�����������...
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

		//�����ַ������Ȳ���
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
				//����Ƿ��ַ�
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
	 * ��¼������Ϣ��StringBuffer
	 */
	private List errors = new LinkedList();

	/**
	 * ��¼������Ϣ��StringBuffer
	 */
	private List warnings = new LinkedList();

	/**
	 * �������Ƚ���ȵĲ��
	 */
	private double deltaEq = 0.0001;

	/**
	 * �������Ƚ���ȵĲ��
	 */
	public void setDeltaEq(double delta)
	{
		this.deltaEq = delta;
	}

	/**
	 * �������Ƚ���ȵĲ��
	 */
	public double getDeltaEq()
	{
		return this.deltaEq;
	}

	/**
	 * ��¼������Ϣ
	 * @param message ������Ϣ
	 */
	public void error(String message)
	{
		errors.add(message);
	}

	/**
	 * error��д
	 * @see error
	 */
	public void e(String message)
	{
		error(message);
	}

	/**
	 * ��¼������Ϣ
	 * @param message ������Ϣ
	 */
	public void warning(String message)
	{
		warnings.add(message);
	}

	/**
	 * warning��д
	 * @see warning
	 */
	public void w(String message)
	{
		warning(message);
	}

	/**
	 * �õ�������Ϣ
	 * @return ������Ϣ
	 */
	public List getErrors()
	{
		return errors;
	}

	/**
	 * �õ�������Ϣ
	 * @return ������Ϣ
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