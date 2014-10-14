package cn.com.youtong.apollo.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.List;


public class StringUtil {

     public static String excapNULL(String s) {
         if(s==null)
           return "";
        else
          return s;
      }


        public static String add(String s, String add) {
                return add(s, add, StringPool.COMMA);
        }

        public static String add(String s, String add, String delimiter) {
                return add(s, add, delimiter, false);
        }

        public static String add(
                String s, String add, String delimiter, boolean allowDuplicates) {

                if ((add == null) || (delimiter == null)) {
                        return null;
                }

                if (s == null) {
                        s = StringPool.BLANK;
                }

                if (allowDuplicates || !contains(s, add, delimiter)) {
                        if (Validator.isNull(s) || s.endsWith(delimiter)) {
                                s += add + delimiter;
                        }
                        else {
                                s += delimiter + add + delimiter;
                        }
                }

                return s;
        }

        public static boolean contains(String s, String text) {
                return contains(s, text, StringPool.COMMA);
        }

        public static boolean contains(String s, String text, String delimiter) {
                if ((s == null) || (text == null) || (delimiter == null)) {
                        return false;
                }

                if (!s.endsWith(delimiter)) {
                        s += delimiter;
                }

                int pos = s.indexOf(delimiter + text + delimiter);

                if (pos == -1) {
                        if (s.startsWith(text + delimiter)) {
                                return true;
                        }

                        return false;
                }

                return true;
        }

        public static int count(String s, String text) {
                if ((s == null) || (text == null)) {
                        return 0;
                }

                int count = 0;

                int pos = s.indexOf(text);

                while (pos != -1) {
                        pos = s.indexOf(text, pos + text.length());
                        count++;
                }

                return count;
        }

        public static String extractChars(String s) {
                if (s == null) {
                        return "";
                }

                char[] c = s.toCharArray();

                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < c.length; i++) {
                        if (Validator.isChar(c[i])) {
                                sb.append(c[i]);
                        }
                }

                return sb.toString();
        }

        public static String extractDigits(String s) {
                if (s == null) {
                        return "";
                }

                char[] c = s.toCharArray();

                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < c.length; i++) {
                        if (Validator.isDigit(c[i])) {
                                sb.append(c[i]);
                        }
                }

                return sb.toString();
        }

        public static String merge(String array[]) {
                return merge(array, StringPool.COMMA);
        }

        public static String merge(String array[], String delimiter) {
                if (array == null) {
                        return null;
                }

                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < array.length; i++) {
                        sb.append(array[i].trim());

                        if ((i + 1) != array.length) {
                                sb.append(delimiter);
                        }
                }

                return sb.toString();
        }


        public static String read(ClassLoader classLoader, String name)
                throws IOException {

                return read(classLoader.getResourceAsStream(name));
        }

        public static String read(InputStream is) throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                StringBuffer sb = new StringBuffer();
                String line = null;

                while ((line = br.readLine()) != null) {
                        sb.append(line).append('\n');
                }

                br.close();

                return sb.toString().trim();
        }

        public static String remove(String s, String remove) {
                return remove(s, remove, StringPool.COMMA);
        }

        public static String remove(String s, String remove, String delimiter) {
                if ((s == null) || (remove == null) || (delimiter == null)) {
                        return null;
                }

                if (Validator.isNotNull(s) && !s.endsWith(delimiter)) {
                        s += delimiter;
                }

                while (contains(s, remove, delimiter)) {
                        int pos = s.indexOf(delimiter + remove + delimiter);

                        if (pos == -1) {
                                if (s.startsWith(remove + delimiter)) {
                                        s = s.substring(
                                                        remove.length() + delimiter.length(), s.length());
                                }
                        }
                        else {
                                s = s.substring(0, pos) + s.substring(pos + remove.length() +
                                        delimiter.length(), s.length());
                        }
                }

                return s;
        }

        public static String replace(String s, char oldSub, char newSub) {
                return replace(s, oldSub, new Character(newSub).toString());
        }

        public static String replace(String s, char oldSub, String newSub) {
                if ((s == null) || (newSub == null)) {
                        return null;
                }

                char[] c = s.toCharArray();

                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < c.length; i++) {
                        if (c[i] == oldSub) {
                                sb.append(newSub);
                        }
                        else {
                                sb.append(c[i]);
                        }
                }

                return sb.toString();
        }

        public static String replace(String s, String oldSub, String newSub) {
                if ((s == null) || (oldSub == null) || (newSub == null)) {
                        return null;
                }

                int y = s.indexOf(oldSub);

                if (y >= 0) {
                        StringBuffer sb = new StringBuffer();
                        int length = oldSub.length();
                        int x = 0;

                        while (x <= y) {
                                sb.append(s.substring(x, y));
                                sb.append(newSub);
                                x = y + length;
                                y = s.indexOf(oldSub, x);
                        }

                        sb.append(s.substring(x));

                        return sb.toString();
                }
                else {
                        return s;
                }
        }

        public static String replace(String s, String[] oldSubs, String[] newSubs) {
                if ((s == null) || (oldSubs == null) || (newSubs == null)) {
                        return null;
                }

                if (oldSubs.length != newSubs.length) {
                        return s;
                }

                for (int i = 0; i < oldSubs.length; i++) {
                        s = replace(s, oldSubs[i], newSubs[i]);
                }

                return s;
        }

        public static String reverse(String s) {
                if (s == null) {
                        return null;
                }

                char[] c = s.toCharArray();
                char[] reverse = new char[c.length];

                for (int i = 0; i < c.length; i++) {
                        reverse[i] = c[c.length - i - 1];
                }

                return new String(reverse);
        }

        public static String shorten(String s) {
                return shorten(s, 20);
        }

        public static String shorten(String s, int length) {
                return shorten(s, length, "..");
        }

        public static String shorten(String s, String suffix) {
                return shorten(s, 20, suffix);
        }

        public static String shorten(String s, int length, String suffix) {
                if (s == null || suffix == null)  {
                        return null;
                }

                if (s.length() > length) {
                        s = s.substring(0, length) + suffix;
                }

                return s;
        }

        public static String[] split(String s) {
                return split(s, StringPool.COMMA);
        }

        public static String[] split(String s, String delimiter) {
                if (s == null || delimiter == null) {
                        return new String[0];
                }

                s = s.trim();

                if (!s.endsWith(delimiter)) {
                        s += delimiter;
                }

                if (s.equals(delimiter)) {
                        return new String[0];
                }

                List nodeValues = new ArrayList();

                if (delimiter.equals("\n") || delimiter.equals("\r")) {
                        try {
                                BufferedReader br = new BufferedReader(new StringReader(s));

                                String line = null;

                                while ((line = br.readLine()) != null) {
                                        nodeValues.add(line);
                                }

                                br.close();
                        }
                        catch (IOException ioe) {
                                ioe.printStackTrace();
                        }
                }
                else {
                        int offset = 0;
                        int pos = s.indexOf(delimiter, offset);

                        while (pos != -1) {
                                nodeValues.add(s.substring(offset, pos));

                                offset = pos + delimiter.length();
                                pos = s.indexOf(delimiter, offset);
                        }
                }

                return (String[])nodeValues.toArray(new String[0]);
        }

        public static boolean[] split(String s, String delimiter, boolean x) {
                String[] array = split(s, delimiter);
                boolean[] newArray = new boolean[array.length];

                for (int i = 0; i < array.length; i++) {
                        boolean value = x;

                        try {
                                value = Boolean.valueOf(array[i]).booleanValue();
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

        public static double[] split(String s, String delimiter, double x) {
                String[] array = split(s, delimiter);
                double[] newArray = new double[array.length];

                for (int i = 0; i < array.length; i++) {
                        double value = x;

                        try {
                                value = Double.parseDouble(array[i]);
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

        public static float[] split(String s, String delimiter, float x) {
                String[] array = split(s, delimiter);
                float[] newArray = new float[array.length];

                for (int i = 0; i < array.length; i++) {
                        float value = x;

                        try {
                                value = Float.parseFloat(array[i]);
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

        public static int[] split(String s, String delimiter, int x) {
                String[] array = split(s, delimiter);
                int[] newArray = new int[array.length];

                for (int i = 0; i < array.length; i++) {
                        int value = x;

                        try {
                                value = Integer.parseInt(array[i]);
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

        public static long[] split(String s, String delimiter, long x) {
                String[] array = split(s, delimiter);
                long[] newArray = new long[array.length];

                for (int i = 0; i < array.length; i++) {
                        long value = x;

                        try {
                                value = Long.parseLong(array[i]);
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

        public static short[] split(String s, String delimiter, short x) {
                String[] array = split(s, delimiter);
                short[] newArray = new short[array.length];

                for (int i = 0; i < array.length; i++) {
                        short value = x;

                        try {
                                value = Short.parseShort(array[i]);
                        }
                        catch (Exception e) {
                        }

                        newArray[i] = value;
                }

                return newArray;
        }

    public static final String stackTrace(Throwable t) {
                String s = null;

                try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        t.printStackTrace(new PrintWriter(baos, true));
                        s = baos.toString();
                }
                catch (Exception e) {
                }

                return s;
    }

        public static boolean startsWith(String s, char begin) {
                return startsWith(s, (new Character(begin)).toString());
        }

        public static boolean startsWith(String s, String begin) {
                if ((s == null) || (begin == null)) {
                        return false;
                }

                if (begin.length() > s.length()) {
                        return false;
                }

                String temp = s.substring(0, begin.length());

                if (temp.equalsIgnoreCase(begin)) {
                        return true;
                }
                else {
                        return false;
                }
        }

        public static String wrap(String text) {
                return wrap(text, 80);
        }

        public static String wrap(String text, int width) {
                if (text == null) {
                        return null;
                }

                StringBuffer sb = new StringBuffer();

                try {
                        BufferedReader br = new BufferedReader(new StringReader(text));

                        String s = "";

                        while ((s = br.readLine()) != null) {
                                if (s.length() == 0) {
                                        sb.append("\n");
                                }
                                else {
                                        while (true) {
                                                int pos = s.lastIndexOf(' ', width);

                                                if ((pos == -1) && (s.length() > width)) {
                                                        sb.append(s.substring(0, width));
                                                        sb.append("\n");

                                                        s = s.substring(width, s.length()).trim();
                                                }
                                                else if ((pos != -1) && (s.length() > width)) {
                                                        sb.append(s.substring(0, pos));
                                                        sb.append("\n");

                                                        s = s.substring(pos, s.length()).trim();
                                                }
                                                else {
                                                        sb.append(s);
                                                        sb.append("\n");

                                                        break;
                                                }
                                        }
                                }
                        }
                }
                catch (IOException ioe) {
                        ioe.printStackTrace();
                }

                return sb.toString();
        }

}
