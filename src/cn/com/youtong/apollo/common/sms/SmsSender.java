/*
 * Created on 2003-11-27
 */
package cn.com.youtong.apollo.common.sms;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.logging.*;
import cn.com.youtong.apollo.common.*;
import cn.com.youtong.apollo.common.authenticators.*;
import cn.com.youtong.apollo.services.*;

public class SmsSender
{

	/** ���ŷ���ҳ�� */
	private static URL sendURL;
	/** ���뷽ʽ */
	private static String encoding;
	/** ��¼ҳ�� */
	private static URL loginURL;
	/** �ύ��ʽ */
	private static String requestMethod;
	/** ���Ž�����fieldName */
	private static String receiverFieldName;
	/** ��������fieldName */
	private static String contentFieldName;

	private static Log log = LogFactory.getLog(SmsSender.class);

	private void init(ArrayList fields)
	{

		try
		{
			encoding = Config.getString("sms.encoding", "gb2312");
			String urlS = Config.getString("sms.send.url");
			String urlL = Config.getString("sms.login.url");
			requestMethod = Config.getString("sms.send.requestmethod", "POST");
			requestMethod = requestMethod.toUpperCase();
			sendURL = new URL(urlS);

			receiverFieldName = Config.getString("sms.send.receiver.fieldname");
			contentFieldName = Config.getString("sms.send.content.fieldname");

			Iterator otherFields = Config.getKeys("sms.send.others.");
			while(otherFields.hasNext())
			{
				String curr = (String) otherFields.next();
				fields.add(curr.substring(16) + "=" + URLEncoder.encode(Config.getString(curr), encoding));
			}
			fields.add(Config.getString("sms.send.fix"));
		}
		catch(MalformedURLException e)
		{
			log.fatal("���͵�URL��ַ����ȷ��");
		}
		catch(UnsupportedEncodingException e)
		{
			log.fatal("��֧�ֵı��뷽ʽ��");
		}
	}

	/**
	 *
	 */
	public SmsSender()
	{
	}

    public void sendMsg(String receiver, String content) {
        if (Config.getString("sms.send.type", "send").equals("send")) {
            send(receiver, content);
        } else {
            send(receiver, content, true);
        }
    }

	public void send(String receiver, String content)
	{
		ArrayList fields = new ArrayList();
		init(fields);

		try
		{
			fields.add(receiverFieldName + "=" + URLEncoder.encode(receiver, encoding));
			fields.add(contentFieldName + "=" + URLEncoder.encode(content, encoding));
			HttpURLConnection httpSmsConn = (HttpURLConnection) sendURL.openConnection();
			httpSmsConn.setRequestMethod(requestMethod);
			httpSmsConn.setInstanceFollowRedirects(false);
			httpSmsConn.setAllowUserInteraction(false);
			httpSmsConn.setDoOutput(true);
			HttpUtils.httpPost(httpSmsConn, fields);

			//HttpUtils.dumpResponse(httpSmsConn);
			httpSmsConn.disconnect();
		}
		catch(Exception e)
		{
			log.error("���͸�:" + receiver + "����Ϊ:" + content + "ʧ��");
			log.error(e);
		}
	}

	public void send(String receiver, String content, boolean needLogin)
	{
        if (needLogin) {
			HttpFormAuthenticator auth = new HttpFormAuthenticator();
			HashMap props = new HashMap();
			props.put(HttpFormAuthenticator.KEY_LOGIN_URL, Config.getString("sms.login.url"));
			props.put(HttpFormAuthenticator.KEY_USER_ATTRIBUTE_NAME, Config.getString("sms.login.name.fieldname"));
			props.put(HttpFormAuthenticator.KEY_PASSWORD_ATTRIBUTE_NAME, Config.getString("sms.login.password.fieldname"));
			props.put(HttpFormAuthenticator.KEY_FORM_ADDTIONAL_DATA, Config.getString("sms.login.fix"));
			props.put(HttpFormAuthenticator.KEY_USES_COOKIE, Boolean.TRUE);
			CredentialData secret = new CredentialDataImpl(Config.getString("sms.login.name.value"),
				Config.getString("sms.login.password.value"), null);
			props.put(cn.com.youtong.apollo.common.authenticators.Authenticator.KEY_CREDENTIAL_DATA, secret);

			send(receiver, content);
		} else {
            send(receiver, content);
        }
	}
}
