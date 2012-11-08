package com.bosssoft.bills.module.basic;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.bosssoft.bills.R;
import com.bosssoft.bills.common.HttpConnectionUtils;
import com.bosssoft.bills.common.HttpHandler;
import com.bosssoft.bills.httpclient.HttpClientUtil;

/** 
 *
 * @ClassName   ������TestActivity 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-10-26
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-10-26   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class TestActivity extends BaseActivity implements OnClickListener {
	
	private EditText nameEditText ;
//	private EditText pwdEdt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
//        pwdEdt = (EditText) findViewById(R.id.pwd);
        findViewById(R.id.reg).setOnClickListener((OnClickListener) this);
        
        findViewById(R.id.cancel).setOnClickListener((OnClickListener) this);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.reg:
			try {
				regUser();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.cancel:
			Toast.makeText(this, "�û�ȡ��ע��!", Toast.LENGTH_SHORT).show();
		default:
			break;
		}
	}
	

	//ע���û�
	public void regUser() throws Exception{
		nameEditText = (EditText) findViewById(R.id.name);
		String name = nameEditText.getText().toString().trim();
//		
//		if (name.equals("")){
//			Toast.makeText(this, "�û�����Ϊ��!", Toast.LENGTH_SHORT).show();
//			return;
//		}

		
//		login();
		test();
//		
//		JSONObject j = handler.getJsonObj();
//		nameEditText.setText(j.getString("success")+j.getString("name"));

//		nameEditText.setText(HttpHelper.requestByHttpGet("","",null));
		
		//Intent i = new Intent();
		//i.setClass(getApplicationContext(), MainActivity.class);
		
		//i.putExtra("xx",name);
		//startActivity(i);
		
//		setContentView(R.layout.activity_main);
//		Toast.makeText(this, "��,��ô������", Toast.LENGTH_SHORT).show();
	}
	
	
	//��home��ʱ��������
//    @Override  
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
//        String name = savedInstanceState.getString("name"); //�ָ�EditText������  
//        nameEditText.setText(name);  
//        super.onRestoreInstanceState(savedInstanceState);  
//    }  
//  
//    @Override  
//    protected void onSaveInstanceState(Bundle outState) {   //����EditText������  
//        outState.putString("name", nameEditText.getText()+"");  
//        super.onSaveInstanceState(outState);  
//    }  
	
	HttpHandler handler = new HttpHandler(TestActivity.this){		
		@Override
		public void succeed(JSONObject jObject) throws JSONException { //�Լ�����ɹ���Ĳ���
			super.succeed(jObject);
			String x = jObject.getString("success");
			Log.v("xxxxxxx", jObject.getString("name").toString());
			
			nameEditText = (EditText) findViewById(R.id.name);
			nameEditText.setText(x+jObject.getString("name"));
		} //Ҳ����������дstart() failed()����
	};
//	
	private void login() throws JSONException {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add((NameValuePair) new BasicNameValuePair("email", "xx"));
		params.add(new BasicNameValuePair("password", "xxpwd"));
		String urlString = "http://192.168.10.73:8086/Rest/bs/buzz/demo/getJson.json?userName=gougougou";
		
		HttpConnectionUtils client = new HttpConnectionUtils(handler);
		client.get(urlString);
//		client.run();
		

//		JSONObject j = handler.getJsonObj();
//		nameEditText.setText(j.getString("success")+j.getString("name"));
	}
	
	public void test(){
		String urlString = "http://192.168.10.73:8086/Rest/bs/buzz/demo/getJson.json";
		
		com.bosssoft.bills.httpclient.HttpClientUtil hc = new HttpClientUtil();
		
		String xx = hc.excutePostUrl(urlString);
		Toast.makeText(getApplicationContext(), xx,Toast.LENGTH_SHORT).show();
	}
}
