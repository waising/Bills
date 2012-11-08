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
 * @ClassName   ������HttpHelper 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-10-25
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-10-25   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class HttpHelper {
	
	public static final String TAG_HTTPGET = "HttpGet��ʽ";
	public static final String TAG_HTTPPOST = "HttpPost��ʽ";
	public static final int HTTP_200 = 200;
	private static final String BASIC_URL = "";
	
	private static HttpClient httpClient;

	// HttpGet��ʽ����
	public static String requestByHttpGet(String url,String method,Map m) throws Exception {
		String result="";
//		String path = "https://reg.163.com/logins.jsp?id=helloworld&pwd=android";
		// �½�HttpGet����
		String httpUrl = returnUrl(url,method,m);;

		HttpGet httpGet = new HttpGet(httpUrl);
//		httpGet.addHeader("Content-Type","application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		
		// ��ȡHttpClient����
		HttpClient httpClient = new DefaultHttpClient();
		// ��ȡHttpResponseʵ��
		HttpResponse httpResp = httpClient.execute(httpGet);
		// �ж��ǹ�����ɹ�
		if (httpResp.getStatusLine().getStatusCode() == HTTP_200) {
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpResp.getEntity().getContent(),"utf-8"));  
//		    StringBuffer sb = new StringBuffer();  
//		    while ((result = br.readLine()) != null) {     
//		        sb.append(result+"\n");     
//		    }     
//		    
//		    result = sb.toString();  
			// ��ȡ���ص�����
			result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Log.i(TAG_HTTPGET, "HttpGet��ʽ����ɹ��������������£�");
			Log.i(TAG_HTTPGET, result);
		} else {
			Log.i(TAG_HTTPGET, "HttpGet��ʽ����ʧ��");
		}
		return result;
	}

	// HttpPost��ʽ����
	public static void requestByHttpPost(String url,String method,List<NameValuePair> params) throws Exception {
		
		// �½�HttpPost����
		String httpUrl = "http://192.168.10.73:8086/Rest/bs/buzz/demo/getJson.json";//returnUrl(url,method,m);
		HttpPost httpPost = new HttpPost(httpUrl);

		// Post����
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("id", "helloworld"));
//		params.add(new BasicNameValuePair("pwd", "android"));
		
		// �����ַ���
		HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		// ���ò���ʵ��
		httpPost.setEntity(entity);
		// ��ȡHttpClient����
		HttpClient httpClient = new DefaultHttpClient();
		// ��ȡHttpResponseʵ��
		HttpResponse httpResp = httpClient.execute(httpPost);
		
	       // ����ʱ
		httpClient.getParams().setParameter("CONNECTION_TIMEOUT", 20000);
        // ��ȡ��ʱ
		httpClient.getParams().setParameter("SO_TIMEOUT", 20000);
        
		// �ж��ǹ�����ɹ�
		if (httpResp.getStatusLine().getStatusCode() == HTTP_200) {
			// ��ȡ���ص�����
			String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
			Log.i(TAG_HTTPGET, "HttpPost��ʽ����ɹ��������������£�");
			Log.i(TAG_HTTPGET, result);
		} else {
			Log.i(TAG_HTTPGET, "HttpPost��ʽ����ʧ��");
		}
	}

	// ��ȡ���ӷ��ص�����
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
	
	//У��url��ַ
	private static String returnUrl(String url,String method,Map m) throws UnsupportedEncodingException{
		
		StringBuilder params= new StringBuilder();
		StringBuilder reqUrl = new StringBuilder();
		
		String param ="",sign="&&";
		//ƴ�Ӳ��� ����encode
		 for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
			 if (iter.hasNext()){
				 params.append((m.size()>1) ? sign:"");
				 param = iter.next().toString();
				 params.append(param).append("=").append(URLEncoder.encode(m.get(param).toString(),"UTF-8"));
			 }
		 }
		
		 //ȫurl
		if (url.equals("")){
			reqUrl.append(BASIC_URL).append("?").append(params.toString());
		}else {
			reqUrl.append(url).append("?").append(params.toString());
		}
		
		return reqUrl.toString();
				
	}
	
	//���ػ�ȡ��Ϣ
	private static String requestByHttpGet(String method,Map m) throws Exception{
		
		return requestByHttpGet("", method, m);
	}
	
	
//	//Dial the GPRS link. 
//	private boolean openDataConnection() { 
//	// Set up data connection. 
//	DataConnection conn = DataConnection.getInstance(); 
//	 
//	if (connectMode == 0) { 
//	ret = conn.openConnection(mContext, ��cmwap��, ��cmwap��, ��cmwap��); 
//	} else { 
//	ret = conn.openConnection(mContext, ��cmnet��, ����, ����); 
//	} 
	 
//	} 
}
