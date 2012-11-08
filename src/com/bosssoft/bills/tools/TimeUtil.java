package com.bosssoft.bills.tools;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/** 
 *
 * @ClassName   ������TimeUtil 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-11-7
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-11-7   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */

/**������ʱ��
 * @author HQ
 *
 */
public class TimeUtil {
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private static final int TIME_LONG = 3000;// ���û����ʱ��
	private static TimeUtil mInstance = null;
	private Context mContext;

	public TimeUtil(Context ctx) {
		mContext = ctx;
		mInstance = this;
	}

	public static TimeUtil getInstance(Context ctx) {
		if (mInstance == null) {
			new TimeUtil(ctx);
		}
		return mInstance;
	}

	public void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new MyTimerTask();
		}
		
		if (mTimer != null && mTimerTask != null) {
			mTimer.schedule(mTimerTask, TIME_LONG);
		}
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Message message = handler.obtainMessage();
			handler.sendMessage(message);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Log.i("CATCH", "handleMessage");
			super.handleMessage(msg);
		}
	};

	public void stopTimer() {

		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
}
