package com.bosssoft.bills.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 
 * @ClassName ������httpClient����
 * @Description ����˵����
 * <p>
 * TODO httpClient����
 *</p>
 ************************************************************************
 * @date        �������ڣ�
 * @author      �����ˣ�
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   
 *
 ***********************************************************************
 *</p>
 */
public class HttpClientUtil {
	
	public  static final String CHARSET_GBK = "GBK";
	
	private final static String TAG = "HTTPCLIENT";
	private static HttpClient  hpcliet= null;
	private final static String BASIC_URL="";

	/**
	 * HttpRequest���׳���IOExceptionͨ����ĳЩ���쳣�������������Ӧ,���ӱ����õ����
	 * ͨ������Ϊ�Ƿ������ɻָ���,����ͨ�������Զ��쳣�ظ���������
	 * 
	 * @return �Զ�����Զ��쳣�ָ�����
	 */
	public static HttpRequestRetryHandler getRetryHandler() {
		// ������һ���Զ��쳣�ظ�,�����������½������10������
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 10) {
					// Do not retry if over max retry count
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					return true;
				}
				if (exception instanceof SSLHandshakeException) {
					// Do not retry on SSL handshake exception
					return false;
				}
				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}
		};
		return retryHandler;
	}

	
	private static HttpClient crtHttpClient(){
		HttpParams params = new BasicHttpParams();
		// ����������ӵ�200
		 // ģ���������һЩ����������ֻ�������������
		params.setParameter(CoreProtocolPNames.USER_AGENT,
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
		params.setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		params.setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, CHARSET_GBK);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

       // ���ó�ʱ
		params.setIntParameter("http.socket.timeout", 180000);
		//���ùر�Nagle�㷨
		params.setBooleanParameter("http.tcp.nodelay", false);
		
		// ���ӳ�ʱ
		params.setIntParameter("http.connection.timeout", 160000);
		
		SchemeRegistry registry = new SchemeRegistry();
		
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params,registry);
		HttpClient httpclient= new DefaultHttpClient(cm, params);
		
        // �����Զ��쳣�ظ�
        ((AbstractHttpClient) httpclient).setHttpRequestRetryHandler(getRetryHandler());
        
        IdleConnectionMonitorThread thd = new IdleConnectionMonitorThread(cm);
        thd.start();
		return httpclient;
	}
	
	/**
	 * 
	 * �������ƣ�getHttpClient 
	 * ����˵����httpclient����
	 * ����˵����
	 * @return
	 * @date   ����ʱ�䣺2012-11-7
	 * @author ���ߣ�wwx
	 */
	public static HttpClient getHttpClient() {
		
	    if(hpcliet == null){
	    	hpcliet =crtHttpClient();
	    };
	    return hpcliet;
		//return hpcliet =crtHttpClient();
	}
	
	
	/**
	 * 
	 * �������ƣ�excuteGet 
	 * ����˵����ִ�д�������url
	 * ����˵����
	 * @param url
	 * @return
	 * @date   ����ʱ�䣺2012-11-7
	 * @author ���ߣ�wwx
	 */
	public static String excuteGet(String url){
		if (url == null || url.trim().length()==0) {
			 url = BASIC_URL;
		 }
		 String url2= StringUtils.substringBefore(url, "?");
		 String paramList= StringUtils.substringAfter(url, "?");
		 String[] paramArray = StringUtils.split(paramList, "&");
		 HashMap<String,String> params = new HashMap<String,String>();
		 for(int i =0;i< paramArray.length;i++){
			 String[] param = new String[2];
		     param[0] = StringUtils.substringBefore(paramArray[i], "=");
		     param[1] = StringUtils.substringAfter(paramArray[i], "=");
		     params.put(param[0], param[1]);
		 }
		 return excuteGet(url2,params,null);
	}
	
	public static String excuteGet(String url, Map<String, String> params){
          return excuteGet(url,params,null);
	}
	
	/**
	 * 
	 * �������ƣ�excuteGet 
	 * ����˵�����ò�����ִ�з���
	 * ����˵����
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @date   ����ʱ�䣺2012-11-7
	 * @author ���ߣ�wwx
	 */
	public static String excuteGet(String url, Map<String, String> params, String charset) {
		    if (url == null || url.trim().length()==0) {
			  url = BASIC_URL;
		    }
		    if(charset == null || charset.trim().length()==0){
		    	charset = CHARSET_GBK;
		    }
			List<NameValuePair> qparams = getParamsList(params);
			if (qparams != null && qparams.size() > 0) {
			charset = (charset == null ? CHARSET_GBK : charset);
			String formatParams = URLEncodedUtils.format(qparams, charset);
			url = (url.indexOf("?")) < 0 ? (url + "?" + formatParams) : (url
			.substring(0, url.indexOf("?") + 1) + formatParams);
			}
			HttpGet hg = new HttpGet(url);
			
			// �������󣬵õ���Ӧ
			try {
				
				Log.v(TAG, "ִ��GETt����"+hg.getURI());
				HttpResponse response = getHttpClient().execute(hg);
				Log.v(TAG,"ִ��GET���: " + response.getStatusLine());
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, charset);
				hg.abort();
				//httpClient.getConnectionManager().shutdown();
				return result;
			} catch (Exception e) {
				hg.abort();
				//httpClient.getConnectionManager().shutdown();
				Log.v(TAG,"ִ��GET����: " + e.getMessage());
				return  "";
			}
			
	}

	
	public static String excutePost(String url, Map<String, String> params){
        return excutePost(url,params,null);
	}
	
	/**
	 * 
	 * �������ƣ�excutePost 
	 * ����˵����url�д�������ִ�з���
	 * ����˵����
	 * @param url
	 * @return
	 * @date   ����ʱ�䣺2012-11-7
	 * @author ���ߣ�wwx
	 */
	public static String excutePost(String url){
		if (url == null || url.trim().length()==0) {
			 url = BASIC_URL;
		 }
		 String url2= StringUtils.substringBefore(url, "?");
		 String paramList= StringUtils.substringAfter(url, "?");
		 String[] paramArray = StringUtils.split(paramList, "&");
		 HashMap<String,String> params = new HashMap<String,String>();
		 for(int i =0;i< paramArray.length;i++){
		     String[] param = new String[2];
		     param[0] = StringUtils.substringBefore(paramArray[i], "=");
		     param[1] = StringUtils.substringAfter(paramArray[i], "=");
		    	 //StringUtils.split(, "=");
		     params.put(param[0], param[1]);
		 }
		 return excutePost(url2,params,null);
	}
	
	/**
	 * 
	 * �������ƣ�excutePost 
	 * ����˵����������ʽ����
	 * ����˵����
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @date   ����ʱ�䣺2012-11-7
	 * @author ���ߣ�wwx
	 */
	public static String excutePost(String url, Map<String, String> params, String charset) {
		    if (url == null || url.trim().length()==0) {
			  url = BASIC_URL;
		    }
		    if(charset == null || charset.trim().length()==0){
		    	charset = CHARSET_GBK;
		    }
		    
		    UrlEncodedFormEntity formEntity = null;
		    try {
			    if (charset == null || StringUtils.isEmpty(charset)) {
			      formEntity = new UrlEncodedFormEntity(getParamsList(params));
			    } else {
			      formEntity = new UrlEncodedFormEntity(getParamsList(params), charset);
			    }
		    } catch (UnsupportedEncodingException e) {
		    throw new IllegalArgumentException("��֧�ֵı��뼯", e);
		    }
		    
		    HttpPost hp = new HttpPost(url);
		    hp.setEntity(formEntity);

		 // �������󣬵õ���Ӧ
			try {
				
				Log.v(TAG,"ִ��POST����: " + hp.getURI());
				HttpResponse response = getHttpClient().execute(hp);
				Log.v(TAG,"ִ��POST���: " + response.getStatusLine());
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, charset);
				hp.abort();
				//httpClient.getConnectionManager().shutdown();
				return result;
			} catch (Exception e) {
				hp.abort();
				//httpClient.getConnectionManager().shutdown();
				//hpcliet = null;
				Log.v(TAG,"ִ��POST����: " + e.getMessage());
				return  "";
			}

	}
	
	/**
	 * 
	 * <p>�������ƣ�excutePostUrl        </p>
	 * <p>����˵����ִ��Post����
	 *
	 * </p>
	 *<p>����˵����</p>
	 * @param httpClient
	 * @param url ������Url��ַ
	 * @return
	 *
	 * @date   ����ʱ�䣺2011-4-19
	 * @author ���ߣ�Josen.Lin
	 */
	public static String excutePostUrl(String url) {
	    if (url == null || url.trim().length()==0) {
		  url = BASIC_URL;
	    }
	    
	    HttpPost hp = new HttpPost(url);
	    HttpContext context =  new BasicHttpContext();

	 // �������󣬵õ���Ӧ
		try {
			
			Log.v(TAG,"ִ��POST����: " + hp.getURI());
			HttpResponse response = getHttpClient().execute(hp, context);
			Log.v(TAG,"ִ��POST���: " + response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, CHARSET_GBK);
//			EntityUtils.consume(entity);
			hp.abort();
			//httpClient.getConnectionManager().shutdown();
			return result;
		} catch (Exception e) {
			hp.abort();
			//httpClient.getConnectionManager().shutdown();
			//hpcliet = null;
			Log.v(TAG,"ִ��POST����: " + e.getMessage());
			return  "";
		}

}

	    /**
	    * ������ļ�/ֵ�Բ���ת��ΪNameValuePair������
	    * 
	    * @param paramsMap
	    * ������, ��/ֵ��
	    * @return NameValuePair������
	    */
	  private static  List<NameValuePair> getParamsList(Map<String, String> paramsMap ){
	      if (paramsMap == null || paramsMap.size() == 0) {
	        return null;
	      }
	      List<NameValuePair> params = new ArrayList<NameValuePair>();
	      for (Map.Entry<String, String> map : paramsMap.entrySet()) {
	          params.add(new BasicNameValuePair(map.getKey(), map.getValue()));
	      }
	      return params;
	    }


}
