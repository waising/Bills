package com.bosssoft.bills.module.basic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/** 
 *
 * @ClassName   ������TextBroadcast 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-10-30
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-10-30   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class TestBroadcast extends BroadcastReceiver {

	public TestBroadcast(){
		Log.v("BROADCAST_TAG","����,��");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		Log.v("BROADCAST_TAG",intent.getStringExtra("xx"));
		
	}

}
