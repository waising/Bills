package com.bosssoft.bills.module.basic;

import com.bosssoft.bills.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/** 
 *
 * @ClassName   ������BaseActivity 
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
public class BaseActivity extends Activity {
	
	private static final String TAG = "BaseActivity";
	
	public final String BASIC_URL = "http://192.168.10.73:8086";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
