package cn.com.youtong.apollo.common;

import javax.servlet.http.*;
import java.util.Date;
import java.text.*;


public class ParamUtils {

    /**
     * 将得到的参数作为字符串
     * @param request
     * @param name 参数名称
     * @return 参数的值或null（参数没找到，或参数为空字符串）
     */
    public static String getParameter(HttpServletRequest request, String name) {
        return getParameter(request, name, false);
    }

    /**
     * 将得到的参数作为字符串
     * @param request
     * @param name 参数名称
     * @param name emptyStringsOK 是否为空时，当NULL处理
     * @return 参数的值或null（参数没找到，或参数为空字符串）
     */
    public static String getParameter(HttpServletRequest request,
            String name, boolean emptyStringsOK)
    {
        String temp = request.getParameter(name);

        if (temp != null) {
            if (temp.equals("") && !emptyStringsOK) {
                return null;
            }
            else {
                return temp;
            }
        }
        else {
            return null;
        }
    }

    /**
     * 将得到的参数作为布尔值
     * @param request
     * @param name 参数名称
     * @return 参数的值或false（参数没找到)
     */
    public static boolean getBooleanParameter(HttpServletRequest request,
            String name)
    {
        return getBooleanParameter(request, name, false);
    }

    /**
     * 将得到的参数作为字符串
     * @param request
     * @param name 参数名称
     * @param name defaultVal 缺省值
     * @return 参数的值或缺省值
     */
    public static boolean getBooleanParameter(HttpServletRequest request,
            String name, boolean defaultVal)
    {
        String temp = request.getParameter(name);

        if ("true".equals(temp) || "on".equals(temp)) {
            return true;
        }
        else if ("false".equals(temp) || "off".equals(temp)) {
            return false;
        }
        else {
            return defaultVal;
        }
    }

    /**
     * 将得到的参数作为int
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static int getIntParameter(HttpServletRequest request,
            String name, int defaultNum)
    {
        String temp = request.getParameter(name);
        if(temp != null && !temp.equals("")) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp);
            }
            catch (Exception ignored) {}
            return num;
        }
        else {
            return defaultNum;
        }
    }


    /**
     * 将得到的参数作为int数组
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static int[] getIntParameters(HttpServletRequest request,
            String name, int defaultNum)
    {
        String[] paramValues = request.getParameterValues(name);

        if (paramValues == null) {
            return null;
        }
        if (paramValues.length < 1) {
            return new int[0];
        }
        int[] values = new int[paramValues.length];
        for (int i=0; i<paramValues.length; i++) {
            try {
                values[i] = Integer.parseInt(paramValues[i]);
            }
            catch (Exception e) {
                values[i] = defaultNum;
            }
        }
        return values;
    }

    /**
     * 将得到的参数作为double
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static double getDoubleParameter(HttpServletRequest request,
            String name, double defaultNum)
    {
        String temp = request.getParameter(name);

        if(temp != null && !temp.equals("")) {
            double num = defaultNum;
            try {
                num = Double.parseDouble(temp);
            }
            catch (Exception ignored) {}
            return num;
        }
        else {
            return defaultNum;
        }
    }

    /**
     * 将得到的参数作为long
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static long getLongParameter(HttpServletRequest request,
            String name, long defaultNum)
    {
        String temp = request.getParameter(name);

        if (temp != null && !temp.equals("")) {
            long num = defaultNum;
            try {
                num = Long.parseLong(temp);
            }
            catch (Exception ignored) {}
            return num;
        }
        else {
            return defaultNum;
        }
    }

    /**
     * 将得到的参数作为long数组
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static long[] getLongParameters(HttpServletRequest request,
            String name, long defaultNum)
    {
        String[] paramValues = request.getParameterValues(name);
        if (paramValues == null) {
            return null;
        }
        if (paramValues.length < 1) {
            return new long[0];
        }
        long[] values = new long[paramValues.length];
        for (int i=0; i<paramValues.length; i++) {
            try {
                values[i] = Long.parseLong(paramValues[i]);
            }
            catch (Exception e) {
                values[i] = defaultNum;
            }
        }
        return values;
    }

    /**
     * 将得到的属性作为字符串
     * @param request
     * @param name 参数名称
     * @param defaultNum 缺省值
     * @return 参数的值或缺省值（参数没找到)
     */
    public static String getAttribute(HttpServletRequest request, String name) {
        return getAttribute (request, name, false);
    }

    /**
    * 将得到的属性作为字符串
    * @param request
    * @param name 参数名称
    * @param name emptyStringsOK 是否为空时，当NULL处理
    * @return 参数的值或null（参数没找到，或参数为空字符串）
     */
    public static String getAttribute(HttpServletRequest request,
            String name, boolean emptyStringsOK)
    {
        String temp = (String)request.getAttribute(name);
        if (temp != null) {
            if (temp.equals("") && !emptyStringsOK) {
                return null;
            }
            else {
                return temp;
            }
        }
        else {
            return null;
        }
    }

    /**
     * 将得到的属性作为字符串
     * @param request
     * @param name 参数名称
     * @param name defaultVal 缺省值
     * @return 参数的值或缺省值
     */
    public static boolean getBooleanAttribute(HttpServletRequest request,
            String name)
    {
        String temp = (String)request.getAttribute(name);
        if (temp != null && temp.equals("true")) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
      * 将得到的属性作为int
      * @param request
      * @param name 参数名称
      * @param defaultNum 缺省值
      * @return 参数的值或缺省值（参数没找到)
     */
    public static int getIntAttribute(HttpServletRequest request,
            String name, int defaultNum)
    {

        String temp = (String)request.getAttribute(name);
        if (temp != null && !temp.equals("")) {
            int num = defaultNum;
            try {
                num = Integer.parseInt(temp);
            }
            catch (Exception ignored) {}
            return num;
        }
        else {
            return defaultNum;
        }
    }

    /**
      * 将得到的参数作为long
      * @param request
      * @param name 参数名称
      * @param defaultNum 缺省值
      * @return 参数的值或缺省值（参数没找到)
     */
    public static long getLongAttribute(HttpServletRequest request,
            String name, long defaultNum)
    {
        String temp = (String)request.getAttribute(name);
        if (temp != null && !temp.equals("")) {
            long num = defaultNum;
            try {
                num = Long.parseLong(temp);
            }
            catch (Exception ignored) {}
            return num;
        }
        else {
            return defaultNum;
        }
     }

     public static Date getDateParameter(HttpServletRequest request,
            String name, Date defaultDate)
     {
       String temp = request.getParameter(name);
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

       if(temp != null && !temp.equals("")) {
         Date date = defaultDate;
         try {
           date =df.parse(temp);
         }
         catch (Exception ignored)
         {
              return defaultDate;
         }
         return date;
       }
       else {
         return defaultDate;
       }
     }

     public static Date getDateParameter(HttpServletRequest request,
           String name)
    {
      String temp = request.getParameter(name);
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

      if(temp != null && !temp.equals("")) {
        Date date = null;
        try {
          date =df.parse(temp);
        }
        catch (Exception ignored)
        {
             return null;
        }
        return date;
      }
      else {
        return null;
      }
     }

}
