package com.bosssoft.bills.module.basic;

import com.bosssoft.bills.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/** 
 *
 * @ClassName   类名：BaseActivity 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-10-26
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-10-26   wwx   创建该类功能。
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
