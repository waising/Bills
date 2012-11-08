package com.bosssoft.bills.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/** 
 *
 * @ClassName   ������HttpHandler 
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
public class HttpHandler extends Handler {

//	private Timer timer = new Timer(true);
	/**
	 * ִ��������ʾ��ʾ�򳬹�ʱ��
	 */
	private static final int SHOWDIALOG_TIME = 200;

	 /**  
     * ������  
     */ 
	private Context context;
	private JSONObject jsObj;
	 /**  
     * �Ի���  
     */ 
	private ProgressDialog progressDialog;

	public JSONObject getJsonObj(){
		return this.jsObj;
	}
	
	public HttpHandler(Context context) {
		this.context = context;
	}

    /**  
     * ���̿�ʼִ��  "Please Wait...", "processing...",
     * @throws InterruptedException 
     */
	protected void start() throws InterruptedException {
		//�������ݳ���ʱ������ʾ��
		Thread.sleep(SHOWDIALOG_TIME);
		progressDialog = ProgressDialog.show(context,
		"��ȴ�...", "���ݴ�����...", true);

	}

    /**  
     * ִ�гɹ�  
     * @param json ���ص�json  
     */
	protected void succeed(JSONObject jObject) throws JSONException {
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
		this.jsObj = jObject;
	}

    /**  
     * ִ��ʧ��  
     * @param json ���ص�json  
     */
	protected void failed(JSONObject jObject) {
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
		this.jsObj = jObject;
//		timer.cancel();
	}
	
	protected void otherHandleMessage(Message message){
	}
	
	//�ж�what����������Ӧ����
	public void handleMessage(Message message) {
		switch (message.what) {
		case HttpConnectionUtils.DID_START: //connection start
			Log.d(context.getClass().getSimpleName(),"http connection start...");
			try {
				start();
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
		case HttpConnectionUtils.DID_SUCCEED: //connection success
			//ȡ����ʾ��
			if(progressDialog!=null && progressDialog.isShowing()){
			  progressDialog.dismiss();
			}
			
			String response = (String) message.obj;
			Log.d(context.getClass().getSimpleName(), "http connection return."+ response);
			try {
				JSONObject jObject = new JSONObject(response == null ? "": response.trim());
				
				if ("true".equals(jObject.getString("success"))) { //operate success
//					Toast.makeText(context, "operate succeed:"+jObject.getString("msg"),Toast.LENGTH_SHORT).show();
					succeed(jObject);
				} else {
//					Toast.makeText(context, "operate fialed:"+jObject.getString("msg"),Toast.LENGTH_LONG).show();
					failed(jObject);
				}
			} catch (JSONException e1) {
				if(progressDialog!=null && progressDialog.isShowing()){
					progressDialog.dismiss();
				}
				e1.printStackTrace();
//				Toast.makeText(context, "Response data is not json data",Toast.LENGTH_LONG).show();
			}
			break;
		case HttpConnectionUtils.DID_ERROR: //connection error
			if(progressDialog!=null && progressDialog.isShowing()){
				progressDialog.dismiss();
			}
			Exception e = (Exception) message.obj;
			e.printStackTrace();
			Log.e(context.getClass().getSimpleName(), "connection fail."+ e.getMessage());
			Toast.makeText(context, "connection fail,please check connection!",Toast.LENGTH_LONG).show();
			break;
		}
		otherHandleMessage(message);
	}

}
