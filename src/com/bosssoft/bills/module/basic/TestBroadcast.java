package com.bosssoft.bills.module.basic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/** 
 *
 * @ClassName   类名：TextBroadcast 
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
public class TestBroadcast extends BroadcastReceiver {

	public TestBroadcast(){
		Log.v("BROADCAST_TAG","来了,亲");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.v("BROADCAST_TAG",intent.getStringExtra("xx"));
		
	}

}
