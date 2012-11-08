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
 * @ClassName   类名：HttpHandler 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-11-1
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-11-1   wwx   创建该类功能。
 *
 ************************************************************************
 *</p>
 */
public class HttpHandler extends Handler {

//	private Timer timer = new Timer(true);
	/**
	 * 执行数据显示提示框超过时间
	 */
	private static final int SHOWDIALOG_TIME = 200;

	 /**  
     * 上下文  
     */ 
	private Context context;
	private JSONObject jsObj;
	 /**  
     * 对话框  
     */ 
	private ProgressDialog progressDialog;

	public JSONObject getJsonObj(){
		return this.jsObj;
	}
	
	public HttpHandler(Context context) {
		this.context = context;
	}

    /**  
     * 进程开始执行  "Please Wait...", "processing...",
     * @throws InterruptedException 
     */
	protected void start() throws InterruptedException {
		//处理数据超过时间则提示框
		Thread.sleep(SHOWDIALOG_TIME);
		progressDialog = ProgressDialog.show(context,
		"请等待...", "数据处理中...", true);

	}

    /**  
     * 执行成功  
     * @param json 返回的json  
     */
	protected void succeed(JSONObject jObject) throws JSONException {
		if(progressDialog!=null && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		
		this.jsObj = jObject;
	}

    /**  
     * 执行失败  
     * @param json 返回的json  
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
	
	//判断what类型做出相应操作
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
			//取消提示框
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
