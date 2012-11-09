package com.bosssoft.bills.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bosssoft.bills.R;
import com.bosssoft.bills.httpclient.HttpClientUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UpdateAppActivity extends Activity {
    /** Called when the activity is first created. */
	private static final String TAG = "UPDATEAPP";
	private Button btnUpdateApp;
	private ProgressDialog pBar;
	private String downPath = "http://10.0.2.2:8080/";//下载地址
	private String appName = "Bills.apk"; //apk name
	private String appVersion = "version.json";
	private int newVerCode = 0;
	private String newVerName = "";
	private Handler handler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
        	if(isNetworkAvailable(this) == false){
        		return;
        	}else{
        		checkToUpdate();
        	}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        btnUpdateApp = (Button)findViewById(R.id.button1);
        btnUpdateApp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			}
        });
    }
    //check the Network is available
    private static boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
    	try{
    	//检测网络是否可用
    		ConnectivityManager cm = (ConnectivityManager)context
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	//check new version and update
	private void checkToUpdate() throws NameNotFoundException {
		// TODO Auto-generated method stub
		if(getServerVersion()){
			int currentCode = CurrentVersion.getVerCode(this);
			if(newVerCode > currentCode)
			{//Current Version is old
//				显示更新对话框			
				showUpdateDialog();
			}
		}
	}
	//show Update Dialog
	private void showUpdateDialog() throws NameNotFoundException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本");
		sb.append(CurrentVersion.getVerName(this));
		sb.append("VerCode:");
		sb.append(CurrentVersion.getVerCode(this));
		sb.append("\n");
		sb.append("发现新版本");
		sb.append(newVerName);
		sb.append("NewVerCode:");
		sb.append(newVerCode);
		sb.append("\n");
		sb.append("是否更新?");
		Dialog dialog = new AlertDialog.Builder(UpdateAppActivity.this)
		.setTitle("软件更新")
		.setMessage(sb.toString())
		.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				  showProgressBar();//更新当前版本
				}
		})
		.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).create();
		dialog.show();
	}
	protected void showProgressBar() {
		// TODO Auto-generated method stub
		pBar = new ProgressDialog(UpdateAppActivity.this);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍候...");
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		downAppFile(downPath + appName);
	}
	//Get ServerVersion from GetUpdateInfo.getUpdateVerJSON
	private boolean getServerVersion() {
		// TODO Auto-generated method stub
		try{
			String newVerJSON = HttpClientUtil.excuteGet(downPath + appVersion);
			JSONArray jsonArray = new JSONArray(newVerJSON);
			if(jsonArray.length() > 0){
				JSONObject obj = jsonArray.getJSONObject(0);
				try{
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				}catch(Exception e){
					Log.e(TAG, e.getMessage());
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	protected void downAppFile(final String url) {
		pBar.show();
		new Thread(){
			public void run(){
				//下载文件
				HttpClientUtil.downAppFile(url, appName);
				haveDownLoad();
			}
		}.start();
	}
	
	//cancel progressBar and start new App
	protected void haveDownLoad() {
		// TODO Auto-generated method stub
		handler.post(new Runnable(){
			public void run(){
				pBar.cancel();
				//提示是否安装新程序			
				Dialog installDialog = new AlertDialog.Builder(UpdateAppActivity.this)
				.setTitle("下载完成")
				.setMessage("是否安装新的应用")
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						installNewApk();
						finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
							}
						}).create();
		
				installDialog.show();
				}
			});
		}
	//安装新的应用
	protected void installNewApk() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(
				new File(Environment.getExternalStorageDirectory(),appName)),"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
}