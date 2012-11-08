package com.bosssoft.bills.common;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
/** 
 *
 * @ClassName   ������HttpConnectionUtils 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-11-1
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-11-1   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
import android.util.Log;


/**
 * HTTP connection helper
 * @author
 *
 */
public class HttpConnectionUtils implements Runnable {
	private static final String TAG = HttpConnectionUtils.class.getSimpleName();
	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;

	private static final int GET = 0;
	private static final int POST = 1;
	private static final int PUT = 2;
	private static final int DELETE = 3;
	private static final int BITMAP = 4;

	private static final int TIMEOUT= 2000;//���ӷ����
	private static final int CONNECTION_TIMEOUT = 60000; //�������˳�ʱʱ��
	private static final int SO_TIMEOUT = 50000;//��ȡ��ʱ
	
	private static final String BASIC_URL = ""; 
	
	/**
	 * �����ַ
	 */
	private String url;
	/**
	 * ��������
	 */
	private int method;
	private Handler handler;
	/**
	 * �������
	 */
	private List<NameValuePair> data;

	private HttpClient httpClient;

	/**
	 * �޲ι��췽�� 
	 */
	public HttpConnectionUtils() {
		this(new Handler());
	}

	/**
	 * ���췽��
	 * @param _handler
	 */
	public HttpConnectionUtils(Handler _handler) {
		handler = _handler;
	}
	
    /**  
     * ����һ���߳�ȥ�������������  
     * @param method ��������  
     * @param url �����ַ  
     * @param data �������  
     */
	public void create(int method, String url, List<NameValuePair> data) {
		Log.d(TAG, "method:"+method+" ,url:"+url+" ,data:"+data);
		this.method = method;
		this.url = url;
		this.data = data;
		ConnectionManager.getInstance().push(this);
	}

	/**
	 * ���ش������� Ĭ�Ϸ��ʵ�ַ
	 */
	public void create(int method,List<NameValuePair> data){
		this.create(method, BASIC_URL, data);
	}
	
	public void get(String url) {
		create(GET, url, null);
	}

	public void post(String url, List<NameValuePair> data) {
		create(POST, url, data);
	}
	
	public void put(String url, List<NameValuePair> data) {
		create(PUT, url, data);
	}

	public void delete(String url) {
		create(DELETE, url, null);
	}

	public void bitmap(String url) {
		create(BITMAP, url, null);
	}

	@Override
	public void run() {
		//��ʼִ���߳�
		handler.sendMessage(Message.obtain(handler, HttpConnectionUtils.DID_START));
		//��������
		httpClient = getHttpClient();
		
		//���ӳ�ʱ
		ConnManagerParams.setTimeout(httpClient.getParams(), TIMEOUT);
		// ����ʱ
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), CONNECTION_TIMEOUT);
		// ��ȡ��ʱ
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), SO_TIMEOUT);
		
		try {
			HttpResponse response = null;
			switch (method) {
			case GET:
				response = httpClient.execute(new HttpGet(url));
				break;
			case POST:
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(data,HTTP.UTF_8));
				response = httpClient.execute(httpPost);
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(url);
				httpPut.setEntity(new UrlEncodedFormEntity(data,HTTP.UTF_8));
				response = httpClient.execute(httpPut);
				break;
			case DELETE:
				response = httpClient.execute(new HttpDelete(url));
				break;
			case BITMAP:
				response = httpClient.execute(new HttpGet(url));
				processBitmapEntity(response.getEntity());
				break;
			}
			if (method < BITMAP)
				processEntity(response.getEntity());
		} catch (Exception e) {
			handler.sendMessage(Message.obtain(handler,HttpConnectionUtils.DID_ERROR, e));
		}
		  ConnectionManager.getInstance().didComplete(this);
	}

	//��������
	private void processEntity(HttpEntity entity) throws IllegalStateException,
			IOException {
/*		BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line,result = "";
		while ((line = br.readLine()) != null)
			result += line;*/
		String result = EntityUtils.toString(entity, "UTF-8");
		Message message = Message.obtain(handler, DID_SUCCEED, result);
		handler.sendMessage(message);
	}

	//����ͼƬ����
	private void processBitmapEntity(HttpEntity entity) throws IOException {
		BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
		Bitmap bm = BitmapFactory.decodeStream(bufHttpEntity.getContent());
		handler.sendMessage(Message.obtain(handler, DID_SUCCEED, bm));
	}
	
	//httpclient ����
	private HttpClient getHttpClient(){
		if (httpClient==null){
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}
}
