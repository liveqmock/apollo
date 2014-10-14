package cn.com.youtong.apollo.common;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class UploadServletRequest extends HttpServletRequestWrapper {

        public UploadServletRequest(HttpServletRequest req)
                throws IOException {

                super(req);

                _params = Http.getUploads(req);
        }

        public DataSource getDataSource(String name) {
                return (DataSource)_params.get(name);
        }

        public String getParameter(String name) {
                Object obj = _params.get(name);

                if (obj instanceof DataSource) {
                        return null;
                }
                else {
                        return (String)obj;
                }
        }

        public Map getParameterMap() {
                Map map = new HashMap();

                Enumeration e = getParameterNames();

                while (e.hasMoreElements()) {
                        String s = (String)e.nextElement();
                        map.put(s, _params.get(s));
                }

                return map;
        }

        public Enumeration getParameterNames() {
                List names = new ArrayList();

                Iterator i = _params.keySet().iterator();

                while (i.hasNext()) {
                        names.add((String)i.next());
                }

                return Collections.enumeration(names);
        }

        public String[] getParameterValues(String name) {
                String value = getParameter(name);

                if (value == null) {
                        return new String[0];
                }
                else {
                        return new String[] {value};
                }
        }

        private Map _params = null;

}
