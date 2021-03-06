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
 * @ClassName   类名：AndroidTools 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-11-8
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-11-8   wwx   创建该类功能。
 *
 ************************************************************************
 *</p>
 */
public class AndroidTools {
    
	final static String TAG = "ANDROIDTOOLS";
	
	/**
	 * 
	 * 函数名称：isConnect 
	 * 功能说明：是否联网
	 * 参数说明：
	 * @return
	 * @date   创建时间：2012-11-8
	 * @author 作者：wwx
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
	 * 函数名称：isWifi 
	 * 功能说明：是否是wifi
	 * 参数说明：
	 * @return
	 * @date   创建时间：2012-11-8
	 * @author 作者：wwx
	 */
	public static boolean isWifi(Activity mActivity){  
	    ConnectivityManager cm = (ConnectivityManager)  
	    		mActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);  
	    if(cm!=null){  
	        NetworkInfo  ni = cm.getActiveNetworkInfo();  
	        if(!ni.getTypeName().equals("WIFI")){  
	            /* 
	             * ni.getTypeNmae()可能取值如下 
	             * WIFI，表示WIFI联网 
	             * MOBILE，表示GPRS、EGPRS 
	             * 3G网络没有测试过 
	             * WIFI和(E)GPRS不能共存，如果两个都打开，系统仅支持WIFI 
	             */  
	            return true;  
	        }  
	    }  
	    return false;  
	}  
	
	/**
	 * 
	 * 函数名称：hideInput 
	 * 功能说明：隐藏软键盘
	 * 参数说明：
	 * @param mActivity
	 * @date   创建时间：2012-11-8
	 * @author 作者：wwx
	 */
	public static void hideInput(Activity mActivity){
		InputMethodManager mInputMethodManager = (InputMethodManager) mActivity.getApplicationContext()
	            .getSystemService(Context.INPUT_METHOD_SERVICE);
	    mInputMethodManager.hideSoftInputFromWindow(new View(mActivity).getWindowToken(), 0); 
	}

	
    /**
     * 
     * 函数名称：isSystemApplication 
     * 功能说明：判断程序是否为系统应用
     * 参数说明：
     * @param context
     * @param packageName
     * @return
     * @date   创建时间：2012-11-8
     * @author 作者：wwx
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
            Log.i(TAG,"获取系统是否为系统应用异常!");
        }
        return isflag;
    }
   
    /**
     * 
     * 函数名称：getVesionName 
     * 功能说明：获取系统版本
     * 参数说明：
     * @param context
     * @return
     * @date   创建时间：2012-11-9
     * @author 作者：wwx
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
     * 函数名称：getVersionCode 
     * 功能说明：
     * 参数说明：
     * @param context
     * @return
     * @date   创建时间：2012-11-9
     * @author 作者：wwx
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
