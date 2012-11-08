package com.bosssoft.bills.common;

import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.apache.http.NameValuePair;

import android.util.Log;
/** 
 *
 * @ClassName   类名：HttpHelper 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-10-25
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-10-25   wwx   创建该类功能。
 *
 ************************************************************************
 *</p>
 */
public class HttpHelper {
	
	public static final String TAG_HTTPGET = "HttpGet方式";
	public static final String TAG_HTTPPOST = "HttpPost方式";
	public static final int HTTP_200 = 200;
	private static final String BASIC_URL = "";
	
	private static HttpClient httpClient;

	// HttpGet方式请求
	public static String requestByHttpGet(String url,String method,Map m) throws Exception {
		String result="";
//		String path = "https://reg.163.com/logins.jsp?id=helloworld&pwd=android";
		// 新建HttpGet对象
		String httpUrl = returnUrl(url,method,m);;

		HttpGet httpGet = new HttpGet(httpUrl);
//		httpGet.addHeader("Content-Type","application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		
		// 获取HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		// 获取HttpResponse实例
		HttpResponse httpResp = httpClient.execute(httpGet);
		// 判断是够请求成功
		if (httpResp.getStatusLine().getStatusCode() == HTTP_200) {
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpResp.getEntity().getContent(),"utf-8"));  
//		    StringBuffer sb = new StringBuffer();  
//		    while ((result = br.readLine()) != null) {     
//		        sb.append(result+"\n");     
//		    }     
//		    
//		    result = sb.toString();  
			// 获取返回的数据
			result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Log.i(TAG_HTTPGET, "HttpGet方式请求成功，返回数据如下：");
			Log.i(TAG_HTTPGET, result);
		} else {
			Log.i(TAG_HTTPGET, "HttpGet方式请求失败");
		}
		return result;
	}

	// HttpPost方式请求
	public static void requestByHttpPost(String url,String method,List<NameValuePair> params) throws Exception {
		
		// 新建HttpPost对象
		String httpUrl = "http://192.168.10.73:8086/Rest/bs/buzz/demo/getJson.json";//returnUrl(url,method,m);
		HttpPost httpPost = new HttpPost(httpUrl);

		// Post参数
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", "helloworld"));
//		params.add(new BasicNameValuePair("pwd", "android"));
		
		// 设置字符集
		HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		// 设置参数实体
		httpPost.setEntity(entity);
		// 获取HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		// 获取HttpResponse实例
		HttpResponse httpResp = httpClient.execute(httpPost);
		
	       // 请求超时
		httpClient.getParams().setParameter("CONNECTION_TIMEOUT", 20000);
        // 读取超时
		httpClient.getParams().setParameter("SO_TIMEOUT", 20000);
        
		// 判断是够请求成功
		if (httpResp.getStatusLine().getStatusCode() == HTTP_200) {
			// 获取返回的数据
			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Log.i(TAG_HTTPGET, "HttpPost方式请求成功，返回数据如下：");
			Log.i(TAG_HTTPGET, result);
		} else {
			Log.i(TAG_HTTPGET, "HttpPost方式请求失败");
		}
	}

	// 获取连接返回的数据
	private static byte[] readStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		byte[] data = baos.toByteArray();
		inputStream.close();
		baos.close();
		return data;
	}
	
	//校验url地址
	private static String returnUrl(String url,String method,Map m) throws UnsupportedEncodingException{
		
		StringBuilder params= new StringBuilder();
		StringBuilder reqUrl = new StringBuilder();
		
		String param ="",sign="&&";
		//拼接参数 并且encode
		 for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
			 if (iter.hasNext()){
				 params.append((m.size()>1) ? sign:"");
				 param = iter.next().toString();
				 params.append(param).append("=").append(URLEncoder.encode(m.get(param).toString(),"UTF-8"));
			 }
		 }
		
		 //全url
		if (url.equals("")){
			reqUrl.append(BASIC_URL).append("?").append(params.toString());
		}else {
			reqUrl.append(url).append("?").append(params.toString());
		}
		
		return reqUrl.toString();
				
	}
	
	//重载获取信息
	private static String requestByHttpGet(String method,Map m) throws Exception{
		
		return requestByHttpGet("", method, m);
	}
	
	
//	//Dial the GPRS link. 
//	private boolean openDataConnection() { 
//	// Set up data connection. 
//	DataConnection conn = DataConnection.getInstance(); 
//	 
//	if (connectMode == 0) { 
//	ret = conn.openConnection(mContext, “cmwap”, “cmwap”, “cmwap”); 
//	} else { 
//	ret = conn.openConnection(mContext, “cmnet”, “”, “”); 
//	} 
	 
//	} 
}
