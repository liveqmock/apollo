package cn.com.youtong.apollo.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.activation.DataSource;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;



public class Http {

        static String proxyHost;
        static int proxyPort;

        static {
                proxyHost = GetterUtil.getString(
                        System.getProperty(Http.class.getName() + ".proxy.host"));
                proxyPort = GetterUtil.getInteger(
                        System.getProperty(Http.class.getName() + ".proxy.port"));
        }

        public static String getCompleteURL(HttpServletRequest req) {
                if (req.getQueryString() == null) {
                        return getRequestURL(req);
                }

                return (HttpUtils.getRequestURL(req)
                                        .append("?")
                                        .append(req.getQueryString())).toString();
        }

        public static String getRequestURL(HttpServletRequest req) {
                return HttpUtils.getRequestURL(req).toString();
        }

        public static Map getUploads(HttpServletRequest req) throws IOException {
                Map map = new HashMap();

                String boundary = req.getHeader("Content-Type");

                if (boundary == null) {
                        return map;
                }

                int pos = boundary.indexOf('=');
                boundary = boundary.substring(pos + 1);
                boundary = "--" + boundary;

                ServletInputStream in = req.getInputStream();

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] bytes = new byte[512];
                int state = 0;

                String name = null;
                String value = null;
                String fileName = null;
                String contentType = null;

                int i = in.readLine(bytes, 0, 512);

                while (i != -1) {
                        String st = new String(bytes, 0, i);

                        if (st.startsWith(boundary)) {
                                state = 0;

                                if (name != null) {
                                        if (value != null) {
                                                map.put(name, value.substring(0, value.length() - 2));
                                        }
                                        else if (buffer.size() > 2) {
                                                DataSource ds = new ByteArrayDataSource(
                                                        buffer.toByteArray(), contentType, fileName);
                                                map.put(name, ds);
                                        }

                                        name = null;
                                        value = null;
                                        fileName = null;
                                        contentType = null;
                                        buffer = new ByteArrayOutputStream();
                                }
                        }
                        else if ((st.startsWith("Content-Disposition: form-data")) &&
                                         (state == 0)) {

                                StringTokenizer tokenizer = new StringTokenizer(st,";=\"");

                                while (tokenizer.hasMoreTokens()) {
                                        String token = tokenizer.nextToken();

                                        if (token.startsWith(" name")) {
                                                name = tokenizer.nextToken();
                                                state = 2;
                                        }
                                        else if (token.startsWith(" filename")) {
                                                fileName = tokenizer.nextToken();

                                                StringTokenizer ftokenizer =
                                                        new StringTokenizer(fileName,"\\/:");

                                                fileName = ftokenizer.nextToken();

                                                while (ftokenizer.hasMoreTokens()) {
                                                        fileName = ftokenizer.nextToken();
                                                }

                                                state = 1;
                                                break;
                                        }
                                }
                        }
                        else if ((st.startsWith("Content-Type")) && (state == 1)) {
                                pos = st.indexOf(":");
                                contentType = st.substring(pos + 2,st.length() - 2);
                        }
                        else if ((st.equals("\r\n")) && (state == 1)) {
                                state = 3;
                        }
                        else if ((st.equals("\r\n")) && (state == 2)) {
                                state = 4;
                        }
                        else if (state == 4) {
                                value = value == null ? st : value + st;
                        }
                        else if (state == 3) {
                                buffer.write(bytes, 0, i);
                        }

                        i = in.readLine(bytes, 0, 512);
                }

                return map;
        }

        public static void submit(String location) throws IOException {
                submit(location, null);
        }

        public static void submit(String location, Cookie[] cookies)
                throws IOException {

                submit(location, cookies, false);
        }

        public static void submit(String location, boolean post)
                throws IOException {

                submit(location, null, post);
        }

        public static void submit(
                        String location, Cookie[] cookies, boolean post)
                throws IOException {

                URLtoByteArray(location, cookies, post);
        }

        public static byte[] URLtoByteArray(String location)
                throws IOException {

                return URLtoByteArray(location, null);
        }

        public static byte[] URLtoByteArray(String location, Cookie[] cookies)
                throws IOException {

                return URLtoByteArray(location, cookies, false);
        }

        public static byte[] URLtoByteArray(String location, boolean post)
                throws IOException {

                return URLtoByteArray(location, null, post);
        }

        public static byte[] URLtoByteArray(
                        String location, Cookie[] cookies, boolean post)
                throws IOException {

                byte[] byteArray = null;

                HttpMethod method = null;

                try {
                        HttpClient client =
                                new HttpClient(new SimpleHttpConnectionManager());

                        if (location == null) {
                                return byteArray;
                        }
                        else if (!location.startsWith("http://") &&
                                         !location.startsWith("https://")) {

                                location = "http://" + location;
                        }

                        HostConfiguration hostConfig = new HostConfiguration();

                        hostConfig.setHost(new URI(location));

                        if (Validator.isNotNull(proxyHost) && proxyPort > 0) {
                                hostConfig.setProxy(proxyHost, proxyPort);
                        }

                        client.setHostConfiguration(hostConfig);
                        client.setConnectionTimeout(5000);
                        client.setTimeout(5000);

                        if (cookies != null && cookies.length > 0) {
                                HttpState state = new HttpState();

                                state.addCookies(cookies);
                                state.setCookiePolicy(CookiePolicy.COMPATIBILITY);

                                client.setState(state);
                        }

                        if (post) {
                                method = new PostMethod(location);
                        }
                        else {
                                method = new GetMethod(location);
                        }

                        method.setFollowRedirects(true);

                        client.executeMethod(method);

                        Header locationHeader = method.getResponseHeader("location");
                        if (locationHeader != null) {
                                return URLtoByteArray(locationHeader.getValue(), cookies, post);
                        }

                        InputStream is = method.getResponseBodyAsStream();

                        if (is != null) {
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                byte[] bytes = new byte[512];

                                for (int i = is.read(bytes, 0, 512); i != -1;
                                                i = is.read(bytes, 0, 512)) {

                                        buffer.write(bytes, 0, i);
                                }

                                byteArray = buffer.toByteArray();

                                is.close();
                                buffer.close();
                        }

                        return byteArray;
                }
                finally {
                        try {
                                if (method != null) {
                                        method.releaseConnection();
                                }
                        }
                        catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }

        public static String URLtoString(String location)
                throws IOException {

                return URLtoString(location, null);
        }

        public static String URLtoString(String location, Cookie[] cookies)
                throws IOException {

                return URLtoString(location, cookies, false);
        }

        public static String URLtoString(String location, boolean post)
                throws IOException {

                return URLtoString(location, null, post);
        }

        public static String URLtoString(String location, Cookie[] cookies,
                                                                         boolean post)
                throws IOException {

                return new String(URLtoByteArray(location, cookies, post));
        }

}
