package com.bosssoft.bills.module.basic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import com.bosssoft.bills.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener  {

	final String BASIC_URL = "http://192.168.10.73:8086";
    private EditText userNameEditText;
    
    String xx,yy;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.ss).setOnClickListener((OnClickListener) this);
        
        findViewById(R.id.send).setOnClickListener((OnClickListener) this);
        
        Intent i = getIntent();
        
        xx = i.getAction();
        
        yy = i.getStringExtra("xx");
        userNameEditText = (EditText) this.findViewById(R.id.user);
        userNameEditText.setText(yy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
    
    
	//试试手气按钮事件
	public void onClick(View v) {
		// TODO Auto-generated method stub
		userNameEditText = (EditText) this.findViewById(R.id.user);

		switch (v.getId()) {
		case R.id.ss:
			userNameEditText.setText(getMsg());
			break;
		case R.id.send:
			sendMsg();
			break;
		default:
			break;
		}
	}
	
	//发送信息
	private void sendMsg(){
		String userName = userNameEditText.getText().toString().trim();
		if (userName.equals("")){
			Toast.makeText(this, "文本框不能为空!", Toast.LENGTH_SHORT).show();
		}else{
			try{
	            StringBuilder buf = new StringBuilder(); 
	            buf.append("userName="+URLEncoder.encode(userName,"gb2312")+"&"); 
	            byte[]data = buf.toString().getBytes("UTF-8"); 
	            //http://192.168.10.73:8086/Rest/bs/buzz/demo/sendMsg.json?userName=xxxxx
	            URL url = new URL(BASIC_URL+"/Rest/bs/buzz/demo/getJson.json?userName=xxxxx"); 
	            HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
	            conn.setRequestMethod("POST"); 
	            conn.setDoOutput(true);  
	            conn.setConnectTimeout(10000);
	            OutputStream out = conn.getOutputStream(); 
	            out.write(data); 
	            if(conn.getResponseCode()==200){ 
	                Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_SHORT).show(); 
	            } 
	            else Toast.makeText(MainActivity.this, "提交失败", Toast.LENGTH_SHORT).show(); 
	        } 
		    catch(Exception ee){ 
		    	System.out.print("ee:"+ee.getMessage());    
		    }
		}
	}
	
	//试试手气
	private String getMsg(){
		String str="";
		try  
		{  //rnd=0.24180545751352472
			URL url = new URL(BASIC_URL+"/Rest/bs/buzz/demo/getJson.json?userName="+userNameEditText.getText().toString().trim());
//			URL url = new URL("http://www.baidu.com");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
			conn.setDoInput(true);  
			conn.setConnectTimeout(10000);  
			conn.setRequestMethod("GET");  
			conn.setRequestProperty("accept", "*/*");  
			String location = conn.getRequestProperty("location");  
			int resCode = conn.getResponseCode();  //200正常
			if (resCode==200){
				conn.connect();  
				InputStream stream = conn.getInputStream();  
				BufferedReader br = new BufferedReader(new InputStreamReader(stream,  
					     "gb2312"));  
			   StringBuffer sb = new StringBuffer();  
			   String data = "";  
			   while ((data = br.readLine()) != null) {     
			        sb.append(data+"\n");     
			   }     
			   	str = sb.toString();   
			   	
			   	JSONObject json = new JSONObject(str);
			   	str = "name:"+json.getString("name")+"   sexxxx:"+json.getString("sex");
				conn.disconnect();  
				stream.close();  
			}
			System.out.println(str);  
		}  
		catch(Exception ee)  
		{  
		  System.out.print("ee:"+ee.getMessage());   
		} 
		return str;
	}
}
