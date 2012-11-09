package com.bosssoft.bills.httpclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.http.client.ClientProtocolException;
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

import android.os.Environment;
import android.util.Log;

/**
 * 
 * @ClassName 类名：httpClient工具
 * @Description 功能说明：
 * <p>
 * TODO httpClient工具
 *</p>
 ************************************************************************
 * @date        创建日期：
 * @author      创建人：
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
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
	 * HttpRequest会抛出的IOException通常中某些的异常比如服务器无响应,连接被重置等情况
	 * 通常被认为是非致命可恢复的,可以通过设置自动异常回复进行重试
	 * 
	 * @return 自定义的自动异常恢复处理
	 */
	public static HttpRequestRetryHandler getRetryHandler() {
		// 常见的一种自动异常回复,在允许的情况下进行最多10次重试
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

	/**
	 * 
	 * 函数名称：crtHttpClient 
	 * 功能说明：创建httpclient对象
	 * 参数说明：
	 * @return
	 * @date   创建时间：2012-11-8
	 * @author 作者：wwx
	 */
	private static HttpClient crtHttpClient(){
		HttpParams params = new BasicHttpParams();
		// 增加最大连接到200
		 // 模拟浏览器，一些服务器程序只允许浏览器访问
		params.setParameter(CoreProtocolPNames.USER_AGENT,
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
		params.setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		params.setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, CHARSET_GBK);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

       // 设置超时
		params.setIntParameter("http.socket.timeout", 180000);
		//设置关闭Nagle算法
		params.setBooleanParameter("http.tcp.nodelay", false);
		
		// 连接超时
		params.setIntParameter("http.connection.timeout", 160000);
		
		SchemeRegistry registry = new SchemeRegistry();
		
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params,registry);
		HttpClient httpclient= new DefaultHttpClient(cm, params);
		
        // 设置自动异常回复
        ((AbstractHttpClient) httpclient).setHttpRequestRetryHandler(getRetryHandler());
        
        IdleConnectionMonitorThread thd = new IdleConnectionMonitorThread(cm);
        thd.start();
		return httpclient;
	}
	
	/**
	 * 
	 * 函数名称：getHttpClient 
	 * 功能说明：httpclient对象
	 * 参数说明：
	 * @return
	 * @date   创建时间：2012-11-7
	 * @author 作者：wwx
	 */
	public static HttpClient getHttpClient() {
		
	    if(hpcliet == null){
	    	hpcliet =crtHttpClient();
	    };
	    return hpcliet;
	}
	
	
	/**
	 * 
	 * 函数名称：excuteGet 
	 * 功能说明：执行带参数的url
	 * 参数说明：
	 * @param url
	 * @return
	 * @date   创建时间：2012-11-7
	 * @author 作者：wwx
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
	 * 函数名称：excuteGet 
	 * 功能说明：用参数的执行方法
	 * 参数说明：
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @date   创建时间：2012-11-7
	 * @author 作者：wwx
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
			
			// 发送请求，得到响应
			try {
				
				Log.v(TAG, "执行GETt请求"+hg.getURI());
				HttpResponse response = getHttpClient().execute(hg);
				Log.v(TAG,"执行GET结果: " + response.getStatusLine());
				HttpEntity entity = response.getEntity();
//				String result = getInputStreamReader(entity); 
				String result = EntityUtils.toString(entity, charset);
				hg.abort();
				//httpClient.getConnectionManager().shutdown();
				return result;
			} catch (Exception e) {
				hg.abort();
				//httpClient.getConnectionManager().shutdown();
				Log.v(TAG,"执行GET出错: " + e.getMessage());
				return  "";
			}
			
	}

	
	public static String excutePost(String url, Map<String, String> params){
        return excutePost(url,params,null);
	}
	
	/**
	 * 
	 * 函数名称：excutePost 
	 * 功能说明：url中带参数的执行方法
	 * 参数说明：
	 * @param url
	 * @return
	 * @date   创建时间：2012-11-7
	 * @author 作者：wwx
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
	 * 函数名称：excutePost 
	 * 功能说明：参数方式访问
	 * 参数说明：
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @date   创建时间：2012-11-7
	 * @author 作者：wwx
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
		        throw new IllegalArgumentException("不支持的编码集", e);
		    }
		    
		    HttpPost hp = new HttpPost(url);
		    hp.setEntity(formEntity);

		 // 发送请求，得到响应
			try {
				Log.v(TAG,"执行POST请求: " + hp.getURI());
				HttpResponse response = getHttpClient().execute(hp);
				Log.v(TAG,"执行POST结果: " + response.getStatusLine());
				HttpEntity entity = response.getEntity();
//				String result = getInputStreamReader(entity); 
				String result = EntityUtils.toString(entity, charset);
				hp.abort();
				//httpClient.getConnectionManager().shutdown();
				return result;
			} catch (Exception e) {
				hp.abort();
				//httpClient.getConnectionManager().shutdown();
				//hpcliet = null;
				Log.v(TAG,"执行POST出错: " + e.getMessage());
				return  "";
			}
	}
	
	/**
	 * 
	 * <p>函数名称：excutePostUrl        </p>
	 * <p>功能说明：执行Post请求
	 *
	 * </p>
	 *<p>参数说明：</p>
	 * @param httpClient
	 * @param url 完整的Url地址
	 * @return
	 *
	 * @date   创建时间：
	 * @author 作者：
	 */
	public static String excutePostUrl(String url) {
	    if (url == null || url.trim().length()==0) {
		  url = BASIC_URL;
	    }
	    
	    HttpPost hp = new HttpPost(url);
	    HttpContext context =  new BasicHttpContext();

	 // 发送请求，得到响应
		try {
			
			Log.v(TAG,"执行POST请求: " + hp.getURI());
			HttpResponse response = getHttpClient().execute(hp, context);
			Log.v(TAG,"执行POST结果: " + response.getStatusLine());
			HttpEntity entity = response.getEntity();
//			String result = getInputStreamReader(entity); 
			String result = EntityUtils.toString(entity, CHARSET_GBK);
//			EntityUtils.consume(entity);
			hp.abort();
			//httpClient.getConnectionManager().shutdown();
			return result;
		} catch (Exception e) {
			hp.abort();
			//httpClient.getConnectionManager().shutdown();
			//hpcliet = null;
			Log.v(TAG,"执行POST出错: " + e.getMessage());
			return  "";
		}

}

	    /**
	    * 将传入的键/值对参数转换为NameValuePair参数集
	    * 
	    * @param paramsMap
	    * 参数集, 键/值对
	    * @return NameValuePair参数集
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


	  /**
	   * 
	   * 函数名称：getInputStreamReader 
	   * 功能说明：获取返回信息
	   * 参数说明：
	   * @param entity
	   * @return
	   * @throws UnsupportedEncodingException
	   * @throws IllegalStateException
	   * @throws IOException
	   * @date   创建时间：2012-11-9
	   * @author 作者：wwx
	   */
	  private static String getInputStreamReader(HttpEntity entity) throws UnsupportedEncodingException, IllegalStateException, IOException{
			StringBuilder result = new StringBuilder();
			if(entity != null){
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent(),"UTF-8"),8192);
				String line = null;
				while((line = reader.readLine()) != null){
					result.append(line+"\n");//按行读取放入StringBuilder中
				}
			    reader.close();
			}
			return result.toString();
	  }
	  
	  /**
	   * 
	   * 函数名称：downAppFile 
	   * 功能说明：下载文件
	   * 参数说明：
	   * @param url
	   * @param appName
	   * @date   创建时间：2012-11-9
	   * @author 作者：wwx
	   */
	  public static void downAppFile(String url,String appName){
		  
		HttpGet hg = new HttpGet(url);
		
		// 发送请求，得到响应
		try 
		{
			HttpResponse response = getHttpClient().execute(hg);
			HttpEntity entity = response.getEntity();
			long length = entity.getContentLength();
			Log.isLoggable("DownTag", (int) length);
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if(is == null){
				throw new RuntimeException("isStream is null");
			}
			File file = new File(Environment.getExternalStorageDirectory(),appName);
			fileOutputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int ch = -1;
			do{
				ch = is.read(buf);
				if(ch <= 0)break;
				fileOutputStream.write(buf, 0, ch);
			}while(true);
			is.close();
			fileOutputStream.close();
		}catch(ClientProtocolException e){
				e.printStackTrace();
		}catch(IOException e){
			hg.abort();
			Log.v(TAG,"执行下载出错: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
