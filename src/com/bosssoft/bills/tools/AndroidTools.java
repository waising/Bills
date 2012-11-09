package com.bosssoft.bills.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/** 
 *
 * @ClassName   ������AndroidTools 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-11-8
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-11-8   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class AndroidTools {
    
	final static String TAG = "ANDROIDTOOLS";
	
	/**
	 * 
	 * �������ƣ�isConnect 
	 * ����˵�����Ƿ�����
	 * ����˵����
	 * @return
	 * @date   ����ʱ�䣺2012-11-8
	 * @author ���ߣ�wwx
	 */
	public static boolean isConnect(Activity mActivity){  
    	try{
    		ConnectivityManager cm = (ConnectivityManager)mActivity.getApplicationContext()
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}  
	
	/**
	 * 	
	 * �������ƣ�isWifi 
	 * ����˵�����Ƿ���wifi
	 * ����˵����
	 * @return
	 * @date   ����ʱ�䣺2012-11-8
	 * @author ���ߣ�wwx
	 */
	public static boolean isWifi(Activity mActivity){  
	    ConnectivityManager cm = (ConnectivityManager)  
	    		mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(cm!=null){  
	        NetworkInfo  ni = cm.getActiveNetworkInfo();  
	        if(!ni.getTypeName().equals("WIFI")){  
	            /* 
	             * ni.getTypeNmae()����ȡֵ���� 
	             * WIFI����ʾWIFI���� 
	             * MOBILE����ʾGPRS��EGPRS 
	             * 3G����û�в��Թ� 
	             * WIFI��(E)GPRS���ܹ��棬����������򿪣�ϵͳ��֧��WIFI 
	             */  
	            return true;  
	        }  
	    }  
	    return false;  
	}  
	
	/**
	 * 
	 * �������ƣ�hideInput 
	 * ����˵�������������
	 * ����˵����
	 * @param mActivity
	 * @date   ����ʱ�䣺2012-11-8
	 * @author ���ߣ�wwx
	 */
	public static void hideInput(Activity mActivity){
		InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getApplicationContext()
	            .getSystemService(Context.INPUT_METHOD_SERVICE);
	    mInputMethodManager.hideSoftInputFromWindow(new View(mActivity).getWindowToken(), 0); 
	}

	
    /**
     * 
     * �������ƣ�isSystemApplication 
     * ����˵�����жϳ����Ƿ�ΪϵͳӦ��
     * ����˵����
     * @param context
     * @param packageName
     * @return
     * @date   ����ʱ�䣺2012-11-8
     * @author ���ߣ�wwx
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        boolean isflag = false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo pInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if ((pInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                isflag = true;
            }
        } catch (Exception e) {
            Log.i(TAG,"��ȡϵͳ�Ƿ�ΪϵͳӦ���쳣!");
        }
        return isflag;
    }
   
    /**
     * 
     * �������ƣ�getVesionName 
     * ����˵������ȡϵͳ�汾
     * ����˵����
     * @param context
     * @return
     * @date   ����ʱ�䣺2012-11-9
     * @author ���ߣ�wwx
     */
    public String getVesionName(Context context) {
    	String versionName = "";
    	try {
    		versionName = context.getPackageManager().getPackageInfo("net.vpntunnel", 0).versionName;
    	} catch (NameNotFoundException e) {
    		Log.i(TAG, "getVesionName:"+e.getMessage());
    	}
     
    	return versionName;
    }
    
    /**
     * 
     * �������ƣ�getVersionCode 
     * ����˵����
     * ����˵����
     * @param context
     * @return
     * @date   ����ʱ�䣺2012-11-9
     * @author ���ߣ�wwx
     */
    public int getVersionCode(Context context) {
    	int versionCode = 0;
    	try {
    		versionCode = context.getPackageManager().getPackageInfo("net.vpntunnel", 0).versionCode;
    	} catch (NameNotFoundException e) {
    		Log.i(TAG, "getVersionCode"+e.getMessage());
    	}
		return versionCode;
    }
    
}
