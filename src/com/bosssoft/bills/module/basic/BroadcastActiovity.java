package com.bosssoft.bills.module.basic;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.bosssoft.bills.R;
import com.bosssoft.bills.common.HttpConnectionUtils;
import com.bosssoft.bills.common.HttpHandler;
import com.bosssoft.bills.common.HttpHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/** 
 *
 * @ClassName   类名：BroadcastActiovity 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-10-30
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-10-30   wwx   创建该类功能。
 *
 ************************************************************************
 *</p>
 */
public class BroadcastActiovity extends Activity implements OnClickListener ,OnItemSelectedListener{

	private static final String ACTION_INTENT_TEST = "com.bosssoft.bills.module.basic.TestBroadcast";
	private static final String ACTION_INTENT_CALL = "";
	
	Button b;
	Spinner s;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast_test);
        
        b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(this);
        
        s = (Spinner) findViewById(R.id.spinner1);
        
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ad.add("xx1");
        ad.add("xx2");
        ad.add("xx3");
        s.setAdapter(ad);
        s.setOnItemSelectedListener(this);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			Intent intent = new Intent(ACTION_INTENT_TEST);
			intent.putExtra("xx", "我去了");
			sendBroadcast(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		Spinner sp = (Spinner) arg0;
//		login();
		try {
			HttpHelper.requestByHttpPost("", "", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Toast.makeText(getApplicationContext(), sp.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
		Log.v("SPINNER", sp.getSelectedItem().toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
		
	}


}
